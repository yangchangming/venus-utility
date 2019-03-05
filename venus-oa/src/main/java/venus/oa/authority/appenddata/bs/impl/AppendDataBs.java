package venus.oa.authority.appenddata.bs.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import venus.oa.authority.appenddata.bs.IAppendDataBs;
import venus.oa.authority.appenddata.dao.IAuAppendDataDao;
import venus.oa.authority.appenddata.vo.AuAppendVo;
import venus.oa.authority.auauthorize.bs.IAuAuthorizeBS;
import venus.oa.authority.auauthorize.vo.AuAuthorizeVo;
import venus.oa.authority.auresource.bs.IAuResourceBs;
import venus.oa.authority.auresource.vo.AuResourceVo;
import venus.oa.organization.aupartyrelation.bs.IAuPartyRelationBs;
import venus.oa.organization.aupartyrelation.vo.AuPartyRelationVo;
import venus.oa.util.DateTools;
import venus.oa.util.GlobalConstants;
import venus.oa.util.ProjTools;
import venus.oa.util.StringHelperTools;
import venus.pub.lang.OID;

import java.util.*;

/**
 *  2008-9-26
 * @author changming.Y <changming.yang.ah@gmail.com>
 */
@Service
public class AppendDataBs implements IAppendDataBs {

	@Autowired
	private IAuAppendDataDao auAppendDataDao;

	@Autowired
	private IAuResourceBs auResourceBs;

	@Autowired
	private IAuAuthorizeBS auAuthorizeBS;

//	@Autowired
//	private IAppendDataBs appendDataBs;

	@Autowired
	private IAuPartyRelationBs auPartyRelationBs;


	/* （非 Javadoc）
	 * @see venus.authority.au.appenddata.bs.IAppendDataBs#getAppendByAuthorizeId(java.lang.String)
	 */
	public Map getAppendByAuthorizeId(String authorizeId) {
		List list = auAppendDataDao.queryAppendByAuthorizeId(authorizeId);
		Map map = new HashMap();
		Iterator it = list.iterator();
		while(it.hasNext()) {
        	AuAppendVo auVo = (AuAppendVo) it.next();
        	map.put(auVo.getId(),auVo);
        }
		return map;
	}

	/* （非 Javadoc）
	 * @see venus.authority.au.appenddata.bs.IAppendDataBs#getExtendAppendAuByVisitorCode(java.lang.String, java.lang.String, java.lang.String)
	 */
	public Map getExtendAppendAuByVisitorCode(String visiCode[], String resourceId,String appendValue) {
		Map map = new HashMap();
		for(int i=0;i<visiCode.length;i++){
	        String[] allCodes = ProjTools.splitTreeCode(visiCode[i]);
	        if(allCodes!=null && allCodes.length>1) {
	            List list = auAppendDataDao.queryAppendByVisitorCode(allCodes, resourceId, appendValue);
	            map.putAll(judgeAuAppend(list));
	        }
		}
        Map returnMap = new HashMap();
		returnMap.put(resourceId,map.values().toArray(new AuAppendVo[0]));
        return returnMap;
	}
	 
    /**
     * 
     * 功能:分析并保存针对功能权限基础上的组织机构的授权结果
     * 对当前授权情况和原先的授权情况进行比较，进行相应的新增和删除操作
     *
     * @param vId 访问者ID
     * @param vCode 访问者编号
     * @param vType 访问者类型
     * @param addCodeArray 打勾的节点的编号数组
     * @param delCodeArray 取消打勾的节点的编号数组
     * @param sNames 资源名称数组
     * @param sTypes 资源类型数组
     * @param authorizeId 功能权限ID
     * @return
     */
    public boolean saveFunOrgAu(String vId, String vCode, String vType, 
            String[] addCodeArray, String[] delCodeArray, String[] sNames, String[] sTypes, String authorizeId) {
        String rType = GlobalConstants.getResType_orga();//组织
        //设置资源信息
        String resIds[] = new String[addCodeArray.length];
        if(addCodeArray.length>0 && addCodeArray[0].length()>0) {
	        //根据addCodeArray查询AuResource表中的相应信息
	    	String queryCondition = "RESOURCE_TYPE='"+rType+"' and ENABLE_STATUS='1' and VALUE in("+ StringHelperTools.parseToSQLStringComma(addCodeArray)+")";
	    	List lResource = auResourceBs.queryByCondition(queryCondition);
	    	Map resMap = new HashMap();
	    	if(lResource != null) {
				for (int i = 0; i < lResource.size(); i++) {
					AuResourceVo vo = (AuResourceVo) lResource.get(i);
					resMap.put(vo.getValue(),vo.getId());
				}
			}
	        for(int i=0; i<addCodeArray.length; i++) {
	        	//如果在AuResource表中查不到，则插入到AuResource表中
	        	if( ! resMap.keySet().contains(addCodeArray[i])) {
	    			AuResourceVo resVo = new AuResourceVo();
	    			resVo.setAccess_type("1");
	    			resVo.setEnable_status("1");
	    			resVo.setFilter_type("like%");
	    			resVo.setIs_public("0");
	    			resVo.setName(sNames[i]);
	    			resVo.setParty_type(sTypes[i]);
	    			resVo.setResource_type(rType);
	    			resVo.setValue(addCodeArray[i]);
	    			resVo.setCreate_date(DateTools.getSysTimestamp());  //打创建时间
	    			OID oid = auResourceBs.insert(resVo); //插入单条记录
	    			resMap.put(resVo.getValue(), oid.toString());
	    		}
	        	resIds[i] = (String)resMap.get(addCodeArray[i]);
	        }
        }
        //将AuAuthorize表的is_append属性设置为1：有附加数据
        AuAuthorizeVo authorizeVo=auAuthorizeBS.find(authorizeId);
        if("0".equals(authorizeVo.getIs_append())){
        	authorizeVo.setIs_append("1");
			auAuthorizeBS.update(authorizeVo);
        }
		//取得修改前的权限列表
		Map selIdMap = getAppendByAuthorizeId(authorizeId);
	    HashMap selCodeMap = new HashMap();
	    for(Iterator it=selIdMap.keySet().iterator(); it.hasNext(); ) {
	    	AuAppendVo auVo = (AuAppendVo)selIdMap.get((String)it.next());
	    	selCodeMap.put(auVo.getAppend_value(),auVo.getId());
	    }
	    Set oldKeySet = selCodeMap.keySet();

		//比较改动的部分，分出哪些要新增哪些要删除
		List addList = new ArrayList();
		List delList = new ArrayList();
		//查找需新增的vo列表
		for (int i = 0; i < addCodeArray.length; i++) {
			if (! oldKeySet.contains(addCodeArray[i])) {
				AuAppendVo vo = new AuAppendVo();
				vo.setVisitor_id(vId);
				vo.setVisitor_code(vCode);
				vo.setAppend_value(addCodeArray[i]);//"1"允许访问
				vo.setAuthorize_id(authorizeId);
				vo.setResource_id(authorizeVo.getResource_id());
				vo.setCreate_date(DateTools.getSysTimestamp());
				addList.add(vo);
			}
		}
		
		//查找需删除的编号列表
		for (int i = 0; i < delCodeArray.length; i++) {
			if (oldKeySet.contains(delCodeArray[i])) {
			    delList.add((String)selCodeMap.get(delCodeArray[i]));	
			}
		}
		for(int i=0; i<addList.size(); i++) {
			auAppendDataDao.insert((AuAppendVo)addList.get(i));
		}
		for(int i=0; i<delList.size(); i++) {
			auAppendDataDao.delete((String)delList.get(i));
		}
        return true;
	}

    /**
     * 
     * 功能: 在同一团体关系类型内，根据优先级过滤附加数据的权限，并返回过滤结果
     *		优先级：拒绝>允许>未授权
     * @param auList
     * @return
     */
    private Map judgeAuAppend(List auList) {
        if(auList== null)
            return null;
        //TODO 待优化
        Map tempMap = new HashMap();
        for(Iterator it=auList.iterator(); it.hasNext(); ) {
        	AuAppendVo auVo = (AuAppendVo) it.next();
        	tempMap.put(auVo.getId(),auVo);
        }
        return tempMap;
    }
	/* （非 Javadoc）
	 * @see venus.authority.au.appenddata.bs.IAppendDataBs#getExtendAppendAuByPartyId(java.lang.String, java.lang.String)
	 */
	public Map getExtendAppendAuByPartyId(String partyId, String resourceId,String appendValue) {
        AuPartyRelationVo queryVo = new AuPartyRelationVo();
        queryVo.setPartyid(partyId);
        List relList = auPartyRelationBs.queryAuPartyRelation(queryVo);
		Map map = new HashMap();
		for(int i=0;i<relList.size();i++){
	        String[] allCodes = ProjTools.splitTreeCode(((AuPartyRelationVo)relList.get(i)).getCode());
	        if(allCodes!=null && allCodes.length>1) {
	            List list = auAppendDataDao.queryAppendByVisitorCode(allCodes, resourceId,appendValue);
		        map.putAll(judgeAuAppend(list));
	        }
		}
		Map returnMap = new HashMap();
		returnMap.put(resourceId,map.values().toArray(new AuAppendVo[0]));
        return returnMap;
        
	}
	/* （非 Javadoc）
	 * @see venus.authority.au.appenddata.bs.IAppendDataBs#getExtendAppendAuByPartyId(java.lang.String, java.lang.String)
	 */
	public Map getExtendAppendAuByPartyId(String party_id, String resource_id) {
		return getExtendAppendAuByPartyId(party_id,resource_id,null);
	}
	/* （非 Javadoc）
	 * @see venus.authority.au.appenddata.bs.IAppendDataBs#deleteByAuthorizeId(java.lang.String)
	 */
	public void deleteByAuthorizeId(String id) {
		auAppendDataDao.deleteByAuthorizeId(id);
	}
    
}

