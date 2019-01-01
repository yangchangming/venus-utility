/*
 * 系统名称:PlatForm
 * 
 * 文件名称: venus.authority.au.auresource.vo --> AuResourceVo.java
 * 
 * 功能描述:
 * 
 * 版本历史: 2006-06-09 15:32:17.33 创建1.0.0版 (甘硕)
 *  
 */

package venus.oa.authority.auresource.vo;

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

public class AuResourceVo extends BaseValueObject {
    
	//开始AU_RESOURCE的属性
	private String id;
	private String resource_type;
	private String is_public; 
	private String access_type; 
	private String name; 
	private String value; 
	private String table_name; 
	private String party_type; 
	private String help; 
	private String enable_status; 
	private Timestamp enable_date; 
	private Timestamp create_date; 
	private Timestamp modify_date;
	private String filter_type;
	private String field_chinesename;
	private String field_name;
	private String table_chinesename;
	//结束AU_RESOURCE的属性
		
	//开始AU_RESOURCE的setter和getter方法
    /**
     * @return 返回 field_chinesename。
     */
    public String getField_chinesename() {
        return field_chinesename;
    }
    /**
     * @param field_chinesename 要设置的 field_chinesename。
     */
    public void setField_chinesename(String field_chinesename) {
        this.field_chinesename = field_chinesename;
    }
    /**
     * @return 返回 field_name。
     */
    public String getField_name() {
        return field_name;
    }
    /**
     * @param field_name 要设置的 field_name。
     */
    public void setField_name(String field_name) {
        this.field_name = field_name;
    }
    /**
     * @return 返回 filter_type。
     */
    public String getFilter_type() {
        return filter_type;
    }
    /**
     * @param filter_type 要设置的 filter_type。
     */
    public void setFilter_type(String filter_type) {
        this.filter_type = filter_type;
    }
    /**
     * @return 返回 table_chinesename。
     */
    public String getTable_chinesename() {
        return table_chinesename;
    }
    /**
     * @param table_chinesename 要设置的 table_chinesename。
     */
    public void setTable_chinesename(String table_chinesename) {
        this.table_chinesename = table_chinesename;
    }
	
    /**
     * @return 返回 access_type。
     */
    public String getAccess_type() {
        return access_type;
    }
    /**
     * @param access_type 要设置的 access_type。
     */
    public void setAccess_type(String access_type) {
        this.access_type = access_type;
    }
    /**
     * @return 返回 create_date。
     */
    public Timestamp getCreate_date() {
        return create_date;
    }
    /**
     * @param create_date 要设置的 create_date。
     */
    public void setCreate_date(Timestamp create_date) {
        this.create_date = create_date;
    }
    /**
     * @return 返回 enable_date。
     */
    public Timestamp getEnable_date() {
        return enable_date;
    }
    /**
     * @param enable_date 要设置的 enable_date。
     */
    public void setEnable_date(Timestamp enable_date) {
        this.enable_date = enable_date;
    }
    /**
     * @return 返回 enable_status。
     */
    public String getEnable_status() {
        return enable_status;
    }
    /**
     * @param enable_status 要设置的 enable_status。
     */
    public void setEnable_status(String enable_status) {
        this.enable_status = enable_status;
    }
    /**
     * @return 返回 help。
     */
    public String getHelp() {
        return help;
    }
    /**
     * @param help 要设置的 help。
     */
    public void setHelp(String help) {
        this.help = help;
    }
    /**
     * @return 返回 id。
     */
    public String getId() {
        return id;
    }
    /**
     * @param id 要设置的 id。
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     * @return 返回 is_public。
     */
    public String getIs_public() {
        return is_public;
    }
    /**
     * @param is_public 要设置的 is_public。
     */
    public void setIs_public(String is_public) {
        this.is_public = is_public;
    }
    /**
     * @return 返回 modify_date。
     */
    public Timestamp getModify_date() {
        return modify_date;
    }
    /**
     * @param modify_date 要设置的 modify_date。
     */
    public void setModify_date(Timestamp modify_date) {
        this.modify_date = modify_date;
    }
    /**
     * @return 返回 name。
     */
    public String getName() {
        return name;
    }
    /**
     * @param name 要设置的 name。
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return 返回 party_type。
     */
    public String getParty_type() {
        return party_type;
    }
    /**
     * @param party_type 要设置的 party_type。
     */
    public void setParty_type(String party_type) {
        this.party_type = party_type;
    }
    /**
     * @return 返回 resource_type。
     */
    public String getResource_type() {
        return resource_type;
    }
    /**
     * @param resource_type 要设置的 resource_type。
     */
    public void setResource_type(String resource_type) {
        this.resource_type = resource_type;
    }
    /**
     * @return 返回 table_name。
     */
    public String getTable_name() {
        return table_name;
    }
    /**
     * @param table_name 要设置的 table_name。
     */
    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }
    /**
     * @return 返回 value。
     */
    public String getValue() {
        return value;
    }
    /**
     * @param value 要设置的 value。
     */
    public void setValue(String value) {
        this.value = value;
    }
    //结束AU_RESOURCE的setter和getter方法
}	

