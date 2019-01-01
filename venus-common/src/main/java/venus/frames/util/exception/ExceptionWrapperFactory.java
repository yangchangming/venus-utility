package venus.frames.util.exception;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import venus.frames.i18n.util.LocaleHolder;
import venus.frames.mainframe.util.PathMgr;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

/**
 * @author sundaiyong
 *
 * 
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ExceptionWrapperFactory {
	
	protected static final Log logger = LogFactory.getLog(ExceptionWrapperFactory.class);
	
	
	public static final String EXCEPTION_INFO_CONFIG_FOLDER = "/exception-infos/" ;
	

	private Map<String, Map<Class, ExceptionInfo>> localeExceptionInfos = new HashMap<String, Map<Class,ExceptionInfo>>();
	
	//工厂的单例
	private static ExceptionWrapperFactory instance = new ExceptionWrapperFactory();
	/*  此方法支持同时从classpath和conf/exception-info/*.xml中找异常配置文件，但这样的话不好做国际化，所以去掉从classpath找文件的功能。
	protected ExceptionWrapperFactory() {
		
		ListableBeanFactory bf = null;
		
		
		 //* Author:wj
	//	 * Date:20050919
		// * Reason:增加了exception-info配置文件读取某一目录中的多个配置文件，同时兼容classpath中的配置文件exception-info.xml。
		 
		try {
			Resource resource = loadResource(EXCEPTION_INFO_PATH);
			if (resource == null || !resource.exists()) {
				//throw new BeanDefinitionStoreException("Unable to locate file [" + EXCEPTION_INFO_PATH  + "]");
				
				try{
					String strExceptionPath = PathMgr.getRealRootPath()+PathMgr.getConfPath()+EXCEPTION_INFO_CONFIG_FILENAMES;
					if (strExceptionPath.startsWith("/")) {
						strExceptionPath = "file://" + strExceptionPath;
					}
					bf  = new FileSystemXmlApplicationContext(strExceptionPath);
					
				}
				catch (Exception ex) {
					logger.warn("Error loading exception infos from config file. Message: " + ex.getMessage());
					exceptionInfos = new HashMap(0);
				}
				
			}else{
				
				BeanFactory bfp  = null;
					
				try{
					
					bfp  = new FileSystemXmlApplicationContext(PathMgr.getRealRootPath()+PathMgr.getConfPath()+EXCEPTION_INFO_CONFIG_FILENAMES);//new XmlBeanFactory(PathMgr.getResourceAsStream(BEANFACTORY_CONFIG_FILENAME));
					bf = new XmlBeanFactory(resource,bfp);
					
				}
				catch (Exception ex) {
					logger.warn("Error loading exception infos from config file. Message: " + ex.getMessage());
					
					bf = new XmlBeanFactory(resource);	
					
				}

				
			
			}
			
			if( bf == null ) {
				
				exceptionInfos = new HashMap(0);
				
				return;
				
			}
			
//			ListableBeanFactory bf = new FileSystemXmlApplicationContext(EXCEPTION_INFO_PATH);
			
			String[] beanNames = bf.getBeanNamesForType(ExceptionInfo.class);
			
			for (int i=0; i<beanNames.length; i++) {
				ExceptionInfo exceptionInfo = (ExceptionInfo)bf.getBean(beanNames[i]);
				exceptionInfos.put(exceptionInfo.getClazz(), exceptionInfo);
			}
		}
		catch (BeanDefinitionStoreException ex) {
			logger.warn("Error loading exception infos from config file. Message: " + ex.getMessage());
			exceptionInfos = new HashMap(0);
		}
	}
	
	protected Resource loadResource(String path) {
		return new ClassPathResource(path);
	}
	*/
	
	protected ExceptionWrapperFactory() {
	    try{
            String strExceptionPath = PathMgr.getRealRootPath()+PathMgr.getConfPath()+EXCEPTION_INFO_CONFIG_FOLDER;
            File configFileFolder = new File(strExceptionPath);
            File[] subFolders = configFileFolder.listFiles();
            int subFolderCount = 0;
            for(File subFolder:subFolders){
                //只查找conf/exception-info/目录的子目录下的异常配置文件，如conf/exception-info/zh/*.xml、conf/exception-info/en/*.xml
                if(subFolder.isDirectory()){
                	//判断目录名是否为有效的locale串，不是则跳过
                	String languageName = subFolder.getName();
                	if(languageName.split("_").length>1)
                		languageName = languageName.split("_")[0];
                	if(!ArrayUtils.contains(Locale.getISOLanguages(),languageName))
                		continue;
                    initExceptionMap(subFolder.getName(),subFolder);
                    subFolderCount ++;
                }
            }
            //为了兼容以前没有国际化的项目，如果没有语言目录，则加载本目录下的文件作为默认语言的map。
            if(subFolderCount == 0){
                initExceptionMap(Locale.getDefault().getLanguage(),configFileFolder);
            }
            
        }
        catch (Exception ex) {
            logger.warn("Error loading exception infos from config file. Message: " + ex.getMessage());
            localeExceptionInfos = new HashMap(0);
        }
	}


    /**
     * @param language
     * @param folder
     */
    private void initExceptionMap(String language,File folder) {
    	String filePath = folder.getAbsolutePath()+"/*.xml";
        if (filePath.startsWith("/")) {
        	filePath = "file://" + filePath;
        }
        ListableBeanFactory bf  = new FileSystemXmlApplicationContext(filePath);
        String[] beanNames = bf.getBeanNamesForType(ExceptionInfo.class);
        Map<Class, ExceptionInfo> exceptionInfos = new HashMap<Class, ExceptionInfo>();
        for (int i=0; i<beanNames.length; i++) {
            ExceptionInfo exceptionInfo = (ExceptionInfo)bf.getBean(beanNames[i]);
            exceptionInfos.put(exceptionInfo.getClazz(), exceptionInfo);
        }
        localeExceptionInfos.put(folder.getName(), exceptionInfos);
    }
	
	
	/**
	 * 在exception-info.xml搜索最匹配的异常信息
	 * @param cause
	 * @return return ExceptionInfo,如果未找到匹配的记录,return null.
	 */
	protected ExceptionInfo getExceptionInfo(Throwable cause) {
	    String language = LocaleHolder.getLocale().getLanguage();
	    //根据当前locale决定要找的异常map
	    Map<Class, ExceptionInfo> exceptionInfos = localeExceptionInfos.get(language);
	    if(exceptionInfos==null||exceptionInfos.size()==0){
	        logger.error("Not found exception config for locale:" + language + ",please config it in " +
	                PathMgr.getRealRootPath()+PathMgr.getConfPath()+EXCEPTION_INFO_CONFIG_FOLDER+language);
	        return null;
	    }
		ExceptionInfo exceptionInfo = (ExceptionInfo)exceptionInfos.get(cause.getClass());
		/**
		 * 如果不能直接找到对应的异常信息,查找其最近的父类
		 */
		if (exceptionInfo == null) {
			Class childClass = cause.getClass();
			Class parentClass = null;
			for (Iterator iter = exceptionInfos.keySet().iterator(); iter.hasNext();) {
				Class clazz = (Class)iter.next();
				//如果是cause的父类
				if (clazz.isAssignableFrom(childClass)) {
					//如果parentClass是clazz的父类
					if (parentClass == null || parentClass.isAssignableFrom(clazz)) {
						parentClass = clazz;
					}
				}
			}
			if (parentClass != null) {
				exceptionInfo = (ExceptionInfo)exceptionInfos.get(parentClass);
			}
		}
		return exceptionInfo;
	}
	
	/**
	 * 
	 * @param cause
	 * @return 异常信息包装类
	 */
	public static ExceptionWrapper getExceptionWrapper(Throwable cause) {
		ExceptionInfo exceptionInfo = ExceptionWrapperFactory.getInstance().getExceptionInfo(cause);
		return new ExceptionWrapper(exceptionInfo, cause);
	}
	
	/**
	 * @return Returns the instance.
	 */
	public static ExceptionWrapperFactory getInstance() {
		return instance;
	}
}
