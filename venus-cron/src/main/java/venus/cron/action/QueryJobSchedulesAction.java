package venus.cron.action;

import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.comparators.NullComparator;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import venus.cron.bo.TriggerBo;
import venus.cron.extend.BoComparator;
import venus.cron.extend.PageTool;
import venus.frames.base.action.BaseAction;
import venus.frames.base.action.IForward;
import venus.frames.base.action.IRequest;
import venus.frames.base.action.IResponse;
import venus.frames.base.bean.DefaultForm;
import venus.frames.i18n.util.LocaleHolder;

import java.util.*;

/**
 * @author zhangbaoyu
 *  
 */
public class QueryJobSchedulesAction extends BaseAction {
    
    //private ILog log = LogMgr.getLogger(QueryJobSchedulesAction.class);
    public IForward service(DefaultForm form, IRequest request, IResponse response) throws Exception {
        Scheduler sched = GapSchedulerFactory.getScheduler();
        if(sched.isShutdown()){
        	String errorInfo = LocaleHolder.getMessage("udp.quartz.Console_Is_Stopped");
        	request.setAttribute("errorInfo", errorInfo);
        	return request.findForward("schedulerStateError");
        }
//        String condition = request.getParameter("condition");
        String jobName = request.getParameter("jobName");
        String goupName = request.getParameter("groupName");
        String startDate = request.getParameter("startDate");
        
		String orderKey = request.getParameter("VENUS_ORDER_KEY");

        //String state = request.getParameter("state");
        
        //条件回写
		Map conditionMap = conditionBack(request,response);
		request.setAttribute("writeBackFormValues", conditionMap);
        //从数据库中获取调度作业集
        List triggerList = new ArrayList();
        String[] triggerGroups = sched.getTriggerGroupNames();
        //Arrays.sort(triggerGroups);
        for (int i = 0; i < triggerGroups.length; i++) {
            if (goupName != null && !"".equals(goupName)&& !goupName.equals(triggerGroups[i])) {
                continue;
            }
            String[] triggerNames = sched.getTriggerNames(triggerGroups[i]);
            if(triggerGroups[i].equals("DEFAULT")){
            	 // Arrays.sort(triggerNames);
                for (int j = 0; j < triggerNames.length; j++) {
                    if (jobName != null && !"".equals(jobName)&& !jobName.equals(triggerNames[j])) {
                        continue;
                    }
                    //排除工作流的任务
                    if(triggerNames[j].length()==16&&triggerNames[j].startsWith("{")
                    		&&triggerNames[j].endsWith("}")&&triggerNames[j].indexOf("-")==9){
                    	continue;
                    }
                    Trigger trigger = sched.getTrigger(triggerNames[j], triggerGroups[i]);
                    if (trigger==null){  // 此处因为偶尔会得不到trigger对象而得到空添加 20071207;
                        continue;
                    }
                    TriggerBo triggerBo = new TriggerBo(trigger);
                    triggerBo.setState(stateEncode(sched.getTriggerState(triggerBo.getName(),triggerBo.getGroup())));
                    if (startDate != null && !"".equals(startDate)&& !startDate.equals(triggerBo.getNextFireTime().substring(0,10))) {
                        continue;
                    }    
                    triggerList.add(triggerBo);
                }
            }else if(triggerGroups[i].equals("WarningTriggerGroupName")){
                continue;
            }else if(triggerGroups[i].equals(Scheduler.DEFAULT_MANUAL_TRIGGERS)){
                //手动运行一条任务
                continue;
            }else{
            	 // Arrays.sort(triggerNames);
                for (int j = 0; j < triggerNames.length; j++) {
                    if (jobName != null && !"".equals(jobName)&& !jobName.equals(triggerNames[j])) {
                        continue;
                    }
                    Trigger trigger = sched.getTrigger(triggerNames[j], triggerGroups[i]);
                    if (trigger==null){  // 此处因为偶尔会得不到trigger对象而得到空添加 20071207;
                        continue;
                    }
                    TriggerBo triggerBo = new TriggerBo(trigger);
                    triggerBo.setState(stateEncode(sched.getTriggerState(triggerBo.getName(),triggerBo.getGroup())));
                    if (startDate != null && !"".equals(startDate)&& !startDate.equals(triggerBo.getNextFireTime().substring(0,10))) {
                        continue;
                    }    
                    triggerList.add(triggerBo);
                }
            }
          
        }
        //处理排序
        if(orderKey==null||"".equals(orderKey)){
            ComparatorChain comparatorChain = new ComparatorChain();
            comparatorChain.addComparator(new BoComparator("jobGroup"));
            comparatorChain.addComparator(new BoComparator("jobName"));
            Collections.sort(triggerList,new NullComparator(comparatorChain));
        }else if(orderKey.endsWith(" DESC")){
            Comparator reverseComparator = new ReverseComparator(new BoComparator(orderKey.substring(0,orderKey.indexOf(" DESC"))));
            Collections.sort(triggerList,new NullComparator(reverseComparator));
        }else {
            if(orderKey.endsWith(" ASC")) orderKey = orderKey.substring(0,orderKey.indexOf(" ASC"));
            Comparator beanComparator = new BoComparator(orderKey);
            Collections.sort(triggerList,new NullComparator(beanComparator));
        }
        
        //处理翻页
        triggerList = PageTool.queryWorkListByPage(triggerList, request, response);
       
        request.setAttribute("triggerList", triggerList);
        return request.findForward("listJobs");
    }
    
    private String stateEncode(int a){
        switch(a){
        	case -1:
        		return LocaleHolder.getMessage("udp.quartz.None");
        	case 0:
        		return LocaleHolder.getMessage("udp.quartz.Normal");
        	case 1:
        		return LocaleHolder.getMessage("udp.quartz.Paused");
        	case 2:
        		return LocaleHolder.getMessage("udp.quartz.Complete");
        	case 3:
        		return LocaleHolder.getMessage("udp.quartz.Error");
        	case 4:
        		return LocaleHolder.getMessage("udp.quartz.Blocked");
        	default:
        	    return String.valueOf(a);
        	
        }
    }
    
	/**
	 * 回写输入条件
	 * @param request
	 * @param response
	 * @return
	 */
	public Map conditionBack(IRequest request, IResponse response)throws Exception {
		
		String jobName = request.getParameter("jobName");
		String groupName = request.getParameter("groupName");
		String startDate = request.getParameter("startDate");

		//查询条件回写
		Map map = new HashMap();
		if (jobName != null && !"".equals(jobName)) {
			map.put("jobName", jobName);
		}
		if (groupName != null && !"".equals(groupName)) {
			map.put("groupName", groupName);
		}
		if (startDate != null && !"".equals(startDate)) {
			map.put("startDate", startDate);
		}
		return map;
	}
	

}