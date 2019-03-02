package venus.oa.organization.employee.adaptor;

import venus.oa.organization.employee.vo.EmployeeVo;
import venus.oa.history.model.adaptor.HistoryAdaptor;
import venus.oa.history.vo.HistoryLogVo;
import venus.oa.util.GlobalConstants;
import venus.oa.util.transform.TransformStrategy;
import venus.oa.util.transform.json.JsonDataTools;

import java.sql.Timestamp;
import java.util.Map;

public class EmployeeAdaptor implements HistoryAdaptor {

	/**
	 * 与历史日志表适配
	 */
	public HistoryLogVo assembler(Object vo) {
		Map map = (Map)vo;
		HistoryLogVo historyVo = new HistoryLogVo();
		EmployeeVo empVo = (EmployeeVo)map.get("EMPLOYEEVO");
		TransformStrategy ts = new JsonDataTools();
		historyVo.setOperate_id((String) map.get("OPERATERID"));
		historyVo.setOperate_name((String) map.get("OPERATERNAME"));
		historyVo.setOperate_type((String) map.get("OPERATETYPE"));
		historyVo.setOperate_date((Timestamp) map.get("SYSDATE"));
		historyVo.setSource_code((String)map.get("SOURCECODE"));
		historyVo.setSource_orgtree((String)map.get("SOURCEORGTREE"));
		historyVo.setSource_id((String)map.get("SOURCEID"));
		if (empVo != null) {
			//historyVo.setSource_id(empVo.getId());
			historyVo.setSource_partyid(empVo.getId());
			historyVo.setSource_name(empVo.getPerson_name());
			historyVo.setSource_typeid(GlobalConstants.getPartyType_empl());
			historyVo.setSource_detail(ts.transform(empVo));
		}
		return historyVo;
	}
	
}

