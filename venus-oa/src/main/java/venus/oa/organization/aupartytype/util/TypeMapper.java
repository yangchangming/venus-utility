/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.oa.organization.aupartytype.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author changming.Y <changming.yang.ah@gmail.com>
 *
 */
public class TypeMapper {
    private static Map javaMap = new HashMap();
    private static Map oracleMap = new HashMap();
    //private static Map mysqlMap = new HashMap();
    //private static Map db2Map = new HashMap();
    //private static Map mssqlserverMap = new HashMap();
    static{
        javaMap.put("text", "java.lang.String");
        javaMap.put("id", "java.lang.String");
        javaMap.put("date", "java.sql.Timestamp");
        javaMap.put("number", "java.lang.String");//and so on...
        javaMap.put("boolean", "java.lang.Boolean");
    }
    static{
        oracleMap.put("text", "varchar2(300)");
        oracleMap.put("id", "char(19)");
        oracleMap.put("date", "timestamp");
        oracleMap.put("number", "int");//and so on...
        oracleMap.put("boolean", "char(1)");
    }
    public static String getJavaType(String key){
        return (String) javaMap.get(key);
    }
    public static String getDBType(String key){
        return (String) oracleMap.get(key);
    }
}
