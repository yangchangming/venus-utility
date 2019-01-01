//代码生成时,文件路径: file:///C:/venustools/workspace/HR_II/src/com/use/comm/auuser/vo/AuUserVo.java
//代码生成时,系统时间: 2006-06-09 15:32:04.668
//代码生成时,操作系统用户: lidonghong

/*
 * 系统名称:PlatForm
 * 
 * 文件名称: venus.authority.au.auuser.vo --> AuUserVo.java
 * 
 * 功能描述:
 * 
 * 版本历史: 2006-06-09 15:32:04.658 创建1.0.0版 (甘硕)
 *  
 */

package venus.oa.authority.auuser.vo;

import venus.frames.base.vo.BaseValueObject;

import java.sql.Timestamp;

/**
 * 功能、用途、现存BUG:
 * 
 * @author 甘硕
 * @version 1.0.0
 * @see 需要参见的其它类
 * @since 1.0.0
 */

public class AuUserVo extends BaseValueObject implements Cloneable {
    
	//开始AU_USER的属性
			
	/**
     * id 表示: 主键
	 * 数据库中的注释: 
     */
     
	private String id;
		
	/**
     * party_id 表示: 团体ID
	 * 数据库中的注释: 
     */
     
	private String party_id;
		
	/**
     * login_id 表示: 登陆ID
	 * 数据库中的注释: 
     */
     
	private String login_id;
		
	/**
     * password 表示: 密码
	 * 数据库中的注释: 
     */
     
	private String password;
		
	/**
     * name 表示: 名称
	 * 数据库中的注释: 
     */
     
	private String name;
		
	/**
     * is_admin 表示: 是否管理员
	 * 数据库中的注释: 
     */
     
	private String is_admin;
		
	/**
     * agent_status 表示: 代理状态
	 * 数据库中的注释: 
     */
     
	private String agent_status;
		
	/**
     * system_code 表示: 所属系统
	 * 数据库中的注释: 
     */
     
	private String system_code;
		
	/**
     * create_date 表示: 创建日期
	 * 数据库中的注释: 
     */
     
	private Timestamp create_date;
		
	/**
     * modify_date 表示: 修改日期
	 * 数据库中的注释: 
     */
     
	private Timestamp modify_date;
	
	/**
     * enable_status 表示: 启用状态
	 * 数据库中的注释: 1－启用  0－禁用
     */
     
	private String enable_status;
	
	/**
     * retire_date 表示: 密码实效日期
	 * 数据库中的注释: 
     */
	
	private Timestamp retire_date;
	
	/**
     * failed_times 表示: 密码尝试失败次数
	 * 数据库中的注释: 
     */
	
	private Integer failed_times;
		
	//结束AU_USER的属性
	
	/**
	 *组织机构_冗余 
	 */
	private String owner_org; 
		
		
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
	//开始AU_USER的setter和getter方法
			
    /**
     * 设置主键
     * 
     * @param id 主键
     */
	public void setId(String id){
		this.id = id;
	}
	
    /**
     * 获得主键
     * 
     * @return 主键
     */
	public String getId(){
		return id;
	}
	
    /**
     * 设置团体ID
     * 
     * @param party_id 团体ID
     */
	public void setParty_id(String party_id){
		this.party_id = party_id;
	}
	
    /**
     * 获得团体ID
     * 
     * @return 团体ID
     */
	public String getParty_id(){
		return party_id;
	}
	
    /**
     * 设置登陆ID
     * 
     * @param login_id 登陆ID
     */
	public void setLogin_id(String login_id){
		this.login_id = login_id;
	}
	
    /**
     * 获得登陆ID
     * 
     * @return 登陆ID
     */
	public String getLogin_id(){
		return login_id;
	}
	
    /**
     * 设置密码
     * 
     * @param password 密码
     */
	public void setPassword(String password){
		this.password = password;
	}
	
    /**
     * 获得密码
     * 
     * @return 密码
     */
	public String getPassword(){
		return password;
	}
	
    /**
     * 设置名称
     * 
     * @param name 名称
     */
	public void setName(String name){
		this.name = name;
	}
	
    /**
     * 获得名称
     * 
     * @return 名称
     */
	public String getName(){
		return name;
	}
	
    /**
     * 设置是否管理员
     * 
     * @param is_admin 是否管理员
     */
	public void setIs_admin(String is_admin){
		this.is_admin = is_admin;
	}
	
    /**
     * 获得是否管理员
     * 
     * @return 是否管理员
     */
	public String getIs_admin(){
		return is_admin;
	}
	
    /**
     * 设置代理状态
     * 
     * @param agent_status 代理状态
     */
	public void setAgent_status(String agent_status){
		this.agent_status = agent_status;
	}
	
    /**
     * 获得代理状态
     * 
     * @return 代理状态
     */
	public String getAgent_status(){
		return agent_status;
	}
	
    /**
     * 设置所属系统
     * 
     * @param system_code 所属系统
     */
	public void setSystem_code(String system_code){
		this.system_code = system_code;
	}
	
    /**
     * 获得所属系统
     * 
     * @return 所属系统
     */
	public String getSystem_code(){
		return system_code;
	}
	
    /**
     * 设置创建日期
     * 
     * @param create_date 创建日期
     */
	public void setCreate_date(Timestamp create_date){
		this.create_date = create_date;
	}
	
    /**
     * 获得创建日期
     * 
     * @return 创建日期
     */
	public Timestamp getCreate_date(){
		return create_date;
	}
	
    /**
     * 设置修改日期
     * 
     * @param modify_date 修改日期
     */
	public void setModify_date(Timestamp modify_date){
		this.modify_date = modify_date;
	}
	
    /**
     * 获得修改日期
     * 
     * @return 修改日期
     */
	public Timestamp getModify_date(){
		return modify_date;
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
	 * @return 返回 failed_times。
	 */
	public Integer getFailed_times() {
		return failed_times;
	}
	/**
	 * @param failed_times 要设置的 failed_times。
	 */
	public void setFailed_times(Integer failed_times) {
		this.failed_times = failed_times;
	}	
	/**
	 * @return 返回 retire_date。
	 */
	public Timestamp getRetire_date() {
		return retire_date;
	}
	/**
	 * @param retire_date 要设置的 retire_date。
	 */
	public void setRetire_date(Timestamp retire_date) {
		this.retire_date = retire_date;
	}
	//结束AU_USER的setter和getter方法
    
	/**
	 * override method 'equals'
	 * 
	 * @param _other 与本对象比较的其它对象
	 * @return boolean 两个对象的各个属性是否都相等
	 */
    public boolean equals(Object _other) {
        if (_other == null) {
            return false;
        }

        if (_other == this) {
            return true;
        }

        if (!(_other instanceof AuUserVo)) {
            return false;
        }

        final AuUserVo _cast = (AuUserVo) _other;
        
        if (id == null ? _cast.id != id : !id.equals(_cast.id)) {
            return false;
        }
	
        if (party_id == null ? _cast.party_id != party_id : !party_id.equals(_cast.party_id)) {
            return false;
        }
	
        if (login_id == null ? _cast.login_id != login_id : !login_id.equals(_cast.login_id)) {
            return false;
        }
	
        if (password == null ? _cast.password != password : !password.equals(_cast.password)) {
            return false;
        }
	
        if (name == null ? _cast.name != name : !name.equals(_cast.name)) {
            return false;
        }
	
        if (is_admin == null ? _cast.is_admin != is_admin : !is_admin.equals(_cast.is_admin)) {
            return false;
        }
	
        if (agent_status == null ? _cast.agent_status != agent_status : !agent_status.equals(_cast.agent_status)) {
            return false;
        }
	
        if (system_code == null ? _cast.system_code != system_code : !system_code.equals(_cast.system_code)) {
            return false;
        }
	
        if (create_date == null ? _cast.create_date != create_date : !create_date.equals(_cast.create_date)) {
            return false;
        }
	
        if (modify_date == null ? _cast.modify_date != modify_date : !modify_date.equals(_cast.modify_date)) {
            return false;
        }
	
        return true;
    }

	/**
	 * override method 'hashCode'
	 * 
	 * @return int Hash码
	 */
	public int hashCode() {
		int _hashCode = 0;
		
		if (id != null) {
            _hashCode = 29 * _hashCode + id.hashCode();
        }
	
		if (party_id != null) {
            _hashCode = 29 * _hashCode + party_id.hashCode();
        }
	
		if (login_id != null) {
            _hashCode = 29 * _hashCode + login_id.hashCode();
        }
	
		if (password != null) {
            _hashCode = 29 * _hashCode + password.hashCode();
        }
	
		if (name != null) {
            _hashCode = 29 * _hashCode + name.hashCode();
        }
	
		if (is_admin != null) {
            _hashCode = 29 * _hashCode + is_admin.hashCode();
        }
	
		if (agent_status != null) {
            _hashCode = 29 * _hashCode + agent_status.hashCode();
        }
	
		if (system_code != null) {
            _hashCode = 29 * _hashCode + system_code.hashCode();
        }
	
		if (create_date != null) {
            _hashCode = 29 * _hashCode + create_date.hashCode();
        }
	
		if (modify_date != null) {
            _hashCode = 29 * _hashCode + modify_date.hashCode();
        }
	
		return _hashCode;
	}
	
	/**
	 * override method 'toString'
	 * 
	 * @return String 字符串表示
	 */
	public String toString() {
	    int index = 0; 
		StringBuffer rtbf = new StringBuffer();
		rtbf.append( "venus.authority.au.auuser.util.IAuUserConstants: " + super.toString() );
		
		rtbf.append("\n" + (++index) + ": " + "id='" + id + "'");
	
		rtbf.append("\n" + (++index) + ": " + "party_id='" + party_id + "'");
	
		rtbf.append("\n" + (++index) + ": " + "login_id='" + login_id + "'");
	
		rtbf.append("\n" + (++index) + ": " + "password='" + password + "'");
	
		rtbf.append("\n" + (++index) + ": " + "name='" + name + "'");
	
		rtbf.append("\n" + (++index) + ": " + "is_admin='" + is_admin + "'");
	
		rtbf.append("\n" + (++index) + ": " + "agent_status='" + agent_status + "'");
	
		rtbf.append("\n" + (++index) + ": " + "system_code='" + system_code + "'");
	
		rtbf.append("\n" + (++index) + ": " + "create_date='" + create_date + "'");
	
		rtbf.append("\n" + (++index) + ": " + "modify_date='" + modify_date + "'");
		
		rtbf.append("\n" + (++index) + ": " + "retire_date='" + retire_date + "'");
		
		rtbf.append("\n" + (++index) + ": " + "failed_times='" + failed_times + "'");
	
		return rtbf.toString();
	}
	
	/**
	 * override method 'clone'
	 *
	 * @see Object#clone()
	 * @return Object 克隆后对象
	 * @throws CloneNotSupportedException
	 */
	public Object clone() throws CloneNotSupportedException {
        super.clone();
        AuUserVo vo = new AuUserVo();
		
		vo.setId(id);
	
		vo.setParty_id(party_id);
	
		vo.setLogin_id(login_id);
	
		vo.setPassword(password);
	
		vo.setName(name);
	
		vo.setIs_admin(is_admin);
	
		vo.setAgent_status(agent_status);
	
		vo.setSystem_code(system_code);
	
		vo.setCreate_date(create_date);
	
		vo.setModify_date(modify_date);
		
		vo.setRetire_date(retire_date);
		
		vo.setFailed_times(failed_times);
	
        return vo;
    }
    
}	

