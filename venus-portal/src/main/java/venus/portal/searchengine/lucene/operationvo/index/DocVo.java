package venus.portal.searchengine.lucene.operationvo.index;

import org.apache.lucene.document.Field;
import venus.portal.searchengine.lucene.operationvo.index.util.DocSearchCol;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by qj on 13-12-25.
 */
public class DocVo {
    private Map<String,FieldVo> fields;

    public DocVo(Map<String,FieldVo> fields) {
        this.setFields(fields);
}

    public DocVo(String key,String title,String contents,String website,String path,String publishtime) {
        this.setFields(new HashMap<String, FieldVo>());
        FieldVo fv_key = new FieldVo(DocSearchCol.KEY.getColName(), key, Field.Store.YES, Field.Index.UN_TOKENIZED,1.0f);
        FieldVo fv_title = new FieldVo(DocSearchCol.TITLE.getColName(), title, Field.Store.YES, Field.Index.TOKENIZED,1.3f);
        FieldVo fv_contents = new FieldVo(DocSearchCol.CONTENTS.getColName(), contents, Field.Store.YES, Field.Index.TOKENIZED,1.0f);
        FieldVo fv_website = new FieldVo(DocSearchCol.WEBSITE.getColName(), website, Field.Store.YES, Field.Index.UN_TOKENIZED,1.0f);
        FieldVo fv_path = new FieldVo(DocSearchCol.PATH.getColName(), path, Field.Store.YES, Field.Index.UN_TOKENIZED,1.0f);
        FieldVo fv_publicshitime = new FieldVo(DocSearchCol.PUBLISHTIME.getColName(), publishtime, Field.Store.YES, Field.Index.UN_TOKENIZED,1.0f);
        this.getFields().put(fv_key.getName(), fv_key);
        this.getFields().put(fv_title.getName(), fv_title);
        this.getFields().put(fv_contents.getName(), fv_contents);
        this.getFields().put(fv_website.getName(), fv_website);
        this.getFields().put(fv_path.getName(), fv_path);
        this.getFields().put(fv_publicshitime.getName(), fv_publicshitime);
    }

    public Map<String, FieldVo> getFields() {
        return fields;
    }

    public void setFields(Map<String, FieldVo> fields) {
        this.fields = fields;
    }

}
