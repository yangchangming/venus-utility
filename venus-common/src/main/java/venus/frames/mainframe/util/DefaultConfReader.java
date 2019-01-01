package venus.frames.mainframe.util;

import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import venus.frames.mainframe.log.LogMgr;
import venus.pub.util.Encode;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 缺省配置解析器，用于读取XML文件的节点信息
 * 
 * @author 张文韬
 */
public class DefaultConfReader implements IConfReader, Serializable {

	/**
	 * 暂存XMl节点对象，供具体解析用<br>
	 * 该类中大部分的XML解析要用该对象
	 */
	private Node m_xmlNode = null;

	/**
	 * 自定义标记
	 */
	private static String TRUE = "1";
	private static String FALSE = "0";

	/**
	 * @roseuid 3F9F79BB01F6
	 */
	public DefaultConfReader() {
		super();
	}

	/**
	 * 利用传入的节点构造该函数
	 * @param xmlObj - 拾取出的配置文件中关于该项（此处为类名）的节点
	 * @roseuid 3F9346A500E0
	 */
	public DefaultConfReader(Node xmlObj) {
		this.fromXml(xmlObj);
	}

	/**
	 * 根据输入的键值strKeyName解析m_xmlNode获取相应的属性值<br>
	 * 
	 * 键值strKeyName要取的是节点中的属性名称<br>
	 * 
	 * XML标准解析过程
	 * 
	 * @param strKeyName 节点的属性名称
	 * @return 该属性对应的值，如果读取过程中出现异常则返回null
	 * @roseuid 3F8E497A021F
	 */
	public String readStringAttribute(String strKeyName) {
		//处理参数为空
		if (strKeyName == null || strKeyName.trim() == "") {
			LogMgr.getLogger(this).info("DefaultConfReader的readStringAttribute方法中，参数空指针异常！");
			return null;
		}
		NamedNodeMap tmpNodeMap = null;
		Node tmpNode = null;
		try {
			tmpNodeMap = m_xmlNode.getAttributes();
			tmpNode = tmpNodeMap.getNamedItem(strKeyName.trim());
		} catch (NullPointerException e) {
			LogMgr.getLogger(this).info(getTagName()+"在DefaultConfReader的readStringAttribute( "+strKeyName+" )方法中,出现空指针异常！");
			return null;
		} catch (Exception e) {
			LogMgr.getLogger(this).info(getTagName()+"在DefaultConfReader的readStringAttribute( "+strKeyName+" )方法中,不能取得相应的值！",e);
			return null;
		}
		try {
			
			return decodeNodeValue(tmpNode);
		} catch (DOMException excp) {
			if (((DOMException)excp).code == 7) {
				LogMgr.getLogger(this).info(getTagName()+"在DefaultConfReader的readStringAttribute( "+strKeyName+" )方法中,节点为只读类型！",excp);
				return null;
			}
			if (((DOMException)excp).code == 2) {
				LogMgr.getLogger(this).info(getTagName()+"在DefaultConfReader的readStringAttribute( "+strKeyName+" )方法中,字符串超过规定长度！",excp);
				return null;
			}
//			LogMgr.getLogger(this).error("在DefaultConfReader的readStringAttribute方法中,空指针异常！",excp);
			return null;
		} catch (NullPointerException e) {
			LogMgr.getLogger(this).info("在DefaultConfReader的readStringAttribute( "+strKeyName+" )方法中,空指针异常！");
			return null;
		}
	}

	/**
	 * 返回该节点项的名称
	 * @return 该节点项的名称
	 * @roseuid 3F8E4BCA01D2
	 */
	public String getTagName() {
		if (this.m_xmlNode==null) return null;
		
		return this.m_xmlNode.getNodeName();
	}

	/**
	 * 根据键值strKeyName得到该属性对应的int型的值<br>
	 * 这里仅是简单的对readStringAttribute(..)的封装，将得到的String的值转成int
	 * @param strKeyName 节点的属性名称
	 * @return 节点属性值的数字表示形式，如果读取过程中出现异常则返回-1
	 * @roseuid 3F8E4BDC0368
	 */
	public int readIntAttribute(String strKeyName) {
		//处理参数为空
		if (strKeyName == null) {
			LogMgr.getLogger(this).info("在DefaultConfReader的readIntAttribute方法中,参数空指针异常！");
			return -1;
		}
		//处理结果为空的情况
		String re = readStringAttribute(strKeyName);
		if (re == null || re.equals("")) {
			LogMgr.getLogger(this).info(
				getTagName()+"在DefaultConfReader的readIntAttribute方法中,readStringAttribute( "+strKeyName+" )方法所得值为空！");
			return -1;
		}
		//返回结果
		try {
			return Integer.parseInt(re);
		} catch (NumberFormatException e) {
			LogMgr.getLogger(this).info(getTagName()+"在DefaultConfReader的readIntAttribute( "+strKeyName+" )方法中,数据类型转换出错！");
			return -1;
		}
	}

	/**
	 * 标准的XML处理<br>
	 * 获取该节点下的所有节点链
	 * @return 该节点下的所有节点链，如果有异常发生则返回null
	 * @roseuid 3F8E4BE6030A
	 */
	public NodeList readChildNodeList() {
		try {
			return m_xmlNode.getChildNodes();
		} catch (NullPointerException e) {
			LogMgr.getLogger(this).info(getTagName()+"在DefaultConfReaderd的readChildNodeList方法中,节点空指针异常！");
			return null;
		}
	}

	/**
	 * 将传入的节点对象暂存供以后处理用
	 * @param xmlObj - 拾取出的配置文件中关于该项（此处为类名）的节点
	 * @roseuid 3F933E7601A0
	 */
	public void fromXml(Node xmlObj) {
		this.m_xmlNode = xmlObj;
	}

	/**
	 * 得到childTag子节点组中属性名为attributeTag的属性值数组<br>
	 * 标准的XML解析过程
	 * @param childTag 要获取的子节点标记
	 * @param attributeTag 属性名称
	 * @return 相应的属性值数组，如果读取过程中发生异常则返回null
	 * @roseuid 3F950D1901CD
	 */
	public String[] readChildStringAry(String childTag, String attributeTag) {
		//如果childTag为空，默认全部进行处理，故不对childTag进行空指针判断
		if (attributeTag == null) {
			LogMgr.getLogger(this).info("在DefaultConfReader的readChildStringAry方法中,参数空指针异常！");
			return null;
		}
		//取得子节点集
		ArrayList tmpAryLst = readChildNodesAry(childTag);
		if ( null == tmpAryLst )
			return null;
		String[] re = new String[tmpAryLst.size()];
		//读取属性值存放到字符串数组中
		int size = tmpAryLst.size();
		for (int i = 0; i < size; i++) {
			try {
				Node tmpNode = (Node) tmpAryLst.get(i);
				NamedNodeMap tmpNodeMap = tmpNode.getAttributes();
				Node nameNode = tmpNodeMap.getNamedItem(attributeTag);
				

				
				re[i] = decodeNodeValue(nameNode);			
				
				
				
			} catch (NullPointerException e) {				
				re[i] = null;
			}
		}
		return re;
	}
	
	/**
	 * 从xml节点对象中将加密过的节点属性解出来
	 * @param n xml属性节点对象
	 * @return 解出来属性节点的值
	 * @roseuid 3F950D1901CD
	 */
	public static String decodeNodeValue(Node n){
		
		if( n==null) return null;
			
		String strold = n.getNodeValue();
		
		String str = null;
		
		if( strold.startsWith("{DES}") ){
			
			str = Encode.decode(strold.substring(5));
		}else{
			
			str = strold;
		
		}
		
		return str;
	
	}
	

	/**
	 * 根据属性名strKeyName得到boolean型的值<br>
	 * 这里仅是简单的对readStringAttribute(..)的封装，将得到的String的值转成boolean
	 * @param strKeyName 属性名
	 * @return 该属性名对应的boolean值
	 * @roseuid 3F9C88FC033B
	 */
	public boolean readBooleanAttribute(String strKeyName) {
		//直接调用readStringAttribute()方法取得相应节点的值
		//在上述方法中完成异常处理，故此处没有再添加异常处理
		String re = readStringAttribute(strKeyName);
		
		return ConfMgr.getBooleanFromString(re);

	}

	/**
	 * 根据childTag解析m_xmlNode获取相应的值，取得名称为childTag的所有子节点<br>
	 * 
	 * childTag要取的是节点中的子节点名
	 * 
	 * XML标准解析过程
	 * 
	 * 如果childTag 为 null 说明是取所有子节点的列表，不做标记的筛选
	 * @param childTag - 要获取的子节点标记
	 * @return 名称为参数值定名称的子节点集，如果发生异常则返回null
	 * @roseuid 3F9C8E2C035B
	 */
	public ArrayList readChildNodesAry(String childTag) {
		try {
			//如果该节点没有子节点则返回空
			if ( null == m_xmlNode )
				return null;
			if (!m_xmlNode.hasChildNodes())
				return null;
			//取得该节点的所有子节点
			NodeList childList = m_xmlNode.getChildNodes();
			ArrayList tmpAryReList = new ArrayList();
			int length = childList.getLength();
			for (int i = 0; i < length; i++) {
				Node tmpNode = childList.item(i);
				if (tmpNode != null && tmpNode.getNodeType() == Node.ELEMENT_NODE) {
					if (childTag == null || tmpNode.getNodeName().equals(childTag)) {
						tmpAryReList.add(tmpNode);
					}
				}	
			}
			return tmpAryReList;
		} catch (NullPointerException e) {
			LogMgr.getLogger(this).info(getTagName()+"在DefaultConfReader的readChildNodesAry( "+childTag+" )方法中,空指针异常！");
			return null;
		}
	}
}
