package venus.oa.organization.company.bs.impl;

import venus.oa.helper.OrgHelper;
import venus.oa.login.vo.LoginSessionVo;
import venus.oa.organization.aupartyrelation.bs.IAuPartyRelationBs;
import venus.oa.organization.aupartyrelation.util.IConstants;
import venus.oa.organization.aupartyrelation.vo.AuPartyRelationVo;
import venus.oa.organization.company.bs.ICompanyBs;
import venus.oa.organization.company.bs.ICompanyFacadeBs;
import venus.oa.organization.company.util.ICompanyConstants;
import venus.oa.organization.company.vo.CompanyVo;
import venus.oa.service.history.bs.IHistoryLogBs;
import venus.oa.util.DateTools;
import venus.oa.util.GlobalConstants;
import venus.oa.util.ProjTools;
import venus.frames.mainframe.util.Helper;

import java.util.HashMap;
import java.util.Map;

public class CompanyFacadeBs implements ICompanyFacadeBs, ICompanyConstants {

	private ICompanyBs companyBs;
	
	/**
	 * @param companyBs 要设置的 companyBs。
	 */
	public void setCompanyBs(ICompanyBs companyBs) {
		this.companyBs = companyBs;
	}
	
	/**
	 * 添加新记录，同时添加团体、团体关系根节点并记录历史日志
	 */
	public String insertRoot(CompanyVo companyVo, LoginSessionVo vo) {
	        String partyid = companyBs.insertRoot(companyVo);
	        IAuPartyRelationBs relBs = (IAuPartyRelationBs) Helper.getBean(IConstants.BS_KEY);
            AuPartyRelationVo relvo = new AuPartyRelationVo();
            relvo.setPartyid(partyid);
            relvo.setRelationtype_id(GlobalConstants.getRelaType_comp());
            AuPartyRelationVo companyRelVo = (AuPartyRelationVo)relBs.queryAuPartyRelation(relvo).get(0);
            IHistoryLogBs historyBs = (IHistoryLogBs) Helper.getBean(LOG_BS_KEY);
            	Map map = new HashMap();
        	map.put("OPERATERID",vo.getParty_id());
        	map.put("OPERATERNAME",vo.getName());
        	map.put("OPERATETYPE", GlobalConstants.HISTORY_LOG_INSERT);
        	map.put("SYSDATE", DateTools.getSysTimestamp());
        	map.put("COMPANYVO",companyVo);
        	map.put("SOURCEID",companyRelVo.getId());
    		map.put("SOURCECODE", ProjTools.getNewTreeCode(5, GlobalConstants.getRelaType_comp()));
    		map.put("SOURCEORGTREE","");
        	historyBs.insert(map);
        	return partyid;
	}
	
	/**
	 * 添加新记录，同时添加团体、团体关系并记录历史日志（如果parentRelId为空则不添加团体关系）
	 */
	public String insert(CompanyVo companyVo, String parentRelId, LoginSessionVo vo) {
	        String partyid = companyBs.insert(companyVo, parentRelId);
    	    IAuPartyRelationBs relBs = (IAuPartyRelationBs) Helper.getBean(IConstants.BS_KEY);
            String parentCode = OrgHelper.getRelationCodeByRelationId(parentRelId);
            AuPartyRelationVo relvo = new AuPartyRelationVo();
            relvo.setPartyid(partyid);
            relvo.setParent_code(parentCode);
            AuPartyRelationVo companyRelVo = (AuPartyRelationVo)relBs.queryAuPartyRelation(relvo).get(0);
            IHistoryLogBs historyBs = (IHistoryLogBs) Helper.getBean(LOG_BS_KEY);
            Map map = new HashMap();
        	map.put("OPERATERID",vo.getParty_id());
        	map.put("OPERATERNAME",vo.getName());
        	map.put("OPERATETYPE", GlobalConstants.HISTORY_LOG_INSERT);
        	map.put("SYSDATE", DateTools.getSysTimestamp());
        	map.put("COMPANYVO",companyVo);
        	map.put("SOURCEID",companyRelVo.getId());
    		map.put("SOURCECODE",companyRelVo.getCode());
    		map.put("SOURCEORGTREE", OrgHelper.getOrgNameByCode(companyRelVo.getCode(),true)); //由于这里保存的是父节点，所以要显示最后一级节点
        	historyBs.insert(map);
        	return partyid;
	} 
	
	/**
	 * 更新单条记录，同时调用接口更新相应的团体、团体关系记录并记下历史日志
	 */
	public int update(CompanyVo companyVo, LoginSessionVo vo) {
        	IHistoryLogBs historyBs = (IHistoryLogBs) Helper.getBean(LOG_BS_KEY);
            	Map map = new HashMap();
        	map.put("OPERATERID",vo.getParty_id());
        	map.put("OPERATERNAME",vo.getName());
        	map.put("OPERATETYPE", GlobalConstants.HISTORY_LOG_UPDATE);
        	map.put("SYSDATE", DateTools.getSysTimestamp());
        	String code[] = new String[]{new String()};
        	code = OrgHelper.getRelationCode(companyVo.getId());
	    	for(int j = 0; j < code.length; j++) {
	    	    IAuPartyRelationBs relBs = (IAuPartyRelationBs) Helper.getBean(IConstants.BS_KEY);
                AuPartyRelationVo relvo = new AuPartyRelationVo();
                relvo.setCode(code[j]);
                map.put("SOURCEID",((AuPartyRelationVo)relBs.queryAuPartyRelation(relvo).get(0)).getId());
                map.put("SOURCECODE",code[j]);
	    		map.put("SOURCEORGTREE", OrgHelper.getOrgNameByCode(code[j],false));
	    	}
	    	int rows = companyBs.update(companyVo); 
	    	map.put("COMPANYVO",companyBs.find(companyVo.getId()));
        	historyBs.insert(map);
        	return rows;
	}
	
	/**
	 * 删除团体同时将团体的详细信息记录到历史表中
	 */
	public int delete(String partyId[],LoginSessionVo vo) {
        	IHistoryLogBs historyBs = (IHistoryLogBs) Helper.getBean(LOG_BS_KEY);
        	CompanyVo companyVo = new CompanyVo();
        	Map map = new HashMap();
        	map.put("OPERATERID",vo.getParty_id());
        	map.put("OPERATERNAME",vo.getName());
        	map.put("OPERATETYPE", GlobalConstants.HISTORY_LOG_DELETE);
        	map.put("SYSDATE", DateTools.getSysTimestamp());
        	String code[] = new String[]{new String()};
        	for (int i = 0; i < partyId.length; i++) {
            		companyVo = companyBs.find(partyId[i]);  //通过id获取vo
            		companyVo.setModify_date(DateTools.getSysTimestamp());
        	    	map.put("COMPANYVO",companyVo);
        	    	code = OrgHelper.getRelationCode(companyVo.getId());
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
		return companyBs.delete(partyId);
	}
	
	/**
	 * 删除团体关系,当人员只有一条关系时删除团体并记录日志
	 */
	public int delete(String relationId,LoginSessionVo vo) {
        	String partyId = OrgHelper.getPartyIDByRelationID(relationId);
        	IHistoryLogBs historyBs = (IHistoryLogBs) Helper.getBean(LOG_BS_KEY);
        	CompanyVo companyVo = companyBs.find(partyId);  //通过id获取vo
        	Map map = new HashMap();
        	map.put("OPERATERID",vo.getParty_id());
        	map.put("OPERATERNAME",vo.getName());
        	map.put("OPERATETYPE", GlobalConstants.HISTORY_LOG_DELETE);
        	map.put("SYSDATE", DateTools.getSysTimestamp());
        	companyVo.setModify_date(DateTools.getSysTimestamp());
        	map.put("COMPANYVO",companyVo);
        	String code = OrgHelper.getRelationCodeByRelationId(relationId);
        	map.put("SOURCEID",relationId);
    		map.put("SOURCECODE",code);
    		map.put("SOURCEORGTREE", OrgHelper.getOrgNameByCode(code,false));
        	historyBs.insert(map);
		return companyBs.delete(relationId);
	}	

}

