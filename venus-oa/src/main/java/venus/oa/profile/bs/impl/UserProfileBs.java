/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.oa.profile.bs.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import venus.oa.profile.bs.IUserProfileBs;
import venus.oa.profile.dao.IUserProfileDao;
import venus.oa.profile.vo.UserProfileVo;
import venus.pub.lang.OID;

/**
 * @author changming.Y <changming.yang.ah@gmail.com>
 *
 */
@Service
public class UserProfileBs implements IUserProfileBs {

    @Autowired
    private IUserProfileDao userProfileDao;

    /* (non-Javadoc)
     * @see venus.authority.service.profile.bs.IUserProfileBs#insert(venus.authority.service.profile.vo.UserProfileVo)
     */
    public OID insert(UserProfileVo vo) {
        return userProfileDao.insert(vo);
    }


    /* (non-Javadoc)
     * @see venus.authority.service.profile.bs.IUserProfileBs#find(java.lang.String, java.lang.String)
     */
    public UserProfileVo find(String partyid, String propertykey) {
        return userProfileDao.find(partyid,propertykey);
    }


    /* (non-Javadoc)
     * @see venus.authority.service.profile.bs.IUserProfileBs#updateValueByVo(venus.authority.service.profile.vo.UserProfileVo)
     */
    public void updateValueByVo(UserProfileVo vo) {
        userProfileDao.updateValueByVo(vo);
    }


    /* (non-Javadoc)
     * @see venus.authority.service.profile.bs.IUserProfileBs#delete(venus.authority.service.profile.vo.UserProfileVo)
     */
    public void delete(UserProfileVo vo) {
        userProfileDao.delete(vo);
    }

}

