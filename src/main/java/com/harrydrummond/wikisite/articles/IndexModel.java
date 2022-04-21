package com.harrydrummond.wikisite.articles;

import com.harrydrummond.wikisite.articles.content.ArticleContent;
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
     * Scans all articles to index from kbrepository and auto updates at set interval
     */
    @Autowired
    public IndexModel(ArticleRepository kbRepository) {
        directory = getNewDirectory();
        scanArticleToIndex(kbRepository.findAll());

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
     * Scans article and adds to index
     * @param article article to add
     */
    public void scanArticleToIndex(final Article article) {
        scanArticleToIndex(List.of(article));
    }

    /**
     * Scans list of articles and adds to index. If article and / or latest content does not exist,
     * it is skipped.
     * @param articleList List of articles to add
     */
    public void scanArticleToIndex(final List<Article> articleList) {
        assert articleList != null;

        scanAndWriteTo(directory, articleList);
    }

    /**
     * Searches index of article's content for relevant results based on query string.
     * Uses Lucene's SimpleAnalyzer for lowercasing, removing stop words and generalising words.
     * @param queryString Query to search index for
     * @return List of top 10 (or less) articles in order of relevance. Indexed fields of
     * tagline, date created, id and title. No more.
     */
    public List<Article> searchByString(String queryString) {
        try {
//            Query query = new QueryParser(CONTENT_TOKENIZED, new SimpleAnalyzer()).parse(queryString);

            IndexReader indexReader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(indexReader);
//            TopDocs topDocs = searcher.search(query, 10);
            List<Article> articleList = new ArrayList<>();

            Analyzer analyzer = new StandardAnalyzer();
            MultiFieldQueryParser queryParser = new MultiFieldQueryParser(
                    new String[] {CONTENT_TOKENIZED, TAGS},
                    analyzer);

            TopDocs topDocs = searcher.search(queryParser.parse(queryString), 10);

            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                Document doc = searcher.doc(scoreDoc.doc);
                Article kb = generateArticleFromDocument(doc);
                articleList.add(kb);
            }
            return articleList;
        } catch (IOException | ParseException ex) {
            LOGGER.trace("Critical Search Error!", ex);
        }
        return Collections.emptyList();
    }

    /**
     * Gets all articles in the index
     * @return List of articles in index
     */
    public List<Article> getAllArticles() {
        List<Article> articleList = new ArrayList<>();
        try {
            IndexReader indexReader = DirectoryReader.open(directory);
            for (int i = 0; i < indexReader.maxDoc(); i++) {
                articleList.add(generateArticleFromDocument(indexReader.document(i)));
            }
        } catch (IOException io) {
            LOGGER.trace("Critical Search * Error!", io);
        }

        return articleList;
    }

    /**
     * Scans the article lists and writes in indexed values to the directory
     * @param directory Directory to write to
     * @param articleList List of knowledge bases to scan and index
     */
    public void scanAndWriteTo(Directory directory, List<Article> articleList) {
        try (IndexWriter directoryWriter = new IndexWriter(directory, new IndexWriterConfig(new EnglishAnalyzer()))){
            for (Article article : articleList) {
                if (article == null) continue;

                Document doc = generateDocument(article);
                if (doc == null) continue;

                directoryWriter.addDocument(doc);
            }
        } catch (IOException io) {
            LOGGER.trace("Failed to instantiate IndexWriter", io);
        }
    }

    /**
     * Generates a document to index based off article. Only keeps
     * ID, TAG_LINE, TITLE and DATE
     * @param article article to generate doc for
     * @return Generated document or null if there is no content in the article to index.
     */
    private Document generateDocument(Article article) {

        ArticleContent latestContent = article.getLatestArticleContent();
        if (latestContent == null || latestContent.getContent() == null) {
            LOGGER.warn(String.format("Tried to scan article %s to index but it has no latest content!", article.getId()));
            return null;
        }

        Document doc = new Document();
        doc.add(new StoredField(ID, article.getId()));
        doc.add(new TextField(CONTENT_TOKENIZED, latestContent.getContent(), Field.Store.NO));
        doc.add(new StoredField(TAG_LINE, article.getTagLine()));
        doc.add(new StoredField(TITLE, article.getTitle()));
        doc.add(new StoredField(DATE, article.getDateCreated().getTime()));
        doc.add(new StoredField(RATING, article.getRating()));
        String tagsList = "";
        if (article.getTags() != null) {
            tagsList = article.getTags().stream().map(Tag::getName).collect(Collectors.joining());
        }
        Field tagsField = new StoredField(TAGS, tagsList);
        doc.add(tagsField);
        return doc;
    }

    // Creates a article object from the supplied Document using articleBuilder
    private Article generateArticleFromDocument(Document doc) {
        return new Article.ArticleBuilder()
                .id(doc.getField(ID).numericValue().longValue())
                .title(doc.get(TITLE))
                .tagLine(doc.get(TAG_LINE))
                .rating(doc.getField(RATING).numericValue().intValue())
                .dateCreated(new Date(doc.getField(DATE).numericValue().longValue()))
                .build();
    }

}