package venus.cron.action;

import org.quartz.Scheduler;
import venus.cron.bo.SchedulerBo;
import venus.frames.base.action.DefaultDispatchAction;
import venus.frames.base.action.IForward;
import venus.frames.base.action.IRequest;
import venus.frames.base.action.IResponse;
import venus.frames.base.bean.DefaultForm;
import venus.frames.i18n.util.LocaleHolder;
import venus.frames.mainframe.log.ILog;
import venus.frames.mainframe.log.LogMgr;

import java.text.SimpleDateFormat;

/**
 * @author zhangbaoyu
 *
 */
public class ScheduleInfoForm extends DefaultDispatchAction {
    private static ILog log = LogMgr.getLogger(ScheduleInfoForm.class);
    public IForward service(DefaultForm form, IRequest request,
                            IResponse response) throws Exception {
        String command = request.getParameter("operation");
        log.debug("ScheduleInfoForm command = "+command);
        //获取调度器
        //SchedulerFactory sf = GapSchedulerFactory.getSchedulerFactory();
        Scheduler sched = GapSchedulerFactory.getScheduler();
        SchedulerBo schedulerBo = new SchedulerBo();
        if(command!=null&&!command.equals("")){
            if (command.equals("start")) {
            	sched = GapSchedulerFactory.getNewScheduler();
                sched.start();
                schedulerBo.setState(LocaleHolder.getMessage("udp.quartz.Run"));
            }else if (command.equals("recover")) {
            	 sched.start();
                 schedulerBo.setState(LocaleHolder.getMessage("udp.quartz.Run"));
            }  else if (command.equals("pause")) {
                sched.standby();
                schedulerBo.setState(LocaleHolder.getMessage("udp.quartz.Suspend"));
            } else if (command.equals("waitAndStop")) {
                sched.shutdown(false);
                schedulerBo.setState(LocaleHolder.getMessage("udp.quartz.Stop"));
            } 
        }else{
        	if( sched.isShutdown()){
	        	schedulerBo.setState(LocaleHolder.getMessage("udp.quartz.Stop"));
	        } else 	if( sched.isInStandbyMode()){
	        	schedulerBo.setState(LocaleHolder.getMessage("udp.quartz.Suspend"));
	        } else if( sched.isStarted()){
	        	schedulerBo.setState(LocaleHolder.getMessage("udp.quartz.Run"));
	        }
	    }
        schedulerBo.setSchedulerName(sched.getSchedulerName());
        schedulerBo.setNumJobsExecuted(String.valueOf(sched.getMetaData().numJobsExecuted()));

        if (sched.getMetaData().jobStoreSupportsPersistence()) {
            schedulerBo.setPersistenceType(LocaleHolder.getMessage("udp.quartz.Database_Storage"));
        } else {
            schedulerBo.setPersistenceType(LocaleHolder.getMessage("udp.quartz.Memory_Storage")); // mp
        }
        if(sched.getMetaData().runningSince()!=null&&!"".equals(sched.getMetaData().runningSince())){
        	schedulerBo.setRunningSince(String.valueOf((new SimpleDateFormat("yyyy-MM-dd k:mm:ss")).format(sched.getMetaData().runningSince())));
        }else{
        	schedulerBo.setRunningSince("");
        }

        schedulerBo.setThreadPoolSize(String.valueOf(sched.getMetaData().getThreadPoolSize()));
        schedulerBo.setVersion(sched.getMetaData().getVersion());
        schedulerBo.setSummary(sched.getMetaData().getSummary());

        request.setAttribute("schedulerBo",schedulerBo);
        return request.findForward("scheduleInfo");
    }

}
