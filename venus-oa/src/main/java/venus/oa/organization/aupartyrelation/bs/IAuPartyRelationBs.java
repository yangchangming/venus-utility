/*
 * 创建日期 2006-10-24
 *
 */
package venus.oa.organization.aupartyrelation.bs;

import venus.oa.organization.aupartyrelation.vo.AuPartyRelationVo;
import venus.pub.lang.OID;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author maxiao
 *
 */
public interface IAuPartyRelationBs {
    
    /**
     * 
     * 功能: 添加团体关系根节点
     *
     * @param partyId 团体主键
     * @param partyRelationTypeId 团体关系类型主键
     * @return
     */
    public boolean initRoot(String partyId, String partyRelationTypeId);
    
    /**
     * 添加新的团体关系
     * @param childPartyId 子团体id
     * @param parentRelId 父团体关系id
     * @param relTypeId 团体关系类型id
     * @param relationtype_keyword 记录导入数据的原始id
     * @return
     */
    public OID addPartyRelation(String childPartyId, String parentRelId, String relTypeId, String relationtype_keyword);
    
    /**
     * 添加新的团体关系
     * @param childPartyId 子团体id
     * @param parentRelId 父团体关系id
     * @param relTypeId 团体关系类型id
     * @return
     */
    public OID addPartyRelation(String childPartyId, String parentRelId, String relTypeId);
    
    /**
     * 
     * 功能: 根据Vo添加新的团体关系
     *
     * @param vo
     */
    public void addPartyRelation(AuPartyRelationVo vo);
    /**
     * 删除团体关系
     * @param id 团体关系id
     * @return
     */
    public boolean deletePartyRelation(String id);
    /**
     * 功能: 查询父编号为parentCode的节点的个数
     * @param parentCode
     * @return
     */
    public int getCountByParentCode(String parentCode);
    /**
     * 对团体关系表进行查询
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
     * 修改团体关系
     * @param vo
     */
    public int update(AuPartyRelationVo vo);
    
    /**
     * 
     * 功能: 组织机构排序
     *
     * @param lChange
     * @return
     */
    public int sort(List lChange);
    
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
     * 根据Id进行查询
     * 
     * @param id 用于查找的id
     * @return 查询到的VO对象
     */
    public AuPartyRelationVo find(String id);
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
     *
     * @param childPartyId
     * @param parentRelId
     * @param relTypeId
     * @return
     */
    public AuPartyRelationVo queryRelationVoByKey(String childPartyId, String parentRelId, String relTypeId);
}

