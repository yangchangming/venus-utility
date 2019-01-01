package venus.pub.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.*;

/**
 * 字符串操作助手类，主要提供以下功能： 1) escape html字符串 2) 截取长度进行显示 3) 字符替换及字符分隔
 * 
 * 从各个组件中抽取形成，类做成时间 2008-09-03
 */
public class StringHelper {
	//日志全局
	private static final Log logger = LogFactory.getLog(StringHelper.class);

	//默认私有构造函数, 防止外界创建实例(其实创建了也无用, 类对我开放的均为静态方法)
	private StringHelper() {
	}

	/**
	 * 将字符串以指定字符串切割后,分配到List中
	 * 
	 * @param strValue-->输入字符串
	 * @return List
	 */
	public static List getTokenizerList(String strValue, String delim) {
		List myList = new ArrayList();
		StringTokenizer stChat = new StringTokenizer(strValue, delim);
		int iLength = stChat.countTokens();
		for (int i = 0; i < iLength; i++) {
			String strTemp = stChat.nextToken();
			if (strTemp == null)
				strTemp = "";
			myList.add(strTemp);
		}
		return myList;
	}

	/**
	 * 将String[]中字符串以逗号分割后拼成一个字符串,不带有单引号
	 * 
	 * @param strArray-->输入字符串数组
	 * @return String
	 */
	public static String parseToSQLString(String[] strArray) {
		if (strArray == null || strArray.length == 0)
			return "";
		String myStr = "";
		for (int i = 0; i < strArray.length - 1; i++) {
			myStr += strArray[i] + ",";
		}
		myStr += strArray[strArray.length - 1];
		return myStr;
	}

	/**
	 * 将String[]中字符串以逗号分割后拼成一个字符串,带有单引号
	 * 
	 * @param strArray-->输入字符串数组
	 * @return String
	 */
	public static String parseToSQLStringComma(String[] strArray) {
		if (strArray == null || strArray.length == 0)
			return "";
		String myStr = "";
		for (int i = 0; i < strArray.length - 1; i++) {
			myStr += "'" + strArray[i] + "',";
		}
		myStr += "'" + strArray[strArray.length - 1] + "'";
		return myStr;
	}

	/**
	 * 将String[]中字符串以逗号分割后拼成一个字符串,带有单引号
	 * 
	 * @param strArray-->输入字符串数组
	 * @return String
	 */
	public static String parseToSQLStringComma(Object[] strArray) {
		if (strArray == null || strArray.length == 0)
			return "";
		String myStr = "";
		for (int i = 0; i < strArray.length - 1; i++) {
			myStr += "'" + strArray[i] + "',";
		}
		myStr += "'" + strArray[strArray.length - 1] + "'";
		return myStr;
	}

	/**
	 * 将ISO字符串转换为GBK编码的字符串。
	 * 
	 * @param str-->输入字符串
	 * @return 经编码后的字符串，如果有异常，则返回原编码字符串
	 */
	public static String iso2Gbk(String original) {
		if (original != null) {

			try {
				return new String(original.getBytes("iso-8859-1"), "gbk");
			} catch (UnsupportedEncodingException e) {
				logger.error(e);
				return null;
			}
		}
		return null;
	}

	/**
	 * 将iso-8859-1字符串转换为UTF-8编码的字符串。
	 * 
	 * @param original-->输入字符串
	 * @return 经编码后的字符串，如果有异常，则返回原编码字符串
	 */
	public static String iso2Utf8(String original) {
		if (original != null) {

			try {
				return new String(original.getBytes("iso-8859-1"), "utf-8");
			} catch (UnsupportedEncodingException e) {
				logger.error(e);
				return null;
			}
		}
		return null;
	}

	/**
	 * 功能: 把指定字符串original 从encode1 转化到encode2
	 * 
	 * @param original
	 * @param encode1
	 * @param encode2
	 * @return
	 */
	public static String encode2Encode(String original, String encode1,
			String encode2) {
		if (original != null) {
			try {
				return new String(original.getBytes(encode1), encode2);
			} catch (UnsupportedEncodingException e) {
				logger.error(e);
				return null;
			}
		}
		return null;
	}

	/**
	 * 功能: 以encode1的编码方式获得original
	 * 
	 * @param original
	 * @param encode1
	 * @return
	 */
	public static String getStringByEncode(String original, String encode1) {
		if (original != null) {
			try {
				return new String(original.getBytes(), encode1);
			} catch (UnsupportedEncodingException e) {
				logger.error(e);
				return null;
			}
		}
		return null;
	}

	/**
	 * 功能: 把指定字符串strSource 中的strFrom 全部替换为strTo
	 * 
	 * @param strSource
	 * @param strFrom
	 * @param strTo
	 * @return
	 */
	public static String replaceAllString(String strSource, String strFrom,
			String strTo) {
		String strDest = "";
		int intFromLen = strFrom.length();
		int intPos;

		while ((intPos = strSource.indexOf(strFrom)) != -1) {
			strDest = strDest + strSource.substring(0, intPos);
			strDest = strDest + strTo;
			strSource = strSource.substring(intPos + intFromLen);
		}
		strDest = strDest + strSource;

		return strDest;
	}

	/**
	 * 过滤Html页面中的敏感字符
	 * 
	 * @param value
	 * @return
	 */
	public static String replaceStringToHtml(String value) {

		if (value == null) {
			return ("");
		}

		char content[] = new char[value.length()];
		value.getChars(0, value.length(), content, 0);
		StringBuffer result = new StringBuffer(content.length + 50);

		for (int i = 0; i < content.length; i++) {
			switch (content[i]) {
			case '<':
				result.append("&lt;");
				break;
			case '>':
				result.append("&gt;");
				break;
			case '&':
				result.append("&amp;");
				break;
			case '"':
				result.append("&quot;");
				break;
			case '\'':
				result.append("&#39;");
				break;
			case '\n':
				result.append("<BR>");
				break;
			case '\r':
				result.append("<BR>");
				break;
			default:
				result.append(content[i]);
			}
		}

		return result.toString();
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

	/**
	 * 显示数据前过滤掉null
	 * 
	 * @param myString
	 * @return
	 */
	public static String prt(String myString) {
		if (myString != null)
			return myString;
		else
			return "";
	}

	/**
	 * 显示数据前过滤掉null，截取一定位数
	 * 
	 * @param myString
	 * @param index
	 *                    最大显示的长度
	 * @return
	 */
	public static String prt(String myString, int index) {
		if (myString != null) {
			if (myString.length() >= index) {
				return myString.substring(0, index);
			} else {
				return myString;
			}
		} else
			return "";
	}

	/**
	 * 显示数据前过滤掉null，截取一定位数，并加上表示，如省略号
	 * 
	 * @param myString
	 * @param index
	 *                    最大显示的长度
	 * @return
	 */
	public static String prt(String myString, int index, String accessional) {
		if (accessional == null || "".equals(accessional)) {
			accessional = "...";
		}
		if (myString != null) {
			if (myString.length() >= index) {
				return myString.substring(0, index) + accessional;
			} else {
				return myString;
			}
		} else
			return "";
	}

	/**
	 * 判断一个数组是否包含一个字符串
	 * 
	 * @param arrayString
	 * @param str
	 * @return
	 */
	public static boolean isContainStringInArray(String[] arrayString, String str) {
		if (arrayString == null || arrayString.length == 0) {
			return false;
		}
		for (int i = 0; i < arrayString.length; i++) {
			if (arrayString[i].equals(str))
				return true;
		}
		return false;
	}

	//解析查询条件，把"$"替换成"%"
	public static String replaceQueryCondition(String str) {
		if (str.indexOf("$") != -1) {
			str = str.replace('$', '%');
		}
		return str;
	}

	/**
	 * 将p1#v1+p2#v2格式的字符串转换为Map(String,String)
	 * 
	 * @param str
	 * @return
	 */
	public static Map parseString(String str) {
		Map result = new HashMap();
		String[] enumsArray = str.split("\\+");
		for (int i = 0; i < enumsArray.length; i++) {
			String[] genum = enumsArray[i].split("#");
			if (genum.length != 2)
				break;
			result.put(genum[0], genum[1]);
		}
		return result;
	}

	/**
	 * 功能: 把指定字符串strSource 中的strFrom 全部替换为strTo
	 * 
	 * @param strSource
	 * @param strFrom
	 * @param strTo
	 * @return
	 */
	public static String replaceAll(String strSource, String strFrom,
			String strTo) {
		String strDest = "";
		int intFromLen = strFrom.length();
		int intPos;

		while ((intPos = strSource.indexOf(strFrom)) != -1) {
			strDest = strDest + strSource.substring(0, intPos);
			strDest = strDest + strTo;
			strSource = strSource.substring(intPos + intFromLen);
		}
		strDest = strDest + strSource;

		return strDest;
	}

	/**
	 * 功能: 把strSource中的第1个strFrom替换为strTo
	 * 
	 * @param strSource
	 * @param strFrom
	 * @param strTo
	 * @return
	 */
	public static String replaceFirst(String strSource, String strFrom,
			String strTo) {
		String strDest = "";
		int intFromLen = strFrom.length();
		int intPos;

		while ((intPos = strSource.indexOf(strFrom)) != -1) {
			strDest = strDest + strSource.substring(0, intPos);
			strDest = strDest + strTo;
			strSource = strSource.substring(intPos + intFromLen);
			break;
		}
		strDest = strDest + strSource;

		return strDest;
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
				{ "\n", "\\n" }, { "\t", "\\t" }, { "\f", "\\f" },
				{ "\b", "\\b" } }); // {"\r", "\
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

	public static String prt(Object obj) {
		if (obj != null) {
			return prt(obj.toString());
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

	public static String prt(Object obj, int index, String accessional) {
		if (obj != null)
			return prt(obj.toString(), index, accessional);
		else {
			return "";
		}
	}

	/**
	 * 功能: 测试各种编码之间的转化，找出乱码原因
	 * 
	 * @param original
	 * @return
	 */
	public static String testAllEncode(String original) {
		return testAllEncode(original, new String[] { "GBK", "iso8859-1",
				"gb2312", "utf-8" });
	}

	/**
	 * 功能: 测试各种编码之间的转化，找出乱码原因
	 * 
	 * @param original
	 * @param encode
	 * @return
	 */
	public static String testAllEncode(String original, String[] encode) {
		String rtValue = "original = " + original + "\n";
		if (encode == null && encode.length < 2) {
			return rtValue;
		}
		for (int i = 0; i < encode.length; i++) {
			rtValue += "\n" + encode[i] + "-->\n";
			for (int j = 0; j < encode.length; j++) {
				if (j != i) {
					rtValue += encode[i] + "-->" + encode[j] + " = "
							+ encode2Encode(original, encode[i], encode[j])
							+ "\n";
				}
			}
		}
		return rtValue;
	}

	/**
	 * 功能: 对url编码
	 * 
	 * @param url
	 * @return
	 */
	public static String encodeUrl(String url) {
		String rtStr = "";
		try {
			if (url != null && url.length() >= 0) {
				rtStr = URLEncoder.encode(url, "GBK");
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return rtStr;
	}

	/**
	 * 功能: 把Map中的值依次取出来，以URL传值的方式拼成字符串
	 * 
	 * @param mValue
	 * @return
	 */
	public static String encodeUrlParameter(Map mValue) {
		return encodeUrlParameter(mValue, new String[0]);
	}

	/**
	 * 功能: 把Map中的值依次取出来，以URL传值的方式拼成字符串
	 * 
	 * @param mValue
	 * @param ignoreName
	 *                    忽略的field
	 * @return
	 */
	public static String encodeUrlParameter(Map mValue, String[] ignoreName) {
		String str = "";
		for (Iterator itMValue = mValue.keySet().iterator(); itMValue.hasNext();) {
			String tempKey = String.valueOf(itMValue.next());
			String tempValue = (mValue.get(tempKey) == null) ? "" : String
					.valueOf(mValue.get(tempKey));
			if (tempKey.startsWith("VENUS") || tempKey.startsWith("RANMIN")) {
				continue;
			}
			if (isContainStringInArray(ignoreName, tempKey)) {
				continue;
			}
			if (str.length() > 0) {
				str += "&";
			}
			str += tempKey + "=" + encodeUrl(tempValue);
		}
		return str;
	}

	/**
	 * 功能: 得到str的首字母大写
	 * 
	 * @param str
	 * @return
	 */
	public static String toFirstUpperCase(String str) {
		if (str == null || str.length() == 0) {
			return str;
		} else {
			String firstStr = str.substring(0, 1);
			return firstStr.toUpperCase() + str.substring(1);
		}
	}

	/**
	 * 功能: 得到百分比的显示
	 * 
	 * @param numerator
	 * @param denominator
	 * @return
	 */
	public static String getPercentage(int numerator, int denominator) {
		return getPercentage(numerator * 1.00, denominator * 1.00);
	}

	/**
	 * 功能: 得到百分比的显示
	 * 
	 * @param numerator
	 * @param denominator
	 * @return
	 */
	public static String getPercentage(double numerator, double denominator) {
		double percentage = numerator * 1.00 / denominator;
		if (String.valueOf(percentage).endsWith(String.valueOf(Double.NaN))) {
			return "空";
		}
		percentage = percentage * 100;
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		return nf.format(percentage) + "%";
	}

	/**
	 * 功能:
	 * 
	 * @param value
	 * @param fractionDigits
	 * @return
	 */
	public static String defaultFormatDouble(double value, int fractionDigits) {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(fractionDigits);
		nf.setMaximumFractionDigits(fractionDigits);
		return nf.format(value);
	}

	/**
	 * 功能: 把15位的身份证号码升级为18位
	 * 
	 * @param oldIdCard
	 * @return
	 */
	public static String updateIdCard(String oldIdCard) {
		String newIdCard = "";
		StringBuffer tempStrOld = new StringBuffer();
		tempStrOld.append(oldIdCard);
		int cOld[] = new int[17];
		int iSum = 0;
		oldIdCard = oldIdCard.substring(0, 6) + "19"
				+ oldIdCard.substring(6, oldIdCard.length());
		try {
			if (oldIdCard.length() != 17) {
				throw new Exception();
			}
			for (int i = 0; i < 17; i++) {
				cOld[i] = Integer.parseInt(String.valueOf(oldIdCard.charAt(i)));
			}
			int wi[] = new int[] { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5,
					8, 4, 2 };
			iSum = 0;
			for (int i = 0; i < 17; i++) {
				iSum = iSum + cOld[i] * wi[i];
			}
		} catch (Exception e) {
			throw new RuntimeException("请输入正确格式的身份证号码!");
		}
		int y = iSum % 11;
		String strVer = new String("10X98765432");
		newIdCard = oldIdCard + strVer.substring(y, y + 1);
		return newIdCard;
	}

	/**
	 * 功能: 得到指定长度的int的字符串
	 * 
	 * @param myInt
	 * @param length
	 * @return
	 */
	public static String getFormatLengthInt(int myInt, int length) {
		String str = String.valueOf(myInt);
		if (str.length() < length) {
			int offLength = length - str.length();
			for (int j = 0; j < offLength; j++) {
				str = "0" + str;
			}
		}
		return str;
	}
}