package venus.oa.authority.auauthorize.vo;

import venus.frames.base.vo.BaseValueObject;

import java.sql.Timestamp;

public class AuAuthorizeVo extends BaseValueObject {

	/**
	 * 主键
	 */
	private String id = null; 
	
	/**
	 * 访问者ID
	 */
	private String visitor_id = null;
	
	/**
	 *访问者编码_冗余
	 */
	private String visitor_code = null;
	
	/**
	 * 访问者类型_冗余
	 */
	private String visitor_type = null;
	
	/**
	 * 资源ID
	 */
	private String resource_id = null;
	
	/**
	 * 资源编码_冗余
	 */
	private String resource_code = null;
	
	/**
	 * 资源编码_名称
	 */
	private String resourcename = null;
	
	/**
	 * 资源类型_冗余
	 */
	private String resource_type = null;
	
	/**
	 * 授权情况
	 */
	private String authorize_status = null;
	
	/**
	 * 访问方式
	 */
	private String access_type = null;
	
	/**
	 * 是否有附加数据
	 */
	private String is_append = null;		
	
	/**
	 * 创建时间
	 */
	private Timestamp  create_date = null;	
	//第三方系统标识
    private String system_id = null;
        
    /**
     * @return the system_id
     */
    public String getSystem_id() {
        return system_id;
    }
    /**
     * @param system_id the system_id to set
     */
    public void setSystem_id(String system_id) {
        this.system_id = system_id;
    }	
	/**
	 * 
	 *
	 */
	public AuAuthorizeVo() {
	    
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
     * @return 返回 authorize_status。
     */
    public String getAuthorize_status() {
        return authorize_status;
    }
    /**
     * @param authorize_status 要设置的 authorize_status。
     */
    public void setAuthorize_status(String authorize_status) {
        this.authorize_status = authorize_status;
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
     * @return 返回 is_append。
     */
    public String getIs_append() {
        return is_append;
    }
    /**
     * @param is_append 要设置的 is_append。
     */
    public void setIs_append(String is_append) {
        this.is_append = is_append;
    }
    /**
     * @return 返回 resource_code。
     */
    public String getResource_code() {
        return resource_code;
    }
    /**
     * @param resource_code 要设置的 resource_code。
     */
    public void setResource_code(String resource_code) {
        this.resource_code = resource_code;
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
     * @return 返回 visitor_type。
     */
    public String getVisitor_type() {
        return visitor_type;
    }
    /**
     * @param visitor_type 要设置的 visitor_type。
     */
    public void setVisitor_type(String visitor_type) {
        this.visitor_type = visitor_type;
    }
    /**
     * @return 返回 resourcename。
     */
    public String getResourcename() {
        return resourcename;
    }
    /**
     * @param resourcename 要设置的 resourcename。
     */
    public void setResourcename(String resourcename) {
        this.resourcename = resourcename;
    }
}

