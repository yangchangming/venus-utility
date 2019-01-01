/*
 * 系统名称:单表模板 --> test
 * 
 * 文件名称: venus.authority.sample.department.vo --> DepartmentVo.java
 * 
 * 功能描述:
 * 
 * 版本历史: 2007-01-31 14:20:10.088 创建1.0.0版 (甘硕)
 *  
 */

package venus.oa.organization.department.vo;

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

public class DepartmentVo extends BaseValueObject implements Cloneable {
    
	//开始AU_DEPARTMENT的属性
			
	/**
     * id 表示: 主键
	 * 数据库中的注释: 
     */
     
	private String id;
		
	/**
     * dept_no 表示: dept_no
	 * 数据库中的注释: 
     */
     
	private String dept_no;
		
	/**
     * dept_name 表示: dept_name
	 * 数据库中的注释: 
     */
     
	private String dept_name;
		
	/**
     * dept_flag 表示: dept_flag
	 * 数据库中的注释: 
     */
     
	private String dept_flag;
		
	/**
     * dept_type 表示: dept_type
	 * 数据库中的注释: 
     */
     
	private String dept_type;
		
	/**
     * dept_level 表示: dept_level
	 * 数据库中的注释: 
     */
     
	private String dept_level;
		
	/**
     * dept_leader 表示: dept_leader
	 * 数据库中的注释: 
     */
     
	private String dept_leader;
		
	/**
     * remark 表示: 备注
	 * 数据库中的注释: 
     */
     
	private String remark;
		
	/**
     * enable_status 表示: 启用/禁用状态
	 * 数据库中的注释: 
     */
     
	private String enable_status;
		
	/**
     * enable_date 表示: 启用/禁用日期
	 * 数据库中的注释: 
     */
     
	private Timestamp enable_date;
		
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
		
	/**
     * column1 表示: column1
	 * 数据库中的注释: 
     */
     
	private String column1;
		
	/**
     * column2 表示: column2
	 * 数据库中的注释: 
     */
     
	private String column2;
		
	/**
     * column3 表示: column3
	 * 数据库中的注释: 
     */
     
	private String column3;
		
	//结束AU_DEPARTMENT的属性
		
		
	//开始AU_DEPARTMENT的setter和getter方法
			
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
     * 设置dept_no
     * 
     * @param dept_no dept_no
     */
	public void setDept_no(String dept_no){
		this.dept_no = dept_no;
	}
	
    /**
     * 获得dept_no
     * 
     * @return dept_no
     */
	public String getDept_no(){
		return dept_no;
	}
	
    /**
     * 设置dept_name
     * 
     * @param dept_name dept_name
     */
	public void setDept_name(String dept_name){
		this.dept_name = dept_name;
	}
	
    /**
     * 获得dept_name
     * 
     * @return dept_name
     */
	public String getDept_name(){
		return dept_name;
	}
	
    /**
     * 设置dept_flag
     * 
     * @param dept_flag dept_flag
     */
	public void setDept_flag(String dept_flag){
		this.dept_flag = dept_flag;
	}
	
    /**
     * 获得dept_flag
     * 
     * @return dept_flag
     */
	public String getDept_flag(){
		return dept_flag;
	}
	
    /**
     * 设置dept_type
     * 
     * @param dept_type dept_type
     */
	public void setDept_type(String dept_type){
		this.dept_type = dept_type;
	}
	
    /**
     * 获得dept_type
     * 
     * @return dept_type
     */
	public String getDept_type(){
		return dept_type;
	}
	
    /**
     * 设置dept_level
     * 
     * @param dept_level dept_level
     */
	public void setDept_level(String dept_level){
		this.dept_level = dept_level;
	}
	
    /**
     * 获得dept_level
     * 
     * @return dept_level
     */
	public String getDept_level(){
		return dept_level;
	}
	
    /**
     * 设置dept_leader
     * 
     * @param dept_leader dept_leader
     */
	public void setDept_leader(String dept_leader){
		this.dept_leader = dept_leader;
	}
	
    /**
     * 获得dept_leader
     * 
     * @return dept_leader
     */
	public String getDept_leader(){
		return dept_leader;
	}
	
    /**
     * 设置备注
     * 
     * @param remark 备注
     */
	public void setRemark(String remark){
		this.remark = remark;
	}
	
    /**
     * 获得备注
     * 
     * @return 备注
     */
	public String getRemark(){
		return remark;
	}
	
    /**
     * 设置启用/禁用状态
     * 
     * @param enable_status 启用/禁用状态
     */
	public void setEnable_status(String enable_status){
		this.enable_status = enable_status;
	}
	
    /**
     * 获得启用/禁用状态
     * 
     * @return 启用/禁用状态
     */
	public String getEnable_status(){
		return enable_status;
	}
	
    /**
     * 设置启用/禁用日期
     * 
     * @param enable_date 启用/禁用日期
     */
	public void setEnable_date(Timestamp enable_date){
		this.enable_date = enable_date;
	}
	
    /**
     * 获得启用/禁用日期
     * 
     * @return 启用/禁用日期
     */
	public Timestamp getEnable_date(){
		return enable_date;
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
     * 设置column1
     * 
     * @param column1 column1
     */
	public void setColumn1(String column1){
		this.column1 = column1;
	}
	
    /**
     * 获得column1
     * 
     * @return column1
     */
	public String getColumn1(){
		return column1;
	}
	
    /**
     * 设置column2
     * 
     * @param column2 column2
     */
	public void setColumn2(String column2){
		this.column2 = column2;
	}
	
    /**
     * 获得column2
     * 
     * @return column2
     */
	public String getColumn2(){
		return column2;
	}
	
    /**
     * 设置column3
     * 
     * @param column3 column3
     */
	public void setColumn3(String column3){
		this.column3 = column3;
	}
	
    /**
     * 获得column3
     * 
     * @return column3
     */
	public String getColumn3(){
		return column3;
	}
	
	//结束AU_DEPARTMENT的setter和getter方法
    
}	

