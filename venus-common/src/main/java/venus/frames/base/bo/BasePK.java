/*
 * Created on 2004-11-30
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package venus.frames.base.bo;

import venus.frames.base.IGlobalsKeys;

import java.io.Serializable;

/**
 * @author sundaiyong
 * 
 * 主键的抽象类
 */
public abstract class BasePK implements Serializable,IGlobalsKeys {
	//转换为字符串
	public abstract String toString();

	//判断是否相等
	public abstract boolean equals(Object object);

	//计算hashCode
	public abstract int hashCode();

}