
	
package venus.portal.link.model;

import venus.frames.base.vo.BaseValueObject;
import venus.portal.helper.EwpVoHelper;

public class Link extends BaseValueObject implements Cloneable {
    
    
	public static final String ID="id";

	public static final String CATEGORY="category";

	public static final String TITLE="title";

	public static final String CONTENT="content";

	public static final String WEBSITE="website";

	public static final String VERSION="version";

    
	//开始ewp_link的属性
			
	/**
     * id 表示: 主键
	 * 数据库中的注释: 
     */
     
	private String id;
		
	/**
     * category 表示: 类别
	 * 数据库中的注释: 
     */
     
	private String category;
		
	/**
     * title 表示: 头衔
	 * 数据库中的注释: 
     */
     
	private String title;
		
	/**
     * content 表示: 内容
	 * 数据库中的注释: 
     */
     
	private String content;
		
	/**
     * website 表示: 网址
	 * 数据库中的注释: 
     */
     
	private String website;
		
	/**
     * version 表示: 版本
	 * 数据库中的注释: 
     */
     
	private Integer version;
		
	//结束ewp_link的属性
		
		
	//开始ewp_link的setter和getter方法
			
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
     * 获得类别
     * 
     * @return 类别
     */
	public String getCategory(){
		return category;
	}
	
    /**
     * 设置类别
     * 
     * @param category 类别
     */
	public void setCategory(String category){
		this.category = category;
	}
	
    /**
     * 获得头衔
     * 
     * @return 头衔
     */
	public String getTitle(){
		return title;
	}
	
    /**
     * 设置头衔
     * 
     * @param title 头衔
     */
	public void setTitle(String title){
		this.title = title;
	}
	
    /**
     * 获得内容
     * 
     * @return 内容
     */
	public String getContent(){
		return content;
	}
	
    /**
     * 设置内容
     * 
     * @param content 内容
     */
	public void setContent(String content){
		this.content = content;
	}
	
    /**
     * 获得网址
     * 
     * @return 网址
     */
	public String getWebsite(){
		return website;
	}
	
    /**
     * 设置网址
     * 
     * @param website 网址
     */
	public void setWebsite(String website){
		this.website = website;
	}
	
    /**
     * 获得版本
     * 
     * @return 版本
     */
	public Integer getVersion(){
		return version;
	}
	
    /**
     * 设置版本
     * 
     * @param version 版本
     */
	public void setVersion(Integer version){
		this.version = version;
	}
	
	//结束ewp_link的setter和getter方法
    
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
    
}	
