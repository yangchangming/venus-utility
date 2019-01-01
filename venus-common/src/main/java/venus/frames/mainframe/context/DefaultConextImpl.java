//Source file: D:\\venus_view\\Tech_Department\\Platform\\Venus\\4项目开发\\1工作区\\4实现\\venus\\frames\\mainframe\\context\\DefaultConextImpl.java

package venus.frames.mainframe.context;

//import venus.frames.mainframe.cache.CacheFactory;
//import venus.frames.mainframe.cache.Cacher;

import venus.frames.mainframe.cache.impl.CacheSimpleImpl;
import venus.frames.mainframe.log.LogMgr;

import java.util.*;

/**
 * 上下文类，该类简单包装了Hashtable<br>
 * 该类为接口Context的默认实现
 * 
 * @author 张文韬
 */
public class DefaultConextImpl implements IContext {

	private String m_strContextName = "";
	
	/**
	 * 用于存储属性值的列表,Hashtable格式的
	 */
	private Hashtable m_hashAttributeTable = null;
	//public ContextMgr theContextMgr;

	/**
	 * 构造函数
	 * @roseuid 3FA0D8C90128
	 */
	public DefaultConextImpl() {
		super();
		//初始化m_hashAttributeTable
		m_hashAttributeTable = new Hashtable();
	}

	/**
	 * 从上下文对象中根据属性name提取对象<br>
	 * 
	 * @param name 要取得的对象对应的属性值
	 * @return Object 存放于上下文环境中的对象
	 * @roseuid 3FA0CCC50389
	 */
	public Object getAttribute(String name) {
		//如果name为空抛出异常
		if (name == null) {
			//throw new IllegalArgumentException("输入的键值为空，请重新输入！");
			LogMgr.getLogger(this).error("DefaultContextImpl的getAttribute方法中，参数空指针错误!");
			return null;
		}
		if (m_hashAttributeTable.get(name) != null) {
			return m_hashAttributeTable.get(name);
		}
		//若哈希表中没有相关对象，返回空
		return null;
	}

	/**
	 * 从上下文对象中根据属性name删除属性中对象值<br>
	 *
	 * @param name 待删除对象对应的属性名
	 * @roseuid 3FA0CCC50399
	 */
	public void removeAttribute(String name) {
		//如果name为空抛出异常
		if (name == null) {
			//throw new IllegalArgumentException("输入的键值为空，请重新输入！");
			LogMgr.getLogger(this).error("DefaultContextImpl的removeAttribute方法中，参数空指针错误!");
			return;
		}
		//删除哈希表中存在的对象
		if (m_hashAttributeTable.get(name) != null) {
			m_hashAttributeTable.remove(name);
		}
		return;
	}

	/**
	 * 在上下文对象中插入属性名和对象<br>
	 * 
	 * @param name 需要往上下文对象里插入的属性名
	 * @param object 属性名对应的对象
	 * @roseuid 3FA0CCC503A8
	 */
	public void setAttribute(String name, Object object) {
		//如果name为空抛出异常
		if (name == null) {
			//throw new IllegalArgumentException("输入的键值为空，请重新输入！");
			LogMgr.getLogger(this).error("DefaultContextImpl的setAttribute方法中，参数空指针错误!");
			return;
		}
		//除去哈希表中使用name键值得对象
		if (m_hashAttributeTable.get(name) != null) {
			m_hashAttributeTable.remove(name);
		}
		//向哈希表中添加对象值
		try {
			m_hashAttributeTable.put(name, object);
		} catch (NullPointerException e) {
			//e.printStackTrace();
		    LogMgr.getLogger(this).error("DefaultContextImpl的setAttribute方法中出现空指针异常！",e);
		}
	}

	/**
	 * 得到上下文对象中所有属性的名称列表
	 * 
	 * 
	 * @return Enumeration 上下文对象中所有属性的名称列表；如果存储属性名的容器为空，则返回null
	 * @roseuid 3FA0CCC503B8
	 */
	public Enumeration getAttributeNames() {
		if (m_hashAttributeTable != null) {
			return m_hashAttributeTable.keys();
		} else
			return null;
	}

	/**
	 * 清除上下文对象中的内容<br>
	 * 
	 * 
	 * @roseuid 3FA0CCC6000E
	 */
	public void clear() {
		this.m_hashAttributeTable.clear();
		//this.m_hashAttributeTable = null;
	}
	
	public void setContextName(String contextName){
		
		this.m_strContextName = contextName;
	
	}
	/* （非 Javadoc）
	 * @see venus.frames.mainframe.cache.ICacher#getImplName()
	 */
	public String getImplName()
	{
		return CacheSimpleImpl.class.getName();
	}

	/* （非 Javadoc）
	 * @see venus.frames.mainframe.cache.ISizeGetter#getSize()
	 */
	public int getSize()
	{
		return -1;
	}

	public Enumeration keys(){
		
		return this.m_hashAttributeTable.keys();
	
	}

	/* （非 Javadoc）
	 * @see java.util.Map#size()
	 */
	public int size() {
		return m_hashAttributeTable.size();
	}

	/* （非 Javadoc）
	 * @see java.util.Map#isEmpty()
	 */
	public boolean isEmpty() {
		return m_hashAttributeTable.isEmpty();
	}

	/* （非 Javadoc）
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	public boolean containsKey(Object arg0) {
		return m_hashAttributeTable.containsKey(arg0);
	}

	/* （非 Javadoc）
	 * @see java.util.Map#containsValue(java.lang.Object)
	 */
	public boolean containsValue(Object arg0) {
		return m_hashAttributeTable.containsValue(arg0);
	}

	/* （非 Javadoc）
	 * @see java.util.Map#values()
	 */
	public Collection values() {
		return m_hashAttributeTable.values();
	}

	/* （非 Javadoc）
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	public void putAll(Map arg0) {
		m_hashAttributeTable.putAll(arg0);		
	}

	/* （非 Javadoc）
	 * @see java.util.Map#entrySet()
	 */
	public Set entrySet() {
		return m_hashAttributeTable.entrySet();
	}

	/* （非 Javadoc）
	 * @see java.util.Map#keySet()
	 */
	public Set keySet() {
		return m_hashAttributeTable.keySet();
	}

	/* （非 Javadoc）
	 * @see java.util.Map#get(java.lang.Object)
	 */
	public Object get(Object arg0) {
		return m_hashAttributeTable.get(arg0);
	}

	/* （非 Javadoc）
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	public Object remove(Object arg0) {
		return m_hashAttributeTable.remove(arg0);
	}

	/* （非 Javadoc）
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	public Object put(Object arg0, Object arg1) {
		return m_hashAttributeTable.put(arg0,arg1);
	}
}
