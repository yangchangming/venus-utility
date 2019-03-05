package venus.oa.authority.auauthorize.bs.impl;

import gap.commons.digest.DigestLoader;
import gap.license.exception.InvalidLicenseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import venus.oa.authority.appenddata.bs.IAppendDataBs;
import venus.oa.authority.appenddata.util.IConstantsimplements;
import venus.oa.authority.auauthorize.bs.IAuAuthorizeBS;
import venus.oa.authority.auauthorize.dao.IAuAuthorizeDao;
import venus.oa.authority.auauthorize.util.IConstants;
import venus.oa.authority.auauthorize.vo.AuAuthorizeVo;
import venus.oa.authority.auauthorizelog.dao.IAuAuthorizeLogDao;
import venus.oa.authority.auauthorizelog.vo.AuAuthorizeLogVo;
import venus.oa.authority.auresource.bs.IAuResourceBs;
import venus.oa.authority.auresource.util.IAuResourceConstants;
import venus.oa.authority.auresource.vo.AuResourceVo;
import venus.oa.authority.auvisitor.bs.IAuVisitorBS;
import venus.oa.authority.auvisitor.vo.AuVisitorVo;
import venus.oa.login.vo.LoginSessionVo;
import venus.oa.organization.aupartyrelation.bs.IAuPartyRelationBs;
import venus.oa.organization.aupartyrelation.vo.AuPartyRelationVo;
import venus.oa.util.DateTools;
import venus.oa.util.GlobalConstants;
import venus.oa.util.ProjTools;
import venus.oa.util.StringHelperTools;
import venus.oa.util.common.bs.ICommonBs;
import venus.frames.base.bs.BaseBusinessService;
//import venus.frames.mainframe.log.ILog;
//import venus.frames.mainframe.log.LogMgr;
import venus.frames.mainframe.util.Helper;
import venus.pub.lang.OID;
import venus.pub.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * 权限BS
 * @author ganshuo
 *
 */
@Service
public class AuAuthorizeBS extends BaseBusinessService implements IAuAuthorizeBS, IConstants {

    @Autowired
    private IAuAuthorizeDao auAuthorizeDao;

    @Autowired
    private IAuAuthorizeLogDao auAuthorizeLogDao;

    @Autowired
    private IAuPartyRelationBs auPartyRelationBs;

    @Autowired
    private IAppendDataBs appendDataBs;

    @Autowired
    private IAuVisitorBS auVisitorBS;

    @Autowired
    private IAuResourceBs auResourceBs;


    /**
     *  添加
     * @param vo
     * @return
     */
    public String insert(AuAuthorizeVo vo) {
        return auAuthorizeDao.insert(vo);
    }    

    /**
     * 更新
     * @param vo
     * @return
     */
    public int update(AuAuthorizeVo vo) {
        return auAuthorizeDao.update(vo);
    }
    
    /**
     * 删除
     * @param id
     * @return
     */
    public int delete(String id) {        
        return auAuthorizeDao.delete(id);
    }
    
    /**
     * 根据访问者ID删除授权情况（包括附加数据）
     * 
     * @param id
     * @return
     */
    public int deleteByVisitorId(String visitorId) {        
        return auAuthorizeDao.deleteByVisitorId(visitorId);
    }

    /**
     * 根据资源ID删除授权情况（包括附加数据）
     * 
     * @param id
     * @return
     */
    public int deleteByResourceId(String resourceId) {        
        return auAuthorizeDao.deleteByResourceId(resourceId);
    }
    /**
     * 
     * 功能: 合并两个数字的因子，返回这些因子的乘积
     *
     * @param oldNum
     * @param newNum
     * @param isSingle
     * @return
     */
    private String mergeNum(String oldNum, String newNum, boolean isSingle) {
        int nOld = Integer.parseInt(oldNum);
        int nNew = Integer.parseInt(newNum);
        if(isSingle) {
            if((nOld%2==0 && nNew%2==0) || (nOld%2!=0 && nNew%2!=0)) {
                return nOld>nNew ? oldNum : newNum;
            }else {
                if(nNew%2==0) {
                    return nOld>(nNew/2) ? String.valueOf(2*nOld) : newNum;
                }else {
                    return (nOld/2)>nNew ? oldNum : String.valueOf(2*nNew);
                }
            }
        }else {
            HashMap mOld = getPrimeNumber(nOld);
            HashMap mNew = getPrimeNumber(nNew);
            mOld.putAll(mNew);
            Iterator it = mOld.keySet().iterator();
            int retNum = 1;
            while(it.hasNext()){
                int keyNum = Integer.parseInt((String)it.next());
                retNum = retNum * keyNum;
            }
            return String.valueOf(retNum);
        }
    }
    /**
     * 
     * 功能:将一个数分解为（不重复的）素数因子 
     *
     * @param num
     * @return
     */
    private HashMap getPrimeNumber(int num) {
        HashMap map = new HashMap();
        int i=2;
        while(i<=num) {
            if(num%i==0) {
                map.put(String.valueOf(i),null);
                num = num/i;
            }else {
                i++;
            }
        }
        return map;
    }
    /**
     * 
     * 功能: 在同一团体关系类型内，根据优先级过滤权限，并返回过滤结果
     *		优先级：拒绝>允许>未授权
     * @param auList
     * @return
     */
    public Map judgeAu4OneRelation(List auList) {
        if(auList== null)
            return null;
        
        Map tempMap = new HashMap();
        for(Iterator it=auList.iterator(); it.hasNext(); ) {
            //同一团体关系类型内拒绝优先, 优先级：拒绝>允许>未授权
            AuAuthorizeVo auVo = (AuAuthorizeVo) it.next();
            AuAuthorizeVo oldVo = (AuAuthorizeVo)tempMap.get(auVo.getResource_id());
            
            //20090417如果有附加权限,则将auVo和oldVo设置附加权限  开始
            String isAppend = auVo.getIs_append();
            if(null!=oldVo)
            	isAppend = "1".equals(isAppend)?"1":oldVo.getIs_append();
            auVo.setIs_append(isAppend);
            if(null!=oldVo)
            	oldVo.setIs_append(isAppend);
            //20090417如果有附加权限,则将auVo和oldVo设置附加权限  完毕
            
            if(oldVo==null) {//前一节点授权状态为未授权
                tempMap.put(auVo.getResource_id(),auVo);
            }else {
                boolean isModify = false;
                //更新授权种类
                String oldAccessType = oldVo.getAccess_type();
                String newAccessType = mergeNum(oldAccessType, auVo.getAccess_type(), true);
                if( ! oldAccessType.equals(newAccessType)) {
                    oldVo.setAccess_type(newAccessType);
                    isModify = true;
                }
	            //前一节点授权状态为可授权(实际上授权状态是未授权，只是授权种类为可授权)
	            if(GlobalConstants.getAuTypeAuthorize().equals(oldVo.getAuthorize_status())){
	                //当前节点授权状态是拒绝或允许时更新授权状态
	                if( ! GlobalConstants.getAuTypeAuthorize().equals(auVo.getAuthorize_status())) {
	                    oldVo.setAuthorize_status(auVo.getAuthorize_status());
	                    isModify = true;
	                }
	            }
	            //前一节点授权状态为允许
	            else if(GlobalConstants.getAuTypePermit().equals(oldVo.getAuthorize_status())){
	                //只有当前节点授权状态为拒绝时才能更新授权状态
	                if(GlobalConstants.getAuTypeForbid().equals(auVo.getAuthorize_status())) {
	                    oldVo.setAuthorize_status(auVo.getAuthorize_status());
	                    isModify = true;
	                }
	            }
	            if(isModify) {
	                tempMap.put(oldVo.getResource_id(),oldVo);
	            }
            }
        }
        return tempMap;
    }
    /**
     * 
     * 功能: 在不同团体关系类型内，根据优先级过滤权限，并返回过滤结果
     *		优先级：允许>未授权>拒绝
     * @param auList
     * @return
     */
    public Map judgeAu4DifRelation(List auList) {
        if(auList == null) 
            return null;
      
        Map tempMap = new HashMap();
        for(Iterator it=auList.iterator(); it.hasNext(); ) {
            //同一团体关系类型内拒绝优先, 优先级：允许>未授权>拒绝
            AuAuthorizeVo auVo = (AuAuthorizeVo) it.next();
            AuAuthorizeVo oldVo = (AuAuthorizeVo)tempMap.get(auVo.getResource_id());
            
            //20090417如果有附加权限,则将auVo和oldVo设置附加权限  开始
            String isAppend = auVo.getIs_append();
            if(null!=oldVo)
            	isAppend = "1".equals(isAppend)?"1":oldVo.getIs_append();
            auVo.setIs_append(isAppend);
            if(null!=oldVo)
            	oldVo.setIs_append(isAppend);
            //20090417如果有附加权限,则将auVo和oldVo设置附加权限  完毕
            
            if(oldVo==null) {//前一节点授权状态为未授权
                tempMap.put(auVo.getResource_id(),auVo);
            }else {
                boolean isModify = false;
                //更新授权种类
                String oldAccessType = oldVo.getAccess_type();
                String newAccessType = mergeNum(oldAccessType, auVo.getAccess_type(), true);
                if( ! oldAccessType.equals(newAccessType)) {
                    oldVo.setAccess_type(newAccessType);
                    isModify = true;
                }
                //前一节点授权状态为拒绝
                if(GlobalConstants.getAuTypeForbid().equals(oldVo.getAuthorize_status())){
                    //当前节点授权状态是允许或未授权时更新授权状态
                    if( ! GlobalConstants.getAuTypeForbid().equals(auVo.getAuthorize_status())) {
	                    oldVo.setAuthorize_status(auVo.getAuthorize_status());
	                    isModify = true;
	                }
                }
                //前一节点授权状态为可授权(实际上授权状态是未授权，只是授权种类为可授权)
                else if(GlobalConstants.getAuTypeAuthorize().equals(oldVo.getAuthorize_status())){
                    //只有当前节点授权状态为允许时才能更新授权状态
	                if(GlobalConstants.getAuTypePermit().equals(auVo.getAuthorize_status())) {
	                    oldVo.setAuthorize_status(auVo.getAuthorize_status());
	                    isModify = true;
	                }
                }
	            if(isModify) {
	                tempMap.put(oldVo.getResource_id(),oldVo);
	            }
            }
        }
        return tempMap;
    }
	
    /**
     * 
     * 功能: 根据访问者ID和资源类型查询该访问者自身拥有的权限
     * 		如果资源类型为null，则查询全部资源类型的权限
     *
     * @param visitorId 访问者ID
     * @param resType 资源类型
     * @return
     */
    public Map getAuByVisitorId(String visitorId, String resType) {
        List list = auAuthorizeDao.queryByVisitorId(visitorId, resType);
        Map map = new HashMap();
        if(list!=null) {
            Iterator it = list.iterator();
            while(it.hasNext()) {
                AuAuthorizeVo auVo = (AuAuthorizeVo) it.next();
                map.put(auVo.getResource_id(),auVo);
            }
        }
        return map;
    }
    
    /**
     * 
     * 功能: 根据访问者ID和资源类型查询该访问者自身拥有的历史权限
     * 		如果资源类型为null，则查询全部资源类型的权限
     *
     * @param visitorId 访问者ID
     * @param resType 资源类型
     * @return
     */        
    public Map queryHistoryAuByVisitorId(String visitorId, String resType){
        List list = auAuthorizeDao.queryHistoryAuByVisitorId(visitorId, resType);
        Map map = new HashMap();
        if(list!=null) {
            Iterator it = list.iterator();
            while(it.hasNext()) {
                AuAuthorizeVo auVo = (AuAuthorizeVo) it.next();
                map.put(auVo.getResource_id(),auVo);
            }
        }
        return map;	
    }    
    
    /**
     * 
     * 功能: 根据访问者Code数组和资源类型查询该访问者自身拥有的权限+在同一团体关系类型内它所继承的权限
     *		如果资源类型为null，则查询全部资源类型的权限
     *
     * @param visiCodes 访问者编号数组
     * @param resType 资源类型
     * @return
     */
    public Map getAuByVisitorCode(String visiCode, String resType) {
        if(null==visiCode||"".equals(visiCode))
            return Collections.EMPTY_MAP;
        String[] visiCodeArray = ProjTools.splitTreeCode(visiCode);
        List list = auAuthorizeDao.queryByVisitorCode(visiCodeArray, resType);
        return this.judgeAu4OneRelation(list);
    }
    
    /**
     * 
     * 功能: 根据访问者Code数组和资源类型查询该访问者在同一团体关系类型内它所继承的权限
     *		如果资源类型为null，则查询全部资源类型的权限
     *
     * @param visiCodes 访问者编号数组
     * @param resType 资源类型
     * @return
     */
    public Map getExtendAuByVisitorCode(String visiCode, String resType) {
        Map map = new HashMap();
        String[] allCodes = ProjTools.splitTreeCode(visiCode);
        if(allCodes!=null && allCodes.length>1) {
            //在数组中去除visiCode（数组中第1个值）
	        String[] visiCodeArray = new String[allCodes.length-1];
	        for(int i=1; i<allCodes.length; i++) {
	            visiCodeArray[i-1] = allCodes[i];
	        }
	        List list = auAuthorizeDao.queryByVisitorCode(visiCodeArray, resType);
	        map = this.judgeAu4OneRelation(list);
        }
        return map;
    }
    
    /**
     * 
     * 功能: 根据团体关系列表和资源类型查询该用户所拥有的权限，包括它继承的
     *
     * @param relList
     * @param sType
     * @return
     */
    public Map getAuByRelList(List relList, String sType){      
        List auList = new ArrayList();
        
        if(relList != null) {
	        for (int i=0; i<relList.size(); i++){
	            AuPartyRelationVo relVo = (AuPartyRelationVo) relList.get(i);
	            String[] visiCodeArray = ProjTools.splitTreeCode(relVo.getCode());
	            List list = auAuthorizeDao.queryByVisitorCode(visiCodeArray ,sType);//获取访问者所有权限
	            //同一团体关系类型之间的权限判断
	            Map tempMap = this.judgeAu4OneRelation(list);
	            if(tempMap != null) {
	                auList.addAll(tempMap.values());
	            }
	        }
        }
        //不同团体关系类型之间的权限判断
        Map auMap = this.judgeAu4DifRelation(auList);
        return auMap==null ? new HashMap() : auMap;
    }
    
    /**
     * 
     * 功能: 根据团体关系列表和资源类型查询该用户所拥有的数据权限，包括它继承的
     * 剔除历史数据权限
     * @param relList
     * @param sType
     * @return
     */
    public Map getOrgAuByRelListWithOutHistory(List relList,String resType){      
        List auList = new ArrayList();
        
        if(relList != null) {
            for (int i=0; i<relList.size(); i++){
                AuPartyRelationVo relVo = (AuPartyRelationVo) relList.get(i);
                String[] visiCodeArray = ProjTools.splitTreeCode(relVo.getCode());
                List list = auAuthorizeDao.queryByVisitorCodeWithOutHistory(visiCodeArray,resType);//获取访问者所有权限
                //同一团体关系类型之间的权限判断
                Map tempMap = this.judgeAu4OneRelation(list);
                if(tempMap != null) {
                    auList.addAll(tempMap.values());
                }
            }
        }
        //不同团体关系类型之间的权限判断
        Map auMap = this.judgeAu4DifRelation(auList);
        return auMap==null ? new HashMap() : auMap;
    }
    
    /**
     * 
     * 功能: 根据partyId、团体关系类型和资源类型查询该用户所拥有的权限，包括它继承的
     *
     * @param partyId
     * @param sType
     * @param relationTypeId
     * @return
     */
    public Map getAuByPartyId(String partyId, String sType, String relationTypeId) {
        AuPartyRelationVo queryVo = new AuPartyRelationVo();
        queryVo.setPartyid(partyId);
        queryVo.setRelationtype_id(relationTypeId);
        List relList = auPartyRelationBs.queryAuPartyRelation(queryVo);
        return this.getAuByRelList(relList, sType);
    }
    
    /**
     * 
     * 功能: 根据partyId和资源类型查询该用户所拥有的权限，包括它继承的
     *
     * @param partyId 用户partyId
     * @param sType 资源类型
     * @return
     */
    public Map getAuByPartyId(String partyId, String sType){
        return getAuByPartyId(partyId, sType, null);
    }
    
    /**
     * 
     * 功能:保存授权结果
     * 对当前授权情况和原先的授权情况进行比较，进行相应的新增、修改和删除操作
     *
     * @param vId 访问者ID
     * @param voList 当前授权情况vo列表
     * @param auType 资源类型
     * @return
     */
    public boolean saveAu(String vId, List voList, String auType) {
//    	DigestLoader loader = DigestLoader.getLoader();
//    	if (loader.isValid() && Math.random() > 0.8) {
//    		log.info( "access method chkau" );
//    		chkau(loader);
//    	} else if (!loader.isValid()) {
//    		chkau(loader);
//    	}

        if(voList==null)
            return false;
        //获取旧的权限列表
        Map selMap = new HashMap();
        String fType = GlobalConstants.getResType_menu();//功能菜单
    	String bType = GlobalConstants.getResType_butn();//功能按钮
        if(fType.equals(auType)) {
            selMap = this.getAuByVisitorId(vId, fType);
            selMap.putAll(this.getAuByVisitorId(vId, bType));
        }else {
            selMap = this.getAuByVisitorId(vId, auType);
        }
        Set keySet = selMap.keySet();
        
        List addList = new ArrayList();
        List modList = new ArrayList();
        List delList = new ArrayList();
		for(int i=0; i<voList.size(); i++) {
		    AuAuthorizeVo vo = (AuAuthorizeVo)voList.get(i);
			if (! keySet.contains(vo.getResource_id())) {
			    addList.add(vo);//找到需要新增的
			}else {
			    AuAuthorizeVo auVo = (AuAuthorizeVo)selMap.get(vo.getResource_id());
			    if( ! auVo.getAuthorize_status().equals(vo.getAuthorize_status()) 
			        || ! auVo.getAccess_type().equals(vo.getAccess_type())) {
			        if(! auVo.getAuthorize_status().equals(vo.getAuthorize_status())) {
			            auVo.setAuthorize_status(vo.getAuthorize_status());
			        }
			        if(! auVo.getAccess_type().equals(vo.getAccess_type())) {
			            auVo.setAccess_type(vo.getAccess_type());
			        }
			        modList.add(auVo);//找到需要修改的
			    }
			}
		}
		//找到需要删除的
		Iterator it=keySet.iterator();
		while(it.hasNext()) {
    	    String key = (String)it.next();
    	    AuAuthorizeVo auVo = (AuAuthorizeVo)selMap.get(key);
    	    boolean flag = true;
    	    for(int i=0; i<voList.size(); i++) {
    	        AuAuthorizeVo vo = (AuAuthorizeVo)voList.get(i);
    	        if (vo.getResource_id().equals(auVo.getResource_id())) {
					flag = false;
					break;
				}
    	    }
    	    if (flag) {
				delList.add(auVo.getId());
			}
    	}
		//准备同步扩展表
		AuAuthorizeVo authorizeVo = null;
		//执行新增
		for(int i=0; i<addList.size(); i++) {
			authorizeVo = (AuAuthorizeVo)addList.get(i);
		    auAuthorizeDao.insert(authorizeVo);
		    if("0".equals(authorizeVo.getAuthorize_status()))
		    	appendDataBs.deleteByAuthorizeId(authorizeVo.getId());
		}
		//执行修改
		for(int i=0; i<modList.size(); i++) {
			authorizeVo = (AuAuthorizeVo)modList.get(i);
		    auAuthorizeDao.update(authorizeVo);
		    if("0".equals(authorizeVo.getAuthorize_status()))
                appendDataBs.deleteByAuthorizeId(authorizeVo.getId());
		}
		//执行删除
		for(int i=0; i<delList.size(); i++) {
		    auAuthorizeDao.delete((String)delList.get(i));
            appendDataBs.deleteByAuthorizeId((String)delList.get(i));
		}
        return true;
    }  
    
    /**
     * 
     * 功能:保存授权结果
     * 对当前授权情况和原先的授权情况进行比较，进行相应的新增、修改和删除操作
     * @param vId 访问者ID
     * @param voList 当前授权情况vo列表
     * @param auType 资源类型
     * @param sessionVo 登录用户session
     * @return
     */
    public boolean saveAu(String vId, List voList, String auType,LoginSessionVo sessionVo) {

//    	DigestLoader loader = DigestLoader.getLoader();
//    	if (loader.isValid() && Math.random() > 0.8) {
//    		log.info( "access method chkau" );
//    		chkau(loader);
//    	} else if (!loader.isValid()) {
//    		chkau(loader);
//    	}

        if(voList==null)
            return false;
        //获取旧的权限列表
        Map selMap = new HashMap();
        String fType = GlobalConstants.getResType_menu();//功能菜单
    	String bType = GlobalConstants.getResType_butn();//功能按钮
        if(fType.equals(auType)) {
            selMap = this.getAuByVisitorId(vId, fType);
            selMap.putAll(this.getAuByVisitorId(vId, bType));
        }else {
            selMap = this.getAuByVisitorId(vId, auType);
        }
        Set keySet = selMap.keySet();
        
        List addList = new ArrayList();
        List modList = new ArrayList();
        List delList = new ArrayList();
		for(int i=0; i<voList.size(); i++) {
		    AuAuthorizeVo vo = (AuAuthorizeVo)voList.get(i);
			if (! keySet.contains(vo.getResource_id())) {
			    addList.add(vo);//找到需要新增的
			}else {
			    AuAuthorizeVo auVo = (AuAuthorizeVo)selMap.get(vo.getResource_id());
			    if( ! auVo.getAuthorize_status().equals(vo.getAuthorize_status()) 
			        || ! auVo.getAccess_type().equals(vo.getAccess_type())) {
			        if(! auVo.getAuthorize_status().equals(vo.getAuthorize_status())) {
			            auVo.setAuthorize_status(vo.getAuthorize_status());
			        }
			        if(! auVo.getAccess_type().equals(vo.getAccess_type())) {
			            auVo.setAccess_type(vo.getAccess_type());
			        }
			        modList.add(auVo);//找到需要修改的
			    }
			}
		}
		//找到需要删除的
		Iterator it=keySet.iterator();
		while(it.hasNext()) {
    	    String key = (String)it.next();
    	    AuAuthorizeVo auVo = (AuAuthorizeVo)selMap.get(key);
    	    boolean flag = true;
    	    for(int i=0; i<voList.size(); i++) {
    	        AuAuthorizeVo vo = (AuAuthorizeVo)voList.get(i);
    	        if (vo.getResource_id().equals(auVo.getResource_id())) {
					flag = false;
					break;
				}
    	    }
    	    if (flag) {
				delList.add(auVo.getId());
			}
    	}
		//准备同步扩展表
		AuAuthorizeVo authorizeVo = null;
		//执行新增
		for(int i=0; i<addList.size(); i++) {
			authorizeVo = (AuAuthorizeVo)addList.get(i);
		    auAuthorizeDao.insert(authorizeVo);
		    if("0".equals(authorizeVo.getAuthorize_status()))
		    	appendDataBs.deleteByAuthorizeId(authorizeVo.getId());
		}
		//执行修改
		for(int i=0; i<modList.size(); i++) {
			authorizeVo = (AuAuthorizeVo)modList.get(i);
		    auAuthorizeDao.update(authorizeVo);
		    if("0".equals(authorizeVo.getAuthorize_status()))
                appendDataBs.deleteByAuthorizeId(authorizeVo.getId());
		}
		//执行删除
		for(int i=0; i<delList.size(); i++) {
		    auAuthorizeDao.delete((String)delList.get(i));
            appendDataBs.deleteByAuthorizeId((String)delList.get(i));
		}
	saveAuLog(vId,sessionVo);
        return true;
    }      
    
    /**
     * 记录授权日志
     * @param visitorId
     * @param sessionVo
     */
    public void saveAuLog(String visitorId,LoginSessionVo sessionVo) {
	List list = auAuthorizeDao.queryByVisitorId(visitorId);
	AuVisitorVo visitorVo = auVisitorBS.find(visitorId);
	String tag = Helper.requestOID(TAGID).toString();
	if (list.size() == 0) { //记录没有授权结果
	    AuAuthorizeLogVo vo = new AuAuthorizeLogVo();
	    vo.setOperate_id(sessionVo.getParty_id());
	    vo.setOperate_name(sessionVo.getName());
	    vo.setOperate_date(DateTools.getSysTimestamp());
	    vo.setVisitor_id(tag);
	    vo.setVisitor_name(visitorVo.getName());
	    vo.setVisitor_code(visitorVo.getCode());
	    vo.setVisitor_type("0");
	    vo.setResource_id(tag);
	    vo.setResource_name("");
	    vo.setResource_code("");
	    vo.setResource_type("0");
	    vo.setAuthorize_status("1");
	    vo.setAuthorize_tag(tag);	    
	    vo.setAccredit_type("0");
	    vo.setCreate_date(DateTools.getSysTimestamp());
	    auAuthorizeLogDao.insert(vo);
	}
	for (int i = 0; i < list.size(); i++) {
	    AuAuthorizeLogVo vo = (AuAuthorizeLogVo)list.get(i);
	    vo.setOperate_id(sessionVo.getParty_id());
	    vo.setOperate_name(sessionVo.getName());
	    vo.setOperate_date(DateTools.getSysTimestamp());
	    vo.setAuthorize_tag(tag);
	    vo.setAccredit_type(vo.getVisitor_code().indexOf(GlobalConstants.getRelaType_proxy()) != -1 ? "1" : "0");
	    vo.setCreate_date(DateTools.getSysTimestamp());
	    auAuthorizeLogDao.insert(vo);
	}
    }
    
    /**
     * 
     * 功能:分析并保存针对组织机构的授权结果
     * 对当前授权情况和原先的授权情况进行比较，进行相应的新增和删除操作
     *
     * @param vId 访问者ID
     * @param vCode 访问者编号
     * @param vType 访问者类型
     * @param addCodeArray 打勾的节点的编号数组
     * @param delCodeArray 取消打勾的节点的编号数组
     * @param sNames 资源名称数组
     * @param sTypes 资源类型数组
     * @return
     */
    public boolean saveOrgAu(String vId, String vCode, String vType, 
            String[] addCodeArray, String[] delCodeArray, String[] sNames, String[] sTypes) {
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
        
		//取得修改前的权限列表
		Map selIdMap = this.getAuByVisitorId(vId, rType);
	    HashMap selCodeMap = new HashMap();
	    for(Iterator it=selIdMap.keySet().iterator(); it.hasNext(); ) {
	    	AuAuthorizeVo auVo = (AuAuthorizeVo)selIdMap.get((String)it.next());
	    	selCodeMap.put(auVo.getResource_code(),auVo.getId());
	    }
	    Set oldKeySet = selCodeMap.keySet();

		//比较改动的部分，分出哪些要新增哪些要删除
		List addList = new ArrayList();
		List delList = new ArrayList();
		//查找需新增的vo列表
		for (int i = 0; i < addCodeArray.length; i++) {
			if (! oldKeySet.contains(addCodeArray[i])) {
			    AuAuthorizeVo vo = new AuAuthorizeVo();
				vo.setVisitor_id(vId);
				vo.setVisitor_code(vCode);
				vo.setVisitor_type(vType);
				vo.setAccess_type("1");
				vo.setAuthorize_status("1");
				vo.setIs_append("0");
				vo.setResource_id(resIds[i]);
				vo.setResource_code(addCodeArray[i]);
				vo.setResource_type(rType);
				vo.setResourcename(sNames[i]);
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
		//执行新增
		for(int i=0; i<addList.size(); i++) {
		    auAuthorizeDao.insert((AuAuthorizeVo)addList.get(i));
		}
		//执行删除
		for(int i=0; i<delList.size(); i++) {
		    auAuthorizeDao.delete((String)delList.get(i));
		}
        return true;
    }      
    
    
    /**
     * 
     * 功能:分析并保存针对组织机构的授权结果
     * 对当前授权情况和原先的授权情况进行比较，进行相应的新增和删除操作
     *
     * @param vId 访问者ID
     * @param vCode 访问者编号
     * @param vType 访问者类型
     * @param addCodeArray 打勾的节点的编号数组
     * @param delCodeArray 取消打勾的节点的编号数组
     * @param sNames 资源名称数组
     * @param sTypes 资源类型数组
     * @param sessionVo 登录用户session
     * @return
     */
    public boolean saveOrgAu(String vId, String vCode, String vType, 
            String[] addCodeArray, String[] delCodeArray, String[] sNames, String[] sTypes,LoginSessionVo sessionVo) {
        //角色授权和代理授权时，支持子节点的精确授权 开始
        String[][] returnArray = childNode(addCodeArray,sNames,sTypes);
        addCodeArray = returnArray[0];
        sNames = returnArray[1];
        sTypes = returnArray[2];
        delCodeArray = childNode(delCodeArray,null,null)[0];
        //角色授权和代理授权时，支持子节点的精确授权 结束
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
        
		//取得修改前的权限列表
		Map selIdMap = this.getAuByVisitorId(vId, rType);
	    HashMap selCodeMap = new HashMap();
	    for(Iterator it=selIdMap.keySet().iterator(); it.hasNext(); ) {
	    	AuAuthorizeVo auVo = (AuAuthorizeVo)selIdMap.get((String)it.next());
	    	selCodeMap.put(auVo.getResource_code(),auVo.getId());
	    }
	    Set oldKeySet = selCodeMap.keySet();

		//比较改动的部分，分出哪些要新增哪些要删除
		List addList = new ArrayList();
		List delList = new ArrayList();
		//查找需新增的vo列表
		for (int i = 0; i < addCodeArray.length; i++) {
			if (! oldKeySet.contains(addCodeArray[i])) {
			    AuAuthorizeVo vo = new AuAuthorizeVo();
				vo.setVisitor_id(vId);
				vo.setVisitor_code(vCode);
				vo.setVisitor_type(vType);
				vo.setAccess_type("1");
				vo.setAuthorize_status("1");
				vo.setIs_append("0");
				vo.setResource_id(resIds[i]);
				vo.setResource_code(addCodeArray[i]);
				vo.setResource_type(rType);
				vo.setResourcename(sNames[i]);
				vo.setCreate_date(DateTools.getSysTimestamp());
				vo.setSystem_id("");
				addList.add(vo);
			}
		}
		
		//查找需删除的编号列表
		for (int i = 0; i < delCodeArray.length; i++) {
			if (oldKeySet.contains(delCodeArray[i])) {
			    delList.add((String)selCodeMap.get(delCodeArray[i]));	
			}
		}
		//执行新增
		for(int i=0; i<addList.size(); i++) {
		    auAuthorizeDao.insert((AuAuthorizeVo)addList.get(i));
		}
		//执行删除
		for(int i=0; i<delList.size(); i++) {
		    auAuthorizeDao.delete((String)delList.get(i));
		}
	saveAuLog(vId,sessionVo);	
        return true;
    }          
    
    private String[][] childNode(String[] codeArray,String[] sNames, String[] sTypes){
        final List codes = new ArrayList();
        final List names = new ArrayList();
        final List types = new ArrayList();
        ICommonBs bs = null;
        for(int i=0;i<codeArray.length;i++){
            String aCode = codeArray[i];
            if(aCode.startsWith(GlobalConstants.getRelaType_comp())){//只要有一个是行政关系，就说明全部是行政关系，那么直接返回并按原来方式处理
                return new String[][]{codeArray,sNames,sTypes};
            }
            //否则，是角色关系或代理关系
            bs = ProjTools.getCommonBsInstance();
            bs.doQuery("select name,code,partytype_id from au_partyrelation where partytype_id='"+fetchPartyTypeByCode(aCode)+"' and code like '"+aCode+"%'", new RowMapper(){
                public Object mapRow(ResultSet rs, int i)
                        throws SQLException {
                    if(!codes.contains(rs.getString("code"))){
                        codes.add(rs.getString("code"));
                        names.add(rs.getString("name"));
                        types.add(rs.getString("partytype_id"));
                    }
                    return "";
                } 
            });
        }
        return new String[][]{(String[]) codes.toArray(new String[0]),(String[]) names.toArray(new String[0]),(String[]) types.toArray(new String[0])};
    }
    private String fetchPartyTypeByCode(String code){
        if(code.startsWith(GlobalConstants.getRelaType_role()))
            return GlobalConstants.getPartyType_role();
        else if(code.startsWith(GlobalConstants.getRelaType_proxy()))
            return GlobalConstants.getPartyType_proxy();
        else{
            AuPartyRelationVo queryVo = new AuPartyRelationVo();
            queryVo.setCode(code);
            List relList = auPartyRelationBs.queryAuPartyRelation(queryVo);
            if(relList.size()>0)
                return ((AuPartyRelationVo)relList.get(0)).getPartytype_id();
            else
                return null;
        }
    }
    
    /**
     * 将用户转换为访问者
     * @param partyId
     * @return list 中为AuVisitorVo
     */
    public List parsePartyIdToVisitor(String partyId) {
        return auAuthorizeDao.parsePartyIdToVisitor(partyId, null);
    }
    
    /**
     * 功能: 根据资源Code查询对该资源拥有允许访问权限的人员partyId列表
     * @param resCode 资源编号
     * @return
     */
    public String[] getPartyIdByResourceCode(String resCode) {
    	List auList = auAuthorizeDao.queryByResourceCode(new String[]{resCode}, null);
    	if(auList == null) {
    		return null;
    	}
    	//下面拆分
    	List permitList = new ArrayList();
    	List refuseList = new ArrayList();
    	for(int i=0; i<auList.size(); i++) {
    		AuAuthorizeVo vo = (AuAuthorizeVo)auList.get(i);
    		if(GlobalConstants.getAuTypePermit().equals(vo.getAuthorize_status())) {
    			permitList.add(vo.getVisitor_code());//得到所有授权状态为”允许“的访问者
    		}else if(GlobalConstants.getAuTypeForbid().equals(vo.getAuthorize_status())) {
    			refuseList.add(vo.getVisitor_code());//得到所有授权状态为”拒绝“的访问者
    		}
    	}
    	//获取“允许”和“拒绝”的用户
    	List pList = auAuthorizeDao.parseVisitorToRelCode((String[])permitList.toArray(new String[0]), GlobalConstants.getPartyType_empl());
    	List rList = auAuthorizeDao.parseVisitorToRelCode((String[])refuseList.toArray(new String[0]), GlobalConstants.getPartyType_empl());
    	//获取最终授权状态为”允许“的用户
    	List fPermitList = new ArrayList();
    	if(pList == null) {
    		return null;
    	}
    	if(rList == null) {
    		fPermitList =  pList;
    	}else {
	    	for(int i=0; i<pList.size(); i++) {
	    		String permitCode = (String)pList.get(i);
	    		boolean pass = true;
	    		for(int j=0; j<rList.size(); j++) {
	    			String refuseCode = (String)rList.get(j);
	    			if(permitCode.equals(refuseCode)) {
	    				pass = false;//踢除该"允许"的code
	    				break;
	    			}
	    		}
	    		if(pass) {
	    			fPermitList.add(permitCode);
	    		}
	    	}
    	}
    	
    	//将fPermitList里的code转化为partyid
    	List partyidList = auAuthorizeDao.parseVisitorToPartyId((String[]) fPermitList.toArray(new String[0]));
    	return partyidList==null ? null : (String[]) partyidList.toArray(new String[0]);
    }
    
    /**
     * 功能: 根据访问者编号数组和团体类型获取相关的（自身及下属）partyid列表；
     * partyTypeId为可选参数，当为null或""时不起作用，将查询全部类型
     * @param visitorCode 访问者编号数组
     * @param partyTypeId 团体类型
     * @return
     */
    public List parseVisitorToPartyId(String visitorCode[], String partyTypeId) {
    	return auAuthorizeDao.parseVisitorToPartyId(visitorCode, partyTypeId);
    }

    private void chkau(DigestLoader loader) {

    	boolean valid = true;
    	try {
    		Class cls = loader.findClass();
    		Method m = ReflectionUtils.findMethod(cls, "checkLicense",
    				new Class[] {});
    		valid = new Boolean(ReflectionUtils.invokeMethod(m, null,
    				new Object[] {}).toString()).booleanValue();
    	} catch (RuntimeException e) {
    		loader.setValid(false);
    		throw e;
    	}
    	if (!valid) {
    		loader.setValid(false);
    		throw new InvalidLicenseException();
    	} else {
    		loader.setValid(true);
//    		log.info( "venus.platform: check au successfully!" );
    	}
    }

	/* （非 Javadoc）
	 * @see venus.authority.au.auauthorize.bs.IAuAuthorizeBS#find(java.lang.String)
	 */
	public AuAuthorizeVo find(String id) {
		return auAuthorizeDao.find(id);
	}
	
	    /**
	     * 功能: 根据访问者编号数组获取相应的partyrelation表自身的code
	     * @param visitorCode 访问者编号数组
	     * @return
	     */
	 public List parseVisitorToRelCode(String visitorCode[]) {
	     	return auAuthorizeDao.parseVisitorToRelCode(visitorCode);
	 }

        /* (non-Javadoc)
         * @see venus.authority.au.auauthorize.bs.IAuAuthorizeBS#getOrgAuByPartyIdWithOutHistory(java.lang.String)
         */
        public Map getOrgAuByPartyIdWithOutHistory(String partyId,String resType) {
            AuPartyRelationVo queryVo = new AuPartyRelationVo();
            queryVo.setPartyid(partyId);
            List relList = auPartyRelationBs.queryAuPartyRelation(queryVo);
            return this.getOrgAuByRelListWithOutHistory(relList,resType);
        }

        /* (non-Javadoc)
         * @see venus.authority.au.auauthorize.bs.IAuAuthorizeBS#getOrgAuByVisitorCodeWithOutHistory(java.lang.String)
         */
        public Map getOrgAuByVisitorCodeWithOutHistory(String visiCode,String resType) {
            String[] visiCodeArray = ProjTools.splitTreeCode(visiCode);
            List list = auAuthorizeDao.queryByVisitorCodeWithOutHistory(visiCodeArray,resType);
            return this.judgeAu4OneRelation(list);
        }
}

