package venus.oa.organization.employee.bs.impl;

import org.springframework.stereotype.Service;
import venus.oa.helper.OrgHelper;
import venus.oa.login.vo.LoginSessionVo;
import venus.oa.organization.aupartyrelation.bs.IAuPartyRelationBs;
import venus.oa.organization.aupartyrelation.util.IConstants;
import venus.oa.organization.aupartyrelation.vo.AuPartyRelationVo;
import venus.oa.organization.employee.bs.IEmployeeBs;
import venus.oa.organization.employee.bs.IEmployeeFacadeBs;
import venus.oa.organization.employee.util.IEmployeeConstants;
import venus.oa.organization.employee.vo.EmployeeVo;
import venus.oa.history.bs.IHistoryLogBs;
import venus.oa.util.DateTools;
import venus.oa.util.GlobalConstants;
import venus.frames.mainframe.util.Helper;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmployeeFacadeBs implements IEmployeeFacadeBs,IEmployeeConstants {
  
	private IEmployeeBs employeeBs;
	
	/**
	 * @param employeeBs 要设置的 employeeBs。
	 */
	public void setEmployeeBs(IEmployeeBs employeeBs) {
		this.employeeBs = employeeBs;
	}	
	
	/**
	 * 添加新记录，同时添加团体、团体关系并记录历史日志（如果parentRelId为空则不添加团体关系）
	 */
	public String insert(EmployeeVo empVo, String parentRelId,LoginSessionVo vo) {
	        String partyid = employeeBs.insert(empVo, parentRelId); //tony 20090806 先插入后记录历史
	        IAuPartyRelationBs relBs = (IAuPartyRelationBs) Helper.getBean(IConstants.BS_KEY);
	        String parentCode = OrgHelper.getRelationCodeByRelationId(parentRelId);
	        AuPartyRelationVo relvo = new AuPartyRelationVo();
	        relvo.setPartyid(partyid);
	        relvo.setParent_code(parentCode);
	        AuPartyRelationVo employeeRelVo = (AuPartyRelationVo)relBs.queryAuPartyRelation(relvo).get(0);
        	IHistoryLogBs historyBs = (IHistoryLogBs)Helper.getBean(LOG_BS_KEY);
            	Map map = new HashMap();
        	map.put("OPERATERID",vo.getParty_id());
        	map.put("OPERATERNAME",vo.getName());
        	map.put("OPERATETYPE", GlobalConstants.HISTORY_LOG_INSERT);
        	map.put("SYSDATE", DateTools.getSysTimestamp());
        	map.put("EMPLOYEEVO",empVo);
        	map.put("SOURCEID",employeeRelVo.getId());
    		map.put("SOURCECODE",employeeRelVo.getCode());//TODO ProjTools.getNewTreeCode(5, code)这种做法在多线程并发时不保险，合理的做法是先插入后获取在记录历史。
    		map.put("SOURCEORGTREE", OrgHelper.getOrgNameByCode(employeeRelVo.getCode(),true)); //由于这里保存的是父节点，所以要显示最后一级节点
        	historyBs.insert(map);
        	return partyid;	    
	}
	
	/**
	 * 更新单条记录，同时调用接口更新相应的团体、团体关系记录并记录历史日志
	 */
	public int update(EmployeeVo empVo,LoginSessionVo vo) {
        	IHistoryLogBs historyBs = (IHistoryLogBs)Helper.getBean(LOG_BS_KEY);
        	Map map = new HashMap();
        	map.put("OPERATERID",vo.getParty_id());
        	map.put("OPERATERNAME",vo.getName());
        	map.put("OPERATETYPE", GlobalConstants.HISTORY_LOG_UPDATE);
        	map.put("SYSDATE", DateTools.getSysTimestamp());
        	String code[] = new String[]{new String()};
        	code = OrgHelper.getRelationCode(empVo.getId());
        	int rows = employeeBs.update(empVo);
        	map.put("EMPLOYEEVO",employeeBs.find(empVo.getId()));
            	for(int j = 0; j < code.length; j++) {
            	    IAuPartyRelationBs relBs = (IAuPartyRelationBs) Helper.getBean(IConstants.BS_KEY);
                    AuPartyRelationVo relvo = new AuPartyRelationVo();
                    relvo.setCode(code[j]);
                    map.put("SOURCEID",((AuPartyRelationVo)relBs.queryAuPartyRelation(relvo).get(0)).getId());
                    map.put("SOURCECODE",code[j]);
            		map.put("SOURCEORGTREE", OrgHelper.getOrgNameByCode(code[j],false));
            		historyBs.insert(map);
            	}         	
        	return rows;
	}		
    
	/**
	 * 删除团体同时将团体的详细信息记录到历史表中
	 */
	public int delete(String partyId[],LoginSessionVo vo) {
    	IHistoryLogBs historyBs = (IHistoryLogBs)Helper.getBean(LOG_BS_KEY);
    	EmployeeVo empVo = new EmployeeVo();
    	Map map = new HashMap();
    	map.put("OPERATERID",vo.getParty_id());
    	map.put("OPERATERNAME",vo.getName());
    	map.put("OPERATETYPE", GlobalConstants.HISTORY_LOG_DELETE);
    	map.put("SYSDATE", DateTools.getSysTimestamp());
    	String code[] = null;
    	for (int i = 0; i < partyId.length; i++) {
    		empVo = employeeBs.find(partyId[i]);  //通过id获取vo
    		empVo.setModify_date(DateTools.getSysTimestamp());
	    	map.put("EMPLOYEEVO",empVo);
	    	code = OrgHelper.getRelationCode(empVo.getId());
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
		return employeeBs.delete(partyId);
	}
	
	/**
	 * 删除团体关系,当人员只有一条关系时删除团体并记录日志
	 */
	public int delete(String relationId,LoginSessionVo vo) {
    	String partyId = OrgHelper.getPartyIDByRelationID(relationId);
    	//if (1== OrgHelper.getRelationCode(partyId).length) { //只有一条关系时删除团体并记录日志
        	IHistoryLogBs historyBs = (IHistoryLogBs)Helper.getBean(LOG_BS_KEY);
        	EmployeeVo  empVo = employeeBs.find(partyId);  //通过id获取vo
        	Map map = new HashMap();
        	map.put("OPERATERID",vo.getParty_id());
        	map.put("OPERATERNAME",vo.getName());
        	map.put("OPERATETYPE", GlobalConstants.HISTORY_LOG_DELETE);
        	map.put("SYSDATE", DateTools.getSysTimestamp());
        	empVo.setModify_date(DateTools.getSysTimestamp());
	    	map.put("EMPLOYEEVO",empVo);
	    	String code = OrgHelper.getRelationCodeByRelationId(relationId);
	    	map.put("SOURCEID",relationId);
    		map.put("SOURCECODE",code);
    		map.put("SOURCEORGTREE", OrgHelper.getOrgNameByCode(code,false));
	    	historyBs.insert(map);
    	//}
		return employeeBs.delete(relationId);
	}

}

