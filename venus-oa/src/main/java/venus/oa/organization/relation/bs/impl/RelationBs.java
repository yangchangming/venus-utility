package venus.oa.organization.relation.bs.impl;

import org.springframework.stereotype.Service;
import venus.oa.helper.OrgHelper;
import venus.oa.login.vo.LoginSessionVo;
import venus.oa.organization.aupartyrelation.bs.IAuPartyRelationBs;
import venus.oa.organization.aupartyrelation.util.IConstants;
import venus.oa.organization.aupartyrelation.vo.AuPartyRelationVo;
import venus.oa.organization.employee.bs.IEmployeeBs;
import venus.oa.organization.employee.util.IEmployeeConstants;
import venus.oa.organization.employee.vo.EmployeeVo;
import venus.oa.organization.relation.bs.IRelationBs;
import venus.oa.history.bs.IHistoryLogBs;
import venus.oa.util.DateTools;
import venus.oa.util.GlobalConstants;
import venus.frames.base.bs.BaseBusinessService;
import venus.frames.mainframe.util.Helper;

import java.util.HashMap;
import java.util.Map;

@Service
public class RelationBs extends BaseBusinessService implements IRelationBs {

    public void addRelation(String[] partyIds, String parentRelId, String relType,LoginSessionVo vo) {
        if (partyIds != null && partyIds.length > 0) {
            IEmployeeBs empBs = (IEmployeeBs) Helper.getBean(IEmployeeConstants.BS_KEY);
            IHistoryLogBs historyBs = (IHistoryLogBs)Helper.getBean(IEmployeeConstants.LOG_BS_KEY);
            IAuPartyRelationBs relBs = (IAuPartyRelationBs) Helper.getBean(IConstants.BS_KEY);
            AuPartyRelationVo relvo = new AuPartyRelationVo();
            for(int i=0; i<partyIds.length; i++) {
                OrgHelper.addPartyRelation(partyIds[i], parentRelId, relType);//调用接口添加团体关系
                EmployeeVo empVo = empBs.find(partyIds[i]);
                relvo.setPartyid(partyIds[i]);
                relvo.setParent_code(OrgHelper.getRelationCodeByRelationId(parentRelId));
                AuPartyRelationVo empRelVo = (AuPartyRelationVo)relBs.queryAuPartyRelation(relvo).get(0);
                Map map = new HashMap();
                map.put("OPERATERID",vo.getParty_id());
                map.put("OPERATERNAME",vo.getName());
                map.put("OPERATETYPE", GlobalConstants.HISTORY_LOG_RELATION);
                map.put("SYSDATE", DateTools.getSysTimestamp());
                map.put("EMPLOYEEVO",empVo);
                map.put("SOURCEID",empRelVo.getId());
                map.put("SOURCECODE",empRelVo.getCode());
                map.put("SOURCEORGTREE", OrgHelper.getOrgNameByCode(empRelVo.getCode(), true)); //由于这里保存的是父节点，所以要显示最后一级节点
                historyBs.insert(map);
            }
        }
    }

}

