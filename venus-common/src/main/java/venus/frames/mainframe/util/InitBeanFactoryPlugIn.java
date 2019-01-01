package venus.frames.mainframe.util;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import venus.frames.base.action.DefaultServlet;
import venus.frames.base.action.DefaultServletException;
import venus.frames.base.action.IRequest;
import venus.frames.base.action.IResponse;
import venus.frames.base.action.plugin.IServletPlugin;
import venus.frames.mainframe.log.ILog;
import venus.frames.mainframe.log.LogMgr;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * spring context initial plugin
 */
public class InitBeanFactoryPlugIn implements IServletPlugin {

    private ILog log = LogMgr.getLogger(InitBeanFactoryPlugIn.class);

    private ContextLoader contextLoader;

    /* (non-Javadoc)
     * @see venus.frames.mainframe.base.action.plugin.IServletPlugin#init(venus.frames.mainframe.base.action.DefaultServlet)
     */
    public void init(DefaultServlet servlet) throws DefaultServletException {

        // use try catch to handle exception if CLASS_CAN_NO_FOUND==true, throws exception if CLASS_CAN_NO_FOUND==false
//        String strClassCanNoFound = servlet.getServletConfig().getInitParameter("CLASS_CAN_NO_FOUND");

        String strClassCanNoFound = "true";
        if (strClassCanNoFound != null) {
            if ("0".equalsIgnoreCase(strClassCanNoFound) || "false".equalsIgnoreCase(strClassCanNoFound) ||
                    "n".equalsIgnoreCase(strClassCanNoFound) || "no".equalsIgnoreCase(strClassCanNoFound)) {
                BeanFactoryHolder.CLASS_CAN_NO_FOUND = false;
            }
        }
        try {
            java.lang.reflect.Field fld = DefaultXmlBeanDefinitionParser.class.getField("CLASS_CAN_NO_FOUND");
            if (fld != null)
                fld.setBoolean(DefaultXmlBeanDefinitionParser.class, BeanFactoryHolder.CLASS_CAN_NO_FOUND);
        } catch (Exception e) {
            log.error("'org.springframework.beans.factory.xml.DefaultXmlBeanDefinitionParser.class' is from spring, So can not ignore 'Bean' defined in spring_config that is 'CLASS NOT FOUND'");
        }
        this.contextLoader = createContextLoader();
        this.contextLoader.initWebApplicationContext(servlet.getServletContext());
        WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servlet.getServletContext());
        BeanFactoryHolder.setBeanFactory(wac);

        if (wac !=null){
            LogMgr.getLogger("venus.frames.mainframe.util.PathMgr").info("");
            LogMgr.getLogger("venus.frames.mainframe.util.PathMgr").info("------------------------------------------------------------------------");
            LogMgr.getLogger("venus.frames.mainframe.util.PathMgr").info("WebApplicationContext Building Success");
            LogMgr.getLogger("venus.frames.mainframe.util.PathMgr").info("------------------------------------------------------------------------");
            LogMgr.getLogger("venus.frames.mainframe.util.PathMgr").info("");
        }
    }


    /* (non-Javadoc)
     * @see venus.frames.mainframe.base.action.plugin.IServletPlugin#service(venus.frames.mainframe.base.action.DefaultServlet, venus.frames.mainframe.base.action.IRequest, venus.frames.mainframe.base.action.IResponse)
     */
    public void service(DefaultServlet servlet, IRequest request, IResponse response) throws DefaultServletException {
        log.info("Attempt to call service method on ContextLoaderServlet as [" + ((HttpServletRequest)request).getContextPath() + "] was ignored");
//        try {
//            (HttpServletResponse)response.sendError(HttpServletResponse.SC_BAD_REQUEST);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }


    /* (non-Javadoc)
     * @see venus.frames.mainframe.base.action.plugin.IServletPlugin#destroy(venus.frames.mainframe.base.action.DefaultServlet)
     */
    public void destroy(DefaultServlet servlet) {
        this.contextLoader.closeWebApplicationContext(servlet.getServletContext());
    }


    /**
     * Create the ContextLoader to use. Can be overridden in subclasses.
     *
     * @return the new ContextLoader
     */
    protected ContextLoader createContextLoader() {
        return new ContextLoader();
    }

    /**
     * Return the ContextLoader used by this servlet.
     */
    public ContextLoader getContextLoader() {
        return contextLoader;
    }


}
