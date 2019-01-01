/*
 * 创建日期 2007-4-20
 * CreateBy zhangbaoyu
 */
package venus.cron.extend;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.comparators.ComparableComparator;

import java.io.Serializable;
import java.util.Comparator;

/**
 * @author zhangbaoyu 为实现TriggerBo对象排序而准备
 */
public class BoComparator implements Comparator, Serializable {

    private String property;

    private Comparator comparator;

    public BoComparator(String property) {
        this(property, ((Comparator) (ComparableComparator.getInstance())));
    }

    public BoComparator(String property, Comparator comparator) {
        setProperty(property);
        this.comparator = comparator;
    }

    public int compare(Object o1, Object o2) {
        Object value1;
        Object value2;
        try{
        value1 = PropertyUtils.getProperty(o1, property);
        value2 = PropertyUtils.getProperty(o2, property);
        }catch(Exception e){
            throw new ClassCastException(e.toString());
        }
        if(value1==null&&value2==null)return 0;
        if(value1!=null&&value2==null)return -1;
        if(value1==null&&value2!=null)return 1;
        return comparator.compare(value1.toString(), value2.toString());
    }

    /**
     * @return 返回 property。
     */
    protected String getProperty() {
        return property;
    }

    /**
     * @param property
     *            要设置的 property。
     */
    public void setProperty(String property) {
        this.property = property;
    }

    /**
     * @return 返回 comparator。
     */
    protected Comparator getComparator() {
        return comparator;
    }
}