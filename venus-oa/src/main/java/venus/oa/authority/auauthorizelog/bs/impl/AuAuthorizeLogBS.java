package venus.oa.authority.auauthorizelog.bs.impl;

import org.springframework.stereotype.Service;
import venus.oa.authority.auauthorize.bs.IAuAuthorizeBS;
import venus.oa.authority.auauthorizelog.bs.IAuAuthorizeLogBS;
import venus.oa.authority.auauthorizelog.dao.IAuAuthorizeLogDao;
import venus.oa.authority.auauthorizelog.util.IConstants;
import venus.oa.authority.auauthorizelog.vo.AuAuthorizeLogVo;
import venus.oa.organization.aupartyrelation.bs.IAuPartyRelationBs;
import venus.oa.organization.aupartyrelation.vo.AuPartyRelationVo;
import venus.oa.util.ProjTools;
import venus.oa.util.SqlBuilder;
import venus.frames.base.bs.BaseBusinessService;
import venus.frames.mainframe.util.Helper;
import venus.pub.lang.OID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zangjian
 *
 */
@Service
public class AuAuthorizeLogBS extends BaseBusinessService implements IAuAuthorizeLogBS,IConstants {

    private IAuAuthorizeLogDao dao = null;
    
    public IAuAuthorizeBS getAuthorizeBs() {
        return (IAuAuthorizeBS) Helper.getBean(venus.oa.authority.auauthorize.util.IConstants.BS_KEY);
    }  

    /**
     * @return the dao
     */
    public IAuAuthorizeLogDao getDao() {
        return dao;
    }

    /**
     * @param dao the dao to set
     */
    public void setDao(IAuAuthorizeLogDao dao) {
        this.dao = dao;
    }
    
    public int getRecordCount(SqlBuilder sql) {
	return getDao().getRecordCount(sql);
    }
    
    public List queryByCondition(SqlBuilder sql) {
	return getDao().queryByCondition(sql);
    }
    
    public OID insert(AuAuthorizeLogVo vo) {
	return getDao().insert(vo);
    }

    /* (non-Javadoc)
     * @see venus.authority.au.auauthorizelog.bs.IAuAuthorizeLogBS#getRecordCount(java.lang.String)
     */
    public int getRecordCount(String queryCondition) {
        return dao.getRecordCount(queryCondition);
    }

    /* (non-Javadoc)
     * @see venus.authority.au.auauthorizelog.bs.IAuAuthorizeLogBS#queryByCondition(int, int, java.lang.String, java.lang.String)
     */
    public List queryByCondition(int no, int size, String queryCondition,
            String orderStr) {
        return dao.queryByCondition(no, size, queryCondition, orderStr);
    }

    /* (non-Javadoc)
     * @see venus.authority.au.auauthorizelog.bs.IAuAuthorizeLogBS#getAuByPartyId(java.lang.String, java.lang.String)
     */
    public Map getAuByPartyId(String partyId, String sType,String auHisTag) {
        return getAuByPartyId(partyId, sType, null,auHisTag);
    }
    /**
     * 
     * 功能: 根据partyId、团体关系类型和资源类型查询该用户所拥有的权限，包括它继承的
     *
     * @param partyId
     * @param sType
     * @param relationTypeId
     * @return
     */
    
    public Map getAuByPartyId(String partyId, String sType, String relationTypeId,String auHisTag) {
        IAuPartyRelationBs relBs = (IAuPartyRelationBs) Helper.getBean(venus.oa.organization.aupartyrelation.util.IConstants.BS_KEY);
        AuPartyRelationVo queryVo = new AuPartyRelationVo();
        queryVo.setPartyid(partyId);
        queryVo.setRelationtype_id(relationTypeId);
        List relList = relBs.queryAuPartyRelation(queryVo);
        
        return this.getAuByRelList(relList, sType,auHisTag);
        
    }
    
    /**
     * 
     * 功能: 根据团体关系列表和资源类型查询该用户所拥有的权限，包括它继承的
     *
     * @param relList
     * @param sType
     * @return
     */
    public Map getAuByRelList(List relList, String sType,String auHisTag){      
        List auList = new ArrayList();
        
        if(relList != null) {
            for (int i=0; i<relList.size(); i++){
                AuPartyRelationVo relVo = (AuPartyRelationVo) relList.get(i);
                String[] visiCodeArray = ProjTools.splitTreeCode(relVo.getCode());
                List list = getDao().queryByVisitorCode(visiCodeArray ,sType,auHisTag);//获取访问者所有权限
                //同一团体关系类型之间的权限判断
                Map tempMap = getAuthorizeBs().judgeAu4OneRelation(list);
                if(tempMap != null) {
                    auList.addAll(tempMap.values());
                }
            }
        }
        //不同团体关系类型之间的权限判断
        Map auMap = getAuthorizeBs().judgeAu4DifRelation(auList);
        return auMap==null ? new HashMap() : auMap;
    }

    /**
     * 
     * 功能: 根据访问者Code数组和资源类型查询该访问者自身拥有的权限+在同一团体关系类型内它所继承的权限
     *      如果资源类型为null，则查询全部资源类型的权限
     *
     * @param visiCodes 访问者编号数组
     * @param resType 资源类型
     * @return
     */
    public Map getAuByVisitorCode(String visiCode, String resType,String auHisTag) {
        if(null==visiCode||"".equals(visiCode))
            return java.util.Collections.EMPTY_MAP;
        String[] visiCodeArray = ProjTools.splitTreeCode(visiCode);
        List list = getDao().queryByVisitorCode(visiCodeArray, resType,auHisTag);
        return getAuthorizeBs().judgeAu4OneRelation(list);
    }

    /**
     * 
     * 功能: 根据访问者Code数组和资源类型查询该访问者自身拥有的数据权限+在同一团体关系类型内它所继承的权限
     *      提出历史数据权限
     *
     * @param visiCodes 访问者编号数组
     * @param resType 资源类型
     * @return
     */
    public Map getOrgAuByVisitorCodeWithOutHistory(String visiCode,String resType,
            String auHisTag) {
        if(null==visiCode||"".equals(visiCode))
            return java.util.Collections.EMPTY_MAP;
        String[] visiCodeArray = ProjTools.splitTreeCode(visiCode);
        List list = getDao().queryByVisitorCodeWithOutHistory(visiCodeArray, resType,auHisTag);
        return getAuthorizeBs().judgeAu4OneRelation(list);
    }
}

