package venus.frames.base.action;

import org.apache.struts.action.ActionForward;
import venus.frames.mainframe.action.HttpRequest;


/**
 * URL转向时的目标URL对象的缺省实现对象
 * 
 * 该对象以继承方式包装org.apache.struts.action.ActionForward 
 * 
 * 主要存储URL转向时的目标URL业务组件，
 * 
 *  Action 在处理完业务逻辑后在相应的DefaultMapping对象中查到相应的 
 * 
 * IForward并返回该URL 主控制器将转向该URL并推还给用户
 * 
 * @author 岳国云
 */
public class DefaultForward extends ActionForward implements IForward
{
   
   /**
    * @roseuid 3FAEF4D30242
    */
   public DefaultForward() 
   {
    super();
   }
   
   /**
    * 根据传入实例名，路径名，"是否以跳转URL的方式在浏览器中转向目标URL"进行构建
    * 
    * @param name -  实例名
    * 
    * @param path - 路径名
    * 
    * @param redirect - 转向的路径
    * 
    * 是否以跳转URL的方式 在浏览器中转向目标URL
    * @roseuid 3FACEFB30125
    */
   public DefaultForward(String name, String path, boolean redirect) 
   {
    super(path,redirect);
    
    if( getRedirect() ) setPath( HttpRequest.checkPath( getPath(), HttpRequest.IS_ATTRIBUTE_CONTINUE ) );
	
    super.setName(name);
   }
   
   /**
    * 根据传入被包装的 org.apache.struts.action.ActionForward 对象
    * 
    * 进行构建
    * 
    * @param af - 传入被包装的 org.apache.struts.action.ActionForward 对象
    * @roseuid 3FACEF400058
    */
   public DefaultForward(ActionForward af) 
   {
     super(af.getPath(),af.getRedirect());
     
     if( getRedirect() ) setPath( HttpRequest.checkPath( getPath(), HttpRequest.IS_ATTRIBUTE_CONTINUE ) );
     
     super.setName(af.getName());
   }
   

   
}
