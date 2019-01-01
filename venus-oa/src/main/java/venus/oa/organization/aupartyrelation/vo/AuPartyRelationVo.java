/*
 * 创建日期 2006-10-24
 *
 */
package venus.oa.organization.aupartyrelation.vo;

import venus.frames.base.vo.BaseValueObject;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author maxiao
 *
 */
public class AuPartyRelationVo extends BaseValueObject{
    //主键
    private String id=null;
    //团体关系类型ID
    private String relationtype_id=null;
    //团体关系类型Name
    private String relationtype_name=null;
    //团体关系类型标识
    private String relationtype_keyword=null;
    //父团体ID
    private String parent_partyid=null;
    //父团体名称
    private String parent_partyname=null;
    //父团体编码
    private String parent_code=null;
    //团体ID
    private String partyid=null;
    //团体编码
    private String code=null;
    //团体名称
    private String name=null;
    //团体类型标识
    private String partytype_id=null;
    //类型内的级别
    private String type_level=null;
    //排序编码
    private String order_code=null;
    //是否为叶子节点
    private String is_leaf="0";
    //类型内是否为叶子节点
    private String type_is_leaf="0";
    //是否继承权限
    private String is_inherit="1";
    //是否主岗位
    private String is_chief="1";
    //Email_冗余
    private String email=null;
    //创建时间
    private Timestamp create_date=null;
    //修改时间
    private Timestamp modify_date=null;
    
    //所有父节点
    private List all_parent_vo=null;
    
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
     * @return 返回 email。
     */
    public String getEmail() {
        return email;
    }
    /**
     * @param email 要设置的 email。
     */
    public void setEmail(String email) {
        this.email = email;
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
     * @return 返回 is_chief。
     */
    public String getIs_chief() {
        return is_chief;
    }
    /**
     * @param is_chief 要设置的 is_chief。
     */
    public void setIs_chief(String is_chief) {
        this.is_chief = is_chief;
    }
    /**
     * @return 返回 is_inherit。
     */
    public String getIs_inherit() {
        return is_inherit;
    }
    /**
     * @param is_inherit 要设置的 is_inherit。
     */
    public void setIs_inherit(String is_inherit) {
        this.is_inherit = is_inherit;
    }
    /**
     * @return 返回 is_leaf。
     */
    public String getIs_leaf() {
        return is_leaf;
    }
    /**
     * @param is_leaf 要设置的 is_leaf。
     */
    public void setIs_leaf(String is_leaf) {
        this.is_leaf = is_leaf;
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
     * @return 返回 order_code。
     */
    public String getOrder_code() {
        return order_code;
    }
    /**
     * @param order_code 要设置的 order_code。
     */
    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }
    /**
     * @return 返回 parent_code。
     */
    public String getParent_code() {
        return parent_code;
    }
    /**
     * @param parent_code 要设置的 parent_code。
     */
    public void setParent_code(String parent_code) {
        this.parent_code = parent_code;
    }
    /**
     * @return 返回 parent_partyid。
     */
    public String getParent_partyid() {
        return parent_partyid;
    }
    /**
     * @param parent_partyid 要设置的 parent_partyid。
     */
    public void setParent_partyid(String parent_partyid) {
        this.parent_partyid = parent_partyid;
    }
    /**
     * @return 返回 partyid。
     */
    public String getPartyid() {
        return partyid;
    }
    /**
     * @param partyid 要设置的 partyid。
     */
    public void setPartyid(String partyid) {
        this.partyid = partyid;
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
     * @return 返回 relationtype_id。
     */
    public String getRelationtype_id() {
        return relationtype_id;
    }
    /**
     * @param relationtype_id 要设置的 relationtype_id。
     */
    public void setRelationtype_id(String relationtype_id) {
        this.relationtype_id = relationtype_id;
    }
    /**
     * @return 返回 relationtype_keyword。
     */
    public String getRelationtype_keyword() {
        return relationtype_keyword;
    }
    /**
     * @param relationtype_keyword 要设置的 relationtype_keyword。
     */
    public void setRelationtype_keyword(String relationtype_keyword) {
        this.relationtype_keyword = relationtype_keyword;
    }
    /**
     * @return 返回 type_is_leaf。
     */
    public String getType_is_leaf() {
        return type_is_leaf;
    }
    /**
     * @param type_is_leaf 要设置的 type_is_leaf。
     */
    public void setType_is_leaf(String type_is_leaf) {
        this.type_is_leaf = type_is_leaf;
    }
    /**
     * @return 返回 type_level。
     */
    public String getType_level() {
        return type_level;
    }
    /**
     * @param type_level 要设置的 type_level。
     */
    public void setType_level(String type_level) {
        this.type_level = type_level;
    }
    /**
     * @return 返回 parent_partyname。
     */
    public String getParent_partyname() {
        return parent_partyname;
    }
    /**
     * @param parent_partyname 要设置的 parent_partyname。
     */
    public void setParent_partyname(String parent_partyname) {
        this.parent_partyname = parent_partyname;
    }
    /**
     * @return 返回 relationtype_name。
     */
    public String getRelationtype_name() {
        return relationtype_name;
    }
    /**
     * @param relationtype_name 要设置的 relationtype_name。
     */
    public void setRelationtype_name(String relationtype_name) {
        this.relationtype_name = relationtype_name;
    }
    
    /**
     * @return 返回 all_parent_vo。
     */
    public List getAll_parent_vo() {
        return all_parent_vo;
    }
    /**
     * @param all_parent_vo 要设置的 all_parent_vo。
     */
    public void setAll_parent_vo(List all_parent_vo) {
        this.all_parent_vo = all_parent_vo;
    }
}

