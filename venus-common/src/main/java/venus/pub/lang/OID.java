package venus.pub.lang;

import java.io.Serializable;

/**
 * <p>Description:提供对OID操作的封装。</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: USE</p>
 * @author 张文韬
 * @version 1.0
 */

public class OID implements Serializable,Comparable {
	//成员变量
	private long m_oid;
	
	//定义最大值常量和最小值常量
	private static final long MAX_VALUE = 9222999999999999999L; 
	private static final long MIN_VALUE = 1000000000000000000L;

	/**
	 * 利用一个Long型值构造新的OID类
	 * @param oid Long型参数
	 * @throws venus.pub.lang.LangException
	 */
	public OID(Long oid) throws LangException {
		try {
			//判断参数是否越界
			if (oid.longValue() > MAX_VALUE || oid.longValue() < MIN_VALUE) {
				throw new LangException("数值越界，请校对！");
			}
			m_oid = oid.longValue();
		} catch (NullPointerException e) {
			throw new LangException("参数空指针异常",e);
		}
	}

	public OID(String strOid) throws LangException {

		try {
			Long oid = Long.valueOf(strOid);
			//判断参数是否越界
			if (oid.longValue() > MAX_VALUE || oid.longValue() < MIN_VALUE) {
				throw new LangException("数值越界，请校对！");
			}
			m_oid = oid.longValue();
		} catch (NullPointerException e) {
			throw new LangException("参数空指针异常",e);
		} catch (Exception e) {
			throw new LangException("参数'"+strOid+"'异常",e);
		}
	}

	/**
	 * 利用一个long型值构造新的OID类
	 * @param oid long型参数
	 * @throws venus.pub.lang.LangException
	 */
	public OID(long oid) throws LangException {
		if (oid > MAX_VALUE || oid < MIN_VALUE) {
			throw new LangException("数值越界，请教对！");
		}
		m_oid = oid;
	}
	
	/**
	 * 取得oid的long型值
	 * @return long oid的long型值
	 */
	public long longValue() {
		return m_oid;
	}

	/**
	 * 取得oid的Long型值
	 * @return Long oid的Long型值
	 */
	public Long getLong() {
		return new Long(m_oid);
	}

	/**
	 * 比较两对象的oid值是否相等
	 * @param objOther 待比较的对象
	 * @return 如果两对象一致返回true；否则返回false；
	 */
	public boolean equals(Object objOther) {
		if (objOther != null && (objOther instanceof OID) && (m_oid == ((OID)objOther).longValue())) {
			return true;
		}
		else return false;
	}
	
	/**
	 * 生成接受方的散列代码
	 * @return int 该对象OID值对应的散列代码
	 */
	public int hashCode() {
		return this.getLong().hashCode();
	}
	
	/**
	 * 继承接口Comparable的抽象方法
	 * @param 	o 待比较的对象
	 * @return -1：小于待比较对象 0：等于待比较对象 1：大于待比较对象
	 */
	public int compareTo(Object o) throws NullPointerException {
		if (o == null) {
			throw new NullPointerException("参数为空！");			
		}
		return compareTo(((OID)o).getLong());
	}
	
	/**
	 * 比较两个Long型数据的大小
	 * @param anotherLong 待比较的Long型数据
	 * @return -1：小于待比较对象 0：等于待比较对象 1：大于待比较对象
	 */
	public int compareTo(Long anotherLong) {
		long thisVal = this.longValue();
		long anotherVal = anotherLong.longValue();
		return (thisVal<anotherVal ? -1 : (thisVal==anotherVal ? 0 : 1));
	}
	

	/**
	 * 
	 * 覆盖了基类的toString()方法,将长整型转换为String
	 */
	public String toString() {
		
		return String.valueOf(m_oid);
	}
}