package venus.oa.organization.auconnectrule.vo;

import venus.frames.base.vo.BaseValueObject;

import java.sql.Timestamp;

public class AuConnectRuleVo extends BaseValueObject {

	/**
	 * 主键
	 */
	private String id = null; 

	/**
	 * 团体关系类型ID
	 */
	private String relation_type_id = null;
	
	/**
	 * 团体关系类型
	 */
	private String relation_type = null; 

	/**
	 * 父团体类型ID
	 */
	private String parent_partytype_id = null;
	
	/**
	 * 父团体类型
	 */
	private String parent_partytype = null;
	
	/**
	 * 子团体类型ID
	 */
	private String child_partytype_id = null;
	
	/**
	 * 子团体类型
	 */
	private String child_partytype = null;
	
	/**
	 * 名称
	 */
	private String name = null;
	
	/**
	 * 创建时间
	 */
	private Timestamp  create_date = null;
	
	/**
	 * 修改时间
	 */
	private Timestamp  modify_date = null;//
	/**
	 * 备注
	 */
	private String remark = null; 
	
	/**
	 * @return 返回 child_partytype。
	 */
	public String getChild_partytype() {
		return child_partytype;
	}
	/**
	 * @param child_partytype 要设置的 child_partytype。
	 */
	public void setChild_partytype(String child_partytype) {
		this.child_partytype = child_partytype;
	}
	/**
	 * @return 返回 child_partytype_id。
	 */
	public String getChild_partytype_id() {
		return child_partytype_id;
	}
	/**
	 * @param child_partytype_id 要设置的 child_partytype_id。
	 */
	public void setChild_partytype_id(String child_partytype_id) {
		this.child_partytype_id = child_partytype_id;
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
	 * @return 返回 parent_partytype。
	 */
	public String getParent_partytype() {
		return parent_partytype;
	}
	/**
	 * @param parent_partytype 要设置的 parent_partytype。
	 */
	public void setParent_partytype(String parent_partytype) {
		this.parent_partytype = parent_partytype;
	}
	/**
	 * @return 返回 parent_partytype_id。
	 */
	public String getParent_partytype_id() {
		return parent_partytype_id;
	}
	/**
	 * @param parent_partytype_id 要设置的 parent_partytype_id。
	 */
	public void setParent_partytype_id(String parent_partytype_id) {
		this.parent_partytype_id = parent_partytype_id;
	}
	/**
	 * @return 返回 relation_type。
	 */
	public String getRelation_type() {
		return relation_type;
	}
	/**
	 * @param relation_type 要设置的 relation_type。
	 */
	public void setRelation_type(String relation_type) {
		this.relation_type = relation_type;
	}
	/**
	 * @return 返回 relation_type_id。
	 */
	public String getRelation_type_id() {
		return relation_type_id;
	}
	/**
	 * @param relation_type_id 要设置的 relation_type_id。
	 */
	public void setRelation_type_id(String relation_type_id) {
		this.relation_type_id = relation_type_id;
	}
	

	/**
	 *  
	 */
	public AuConnectRuleVo() {

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
}

