package venus.pub.lang;

import venus.frames.mainframe.log.LogMgr;

import java.io.Serializable;
import java.text.SimpleDateFormat;

/**
 * <p>Description: 用hh:mm:ss形式表示时间。
 *   支持的输入类型包括：java.sql.Date、java.util.Date、整型（毫秒）、字符串等。<br>
 *   另外也可以对时间的先后进行比较，需要注意的是，该类中支持的大多数操作主要是针对
 *   具体的时间，而不考虑日期。
 * </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company:USE </p>
 * @author 张文韬
 * @version 1.0
 */

public class VTime implements Serializable,Comparable {
	//变量
	//存储时间值
	private String value = null;

	//构造函数
	/**
	 * 构造子注释<br>
	 * 默认为当前时间
	 */
	public VTime() {
		this(System.currentTimeMillis());
	}

	/**
	 * 以从1970年1月1日0时0分0秒到现在的毫秒数来构造时间
	 * @param m long 毫秒数
	 */
	public VTime(long m) {
		this(new java.util.Date(m));
	}

	/**
	 *用java.sql.Date类型构造VTime时间类型。
	 * @param time java.sql.Date
	 */
	public VTime(java.sql.Date time) {
		this((java.util.Date) time);
	}

	/**
	 *用java.util.Date类型构造VTime时间类型。
	 * @param time java.util.Date
	 */
	public VTime(java.util.Date time) {
		this((new SimpleDateFormat("HH:mm:ss")).format(time));
	}

	/**
	 *用HH-mm-ss形式的字符串构造时间类型。
	 * @param strTime 待转换的表示时间的字符串
	 */
	public VTime(String strTime) {
		this(strTime, true);
	}

	/**
	 *用HH-mm-ss形式的字符串构造时间类型。<br>
	 *如果解析，用HH-mm-ss形式的字符串构造时间类型；反之时间的格式不确定。
	 * @param strTime 待转换的字符串
	 * @param isParse true--将字符串转换为标准形式；false--不进行转换
	 */
	public VTime(String strTime, boolean isParse) {
		if (isParse) {
			value = getValideVTimeString(strTime);
		} else
			value = strTime;
	}

	/**
	 * 克隆时间对象。
	 * @return venus.pub.lang.VTime
	 */
	public Object clone() {
		return new VTime(value);
	}

	/**
	 *如果字符串符合时间的形式则按照规定的格式"HH:mm:ss"进行转换；
	 * @param strTime 表示时间的字符串
	 * @return 空串或者符合规定格式的字符串
	 */
	public static String getValideVTimeString(String strTime) {
		if (strTime == null) {
			return null;
		}
		//判断字符串是否为合法字符串,若合法则直接返回，否则进行相应转换
		if (isAllowTime(strTime)) {
			return strTime;
		} else {
			try {
				int hour = 0;
				int minute = 0;
				int second = 0;
				//如果字符串中含有符号":"，则分别取出小时、分、秒
				int index = strTime.indexOf(":");
				if (index < 1) {
					if (strTime.trim().length() > 0)
						try {
							hour = Integer.parseInt(strTime.trim());
						} catch (NumberFormatException e) {
							LogMgr.getLogger("venus.pub.lang.VTime").error("数字转换时出现问题，请检查输入是否有误！",e);
							//System.out.println("数字转换时出现问题，请检查输入是否有误！");
							return null;
						}
				} else {
					//取得小时
					try {
						hour =
							Integer.parseInt(
								strTime.substring(0, index).trim());
					} catch (NumberFormatException e) {
						LogMgr.getLogger("venus.pub.lang.VTime").error("数字转换时出现问题，请检查输入是否有误！",e);
						//System.out.println("数字转换时出现问题，请检查输入是否有误！");
						return null;
					}
					//取得分钟
					String tempStr = strTime.substring(index + 1).trim();
					if (tempStr.length() > 0) {
						index = tempStr.indexOf(":");
						if (index < 1) {
							try {
								minute = Integer.parseInt(tempStr.trim());
							} catch (NumberFormatException e) {
								LogMgr.getLogger("venus.pub.lang.VTime").error("数字转换时出现问题，请检查输入是否有误！",e);
								//System.out.println("数字转换时出现问题，请检查输入是否有误！");
								return null;
							}
						} else {
							try {
								minute =
									Integer.parseInt(
										tempStr.substring(0, index).trim());
							} catch (NumberFormatException e) {
								LogMgr.getLogger("venus.pub.lang.VTime").error("数字转换时出现问题，请检查输入是否有误！",e);
								//System.out.println("数字转换时出现问题，请检查输入是否有误！");
								return null;
							}
							//取得秒
							if (tempStr.substring(index + 1).trim().length()
								> 0) {
								try {
									second =
										Integer.parseInt(
											tempStr
												.substring(index + 1)
												.trim());
								} catch (NumberFormatException e) {
									LogMgr.getLogger("venus.pub.lang.VTime").error("数字转换时出现问题，请检查输入是否有误！",e);
									//System.out.println("数字转换时出现问题，请检查输入是否有误！");
									return null;
								}
							}
						}
					}
				}
				//判断小时是否合理
				if (hour < 0 || hour > 24) {
					return null;
				}
				//判断分钟是否合理
				if (minute < 0 || minute > 59) {
					return null;
				}
				//判断秒是否合理
				if (second < 0 || second > 59) {
					return null;
				}
				//将时间转换为字符串
				String strHour = String.valueOf(hour);
				String strMinute = String.valueOf(minute);
				String strSecond = String.valueOf(second);
				//进行标准形式的转换
				if (strHour.length() < 2)
					strHour = "0" + strHour;
				if (strMinute.length() < 2)
					strMinute = "0" + strMinute;
				if (strSecond.length() < 2)
					strSecond = "0" + strSecond;
				return strHour + ":" + strMinute + ":" + strSecond;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	}

	/**
	 * 该方法的主要目的是取得小时的数字表示形式
	 * @return 小时的数字表示形式
	 */
	public int getHour() {
		return Integer.valueOf(value.substring(0, 2)).intValue();

	}

	/**
	 * 该方法的主要目的是取得分钟的数字表示形式
	 * @return 分钟的数字表示形式
	 */
	public int getMinute() {
		return Integer.valueOf(value.substring(3, 5)).intValue();
	}

	/**
	 * 该方法的主要目的是取得秒的数字表示形式
	 * @return 秒的数字表示形式
	 */
	public int getSecond() {
		return Integer.valueOf(value.substring(6, 8)).intValue();
	}

	/**
	 * 该方法的主要目的是取得该对象的毫秒表示形式
	 * @return 时间的毫秒表示形式
	 */
	public int getMillis() {
		return (getHour() * 3600 + getMinute() * 60 + getSecond()) * 1000;
	}

	/**
	 * 判断时间是否为规定的标准形式：HH:mm:ss
	 * @param time 待判断的字符串型时间
	 * @return boolean 符合规定返回真，反之返回假
	 */
	public static boolean isAllowTime(String time) {
		//如果字符串为空返回true
		if (time == null || time.trim().length() == 0)
			return true;
		//如果字符串的长度不是8，返回false
		if (time.trim().length() != 8) {
			return false;
		}
		//如果时间之间的间隔号不是':'，返回false
		if (time.charAt(2) != ':' || time.charAt(5) != ':') {
			return false;
		}
		//判断小时分秒是否合理
		try {
			int hour = Integer.parseInt(time.trim().substring(0, 2));
			int minute = Integer.parseInt(time.trim().substring(3, 5));
			int second = Integer.parseInt(time.trim().substring(6, 8));
			if (hour > 24 || hour < 0) {
				return false;
			}
			if (minute < 0 || minute > 59) {
				return false;
			}
			if (second < 0 || second > 59) {
				return false;
			}
		} catch (NumberFormatException e) {
			LogMgr.getLogger("venus.pub.lang.VTime").error("数字转换时出现问题，请检查输入是否有误！",e);
			//System.out.println("数字转换时出错，请检查输入是否有误！");
			return false;
		}
		//一切正常
		return true;
	}

	/**
	 * 比较对象时间和参数时间的先后
	 * @param when 参数时间
	 * @return 若是对象时间在参数时间之后返回true，否则返回false
	 */
	public boolean after(VTime when) {
		return value.compareTo(when.value) > 0;
	}

	/**
	 * 比较对象时间和参数时间的先后
	 * @param when 参数时间
	 * @return 若是对象时间在参数时间之前返回true，否则返回false
	 */
	public boolean before(VTime when) {
		return value.compareTo(when.value) < 0;
	}

	/**
	 * 实现Comparable接口的抽象方法
	 * @param o 待比较的对象
	 * @return -1----表示对象时间在参数时间之前;0----表示二者为同一时刻;1----表示对象时间在参数时间之后
	 */
	public int compareTo(Object o) {
		return compareTo((VTime)o);
	}
	
	/**
	 * 比较对象时间和参数时间的先后，不考虑日期先后
	 * -1----表示对象时间在参数时间之前
	 *  0----表示二者为同一时刻
	 *  1----表示对象时间在参数时间之后
	 * @param when 参数时间
	 * @return 对象时间在参数时间之前返回-1,二者为同一时刻返回0,对象时间在参数时间之后返回1
	 */
	public int compareTo(VTime when) {
		int i = value.compareTo(when.value);
		if (i > 0) {
			return 1;
		} else if (i < 0) {
			return -1;
		} else
			return 0;
	}

	/**
	 * 比较对象时间和参数对象时间的先后，不考虑日期先后
	 * @param o 参数对象
	 * @return 若是对象时间和参数对象时间是同一时间返回true，否则返回false
	 */
	public boolean equals(Object o) {
		if ((o != null) && (o instanceof VTime)) {
			return value.equals(o.toString());
		} else
			return false;
	}

	/**
	 * 返回时间的字符串表达形式
	 * @return 时间的字符串表达形式
	 */
	public String toString() {
		return value == null ? "" : value;
	}

}