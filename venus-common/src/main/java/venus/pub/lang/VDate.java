package venus.pub.lang;

import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

/**
 * @author libaoyu
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 * VDate是一个表示日期的类，即表示某年某月某日。
 * 注意：日期的大小必须限制在1850-1-1到2050-12-31日之间。
 * 和java标准库中的Date类不同，VDate的月份是用数字1～12表示的。 
 * */

public final class VDate implements Serializable {

	private final int year;
	private final int month;
	private final int date;

	private static final long serialVersionUID = 2389798743985769l;

	public final int getYear() {
		return year;
	}

	public final int getMonth() {
		return month;
	}

	public final int getDate() {
		return date;
	}
	/**
	 * Constructor for VDate.
	 * 无参数构造函数，使用当前时期进行构造。
	 */
	public VDate() {
		GregorianCalendar gc = new GregorianCalendar();
		this.year = gc.get(Calendar.YEAR);
		this.month = gc.get(Calendar.MONTH) + 1;
		this.date = gc.get(Calendar.DATE);
	}

	/**
	 * Constructor for VDate.
	 * @param year
	 * @param month
	 * @param date
	 * 构造函数
	 * 注意：日期的大小必须限制在1850-1-1到2050-12-31日之间。
	 */

	public VDate(int year, int month, int date) {
		if ((year > 2050) || (year < 1850) || (month > 12) || (month < 1)) {
			throw new IllegalArgumentException();
		}
		if (month == 2) {
			if (VDateTime.isLeapYear(year)) {
				if ((date < 0) || (date > 29))
					throw new IllegalArgumentException();
			} else {
				if ((date < 0) || (date > 28))
					throw new IllegalArgumentException();
			}
		} else if (month == 4 || month == 6 || month == 9 || month == 11) {
			if ((date < 0) || (date > 31))
				throw new IllegalArgumentException();
		} else if ((date < 0) || (date > 32))
			throw new IllegalArgumentException();
		this.year = year;
		this.month = month;
		this.date = date;
	}

	/**
	 * Constructor for VDate.
	 * @param year
	 * @param month
	 * @param date
	 * @param hour
	 * @param minute
	 */
	public VDate(int year, int month, int date, int hour, int minute) {
		this(year, month, date);
	}

	/** Constructor for VDate.
	 * @param year
	 * @param month
	 * @param date
	 * @param hour
	 * @param minute
	 * @param second
	 */
	public VDate(
		int year,
		int month,
		int date,
		int hour,
		int minute,
		int second) {
		this(year, month, date);
	}

	public VDate(long millis) {
		GregorianCalendar gc = new GregorianCalendar();
		Date date = new Date(millis);
		gc.setTime(date);
		this.year = gc.get(Calendar.YEAR);
		this.month = gc.get(Calendar.MONTH) + 1;
		this.date = gc.get(Calendar.DATE);
	}

	public VDate(String strDate) {
		this(strDate, true);
	}

	public VDate(String strDate, boolean isParse) {
		if (isParse) {
			if (!VDate.isAllowDate(strDate))
				throw new IllegalArgumentException();
		}
		strDate = strDate.trim();
		String s = null;
		int j = 0;
		int year = 0;
		int month = 0;
		int date = 0;
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
		this.year = year;
		this.month = month;
		this.date = date;
	}

	public VDate(VDateTime datetime) {
		this(datetime.getTime());
	}

	public final boolean after(VDate when) {
		return getYear() != when.getYear()
			? getYear() > when.getYear()
			: getMonth() != when.getMonth()
			? getMonth() > when.getMonth()
			: (getDate() != when.getDate() ? getDate() > when.getDate() : false);
	}

	public final boolean before(VDate when) {
		return getYear() != when.getYear()
			? getYear() < when.getYear()
			: getMonth() != when.getMonth()
			? getMonth() < when.getMonth()
			: (getDate() != when.getDate() ? getDate() < when.getDate() : false);
	}

	public final int compareTo(VDate date) {
		if (date == null) {
			return Integer.MIN_VALUE;
		} else if (after(date)) {
			return 1;
		} else if (before(date)) {
			return -1;
		} else
			return 0;
	}

	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o != null && getClass() == o.getClass()) {
			VDate date = (VDate) o;
			if (getYear() == date.getYear()
				&& getMonth() == date.getMonth()
				&& getDate() == date.getDate()) {
				return true;
			}
		}
		return false;
	}

	public final VDate getDateAfter(int days) {
		GregorianCalendar gc =
			new GregorianCalendar(getYear(), getMonth() - 1, getDate());
		Date udate = gc.getTime();
		udate = new Date(udate.getTime() + days * 24 * 3600 * 1000);
		gc.setTime(udate);
		int year = gc.get(Calendar.YEAR);
		int month = gc.get(Calendar.MONTH) + 1;
		int date = gc.get(Calendar.DATE);
		return new VDate(year, month, date);
	}

	public final VDate getDateBefore(int days) {
		GregorianCalendar gc =
			new GregorianCalendar(getYear(), getMonth() - 1, getDate());
		Date udate = gc.getTime();
		udate = new Date(udate.getTime() - days * 24 * 3600 * 1000);
		gc.setTime(udate);
		int year = gc.get(Calendar.YEAR);
		int month = gc.get(Calendar.MONTH) + 1;
		int date = gc.get(Calendar.DATE);
		return new VDate(year, month, date);
	}

	public final int getDaysAfter(VDate when) {
		GregorianCalendar gc =
			new GregorianCalendar(getYear(), getMonth(), getDate());
		Date udate = gc.getTime();
		long thisTime = udate.getTime();
		gc =
			new GregorianCalendar(
				when.getYear(),
				when.getMonth(),
				when.getDate());
		udate = gc.getTime();
		long whenTime = udate.getTime();
		long millis = thisTime - whenTime;
		return (int) (millis / (24 * 3600 * 1000));
	}

	public final static int getDaysBetween(VDate begin, VDate end) {
		return end.getDaysAfter(begin);
	}

	public final static int getDaysMonth(int year, int month) {
		if ((year > 2050) || (year < 1850) || (month < 1) || (month > 12)) {
			throw new IllegalArgumentException();
		}
		if (month == 2) {
			if (VDateTime.isLeapYear(year)) {
				return 29;
			} else
				return 28;
		} else if (month == 4 || month == 6 || month == 9 || month == 11) {
			return 30;
		}
		return 31;
	}

	public final static String getValidVDateString(String sDate) {
		if (!(VDate.isAllowDate(sDate))) {
			throw new IllegalArgumentException();
		}
		sDate = sDate.trim();
		String s = null;
		StringBuffer sb = new StringBuffer();
		String[] strDateArr = new String[3];
		int j = 0;
		StringTokenizer st = null;
		if ((sDate.indexOf('-') != -1) && (sDate.indexOf('/') == -1)) {
			st = new StringTokenizer(sDate, "-", false);
		} else if ((sDate.indexOf('/') != -1) && (sDate.indexOf('-') == -1)) {
			st = new StringTokenizer(sDate, "/", false);
		}
		if (st == null)
			throw new IllegalArgumentException();
		while (st.hasMoreTokens()) {
			s = st.nextToken();
			if (j != 0) {
				if (s.length() == 1) {
					s = "0" + s;
				}
			}
			strDateArr[j++] = s;
		}

		return (
			sb.append(strDateArr[0]).append("-").append(strDateArr[1]).append(
				"-").append(
				strDateArr[2]))
			.toString();
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + year;
		result = 37 * result + month;
		result = 37 * result + date;
		return result;
	}

	public final static boolean isAllowDate(String strDate) {
		if ((strDate == null) || (strDate.length() == 0)) {
			return false;
		}
		strDate = strDate.trim();
		String s = null;
		int year = 0;
		int month = 0;
		int date = 0;
		int j = 0;
		StringTokenizer st = null;
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
						if ((date < 0) || (date > 30))
							return false;
					} else {
						if ((date < 0) || (date > 29))
							return false;
					}
				} else if (
					month == 4 || month == 6 || month == 9 || month == 11) {
					if ((date < 0) || (date > 31))
						return false;
				}
				if ((date < 1) || (date > 31))
					return false;
			}
			j++;
		}
		if (j != 3)
			return false;
		return true;
	}

	public final String toLocaleString() {
		String strMonth = Integer.toString(month);
		if (strMonth.length() == 1) {
			strMonth = "0" + strMonth;
		}
		String strDate = Integer.toString(date);
		if (strDate.length() == 1) {
			strDate = "0" + strDate;
		}
		StringBuffer sb = new StringBuffer();
		sb.append(Integer.toString(year));
		sb.append("-");
		sb.append(strMonth);
		sb.append("-");
		sb.append(strDate);
		return sb.toString();
	}

	public static void main(String[] args) throws ClassNotFoundException, IOException{
		//		java.sql.Date d = new java.sql.Date(123456789l);
		//		VDateTime v = new VDateTime(d);
		//		System.out.println(v.getVDate().toLocaleString());
		//		VDateTime vd = new VDateTime(1970, 3, 15);
		//		System.out.println(vd.getTime());
		//		System.out.println(vd.getVDate().toLocaleString());
		//		System.out.println(VDateTime.getDaysBetween(v, vd));
		//		VDate vdtTwoMonthDate1 = new VDate(1999, 2, 28);
		//		VDate vdtTwoMonthDate2 = new VDate(1999, 2, 1);
		//		System.out.println(vdtTwoMonthDate1.getDateAfter(1).toLocaleString());
		//		VDateTime dateTime = new VDateTime();
		//		Date sdate = new Date(123456789l);
		//		VDateTime dateTimeD = new VDateTime(sdate);
		//		//System.out.println(VDateTime.getDaysBetween(dateTimeD, dateTime));
		//		//System.out.println(vdtTwoMonthDate2.getDateAfter(1).toLocaleString());
		//		VDate vvv = new VDate();
		//		System.out.println(vvv.toLocaleString());
		VDate v = new VDate();
		VDate vv = new VDate(1928,2,3);
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("v.serial"));
        out.writeObject(v);
        out.writeObject(vv);
        out.close();
        ObjectInputStream in = new ObjectInputStream(new FileInputStream("v.serial"));		
        VDate dd = (VDate)in.readObject();
        VDate dv = (VDate)in.readObject();
        System.out.println(dd.toLocaleString());
        System.out.println(dv.toLocaleString());
	}
}
