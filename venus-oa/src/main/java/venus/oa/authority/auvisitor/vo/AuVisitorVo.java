package venus.oa.authority.auvisitor.vo;

import venus.frames.base.vo.BaseValueObject;

import java.sql.Timestamp;

public class AuVisitorVo extends BaseValueObject {

	/**
	 * 主键
	 */
	private String id = null; 
	
	/**
	 * 原始ID
	 */
	private String original_id = null;
	
	/**
	 * 团体关系id
	 */
	private String partytype_id = null;
	
	/**
	 * 团体关系类型id
	 */
	private String partyrelationtype_id = null;    
	
	/**
	 * 团体关系
	 */
	private String partytype = null;
	
	/**
	 * 团体关系类型
	 */
	private String partyrelationtype = null;    
	
	/**
	 * 访问者类型
	 */
	private String visitor_type = null;
	
	/**
	 * 名称_冗余
	 */
	private String name = null;
	
	/**
	 * 编号_冗余
	 */
	private String code = null;			
	
	/**
	 * 创建时间
	 */
	private Timestamp  create_date = null;
	
	/**
	 * 修改时间	
	 */
	private Timestamp  modify_date = null;	
	
	/**
	 * 上级节点名称_冗余
	 */
	private String parentName = null;	
	/**
	 * 团体类型分类中团体是否可用2006-12-07
	 */
	private String enable_status = null;

	private String remark = null;
	/**
	 *组织机构_冗余 
	 */
	private String owner_org = null;
	
	/**
	 * 
	 *
	 */
	public AuVisitorVo() {

	}

    /**
     * @return 返回 code。
     */
    public String getCode() {
        return code;
    }
    /**
     * @param code 要设置的 code。
     */
    public void setCode(String code) {
        this.code = code;
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
     * @return 返回 original_id。
     */
    public String getOriginal_id() {
        return original_id;
    }
    /**
     * @param original_id 要设置的 original_id。
     */
    public void setOriginal_id(String original_id) {
        this.original_id = original_id;
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
     * @return 返回 partyrelationtype。
     */
    public String getPartyrelationtype() {
        return partyrelationtype;
    }
    /**
     * @param partyrelationtype 要设置的 partyrelationtype。
     */
    public void setPartyrelationtype(String partyrelationtype) {
        this.partyrelationtype = partyrelationtype;
    }
    /**
     * @return 返回 partyrelationtype_id。
     */
    public String getPartyrelationtype_id() {
        return partyrelationtype_id;
    }
    /**
     * @param partyrelationtype_id 要设置的 partyrelationtype_id。
     */
    public void setPartyrelationtype_id(String partyrelationtype_id) {
        this.partyrelationtype_id = partyrelationtype_id;
    }
    /**
     * @return 返回 partytype。
     */
    public String getPartytype() {
        return partytype;
    }
    /**
     * @param partytype 要设置的 partytype。
     */
    public void setPartytype(String partytype) {
        this.partytype = partytype;
    }
    /**
     * @return 返回 partytype_id。
     */
    public String getPartytype_id() {
        return partytype_id;
    }
    /**
     * @param partytype_id 要设置的 partytype_id。
     */
    public void setPartytype_id(String partytype_id) {
        this.partytype_id = partytype_id;
    }
    /**
     * @return 返回 parentName。
     */
    public String getParentName() {
        return parentName;
    }
    /**
     * @param parentName 要设置的 parentName。
     */
    public void setParentName(String parentName) {
        this.parentName = parentName;
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
     * @return 返回 remark。
     */
    public String getRemark() {
        return remark;
    }
    /**
     * @param remark 要设置的 remark。
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }
	/**
	 * @return 返回 owner_org。
	 */
	public String getOwner_org() {
		return owner_org;
	}
	/**
	 * @param owner_org 要设置的 owner_org。
	 */
	public void setOwner_org(String owner_org) {
		this.owner_org = owner_org;
	}
}

