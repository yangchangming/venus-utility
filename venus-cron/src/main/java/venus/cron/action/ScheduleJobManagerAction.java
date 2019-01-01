package venus.cron.action;

import org.quartz.Scheduler;
import venus.frames.base.action.BaseAction;
import venus.frames.base.action.IForward;
import venus.frames.base.action.IRequest;
import venus.frames.base.action.IResponse;
import venus.frames.base.bean.DefaultForm;
import venus.frames.mainframe.log.ILog;
import venus.frames.mainframe.log.LogMgr;

public class ScheduleJobManagerAction extends BaseAction {
    private static ILog log = LogMgr.getLogger(ScheduleJobManagerAction.class);
    public IForward service(DefaultForm form, IRequest request,
                            IResponse response) throws Exception {
        String command = request.getParameter("command");
        String jobName = request.getParameter("triggerName");
        String groupName = request.getParameter("triggerGroup");
        if(groupName.equals("DEFAULT"))groupName=null;
        // 获取调度器
//        SchedulerFactory sf = new StdSchedulerFactory();
//        Scheduler scheduler = sf.getScheduler();
        Scheduler scheduler = GapSchedulerFactory.getScheduler();
        
        if (command.equals("start")) {
            log.info("start job: '"+jobName+"' |||||||||||||||||||||||||");
            scheduler.triggerJob(jobName, groupName);

        } else if (command.equals("stop")) {
            log.info("stop job: '"+jobName+"' |||||||||||||||||||||||||");
            //scheduler.unscheduleJob(triggerName, triggerGroup);
            scheduler.unscheduleJob(jobName, groupName);
        } else if (command.equals("pause")) {
            log.info("pause job: '"+jobName+"' |||||||||||||||||||||||||");
            scheduler.pauseJob(jobName, groupName);

        } else if (command.equals("resume")) {
            log.info("resume job: '"+jobName+"' |||||||||||||||||||||||||");
            scheduler.resumeJob(jobName, groupName);

        } else if (command.equals("deleteJob")) {
            log.info("delete job: '"+jobName+"' |||||||||||||||||||||||||");
            scheduler.deleteJob(jobName, groupName);

        }
        return request.findForward("listJobs");
    }
}