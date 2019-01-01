/*
 * 创建日期 2006-10-24
 *
 */
package venus.oa.organization.auparty.dao;

import venus.oa.organization.auparty.vo.PartyVo;
import venus.oa.organization.aupartyrelation.vo.AuPartyRelationVo;

import java.util.List;
import java.util.Map;

/**
 * @author maxiao
 *
 */
public interface IAuPartyDao {
    /**
     * 新增团体
     * @param vo
     * @return
     */
    public String addParty(PartyVo vo);
    /**
     * 据partytype_id查au_partytype表，如查询结果为空，则报错
     * @param partytype_id
     * @return
     */
    public List queryPartytype(String partytype_id);
    /**
     * 据name查au_party表
     * @param partyname
     * @return
     */
    public List queryPartyByName(PartyVo vo);
    
    /**
     * 据id查au_party表，如查询结果为空，则报错
     * @param partytype_id
     * @return
     */
    public List queryParty(String id);
    /**
     * 更新团体表
     * @param vo
     */
    public void updateParty(PartyVo vo);
    /**
     * 查询团体关系表
     * @param party_id
     * @return
     */
    public List queryPartyRelation(String party_id);
    /**
     * 根据parent_code查询数据库
     * @param parent_code
     * @return
     */
    public List findPartyRelationForParent_Code(String parent_code);
    /**
     * 更新团体关系表
     * @param vo
     */
    public void updatePartyRelation(AuPartyRelationVo vo);
    /**
     * 禁用团体
     * @param id
     */
    public void disableParty(String id);
    /**
     * 删除团体关系
     * @param code
     */
    public void deletePartyRelation(String code);
    /**
     * 设置父节点为叶子节点
     * @param id
     */
    public void setLeafPartyRelation(String code);
    
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
     * 通过Id列表获得name的Map
     * @param party_id
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
     * 功能: 根据id删除一条数据
     *
     * @param id
     * @return
     */
    public int delete(String id);
    
}

