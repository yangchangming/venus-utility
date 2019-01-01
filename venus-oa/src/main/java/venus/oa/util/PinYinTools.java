/*
 * 创建日期 2008-8-1
 */
package venus.oa.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import venus.commons.xmlenum.EnumRepository;
import venus.commons.xmlenum.EnumValueMap;

import java.util.*;

/**
 * 2008-8-1
 * 按拼音排序
 * @author changming.Y <changming.yang.ah@gmail.com>
 */
public class PinYinTools {
	private static EnumRepository er;
	static {
	    er = EnumRepository.getInstance();
            er.loadFromDir();
	}
        
	/**
	 *  2008-8-1
	 * 拼音排序通用比较器
	 * @author changming.Y <changming.yang.ah@gmail.com>
	 */
	public class PinyinComparator implements Comparator {
		private String concatPinyinStringArray(String[] pinyinArray) {
			StringBuffer pinyinStrBuf = new StringBuffer();

			if ((null != pinyinArray) && (pinyinArray.length > 0)) {
				for (int i = 0; i < pinyinArray.length; i++) {
					pinyinStrBuf.append(pinyinArray[i]);
				}
			}
			String outputString = pinyinStrBuf.toString();
			return outputString;
		}

		public int compare(Object o1, Object o2) {

			char c1 = ((String) o1).charAt(0);
			char c2 = ((String) o2).charAt(0);
			return concatPinyinStringArray(
					PinyinHelper.toHanyuPinyinStringArray(c1)).compareTo(
					concatPinyinStringArray(PinyinHelper
							.toHanyuPinyinStringArray(c2)));
		}
	}

	/**
	 *  2008-8-1
	 * 组织参照树专用拼音排序比较器
	 * @author changming.Y <changming.yang.ah@gmail.com>
	 */
	public class OrganizeComparator implements Comparator {
		public int compare(Object o1, Object o2) {
			return new PinyinComparator().compare(((Map) o1).get("name"),
					((Map) o2).get("name"));
		}
	}
	
	/**
	 * 获得中文的拼音
	 * @param str
	 * @return
	 */
	public static String getPingYin(String str) {
	    	if (str == null || str.length() ==0)
	    	    return null;
		char[] c;
		c = str.toCharArray();
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		//返回拼音大小写设置
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		//返回的字符串中去除音调标记
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		//发音"驴（lu->lv）"的那个韵母用v表示
		format.setVCharType(HanyuPinyinVCharType.WITH_V);
		StringBuffer pinyin = new StringBuffer();
		for (int i = 0; i < c.length; i++) {
			// 判断是否为汉字字符函数
			if (Character.toString(c[i]).matches("[\\u4E00-\\u9FA5]+")) {
				pinyin.append(getPingYin(c[i], format));
			} else
				pinyin.append(Character.toString(c[i]).toLowerCase());
		}
		return pinyin.toString();
	}	
	
	/**
	 * 获得中文字符的拼音
	 * @param c
	 * @param format
	 * @return
	 */
	public static String getPingYin(char c,HanyuPinyinOutputFormat format) {
		String s = "";
		try {
        		String[] str = PinyinHelper.toHanyuPinyinStringArray(c, format);
        		if (str.length > 1) { //多音字处理
        			s = getMultiToneCharacter(c,format);
        		} else {
        			s = str[0];
        		}
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
		return s;
	}
	
	/**
	 * 获得多音字中常用姓的拼音
	 * @param c
	 * @param format
	 * @return
	 */	
	private static String getMultiToneCharacter(char c,HanyuPinyinOutputFormat format) {
	    	String str = "";
	        EnumValueMap multiTone = er.getEnumValueMap("MultiToneCharacter");
	        if (multiTone.getEnumList() == null)
	            return String.valueOf(c);
		try {
		    str = multiTone.getValue(String.valueOf(c));
		    if (str == null) {
			String[] pinyin = PinyinHelper.toHanyuPinyinStringArray(c, format);
    			str = pinyin[0];			
		    }
            	} catch (BadHanyuPinyinOutputFormatCombination e) {
            		e.printStackTrace();
            	}
            	return str;	    
	}
	
	//测试
	public static void main(String[] args) {
		String[] data = { venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Sun"), venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Meng"), venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Song"), venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Yin"), venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Liao"), venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Zhang"), venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Zhang"), venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Zhang"), venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Xu"), venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Queensland"),
				venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Cao"), venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Once"), venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Yi"), "tony", "sultane" };
		List al = new ArrayList();
		for (int i = 0; i < data.length; i++) {
			Map nameMap = new HashMap();
			nameMap.put("name", data[i]);
			al.add(nameMap);
		}
		Object[] objs = al.toArray();
		Arrays.sort(objs, new PinYinTools().new OrganizeComparator());
		System.out.println(Arrays.asList(objs));
	}
}

