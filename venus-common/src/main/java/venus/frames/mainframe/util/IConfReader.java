package venus.frames.mainframe.util;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.Serializable;

/**
 * 配置解析器接口<br>
 * 
 * 若不用系统提供的 缺省配置解析器 可以自己编写代码实现该接口
 * 
 * @author 张文韬
 */
public interface IConfReader extends Serializable 
{
   
   /**
    * 约定配置在XML文件boolean型的数据的值(是)
    */
   public static String TRUE = "1";
   
   /**
    * 约定配置在XML文件的boolean型的数据的值(否)
    */
   public String FALSE = "0";
   
   /**
    * 根据输入的键值strTagName获取节点相应的属性值
    * @param strTagName 节点中的某一个属性名
    * @return String 对应于该属性的值
    * @roseuid 3F8E454701D8
    */
   public String readStringAttribute(String strTagName);

   /**
    * 返回该节点项的名称
    * @return String 返回该节点项的名称
    * @roseuid 3F8E4A4F0327
    */
   public String getTagName();
   
   /**
    * 获取该节点下的所有节点链
    * @return NodeList 该节点下的所有节点链
    * @roseuid 3F8E4AD4027B
    */
   public NodeList readChildNodeList();
   
   /**
    * 将传入的节点对象暂存供以后处理用
    * @param xmlObj 待处理的节点
    * @roseuid 3F93420F0303
    */
   public void fromXml(Node xmlObj);
   
   /**
    * 根据键值strKeyName得到该属性对应的int型的值<br>
	* 这里仅是简单的对readStringAttribute(..)的封装，将得到的String的值转成int
	* @param strKeyName 节点的属性名称
	* @return 节点属性值的数字表示形式，如果读取过程中出现异常则返回-1
    */
   public int readIntAttribute(String strKeyName) ;
   
   /**
    * 根据属性名strKeyName得到该属性对应的boolean型值<br>
	* 这里仅是简单的对readStringAttribute(..)的封装，将得到的String的值转成boolean
	* @param strKeyName 属性名
	* @return 该属性名对应的boolean值 
    */
   public boolean readBooleanAttribute(String strKeyName) ;
   
   /**
     * 根据childTag解析m_xmlNode获取相应的值，取得名称为childTag的所有子节点<br>
	 * childTag要取的是节点中的子节点名<br>
	 * 执行XML标准解析过程<br>
	 * 注：如果childTag为null默认为是取所有子节点的列表。
	 * @param childTag - 要获取的子节点标记
	 * @return 名称为参数值定名称的子节点集，如果发生异常则返回null 
    */
   public java.util.ArrayList readChildNodesAry(String childTag) ;
   
   /**
     * 得到childTag子节点组中属性名为attributeTag的属性值数组<br>
	 * 标准的XML解析过程
	 * @param childTag 要获取的子节点标记
	 * @param attributeTag 属性名称
	 * @return 相应的属性值数组，如果读取过程中发生异常则返回null 
    */
   public String[] readChildStringAry(String childTag, String attributeTag) ;  
}
