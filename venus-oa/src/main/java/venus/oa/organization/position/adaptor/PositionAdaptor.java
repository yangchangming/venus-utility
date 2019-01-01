package venus.oa.organization.position.adaptor;

import venus.oa.organization.position.vo.PositionVo;
import venus.oa.service.history.model.adaptor.HistoryAdaptor;
import venus.oa.service.history.vo.HistoryLogVo;
import venus.oa.util.GlobalConstants;
import venus.oa.util.transform.TransformStrategy;
import venus.oa.util.transform.json.JsonDataTools;

import java.sql.Timestamp;
import java.util.Map;

public class PositionAdaptor implements HistoryAdaptor {

	/**
	 * 与历史日志表适配
	 */
	public HistoryLogVo assembler(Object vo) {
		Map map = (Map)vo;
		HistoryLogVo historyVo = new HistoryLogVo();
		PositionVo posVo = (PositionVo)map.get("POSITIONVO");
		TransformStrategy ts = new JsonDataTools();
		historyVo.setOperate_id((String) map.get("OPERATERID"));
		historyVo.setOperate_name((String) map.get("OPERATERNAME"));
		historyVo.setOperate_type((String) map.get("OPERATETYPE"));
		historyVo.setOperate_date((Timestamp) map.get("SYSDATE"));
		historyVo.setSource_code((String)map.get("SOURCECODE"));
		historyVo.setSource_orgtree((String)map.get("SOURCEORGTREE"));	
		historyVo.setSource_id((String)map.get("SOURCEID"));
		if (posVo != null) {
			//historyVo.setSource_id(posVo.getId());
			historyVo.setSource_partyid(posVo.getId());
			historyVo.setSource_name(posVo.getPosition_name());
			historyVo.setSource_typeid(GlobalConstants.getPartyType_posi());
			historyVo.setSource_detail(ts.transform(posVo));
		}
		return historyVo;
	}

}

