/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.portal.templatetype.bs.impl;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * @author zhanrenyang
 *
 */
public class HashMapMapper implements RowMapper {
    public Object mapRow(ResultSet rs, int i) throws SQLException {
        HashMap row = new HashMap();
        int num = rs.getMetaData().getColumnCount();
        for (int j = 0; j < num; j++) {
            String colName = rs.getMetaData().getColumnLabel(j + 1);
            if (colName != null) {
                row.put(colName, rs.getObject(colName));
            }
        }
        return row;
    }
}
