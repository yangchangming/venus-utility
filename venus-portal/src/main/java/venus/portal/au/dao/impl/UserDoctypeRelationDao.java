package venus.portal.au.dao.impl;

import venus.frames.base.dao.BaseHibernateDao;
import venus.portal.au.dao.IUserDoctypeRelationDao;
import venus.portal.au.model.UserDoctypeRelation;
import venus.portal.au.util.IConstants;

import java.util.List;

//import venus.portal.au.util.IConstants;

/**
 * Created with IntelliJ IDEA.
 * User: qj
 * Date: 13-10-9
 * Time: 上午10:41
 */
public class UserDoctypeRelationDao extends BaseHibernateDao implements IUserDoctypeRelationDao,
        IConstants {

    public void deleteAll(List relations){
        super.deleteAll(relations);
    }

    public void delete(UserDoctypeRelation relation) {
        super.delete(relation);
    }

    public int deleteByUserIDAndWebsiteID(String userID,String website_id){
        String hql = "delete UserDoctypeRelation udr where udr.userID=:user_id and udr.websiteID=:website_id";
        return super.getSession().createQuery(hql).setParameter("user_id", userID).setParameter("website_id",website_id).executeUpdate();
    }

    public void update(UserDoctypeRelation relation) {
        super.update(relation);
    }

    public void save(UserDoctypeRelation relation) {
        super.save(relation);
    }

    public List queryAllRelation() {
        return super.find("from UserDoctypeRelation");
    }

    public List findRelationByUserID(String userID) {
        return super.find("from UserDoctypeRelation udr where udr.userID=?",userID);
    }

    public List findRelationByUserIDAndWebsiteID(String userID,String websiteID) {
        return super.find("from UserDoctypeRelation udr where udr.userID=? and udr.websiteID=?",new String[]{userID, websiteID});
    }
}
