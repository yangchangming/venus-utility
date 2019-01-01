package venus.pub.lang;

/**
 * <p>时间戳类，提供有关时间戳的相关操作</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company:USE </p>
 * @author 张文韬
 * @version 1.0
 */
public final class VTimeStamp implements java.io.Serializable,Comparable {
	private long m_timestamp;
	/**
	 * Long型数据的最大值
	 */
	public static final long MAX_VALUE = Long.MAX_VALUE;
	/**
	 * Long型数据的最小值
	 */
	public static final long MIN_VALUE = Long.MIN_VALUE;

	/**
	 * 空构造，默认调用VTimeStamp(0)。
	 */
	public VTimeStamp() {
		this(0);
	}

	/**
	 * 用long型构造。
	 * @param l long型参数
	 */
	public VTimeStamp(long l) {
		m_timestamp = l;
	}

	/**
	 * 用Long型构造。
	 * @param ts Long型参数
	 */
	public VTimeStamp(Long ts) {
		if (ts == null) {
			m_timestamp = 0;
		} else {
			m_timestamp = ts.longValue();
		}
	}

	/**
	 * 用VTimeStamp型构造。
	 * @param ts 用VTimeStamp对象作参数
	 */
	public VTimeStamp(VTimeStamp ts) {
		if (ts == null) {
			m_timestamp = 0;
		} else {
			m_timestamp = ts.longValue();
		}
	}

	/**
	 * 比较时间戳先后，在参数ts后返回true,否则返回false
	 * @param ts 待比较的时间戳对象
	 * @return 该对象值在参数ts后返回true,否则返回false
	 */
	public boolean after(VTimeStamp ts) {
		if (ts == null) {
			return false;
		}
		if (m_timestamp > ts.longValue()) {
			return true;
		}
		return false;
	}

	/**
	 * 比较时间戳先后，在参数ts前返回true,否则返回false
	 * @param ts 待比较的时间戳对象
	 * @return 该对象值在参数ts前返回true,否则返回false
	 */
	public boolean before(VTimeStamp ts) {
		if (ts == null) {
			return false;
		}
		if (m_timestamp < ts.longValue()) {
			return true;
		}
		return false;
	}

	/**
	 * 实现Comparable接口的抽象方法
	 * @param Object 待比较的对象
	 * @return 该对象值在参数o前返回-1，相同返回0，在参数o后返回1，参数为空返回Integer.MIN_VALUE
	 */
	public int compareTo(Object o) {
		return compareTo((VTimeStamp)o);
	}
	
	/**
	 * 比较时间戳先后
	 * @param ts 待比较的时间戳对象
	 * @return 该对象值在参数ts前返回-1，相同返回0，在参数ts后返回1，参数为空返回Integer.MIN_VALUE
	 */
	public int compareTo(VTimeStamp ts) {
		if (ts == null) {
			return Integer.MIN_VALUE;
		}
		long thisVal = longValue();
		long anotherVal = ts.longValue();
		return (thisVal < anotherVal ? -1 : (thisVal == anotherVal ? 0 : 1));
	}

	/**
	 * 比较时间戳是否相同，相同返回true,否则返回false
	 * @param o 待比较的时间戳对象
	 * @return 相同返回true,否则返回false
	 */
	public boolean equals(Object o) {
		if ((o != null) && (o instanceof VTimeStamp) && (longValue() == ((VTimeStamp)o).longValue())) {
			return true;
		}
		else return false;
	}

	/**
	 * 获取时间戳的Long型
	 * @return 时间戳的Long型值
	 */
	public Long getValue() {
		return new Long(m_timestamp);
	}

	/**
	 * 获取时间戳的long型值
	 * @return 时间戳的long型值
	 */
	public long longValue() {
		return m_timestamp;
	}

	/**
	 * 用long型参数设置时间戳值
	 * @param l 用long型参数作时间戳的值
	 */
	public void setValue(long l) {
		m_timestamp = l;
	}

	/**
	 * 用Long型参数设置时间戳值
	 * @param l 用Long型参数作时间戳的值
	 */
	public void setValue(Long l) {
		if (l == null) {
			m_timestamp = 0;
		} else {
			m_timestamp = l.longValue();
		}
	}

	/**
	 * 用VTimeStamp型参数设置时间戳值,参数为空设为Long(0)
	 * @param ts 用VTimeStamp对象设定时间戳的值
	 */
	public void setValue(VTimeStamp ts) {
		if (ts == null) {
			m_timestamp = 0;
		} else {
			m_timestamp = ts.longValue();
		}
	}

	/**
	 * cloneVTimeStamp对象
	 * @return 新的时间戳对象
	 */
	public Object clone() {
		return new VTimeStamp(m_timestamp);
	}
}