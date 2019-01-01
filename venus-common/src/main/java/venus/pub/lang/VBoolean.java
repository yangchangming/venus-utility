package venus.pub.lang;

import java.io.Serializable;

/**
 * <p>Description：主要提供布尔类型的方法封装。 </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: USE </p>
 * @author 张文韬
 * @version 1.0
 */

public class VBoolean implements Serializable {
	private boolean value = false;
	//以下为构造函数
	/**
	 * 按布尔型构造VBoolean。
	 * @param b 布尔型变量
	 */
	public VBoolean(boolean b) {
		super();
		value = b;
	}

	/**
	 * 按字符型构造VBoolean,输入参数为'Y'或'y'时为true，
	 * 其他情况均为false。
	 * @param ch 字符型变量
	 */
	public VBoolean(char ch) {
		super();
		if (ch == 'Y' || ch == 'y') {
			value = true;
		} else
			value = false;
	}

	/**
	 * 按字符串型构造VBoolean，"Y","y"或者"true"(忽略大小写)为true,其他为false。
	 * @param str 字符串型变量
	 */
	public VBoolean(String str) {
		if (str!=null&&str.length() > 0
			&& (str.equalsIgnoreCase("true")
				|| str.charAt(0) == 'y'
				|| str.charAt(0) == 'Y')) {
			value = true;
		} else
			value = false;
	}
	/**
	 * 返回对象的布尔型值
	 * @return value 布尔型值
	 */
	public boolean booleanValue() {
		return value;
	}
	/**
	 * 生成接受方的散列代码，true返回1231，false返回1237。
	 * @return int 得到的散列代码值
	 */
	public int hashCode() {
		return value ? 1231 : 1237;
	}

	/**
	 * 比较两对象包含的value值是否相等,相等返回true,反之返回false。
	 * @param o 待比较的对象
	 * @return 布尔型值 两对象包含的value值相等返回true,反之返回false
	 */
	public boolean equals(Object o) {
		if (o != null
			&& (o instanceof VBoolean)
			&& (value == ((VBoolean) o).booleanValue())) {
			return true;
		} else
			return false;
	}

	/**
	 * 返回该对象的String型值，true为”Y”； false为”N”
	 * @param null
	 * @return 字符串"Y"或"N":true为”Y”、false为”N”
	 */
	public String toString() {
		return value ? "Y" : "N";
	}

}
