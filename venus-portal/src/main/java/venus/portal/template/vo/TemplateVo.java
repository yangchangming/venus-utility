package venus.portal.template.vo;

import venus.frames.base.vo.BaseValueObject;

import java.sql.Timestamp;

public class TemplateVo extends BaseValueObject implements Cloneable {
    
	private String id;
	private String template_name;
	private String template_content;
    private String order_code;
    private String remark;
    private String usable_status;
    private Timestamp create_date;
    private String create_ip;
    private String create_user_id;
    private Timestamp modify_date;
    private String modify_ip;
    private String modify_user_id;
    private Long version; //版本(乐观锁)
    
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
     * 获得template_name
     * 
     * @return template_name
     */
	public String getTemplate_name(){
		return template_name;
	}
	
    /**
     * 设置template_name
     * 
     * @param template_name template_name
     */
	public void setTemplate_name(String template_name){
		this.template_name = template_name;
	}
	
    /**
     * 获得template_content
     * 
     * @return template_content
     */
	public String getTemplate_content(){
		return template_content;
	}
	
    /**
     * 设置template_content
     * 
     * @param template_content template_content
     */
	public void setTemplate_content(String template_content){
		this.template_content = template_content;
	}

    public String getOrder_code() {
        return order_code;
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUsable_status() {
        return usable_status;
    }

    public void setUsable_status(String usable_status) {
        this.usable_status = usable_status;
    }

    public Timestamp getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Timestamp create_date) {
        this.create_date = create_date;
    }

    public String getCreate_ip() {
        return create_ip;
    }

    public void setCreate_ip(String create_ip) {
        this.create_ip = create_ip;
    }

    public String getCreate_user_id() {
        return create_user_id;
    }

    public void setCreate_user_id(String create_user_id) {
        this.create_user_id = create_user_id;
    }

    public Timestamp getModify_date() {
        return modify_date;
    }

    public void setModify_date(Timestamp modify_date) {
        this.modify_date = modify_date;
    }

    public String getModify_ip() {
        return modify_ip;
    }

    public void setModify_ip(String modify_ip) {
        this.modify_ip = modify_ip;
    }

    public String getModify_user_id() {
        return modify_user_id;
    }

    public void setModify_user_id(String modify_user_id) {
        this.modify_user_id = modify_user_id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
	
	//结束template的setter和getter方法
    
    public Object clone() {
      try {
        return super.clone();
      } catch(CloneNotSupportedException e) {
        System.out.println("Cloning not allowed.");
        return this;
      }
    }

}	
