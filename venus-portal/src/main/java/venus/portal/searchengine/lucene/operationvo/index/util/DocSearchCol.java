package venus.portal.searchengine.lucene.operationvo.index.util;

/**
 * Created by qj on 13-12-25.
 */
public enum DocSearchCol {
    KEY("key"),
    TITLE("title"),
    CONTENTS("contents"),
    WEBSITE("website"),
    PATH("path"),
    PUBLISHTIME("publishtime");

    private String colName;
    DocSearchCol(String colName){
        this.colName=colName;
    }

    public String getColName() {
        return colName;
    }
}
