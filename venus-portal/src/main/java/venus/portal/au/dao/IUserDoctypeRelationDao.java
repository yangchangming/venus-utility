package venus.portal.au.dao;

import venus.portal.au.model.UserDoctypeRelation;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: qj
 * Date: 13-10-9
 * Time: 上午10:47
 * To change this template use File | Settings | File Templates.
 */
public interface IUserDoctypeRelationDao {

    public void deleteAll(List relations);

    public int deleteByUserIDAndWebsiteID(String roleID, String website_id);

    public void delete(UserDoctypeRelation relations);

    public void update(UserDoctypeRelation relations);

    public void save(UserDoctypeRelation relations);

    public List queryAllRelation();

    public List findRelationByUserID(String userID);

    public List findRelationByUserIDAndWebsiteID(String userID, String websiteID);
}
