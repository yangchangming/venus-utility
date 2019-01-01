package venus.frames.base.action;

import venus.frames.base.IGlobalsKeys;
import venus.frames.mainframe.context.IContext;
import venus.frames.mainframe.currentlogin.IProfile;

import javax.servlet.ServletRequest;
import java.util.Map;


/**
 * 程序处理的请求对象的接口约定
 * 
 * @author 岳国云
 */
public interface IRequest extends IGlobalsKeys
{
   
   /**
    * 得到该请求对象对应的 Action的名字
    * @return String - 该请求对象对应的 Action的名字
    * @roseuid 3F83C3F103E7
    */
   public String getActionPath();
   
   /**
    * 返回该请求对象对应的配置数据对象
    * @return IMappingCfg - 该请求对象对应的配置数据对象
    * @roseuid 3F83C4000232
    */
   public IMappingCfg getMapping();
   
   /**
    * 返回该请求对象所对应的传入参数的列表
    * @return Map - 该请求对象所对应的传入参数 的列表
    * @roseuid 3F83C41A0213
    */
   public Map getParmsMap();
   
   /**
    * 返回该请求对象对应的个性数据堆对象
    * @return PersistentProfile - 该请求对象对应的个性数据堆对象
    * @roseuid 3F83C45102AF
    */
   public IProfile getCurrentLoginProfile();
   
   /**
    * 返回该请求对象所包装的ServletRequest
    * @return ServletRequest - 该请求对象所包装的ServletRequest
    * @roseuid 3F83C63C03E7
    */
   public ServletRequest getServletRequest();
   
   /**
    * 返回该请求对象所对应的上下文对象
    * @return venus.frames.mainframe.context.IContext - 该请求对象所对应的上下文对象
    * @roseuid 3F862BAE019E
    */
   public IContext getContext();
   
  /**
   * 返回该请求逻辑名pathname对应的页面对象
   * @param pathname - 页面跳转逻辑名
   *
   * @return  IForward - 页面跳转对象
   */
  public IForward findForward(String pathname) ;
  
  /**
   * 返回该请求对象中指定的参数值，如不存在返回空
   * @return name - 键值
   */
  public String getParameter(String name);
  
  public String[] getParameterValues(String name); 
  
  /**
   * 存储该对象的一个属性值
   * @param name - 键值
   * @param value - 属性值
   */
  public void setAttribute(String name, Object value);
  
  /**
   * 返回该请求对象中指定的属性值，如不存在返回空
   * @param name - 键值
   * @return Object - 要返回的属性值
   */
  public Object getAttribute(String name);
  
}
