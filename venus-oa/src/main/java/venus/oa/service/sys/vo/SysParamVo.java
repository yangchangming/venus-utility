/*
 * 创建日期 2008-7-31
 */
package venus.oa.service.sys.vo;

import venus.frames.base.vo.BaseValueObject;

import java.sql.Timestamp;

/**
 *  2008-7-31
 * @author changming.Y <changming.yang.ah@gmail.com>
 */
public class SysParamVo extends BaseValueObject {
	/**
	 * <code>id</code> 主键
	 */
	private String id;
	private String propertykey;
	private String value;
	private String creatorId;
	private String creatorName;
	private Timestamp iniTime;
	private Timestamp updateTime;
	private String description;
	/**
	 * 属性类型 0:系统默认属性,1:用户自定义属性
	 */
	private String propertytype;
	/**
	 * 启用/禁用 0代表禁用；1代表启用
	 */
	private String enable;
	private String column1;
	
	/**
	 * @return the propertytype
	 */
	public String getPropertytype() {
	    return propertytype;
	}
	/**
	 * @param propertytype the propertytype to set
	 */
	public void setPropertytype(String propertytype) {
	    this.propertytype = propertytype;
	}
	/**
	 * @return 返回 column1。
	 */
	public String getColumn1() {
		return column1;
	}
	/**
	 * @param column1 要设置的 column1。
	 */
	public void setColumn1(String column1) {
		this.column1 = column1;
	}
	/**
	 * @return 返回 creatorId。
	 */
	public String getCreatorId() {
		return creatorId;
	}
	/**
	 * @param creatorId 要设置的 creatorId。
	 */
	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}
	/**
	 * @return 返回 creatorName。
	 */
	public String getCreatorName() {
		return creatorName;
	}
	/**
	 * @param creatorName 要设置的 creatorName。
	 */
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
	/**
	 * @return 返回 description。
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description 要设置的 description。
	 */
	public void setDescription(String description) {
		this.description = description;
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
	 * @return 返回 iniTime。
	 */
	public Timestamp getIniTime() {
		return iniTime;
	}
	/**
	 * @param iniTime 要设置的 iniTime。
	 */
	public void setIniTime(Timestamp iniTime) {
		this.iniTime = iniTime;
	}
	/**
	 * @return 返回 key。
	 */
	public String getPropertykey() {
		return propertykey;
	}
	/**
	 * @param key 要设置的 key。
	 */
	public void setPropertykey(String propertykey) {
		this.propertykey = propertykey;
	}
	/**
	 * @return 返回 updateTime。
	 */
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	/**
	 * @param updateTime 要设置的 updateTime。
	 */
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
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
	/**
	 * @return 返回 enable。
	 */
	public String getEnable() {
		return enable;
	}
	/**
	 * @param enable 要设置的 enable。
	 */
	public void setEnable(String enable) {
		this.enable = enable;
	}
}

