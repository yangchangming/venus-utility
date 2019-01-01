package venus.portal.au.bs.impl;

import org.apache.commons.lang.StringUtils;
import venus.oa.util.GlobalConstants;
import venus.portal.au.bs.IAuRelationBS;
import venus.portal.au.dao.IRoleDoctypeRelationDao;
import venus.portal.au.dao.IUserDoctypeRelationDao;
import venus.portal.au.model.RoleDoctypeRelation;
import venus.portal.au.model.UserDoctypeRelation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: qj
 * Date: 13-10-9
 * Time: 上午10:41
 */
public class AuRelationBS implements IAuRelationBS {
    private IRoleDoctypeRelationDao roleDoctypeRelationDao;
    private IUserDoctypeRelationDao userDoctypeRelationDao;

    public IRoleDoctypeRelationDao getRoleDoctypeRelationDao() {
        return roleDoctypeRelationDao;
    }

    public void setRoleDoctypeRelationDao(IRoleDoctypeRelationDao roleDoctypeRelationDao) {
        this.roleDoctypeRelationDao = roleDoctypeRelationDao;
    }

    public IUserDoctypeRelationDao getUserDoctypeRelationDao() {
        return userDoctypeRelationDao;
    }

    public void setUserDoctypeRelationDao(IUserDoctypeRelationDao userDoctypeRelationDao) {
        this.userDoctypeRelationDao = userDoctypeRelationDao;
    }

    private void saveOrUpdateRoleDoctypeRelations(String roleID, String websiteID, String[] doctypes) {
        roleDoctypeRelationDao.deleteByRoleIDAndWebsiteID(roleID, websiteID);
        for (String doctype : doctypes) {
            RoleDoctypeRelation roleDoctypeRelation = new RoleDoctypeRelation();
            roleDoctypeRelation.setDocTypeID(doctype);
            roleDoctypeRelation.setRoleID(roleID);
            roleDoctypeRelation.setWebsiteID(websiteID);
            roleDoctypeRelationDao.save(roleDoctypeRelation);
        }
    }

    private void saveOrUpdateUserDoctypeRelations(String userID, String websiteID, String[] doctypes) {
        userDoctypeRelationDao.deleteByUserIDAndWebsiteID(userID, websiteID);
        for (String doctype : doctypes) {
            if (!StringUtils.isBlank(doctype)) {
                UserDoctypeRelation userDoctypeRelation = new UserDoctypeRelation();
                userDoctypeRelation.setDocTypeID(doctype);
                userDoctypeRelation.setUserID(userID);
                userDoctypeRelation.setWebsiteID(websiteID);
                userDoctypeRelationDao.save(userDoctypeRelation);
            }
        }
    }

    public void saveOrUpdateDoctypeRelations(String pType, String partyId, String websiteID, String[] doctypes) {
        String[] doctypesAfter = new String[doctypes.length];
        (new HashSet<String>(Arrays.asList(doctypes))).toArray(doctypesAfter);// 去除数组中的重复id

        if (GlobalConstants.isPerson(pType)) {
            saveOrUpdateUserDoctypeRelations(partyId, websiteID, doctypesAfter);
        }
        if (GlobalConstants.isRole(pType)) {
            saveOrUpdateRoleDoctypeRelations(partyId, websiteID, doctypesAfter);
        }
    }

    private List queryRoleDoctypeRelations(String roleID, String websiteID) {
        return roleDoctypeRelationDao.findRelationByRoleIDAndWebsiteID(roleID, websiteID);

    }

    private List queryUserDoctypeRelations(String userID, String websiteID) {
        return userDoctypeRelationDao.findRelationByUserIDAndWebsiteID(userID, websiteID);
    }

    public List queryDoctypeRelations(String pType, String partyId, String websiteID) {
        if (GlobalConstants.isPerson(pType)) {
            return queryUserDoctypeRelations(partyId, websiteID);
        }
        if (GlobalConstants.isRole(pType)) {
            return queryRoleDoctypeRelations(partyId, websiteID);
        }
        return new ArrayList();
    }

    public List queryDoctypes(String pType, String partyId, String websiteID) {
        List rsList = new ArrayList();
        if (GlobalConstants.isPerson(pType)) {
            List<UserDoctypeRelation> list = queryUserDoctypeRelations(partyId, websiteID);
            for (UserDoctypeRelation o : list) {
                rsList.add(o.getDocTypeID());
            }
        }
        if (GlobalConstants.isRole(pType)) {
            List<RoleDoctypeRelation> list = queryRoleDoctypeRelations(partyId, websiteID);
            for (RoleDoctypeRelation o : list) {
                rsList.add(o.getDocTypeID());
            }
        }
        return rsList;
    }

    public HashSet queryDoctypes(List<String> partyIdUsers, List<String> partyIdRoles, String websiteID) {
        HashSet rsSet = new HashSet();
        for (String userId : partyIdUsers) {
            List<UserDoctypeRelation> list = queryUserDoctypeRelations(userId, websiteID);
            for (UserDoctypeRelation o : list) {
                rsSet.add(o.getDocTypeID());
            }
        }
        for (String roleId : partyIdRoles) {
            List<RoleDoctypeRelation> list = queryRoleDoctypeRelations(roleId, websiteID);
            for (RoleDoctypeRelation o : list) {
                rsSet.add(o.getDocTypeID());
            }
        }
        return rsSet;
    }

}
