/*
 * 系统名称:单表模板 --> test
 * 
 * 文件名称: venus.authority.sample.employee.vo --> EmployeeVo.java
 * 
 * 功能描述:
 * 
 * 版本历史: 2007-01-31 14:20:08.541 创建1.0.0版 (甘硕)
 *  
 */

package venus.oa.organization.employee.vo;

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

public class EmployeeVo extends BaseValueObject {
    
	//开始AU_EMPLOYEE的属性
			
	/**
     * id 表示: 主键
	 * 数据库中的注释: 
     */
     
	private String id;
		
	/**
     * person_no 表示: 员工编号
	 * 数据库中的注释: 
     */
     
	private String person_no;
		
	/**
     * person_name 表示: 姓名
	 * 数据库中的注释: 
     */
     
	private String person_name;
		
	/**
     * english_name 表示: 英文名
	 * 数据库中的注释: 
     */
     
	private String english_name;
		
	/**
     * person_type 表示: 类型
	 * 数据库中的注释: 
     */
     
	private String person_type;
		
	/**
     * sex 表示: 性别
	 * 数据库中的注释: 
     */
     
	private String sex;
		
	/**
     * mobile 表示: 移动电话
	 * 数据库中的注释: 
     */
     
	private String mobile;
		
	/**
     * tel 表示: 电话
	 * 数据库中的注释: 
     */
     
	private String tel;
		
	/**
     * email 表示: 电子邮件
	 * 数据库中的注释: 
     */
     
	private String email;
		
	/**
     * address 表示: 联系地址
	 * 数据库中的注释: 
     */
     
	private String address;
		
	/**
     * postalcode 表示: 邮编
	 * 数据库中的注释: 
     */
     
	private String postalcode;
		
		
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
		
		
	//结束AU_EMPLOYEE的属性
		
		
	//开始AU_EMPLOYEE的setter和getter方法
			
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
     * 设置员工编号
     * 
     * @param person_no 员工编号
     */
	public void setPerson_no(String person_no){
		this.person_no = person_no;
	}
	
    /**
     * 获得员工编号
     * 
     * @return 员工编号
     */
	public String getPerson_no(){
		return person_no;
	}
	
    /**
     * 设置姓名
     * 
     * @param person_name 姓名
     */
	public void setPerson_name(String person_name){
		this.person_name = person_name;
	}
	
    /**
     * 获得姓名
     * 
     * @return 姓名
     */
	public String getPerson_name(){
		return person_name;
	}
	
    /**
     * 设置英文名
     * 
     * @param english_name 英文名
     */
	public void setEnglish_name(String english_name){
		this.english_name = english_name;
	}
	
    /**
     * 获得英文名
     * 
     * @return 英文名
     */
	public String getEnglish_name(){
		return english_name;
	}
	
    /**
     * 设置类型
     * 
     * @param person_type 类型
     */
	public void setPerson_type(String person_type){
		this.person_type = person_type;
	}
	
    /**
     * 获得类型
     * 
     * @return 类型
     */
	public String getPerson_type(){
		return person_type;
	}
	
    /**
     * 设置性别
     * 
     * @param sex 性别
     */
	public void setSex(String sex){
		this.sex = sex;
	}
	
    /**
     * 获得性别
     * 
     * @return 性别
     */
	public String getSex(){
		return sex;
	}
	
	
    /**
     * @return 返回 mobile。
     */
    public String getMobile() {
        return mobile;
    }
    
    /**
     * @param mobile 要设置的 mobile。
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    
    /**
     * 设置电话
     * 
     * @param tel 电话
     */
	public void setTel(String tel){
		this.tel = tel;
	}
	
    /**
     * 获得电话
     * 
     * @return 电话
     */
	public String getTel(){
		return tel;
	}
	
    /**
     * 设置电子邮件
     * 
     * @param email 电子邮件
     */
	public void setEmail(String email){
		this.email = email;
	}
	
    /**
     * 获得电子邮件
     * 
     * @return 电子邮件
     */
	public String getEmail(){
		return email;
	}
	
    /**
     * 设置联系地址
     * 
     * @param address 联系地址
     */
	public void setAddress(String address){
		this.address = address;
	}
	
    /**
     * 获得联系地址
     * 
     * @return 联系地址
     */
	public String getAddress(){
		return address;
	}
	
    /**
     * 设置邮编
     * 
     * @param postalcode 邮编
     */
	public void setPostalcode(String postalcode){
		this.postalcode = postalcode;
	}
	
    /**
     * 获得邮编
     * 
     * @return 邮编
     */
	public String getPostalcode(){
		return postalcode;
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
	//结束AU_EMPLOYEE的setter和getter方法
}	

