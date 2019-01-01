package venus.oa.authority.auvisitor.bs.impl;

import venus.oa.authority.auauthorize.dao.IAuAuthorizeDao;
import venus.oa.authority.auvisitor.bs.IAuVisitorBS;
import venus.oa.authority.auvisitor.dao.IAuVisitorDao;
import venus.oa.authority.auvisitor.util.IConstants;
import venus.oa.authority.auvisitor.vo.AuVisitorVo;
import venus.oa.organization.aupartyrelation.bs.IAuPartyRelationBs;
import venus.oa.organization.aupartyrelation.vo.AuPartyRelationVo;
import venus.oa.util.DateTools;
import venus.oa.util.GlobalConstants;
import venus.oa.util.tree.DeepTreeSearch;
import venus.frames.base.bs.BaseBusinessService;
import venus.frames.base.exception.BaseApplicationException;
import venus.frames.mainframe.log.ILog;
import venus.frames.mainframe.log.LogMgr;
import venus.frames.mainframe.util.Helper;

import java.util.ArrayList;
import java.util.List;

/**
 * 团体类型BS
 * 
 * @author wumingqiang
 *  
 */
public class AuVisitorBS extends BaseBusinessService implements IAuVisitorBS, IConstants {

    private static ILog log = LogMgr.getLogger(AuVisitorBS.class);

    private IAuVisitorDao dao = null;

    /**
     * 按条件查询,返回LIST
     * 
     * @param no
     * @param size
     * @param orderStr
     * @param objVo
     * @return
     */
    public List simpleQuery(int no, int size, String orderStr, Object objVo) {
        return getDao().simpleQuery(no, size, orderStr, objVo);
    }

    /**
     * 通过团体类型分类按条件查询,返回LIST
     * 
     * @param no
     * @param size
     * @param orderStr
     * @param objVo
     * @return
     */
    public List simpleQueryByTypes(int no, int size, String orderStr, Object objVo, String condition) {
    	List list = getDao().simpleQueryByTypes(no, size, orderStr, objVo, condition);
    	List result = new ArrayList();
    	AuVisitorVo vo = new AuVisitorVo();
    	for(int i=0; i<list.size(); i++) {
    		vo = (AuVisitorVo)list.get(i);
    		if ("1".equals(orderStr)) { //用户授权
    			vo.setOwner_org(DeepTreeSearch.getOrgNameById(vo.getId()));
    		} else { //角色、机构授权
    			vo.setOwner_org(DeepTreeSearch.getOrgNameByCode(vo.getCode()));
    		}
    		result.add(vo);
    	}
        return result;
    }

    /**
     * 删除
     * 
     * @param id
     * @return
     */
    public int delete(String id) {
        //获得IAuAuthorizeDao
        IAuAuthorizeDao auAuthorizeDao = (IAuAuthorizeDao) Helper.getBean("auauthorize_dao");
        //删除授权情况（包括附加数据）
        auAuthorizeDao.deleteByVisitorId(id);
        //删除访问者
        return getDao().delete(id);
    }

    /**
     * 
     * 功能: 根据访问者原始ID删除
     * 
     * @param origId
     * @return
     */
    public int deleteByOrigId(String origId) {
        AuVisitorVo vo = (AuVisitorVo) getDao().findByOrgId(origId);
        if (vo != null) {
            //获得IAuAuthorizeDao
            IAuAuthorizeDao auAuthorizeDao = (IAuAuthorizeDao) Helper.getBean("auauthorize_dao");
            //删除授权情况（包括附加数据）
            auAuthorizeDao.deleteByVisitorId(vo.getId());
            //删除访问者
            return getDao().delete(vo.getId());
        } else {
            return 0;
        }
    }

    /**
     * 获得记录数
     * 
     * @return
     */
    public int getRecordCount() {
        return getDao().getRecordCount();
    }

    /**
     * 按条件获得记录数
     * 
     * @param queryCondition
     * @return
     */
    public int getRecordCount(Object objVo) {
        return getDao().getRecordCount(objVo);
    }

    /**
     * @return
     */
    public IAuVisitorDao getDao() {
        return dao;
    }

    /**
     * @param dao
     */
    public void setDao(IAuVisitorDao dao) {
        this.dao = dao;
    }

    /**
     * 添加
     * 
     * @param rvo
     * @return
     */
    public String insert(Object objVo) {
        AuVisitorVo vo = (AuVisitorVo) objVo;
        if (getDao().findByOrgId(vo.getOriginal_id(), vo.getVisitor_type()) != null) {
            throw new BaseApplicationException(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Add_the_record_already_exists_"));
        }
        return getDao().insert(objVo);

    }

    /**
     * 更新
     * 
     * @param objVo
     * @return
     */
    public int update(Object objVo) {
        return getDao().update(objVo);
    }

    /**
     * 
     * 功能: 根据团体关系ID和团体类型查询相应的访问者Vo，如果查不到则自动生成一个访问者vo并添加到访问者表中，然后返回新添加的访问者vo
     * 
     * @param relId 团体关系ID
     * @param pType 团体类型
     * @return
     */
    public AuVisitorVo queryByRelationId(String relId, String pType) {
        log.debug("根据团体关系ID和团体类型查询相应的访问者Vo，如果查不到则自动生成一个访问者vo并添加到访问者表中，然后返回新添加的访问者vo");
        AuVisitorVo vo = (AuVisitorVo) getDao().findByOrgId(relId, GlobalConstants.getVisiTypeByPartyType(pType));
        if (vo == null) {
            IAuPartyRelationBs relBs = (IAuPartyRelationBs) Helper
                    .getBean(venus.oa.organization.aupartyrelation.util.IConstants.BS_KEY);
            AuPartyRelationVo relVo = relBs.find(relId);
            vo = new AuVisitorVo();
            vo.setCode(relVo.getCode());
            vo.setCreate_date(DateTools.getSysTimestamp());
            vo.setName(relVo.getName());
            vo.setOriginal_id(relVo.getId());
            vo.setPartyrelationtype_id(relVo.getRelationtype_id());
            vo.setPartytype_id(relVo.getPartytype_id());
            vo.setVisitor_type(GlobalConstants.getVisiTypeByPartyType(relVo.getPartytype_id()));
            getDao().insert(vo);
        }
        return vo;
    }

    /**
     * 通过团体类型分类查询所有
     * 
     * @param no
     * @param size
     * @param orderStr
     * @return
     */
    public List queryAllByTypes(int no, int size, String orderStr) {
        return getDao().queryAllByTypes(no, size, orderStr);
    }
    /**
     * 按条件获得记录数
     * 
     * @param queryCondition
     * @return
     */
    public int getRecordCountByTypes(String partyTypes){
        return getDao().getRecordCountByTypes(partyTypes);
    }
    /**
     * 按条件获得记录数
     * 
     * @param queryCondition
     * @return
     */
    public int getRecordCountByTypes(String partyTypes, Object objVo, String condition){
        return getDao().getRecordCountByTypes(partyTypes,objVo, condition);
    }

    /* (non-Javadoc)
     * @see venus.authority.au.auvisitor.bs.IAuVisitorBS#find(java.lang.String)
     */
    public AuVisitorVo find(String visitorId) {
        return dao.find(visitorId);
    }
}

