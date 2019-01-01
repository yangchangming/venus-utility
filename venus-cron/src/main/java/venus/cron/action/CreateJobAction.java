/*
 * 创建日期 2007-3-19
 * CreateBy zhangbaoyu
 */
package venus.cron.action;

import venus.cron.bo.JobBo;
import venus.frames.base.action.BaseAction;
import venus.frames.base.action.IForward;
import venus.frames.base.action.IRequest;
import venus.frames.base.action.IResponse;
import venus.frames.base.bean.DefaultForm;
import venus.frames.i18n.util.LocaleHolder;
import venus.frames.mainframe.action.HttpRequest;
import venus.frames.mainframe.log.ILog;
import venus.frames.mainframe.log.LogMgr;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangbaoyu
 *
 */
public class CreateJobAction extends BaseAction {
    private static ILog log = LogMgr.getLogger(CreateJobAction.class);

    public IForward service(DefaultForm form, IRequest request,
                            IResponse response) throws Exception {
           
                JobBo jobBo = new JobBo();
                jobBo.setGroupName(request.getParameter("jobGroup"));
                jobBo.setJobName(request.getParameter("jobName"));
                jobBo.setClassName(request.getParameter("className"));
                jobBo.setDescription(request.getParameter("description"));
                
                //验证类是否存在；
                try {
    			    Class.forName(jobBo.getClassName());
    			} catch (ClassNotFoundException e) {
    				//String errorInfo  =  URLEncoder.encode("错误:找不到类\"" + jobBo.getClassName() + "\",请检查类定义和类路径是否正确！","GBK");
    				String errorInfo  = LocaleHolder.getMessage("udp.quartz.Class_Not_Found", new Object[]{jobBo.getClassName()});
    				request.setAttribute("errorInfo",errorInfo);
    				return request.findForward("classNotFoundException");
    			}
    			String[] paraNames=request.getParameterValues("jobParameter");
                if(paraNames!=null&&paraNames.length>0){
                    String[] paraValues=request.getParameterValues("jobPValue");
                    Map paraMap = new HashMap();
	                for(int i=0;i<paraNames.length;i++){
	                    paraMap.put(paraNames[i],paraValues[i]);
	                }
	                jobBo.setParaMap(paraMap);
                }
                               
                //将新建的作业暂时放到session中
                HttpSession session = ((HttpRequest)request).getSession();
                session.setAttribute("jobdefine",jobBo);
                log.debug("创建任务定义："+jobBo.getJobName()+"     "+jobBo.getClassName()+"        "+jobBo.getDescription());
                return request.findForward("createJob");
                
        
    }
}
