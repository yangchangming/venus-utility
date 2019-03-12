package venus;

import org.apache.commons.lang3.StringUtils;
import venus.frames.mainframe.log.ILog;
import venus.frames.mainframe.log.LogMgr;
import venus.frames.mainframe.util.BeanFactoryHolder;
import venus.frames.mainframe.util.PathMgr;

import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p> global helper </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2018-04-09 22:34
 */
public class VenusHelper {

    public static boolean SQL_FILTER = false;
    public static String DEFAULT_UPLOAD_PATH = "/WEB-INF/upload/";
    public static boolean IS_LOG_SQL_END_ENABLED = false;
    public static boolean IS_LOG_SQL_START_ENABLED = false;
    public static boolean VALIDATE_AT_POPULATE = false;
    public static boolean VALIDATE_AT_POPULATE_FROM_RESULTSET = false;
    public static int DEFAULT_PAGE_SIZE = 15;
    public static String THEME = "flat";


    public static Object getBean(String name) {
        return BeanFactoryHolder.getBean(name);
    }


    /**
     * 提供统一的文件上传目录，可以通过两种方式配置
     * 1、配置相对路径，以WEB-INF为根，例如/WEB-INF/upload/
     * 2、配置绝对路径,返回值均为绝对路径
     *
     * @return
     */
    public static String getUploadPath() {
        if (DEFAULT_UPLOAD_PATH.indexOf("WEB-INF") > 0)
            return PathMgr.getRealRootPath() + "/" + DEFAULT_UPLOAD_PATH;
        return DEFAULT_UPLOAD_PATH;
    }

    public static String getTheme() {
        String theme = THEME;
        if (StringUtils.isEmpty(theme))
            theme = "flat";
        return theme;
    }

    /**
     * 过滤SQL语句 过滤的SQL语句格式如下： 1.数字 (<>=!) 数字 2.(数字 (<>=!) 数字) 3.NOT 数字 (<>=!)
     * 数字
     *
     * @param sql String 需要过滤的SQL语句
     * @return String 过滤后的SQL语句
     */
    public static String doSqlFilter(String sql) {
        boolean ExsitParentheses = true;
        boolean ExsitNOT = true;

        String regEx1 = "[+-]*\\s*\\d+[!=<>\\s]+[+-]*\\s*\\d+";
        String regEx2 = "\\(\\s*###\\s*\\)";
        String regEx3 = "(?i)not\\s+###";
        String regEx4 = "###\\s+((?i)or|(?i)and)+\\s";
        String regEx5 = "\\s((?i)or|(?i)and|(?i)where(?i)|having)+\\s+###";
        Matcher matcher = Pattern.compile(regEx1).matcher(sql);
        if (matcher.find()) {
            sql = matcher.replaceAll(" ### ");
        }
        while (ExsitNOT || ExsitParentheses) {
            ExsitNOT = true;
            ExsitParentheses = true;
            matcher = Pattern.compile(regEx2).matcher(sql);
            if (matcher.find()) {
                sql = matcher.replaceAll(" ### ");
            } else {
                ExsitParentheses = false;
            }
            matcher = Pattern.compile(regEx3).matcher(sql);
            if (matcher.find()) {
                sql = matcher.replaceAll(" ### ");
            } else {
                ExsitNOT = false;
            }
            matcher = Pattern.compile(regEx4).matcher(sql);
            if (matcher.find()) {
                sql = matcher.replaceAll(" ");
            }
            matcher = Pattern.compile(regEx5).matcher(sql);
            if (matcher.find()) {
                sql = matcher.replaceAll(" ");
            }
        }
        return sql;
    }


    /**
     * 此方法为辅助方法，代理 LogMgr.getLogger(caller)
     * <p/>
     * 静态方法对应于 getLogger(...) 的静态代理方法
     * <p/>
     * 传入参数：记录日志的调用者名字，参数不可以为空，否则会抛出空指针异常
     * <p/>
     * 得到LogMgr 的实例并根据得到的实现者名得到 LOG驱动实例
     *
     * @param caller 记录日志的调用者名字
     * @return venus.frames.mainframe.log.Ilogger - LOG驱动实例
     */
    @Deprecated
    public static ILog getLogger(String caller) {
        return LogMgr.getLogger(caller);
    }

    /**
     * 功能：从map中获取指定值，不区分key的大小写
     * @param map
     * @param key
     * @return
     */
    public static Object getValueFromMapByKey(Map map, String key) {
        Object result = null;
        Iterator iter = map.keySet().iterator();
        while (iter.hasNext()) {
            String iKey = iter.next().toString();
            if (iKey.equalsIgnoreCase(key)) {
                result = map.get(key);
                return result;
            }
        }
        return result;
    }
}
