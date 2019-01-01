package venus.oa.adapter.impl;

import org.springframework.stereotype.Component;
import venus.frames.mainframe.util.Helper;
import venus.oa.adapter.IAuthorization;
import venus.oa.authority.auuser.bs.IAuUserBs;
import venus.oa.authority.auuser.util.IAuUserConstants;
import venus.oa.authority.auuser.vo.AuUserVo;
import venus.oa.helper.LoginHelper;
import venus.oa.login.vo.LoginSessionVo;
import venus.oa.organization.aupartyrelation.bs.IAuPartyRelationBs;
import venus.oa.organization.aupartyrelation.util.IConstants;
import venus.oa.organization.aupartyrelation.vo.AuPartyRelationVo;
import venus.oa.util.GlobalConstants;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 权限系统对外部的api，临时实现方式，需要重构
 */
@Component("authorityAdapter")
public class AuthorityAdaptorImpl implements IAuthorization {

    public String fetchUserId(HttpServletRequest request) {
        /*String retStr = LoginHelper.getLoginVo(request).getCurrent_code();
        if(null==retStr||0==retStr.length())
            retStr = LoginHelper.getLoginVo(request).getParty_id();//admin没有关系，返回partyid
        return retStr;//返回关系编码code
         */
        if (request == null) //没有登录系统时，返回null
            return null;
        if(LoginHelper.getIsAdmin(request)){
            return LoginHelper.getLoginVo(request).getParty_id();//admin没有关系，返回partyid
        }
        LoginSessionVo sessionVo = LoginHelper.getLoginVo(request);
        if (sessionVo == null)
            return null;
        String currentCode = sessionVo.getCurrent_code();
        if (currentCode == null) //当用户登录系统后，且不需要选择机构关系时，返回当前partyId
            return sessionVo.getParty_id();
        IAuPartyRelationBs relBs = (IAuPartyRelationBs) Helper.getBean(IConstants.BS_KEY);
        AuPartyRelationVo vo = new AuPartyRelationVo();
        vo.setCode(currentCode);
        List list = relBs.queryAuPartyRelation(vo);
        if(list==null || list.size()==0) {
            throw new RuntimeException(venus.frames.i18n.util.LocaleHolder.getMessage("udp.authority.Please_login_first"));
        }
        AuPartyRelationVo relationVo =(AuPartyRelationVo)list.get(0);
        String id = null;
        if(relationVo.getRelationtype_id().equals(GlobalConstants.getRelaType_proxy())){
            id = relationVo.getId();
        }else{
            id = relationVo.getPartyid();
        }
        return id;
    }

    @Override
    public Map findResourcesById(String userId) {
        return null;
    }

    @Override
    public List findResourcesByIdAndResType(String userId, String resType) {
        return null;
    }

    @Override
    public List findAllResources() {
        return null;
    }

    @Override
    public List findResourcesByConditions(String userId, String resType, String groupName) {
        return null;
    }

    @Override
    public List findResourcesByConditions(String resType, String groupName) {
        return null;
    }

    @Override
    public void registerResources(List resources) {

    }


    /*
     * （非 Javadoc）
     * 
     * @see udp.common.authority.service.IAuthorization#findUserDetailsById(java.lang.String)
     */
//    public UserDetails findUserDetailsById(String userId) {
//        PartyVo partyVo = null;
//        if(19==userId.length()){
//            partyVo = OrgHelper.getPartyVoByID(userId);
//        }else{
//            partyVo = OrgHelper.getPartyVoByCode(userId);
//        }
//        IAuUserBs bs = (IAuUserBs) Helper
//                .getBean(IAuUserConstants.BS_KEY);
//        AuUserVo userVo = bs.getByPartyId(partyVo.getId());
//        UserDetails userDetails = new UserDetails();
//        userDetails.setName(partyVo.getName());
//        userDetails.setId(userId);
//        userDetails.setUserName(userVo.getLogin_id());
//        userDetails.setPassword(userVo.getPassword());
//        userDetails.setAuthorities(findResourcesById(userId));
//        return userDetails;
//    }

    /*
     * （非 Javadoc）
     * 
     * @see udp.common.authority.service.IAuthorization#findResourcesById(java.lang.String)
     */
//    public Map findResourcesById(String userId) {
//        IAuAuthorizeBS auBs = (IAuAuthorizeBS) Helper.getBean(venus.authority.au.auauthorize.util.IConstants.BS_KEY);
//        Map auMap = 19==userId.length()?auBs.getAuByPartyId(userId, null):auBs.getAuByVisitorCode(userId, null);
//        //获取全部菜单、按钮
//        String fType = GlobalConstants.getResType_menu();//功能菜单
//        String bType = GlobalConstants.getResType_butn();//功能按钮
//        IAuFunctreeBs funcBs = (IAuFunctreeBs) Helper
//                .getBean(IAuFunctreeConstants.BS_KEY);
//        List lFunc = funcBs.queryByCondition("TYPE='" + fType + "' OR TYPE='"
//                + bType + "'");
//        Map m_all_func = new HashMap();
//        for (Iterator it = lFunc.iterator(); it.hasNext();) {
//            AuFunctreeVo vo = (AuFunctreeVo) it.next();
//            m_all_func.put(vo.getId(), vo);
//        }
//        //将权限拆分：
//        String AU_PERMIT = GlobalConstants.getAuTypePermit();//权限类型——允许
//        Map authorities = new HashMap();
//        List menus = new ArrayList();
//        List buttons = new ArrayList();
//        List records = new ArrayList();
//        List columns = new ArrayList();
//        List orgs = new ArrayList();
//        for (Iterator it = auMap.keySet().iterator(); it.hasNext();) {
//            AuAuthorizeVo auVo = (AuAuthorizeVo) auMap.get((String) it.next());
//            String resType = auVo.getResource_type();//资源类型
//            String resId = auVo.getResource_id();//资源id
//            String auStatus = auVo.getAuthorize_status();//授权情况
//            if (GlobalConstants.getResType_butn().equals(resType)) {//按钮
//                AuFunctreeVo fvo = (AuFunctreeVo) m_all_func.get(resId);
//                if (AU_PERMIT.equals(auStatus)) { //只取允许的
//                    Resource res = new Resource();
//                    res.setId(fvo.getCode());
//                    res.setName(fvo.getName());
//                    res.setType(IAuthorityConstants.RESOURCE_TYPE_BUTTON);
//                    res.setValue(fvo.getKeyword());//按钮标识
//                    buttons.add(res);
//                }
//            } else if (GlobalConstants.getResType_menu().equals(resType)) {//菜单
//                AuFunctreeVo fvo = (AuFunctreeVo) m_all_func.get(resId);
//                if (AU_PERMIT.equals(auStatus)) { //只取允许的
//                    Resource res = new Resource();
//                    res.setId(fvo.getCode());
//                    res.setName(fvo.getName());
//                    res.setType(IAuthorityConstants.RESOURCE_TYPE_MENU);
//                    res.setValue(fvo.getUrl());
//                    menus.add(res);
//                }
//            } else if (GlobalConstants.getResType_fild().equals(resType)) {//字段
//                if (AU_PERMIT.equals(auStatus)) { //只取允许的
//                    Resource res = new Resource();
//                    res.setId(auVo.getResource_id());
//                    res.setName(auVo.getResourcename());
//                    res.setType(IAuthorityConstants.RESOURCE_TYPE_COLUMN);
//                    res.setValue(auVo.getResourcename());
//                    IAuResourceBs resourceBs = (IAuResourceBs) Helper
//                            .getBean(IAuResourceConstants.BS_KEY);
//                    res.setGroupName(resourceBs.find(auVo.getResource_id())
//                            .getTable_name());
//                    columns.add(res);
//                }
//            } else if (GlobalConstants.getResType_recd().equals(resType)) {//记录
//                if (AU_PERMIT.equals(auStatus)) { //只取允许的
//                    IAuResourceBs resourceBs = (IAuResourceBs) Helper.getBean(IAuResourceConstants.BS_KEY);
//                    AuResourceVo resourceVo = resourceBs.find(auVo.getResource_id());
//                    String tableName = resourceVo.getTable_name();
//                    Resource res = new Resource();
//                    res.setId(auVo.getResource_id());
//                    res.setName(auVo.getResourcename());
//                    res.setType(IAuthorityConstants.RESOURCE_TYPE_RECORD);
//                    res.setValue(resourceVo.getValue());
//                    res.setGroupName(tableName);
//                    records.add(res);
//                }
//            } else if (GlobalConstants.getResType_orga().equals(resType)) {//组织
//                Resource res = new Resource();
//                res.setId(auVo.getResource_code());
//                res.setName(auVo.getResourcename());
//                res.setType("ORGS");//TODO组织机构权限
//                res.setValue(auVo.getResource_code());
//                orgs.add(res);
//            }
//        }
//        authorities.put(IAuthorityConstants.RESOURCE_TYPE_MENU, menus);// menu类型
//        authorities.put(IAuthorityConstants.RESOURCE_TYPE_BUTTON, buttons);// button类型
//        authorities.put(IAuthorityConstants.RESOURCE_TYPE_RECORD, records);// record类型
//        authorities.put(IAuthorityConstants.RESOURCE_TYPE_COLUMN, columns);// column类型
//        return authorities;
//    }

    /*
     * （非 Javadoc）
     * 
     * @see udp.common.authority.service.IAuthorization#findResourcesByIdAndResType(java.lang.String,
     *           java.lang.String)
     */
//    public List findResourcesByIdAndResType(String userId, String resourceType) {
//        IAuAuthorizeBS auBs = (IAuAuthorizeBS) Helper.getBean(venus.authority.au.auauthorize.util.IConstants.BS_KEY);
//        Map auMap = 19==userId.length()?auBs.getAuByPartyId(userId, null):auBs.getAuByVisitorCode(userId, null);
//        //获取全部菜单、按钮
//        String fType = GlobalConstants.getResType_menu();//功能菜单
//        String bType = GlobalConstants.getResType_butn();//功能按钮
//        IAuFunctreeBs funcBs = (IAuFunctreeBs) Helper
//                .getBean(IAuFunctreeConstants.BS_KEY);
//        List lFunc = funcBs.queryByCondition("TYPE='" + fType + "' OR TYPE='"
//                + bType + "'");
//        Map m_all_func = new HashMap();
//        for (Iterator it = lFunc.iterator(); it.hasNext();) {
//            AuFunctreeVo vo = (AuFunctreeVo) it.next();
//            m_all_func.put(vo.getId(), vo);
//        }
//        //将权限拆分：
//        String AU_PERMIT = GlobalConstants.getAuTypePermit();//权限类型——允许
//        List returnList = new ArrayList();
//        for (Iterator it = auMap.keySet().iterator(); it.hasNext();) {
//            AuAuthorizeVo auVo = (AuAuthorizeVo) auMap.get((String) it.next());
//            String resType = auVo.getResource_type();//资源类型
//            String resId = auVo.getResource_id();//资源id
//            String auStatus = auVo.getAuthorize_status();//授权情况
//            if (resourceType.equals(IAuthorityConstants.RESOURCE_TYPE_BUTTON)
//                    && GlobalConstants.getResType_butn().equals(resType)) {//按钮
//                AuFunctreeVo fvo = (AuFunctreeVo) m_all_func.get(resId);
//                if (AU_PERMIT.equals(auStatus)) { //只取允许的
//                    Resource res = new Resource();
//                    res.setId(fvo.getCode());
//                    res.setName(fvo.getName());
//                    res.setType(IAuthorityConstants.RESOURCE_TYPE_BUTTON);
//                    res.setValue(fvo.getKeyword());//按钮标识
//                    returnList.add(res);
//                }
//            } else if (resourceType.equals(IAuthorityConstants.RESOURCE_TYPE_MENU)
//                    && GlobalConstants.getResType_menu().equals(resType)) {//菜单
//                AuFunctreeVo fvo = (AuFunctreeVo) m_all_func.get(resId);
//                if (AU_PERMIT.equals(auStatus)) { //只取允许的
//                    Resource res = new Resource();
//                    res.setId(fvo.getCode());
//                    res.setName(fvo.getName());
//                    res.setType(IAuthorityConstants.RESOURCE_TYPE_MENU);
//                    res.setValue(fvo.getUrl());
//                    returnList.add(res);
//                }
//            } else if (resourceType.equals(IAuthorityConstants.RESOURCE_TYPE_COLUMN)
//                    && GlobalConstants.getResType_fild().equals(resType)) {//字段
//                if (AU_PERMIT.equals(auStatus)) { //只取允许的
//                    Resource res = new Resource();
//                    res.setId(auVo.getResource_id());
//                    res.setName(auVo.getResourcename());
//                    res.setType(IAuthorityConstants.RESOURCE_TYPE_COLUMN);
//                    res.setValue(auVo.getResourcename());
//                    IAuResourceBs resourceBs = (IAuResourceBs) Helper
//                            .getBean(IAuResourceConstants.BS_KEY);
//                    res.setGroupName(resourceBs.find(auVo.getResource_id())
//                            .getTable_name());
//                    returnList.add(res);
//                }
//            } else if (resourceType.equals(IAuthorityConstants.RESOURCE_TYPE_RECORD)
//                    && GlobalConstants.getResType_recd().equals(resType)) {//记录
//                if (AU_PERMIT.equals(auStatus)) { //只取允许的
//                    IAuResourceBs resourceBs = (IAuResourceBs) Helper.getBean(IAuResourceConstants.BS_KEY);
//                    AuResourceVo resourceVo = resourceBs.find(auVo.getResource_id());
//                    String tableName = resourceVo.getTable_name();
//                    Resource res = new Resource();
//                    res.setId(auVo.getResource_id());
//                    res.setName(auVo.getResourcename());
//                    res.setType(IAuthorityConstants.RESOURCE_TYPE_RECORD);
//                    res.setValue(resourceVo.getValue());
//                    res.setGroupName(tableName);
//                    returnList.add(res);
//                }
//            } else if (resourceType.equals("ORGS")
//                    && GlobalConstants.getResType_orga().equals(resType)) {//组织
//                Resource res = new Resource();
//                res.setId(auVo.getResource_code());
//                res.setName(auVo.getResourcename());
//                res.setType("ORGS");//TODO组织机构权限
//                res.setValue(auVo.getResource_code());
//                returnList.add(res);
//            }
//        }
//        return returnList;
//    }

    /*
     * （非 Javadoc）
     * 
     * @see udp.common.authority.service.IAuthorization#findAllResources()
     */
//    public List findAllResources() {
//        IAuResourceBs resourceBs = (IAuResourceBs) Helper.getBean(IAuResourceConstants.BS_KEY);
//        List resourceList = resourceBs.queryAll();
//        String fType = GlobalConstants.getResType_menu();//功能菜单
//        String bType = GlobalConstants.getResType_butn();//功能按钮
//        IAuFunctreeBs funcBs = (IAuFunctreeBs) Helper
//                .getBean(IAuFunctreeConstants.BS_KEY);
//        List lFunc = funcBs.queryByCondition("TYPE='" + fType + "' OR TYPE='"
//                + bType + "'");
//        Map m_all_func = new HashMap();
//        for (Iterator it = lFunc.iterator(); it.hasNext();) {
//            AuFunctreeVo vo = (AuFunctreeVo) it.next();
//            m_all_func.put(vo.getId(), vo);
//        }
//        //将权限拆分：
//        List returnList = new ArrayList();
//        for (Iterator it = resourceList.iterator(); it.hasNext();) {
//            AuResourceVo auVo = (AuResourceVo) it.next();
//            String resType = auVo.getResource_type();//资源类型
//            if (GlobalConstants.getResType_butn().equals(resType)) {//按钮
//                AuFunctreeVo fvo = (AuFunctreeVo) m_all_func.get(auVo.getId());
//                Resource res = new Resource();
//                res.setId(fvo.getCode());
//                res.setName(fvo.getName());
//                res.setType(IAuthorityConstants.RESOURCE_TYPE_BUTTON);
//                res.setValue(fvo.getKeyword());//按钮标识
//                returnList.add(res);
//            } else if (GlobalConstants.getResType_menu().equals(resType)) {//菜单
//                AuFunctreeVo fvo = (AuFunctreeVo) m_all_func.get(auVo.getId());
//                Resource res = new Resource();
//                res.setId(fvo.getCode());
//                res.setName(fvo.getName());
//                res.setType(IAuthorityConstants.RESOURCE_TYPE_MENU);
//                res.setValue(fvo.getUrl());
//                returnList.add(res);
//            } else if (GlobalConstants.getResType_fild().equals(resType)) {//字段
//                Resource res = new Resource();
//                res.setId(auVo.getId());
//                res.setName(auVo.getName());
//                res.setType(IAuthorityConstants.RESOURCE_TYPE_COLUMN);
//                res.setValue(auVo.getField_name());
//                res.setGroupName(auVo.getTable_name());
//                returnList.add(res);
//            } else if (GlobalConstants.getResType_recd().equals(resType)) {//记录
//                Resource res = new Resource();
//                res.setId(auVo.getId());
//                res.setName(auVo.getName());
//                res.setType(IAuthorityConstants.RESOURCE_TYPE_RECORD);
//                res.setValue(auVo.getValue());
//                res.setGroupName(resourceBs.find(auVo.getTable_name())
//                        .getTable_name());
//                returnList.add(res);
//            } else if (GlobalConstants.getResType_orga().equals(resType)) {//组织
//                Resource res = new Resource();
//                res.setId(auVo.getId());
//                res.setName(auVo.getName());
//                res.setType("ORGS");//TODO组织机构权限
//                res.setValue(auVo.getValue());
//                returnList.add(res);
//            }
//        }
//        return returnList;
//    }

    /*
     * （非 Javadoc）
     * 
     * @see udp.common.authority.service.IAuthorization#findResourcesByConditions(java.lang.String,
     *           java.lang.String, java.lang.String)
     */
//    public List findResourcesByConditions(String userId, String resourceType, String groupName) {
//        IAuAuthorizeBS auBs = (IAuAuthorizeBS) Helper.getBean(venus.authority.au.auauthorize.util.IConstants.BS_KEY);
//        Map auMap = 19==userId.length()?auBs.getAuByPartyId(userId, null):auBs.getAuByVisitorCode(userId, null);
//        //将权限拆分：
//        String AU_PERMIT = GlobalConstants.getAuTypePermit();//权限类型——允许
//        List returnList = new ArrayList();
//        for (Iterator it = auMap.keySet().iterator(); it.hasNext();) {
//            AuAuthorizeVo auVo = (AuAuthorizeVo) auMap.get((String) it.next());
//            String resType = auVo.getResource_type();//资源类型
//            String auStatus = auVo.getAuthorize_status();//授权情况
//            if (resourceType.equals(IAuthorityConstants.RESOURCE_TYPE_COLUMN)
//                    && GlobalConstants.getResType_fild().equals(resType)) {//字段
//                IAuResourceBs resourceBs = (IAuResourceBs) Helper
//                        .getBean(IAuResourceConstants.BS_KEY);
//                String tableName = resourceBs.find(auVo.getResource_id())
//                        .getTable_name();
//                if (AU_PERMIT.equals(auStatus) && groupName.toUpperCase().equals(tableName.toUpperCase())) { //只取允许的和表名符合的
//                    Resource res = new Resource();
//                    res.setId(auVo.getResource_id());
//                    res.setName(auVo.getResource_code());    //返回资源编码
//                    res.setType(IAuthorityConstants.RESOURCE_TYPE_COLUMN);
//                    res.setValue(auVo.getAccess_type());   //返回权限: 可授权2，只读3，可写5
//                    res.setGroupName(tableName);
//                    returnList.add(res);
//                }
//            } else if (resourceType.equals(IAuthorityConstants.RESOURCE_TYPE_RECORD)
//                    && GlobalConstants.getResType_recd().equals(resType)) {//记录
//                IAuResourceBs resourceBs = (IAuResourceBs) Helper
//                        .getBean(IAuResourceConstants.BS_KEY);
//                AuResourceVo resourceVo = resourceBs.find(auVo.getResource_id());
//                String tableName = resourceVo.getTable_name();
//                if (AU_PERMIT.equals(auStatus) && groupName.toUpperCase().equals(tableName.toUpperCase())) { //只取允许的和表名符合的
//                    Resource res = new Resource();
//                    res.setId(auVo.getResource_id());
//                    res.setName(auVo.getResourcename());
//                    res.setType(IAuthorityConstants.RESOURCE_TYPE_RECORD);
//                    res.setValue(resourceVo.getValue());
//                    res.setGroupName(tableName);
//                    returnList.add(res);
//                }
//            } else if (resourceType.equals(IAuthorityConstants.RESOURCE_TYPE_BUTTON)
//                    && GlobalConstants.getResType_butn().equals(resType)) {//按钮
//                IAuResourceBs resourceBs = (IAuResourceBs) Helper
//                        .getBean(IAuResourceConstants.BS_KEY);
//                String code_value = resourceBs.find(auVo.getResource_id())
//                        .getField_name();
//                if (AU_PERMIT.equals(auStatus)) { //只取允许的
//                    Resource res = new Resource();
//                    res.setId(auVo.getResource_id());
//                    res.setName(auVo.getResourcename());
//                    res.setType(IAuthorityConstants.RESOURCE_TYPE_BUTTON);
//                    res.setValue(code_value);
//                    returnList.add(res);
//                }
//            }
//        }
//        return returnList;
//    }

    /*
     * （非 Javadoc）
     * 
     * @see udp.common.authority.service.IAuthorization#findResourcesByConditions(java.lang.String,
     *           java.lang.String)
     */
//    public List findResourcesByConditions(String resourceType, String groupName) {
//        IAuResourceBs resourceBs = (IAuResourceBs) Helper
//                .getBean(IAuResourceConstants.BS_KEY);
//        List resourceList = resourceBs.queryAll();
//        String fType = GlobalConstants.getResType_menu();//功能菜单
//        String bType = GlobalConstants.getResType_butn();//功能按钮
//        IAuFunctreeBs funcBs = (IAuFunctreeBs) Helper
//                .getBean(IAuFunctreeConstants.BS_KEY);
//        List lFunc = funcBs.queryByCondition("TYPE='" + fType + "' OR TYPE='"
//                + bType + "'");
//        Map m_all_func = new HashMap();
//        for (Iterator it = lFunc.iterator(); it.hasNext();) {
//            AuFunctreeVo vo = (AuFunctreeVo) it.next();
//            m_all_func.put(vo.getId(), vo);
//        }
//        //将权限拆分：
//        List returnList = new ArrayList();
//        for (Iterator it = resourceList.iterator(); it.hasNext();) {
//            AuResourceVo auVo = (AuResourceVo) it.next();
//            String resType = auVo.getResource_type();//资源类型
//            if (resourceType.equals(IAuthorityConstants.RESOURCE_TYPE_COLUMN)&&GlobalConstants.getResType_fild().equals(resType)) {//字段
//                Resource res = new Resource();
//                res.setId(auVo.getId());
//                res.setName(auVo.getName());
//                res.setType(IAuthorityConstants.RESOURCE_TYPE_COLUMN);
//                res.setValue(auVo.getField_name());
//                res.setGroupName(auVo.getTable_name());
//                returnList.add(res);
//            } else if (resourceType.equals(IAuthorityConstants.RESOURCE_TYPE_RECORD)&&GlobalConstants.getResType_recd().equals(resType)) {//记录
//                Resource res = new Resource();
//                res.setId(auVo.getId());
//                res.setName(auVo.getName());
//                res.setType(IAuthorityConstants.RESOURCE_TYPE_RECORD);
//                res.setValue(auVo.getValue());
//                res.setGroupName(auVo.getTable_name());
//                returnList.add(res);
//            } else if (resourceType.equals(IAuthorityConstants.RESOURCE_TYPE_BUTTON)&&GlobalConstants.getResType_butn().equals(resType)) {//按钮
//                Resource res = new Resource();
//                res.setId(auVo.getId());
//                res.setName(auVo.getName());
//                res.setType(IAuthorityConstants.RESOURCE_TYPE_BUTTON);
//                res.setValue(auVo.getField_name());
//                returnList.add(res);
//            }
//        }
//        return returnList;
//    }

    /*
     * （非 Javadoc）
     * 
     * @see udp.common.authority.service.IAuthorization#registerResources(java.util.List)
     */
//    public void registerResources(List resources) {
//        IAuResourceBs resourceBs = (IAuResourceBs) Helper
//        .getBean(IAuResourceConstants.BS_KEY);
//        for(int i=0;i<resources.size();i++){
//            Resource rs = (Resource)resources.get(i);
//            AuResourceVo vo = new AuResourceVo();
//            vo.setName(rs.getName());
//            vo.setValue(rs.getValue());
//            vo.setAccess_type("1");
//            vo.setCreate_date(DateTools.getSysTimestamp());
//            vo.setEnable_status("1");
//            if(rs.getType().equals(IAuthorityConstants.RESOURCE_TYPE_BUTTON)){
//                vo.setResource_type(GlobalConstants.getResType_butn());
//            }else if(rs.getType().equals(IAuthorityConstants.RESOURCE_TYPE_MENU)){
//                vo.setResource_type(GlobalConstants.getResType_menu());
//            }else if(rs.getType().equals(IAuthorityConstants.RESOURCE_TYPE_COLUMN)){
//                vo.setResource_type(GlobalConstants.getResType_fild());
//                vo.setTable_name(rs.getGroupName());
//            }else if(rs.getType().equals(IAuthorityConstants.RESOURCE_TYPE_RECORD)){
//                vo.setResource_type(GlobalConstants.getResType_recd());
//                vo.setTable_name(rs.getGroupName());
//                vo.setField_name("N/A");
//            }else if(rs.getType().equals("ORGS")){
//                vo.setResource_type(GlobalConstants.getResType_orga());
//            }
//            resourceBs.insert(vo);
//        }
//    }

    /* (non-Javadoc)
     * @see udp.common.authority.service.IAuthorization#fetchUser(javax.servlet.http.HttpServletRequest)
     */
//    public Party fetchUser(HttpServletRequest request) {
//        if (request == null) //没有登录系统时，返回null
//            return null;
//        Party party = new Party();
//        if(LoginHelper.getIsAdmin(request)){
//            LoginSessionVo sessionVo = LoginHelper.getLoginVo(request);
//            party.setId(sessionVo.getParty_id());
//            party.setName(sessionVo.getName());
//            return party;//admin没有关系，返回partyid
//        }
//        LoginSessionVo sessionVo = LoginHelper.getLoginVo(request);
//        if (sessionVo == null)
//            return null;
//        String currentCode = sessionVo.getCurrent_code();
//        if (currentCode != null){ //当用户登录系统后，且不需要选择机构关系时，返回当前partyId
//            party.setCode(currentCode);
//        }
//        party.setId(sessionVo.getParty_id());
//        party.setName(sessionVo.getName());
//        party.setType(GlobalConstants.getAuPartyTypePeop());
//        List upRelationVos = OrgHelper.getParentRelation(currentCode);
//        Party leafParty = party;
//        Party currentParty = party;
//        for(int i=0;i<upRelationVos.size();i++){
//            Party upParty = initUpParty((AuPartyRelationVo)upRelationVos.get(i));
//            currentParty.setParent(upParty);
//            currentParty = upParty;
//        }
//        return leafParty;
//    }
    
//    private Party initUpParty(AuPartyRelationVo upRelationVo){
//        Party party = new Party();
//        party.setId(upRelationVo.getPartyid());
//        party.setName(upRelationVo.getName());
//        party.setType(upRelationVo.getPartytype_id());
//        party.setCode(upRelationVo.getCode());
//        return party;
//    }

    /* (non-Javadoc)
     * @see udp.common.authority.service.IAuthorization#findIdByNameAndPass(java.lang.String, java.lang.String)
     */
    public String findIdByNameAndPass(String name, String pass) {
        if(1==LoginHelper.validate(name, pass)){//用户身份校验
            IAuUserBs bs = (IAuUserBs) Helper.getBean(IAuUserConstants.BS_KEY);
            List lResult = bs.queryByCondition("login_id='" + name + "'");
            if (lResult != null && lResult.size() > 0) {
                AuUserVo userVo = (AuUserVo)lResult.get(0);
                return userVo.getParty_id();
            }
            return null;
        }
        return null;
    }

    /* (non-Javadoc)
     * @see udp.common.authority.service.IAuthorization#fetchUserKey(javax.servlet.http.HttpServletRequest)
     */
    public String fetchUserKey(HttpServletRequest request) {
        String code = LoginHelper.getLoginVo(request).getCurrent_code();
        if(null==code)
            return LoginHelper.getPartyId(request);
        return code;
    }

}