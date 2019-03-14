package venus.oa.orgadjust.bs.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import venus.frames.base.bs.BaseBusinessService;
import venus.oa.history.bs.IHistoryLogBs;
import venus.oa.orgadjust.bs.IAdjustOrganizeBs;
import venus.oa.orgadjust.util.IContants;
import venus.oa.organization.aupartyrelation.bs.IAuPartyRelationBs;
import venus.oa.organization.aupartyrelation.vo.AuPartyRelationVo;
import venus.oa.util.DateTools;
import venus.pub.lang.OID;

import java.util.*;

@Service
public class AdjustOrganizeBs extends BaseBusinessService implements IAdjustOrganizeBs,IContants {

	@Autowired
	private IAuPartyRelationBs relationBs;

	@Autowired
	private IHistoryLogBs adjustOrgLogBs;

	private AuPartyRelationVo destVo;

	private List list; //谁写的傻逼代码！

	/**
	 * @param relationBs
	 */
	public void setRelationBs(IAuPartyRelationBs relationBs) {
		this.relationBs = relationBs;
	}

	/**
	 * 级联删除关系
	 * 
	 * @param code
	 */
	private void dropRelation(String code) {
		AuPartyRelationVo vo = new AuPartyRelationVo();
		vo.setCode(code);
		list = new ArrayList();
		list.addAll(relationBs.queryAllByCode(vo.getCode())); //获得当明节点下所有子节点数据
		list.addAll(relationBs.queryAuPartyRelation(vo)); //获得当前节点数据
		Collections.sort(list, new Comparator() { //删除时需要从叶子节点开始,需要将记录按叶子到父节点的顺序进行排序
			public int compare(Object src, Object dest) {
				AuPartyRelationVo relationVoSrc = (AuPartyRelationVo) src;
				AuPartyRelationVo relationVoDest = (AuPartyRelationVo) dest;
				long srcCodeLength = relationVoSrc.getCode().length();
				long destCodeLength = relationVoDest.getCode().length();
				if (srcCodeLength <= destCodeLength)
					return 1;
				else
					return 0;
			}

		});
		for (int i = 0; i < list.size(); i++) {//从叶向枝删除
			vo = (AuPartyRelationVo) list.get(i);
			//获取父级关系
			List parList = relationBs.queryParentRelation(vo.getParent_code());
			vo.setAll_parent_vo(parList);
//			log.debug(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Tone_class_Delete_") + vo.getName());
			relationBs.deletePartyRelation(vo.getId()); //根据机构ID从删除机构
		}
	}

	/**
	 * 级联增加关系
	 * 
	 * @param code
	 */
	private void addRelation(String code) {
		Map partyId2Oid = new HashMap();
		AuPartyRelationVo relationVo = new AuPartyRelationVo();
		relationVo.setCode(code);
		List relationVoList = relationBs.queryAuPartyRelation(relationVo); //获得当前节点数据
		int nodeCount = list.size() - 1;//需要处理节点的次数
		destVo = new AuPartyRelationVo();
		for (int i = nodeCount ; i >=  0; i--) {//从枝向叶挂接
			AuPartyRelationVo vo = (AuPartyRelationVo) list.get(i);
			destVo = (AuPartyRelationVo)relationVoList.get(0);
//			log.debug(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Transfer_level_increase_") + vo.getName());
			OID oid = relationBs.addPartyRelation(vo.getPartyid(),nodeCount==i?destVo.getId():partyId2Oid.get(vo.getParent_partyid()).toString(),vo.getRelationtype_id(),vo.getRelationtype_keyword());
			partyId2Oid.put(vo.getPartyid(),oid.toString());
		}
	}

	/**
	 * 记录操作日志
	 * 
	 * @param orgId
	 */
	private void addLog(String orgId) {
		Map map = new HashMap();
		map.put("ORGID",orgId);
		map.put("SYSDATE", DateTools.getSysTimestamp());
		for (int i = 0; i < list.size(); i++) {
			AuPartyRelationVo vo = (AuPartyRelationVo) list.get(i);
			map.put("HISTORYVO",vo);
			map.put("HISTORYDESTVO", destVo);
			adjustOrgLogBs.insert(map);
			map.remove("HISTORYVO");
			map.remove("HISTORYDESTVO");
		}
	}

	/**
	 * 调用删除和增加机构的业务方法，实现机构调级操作
	 * 
	 * @param orgId
	 *                    操作者机构ID
	 * @param oldNodeCode
	 *                    旧机构的节点CODE
	 * @param newNodeCode
	 *                    新机构的节点CODE
	 */
	public void updateRelation(String orgId, String oldNodeCode,String newNodeCode) {
		dropRelation(oldNodeCode);
		addRelation(newNodeCode);
		addLog(orgId);
	}
}

