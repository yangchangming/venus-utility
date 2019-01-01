package venus.oa.organization.company.adaptor;

import venus.oa.organization.company.vo.CompanyVo;
import venus.oa.service.history.model.adaptor.HistoryAdaptor;
import venus.oa.service.history.vo.HistoryLogVo;
import venus.oa.util.GlobalConstants;
import venus.oa.util.transform.TransformStrategy;
import venus.oa.util.transform.json.JsonDataTools;

import java.sql.Timestamp;
import java.util.Map;

public class CompanyAdaptor implements HistoryAdaptor {

	/**
	 * 与历史日志表适配
	 */
	public HistoryLogVo assembler(Object vo) {
		Map map = (Map)vo;
		HistoryLogVo historyVo = new HistoryLogVo();
		CompanyVo companyVo = (CompanyVo)map.get("COMPANYVO");
		TransformStrategy ts = new JsonDataTools();
		historyVo.setOperate_id((String) map.get("OPERATERID"));
		historyVo.setOperate_name((String) map.get("OPERATERNAME"));
		historyVo.setOperate_type((String) map.get("OPERATETYPE"));
		historyVo.setOperate_date((Timestamp) map.get("SYSDATE"));
		historyVo.setSource_code((String)map.get("SOURCECODE"));
		historyVo.setSource_orgtree((String)map.get("SOURCEORGTREE"));	
		historyVo.setSource_id((String)map.get("SOURCEID"));
		if (companyVo != null) {
			//historyVo.setSource_id(companyVo.getId());
			historyVo.setSource_partyid(companyVo.getId());
			historyVo.setSource_name(companyVo.getCompany_name());
			historyVo.setSource_typeid(GlobalConstants.getPartyType_comp());
			historyVo.setSource_detail(ts.transform(companyVo));
		}
		return historyVo;
	}

}

