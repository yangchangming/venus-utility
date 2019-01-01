/*
 * 创建日期 2007-3-20
 * CreateBy zhangbaoyu
 */
package venus.cron.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author zhangbaoyu
 *
 */
public class ReportJob implements Job {
    
//    private ILog log = LogMgr.getLogger(ReportJob.class);
    public  void execute(JobExecutionContext context)
    throws JobExecutionException {
    	//IReportService reportJobService = (IReportService)Helper.getBean("reportJobService");
    	//reportJobService.generateReportDocument("schedule report job call!!!I'm coming!");
    }

}
