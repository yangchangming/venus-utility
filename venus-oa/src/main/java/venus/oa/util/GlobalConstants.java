package venus.oa.util;


import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import venus.oa.organization.aupartytype.bs.IAuPartyTypeBS;
import venus.oa.organization.aupartytype.util.IConstants;
import venus.oa.organization.aupartytype.vo.AuPartyTypeVo;
import venus.oa.service.sys.bs.ISysParamsBs;
import venus.oa.service.sys.vo.SysParamVo;
import venus.frames.base.exception.BaseApplicationException;
import venus.frames.mainframe.util.Helper;
import venus.frames.mainframe.util.PathMgr;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author ganshuo  
 * 
 */
public class GlobalConstants {
    
    private static String casFilterUser = "";//portal 账号session名
    private static String portalLoginUrl = "";//portal 登陆url
    private static String portalLogoutUrl = "";//portal 注销url
    
    private final static String auPartyTypePeop = "1";	//团体类型分类——人员
    private final static String auPartyTypeRole = "2";	//团体类型分类——角色
    private final static String auPartyTypeDept = "3";	//团体类型分类——机构
    private final static String auPartyTypeOther = "4";	//团体类型分类——其它
    private final static String sTypeMenu = "0";//资源类型——功能菜单
    private final static String sTypeButn = "2";//资源类型——功能按钮
    private final static String sTypeFild = "3";//资源类型——数据字段
    private final static String sTypeRecd = "4";//资源类型——数据记录
    private final static String sTypeOrga = "5";//资源类型——组织机构
    private final static String auTypeForbid = "0";		//权限类型——拒绝
    private final static String auTypePermit = "1";		//权限类型——允许
    private final static String auTypeAuthorize = "2";	//权限类型——可授权
    private static String rTypeComp = "";//团体关系类型——行政关系
    private static String rTypeRole = "";//团体关系类型——角色关系
    private static String rTypeProxy = "";//团体关系类型——代理关系
    private static String rIDRole = "";//团体关系ID——角色关系根节点ID
    private static String rIDProxy = "";//团体关系ID——代理关系根节点ID
    private static String pTypeEmpl = "";//团体类型——员工
    private static String pTypePosi = "";//团体类型——岗位
    private static String pTypeProxy = "";//团体类型——代理
    private static String pTypeRole = "";//团体类型——角色
    private static String pTypeDept = "";//团体类型——部门
    private static String pTypeComp = "";//团体类型——公司
    private static String linkage = "";//新建团体规则列表联动
    private static String sessionTimeOut = "1200";//session 失效时间，单位是秒
    public final static String RETRYTIMES = "RETRYTIMES";//重试密码次数
    public final static String PWDLIFECYCLE = "PWDLIFECYCLE";//密码有效天数
    public final static String PWDNOFITY = "PWDNOFITY";//提前通知密码过期的天数
    public final static String CHOOSEAUREL = "CHOOSEAUREL";//登录关系选择开关
    public final static String LOGINSTRATEGY = "LOGINSTRATEGY"; //系统登录策略
    public final static String FUNCTREEMODE = "FUNCTREEMODE"; //功能树加载模式
    public final static String ADMIN_PWD_EXPIRED = "ADMIN_PWD_EXPIRED"; //管理员密码是否过期
    public final static String ORGANIZETOOLTIP = "ORGANIZETOOLTIP"; //组织机构树使用提示框显示
    public final static String SESSIONTIMEOUT = "SESSIONTIMEOUT"; //系统session超时时间
    public final static String MULTITAB = "MULTITAB"; //多页签功能
    public final static String FUNCMENUTYPE = "FUNCMENUTYPE"; //功能菜单展现方式
    public final static String DATAPRIV_INHERIT = "DATAPRIV_INHERIT";//数据权限是否继承
    
    /*
     * 历史类型规则
     * XYZ三位中：
     * X位为业务类型位，0表示不关注，1表示业务调级，2表示业务代理；
     * Y位为操作对象类型位，0表示不关注，1表示团体操作，2表示关系操作；
     * Z位为操作动作类型位，0表示不关注，1表示删除，2表示增加，3表示修改；
     * 查询是可以使用like和占位符“_”进行。
     */
    public final static String HISTORY_LOG_ADJUST = "123"; //历史日志记录的操作类型——业务调级
    public final static String HISTORY_LOG_DELETE =  "011"; 	//历史日志记录的操作类型——删除团体
    public final static String HISTORY_LOG_INSERT =  "012"; 	//历史日志记录的操作类型——增加团体
    public final static String HISTORY_LOG_UPDATE =  "013"; 	//历史日志记录的操作类型——修改团体
	public final static String HISTORY_LOG_RELATION =  "014";     //历史日志记录的操作类型——关联人员
    public final static String HISTORY_LOG_INSERT_PROXY_RELATION = "222";  //历史日志记录的操作类型——新增代理关系
    public final static String HISTORY_LOG_DELETE_PROXY_RELATION = "221";  //历史日志记录的操作类型——删除代理关系
    public final static String ORGRELATION_ALL = "ALL"; //显示组织机构所有关系
    
    private static HashMap mPartyType = null;
    private static HashMap mSysParas = null;//系统配置项缓存
    private static boolean hasLoad = false;
    /**
     * 功能: 读取所有系统配置项
     */
    public static void loadSysParas(){
    	ISysParamsBs bs = (ISysParamsBs) Helper.getBean(venus.oa.service.sys.util.IConstants.BS_KEY);
    	List list=bs.queryByCondition(" ENABLE='1' ");
    	if(list!=null || list.size()>0) {
    		mSysParas = new HashMap();
	        for(int i=0; i<list.size(); i++) {
	        	SysParamVo vo = (SysParamVo)list.get(i);
	        	mSysParas.put(vo.getPropertykey(),vo);
	        }
        }
    }
    /**
     * 功能: 读取所有启用状态的团体类型
     */
    public static void loadPartyType() {
        IAuPartyTypeBS bs = (IAuPartyTypeBS) Helper.getBean(IConstants.BS_KEY);
        List list = bs.queryAllEnable(-1,-1,null);
        if(list!=null || list.size()>0) {
            mPartyType = new HashMap();
	        for(int i=0; i<list.size(); i++) {
	            AuPartyTypeVo vo = (AuPartyTypeVo)list.get(i);
	            mPartyType.put(vo.getId(),vo.getKeyword());
	        }
        }
    }
    
    /**
     * 功能: 从xml读取团体关系类型ID
     */
    public static boolean isHasLoad() {
        if (!hasLoad){
            SAXReader reader = new SAXReader();             
            
            Document document;
            try {
                String filename = PathMgr.getSingleton().getRealPath("/WEB-INF/conf/authority.xml");
                
                document = reader.read(new File(filename)); 
            } catch (Exception e) {                
                throw new BaseApplicationException(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Error_reading_configuration_files_")+e.toString());
            }   
            if (document == null){
                throw new BaseApplicationException(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Configuration_files_do_not_exist_"));
            }
            Element root = document.getRootElement();     
            Element constparams = root.element("const");
            rTypeComp = (String) constparams.elementText("rTypeComp"); 
            rTypeRole = (String) constparams.elementText("rTypeRole"); 
            rTypeProxy = (String) constparams.elementText("rTypeProxy"); 
            rIDRole = (String) constparams.elementText("rIDRole"); 
            rIDProxy = (String) constparams.elementText("rIDProxy"); 
            pTypeComp = (String) constparams.elementText("pTypeComp");  
            pTypeDept = (String) constparams.elementText("pTypeDept"); 
            pTypePosi = (String) constparams.elementText("pTypePosi"); 
            linkage = (String) constparams.elementText("linkage");
            pTypeEmpl = (String) constparams.elementText("pTypeEmpl"); 
            pTypeRole = (String) constparams.elementText("pTypeRole");
            pTypeProxy = (String) constparams.elementText("pTypeProxy");
            casFilterUser = (String) constparams.elementText("casFilterUser");
            portalLoginUrl = (String) constparams.elementText("portalLoginUrl");
            portalLogoutUrl = (String) constparams.elementText("portalLogoutUrl");
            sessionTimeOut = (String) constparams.elementText("sessionTimeOut");
            hasLoad = true;
        }
        return hasLoad;
    }
    
	public GlobalConstants() {
	}
	
	/**
	 * 功能: 获取访问者类型——人员
	 */
	public static String getVisiType_empl() {
	    return auPartyTypePeop;
	}
	/**
	 * 功能: 获取访问者类型——角色
	 */
	public static String getVisiType_role() {
		return auPartyTypeRole;
	}
	/**
	 * 功能: 获取访问者类型——机构
	 */
	public static String getVisiType_comp() {
	    return auPartyTypeDept;
	}

	/**
	 * 功能: 根据团体类型ID获取访问者类型
	 */
	public static String getVisiTypeByPartyType(String partyTypeId) {
	    if(mPartyType==null) {
	        loadPartyType();//加载所有团体类型
	    }
	    if(mPartyType.keySet().contains(partyTypeId)) {
	        return (String)mPartyType.get(partyTypeId);
	    }else {
	        //如果找不到，可能是新增加的团体类型，再尝试加载一次
	        loadPartyType();
	        if(mPartyType.keySet().contains(partyTypeId)) {
		        return (String)mPartyType.get(partyTypeId);
	        }
	    }
		return null;
	}
	/**
	 * 
	 * 功能: 根据团体类型ID判断是否人员
	 *
	 * @param partyTypeId
	 * @return
	 */
	public static boolean isPerson(String partyTypeId) {
	    String visitorType = getVisiTypeByPartyType(partyTypeId);
	    if(visitorType!=null && visitorType.equals(auPartyTypePeop))
	        return true;
	    else
	        return false;
	}
	/**
	 * 
	 * 功能: 根据团体类型ID判断是否角色
	 *
	 * @param partyTypeId
	 * @return
	 */
	public static boolean isRole(String partyTypeId) {
	    String visitorType = getVisiTypeByPartyType(partyTypeId);
	    if(visitorType!=null && visitorType.equals(auPartyTypeRole))
	        return true;
	    else
	        return false;
	}
	/**
	 * 
	 * 功能: 根据团体类型ID判断是否机构
	 *
	 * @param partyTypeId
	 * @return
	 */
	public static boolean isDept(String partyTypeId) {
	    String visitorType = getVisiTypeByPartyType(partyTypeId);
	    if(visitorType!=null && visitorType.equals(auPartyTypeDept))
	        return true;
	    else
	        return false;
	}
	
	/**
	 * 功能: 获取资源类型——菜单
	 */
	public static String getResType_menu() {
	    return sTypeMenu;
	}
	
	/**
	 * 功能: 获取资源类型——按钮
	 */
	public static String getResType_butn() {
	    return sTypeButn;
	}
	
	/**
	 * 功能: 获取资源类型——字段
	 */
	public static String getResType_fild() {
	    return sTypeFild;
	}
	
	/**
	 * 功能: 获取资源类型——记录
	 */
	public static String getResType_recd() {
	    return sTypeRecd;
	}
	
	/**
	 * 功能: 获取资源类型——组织
	 */
	public static String getResType_orga() {
	    return sTypeOrga;
	}

    /**
     * 功能: 获取团体类型的分类——机构（注意：这不是团体类型PartyType）
     * @return 返回 auPartyTypeDept。
     */
    public static String getAuPartyTypeDept() {
        return auPartyTypeDept;
    }
    /**
     * 功功能: 获取团体类型的分类——其它（注意：这不是团体类型PartyType）
     * @return 返回 auPartyTypeOther。
     */
    public static String getAuPartyTypeOther() {
        return auPartyTypeOther;
    }
    /**
     * 功能: 获取团体类型的分类——人员（注意：这不是团体类型PartyType）
     * @return 返回 auPartyTypePeop。
     */
    public static String getAuPartyTypePeop() {
        return auPartyTypePeop;
    }
    /**
     * 功能: 获取团体类型的分类——角色（注意：这不是团体类型PartyType）
     * @return 返回 auPartyTypeRole。
     */
    public static String getAuPartyTypeRole() {
        return auPartyTypeRole;
    }
    /**
     * 功能: 获取权限类型——拒绝
     * @return
     */
    public static String getAuTypeForbid() {
        return auTypeForbid;
    }
    /**
     * 功能: 获取权限类型——允许
     * @return
     */
    public static String getAuTypePermit() {
        return auTypePermit;
    }
    /**
     * 功能: 获取权限类型——可授权
     * @return
     */
    public static String getAuTypeAuthorize() {
        return auTypeAuthorize;
    }
	/**
	 * 功能: 获取团体关系类型——角色关系
	 */
	public static String getRelaType_role() {
	    isHasLoad();
	    return rTypeRole;
	}
	/**
     * 功能: 获取团体关系类型——代理关系
     */
    public static String getRelaType_proxy() {
        isHasLoad();
        return rTypeProxy;
    }
	/**
	 * 功能: 获取团体关系ID——角色关系根节点ID
	 */
	public static String getRelaID_role() {
	    isHasLoad();
	    return rIDRole;
	}
	/**
     * 功能: 获取团体关系ID——代理关系根节点ID
     */
    public static String getRelaID_proxy() {
        isHasLoad();
        return rIDProxy;
    }
    /**
	 * 功能: 获取团体关系类型——行政关系
	 */
	public static String getRelaType_comp() {
	    isHasLoad();
	    return rTypeComp;
	}
	/**
	 * 功能: 获取portal 账号session名
	 */
	public static String getCasFilterUser() {
	    isHasLoad();
	    return casFilterUser;
	}
	
	/**
	 * 功能: 获取portal登陆url
	 */
	public static String getPortalLoginUrl() {
	    isHasLoad();
	    return portalLoginUrl;
	}
	
	/**
	 * 功能: 获取portal注销url
	 */
	public static String getPortalLogoutUrl() {
	    isHasLoad();
	    return portalLogoutUrl;
	}
	
	/**
	 * 功能: 获取团体类型——公司
	 */
	public static String getPartyType_comp() {
	    isHasLoad();
	    return pTypeComp;
	}
	
	/**
	 * 功能: 获取团体类型——部门
	 */
	public static String getPartyType_dept() {
	    isHasLoad();
	    return pTypeDept;
	}
	
	/**
	 * 功能: 获取团体类型——角色
	 */
	public static String getPartyType_role() {
	    isHasLoad();
	    return pTypeRole;
	}
	
	/**
     * 功能: 获取团体类型——代理
     */
    public static String getPartyType_proxy() {
        isHasLoad();
        return pTypeProxy;
    }
    
	/**
	 * 功能: 获取团体类型——员工
	 */
	public static String getPartyType_empl() {
	    isHasLoad();
	    return pTypeEmpl;
	}
	
	/**
	 * 功能: 获取团体类型——岗位
	 */
	public static String getPartyType_posi() {
	    isHasLoad();
	    return pTypePosi;
	}

	public static String getLinkage() {
	    isHasLoad();
	    return linkage;
	}
	public static int getSessionTimeOut() {
	    isHasLoad();
	    SysParamVo sysParamVo = getSysParam(GlobalConstants.SESSIONTIMEOUT);
	    if (sysParamVo == null)
	        return Integer.parseInt(sessionTimeOut);
	    if ("0".equals(sysParamVo.getValue()) || !Pattern.compile("[0-9]*").matcher(sysParamVo.getValue()).matches())
	        return Integer.parseInt(sessionTimeOut);
	    return Integer.parseInt(sysParamVo.getValue());
	}
    /**
     * 通过键值获取系统配置项
     * @param key 键值
     * @return 系统配置项Vo
     */
    public static SysParamVo getSysParam(String key){
    	if(null == mSysParas){
    		loadSysParas();
    	}
    	return  (SysParamVo) mSysParas.get(key);
    }
    public static void main(String[] args) {
        
    }
}

