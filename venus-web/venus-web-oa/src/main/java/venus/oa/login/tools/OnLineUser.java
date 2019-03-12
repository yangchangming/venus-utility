package venus.oa.login.tools;

import venus.oa.loginlog.bs.ILoginLogBs;
import venus.oa.loginlog.vo.LoginLogVo;
import venus.oa.util.DateTools;
import venus.springsupport.BeanFactoryHelper;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import java.util.*;

public class OnLineUser implements HttpSessionBindingListener { 
    
    private static Map users=Collections.synchronizedMap(new HashMap());
    //private static CacheFactory users;
    
    private static ILoginLogBs getBs() {
        return (ILoginLogBs) BeanFactoryHelper.getBean("loginLogBs");  //得到BS对象,受事务控制
    }
    
    /**
     * 通过session删除在线用户信息
     * 
     * @return
     */
    private static void deleteOnlineUserBySessionId(String session_id){
        if(session_id!=null){
            users.remove(session_id);
        }
    }
    
    /**
     * 记录退出时间
     * 
     * @param id
     * @return
     */
    private static int recordLogoutTime(String id){
    	LoginLogVo vo = (LoginLogVo)getBs().find(id);
        vo.setLogout_time(DateTools.getSysTimestamp());
        return getBs().update(vo);
    }
    
    /**
     * 退出系统
     * 
     * @param session_id
     * @return
     */
    public static void logout(String session_id) {
        OnlineUserVo vo  = (OnlineUserVo)users.get(session_id);
//        LogMgr.getLogger("venus.authority.login.tools.OnLineUser").info("valueUnbound OnlineUserVO:" + vo);
        recordLogoutTime(vo.getId());//登记退出时间
        deleteOnlineUserBySessionId(session_id);//删除在线用户信息
    }
    
    /**
     * 根据session id判断是否是新的登录
     * @param session_id
     * @return
     */
    public static boolean isNewLogin(String session_id) {
    	return ! users.containsKey(session_id);
    }
    
    /**
     * 根据session id 强制退出系统
     * @param session_id
     * @return
     */
    public static void forceOffline(String session_id) {
    	 if(session_id != null){
    	 	OnlineUserVo vo = (OnlineUserVo)users.get(session_id);
    	 	if (vo != null) {
        	 	HttpSession userSession = vo.getUserSession();
        	 	userSession.invalidate();
    	 	}
        }
    }
    
    /**
     * 将监听器置入session后的响应
     * 
     */
    public void valueBound(HttpSessionBindingEvent e) {        
        HttpSession session = e.getSession();
        OnlineUserVo vo = (OnlineUserVo)session.getAttribute("OnlineUserVo");
//        LogMgr.getLogger("venus.authority.login.tools.OnLineUser").info("valueBound OnlineUserVO:" + vo);
        String session_id = vo.getSession_id();        
        if(isNewLogin(session_id)){//只有新的session_id才能存入map中
            getBs().insert(vo);//插入新的登录日志
            users.put(session_id, vo);//添加在线用户
        }
    } 
    
    /**
     * session失效时的响应
     */
    public void valueUnbound(HttpSessionBindingEvent e) { 
    	if  ( e.getValue() == null )
    		return;
        HttpSession session = e.getSession();
        String session_id = session.getId();
        logout(session_id);//退出系统
    } 

    /**
     * 获得在线用户人数
     * 
     * @return 在线用户人数
     */
    public static int getOnlineUserCount(){  
        return users.size();
    }
    /**
     * 根据登录账号查询在线用户列表
     * @param login_id
     * @return
     */
    public static List queryOnlineUserListByLoginId(String login_id) {
    	
    	List onlineUserList = new ArrayList();
    	if(users!=null){
         	Iterator it = users.values().iterator();
         	while (it.hasNext()) {
         		OnlineUserVo vo = (OnlineUserVo) it.next();
         		if( vo.getLogin_id().indexOf(login_id) != -1 ){
                 	onlineUserList.add(vo);
                 }
             }
         }
         return onlineUserList;
    }
    /**
     * 根据用户姓名查询在线用户列表
     * @param name
     * @return
     */
    public static List queryOnlineUserListByName(String name) {
    	
    	List onlineUserList = new ArrayList();
    	if(users!=null){
         	Iterator it = users.values().iterator();
         	while (it.hasNext()) {
         		OnlineUserVo vo = (OnlineUserVo) it.next();
         		if( vo.getName().indexOf(name) != -1 ){
                 	onlineUserList.add(vo);
                 }
             }
         }
         return onlineUserList;
    }
    /**
     * 查询全部在线用户列表
     * @return
     */
    public static List queryOnlineUserList() {
    	
    	List onlineUserList = new ArrayList();
    	if(users!=null){
         	Iterator it = users.values().iterator();
         	while (it.hasNext()) {
         		OnlineUserVo vo = (OnlineUserVo) it.next();
         		onlineUserList.add(vo);
             }
         }
         return onlineUserList;
    }
    /**
     * 根据登录账号或用户姓名查询在线用户列表
     * @param login_id
     * @param name
     * @return
     */
    public static List queryOnlineUserList(String login_id, String name) {
    	
    	List onlineUserList = new ArrayList();
    	if(users!=null){
         	Iterator it = users.values().iterator();
         	while (it.hasNext()) {
         		OnlineUserVo vo = (OnlineUserVo) it.next();
         		if( (vo.getLogin_id().indexOf(login_id) != -1) && (vo.getName().indexOf(name) != -1) ) {
                 	onlineUserList.add(vo);
                 }
             }
         }
         return onlineUserList;
    }
    
} 



