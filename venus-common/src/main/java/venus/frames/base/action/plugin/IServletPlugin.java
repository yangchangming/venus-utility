package venus.frames.base.action.plugin;

import venus.frames.base.action.DefaultServlet;
import venus.frames.base.action.DefaultServletException;
import venus.frames.base.action.IRequest;
import venus.frames.base.action.IResponse;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 需插入 Servlet 中的插件的标准接口
 * 
 * 通过 web.xml 将这些插件部署进 Servlet
 * 
 * 配置数据可以配在WEB.XML中servlet数据中：
 * <init-param>      
 * <param-name>init-classes</param-name>      
 * <param-value>venus.frames.mainframe.util.InitPathPlugIn</param-value>
 * </init-param>
 * <init-param>      
 * <param-name>service-classes</param-name>      
 * <param-value>venus.frames.mainframe.util.InitPathPlugIn</param-value>
 * </init-param>
 * 
 * @author 岳国云
 */
public interface IServletPlugin 
{
   
   /**
    * 插件初始化方法
    * @param context - 调用该插件的 Servlet 的实例的句柄
    * @throws venus.frames.base.action.DefaultServletException
    * @roseuid 3F41E0420303
    */
   public void init(DefaultServlet servlet) throws DefaultServletException;
   
   /**
    * 在Servlet 接受到一个请求后运行service方法时会同时运行插件的该方法
    * @param servlet - 调用该插件的 Servlet 的实例的句柄
    * @param request - 传入的请求对象
    * @param response - 供传出的响应对象
    * @return void 
    * @throws venus.frames.base.action.DefaultServletException
    * @roseuid 3F41E07F0264
    */
   public void service(DefaultServlet servlet, IRequest request, IResponse response) throws DefaultServletException;
   
   /**
    * 该插件的销毁方法，在Servlet 自销毁时销毁
    * @param servlet - 调用该插件的 Servlet 的实例的句柄
    * @param request - 传入的请求对象
    * @param response - 供传出的响应对象
    * @return void
    * @roseuid 3F41E13901E1
    */
   public void destroy(DefaultServlet servlet);
}
