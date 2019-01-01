/*
 * 创建日期 2007-3-23
 * CreateBy zhangbaoyu
 */
package venus.cron.bo;

import venus.frames.base.vo.BaseValueObject;

import java.util.Map;

/**
 * @author zhangbaoyu
 *
 */
public class JobBo extends BaseValueObject {

    private String jobName;
    private String groupName;
    private String className;
    private String description;
    private Map paraMap;
    
    /**
     * @return 返回 className。
     */
    public String getClassName() {
        return className;
    }
    /**
     * @param className 要设置的 className。
     */
    public void setClassName(String className) {
        this.className = className;
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
     * @return 返回 groupName。
     */
    public String getGroupName() {
        return groupName;
    }
    /**
     * @param groupName 要设置的 groupName。
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    /**
     * @return 返回 jobName。
     */
    public String getJobName() {
        return jobName;
    }
    /**
     * @param name 要设置的 jobName。
     */
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }
    /**
     * @return 返回 paraMap。
     */
    public Map getParaMap() {
        return paraMap;
    }
    /**
     * @param paraMap 要设置的 paraMap。
     */
    public void setParaMap(Map paraMap) {
        this.paraMap = paraMap;
    }
}
