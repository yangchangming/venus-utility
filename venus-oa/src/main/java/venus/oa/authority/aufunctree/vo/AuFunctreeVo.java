/*
 * 系统名称:PlatForm
 * 
 * 文件名称: venus.authority.au.aufunctree.vo --> AuFunctreeVo.java
 * 
 * 功能描述:
 * 
 * 版本历史: 2006-06-09 15:32:55.093 创建1.0.0版 (甘硕)
 *  
 */

package venus.oa.authority.aufunctree.vo;

import venus.frames.base.vo.BaseValueObject;

import java.sql.Timestamp;

/**
 * 功能、用途、现存BUG:
 * 
 * @author 甘硕
 * @version 1.0.0
 * @see 需要参见的其它类
 * @since 1.0.0
 */

public class AuFunctreeVo extends BaseValueObject {
    
	//开始AU_FUNCTREE的属性
			
	/**
     * id 表示: 主键
	 * 数据库中的注释: 
     */
     
	private String id;
		
	/**
     * type 表示: 类型
	 * 数据库中的注释: 
     */
     
	private String type;
		
	/**
     * code 表示: 编码
	 * 数据库中的注释: 
     */
     
	private String code;
		
	/**
     * parent_code 表示: 父编码
	 * 数据库中的注释: 
     */
     
	private String parent_code;
		
	/**
     * total_code 表示: 全编码
	 * 数据库中的注释: 
     */
     
	private String total_code;
		
	/**
     * name 表示: 名称
	 * 数据库中的注释: 
     */
     
	private String name;
		
	/**
     * hot_key 表示: 快捷键
	 * 数据库中的注释: 
     */
     
	private String hot_key;
		
	/**
     * help 表示: 帮助信息
	 * 数据库中的注释: 
     */
     
	private String help;
		
	/**
     * url 表示: 实际链接
	 * 数据库中的注释: 
     */
     
	private String url;
		
	/**
     * is_leaf 表示: 是否叶子
	 * 数据库中的注释: 
     */
     
	private String is_leaf;
		
	/**
     * type_is_leaf 表示: 类型内是否叶子
	 * 数据库中的注释: 
     */
     
	private String type_is_leaf;
		
	/**
     * order_code 表示: 排序编码
	 * 数据库中的注释: 
     */
     
	private String order_code;
		
	/**
     * system_id 表示: 导入权限标识
	 * 数据库中的注释: 
     */
     
	private String system_id;
	
	/**
     * system_id 表示: 导入资源标识
     * 数据库中的注释: 
     */
     
    private String tree_id;
		
	/**
     * create_date 表示: 创建日期
	 * 数据库中的注释: 
     */
     
	private Timestamp create_date;
		
	/**
     * modify_date 表示: 修改日期
	 * 数据库中的注释: 
     */
     
	private Timestamp modify_date;
	
	private String keyword;
	
	private int tree_level;
	
	private String is_ssl="0";//是否支持ssl
	
	private String is_public; 
		
	//结束AU_FUNCTREE的属性
		
		
	//开始AU_FUNCTREE的setter和getter方法
		
	
	
    /**
     * @return the is_public
     */
    public String getIs_public() {
        return is_public;
    }

    /**
     * @param is_public the is_public to set
     */
    public void setIs_public(String is_public) {
        this.is_public = is_public;
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
     * 获得主键
     * 
     * @return 主键
     */
	public String getId(){
		return id;
	}
	
    /**
     * 设置类型
     * 
     * @param type 类型
     */
	public void setType(String type){
		this.type = type;
	}
	
    /**
     * 获得类型
     * 
     * @return 类型
     */
	public String getType(){
		return type;
	}
	
    /**
     * 设置编码
     * 
     * @param code 编码
     */
	public void setCode(String code){
		this.code = code;
	}
	
    /**
     * 获得编码
     * 
     * @return 编码
     */
	public String getCode(){
		return code;
	}
	
    /**
     * 设置父编码
     * 
     * @param parent_code 父编码
     */
	public void setParent_code(String parent_code){
		this.parent_code = parent_code;
	}
	
    /**
     * 获得父编码
     * 
     * @return 父编码
     */
	public String getParent_code(){
		return parent_code;
	}
	
    /**
     * 设置全编码
     * 
     * @param total_code 全编码
     */
	public void setTotal_code(String total_code){
		this.total_code = total_code;
	}
	
    /**
     * 获得全编码
     * 
     * @return 全编码
     */
	public String getTotal_code(){
		return total_code;
	}
	
    /**
     * 设置名称
     * 
     * @param name 名称
     */
	public void setName(String name){
		this.name = name;
	}
	
    /**
     * 获得名称
     * 
     * @return 名称
     */
	public String getName(){
		return name;
	}
	
    /**
     * 设置快捷键
     * 
     * @param hot_key 快捷键
     */
	public void setHot_key(String hot_key){
		this.hot_key = hot_key;
	}
	
    /**
     * 获得快捷键
     * 
     * @return 快捷键
     */
	public String getHot_key(){
		return hot_key;
	}
	
    /**
     * 设置帮助信息
     * 
     * @param help 帮助信息
     */
	public void setHelp(String help){
		this.help = help;
	}
	
    /**
     * 获得帮助信息
     * 
     * @return 帮助信息
     */
	public String getHelp(){
		return help;
	}
	
    /**
     * 设置实际链接
     * 
     * @param url 实际链接
     */
	public void setUrl(String url){
		this.url = url;
	}
	
    /**
     * 获得实际链接
     * 
     * @return 实际链接
     */
	public String getUrl(){
		return url;
	}
	
    /**
     * 设置是否叶子
     * 
     * @param is_leaf 是否叶子
     */
	public void setIs_leaf(String is_leaf){
		this.is_leaf = is_leaf;
	}
	
    /**
     * 获得是否叶子
     * 
     * @return 是否叶子
     */
	public String getIs_leaf(){
		return is_leaf;
	}
	
    /**
     * 设置类型内是否叶子
     * 
     * @param type_is_leaf 类型内是否叶子
     */
	public void setType_is_leaf(String type_is_leaf){
		this.type_is_leaf = type_is_leaf;
	}
	
    /**
     * 获得类型内是否叶子
     * 
     * @return 类型内是否叶子
     */
	public String getType_is_leaf(){
		return type_is_leaf;
	}
	
    /**
     * 设置排序编码
     * 
     * @param order_code 排序编码
     */
	public void setOrder_code(String order_code){
		this.order_code = order_code;
	}
	
    /**
     * 获得排序编码
     * 
     * @return 排序编码
     */
	public String getOrder_code(){
		return order_code;
	}
	
    /**
     * 设置所属系统
     * 
     * @param system_id 所属系统
     */
	public void setSystem_id(String system_id){
		this.system_id = system_id;
	}
	
    /**
     * 获得所属系统
     * 
     * @return 所属系统
     */
	public String getSystem_id(){
		return system_id;
	}
	
    /**
     * 设置创建日期
     * 
     * @param create_date 创建日期
     */
	public void setCreate_date(Timestamp create_date){
		this.create_date = create_date;
	}
	
    /**
     * 获得创建日期
     * 
     * @return 创建日期
     */
	public Timestamp getCreate_date(){
		return create_date;
	}
	
    /**
     * 设置修改日期
     * 
     * @param modify_date 修改日期
     */
	public void setModify_date(Timestamp modify_date){
		this.modify_date = modify_date;
	}
	
    /**
     * 获得修改日期
     * 
     * @return 修改日期
     */
	public Timestamp getModify_date(){
		return modify_date;
	}
	
	/**
     * @return 返回 keyword。
     */
    public String getKeyword() {
        return keyword;
    }
    /**
     * @param keyword 要设置的 keyword。
     */
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    
    /**
     * @return 返回 tree_level。
     */
    public int getTree_level() {
        return tree_level;
    }
    /**
     * @param tree_level 要设置的 tree_level。
     */
    public void setTree_level(int tree_level) {
        this.tree_level = tree_level;
    }
	//结束AU_FUNCTREE的setter和getter方法

    /**
     * @return the ssl
     */
    public String getIs_ssl() {
        return is_ssl;
    }

    /**
     * @param ssl the ssl to set
     */
    public void setIs_ssl(String ssl) {
        this.is_ssl = ssl;
    }

    /**
     * @return the tree_id
     */
    public String getTree_id() {
        return tree_id;
    }

    /**
     * @param tree_id the tree_id to set
     */
    public void setTree_id(String tree_id) {
        this.tree_id = tree_id;
    }
    
}	

