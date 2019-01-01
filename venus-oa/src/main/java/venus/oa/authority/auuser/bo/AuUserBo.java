package venus.oa.authority.auuser.bo;

import venus.oa.authority.auuser.vo.AuUserVo;
import venus.frames.base.vo.BaseValueObject;

import java.sql.Timestamp;

/**
 * @author zangjian
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class AuUserBo extends BaseValueObject implements Cloneable {

	private AuUserVo userVo;
    
    private String owner_org; //所属机构

	/**
	 * @return 返回 userVo。
	 */
	public AuUserVo getUserVo() {
		return userVo;
	}
	/**
	 * @param userVo 要设置的 userVo。
	 */
	public void setUserVo(AuUserVo userVo) {
		this.userVo = userVo;
	}
	
	//开始AU_USER的setter和getter方法
		
	/**
	* 设置主键
	* 
	* @param id 主键
	*/
	public void setId(String id){
		userVo.setId(id);
	}
	
	/**
	* 获得主键
	* 
	* @return 主键
	*/
	public String getId(){
		return userVo.getId();
	}
	
	/**
	* 设置团体ID
	* 
	* @param party_id 团体ID
	*/
	public void setParty_id(String party_id){
		userVo.setParty_id(party_id);
	}
	
	/**
	* 获得团体ID
	* 
	* @return 团体ID
	*/
	public String getParty_id(){
		return userVo.getParty_id();
	}
	
	/**
	* 设置登陆ID
	* 
	* @param login_id 登陆ID
	*/
	public void setLogin_id(String login_id){
		userVo.setLogin_id(login_id);
	}
	
	/**
	* 获得登陆ID
	* 
	* @return 登陆ID
	*/
	public String getLogin_id(){
		return userVo.getLogin_id();
	}
	
	/**
	* 设置密码
	* 
	* @param password 密码
	*/
	public void setPassword(String password){
		userVo.setPassword(password);
	}
	
	/**
	* 获得密码
	* 
	* @return 密码
	*/
	public String getPassword(){
		return userVo.getPassword();
	}
	
	/**
	* 设置名称
	* 
	* @param name 名称
	*/
	public void setName(String name){
		userVo.setName(name);
	}
	
	/**
	* 获得名称
	* 
	* @return 名称
	*/
	public String getName(){
		return userVo.getName();
	}
	
	/**
	* 设置是否管理员
	* 
	* @param is_admin 是否管理员
	*/
	public void setIs_admin(String is_admin){
		userVo.setIs_admin(is_admin);
	}
	
	/**
	* 获得是否管理员
	* 
	* @return 是否管理员
	*/
	public String getIs_admin(){
		return userVo.getIs_admin();
	}
	
	/**
	* 设置代理状态
	* 
	* @param agent_status 代理状态
	*/
	public void setAgent_status(String agent_status){
		userVo.setAgent_status(agent_status);
	}
	
	/**
	* 获得代理状态
	* 
	* @return 代理状态
	*/
	public String getAgent_status(){
		return userVo.getAgent_status();
	}
	
	/**
	* 设置所属系统
	* 
	* @param system_code 所属系统
	*/
	public void setSystem_code(String system_code){
		userVo.setSystem_code(system_code);
	}
	
	/**
	* 获得所属系统
	* 
	* @return 所属系统
	*/
	public String getSystem_code(){
		return userVo.getSystem_code();
	}
	
	/**
	* 设置创建日期
	* 
	* @param create_date 创建日期
	*/
	public void setCreate_date(Timestamp create_date){
		userVo.setCreate_date(create_date);
	}
	
	/**
	* 获得创建日期
	* 
	* @return 创建日期
	*/
	public Timestamp getCreate_date(){
		return userVo.getCreate_date();
	}
	
	/**
	* 设置修改日期
	* 
	* @param modify_date 修改日期
	*/
	public void setModify_date(Timestamp modify_date){
		userVo.setModify_date(modify_date);
	}
	
	/**
	* 获得修改日期
	* 
	* @return 修改日期
	*/
	public Timestamp getModify_date(){
		return userVo.getModify_date();
	}
	
	/**
	* @return 返回 enable_status。
	*/
	public String getEnable_status() {
		return userVo.getEnable_status();
	}
	/**
	* @param enable_status 要设置的 enable_status。
	*/
	public void setEnable_status(String enable_status) {
		userVo.setEnable_status(enable_status);
	}
	/**
	* @return 返回 failed_times。
	*/
	public Integer getFailed_times() {
		return userVo.getFailed_times();
	}
	/**
	* @param failed_times 要设置的 failed_times。
	*/
	public void setFailed_times(Integer failed_times) {
		userVo.setFailed_times(failed_times);
	}	
	/**
	* @return 返回 retire_date。
	*/
	public Timestamp getRetire_date() {
		return userVo.getRetire_date();
	}
	/**
	* @param retire_date 要设置的 retire_date。
	*/
	public void setRetire_date(Timestamp retire_date) {
		userVo.setRetire_date(retire_date);
	}
	//结束AU_USER的setter和getter方法	
	
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

