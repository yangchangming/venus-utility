/*
 * 创建日期 2007-3-22
 * CreateBy zhangbaoyu
 */
package venus.cron.bo;

import org.quartz.Trigger;
import venus.frames.base.vo.BaseValueObject;

import java.sql.Timestamp;
import java.util.Map;

/**
 * @author zhangbaoyu
 *
 */
public class TriggerBo extends BaseValueObject {
    
    private String name;
    private String group;
    private String jobName;
    private String jobGroup;
    private boolean isVolatile; 
    private String description ;
    private String nextFireTime;
    private String previousFireTime;
    private int priority; 
    private String state;
    private String type;
    private String startTime;
    private String endTime;
    private String calenderName;
    private int misFireInstr; 
    private Map jobData;
    public TriggerBo (Trigger trigger){
        name = trigger.getName();
        group = trigger.getGroup();
        jobName = trigger.getJobName();
        jobGroup = trigger.getJobGroup();
        isVolatile = trigger.isVolatile();
        description = trigger.getDescription();
        if(trigger.getNextFireTime()!=null){
            nextFireTime = new Timestamp(trigger.getNextFireTime().getTime()).toString().substring(0,19);
        }
        if(trigger.getPreviousFireTime()!=null){
            previousFireTime = new Timestamp(trigger.getPreviousFireTime().getTime()).toString().substring(0,19);
        }
        priority = trigger.getPriority();
        startTime = new Timestamp(trigger.getStartTime().getTime()).toString().substring(0,19);
        if(trigger.getEndTime()!=null){
            endTime = new Timestamp(trigger.getEndTime().getTime()).toString().substring(0,19);
        }
        calenderName = trigger.getCalendarName();
        misFireInstr = trigger.getMisfireInstruction();
        jobData = trigger.getJobDataMap();
    }
   
    /**
     * @return 返回 calenderName。
     */
    public String getCalenderName() {
        return calenderName;
    }
    /**
     * @param calenderName 要设置的 calenderName。
     */
    public void setCalenderName(String calenderName) {
        this.calenderName = calenderName;
    }
    /**
     * @return 返回 description。
     */
    public String getDescription() {
        return description;
    }
    /**
     * @param description 要设置的 description。
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * @return 返回 endTime。
     */
    public String getEndTime() {
        return endTime;
    }
    /**
     * @param endTime 要设置的 endTime。
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    /**
     * @return 返回 group。
     */
    public String getGroup() {
        return group;
    }
    /**
     * @param group 要设置的 group。
     */
    public void setGroup(String group) {
        this.group = group;
    }
    /**
     * @return 返回 isVolatile。
     */
    public boolean isVolatile() {
        return isVolatile;
    }
    /**
     * @param isVolatile 要设置的 isVolatile。
     */
    public void setVolatile(boolean isVolatile) {
        this.isVolatile = isVolatile;
    }
    /**
     * @return 返回 jobData。
     */
    public Map getJobData() {
        return jobData;
    }
    /**
     * @param jobData 要设置的 jobData。
     */
    public void setJobData(Map jobData) {
        this.jobData = jobData;
    }
    /**
     * @return 返回 jobGroup。
     */
    public String getJobGroup() {
        return jobGroup;
    }
    /**
     * @param jobGroup 要设置的 jobGroup。
     */
    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }
    /**
     * @return 返回 jobName。
     */
    public String getJobName() {
        return jobName;
    }
    /**
     * @param jobName 要设置的 jobName。
     */
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }
    /**
     * @return 返回 misFireInstr。
     */
    public int getMisFireInstr() {
        return misFireInstr;
    }
    /**
     * @param misFireInstr 要设置的 misFireInstr。
     */
    public void setMisFireInstr(int misFireInstr) {
        this.misFireInstr = misFireInstr;
    }
    /**
     * @return 返回 name。
     */
    public String getName() {
        return name;
    }
    /**
     * @param name 要设置的 name。
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return 返回 nextFireTime。
     */
    public String getNextFireTime() {
        return nextFireTime;
    }
    /**
     * @param nextFireTime 要设置的 nextFireTime。
     */
    public void setNextFireTime(String nextFireTime) {
        this.nextFireTime = nextFireTime;
    }
    /**
     * @return 返回 prevFireTime。
     */
    public String getPreviousFireTime() {
        return previousFireTime;
    }
    /**
     * @param prevFireTime 要设置的 prevFireTime。
     */
    public void setPreviousFireTime(String previousFireTime) {
        this.previousFireTime = previousFireTime;
    }
    /**
     * @return 返回 priority。
     */
    public int getPriority() {
        return priority;
    }
    /**
     * @param priority 要设置的 priority。
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }
    /**
     * @return 返回 startTime。
     */
    public String getStartTime() {
        return startTime;
    }
    /**
     * @param startTime 要设置的 startTime。
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    /**
     * @return 返回 state。
     */
    public String getState() {
        return state;
    }
    /**
     * @param state 要设置的 state。
     */
    public void setState(String state) {
        this.state = state;
    }
    /**
     * @return 返回 type。
     */
    public String getType() {
        return type;
    }
    /**
     * @param type 要设置的 type。
     */
    public void setType(String type) {
        this.type = type;
    }
}
