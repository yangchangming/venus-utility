/*
 * 
 * 创建日期 2005-7-24
 *
 */
package venus.pub.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * comment for DateUtil
 * <p/>
 * 日期字符串的加减、格式化公共工具类
 *
 * @author zhengyan
 */
public class DateUtil {

    public static final int FORMAT_SHORTDATE = 0; //YYYY-MM

    public static final int FORMAT_DATE = 1; //YYYY-MM-DD

    public static final int FORMAT_DATETIME = 2; //YYYY-MM-DD HH:MM:SS

    public static final int FORMAT_DATETIMEMILLISECOND = 3; //YYYY-MM-DD HH:MM:SS.sss

    public static final int ONE_DAY_MILLISECOND = 86400000;

    /**
     * 得到年
     *
     * @param sDate
     * @return
     */
    public static int getYear(String sDate) {
        return Integer.parseInt(sDate.substring(0, 4));
    }

    /**
     * 得到月
     *
     * @param sDate
     * @return
     */
    public static int getMonth(String sDate) {
        return Integer.parseInt(sDate.substring(5, sDate.indexOf("-", 5)));
    }

    /**
     * 得到日期
     *
     * @param sDate
     * @return
     */
    public static int getDay(String sDate) {
        sDate = sDate.substring(sDate.lastIndexOf("-") + 1);

        if (sDate.indexOf(" ") != -1) sDate = sDate.substring(0, sDate.indexOf(" "));

        return Integer.parseInt(sDate);
    }


    /**
     * 得到今天的日期
     *
     * @return
     */
    public static String getBusinessDate() {
        //TODO 需要修正为得到业务日期
        return getDateTimeString(System.currentTimeMillis(), FORMAT_DATE);

    }

    /**
     * 得到今天的日期
     *
     * @return
     */
    public static String getToday() {
        return getDateTimeString(System.currentTimeMillis(), FORMAT_DATE);

    }

    /**
     * 得到当前的时间戳
     *
     * @return yyyy-mm-dd hh:mm:ss.sss 格式的当前时间戳
     */
    public static String getCurrentTimeStamp() {
        return getDateTimeString(System.currentTimeMillis(), FORMAT_DATETIMEMILLISECOND);

    }

    /**
     * 得到当前的时间戳
     *
     * @return yyyy-mm-dd hh:mm:ss.sss 格式的当前时间戳
     */
    public static String getCurrentDateTime() {
        return getDateTimeString(System.currentTimeMillis(), FORMAT_DATETIME);

    }

    /**
     * 判断一个字符串是否是日期 日期之间的分割符号为 "-"
     */
    public static boolean isDateTimeString(String sDate) {
        String separator = "-";
        StringTokenizer token = new StringTokenizer(sDate, separator);
        String year = token.nextToken();
        String month = token.nextToken();
        String day = token.nextToken();

        if (day.indexOf(" ") != -1) {
            day = day.substring(0, day.indexOf(" "));
            boolean is_date = isDate(year, month, day);

            String time = day.substring(day.indexOf(" ") + 1);
            return is_date && isTime(time);
        } else {
            return isDate(year, month, day);
        }
    }

    /**
     * 判断是否是合法日期
     */
    private static boolean isDate(String year, String month, String day) {
        return isDate(year + "", month + "", day + "");

    }

    /**
     * 判断是否是合法日期
     */
    private static boolean isDate(int year, int month, int day) {
        int[] day_of_months = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        try {
            boolean bLeapYear = false;
            if ((year + "").length() != 4)
                return false;

            if (isLeapYear(year))
                day_of_months[1] = 29;
            if (month < 1 || month > 12)
                return false;
            if (day < 1 || day > day_of_months[month - 1])
                return false;
            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 是否是合法的时间格式
     *
     * @param time
     * @return
     */
    private static boolean isTime(String time) {

        if (time == null || time.equals(""))
            time = "00:00:00.000";

        String[] s = time.split(":");

        //因为pattern中.是可以代表任意字符，需要转义
        String[] s1 = s[2].split("\\.");

        boolean ret = (Integer.parseInt(s[0]) < 24 && Integer.parseInt(s[1]) < 60);

        if (s1.length == 2) {
            ret = ret && Integer.parseInt(s1[0]) < 60
                    && Integer.parseInt(s1[1]) < 1000;
        } else if (s1.length == 0) {
            ret = ret && Integer.parseInt(s[2]) < 60;
        } else {
            ret = false;
        }
        return ret;

    }

    /**
     * 判断是否闰年
     */
    private static boolean isLeapYear(int year) {
        if (year % 4 != 0)
            return false;
        if ((year % 100 == 0) && (year % 400 != 0))
            return false;
        return true;
    }

    /**
     * @param date_time_str 格式形如 yyyy-mm-dd hh:mm:ss.sss ，或 yyyy-mm-dd hh:mm:ss 或
     *                      yyyy-mm-dd
     * @return 毫秒数
     * @throws Exception
     */
    public static long getMillisecond(String date_time_str) {

        if (date_time_str.indexOf(" ") == -1)
            return getMillisecond(date_time_str, null);

        String s[] = date_time_str.split(" ");

        return getMillisecond(s[0], s[1]);
    }

    /**
     * 得到日期,时间 距1970-1-1的毫秒数
     *
     * @param date yyyy-m-d 或 yyyy-mm-dd 格式
     * @param time hh:mm:ss.sss 格式
     */
    private static long getMillisecond(String date, String time) {

        if (time == null || time.equals(""))
            time = "00:00:00.000";

        String[] s = date.split("-");
        String[] s2 = time.split(":");

        //  因为pattern中.是可以代表任意字符，需要转义
        String[] s3 = s2[2].split("\\.");

        Calendar cl = Calendar.getInstance();

        cl.set(Calendar.YEAR, Integer.parseInt(s[0]));
        cl.set(Calendar.MONTH, Integer.parseInt(s[1]) - 1);
        cl.set(Calendar.DAY_OF_MONTH, Integer.parseInt(s[2]));

        cl.set(Calendar.HOUR_OF_DAY, Integer.parseInt(s2[0]));
        cl.set(Calendar.MINUTE, Integer.parseInt(s2[1]));

        if (s3.length > 1) {
            cl.set(Calendar.SECOND, Integer.parseInt(s3[0]));
            cl.set(Calendar.MILLISECOND, Integer.parseInt(s3[1]));
        } else {
            cl.set(Calendar.SECOND, Integer.parseInt(s2[2]));
            cl.set(Calendar.MILLISECOND, 0);
        }
        return cl.getTimeInMillis();
    }

    /**
     * 将毫秒数字转换为日期
     *
     * @param mill 毫秒数
     * @return yyyy/mm/dd 格式的日期字符串,mill是小于0时返回""
     */
    public static String getDateString(long mill) {
        return getDateTimeString(mill, FORMAT_DATE);
    }

    private static String getDateTimeString(long mill) {
        return getDateTimeString(mill, FORMAT_DATETIME);
    }

    /**
     * 将输入的时间日期串格式化为标准格式
     *
     * @param date_time_str
     * @param format
     * @return
     */
    public static String formatDateTimeString(String date_time_str, int format) {
        return getDateTimeString(getMillisecond(date_time_str), format);

    }


    /**
     * 将毫秒数字转换为日期字符串
     *
     * @param mill   毫秒数
     * @param format 格式
     * @return 返回格式化的日期字符串, mill是小于0时返回""
     */

    public static String getDateTimeString(long mill, int format) {
        if (mill < 0)
            return "";

        Calendar cl = Calendar.getInstance();

        cl.setTimeInMillis(mill);

        int year = cl.get(Calendar.YEAR);
        int month = cl.get(Calendar.MONTH) + 1;
        int day = cl.get(Calendar.DAY_OF_MONTH);
        int hour = cl.get(Calendar.HOUR_OF_DAY);
        int mm = cl.get(Calendar.MINUTE);
        int ss = cl.get(Calendar.SECOND);
        int ms = cl.get(Calendar.MILLISECOND);


        String ret = "";
        switch (format) {
            case FORMAT_SHORTDATE:
                ret = year + "-" + (month < 10 ? "0" + month : "" + month);
                break;
            case FORMAT_DATE:
                ret = year + "-" + (month < 10 ? "0" + month : "" + month) + "-"
                        + (day < 10 ? "0" + day : "" + day);
                break;
            case FORMAT_DATETIME:
                ret = year + "-" + (month < 10 ? "0" + month : "" + month) + "-"
                        + (day < 10 ? "0" + day : "" + day) + " "
                        + (hour < 10 ? "0" + hour : "" + hour) + ":"
                        + (mm < 10 ? "0" + mm : "" + mm) + ":"
                        + (ss < 10 ? "0" + ss : "" + ss);
                break;
            case FORMAT_DATETIMEMILLISECOND:

                String sMs;

                if (ms < 10)
                    sMs = "00" + ms;
                else if (ms < 100)
                    sMs = "0" + ms;
                else
                    sMs = "" + ms;


                sMs = "." + sMs;

                ret = year + "-" + (month < 10 ? "0" + month : "" + month) + "-"
                        + (day < 10 ? "0" + day : "" + day) + " "
                        + (hour < 10 ? "0" + hour : "" + hour) + ":"
                        + (mm < 10 ? "0" + mm : "" + mm) + ":"
                        + (ss < 10 ? "0" + ss : "" + ss) + sMs;

                break;
        }
        return ret;
    }

    /**
     * 判断两个日期yyyy-mm-dd之间相差多少天（只按照日期计算，时间忽略) 算头不算尾，日期的直接相减
     */
    public static int substract(String biger_date_time, String smaller_date_time) {
        return (int) ((getMillisecond(formatDateTimeString(biger_date_time, FORMAT_DATE)) -
                getMillisecond(formatDateTimeString(smaller_date_time, FORMAT_DATE))) / ONE_DAY_MILLISECOND);

    }

    /**
     * 两个日期相减 ，得到整月月数和零头的天数
     *
     * @param biger_date_time
     * @param smaller_date_time
     * @return [整月月数，零头的天数]
     */
    public static int[] substract1(String biger_date_time, String smaller_date_time) {
        int year_biger = getYear(biger_date_time);
        int month_biger = getMonth(biger_date_time);
        int day_biger = getDay(biger_date_time);

        int year_smaller = getYear(smaller_date_time);
        int month_smaller = getMonth(smaller_date_time);
        int day_smaller = getDay(smaller_date_time);

        int retMonth = (year_biger - year_smaller) * 12 + month_biger - month_smaller;
        int retDay = 0;
        if (day_biger < day_smaller - 1) {
            retMonth = retMonth - 1;

            retDay = substract(biger_date_time, year_biger + "-" + (month_biger - 1) + "-" + day_smaller);
        } else {
            retDay = day_biger - day_smaller;
        }

        return new int[]{retMonth, retDay};

    }

    /**
     * 加上几天后的日期时间串
     *
     * @param date_time_str
     * @param days
     * @param format        字符串的格式
     * @return
     */
    public static String add(String date_time_str, int days, int format) {
        return getDateTimeString(getMillisecond(date_time_str) + days
                * ONE_DAY_MILLISECOND, format);
    }

    /**
     * 加上几天后的日期串
     *
     * @param date_time_str
     * @param days
     * @param format        字符串的格式
     * @return
     */
    public static String add(String date_time_str, int days) {
        return add(date_time_str, days, FORMAT_DATE);
    }

    /**
     * 一个日期上加上一个月数，得到一个新的日期
     *
     * @param date
     * @param months
     * @return 如果当天不是一个合法日期，取当月的月末。 即2003-10-30 + 4个月等于 2004-02-29
     */
    public static String addMonth(String date, int months) {
        int year = getYear(date);
        int month = getMonth(date) + months;
        int day = getDay(date);

        if (month > 12) {
				if(month % 12 == 0){/**add by xial 处理月份为12的倍数*/
				       if(((int)month /12) > 1){/**非同年*/
 		  				       year += ((int)month /12)-1;
   		   			    	   month = getMonth(date);
					  }
    			}else{
					year += ((int)month /12);
					month = month % 12;
				}
        } else if (month < 1) {
            int counter = 0;
            do {
                counter++;
                month += 12;
            } while (month < 1);

            year -= counter;
        }

        if (!isDate(year, month, day)) {
            do {
                day--;
            }
            while (!isDate(year, month, day));
        }
        return year + "-" + (month < 10 ? "0" + month : month + "") + "-" + (day < 10 ? "0" + day : day + "");

    }

    /**
     * 根据字符串参数转换为对应的日期并返回
     * 注意:字符串参数中包含时间，会全部置为00:00:00
     * 如:2010-1-11 11:06:33 会转换为Mon Jan 11 00:00:00 CST 2010
     * 如:2010-1-11会转换为Mon Jan 11 00:00:00 CST 2010
     *
     * @param dateValue
     * @return
     * @throws ParseException
     */
    public static Date getGBDateFrmString(String dateValue)
            throws ParseException {
        Locale locale = Locale.SIMPLIFIED_CHINESE;
        DateFormat df = DateFormat.getDateInstance(2, locale);
        return df.parse(dateValue);
    }

    /**
     * 根据字符串参数转换为对应的日期时间并返回
     * 参数中必须包含时间，否则会报错java.text.ParseException: Unparseable date: "2010-1-11"
     * 如:2010-1-11 11:06:33 会转换为Mon Jan 11 11:06:33 CST 2010
     *
     * @param dateValue
     * @return
     * @throws ParseException
     */
    public static Date getGBDateTimeFrmString(String dateValue)
            throws ParseException {
        Locale locale = Locale.SIMPLIFIED_CHINESE;
        DateFormat df = DateFormat.getDateTimeInstance(2, 2, locale);
        return df.parse(dateValue);
    }

    public static String getStringFrmGBDate(Date dateValue) {
        return dateValue.toString();
    }
}
