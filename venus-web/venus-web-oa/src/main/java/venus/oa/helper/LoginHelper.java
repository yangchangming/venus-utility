package venus.oa.helper;

import org.apache.commons.lang.StringUtils;
import venus.oa.authority.appenddata.vo.AuAppendVo;
import venus.oa.authority.auauthorize.bs.IAuAuthorizeBS;
import venus.oa.authority.aufunctree.bs.IAuFunctreeBs;
import venus.oa.authority.aufunctree.vo.AuFunctreeVo;
import venus.oa.authority.auuser.bs.IAuUserBs;
import venus.oa.authority.auuser.vo.AuUserVo;
import venus.oa.login.vo.LoginSessionVo;
import venus.oa.organization.aupartyrelation.vo.AuPartyRelationVo;
import venus.oa.util.Encode;
import venus.oa.util.GlobalConstants;
import venus.oa.util.VoHelperTools;
import venus.springsupport.BeanFactoryHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

public class LoginHelper {

    /**
     * 功能: 获取LoginSessionVo
     * 
     * @param req HttpServletRequest
     * @return 如果已登陆且session有效则返回LoginSessionVo，否则返回null 
     */
    public static LoginSessionVo getLoginVo(HttpServletRequest req) {
        HttpSession session = req.getSession(false);//检测当前有无session存在,如果不存在则创建一个,如果存在就返回当前的
        LoginSessionVo vo = null;
        if(session != null && session.getAttribute("LOGIN_SESSION_VO") != null) {
            vo = (LoginSessionVo) session.getAttribute("LOGIN_SESSION_VO");            
        }
        return vo;
    }

    /**
     * 功能: 获取LoginSessionVo
     *
     * @param session HttpSession
     * @return 如果已登陆且session有效则返回LoginSessionVo，否则返回null 
     */
    public static LoginSessionVo getLoginVo(HttpSession session) {
        LoginSessionVo vo = null;
        if(session != null && session.getAttribute("LOGIN_SESSION_VO") != null) {
            vo = (LoginSessionVo) session.getAttribute("LOGIN_SESSION_VO");            
        }
        return vo;
    }
    /**
     * 
     * 功能: 获取登陆人姓名
     *
     * @param req
     * @return 如果已登陆且session有效则返回登陆人姓名，否则返回null
     */
    public static String getLoginName(HttpServletRequest req) {
        LoginSessionVo vo = getLoginVo(req);
        if(vo==null)
            return null;
        return vo.getName();
    } 
    /**
     * 
     * 功能: 获取登陆人用户名(登录账号)
     *
     * @param req
     * @return 如果已登陆且session有效则返回登陆人用户名(登录账号)，否则返回null
     */
    public static String getLoginId(HttpServletRequest req) {
        LoginSessionVo vo = getLoginVo(req);
        if(vo==null)
            return null;
        return vo.getLogin_id();
    } 
    /**
     * 
     * 功能: 获取登陆人partyid
     *
     * @param req
     * @return 如果已登陆且session有效则返回登陆人partyid，否则返回null
     */
    public static String getPartyId(HttpServletRequest req) {
        LoginSessionVo vo = getLoginVo(req);
        if(vo==null)
            return null;
		return vo.getParty_id();
    } 
    /**
     * 功能: 判断登陆人是否超级管理员
     * @param req
     * @return 如果已登陆，session有效并且是超级管理员则返回true，否则返回false
     */
    public static boolean getIsAdmin(HttpServletRequest req) {
    	LoginSessionVo vo = getLoginVo(req);
    	if(vo==null)
    		return false;
    	return vo.getIs_admin().equals("1")?true:false;
    }
    
    /**
     * 
     * 功能: 获取登陆人所属公司编号（简版专用）
     *
     * @param req
     * @return 如果已登陆且session有效则返回登陆人所属公司编号，否则返回null
     */
    public static String[] getCompanyCode(HttpServletRequest req) {
    	LoginSessionVo vo = getLoginVo(req);
    	if(vo==null) {
    		return null;
    	}
    	List rel = vo.getRelation_vo_list();
    	if(rel==null || rel.size()==0) {
    	    return new String[]{GlobalConstants.getRelaType_comp()+"00001"};//如果没有所属关系,返回根节点编号
    	}
    	ArrayList codes = new ArrayList();
    	for(int i=0; i<rel.size(); i++) {
    	    AuPartyRelationVo relVo = (AuPartyRelationVo)rel.get(i);
    	    List pRel = relVo.getAll_parent_vo();
    	    for(int j=pRel.size()-1; j>=0; j--) {
    	        AuPartyRelationVo pRelVo = (AuPartyRelationVo)pRel.get(j);
    	        if(GlobalConstants.getPartyType_comp().equals(pRelVo.getPartytype_id())) {
    	            codes.add(pRelVo.getCode());
    	            break;
    	        }
    	    }
    	}
    	return (String[]) codes.toArray(new String[0]);
    }
    /**
     * 
     * 功能: 获取登陆人所属公司名称（简版专用）
     *
     * @param req
     * @return 如果已登陆且session有效则返回登陆人所属公司名称，否则返回null
     */
    public static String[] getCompanyName(HttpServletRequest req) {
    	LoginSessionVo vo = getLoginVo(req);
    	if(vo==null) {
    		return null;
    	}
    	List rel = vo.getRelation_vo_list();
    	if(rel==null || rel.size()==0) {
    	    return new String[]{"-"};//如果没有所属关系,返回"-"
    	}
    	ArrayList names = new ArrayList();
    	for(int i=0; i<rel.size(); i++) {
    	    AuPartyRelationVo relVo = (AuPartyRelationVo)rel.get(i);
    	    List pRel = relVo.getAll_parent_vo();
    	    for(int j=pRel.size()-1; j>=0; j--) {
    	        AuPartyRelationVo pRelVo = (AuPartyRelationVo)pRel.get(j);
    	        if(GlobalConstants.getPartyType_comp().equals(pRelVo.getPartytype_id())) {
    	            names.add(pRelVo.getName());
    	            break;
    	        }
    	    }
    	}
    	return (String[]) names.toArray(new String[0]);
    }
    /**
     * 
     * 功能: 获取登陆人所属部门编号（简版专用）
     *
     * @param req
     * @return 如果已登陆且session有效则返回登陆人所属部门编号，否则返回null
     */
    public static String[] getDepartmentCode(HttpServletRequest req) {
    	LoginSessionVo vo = getLoginVo(req);
    	if(vo==null) {
    		return null;
    	}
    	List rel = vo.getRelation_vo_list();
    	if(rel==null || rel.size()==0) {
    	    return new String[]{GlobalConstants.getRelaType_comp()+"00001"};//如果没有所属关系,返回根节点编号
    	}
    	ArrayList codes = new ArrayList();
    	for(int i=0; i<rel.size(); i++) {
    	    AuPartyRelationVo relVo = (AuPartyRelationVo)rel.get(i);
    	    List pRel = relVo.getAll_parent_vo();
    	    for(int j=pRel.size()-1; j>=0; j--) {
    	        AuPartyRelationVo pRelVo = (AuPartyRelationVo)pRel.get(j);
    	        if(GlobalConstants.getPartyType_dept().equals(pRelVo.getPartytype_id())) {
    	        	codes.add(pRelVo.getCode());
    	            break;
    	        }
    	    }
    	}
    	return (String[]) codes.toArray(new String[0]);
    }
    /**
     * 
     * 功能: 获取登陆人所属部门名称（简版专用）
     *
     * @param req
     * @return 如果已登陆且session有效则返回登陆人所属部门名称，否则返回null
     */
    public static String[] getDepartmentName(HttpServletRequest req) {
    	LoginSessionVo vo = getLoginVo(req);
    	if(vo==null) {
    		return null;
    	}
    	List rel = vo.getRelation_vo_list();
    	if(rel==null || rel.size()==0) {
    	    return new String[]{"-"};//如果没有所属关系,返回"-"
    	}
    	ArrayList names = new ArrayList();
    	for(int i=0; i<rel.size(); i++) {
    	    AuPartyRelationVo relVo = (AuPartyRelationVo)rel.get(i);
    	    List pRel = relVo.getAll_parent_vo();
    	    for(int j=pRel.size()-1; j>=0; j--) {
    	        AuPartyRelationVo pRelVo = (AuPartyRelationVo)pRel.get(j);
    	        if(GlobalConstants.getPartyType_dept().equals(pRelVo.getPartytype_id())) {
    	        	names.add(pRelVo.getName());
    	            break;
    	        }
    	    }
    	}
    	return (String[]) names.toArray(new String[0]);
    }
    /**
     * 
     * 功能: 获取登陆人所属岗位编号（简版专用）
     *
     * @param req
     * @return 如果已登陆且session有效则返回登陆人所属岗位编号，否则返回null
     */
    public static String[] getPositionCode(HttpServletRequest req) {
    	LoginSessionVo vo = getLoginVo(req);
    	if(vo==null) {
    		return null;
    	}
    	List rel = vo.getRelation_vo_list();
    	if(rel==null || rel.size()==0) {
    	    return new String[]{GlobalConstants.getRelaType_comp()+"00001"};//如果没有所属关系,返回根节点编号
    	}
    	ArrayList codes = new ArrayList();
    	for(int i=0; i<rel.size(); i++) {
    	    AuPartyRelationVo relVo = (AuPartyRelationVo)rel.get(i);
    	    List pRel = relVo.getAll_parent_vo();
    	    for(int j=pRel.size()-1; j>=0; j--) {
    	        AuPartyRelationVo pRelVo = (AuPartyRelationVo)pRel.get(j);
    	        if(GlobalConstants.getPartyType_posi().equals(pRelVo.getPartytype_id())) {
    	        	codes.add(pRelVo.getCode());
    	            break;
    	        }
    	    }
    	}
    	return (String[]) codes.toArray(new String[0]);
    }
    /**
     * 
     * 功能: 获取登陆人所属岗位名称（简版专用）
     *
     * @param req
     * @return 如果已登陆且session有效则返回登陆人所属岗位名称，否则返回null
     */
    public static String[] getPositionName(HttpServletRequest req) {
    	LoginSessionVo vo = getLoginVo(req);
    	if(vo==null) {
    		return null;
    	}
    	List rel = vo.getRelation_vo_list();
    	if(rel==null || rel.size()==0) {
    	    return new String[]{"-"};//如果没有所属关系,返回"-"
    	}
    	ArrayList names = new ArrayList();
    	for(int i=0; i<rel.size(); i++) {
    	    AuPartyRelationVo relVo = (AuPartyRelationVo)rel.get(i);
    	    List pRel = relVo.getAll_parent_vo();
    	    for(int j=pRel.size()-1; j>=0; j--) {
    	        AuPartyRelationVo pRelVo = (AuPartyRelationVo)pRel.get(j);
    	        if(GlobalConstants.getPartyType_posi().equals(pRelVo.getPartytype_id())) {
    	        	names.add(pRelVo.getName());
    	            break;
    	        }
    	    }
    	}
    	return (String[]) names.toArray(new String[0]);
    }
    /**
     * 
     * 功能: 获取登陆人自身的组织编号（简版专用）
     *
     * @param req
     * @return 如果已登陆且session有效则返回登陆人自身的组织编号，否则返回null
     */
    public static String[] getEmployeeCode(HttpServletRequest req) {
    	LoginSessionVo vo = getLoginVo(req);
    	if(vo==null) {
    		return null;
    	}
    	List rel = vo.getRelation_vo_list();
    	if(rel==null || rel.size()==0) {
    	    return new String[]{GlobalConstants.getRelaType_comp()+"00001"};//如果没有所属关系,返回根节点编号
    	}
    	String codes[] = new String[rel.size()];
    	for(int i=0; i<rel.size(); i++) {
    	    codes[i] = ((AuPartyRelationVo)rel.get(i)).getCode();
    	}
    	return codes;
    }
    
    
    
    /**
     * 获取登陆人拥有权限的组织机构
     * @param req
     * @return 如果已登陆且session有效则返回组织机构ID的String数组，否则返回null
     */
    public static String[] getOwnerOrg(HttpServletRequest req) {
    	LoginSessionVo vo = getLoginVo(req);
        if(vo==null)
            return null;
        return vo.getOwner_org_arr();
    } 
    
    /**
     * 获得登陆人拥有数据权限（不包括历史数据权限）
     * @param req
     * @return
     */
    public static String[] getOwnerOrgWithoutHistory(HttpServletRequest req) {
    	LoginSessionVo vo = getLoginVo(req);
        if(vo==null)
            return null;
        IAuAuthorizeBS bs = (IAuAuthorizeBS)BeanFactoryHelper.getBean("auAuthorizeBS");
        List list = bs.parseVisitorToRelCode(vo.getOwner_org_arr()); //通过partyrelation表过滤掉历史数据权限的code
        if (null!=list&&list.size() < 1 && getIsAdmin(req))  //当前登陆人为管理员
            return vo.getOwner_org_arr();
        String[] str = new String[null!=list?list.size():0]; //数组中保存的是当前数据权限code
        for (int i = 0 ;i < str.length ; i++) {
           str[i] = list.get(i).toString();
        }
        return str;
    }
    
    /**
     * 获取登陆人拥有的功能数据权限
     * @param req
     * @return 如果已登陆且Session有效则返回组织机构CODE的String数组，否则返回null
     */
    public static String[] getOwnerFunOrg(HttpServletRequest req){
    	String resourceId = (String) req.getSession().getAttribute("_function_id_");
    	LoginSessionVo vo = getLoginVo(req);
    	Map owner_fun_orgs = vo.getOwner_fun_orga();
    	AuAppendVo[] appendVo = (AuAppendVo[])owner_fun_orgs.get(resourceId);
    	if(null==appendVo)
    		return null;
	  	String[] codes = new String[appendVo.length];
	  	for(int i=0;i<appendVo.length;i++){
	  		codes[i]=appendVo[i].getAppend_value();
	  	}
	  	return 0==codes.length?null:codes;
    }
    /**
     * 获取登陆人拥有权限的功能菜单
     * @param req
     * @return 如果已登陆且session有效则返回功能菜单的Map，否则返回null
     */
    public static Map getOwnerMenu(HttpServletRequest req) {
    	LoginSessionVo vo = getLoginVo(req);
        if(vo==null)
            return null;
        return vo.getOwner_menu_map();
    } 
    
    /**
     * 获得公共菜单树
     *
     * @return
     */
    public static List getPublicFuncTree(){
        List publicFuncTreeList = new ArrayList();
        IAuFunctreeBs bs = (IAuFunctreeBs)BeanFactoryHelper.getBean("auFunctreeBs");
        List publicMenuList = bs.queryByCondition(" type = '0' and is_public = '1' ", " TREE_LEVEL,ORDER_CODE ");
        if(null == publicMenuList)
            return Collections.emptyList();
        for (int i = 0; i < publicMenuList.size(); i++) {
            AuFunctreeVo vo = (AuFunctreeVo)publicMenuList.get(i);
            if("1".equals(vo.getIs_public())&&StringUtils.isNotEmpty(vo.getUrl())){
                publicFuncTreeList.add(vo.getUrl());
            }
        }
        return publicFuncTreeList;
    }
    
    
    /**
     * 获得当前登录人拥有权限的第一级功能菜单
     * @param req
     * @return 第一级功能菜单列表
     */
    public static List getFirstLevelMenu(HttpServletRequest req) {
        HttpSession session = req.getSession(false);//检测当前有无session存在,如果不存在则创建一个,如果存在就返回当前的
        session.setAttribute("firstMenuList", new ArrayList());
        session.setAttribute("secondMenuList", new ArrayList());
        session.setAttribute("thirdMenuList", new ArrayList());
        List firstMenuList = (List)session.getAttribute("firstMenuList");
        List secondMenuList = (List)session.getAttribute("secondMenuList");
        List thirdMenuList = (List)session.getAttribute("thirdMenuList");
        Map menuMap = getOwnerMenu(req);
        if (menuMap == null)
            return new ArrayList();

        IAuFunctreeBs bs = (IAuFunctreeBs) BeanFactoryHelper.getBean("auFunctreeBs");
        List allMenuList = bs.queryByCondition(" type = '0' ", " TREE_LEVEL,ORDER_CODE ");
        if (allMenuList == null)
            return new ArrayList();
        if (LoginHelper.getIsAdmin(req)) { //超级管理员菜单
            for (int i = 0; i < allMenuList.size(); i++) {
                AuFunctreeVo vo = (AuFunctreeVo)allMenuList.get(i);
                switch (vo.getTree_level()) {
                    case 2:firstMenuList.add(vo);
                                 break;
                    case 3:secondMenuList.add(vo);
                                 break;
                    case 4:thirdMenuList.add(vo);
                                 break;
                    default:break;
                }
            }
            return firstMenuList;
        }
        for (int i = 0; i < allMenuList.size(); i++) {
            AuFunctreeVo vo = (AuFunctreeVo)allMenuList.get(i);
            if(menuMap.keySet().contains(vo.getTotal_code()) || "1".equals(vo.getIs_public()))  {
                switch(vo.getTree_level()) {
                case 2: //获取第一级菜单
                            firstMenuList.add(vo);
                            break;
                case 3: //获取第一、二级菜单
                            secondMenuList.add(vo);
                            for (int j = 0; j < allMenuList.size(); j++) {
                                AuFunctreeVo svo = (AuFunctreeVo)allMenuList.get(j);
                                if (vo.getParent_code().equals(svo.getTotal_code())) {
                                    firstMenuList.add(svo);
                                    break;
                                }
                            }                            
                            break;
                case 4: //获取第一、二、三级菜单
                            thirdMenuList.add(vo);
                            AuFunctreeVo svo = new AuFunctreeVo() ;
                            for (int j = 0; j < allMenuList.size();j++) {
                                svo = (AuFunctreeVo)allMenuList.get(j);
                                if (vo.getParent_code().equals(svo.getTotal_code())) {
                                    secondMenuList.add(svo);
                                    for (int k = 0;k < allMenuList.size(); k++) {
                                        AuFunctreeVo fvo = (AuFunctreeVo)allMenuList.get(k);
                                        if (svo.getParent_code().equals(fvo.getTotal_code())) {
                                            firstMenuList.add(fvo);
                                            break;
                                        }
                                    }  
                                    break;
                                }
                            }  
                            break;
                default:break;
                }                
            }
        }
        return VoHelperTools.removeDuplicate(firstMenuList);
    }
    
    /**
     * 根据当前节点的totalCode，获得下一级的节点列表
     * @param req
     * @param totalCode
     * @return 下一级的功能菜单列表
     */
    public static List getNextLevelMenu(HttpServletRequest req,String totalCode) {
        if (totalCode == null || "".equals(totalCode))
            return new ArrayList();
        List list = new ArrayList();
        HttpSession session = req.getSession(false);
        List firstMenuList = (List)session.getAttribute("firstMenuList");
        List secondMenuList = (List)session.getAttribute("secondMenuList");
        List thirdMenuList = (List)session.getAttribute("thirdMenuList");        
        list.addAll(VoHelperTools.removeDuplicate(firstMenuList));
        list.addAll(VoHelperTools.removeDuplicate(secondMenuList));
        list.addAll(VoHelperTools.removeDuplicate(thirdMenuList));
        List returnList = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            AuFunctreeVo vo = (AuFunctreeVo)list.get(i);
            if (totalCode.equals(vo.getParent_code()))
                returnList.add(vo);
        }
        return returnList;        
    }
    
    /**
     * 获得当前节点上一级节点的vo
     * @param req
     * @param totalCode
     * @return 上一级功能菜单节点vo
     */
    public static AuFunctreeVo getPreviousLevelMenu(HttpServletRequest req, String totalCode) {
        if (totalCode == null || "".equals(totalCode))
            return null;
        List list = new ArrayList();
        HttpSession session = req.getSession(false);
        List firstMenuList = (List)session.getAttribute("firstMenuList");
        List secondMenuList = (List)session.getAttribute("secondMenuList");
        List thirdMenuList = (List)session.getAttribute("thirdMenuList");        
        list.addAll(VoHelperTools.removeDuplicate(firstMenuList));
        list.addAll(VoHelperTools.removeDuplicate(secondMenuList));
        list.addAll(VoHelperTools.removeDuplicate(thirdMenuList));
        String parentCode = "";
        for (int i = 0; i < list.size(); i++) {
            AuFunctreeVo vo = (AuFunctreeVo)list.get(i);
            if (totalCode.equals(vo.getTotal_code())) {
                parentCode = vo.getParent_code();
                break;
            }
        }
        for (int i = 0; i < list.size(); i++) {
            AuFunctreeVo vo = (AuFunctreeVo)list.get(i);
            if (vo.getTotal_code().equals(parentCode))
                return vo;
        }
        return null;
    }
    
    /**
     * 获得功能菜单节点路径
     * @param req
     * @param totalCode
     * @return 功能菜单节点路径，格式为xxx -> xxx -> xxx
     */
    public static String getMenuPath(HttpServletRequest req,String totalCode) {
        if (totalCode == null || "".equals(totalCode))
            return null;
        IAuFunctreeBs bs = (IAuFunctreeBs) BeanFactoryHelper.getBean("auFunctreeBs");
        List allMenuList = bs.queryByCondition(" type = '0' ", " TREE_LEVEL,ORDER_CODE ");
        if (allMenuList == null)
            return null;        
        String menuPath = "";
        AuFunctreeVo currentVo = new AuFunctreeVo();
        for (int i = 0; i < allMenuList.size(); i++) {
            currentVo = (AuFunctreeVo)allMenuList.get(i);
            if (currentVo.getTotal_code().equals(totalCode)) {
                menuPath = currentVo.getName();
                break;
            }
        }
        for (int i = 0; i < allMenuList.size(); i++) {
            AuFunctreeVo vo = (AuFunctreeVo)allMenuList.get(i);
            if (vo.getTotal_code().equals(currentVo.getParent_code()) && !"1".equals(vo.getTree_level())) {
                menuPath = vo.getName() + " -> " +  menuPath;
                currentVo = vo;
                i = 0;
            }
        }
        return menuPath;
    }
      
    /**
     * 获取登陆人拥有权限的功能按钮
     * @param req
     * @return 如果已登陆且session有效则返回功能按钮的Map，否则返回null
     */
    public static Map getOwnerButn(HttpServletRequest req) {
    	LoginSessionVo vo = getLoginVo(req);
        if(vo==null)
            return null;
        return vo.getOwner_butn_map();
    } 
    /**
     * 获取登陆人拥有权限的数据字段
     * @param req
     * @return 如果已登陆且session有效则返回数据字段的Map，否则返回null
     */
    public static Map getOwnerFild(HttpServletRequest req) {
    	LoginSessionVo vo = getLoginVo(req);
        if(vo==null)
            return null;
        return vo.getOwner_fild_map();
    } 
    /**
     * 获取登陆人拥有权限的数据记录
     * @param req
     * @return 如果已登陆且session有效则返回数据记录的Map，否则返回null
     */
    public static Map getOwnerRecd(HttpServletRequest req) {
    	LoginSessionVo vo = getLoginVo(req);
        if(vo==null)
            return null;
        return vo.getOwner_recd_map();
    }
    /**
     * 获取登陆人拥有权限的功能菜单的url
     * @param req
     * @return
     */
    public static Map getOwnerMenuUrl(HttpServletRequest req) {
    	LoginSessionVo vo = getLoginVo(req);
        if(vo==null)
            return null;
        return vo.getOwner_menu_url_map();
    } 
    /**
     * 获取全部功能菜单的url
     * @param req
     * @return
     */
    public static Map getAllMenuUrl(HttpServletRequest req) {
    	LoginSessionVo vo = getLoginVo(req);
        if(vo==null)
            return null;
        return vo.getAll_menu_url_map();
    } 
    /**
     * 获取登陆人拥有授权权限的组织机构
     * @param req
     * @return 如果已登陆且session有效则返回组织机构ID的String数组，否则返回null
     */
    
    /* 作用和效果与getOwnerOrg方法一致
    public static String[] getOwnerOrg4Admin(HttpServletRequest req) {
    	LoginSessionVo vo = getLoginVo(req);
        if(vo==null)
            return null;
        return vo.getOwner_org_arr_admin();
    } 
    */
    
    /**
     * 获取登陆人拥有授权权限的功能菜单
     * @param req
     * @return 如果已登陆且session有效则返回功能菜单的Map，否则返回null
     */
    public static Map getOwnerMenu4Admin(HttpServletRequest req) {
    	LoginSessionVo vo = getLoginVo(req);
        if(vo==null)
            return null;
        return vo.getOwner_menu_map_admin();
    } 
    /**
     * 获取登陆人拥有授权权限的功能按钮
     * @param req
     * @return 如果已登陆且session有效则返回功能按钮的Map，否则返回null
     */
    public static Map getOwnerButn4Admin(HttpServletRequest req) {
    	LoginSessionVo vo = getLoginVo(req);
        if(vo==null)
            return null;
        return vo.getOwner_butn_map_admin();
    } 
    /**
     * 获取登陆人拥有授权权限的数据字段
     * @param req
     * @return 如果已登陆且session有效则返回数据字段的Map，否则返回null
     */
    public static Map getOwnerFild4Admin(HttpServletRequest req) {
    	LoginSessionVo vo = getLoginVo(req);
        if(vo==null)
            return null;
        return vo.getOwner_fild_map_admin();
    } 
    /**
     * 获取登陆人拥有授权权限的数据记录
     * @param req
     * @return 如果已登陆且session有效则返回数据记录的Map，否则返回null
     */
    public static Map getOwnerRecd4Admin(HttpServletRequest req) {
    	LoginSessionVo vo = getLoginVo(req);
        if(vo==null)
            return null;
        return vo.getOwner_recd_map_admin();
    }
    /**
     * 获取登陆人拥有的功能权限（菜单+按钮）
     * @param req
     * @return 如果已登陆且session有效则返回功能权限的Map，否则返回null
     */
    public static Map getOwnerFunc(HttpServletRequest req) {
    	LoginSessionVo vo = getLoginVo(req);
        if(vo==null)
            return null;
        return vo.getOwner_func_map();
    }
    /**
     * 获取登陆人拥有授权权限的功能权限（菜单+按钮）
     * @param req
     * @return 如果已登陆且session有效则返回功能权限的Map，否则返回null
     */
    public static Map getOwnerFunc4Admin(HttpServletRequest req) {
    	LoginSessionVo vo = getLoginVo(req);
        if(vo==null)
            return null;
        return vo.getOwner_func_map_admin();
    }
    /**
     * 
     * 功能: 判断当前登陆人是否有某按钮的权限
     *
     * @param session
     * @param keyword 按钮的keyword
     * @return
     */
    public static boolean hasButnAu(HttpSession session, String keyword) {
        LoginSessionVo vo = getLoginVo(session);
        if(vo==null)
            return false;
        if("1".equals(vo.getIs_admin())) {
            return true;
        }
        Map map = vo.getOwner_butn_map();
        if(map==null)
            return false;
        for(Iterator it=map.keySet().iterator(); it.hasNext(); ) {
	    	AuFunctreeVo fVo = (AuFunctreeVo)map.get((String)it.next());
	    	if(keyword.equals(fVo.getKeyword())) {
	    	   return true;
	    	}
	    }
        return false;
    }
    /**
     * 根据用户名密码验证用户是否合法
     * @param login_id
     * @param password
     * @return 1 校验通过， -1 帐号被禁用，0 密码有误， -2 帐号不存在
     */
    public static int validate(String login_id, String password)  {
    	IAuUserBs bs = (IAuUserBs) BeanFactoryHelper.getBean("auUserBs");
        List lResult = bs.queryByCondition("login_id='" + login_id + "'");
        //用户身份校验
        if (lResult != null && lResult.size() > 0) {
            AuUserVo userVo = (AuUserVo)lResult.get(0);
            if (Encode.encode(password).equals(userVo.getPassword()))  {
                if ("1".equals(userVo.getEnable_status()))  {
                	return 1; //校验通过
                } else {
                	return -1; //帐号被禁用
                }
            } else {
            	return 0; //"密码有误
            }
        } else {
        	return -2;  //"帐号不存在
        }
    }
    /**
     * 返回离登陆人最近的符合团体类型要求的上级机构。
     * @param req
     * @param partyTypeId 团体类型
     * @return 上级机构的code，如果没有找到，返回null
     */
    public static String getDirectParentCodeByType(HttpServletRequest req,String partyTypeId) {
        LoginSessionVo vo = getLoginVo(req);
        if(vo==null)
            return null;
        String code = vo.getCurrent_code();
        if(StringUtils.isEmpty(code))
            return null;
        List al = OrgHelper.getParentRelation(code);
        for(int i=0;i<al.size();i++){
            AuPartyRelationVo relVo = (AuPartyRelationVo)al.get(i);
            if(relVo.getPartytype_id().equals(partyTypeId)){
                return relVo.getCode();
            }
        }
        return null;
    }
    /**
     * 返回离登陆人直接上级部门。
     * @param req
     * @return 上级部门的code，如果没有找到，返回null
     */
    public static String getDirectDepartmentCode(HttpServletRequest req) {
        return getDirectParentCodeByType(req,GlobalConstants.getPartyType_dept());
    }
}