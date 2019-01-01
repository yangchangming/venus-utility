/*
 * 创建日期 2006-10-24
 */
package venus.oa.util;

import venus.oa.service.sys.vo.SysParamVo;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author ganshuo  
 * 实现一些通用的日期,时间处理
 */
public class DateTools {
	public DateTools() {
	}
	/**  一天的毫秒数  */
	public final static long ONE_DAY_MILLIS = 24 * 60 * 60 * 1000;
	/**  本地化为简体中文  */
	public final static Locale DEFAULT_CHINA_LOCALE = Locale.SIMPLIFIED_CHINESE;
	/**  时区设置为北京时间  */
	public final static TimeZone DEFAULT_CHINA_TIMEZONE = TimeZone.getTimeZone("GMT+8:00");

	public final static String[] DEFAULT_WEEK_ARRAY_DESC = new String[]{venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Monday"), venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Tuesday"), venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Wednesday"), venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Thursday"), venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Friday"), venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Saturday"), venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Sunday")};
	
	/**
	 * 取Java虚拟机系统时间, 返回当前时间戳
	 * 
	 * @return Timestamp类型的时间
	 */
	public static Timestamp getSysTimestamp() {
		return new Timestamp(System.currentTimeMillis());
	}
	
	/**
	 * 取Java虚拟机系统时间, 返回当前日期
	 * 
	 * @return 只返回String格式的日期，YYYY-MM-DD， 长10位
	 */
	public static String getSysDate() {
		return new Timestamp(System.currentTimeMillis()).toString().substring(0,10);
	}
	
	/**
	 * 取Java虚拟机系统时间, 返回当前日期和时间
	 * 
	 * @return 返回String格式的日期和时间, YYYY-MM-DD HH24:MI:SS， 长19位
	 */
	public static String getSysDateTime() {
		return new Timestamp(System.currentTimeMillis()).toString().substring(0,19);
	}
	
	/**
	 * 获得时间戳
	 * 
	 * @param strDate YYYY-MM-DD HH24:MI:SS格式的字符串
	 * @return 时间戳
	 */
	public static Timestamp getTimestamp(String strDate) {
		Timestamp rtValue = null;
		if(strDate == null || strDate.length()==0) {
			return null;
		}
		try {
			rtValue = Timestamp.valueOf(strDate);
		} catch (Exception e) {
			strDate = strDate.trim() + " 00:00:00";
			try {
				rtValue = Timestamp.valueOf(strDate);
			} catch (RuntimeException e1) {
				e1.printStackTrace();
			}
		}

		return rtValue;
	}
	
	/**
	 * 取Java虚拟机系统时间, 返回当前日历
	 * 
	 * @return 返回String格式的日期和时间, YYYY-MM-DD HH24:MI:SS， 长19位
	 */
	public static String getSysDateTimeByCalendar() {
		StringBuffer str = new StringBuffer();
		Calendar rightNow = Calendar.getInstance(DEFAULT_CHINA_TIMEZONE, DEFAULT_CHINA_LOCALE);
		int iYear = rightNow.get(Calendar.YEAR);
		int iMonth = rightNow.get(Calendar.MONTH) + 1;
		int iDate = rightNow.get(Calendar.DATE);
		int iHour = rightNow.get(Calendar.HOUR_OF_DAY);
		int iMinute = rightNow.get(Calendar.MINUTE);
		int iSecond = rightNow.get(Calendar.SECOND);
		str.append(iYear);
		str.append("-");
		if(iMonth<10)
			str.append("0");
		str.append(iMonth);
		str.append("-");
		if(iDate<10)
			str.append("0");
		str.append(iDate);
		str.append(" ");
		str.append(iHour);
		if(iHour<10)
			str.append("0");
		str.append(":");
		str.append(iMinute);
		if(iMinute<0)
			str.append("0");
		str.append(":");
		if(iSecond<0)
			str.append("0");
		str.append(iSecond);
		return str.toString();
	}

	/**
	 * 取Java虚拟机系统时间, 返回当前日历
	 * 
	 * @return 只返回String格式的日期，YYYY-MM-DD， 长10位
	 */
	public static String getSysDateByCalendar() {
		StringBuffer str = new StringBuffer();
		Calendar rightNow = Calendar.getInstance(DEFAULT_CHINA_TIMEZONE,DEFAULT_CHINA_LOCALE);
		int iYear = rightNow.get(Calendar.YEAR);
		int iMonth = rightNow.get(Calendar.MONTH) + 1;
		int iDate = rightNow.get(Calendar.DATE);
		str.append(iYear);
		str.append("-");
		if (iMonth < 10) {
			str.append("0");
		}
		str.append(iMonth);
		str.append("-");
		if (iDate < 10) {
			str.append("0");
		}
		str.append(iDate);
		return str.toString();
	}
	
    /**
     * 功能: 获得本地化的时间
     *
     * @param dateDesc YYYY-MM-DD HH24:MI:SS 格式的字符串
     * @return
     */
    public static Calendar getCalendar(String dateDesc) {
        Calendar c = Calendar.getInstance(DateTools.DEFAULT_CHINA_TIMEZONE, DateTools.DEFAULT_CHINA_LOCALE);
        c.setTime(DateTools.getTimestamp(dateDesc));
        return c;
    }
    
    /**
     * 功能: 获得本地化的时间
     *
     * @param longDate 时间的长整数
     * @return
     */
    public static Calendar getCalendar(long longDate) {
        Calendar c = Calendar.getInstance(DateTools.DEFAULT_CHINA_TIMEZONE, DateTools.DEFAULT_CHINA_LOCALE);
        c.setTimeInMillis(longDate);
        return c;
    }
    
    /**
     * 功能: 获得格式化的日期和时间描述
     *
     * @param longDate 时间的长整数
     * @return YYYY-MM-DD HH24:MI:SS 格式的字符串
     */
    public static String getFormatDateTimeDesc(long longDate) {
        return new Timestamp(longDate).toString().substring(0, 19);
    }
    
    /**
     * 功能: 由毫秒数得到小时数
     *
     * @param longDate
     * @return
     */
    public static double getHourNumberByLong(long longDate) {
        return longDate / 60 * 60 * 1000;
    }
    
    /**
     * 功能: 判断thisDate是否是今天
     *
     * @param thisDate
     * @return
     */
    public static boolean isToday(Date thisDate) {
        String today = getFormatDateTimeDesc(System.currentTimeMillis());
        String thisDateCal = getFormatDateTimeDesc(thisDate.getTime());
        if(today.substring(0,10).endsWith(thisDateCal.substring(0,10))) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * 功能: 判断long1-long2和long3-long4区域是否重叠
     *
     * @param long1
     * @param long2
     * @param long3
     * @param long4
     * @return
     */
    public static boolean isOverlap(long long1, long long2, long long3, long long4) {
        boolean returnValue = false;
        if(long3 >= long1 && long3 < long2) {
            returnValue = true;
        } else if(long4 <= long2 && long4 > long1) {
            returnValue = true;
        } else if(long3 < long1 && long4 > long2 ) {
            returnValue = true;
        }
        return returnValue;
    }

    /**
     * 功能: 转换时间为字符串，精确到分钟
     *
     * @param time1 Timestamp
     * @return String
     */
    public static String getTimePrecMinute(Timestamp time1){
        if(time1==null)
            return "";
        Calendar cal = Calendar.getInstance(DateTools.DEFAULT_CHINA_TIMEZONE, DateTools.DEFAULT_CHINA_LOCALE);
        cal.setTimeInMillis(time1.getTime());
        //年
        String strYear = String.valueOf(cal.get(Calendar.YEAR));
        //月
        String strMon;
        if(cal.get(Calendar.MONTH)+1<10){
            strMon = "0"+String.valueOf(cal.get(Calendar.MONTH)+1);
        }else{
            strMon = String.valueOf(cal.get(Calendar.MONTH)+1);
        }
        //日
        String strDay ;
        if(cal.get(Calendar.DATE)<10){
            strDay = "0"+String.valueOf(cal.get(Calendar.DATE));
        }else{
            strDay = String.valueOf(cal.get(Calendar.DATE));
        }
        //时
        String strHour;
        if(cal.get(Calendar.HOUR_OF_DAY)<10){
            strHour = "0" + String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
        }else{
            strHour = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
        }
        
        //分
        String strMin ;
        if(cal.get(Calendar.MINUTE)<10){
            strMin = "0" + String.valueOf(cal.get(Calendar.MINUTE));
        }else{
            strMin = String.valueOf(cal.get(Calendar.MINUTE));
        }
        return strYear+"-"+strMon+"-"+strDay+" "+strHour+":"+strMin;
    }
    
    /**
     * 功能: 转换时间为字符串，精确到天
     *
     * @param otime
     * @return String
     */
    public static String getStrDatePrecDay(Object otime){
        if(otime==null||otime.getClass().getName().equals("java.lang.String"))
            return "";
        Timestamp time1 = (Timestamp)otime;
        
        Calendar cal = Calendar.getInstance(DateTools.DEFAULT_CHINA_TIMEZONE, DateTools.DEFAULT_CHINA_LOCALE);
        cal.setTimeInMillis(time1.getTime());
        //年
        String strYear = String.valueOf(cal.get(Calendar.YEAR));
        //月
        String strMon;
        if(cal.get(Calendar.MONTH)+1<10){
            strMon = "0"+String.valueOf(cal.get(Calendar.MONTH)+1);
        }else{
            strMon = String.valueOf(cal.get(Calendar.MONTH)+1);
        }
        //日
        String strDay ;
        if(cal.get(Calendar.DATE)<10){
            strDay = "0"+String.valueOf(cal.get(Calendar.DATE));
        }else{
            strDay = String.valueOf(cal.get(Calendar.DATE));
        }
        return strYear+"-"+strMon+"-"+strDay;
    }
    
    /**
     * 功能:  比较compareDate是否比当前时间早minTime，默认三天内
     *
     * @param compareDate 要比较的时间
     * @return
     */
    public static boolean isNew (long compareDate) {
        return isNew(compareDate, 3 * ONE_DAY_MILLIS);
    }
    
    /**
     * 功能: 比较compareDate是否比当前时间早minTime
     *
     * @param compareDate 要比较的时间
     * @param minTime 最小差距
     * @return
     */
    public static boolean isNew (long compareDate, long minTime) {
        if(System.currentTimeMillis() - compareDate > minTime) {
            return false;
        } else {
            return true;
        }
    }
    
    /**
     * 功能: 得到系统时间如20060101010101
     *
     * @return
     */
    public static String getJoinedSysDateTime() {
        String str = getSysDateTime();
        str = str.substring(0,4) + str.substring(5,7) + str.substring(8,10) + str.substring(11,13) + str.substring(14,16) + str.substring(17,19); 
        return str;
    }
    
    /**
     * 获取过期日期
     * @return 过期日期时间戳
     */
    public static Timestamp getRetireDate(){
    	int pwdLifeCycle = -1;
    	SysParamVo sysParamVo = GlobalConstants.getSysParam(GlobalConstants.PWDLIFECYCLE);
        if(null!=sysParamVo){
        	pwdLifeCycle = Integer.parseInt(sysParamVo.getValue());
        }
        if(-1==pwdLifeCycle)
        	return null;
        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.DATE,pwdLifeCycle);
        return new Timestamp(cal.getTime().getTime());
    }
    
    public static long getDateBetween(Date dateBig, Date dateSmall) { 
        //return dateBig.getTime() / (24*60*60*1000) - dateSmall.getTime() / (24*60*60*1000); 
        return dateBig.getTime() / 86400000 - dateSmall.getTime() / 86400000;  //用立即数，减少乘法计算的开销
    } 
    
	public static void main(String[] args) {
	    //Timestamp time1 = new Timestamp(System.currentTimeMillis());
	    //System.out.println(getTimePrecMinute(time1));
	}

}

