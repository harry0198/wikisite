package com.harrydrummond.wikisite.knowledgebase;

import com.harrydrummond.wikisite.knowledgebase.content.KnowledgeBaseContent;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.*;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class IndexModel {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexModel.class);

    private static final String ID = "ID";
    private static final String CONTENT_TOKENIZED = "CONTENT_TOKENIZED";
    private static final String TAG_LINE = "TAGLINE";
    private static final String TITLE = "TITLE";
    private static final String DATE = "DATE";
    private static final String RATING = "RATING";
    private static final String TAGS = "TAGS";

    // Eventually, it is expected we will move to a file based indexing system or others so we use the parent
    private Directory directory;

    /**
     * Default constructor, assigns directory field to which found in #getNewDirectory
     * Scans all knowledgebases to index from kbrepository and auto updates at set interval
     */
    @Autowired
    public IndexModel(KnowledgeBaseRepository kbRepository) {
        directory = getNewDirectory();
        scanKnowledgeBaseToIndex(kbRepository.findAll());

        final int PERIOD = (1000 * 60) * 30; // 30 mins
        new Timer().scheduleAtFixedRate(new IndexTask(this, kbRepository), PERIOD, PERIOD);
    }

    /**
     * Gets a new directory from lucene
     * @return New Directory Object
     */
    public static Directory getNewDirectory() {
        // Due to current database size, the overhead here is less than if we were to write and store to file.
        // Once site becomes large enough, we will move to a file based indexing system but as of now, the costs are not nominal.
        return new ByteBuffersDirectory();
    }

    /**
     * Simple setter, updates the directory
     * @param directory Directory to set
     */
    public void setDirectory(Directory directory) {
        assert directory != null;
        this.directory = directory;
    }

    /**
     * Scans KnowledgeBase and adds to index
     * @param knowledgeBase KnowledgeBase to add
     */
    public void scanKnowledgeBaseToIndex(final KnowledgeBase knowledgeBase) {
        scanKnowledgeBaseToIndex(List.of(knowledgeBase));
    }

    /**
     * Scans list of knowledgebases and adds to index. If knowledgebase and / or latest content does not exist,
     * it is skipped.
     * @param knowledgeBaseList List of KnowledgeBases to add
     */
    public void scanKnowledgeBaseToIndex(final List<KnowledgeBase> knowledgeBaseList) {
        assert knowledgeBaseList != null;

        scanAndWriteTo(directory, knowledgeBaseList);
    }

    /**
     * Searches index of knowledgebase's content for relevant results based on query string.
     * Uses Lucene's SimpleAnalyzer for lowercasing, removing stop words and generalising words.
     * @param queryString Query to search index for
     * @return List of top 10 (or less) knowledgebases in order of relevance. Indexed fields of
     * tagline, date created, id and title. No more.
     */
    public List<KnowledgeBase> searchByString(String queryString) {
        try {
//            Query query = new QueryParser(CONTENT_TOKENIZED, new SimpleAnalyzer()).parse(queryString);

            IndexReader indexReader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(indexReader);
//            TopDocs topDocs = searcher.search(query, 10);
            List<KnowledgeBase> knowledgeBaseList = new ArrayList<>();

            Analyzer analyzer = new StandardAnalyzer();
            MultiFieldQueryParser queryParser = new MultiFieldQueryParser(
                    new String[] {CONTENT_TOKENIZED, TAGS},
                    analyzer);

            TopDocs topDocs = searcher.search(queryParser.parse(queryString), 10);

            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                Document doc = searcher.doc(scoreDoc.doc);
                KnowledgeBase kb = generateKnowledgeBaseFromDocument(doc);
                knowledgeBaseList.add(kb);
            }
            return knowledgeBaseList;
        } catch (IOException | ParseException ex) {
            LOGGER.trace("Critical Search Error!", ex);
        }
        return Collections.emptyList();
    }

    /**
     * Gets all knowledgebases in the index
     * @return List of knowledgebases in index
     */
    public List<KnowledgeBase> getAllKnowledgeBases() {
        List<KnowledgeBase> knowledgeBaseList = new ArrayList<>();
        try {
            IndexReader indexReader = DirectoryReader.open(directory);
            for (int i = 0; i < indexReader.maxDoc(); i++) {
                knowledgeBaseList.add(generateKnowledgeBaseFromDocument(indexReader.document(i)));
            }
        } catch (IOException io) {
            LOGGER.trace("Critical Search * Error!", io);
        }

        return knowledgeBaseList;
    }

    /**
     * Scans the knowledgebase lists and writes in indexed values to the directory
     * @param directory Directory to write to
     * @param knowledgeBaseList List of knowledge bases to scan and index
     */
    public void scanAndWriteTo(Directory directory, List<KnowledgeBase> knowledgeBaseList) {
        try (IndexWriter directoryWriter = new IndexWriter(directory, new IndexWriterConfig(new EnglishAnalyzer()))){
            for (KnowledgeBase knowledgeBase : knowledgeBaseList) {
                if (knowledgeBase == null) continue;

                Document doc = generateDocument(knowledgeBase);
                if (doc == null) continue;

                directoryWriter.addDocument(doc);
            }
        } catch (IOException io) {
            LOGGER.trace("Failed to instantiate IndexWriter", io);
        }
    }

    /**
     * Generates a document to index based off knowledgebase. Only keeps
     * ID, TAG_LINE, TITLE and DATE
     * @param knowledgeBase Knowledgebase to generate doc for
     * @return Generated document or null if there is no content in the knowledgebase to index.
     */
    private Document generateDocument(KnowledgeBase knowledgeBase) {

        KnowledgeBaseContent latestContent = knowledgeBase.getLatestKnowledgeBaseContent();
        if (latestContent == null || latestContent.getContent() == null) {
            LOGGER.warn(String.format("Tried to scan knowledgebase %s to index but it has no latest content!", knowledgeBase.getId()));
            return null;
        }

        Document doc = new Document();
        doc.add(new StoredField(ID, knowledgeBase.getId()));
        doc.add(new TextField(CONTENT_TOKENIZED, latestContent.getContent(), Field.Store.NO));
        doc.add(new StoredField(TAG_LINE, knowledgeBase.getTagLine()));
        doc.add(new StoredField(TITLE, knowledgeBase.getTitle()));
        doc.add(new StoredField(DATE, knowledgeBase.getDateCreated().getTime()));
        doc.add(new StoredField(RATING, knowledgeBase.getRating()));
        String tagsList = "";
        if (knowledgeBase.getTags() != null) {
            tagsList = knowledgeBase.getTags().stream().map(Tag::getName).collect(Collectors.joining());
        }
        Field tagsField = new StoredField(TAGS, tagsList);
        doc.add(tagsField);
        return doc;
    }

    // Creates a KnowledgeBase object from the supplied Document using KnowledgeBaseBuilder
    private KnowledgeBase generateKnowledgeBaseFromDocument(Document doc) {
        return new KnowledgeBase.KnowledgeBaseBuilder()
                .setId(doc.getField(ID).numericValue().longValue())
                .setTitle(doc.get(TITLE))
                .setTagLine(doc.get(TAG_LINE))
                .setRating(doc.getField(RATING).numericValue().intValue())
                .setDateCreated(new Date(doc.getField(DATE).numericValue().longValue()))
                .build();
    }

}