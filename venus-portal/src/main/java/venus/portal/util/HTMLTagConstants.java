package venus.portal.util;

import org.htmlparser.Parser;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.TextExtractingVisitor;

/**
 *@author zhangrenyang 
 *@date  2011-11-11
 */
public class HTMLTagConstants {

    /**站点ID*/
    public final static String PLAIN＿CONTENTTYPE="text/plain; charset=UTF-8";

    public static String getText(String html){
        String result="";
        try {
            Parser parser = new Parser();
            parser.setInputHTML(html);
            TextExtractingVisitor visitor = new TextExtractingVisitor();
            parser.visitAllNodesWith(visitor);
            result = visitor.getExtractedText();
        } catch (ParserException e) {

        }
        return  result;
    }

    
}
