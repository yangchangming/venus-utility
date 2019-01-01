package venus.frames.base.action;

import venus.frames.base.IGlobalsKeys;


/**
 * 用于存储该业务组件Action的配置数据的对象接口约定
 * 
 * @author 岳国云
 */
public interface IMappingCfg extends IGlobalsKeys 
{
   
   /**
    * 得到配置中的该Action配置数据中传入的参数
    * @return String - 该Action配置数据中传入的参数
    * @roseuid 3FACF64B035D
    */
   public String getParameter();
   
   /**
    * 在该配置对象中根据名字查到目标URL
    * @param name - 配置对象中的逻辑名字
    * @return venus.frames.mainframe.base.action.IForward - 目标URL对象
    * @roseuid 3FACF651009F
    */
   public IForward findForward(String name);
   
   /**
    * 返回该 Action 组件对输入的表单数据是否需要校验
    * @return boolean
    * @roseuid 3FACF781031D
    */
   public boolean getValidate();
   
   /**
    * 返回该 Action 组件配置中name 参数的值
    * @return String - 该 Action 组件配置中name 参数的值
    * @roseuid 3FACF79A034B
    */
   public String getFormName();
   
   /**
    * 返回该 Action 组件对应的路径名
    * @return String - 该 Action 组件对应的路径名
    * @roseuid 3FACF7A40381
    */
   public String getPath();
   
   /**
    * 返回该 Action 组件有效生命周期范围
    * @return String - 该 Action 组件有效生命周期范围
    * @roseuid 3FACF823021C
    */
   public String getScope();
   
   /**
    * 为该 Action 组件配置数据对象中加入新的转向url
    * @param forward - 加入新的转向url
    * @return void
    * @roseuid 3FACF8B001BA
    */
//   public void addForward(IForward forward);
   
   /**
    * 返回该 Action 组件对应的缺省INPUT页面的 URL
    * @return String - 该 Action 组件对应的缺省INPUT页面的 URL
    * @roseuid 3FACF8E80305
    */
   public String getInputPage();
    
    /**
    * 返回该 Action 组件对应的缺省INPUT页面的 Forwar
    * @return IForward - 该 Action 组件对应的缺省INPUT页面的 Forwar
    */
   public IForward getInputForward();
      
}
