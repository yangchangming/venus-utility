package venus.frames.mainframe.taskmgr;

/**
 * 线程服务类接口
 * 
 * 
 * @author 岳国云
 */
public interface IService 
{
   
   /**
    * 服务类型：启动型
    */
   public static int STARTUP_SERVICE = 0;
   
   /**
    * 服务类型：后台服务
    */
   public static int DAEMON_SERVICE = 1;
   
   /**
    * 得到服务名称
    * @return String
    * @roseuid 3F93479F03E1
    */
   public String getServiceName();
   
   /**
    * 返回服务类型
    * @return int
    * @roseuid 3F93480503B3
    */
   public int getServiceType();
   
   /**
    * 停止服务
    * @roseuid 3F93492203A7
    */
   public void shutdown();
   
   /**
    * 启动服务
    * @roseuid 3F93493C0202
    */
   public void startup();
}
