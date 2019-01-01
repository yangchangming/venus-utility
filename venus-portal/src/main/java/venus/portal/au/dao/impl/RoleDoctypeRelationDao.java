package venus.portal.au.dao.impl;

import venus.portal.au.dao.IRoleDoctypeRelationDao;
import venus.portal.au.model.RoleDoctypeRelation;
import venus.portal.au.util.IConstants;
import venus.frames.base.dao.BaseHibernateDao;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: qj
 * Date: 13-10-9
 * Time: 上午10:41
 */
public class RoleDoctypeRelationDao  extends BaseHibernateDao implements IRoleDoctypeRelationDao,
        IConstants {

    public void deleteAll(List relations){
        super.deleteAll(relations);
    }

    public int deleteByRoleIDAndWebsiteID(String roleID,String website_id){
        String hql = "delete RoleDoctypeRelation rdr where rdr.roleID=:role_id and rdr.websiteID=:website_id";
        return super.getSession().createQuery(hql).setParameter("role_id", roleID).setParameter("website_id",website_id).executeUpdate();
    }

    public void delete(RoleDoctypeRelation relation) {
        super.delete(relation);
    }

    public void update(RoleDoctypeRelation relation) {
        super.update(relation);
    }

    public void save(RoleDoctypeRelation relation) {
        super.save(relation);
    }

    public List queryAllRelation() {
        return super.find("from RoleDoctypeRelation");
    }

    public List findRelationByRoleID(String roleID) {
        return super.find("from RoleDoctypeRelation rdr where rdr.roleID=?",roleID);
    }

    public List findRelationByRoleIDAndWebsiteID(String roleID,String websiteID) {
        return super.find("from RoleDoctypeRelation rdr where rdr.roleID=? and rdr.websiteID=?",new String[]{roleID, websiteID});
    }
}
