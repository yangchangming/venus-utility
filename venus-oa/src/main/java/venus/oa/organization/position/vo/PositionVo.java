/*
 * 系统名称: VENUS 组织权限系统
 * 
 * 文件名称: venus.authority.sample.position.vo --> PositionVo.java
 * 
 * 功能描述:
 * 
 * 版本历史: 2007-01-31 14:20:07.15 创建1.0.0版 (甘硕)
 *  
 */

package venus.oa.organization.position.vo;

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

public class PositionVo extends BaseValueObject implements Cloneable {
    
	//开始AU_POSITION的属性
			
	/**
     * id 表示: 主键
	 * 数据库中的注释: 
     */
     
	private String id;
		
	/**
     * position_no 表示: 岗位编号
	 * 数据库中的注释: 
     */
     
	private String position_no;
		
	/**
     * position_name 表示: 岗位名称
	 * 数据库中的注释: 
     */
     
	private String position_name;
		
	/**
     * position_flag 表示: 岗位标识
	 * 数据库中的注释: 
     */
     
	private String position_flag;
		
	/**
     * position_type 表示: 岗位类型
	 * 数据库中的注释: 
     */
     
	private String position_type;
		
	/**
     * position_level 表示: 岗位级别
	 * 数据库中的注释: 
     */
     
	private String position_level;
		
	/**
     * leader_flag 表示: 是否领导
	 * 数据库中的注释: 
     */
     
	private String leader_flag;
		
	/**
     * leader_level 表示: 领导级别
	 * 数据库中的注释: 
     */
     
	private String leader_level;
		
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
     * column1 表示: 备用字段1
	 * 数据库中的注释: 
     */
     
	private String column1;
		
	/**
     * column2 表示: 备用字段2
	 * 数据库中的注释: 
     */
     
	private String column2;
		
	/**
     * column3 表示: 备用字段3
	 * 数据库中的注释: 
     */
     
	private String column3;
		
	//结束AU_POSITION的属性
		
		
	//开始AU_POSITION的setter和getter方法
			
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
     * 设置岗位编号
     * 
     * @param position_no 岗位编号
     */
	public void setPosition_no(String position_no){
		this.position_no = position_no;
	}
	
    /**
     * 获得岗位编号
     * 
     * @return 岗位编号
     */
	public String getPosition_no(){
		return position_no;
	}
	
    /**
     * 设置岗位名称
     * 
     * @param position_name 岗位名称
     */
	public void setPosition_name(String position_name){
		this.position_name = position_name;
	}
	
    /**
     * 获得岗位名称
     * 
     * @return 岗位名称
     */
	public String getPosition_name(){
		return position_name;
	}
	
    /**
     * 设置岗位标识
     * 
     * @param position_flag 岗位标识
     */
	public void setPosition_flag(String position_flag){
		this.position_flag = position_flag;
	}
	
    /**
     * 获得岗位标识
     * 
     * @return 岗位标识
     */
	public String getPosition_flag(){
		return position_flag;
	}
	
    /**
     * 设置岗位类型
     * 
     * @param position_type 岗位类型
     */
	public void setPosition_type(String position_type){
		this.position_type = position_type;
	}
	
    /**
     * 获得岗位类型
     * 
     * @return 岗位类型
     */
	public String getPosition_type(){
		return position_type;
	}
	
    /**
     * 设置岗位级别
     * 
     * @param position_level 岗位级别
     */
	public void setPosition_level(String position_level){
		this.position_level = position_level;
	}
	
    /**
     * 获得岗位级别
     * 
     * @return 岗位级别
     */
	public String getPosition_level(){
		return position_level;
	}
	
    /**
     * 设置是否领导
     * 
     * @param leader_flag 是否领导
     */
	public void setLeader_flag(String leader_flag){
		this.leader_flag = leader_flag;
	}
	
    /**
     * 获得是否领导
     * 
     * @return 是否领导
     */
	public String getLeader_flag(){
		return leader_flag;
	}
	
    /**
     * 设置领导级别
     * 
     * @param leader_level 领导级别
     */
	public void setLeader_level(String leader_level){
		this.leader_level = leader_level;
	}
	
    /**
     * 获得领导级别
     * 
     * @return 领导级别
     */
	public String getLeader_level(){
		return leader_level;
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
     * 设置备用字段1
     * 
     * @param column1 备用字段1
     */
	public void setColumn1(String column1){
		this.column1 = column1;
	}
	
    /**
     * 获得备用字段1
     * 
     * @return 备用字段1
     */
	public String getColumn1(){
		return column1;
	}
	
    /**
     * 设置备用字段2
     * 
     * @param column2 备用字段2
     */
	public void setColumn2(String column2){
		this.column2 = column2;
	}
	
    /**
     * 获得备用字段2
     * 
     * @return 备用字段2
     */
	public String getColumn2(){
		return column2;
	}
	
    /**
     * 设置备用字段3
     * 
     * @param column3 备用字段3
     */
	public void setColumn3(String column3){
		this.column3 = column3;
	}
	
    /**
     * 获得备用字段3
     * 
     * @return 备用字段3
     */
	public String getColumn3(){
		return column3;
	}
	
	//结束AU_POSITION的setter和getter方法 
}	

