/**
 *@author zhangrenyang 
 *@date  2011-9-28
*/
package venus.portal.helper;

import java.sql.Timestamp;

/**
 *@author zhangrenyang 
 *@date  2011-9-28
 */
public class EwpDateHelper {

    /**
     * 取Java虚拟机系统时间, 返回当前日期和时间
     * @return 返回String格式的日期和时间, YYYY-MM-DD HH24:MI:SS， 长19位
     */
    public static String getSysDateTime() {
        return new Timestamp(System.currentTimeMillis()).toString().substring(0,19);
    }
    
    /**
     * 取Java虚拟机系统时间, 返回当前时间戳
     * 
     * @return Timestamp类型的时间
     */
    public static Timestamp getSysTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
    
}
