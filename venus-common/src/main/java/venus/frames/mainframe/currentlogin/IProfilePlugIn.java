package venus.frames.mainframe.currentlogin;

import javax.servlet.http.HttpServletRequest;
import java.util.Hashtable;


/**
 * 用户 Profile 插件接口说明
 * 
 * 主要功能 ：用于规范用户Profile初始化和销毁时的插件运行的接口说明
 * 
 * 用户Profile初始化时运行 createProfile(sessionid,loginer)
 * 
 * 返回 Hashtable，其中记录插件需要插入 profile的数据
 * 
 * 用户Profile销毁时运行eraseProfile(sessionid,loginName)
 * 
 */
public interface IProfilePlugIn 
{
   
   /**
    * 创建该用户的 Profile 时运行该插件的该方法
    * 
    * @param sessionid - 当前连接的sessionid号
    * @param loginName - 当前用户名
    * @param ht - 传入的存储数据的Hashtabel
    * @return Hashtable - 传入的存储数据的Hashtabel
    * @roseuid 3FB48824030C
    */
   public Hashtable onCreateProfileForLoginer(String sessionid, String loginName, Hashtable ht, HttpServletRequest req);
   
   /**
    * 删除该连接的 SessionProfile 时运行该插件的该方法
    * 
    * @param sessionid - 当前连接的sessionid号
    * @param loginName - 当前用户名
    * @return boolean 
    * @roseuid 3FB489290157
    */
   public boolean onEraseSessionProfile(String sessionid, String loginName);
}
