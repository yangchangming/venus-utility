//Source file: D:\\venus_view\\Tech_Department\\Platform\\Venus\\4项目开发\\1工作区\\4实现\\venus\\frames\\mainframe\\log\\Ilogger.java

package venus.frames.mainframe.log;


/**
 * log 提供者，即实现LOG驱动的类所需要提供的接口
 * 
 * @author 岳国云
 */
public interface ILogger extends ILog 
{
   
   /**
    * 设置记录 LOG 的类名
    * @param clsname - 设置记录日志的调用者的名字
    * @roseuid 3F7249B003A9
    */
   public void setClassName(String clsname);
   
   /**
    * 为便于用户使用提供的一个简单静态代理接口
    * 
    * 静态方法得到 LOG 驱动实例
    * @param obj - 记录日志的调用者自身句柄
    * @return venus.frames.mainframe.log.ILog
    * @roseuid 3F7535440128
    */
   public ILog getLogger(Object obj);
   
   /**
    * 得到LOG驱动实现类的名字
    * @return String
    * @roseuid 3F94926703BF
    */
   public String getImplName();

}
