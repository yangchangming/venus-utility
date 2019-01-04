package venus.oa.organization.aupartytype.vo;

import venus.frames.base.vo.BaseValueObject;

import java.sql.Timestamp;

public class AuPartyTypeVo extends BaseValueObject {

	/**
	 * 主键
	 */
	private String id = null; 
	
	/**
	 * 名称
	 */
	private String name = null;
	
	/**
	 * 标识
	 */
	private String keyword = null;
	
	/**
	 * 映射哪张表
	 */
	private String table_name = null;
	
	/**
	 * 启用/禁用状态
	 */
	private String enable_status = null;	
	
	/**
	 * 启用/禁用时间
	 */
	private Timestamp enable_date = null;
	
	/**
	 * 切割编码前缀
	 */
	private String  code_prefix = null;	
	
	/**
	 * 创建时间
	 */
	private Timestamp  create_date = null;
	
	/**
	 * 修改时间	
	 */
	private Timestamp  modify_date = null;
	
	/**
	 * 备注
	 */
	private String remark = null;
	
	/**
	 * @return 返回 code_prefix。
	 */
	public String getCode_prefix() {
		return code_prefix;
	}
	/**
	 * @param code_prefix 要设置的 code_prefix。
	 */
	public void setCode_prefix(String code_prefix) {
		this.code_prefix = code_prefix;
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
	 * 
	 *
	 */
	public AuPartyTypeVo() {

	}

	
	
	/**
	 * @return 返回 keyword。
	 */
	public String getKeyword() {
		return keyword;
	}
	/**
	 * @param keyword 要设置的 keyword。
	 */
	public void setKeyword(String keyword) {
		this.keyword = keyword;
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
