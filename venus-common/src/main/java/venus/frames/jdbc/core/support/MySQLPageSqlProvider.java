/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.frames.jdbc.core.support;

import venus.frames.jdbc.core.IPageSqlProvider;

/**
 * MySQL分页优化。
 * TODO: 数据量非常大的时候limit m,n性能会出现问题，需要对主键名进行约定从而实施优化。
 * 优化参考语句：
 * SELECT * FROM TABLE_NAME WHERE ID >= (SELECT ID FROM TABLE_NAME ORDER BY ID limit firstResult,1) limit maxResult
 */
public class MySQLPageSqlProvider implements IPageSqlProvider {

    public String getSql(String oldSqlStr, int firstResult, int maxResult) {
        if(firstResult < 0) {
            firstResult = 0;
        }
        StringBuffer sb = new StringBuffer();
        sb.append(oldSqlStr);
        sb.append(" limit ");
        sb.append(firstResult);
        sb.append(", ");
        sb.append(maxResult);

        return sb.toString();
    }
}
