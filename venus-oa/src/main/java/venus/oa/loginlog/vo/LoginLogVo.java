package venus.oa.loginlog.vo;

import venus.frames.base.vo.BaseValueObject;
import java.sql.Timestamp;

public class LoginLogVo extends BaseValueObject implements Cloneable {

	private String id;
		
	/**
     * login_id 表示: 账号
	 * 数据库中的注释: 
     */
     
	private String login_id;
		
	/**
     * name 表示: 姓名
	 * 数据库中的注释: 
     */
     
	private String name;
	
	/**
     * party_id 表示: party_id
     * 数据库中的注释: 
     */
     
    private String party_id;
		
	/**
     * login_ip 表示: IP地址
	 * 数据库中的注释: 
     */
     
	private String login_ip;
	
	/**
	 * 客户端MAC地址
	 */
	private String login_mac;
		
	/**
     * ie 表示: IE版本
	 * 数据库中的注释: 
     */
     
	private String ie;
		
	/**
     * os 表示: 操作系统版本
	 * 数据库中的注释: 
     */
     
	private String os;
		
	/**
     * host 表示: 主机名
	 * 数据库中的注释: 
     */
     
	private String host;
		
	/**
     * logout_type 表示: 退出类型
	 * 数据库中的注释: 
     */
     
	private String logout_type;
		
	/**
     * login_time 表示: 登录时间
	 * 数据库中的注释: 
     */
     
	private Timestamp login_time;
		
	/**
     * logout_time 表示: 退出时间
	 * 数据库中的注释: 
     */
     
	private Timestamp logout_time;
	
	/**
     * logout_state 表示: 登录是否成功以或者失败的原因
	 */
     
	private String login_state;
	
	/**
	 * 加锁时间：可用场景例如加入黑名单的时间
	 */
	private Timestamp lock_time;
		
	//结束AU_LOGIN_LOG的属性
		
		
	//开始AU_LOGIN_LOG的setter和getter方法
			
    /**
     * 获得主键
     * 
     * @return 主键
     */
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
     * 获得账号
     * 
     * @return 账号
     */
	public String getLogin_id(){
		return login_id;
	}
	
    /**
     * 设置账号
     * 
     * @param login_id 账号
     */
	public void setLogin_id(String login_id){
		this.login_id = login_id;
	}
	
    /**
     * 获得姓名
     * 
     * @return 姓名
     */
	public String getName(){
		return name;
	}
	
    /**
     * 设置姓名
     * 
     * @param name 姓名
     */
	public void setName(String name){
		this.name = name;
	}
	
    /**
     * 获得IP地址
     * 
     * @return IP地址
     */
	public String getLogin_ip(){
		return login_ip;
	}
	
    /**
     * 设置IP地址
     * 
     * @param login_ip IP地址
     */
	public void setLogin_ip(String login_ip){
		this.login_ip = login_ip;
	}
	
    /**
     * 获得IE版本
     * 
     * @return IE版本
     */
	public String getIe(){
		return ie;
	}
	
    /**
     * 设置IE版本
     * 
     * @param ie IE版本
     */
	public void setIe(String ie){
		this.ie = ie;
	}
	
    /**
     * 获得操作系统版本
     * 
     * @return 操作系统版本
     */
	public String getOs(){
		return os;
	}
	
    /**
     * 设置操作系统版本
     * 
     * @param os 操作系统版本
     */
	public void setOs(String os){
		this.os = os;
	}
	
    /**
     * 获得主机名
     * 
     * @return 主机名
     */
	public String getHost(){
		return host;
	}
	
    /**
     * 设置主机名
     * 
     * @param host 主机名
     */
	public void setHost(String host){
		this.host = host;
	}
	
    /**
     * 获得退出类型
     * 
     * @return 退出类型
     */
	public String getLogout_type(){
		return logout_type;
	}
	
    /**
     * 设置退出类型
     * 
     * @param logout_type 退出类型
     */
	public void setLogout_type(String logout_type){
		this.logout_type = logout_type;
	}
	
    /**
     * 获得登录时间
     * 
     * @return 登录时间
     */
	public Timestamp getLogin_time(){
		return login_time;
	}
	
    /**
     * 设置登录时间
     * 
     * @param login_time 登录时间
     */
	public void setLogin_time(Timestamp login_time){
		this.login_time = login_time;
	}
	
    /**
     * 获得退出时间
     * 
     * @return 退出时间
     */
	public Timestamp getLogout_time(){
		return logout_time;
	}
	
    /**
     * 设置退出时间
     * 
     * @param logout_time 退出时间
     */
	public void setLogout_time(Timestamp logout_time){
		this.logout_time = logout_time;
	}
	
	/**
	 * @return 返回 login_state。
	 */
	public String getLogin_state() {
		return login_state;
	}
	/**
	 * @param login_state 要设置的 login_state。
	 */
	public void setLogin_state(String login_state) {
		this.login_state = login_state;
	}
	//结束AU_LOGIN_LOG的setter和getter方法

	/**
	 * @return the login_mac
	 */
	public String getLogin_mac() {
	    return login_mac;
	}

	/**
	 * @param login_mac the login_mac to set
	 */
	public void setLogin_mac(String login_mac) {
	    this.login_mac = login_mac;
	}

    /**
     * @return the lock_time
     */
    public Timestamp getLock_time() {
        return lock_time;
    }

    /**
     * @param lock_time the lock_time to set
     */
    public void setLock_time(Timestamp lock_time) {
        this.lock_time = lock_time;
    }

    /**
     * @return the party_id
     */
    public String getParty_id() {
        return party_id;
    }

    /**
     * @param party_id the party_id to set
     */
    public void setParty_id(String party_id) {
        this.party_id = party_id;
    }

}