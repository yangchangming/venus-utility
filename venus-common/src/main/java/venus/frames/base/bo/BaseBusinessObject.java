/*
 * Created on 2004-11-30
 *
 * 业务对象的基类
 */
package venus.frames.base.bo;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import venus.frames.base.IGlobalsKeys;

import java.io.Serializable;

/**
 * @author 孙代勇
 * 
 * 业务对象类的抽象基类,以业务对象Id为组键
 */
public abstract class BaseBusinessObject implements Serializable,IGlobalsKeys {
	private String id;

	/**
	 * @return 返回Id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            设置Id.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 转换为字符串
	 */
	public String toString() {
		return new ToStringBuilder(this).append("id", getId()).toString();
	}

	/**
	 * 判断两个对象是否相等
	 */
	public boolean equals(Object other) {
		if (other == null)
			return false;
		if ((this == other))
			return true;
		if (!(other.getClass().equals(this.getClass())))
			return false;
		BaseBusinessObject castOther = (BaseBusinessObject) other;
		return new EqualsBuilder().append(this.getId(), castOther.getId())
				.isEquals();
	}

	/**
	 * 计算hash码
	 */
	public int hashCode() {
		return new HashCodeBuilder().append(this.getId()).toHashCode();
	}

}