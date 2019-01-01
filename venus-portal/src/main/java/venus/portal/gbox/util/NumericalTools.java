package venus.portal.gbox.util;


public class NumericalTools {
    
    /**
     * 将长整型数值转换为长整型字符串(KB)
     * @param longNumber
     * @return String 长整型字符串
     */
    public static String longToKB(String longNumber) {
        if (longNumber == null || "".equals(longNumber))
            return "0";
        return String.valueOf(Math.round(Long.parseLong(longNumber) / 1024));
    }
    
    /**
     * 将长整型数值转换为长整型字符串(KB)
     * @param number
     * @return String 长整型字符串
     */    
    public static String longToKB(Long number) {
        return String.valueOf(Math.round(number / 1024));
    }
    
    /**
     * 将长整型数值转换为长整型字符串(MB)
     * @param longNumber
     * @return String 长整型字符串
     */    
    public static String longToMB(String longNumber) {
        if (longNumber == null || "".equals(longNumber))
            return "0";
        return String.valueOf(Math.round(Long.parseLong(longNumber) / (1024 * 1024)));
    }    
    
    /**
     * 将长整型数值转换为长整型字符串(MB)
     * @param number
     * @return String 长整型字符串
     */       
    public static String longToMB(Long number) {
        return String.valueOf(Math.round(number / (1024 * 1024)));
    }
    
    /**
     * 将整形数值(KB)转换为长整型数值
     * @param intNumber
     * @return 长整型数值
     */
    public static long KBToLong(String intNumber) {
        if (intNumber == null || "".equals(intNumber))
            return 0;
        return KBToLong(Integer.parseInt(intNumber));
    }
    
    /**
     * 将整形数值(MB)转换为长整型数值
     * @param number
     * @return 长整型数值
     */    
    public static long MBToLong(String intNumber) {
        if (intNumber == null || "".equals(intNumber))
            return 0;
        return MBToLong(Integer.parseInt(intNumber));
    }    
    
    /**
     * 将整形数值(KB)转换为长整型数值
     * @param number
     * @return 长整型数值
     */
    public static long KBToLong(int number) {
        return number * 1024L;
    }
    
    /**
     * 将整形数值(MB)转换为长整型数值
     * @param number
     * @return 长整型数值
     */    
    public static long MBToLong(int number) {
        return number *  (1024 * 1024L); //2012-04-06加入L 确保数据在转换时超过integer最大值1894776832
    }    
    
}
