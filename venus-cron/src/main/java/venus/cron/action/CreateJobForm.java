package venus.cron.action;

import org.apache.commons.betwixt.io.BeanReader;
import org.quartz.Scheduler;
import venus.cron.bo.DefinitionManager;
import venus.cron.extend.QuartzUtil;
import venus.frames.base.action.DefaultDispatchAction;
import venus.frames.base.action.IForward;
import venus.frames.base.action.IRequest;
import venus.frames.base.action.IResponse;
import venus.frames.base.bean.DefaultForm;
import venus.frames.i18n.util.LocaleHolder;
import venus.frames.mainframe.log.ILog;
import venus.frames.mainframe.log.LogMgr;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;


/**
 * @author zhangbaoyu
 *
 */
public class CreateJobForm extends DefaultDispatchAction {
    private static ILog log = LogMgr.getLogger(CreateJobForm.class);


    public IForward doCreateJobForm(DefaultForm form, IRequest request,
                                    IResponse response) throws Exception {
        //      获取调度器
        Scheduler sched = GapSchedulerFactory.getScheduler();
        if(sched.isShutdown()){
        	String errorInfo = LocaleHolder.getMessage("udp.quartz.Console_Is_Stopped");
        	request.setAttribute("errorInfo", errorInfo);
        	return request.findForward("schedulerStateError");
        }
        return request.findForward("createJobForm");
    }

    public IForward doChooseJobDefines(DefaultForm form, IRequest request,
                                       IResponse response) throws Exception {
    	Scheduler sched = GapSchedulerFactory.getScheduler();
    	 String[] jobGroups = sched.getJobGroupNames();
         //DefinitionManager def = (DefinitionManager)request.getServletRequest().geth.getAttribute(QuartzUtil.JOB_DEFINITIONS_PROP);
         HttpServletRequest hsRequest= (HttpServletRequest)request.getServletRequest();
         ServletContext context = hsRequest.getSession().getServletContext();
         DefinitionManager def = (DefinitionManager)context.getAttribute(QuartzUtil.JOB_DEFINITIONS_PROP);
         Date lastDate = (Date)context.getAttribute(QuartzUtil.JOB_DEFINITIONS_DATE);
         File defFile = new File((String)context.getAttribute(QuartzUtil.JOB_DEFINITIONS_FILE));
         if(def==null||!lastDate.equals(new Date(defFile.lastModified()))){
             log.info("reload jobDefinitions.xml");
 	        BeanReader beanReader = new BeanReader();
 	        // Configure the reader
 	        beanReader.getXMLIntrospector().setAttributesForPrimitives(false);
 	        beanReader.getBindingConfiguration().setMapIDs(false);
 	        beanReader.registerBeanClass("JobDefinitions",DefinitionManager.class);
 	        //defFile = new File(((HttpRequest)request).getRealPath("/WEB-INF/conf/JobDefinitions.xml"));
 	        
 	        def = (DefinitionManager) beanReader.parse(defFile);
             context.setAttribute(QuartzUtil.JOB_DEFINITIONS_PROP,def);
         }
         request.setAttribute("jobGroups",jobGroups);
         log.debug("jobGroups.length = "+jobGroups.length);
    	  return request.findForward("chooseJobDefines");
    }
    
    public IForward doCreateCronExpression(DefaultForm form, IRequest request,
                                           IResponse response) throws Exception {
        
        return request.findForward("createCronExpression");
    }
}
