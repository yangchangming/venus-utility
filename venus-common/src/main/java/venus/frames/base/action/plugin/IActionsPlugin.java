//Source file: D:\\venus_view\\Tech_Department\\Platform\\Venus\\4项目开发\\1工作区\\4实现\\venus\\frames\\mainframe\\base\\action\\plugin\\IActionsPlugin.java

package venus.frames.base.action.plugin;

import venus.frames.base.action.*;
import venus.frames.base.bean.DefaultForm;

/**
 * 嵌入分发逻辑控制的插件标准接口起调类为 DefaultDispatchAction 的 service(...)方法
 * 
 * 建议plugin不要返回NULL，由于主调用程序如果发现返回为NULL，便会继续查找可用的PLUGIN运行
 * 
 * 查找顺序为：
 * 
 * 查找配置文件定义的顺序为：
 * "%Action.cmd%":被 DefaultDispatchAction 注册了的某一请求Action的cmd的某一传参。
 * "%Action%":被 DefaultDispatchAction 注册了的某一请求Action。
 * common：通用使用于所有被 DefaultDispatchAction 注册了的请求Action。
 * 
 * 先查[ACTION名].[CMD名]对应的CLASS，再查[ACTION名]对应的CLASS，再查[common]对应的CLASS
 * 
 * 如果该配置数据没有找到，主调用程序便查找getMethod(...)方法得到处理方法，如果没有则返回Error
 * 
 * 2.如果[类名].properties中查找返回NULL，主调用程序则查找
 *   protected HashMap iniMethodNameMap() {
 *       HashMap map = new HashMap();
 *       map.put("helloMap", "myHello");
 *       return map;
 *   }
 * 中定义的映射表，如果找到适当的方法则运行。
 * 
 * @author 岳国云
 */
public interface IActionsPlugin 
{
   
   /**
    * 插件统一运行的主要方法
    * 
    * @param actionMapping - 请求对象对应的配置数据对象
    * @param dataForm - 请求对象对应的数据对象
    * @param rq - IRequest请求对象
    * @param rps - IResponse响应对象
    * @return venus.frames.mainframe.base.action.IForward - 页面转向对象
    * @throws venus.frames.base.action.DefaultServletException
    * @roseuid 3F42C6920130
    */
   public IForward service(IMappingCfg actionMapping, DefaultForm dataForm, IRequest rqs, IResponse rps) throws DefaultServletException;
}
