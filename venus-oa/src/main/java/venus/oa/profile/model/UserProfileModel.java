/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.oa.profile.model;

import venus.oa.authority.auuser.bo.AuUserBo;
import venus.oa.profile.bs.IUserProfileBs;
import venus.oa.profile.util.IContants;
import venus.oa.profile.vo.UserProfileVo;
import venus.frames.mainframe.util.Helper;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户概要配置
 * @author changming.Y <changming.yang.ah@gmail.com>
 *
 */
public class UserProfileModel {
    private String partyid;//用户标识
    private Map profileItems = new HashMap();//用户概要配置集合
    
    /**
     * 获得带事务的历史日志BS
     * @return
     */
    public IUserProfileBs getBs(){
        return (IUserProfileBs) Helper.getBean(IContants.BS_KEY);
    }
    
    public UserProfileModel(String partyid){
        this.partyid = partyid;
    }
    /**
     * 联合查询功能
     * @param sql 查询构建器
     * @return
     
    public List unionQuery(SqlBuilder sql){
        //TODO 实现
        return new ArrayList();
    }
    
    public void colum2RowInit(){
        CommonFunction function = (CommonFunction)Helper.getBean(IContants.COMMON_FUNCTION_UTIL);
        function.createFunction4Row2Line("Au_UserProfile", "VALUE", "PROPERTYKEY");
    }
    */
    /**
     * 初始化
     * @param partyid
     */
    public void init(String partyid) {
        this.partyid = partyid;
    }

    /**
     * 更新概要配置文件
     * @param key
     * @param value
     */
    public void updateProfile(String key, String value) {
        UserProfileVo vo = new UserProfileVo();
        vo.setPartyid(partyid);
        vo.setPropertykey(key);
        vo.setValue(value);
        if(containsProfile(key)){
            profileItems.remove(key);
            getBs().updateValueByVo(vo);
        }else{
            getBs().insert(vo);
        }
        profileItems.put(key, value); 
        
    }
    
    /**
     * 是否包含key，懒加载
     * @param key
     * @return
     */
    public boolean containsProfile(String key){
        if(!profileItems.containsKey(key)){
            UserProfileVo vo = getBs().find(partyid, key);
            if(null!=vo){
                profileItems.put(vo.getPropertykey(), vo.getValue());
            }
        }
        return profileItems.containsKey(key);
    }

    /**
     * 用户配置项用户信息
     * @return
     */
    public void whoAmI(Object vo) {
        //TODO 动态设置vo
        ((AuUserBo)vo).setParty_id(partyid);
    }

    /**
     * 获取配置项的值，快照状态
     * @param key
     * @return
     */
    public String snapshotValue(String key) {
        if(containsProfile(key)){
            return (String) profileItems.get(key);
        }else{
            return null;
        }
    }    
    
    /**
     * 更新概要配置文件
     * @param key
     * @param value
     */
    public void removeProfile() {
        UserProfileVo vo = new UserProfileVo();
        vo.setPartyid(partyid);
        getBs().delete(vo);
        profileItems=Collections.EMPTY_MAP;
    }
}

