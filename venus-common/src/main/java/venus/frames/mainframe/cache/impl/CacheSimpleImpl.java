package venus.frames.mainframe.cache.impl;

import venus.frames.mainframe.cache.ICacher;
import venus.frames.mainframe.log.LogMgr;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

/**
 * @author wujun
 *
 * 更改所生成类型注释的模板为
 * 窗口 > 首选项 > Java > 代码生成 > 代码和注释
 */
public class CacheSimpleImpl implements ICacher
{

	private String m_strCacheName = "";
	
	/**
	 * 用于存储属性值的列表,Hashtable格式的
	 */
	private Hashtable m_hashAttributeTable = null;
	//public ContextMgr theContextMgr;

	/**
	 * 构造函数
	 * @roseuid 3FA0D8C90128
	 */
	public CacheSimpleImpl() {
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
	public Object getByKey(Object key) {
		//如果name为空抛出异常
		if (key == null) {
			//throw new IllegalArgumentException("输入的键值为空，请重新输入！");
			LogMgr.getLogger(this).error("CacheSimpleImpl的getByKey方法中，参数空指针错误!");
			return null;
		}
		if (m_hashAttributeTable.get(key) != null) {
			return m_hashAttributeTable.get(key);
		}
		//若哈希表中没有相关对象，返回空
		return null;
	}

	/**
	 * 从cache中根据key删除对象值<br>
	 *
	 * @param name 待删除对象对应的属性名
	 */
	public void removeByKey(Object key) {
		//如果name为空抛出异常
		if (key == null) {
			//throw new IllegalArgumentException("输入的键值为空，请重新输入！");
			LogMgr.getLogger(this).error("CacheSimpleImpl的removeByKey方法中，参数空指针错误!");
			return;
		}
		//删除哈希表中存在的对象
		if (m_hashAttributeTable.get(key) != null) {
			m_hashAttributeTable.remove(key);
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
	public void insert(Object key, Object object) {
		//如果name为空抛出异常
		if (key == null) {
			//throw new IllegalArgumentException("输入的键值为空，请重新输入！");
			LogMgr.getLogger(this).error("CacheSimpleImpl的insert方法中，参数空指针错误!");
			return;
		}
		//除去哈希表中使用name键值得对象
		if (m_hashAttributeTable.get(key) != null) {
			m_hashAttributeTable.remove(key);
		}
		//向哈希表中添加对象值
		try {
			m_hashAttributeTable.put(key, object);
		} catch (NullPointerException e) {
			//e.printStackTrace();
			LogMgr.getLogger(this).error("CacheSimpleImpl的insert方法中出现空指针异常！",e);
		}
	}

	/**
	 * 清除cache中的内容<br>
	 * 
	 * 
	 * @roseuid 3FA0CCC6000E
	 */
	public void clear() {
		this.m_hashAttributeTable.clear();
	}
	
	public void setCacheName(String cacheName){
		
		this.m_strCacheName = cacheName;
		
	}
	
	/**
	 * 获得 CAHCE 的最大容量
	 * @return int - 该CAHCE 的最大容量
	 * @roseuid 3F8E099E0363
	 */
	public int getMaxSize(){
		
		return -1;
	
	}
	

	/* （非 Javadoc）
	 * @see venus.frames.mainframe.cache.ICacher#setMaxSize(int)
	 */
	public void setMaxSize(int maxSize)
	{
		
	}


	/* （非 Javadoc）
	 * @see venus.frames.mainframe.cache.ICacher#getCacheName()
	 */
	public String getCacheName()
	{
		return this.m_strCacheName;
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

	public java.util.Enumeration keys(){
		
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
