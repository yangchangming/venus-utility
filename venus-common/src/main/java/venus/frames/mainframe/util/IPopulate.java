package venus.frames.mainframe.util;

/**
 * @author wujun
 * @author huqi
 */
public interface IPopulate {

    /**
     * @param source
     *            注值的源属性值
     * @param obj
     *            注值的目标对象
     * @param targetTypeName
     *            注值的目标属性的类型
     * @param targetName
     *            注值的目标属性的名字
     * @return 注值的目标对象
     */
    public Object populate(Object source, Object obj, String targetTypeName,
                           String targetName);

}