package venus.oa.organization.auparty.vo;

import venus.frames.base.vo.BaseValueObject;

import java.sql.Timestamp;

/**
 * @author changming.yang.ah@gmail.com
 *
 */
public class PartyVo extends BaseValueObject {
    //主键
    private String id;
    //团体类型ID
    private String partytype_id;
    //团体类型名称
    private String partyname;
    //团体类型标识
    private String partytype_keyword;
    //是否继承权限
    private String is_inherit;
    //是否真实团体
    private String is_real;
    //名称
    private String name;
    //Email
    private String email;
    //启用/禁用状态
    private String enable_status;
    //启用/禁用时间
    private Timestamp enable_date;
    //创建时间
    private Timestamp create_date;
    //修改时间
    private Timestamp modify_date;
    //所属机构
    private String owner_org;
    //备注
    private String remark;


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
     * @return 返回 email。
     */
    public String getEmail() {
    	if(email==null){
    		email="";
    	}
        return email;
    }
    /**
     * @param email 要设置的 email。
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return 返回 enable_status。
     */
    public String getEnable_status() {
        if(enable_status==null || enable_status.trim().equals("")){
            enable_status="1";
        }
        return enable_status;
    }
    /**
     * @param enable_status 要设置的 enable_status。
     */
    public void setEnable_status(String enable_status) {
        this.enable_status = enable_status;
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
     * @return 返回 is_inherit。
     */
    public String getIs_inherit() {
        if(is_inherit==null || is_inherit.trim().equals("")){
            is_inherit="1";
        }
        return is_inherit;
    }
    /**
     * @param is_inherit 要设置的 is_inherit。
     */
    public void setIs_inherit(String is_inherit) {
        this.is_inherit = is_inherit;
    }
    /**
     * @return 返回 is_real。
     */
    public String getIs_real() {
        if(is_real==null || is_real.trim().equals("")){
            is_real="1";
        }
        return is_real;
    }
    /**
     * @param is_real 要设置的 is_real。
     */
    public void setIs_real(String is_real) {
        this.is_real = is_real;
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
     * @return 返回 partytype_keyword。
     */
    public String getPartytype_keyword() {
        if(partytype_keyword==null){
            partytype_keyword="";
    	}
        return partytype_keyword;
    }
    /**
     * @param partytype_keyword 要设置的 partytype_keyword。
     */
    public void setPartytype_keyword(String partytype_keyword) {
        this.partytype_keyword = partytype_keyword;
    }
    /**
     * @return 返回 partyName。
     */
    public String getPartyname() {
        return partyname;
    }
    /**
     * @param partyName 要设置的 partyName。
     */
    public void setPartyname(String partyname) {
        this.partyname = partyname;
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

