package venus.pub.lang;

import java.io.*;
import java.util.*;

/**
 * @author libaoyu
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 * 此类表示一个精确到秒的时间值，
 */

public final class VDateTime implements Serializable {

	private final Calendar greCal;
	
	private static final long serialVersionUID = 968095643435435546l;

	/**
	 * Constructor for VDateTime.
	 */
	public VDateTime() {
		greCal = new GregorianCalendar();
	}

	/**
	 * Constructor for VDateTime.
	 * @param zone
	 */
	public VDateTime(TimeZone zone) {
		greCal = new GregorianCalendar(zone);
	}

	/**
	 * Constructor for VDateTime.
	 * @param aLocale
	 */
	public VDateTime(Locale aLocale) {
		greCal = new GregorianCalendar(aLocale);
	}

	/**
	 * Constructor for VDateTime.
	 * @param zone
	 * @param aLocale
	 */
	public VDateTime(TimeZone zone, Locale aLocale) {
		greCal = new GregorianCalendar(zone, aLocale);
	}

	/**
	 * Constructor for VDateTime.
	 * @param year
	 * @param month
	 * @param date
	 */
	public VDateTime(int year, int month, int date) {
		this(year, month, date, 0, 0, 0);
	}

	/**
	 * Constructor for VDateTime.
	 * @param year
	 * @param month
	 * @param date
	 * @param hour
	 * @param minute
	 */
	public VDateTime(int year, int month, int date, int hour, int minute) {
		this(year, month, date, hour, minute, 0);
	}

	/**
	 * Constructor for VDateTime.
	 * @param year
	 * @param month
	 * @param date
	 * @param hour
	 * @param minute
	 * @param second
	 */
	public VDateTime(
		int year,
		int month,
		int date,
		int hour,
		int minute,
		int second) {
		boolean flag = true;
		if ((year > 2050) || (year < 1850)) {
			flag = false;
		} else if ((month > 12) || (month < 1)) {
			flag = false;
		} else if (month == 2) {
			if (VDateTime.isLeapYear(year)) {
				if ((date < 1) || (date > 29))
					flag = false;
			} else {
				if ((date < 1) || (date > 28))
					flag = false;
			}
		} else if (month == 4 || month == 6 || month == 9 || month == 11) {
			if ((date < 1) || (date > 30))
				flag = false;
		}
		if ((date < 1) || (date > 31))
			flag = false;

		if (hour == 24) {
			if ((minute != 0) || (second != 0)) {
				flag = false;
			}
		}
		if ((hour > 24)
			|| (hour < 0)
			|| (minute > 59)
			|| (minute < 0)
			|| (second > 59)
			|| (second < 0)) {
			flag = false;
		}
		if (flag) {
			greCal =
				new GregorianCalendar(
					year,
					month - 1,
					date,
					hour,
					minute,
					second);
		} else {
			throw new IllegalArgumentException();
		}
	}

	public VDateTime(long millis) {
		greCal = new GregorianCalendar();
		Date date = new Date(millis);
		greCal.setTime(date);
	}

	public VDateTime(String strDateTime) {
		this(strDateTime, true);
	}

	public VDateTime(String strDateTime, boolean isParse) {
		if (isParse) {
			if (!isValidDateTime(strDateTime)) {
				throw new IllegalArgumentException();
			}
		}
		strDateTime = strDateTime.trim();
		int i = strDateTime.indexOf(' ');
		String strDate = strDateTime.substring(0, i);
		String strTime = strDateTime.substring(i + 1);
		String s = null;
		int year = 0;
		int month = 0;
		int date = 0;
		int hour = 0;
		int minute = 0;
		int second = 0;
		int j = 0;
		StringTokenizer st = null;
		if ((strDate.indexOf('-') != -1) && (strDate.indexOf('/') == -1)) {
			st = new StringTokenizer(strDate, "-", false);
		} else if (
			(strDate.indexOf('/') != -1) && (strDate.indexOf('-') == -1)) {
			st = new StringTokenizer(strDate, "/", false);
		}
		while (st.hasMoreTokens()) {
			s = st.nextToken();
			if (j == 0) {
				year = Integer.parseInt(s);
			} else if (j == 1) {
				month = Integer.parseInt(s);
			} else if (j == 2) {
				date = Integer.parseInt(s);
			}
			j++;
		}
		j = 0;
		st = new StringTokenizer(strTime, ":", false);
		while (st.hasMoreTokens()) {
			s = st.nextToken();
			if (j == 0) {
				hour = Integer.parseInt(s);
			} else if (j == 1) {
				minute = Integer.parseInt(s);
			} else if (j == 2) {
				second = Integer.parseInt(s);
			}
			j++;
		}
		greCal =
			new GregorianCalendar(year, month - 1, date, hour, minute, second);
	}

	public VDateTime(java.sql.Date sqlDate) {
		greCal = new GregorianCalendar();
		greCal.setTime(sqlDate);
	}

	public VDateTime(Date date) {
		greCal = new GregorianCalendar();
		greCal.setTime(date);
	}

	// 用VDate和VTime和起来构造,参数为空时抛出NullPointerException	
	public VDateTime(VDate vdate, VTime vtime) {
		if ((vdate == null) || (vtime == null)) {
			throw new NullPointerException();
		}
		int year = vdate.getYear();
		int month = vdate.getMonth();
		int date = vdate.getDate();
		int hour = vtime.getHour();
		int minute = vtime.getMinute();
		int second = vtime.getSecond();
		greCal =
			new GregorianCalendar(year, month - 1, date, hour, minute, second);
	}

	//获取两个VDateTime,VDateTime之间天数,计算算法：datetime2-datetime1 
	public final static int getDaysBetween(VDateTime first, VDateTime second) {
		if (!first.greCal.getTimeZone().equals(second.greCal.getTimeZone())) {
			throw new IllegalArgumentException();
		}
		return VDate.getDaysBetween(first.getVDate(), second.getVDate());
	}

	public final static int getDaysOfMonth(int year, int month) {
		return VDate.getDaysMonth(year, month);
	}

	//将参数字符串，转换成yyyy-MM-dd hh:mm:ss格式字有效的字符串。
	public final static String getValidVDateTimeString(String datetime) {
		if (!(VDateTime.isValidDateTime(datetime))) {
			throw new IllegalArgumentException();
		}
		datetime = datetime.trim();
		int i = datetime.indexOf(' ');
		String strDate = datetime.substring(0, i);
		String strTime = datetime.substring(i + 1);
		String s = null;
		StringBuffer sb = new StringBuffer();
		String[] strDateArr = new String[3];
		String[] strTimeArr = new String[3];
		int j = 0;
		StringTokenizer st = null;
		if ((strDate.indexOf('-') != -1) && (strDate.indexOf('/') == -1)) {
			st = new StringTokenizer(strDate, "-", false);
		} else if (
			(strDate.indexOf('/') != -1) && (strDate.indexOf('-') == -1)) {
			st = new StringTokenizer(strDate, "/", false);
		}
		while (st.hasMoreTokens()) {
			s = st.nextToken();
			if (j != 0) {
				if (s.length() == 1) {
					s = "0" + s;
				}
			}
			strDateArr[j++] = s;
		}
		j = 0;
		st = new StringTokenizer(strTime, ":", false);
		while (st.hasMoreTokens()) {
			s = st.nextToken();
			if (s.length() == 1) {
				s = "0" + s;
			}
			strTimeArr[j++] = s;
		}
		return (
			sb
				.append(strDateArr[0])
				.append("-")
				.append(strDateArr[1])
				.append("-")
				.append(strDateArr[2])
				.append(" ")
				.append(strTimeArr[0])
				.append(":")
				.append(strTimeArr[1])
				.append(":")
				.append(strTimeArr[2]))
			.toString();

	}

	public final static boolean isValidDateTime(String datetime) {
		if ((datetime == null) || (datetime.length() == 0)) {
			return false;
		}
		datetime = datetime.trim();
		int i = datetime.indexOf(' ');
		if (i == -1)
			return false;
		String strDate = datetime.substring(0, i);
		String strTime = datetime.substring(i + 1);
		String s = null;
		int year = 0;
		int month = 0;
		int date = 0;
		int j = 0;
		StringTokenizer st = null;
		if ((strDate.endsWith("-"))
			|| (strDate.endsWith("/"))
			|| (strDate.startsWith("-"))
			|| (strDate.startsWith("/"))) {
			return false;
		}
		if ((strDate.indexOf('-') != -1) && (strDate.indexOf('/') == -1)) {
			st = new StringTokenizer(strDate, "-", false);
		} else if (
			(strDate.indexOf('/') != -1) && (strDate.indexOf('-') == -1)) {
			st = new StringTokenizer(strDate, "/", false);
		}
		if (st == null) {
			return false;
		} else if (st.countTokens() != 3) {
			return false;
		}
		while (st.hasMoreTokens()) {
			s = st.nextToken();
			if (j == 0) {
				try {
					year = Integer.parseInt(s);
				} catch (NumberFormatException e) {
					return false;
				}
				if ((s.length() != 4) || (year > 2050) || (year < 1850)) {
					return false;
				}
			} else if (j == 1) {
				try {
					month = Integer.parseInt(s);
				} catch (NumberFormatException e) {
					return false;
				}
				if ((month > 12) || (month < 1)) {
					return false;
				}
			} else if (j == 2) {
				try {
					date = Integer.parseInt(s);
				} catch (NumberFormatException e) {
					return false;
				}
				if (month == 2) {
					if (VDateTime.isLeapYear(year)) {
						if ((date < 1) || (date > 29))
							return false;
					} else {
						if ((date < 1) || (date > 28))
							return false;
					}
				} else if (
					month == 4 || month == 6 || month == 9 || month == 11) {
					if ((date < 1) || (date > 30))
						return false;
				}
				if ((date < 1) || (date > 31))
					return false;
			}
			j++;
		}
		j = 0;
		int hour = 0;
		int minute = 0;
		int second = 0;
		if ((strTime.endsWith(":")) || (strTime.endsWith(":"))) {
			return false;
		}
		st = new StringTokenizer(strTime, ":", false);
		if (st == null) {
			return false;
		} else if (st.countTokens() != 3) {
			return false;
		}
		while (st.hasMoreTokens()) {
			s = st.nextToken();
			if (j == 0) {
				try {
					hour = Integer.parseInt(s);
				} catch (NumberFormatException e) {
					return false;
				}
			} else if (j == 1) {
				try {
					minute = Integer.parseInt(s);
				} catch (NumberFormatException e) {
					return false;
				}
			} else if (j == 2) {
				try {
					second = Integer.parseInt(s);
				} catch (NumberFormatException e) {
					return false;
				}
			}
			j++;
		}
		if (hour == 24) {
			if ((minute != 0) || (second != 0)) {
				return false;
			}
		}
		if ((hour > 24)
			|| (hour < 0)
			|| (minute > 59)
			|| (minute < 0)
			|| (second > 59)
			|| (second < 0)) {
			return false;
		}
		return true;
	}

	public final long getTime() {
		Date date = greCal.getTime();
		return date.getTime();
	}

	public final static boolean isLeapYear(int year) {
		GregorianCalendar gc = new GregorianCalendar();
		return gc.isLeapYear(year);
	}

	public final boolean after(VDateTime when) {
		if (!greCal.getTimeZone().equals(when.greCal.getTimeZone())) {
			throw new IllegalArgumentException();
		}
		return getTime() > when.getTime();
	}

	public final boolean before(VDateTime when) {
		if (!greCal.getTimeZone().equals(when.greCal.getTimeZone())) {
			throw new IllegalArgumentException();
		}
		return getTime() < when.getTime();
	}

	public final int compareTo(VDateTime datetime) {
		if (datetime == null) {
			return Integer.MIN_VALUE;
		} else if (after(datetime)) {
			return 1;
		} else if (before(datetime)) {
			return -1;
		} else
			return 0;
	}

	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o != null && getClass() == o.getClass()) {
			VDateTime datetime = (VDateTime) o;
			if (getTime() == datetime.getTime()) {
				return true;
			}
		}
		return false;
	}

	public int hashCode() {
		return greCal.hashCode();
	}

	public final static VDate getVDate(VDateTime datetime) {
		return datetime.getVDate();
	}
	public final VDate getVDate() {
		return new VDate(
			greCal.get(Calendar.YEAR),
			greCal.get(Calendar.MONTH) + 1,
			greCal.get(Calendar.DATE));
	}

	public final String toLocaleString() {
		StringBuffer sb = new StringBuffer();
		int month = greCal.get(Calendar.MONTH) + 1;
		int date = greCal.get(Calendar.DATE);
		String strMonth = Integer.toString(month);
		if (strMonth.length() == 1) {
			strMonth = "0" + strMonth;
		}
		String strDate = Integer.toString(date);
		if (strDate.length() == 1) {
			strDate = "0" + strDate;
		}
		int hour = greCal.get(Calendar.HOUR);
		if (greCal.get(Calendar.AM_PM) == Calendar.PM) {
			hour = hour + 12;
		}
		int minute = greCal.get(Calendar.MINUTE);
		int second = greCal.get(Calendar.SECOND);
		String strHour = Integer.toString(hour);
		if (strHour.length() == 1) {
			strHour = "0" + strHour;
		}
		String strMinute = Integer.toString(minute);
		if (strMinute.length() == 1) {
			strMinute = "0" + strMinute;
		}
		String strSecond = Integer.toString(second);
		if (strSecond.length() == 1) {
			strSecond = "0" + strSecond;
		}
		sb.append(Integer.toString(greCal.get(Calendar.YEAR)));
		sb.append("-");
		sb.append(strMonth);
		sb.append("-");
		sb.append(strDate);
		sb.append(" ");
		sb.append(strHour);
		sb.append(":");
		sb.append(strMinute);
		sb.append(":");
		sb.append(strSecond);
		return sb.toString();
	}

	public static void main(String[] args) throws ClassNotFoundException, IOException{
//		//VDateTime v = new VDateTime(l
//		VDateTime gg = new VDateTime(TimeZone.getDefault());
//		GregorianCalendar g =
//			new GregorianCalendar(
//				TimeZone.getTimeZone("America/Denver"),
//				Locale.FRANCE);
//		//			String[] IDs = TimeZone.getAvailableIDs();
//		//			for (int i=0; i<IDs.length; i++){
//		//				System.out.println(IDs[i]);
//		//			}
//		System.out.println(g.getInstance());
//		System.out.println(gg.toLocaleString());
//		GregorianCalendar gc =
//			new GregorianCalendar(TimeZone.getDefault(), Locale.FRANCE);
//		System.out.println(gc.getInstance());
//		VDateTime vdt = new VDateTime();
//		VDateTime vdtd = new VDateTime(123456789l);
//		VDateTime.getDaysBetween(vdt, vdtd);
        VDateTime v = new VDateTime();
		//VDate vv = new VDate(1928,2,3);
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("v.serial"));
        out.writeObject(v);
       // out.writeObject(vv);
        out.close();
        ObjectInputStream in = new ObjectInputStream(new FileInputStream("v.serial"));		
        VDateTime dd = (VDateTime)in.readObject();
        //VDate dv = (VDate)in.readObject();
        System.out.println(dd.toLocaleString());
       // System.out.println(dv.toLocaleString());
	}

}
