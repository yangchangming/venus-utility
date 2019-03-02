package venus.oa.organization.department.adaptor;

import venus.oa.organization.department.vo.DepartmentVo;
import venus.oa.history.model.adaptor.HistoryAdaptor;
import venus.oa.history.vo.HistoryLogVo;
import venus.oa.util.GlobalConstants;
import venus.oa.util.transform.TransformStrategy;
import venus.oa.util.transform.json.JsonDataTools;

import java.sql.Timestamp;
import java.util.Map;

public class DepartmentAdaptor implements HistoryAdaptor {
	
	/**
	 * 与历史日志表适配
	 */
	public HistoryLogVo assembler(Object vo) {
		Map map = (Map)vo;
		HistoryLogVo historyVo = new HistoryLogVo();
		DepartmentVo departVo = (DepartmentVo)map.get("DEPARTMENTVO");
		TransformStrategy ts = new JsonDataTools();
		historyVo.setOperate_id((String) map.get("OPERATERID"));
		historyVo.setOperate_name((String) map.get("OPERATERNAME"));
		historyVo.setOperate_type((String) map.get("OPERATETYPE")); 
		historyVo.setOperate_date((Timestamp) map.get("SYSDATE"));
		historyVo.setSource_code((String)map.get("SOURCECODE"));
		historyVo.setSource_orgtree((String)map.get("SOURCEORGTREE"));
		historyVo.setSource_id((String)map.get("SOURCEID"));
		if (departVo != null) {
			//historyVo.setSource_id(departVo.getId());
			historyVo.setSource_partyid(departVo.getId());
			historyVo.setSource_name(departVo.getDept_name());
			historyVo.setSource_typeid(GlobalConstants.getPartyType_dept());
			historyVo.setSource_detail(ts.transform(departVo));
		}
		return historyVo;
	}

}

