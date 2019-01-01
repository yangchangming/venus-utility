/*
 * 创建日期 2006-10-24
 *
 */
package venus.oa.organization.auparty.bs;

import venus.oa.organization.auparty.vo.PartyVo;

import java.util.List;
import java.util.Map;

/**
 * @author maxiao
 *
 */
public interface IAuPartyBs {
    /**
     * 新增团体
     * @param vo
     * @return
     */
    public String addParty(PartyVo vo);
    /**
     * 
     * 功能: 添加新的团体和团体关系，如果parentRelId（父团体关系ID）为null或""则添加该节点为根节点
     *
     * @param vo 团体vo
     * @param parentRelId 父团体关系ID
     * @param relTypeId 团体关系类型ID
     * @return 团体ID
     */
    public String addPartyAndRelation(PartyVo vo, String parentRelId, String relTypeId);
    /**
     * 修改团体
     * @param vo
     * @return
     */
    public boolean updateParty(PartyVo vo);
    /**
     * 禁用团体
     * @param partyId
     * @return
     */
    public boolean disableParty(String partyId);   
    
    /**
     * 按条件获得记录数
     * @param queryCondition
     * @return
     */
    public int getRecordCount(String typeId, Object objVo);

    /**
     * 按条件查询,返回LIST
     * @param no
     * @param size
     * @param orderStr
     * @param objVo
     * @return
     */
    public List simpleQuery(int no, int size, String orderStr, String typeId, Object objVo);
    
    /**
     * 启用
     * @param id
     * @return
     */
    public int enableParty(String id);
    
    /**
     * 查找
     * @param id
     * @return
     */
    public Object find(String objid);

    /**
     * 查询团体关系表
     * @param party_id
     * @return
     */
    public List queryAllPartyRelation(String party_id);
    
    /**
     * 通过id列表获得name的Map
     * @param lPartyId
     * @return
     */
    public Map getNameMapByKey(List lPartyId);
    /**
     * 按条件查询,返回LIST
     * @param no
     * @param size
     * @param orderStr
     * @param typeId
     * @param objVo
     * @return
     */
    public List simpleQueryPerson(int no, int size, String orderStr, Object objVo);
    /**
     * 按条件查询,返回LIST
     * @param no
     * @param size
     * @param orderStr
     * @param typeId
     * @param queryCondition
     * @return
     */
    public List simpleQueryPerson(int no, int size, String orderStr, String queryCondition);    
    /**
     * 按条件获得记录数
     * @param queryCondition
     * @return
     */
    public int getRecordCountPerson(Object objVo) ;
    /**
     * 按条件获得记录数
     * @param queryCondition
     * @return
     */
    public int getRecordCountPerson(String queryCondition) ;    
    /**
     * 查询团体关系表
     * @param party_id
     * @return
     */
    public List queryAllPartyRelationDivPage(int no, int size, String party_id, Object objVo);
    /**
     * 查询团体关系表
     * @param party_id
     * @return
     */
    public int getRecordCountPartyRelation(String party_id);
    
    /**
     * 
     * 功能:删除团体\团体关系\相应账号及权限相关的数据 
     *
     * @param partyId
     * @return
     */
    public boolean delete(String partyId);
    
}

