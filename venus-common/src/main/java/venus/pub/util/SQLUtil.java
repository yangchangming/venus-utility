package venus.pub.util;

import venus.VenusHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author lixizhi
 * 
 * SQL工具类，用于动态生成SQL
 * 
 */
public class SQLUtil {

    public static final String PARAMVALUES = "paramValues";
    public static final String PARAMNAMES = "paramNames";

    public static final String SQL = "sql";

    /**
     * 获取insert语句
     * 
     * @param table
     * @param bean
     * @return sql语句信息 类型为Map，包含三个元素：<"sql",sql语句>，<paramValues,类型为List的参数值>，<paramNames,类型为List的参数名称>   
     * 
     */
    public static Map getInsertSQL(String table, Object bean) {
        if (bean.getClass().getName().equals("java.util.HashMap"))
            return getInsertSQL(table, (Map) bean);
        String sql = "insert into " + table + " (";
        String values = "";
        // paramValues是插入的参数值，用于PreparedStatement.set...
        ArrayList paramValues = new ArrayList();
        // paramNames是参数值对应的字段名
        ArrayList paramNames = new ArrayList();
        Map map = new HashMap();

        // 取出bean里的所有字段
        Class beanClass = bean.getClass();
        Field[] fields = beanClass.getDeclaredFields();

        // 将map里的值赋给bean
        try {
            for (int i = 0; i < fields.length; i++) {
                String fieldname = fields[i].getName();
                if (i == 0)
                    sql += fieldname;
                else
                    sql += "," + fieldname;

                // 调用get方法获取变量值
                String methodname = getMethodName(fieldname, "get");
                Method method = beanClass.getMethod(methodname, new Class[]{null});
                Object fieldvalue = method.invoke(bean, new Object[]{null});
                if (i == 0)
                    values += "?";
                else
                    values += ", ?";
                paramValues.add(fieldvalue);
                paramNames.add(fieldname);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        sql += ") values(" + values + ")";

        map.put(SQL, sql);
        map.put(PARAMVALUES, paramValues);
        map.put(PARAMNAMES, paramNames);
        return map;

    }

    /**
     * 获取insert语句
     * 
     * @param table
     * @param data
     *            类型为map<key,value>，key为字段名，value为具体类型的字段值
     * @return sql语句信息 类型为Map，包含三个元素：<"sql",sql语句>，<paramValues,类型为List的参数值>，<paramNames,类型为List的参数名称>
     */
    public static Map getInsertSQL(String table, Map data) {
        String sql = "insert into " + table + " (";
        String values = "";
        // paramValues是插入的参数值，用于PreparedStatement.set...
        ArrayList paramValues = new ArrayList();
        // paramNames是参数值对应的字段名
        ArrayList paramNames = new ArrayList();
        Map map = new HashMap();

        Iterator iterator = data.keySet().iterator();
        // 将map里的值赋给bean
        try {
            int i = 0;
            while (iterator.hasNext()) {
                String fieldname = iterator.next().toString();
                if (i == 0)
                    sql += fieldname;
                else
                    sql += "," + fieldname;
                Object fieldvalue = VenusHelper.getValueFromMapByKey(data, fieldname);
                if (i == 0)
                    values += "?";
                else
                    values += ", ?";
                i++;
                paramValues.add(fieldvalue);
                paramNames.add(fieldname);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        sql += ") values(" + values + ")";

        map.put(SQL, sql);
        map.put(PARAMVALUES, paramValues);
        map.put(PARAMNAMES, paramNames);
        return map;

    }

    /**
     * 获取update语句
     * 
     * @param table
     * @param condition
     *            格式为"and 条件"
     * @param bean
     * @return sql语句信息 类型为Map，包含三个元素：<"sql",sql语句>，<paramValues,类型为List的参数值>，<paramNames,类型为List的参数名称>
     */
    public static Map getUpdateSQL(String table, String condition, Object bean) {
        if (bean.getClass().getName().equals("java.util.HashMap"))
            return getUpdateSQL(table, condition, (Map) bean);
        String sql = "update " + table + " set ";
        // paramValues是插入的参数值，用于PreparedStatement.set...
        ArrayList paramValues = new ArrayList();
        // paramNames是参数值对应的字段名
        ArrayList paramNames = new ArrayList();
        Map map = new HashMap();

        // 取出bean里的所有字段
        Class beanClass = bean.getClass();
        Field[] fields = beanClass.getDeclaredFields();

        // 将map里的值赋给bean
        try {
            for (int i = 0; i < fields.length; i++) {
                String fieldname = fields[i].getName();
                // 调用get方法获取变量值
                String methodname = getMethodName(fieldname, "get");
                Method method = beanClass.getMethod(methodname);
                Object fieldvalue = method.invoke(bean);
                sql += fieldname + "=?,";
                paramNames.add(fieldname);
                paramValues.add(fieldvalue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        sql = sql.substring(0, sql.length() - 1);
        sql += " where 1=1 " + condition;
        map.put("sql", sql);
        map.put("paramValues", paramValues);
        map.put("paramNames", paramNames);
        return map;
    }

    /**
     * 获取update语句
     * 
     * @param table
     * @param key
     * @param bean
     * @return sql语句信息 类型为Map，包含三个元素：<"sql",sql语句>，<paramValues,类型为List的参数值>，<paramNames,类型为List的参数名称>
     */
    public static Map getUpdateSQL(String table, Object key, Object bean) {
        return getUpdateSQL(table, new String[] { key.toString() }, bean);
    }

    /**
     * 获取update语句
     * 
     * @param table
     * @param key
     * @param bean
     * @return sql语句信息 类型为Map，包含三个元素：<"sql",sql语句>，<paramValues,类型为List的参数值>，<paramNames,类型为List的参数名称>
     */
    public static Map getUpdateSQL(String table, String[] key, Object bean) {
        if (bean.getClass().getName().equals("java.util.HashMap"))
            return getUpdateSQL(table, key, (Map) bean);
        List keyList = Arrays.asList(key);
        String sql = "update " + table + " set ";
        String keyStr = "";
        Map keyValue = new HashMap();
        // paramValues是插入的参数值，用于PreparedStatement.set...
        ArrayList paramValues = new ArrayList();
        // paramNames是参数值对应的字段名
        ArrayList paramNames = new ArrayList();
        Map map = new HashMap();

        // 取出bean里的所有字段
        Class beanClass = bean.getClass();
        Field[] fields = beanClass.getDeclaredFields();

        // 将map里的值赋给bean
        try {
            for (int i = 0; i < fields.length; i++) {
                String fieldname = fields[i].getName();
                // 调用get方法获取变量值
                String methodname = getMethodName(fieldname, "get");
                Method method = beanClass.getMethod(methodname);
                Object fieldvalue = method.invoke(bean);
                if (keyList.indexOf(fieldname) > 0) {
                    keyValue.put(fieldname, fieldvalue);
                } else {
                    sql += fieldname + "=?,";
                    paramValues.add(fieldvalue);
                    paramNames.add(fieldname);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        sql = sql.substring(0, sql.length() - 1);
        for (int i = 0; i < keyList.size(); i++) {
            if (i == keyList.size() - 1)
                keyStr = keyStr + keyList.get(i) + "=?";
            else
                keyStr = keyStr + keyList.get(i) + "=? and ";
            paramValues.add(keyValue.get(keyList.get(i)));
            paramNames.add(keyList.get(i));
        }
        if (!keyStr.equals(""))
            sql += " where " + keyStr;
        map.put("sql", sql);
        map.put("paramValues", paramValues);
        map.put("paramNames", paramNames);
        return map;
    }

    /**
     * 获取update语句
     * 
     * @param table
     * @param condition
     *            格式为"and 条件"
     * @param data
     * @return sql语句信息 类型为Map，包含三个元素：<"sql",sql语句>，<paramValues,类型为List的参数值>，<paramNames,类型为List的参数名称>
     */
    public static Map getUpdateSQL(String table, String condition, Map data) {
        String sql = "update " + table + " set ";
        // paramValues是插入的参数值，用于PreparedStatement.set...
        ArrayList paramValues = new ArrayList();
        // paramNames是参数值对应的字段名
        ArrayList paramNames = new ArrayList();
        Map map = new HashMap();
        // 取出bean里的所有字段
        Iterator iterator = data.keySet().iterator();

        // 将map里的值赋给bean
        try {
            while (iterator.hasNext()) {
                String fieldname = iterator.next().toString();
                Object fieldvalue = VenusHelper.getValueFromMapByKey(data,
                        fieldname);
                sql += fieldname + "=?,";
                paramValues.add(fieldvalue);
                paramNames.add(fieldname);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        sql = sql.substring(0, sql.length() - 1);
        sql += " where 1=1 " + condition;
        map.put("sql", sql);
        map.put("paramValues", paramValues);
        map.put("paramNames", paramNames);
        return map;
    }

    /**
     * 获取update语句
     * 
     * @param table
     * @param key
     * @param data
     *            类型为map<key,value>，key为主键名称，value为主键值列表
     * @return sql语句信息 类型为Map，包含三个元素：<"sql",sql语句>，<paramValues,类型为List的参数值>，<paramNames,类型为List的参数名称>
     */
    public static Map getUpdateSQL(String table, Object key, Map data) {
        return getUpdateSQL(table, new String[] { key.toString() }, data);
    }

    /**
     * 获取update语句
     * 
     * @param table
     * @param key
     * @param data
     *            类型为map<key,value>，key为主键名称，value为主键值列表
     * @return sql语句信息 类型为Map，包含三个元素：<"sql",sql语句>，<paramValues,类型为List的参数值>，<paramNames,类型为List的参数名称>
     */
    public static Map getUpdateSQL(String table, String[] key, Map data) {
        List keyList = Arrays.asList(key);
        String sql = "update " + table + " set ";
        // paramValues是插入的参数值，用于PreparedStatement.set...
        ArrayList paramValues = new ArrayList();
        // paramNames是参数值对应的字段名
        ArrayList paramNames = new ArrayList();
        Map map = new HashMap();
        String keyStr = "";
        // 取出bean里的所有字段
        Iterator iterator = data.keySet().iterator();

        // 将map里的值赋给bean
        try {
            while (iterator.hasNext()) {
                String fieldname = iterator.next().toString();
                Object fieldvalue = VenusHelper.getValueFromMapByKey(data,
                        fieldname);
                if (keyList.indexOf(fieldname) > 0) {
                    continue;
                } else {
                    sql += fieldname + "=?,";
                    paramValues.add(fieldvalue);
                    paramNames.add(fieldname);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        sql = sql.substring(0, sql.length() - 1);
        for (int i = 0; i < keyList.size(); i++) {
            if (i == keyList.size() - 1)
                keyStr = keyStr + keyList.get(i) + "=?";
            else
                keyStr = keyStr + keyList.get(i) + "=? and ";
            paramValues.add(VenusHelper.getValueFromMapByKey(data, keyList.get(i)
                    .toString()));
            paramNames.add(keyList.get(i));
        }
        if (!keyStr.equals(""))
            sql += " where " + keyStr;
        map.put("sql", sql);
        map.put("paramValues", paramValues);
        map.put("paramNames", paramNames);
        return map;
    }

    /**
     * 获取delete语句
     * 
     * @param table
     * @param key
     *            主键数组
     * @param data
     *            类型为map<key,value>，key为主键名称，value为主键值列表
     * @param flag
     * @return sql语句信息 类型为Map，包含三个元素：<"sql",sql语句>，<paramValues,类型为List的参数值>，<paramNames,类型为List的参数名称>
     */
    public static Map getDeleteSQL(String table, String[] key, Map data,
            boolean flag) {
        String sql = "delete from " + table;
        List keyList = Arrays.asList(key);
        // paramValues是插入的参数值，用于PreparedStatement.set...
        ArrayList paramValues = new ArrayList();
        // paramNames是参数值对应的字段名
        ArrayList paramNames = new ArrayList();
        Map map = new HashMap();
        String keyStr = "";
        String tmpStr = "";
        if (!flag)
            tmpStr = " not ";
        for (int i = 0; i < keyList.size(); i++) {
            String values = "";
            List list = (List) data.get(keyList.get(i));
            for (int j = 0; j < list.size(); j++) {
                values = values + "'" + list.get(j) + "',";
            }
            values = values.substring(0, values.length() - 1);

            if (i == keyList.size() - 1)
                keyStr = keyStr + keyList.get(i) + tmpStr + " in (" + values
                        + ")";
            else
                keyStr = keyStr + keyList.get(i) + tmpStr + " in (" + values
                        + ")  and ";
        }
        if (!keyStr.equals(""))
            sql += " where " + keyStr;
        map.put("sql", sql);
        map.put("paramValues", paramValues);
        map.put("paramNames", paramNames);
        return map;
    }

    /**
     * 获取delete语句
     * 
     * @param table
     * @param condition
     *            格式为"and 条件"
     * @return sql语句信息 类型为Map，包含三个元素：<"sql",sql语句>，<paramValues,类型为List的参数值>，<paramNames,类型为List的参数名称>
     */
    public static Map getDeleteSQL(String table, String condition) {
        String sql = "delete from " + table;
        // paramValues是插入的参数值，用于PreparedStatement.set...
        ArrayList paramValues = new ArrayList();
        // paramNames是参数值对应的字段名
        ArrayList paramNames = new ArrayList();
        Map map = new HashMap();
        sql += " where 1=1 " + condition;
        map.put("sql", sql);
        map.put("paramValues", paramValues);
        map.put("paramNames", paramNames);
        return map;
    }

    /**
     * 获取delete语句
     * 
     * @param table
     * @param key
     *            条件关键字对象，具体类型为String或String[]，类型为String时，data类型为List；类型为String[]时，data类型为Map<key,List>
     *            条件关键字用于拼过滤条件，与data连用，拼出来的条件类似：key [not] in (value1,value2...)
     * @param data
     *            与关键字对应的值，类型为List<key对应的值>或Map<key,List类型的值列表>，其中key为参数key
     * @param flag
     * @return sql语句信息 类型为Map，包含三个元素：<"sql",sql语句>，<paramValues,类型为List的参数值>，<paramNames,类型为List的参数名称>
     */
    public static Map getDeleteSQL(String table, Object key, Object data,
            boolean flag) {
        if (key.getClass().getName().equals("[Ljava.lang.String;"))
            return getDeleteSQL(table, (String[]) key, (Map) data, flag);
        else {
            Map dataMap = new HashMap();
            dataMap.put(key.toString(), data);
            return getDeleteSQL(table, new String[] { key.toString() }, dataMap);
        }
    }

    /**
     * 获取delete语句
     * 
     * @param table
     * @param key
     *            条件关键字数组，用于拼过滤条件，与data连用，拼出来的条件类似：key [not] in
     *            (value1,value2...)
     * @param data
     *            与关键字对应的值，类型为Map<key,List类型的值列表>，其中key为参数key
     * @return sql语句信息 类型为Map，包含三个元素：<"sql",sql语句>，<paramValues,类型为List的参数值>，<paramNames,类型为List的参数名称>
     */
    public static Map getDeleteSQL(String table, String[] key, Map data) {
        return getDeleteSQL(table, key, data, true);
    }

    /**
     * 获取批量处理的delete语句
     * @param table 
     * @param key 条件关键字数组，用于拼过滤条件
     * @param data 与关键字对应的值
     * @return sql 语句信息 类型为Map，包含三个元素：<"sql",sql语句>，<paramValues,类型为List的参数值>，<paramNames,类型为List的参数名称>
     */
   public static Map getBatchDeleteSQL(String table, String[] key, Map data){
        String sql = "delete from "+table + " where 1=1 ";
        // paramValues是插入的参数值，用于PreparedStatement.set...
        ArrayList paramValues = new ArrayList();
        // paramNames是参数值对应的字段名
        ArrayList paramNames = new ArrayList();
        Map map = new HashMap();
        
        for(int i=0; i<key.length; i++){
            sql = sql + " and " + key[i] + "=?";
            paramNames.add(key[i]);
            paramValues.add(data.get(key[i]));
            
        }
        map.put("sql", sql);
        map.put("paramValues", paramValues);
        map.put("paramNames", paramNames);
        return map;
    }
    /**
     * 获取select语句
     * 
     * @param table
     * @param selectkey
     *            选择项，例如：field1,field2
     * @param key
     *            条件关键字，用于拼过滤条件，与data连用，拼出来的条件类似：key [not] in (value1,value2...)
     * @param condition
     *            格式为"and 条件"
     * @param data
     *            与关键字对应的值，类型为List<key对应的值>，其中key为参数key。
     * @param flag
     *            使用key和data参数拼写过滤条件时，指明in前面是否有not
     * @return select语句
     */
    public static String getSelectSQL(String table, String selectkey,
            String key, String condition, List data, boolean flag) {
        if (data == null)
            return getSelectSQL(table, selectkey, condition);
        String sql = "select " + selectkey + " from " + table;
        String values = "";
        String tmpStr = "";
        if (!flag)
            tmpStr = " not ";
        for (int i = 0; i < data.size(); i++) {
            values = values + "'" + data.get(i) + "',";
        }
        values = values.substring(0, values.length() - 1);
        sql += " where " + key + tmpStr + " in (" + values + ") ";
        if (condition != null)
            sql += condition;
        return sql;

    }

    /**
     * 获取select语句
     * 
     * @param table
     * @param selectkey
     *            选择项，例如：field1,field2
     * @param condition
     *            格式为"and 条件"
     * @return select语句
     */
    public static String getSelectSQL(String table, String selectkey,
            String condition) {
        String sql = "select " + selectkey + " from " + table;
        if (condition != null)
            sql += " where 1=1 " + condition;
        return sql;

    }

    /**
     * 获取字段的get/set方法名
     * 
     * @param fieldname
     * @param type
     *            get/set
     * 
     * @return 方法名
     */
    private static String getMethodName(String fieldname, String type) {
        char upper = Character.toUpperCase(fieldname.charAt(0));
        return type + upper + fieldname.substring(1);
    }
}
