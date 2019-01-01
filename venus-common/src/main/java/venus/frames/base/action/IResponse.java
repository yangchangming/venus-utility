//Source file: D:\\venus_view\\Tech_Department\\Platform\\Venus\\4项目开发\\1工作区\\4实现\\venus\\frames\\mainframe\\base\\action\\IResponse.java

package venus.frames.base.action;

import venus.frames.base.IGlobalsKeys;

import javax.servlet.ServletResponse;


/**
 * 程序处理完逻辑后返回的响应对象的接口约定
 * 
 * @author 岳国云
 */
public interface IResponse extends IGlobalsKeys
{
   
   /**
    * 放回该响应对象转向的目标页面
    * @return venus.frames.mainframe.base.action.IForward - 该响应对象转向的目标页面
    * @roseuid 3F83C3CB02BE
    */
   public IForward getForward();
   
   /**
    * 返回该响应对象所包装的 ServletResponse
    * @return ServletResponse - 该响应对象所包装的 ServletResponse
    * @roseuid 3F83C70201C4
    */
   public ServletResponse getServletResponse();
   
  
}
