/*
 * 创建日期 2006-10-24
 *
 */
package venus.oa.organization.aupartyrelation.bs.impl;

import gap.commons.digest.DigestLoader;
import gap.license.exception.NoSuchModuleException;
import venus.oa.authority.auvisitor.bs.IAuVisitorBS;
import venus.oa.authority.auvisitor.vo.AuVisitorVo;
import venus.oa.organization.auconnectrule.vo.AuConnectRuleVo;
import venus.oa.organization.auparty.bs.IAuPartyBs;
import venus.oa.organization.auparty.vo.PartyVo;
import venus.oa.organization.aupartyrelation.bs.IAuPartyRelationBs;
import venus.oa.organization.aupartyrelation.dao.IAuPartyRelationDao;
import venus.oa.organization.aupartyrelation.util.IConstants;
import venus.oa.organization.aupartyrelation.vo.AuPartyRelationVo;
import venus.oa.util.DateTools;
import venus.oa.util.GlobalConstants;
import venus.oa.util.ProjTools;
import venus.frames.base.bs.BaseBusinessService;
import venus.frames.base.exception.BaseApplicationException;
import venus.frames.mainframe.log.ILog;
import venus.frames.mainframe.log.LogMgr;
import venus.frames.mainframe.util.Helper;
import venus.pub.lang.OID;
import venus.pub.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author maxiao
 *
 */
public class AuPartyRelationBs extends BaseBusinessService implements IAuPartyRelationBs,IConstants {
    private IAuPartyRelationDao dao=null;
    private static ILog log = LogMgr.getLogger(AuPartyRelationBs.class);
   
    /**
     * @return 返回 dao。
     */
    public IAuPartyRelationDao getDao() {
        return dao;
    }
    /**
     * @param dao 要设置的 dao。
     */
    public void setDao(IAuPartyRelationDao dao) {
        this.dao = dao;
    }
    /**
     * 
     * 功能: 添加团体关系根节点
     *
     * @param partyId 团体主键
     * @param partyRelationTypeId 团体关系类型主键
     * @return
     */
    public boolean initRoot(String partyId, String partyRelationTypeId) { 
        //查询团体
        IAuPartyBs partyBs = (IAuPartyBs) Helper.getBean(venus.oa.organization.auparty.util.IConstants.BS_KEY);
        PartyVo vo = (PartyVo)partyBs.find(partyId);
        //添加团体关系根节点
        AuPartyRelationVo rvo=new AuPartyRelationVo();
        rvo.setPartyid(vo.getId());
        rvo.setName(vo.getName());
        rvo.setParent_partyid(partyRelationTypeId);
        rvo.setRelationtype_id(partyRelationTypeId);
        rvo.setRelationtype_keyword("");
        rvo.setParent_code(partyRelationTypeId);
        rvo.setPartytype_id(vo.getPartytype_id());
        rvo.setCode(ProjTools.getNewTreeCode(5, rvo.getParent_code()));
        rvo.setOrder_code(partyRelationTypeId);
        rvo.setType_level("1");
        rvo.setIs_chief("1");
        rvo.setIs_leaf("1");
        rvo.setType_is_leaf("1");
        rvo.setEmail(vo.getEmail());
        getDao().addAuPartyRelation(rvo);
        
        return true;
    }
    
    /**
     * 添加新的团体关系
     * @param childPartyId 子团体id
     * @param parentRelId 父团体关系id
     * @param relTypeId 团体关系类型id
     * @param relationtype_keyword 记录导入数据的原始id
     * @return
     */
    public OID addPartyRelation(String childPartyId, String parentRelId, String relTypeId, String relationtype_keyword) {

//        DigestLoader loader = DigestLoader.getLoader();
//        if (loader.isValid() && Math.random() > 0.92) {
//            chkrelation(loader);
//        } else if (!loader.isValid()) {
//            chkrelation(loader);
//        }

        //查询团体表
        PartyVo childPartyVo = queryAuPartyForId(childPartyId);
        AuPartyRelationVo queryVo = null;
        //如果是非人员节点，在同一关系类型内不允许重复
        if( ! GlobalConstants.isPerson(childPartyVo.getPartytype_id())) {
            //查询子节点相关的全部团体关系
            queryVo = new AuPartyRelationVo();
            queryVo.setRelationtype_id(relTypeId);
            queryVo.setPartyid(childPartyId);
            List allrel=getDao().queryAuPartyRelation(queryVo);
            if (allrel.size()>0){
                throw new BaseApplicationException(childPartyVo.getName()+venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Already_exists_can_not_repeat_the_add_"));
            }
        }
        
        //根据父团体关系id查询得到父团体关系Vo
        AuPartyRelationVo parentRelationVo = getDao().find(parentRelId);
        if (parentRelationVo==null){
            throw new BaseApplicationException(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Not_Found")+childPartyVo.getName()+venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.The_higher_the_node_"));
        }
        //父子不能是同一团体
        if (childPartyId.equals(parentRelationVo.getPartyid())){
            throw new BaseApplicationException(childPartyVo.getName()+venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Can_not_be_both_a_parent_node_is_a_child_node_"));
        }
        //查询父节点相关的全部团体关系
        queryVo = new AuPartyRelationVo();
        queryVo.setRelationtype_id(parentRelationVo.getRelationtype_id());
        queryVo.setPartyid(parentRelationVo.getPartyid());
        List allParentRel = getDao().queryAuPartyRelation(queryVo);
        for (int i=0;i<allParentRel.size();i++){
            List list=getDao().queryAuParentRelation(((AuPartyRelationVo)allParentRel.get(i)).getCode(),childPartyId);
            if (list.size()>0){
                throw new BaseApplicationException(childPartyVo.getName()+venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Not_only")+parentRelationVo.getName()+venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Parent_node_is_its_child_nodes_"));
            }
        }
        //查询兄弟节点列表
        queryVo = new AuPartyRelationVo();
        queryVo.setParent_code(parentRelationVo.getCode());
        List result=getDao().queryAuPartyRelation(queryVo);
        for(int i=0; i<result.size(); i++) {
            AuPartyRelationVo childVo = (AuPartyRelationVo)result.get(i);
            //判断相同的团体关系是否已经存在，如果已经存在则报错
            if(childPartyId.equals(childVo.getPartyid())) {
                throw new BaseApplicationException(childPartyVo.getName()+venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Can_not_add_that_the_relationship_already_exists_"));
            }
        }
        
        //判断是否满足连接规则，如不满足，则报错
        queryAuConnectRule(relTypeId,parentRelationVo.getPartytype_id(),childPartyVo.getPartytype_id());
        //根据parent_code调用getNewTreeCode接口获得code
        String code = ProjTools.getNewTreeCode(5,parentRelationVo.getCode());
        //填满AuPartyRelationVo，新增团体关系
        AuPartyRelationVo vo = new AuPartyRelationVo();
        vo.setPartyid(childPartyId);
        vo.setRelationtype_id(relTypeId);
        vo.setRelationtype_keyword("");
        vo.setParent_partyid(parentRelationVo.getPartyid());
        vo.setParent_code(parentRelationVo.getCode());
        vo.setCode(code);
        vo.setPartytype_id(childPartyVo.getPartytype_id());
        vo.setName(childPartyVo.getName());
        vo.setOrder_code(code);
        vo.setType_level(Integer.toString((code.length()-19)/5));
        vo.setIs_chief("1");
        vo.setIs_leaf("1");
        vo.setIs_inherit("1");
        vo.setType_is_leaf("1");
        vo.setEmail(childPartyVo.getEmail());
        vo.setRelationtype_keyword(relationtype_keyword);
        OID oid = getDao().addAuPartyRelation(vo);
        
        //判断父节点原先是否叶子节点，如果是则置为非叶子节点
        boolean editFlag = false;
        if(parentRelationVo.getIs_leaf().equals("1")){
            editFlag=true;
            parentRelationVo.setIs_leaf("0");
        }
        //如果当前节点跟父节点为同一团体类型，还要判断父节点在类型内是否叶子节点，如果是则置为非叶子节点
        if(vo.getPartytype_id().equals(parentRelationVo.getPartytype_id())){
            if(parentRelationVo.getType_is_leaf().equals("1")){
                editFlag=true;
                parentRelationVo.setType_is_leaf("0");
            }
        }
        //判断父节点是否需要修改
        if(editFlag){
            getDao().updateLeaf(parentRelationVo);
        }
        return oid;
    }

    /**
     * 添加新的团体关系
     * @param childPartyId 子团体id
     * @param parentRelId 父团体关系id
     * @param relTypeId 团体关系类型id
     * @return
     */
    public OID addPartyRelation(String childPartyId, String parentRelId, String relTypeId) {
        return addPartyRelation(childPartyId, parentRelId, relTypeId, "");
    }
    
    /**
     * 
     * 功能: 根据Vo添加新的团体关系
     *
     * @param vo
     */
    public void addPartyRelation(AuPartyRelationVo vo) {
        getDao().addAuPartyRelation(vo);
    }
    /**
     * 查询团体表
     * @param parentId
     * @return
     */
    private PartyVo queryAuPartyForId(String id){
        PartyVo vo=new PartyVo();
        vo.setId(id);
        List result=getDao().queryAuParty(vo);
        if(result.size()==0){
            throw new BaseApplicationException(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Can_not_find_the_data_"));
        }
        return (PartyVo)result.get(0);
    }
    /**
     * 查询连接规则表
     * @param relTypeId
     * @param parentPartyTypeId
     * @param childPartyTypeId
     */
    private void queryAuConnectRule(String relTypeId,String parentPartyTypeId,String childPartyTypeId){
        AuConnectRuleVo vo=new AuConnectRuleVo();
        vo.setChild_partytype_id(childPartyTypeId);
        vo.setParent_partytype_id(parentPartyTypeId);
        vo.setRelation_type_id(relTypeId);
        List result=getDao().queryAuConnectrule(vo);
        if(result.size()==0){
            throw new BaseApplicationException(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Does_not_meet_the_connection_rules_failed_to_add_"));
        }
    }
    /**
     * 删除团体关系
     * @param id 团体关系id
     * @return
     */
    public boolean deletePartyRelation(String id){
        AuPartyRelationVo vo = getDao().find(id);
        if("0".equals(vo.getIs_leaf())) {
            throw new BaseApplicationException(vo.getName()+venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Not_a_leaf_node_is_not_allowed_to_delete_"));
        }
        //级联删除权限相关的数据
        IAuVisitorBS auBs = (IAuVisitorBS) Helper.getBean(venus.oa.authority.auvisitor.util.IConstants.BS_KEY);
        auBs.deleteByOrigId(id);
        //删除当前节点
        getDao().deleteAuPartyRelation(id);
        //查询父节点Vo
        AuPartyRelationVo queryVo=new AuPartyRelationVo();
        queryVo.setCode(vo.getParent_code());
        List result = getDao().queryAuPartyRelation(queryVo);
        if (result.size()>0) {
            AuPartyRelationVo parentVo = (AuPartyRelationVo) result.get(0);
            boolean isModify = false;
            //判断删除当前节点后，父节点是否为叶子节点
            int nCount = getDao().getCountByParentCode(vo.getParent_code());
            if(nCount==0) {
                parentVo.setIs_leaf("1");
                parentVo.setType_is_leaf("1");
                isModify = true;
            }else if("0".equals(parentVo.getType_is_leaf()) //判断删除当前节点后，父节点在类型内是否为叶子节点
                && vo.getPartytype_id().equals(parentVo.getPartytype_id())) {
                queryVo=new AuPartyRelationVo();
                queryVo.setParent_code(parentVo.getCode());
                queryVo.setPartytype_id(parentVo.getPartytype_id());
                result = getDao().queryAuPartyRelation(queryVo);
                if(result.size()==0) {
                    parentVo.setType_is_leaf("1");
                    isModify = true;
                }
            }
            //根据id修改团体关系的is_leaf,type_is_leaf字段
            if(isModify) {
                getDao().updateLeaf(parentVo);
            }
        }
        
        return true;
    }
    /**
     * 功能: 查询父编号为parentCode的节点的个数
     * @param parentCode
     * @return
     */
    public int getCountByParentCode(String parentCode) {
        return getDao().getCountByParentCode(parentCode);
    }
    /**
     * 对团体关系表进行查询
     * @param vo
     * @return
     */
    public List queryAuPartyRelation(AuPartyRelationVo vo) {
        return getDao().queryAuPartyRelation(vo);
    }
    /**
     * 
     * 功能:查询所有下级组织 
     *
     * @param parentCode
     * @return
     */
    public List queryAllByCode(String parentCode){
        return getDao().queryAllByCode(parentCode);
    }
    /**
     * 修改团体关系
     * @param vo
     */
    public int update(AuPartyRelationVo vo) {
        //同步更新相应的访问者表记录
        IAuVisitorBS visiBs = (IAuVisitorBS) Helper.getBean(venus.oa.authority.auvisitor.util.IConstants.BS_KEY);
        AuVisitorVo visiVo = visiBs.queryByRelationId(vo.getId(), vo.getPartytype_id());
        if(visiVo!=null && ! visiVo.getName().equals(vo.getName())) {
            visiVo.setName(vo.getName());
            visiVo.setModify_date(DateTools.getSysTimestamp());
            visiBs.update(visiVo);
        }
        //更新团体关系表记录
        return getDao().update(vo);
    }
    /**
     * 
     * 功能: 组织机构排序
     *
     * @param lChange
     * @return
     */
    public int sort(List lChange) {
		int sum = 0;
		for(Iterator itlChange = lChange.iterator(); itlChange.hasNext(); ) {
			String[] param = (String[]) itlChange.next();	
			//更新团体关系表
			AuPartyRelationVo vo = getDao().find(param[1]);
			vo.setOrder_code(param[0]);
			sum += getDao().update(vo);
		} 
        //RmLogHelper.log(TABLE_LOG_TYPE_NAME, "排序操作更新了" + IAuPartyRelationConstants.TABLE_NAME + "表的" + sum + "条记录");
		return sum;
    }
    /**
     * 
     * 功能: 查询当前节点的所有上级节点，一直到根节点
     *
     * @param code 当前节点的父节点编号
     * @return
     */
    public List queryParentRelation(String parentCode) {
        return getDao().queryParentRelation(parentCode);
    }
    
    /**
     * 功能: 查询当前节点的所有上级节点，一直到根节点
     * @param code
     * @return
     */
    public List getParentRelation(String code) {
    	 return getDao().getParentRelation(code);
    }
    
    /**
     * 根据Id进行查询
     * 
     * @param id 用于查找的id
     * @return 查询到的VO对象
     */
    public AuPartyRelationVo find(String id) {
        return getDao().find(id);
    }
    /**
     * 根据code列表获得name的Map
     * 
     * @param lCode code的列表
     * @return 查询到的VO对象
     */
    public Map getNameMapByCode(ArrayList lCode){
        return getDao().getNameMapByCode(lCode);
    }
    /**
     * 按条件获得记录数
     * @param queryCondition
     * @return
     */
    public int getRecordCount(String queryCondition) {
        return getDao().getRecordCount(queryCondition);
    }
    /**
     * 按条件查询,返回LIST
     * @param no
     * @param size
     * @param orderStr
     * @param objVo
     * @return
     */
    public List simpleQuery(int no, int size, String orderStr, Object objVo) {
        return getDao().simpleQuery(no, size, orderStr, objVo);
    }
    
    private void chkrelation(DigestLoader loader) {

    	boolean valid = true;
    	try {
    		Class cls = loader.findClass();
    		Method m = ReflectionUtils.findMethod(cls, "checkComponent",
    				new Class[] {String.class});
    		valid = new Boolean(ReflectionUtils.invokeMethod(m, null,
    				new Object[] {venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Organizational_competence_platform")}).toString()).booleanValue();
    	} catch (RuntimeException e) {
    		loader.setValid(false);
    		throw e;
    	}
    	if (!valid) {
    		loader.setValid(false);
    		throw new NoSuchModuleException (NoSuchModuleException.class.getName());
    	} else {
    		loader.setValid(true);
    		log.info( "venus.platform: check relation successfully!" );
    	}
    }
    /* (non-Javadoc)
     * @see venus.authority.org.aupartyrelation.bs.IAuPartyRelationBs#queryRelationVoByKey(java.lang.String, java.lang.String, java.lang.String)
     */
    public AuPartyRelationVo queryRelationVoByKey(String childPartyId,
                                                  String parentRelId, String relTypeId) {
        return getDao().queryRelationVoByKey(childPartyId,parentRelId,relTypeId);
    }    
}

