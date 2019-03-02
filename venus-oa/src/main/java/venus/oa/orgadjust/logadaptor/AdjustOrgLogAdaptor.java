/*
 * 创建日期 2008-10-30
 *
 */
package venus.oa.orgadjust.logadaptor;

import venus.oa.organization.auparty.bs.IAuPartyBs;
import venus.oa.organization.auparty.vo.PartyVo;
import venus.oa.organization.aupartyrelation.vo.AuPartyRelationVo;
import venus.oa.history.model.adaptor.HistoryAdaptor;
import venus.oa.history.vo.HistoryLogVo;
import venus.oa.util.GlobalConstants;
import venus.oa.util.tree.DeepTreeSearch;

import java.sql.Timestamp;
import java.util.Map;

/**
 * @author zangjian
 * 
 * 调级日志适配器 
 */
public class AdjustOrgLogAdaptor implements HistoryAdaptor {
	private IAuPartyBs partyBs;

	/**
	 * @param partyBs
	 *                    要设置的 partyBs。
	 */
	public void setPartyBs(IAuPartyBs partyBs) {
		this.partyBs = partyBs;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see venus.authority.service.history.model.adaptor.HistoryAdaptor#assembler(java.lang.Object)
	 */
	public HistoryLogVo assembler(Object vo) {
		Map map = (Map) vo;
		HistoryLogVo historyVo = new HistoryLogVo();
		AuPartyRelationVo relationVo = (AuPartyRelationVo) map.get("HISTORYVO");
		AuPartyRelationVo destVo = (AuPartyRelationVo) map.get("HISTORYDESTVO");
		String orgId = (String) map.get("ORGID");
		historyVo.setOperate_date((Timestamp) map.get("SYSDATE"));
		historyVo.setOperate_id(orgId);
		PartyVo partyVo = (PartyVo) partyBs.find(orgId);
		historyVo.setOperate_name(partyVo == null?orgId : partyVo.getName());
		historyVo.setOperate_type(GlobalConstants.HISTORY_LOG_ADJUST);//业务调级
		historyVo.setSource_code(relationVo.getCode());
		historyVo.setSource_id(relationVo.getId());
		historyVo.setSource_partyid(relationVo.getPartyid());
		historyVo.setSource_name(relationVo.getName());
		historyVo.setSource_orgtree(DeepTreeSearch.getOrgNameByHistoryCode(relationVo, false));
		historyVo.setDest_id(destVo.getId());
		historyVo.setDest_code(destVo.getCode());
		historyVo.setDest_name(destVo.getName());
		historyVo.setDest_orgtree(DeepTreeSearch.getOrgNameByCode(destVo.getCode()));
		return historyVo;
	}

}

