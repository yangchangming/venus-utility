package venus.oa.organization.aupartyrelationtype.bs.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import venus.frames.base.bs.BaseBusinessService;
import venus.frames.base.exception.BaseApplicationException;
import venus.oa.organization.auconnectrule.dao.IAuConnectRuleDao;
import venus.oa.organization.auconnectrule.vo.AuConnectRuleVo;
import venus.oa.organization.aupartyrelation.dao.IAuPartyRelationDao;
import venus.oa.organization.aupartyrelation.vo.AuPartyRelationVo;
import venus.oa.organization.aupartyrelationtype.bs.IAuPartyRelationTypeBS;
import venus.oa.organization.aupartyrelationtype.dao.IAuPartyRelationTypeDao;
import venus.oa.organization.aupartyrelationtype.util.IConstants;
import venus.oa.organization.aupartyrelationtype.vo.AuPartyRelationTypeVo;
import venus.pub.lang.OID;

import java.util.List;

//import venus.frames.mainframe.log.ILog;
//import venus.frames.mainframe.log.LogMgr;

/**
 * 团体关系类型BS
 * @author wumingqiang
 *
 */
@Service
public class AuPartyRelationTypeBS extends BaseBusinessService implements IAuPartyRelationTypeBS, IConstants {

    @Autowired
    private IAuPartyRelationTypeDao auPartyRelationTypeDao;

    @Autowired
    private IAuPartyRelationDao auPartyRelationDao;

    @Autowired
    private IAuConnectRuleDao auConnectRuleDao;


    /**
     * 查询所有
     * @param no
     * @param size
     * @param orderStr
     * @return
     */
    public List queryAll(int no, int size, String orderStr) {
        return auPartyRelationTypeDao.queryAll(no, size, orderStr);
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
        return auPartyRelationTypeDao.simpleQuery(no, size, orderStr, objVo);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    public int delete(String id) {
        return auPartyRelationTypeDao.delete(id);
    }

    /**
     * 获得记录数
     * @return
     */
    public int getRecordCount() {
        return auPartyRelationTypeDao.getRecordCount();
    }


    /**
     * 按条件获得记录数
     * @param queryCondition
     * @return
     */
    public int getRecordCount(String queryCondition) {
        return auPartyRelationTypeDao.getRecordCount(queryCondition);
    }

    public OID insert(Object objVo) {
        List list = auPartyRelationTypeDao.queryByName(objVo);
        if (list.size()>0) {
            throw new BaseApplicationException(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Relationship_type_name_of_the_group_to_repeat_re_edit"));
        }
        return auPartyRelationTypeDao.insert(objVo);
    }

    /**
     * 查找
     * @param id
     * @return
     */
    public Object find(String id) {
        return auPartyRelationTypeDao.find(id);
    }

    /**
     * 更新
     * @param objVo
     * @return
     */
    public int update(Object objVo) {
        List list = auPartyRelationTypeDao.queryByName(objVo);
        if (list.size()>1) {
//            log.error(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Relationship_type_name_of_the_group_to_repeat_re_edit_")+((AuPartyRelationTypeVo) objVo).getName());
            throw new BaseApplicationException(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Relationship_type_name_of_the_group_to_repeat_re_edit"));
        }
        else if (list.size()==1){
            AuPartyRelationTypeVo vo = (AuPartyRelationTypeVo)list.get(0);  
            if (!vo.getId().equals(((AuPartyRelationTypeVo) objVo).getId())) {
//                log.error(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Relationship_type_name_of_the_group_to_repeat_re_edit_")+((AuPartyRelationTypeVo) objVo).getName());
                throw new BaseApplicationException(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Relationship_type_name_of_the_group_to_repeat_re_edit"));
            }
        }
        return auPartyRelationTypeDao.update(objVo);
    }

	public List getPartyAll() {
		return auPartyRelationTypeDao.getPartyAll();
	}    
	/**
     * 启用
     * @param id
     * @return
     */
	public int enable(String id) {
		return auPartyRelationTypeDao.enable(id);
	}
	/**
     * 禁用
     * @param id
     * @return
     */
	public int disable(String id) {
	    AuPartyRelationVo vo = new AuPartyRelationVo();
	    vo.setRelationtype_id(id);
	    List list = auPartyRelationDao.queryAuPartyRelation(vo);
	    if (list.size()>0) {
//	        log.error(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Relationship_as_the_relationship_has_been_used_Please_delete_the_relevant_data_")+id);
	        throw new BaseApplicationException(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Relationship_as_the_relationship_has_been_used_Please_delete_the_relevant_data"));
	    }
	    AuConnectRuleVo tvo = new AuConnectRuleVo();
	    tvo.setParent_partytype_id(id);
	    list = auConnectRuleDao.queryByType(tvo);
	    if (list.size()>0) {
//	        log.error(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Relationship_as_the_relationship_has_been_used_Please_delete_the_relevant_data_")+id);
	        throw new BaseApplicationException(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Groups_of_this_type_have_been_used_to_connect_the_rules_Please_delete_the_relevant_data"));
	    }
	    tvo.setParent_partytype_id("");
	    tvo.setChild_partytype_id(id);
	    list = auConnectRuleDao.queryByType(tvo);
	    if (list.size()>0) {
//	        log.error(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Relationship_as_the_relationship_has_been_used_Please_delete_the_relevant_data_")+id);
	        throw new BaseApplicationException(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Groups_of_this_type_have_been_used_to_connect_the_rules_Please_delete_the_relevant_data"));
	    }
		return auPartyRelationTypeDao.disable(id);
	}
	/**
     * 根据KeyWord获得ID
     * @param keyword
     * @return
     */
	public String getIdByKeyWord(String keyword){
    	return auPartyRelationTypeDao.getIdByKeyWord(keyword);
    }
	/**
     * 
     * 功能: 查询所有启用状态的团体关系类型
     * 当参数no和size小于或等于0时，不翻页
     * 当参数orderStr为null时，不进行排序
     * @param no
     * @param size
     * @param orderStr
     * @return
     */
    public List queryAllEnable(int no, int size, String orderStr) {
        return auPartyRelationTypeDao.queryAllEnable(no,size,orderStr);
    }
    /**
     * List 中是LabelValueBean
     * @return
     */
    public List getPartyAllByKeyWord(String keyword){
        return auPartyRelationTypeDao.getPartyAllByKeyWord(keyword);
    }
}

