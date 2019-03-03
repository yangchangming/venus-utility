package venus.oa.authority.auuser.bs.impl;

import org.springframework.stereotype.Service;
import venus.oa.authority.auuser.adaptor.InitLoginAndPwd;
import venus.oa.authority.auuser.bs.IAuUserBs;
import venus.oa.authority.auuser.dao.IAuUserDao;
import venus.oa.authority.auuser.util.IAuUserConstants;
import venus.oa.authority.auuser.vo.AuUserVo;
import venus.oa.helper.OrgHelper;
import venus.oa.organization.auparty.bs.IAuPartyBs;
import venus.oa.organization.auparty.util.IConstants;
import venus.oa.organization.auparty.vo.PartyVo;
import venus.oa.profile.model.UserProfileModel;
import venus.oa.sysparam.vo.SysParamVo;
import venus.oa.util.DateTools;
import venus.oa.util.Encode;
import venus.oa.util.GlobalConstants;
import venus.oa.util.tree.DeepTreeSearch;
import venus.commons.xmlenum.EnumRepository;
import venus.commons.xmlenum.EnumValueMap;
import venus.frames.base.bs.BaseBusinessService;
import venus.frames.base.exception.BaseApplicationException;
//import venus.frames.mainframe.log.ILog;
//import venus.frames.mainframe.log.LogMgr;
import venus.frames.mainframe.util.Helper;
import venus.pub.lang.OID;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuUserBs extends BaseBusinessService implements IAuUserBs, IAuUserConstants {

//    private static ILog log = LogMgr.getLogger(AuUserBs.class);




    /**
     * dao 表示: 数据访问层的实例
     */
    private IAuUserDao dao = null;

    /**
     * 设置数据访问接口
     * 
     * @return
     */
    public IAuUserDao getDao() {
        return dao;
    }

    /**
     * 获取数据访问接口
     * 
     * @param dao
     */
    public void setDao(IAuUserDao dao) {
        this.dao = dao;
    }

    /**
     * 
     * 功能: 获取portal帐户信息
     *
     * @param portalUserId
     * @return
     *
    public UserMappingEntry getPortalUserMap(String portalUserId) {
        try{
            String thisAppXmlPath = PathMgr.getSingleton().getRealPath("WEB-INF/conf/applicationContext-remote/remote-applicationContext.xml");
            if (thisAppXmlPath.startsWith("/")) {
            	thisAppXmlPath = "file://" + thisAppXmlPath;
            }
            ApplicationContext context = new FileSystemXmlApplicationContext(thisAppXmlPath);
            AuthService authService = (AuthService) context.getBean("authServiceProxy");
            return authService.getEntry(portalUserId, com.ext.portlet.util.Constants.USERMAPPING_APP_PMIS);
        }catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 
     * 功能: 同步portal帐户信息
     *
    private void updatePortalUser(String old_login_id, String new_login_id, String password) {
        try{
            String thisAppXmlPath = PathMgr.getSingleton().getRealPath("WEB-INF/conf/applicationContext-remote/remote-applicationContext.xml");
            if (thisAppXmlPath.startsWith("/")) {
            	thisAppXmlPath = "file://" + thisAppXmlPath;
            }
            ApplicationContext context = new FileSystemXmlApplicationContext(thisAppXmlPath);
            AuthService authService = (AuthService) context.getBean("authServiceProxy");
            authService.updateEntry(old_login_id, new_login_id, password, com.ext.portlet.util.Constants.USERMAPPING_APP_PMIS);
        }catch(Exception e) {
            e.printStackTrace();
            //throw new BaseApplicationException("同步portal帐户信息时发生异常");
        }
    }

    /**
     * 插入单条记录
     * 
     * @param vo 用于添加的VO对象
     * @return 若添加成功，返回新生成的Oid
     */
    public OID insert(AuUserVo vo) {
    	vo.setRetire_date(DateTools.getRetireDate());//设置过期时间
    	vo.setFailed_times(new Integer(0));
		OID oid = getDao().insert(vo);
        //RmLogHelper.log(TABLE_LOG_TYPE_NAME, "插入了1条记录,id=" + String.valueOf(oid));
		return oid;
    }
    
    private String[] initLoginIdAndPwd(AuUserVo vo){        
        return ((InitLoginAndPwd) Helper.getBean("loginIdAndPwd")).customLoginAndPwd(vo.getParty_id(), vo.getName());
    }
    
    /**
     * 插入多条记录
     * @param codes
     * @return
     */
    public OID[] insertMulti(String[] codes) {
	if (codes == null || codes.length < 1)
	    return null;
	List list = new ArrayList();
	for (int i = 0; i < codes.length; i++) {
	    list.addAll(queryNoAccountUser(codes[i]));
	}
	OID[] oid = new OID[list.size()];
	for (int j = 0; j < list.size(); j++) {
	    AuUserVo vo = (AuUserVo)list.get(j);
	    vo.setParty_id(vo.getParty_id());
	    vo.setName(vo.getName());
	    String loginidAndPwd[] = initLoginIdAndPwd(vo);
        vo.setLogin_id(loginidAndPwd[0]);
        vo.setPassword(loginidAndPwd[1]);
        vo.setIs_admin("0");
        vo.setAgent_status("0");
        vo.setEnable_status("1");
        vo.setCreate_date(DateTools.getSysTimestamp());
        oid[j] = insert(vo);
	}
	return oid;
    }
    
    /**
     * 插入单条记录，同时插入party表一条记录
     * 
     * @param vo 用于添加的VO对象
     * @return 若添加成功，返回新生成的Oid
     */
    public OID insert4party(AuUserVo vo) {
    	
    	//添加新的团体
        PartyVo partyVo = new PartyVo();
        partyVo.setPartytype_id(GlobalConstants.getPartyType_empl());//团体类型表的主键ID
        partyVo.setName(vo.getName());//团体名称
        partyVo.setEmail("");//团体EMAIL（对员工类型为必填）	
        partyVo.setRemark("");//备注	
        partyVo.setEnable_status("1");//启用/禁用状态	0禁用,1启用
        partyVo.setCreate_date(vo.getCreate_date());//创建时间
        String partyId = OrgHelper.addParty(partyVo);//调用接口添加团体
        
        //添加团体关系，把用户关联到角色的根节点之下
        String relType = GlobalConstants.getRelaType_role();//团体关系类型－行政关系
        String parentRelId = GlobalConstants.getRelaID_role();//角色关系根节点ID
        if(parentRelId!=null && !"".equals(parentRelId) && !"null".equals(parentRelId)) {
        	OrgHelper.addPartyRelation(partyId, parentRelId, relType);//调用接口添加团体关系
        }
        
        //添加账号
        vo.setParty_id(partyId);
        vo.setRetire_date(DateTools.getRetireDate());//设置过期时间
        vo.setFailed_times(new Integer(0));
		OID oid = getDao().insert(vo);
        //RmLogHelper.log(TABLE_LOG_TYPE_NAME, "插入了1条记录,id=" + String.valueOf(oid));
		return oid;
    }

    /**
     * 删除单条记录
     * 
     * @param id 用于删除的记录的id
     * @return 成功删除的记录数
     */
    public int delete(String id) {
    	return getDao().delete(id);  
    }

    /**
     * 删除多条记录
     * 
     * @param id 用于删除的记录的id
     * @return 成功删除的记录数
     */
    public int delete(String id[]) {
        return getDao().delete(id);
//          RmLogHelper.log(TABLE_LOG_TYPE_NAME, "删除了" + sum + "条记录,id=" + String.valueOf(id));
    }

    /**
     * 根据Id进行查询
     * 
     * @param id 用于查找的id
     * @return 查询到的VO对象
     */
    public AuUserVo find(String id) {
		AuUserVo vo = getDao().find(id);
        //RmLogHelper.log(TABLE_LOG_TYPE_NAME, "察看了1条记录,id=" + id);
		return vo;
    }

    /**
     * 重置密码
     * @param id
     * @return
     */
    public int resetPassword(String id) {
    	AuUserVo vo = getDao().find(id);
    	vo.setPassword(vo.getLogin_id());//设置密码＝登陆帐号
    	vo.setRetire_date(DateTools.getRetireDate());//设置过期时间
    	vo.setFailed_times(new Integer(0));
    	//this.updatePortalUser(vo.getLogin_id(), vo.getLogin_id(), vo.getPassword());//更新portal账号
    	return getDao().update(vo);//更新au_user表
        //RmLogHelper.log(TABLE_LOG_TYPE_NAME, "更新了" + sum + "条记录,id=" + String.valueOf(vo.getId()));
    }
    /**
     * 启用
     * @param id
     * @return
     */
    public int enable(String id) {
    	AuUserVo vo = getDao().find(id);
    	IAuPartyBs partyBs = (IAuPartyBs) Helper.getBean(IConstants.BS_KEY);
        PartyVo partyVo= (PartyVo) partyBs.find(vo.getParty_id());
        if("0".equals(partyVo.getEnable_status())){
            throw new BaseApplicationException(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.The_group_is_disabled_group_"));
        }
    	vo.setEnable_status("1");
    	vo.setFailed_times(new Integer(0));
    	vo.setRetire_date(DateTools.getRetireDate());//设置过期时间
    	return getDao().update(vo);//更新au_user表
    }
    /**
     * 禁用
     * @param id
     * @return
     */
    public int disable(String id) {
        AuUserVo vo = getDao().find(id);
        vo.setEnable_status("0");
		return getDao().update(vo);//更新au_user表
    }
    /**
     * 更新单条记录
     * 
     * @param vo 用于更新的VO对象
     * @return 成功更新的记录数
     */
    public int update(AuUserVo vo) {
        AuUserVo oldVo = getDao().find(vo.getId());
        //this.updatePortalUser(oldVo.getLogin_id(), vo.getLogin_id(), vo.getPassword());//更新portal账号
        //如果新旧密码不同，则认为密码被修改
        if(!oldVo.getPassword().equals(vo.getPassword())){
            //如密码被修改，则在用户概要配置中记录修改次数。
            EnumRepository er = EnumRepository.getInstance();
            er.loadFromDir();
            EnumValueMap pwdtimes = er.getEnumValueMap("Au_UserProfile");
            UserProfileModel profile = new UserProfileModel(vo.getParty_id());
            profile.updateProfile(pwdtimes.getValue("CHANGEPWDTIMES"), String.valueOf(Integer.parseInt(null==profile.snapshotValue(pwdtimes.getValue("CHANGEPWDTIMES"))?"0":profile.snapshotValue(pwdtimes.getValue("CHANGEPWDTIMES")))+1));
        }
    	return getDao().update(vo);//更新au_user表
    }
    
    /**
     * 更新单条记录，同时更新团体表相应的记录
     * 
     * @param vo 用于更新的VO对象
     * @return 成功更新的记录数
     */
    public int update4party(AuUserVo vo) {

    	//修改团体
        PartyVo partyVo = new PartyVo();
        partyVo.setId(vo.getParty_id());//团体主键
        partyVo.setName(vo.getName());//团体名称
        OrgHelper.updateParty(partyVo);//调用接口进行更新
        vo.setRetire_date(DateTools.getRetireDate());//设置过期时间
        vo.setFailed_times(new Integer(0));
		return update(vo);//更新au_user表
    }

    /**
     * 查询所有的VO对象列表，不翻页
     * 
     * @return 查询到的VO列表
     */
    public List queryAll() {
		List lResult = getDao().queryAll();
        //RmLogHelper.log(TABLE_LOG_TYPE_NAME, "查询了多条记录,recordSum=" + lResult.size() + ", cmd=queryAll()");
		return lResult;
    }
    
    /**
     * 查询所有的VO对象列表，不翻页，带排序字符
     * 
     * @param orderStr 排序字符
     * @return 查询到的VO列表
     */
    public List queryAll(String orderStr) {
		List lResult = getDao().queryAll(orderStr);
		//RmLogHelper.log(TABLE_LOG_TYPE_NAME, "查询了多条记录,recordSum=" + lResult.size() + ", cmd=queryAll(" + orderStr + ")");
		return lResult;
    }

    /**
     * 查询所有的VO对象列表，带翻页
     * 
     * @param no 当前页数
     * @param size 每页记录数
     * @return 查询到的VO列表
     */
    public List queryAll(int no, int size) {
		List lResult = getDao().queryAll(no, size);
		//RmLogHelper.log(TABLE_LOG_TYPE_NAME, "查询了多条记录,recordSum=" + lResult.size() + ",cmd=queryAll(" + no + ", " + size + ")");
		return lResult;
    }

    /**
     * 查询所有的VO对象列表，带翻页，带排序字符
     * 
     * @param no 当前页数
     * @param size 每页记录数
     * @param orderStr 排序字符
     * @return 查询到的VO列表
     */
    public List queryAll(int no, int size, String orderStr) {
		List lResult = getDao().queryAll(no, size, orderStr);
		//RmLogHelper.log(TABLE_LOG_TYPE_NAME, "查询了多条记录,recordSum=" + lResult.size() + ", cmd=queryAll(" + no + ", " + size + ", " + orderStr + ")");
		return lResult;
    }

    /**
     * 查询总记录数
     * 
     * @return 总记录数
     */
    public int getRecordCount() {
        int sum = getDao().getRecordCount();
        //RmLogHelper.log(TABLE_LOG_TYPE_NAME, "查询到了总记录数,sum=" + sum);
        return sum;
    }

    /**
     * 查询总记录数，带查询条件
     * 
     * @param queryCondition 查询条件
     * @return 总记录数
     */
    public int getRecordCount(String queryCondition) {
		int sum = getDao().getRecordCount(queryCondition);
		//RmLogHelper.log(TABLE_LOG_TYPE_NAME, "查询到了总记录数,sum=" + sum + ", queryCondition=" + queryCondition);
		return sum;
    }
    
    /**
     * 通过查询条件获得所有的VO对象列表，不带翻页查全部
     *
     * @param queryCondition 查询条件
     * @return 查询到的VO列表
     */
    public List queryByCondition(String queryCondition) {
		List lResult = getDao().queryByCondition(queryCondition);
        //RmLogHelper.log(TABLE_LOG_TYPE_NAME, "按条件查询了多条记录,recordSum=" + lResult.size() + ", queryCondition=" + queryCondition);
		return lResult;
    }
    
    /**
     * 通过查询条件获得所有的VO对象列表，不带翻页查全部，带排序字符
     *
     * @param queryCondition 查询条件
     * @param orderStr 排序字符
     * @return 查询到的VO列表
     */
    public List queryByCondition(String queryCondition, String orderStr) {
		List lResult = getDao().queryByCondition(queryCondition, orderStr);
		//RmLogHelper.log(TABLE_LOG_TYPE_NAME, "按条件查询了多条记录,recordSum=" + lResult.size() + ", queryCondition=" + queryCondition + ", orderStr=" + orderStr);
		
		return lResult;
    }
    
    /**
     * 通过查询条件获得所有的VO对象列表，带翻页
     * 
     * @param no 当前页数
     * @param size 每页记录数
     * @param queryCondition 查询条件
     * @return 查询到的VO列表
     */
    public List queryByCondition(int no, int size, String queryCondition) {
		List lResult = getDao().queryByCondition(no, size, queryCondition);
		//RmLogHelper.log(TABLE_LOG_TYPE_NAME, "按条件查询了多条记录,recordSum=" + lResult.size() + ", no=" + no + ", size=" + size + ", queryCondition=" + queryCondition);
		return lResult;
    }
    
    /**
     * 查询总记录数，带查询条件，可以控制数据权限
     * 
     * @param queryCondition 查询条件
     * @return 总记录数
     */
    public int getRecordCount4Limit(String queryCondition) {
		int sum = getDao().getRecordCount4Limit(queryCondition);
		return sum;
    }
    
    /**
     * 可以控制数据权限的查询
     * 
     * @param no 当前页数
     * @param size 每页记录数
     * @param queryCondition 查询条件
     * @return 查询到的VO列表
     */
    public List queryByCondition4Limit(int no, int size, String queryCondition) {
    	List list = getDao().queryByCondition4Limit(no, size, queryCondition);
    	List result = new ArrayList();
    	AuUserVo userVo = new AuUserVo();
    	SysParamVo organizeTooltip = GlobalConstants.getSysParam(GlobalConstants.ORGANIZETOOLTIP);
        for(int i=0; i<list.size(); i++) {
        	userVo = (AuUserVo)list.get(i);
        	if (organizeTooltip == null) {
        	    userVo.setOwner_org(DeepTreeSearch.getOrgNameById(userVo.getParty_id(), false));
        	}
        	result.add(userVo);
        }    	
		return result;
    }
    
    /**
     * 通过查询条件获得所有的VO对象列表，带翻页，带排序字符
     * 
     * @param no 当前页数
     * @param size 每页记录数
     * @param queryCondition 查询条件
     * @param orderStr 排序字符
     * @return 查询到的VO列表
     */
    public List queryByCondition(int no, int size, String queryCondition, String orderStr) {
		List lResult = getDao().queryByCondition(no, size, queryCondition, orderStr);
		//RmLogHelper.log(TABLE_LOG_TYPE_NAME, "按条件查询了多条记录,recordSum=" + lResult.size() + ", no=" + no + ", size=" + size + ", queryCondition=" + queryCondition + ", orderStr=" + orderStr);
		return lResult;
    }
    
    /**
     * 通过查询条件获得所有的VO对象列表，带翻页，带排序字符
     * 关联了组织机构，可以按公司或部门查
     * @param no
     * @param size
     * @param queryCondition
     * @param orderStr
     * @return
     */
    public List queryByCondition4Org(int no, int size, String queryCondition, String orderStr) {
		List lResult = getDao().queryByCondition4Org(no, size, queryCondition, orderStr);
		//RmLogHelper.log(TABLE_LOG_TYPE_NAME, "按条件查询了多条记录,recordSum=" + lResult.size() + ", no=" + no + ", size=" + size + ", queryCondition=" + queryCondition + ", orderStr=" + orderStr);
		return lResult;
    }
    
    /**
     * 查询总记录数，带查询条件
     * 关联了组织机构，可以按公司或部门查
     * 
     * @param queryCondition 查询条件
     * @return 总记录数
     */
    public int getRecordCount4Org(String queryCondition) {
		int sum = getDao().getRecordCount4Org(queryCondition);
		//RmLogHelper.log(TABLE_LOG_TYPE_NAME, "查询到了总记录数,sum=" + sum + ", queryCondition=" + queryCondition);
		return sum;
    }
    
    /**
     * 根据partyId进行查询
     * 
     * @param partyid 用于查找的partyid
     * @return 查询到的VO对象
     */
    public AuUserVo getByPartyId(String partyid) {
        List lResult = getDao().queryByCondition("PARTY_ID='"+partyid+"'");
        if(lResult==null || lResult.size()==0) {
            return null;
        }
		return (AuUserVo)lResult.get(0);
    }
    
    /**
     * 账号是否存在
     * @param loginId
     * @return
     */
    public boolean hasLoginId(String loginId) {
        int sn = getDao().getRecordCount("LOGIN_ID ='" + loginId + "'");
        return 0!=sn;
    }
    
    /**
     * 获得未创建用户的用户信息列表
     * @param code
     * @return
     */
    public List queryNoAccountUser(String code) {
	return getDao().queryNoAccountUser(code);
    }
    
    /* (non-Javadoc)
     * @see venus.authority.au.auuser.bs.IAuUserBs#authorize(java.lang.String, java.lang.String)
     */
    public int authorize(String loginId, String passWord) {
        AuUserVo userVo = null;
        try{
            userVo = (AuUserVo)getDao().findByLoginId(loginId);
        }catch(Exception e){
//            log.warn("账户"+loginId+"验证失败！");
        }
        if(null==userVo){
            return -2;  //"帐号不存在
        }else{
            if (Encode.encode(passWord).equals(userVo.getPassword()))  {
                if ("1".equals(userVo.getEnable_status()))  {
                    return 1; //校验通过
                } else {
                    return -1; //帐号被禁用
                }
            } else {
                return 0; //"密码有误
            }
        }
    }
}

