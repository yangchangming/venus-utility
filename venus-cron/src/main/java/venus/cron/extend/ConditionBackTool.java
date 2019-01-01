/*
 * 创建日期 2007-4-11
 * CreateBy zhangbaoyu
 */
package venus.cron.extend;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Map;

/**
 * @author zhangbaoyu
 *
 */
public class ConditionBackTool {
    /**
     * 回写表单
     * 
     * @param request
     * @return
     */
    public static String writeBackMapToForm(Map mRequest) {
        StringBuffer rtValue = new StringBuffer();
        rtValue.append("  var mForm = new Object();\n");
        rtValue.append("  var indexArray = new Array();\n");
        rtValue.append("  function writeBackMapToForm() {\n");
        Iterator itMRequest = mRequest.keySet().iterator();
        while (itMRequest.hasNext()) {
            String tempKey = (String) itMRequest.next();
            Object tempValue = mRequest.get(tempKey);
            if (tempKey.startsWith("VENUS")) //回头要扩展
                continue;
            String tempValueNew = "";
            String[][] toBeDispose = new String[][] { { "'", "\\'" },
                    { "\"", "\\\"" }, { "\\", "\\\\" }, { "\t", "\\t" },
                    { "\f", "\\f" }, { "\b", "\\b" }, { "\r", "\\u000D" },
                    { "\n", "\\u000A" } }; //要转义的二位数组列表
            if (tempValue instanceof String) { //如果是单值，直接注入
                tempValueNew = replaceStringToHtml(
                        (String) tempValue, toBeDispose);
                rtValue.append("    indexArray[indexArray.length] = \""
                        + tempKey + "\";\n");
                rtValue.append("    mForm[\"" + tempKey + "\"] = \""
                        + tempValueNew + "\";\n");
            } else if (tempValue instanceof String[]) { //如果是多值，放入数组
                rtValue.append("    indexArray[indexArray.length] = \""
                        + tempKey + "\";\n");
                String[] myArray = (String[]) tempValue;
                rtValue.append("    mForm[\"" + tempKey + "\"] = [");
                for (int i = 0; i < myArray.length; i++) {
                    if (i > 0)
                        rtValue.append(",");
                    tempValueNew = replaceStringToHtml(
                            myArray[i], toBeDispose);
                    rtValue.append("\"" + tempValueNew + "\"");
                }
                rtValue.append("];\n");
            } else if (tempValue instanceof Timestamp) { //如果是时间戳，直接注入
                if (tempValue == null) {
                    continue;
                }
                tempValueNew = replaceStringToHtml(tempValue
                        .toString().substring(0, 19), toBeDispose);
                rtValue.append("    indexArray[indexArray.length] = \""
                        + tempKey + "\";\n");
                rtValue.append("    mForm[\"" + tempKey + "\"] = \""
                        + tempValueNew + "\";\n");
            } else {
                if (tempValue != null) {
                    log("在回写页面时，遇到了未知java类型：" + tempValue);
                }
                continue;
            }
        }
        rtValue.append("    for(var i=0; i<indexArray.length; i++) {\n");
        rtValue.append("	  writeBackValue(indexArray[i]);\n");
        rtValue.append("    }\n");
        rtValue.append("  }\n");
        return rtValue.toString();
    }
    
    /**
     * 过滤Html页面中的敏感字符，接受过滤的字符列表和转化后的值
     * 
     * @param value
     * @return
     */
    public static String replaceStringToHtml(String value, String[][] aString) {

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
    
    public static String log(String str) {
        System.out.println(new Timestamp(System.currentTimeMillis()).toString()
                + ":" + str);
        return new Timestamp(System.currentTimeMillis()).toString() + ":" + str;
    }
}
