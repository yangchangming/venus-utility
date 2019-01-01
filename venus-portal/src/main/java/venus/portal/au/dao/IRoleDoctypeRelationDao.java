package venus.portal.au.dao;

import venus.portal.au.model.RoleDoctypeRelation;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: qj
 * Date: 13-10-9
 * Time: 上午10:47
 * To change this template use File | Settings | File Templates.
 */
public interface IRoleDoctypeRelationDao {

    public void deleteAll(List relations);

    public int deleteByRoleIDAndWebsiteID(String roleID, String website_id);

    public void delete(RoleDoctypeRelation relations);

    public void update(RoleDoctypeRelation relations);

    public void save(RoleDoctypeRelation relations);

    public List queryAllRelation();

    public List findRelationByRoleID(String roleID);

    public List findRelationByRoleIDAndWebsiteID(String roleID, String websiteID);
}
