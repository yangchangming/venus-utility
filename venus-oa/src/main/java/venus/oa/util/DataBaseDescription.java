/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.oa.util;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author changming.Y <changming.yang.ah@gmail.com>
 *
 */
public class DataBaseDescription {
    public static String getDatabaseProductName(Connection con){
        try {
            return con.getMetaData().getDatabaseProductName().toLowerCase();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
