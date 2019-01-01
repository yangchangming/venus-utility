package venus.frames.mainframe.action;

import java.beans.PropertyChangeEvent;

/**
 * 用户操作时 Action 更替的事件
 * 
 * 如果用户操作的时操作的业务Action 更替了则抛出该事件
 */
public class ActionChangeEvent extends PropertyChangeEvent 
{
   
   /**
    * 记录当前操作时的 sessionid
    */
   private String m_strSessionId = null;
   
   /**
    * 记录当前操作时的历史操作的状态
    */
   private String m_strOldState = null;
   
   /**
    * 说明 Action 变更
    */
   public static String ACTION_CHG_KEY = "ActionName";
   
   /**
    * 说明 URL路径变更
    */
   public static String PATH_CHG_KEY = "path";
   
   /**
    * 说明为新建PROFILE 的事件
    */
   public static String BUILD_KEY="BUILD_PROFILE";
   
  /**
   * 说明为销毁PROFILE 的事件
   * 
   */
  public static String ERASE_KEY="ERASE_PROFILE";
  
   /**
    * 构造器
    * 
    * @param source  当前事件源
    * @param propertyName  ActionChangeEvent事件类型
    * @param oldValue 历史操作的状态
    * @param newValue 当前Path事件操作
    * @param sessionid  当前用户的sessionid号
    * @param oldstate  历史操作的状态
    * @roseuid 3FAE354502E2
    */
   public ActionChangeEvent(Object source, String propertyName, Object oldValue, Object newValue, String sessionid, String oldstate) 
   {
    super(source,propertyName,oldValue,newValue) ;
    this.setSessionId(sessionid);
    this.setOldState(oldstate);
   }
   
   /**
    * 得到当前操作时的 sessionid  
    * 
    * @return String 当前操作用户的sessionid号
    * @roseuid 3FAE1E330221
    */
   public String getSessionId() 
   {
    return this.m_strSessionId;
   }
   
   /**
    * 设置当前操作时的 sessionid  
    * 
    * @param sid 当前操作用户的sessionid号
    * @roseuid 3FAE1E7E019D
    */
   public void setSessionId(String sid) 
   {
    this.m_strSessionId=sid;
   }
   
   /**
    * 得到当前操作时的历史操作的状态
    * 
    * @return String 当前操作时的历史操作的状态
    * @roseuid 3FAE1EB5003E
    */
   public String getOldState() 
   {
    return this.m_strOldState;
   }
   
   /**
    * 设置当前操作时的历史操作的状态
    * 
    * @param state 当前操作时的历史操作的状态
    * @roseuid 3FAE1EDB0115
    */
   public void setOldState(String state) 
   {
    this.m_strOldState = state ;
   }
}
