package venus.oa.organization.employee.bs.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import venus.oa.helper.OrgHelper;
import venus.oa.history.bs.IHistoryLogBs;
import venus.oa.login.vo.LoginSessionVo;
import venus.oa.organization.aupartyrelation.bs.IAuPartyRelationBs;
import venus.oa.organization.aupartyrelation.vo.AuPartyRelationVo;
import venus.oa.organization.employee.bs.IEmployeeBs;
import venus.oa.organization.employee.bs.IEmployeeFacadeBs;
import venus.oa.organization.employee.util.IEmployeeConstants;
import venus.oa.organization.employee.vo.EmployeeVo;
import venus.oa.util.DateTools;
import venus.oa.util.GlobalConstants;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmployeeFacadeBs implements IEmployeeFacadeBs,IEmployeeConstants {

	@Autowired
	private IEmployeeBs employeeBs;

	@Autowired
	private IAuPartyRelationBs auPartyRelationBs;

	@Autowired
	private IHistoryLogBs historyLogBs;
	
	/**
	 * 添加新记录，同时添加团体、团体关系并记录历史日志（如果parentRelId为空则不添加团体关系）
	 */
	public String insert(EmployeeVo empVo, String parentRelId,LoginSessionVo vo) {
	        String partyid = employeeBs.insert(empVo, parentRelId); //tony 20090806 先插入后记录历史
	        String parentCode = OrgHelper.getRelationCodeByRelationId(parentRelId);
	        AuPartyRelationVo relvo = new AuPartyRelationVo();
	        relvo.setPartyid(partyid);
	        relvo.setParent_code(parentCode);
	        AuPartyRelationVo employeeRelVo = (AuPartyRelationVo)auPartyRelationBs.queryAuPartyRelation(relvo).get(0);
            	Map map = new HashMap();
        	map.put("OPERATERID",vo.getParty_id());
        	map.put("OPERATERNAME",vo.getName());
        	map.put("OPERATETYPE", GlobalConstants.HISTORY_LOG_INSERT);
        	map.put("SYSDATE", DateTools.getSysTimestamp());
        	map.put("EMPLOYEEVO",empVo);
        	map.put("SOURCEID",employeeRelVo.getId());
    		map.put("SOURCECODE",employeeRelVo.getCode());//TODO ProjTools.getNewTreeCode(5, code)这种做法在多线程并发时不保险，合理的做法是先插入后获取在记录历史。
    		map.put("SOURCEORGTREE", OrgHelper.getOrgNameByCode(employeeRelVo.getCode(),true)); //由于这里保存的是父节点，所以要显示最后一级节点
		historyLogBs.insert(map);
        	return partyid;	    
	}
	
	/**
	 * 更新单条记录，同时调用接口更新相应的团体、团体关系记录并记录历史日志
	 */
	public int update(EmployeeVo empVo,LoginSessionVo vo) {
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
                    AuPartyRelationVo relvo = new AuPartyRelationVo();
                    relvo.setCode(code[j]);
                    map.put("SOURCEID",((AuPartyRelationVo)auPartyRelationBs.queryAuPartyRelation(relvo).get(0)).getId());
                    map.put("SOURCECODE",code[j]);
            		map.put("SOURCEORGTREE", OrgHelper.getOrgNameByCode(code[j],false));
					historyLogBs.insert(map);
            	}         	
        	return rows;
	}		
    
	/**
	 * 删除团体同时将团体的详细信息记录到历史表中
	 */
	public int delete(String partyId[],LoginSessionVo vo) {
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
	            AuPartyRelationVo relvo = new AuPartyRelationVo();
	            relvo.setCode(code[j]);
	    	    map.put("SOURCEID",((AuPartyRelationVo)auPartyRelationBs.queryAuPartyRelation(relvo).get(0)).getId());
	    		map.put("SOURCECODE",code[j]);
	    		map.put("SOURCEORGTREE", OrgHelper.getOrgNameByCode(code[j],false));
				historyLogBs.insert(map);
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
		historyLogBs.insert(map);
    	//}
		return employeeBs.delete(relationId);
	}

}

