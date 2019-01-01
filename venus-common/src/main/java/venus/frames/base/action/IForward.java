package venus.frames.base.action;

import venus.frames.base.IGlobalsKeys;


/**
 * 主要为URL转向时的目标URL对象的接口约定
 * 
 * 业务组件 Action 在处理完业务逻辑后 在 相应的 DefaultMapping对象中查到相应的 
 * IForward 并返回该URL主控制器将转向该URL并推还给用户
 * 
 * @author 岳国云
 * 
 */
public interface IForward extends IGlobalsKeys 
{
   
   /**
    * 得到目标URL的名字
    * @return String - 目标URL的名字
    * @roseuid 3FACF43A02A8
    */
   public String getName();
   
   /**
    * 得到目标URL的路径
    * @return String - 目标URL的路径
    * @roseuid 3FACF4420029
    */
   public String getPath();
   
   /**
    * 得到目标URL的转向是否是 "Redirect"方式
    * @return boolean
    * @roseuid 3FACF4490169
    */
   public boolean getRedirect();
   
   /**
    * 设置目标URL的名字
    * @param name - 目标URL的名字
    * @return Void
    * @roseuid 3FACF45C0348
    */
   public void setName(String name);
   
   /**
    * 设置目标URL的路径
    * @param path - 目标URL的路径
    * @return Void
    * @roseuid 3FACF46E0072
    */
   public void setPath(String path);
   
   /**
    * 设置目标URL的转向是否 是 "Redirect"方式
    * @param redirect - 目标URL的转向是否 是 "Redirect"方式
    * @return Void
    * @roseuid 3FACF47803C0
    */
   public void setRedirect(boolean redirect);
}
