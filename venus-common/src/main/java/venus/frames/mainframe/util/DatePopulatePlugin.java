package venus.frames.mainframe.util;

import venus.frames.mainframe.log.LogMgr;
import venus.pub.util.DateUtil;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;

/**
 * Support java.sql.Date->java.util.Date <br>
 * ->java.lang.String <br>
 * ->java.sql.Timestamp
 * <p>
 * Support java.sql.Timestamp->java.util.Date <br>
 * ->java.lang.String <br>
 * ->java.sql.Date
 * <p>
 * Support *->java.util.Date <br>
 * Support *->java.sql.Timestamp <br>
 * 
 * @author huqi, fixed bug by changming.y
 * @version 1.0.0
 * @see
 * @since 1.0.0
 */
public class DatePopulatePlugin implements IPopulate {

    private final static String J_UTIL_DATE = java.util.Date.class.getName();

    private final static String J_SQL_DATE = java.sql.Date.class.getName();

    private final static String J_SQL_TIMESTAMP = Timestamp.class
            .getName();

    private final static String J_LANG_STRING = String.class
            .getName();

    /**
     * 如果目标对象与源对象类型相同，则直接返回源对象
     *
     * @see IPopulate#populate(Object,
     *      Object, String, String)
     * @param source
     *            源对象
     * @param obj
     *            注值目标（不使用）
     * @param targetTypeName
     *            注值目标Class类型
     * @param targetName
     *            注值目标域名（不使用）
     * @return
     */
    public Object populate(Object source, Object obj, String targetTypeName,
            String targetName) {

        if (targetTypeName.equals(source.getClass().getName()))
            return source;

        if (source instanceof Timestamp) {

            Timestamp sc = (Timestamp) source;

            if (J_UTIL_DATE.equals(targetTypeName)) {
                return new java.util.Date(sc.getTime());
            } else if (J_LANG_STRING.equals(targetTypeName)) {
                return sc.toString();
            } else if (J_SQL_DATE.equals(targetTypeName)) {
                return new java.sql.Date(sc.getTime());
            } else {
                throw new IllegalArgumentException(
                        "Populater not support this targetTypeName: "
                                + targetTypeName + "<source-"
                                + source.getClass().getName() + ">.");
            }
        } else if (source instanceof java.sql.Date) {

            java.sql.Date sc = (java.sql.Date) source;

            if (J_UTIL_DATE.equals(targetTypeName)) {
                return new java.util.Date(sc.getTime());
            } else if (J_LANG_STRING.equals(targetTypeName)) {
                return new java.sql.Date(sc.getTime()).toString();
            } else if (J_SQL_TIMESTAMP.equals(targetTypeName)) {
                return new Timestamp(sc.getTime());
            } else {
                throw new IllegalArgumentException(
                        "Populater not support this targetTypeName: "
                                + targetTypeName + "<source-"
                                + source.getClass().getName() + ">.");
            }
        }

        //deal with *->java.util.Date
        if (targetTypeName != null && targetTypeName.equals(J_UTIL_DATE)) {
            String dataStr = (String) source;
            if (!(dataStr == null || dataStr.equals(""))) {
                try {
                    java.util.Date date = DateUtil.getGBDateFrmString(formatDateStrStand(dataStr, "-"));
                    return date;
                } catch (ParseException e) {
                    LogMgr.getLogger(this.getClass().getName()).error(
                            "In *->java.util.Date: " + e.getMessage()
                                    + ", source to string is: " + dataStr);
                    return null;
                }
            }
        }
        
        //deal with *->java.sql.Timestamp, added by changming.y
        if (targetTypeName != null && targetTypeName.equals(J_SQL_TIMESTAMP)) {
            if (source instanceof oracle.sql.TIMESTAMP) {
                oracle.sql.TIMESTAMP tmpTs = (oracle.sql.TIMESTAMP)source;
                try {
                    return tmpTs.timestampValue();
                } catch (SQLException e) {
                    LogMgr.getLogger(this.getClass().getName()).error("*->java.sql.TimeStamp: " + e.getMessage()  + ", source is: " + tmpTs);
                    return null;
                } catch (Exception e) {
                    LogMgr.getLogger(this.getClass().getName()).error("Exception ", e.getCause());
                    return null;
                }
            } else {
                String dataStr = source.toString();
                if ( dataStr != null && dataStr.length() != 0 ){
                    try{
                        Timestamp ts = Timestamp.valueOf(dataStr);
                        return ts;
                    }catch( IllegalArgumentException e ){
                        dataStr = dataStr.trim() + " 00:00:00";
                        try {
                            Timestamp ts = Timestamp.valueOf(dataStr);
                            return ts;
                        } catch (Exception e1) {
                            LogMgr.getLogger(this.getClass().getName()).error("*->java.sql.TimeStamp: " + e1.getMessage()  + ", source to string is: " + dataStr);
                            return null;
                        }
                    }
                }
            }
        }

        return null;
    }

    private String formatDateStrStand(String str, String sign) {
        String[] strDateArr = str.split(sign);
        if (strDateArr == null)
            return null;
        if (strDateArr.length == 1)
            return null;
        String tmpstr = "";
        String newdatestr = "";
        tmpstr = strDateArr[0]; //获得年数据
        if (tmpstr == null || tmpstr.length() != 4)
            return null;
        newdatestr = newdatestr + tmpstr + sign;
        tmpstr = strDateArr[1]; //获得月数据
        tmpstr = toStand(tmpstr);
        newdatestr = newdatestr + tmpstr;

        if (strDateArr.length == 3) {
            tmpstr = strDateArr[2]; //获得日数据
            tmpstr = toStand(tmpstr);
            newdatestr = newdatestr + sign + tmpstr;
        }
        return newdatestr;
    }

    private String toStand(String str) {
        if (str == null || str.equals("") || str.length() > 2) {
            return null;
        }
        if (str.length() == 1) {
            str = "0" + str;
        }
        return str;
    }

}