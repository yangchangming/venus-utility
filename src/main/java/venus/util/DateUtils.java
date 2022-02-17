package venus.util;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    /**
     * 获取当前时间
     * @return
     */
    public static String getCurrentTime() {
        Date date = new Date(); //获取当前的系统时间。
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ; //使用了默认的格式创建了一个日期格式化对象。
        String time = dateFormat.format(date); //可以把日期转换转指定格式的字符串
        return time;
    }

    /**
     * 获取当前时间流水
     * @return
     */
    public static String getSerialNo(){
        Date date = new Date(); //获取当前的系统时间。
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss") ;
        String serialNo = dateFormat.format(date);//时间流水
        return serialNo;
    }
    /**
     * 获取当前时间流水
     * @return
     */
    public static Timestamp getTimestamp(){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return timestamp;
    }
}
