//Source file: D:\\venus_view\\Tech_Department\\Platform\\Venus\\4项目开发\\1工作区\\4实现\\venus\\pub\\util\\StringUtil.java

package venus.pub.util;

import java.awt.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * String操作相关工具。
 * 本类方法基本为 public static final修饰
 * 注意效率问题，可将String转换成byte[]运算
 */
public class StringUtil {


	/**
	 * 将空格或空字符串转换为null；
	 * @param str
	 * @return String
	 * @roseuid 3F6FFFD303C2
	 */
	public static final String spaceToNull(String str) {
		if (!(str.equals("")) && !(str.equals(" "))) {
			throw new IllegalArgumentException();
		}
		return null;
	}

	/**
	 * 将null转换为空字符串""，不为空去掉尾部空格；
	 * @param str
	 * @return String
	 * @roseuid 3F6FFFD303CC
	 */
	public static final String nullToSpace(String str) {

		if (str == null) {
			return "";
		} else {
			char[] chArr = str.toCharArray();
			int l = chArr.length;
			int length = l;
			while ((l > 0) && (chArr[l - 1] <= ' ')) {
				l--;
			}
			if (l == 0) {
				return "";
			}
			return ((l < length) ? str.substring(0, l) : str);
		}
	}

	/**
	 * 将null或空字符串转换为"&nbsp;"；
	 * @param str
	 * @return String
	 * @roseuid 3F6FFFD303D6
	 */
	public static final String nullToHtml(String str) {
		if (!(str == null) && !(str.equals(""))) {
			throw new IllegalArgumentException();
		}
		return "&nbsp;";
	}

	/**
	 * 将字符串后面的空格去掉。若字符串为空或去掉空格后为空则返回null，否则返回一个新的
	 * 字符串
	 * @param str
	 * @return String
	 * @roseuid 3F6FFFD303E0
	 */
	public static final String trimString(String str) {
		char[] chArr = str.toCharArray();
		int l = chArr.length;
		int length = l;
		while ((l > 0) && (chArr[l - 1] <= ' ')) {
			l--;
		}
		if (l == 0) {
			return null;
		}
		return ((l < length) ? str.substring(0, l) : str);
	}

	/**
	 * 取得用分隔符连接起来的字符串的前半部分；
	 * 如果参数字符串中不包含该分隔符，则把参数字符串原封不动的返回
	 * 注意：空串“”是非法的分隔符
	 * @param str
	 * @param separator
	 * @return String
	 * @roseuid 3F6FFFD40002
	 */
	public static final String getStrBeforeSeparator(
		String str,
		String separator) {
		if ((separator.length() == 0) || (str.length() == 0)) {
			throw new IllegalArgumentException();
		}
		int i = str.indexOf(separator);
		if (i == -1) {
			return str;
		}
		return str.substring(0, i);
	}

	/**
	 * 取得用分隔符连接起来的字符串的后半部分；
	 * 如果参数字符串中不包含分隔符，则返回null；
	 * 注意：空串“”是非法的分隔符；
	 * @param str
	 * @param separator
	 * @return String
	 * @roseuid 3F6FFFD40015
	 */
	public static final String getStrAftereSeparator(
		String str,
		String separator) {
		if ((separator.length() == 0) || (str.length() == 0)) {
			throw new IllegalArgumentException();
		}
		int i = str.indexOf(separator);
		if (i == -1) {
			return null;
		}
		return str.substring(i + separator.length());
	}

	/**
	 * 将gb编码转换为unicode，参数为字符串数组；
	 * @param srcAry
	 * @return java.lang.String[]
	 * @roseuid 3F6FFFD40021
	 */
	public static final String[] gbToUnicode(String[] srcAry) {
		int l = srcAry.length;
		for (int i = 0; i < l; i++) {
			srcAry[i] = gbToUnicode(srcAry[i]);
		}
		return srcAry;
	}

	/**
	 * 将gb编码转换为unicode，参数为字符串；
	 * @param str
	 * @return String
	 * @roseuid 3F6FFFD4002A
	 */
	public static final String gbToUnicode(String str) {
		char[] c = str.toCharArray();
		int n = c.length;
		byte[] b = new byte[n];
		for (int i = 0; i < n; i++)
			b[i] = (byte) c[i];
		return new String(b);
	}

	/**
	 * 将unicode编码转换为gb码，参数为字符串数组；
	 * @param srcAry
	 * @return java.lang.String[]
	 * @roseuid 3F6FFFD4003D
	 */
	public static final String[] unicodeToGb(String[] srcAry) {
		int l = srcAry.length;
		for (int i = 0; i < l; i++) {
			srcAry[i] = unicodeToGb(srcAry[i]);
		}
		return srcAry;
	}

	/**
	 * 将unicode编码转换为gb码，参数为字符串；
	 * @param src
	 * @return String
	 * @roseuid 3F6FFFD40047
	 */
	public static final String unicodeToGb(String src) {
		byte[] b = src.getBytes();
		int n = b.length;
		char[] c = new char[n];
		for (int i = 0; i < n; i++)
			c[i] = (char) ((short) b[i] & 0xff);
		return new String(c);
	}

	/**
	 * 从字符串中取得两个指定字符串之间的字符串；
	 * 注意：两个指定的字符串不能相等，且不能互相包含，且不能为空串。
	 * 否则将抛出IllegalArgumentException异常。
	 * 注意：当两个指定字符串有任意一个没有在源字符串中出现时，返回null。
	 * 注意：两个指定的字符串是有顺序的，如果第一个指定的字符串第一次出现的位置
	 * 大于第二个，或者第一个字符串的最后一个字符的位置序号大于第二个字符串的第
	 * 一个字符的位置序号，则该方法将抛出IllegalArgumentException异常。
	 * @param source
	 * @param strBegin
	 * @param strEnd
	 * @return String
	 * @roseuid 3F6FFFD40052
	 */
	public static final String getStr(
		String source,
		String strBegin,
		String strEnd) {
		if ((strBegin.equals(strEnd))
			|| (strBegin.indexOf(strEnd) != -1)
			|| (strEnd.indexOf(strBegin) != -1)
			|| (strBegin.length() == 0)
			|| (source.length() == 0)
			|| (strEnd.length() == 0)) {
			throw new IllegalArgumentException();
		}
		int begin = source.indexOf(strBegin);
		int end = source.indexOf(strEnd);
		if ((begin == -1) || (end == -1)) {
			return null;
		}
		begin = begin + strBegin.length();
		return source.substring(begin, end);
	}
	/*
	 * * 从字符串中去掉指定字符串，返回剩下的字符串；
	 * @param source
	 * @param separator
	 * @return String
	 * @roseuid 3F6FFFD40065
	 */
	public static final String removeStr(String source, String str) {
		if ((source.length() == 0) || (str.length() == 0)) {
			throw new IllegalArgumentException();
		}
		int i = source.indexOf(str);
		if (i == -1)
			return source;
		StringBuffer sb = new StringBuffer();
		sb.append(source.substring(0, i)).append(
			source.substring(i + str.length()));
		if (sb.length() == 0) {
			return sb.toString();
		}
		return StringUtil.removeStr(sb.toString(), str);
	}

	/**
	 * 从字符串中去掉两个指定字符串之间的字符串，返回剩下的字符串；
	 * @param source
	 * @param strBegin
	 * @param strEnd
	 * @return String
	 * @roseuid 3F6FFFD40070
	 */
	public static final String removeStr(
		String source,
		String strBegin,
		String strEnd) {
		if ((source.length() == 0)
			|| (strBegin.length() == 0)
			|| (strEnd.length() == 0)) {
			throw new IllegalArgumentException();
		}
		if ((strBegin.equals(strEnd)
			|| (strBegin.indexOf(strEnd) != -1)
			|| (strEnd.indexOf(strBegin) != -1))
			|| (strBegin.length() == 0)
			|| (strEnd.length() == 0)) {
			throw new IllegalArgumentException();
		}
		StringBuffer sb = new StringBuffer();
		int begin = source.indexOf(strBegin);
		int end = source.indexOf(strEnd);
		if ((begin == -1) || (end == -1)) {
			return null;
		}
		begin = begin + strBegin.length();
		if (begin <= end) {
			return sb
				.append(source.substring(0, begin))
				.append(source.substring(end))
				.toString();
		}
		return null;
	} 
	
	 /**
     * Replaces all instances of oldString with newString in line.
     */
    public static final String replace( String line, String oldString, String newString )
    {
        int i=0;
        if ( ( i=line.indexOf( oldString, i ) ) >= 0 ) {
            char [] line2 = line.toCharArray();
	        char [] newString2 = newString.toCharArray();
            int oLength = oldString.length();
            StringBuffer buf = new StringBuffer(line2.length);
            buf.append(line2, 0, i).append(newString2);
            i += oLength;
            int j = i;
            while( ( i=line.indexOf( oldString, i ) ) > 0 ) {
                buf.append(line2, j, i-j).append(newString2);
                i += oLength;
                j = i;
            }
            buf.append(line2, j, line2.length - j);
            return buf.toString();
        }
        return line;
    }

   /**
    * Replaces all instances of oldString with newString in line.
    * The count Integer is updated with number of replaces.
    */
    public static final String replace( String line, String oldString, String newString,
            Integer count)
    {
        int i=0;
        if ( ( i=line.indexOf( oldString, i ) ) >= 0 ) {
            int counter = 0;
            counter++;
            char [] line2 = line.toCharArray();
	        char [] newString2 = newString.toCharArray();
            int oLength = oldString.length();
            StringBuffer buf = new StringBuffer(line2.length);
            buf.append(line2, 0, i).append(newString2);
            i += oLength;
            int j = i;
            while( ( i=line.indexOf( oldString, i ) ) > 0 ) {
                counter++;
                buf.append(line2, j, i-j).append(newString2);
                i += oLength;
                j = i;
            }
            buf.append(line2, j, line2.length - j);
            count = new Integer(counter);
            return buf.toString();
        }
        return line;
    }

    private static String LT = "&lt;";
    private static String GT = "&gt;";

    /**
     * This method takes a string which may contain HTML tags (ie, <b>, <table>,
     * etc) and converts the '<' and '>' characters to their HTML escape
     * sequences.
     *
     * @param input The text to be converted.
     * @return The input string with the characters '<' and '>' replaced with
     *  &lt; and &gt; respectively.
     */
    public static final String escapeHTMLTags( String input ) {
        //Check if the string is null or zero length -- if so, return
        //what was sent in.
        if( input == null || input.length() == 0 ) {
            return input;
        }
        //Use a StringBuffer in lieu of String concatenation -- it is
        //much more efficient this way.
        StringBuffer buf = new StringBuffer(input.length());
        char ch = ' ';
        for( int i=0; i<input.length(); i++ ) {
            ch = input.charAt(i);
            if( ch == '<' ) {
                buf.append( LT );
            }
            else if( ch == '>' ) {
                buf.append( GT );
            }
            else {
                buf.append( ch );
            }
        }
        return buf.toString();
    }

    /**
     * Used by the hash method.
     */
    private static MessageDigest digest = null;

    /**
     * Hashes a String using the Md5 algorithm and returns the result as a
     * String of hexadecimal numbers.
     * <p>
     * A hash is a one-way function -- that is, given an
     * input, an output is easily computed. However, given the output, the
     * input is almost impossible to compute. This is useful for passwords
     * since we can store the hash and a hacker will then have a very hard time
     * determining the original password.
     * <p>
     * In Jive, every time a user logs in, we simply
     * take their plain text password, compute the hash, and compare the
     * generated hash to the stored hash. Since it is almost impossible that
     * two passwords will generate the same hash, we know if the user gave us
     * the correct password or not. The only negative to this system is that
     * password recovery is basically impossible. Therefore, a reset password
     * method is used instead.
     *
     * @param data the String to compute the hash of.
     */
    public static String hash(String data) {
        if (digest == null) {
            try {
                digest = MessageDigest.getInstance("MD5");
            }
            catch (NoSuchAlgorithmException nsae) {
                System.err.println("Failed to load the MD5 MessageDigest. " +
                "Jive will be unable to function normally.");
                nsae.printStackTrace();
            }
        }
        //Now, compute hash.
        digest.update(data.getBytes());
        return toHex(digest.digest());
    }

    /**
     * Turns array of bytes into string representing each byte as
     * unsigned hex number.
     * <p>
     * Method by Santeri Paavolainen, Helsinki Finland 1996<br>
     * (c) Santeri Paavolainen, Helsinki Finland 1996<br>
     * Distributed under LGPL.
     *
     * @param hash Array of bytes to convert to hex-string
     * @returns generated hex string
     */
     public static String toHex (byte hash[]) {
        StringBuffer buf = new StringBuffer(hash.length * 2);
        int i;

        for (i = 0; i < hash.length; i++) {
            if (((int) hash[i] & 0xff) < 0x10) {
                buf.append("0");
            }
            buf.append(Long.toString((int) hash[i] & 0xff, 16));
        }
        return buf.toString();
     }

    public static String changeCode(String srcString,String srcEnCode,String outEnCode){
    if ( srcString==null || srcString.equals("") ) return "";
    if ( srcEnCode.equals(outEnCode) ) return srcString;
    try
      {
        String messagetmp = new String((srcString).getBytes(srcEnCode),outEnCode);
        return messagetmp;
       }
    catch(java.io.UnsupportedEncodingException e){
         System.out.println("changeCode err");
         return srcString ;
       }
    }

	/**
	 * This method converts the "'" characters to "''".
	 * "\"->"&quot;";"<"->"&lt;";">"->"&gt;";
	 *
	 * @param input The text to be converted.
	 * @return converts the "'" characters to "''".
	 */
    public static String StrToDouble(String strSource) {
    String strResult = "";
    for (int i=0;i<strSource.length();i++) {
      if (strSource.substring(i,i+1).equalsIgnoreCase("'")) {
        strResult = strResult + "''";
      } else if (strSource.substring(i,i+1).equalsIgnoreCase("\"")) {
        strResult = strResult + "&quot;";
      } else if (strSource.substring(i,i+1).equalsIgnoreCase("<")) {
        strResult = strResult + "&lt;";
      } else if (strSource.substring(i,i+1).equalsIgnoreCase(">")) {
        strResult = strResult + "&gt;";
      } else {
        strResult = strResult + strSource.substring(i,i+1);
      }
    }
    return strResult;
  }
  
  /**
   * This method takes a string to HTML tags 
   * (ie,"&lt", "&gt" , "&amp;" , "&quot;","&copy;","&reg;","&yen;","&euro;","&#153;"etc) 
   * 
   * CR --> "& lt br  & gt"
   *
   * @param input The text to be converted.
   * @return converts to their HTML escape
   */
  public static String htmEncode(String s) {
	if (s==null) return null;
	if (s=="") return "";
    
    StringBuffer stringbuffer = new StringBuffer();
    int j = s.length();
    for(int i = 0; i < j; i++) {
      char c = s.charAt(i);
      switch(c) {
        case 60:
                  stringbuffer.append("&lt;");
                  break;
        case 62:
                  stringbuffer.append("&gt;");
                  break;
        case 38:
                  stringbuffer.append("&amp;");
                  break;
        case 34:
                  stringbuffer.append("&quot;");
                  break;
        case 169:
                  stringbuffer.append("&copy;");
                  break;
        case 174:
                  stringbuffer.append("&reg;");
                  break;
        case 165:
                  stringbuffer.append("&yen;");
                  break;
        case 8364:
                  stringbuffer.append("&euro;");
                  break;
        case 8482:
                  stringbuffer.append("&#153;");
                  break;
        case 13:
                  if(i < j - 1 && s.charAt(i + 1) == 10) {
                    stringbuffer.append(LT+"br"+GT);
                    i++;
                  }
                  break;
        case 32:
                  /*
                  if(i < j - 1 && s.charAt(i + 1) == ' ') {
                    stringbuffer.append(" &nbsp;");
                    i++;
                  }
                  */
				  stringbuffer.append("&nbsp;");
                  break;

        default:
                  stringbuffer.append(c);
                  break;
      }
    }
    return new String(stringbuffer.toString());
  }
  

  /**
   * 特殊字符逆向处理：将经过htmEncode(...)后的字符串恢复到原始状态
   * (ie,"&lt", "&gt" , "&amp;" , "&quot;","&copy;","&reg;","&yen;","&euro;","&#153;"etc) 
   * 
   * "& lt br & gt" --> CR
   * 
   * @param s 待处理的字符
   * @return 
   */
  public static String htmEncodeReverse(String s)
	 {
	 	s = replace(s,"&lt;","<");
	 	
	 	s = replace(s,"&gt;",">");
	 	
	 	s = replace(s,"&amp;","&");
	 	
	 	s = replace(s,"&quot;","\"");	 	
	 	
	 	s = replace(s,"&copy;", String.valueOf( 169 ) );
	 	
	 	s = replace(s,"&reg;",String.valueOf( 174 )  );
	 	
	 	s = replace(s,"&yen;",String.valueOf( 165 )  );
	 	
	 	s = replace(s,"&euro;",String.valueOf( 8364 )  );
	 	
	 	s = replace(s,"&#153;",String.valueOf( 8482 )  );
	 	
	 	s = replace(s,"<br>",String.valueOf( 13 )  );
	 	
	 	s = replace(s,"&nbsp;",String.valueOf( 32 )  );
	 	
	 	s = replace(s,"&" + Integer.toString('\r') +";", "\r");
	 	
	 	s = replace(s,"&" + Integer.toString('\n') +";", "\n");
	 	
	 	return s;
	 	
	 }
  
  /**
   * 特殊字符逆向处理：将经过htmEncodeWithBr(...)后的字符串恢复到原始状态
   * (ie,"&lt", "&gt" , "&amp;" , "&quot;","&copy;","&reg;","&yen;","&euro;","&#153;"etc) 
   * 
   * "&lt br &gt" --> CR
   * 
   * @param s 待处理的字符
   * @return 
   */
  public static String htmEncodeWithBrReverse(String s)
	 {
	 	
  	    s = replace(s,"<br>",String.valueOf( 13 )  );
  	    
  		s = replace(s,"&lt;","<");
	 	
	 	s = replace(s,"&gt;",">");
	 	
	 	s = replace(s,"&amp;","&");
	 	
	 	s = replace(s,"&quot;","\"");	 	
	 	
	 	s = replace(s,"&copy;", String.valueOf( 169 ) );
	 	
	 	s = replace(s,"&reg;",String.valueOf( 174 )  );
	 	
	 	s = replace(s,"&yen;",String.valueOf( 165 )  );
	 	
	 	s = replace(s,"&euro;",String.valueOf( 8364 )  );
	 	
	 	s = replace(s,"&#153;",String.valueOf( 8482 )  );	 	
	 	
	 	s = replace(s,"&nbsp;",String.valueOf( 32 )  );
	 	
	 	s = replace(s,"&" + Integer.toString('\r') +";", "\r");
	 	
	 	s = replace(s,"&" + Integer.toString('\n') +";", "\n");
	 	
	 	return s;
	 	
	 }
  
  
  /**
   * This method takes a string to HTML tags 
   * (ie,"&lt", "&gt" , "&amp;" , "&quot;","&copy;","&reg;","&yen;","&euro;","&#153;"etc) 
   * 
   *  CR --> "&lt br &gt"
   *
   * @param input The text to be converted.
   * @return converts to their HTML escape
   */
  public static String htmEncodeWithBr(String s) {
	if (s==null) return null;
	if (s=="") return "";
    
    StringBuffer stringbuffer = new StringBuffer();
    int j = s.length();
    for(int i = 0; i < j; i++) {
      char c = s.charAt(i);
      switch(c) {
        case 60:
                  stringbuffer.append("&lt;");
                  break;
        case 62:
                  stringbuffer.append("&gt;");
                  break;
        case 38:
                  stringbuffer.append("&amp;");
                  break;
        case 34:
                  stringbuffer.append("&quot;");
                  break;
        case 169:
                  stringbuffer.append("&copy;");
                  break;
        case 174:
                  stringbuffer.append("&reg;");
                  break;
        case 165:
                  stringbuffer.append("&yen;");
                  break;
        case 8364:
                  stringbuffer.append("&euro;");
                  break;
        case 8482:
                  stringbuffer.append("&#153;");
                  break;
        case 13:
                  if(i < j - 1 && s.charAt(i + 1) == 10) {
                    stringbuffer.append("<br>");
                    i++;
                  }
                  break;
        case 32:
                  /*
                  if(i < j - 1 && s.charAt(i + 1) == ' ') {
                    stringbuffer.append(" &nbsp;");
                    i++;
                  }
                  */
				  stringbuffer.append("&nbsp;");
                  break;

        default:
                  stringbuffer.append(c);
                  break;
      }
    }
    return new String(stringbuffer.toString());
  }
  
  /**
   * This method converts the "'" characters to "''".
   * 
   * @param input The text to be converted.
   * @return converts the "'" characters to "''".
   */
  public static String replaceSingleQuotes(String s) { //Replace Single Quotes
      if (s==null) return null;
      if (s=="") return "";

      String tmps = s ;
      int sindex = s.indexOf("'");
      if  ( sindex == 0 ) tmps = "''"+ replaceSingleQuotes( s.substring(1) ) ;
      if  ( sindex > 0 ) tmps = s.substring( 0,sindex ) + "''" + replaceSingleQuotes( s.substring( sindex+1 ) ) ;
      return tmps ;

  }

  /**
   * This method converts the "'" characters to "\'".
   * 
   * @param input The text to be converted.
   * @return converts the "'" characters to "''".
   */
  public static String replaceQuotes(String s) { //Replace Single Quotes
      if (s==null) return null;
      if (s=="") return "";

      String tmps = s ;
      int sindex = s.indexOf("'");
      if  ( sindex == 0 ) tmps = "\\'"+ replaceQuotes( s.substring(1) ) ;
      if  ( sindex > 0 ) tmps = s.substring( 0,sindex ) + "\\'" + replaceQuotes( s.substring( sindex+1 ) ) ;
      return tmps ;
  }
  
	/**
	 * Turn special characters into escaped characters conforming to JavaScript.
	 * Handles complete character set defined in HTML 4.01 recommendation.
	 * @param input the input string
	 * @return the escaped string
	 */
	public static String javaScriptEscape(String input) {
		if (input == null) {
			return input;
		}

		StringBuffer filtered = new StringBuffer(input.length());
		char prevChar = '\u0000';
		char c;
		for (int i = 0; i < input.length(); i++) {
			c = input.charAt(i);
			if (c == '"') {
				filtered.append("\\\"");
			}
			else if (c == '\'') {
				filtered.append("\\'");
			}
			else if (c == '\\') {
				filtered.append("\\\\");
			}
			else if (c == '\t') {
				filtered.append("\\t");
			}
			else if (c == '\n') {
				if (prevChar != '\r') {
					filtered.append("\\n");
				}
			}
			else if (c == '\r') {
				filtered.append("\\n");
			}
			else if (c == '\f') {
				filtered.append("\\f");
			}
			else {
				filtered.append(c);
			}
			prevChar = c;

		}
		return filtered.toString();
	}
  
  /**
   * This method converts the "'" characters to "''".
   * and takes the string which may contain HTML tags (ie, "<",
   * " ",">", etc) and converts to their HTML escape
   * sequences.
   *
   * @param s The text to be converted.
   * @return converts characters.
   */
  public static String replaceSingleQuotesAndHtmEncode(String s) { //Replace Single Quotes
	return htmEncode( replaceSingleQuotes(s));

  }

  /**
   * 将字符串source中strBereplaced的字符串用strReplace代替 ；
   * @param source
   * @param strBereplaced
   * @param strReplace
   * @return String
   * @roseuid 3F6FFFD40083
   */
	public static final String replaceAllString(
		String source,
		String strBereplaced,
		String strReplace) {
		if ((source.length() == 0)
			|| (strBereplaced.length() == 0)
			|| (strReplace.length() == 0)) {
			throw new IllegalArgumentException();
		}
		StringBuffer sb = new StringBuffer();
		if (source.indexOf(strBereplaced) == -1) {
			return source;
		} else {
			int i = source.indexOf(strBereplaced);
			return source =
				sb
					.append(source.substring(0, i))
					.append(strReplace)
					.append(
						StringUtil.replaceAllString(
							source
								.substring(i + strBereplaced.length())
								.toString(),
							strBereplaced,
							strReplace))
					.toString();
		}
	} 
	
	/**
	 * 将字符串source中从strBegin到strEnd之间的字符串用strReplace代替 ；
	 * @param source
	 * @param strBegin
	 * @param strEnd
	 * @param strReplace
	 * @return String
	 * @roseuid 3F6FFFD40097
	 */
	public static final String replaceFromTo(
		String source,
		String strBegin,
		String strEnd,
		String strReplace) {
		if ((source.length() == 0)
			|| (strBegin.length() == 0)
			|| (strEnd.length() == 0)
			|| (strReplace.length() == 0)) {
			throw new IllegalArgumentException();
		}
		StringBuffer sb = new StringBuffer();
		int begin = source.indexOf(strBegin);
		int end = source.indexOf(strEnd);
		if ((begin == -1) || (end == -1)) {
			return null;
		}
		begin = begin + strBegin.length();
		if (begin < end) {
			return sb
				.append(source.substring(0, begin))
				.append(strReplace)
				.append(source.substring(end))
				.toString();
		}
		return null;
	} 
	/**
	 * 得到给定字符串在特定屏幕下的宽度；
	 * @param fontMetrics
	 * @param str
	 * @return int
	 * @roseuid 3F6FFFD400AB
	 */

	public static final int computeStringWidth(
		FontMetrics fontMetrics,
		String str) {
		return fontMetrics.stringWidth(str);
	} 
	/**
	 * 将字符串去掉分隔符号，并转换成字符串数组。
	 * @param source
	 * @param token
	 * @return java.lang.String[]
	 * @roseuid 3F6FFFD400C9
	 */
	public static final String[] convertArray(String source, String token) {
		String[] strArr = null;
		ArrayList al = new ArrayList();
		StringTokenizer st = new StringTokenizer(source, token, false);
		while (st.hasMoreTokens()) {
			al.add(st.nextToken());
		}
		strArr = new String[al.size()];
		for (int i = 0; i < al.size(); i++) {
			strArr[i] = (String) (al.get(i));
		}
		return strArr;
	}
	

    /**
     * 把字符串分隔成数组
     * 
     * @param str
     *            字符 如： 1/2/3/4/5
     * @param seperator
     *            分隔符号 如: /
     * @return String[] 字符串数组 如: {1,2,3,4,5}
     */
    public static String[] split(String str, String seperator) {
        if(str==null) return new String[0];
        
        StringTokenizer token = new StringTokenizer(str, seperator);
        int count = token.countTokens();
        String[] ret = new String[count];
        for (int i = 0; i < count; i++) {
            ret[i] = token.nextToken();
        }
        return ret;
    }
    
    /**
     * 将字符串数组用分隔符组合成一个字符串
     * @param strArray 字符串数组
     * @param seperator 分隔符
     * @return
     */
    public static String combine(String[] strArray,String seperator){
        if(strArray==null) return null;
        
        StringBuffer sb=new StringBuffer();
        int len=strArray.length;
        for(int i=0;i<len;i++){
            sb.append(strArray[i]);
            if(i!=len-1){
                sb.append(seperator);
            }
        }
        return sb.toString();
    }
    
    /**
     * 判断一个字符串是否在一个字符串数组里
     * @param strArray
     * @param str
     * @return
     */
    public static boolean contain(String[] strArray,String str){
        if(strArray==null||str==null) return false;
        
        int len=strArray.length;
        for(int i=0;i<len;i++){
            if(str.equals(strArray[i])){
                return true;
            }
        }
        return false;
    }
	
    
    
    
    /**
     * 特殊字符逆向处理：将经过normalize(...)后的字符串恢复到原始状态
     * 反处理的HTML特殊标签："&lt", "&gt" , "&amp;" , "&quot;",换行
     * @param s 待处理的字符
     * @return 
     */
    public static String normalizeReverse(String s)
	 {
	 	s = replace(s,"&lt;","<");
	 	s = replace(s,"&gt;",">");
	 	s = replace(s,"&amp;","&");
	 	s = replace(s,"&quot;","\"");
	 	s = replace(s,"&" + Integer.toString('\r') +";", "\r");
	 	s = replace(s,"&" + Integer.toString('\n') +";", "\n");
	 	return s;
	 	
	 }
	
    /**
     * 特殊字符逆向处理：类似于htmEncode(...),处理的基本的HTML特殊标签，没有htmEncode(...)处理的多
     * 处理的HTML特殊标签："&lt", "&gt" , "&amp;" , "&quot;",换行
     * @param s 待处理的字符
     * @return 
     */
    public static String normalize(String s) {
		
        StringBuffer str = new StringBuffer();

        int len = (s != null) ? s.length() : 0;
        for (int i = 0; i < len; i++) {
            char ch = s.charAt(i);
            switch (ch) {
            case '<': {
                    str.append("&lt;");
                    break;
                }
            case '>': {
                    str.append("&gt;");
                    break;
                }
            case '&': {
                    str.append("&amp;");
                    break;
                }
            case '"': {
                    str.append("&quot;");
                    break;
                }
            case '\r':
            case '\n': {
                    str.append("&#");
                    str.append(Integer.toString(ch));
                    str.append(';');
                    break;
                }
            default: {
                    str.append(ch);
                }
            }
        }

        return str.toString();

    } 
	
	
}
