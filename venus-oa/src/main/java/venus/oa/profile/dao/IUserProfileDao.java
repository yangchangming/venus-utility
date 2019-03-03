/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.oa.profile.dao;

import venus.oa.profile.vo.UserProfileVo;
import venus.pub.lang.OID;

/**
 * @author changming.Y <changming.yang.ah@gmail.com>
 *
 */
public interface IUserProfileDao {

    /**
     * 创建用户配置项
     * @param vo
     * @return
     */
    public OID insert(UserProfileVo vo);

    /**
     * 根据团体id和属性key定位一条记录
     * @param partyid
     * @param propertykey
     * @return
     */
    public UserProfileVo find(String partyid, String propertykey);

    /**
     * 根据partyid和propertykey修改value
     * @param vo
     */
    public int updateValueByVo(UserProfileVo vo);

    /**
     * 根据UserProfileVo进行删除
     * @param vo
     */
    public void delete(UserProfileVo vo);

}

