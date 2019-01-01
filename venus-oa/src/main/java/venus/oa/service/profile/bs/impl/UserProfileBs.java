/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.oa.service.profile.bs.impl;

import venus.oa.service.profile.bs.IUserProfileBs;
import venus.oa.service.profile.dao.IUserProfileDao;
import venus.oa.service.profile.vo.UserProfileVo;
import venus.pub.lang.OID;


/**
 * @author changming.Y <changming.yang.ah@gmail.com>
 *
 */
public class UserProfileBs implements IUserProfileBs {
    private IUserProfileDao dao;
        
    /**
     * @param dao the dao to set
     */
    public void setDao(IUserProfileDao dao) {
        this.dao = dao;
    }


    /* (non-Javadoc)
     * @see venus.authority.service.profile.bs.IUserProfileBs#insert(venus.authority.service.profile.vo.UserProfileVo)
     */
    public OID insert(UserProfileVo vo) {
        return dao.insert(vo);
    }


    /* (non-Javadoc)
     * @see venus.authority.service.profile.bs.IUserProfileBs#find(java.lang.String, java.lang.String)
     */
    public UserProfileVo find(String partyid, String propertykey) {
        return dao.find(partyid,propertykey);
    }


    /* (non-Javadoc)
     * @see venus.authority.service.profile.bs.IUserProfileBs#updateValueByVo(venus.authority.service.profile.vo.UserProfileVo)
     */
    public void updateValueByVo(UserProfileVo vo) {
        dao.updateValueByVo(vo);
    }


    /* (non-Javadoc)
     * @see venus.authority.service.profile.bs.IUserProfileBs#delete(venus.authority.service.profile.vo.UserProfileVo)
     */
    public void delete(UserProfileVo vo) {
        dao.delete(vo);
    }

}

