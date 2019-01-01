/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.oa.authority.auauthorizelog.bs;

import venus.oa.authority.auauthorizelog.vo.AuAuthorizeLogVo;
import venus.oa.util.SqlBuilder;
import venus.pub.lang.OID;

import java.util.List;
import java.util.Map;

/**
 * @author zangjian
 *
 */
public interface IAuAuthorizeLogBS {
    
    /**
     * 查询总记录数，带查询条件
     * @param sql
     * @return
     */
    public int getRecordCount(SqlBuilder sql);
    
    /**
     * 通过查询条件获得所有的VO对象列表
     * @param sql
     * @return 查询到的VO列表
     */
    public List queryByCondition(SqlBuilder sql);
    
    /**
     * 添加记录到权限日志表
     * @param vo
     * @return
     */
    public OID insert(AuAuthorizeLogVo vo);
    
    /**
     * 通过查询条件获得所有的VO对象列表，带翻页，带排序字符
     * 
     * @param no 当前页数
     * @param size 每页记录数
     * @param queryCondition 查询条件
     * @param orderStr 排序字符
     * @return 查询到的VO列表
     */
    public List queryByCondition(int no, int size, String queryCondition, String orderStr);
    
    /**
     * 查询总记录数，带查询条件
     * 
     * @param queryCondition 查询条件
     * @return 总记录数
     */
    public int getRecordCount(String queryCondition);
    
    /**
     * 
     * 功能: 根据partyId和资源类型查询该用户所拥有的权限，包括它继承的
     *
     * @param partyId 用户partyId
     * @param sType 资源类型
     * @return
     */
    public Map getAuByPartyId(String partyId, String sType, String auHisTag);
    
    /**
     * 
     * 功能: 根据访问者Code数组和资源类型查询该访问者自身拥有的权限+在同一团体关系类型内它所继承的权限
     *      如果资源类型为null，则查询全部资源类型的权限
     *
     * @param visiCodes 访问者编号数组
     * @param resType 资源类型
     * @return
     */
    public Map getAuByVisitorCode(String visiCode, String resType, String auHisTag);
    
    /**
     * 
     * 功能: 根据访问者Code数组和资源类型查询该访问者自身拥有的数据权限+在同一团体关系类型内它所继承的权限
     *     剔除历史数据权限
     *
     * @param visiCodes 访问者编号数组
     * @param resType 资源类型
     * @return
     */
    public Map getOrgAuByVisitorCodeWithOutHistory(String visiCode, String resType, String auHisTag);
    
}

