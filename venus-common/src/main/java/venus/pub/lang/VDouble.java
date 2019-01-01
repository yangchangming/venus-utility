package venus.pub.lang;

import java.math.BigDecimal;

/**
 * <p>Description:对浮点数的计算进行统一处理，支持科学计数法。<br>
 *    可以进行加减乘除以及取绝对值等操作；
 *    目前该算法可最大支持45位有效数字的计算；其中最大支持36位整数、8位小数。<br>
 *    加减乘除运算结果最大可精确到小数点后九位（指定精度超出9时，按照精度为9进行处理），
 * 	  默认精度为小数点后八位。<br>
 *    进位模式建议采用ROUND_UP、ROUND_DOWN、ROUND_CEILING、ROUND_FLOOR以及ROUND_HALF_UP，
 *    不建议采用其他进位模式。<br>
 * </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: USE</p>
 * @author 张文韬
 * @version 1.0
 */

public class VDouble extends Number implements java.io.Serializable {

	/**
	 * 默认权值为-8
	 */
	public static final int DEFAULT_POWER = -8;
	/**
	 * power用来表示小数的位数（也可以说是小数的精度、权值），与整数位没有关系
	 */
	private int power = DEFAULT_POWER;

	/**
	 * 判断小数末尾的0是否需要去掉
	 */
	private boolean trimZero = false;

	/**
	 * 进位模式，远离零，计算值只会增加不会减小
	 */
	public final static int ROUND_UP = 0;

	/**
	 * 舍位模式，向零靠近，计算值只会减小不会增加
	 */
	public final static int ROUND_DOWN = 1;

	/**
	 * 向正无穷舍入的模式，如果小数为正，则按照向上进位方式（即远离零）操作；反之，如果为负
	 * 则按照向零靠近的原则操作。注意这种方式只会使计算值增加而不会使其减小
	 */
	public final static int ROUND_CEILING = 2;

	/**
	 * 向负无穷舍入的模式，如果小数为正，则按照向零靠近操作；反之，如果为负
	 * 则按照远离零的原则操作。注意这种方式只会使计算值减小而不会使其增加
	 */
	public final static int ROUND_FLOOR = 3;

	/**
	 * 典型四舍五入的模式
	 */
	public final static int ROUND_HALF_UP = 4;

	/**
	 * 四舍五入模式，需要注意的是与相邻的数距离相等时采用舍位模式
	 */
	public final static int ROUND_HALF_DOWN = 5;

	/**
	 * 四舍五入模式，需要注意的是与相邻的数距离相等时采用向偶数端靠近的原则
	 * 这样可以减小迭代计算时的误差
	 */
	public final static int ROUND_HALF_EVEN = 6;

	/**
	 * 这种模式下不采用任何舍入方式，要求取得精确计算结果，如果采用了舍入模式，那么
	 * 系统将会抛出异常<tt>ArithmeticException</tt>
	 */
	public final static int ROUND_UNNECESSARY = 7;

	//初始化对象
	private static VDouble ONE;

	//定义存放数据的数组长度为5，每个数组单元可放9位数字；即表示的数据可以是(5*9)位
	private static final int ARRAY_LENGTH = 5;
	private static final int EFFICIENCY_SEATE = 9;
	//掩码 (long) Math.pow(10, EFFICIENCY_SEATE); 注意要和上面的EFFICIENCY_SEATE同步更改
	private final static long MAX_ONELONG_VALUE = (long) 1E9;

	//用于存放小数位四舍五入时的掩码，取值从10的0次方到10的9次方
	private static final long POWER_ARRAY[];

	//表示数据的正负，1为正，-1为负
	private byte si = 1;

	//用于存放数据的数组
	private long v[] = new long[ARRAY_LENGTH];

	/**
	 * 确保调用构造函数之前对下列内容进行初始化
	 */
	static {
		POWER_ARRAY = new long[EFFICIENCY_SEATE + 2];
		//数组中依次存放10的9、8、7、6、5...0次方以及0；
		for (int i = 0; i < POWER_ARRAY.length - 1; i++) {
			POWER_ARRAY[i] = (long) Math.pow(10, EFFICIENCY_SEATE - i);
		}
		//将数组的最后一位赋值为0
		POWER_ARRAY[POWER_ARRAY.length - 1] = 0;
		//初始化ONE
		ONE = new VDouble(1.0);
	}

	/**
	 * 构造函数
	 */
	public VDouble() {
		super();
	}

	/**
	 * 利用浮点数进行构造，默认调用VDouble(double,int);
	 * @param d 待初始化的浮点数
	 * @throws NumberFormatException
	 */
	public VDouble(double d) throws NumberFormatException {
		this(d, DEFAULT_POWER);
	}

	/**
	 * 利用Double型数据进行构造，默认调用VDouble(double,int);
	 * @param d 待初始化的Double型数据
	 * @throws NumberFormatException
	 */
	public VDouble(Double d) throws NumberFormatException {
		this(d.doubleValue(), DEFAULT_POWER);
	}

	/**
	 * 构造函数，给一个浮点数d指定小数的有效位数newPower;
	 * 除了利用String型变量作为参数的构造函数外，其他构造函数最终都要调用该构造函数
	 * @param d 待初始化的浮点数
	 * @param newPower 指定浮点数精度，即保留几位小数
	 * @throws NumberFormatException
	 */
	public VDouble(double d, int newPower) throws NumberFormatException {
		setValue(d, newPower);
	}
	/**
	 * 构造函数，默认调用VDouble(long d);
	 * @param d 整型参数
	 * @throws NumberFormatException
	 */
	public VDouble(int d) throws NumberFormatException {
		this((long) d);
	}

	/**
	 * 构造函数，默认调用VDouble(long,int)
	 * @param d 整型参数
	 * @param newPower 指定小数的精度
	 * @throws NumberFormatException
	 */
	public VDouble(int d, int newPower) throws NumberFormatException {
		this((long) d, newPower);
	}

	/**
	 * 构造函数，默认调用VDouble(long,DEFAULT_POWER)
	 * @param d 长整型参数
	 * @throws NumberFormatException
	 */
	public VDouble(long d) throws NumberFormatException {
		this(d, DEFAULT_POWER);
	}

	/**
	 * 构造函数，默认调用VDouble(double,int);
	 * @param d 长整型参数
	 * @param newPower 指定小数的有效位数，即精度
	 * @throws NumberFormatException
	 */
	public VDouble(long d, int newPower) throws NumberFormatException {
		this(d + 0.0, newPower);
	}

	/**
	 * 构造函数，利用字符串作为参数，默认小数的精度不超过DEFAULT_POWER位
	 * @param str 字符串型参数
	 * @throws NumberFormatException
	 */
	public VDouble(String str) throws NumberFormatException {
		this(str, DEFAULT_POWER);
		//		//暂存字符串
		//		String s = "";
		//		if (str == null || str.trim().length() == 0)
		//			s = "0";
		//		else {
		//			//如果字符串中有","则将其去掉
		//			java.util.StringTokenizer token =
		//				new java.util.StringTokenizer(str, ",");
		//			while (token.hasMoreElements()) {
		//				s += token.nextElement().toString();
		//			}
		//			//判断是否为科学计数法，如果是直接利用Double提供的方法转换为double类型
		//			if (s.indexOf('e') >= 0 || s.indexOf('E') >= 0) {
		//				setValue(Double.parseDouble(s), -8);
		//				return;
		//			}
		//			//判断字符串代表的是正数还是负数
		//			if (s.charAt(0) == '-') {
		//				si = -1;
		//				s = s.substring(1);
		//			} else if (s.charAt(0) == '+')
		//				s = s.substring(1);
		//		}
		//		int loc = s.indexOf('.');
		//		//如果为小数，则分别提取小数部分和整数部分
		//		if (loc >= 0) {
		//			//s1存储小数部分
		//			String s1 = s.substring(loc + 1);
		//			if (s1.length() > -DEFAULT_POWER) {
		//				if (-DEFAULT_POWER >= EFFICIENCY_SEATE)
		//					s1 = s1.substring(0, EFFICIENCY_SEATE);
		//				else
		//					s1 = s1.substring(0, 1 - DEFAULT_POWER);
		//			}
		//			//取得小数的权值
		//			power = -s1.length();
		//			//power为负数，当比有效位数小时需要以有效位为准
		//			//将小数的位数调整为有效位数的长度
		//			if (power < -EFFICIENCY_SEATE) {
		//				power = -EFFICIENCY_SEATE;
		//				s1 = s.substring(loc + 1, EFFICIENCY_SEATE + 1 + loc);
		//			} else {
		//				for (int i = s1.length(); i < EFFICIENCY_SEATE; i++)
		//					s1 += "0";
		//			}
		//			//v[0]用于存储小数部分
		//			v[0] = Long.parseLong(s1);
		//			//取得整数
		//			s = s.substring(0, loc);
		//		} else {
		//			power = 0;
		//			v[0] = 0;
		//		}
		//		/** 现在只剩下整数部分，需要进行计算 */
		//		int len = s.length();
		//		int sitLoc = 1;
		//		//将整数按照有效位拆分后从低位到高位分别存储在v[1]到v[5]中
		//		while (len > 0) {
		//			String s1 = "";
		//			if (len > EFFICIENCY_SEATE) {
		//				s1 = s.substring(len - EFFICIENCY_SEATE);
		//				s = s.substring(0, len - EFFICIENCY_SEATE);
		//			} else {
		//				s1 = s;
		//				s = "";
		//			}
		//			len = s.length();
		//			v[sitLoc++] = Long.parseLong(s1);
		//		}
		//		for (int i = sitLoc; i < v.length; i++)
		//			v[i] = 0;
		//		//按照默认精度对小数位进行处理
		//		round(ROUND_HALF_UP);
	}

	/**
	 * 构造函数，利用字符串作为参数，指定小数的精度为newPower位
	 * @param str 字符串型参数
	 * @param newPower 指定的小数的有效位数
	 * @throws NumberFormatException
	 */
	public VDouble(String str, int newPower) throws NumberFormatException {
		//对精度newPower进行处理，确保取值范围在0到-8之间
		if (newPower > 0) {
			newPower = -newPower;
		}
		newPower = newPower > -8 ? newPower : -8;

		//暂存字符串
		String s = "";
		if (str == null || str.trim().length() == 0)
			s = "0";
		else {
			//如果字符串中有","则将其去掉
			java.util.StringTokenizer token =
				new java.util.StringTokenizer(str, ",");
			while (token.hasMoreElements()) {
				s += token.nextElement().toString();
			}
			//判断是否为科学计数法，如果是直接利用Double提供的方法转换为double类型
			if (s.indexOf('e') >= 0 || s.indexOf('E') >= 0) {
				setValue(Double.parseDouble(s), -newPower);
				return;
			}
			//判断字符串代表的是正数还是负数
			if (s.charAt(0) == '-') {
				si = -1;
				s = s.substring(1);
			} else if (s.charAt(0) == '+')
				s = s.substring(1);
		}
		int loc = s.indexOf('.');
		//如果为小数，则分别提取小数部分和整数部分
		if (loc >= 0) {
			//s1存储小数部分
			String s1 = s.substring(loc + 1);
			if (s1.length() > -newPower) {
				if (-DEFAULT_POWER >= EFFICIENCY_SEATE)
					s1 = s1.substring(0, EFFICIENCY_SEATE);
				else
					s1 = s1.substring(0, 1 - newPower);
			}
			//取得小数的权值
			power = newPower;
			//power为负数，当小于有效位数时需要以有效位为准
			//将小数的位数调整为有效位数的长度
			for (int i = s1.length(); i < EFFICIENCY_SEATE; i++)
				s1 += "0";
			//v[0]用于存储小数部分
			v[0] = Long.parseLong(s1);
			//取得整数
			s = s.substring(0, loc);
		} else {
			power = newPower;
			v[0] = 0;
		}
		/** 现在只剩下整数部分，需要进行计算 */
		int len = s.length();
		int sitLoc = 1;
		//将整数按照有效位拆分后从低位到高位分别存储在v[1]到v[5]中
		while (len > 0) {
			String s1 = "";
			if (len > EFFICIENCY_SEATE) {
				s1 = s.substring(len - EFFICIENCY_SEATE);
				s = s.substring(0, len - EFFICIENCY_SEATE);
			} else {
				s1 = s;
				s = "";
			}
			len = s.length();
			v[sitLoc++] = Long.parseLong(s1);
		}
		for (int i = sitLoc; i < v.length; i++)
			v[i] = 0;
		//按照newPower指定的精度对小数位进行处理
		round(ROUND_HALF_UP);
	}

	/**
	 * 构造函数，利用字符串作为参数，指定小数的精度为newPower位
	 * @param str 字符串型参数
	 * @param newPower 指定的小数的有效位数
	 * @param roundingMode 进位模式
	 * @throws NumberFormatException
	 */
	public VDouble(String str, int newPower,int roundingMode) throws NumberFormatException {
		//对精度newPower进行处理，确保取值范围在0到-8之间
		if (newPower > 0) {
			newPower = -newPower;
		}
		newPower = newPower > -8 ? newPower : -8;

		//暂存字符串
		String s = "";
		if (str == null || str.trim().length() == 0)
			s = "0";
		else {
			//如果字符串中有","则将其去掉
			java.util.StringTokenizer token =
				new java.util.StringTokenizer(str, ",");
			while (token.hasMoreElements()) {
				s += token.nextElement().toString();
			}
			//判断是否为科学计数法，如果是直接利用Double提供的方法转换为double类型
			if (s.indexOf('e') >= 0 || s.indexOf('E') >= 0) {
				setValue(Double.parseDouble(s), -newPower);
				return;
			}
			//判断字符串代表的是正数还是负数
			if (s.charAt(0) == '-') {
				si = -1;
				s = s.substring(1);
			} else if (s.charAt(0) == '+')
				s = s.substring(1);
		}
		int loc = s.indexOf('.');
		//如果为小数，则分别提取小数部分和整数部分
		if (loc >= 0) {
			//s1存储小数部分
			String s1 = s.substring(loc + 1);
			if (s1.length() > -newPower) {
				if (-DEFAULT_POWER >= EFFICIENCY_SEATE)
					s1 = s1.substring(0, EFFICIENCY_SEATE);
				else
					s1 = s1.substring(0, 1 - newPower);
			}
			//取得小数的权值
			power = newPower;
			//power为负数，当小于有效位数时需要以有效位为准
			//将小数的位数调整为有效位数的长度
			for (int i = s1.length(); i < EFFICIENCY_SEATE; i++)
				s1 += "0";
			//v[0]用于存储小数部分
			v[0] = Long.parseLong(s1);
			//取得整数
			s = s.substring(0, loc);
		} else {
			power = newPower;
			v[0] = 0;
		}
		/** 现在只剩下整数部分，需要进行计算 */
		int len = s.length();
		int sitLoc = 1;
		//将整数按照有效位拆分后从低位到高位分别存储在v[1]到v[5]中
		while (len > 0) {
			String s1 = "";
			if (len > EFFICIENCY_SEATE) {
				s1 = s.substring(len - EFFICIENCY_SEATE);
				s = s.substring(0, len - EFFICIENCY_SEATE);
			} else {
				s1 = s;
				s = "";
			}
			len = s.length();
			v[sitLoc++] = Long.parseLong(s1);
		}
		for (int i = sitLoc; i < v.length; i++)
			v[i] = 0;
		//按照newPower指定的精度对小数位进行处理
		round(roundingMode);
	}
	
	/**
	 * 利用BigDecimal类型数据作参数对VDouble进行构造
	 * @param value BigDecimal类型参数
	 */
	public VDouble(BigDecimal value) {
		this(value.doubleValue());
	}

	/**
	 * 利用VDouble作参数对进行构造
	 * @param vd VDouble类型参数
	 */
	public VDouble(VDouble vd) {
		si = vd.si;
		for (int i = 0; i < v.length; i++) {
			v[i] = vd.v[i];
		}
		power = vd.power;
	}

	/**
	 * 进行加法操作，结果默认采用四舍五入模式
	 * @param d1 double型的加数
	 * @return VDouble对象
	 * @throws LangException
	 */
	public VDouble add(double d1) throws LangException {
		return add(new VDouble(d1));
	}

	/**
	 * 进行加法操作，结果默认采用四舍五入模式
	 * @param ufd 以一个VDouble对象作为加数
	 * @return VDouble对象
	 * @throws LangException
	 */
	public VDouble add(VDouble ufd) throws LangException {
		return add(ufd, DEFAULT_POWER, ROUND_HALF_UP);
	}

	/**
	 * 进行加法操作，结果默认采用四舍五入模式
	 * @param ufd 以一个VDouble对象作为加数
	 * @param newPower 指定小数的精度
	 * @return VDouble对象
	 * @throws LangException
	 */
	public VDouble add(VDouble ufd, int newPower) throws LangException {
		return add(ufd, newPower, ROUND_HALF_UP);
	}

	/**
	 * 进行加法操作
	 * @param ufd 以一个VDouble对象作为加数
	 * @param newPower 指定小数的精度
	 * @param roundingMode 指定进位模式
	 * @return VDouble对象
	 * @throws LangException
	 */
	public VDouble add(VDouble ufd, int newPower, int roundingMode) throws LangException {
		//对精度newPower进行处理，确保取值范围在0到-8之间
		if (newPower > 0) {
			newPower = -newPower;
		}
		newPower = newPower > -8 ? newPower : -8;
		//创建一个新的对象
		VDouble vdnew = new VDouble(this);
		//使二者的精度一样，便于进行计算
		vdnew.power = newPower;
		//进行具体操作
		vdnew.addUp0(ufd, newPower, roundingMode);
		return vdnew;
	}

	/**
	 * 执行具体的加法操作，默认调用addUp0(VDouble,power,ROUND_HALF_UP)
	 * @param ufd double型的加数
	 * @throws LangException
	 */
	private void addUp0(double ufd) throws LangException {
		addUp0(new VDouble(ufd), power, ROUND_HALF_UP);
	}

	/**
	 * 执行具体的加法操作
	 * @param vd VDoule型加数
	 * @param newPower 指定精度
	 * @param roundingMode 进位方式
	 * @throws LangException
	 */
	private void addUp0(VDouble vd, int newPower, int roundingMode) throws LangException {
		/** 首先判断POWER的大小 */
		//power = newPower > 0 ? 0 : ((newPower >= -9) ? newPower : -9);
		//为数组v中的数据添加符号位，正数不变，若为负数将v中的所有数据变反
		//对精度newPower进行处理，确保取值范围在0到-8之间
		if (newPower > 0) {
			newPower = -newPower;
		}
		newPower = newPower > -8 ? newPower : -8;
		toPlus();
		vd.toPlus();
		//进行加法操作
		for (int i = 0; i < v.length; i++) {
			v[i] += vd.v[i];
		}
		//判断结果是否为负数并进行调整
		judgeNegative();
		//调整进位
		adjustIncludeFs();
		if (String.valueOf(v[4]).length() > 9) {
			throw new LangException("数值越界！");
		}
		/** 将toPlus对 vd 进行的符号变化调整回来 */
		vd.judgeNegative();
		//四舍五入
		round(roundingMode);
	}

	/**
	 * 进行减法操作，默认调用sub(VDouble,DEFAULT_POWER,ROUND_HALF_UP)
	 * @param d double型减数
	 * @return VDouble对象
	 * @throws LangException
	 */
	public VDouble sub(double d) throws LangException {
		VDouble vd = new VDouble(d);
		return sub(vd, DEFAULT_POWER, ROUND_HALF_UP);
	}

	/**
	 * 进行减法操作，默认调用sub(VDouble,DEFAULT_POWER,ROUND_HALF_UP)
	 * @param vd VDouble型减数
	 * @return VDouble对象
	 * @throws LangException
	 */
	public VDouble sub(VDouble vd) throws LangException {
		return sub(vd, DEFAULT_POWER, ROUND_HALF_UP);
	}

	/**
	 * 进行减法操作，默认的进位方式为典型四舍五入模式
	 * @param vd VDouble型减数
	 * @param newPower 指定的小数精度
	 * @return VDouble对象
	 * @throws LangException
	 */
	public VDouble sub(VDouble vd, int newPower) throws LangException {
		return sub(vd, newPower, ROUND_HALF_UP);
	}

	/**
	 * 减法操作的核心，执行具体的操作
	 * @param vd VDouble型减数
	 * @param newPower 指定的小数精度
	 * @param roundingMode 指定的进位模式
	 * @return 相减的结果，VDouble对象
	 * @throws LangException
	 */
	public VDouble sub(VDouble vd, int newPower, int roundingMode) throws LangException {
		//对精度newPower进行处理，确保取值范围在0到-8之间
		if (newPower > 0) {
			newPower = -newPower;
		}
		newPower = newPower > -8 ? newPower : -8;
		//转换vd的符号
		VDouble vdnew = new VDouble(vd);
		vdnew.si = (byte) - vd.si;
		//加上vd的相反数，返回
		return add(vdnew, newPower, roundingMode);
	}

	/**
	 * 进行乘法操作，默认调用multiply(VDouble, DEFAULT_POWER, ROUND_HALF_UP)
	 * @param d1 double型乘数
	 * @return 结果以VDouble对象的形式输出
	 * @throws LangException
	 */
	public VDouble multiply(double d1) throws LangException {
		VDouble vd1 = new VDouble(d1);
		return multiply(vd1, DEFAULT_POWER, ROUND_HALF_UP);
	}

	/**
	 * 进行乘法操作，默认调用multiply(VDouble, DEFAULT_POWER, ROUND_HALF_UP)
	 * @param vd 以一个VDouble对象作为乘数
	 * @return 结果以VDouble对象的形式输出
	 * @throws LangException
	 */
	public VDouble multiply(VDouble vd) throws LangException {
		return multiply(vd, DEFAULT_POWER, ROUND_HALF_UP);
	}

	/**
	 * 进行乘法操作，默认调用multiply(VDouble, newPower, ROUND_HALF_UP)
	 * @param vd 以一个VDouble对象作为乘数
	 * @param newPower 指定小数精度
	 * @return 结果以VDouble对象的形式输出
	 * @throws LangException
	 */
	public VDouble multiply(VDouble vd, int newPower) throws LangException {
		return multiply(vd, newPower, ROUND_HALF_UP);
	}

	/**
	 * 具体执行乘法操作，其他乘法最终都要调用该方法
	 * @param vd 以一个VDouble对象作为乘数
	 * @param newPower 指定小数精度
	 * @param roundingMode 进位方式
	 * @return 结果以VDouble对象的形式给出
	 * @throws LangException
	 */
	public VDouble multiply(VDouble vd, int newPower, int roundingMode) throws LangException {
		//对精度newPower进行处理，确保取值范围在0到-8之间
		if (newPower > 0) {
			newPower = -newPower;
		}
		newPower = newPower > -8 ? newPower : -8;

		long mv[] = new long[ARRAY_LENGTH * 2 + 1];
		for (int i = 0; i < mv.length; i++) {
			mv[i] = 0;
		}
		/**
		 * 算法的核心，“取值-进位”，各个对应位上的数字在做完乘法操作后只进行初步的
		 * 进位调整，最终的数据的进位调整由round()方法完成。
		 */
		for (int i = 0; i < v.length; i++) {
			for (int j = 0; j < v.length; j++) {
				long l = v[i] * vd.v[j];
				mv[i + j] += l % MAX_ONELONG_VALUE;
				mv[i + j + 1] += l / MAX_ONELONG_VALUE;
			}
		}
		VDouble vdnew = new VDouble();
		vdnew.power = newPower;
		//保证两者相乘所得结果的符号正确
		vdnew.si = this.si;
		vdnew.si = (byte) (vdnew.si * vd.si);
		//由于两数相乘以后小数的位数将变为相乘两数小数位数的和(这里为原数据的两倍)，所以mv[0]
		//以及mv[1]存储的都是小数，为了精度的需要，我们舍弃超出8位小数的部分，即mv[0]。
		for (int i = 0; i < v.length; i++) {
			vdnew.v[i] = mv[i + 1];
		}
		if (String.valueOf(vdnew.v[4]).length() > 9 || mv[6] != 0) {
			throw new LangException("数值越界");
		}
		vdnew.round(roundingMode);
		return vdnew;
	}

	/**
	 * 进行除法操作
	 * @param d double型除数
	 * @return 结果的VDouble实例
	 * @throws LangException
	 */
	public VDouble div(double d) throws LangException {
		VDouble vd = new VDouble(d);
		return this.div(vd, DEFAULT_POWER, ROUND_HALF_UP);
		//		double dl = getDouble() / d;
		//		VDouble vd = new VDouble(dl, DEFAULT_POWER);
		//		vd.round(ROUND_HALF_UP);
		//		return vd;
	}

	/**
	 * 进行除法操作
	 * @param vd VDouble型除数
	 * @return 结果的VDouble实例
	 * @throws LangException
	 */
	public VDouble div(VDouble vd) throws LangException {
		return this.div(vd, DEFAULT_POWER, ROUND_HALF_UP);
		//		double dl = getDouble() / vd.getDouble();
		//		VDouble vdnew = new VDouble(dl, DEFAULT_POWER);
		//		vdnew.round(ROUND_HALF_UP);
		//		return vdnew;
	}

	/**
	 * 除法操作
	 * @param vd VDouble型除数
	 * @param newPower 指定的小数精度
	 * @return 结果的VDouble实例
	 * @throws LangException
	 */
	public VDouble div(VDouble vd, int newPower) throws LangException {
		return this.div(vd, newPower, ROUND_HALF_UP);
		//		//对精度newPower进行处理，确保取值范围在0到-9之间
		//		if (newPower > 0) {
		//			newPower = -newPower;
		//		}
		//		newPower = newPower > -9 ? newPower : -9;
		//		//进行除法操作
		//		double dl = getDouble() / vd.getDouble();
		//		VDouble vdnew = new VDouble(dl, newPower);
		//		vdnew.round(ROUND_HALF_UP);
		//		return vdnew;
	}

	/**
	 * 除法操作
	 * @param vd VDouble型除数
	 * @param newPower 指定的小数精度
	 * @param roundingMode 指定的进位模式
	 * @return 结果的VDouble实例
	 * @throws LangException
	 */
	public VDouble div(VDouble vd, int newPower, int roundingMode) throws LangException {
		//对精度newPower进行处理，确保取值范围在0到-8之间
		if (newPower > 0) {
			newPower = -newPower;
		}
		newPower = newPower > -8 ? newPower : -8;
		//进行除法操作
		BigDecimal bigDec =
			this.toBigDecimal().divide(vd.toBigDecimal(), 9, ROUND_HALF_UP);
		VDouble vdnew = new VDouble(bigDec.toString(), newPower,roundingMode);
		if (String.valueOf(vdnew.v[4]).length() > 9) {
			throw new LangException("数值越界");
		}
		//vdnew.round(roundingMode);
		return vdnew;
		//		double dl = getDouble() / vd.getDouble();
		//		VDouble vdnew = new VDouble(dl, newPower);
		//		vdnew.round(roundingMode);
		//		return vdnew;
	}

	/**
	 * 计算一组数据，每一步进行ROUND计算
	 * @param dArray double型数组
	 * @return VDouble对象
	 * @throws LangException
	 */
	public static VDouble sum(double[] dArray) throws LangException {
		return sum(dArray, DEFAULT_POWER);
	}

	/**
	 * 计算一组数据，每一步进行ROUND计算
	 * @param dArray double型数组
	 * @param newPower 指定小数的精度
	 * @return VDouble对象
	 * @throws LangException
	 */
	public static VDouble sum(double[] dArray, int newPower) throws LangException {
		//如果dArray为空，返回null
		if (dArray == null) {
			return null;
		}
		//对精度newPower进行处理，确保取值范围在0到-8之间
		if (newPower > 0) {
			newPower = -newPower;
		}
		newPower = newPower > -8 ? newPower : -8;

		VDouble vd = new VDouble(0, newPower);
		for (int i = 0; i < dArray.length; i++) {
			vd.addUp0(dArray[i]);
		}
		return vd;
	}

	/**
	 * 计算一组数据，每一步进行ROUND计算
	 * @param dArray double型数组
	 * @param newPower 指定小数的精度
	 * @param roundingMode 指定的进位模式
	 * @return VDouble对象
	 * @throws LangException
	 */
	public static VDouble sum (
		double[] dArray,
		int newPower,
		int roundingMode) throws LangException {
		//如果dArray为空，返回null
		if (dArray == null) {
			return null;
		}
		//对精度newPower进行处理，确保取值范围在0到-8之间
		if (newPower > 0) {
			newPower = -newPower;
		}
		newPower = newPower > -8 ? newPower : -8;

		VDouble vd = new VDouble(0, newPower);
		for (int i = 0; i < dArray.length; i++) {
			VDouble vdNew = new VDouble(dArray[i], newPower);
			vd.addUp0(vdNew, newPower, roundingMode);
		}
		return vd;
	}

	/**
	 * 取得指定精度和进位模式的该对象的值
	 * @param power 指定小数位数(精度)
	 * @param roundingMode 进位模式
	 * @return 参数指定的精度和进位模式的该对象的值
	 */
	public VDouble setScale(int power, int roundingMode) {
		//乘上VDouble(1.0)
		try {
		return multiply(ONE, power, roundingMode);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 初始化一个对象，用于处理以double型数据为参数的构造函数
	 * 将参数d拆分后分别存在v[0]到v[5]中；
	 * @param d 待处理的数据
	 * @param newPower 小数的精度
	 */
	private void setValue(double d, int newPower) {
		//将d转换为正数处理
		if (d < 0) {
			d = -d;
			si = -1;
		}
		//确保小数的精度power在0到-8之间
		if (newPower > 0) {
			newPower = -newPower;
		}
		power = newPower > -8 ? newPower : -8;
		/**这里统一使用小数点后九位而不管power的值是多少，具体的精度控制在round()方法中完成*/
		//取得d的小数部分
		double dxs = d % 1;
		//将d的小数位去掉
		d -= dxs;
		/**处理整数部分*/
		//将数据从低位到高位每九位为一段依次放入数组v中，当整数位数小于9时，只有v[1]有效
		//其他项实际上为垃圾项，值均为0
		for (int i = 1; i < v.length; i++) {
			v[i] = (long) (d % MAX_ONELONG_VALUE);
			d = d / MAX_ONELONG_VALUE;
		}
		/**处理小数部分*/
		long v2 = 0;
		if (dxs == 0.0)
			v2 = (long) (dxs * MAX_ONELONG_VALUE);
		else
			v2 = (long) ((dxs + 0.00000000001) * MAX_ONELONG_VALUE);
		v[0] = v2;
		//四舍五入，power将在round()方法中方显“英雄本色”
		round(ROUND_HALF_UP);
	}

	/**
	 * 按照当前的power，去掉小数多余的部分
	 * 比如999999.99123456 power = -2
	 * result is 999999.990000000
	 * 又如1.325555555 power = -2
	 * 结果为：1.33000000（如果采用四舍五入模式的话）
	 * 需要对各个进位方式进行考查
	 * @param roundingMode 进位模式
	 */
	private void round(int roundingMode) {
		//默认为典型四舍五入方式
		boolean increment = true;
		//根据roundingMode的值判断采取何种进位方式
		switch (roundingMode) {
			case ROUND_UP :
				increment = true;
				break;
			case ROUND_CEILING :
				increment = (si == 1);
				break;
			case ROUND_FLOOR :
				increment = (si == -1);
				break;
			case ROUND_DOWN :
				increment = false;
				break;
			default :
				break;
		}
		int p = -power;
		//10的(9-(p+1))次方，取得对小数部分实行四舍五入的权值
		long vxs = POWER_ARRAY[p + 1];
		//处理进位模式
		StringBuffer strBuf = new StringBuffer();
		double d = (double) v[0] / MAX_ONELONG_VALUE;
		strBuf.append(String.valueOf(d));
		while (strBuf.length() < 11) {
			strBuf.append(0);
		}
		String tempStr = strBuf.toString();
		/** 作内部运算 */
		if (increment
			&& (roundingMode == ROUND_UP
				|| roundingMode == ROUND_CEILING
				|| roundingMode == ROUND_FLOOR) && (Integer.valueOf(tempStr.substring(p + 2)).intValue() != 0)) {
			//采取进位操作
			v[0] += vxs * 10;
			//调整进位
			adjustNotIncludeFs();
		} else if (increment) {
			//对小数的（power+1）位加5，从而实现四舍五入
			//例如v[0]=12666，如果想保留两位小数，那么就执行v[0]+500,对第三位小数采取四舍五入
			v[0] += vxs * 5;
			//调整进位
			adjustNotIncludeFs();
		}
		//调整进位
		adjustNotIncludeFs();
		//调整小数，去掉小数power以后的位
		cutdown();
		//为0时去掉负号
		boolean isZero = true;
		for (int i = 0; i < v.length; i++) {
			if (v[i] != 0) {
				isZero = false;
				break;
			}
		}
		if (si == -1 && isZero)
			si = 1;
	}

	/**
	 * 对小数进行舍入操作，小数位按照newPower规定的有效位数补零。<br>
	 * 例如在典型四舍五入模式下，round(1.55555,-2,ROUND_HALF_UP)的结果应为2.00
	 * @return VDouble
	 * @param d double
	 * @param roundingMode 进位模式
	 * @param newPower 小数精度
	 */
	private VDouble round(double d, int newPower, int roundingMode) {
		//对精度newPower进行处理，确保绝对值不超过8
		if (newPower > 0) {
			newPower = -newPower;
		}
		newPower = (newPower > -8) ? newPower : -8;
		//默认采取四舍五入方式
		boolean increment = true;
		switch (roundingMode) {
			case ROUND_UP :
				increment = true;
				break;
			case ROUND_CEILING :
				increment = (d > 0);
				break;
			case ROUND_FLOOR :
				increment = (d < 0);
				break;
			case ROUND_DOWN :
				increment = false;
				break;
			default :
				break;
		}
		//对小数部分进行相应的进位操作
		long l = (long) (d + ((increment) ? 0.5 : 0));
		return new VDouble(l, newPower);
	}

	/**
	 * 将分段的数据重新组合，主要是针对低位上超出九位数而需要进位时设定的。<br>
	 * 将低位上需要进位的值加到高位上。<br>
	 * 实际上在不需要进位的情况下，该方法不起任何作用。<br>
	 * 该方法不包含v[i]中存在负数的情况<br>
	 */
	private void adjustNotIncludeFs() {
		for (int i = 1; i < v.length; i++) {
			v[i] = v[i] + v[i - 1] / MAX_ONELONG_VALUE;
			v[i - 1] = v[i - 1] % MAX_ONELONG_VALUE;
		}
	}

	/**
	 * 将分段的数据重新组合，主要是针对低位上超出九位数而需要进位时设定的。<br>
	 * 将低位上需要进位的值加到高位上。<br>
	 * 实际上在不需要进位的情况下，该方法不起任何作用。<br>
	 * 该方法包含v[i]中存在负数的情况<br>
	 */
	private void adjustIncludeFs() {
		for (int i = 1; i < v.length; i++) {
			//如果低位为负数，则从高位借一低位上加上MAX_ONELONG_VALUE
			if (v[i - 1] < 0) {
				v[i]--;
				v[i - 1] += MAX_ONELONG_VALUE;
			} else {
				v[i] = v[i] + v[i - 1] / MAX_ONELONG_VALUE;
				v[i - 1] = v[i - 1] % MAX_ONELONG_VALUE;
			}
		}
	}

	/**
	 * 只保留power位小数，其他的全部去掉。
	 */
	private void cutdown() {
		int p = -power;
		//去掉多余位，以0代之
		v[0] = v[0] / POWER_ARRAY[p] * POWER_ARRAY[p];
	}

	/**
	 * 判断数据是否为负，并进行相应转换
	 */
	private void judgeNegative() {
		//判断标志
		boolean isFs = false;
		//从高位到低位进行判断，如果最高位为负则该数据为负数，反之为正数
		for (int i = v.length - 1; i >= 0; i--) {
			if (v[i] < 0) {
				isFs = true;
				break;
			}
			if (v[i] > 0)
				break;
		}
		//如果是负数则将si变为-1，并将数组中的值变反
		if (isFs) {
			for (int i = 0; i < v.length; i++)
				v[i] = -v[i];
			si = -1;
		}
	}

	/**
	 * 取得该对象的double型值
	 * @return double 该对象的值
	 */
	public double getDouble() {
		return this.doubleValue();
	}

	/**
	 * 取得对象的值
	 * @return double 该对象对应的数据的double型值
	 */
	public double doubleValue() {
		double d = 0;
		for (int i = v.length - 1; i >= 0; i--) {
			//d = d + v[i] * MAX_ONELONG_VALUE;
			d *= MAX_ONELONG_VALUE;
			d += v[i];
		}
		//恢复小数部分
		d = d / MAX_ONELONG_VALUE;
		//恢复原数据的正负号并返回
		return d * si;
	}

	/**
	 * 取得对象的float型数值
	 * @return 对象的float型数值
	 */
	public float floatValue() {
		return (float) getDouble();
	}

	/**
	 * 取得对象的long型数值
	 * @return 对象的long型数值
	 */
	public long longValue() {
		long d = 0;
		//去掉低位
		for (int i = v.length - 1; i > 0; i--) {
			d *= MAX_ONELONG_VALUE;
			d += v[i];
		}
		//返回原数据的long型值
		return d * si;
	}

	/**
	 * 取得对象的int型数值
	 * @return 对象的int型数值
	 */
	public int intValue() {
		return (int) getDouble();
	}

	/**
	 * 比较两对象值的大小
	 * @param   o 待比较的对象
	 * @return  如果该对象小于待比较对象返回一个负整数，如果等于则返回零，如果大于
	 *          返回一个正整数
	 * @throws ClassCastException if the specified object's type prevents it
	 *         from being compared to this Object.
	 */
	public int compareTo(Object o) {
		int i = toString().compareTo(((VDouble) o).toString());
		if (i > 0) {
			return 1;
		} else if (i < 0) {
			return -1;
		} else
			return 0;
	}

	/**
	 * 比较两个对象值是否相等
	 * @param o 待比较的对象
	 * @return 相等返回true，否则返回false
	 */
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o.getClass() == VDouble.class) {
			VDouble vd = (VDouble) o;
			/** 符号相同 */
			if (si != vd.si)
				return false;
			for (int i = 0; i < v.length; i++) {
				if (v[i] != vd.v[i])
					return false;
			}
			return true;
		}
		if (o instanceof Number) {
			return equals(new VDouble("" + o));
		}
		return false;
	}

	/**
	 * 转换为BigDecimal。
	 * @return java.math.BigDecimal
	 */
	public BigDecimal toBigDecimal() {
		return new BigDecimal(toString());
	}

	/**
	 * 转换为Double。
	 * @return java.lang.Double
	 */
	public Double toDouble() {
		return new Double(getDouble());
	}

	/**
	 * 为了进行运算，简化运算，将符号位填加到每一个数值上去，
	 * 这样进行加后然后进行进位调整。
	 * 将所有的符号变换到每一位上
	 */
	private void toPlus() {
		if (si == 1)
			return;
		si = 1;
		for (int i = 0; i < v.length; i++) {
			v[i] = -v[i];
		}
	}

	/**
	 * 将数值转换为字符串输出
	 * @return String 数值的字符串表达形式
	 */
	public String toString() {
		/** 没有添加位数，表示前面没有有效位数*/
		boolean addZero = false;
		StringBuffer sb = new StringBuffer();
		//如果为负数在数字前添加负号
		if (si == -1)
			sb.append("-");
		//将原数据的整数部分转化为字符串，(小数位不够9位的以0补之)
		for (int i = v.length - 1; i > 0; i--) {
			if (v[i] == 0 && !addZero)
				continue;
			String temp = String.valueOf(v[i]);
			//该步骤用于处理低位转换为字符串后不足9位(缺少有效位数)的情况，需要在高位后面加0补之
			if (addZero) {
				int len = temp.length();
				int addZeroNo = EFFICIENCY_SEATE - len;
				for (int j = 0; j < addZeroNo; j++) {
					sb.append('0');
				}
			}
			sb.append(temp);
			addZero = true;
		}
		//处理原数据整数部分为0的情况
		if (!addZero)
			sb.append('0');
		//处理小数位
		if (power < 0) {
			sb.append('.');
			for (int j = 0; j < EFFICIENCY_SEATE && j < -power; j++) {
				sb.append((v[0] / POWER_ARRAY[j + 1]) % 10);
			}
		}
		//压缩小数点后尾部0
		int index = -1;
		if (isTrimZero()) {
			if (power < 0) {
				String sTemp = sb.toString();
				//确定小数尾部0的起始位置，如果小数全部为0则以小数点位置为起始点
				for (int i = sb.length() - 1; i >= 0; i--) {
					if (sTemp.substring(i, i + 1).equals("0"))
						index = i;
					else {
						if (sTemp.substring(i, i + 1).equals(".")) {
							index = i;
						}
						break;
					}
				}
			}
		}
		if (index >= 0)
			sb = sb.delete(index, sb.length());
		return sb.toString();
	}

	/**
	 * 取绝对值。
	 * @return VDouble 返回一个新的对象，其值是该实例所含值的绝对值
	 */
	public VDouble abs() {
		VDouble fdnew = new VDouble();
		fdnew.power = this.power;
		fdnew.si = 1;
		for (int i = 0; i < v.length; i++) {
			fdnew.v[i] = v[i];
		}
		return fdnew;
	}

	/**
	 * 返回小数精度
	 * @return int 小数的精度power
	 */
	public int getPower() {
		return power;
	}

	/**
	 * 判断是否去掉数据后多余的0
	 * @return boolean true:去掉0，只保留有效数字 false:保留末尾的0
	 */
	public boolean isTrimZero() {
		return trimZero;
	}

	/**
	 * 设置是否去掉数据末尾的0
	 * @param newTrimZero true:去掉0，只保留有效数字  false:不去掉
	 */
	public void setTrimZero(boolean newTrimZero) {
		trimZero = newTrimZero;
	}

	/**
	 * 将一个长整型值传入数组v中
	 * @param va 待传入的long型数
	 */
	private void assignValue(long va) {
		for (int i = 0; i < ARRAY_LENGTH; i++) {
			v[i] = va % MAX_ONELONG_VALUE;
			va = va / MAX_ONELONG_VALUE;
		}
	}

}