package venus.portal.searchengine.lucene.Operation;

import org.apache.lucene.queryParser.ParseException;
import venus.portal.searchengine.lucene.operationvo.SearchCondition;
import venus.portal.searchengine.lucene.operationvo.index.DocVo;

import java.io.IOException;
import java.util.Map;

/**
 * Created by qj on 13-12-20.
 */
public interface Operation {

    public void insertIndex(DocVo docVo) throws IOException;
    public void updateIndex(DocVo docVo) throws IOException;
    public void deleteIndex(DocVo docVo) throws IOException;
    public SearchCondition searchIndex(Map<String, String> searchCondition) throws ParseException, IOException;
}
