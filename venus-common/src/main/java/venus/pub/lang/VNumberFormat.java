package venus.pub.lang;

import java.text.DecimalFormat;
import java.util.Locale;

/**
 * <p>Description:主要提供数字格式的封装功能，输入包括整型（含整型对象）、<br>
 * 浮点型以及VDouble型，输出可以是普通格式、货币格式、百分数格式等。</p>
 * 注：欧元区国家的货币表示形式统一规定该国原来的货币表示形式
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company:USE</p>
 * @author 张文韬
 * @version 1.0
 */

public class VNumberFormat {
	/**
	 * 将数值转换为标准形式的标记
	 */
	public static final int NUMBERSTYLE = 0;
	/**
	 * 将数值转换为货币形式的标记
	 */
	public static final int CURRENCYSTYLE = 1;
	/**
	 * 将数值转换为百分号形式的标记
	 */
	public static final int PERCENTSTYLE = 2;
	/**
	 * 将数值转换为科学计数的标记
	 */
	public static final int SCIENTIFICSTYLE = 3;

	/**
	 * 构造子注释
	 */
	public VNumberFormat() {
		super();
	}

	/**
	 * 以常规形式返回double型数字的字符串表达式
	 * @param value double
	 * @return 数字的常规表示形式
	 */
	public static String format(double value) {
		return format(value, NUMBERSTYLE);
	}

	/**
	 * 根据输入的转换格式返回字符串的相应表达形式
	 * @param value double
	 * @param style 转换的格式
	 * @return 数字的相应字符串表达形式
	 */
	public static String format(double value, int style) {
		return format(value, style, null);
	}

	/**
	 * 根据输入的条件将double型数字转换为符合相应转换格式和地区习惯的形式
	 * @param value double
	 * @param style 转换的格式
	 * @param locale 地区信息
	 * @return 符合要求的字符串
	 */
	public static String format(
		double value,
		int style,
		Locale locale) {
		java.text.NumberFormat form = null;
		DecimalFormat scienceForm = new DecimalFormat("0.#########E0");
		if (locale == null) {
			switch (style) {
				case NUMBERSTYLE :
					form = java.text.NumberFormat.getNumberInstance();
					break;
				case CURRENCYSTYLE :
					form = java.text.NumberFormat.getCurrencyInstance();
					break;
				case PERCENTSTYLE :
					form = java.text.NumberFormat.getPercentInstance();
					break;
				case SCIENTIFICSTYLE :
					return scienceForm.format(value);
				default :
					form = java.text.NumberFormat.getInstance();
					break;
			}
		} else {
			switch (style) {
				case NUMBERSTYLE :
					form = java.text.NumberFormat.getNumberInstance(locale);
					break;
				case CURRENCYSTYLE :
					form = java.text.NumberFormat.getCurrencyInstance(locale);
					//德国的货币统一规定为"数字 + DM"
					if (locale == Locale.GERMANY) {
						form.setMaximumFractionDigits(9);
		                String str = form.format(value);
		                str = str.substring(0,str.indexOf(" ")) + " DM";
		                return str;
					}
					//意大利货币统一为"L. +数字"
					if (locale == Locale.ITALY) {
						form.setMaximumFractionDigits(9);
		                String str = form.format(value);
		                str = "L." + str.substring(str.indexOf(" "));
		                return str;
					}
					break;
				case PERCENTSTYLE :
					form = java.text.NumberFormat.getPercentInstance(locale);
					break;
				case SCIENTIFICSTYLE :
					return scienceForm.format(value);
				default :
					form = java.text.NumberFormat.getInstance(locale);
					break;
			}
		}
		//设置小数位最长为9
		form.setMaximumFractionDigits(9);
		return form.format(value);
	}

	/**
	 * 对整形数进行格式化，默认为常规数字格式
	 * @param value 待转换的整形数
	 * @return 经过转换的数字的字符串表达形式
	 */
	public static String format(int value) {
		return format(value, NUMBERSTYLE);
	}

	/**
	 * 根据输入的内容将整形数转换为相应的形式，默认不包含地区信息
	 * @param value 待转换的整形数
	 * @param style 转换的格式
	 * @return 转换后的数字的字符串表达形式
	 */
	public static String format(int value, int style) {
		return format(value, style, null);
	}

	/**
	 * 根据输入的条件将整形数字转换为符合相应转换格式和地区习惯的形式
	 * @param value double
	 * @param style 转换的格式
	 * @param locale 地区信息
	 * @return 转换后的数字的字符串表达形式
	 */
	public static String format(
		int value,
		int style,
		Locale locale) {
		return format((long) value, style, locale);
	}

	/**
	 * 对长整形数进行格式化，默认为常规数字格式
	 * @param value long
	 * @return 经过转换的数字的字符串表达形式
	 */
	public static String format(long value) {
		return format(value, NUMBERSTYLE);
	}

	/**
	 * 根据输入的内容将长整形数转换为相应的形式，默认不包含地区信息
	 * @param value 待转换的长整形数
	 * @param style 转换的格式
	 * @return 转换后的数字的字符串表达形式
	 */
	public static String format(long value, int style) {
		return format(value, style, null);
	}

	/**
	 * 根据输入的条件将长整形数字转换为符合相应转换格式和地区习惯的形式
	 * @param value 待转换的长整形数
	 * @param style 转换的格式
	 * @param locale 地区信息
	 * @return 转换后的数字的字符串表达形式
	 */
	public static String format(
		long value,
		int style,
		Locale locale) {
		java.text.NumberFormat form = null;
		DecimalFormat scienceForm = new DecimalFormat("0.#########E0");
		if (locale == null) {
			switch (style) {
				case NUMBERSTYLE :
					form = java.text.NumberFormat.getNumberInstance();
					break;
				case CURRENCYSTYLE :
					form = java.text.NumberFormat.getCurrencyInstance();
					break;
				case PERCENTSTYLE :
					form = java.text.NumberFormat.getPercentInstance();
					break;
				case SCIENTIFICSTYLE :
					return scienceForm.format(value);
				default :
					form = java.text.NumberFormat.getInstance();
					break;
			}
		} else {
			switch (style) {
				case NUMBERSTYLE :
					form = java.text.NumberFormat.getNumberInstance(locale);
					break;
				case CURRENCYSTYLE :
					form = java.text.NumberFormat.getCurrencyInstance(locale);
					//德国的货币统一规定为"数字 + DM"
					if (locale == Locale.GERMANY) {
						//form.setMaximumFractionDigits(9);
		                String str = form.format(value);
		                str = str.substring(0,str.indexOf(" ")) + " DM";
		                return str;
					}
					//意大利货币统一为"L. +数字"
					if (locale == Locale.ITALY) {
						//form.setMaximumFractionDigits(9);
		                String str = form.format(value);
		                str = "L." + str.substring(str.indexOf(" "));
		                return str;
					}
					break;
				case PERCENTSTYLE :
					form = java.text.NumberFormat.getPercentInstance(locale);
					break;
				case SCIENTIFICSTYLE :
					return scienceForm.format(value);
				default :
					form = java.text.NumberFormat.getInstance(locale);
					break;
			}
		}
		return form.format(value);
	}

	/**
	 * 以常规形式返回Double型数字的字符串表达式
	 * @param value Double
	 * @return 数字的常规字符串表示形式
	 */
	public static String format(Double value) {
		return format(value, NUMBERSTYLE);
	}

	/**
	 * 根据输入的内容将Double型数转换为相应的形式，默认不包含地区信息
	 * @param value 待转换的Double型数
	 * @param style 转换的格式
	 * @return 转换后的数字的字符串表达形式
	 */
	public static String format(Double value, int style) {
		return format(value, style, null);
	}

	/**
	 * 根据输入的条件将Double型数字转换为符合相应转换格式和地区习惯的形式
	 * @param value 待转换的Double型数
	 * @param style 转换的格式
	 * @param locale 地区信息
	 * @return 转换后的数字的字符串表达形式
	 */
	public static String format(
		Double value,
		int style,
		Locale locale) {
		//如果对象不为空，则调用double数据转换方法进行转换，否则返回空字符串
		if (value != null)
			return format(value.doubleValue(), style, locale);
		else
			return "";
	}

	/**
	 * 以常规形式返回Integer型数字的字符串表达式
	 * @param value Integer
	 * @return 数字的常规字符串表示形式
	 */
	public static String format(Integer value) {
		return format(value, NUMBERSTYLE);
	}

	/**
	 * 根据输入的内容将Integer型数转换为相应的形式，默认不包含地区信息
	 * @param value 待转换的Integer型数
	 * @param style 转换的格式
	 * @return 转换后的数字的字符串表达形式
	 */
	public static String format(Integer value, int style) {
		return format(value, style, null);
	}

	/**
	 * 根据输入的条件将Integer型数字转换为符合相应转换格式和地区习惯的形式
	 * @param value 待转换的Integer型数
	 * @param style 转换的格式
	 * @param locale 地区信息
	 * @return 转换后的数字的字符串表达形式
	 */
	public static String format(
		Integer value,
		int style,
		Locale locale) {
		//如果对象不为空，则调用int数据转换方法进行转换，否则返回空字符串
		if (value != null)
			return format(value.intValue(), style, locale);
		else
			return "";
	}

	/**
	 * 以常规形式返回Long型数字的字符串表达式
	 * @param value Long
	 * @return 数字的常规字符串表示形式
	 */
	public static String format(Long value) {
		return format(value, NUMBERSTYLE);
	}

	/**
	 * 根据输入的内容将Long型数转换为相应的形式，默认不包含地区信息
	 * @param value 待转换的Long型数
	 * @param style 转换的格式
	 * @return 转换后的数字的字符串表达形式
	 */
	public static String format(Long value, int style) {
		return format(value, style, null);
	}

	/**
	 * 根据输入的条件将Long型数字转换为符合相应转换格式和地区习惯的形式
	 * @param value 待转换的Long型数
	 * @param style 转换的格式
	 * @param locale 地区信息
	 * @return 转换后的数字的字符串表达形式
	 */
	public static String format(
		Long value,
		int style,
		Locale locale) {
		//如果对象不为空，则调用long数据转换方法进行转换，否则返回空字符串
		if (value != null)
			return format(value.longValue(), style, locale);
		else
			return "";
	}

	/**
	 * 以常规形式返回VDouble型数字的字符串表达式
	 * @param value VDouble
	 * @return 数字的常规字符串表示形式
	 */
	public static String format(VDouble value) {
		return format(value, NUMBERSTYLE);
	}

	/**
	 * 根据输入的内容将VDouble型数转换为相应的形式，默认不包含地区信息
	 * @param value 待转换的VDouble型数
	 * @param style 转换的格式
	 * @return 转换后的数字的字符串表达形式
	 */
	public static String format(VDouble value, int style) {
		return format(value, style, null);
	}

	/**
	 * 根据输入的条件将VDouble型数字转换为符合相应转换格式和地区习惯的形式
	 * @param value 待转换的VDouble型数
	 * @param style 转换的格式
	 * @param locale 地区信息
	 * @return 转换后的数字的字符串表达形式
	 */
	public static String format(
		VDouble value,
		int style,
		Locale locale) {
		//如果对象不为空，则调用double数据转换方法进行转换，否则返回空字符串
		if (value != null)
			return format(value.doubleValue(), style, locale);
		else
			return "";
	}
	
	/**
	 * 测试程序
	 */
//	public static void main(String[] args) {
//		long ml = 123456789l;
//		VNumberFormat numberFormat = new VNumberFormat();
//		System.out.println(numberFormat.format(ml, 1, Locale.GERMANY));	
//		Currency cur = Currency.getInstance(Locale.GERMANY);
//		NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.GERMANY).setCurrency(cur);
//		System.out.println(nf.format(ml));
//		System.out.println(cur.getSymbol());
//		System.out.println(cur.getCurrencyCode());
//		System.out.println(cur.getDefaultFractionDigits());
//	}

}