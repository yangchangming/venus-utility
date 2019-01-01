/*
 * Created on 2004-11-30
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package venus.frames.base.bo;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import venus.frames.base.IGlobalsKeys;

import java.io.Serializable;

/**
 * @author sundaiyong
 *
 * 有复合主键的业务抽象基类
 */
public abstract class BasePKBusinessObject implements Serializable,IGlobalsKeys{
	
	private BasePK pk;
	/**
	 * 转换为字符串
	 */
	public String toString() {
		return new ToStringBuilder(this).append("key", getPk()).toString();
	}

	/**
	 * 判断两个对象是否相等
	 */
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other.getClass().equals(this.getClass())))
			return false;
		BasePKBusinessObject castOther = (BasePKBusinessObject) other;
		return new EqualsBuilder().append(this.getPk(), castOther.getPk())
				.isEquals();
	}

	/**
	 * 计算hash码
	 */
	public int hashCode() {
		return new HashCodeBuilder().append(getPk()).toHashCode();
	}

	/**
	 * @return Returns the pk.
	 */
	public BasePK getPk() {
		return pk;
	}
	/**
	 * @param pk The pk to set.
	 */
	public void setPk(BasePK pk) {
		this.pk = pk;
	}
}
