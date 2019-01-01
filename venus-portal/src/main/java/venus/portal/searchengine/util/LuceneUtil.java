package venus.portal.searchengine.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by qj on 14-5-5.
 */
public class LuceneUtil {
    //正则表达式转义字符 * . ? + $ ^ [ ] ( ) { } | \ /
    //lucene转义字符   + - && || ! ( ) { } [ ] ^ " ~ * ? : \
    static String[] transferStrings = new String[]{"\\+", "-", "&", "\\|", "!", "\\(", "\\)", "\\{", "\\}", "\\[", "\\]", "\\^", "\\\"", "~", "\\*", "\\?", ":", "\\\\"};
    static Pattern p = Pattern.compile("((" + StringUtils.join(transferStrings, ")|(") + ")){1}");


    public static String replaceTransferString(String src) {
        Matcher m = p.matcher(src);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, Matcher.quoteReplacement("\\" + m.group()));
        }
        m.appendTail(sb);
        return sb.toString();
    }
}
