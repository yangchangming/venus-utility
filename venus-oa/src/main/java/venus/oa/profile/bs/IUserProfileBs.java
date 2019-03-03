/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.oa.profile.bs;

import venus.oa.profile.vo.UserProfileVo;
import venus.pub.lang.OID;

/**
 * @author changming.Y <changming.yang.ah@gmail.com>
 *
 */
public interface IUserProfileBs {
    /**
     * 增加用户概要配置项
     * @param vo
     * @return
     */
    public OID insert(UserProfileVo vo);
    /**
     * 根据条件查询一条用户配置项
     * @param partyid 团体标识
     * @param propertykey 配置项的key
     * @return
     */
    public UserProfileVo find(String partyid, String propertykey);
    /**
     * 根据partyid和propertykey修改value
     * @param vo
     */
    public void updateValueByVo(UserProfileVo vo);
    /**根据UserProfileVo信息进行删除
     * @param vo
     */
    public void delete(UserProfileVo vo);
}

