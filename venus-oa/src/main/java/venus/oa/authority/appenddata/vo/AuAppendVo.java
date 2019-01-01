package venus.oa.authority.appenddata.vo;

import venus.frames.base.vo.BaseValueObject;

import java.sql.Timestamp;

/**
 * 权限附加数据Vo
 *  2008-9-18
 * @author changming.Y <changming.yang.ah@gmail.com>
 */
public class AuAppendVo extends BaseValueObject {
	private String id;              
	private String visitor_id;     
	private String visitor_code;
	private String resource_id;      
	private String authorize_id;     
	private String append_value;     
	private Timestamp create_date;

	/**
	 * @return 返回 append_value。
	 */
	public String getAppend_value() {
		return append_value;
	}
	/**
	 * @param append_value 要设置的 append_value。
	 */
	public void setAppend_value(String append_value) {
		this.append_value = append_value;
	}
	/**
	 * @return 返回 authorize_id。
	 */
	public String getAuthorize_id() {
		return authorize_id;
	}
	/**
	 * @param authorize_id 要设置的 authorize_id。
	 */
	public void setAuthorize_id(String authorize_id) {
		this.authorize_id = authorize_id;
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
	 * @return 返回 resource_id。
	 */
	public String getResource_id() {
		return resource_id;
	}
	/**
	 * @param resource_id 要设置的 resource_id。
	 */
	public void setResource_id(String resource_id) {
		this.resource_id = resource_id;
	}
	/**
	 * @return 返回 visitor_id。
	 */
	public String getVisitor_id() {
		return visitor_id;
	}
	/**
	 * @param visitor_id 要设置的 visitor_id。
	 */
	public void setVisitor_id(String visitor_id) {
		this.visitor_id = visitor_id;
	}
	/**
	 * @return 返回 visitor_code。
	 */
	public String getVisitor_code() {
		return visitor_code;
	}
	/**
	 * @param visitor_code 要设置的 visitor_code。
	 */
	public void setVisitor_code(String visitor_code) {
		this.visitor_code = visitor_code;
	}
}

