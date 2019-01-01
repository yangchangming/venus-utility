/*
 * 创建日期 2007-3-19
 * CreateBy zhangbaoyu
 */
package venus.cron.action;

import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.comparators.NullComparator;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
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
public class QueryJobDefinesAction extends BaseAction {

    public IForward service(DefaultForm form, IRequest request,
                            IResponse response) throws Exception {
        Scheduler sched = GapSchedulerFactory.getScheduler();
        if(sched.isShutdown()){
        	String errorInfo = LocaleHolder.getMessage("udp.quartz.Console_Is_Stopped");
        	request.setAttribute("errorInfo", errorInfo);
        	return request.findForward("schedulerStateError");
        }
        String condition = request.getParameter("condition");
        String jobName = request.getParameter("jobName");
        String groupName = request.getParameter("groupName");
        String description = request.getParameter("description");
        
        String orderKey = request.getParameter("VENUS_ORDER_KEY");
        //条件回写
		Map conditionMap = conditionBack(request,response);
		request.setAttribute("writeBackFormValues", conditionMap);
        //从数据库中获取调度作业集
        List jobList = new ArrayList();
        String[] jobGroups = sched.getJobGroupNames();
        for (int i = 0; i < jobGroups.length; i++) {
            String[] jobNames = sched.getJobNames(jobGroups[i]);
            if(jobGroups[i].equals("DEFAULT")){
                for (int j = 0; j < jobNames.length; j++) {
                    if(condition!=null&&condition.equals("true")){
                        if(!jobName.equals("")&&!jobName.equals(jobNames[j])){
                            continue;
                        }
                    }
                    //排除工作流的任务
                    if(jobNames[j].length()==16&&jobNames[j].startsWith("{")
                    		&&jobNames[j].endsWith("}")&&jobNames[j].indexOf("-")==9){
                    	continue;
                    }
                    JobDetail jobDetail = sched.getJobDetail(jobNames[j],
                            jobGroups[i]);
                    if(condition!=null&&condition.equals("true")){
                        if(!groupName.equals("")&&!groupName.equals(jobDetail.getGroup())){
                            continue;
                        }
                        if(!description.equals("")&&!description.equals(jobDetail.getDescription())){
                            continue;
                        }
                    }
                    jobList.add(jobDetail);
                }
            }else if(jobGroups[i].equals("WarningTriggerGroupName")){
                continue;
            }else{
                for (int j = 0; j < jobNames.length; j++) {
                    if(condition!=null&&condition.equals("true")){
                        if(!jobName.equals("")&&!jobName.equals(jobNames[j])){
                            continue;
                        }
                    }
                    JobDetail jobDetail = sched.getJobDetail(jobNames[j],
                            jobGroups[i]);
                    if(condition!=null&&condition.equals("true")){
                        if(!groupName.equals("")&&!groupName.equals(jobDetail.getGroup())){
                            continue;
                        }
                        if(!description.equals("")&&!description.equals(jobDetail.getDescription())){
                            continue;
                        }
                    }
                    jobList.add(jobDetail);
                }
            }

        }
//      处理排序
        if(orderKey==null||"".equals(orderKey)){
            ComparatorChain comparatorChain = new ComparatorChain();
            comparatorChain.addComparator(new BoComparator("group"));
            comparatorChain.addComparator(new BoComparator("name"));
            comparatorChain.addComparator(new BoComparator("jobClass"));
            Collections.sort(jobList,new NullComparator(comparatorChain));
        }else if(orderKey.endsWith(" DESC")){
            Comparator reverseComparator = new ReverseComparator(new BoComparator(orderKey.substring(0,orderKey.indexOf(" DESC"))));
            Collections.sort(jobList,new NullComparator(reverseComparator));
        }else {
            if(orderKey.endsWith(" ASC")) orderKey = orderKey.substring(0,orderKey.indexOf(" ASC"));
            Comparator beanComparator = new BoComparator(orderKey);
            Collections.sort(jobList,new NullComparator(beanComparator));
        }
        //处理翻页
        jobList = PageTool.queryWorkListByPage(jobList,request,response);

        request.setAttribute("jobList", jobList);
        return request.findForward("listJobDefines");
    }
	/**
	 * 回写输入条件
	 * @param request
	 * @param response
	 * @return
	 */
	public Map conditionBack(IRequest request,
                             IResponse response)throws Exception {
		
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