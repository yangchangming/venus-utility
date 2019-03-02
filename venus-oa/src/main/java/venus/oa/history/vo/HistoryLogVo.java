package venus.oa.history.vo;

import venus.frames.base.vo.BaseValueObject;

import java.sql.Timestamp;

public class HistoryLogVo  extends BaseValueObject {
	private String  id;             
	private Timestamp operate_date;        
	private String operate_id;         
	private String operate_name;       
	private String operate_type;
	private String source_id ;
	private String source_partyid ;
	private String source_code;
	private String source_name;
	private String source_orgtree;
	private String source_detail;
	private String source_typeid;
	private String dest_id;
	private String dest_code;
	private String dest_name;
	private String dest_orgtree;
	private String tag_id;
	private String tag_userid ;
	private Timestamp  tag_date ;
	private String cloumn1;
	private String cloumn2;
	private String cloumn3;
	
	/**
	 * @return 返回 source_typeid。
	 */
	public String getSource_typeid() {
		return source_typeid;
	}
	/**
	 * @param source_typeid 要设置的 source_typeid。
	 */
	public void setSource_typeid(String source_typeid) {
		this.source_typeid = source_typeid;
	}
	/**
	 * @return 返回 source_detail。
	 */
	public String getSource_detail() {
		return source_detail;
	}
	/**
	 * @param source_detail 要设置的 source_detail。
	 */
	public void setSource_detail(String source_detail) {
		this.source_detail = source_detail;
	}
	/**
	 * @return 返回 cloumn1。
	 */
	public String getCloumn1() {
		return cloumn1;
	}
	/**
	 * @param cloumn1 要设置的 cloumn1。
	 */
	public void setCloumn1(String cloumn1) {
		this.cloumn1 = cloumn1;
	}
	/**
	 * @return 返回 cloumn2。
	 */
	public String getCloumn2() {
		return cloumn2;
	}
	/**
	 * @param cloumn2 要设置的 cloumn2。
	 */
	public void setCloumn2(String cloumn2) {
		this.cloumn2 = cloumn2;
	}
	/**
	 * @return 返回 cloumn3。
	 */
	public String getCloumn3() {
		return cloumn3;
	}
	/**
	 * @param cloumn3 要设置的 cloumn3。
	 */
	public void setCloumn3(String cloumn3) {
		this.cloumn3 = cloumn3;
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
	 * @return 返回 operate_date。
	 */
	public Timestamp getOperate_date() {
		return operate_date;
	}
	/**
	 * @param operate_date 要设置的 operate_date。
	 */
	public void setOperate_date(Timestamp operate_date) {
		this.operate_date = operate_date;
	}
	/**
	 * @return 返回 operate_id。
	 */
	public String getOperate_id() {
		return operate_id;
	}
	/**
	 * @param operate_id 要设置的 operate_id。
	 */
	public void setOperate_id(String operate_id) {
		this.operate_id = operate_id;
	}
	/**
	 * @return 返回 operate_name。
	 */
	public String getOperate_name() {
		return operate_name;
	}
	/**
	 * @param operate_name 要设置的 operate_name。
	 */
	public void setOperate_name(String operate_name) {
		this.operate_name = operate_name;
	}
	/**
	 * @return 返回 source_code。
	 */
	public String getSource_code() {
		return source_code;
	}
	/**
	 * @param source_code 要设置的 source_code。
	 */
	public void setSource_code(String source_code) {
		this.source_code = source_code;
	}
	/**
	 * @return 返回 source_id。
	 */
	public String getSource_id() {
		return source_id;
	}
	/**
	 * @param source_id 要设置的 source_id。
	 */
	public void setSource_id(String source_id) {
		this.source_id = source_id;
	}
	/**
	 * @return 返回 source_name。
	 */
	public String getSource_name() {
		return source_name;
	}
	/**
	 * @param source_name 要设置的 source_name。
	 */
	public void setSource_name(String source_name) {
		this.source_name = source_name;
	}
	/**
	 * @return 返回 source_orgtree。
	 */
	public String getSource_orgtree() {
		return source_orgtree;
	}
	/**
	 * @param source_orgtree 要设置的 source_orgtree。
	 */
	public void setSource_orgtree(String source_orgtree) {
		this.source_orgtree = source_orgtree;
	}
	/**
	 * @return 返回 tag_date。
	 */
	public Timestamp getTag_date() {
		return tag_date;
	}
	/**
	 * @param tag_date 要设置的 tag_date。
	 */
	public void setTag_date(Timestamp tag_date) {
		this.tag_date = tag_date;
	}
	/**
	 * @return 返回 tag_id。
	 */
	public String getTag_id() {
		return tag_id;
	}
	/**
	 * @param tag_id 要设置的 tag_id。
	 */
	public void setTag_id(String tag_id) {
		this.tag_id = tag_id;
	}
	/**
	 * @return 返回 tag_userid。
	 */
	public String getTag_userid() {
		return tag_userid;
	}
	/**
	 * @param tag_userid 要设置的 tag_userid。
	 */
	public void setTag_userid(String tag_userid) {
		this.tag_userid = tag_userid;
	}
	/**
	 * @return 返回 operate_type。
	 */
	public String getOperate_type() {
		return operate_type;
	}
	/**
	 * @param operate_type 要设置的 operate_type。
	 */
	public void setOperate_type(String operate_type) {
		this.operate_type = operate_type;
	}
	/**
	 * @return 返回 source_partyid。
	 */
	public String getSource_partyid() {
		return source_partyid;
	}
	/**
	 * @param source_partyid 要设置的 source_partyid。
	 */
	public void setSource_partyid(String source_partyid) {
		this.source_partyid = source_partyid;
	}
	/**
	 * @return 返回 dest_code。
	 */
	public String getDest_code() {
		return dest_code;
	}
	/**
	 * @param dest_code 要设置的 dest_code。
	 */
	public void setDest_code(String dest_code) {
		this.dest_code = dest_code;
	}
	/**
	 * @return 返回 dest_id。
	 */
	public String getDest_id() {
		return dest_id;
	}
	/**
	 * @param dest_id 要设置的 dest_id。
	 */
	public void setDest_id(String dest_id) {
		this.dest_id = dest_id;
	}
	/**
	 * @return 返回 dest_name。
	 */
	public String getDest_name() {
		return dest_name;
	}
	/**
	 * @param dest_name 要设置的 dest_name。
	 */
	public void setDest_name(String dest_name) {
		this.dest_name = dest_name;
	}
	/**
	 * @return 返回 dest_orgtree。
	 */
	public String getDest_orgtree() {
		return dest_orgtree;
	}
	/**
	 * @param dest_orgtree 要设置的 dest_orgtree。
	 */
	public void setDest_orgtree(String dest_orgtree) {
		this.dest_orgtree = dest_orgtree;
	}
}

