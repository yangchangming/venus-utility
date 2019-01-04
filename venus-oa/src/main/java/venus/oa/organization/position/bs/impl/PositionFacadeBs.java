package venus.oa.organization.position.bs.impl;

import venus.oa.helper.OrgHelper;
import venus.oa.login.vo.LoginSessionVo;
import venus.oa.organization.aupartyrelation.bs.IAuPartyRelationBs;
import venus.oa.organization.aupartyrelation.util.IConstants;
import venus.oa.organization.aupartyrelation.vo.AuPartyRelationVo;
import venus.oa.organization.position.bs.IPositionBs;
import venus.oa.organization.position.bs.IPositionFacadeBs;
import venus.oa.organization.position.util.IPositionConstants;
import venus.oa.organization.position.vo.PositionVo;
import venus.oa.service.history.bs.IHistoryLogBs;
import venus.oa.util.DateTools;
import venus.oa.util.GlobalConstants;
import venus.frames.mainframe.util.Helper;

import java.util.HashMap;
import java.util.Map;

public class PositionFacadeBs implements IPositionFacadeBs, IPositionConstants {
  
	private IPositionBs positionBs;
	
	/**
	 * @param positionBs 要设置的 positionBs。
	 */
	public void setPositionBs(IPositionBs positionBs) {
		this.positionBs = positionBs;
	}
	
	/**
	 * 添加新记录，同时添加团体、团体关系并记录历史日志（如果parentRelId为空则不添加团体关系）
	 */
	public String insert(PositionVo postionVo, String parentRelId, LoginSessionVo vo) {
	        String partyid = positionBs.insert(postionVo, parentRelId);
	        IAuPartyRelationBs relBs = (IAuPartyRelationBs) Helper.getBean(IConstants.BS_KEY);
            String parentCode = OrgHelper.getRelationCodeByRelationId(parentRelId);
            AuPartyRelationVo relvo = new AuPartyRelationVo();
            relvo.setPartyid(partyid);
            relvo.setParent_code(parentCode);
            AuPartyRelationVo positionRelVo = (AuPartyRelationVo)relBs.queryAuPartyRelation(relvo).get(0);
            IHistoryLogBs historyBs = (IHistoryLogBs) Helper.getBean(LOG_BS_KEY);
            	Map map = new HashMap();
        	map.put("OPERATERID",vo.getParty_id());
        	map.put("OPERATERNAME",vo.getName());
        	map.put("OPERATETYPE", GlobalConstants.HISTORY_LOG_INSERT);
        	map.put("SYSDATE", DateTools.getSysTimestamp());
        	map.put("POSITIONVO",postionVo);
        	map.put("SOURCEID",positionRelVo.getId());
    		map.put("SOURCECODE",positionRelVo.getCode());
    		map.put("SOURCEORGTREE", OrgHelper.getOrgNameByCode(positionRelVo.getCode(),true)); //由于这里保存的是父节点，所以要显示最后一级节点
        	historyBs.insert(map);
        	return partyid;	    
	}
	
	/**
	 * 更新单条记录，同时调用接口更新相应的团体、团体关系记录并记录历史日志
	 */
	public int update(PositionVo postionVo, LoginSessionVo vo) {
        	IHistoryLogBs historyBs = (IHistoryLogBs) Helper.getBean(LOG_BS_KEY);
        	Map map = new HashMap();
        	map.put("OPERATERID",vo.getParty_id());
        	map.put("OPERATERNAME",vo.getName());
        	map.put("OPERATETYPE", GlobalConstants.HISTORY_LOG_UPDATE);
        	map.put("SYSDATE", DateTools.getSysTimestamp());
        	
        	String code[] = new String[]{new String()};
        	code = OrgHelper.getRelationCode(postionVo.getId());
            	for(int j = 0; j < code.length; j++) {
            	    IAuPartyRelationBs relBs = (IAuPartyRelationBs) Helper.getBean(IConstants.BS_KEY);
                    AuPartyRelationVo relvo = new AuPartyRelationVo();
                    relvo.setCode(code[j]);
                    map.put("SOURCEID",((AuPartyRelationVo)relBs.queryAuPartyRelation(relvo).get(0)).getId());
                    map.put("SOURCECODE",code[j]);
            		map.put("SOURCEORGTREE", OrgHelper.getOrgNameByCode(code[j], false));
            	}         	
            	int rows = positionBs.update(postionVo); 
            	map.put("POSITIONVO",positionBs.find(postionVo.getId()));
        	historyBs.insert(map);
        	return rows;   
	}	
	
	/**
	 * 删除团体同时将团体的详细信息记录到历史表中
	 */
	public int delete(String partyId[],LoginSessionVo vo) {
    	IHistoryLogBs historyBs = (IHistoryLogBs) Helper.getBean(LOG_BS_KEY);
    	PositionVo posVo = new PositionVo();
    	Map map = new HashMap();
    	map.put("OPERATERID",vo.getParty_id());
    	map.put("OPERATERNAME",vo.getName());
    	map.put("OPERATETYPE", GlobalConstants.HISTORY_LOG_DELETE);
    	map.put("SYSDATE", DateTools.getSysTimestamp());
    	String code[] = new String[]{new String()};
    	for (int i = 0; i < partyId.length; i++) {
    		posVo = positionBs.find(partyId[i]);  //通过id获取vo
    		posVo.setModify_date(DateTools.getSysTimestamp());
	    	map.put("POSITIONVO",posVo);
	    	code = OrgHelper.getRelationCode(posVo.getId());
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
		return positionBs.delete(partyId);
	}
	
	/**
	 * 删除团体关系,当人员只有一条关系时删除团体并记录日志
	 */
	public int delete(String relationId,LoginSessionVo vo) {
    	String partyId = OrgHelper.getPartyIDByRelationID(relationId);
    	IHistoryLogBs historyBs = (IHistoryLogBs) Helper.getBean(LOG_BS_KEY);
    	PositionVo posVo = positionBs.find(partyId);  //通过id获取vo
    	Map map = new HashMap();
    	map.put("OPERATERID",vo.getParty_id());
    	map.put("OPERATERNAME",vo.getName());
    	map.put("OPERATETYPE", GlobalConstants.HISTORY_LOG_DELETE);
    	map.put("SYSDATE", DateTools.getSysTimestamp());
    	posVo.setModify_date(DateTools.getSysTimestamp());
    	map.put("POSITIONVO",posVo);
    	String code = OrgHelper.getRelationCodeByRelationId(relationId);
    	map.put("SOURCEID",relationId);
		map.put("SOURCECODE",code);
		map.put("SOURCEORGTREE", OrgHelper.getOrgNameByCode(code,false));
    	historyBs.insert(map);
		return positionBs.delete(relationId);
	}
}
