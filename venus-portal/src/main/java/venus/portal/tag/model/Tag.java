/*
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */

package venus.portal.tag.model;

import venus.frames.base.vo.BaseValueObject;
import venus.portal.helper.EwpVoHelper;

/**
 * @author zhangrenyang
 * 
 */

public class Tag extends BaseValueObject implements Cloneable {
    
    public static final String NAME="name";
	//开始ewp_tag的属性
			
	/**
     * id 表示: 主键
	 * 数据库中的注释: 
     */
     
	private String id;
		
	/**
     * name 表示: name
	 * 数据库中的注释: 
     */
     
	private String name;
    private Long version; //版本(乐观锁)
		
	//结束ewp_tag的属性
		
		
	//开始ewp_tag的setter和getter方法
			
    /**
     * 获得主键
     * 
     * @return 主键
     */
	public String getId(){
		return id;
	}
	
    /**
     * 设置主键
     * 
     * @param id 主键
     */
	public void setId(String id){
		this.id = id;
	}
	
    /**
     * 获得name
     * 
     * @return name
     */
	public String getName(){
		return name;
	}
	
    /**
     * 设置name
     * 
     * @param name name
     */
	public void setName(String name){
		this.name = name;
	}
	
	//结束ewp_tag的setter和getter方法
    
	/**
	 * override method 'equals'
	 * 
	 * @param other 与本对象比较的其它对象
	 * @return boolean 两个对象的各个属性是否都相等
	 */
    public boolean equals(Object other) {
        return EwpVoHelper.voEquals(this, other);
    }

	/**
	 * override method 'hashCode'
	 * 
	 * @return int Hash码
	 */
	public int hashCode() {
	    return EwpVoHelper.voHashCode(this);
	}
	
	/**
	 * override method 'clone'
	 *
	 * @see Object#clone()
	 * @return Object 克隆后对象
	 */
	public Object clone() {
	    return EwpVoHelper.voClone(this);
    }

	
	/**
	 * override method 'toString'
	 * 
	 * @return String 字符串表示
	 */
	public String toString() {
	    return super.toString() + ":" + EwpVoHelper.voToString(this);
	}

    /**
     * @return the version
     */
    public Long getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(Long version) {
        this.version = version;
    }
    
}	
