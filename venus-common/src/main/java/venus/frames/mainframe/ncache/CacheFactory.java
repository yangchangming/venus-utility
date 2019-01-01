package venus.frames.mainframe.ncache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import venus.frames.mainframe.util.ConfMgr;
import venus.frames.mainframe.util.DefaultConfReader;

//import venus.frames.mainframe.ncache.memcached.MemcachedManagerExtend;

public class CacheFactory {

    private ICacheManager cacheManager = null;

    private String cacheMgrImplName = null;

    private static CacheFactory instance = null;

    public final static String DEFAULT_CACHEMANAGER_NAME = "venus.frames.mainframe.ncache.impl.UdpCacheManager";

    private static Log log = LogFactory.getLog(CacheFactory.class);

    private CacheFactory() {
    }

    private ICacheManager buildCacheManager(String strImplClassName) {
        ICacheManager cacheManager = null;

        if (strImplClassName == null) {
            log.error("buildCache(...)：参数为空");
            return null;
        }

        // 得到CacheImpl的CLASS对象并初始化,此处注意strImplClassName类应该在classpath下
        try {
            ClassLoader contextClassLoader = Thread.currentThread()
                    .getContextClassLoader();
            Class clazz = contextClassLoader.loadClass(strImplClassName);
            cacheManager = (ICacheManager) clazz.newInstance();
        } catch (ClassNotFoundException cnfe) {
            log.warn("buildCache(...):  ClassNotFoundException", cnfe);
            return null;
        } catch (IllegalAccessException iae) {
            log.warn("buildCache(...):   IllegalAccessException", iae);
            return null;
        } catch (InstantiationException ie) {
            log.warn("buildCache(...):  InstantiationException", ie);
            return null;
        }

        return cacheManager;
    }

    protected void loadConf() {
        // 以本类名为标识从XML文件中提取cache实现类的名字，
        // 配置项名“ImplName”.
        try {
            DefaultConfReader dcr = new DefaultConfReader(ConfMgr.getNode(this
                    .getClass().getName()));
            this.cacheMgrImplName = dcr.readStringAttribute("cacheMgrImplName");
        } catch (NullPointerException be) {
            log.error("loadConf(...) : NullPointerException");
            this.cacheMgrImplName = null;
        }
    }

    public static ICacheManager createCacheManager() {

        // 初始工厂类并加载配置数据
        CacheFactory cf = getInstance();

        if (cf.cacheManager != null)
            return cf.cacheManager;

        // 如果列表中没有cacheName的Cacher实例，则构建一个实例
        return cf.buildCacheManager(cf.getCacheMgrImplName());
    }

    protected String getCacheMgrImplName() {

        if (this.cacheMgrImplName == null) {

            // 从配置文件提取数据
            loadConf();

            // 如果未提取到数据 则使用缺省数据
            if (this.cacheMgrImplName == null || this.cacheMgrImplName.equals("")) {
                this.cacheMgrImplName = DEFAULT_CACHEMANAGER_NAME;
            }

        }
        return this.cacheMgrImplName;
    }


    /**
     * 获取cache服务类，系统启动时会初始化cache服务类
     *
     * @return
     */
    public static ICacheManager getCacheManager() {
        // 初始工厂类并加载配置数据
        CacheFactory cf = getInstance();

        // 如果列表中保存有cacheName的Cacher实例，则返回此实例
        if (cf.cacheManager != null) {
            return cf.cacheManager;
        } else {
            // 新构建成一个Cache实例
            return cf.buildCacheManager(cf.getCacheMgrImplName());
        }
    }


    /**
     * 获取memcached扩展cache服务类
     *
     * @return
     */
//    public static MemcachedManagerExtend getExtendCacheManager() {
//
//        CacheFactory cf = getInstance();
//
//        if(cf.cacheManager == null){
//            log.info("initial cacheManager.....");
//            cf.cacheManager = cf.buildCacheManager(cf.getCacheMgrImplName());
//        }
//        return MemcachedManagerExtend.getInstance();
//    }


    public synchronized static CacheFactory getInstance() {

        if (instance == null) {
            instance = new CacheFactory();

            // 加载配置数据
            try {
                instance.loadConf();
            } catch (Exception e) {
                log.error("Can't get the implement class name of cacher, so you have to provide the impl class when cache data");
            }
        }
        return instance;
    }


    public static void main(String[] args) {

        while (true) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
