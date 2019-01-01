package venus.portal.util;

import org.apache.commons.lang.StringUtils;
import venus.commons.xmlenum.EnumRepository;
import venus.commons.xmlenum.EnumValueMap;

import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;


/**
 * 枚举的服务类
 *
 * @author zhangrenyang
 * @date 2011-11-16
 */
public class EnumTools {
    /**
     * 模板类型
     */
    public final static String TEMPLATETYPE = "TemplateType";
    /**
     * 排序号
     */
    public final static String SORTNUM = "SortNum";
    /**
     * 真假布尔值
     */
    public final static String LOGICTRUEFALSE = "LogicTrueFalse";
    /**
     * 从何处得知
     */
    public final static String WHERETOKNOW = "WhereToKnow";
    /**
     * 链接种类
     */
    public final static String LINKCATEGORY = "LinkCategory";
    /**
     * 导航菜单
     */
    public final static String NAVIGATE_MENU = "NavigateMenu";
    /**
     * 是或否
     */
    public final static String LOGICBOOLEAN = "LogicBoolean";
    /**
     * 链接跳转方式
     */
    public final static String LINKTARGET = "LinkTarget";

    /**
     * 中文
     */
    public final static String CHINESE = "zh";
    /**
     * 英文
     */
    public final static String ENGLISH = "en";
    /**
     * 日文
     */
    public final static String JAPANESE = "ja";
    /**
     * 德文
     */
    public final static String GERMAN = "de";

    private static EnumRepository er = EnumRepository.getInstance();

    /**
     * 默认比较器
     */
    private static Comparator defaultComparator = new Comparator() {
        public int compare(Object o1, Object o2) {
            return (o1 == null ? "" : o1.toString()).compareTo(o2 == null ? "" : o2.toString());
        }
    };

    /**
     * 数字比较器
     */
    public static Comparator numberComparator = new Comparator() {
        public int compare(Object o1, Object o2) {
            Integer num1 = o1 == null ? 0 : new Integer(o1.toString());
            Integer num2 = o2 == null ? 0 : new Integer(o2.toString());
            return num1.compareTo(num2);
        }
    };

    static {
        er.loadFromDir();
    }

    /**
     * 根据枚举键得到枚举值
     *
     * @param key 枚举键
     * @return 排序后的map
     * @throws
     */
    public static TreeMap<String, String> getSortedEnumMap(String key) {
        return getSortedEnumMap(key, defaultComparator);
    }

    /**
     * 根据枚举键得到枚举值
     *
     * @param key        枚举键
     * @param comparator 排序的接口实现类
     * @return 排序后的map
     * @throws
     */
    public static TreeMap<String, String> getSortedEnumMap(String key, Comparator comparator) {
        EnumValueMap map = er.getEnumValueMap(key);
        List list = map.getEnumList();
        TreeMap<String, String> treeMap = new TreeMap<String, String>(comparator);
        for (int i = 0; i < list.size(); i++) {
            treeMap.put(map.getValue(list.get(i).toString()), list.get(i).toString());
        }
        return treeMap;
    }

    /**
     * 取得此语言的排序编号
     *
     * @param language
     * @return
     */
    public static int getOrderNum(String language) {
        if (StringUtils.equals(language, CHINESE)) {
            return 0;
        } else if (StringUtils.equals(language, ENGLISH)) {
            return 1;
        } else if (StringUtils.equals(language, JAPANESE)) {
            return 2;
        } else if (StringUtils.equals(language, GERMAN)) {
            return 3;
        }
        return language.hashCode();
    }

}
