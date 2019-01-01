package venus.cron.action;

import gap.commons.digest.DigestLoader;
import gap.license.exception.InvalidLicenseException;
import org.apache.commons.betwixt.io.BeanReader;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.xml.sax.SAXException;
import venus.cron.bo.DefinitionManager;
import venus.cron.extend.QuartzUtil;
import venus.cron.servlet.QuartzInitializerServlet;
import venus.frames.mainframe.log.ILog;
import venus.frames.mainframe.log.LogMgr;
import venus.frames.mainframe.util.PathMgr;
import venus.pub.util.ReflectionUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.beans.IntrospectionException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;


/**
 * Definition extends QuartInitializerServlet by calling its super methods, but
 * also loading JobDefinitions into application context
 * 
 * @author zhangbaoyu
 */

public class DefinitionInitializer extends QuartzInitializerServlet {

    private static ILog log = LogMgr.getLogger(DefinitionInitializer.class);
    public static String DEFAULT_DEFINITION_FILE = "/JobDefinitions.xml";
    public static String CONFIG_FILE;

    public void init(ServletConfig cfg) throws ServletException{

    	//check license
//		DigestLoader loader = DigestLoader.getLoader();
//		if (loader.isValid() && Math.random() > 0.2) {
//			validateLicense(loader);
//		} else if (!loader.isValid()) {
//			validateLicense(loader);
//		}
    	
    	CONFIG_FILE = PathMgr.getRealRootPath()+cfg.getInitParameter("config-file");
        super.init(cfg);
        ServletContext context = cfg.getServletContext();
        String definitionPath = this.getInitParameter("definition-file");
        GapSchedulerFactory.init(cfg);
        Scheduler sched = GapSchedulerFactory.getScheduler(); 
        try {
			sched.start();
		} catch (SchedulerException e1) {
			 e1.printStackTrace();
		}
        //String propertiesFile = this.getInitParameter("org.quartz.properties");
        //System.setProperty("org.quartz.properties",propertiesFile);
        BeanReader beanReader = new BeanReader();
        // Configure the reader
        beanReader.getXMLIntrospector().setAttributesForPrimitives(false);
        if (definitionPath != null && definitionPath != "") {
            context.setAttribute(QuartzUtil.JOB_DEFINITIONS_FILE, context.getRealPath(definitionPath));
            // Now we parse the xml
            try {
                // Register beans so that betwixt knows what the xml is to be
                // converted to
                beanReader.registerBeanClass("JobDefinitions",DefinitionManager.class);
                File defFile = new File(context.getRealPath(definitionPath));
                if (!defFile.exists()) {
                    //Alternate user definitions file, not specfic or does not exist.  Default resource /JobDefinitions.xml will be tried.
                    defFile = new File(context.getRealPath("/WEB-INF/conf/JobDefinitions.xml"));
                    //log.error("Attempting to read definitions from file " + this.getClass().getResource(DEFAULT_DEFINITION_FILE).getFile());
//                    URL url = this.getClass().getResource(DEFAULT_DEFINITION_FILE);
//                    if (url == null) {
//                        log.error("resource " + DEFAULT_DEFINITION_FILE+ " not found");
//                    }
//                    defFile = new File(url.getFile());
                } else {
                    log.info("Reading definitions from " + definitionPath);
                }
                DefinitionManager defs = (DefinitionManager) beanReader.parse(defFile);  
                Date lastModified = new Date(defFile.lastModified());
                if (defs != null) {
                    context.setAttribute(QuartzUtil.JOB_DEFINITIONS_PROP, defs);
                	context.setAttribute(QuartzUtil.JOB_DEFINITIONS_DATE, lastModified);
//        	        Map defMap = defs.getDefinitions();
//        	        for(Iterator itor=defMap.entrySet().iterator();itor.hasNext();){
//        	            Entry entry = (Entry) itor.next();
//        	            JobDefinition jobDef = (JobDefinition)entry.getValue();
//        	            log.debug("---------name: "+jobDef.getName());
//        	            log.debug("---------class: "+jobDef.getClassName());
//        	            log.debug("---------description: "+jobDef.getDescription());
//        	            log.debug("==============================");
//        	        }
                    log.info(defs.getDefinitions().size()+ " Definition(s) loaded from config file");
                } else {
                    log.error("no definitions found");
                }
            } catch (IntrospectionException e) {
                log.error("error reading definitions", e);
            } catch (IOException e) {
                log.error("IO error reading definitions", e);
            } catch (SAXException e) {
                log.error("error reading definitions", e);
            }
        } else {
            log.error("Error definition-file init parameter not specified");
        }
    }

    public void destroy() {
        this.getServletContext().setAttribute("Util.JOB_DEFINITIONS_PROP", null);
        super.destroy();
    }

	/**
	 * @param loader
	 */
	private void validateLicense(DigestLoader loader) {
		boolean valid = true;
		try {
			Class cls = loader.findClass();
			Method m = ReflectionUtils.findMethod(cls, "checkLicense",
					new Class[] {});
			valid = new Boolean(ReflectionUtils.invokeMethod(m, null,
					new Object[] {}).toString()).booleanValue();
		} catch (RuntimeException e) {
			loader.setValid(false);
			throw e;
		}
		if (!valid) {
			loader.setValid(false);
			throw new InvalidLicenseException();
		} else {
			loader.setValid(true);
		}
	}

}