package venus.frames.mainframe.util;


import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import venus.frames.mainframe.log.ILog;
import venus.frames.mainframe.log.LogMgr;

/**
 * @author sundaiyong
 */
public class BeanFactoryHolder {

    //public static String BEANFACTORY_CONFIG_FILENAME = "/WEB-INF/conf/applicationContext.xml" ;
    private static ILog log = LogMgr.getLogger(BeanFactoryHolder.class);
    public static boolean CLASS_CAN_NO_FOUND = true;
    public static String BEANFACTORY_CONFIG_FILENAMES = "/applicationContext/*.xml";
    private static BeanFactory beanFactory = null;

    /**
     * get bean factory
     * @return
     */
    public static BeanFactory getBeanFactory() {
        if (beanFactory == null) {
            try {
                java.lang.reflect.Field fld = DefaultXmlBeanDefinitionParser.class.getField("CLASS_CAN_NO_FOUND");
                if (fld != null)
                    fld.setBoolean(DefaultXmlBeanDefinitionParser.class, CLASS_CAN_NO_FOUND);
            } catch (Exception e) {
                log.error("'org.springframework.beans.factory.xml.DefaultXmlBeanDefinitionParser.class' is from spring, So can not ignore 'Bean' defined in spring_config that is 'CLASS NOT FOUND'");
            }
            String thisAppXmlPath = PathMgr.getRealRootPath() + PathMgr.getConfPath() + BEANFACTORY_CONFIG_FILENAMES;
            if (thisAppXmlPath.startsWith("/")) {
                thisAppXmlPath = "file://" + thisAppXmlPath;
            }
            beanFactory = new FileSystemXmlApplicationContext(thisAppXmlPath);
        }
        return beanFactory;
    }

    /**
     * BeanFactory setter method;
     *
     * @param args
     */
    public static void setBeanFactory(BeanFactory args) {
        beanFactory = args;
    }

    /**
     * Get bean by string parameter bean name
     *
     * @param name
     * @return Bean
     */
    public static Object getBean(String name) {
        if (beanFactory == null) {
            if (getBeanFactory() == null) return null;
        }
        return beanFactory.getBean(name);
    }
}





