package venus.portal.searchengine.lucene.Operation.impl;

import com.google.common.base.Strings;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.search.highlight.Scorer;
import org.apache.lucene.store.FSDirectory;
import venus.frames.i18n.util.LocaleHolder;
import venus.portal.searchengine.lucene.Operation.Operation;
import venus.portal.searchengine.lucene.operationvo.SearchCondition;
import venus.portal.searchengine.lucene.operationvo.index.DocVo;
import venus.portal.searchengine.lucene.operationvo.index.FieldVo;
import venus.portal.searchengine.lucene.operationvo.index.util.DocSearchCol;
import venus.portal.searchengine.util.LuceneUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by qj on 13-12-20.
 */
public class OperationImpl implements Operation {
    private static Log logger = LogFactory.getLog(OperationImpl.class);

    private String indexDir;
    private Analyzer analyzer;

    public void insertIndex(DocVo docVo) throws IOException {

        File indexDir = new File(this.indexDir);
        FSDirectory fsDir;
        IndexWriter writer;
        if (indexFileExists(indexDir)) {
            fsDir = FSDirectory.getDirectory(indexDir, false);
            IndexReader.unlock(fsDir);
            writer = new IndexWriter(indexDir, this.analyzer, false);
        } else {
            fsDir = FSDirectory.getDirectory(indexDir, true);
            writer = new IndexWriter(indexDir, this.analyzer, true);
        }

        Document doc = new Document();
        for (FieldVo fv : docVo.getFields().values()) {
            Field f = new Field(fv.getName(), fv.getValue(), fv.getStore(), fv.getIndex());
            f.setBoost(fv.getFieldBoost());
            doc.add(f);
        }
        writer.addDocument(doc);
        writer.optimize();
        writer.close();
        fsDir.close();

    }

    public void updateIndex(DocVo docVo) throws IOException {
        this.deleteIndex(docVo);
        this.insertIndex(docVo);

    }

    public void deleteIndex(String key) throws IOException {
        File indexDir = new File(this.indexDir);
        if (!indexFileExists(indexDir)) {
            logger.error(LocaleHolder.getMessage("udp.searchengine.no_index_file_please_initialize_the_index"));
//            throw new SearchEngineException(LocaleHolder.getMessage("udp.searchengine.no_index_file_please_initialize_the_index"));
            throw new RuntimeException(LocaleHolder.getMessage("udp.searchengine.no_index_file_please_initialize_the_index"));
        }
        FSDirectory fsDir = FSDirectory.getDirectory(indexDir, false);
        Term term = new Term(DocSearchCol.KEY.getColName(), key);
        IndexReader reader = IndexReader.open(fsDir);

        IndexReader.unlock(fsDir);
        reader.deleteDocuments(term);

        reader.close();
        fsDir.close();
    }

    public void deleteIndex(DocVo docVo) throws IOException {
        this.deleteIndex(docVo.getFields().get(DocSearchCol.KEY.getColName()).getValue());
    }

    public boolean isExistIndex(DocVo docVo) throws IOException {
        this.createIndexDirForNotExist();
        TermQuery query = new TermQuery(new Term(DocSearchCol.KEY.getColName(), docVo.getFields().get(DocSearchCol.KEY.getColName()).getValue()));
        if (!indexFileExists(new File(this.indexDir))) {
            logger.error(LocaleHolder.getMessage("udp.searchengine.no_index_file_please_initialize_the_index"));
//            throw new SearchEngineException(LocaleHolder.getMessage("udp.searchengine.no_index_file_please_initialize_the_index"));
            throw new RuntimeException(LocaleHolder.getMessage("udp.searchengine.no_index_file_please_initialize_the_index"));
        }

        FSDirectory fsDir = FSDirectory.getDirectory(indexDir, false);
        //解決LOCK的方法
        IndexReader.unlock(fsDir);
        IndexSearcher searcher = new IndexSearcher(fsDir);
        Hits hits = searcher.search(query);
        int count = hits.length();
        searcher.close();
        if (count > 0) {
            return true;
        }
        return false;
    }

    public SearchCondition searchIndex(Map<String, String> searchCondition) throws ParseException, IOException {

        int contentsLength = 200;

        String[] queries = {searchCondition.get(SearchCondition.CONDITION_KEY), searchCondition.get(SearchCondition.CONDITION_KEY), searchCondition.get(SearchCondition.CONDITION_WEBSITE)};
        String[] fields = {DocSearchCol.TITLE.getColName(), DocSearchCol.CONTENTS.getColName(), DocSearchCol.WEBSITE.getColName()};
        //指定查询字句之间的关系
        BooleanClause.Occur[] clauses = {BooleanClause.Occur.SHOULD, BooleanClause.Occur.MUST, BooleanClause.Occur.MUST};
        //转成多域查询MultiFieldQuery
        Query query = null;
        //query = MultiFieldQueryParser.parse(queries, fields, clauses, this.analyzer);
        org.apache.lucene.queryParser.QueryParser queryParser = new MultiFieldQueryParser(fields, this.analyzer);
        query = queryParser.parse("(" + DocSearchCol.TITLE.getColName() + ":" +
                LuceneUtil.replaceTransferString(searchCondition.get(SearchCondition.CONDITION_KEY)) +
                " || " + DocSearchCol.CONTENTS.getColName() + ":" +
                LuceneUtil.replaceTransferString(searchCondition.get(SearchCondition.CONDITION_KEY)) +
                ") && " + DocSearchCol.WEBSITE.getColName() + ":" +
                LuceneUtil.replaceTransferString(searchCondition.get(SearchCondition.CONDITION_WEBSITE)));

        if (!indexFileExists(new File(this.indexDir))) {
            logger.error(LocaleHolder.getMessage("udp.searchengine.no_index_file_please_initialize_the_index"));
//            throw new SearchEngineException(LocaleHolder.getMessage("udp.searchengine.no_index_file_please_initialize_the_index"));
            throw new RuntimeException(LocaleHolder.getMessage("udp.searchengine.no_index_file_please_initialize_the_index"));
        }

        FSDirectory fsDir = FSDirectory.getDirectory(indexDir, false);
        //解決LOCK的方法
        IndexReader.unlock(fsDir);
        IndexSearcher searcher = new IndexSearcher(fsDir);
        Hits hits = searcher.search(query);

        Scorer scorer = new QueryScorer(query);
        SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<font color='CC0033'>", "</font>");
        Highlighter highlighter = new Highlighter(formatter, scorer);
        Fragmenter fragmenter = new SimpleFragmenter(contentsLength);
        highlighter.setTextFragmenter(fragmenter);

        SearchCondition rsSearchVo = new SearchCondition("", "", 1, 0);

        List<DocVo> docs = new ArrayList<DocVo>();
        if (hits.length() > 0) {
            int currentPage = 1;
            if (searchCondition.get(SearchCondition.CURRENT_PAGE) == null || "".equals(searchCondition.get(SearchCondition.CURRENT_PAGE))) {
                currentPage = 1;
            } else {
                currentPage = Integer.valueOf(searchCondition.get(SearchCondition.CURRENT_PAGE));
            }
            int recordNum = hits.length();
            rsSearchVo = new SearchCondition(searchCondition.get(SearchCondition.CONDITION_KEY), searchCondition.get(SearchCondition.CONDITION_WEBSITE), currentPage, recordNum);
            for (int i = (rsSearchVo.getPageResults().getResultsFrom() - 1); i <= (rsSearchVo.getPageResults().getResultsEnd() - 1); i++) {
                if (i >= recordNum) {
                    break;
                }
                Document document;
                document = hits.doc(i);
                String title = highlighter.getBestFragment(this.analyzer, DocSearchCol.TITLE.getColName(), document.get(DocSearchCol.TITLE.getColName()));
                String contents = highlighter.getBestFragment(this.analyzer, DocSearchCol.CONTENTS.getColName(), document.get(DocSearchCol.CONTENTS.getColName()));

                //防止未搜索到的内容在前台显示为空
                if (Strings.isNullOrEmpty(title)) {
                    title = document.get(DocSearchCol.TITLE.getColName());
                }
                if (Strings.isNullOrEmpty(contents)) {
                    String docContent = document.get(DocSearchCol.CONTENTS.getColName());
                    contents = docContent.substring(docContent.length() > contentsLength ? contentsLength : docContent.length());
                }

                DocVo docVo = new DocVo(document.get(DocSearchCol.KEY.getColName()),
                        title,
                        contents,
                        document.get(DocSearchCol.WEBSITE.getColName()),
                        document.get(DocSearchCol.PATH.getColName()),
                        document.get(DocSearchCol.PUBLISHTIME.getColName()));

                docs.add(docVo);
            }
        }
        searcher.close();
        rsSearchVo.getPageResults().setResults(docs);
        return rsSearchVo;
    }

    private boolean indexFileExists(File file) {
        return IndexReader.indexExists(file);
    }

    private void createIndexDirForNotExist() throws IOException {
        File indexDir = new File(this.indexDir);
        FSDirectory fsDir;
        IndexWriter writer;
        if (!(indexFileExists(indexDir))) {
            fsDir = FSDirectory.getDirectory(indexDir, true);
            writer = new IndexWriter(indexDir, this.analyzer, true);
            writer.optimize();
            writer.close();
            fsDir.close();
        }
    }

    public String getIndexDir() {
        return indexDir;
    }

    public void setIndexDir(String indexDir) {
        this.indexDir = indexDir;
    }

    public Analyzer getAnalyzer() {
        return analyzer;
    }

    public void setAnalyzer(Analyzer analyzer) {
        this.analyzer = analyzer;
    }
}
