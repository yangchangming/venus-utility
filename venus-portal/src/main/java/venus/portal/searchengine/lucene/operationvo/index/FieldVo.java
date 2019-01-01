package venus.portal.searchengine.lucene.operationvo.index;

import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;

/**
 * Created by qj on 13-12-25.
 */
public class FieldVo {
    private String name;
    private String value;
    private Store store;
    private Index index;
    private float fieldBoost;

    public FieldVo(String name, String value, Store store, Index index, float fieldBoost) {
        this.name = name;
        this.setValue(value);
        this.store = store;
        this.index = index;
        this.fieldBoost = fieldBoost;
    }

    public FieldVo(String name, String value) {
        this.name = name;
        this.setValue(value);
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public Store getStore() {
        return store;
    }

    public Index getIndex() {
        return index;
    }

    public float getFieldBoost() {
        return fieldBoost;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
