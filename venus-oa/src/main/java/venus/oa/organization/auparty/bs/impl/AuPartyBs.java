/*
 * 创建日期 2006-10-24
 *
 */
package venus.oa.organization.auparty.bs.impl;


import venus.oa.authority.auuser.bs.IAuUserBs;
import venus.oa.authority.auuser.util.IAuUserConstants;
import venus.oa.authority.auuser.vo.AuUserVo;
import venus.oa.organization.auparty.bs.IAuPartyBs;
import venus.oa.organization.auparty.dao.IAuPartyDao;
import venus.oa.organization.auparty.vo.PartyVo;
import venus.oa.organization.aupartyrelation.bs.IAuPartyRelationBs;
import venus.oa.organization.aupartyrelation.util.IConstants;
import venus.oa.organization.aupartyrelation.vo.AuPartyRelationVo;
import venus.oa.organization.aupartytype.dao.IAuPartyTypeDao;
import venus.oa.organization.aupartytype.vo.AuPartyTypeVo;
import venus.oa.service.profile.model.UserProfileModel;
import venus.oa.service.sys.vo.SysParamVo;
import venus.oa.util.DateTools;
import venus.oa.util.GlobalConstants;
import venus.oa.util.ProjTools;
import venus.oa.util.tree.DeepTreeSearch;
import venus.frames.base.bs.BaseBusinessService;
import venus.frames.base.exception.BaseApplicationException;
import venus.frames.mainframe.util.Helper;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author maxiao
 *
 */
public class AuPartyBs extends BaseBusinessService implements IAuPartyBs {
    
    private IAuPartyDao dao = null;
    
    /**
     * @return 返回 dao。
     */
    public IAuPartyDao getDao() {
        return dao;
    }
    /**
     * @param dao 要设置的 dao。
     */
    public void setDao(IAuPartyDao dao) {
        this.dao = dao;
    }
    /**
     * 新增团体
     */
    public String addParty(PartyVo vo) {
        //判断团体名称是否存在
        //List list = getDao().queryPartyByName(vo);
        //if (list.size()>0) {
        //    throw new BaseApplicationException("团体名称重复，请重新编辑");
        //}
        return getDao().addParty(vo);
    }
    /**
     * 
     * 功能: 添加新的团体和团体关系，如果parentRelId（父团体关系ID）为null或""则添加该节点为根节点
     *
     * @param vo 团体vo
     * @param parentRelId 父团体关系ID
     * @param relTypeId 团体关系类型ID
     * @return 团体ID
     */
    public String addPartyAndRelation(PartyVo vo, String parentRelId, String relTypeId) {
        //添加新的团体 
        String partyId = addParty(vo);
        //添加团体关系
        IAuPartyRelationBs relationBs = (IAuPartyRelationBs) Helper.getBean(IConstants.BS_KEY);
        if(parentRelId==null || parentRelId.length()==0 || "null".equals(parentRelId)) {
	        //添加团体关系根节点
	        AuPartyRelationVo relationVo=new AuPartyRelationVo();
	        relationVo.setPartyid(partyId);
	        relationVo.setName(vo.getName());
	        relationVo.setParent_partyid(relTypeId);
	        relationVo.setRelationtype_id(relTypeId);
	        relationVo.setRelationtype_keyword("");
	        relationVo.setParent_code(relTypeId);
	        relationVo.setPartytype_id(vo.getPartytype_id());
	        relationVo.setCode(ProjTools.getNewTreeCode(5, relationVo.getParent_code()));
	        relationVo.setOrder_code(relTypeId);
	        relationVo.setType_level("1");
	        relationVo.setIs_chief("1");
	        relationVo.setIs_leaf("1");
	        relationVo.setType_is_leaf("1");
	        relationVo.setEmail(vo.getEmail());
	        relationBs.addPartyRelation(relationVo);
        }else {
            //添加新的团体关系
            relationBs.addPartyRelation(partyId, parentRelId, relTypeId);
        }
		return partyId;
    }
    /**
     * 修改团体
     * @param vo
     * @return
     */
    public boolean updateParty (PartyVo vo){
        //根据该id查询得到旧的vo
        PartyVo oldVo = (PartyVo)getDao().queryParty(vo.getId()).get(0);
        
        //更新团体表
        if(!(vo.getName()==null || vo.getName().length()==0)){
            oldVo.setName(vo.getName());
        }
        //if(!(vo.getEmail()==null || vo.getEmail().length()==0)){ //邮箱地址置空后数据也要同步
            oldVo.setEmail(vo.getEmail());
        //}
        //if(!(vo.getRemark()==null || vo.getRemark().length()==0)){ //20100105 没道理
            oldVo.setRemark(vo.getRemark());
        //}
        if(!(vo.getOwner_org()==null || vo.getOwner_org().length()==0)){
            oldVo.setOwner_org(vo.getOwner_org());
        }
        getDao().updateParty(oldVo);
        
        //更新用户表
        IAuUserBs userBs = (IAuUserBs) Helper.getBean(IAuUserConstants.BS_KEY);
        AuUserVo userVo = userBs.getByPartyId(vo.getId());
        if(userVo!=null && !userVo.getName().equals(vo.getName())) {
            userVo.setName(vo.getName());
            userVo.setModify_date(DateTools.getSysTimestamp());
            userBs.update(userVo);
        }
        
        //更新相应的团体关系
        IAuPartyRelationBs relBs = (IAuPartyRelationBs) Helper.getBean(IConstants.BS_KEY);
        AuPartyRelationVo relVo = new AuPartyRelationVo();
        relVo.setPartyid(vo.getId());
        List list = relBs.queryAuPartyRelation(relVo);
        if(list!=null && list.size()>0) {
	        for(int i=0; i<list.size(); i++) {
	            AuPartyRelationVo oldRelVo = (AuPartyRelationVo)list.get(i);
	            if(!(vo.getName()==null || vo.getName().length()==0)){
	                oldRelVo.setName(vo.getName());
	            }
	            if(!(vo.getEmail()==null || vo.getEmail().length()==0)){
	                oldRelVo.setEmail(vo.getEmail());
	            }  
	            if(!(vo.getIs_inherit()==null || vo.getIs_inherit().length()==0)){
	                oldRelVo.setIs_inherit(vo.getIs_inherit());
	            }
		        relBs.update(oldRelVo);
	        }
        }
        
        return true;
    }
    /**
     * 禁用团体
     * @param partyId
     * @return
     */
    public boolean disableParty(String partyId){
        //禁用团体
        getDao().disableParty(partyId);
        //查询相应的团体关系
        IAuPartyRelationBs relBs = (IAuPartyRelationBs) Helper.getBean(IConstants.BS_KEY);
        AuPartyRelationVo queryVo = new AuPartyRelationVo();
        queryVo.setPartyid(partyId);
        List result = relBs.queryAuPartyRelation(queryVo);
        if (result.size()>0){
            throw new BaseApplicationException("该团体已被使用,不允许禁用！");
        }
        //删除账户
        IAuUserBs useBs = (IAuUserBs) Helper.getBean(IAuUserConstants.BS_KEY);
        AuUserVo userVo = useBs.getByPartyId(partyId);
        if(userVo!=null && userVo.getId()!=null){
            useBs.delete(userVo.getId());
        }
        return true;
    }

    /**
     * 
     * 功能:删除团体\团体关系\相应账号及权限相关的数据 
     *
     * @param partyId
     * @return
     */
    public boolean delete(String partyId) {
        //删除相应的团体关系
        IAuPartyRelationBs relBs = (IAuPartyRelationBs) Helper.getBean(IConstants.BS_KEY);
        AuPartyRelationVo queryVo = new AuPartyRelationVo();
        queryVo.setPartyid(partyId);
        List result = relBs.queryAuPartyRelation(queryVo);
        Iterator it = result.iterator();
        while(it.hasNext()){
            AuPartyRelationVo vo=(AuPartyRelationVo)it.next();
            relBs.deletePartyRelation(vo.getId());
        }
        //删除账户
        IAuUserBs useBs = (IAuUserBs) Helper.getBean(IAuUserConstants.BS_KEY);
        AuUserVo userVo = useBs.getByPartyId(partyId);
        if(userVo!=null && userVo.getId()!=null){
            useBs.delete(userVo.getId());
        }
        new UserProfileModel(partyId).removeProfile();
        //删除团体
        getDao().delete(partyId);
        return true;
    }
    
    /**
     * 按条件查询,返回LIST
     * @param no
     * @param size
     * @param orderStr
     * @param objVo
     * @return
     */
    public List simpleQuery(int no, int size, String orderStr,String typeId, Object objVo) {
        return getDao().simpleQuery(no, size, orderStr, typeId, objVo);
    }

    /**
     * 启用
     * @param id
     * @return
     */
	public int enableParty(String id) {
	    PartyVo vo = (PartyVo) find(id);
	    IAuPartyTypeDao auPartyTypeDao = (IAuPartyTypeDao) Helper.getBean("aupartytype_dao");
	    AuPartyTypeVo tvo = (AuPartyTypeVo) auPartyTypeDao.find(vo.getPartytype_id());
	    if (!"1".equals(tvo.getEnable_status())) {
	        throw new BaseApplicationException(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Body_type_has_been_disabled_"));
	    }	    
		return getDao().enableParty(id);
	}

    /**
     * 按条件获得记录数
     * @param queryCondition
     * @return
     */
    public int getRecordCount(String typeId, Object objVo) {
        return getDao().getRecordCount(typeId, objVo);
    }
    
    /**
     * 查找
     * @param id
     * @return
     */
    public Object find(String id) {
        return getDao().find(id);
    }

    /**
     * 查询团体关系表
     * @param party_id
     * @return
     */
    public List queryAllPartyRelation(String party_id){
        return getDao().queryAllPartyRelation(party_id);        
    }
    
    /**
     * 通过id列表获得name的Map
     * @param lPartyId
     * @return
     */
    public Map getNameMapByKey(List lPartyId){
        return getDao().getNameMapByKey(lPartyId);  
    }
    /**
     * 按条件查询,返回LIST
     * @param no
     * @param size
     * @param orderStr
     * @param typeId
     * @param objVo
     * @return
     */
    public List simpleQueryPerson(int no, int size, String orderStr, Object objVo){
    	List list = getDao().simpleQueryPerson(no,size,orderStr,objVo);
        for(int i = 0; i < list.size();  i++) {
        	PartyVo partyVo = (PartyVo)list.get(i);
        	partyVo.setOwner_org(DeepTreeSearch.getOrgNameById(partyVo.getId(), false));
        }
        return list;
    }
    /**
     * 按条件查询,返回LIST
     * @param no
     * @param size
     * @param orderStr
     * @param typeId
     * @param queryCondition
     * @return
     */
    public List simpleQueryPerson(int no, int size, String orderStr, String queryCondition){
    	List list = getDao().simpleQueryPerson(no,size,orderStr,queryCondition);
    	SysParamVo organizeTooltip = GlobalConstants.getSysParam(GlobalConstants.ORGANIZETOOLTIP);
        for(int i = 0; i < list.size();  i++) {
        	PartyVo partyVo = (PartyVo)list.get(i);
        	if (organizeTooltip == null) {
        	    partyVo.setOwner_org(DeepTreeSearch.getOrgNameById(partyVo.getId(),false));
        	}
        }
        return list;
    }    
    /**
     * 按条件获得记录数
     * @param queryCondition
     * @return
     */
    public int getRecordCountPerson(Object objVo) {
        return getDao().getRecordCountPerson(objVo);
    }
    /**
     * 按条件获得记录数
     * @param queryCondition
     * @return
     */
    public int getRecordCountPerson(String queryCondition) {
        return getDao().getRecordCountPerson(queryCondition);
    }    
    /**
     * 查询团体关系表
     * @param party_id
     * @return
     */
    public List queryAllPartyRelationDivPage(int no, int size, String party_id, Object objVo){
        return getDao().queryAllPartyRelationDivPage(no,size,party_id,objVo);
    }
    /**
     * 查询团体关系表
     * @param party_id
     * @return
     */
    public int getRecordCountPartyRelation(String party_id){
        return getDao().getRecordCountPartyRelation(party_id);
    }
}

