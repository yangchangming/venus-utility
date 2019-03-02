package venus.oa.login.vo;

import venus.frames.base.vo.BaseValueObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginSessionVo extends BaseValueObject {

    //团体ID
    private String party_id;
    
    //登陆ID 
    private String login_id;
    
    //密码 
    private String password;

    //名称 
    private String name;

    //是否管理员 
    private String is_admin;

    //代理状态 
    private String agent_status;
    
    //团体关系类型
    private String relationtype_id;
    
    //当前关系编码
    private String current_code;
    
    
    //所属团体关系vo列表（每个vo又包含其所有上级节点的vo）
    private List relation_vo_list;
    
    //拥有权限的组织机构
    private String[] owner_org_arr;
    
    //拥有权限的字段
    private Map owner_fild_map;
    
    //拥有权限的记录
    private Map owner_recd_map;
    
    //拥有权限的按钮
    private Map owner_butn_map;
    
    //拥有权限的菜单
    private Map owner_menu_map;
    
    //拥有权限的菜单+按钮
    private Map owner_func_map;
    
    //拥有权限的功能菜单的url
    private Map owner_menu_url_map;
    
    //全部功能菜单的url
    private Map all_menu_url_map;
    
    //授权管理员拥有权限的组织机构
    //private String[] owner_org_arr_admin;
    
    //授权管理员拥有权限的字段
    private Map owner_fild_map_admin;
    
    //授权管理员拥有权限的记录
    private Map owner_recd_map_admin;
    
    //授权管理员拥有权限的按钮
    private Map owner_butn_map_admin;
    
    //授权管理员拥有权限的菜单
    private Map owner_menu_map_admin;
    
    //授权管理员拥有权限的菜单+按钮
    private Map owner_func_map_admin;
    
    //拥有的功能数据权限
    private Map owner_fun_orga;
    //所有注册的字段
    private Map all_field_map;
    
    //结束属性

    //开始setter和getter方法

    
    
	/**
     * 设置团体ID
     * 
     * @param party_id 团体ID
     */
    public void setParty_id(String party_id) {
        this.party_id = party_id;
    }

    /**
     * 获得团体ID
     * 
     * @return 团体ID
     */
    public String getParty_id() {
        return party_id;
    }

    /**
     * 设置登陆ID
     * 
     * @param login_id 登陆ID
     */
    public void setLogin_id(String login_id) {
        this.login_id = login_id;
    }

    /**
     * 获得登陆ID
     * 
     * @return 登陆ID
     */
    public String getLogin_id() {
        return login_id;
    }

    /**
     * 设置密码
     * 
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获得密码
     * 
     * @return 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置名称
     * 
     * @param name 名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获得名称
     * 
     * @return 名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置是否管理员
     * 
     * @param is_admin 是否管理员
     */
    public void setIs_admin(String is_admin) {
        this.is_admin = is_admin;
    }

    /**
     * 获得是否管理员
     * 
     * @return 是否管理员
     */
    public String getIs_admin() {
        return is_admin;
    }

    /**
     * 设置代理状态
     * 
     * @param agent_status 代理状态
     */
    public void setAgent_status(String agent_status) {
        this.agent_status = agent_status;
    }

    /**
     * 获得代理状态
     * 
     * @return 代理状态
     */
    public String getAgent_status() {
        return agent_status;
    }
    
    
    /**
     * @return 返回 all_menu_url_map。
     */
    public Map getAll_menu_url_map() {
        return all_menu_url_map;
    }
    /**
     * @param all_menu_url_map 要设置的 all_menu_url_map。
     */
    public void setAll_menu_url_map(Map all_menu_url_map) {
        this.all_menu_url_map = all_menu_url_map;
    }
    /**
     * @return 返回 owner_butn_map。
     */
    public Map getOwner_butn_map() {
        return owner_butn_map;
    }
    /**
     * @param owner_butn_map 要设置的 owner_butn_map。
     */
    public void setOwner_butn_map(Map owner_butn_map) {
        this.owner_butn_map = owner_butn_map;
    }
    /**
     * @return 返回 owner_fild_map。
     */
    public Map getOwner_fild_map() {
        return owner_fild_map;
    }
    /**
     * @param owner_fild_map 要设置的 owner_fild_map。
     */
    public void setOwner_fild_map(Map owner_fild_map) {
        this.owner_fild_map = owner_fild_map;
    }
    /**
     * @return 返回 owner_menu_map。
     */
    public Map getOwner_menu_map() {
        if(owner_menu_map==null)
			return null;
        //补充菜单缺少的父节点
        String[] keys = (String[])owner_menu_map.keySet().toArray(new String[0]);
		for( int i=0; i<keys.length; i++ ) {
			String key = keys[i];
			key = key.substring(0,key.length()-3);
			while(key.length()>0) {
				if(!owner_menu_map.keySet().contains(key)) {
				    owner_menu_map.put(key,"");
				}
				key = key.substring(0,key.length()-3);
			}
		}
        return owner_menu_map;
    }
    /**
     * @param owner_menu_map 要设置的 owner_menu_map。
     */
    public void setOwner_menu_map(Map owner_menu_map) {
        this.owner_menu_map = owner_menu_map;
    }
    /**
     * @return 返回 owner_menu_url_map。
     */
    public Map getOwner_menu_url_map() {
        return owner_menu_url_map;
    }
    /**
     * @param owner_menu_url_map 要设置的 owner_menu_url_map。
     */
    public void setOwner_menu_url_map(Map owner_menu_url_map) {
        this.owner_menu_url_map = owner_menu_url_map;
    }
    /**
     * @return 返回 owner_org_arr。
     */
    public String[] getOwner_org_arr() {
        return owner_org_arr;
    }
    /**
     * @param owner_org_arr 要设置的 owner_org_arr。
     */
    public void setOwner_org_arr(String[] owner_org_arr) {
        this.owner_org_arr = owner_org_arr;
    }
    /**
     * @return 返回 owner_recd_map。
     */
    public Map getOwner_recd_map() {
        return owner_recd_map;
    }
    /**
     * @param owner_recd_map 要设置的 owner_recd_map。
     */
    public void setOwner_recd_map(Map owner_recd_map) {
        this.owner_recd_map = owner_recd_map;
    }
    /**
     * @return 返回 relation_vo_list。
     */
    public List getRelation_vo_list() {
        return relation_vo_list;
    }
    /**
     * @param relation_vo_list 要设置的 relation_vo_list。
     */
    public void setRelation_vo_list(List relation_vo_list) {
        this.relation_vo_list = relation_vo_list;
    }
    
    /**
     * @return 返回 owner_butn_map_admin。
     */
    public Map getOwner_butn_map_admin() {
        return owner_butn_map_admin;
    }
    /**
     * @param owner_butn_map_admin 要设置的 owner_butn_map_admin。
     */
    public void setOwner_butn_map_admin(Map owner_butn_map_admin) {
        this.owner_butn_map_admin = owner_butn_map_admin;
    }
    /**
     * @return 返回 owner_fild_map_admin。
     */
    public Map getOwner_fild_map_admin() {
        return owner_fild_map_admin;
    }
    /**
     * @param owner_fild_map_admin 要设置的 owner_fild_map_admin。
     */
    public void setOwner_fild_map_admin(Map owner_fild_map_admin) {
        this.owner_fild_map_admin = owner_fild_map_admin;
    }
    /**
     * @return 返回 owner_menu_map_admin。
     */
    public Map getOwner_menu_map_admin() {
        if(owner_menu_map_admin==null)
			return null;
        //补充菜单缺少的父节点
        String[] keys = (String[])owner_menu_map_admin.keySet().toArray(new String[0]);
		for( int i=0; i<keys.length; i++ ) {
			String key = keys[i];
			key = key.substring(0,key.length()-3);
			while(key.length()>0) {
				if(!owner_menu_map_admin.keySet().contains(key)) {
				    owner_menu_map_admin.put(key,"");
				}
				key = key.substring(0,key.length()-3);
			}
		}
        return owner_menu_map_admin;
    }
    /**
     * @param owner_menu_map_admin 要设置的 owner_menu_map_admin。
     */
    public void setOwner_menu_map_admin(Map owner_menu_map_admin) {
        this.owner_menu_map_admin = owner_menu_map_admin;
    }
    /**
     * @return 返回 owner_org_arr_admin。
     */
    /*
    public String[] getOwner_org_arr_admin() {
        return owner_org_arr_admin;
    }
    */
    /**
     * @param owner_org_arr_admin 要设置的 owner_org_arr_admin。
     */
    /*
    public void setOwner_org_arr_admin(String[] owner_org_arr_admin) {
        this.owner_org_arr_admin = owner_org_arr_admin;
    }
    */
    /**
     * @return 返回 owner_recd_map_admin。
     */
    public Map getOwner_recd_map_admin() {
        return owner_recd_map_admin;
    }
    /**
     * @param owner_recd_map_admin 要设置的 owner_recd_map_admin。
     */
    public void setOwner_recd_map_admin(Map owner_recd_map_admin) {
        this.owner_recd_map_admin = owner_recd_map_admin;
    }
    /**
     * @return 返回 owner_func_map。
     */
    public Map getOwner_func_map() {
        this.owner_func_map = new HashMap();
        this.owner_func_map.putAll(this.owner_menu_map);
        this.owner_func_map.putAll(this.owner_butn_map);
        //补充缺少的父节点
        String[] keys = (String[])this.owner_func_map.keySet().toArray(new String[0]);
		for( int i=0; i<keys.length; i++ ) {
			String key = keys[i];
			key = key.substring(0,key.length()-3);
			while(key.length()>0) {
				if( ! this.owner_func_map.keySet().contains(key)) {
				    this.owner_func_map.put(key,"");
				}
				key = key.substring(0,key.length()-3);
			}
		}
        return owner_func_map;
    }
    /**
     * @param owner_func_map 要设置的 owner_func_map。
     */
    public void setOwner_func_map(Map owner_func_map) {
        this.owner_func_map = owner_func_map;
    }
    /**
     * @return 返回 owner_func_map_admin。
     */
    public Map getOwner_func_map_admin() {
        this.owner_func_map_admin = new HashMap();
        this.owner_func_map_admin.putAll(this.owner_menu_map_admin);
        this.owner_func_map_admin.putAll(this.owner_butn_map_admin);
        //补充缺少的父节点
        String[] keys = (String[])this.owner_func_map_admin.keySet().toArray(new String[0]);
		for( int i=0; i<keys.length; i++ ) {
			String key = keys[i];
			key = key.substring(0,key.length()-3);
			while(key.length()>0) {
				if( ! this.owner_func_map_admin.keySet().contains(key)) {
				    this.owner_func_map_admin.put(key,"");
				}
				key = key.substring(0,key.length()-3);
			}
		}
        return owner_func_map_admin;
    }
    /**
     * @param owner_func_map_admin 要设置的 owner_func_map_admin。
     */
    public void setOwner_func_map_admin(Map owner_func_map_admin) {
        this.owner_func_map_admin = owner_func_map_admin;
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
	 * @return 返回 owner_fun_orga。
	 */
	public Map getOwner_fun_orga() {
		return owner_fun_orga;
	}
	/**
	 * @param owner_fun_orga 要设置的 owner_fun_orga。
	 */
	public void setOwner_fun_orga(Map owner_fun_orga) {
		this.owner_fun_orga = owner_fun_orga;
	}
	/**
	 * @return 返回 current_code。
	 */
	public String getCurrent_code() {
		return current_code;
	}
	/**
	 * @param current_code 要设置的 current_code。
	 */
	public void setCurrent_code(String current_code) {
		this.current_code = current_code;
	}

    public Map getAll_field_map() {
        return all_field_map;
    }

    public void setAll_field_map(Map all_field_map) {
        this.all_field_map = all_field_map;
    }
}

