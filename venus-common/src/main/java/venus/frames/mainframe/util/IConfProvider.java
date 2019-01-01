package venus.frames.mainframe.util;

import org.w3c.dom.Node;

import java.io.Serializable;

/**
 * 配置解析器接口<br>
 * 
 * 若不用系统提供的 缺省配置解析器 可以自己编写代码实现该接口
 * 
 * @author 张文韬
 */
public interface IConfProvider extends Serializable 
{
   
   
   /**
    * 从类实例对象中获取该节点配置数据，而不是从配置文件中
    * @return Node 该配置数据的节点对象
    * @roseuid 3F8E4AD4027B
    */
   public Node getNode(String tag, Object obj);

}
