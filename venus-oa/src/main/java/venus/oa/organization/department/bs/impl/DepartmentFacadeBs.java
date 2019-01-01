package venus.oa.organization.department.bs.impl;

import venus.oa.helper.OrgHelper;
import venus.oa.login.vo.LoginSessionVo;
import venus.oa.organization.aupartyrelation.bs.IAuPartyRelationBs;
import venus.oa.organization.aupartyrelation.util.IConstants;
import venus.oa.organization.aupartyrelation.vo.AuPartyRelationVo;
import venus.oa.organization.department.bs.IDepartmentBs;
import venus.oa.organization.department.bs.IDepartmentFacadeBs;
import venus.oa.organization.department.util.IDepartmentConstants;
import venus.oa.organization.department.vo.DepartmentVo;
import venus.oa.service.history.bs.IHistoryLogBs;
import venus.oa.util.DateTools;
import venus.oa.util.GlobalConstants;
import venus.frames.mainframe.util.Helper;

import java.util.HashMap;
import java.util.Map;

public class DepartmentFacadeBs implements IDepartmentFacadeBs,IDepartmentConstants {

	private IDepartmentBs departmentBs;
	
	/**
	 * @param departmentBs 要设置的 departmentBs。
	 */
	public void setDepartmentBs(IDepartmentBs departmentBs) {
		this.departmentBs = departmentBs;
	}
	
	/**
	 * 添加新记录，同时添加团体、团体关系并记录历史日志（如果parentRelId为空则不添加团体关系）
	 */
	public String insert(DepartmentVo deptVo, String parentRelId, LoginSessionVo vo) {
	    String partyid = departmentBs.insert(deptVo, parentRelId);
	    IAuPartyRelationBs relBs = (IAuPartyRelationBs) Helper.getBean(IConstants.BS_KEY);
        String parentCode = OrgHelper.getRelationCodeByRelationId(parentRelId);
        AuPartyRelationVo relvo = new AuPartyRelationVo();
        relvo.setPartyid(partyid);
        relvo.setParent_code(parentCode);
        AuPartyRelationVo departmentRelVo = (AuPartyRelationVo)relBs.queryAuPartyRelation(relvo).get(0);
        IHistoryLogBs historyBs = (IHistoryLogBs) Helper.getBean(LOG_BS_KEY);
            	Map map = new HashMap();
        	map.put("OPERATERID",vo.getParty_id());
        	map.put("OPERATERNAME",vo.getName());
        	map.put("OPERATETYPE", GlobalConstants.HISTORY_LOG_INSERT);
        	map.put("SYSDATE", DateTools.getSysTimestamp());
        	map.put("DEPARTMENTVO",deptVo);
        	map.put("SOURCEID",departmentRelVo.getId());
    		map.put("SOURCECODE",departmentRelVo.getCode());
    		map.put("SOURCEORGTREE", OrgHelper.getOrgNameByCode(departmentRelVo.getCode(),true)); //由于这里保存的是父节点，所以要显示最后一级节点
        	historyBs.insert(map);
        	return partyid;	    
	}
	
	/**
	 * 更新单条记录，同时调用接口更新相应的团体、团体关系记录并记录历史日志
	 */
	public int update(DepartmentVo deptVo, LoginSessionVo vo) {
        	IHistoryLogBs historyBs = (IHistoryLogBs) Helper.getBean(LOG_BS_KEY);
        	Map map = new HashMap();
        	map.put("OPERATERID",vo.getParty_id());
        	map.put("OPERATERNAME",vo.getName());
        	map.put("OPERATETYPE", GlobalConstants.HISTORY_LOG_UPDATE);
        	map.put("SYSDATE", DateTools.getSysTimestamp());
        	
        	String code[] = new String[]{new String()};
        	code = OrgHelper.getRelationCode(deptVo.getId());
            	for(int j = 0; j < code.length; j++) {
            	    IAuPartyRelationBs relBs = (IAuPartyRelationBs) Helper.getBean(IConstants.BS_KEY);
                    AuPartyRelationVo relvo = new AuPartyRelationVo();
                    relvo.setCode(code[j]);
                    map.put("SOURCEID",((AuPartyRelationVo)relBs.queryAuPartyRelation(relvo).get(0)).getId());
                    map.put("SOURCECODE",code[j]);
            		map.put("SOURCEORGTREE", OrgHelper.getOrgNameByCode(code[j],false));
            	}         	
            	int rows = departmentBs.update(deptVo);
            	map.put("DEPARTMENTVO",departmentBs.find(deptVo.getId()));
        	historyBs.insert(map);
        	return rows;
	}
	
	/**
	 * 删除团体同时将团体的详细信息记录到历史表中
	 */
	public int delete(String partyId[],LoginSessionVo vo) {
    	IHistoryLogBs historyBs = (IHistoryLogBs) Helper.getBean(LOG_BS_KEY);
    	DepartmentVo deptVo = new DepartmentVo();
    	Map map = new HashMap();
    	map.put("OPERATERID",vo.getParty_id());
    	map.put("OPERATERNAME",vo.getName());
    	map.put("OPERATETYPE", GlobalConstants.HISTORY_LOG_DELETE);
    	map.put("SYSDATE", DateTools.getSysTimestamp());
    	String code[] = new String[]{new String()};
    	for (int i = 0; i < partyId.length; i++) {
    		deptVo = departmentBs.find(partyId[i]);  //通过id获取vo
    		deptVo.setModify_date(DateTools.getSysTimestamp());
	    	map.put("DEPARTMENTVO",deptVo);
	    	code = OrgHelper.getRelationCode(deptVo.getId());
	    	for(int j = 0; j < code.length; j++) {
	    	    IAuPartyRelationBs relBs = (IAuPartyRelationBs) Helper.getBean(IConstants.BS_KEY);
                AuPartyRelationVo relvo = new AuPartyRelationVo();
                relvo.setCode(code[j]);
                map.put("SOURCEID",((AuPartyRelationVo)relBs.queryAuPartyRelation(relvo).get(0)).getId());
                map.put("SOURCECODE",code[j]);
	    		map.put("SOURCEORGTREE", OrgHelper.getOrgNameByCode(code[j],false));
	    		historyBs.insert(map);	  
	    	}    		
    	}
		return departmentBs.delete(partyId);
	}
	
	/**
	 * 删除团体关系,当人员只有一条关系时删除团体并记录日志
	 */
	public int delete(String relationId,LoginSessionVo vo) {
    	String partyId = OrgHelper.getPartyIDByRelationID(relationId);
    	IHistoryLogBs historyBs = (IHistoryLogBs) Helper.getBean(LOG_BS_KEY);
    	DepartmentVo deptVo = departmentBs.find(partyId);  //通过id获取vo
    	Map map = new HashMap();
    	map.put("OPERATERID",vo.getParty_id());
    	map.put("OPERATERNAME",vo.getName());
    	map.put("OPERATETYPE", GlobalConstants.HISTORY_LOG_DELETE);
    	map.put("SYSDATE", DateTools.getSysTimestamp());
    	deptVo.setModify_date(DateTools.getSysTimestamp());
    	map.put("DEPARTMENTVO",deptVo);
    	String code = OrgHelper.getRelationCodeByRelationId(relationId);
    	map.put("SOURCEID",relationId);
		map.put("SOURCECODE",code);
		map.put("SOURCEORGTREE", OrgHelper.getOrgNameByCode(code,false));
    	historyBs.insert(map);
		return departmentBs.delete(relationId);
	}
}

