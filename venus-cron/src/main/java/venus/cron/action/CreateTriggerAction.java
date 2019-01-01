/*
 * 创建日期 2007-3-19
 * CreateBy zhangbaoyu
 */
package venus.cron.action;

import org.quartz.*;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author zhangbaoyu
 *  
 */
public class CreateTriggerAction extends BaseAction {
    private static ILog log = LogMgr.getLogger(CreateTriggerAction.class);

    public IForward service(DefaultForm form, IRequest request,
                            IResponse response) throws Exception {

        HttpSession session = ((HttpRequest) request).getSession();
        JobBo jobBo = (JobBo) session.getAttribute("jobdefine");
        session.removeAttribute("jobdefine");
        JobDetail job = null;
        Trigger trigger = null;
        Calendar calendar = Calendar.getInstance();
        if (jobBo != null) {
            job = new JobDetail(jobBo.getJobName(), jobBo.getGroupName(), Class
                    .forName(jobBo.getClassName()));
            job.setDescription(jobBo.getDescription());
            if (jobBo.getParaMap() != null) {
                job.setJobDataMap(new JobDataMap(jobBo.getParaMap()));
            }
        }

        log.debug("创建任务定义对象：" + jobBo.getJobName());
        log.debug("开始创建触发器================");
        String plan = request.getParameter("plan");
        try {
            if (IConstants.ONCE.equals(plan)) {
                //执行一次
                Date[] quartzDate = quartzDate(request, "1");
                trigger = new SimpleTrigger(jobBo.getJobName(), jobBo
                        .getGroupName(), quartzDate[0]);
            } else if (IConstants.PERDAY.equals(plan)) {
                //按天执行
                Date[] quartzDate = quartzDate(request, "2");
                calendar.setTime(quartzDate[0]);
                String dayPlan = request.getParameter("dayPlan");
                if (IConstants.PERDAY.equals(dayPlan)) {
                    trigger = TriggerUtils.makeDailyTrigger(jobBo.getJobName(),
                            calendar.get(Calendar.HOUR_OF_DAY), calendar
                                    .get(Calendar.MINUTE));
                    trigger.setGroup(jobBo.getGroupName());
                    trigger.setStartTime(quartzDate[0]);
                    trigger.setEndTime(quartzDate[1]);
                } else if (IConstants.PERWORKDAY.equals(dayPlan)) {
                    String cronExpression = "0 "
                            + calendar.get(Calendar.MINUTE) + " "
                            + calendar.get(Calendar.HOUR_OF_DAY) + " ? * 2-6";
                    trigger = new CronTrigger(jobBo.getJobName(), jobBo
                            .getGroupName(), cronExpression);
                    trigger.setStartTime(quartzDate[0]);
                    trigger.setEndTime(quartzDate[1]);
                } else if (IConstants.PERDAYS.equals(dayPlan)) {
                    long repeatInterval = Integer.parseInt(request
                            .getParameter("days"))
                            * 24 * 60 * 60 * 1000;

                    trigger = new SimpleTrigger(jobBo.getJobName(), jobBo
                            .getGroupName(), -1, repeatInterval);
                    trigger.setStartTime(quartzDate[0]);
                    if (null != quartzDate[1] && !"".equals(quartzDate[1])) {
                        calendar.setTime(quartzDate[1]);
                        calendar
                                .setTimeInMillis(calendar.getTimeInMillis() + 499);
                        trigger.setEndTime(calendar.getTime());
                    }
                    //		            long repeatInterval =
                    // Integer.parseInt(request.getParameter("days"));
                    //	                String cronExpression = "0 " +
                    // calendar.get(Calendar.MINUTE) + " " +
                    // calendar.get(Calendar.HOUR_OF_DAY) + "
                    // ?/"+repeatInterval+" * *";
                    //	                trigger = new
                    // CronTrigger(jobBo.getJobName(),jobBo.getGroupName(),cronExpression);
                }
            } else if (IConstants.PERWEEK.equals(plan)) {
                //  按周执行
                Date[] quartzDate = quartzDate(request, "3");
                calendar.setTime(quartzDate[0]);
                String[] weekDays = request.getParameterValues("weekDay");
                StringBuffer dayOfWeek = new StringBuffer();
                if (null == weekDays || 0 == weekDays.length) {
                    dayOfWeek.append("*");
                } else {
                    for (int i = 0; i < weekDays.length; i++) {
                        if (i == 0) {
                            dayOfWeek.append(weekDays[i]);
                        } else {
                            dayOfWeek.append("," + weekDays[i]);
                        }
                    }
                }
                //long repeatInterval =
                // (Integer.parseInt(request.getParameter("weeks"))-1)*7;//间隔多少周;
                String cronExpression = "0 " + calendar.get(Calendar.MINUTE)
                        + " " + calendar.get(Calendar.HOUR_OF_DAY) + " ? * "
                        + dayOfWeek;
                trigger = new CronTrigger(jobBo.getJobName(), jobBo
                        .getGroupName(), cronExpression);
                trigger.setStartTime(quartzDate[0]);
                trigger.setEndTime(quartzDate[1]);
            } else if (IConstants.PERMONTH.equals(plan)) {
                // 按月执行
                Date[] quartzDate = quartzDate(request, "4");
                calendar.setTime(quartzDate[0]);
                String dayOrweek = request.getParameter("dayOrweek");
                String date = request.getParameter("date");
                String weekOfMonth = request.getParameter("weekOfMonth");
                String dayOfWeek = request.getParameter("dayOfWeek");
                String[] months = request.getParameterValues("month");
                StringBuffer monthsBuffer = new StringBuffer();
                if (null == months || 0 == months.length) {
                    monthsBuffer.append("*");
                } else {
                    for (int i = 0; i < months.length; i++) {
                        if (i == 0) {
                            monthsBuffer.append(months[i]);
                        } else {
                            monthsBuffer.append("," + months[i]);
                        }
                    }
                }
                if (dayOrweek.equals("day")) {
                    String cronExpression = "0 "
                            + calendar.get(Calendar.MINUTE) + " "
                            + calendar.get(Calendar.HOUR_OF_DAY) + " " + date
                            + " " + monthsBuffer + " ?";
                    log.debug("cronExpression = " + cronExpression);
                    trigger = new CronTrigger(jobBo.getJobName(), jobBo
                            .getGroupName(), cronExpression);
                } else {
                    String cronExpression = "0 "
                            + calendar.get(Calendar.MINUTE) + " "
                            + calendar.get(Calendar.HOUR_OF_DAY) + " ? "
                            + monthsBuffer + " " + dayOfWeek;
                    if (weekOfMonth.endsWith("L"))
                        cronExpression = cronExpression + weekOfMonth;
                    else {
                        cronExpression = cronExpression + "#" + weekOfMonth;
                    }
                    trigger = new CronTrigger(jobBo.getJobName(), jobBo
                            .getGroupName(), cronExpression);
                }
                trigger.setStartTime(quartzDate[0]);
                trigger.setEndTime(quartzDate[1]);
            } else if (IConstants.CUSTOMER.equals(plan)) {
                String cronExpression = request.getParameter("cronExpression");
               try{
                trigger = new CronTrigger(jobBo.getJobName(), jobBo
                        .getGroupName(), cronExpression);
               }catch (java.text.ParseException  e) {
                    System.out.println(e.getMessage());
                   throw new IllegalArgumentException();
                }
            }
        } catch (IllegalArgumentException  e) {
            System.out.println(e.getMessage());
            String errorInfo =  LocaleHolder.getMessage("udp.quartz.Parameter_Illegal");;
		    request.setAttribute("errorInfo", errorInfo);
		    return request.findForward("objectAlreadyExistsException");
        }
        //获取调度器，并将新建的作业触发器加入调度器中
        //            SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = GapSchedulerFactory.getScheduler();
        List jobList = new ArrayList();
        String[] jobGroups = sched.getJobGroupNames();
        for (int i = 0; i < jobGroups.length; i++) {
            String[] jobNames = sched.getJobNames(jobGroups[i]);
            for (int j = 0; j < jobNames.length; j++) {
                //排除工作流的任务
                if(jobNames[j].length()==16&&jobNames[j].startsWith("{")
                        &&jobNames[j].endsWith("}")&&jobNames[j].indexOf("-")==9){
                    continue;
                }
                JobDetail jobDetail = sched.getJobDetail(jobNames[j], jobGroups[i]);
                jobList.add(jobDetail);
            }
        }
        if (job != null && trigger != null) {
            try {
                sched.scheduleJob(job, trigger);
            } catch (ObjectAlreadyExistsException e) {
                String errorInfo = LocaleHolder.getMessage("udp.quartz.Job_Error", new Object[]{job.getName(), job.getGroup()});
                request.setAttribute("errorInfo", errorInfo);
                return request.findForward("objectAlreadyExistsException");
            } catch (SchedulerException se) {
                String errorInfo = LocaleHolder.getMessage("udp.quartz.Parameter_Illegal");
    		    request.setAttribute("errorInfo", errorInfo);
    		    return request.findForward("objectAlreadyExistsException");
            }

        }
        // test code
        if (!sched.isStarted()) {
            sched.start();
        }
        return request.findForward("listJobs");
    }

//    private SimpleTrigger onceTrigger(long ldate) {
//        SimpleTrigger trigger = new SimpleTrigger("trigger1", "group1",
//                new Date(ldate));
//        return trigger;
//    }

    private Date[] quartzDate(IRequest request, String str) throws Exception {
        Date[] quartzDate = new Date[2]; //存放开始日期和结束日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String beginDate = request.getParameter("beginDate" + str);
        String beginTime = request.getParameter("beginTime" + str);
        String endDate = request.getParameter("endDate" + str);
        String endTime = request.getParameter("endTime" + str);
        Date runTime = new Date();
        //System.out.println(sdf.format(runTime));
        String dateStr = null;
        if (beginDate != null && !beginDate.equals("")) {
            if (beginTime != null && !beginTime.equals("")) {
                dateStr = beginDate + " " + beginTime + ":00";
                //将String类型的日期和时间转换成long型时间，然后创建触发器
                runTime = sdf.parse(dateStr);
            } else {
                runTime = sdf.parse(beginDate
                        + sdf.format(runTime).substring(10));
            }

        }
        quartzDate[0] = runTime;
        if (endDate != null && !endDate.equals("") && endTime != null
                && !endTime.equals("")) {
            dateStr = endDate + " " + endTime + ":00";
            //将String类型的日期和时间转换成long型时间，然后创建触发器
            quartzDate[1] = sdf.parse(dateStr);
        }
        return quartzDate;
    }
}