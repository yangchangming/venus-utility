/**
 *@author zhangrenyang 
 *@date  2011-9-28
*/
package venus.portal.helper;

import java.sql.Timestamp;

/**
 *@author zhangrenyang 
 *@date  2011-9-28
 */
public class EwpStringHelper {
    public static String log(String str) {
        return log((Object)str);
    }

    public static String log(Object obj) {
        System.out.println("RANMIN  " + new Timestamp(System.currentTimeMillis()).toString()
                + ": " + obj);
        return "RANMIN  " + new Timestamp(System.currentTimeMillis()).toString() + ": "
                + obj;
    }
    
    /**
     * 判断一个数组是否包含一个字符串
     * @param arrayString
     * @param str
     * @return
     */
    public static boolean containStringInArray(String[] arrayString, String str) {
        if (arrayString == null || arrayString.length == 0) {
            return false;
        }
        for (int i = 0; i < arrayString.length; i++) {
            if (arrayString[i].equals(str))
                return true;
        }
        return false;
    }
    
    /**
     * 显示数据前过滤掉null
     * 
     * @param myString
     * @return
     */
    public static String prt(String myString) {
        if (myString != null && myString.length() != 0) {
            return myString;
        } else {
            return "";
        }
    }

    public static String prt(Object obj) {
        if (obj != null) {
            return prt(obj.toString());
        } else {
            return "";
        }
    }

    /**
     * 显示数据前过滤掉null，截取一定位数
     * 
     * @param myString
     * @param index
     *            最大显示的长度
     * @return
     */
    public static String prt(String myString, int index) {
        if (myString != null && myString.length() != 0) {
            if (myString.length() >= index) {
                return myString.substring(0, index);
            } else {
                return myString;
            }
        } else {
            return "";
        }
    }

    public static String prt(Object obj, int index) {
        if (obj != null) {
            return prt(obj.toString(), index);
        } else {
            return "";
        }
    }

    /**
     * 显示数据前过滤掉null，截取一定位数，并加上表示，如省略号
     * 
     * @param myString
     * @param index
     *            最大显示的长度
     * @return
     */
    public static String prt(String myString, int index, String accessional) {
        int accessionalLength = 0;
        if (index < 0) {
            return myString;
        }
        if (accessional == null || "".equals(accessional)) {
            accessional = "...";
        }
        accessionalLength = accessional.length();
        if (myString != null) {
            if (index <= accessionalLength) {
                return myString.substring(0, index);
            } else if (myString.length() >= index - accessionalLength) {
                return myString.substring(0, index - accessionalLength)
                        + accessional;
            } else {
                return myString;
            }
        } else {
            return "";
        }
    }

    public static String prt(Object obj, int index, String accessional) {
        if (obj != null)
            return prt(obj.toString(), index, accessional);
        else {
            return "";
        }
    }
    
    /**
     * 过滤Html页面中的敏感字符
     * 
     * @param value
     * @return
     */
    public static String replaceStringToHtml(Object obj) {
        return replaceStringToHtml(obj == null ? "" : obj.toString()); 
    }
    
    /**
     * 过滤Html页面中的敏感字符
     * 
     * @param value
     * @return
     */
    public static String replaceStringToHtml(String value) {
        return replaceStringByRule(value, new String[][] { { "<", "&lt;" },
                { ">", "&gt;" }, { "&", "&amp;" }, { "\"", "&quot;" },
                { "'", "&#39;" }, { "\n", "<BR/>" }, { "\r", "<BR/>" } });
    }
    

    /**
     * 过滤Html页面中的敏感字符，接受过滤的字符列表和转化后的值
     * 
     * @param value
     * @return
     */
    public static String replaceStringByRule(String value, String[][] aString) {
        if (value == null) {
            return ("");
        }
        char content[] = new char[value.length()];
        value.getChars(0, value.length(), content, 0);
        StringBuffer result = new StringBuffer(content.length + 50);

        for (int i = 0; i < content.length; i++) {
            boolean isTransct = false;
            for (int j = 0; j < aString.length; j++) {
                if (String.valueOf(content[i]).equals(aString[j][0])) {
                    result.append(aString[j][1]);
                    isTransct = true;
                    break;
                }
            }
            if (!isTransct) {
                result.append(content[i]);
            }
        }
        return result.toString();
    }
    
    /**
     * 功能: 过滤Html页面中的敏感字符,用于在script脚本中显示
     * 
     * @param value
     * @return
     */
    public static String replaceStringToScript(Object obj) {
        return replaceStringToScript(obj == null ? "" : obj.toString());
    }

    /**
     * 功能: 过滤Html页面中的敏感字符,用于在script脚本中显示
     * 
     * @param value
     * @return
     */
    public static String replaceStringToScript(String value) {
        return replaceStringByRule(value, new String[][] { { "'", "\\'" },
                { "\"", "\\\"" }, { "\\", "\\\\" }, { "\r", "\\r" },
                { "\n", "\\n" }, {"\t", "\\t"}, {"\f", "\\f"},{"\b", "\\b"} });  // {"\r", "\\u000D"}, {"\n", "\\u000A"}
    }

    /**
     * 处理字符串为一个非NULL值
     *@param val 初始值
     *@return　处理后的值
     *@exception
     */
    public static  String trimNullAsEmpty(String val){
        return val==null?"":val;
    }
    
    
    
}
