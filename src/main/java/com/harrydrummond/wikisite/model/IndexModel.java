package com.harrydrummond.wikisite.model;

import com.harrydrummond.wikisite.entity.KnowledgeBase;
import com.harrydrummond.wikisite.entity.KnowledgeBaseContent;
import com.harrydrummond.wikisite.repository.KnowledgeBaseRepository;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Date;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

@Transactional(readOnly = true)
public class IndexModel {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexModel.class);

    private static final String ID = "ID";
    private static final String CONTENT_TOKENIZED = "CONTENT_TOKENIZED";
    private static final String CONTENT_BODY = "CONTENT_BODY";
    private static final String TAG_LINE = "TAGLINE";
    private static final String TITLE = "TITLE";
    private static final String DATE = "DATE";

    // Eventually, it is expected we will move to a file based indexing system so we use the parent
    private final Directory directory;

    public IndexModel() {
        // Due to current database size, the overhead here is less than if we were to write and store to file.
        // Once site becomes large enough, we will move to a file based indexing system but as of now, the costs are not nominal.
        directory = new ByteBuffersDirectory();
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
        checkNotNull(knowledgeBaseList);

        try (IndexWriter directoryWriter = new IndexWriter(directory, new IndexWriterConfig(new EnglishAnalyzer()))){
            for (KnowledgeBase knowledgeBase : knowledgeBaseList) {
                if (knowledgeBase == null) continue;
                KnowledgeBaseContent latestContent = knowledgeBase.getLatestKnowledgeBaseContent();
                if (latestContent == null || latestContent.getContent() == null) {
                    LOGGER.warn(String.format("Tried to scan knowledgebase %s to index but it has no latest content!", knowledgeBase.getId()));
                    continue;
                }

                Document doc = new Document();
                doc.add(new StoredField(ID, knowledgeBase.getId()));
                doc.add(new TextField(CONTENT_TOKENIZED, latestContent.getContent(), Field.Store.NO));
                doc.add(new StoredField(TAG_LINE, knowledgeBase.getTagLine()));
                doc.add(new StoredField(TITLE, knowledgeBase.getTitle()));
                doc.add(new StoredField(DATE, knowledgeBase.getDateCreated().getTime()));

                directoryWriter.addDocument(doc);
            }
        } catch (IOException io) {
            LOGGER.trace("Failed to instantiate IndexWriter", io);
        }
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
            Query query = new QueryParser(CONTENT_TOKENIZED, new SimpleAnalyzer()).parse(queryString);

            IndexReader indexReader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(indexReader);
            TopDocs topDocs = searcher.search(query, 10);
            List<KnowledgeBase> knowledgeBaseList = new ArrayList<>();

            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                Document doc = searcher.doc(scoreDoc.doc);
                KnowledgeBase kb = new KnowledgeBase(
                        doc.getField(ID).numericValue().longValue(),
                        doc.get(TITLE));
                kb.setTagLine(doc.get(TAG_LINE));
                kb.setDateCreated(new Date(doc.getField(DATE).numericValue().longValue()));
                knowledgeBaseList.add(kb);
            }
            return knowledgeBaseList;
        } catch (IOException | ParseException ex) {
            LOGGER.trace("Critical Search Error!", ex);
        }
        return Collections.emptyList();
    }
}