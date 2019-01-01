/*
 * 创建日期 2007-3-20
 * CreateBy zhangbaoyu
 */
package venus.cron.jobs;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 * @author zhangbaoyu
 *
 */
public class HelloJob implements Job {
	//JFrame frame = new JFrame("My Window");
//    private ILog log = LogMgr.getLogger(HelloJob.class);
    public  void execute(JobExecutionContext context)
    throws JobExecutionException {
        String jobName = context.getJobDetail().getFullName();
        JobDataMap data = context.getJobDetail().getJobDataMap();
        System.out.println(new Date()+" hi: "+jobName + " is executed!!!!!!!!");
        //JOptionPane.showMessageDialog(frame, jobName+" is exectued on "+new Date(System.currentTimeMillis()).toString());
        for(Iterator iter = data.entrySet().iterator(); iter.hasNext();){
            Map.Entry entry = (Map.Entry)iter.next();
            System.out.println("parameter--"+entry.getKey()+" = "+entry.getValue());
        }
    }
}
