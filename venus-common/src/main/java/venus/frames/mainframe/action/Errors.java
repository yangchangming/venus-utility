package venus.frames.mainframe.action;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import venus.frames.base.IGlobalsKeys;
import venus.frames.base.action.BaseActionError;

import java.util.Iterator;

/**
 * 返回供前端显示的错误对象列表
 * 
 * @author 岳国云
 */
public class Errors extends ActionErrors implements IGlobalsKeys
{
   
   /**
    * 缺省错误标识名
    */
   public static String DEFAULT_ERR_NAME = "DEFAULT_ERR_NAME";
   
   /**
    * 构造器
    * 
    * 根据传入的错误对象构建该列表
    * 
    * @param err - 传入的错误对象
    * @roseuid 3FAE44F40303
    */
   public Errors(BaseActionError err)
   {
    add(DEFAULT_ERR_NAME,err);
   }
   
   /**
    * 缺省构造器
    * 
    * @roseuid 3FAE43EA0315
    */
   public Errors() 
   {
    super();
   }
   
   /**
    * 根据传入的属性值和错误对象构建该列表
    * @param name - 传入的属性值
    * @param err - 传入的错误对象
    * @roseuid 3FAE4B5801A7
    */
   public void add(String name, BaseActionError err)
   {
    super.add(name,(ActionError)err);
   }
   
   /**
    * 清除所有BaseError错误对象
    * @roseuid 3FAE4BAA0033
    */
   public void clear() 
   {
    super.clear();
   }
   
   /**
    * 是否存在错误对象，为空则返回TRUE
    * @return boolean 
    * @roseuid 3FAE4BC102F3
    */
   public boolean isEmpty() 
   {
    return super.isEmpty();
   }
   
   /**
    * 得到所有错误对象集
    * @return Iterator
    * @roseuid 3FAE4BEC0268
    */
   public Iterator get() 
   {
    return super.get();
   }
   
   /**
    * 得到属性name对应的错误对象集
    * @param name
    * @return Iterator
    * @roseuid 3FAE4C0C02FB
    */
   public Iterator get(String name) 
   {
    return super.get(name);
   }
   
   /**
    * 返回所有的错误对象个数
    * @return int
    * @roseuid 3FAE4C3A006C
    */
   public int size() 
   {
    return super.size();
   }
   
   /**
    * 返回属性name对应的错误对象个数
    * @param name - 属性值
    * @return int - 错误对象个数
    * @roseuid 3FAE4C4403B9
    */
   public int size(String name) 
   {
    return super.size(name);
   }
   
   /**
    * struts 的方法不提倡使用，由getNames() 替换
    * 
    * @return Iterator - 本对象的错误信息属性集
    * @roseuid 3FAE4C4D024A
    */
   public Iterator properties() 
   {
   return super.properties();
   }
   
   /**
    * struts 的方法不提倡使用，由isEmpty() 替换
    * 同isEmpty()，返回本对象是否存在错误对象，为空则返回TRUE
    * @return boolean
    * @roseuid 3FAE4CB001F2
    */
/*
   public boolean empty() 
   {
    return super.empty();
   }
*/   
   /**
    * 返回本对象所有的BaseError属性集
    * @return Iterator - BaseError属性集
    * @roseuid 3FAE4D1C00D5
    */
   public Iterator getNames() 
   {
   return super.properties();
   }
}
