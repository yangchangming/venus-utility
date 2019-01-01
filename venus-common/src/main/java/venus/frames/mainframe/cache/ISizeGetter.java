//Source file: D:\\venus_view\\Tech_Department\\Platform\\Venus\\4项目开发\\1工作区\\4实现\\venus\\frames\\mainframe\\cache\\SizeGetter.java

package venus.frames.mainframe.cache;


/**
 * 获取对象大小的接口
 * 
 * 需要存入CACHE的类必须实现该接口才可存入cache，便于cache的管理
 * 
 * @author 岳国云
 */
public interface ISizeGetter 
{
   
   /**
    * @return int - 对象大小
    * @roseuid 3F8DEDC80076
    */
   public int getSize();
}
