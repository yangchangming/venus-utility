package venus.frames.mainframe.context;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Map;

/**
 * 上下文对象的统一接口<br>
 * 
 * 调用者使用该统一接口获取数据
 * 
 * @author 张文韬
 */
public interface IContext extends Map, Serializable 
{
   
   /**
    * 从上下文对象中根据name提取属性对象
    * @param name 要取得的对象对应的属性值
    * @return Object 存放于上下文环境中的对象
    * @roseuid 3F8A0F10031C
    */
   public Object getAttribute(String name);
   
   /**
    * 从上下文对象中根据name删除属性中对象值
    * @param name 待删除对象对应的属性名
    * @roseuid 3F8A0FFD0157
    */
   public void removeAttribute(String name);
   
   /**
    * 在上下文对象中插入属性名和对象
    * @param name 需要往上下文对象里插入的属性名
    * @param object 属性名对应的对象
    * @roseuid 3F8A10240147
    */
   public void setAttribute(String name, Object object);
   
   /**
    * 得到上下文对象中所有属性的名称列表
    * @return Enumeration 上下文对象中所有属性的名称列表；如果存储属性名的容器为空，则返回null
    * @roseuid 3FA0C45F0195
    */
   public Enumeration getAttributeNames();
   
   /**
    * 清除上下文对象中的内容
    * @roseuid 3FA0C77E03A8
    */
   public void clear();
   
   
   public void setContextName(String contextName);
   
   
}
