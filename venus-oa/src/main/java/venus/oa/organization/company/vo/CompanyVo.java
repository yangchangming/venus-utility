/*
 * 系统名称:单表模板 --> test
 * 
 * 文件名称: venus.authority.sample.company.vo --> CompanyVo.java
 * 
 * 功能描述:
 * 
 * 版本历史: 2007-01-31 14:20:11.432 创建1.0.0版 (甘硕)
 *  
 */

package venus.oa.organization.company.vo;

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

public class CompanyVo extends BaseValueObject {
    
	//开始AU_COMPANY的属性
			
	/**
     * id 表示: 主键
	 * 数据库中的注释: 
     */
     
	private String id;
		
	/**
     * company_no 表示: 公司编号
	 * 数据库中的注释: 
     */
     
	private String company_no;
		
	/**
     * company_name 表示: 公司名称
	 * 数据库中的注释: 
     */
     
	private String company_name;
	
	/**
     * short_name 表示: 公司简称
	 * 数据库中的注释: 
     */
     
	private String short_name;
		
	/**
     * company_flag 表示: 公司标识
	 * 数据库中的注释: 
     */
     
	private String company_flag;
		
	/**
     * company_type 表示: 公司类型
	 * 数据库中的注释: 
     */
     
	private String company_type;
		
	/**
     * company_level 表示: 公司级别
	 * 数据库中的注释: 
     */
     
	private String company_level;
		
	/**
     * area 表示: 区域
	 * 数据库中的注释: 
     */
     
	private String area;
		
	/**
     * linkman 表示: 联系人
	 * 数据库中的注释: 
     */
     
	private String linkman;
		
	/**
     * tel 表示: 联系电话
	 * 数据库中的注释: 
     */
     
	private String tel;
		
	/**
     * fax 表示: 传真
	 * 数据库中的注释: 
     */
     
	private String fax;
		
	/**
     * postalcode 表示: 邮编
	 * 数据库中的注释: 
     */
     
	private String postalcode;
		
	/**
     * address 表示: 地址
	 * 数据库中的注释: 
     */
     
	private String address;
		
	/**
     * email 表示: 电子邮件
	 * 数据库中的注释: 
     */
     
	private String email;
		
	/**
     * web 表示: 网址
	 * 数据库中的注释: 
     */
     
	private String web;
		
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
		
		
	//开始AU_COMPANY的setter和getter方法
			
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
     * 设置公司编号
     * 
     * @param company_no 公司编号
     */
	public void setCompany_no(String company_no){
		this.company_no = company_no;
	}
	
    /**
     * 获得公司编号
     * 
     * @return 公司编号
     */
	public String getCompany_no(){
		return company_no;
	}
	
    /**
     * 设置公司名称
     * 
     * @param company_name 公司名称
     */
	public void setCompany_name(String company_name){
		this.company_name = company_name;
	}
	
    /**
     * 获得公司名称
     * 
     * @return 公司名称
     */
	public String getCompany_name(){
		return company_name;
	}
	
    /**
     * 设置公司标识
     * 
     * @param company_flag 公司标识
     */
	public void setCompany_flag(String company_flag){
		this.company_flag = company_flag;
	}
	
    /**
     * 获得公司标识
     * 
     * @return 公司标识
     */
	public String getCompany_flag(){
		return company_flag;
	}
	
    /**
     * 设置公司类型
     * 
     * @param company_type 公司类型
     */
	public void setCompany_type(String company_type){
		this.company_type = company_type;
	}
	
    /**
     * 获得公司类型
     * 
     * @return 公司类型
     */
	public String getCompany_type(){
		return company_type;
	}
	
    /**
     * 设置公司级别
     * 
     * @param company_level 公司级别
     */
	public void setCompany_level(String company_level){
		this.company_level = company_level;
	}
	
    /**
     * 获得公司级别
     * 
     * @return 公司级别
     */
	public String getCompany_level(){
		return company_level;
	}
	
    /**
     * 设置区域
     * 
     * @param area 区域
     */
	public void setArea(String area){
		this.area = area;
	}
	
    /**
     * 获得区域
     * 
     * @return 区域
     */
	public String getArea(){
		return area;
	}
	
    /**
     * 设置联系人
     * 
     * @param linkman 联系人
     */
	public void setLinkman(String linkman){
		this.linkman = linkman;
	}
	
    /**
     * 获得联系人
     * 
     * @return 联系人
     */
	public String getLinkman(){
		return linkman;
	}
	
    /**
     * 设置联系电话
     * 
     * @param tel 联系电话
     */
	public void setTel(String tel){
		this.tel = tel;
	}
	
    /**
     * 获得联系电话
     * 
     * @return 联系电话
     */
	public String getTel(){
		return tel;
	}
	
    /**
     * 设置传真
     * 
     * @param fax 传真
     */
	public void setFax(String fax){
		this.fax = fax;
	}
	
    /**
     * 获得传真
     * 
     * @return 传真
     */
	public String getFax(){
		return fax;
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
     * 设置地址
     * 
     * @param address 地址
     */
	public void setAddress(String address){
		this.address = address;
	}
	
    /**
     * 获得地址
     * 
     * @return 地址
     */
	public String getAddress(){
		return address;
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
     * 设置网址
     * 
     * @param web 网址
     */
	public void setWeb(String web){
		this.web = web;
	}
	
    /**
     * 获得网址
     * 
     * @return 网址
     */
	public String getWeb(){
		return web;
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
	
    /**
     * @return 返回 short_name。
     */
    public String getShort_name() {
        return short_name;
    }
    /**
     * @param short_name 要设置的 short_name。
     */
    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }
	
	//结束AU_COMPANY的setter和getter方法
    
}	

