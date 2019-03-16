package venus.frames.mainframe.util;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.*;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import venus.frames.base.IGlobalsKeys;
import venus.frames.base.action.IRequest;
import venus.frames.base.vo.BaseValueObject;
import venus.frames.mainframe.log.ILog;
import venus.frames.mainframe.log.LogMgr;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.*;

/**
 * @author wujun
 * @author huqi
 * 
 * @serialData 2005-12-7
 */
public class PopulateUtil implements IGlobalsKeys {

    private static Map m_populatePlugins = null;

    private static void initConfig() {

        String confName = PopulateUtil.class.getName();

        if (m_populatePlugins == null) {
            m_populatePlugins = new HashMap();
            IConfReader icr = ConfMgr.getConfReader(confName);
            if (icr == null)
                return;
            ArrayList csAryLst = icr.readChildNodesAry("populatePlugin");
            int cslen = csAryLst.size();
            Hashtable hashCS = new Hashtable(cslen);
            //将信息存到数组中
            for (int i = 0; i < cslen; i++) {
                try {
                    Node tmpNode1 = (Node) csAryLst.get(i);
                    //取信息
                    NamedNodeMap tmpNodeMap1 = tmpNode1.getAttributes();
                    Node typeNameNode = tmpNodeMap1.getNamedItem("type");
                    Node pluginNameNode = tmpNodeMap1.getNamedItem("plugin");
                    Class plugin = ClassLocator.loadClass(pluginNameNode
                            .getNodeValue());
                    m_populatePlugins.put(typeNameNode.getNodeValue()
                            .toUpperCase(), plugin.newInstance());
                } catch (Exception e) {
                    getIlog().error(e.getMessage(), e);

                }
            }
        }
    }

    private static boolean superPopulate(String targetType, Object source,
            Object targerObject, String targetName) {
        if (targetType == null || source == null || targerObject == null
                || targetName == null) {
            return false;
        }
        initConfig();
        String targetTypeUpName = targetType.toUpperCase();
        String sourceTypeUpName = source.getClass().getName().toUpperCase();
        String Key1 = "*->" + targetTypeUpName;
        String Key2 = sourceTypeUpName + "->*";
        String Key3 = sourceTypeUpName + "->" + targetTypeUpName;
        IPopulate p = null;
        try {
            if (m_populatePlugins != null
                    && (m_populatePlugins.containsKey(Key1)
                            || m_populatePlugins.containsKey(Key2) || m_populatePlugins
                            .containsKey(Key3))) {
                if (m_populatePlugins.containsKey(Key1)) {
                    p = (IPopulate) m_populatePlugins.get(Key1);
                }
                if (m_populatePlugins.containsKey(Key2)) {
                    p = (IPopulate) m_populatePlugins.get(Key2);
                }
                if (m_populatePlugins.containsKey(Key3)) {
                    p = (IPopulate) m_populatePlugins.get(Key3);
                }
            } else {
                return false;
            }
            Object newProp = p.populate(source, targerObject, targetType, targetName);
            if (newProp == null)
                return true;
            BeanUtils.setProperty(targerObject, targetName, newProp);
        } catch (Exception e) {
            getIlog().error(
                    "In populate to " + targerObject + "'s Property("
                            + targetName + ") value from " + sourceTypeUpName,
                    e);
            return false;
        }
        return true;
    }

    /**
     * 得到日志记录驱动实例
     * 
     * @return ILog LOG驱动实例
     */
    private final static ILog getIlog() {
        return LogMgr.getLogger(PopulateUtil.class.getName());
    }

    /**
     * Bean注值工具方法
     * 
     * @param obj
     *            target bean
     * @param request
     *            source request
     * @return
     */
    public static boolean populate(Object obj, IRequest request) {
        try {
            RequestUtil.populate(obj, (HttpServletRequest) request.getServletRequest(), null, null);
        } catch (ServletException e) {
            getIlog().error("In populate bean: " + obj, e);
            return false;
        }
        return true;
    }


    /**
     * Bean复制工具方法
     * 
     * @param obj
     *            target bean
     * @param request
     *            source request
     * @param ignoreProperties
     *            ignore target bean's property name
     * @return
     */
    public static boolean populate(Object obj, IRequest request,
            String[] ignoreProperties) {
        try {
            RequestUtil.populate(obj, (HttpServletRequest) request.getServletRequest(), ignoreProperties);
        } catch (ServletException e) {
            getIlog().error("In populate bean: " + obj, e);
            return false;
        }
        return true;
    }

    /**
     * Bean复制工具方法
     * 
     * @param obj
     *            target bean
     * @param request
     *            source request
     * @param map
     *            rename property map: key: srcName, value:targetName
     * @return
     */
    public static boolean populate(Object obj, IRequest request, Map map) {
        try {
            RequestUtil.populate(obj, (HttpServletRequest) request
                    .getServletRequest(), map);
        } catch (ServletException e) {
            getIlog().error("In populate bean: " + obj, e);
            return false;
        }
        return true;
    }

    /**
     * Bean复制工具方法
     * 
     * @param obj
     *            target bean
     * @param request
     *            source request
     * @param map
     *            rename property map: key: srcName, value:targetName
     * @param ignoreProperties
     *            ignore target bean's property name
     * @return
     */
    public static boolean populate(Object obj, IRequest request, Map map, String[] ignoreProperties) {
        try {
            RequestUtil.populate(obj, (HttpServletRequest) request.getServletRequest(), map, ignoreProperties);
        } catch (ServletException e) {
            getIlog().error("In populate bean: " + obj, e);
            return false;
        }
        return true;
    }

    /**
     * Bean注值工具方法
     * 
     * @param obj
     *            target bean
     * @param rs
     *            source ResultSet
     * @return
     * @throws SQLException
     * @throws BeansException
     */
    public static boolean populate(Object obj, java.sql.ResultSet rs) throws SQLException {
        return populate(obj, rs, null, null);
    }

    /**
     * Bean注值工具方法
     * 
     * @param obj
     *            target bean
     * @param rs
     *            source ResultSet
     * @param map
     *            rename property map: key: srcName, value:targetName
     * @return
     * @throws SQLException
     * @throws BeansException
     */
    public static boolean populate(Object obj, java.sql.ResultSet rs, Map map) throws SQLException {
        return populate(obj, rs, map, null);
    }

    /**
     * Bean注值工具方法
     * 
     * @param obj
     *            target bean
     * @param rs
     *            source ResultSet
     * @param ignoreProperties
     *            ignore target bean's property name
     * @return
     * @throws SQLException
     */
    public static boolean populate(Object obj, java.sql.ResultSet rs, String[] ignoreProperties) throws SQLException {
        return populate(obj, rs, null, ignoreProperties);
    }

    /**
     * Bean注值工具方法
     * 
     * @param obj
     *            target bean
     * @param rs
     *            source ResultSet
     * @param map
     *            rename property map: key: srcName, value:targetName
     * @param ignoreProperties
     *            ignore target bean's property name
     * @return
     * @throws SQLException
     */
    public static boolean populate(Object obj, java.sql.ResultSet rs, Map map, String[] ignoreProperties) throws SQLException {
        try {
            List ignoreList = (ignoreProperties != null) ? Arrays.asList(ignoreProperties) : null;
            BeanWrapper bw = new BeanWrapperImpl(obj);
            PropertyDescriptor[] pd = bw.getPropertyDescriptors();
            Map amap = new HashMap();
            if (map != null) {
                Iterator iter = map.keySet().iterator();
                while (iter.hasNext()) {
                    Object key = iter.next();
                    Object value = map.get(key);
                    amap.put(value, key);
                }
            }
            List colNameList = new ArrayList();
            int num = rs.getMetaData().getColumnCount();
            for (int i = 0; i < num; i++) {
                String colName = rs.getMetaData().getColumnLabel(i + 1);
                if (colName != null) {
                    colNameList.add(colName.toUpperCase());
                }
            }

            for (int i = 0; i < pd.length; i++) {
                String name = pd[i].getName();
                String targetName = name;
                if (amap != null && amap.containsKey(targetName)) {
                    name = (String) amap.get(name);
                }
                if (!"class".equals(targetName) && colNameList.contains(name.toUpperCase())
                        && (ignoreProperties == null || (!ignoreList.contains(targetName)))) {
                    try {
                        Object res = rs.getObject(name);
                        Class targetTypeClazz = bw.getPropertyType(targetName);
                        if (superPopulate(targetTypeClazz.getName(), res, obj, targetName)) {
                            continue;
                        }
                        if (res != null && targetTypeClazz != null) {
                            BeanUtils.setProperty(obj, targetName, res);
                        }
                    } catch (Exception e) {
                        getIlog().error("In populate " + obj + " Property(" + targetName + ") value from Db ResultSet", e);
                    }
                }
            }
        } catch (Exception e) {
            getIlog().error("In populate " + obj + " value from Db ResultSet", e);
        }
        if (Helper.VALIDATE_AT_POPULATE_FROM_RESULTSET) {
            if (obj instanceof BaseValueObject) {
                validate((BaseValueObject) obj);
            }
        }
        return true;
    }

    /**
     * 调用Bean自身校验方法工具方法 if validate not pass throw new
     * BaseApplicationException(msg)
     *
     *  @param vo  BaseValueObject
     *
     */
    public static void validate(BaseValueObject vo) {
        vo.validate();
    }

    /**
     * 根据request变量中的参数值向Bean中注值
     *
     * @author lihong@use.com.cn
     *
     * @param obj
     *            被注值的Bean
     * @param request
     * @param prefix
     *            request变量中参数的前缀
     * @param postfix
     *            request变量中参数的后缀
     * @param effectiveProperties
     *            参与注值的有效属性
     */
    public static boolean populateFromRequest(Object obj, IRequest request, String prefix, String postfix, String[] effectiveProperties) {
        return populateFromRequest(obj, request, null, null, null, effectiveProperties);
    }

    /**
     * 根据request变量中的参数值向Bean中注值
     *
     * @author lihong@use.com.cn
     *
     * @param obj
     *            被注值的Bean
     * @param request
     * @param prefix
     *            request变量中参数的前缀
     * @param postfix
     *            request变量中参数的后缀
     * @param map
     *            rename property map: key: srcName, value:targetName
     * @param effectiveProperties
     *            参与注值的有效属性
     */
    public static boolean populateFromRequest(Object obj, IRequest request, String prefix, String suffix, Map map, String[] effectiveProperties) {
        try {
            List effectList = (effectiveProperties != null) ? Arrays.asList(effectiveProperties) : null;
            Enumeration names = request.getServletRequest().getParameterNames();
            List ignoreList = new ArrayList();
            while (names.hasMoreElements()) {
                String name = (String) names.nextElement();
                String stripped = name;
                if (prefix != null) {
                    if (!stripped.startsWith(prefix)) {
                        continue;
                    }
                    stripped = stripped.substring(prefix.length());
                }
                if (suffix != null) {
                    if (!stripped.endsWith(suffix)) {
                        continue;
                    }
                    stripped = stripped.substring(0, stripped.length()
                            - suffix.length());
                }
                if (!effectList.contains(stripped))
                    ignoreList.add(stripped);
            }
            String[] ignoreProperties = {};
            int ignoreLen = ignoreList.size();
            if (ignoreLen > 0) {
                ignoreProperties = new String[ignoreLen];
                for (int i = 0; i < ignoreLen; i++) {
                    ignoreProperties[i] = (String) ignoreList.get(i);
                }
            }
            RequestUtil.populate(obj, null, null, (HttpServletRequest) request.getServletRequest(), map, ignoreProperties);
        } catch (ServletException e) {
            getIlog().error("In populate bean: " + obj, e);
            return false;
        }
        return true;
    }

    /**
     * Bean复制工具方法
     *
     * @param source
     *            source bean
     * @param target
     *            target bean
     * @param ignoreProperties
     *            ignore target bean's property name
     * @return
     */
    private static boolean copyPropertiesWithBeanUtils(Object source,
            Object target, String[] ignoreProperties) {
        List ignoreList = (ignoreProperties != null) ? Arrays.asList(ignoreProperties) : null;
        BeanWrapper sourceBw = new BeanWrapperImpl(source);
        for (int i = 0; i < sourceBw.getPropertyDescriptors().length; i++) {
            PropertyDescriptor sourceDesc = sourceBw.getPropertyDescriptors()[i];
            String name = sourceDesc.getName();
            if ((ignoreProperties == null || (!ignoreList.contains(name)))) {
                try {
                    BeanUtils.copyProperty(target, name, source);
                } catch (Exception e) {
                    getIlog().error("BeanUtils.copyProperty(" + target + "," + name + "," + source + ")", e);
                }
            }
        }
        return true;
    }

    /**
     * Bean复制工具方法
     *
     * @param source
     *            source bean
     * @param target
     *            target bean
     * @param ignoreProperties
     *            ignore target bean's property name
     * @return
     */
    public static boolean copyProperties(Object source, Object target, String[] ignoreProperties) {
        return copyProperties(source, target, null, ignoreProperties);
    }

    /**
     * Bean复制工具方法
     *
     * @param source
     *            source bean
     * @param target
     *            target bean
     * @return
     */
    public static boolean copyProperties(Object source, Object target) {
        return copyProperties(source, target, null, null);
    }

    /**
     * Bean复制工具方法
     *
     * @param map
     *    rename property map: key: srcName, value:targetName
     * @return
     */
    public static boolean copyProperties(Object source, Object target, Map map) {
        return copyProperties(source, target, map, null);
    }

    /**
     * Bean复制工具方法 缺省将同名的属性均注值
     *
     * @param source
     *            source bean
     * @param target
     *            target bean
     * @param map
     *            rename property map: key: srcName, value:targetName
     * @param ignoreProperties
     *            ignore target bean's property name
     * @return
     */
    public static boolean copyProperties(Object source, Object target, Map map, String ignoreProperties[]) {
        List ignoreList = ignoreProperties == null ? null : Arrays.asList(ignoreProperties);
        if (target != null && target instanceof Map) {
            if (source instanceof Map) {
                Map m = (Map) source;
                for (Iterator iter = m.keySet().iterator(); iter.hasNext();) {
                    String name = (String) iter.next();
                    String targetName = name;
                    if (map != null && map.containsKey(name))
                        targetName = (String) map.get(name);
                    if ((ignoreProperties == null || !ignoreList
                            .contains(targetName))
                            && m.containsKey(name)) {

                        ((Map) target).put(targetName, m.get(name));
                    }

                }

            } else {
                try {
                    BeanWrapper sourceBw = new BeanWrapperImpl(source);
                    MutablePropertyValues values = new MutablePropertyValues();
                    for (int i = 0; i < sourceBw.getPropertyDescriptors().length; i++) {
                        try {
                            PropertyDescriptor sourceDesc = sourceBw
                                    .getPropertyDescriptors()[i];
                            String name = sourceDesc.getName();
                            String targetName = name;
                            if (map != null && map.containsKey(name)) {

                                targetName = (String) map.get(name);
                            }

                            if (!"class".equals(targetName)
                                    && (ignoreProperties == null || (!ignoreList
                                            .contains(targetName)))) {
                                ((Map) target).put(targetName, sourceBw
                                        .getPropertyValue(name));
                            }

                        } catch (Exception e) {
                            getIlog().info(
                                    e.getMessage()
                                            + "  BeanUtils.copyProperty(");
                        }

                    }
                } catch (Exception e) {
                    getIlog()
                            .error(e.getMessage() + " BeanUtils.copyProperty(");
                }
            }

        } else if (target != null) {

            BeanWrapper targetBw = new BeanWrapperImpl(target);

            if (source instanceof Map) {
                Map m = (Map) source;
                for (Iterator iter = m.keySet().iterator(); iter.hasNext();) {
                    String name = (String) iter.next();
                    String targetName = name;
                    if (map != null && map.containsKey(name))
                        targetName = (String) map.get(name);
                    if ((ignoreProperties == null || !ignoreList
                            .contains(targetName))
                            && m.containsKey(name)) {
                        try {
                            Class cls = targetBw.getPropertyType(targetName);
                            if (null == cls)
                                continue;
                            if (!superPopulate(cls.getName(), m.get(name),
                                    target, targetName))
                                BeanUtils.setProperty(target, targetName, m
                                        .get(name));
                        } catch (IllegalAccessException e) {
                            getIlog().error(
                                    e.getMessage() + " BeanUtils.copyProperty("
                                            + target + "," + targetName + ","
                                            + source + ")");
                        } catch (InvocationTargetException e) {
                            getIlog().error(
                                    e.getMessage() + " BeanUtils.copyProperty("
                                            + target + "," + targetName + ","
                                            + source + ")");
                        } catch (Exception e) {
                            getIlog().info(
                                    e.getMessage() + " BeanUtils.copyProperty("
                                            + target + "," + targetName + ","
                                            + source + ")");
                        }
                    }
                }

            } else {
                try {
                    BeanWrapper sourceBw = new BeanWrapperImpl(source);
                    PropertyValue value = null;
                    for (int i = 0; i < sourceBw.getPropertyDescriptors().length; i++) {
                        try {
                            PropertyDescriptor sourceDesc = sourceBw
                                    .getPropertyDescriptors()[i];
                            String name = sourceDesc.getName();
                            String targetName = name;
                            if (map != null && map.containsKey(name))
                                targetName = (String) map.get(name);
                            PropertyDescriptor targetDesc = targetBw
                                    .getPropertyDescriptor(targetName);

                            if (superPopulate(targetDesc.getPropertyType()
                                    .getName(),
                                    sourceBw.getPropertyValue(name), target,
                                    targetName)) {

                                continue;

                            }

                            BeanUtils.setProperty(target, targetName, sourceBw
                                    .getPropertyValue(name));

                        } catch (Exception e) {
                            getIlog()
                                    .info(
                                            e.getMessage()
                                                    + " BeanUtils.copyProperty(");
                        }

                    }
                } catch (Exception e) {
                    getIlog().error(
                            e.getMessage() + "  BeanUtils.copyProperty(");
                }
            }

        }
        if (Helper.VALIDATE_AT_POPULATE) {
            if (target instanceof BaseValueObject) {
                validate((BaseValueObject) target);
            }
        }
        return true;
    }

    /**
     * Bean复制工具方法
     *
     * @param source
     *            source bean must be a instance of target
     * @param target
     *            target bean
     * @return
     */
    public static boolean copyPropertiesFromRelative(Object source,
            Object target) {
        org.springframework.beans.BeanUtils.copyProperties(source, target);
        return true;
    }

    /**
     * Bean复制工具方法
     *
     * @param source
     *            source bean must be a instance of target
     * @param target
     *            target bean
     * @param ignoreProperties
     *            array of property names to ignore
     * @return
     */
    public static boolean copyPropertiesFromRelative(Object source,
            Object target, String[] ignoreProperties) {
        org.springframework.beans.BeanUtils.copyProperties(source, target,
                ignoreProperties);
        return true;
    }

    /**
     * Bean复制工具方法
     *
     * @param srcObj
     *            source bean must be a instance of target
     * @param targetObj
     *            target bean
     * @param map
     *            key: targetName, value:srcName
     * @return
     */
    public static boolean copyPropertiesFromRelative(Object srcObj,
            Object targetObj, Map map) {

        BeanWrapper sourceBw = new BeanWrapperImpl(
                srcObj);
        BeanWrapper targetBw = new BeanWrapperImpl(
                targetObj);
        MutablePropertyValues values = new MutablePropertyValues();

        Iterator iter = map.keySet().iterator();

        while (iter.hasNext()) {

            String targetName = (String) iter.next();

            String srcName = (String) map.get(targetName);

            PropertyDescriptor sourceDesc = sourceBw
                    .getPropertyDescriptor(srcName);

            PropertyDescriptor targetDesc = targetBw
                    .getPropertyDescriptor(targetName);

            if (targetDesc.getWriteMethod() != null
                    && targetDesc.getReadMethod() != null) {

                values
                        .addPropertyValue(new PropertyValue(
                                targetName, sourceBw.getPropertyValue(srcName)));

            }

        }

        targetBw.setPropertyValues(values);

        return true;
    }

    /**
     * Bean复制工具方法 缺省将同名的属性均注值
     *
     * @param srcObj
     *            source bean must be a instance of target
     * @param targetObj
     *            target bean
     * @param map
     *            key: targetName, value:srcName
     * @return
     */
    public static boolean copyPropertiesFromRelativeAll(Object srcObj,
            Object targetObj, Map map) {

        org.springframework.beans.BeanUtils.copyProperties(srcObj, targetObj);

        BeanWrapper sourceBw = new BeanWrapperImpl(
                srcObj);
        BeanWrapper targetBw = new BeanWrapperImpl(
                targetObj);
        MutablePropertyValues values = new MutablePropertyValues();

        Iterator iter = map.keySet().iterator();

        while (iter.hasNext()) {

            String targetName = (String) iter.next();

            String srcName = (String) map.get(targetName);

            PropertyDescriptor sourceDesc = sourceBw
                    .getPropertyDescriptor(srcName);

            PropertyDescriptor targetDesc = targetBw
                    .getPropertyDescriptor(targetName);

            if (targetDesc.getWriteMethod() != null
                    && targetDesc.getReadMethod() != null) {

                values
                        .addPropertyValue(new PropertyValue(
                                targetName, sourceBw.getPropertyValue(srcName)));

            }

        }

        targetBw.setPropertyValues(values);

        return true;
    }

}