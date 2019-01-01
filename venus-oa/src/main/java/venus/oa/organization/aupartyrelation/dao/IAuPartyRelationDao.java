/*
 * 创建日期 2006-10-24
 *
 */
package venus.oa.organization.aupartyrelation.dao;


import venus.oa.organization.auconnectrule.vo.AuConnectRuleVo;
import venus.oa.organization.auparty.vo.PartyVo;
import venus.oa.organization.aupartyrelation.vo.AuPartyRelationVo;
import venus.pub.lang.OID;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author maxiao
 *
 */
public interface IAuPartyRelationDao {
    /**
     * 对团体关系表进行查询,判断该团体关系是否已经存在，如果已经存在则报错
     * @param vo
     * @return
     */
    public List queryAuPartyRelation(AuPartyRelationVo vo);
    /**
     * 
     * 功能:查询所有下级组织 
     *
     * @param parentCode
     * @return
     */
    public List queryAllByCode(String parentCode);
    /**
     * 查询团体关系类型表，获得团体关系类型标识partytype_keyword，如查询结果为空或已禁用，则报错
     * @param vo
     * @return
     */
    public List queryAuPartyRelationType(AuPartyRelationVo vo);
    
    /**
     * 查询团体表，获得团体的partytype_id
     * @param parentId
     * @return
     */
    public List queryAuParty(PartyVo vo);
    
    /**
     * 查询连接规则表
     * @param parentId
     * @return
     */
    public List queryAuConnectrule(AuConnectRuleVo vo);
    
    /**
     * 新增一个团体关系
     * @param vo
     */
    public OID addAuPartyRelation(AuPartyRelationVo vo);
    /**
     * 修改团体关系
     * @param vo
     */
    public void updateLeaf(AuPartyRelationVo vo);
    /**
     * 删除团体关系
     * @param code
     */
    public void deleteAuPartyRelation(String code);
    /**
     * 功能: 查询父编号为parentCode的节点的个数
     * @param parentCode
     * @return
     */
    public int getCountByParentCode(String parentCode);
    /**
     * 根据Id进行查询
     * 
     * @param id 用于查找的id
     * @return 查询到的VO对象
     */
    public AuPartyRelationVo find(String id);
    /**
     * 修改团体关系
     * @param vo
     */
    public int update(AuPartyRelationVo vo);
    
    /**
     * 查询当前parentcode的父节点中是childpartyid的团体
     * @param parentcode
     * @param childPartyId
     * @return
     */
    public List queryAuParentRelation(String parentcode, String childPartyId);

    /**
     * 
     * 功能: 查询当前节点的所有上级节点，一直到根节点
     *
     * @param code 当前节点的父节点编号
     * @return
     */
    public List queryParentRelation(String parentCode);
    
    /**
     * 功能: 查询当前节点的所有上级节点，一直到根节点
     * @param code
     * @return
     */
    public List getParentRelation(String code);
    
    /**
     * 根据code列表获得name的Map
     * 
     * @param lCode code的列表
     * @return 查询到的VO对象
     */
    public Map getNameMapByCode(ArrayList lCode);
    /**
     * 按条件获得记录数
     * @param queryCondition
     * @return
     */
    public int getRecordCount(String queryCondition);
    /**
     * 按条件查询,返回LIST
     * @param no
     * @param size
     * @param orderStr
     * @param objVo
     * @return
     */
    public List simpleQuery(int no, int size, String orderStr, Object objVo);

    /**
     * 根据子partyid、父关系id和关系类型id查找唯一关系vo
     * @param childPartyId
     * @param parentRelId
     * @param relTypeId
     * @return
     */
    public AuPartyRelationVo queryRelationVoByKey(String childPartyId,
                                                  String parentRelId, String relTypeId);
}

