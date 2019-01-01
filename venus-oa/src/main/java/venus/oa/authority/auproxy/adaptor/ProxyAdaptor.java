/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.oa.authority.auproxy.adaptor;

import venus.oa.organization.auparty.vo.PartyVo;
import venus.oa.organization.aupartyrelation.vo.AuPartyRelationVo;
import venus.oa.service.history.model.adaptor.HistoryAdaptor;
import venus.oa.service.history.vo.HistoryLogVo;
import venus.oa.util.GlobalConstants;
import venus.oa.util.transform.TransformStrategy;
import venus.oa.util.transform.json.JsonDataTools;

import java.sql.Timestamp;
import java.util.Map;

/**
 * @author changming.Y <changming.yang.ah@gmail.com>
 *
 */
public class ProxyAdaptor implements HistoryAdaptor {

    /* (non-Javadoc)
     * @see venus.authority.service.history.model.adaptor.HistoryAdaptor#assembler(java.lang.Object)
     */
    public HistoryLogVo assembler(Object vo) {
        Map map = (Map)vo;
        HistoryLogVo historyVo = new HistoryLogVo();
        PartyVo porxyVo = (PartyVo)map.get("PROXYVO");
        AuPartyRelationVo relVo = (AuPartyRelationVo)map.get("PROXYRELATIONVO");
        TransformStrategy ts = new JsonDataTools();
        historyVo.setOperate_id((String) map.get("OPERATERID"));
        historyVo.setOperate_name((String) map.get("OPERATERNAME"));
        historyVo.setOperate_type((String) map.get("OPERATETYPE"));
        historyVo.setOperate_date((Timestamp) map.get("SYSDATE"));
        historyVo.setSource_code((String)map.get("SOURCECODE"));
        historyVo.setSource_orgtree((String)map.get("SOURCEORGTREE"));
        if (null != porxyVo) {
            historyVo.setSource_id(porxyVo.getId());
            historyVo.setSource_partyid(porxyVo.getId());
            historyVo.setSource_name(porxyVo.getName());
            historyVo.setSource_typeid(GlobalConstants.getPartyType_proxy());
            historyVo.setSource_detail(ts.transform(porxyVo));
        }else if(null != relVo){
            historyVo.setSource_id(relVo.getId());
            historyVo.setSource_partyid(relVo.getId());
            historyVo.setSource_name(relVo.getName());
            historyVo.setSource_typeid(GlobalConstants.getRelaType_proxy());
            historyVo.setSource_detail(ts.transform(relVo));
        }
        return historyVo;
    }

}

