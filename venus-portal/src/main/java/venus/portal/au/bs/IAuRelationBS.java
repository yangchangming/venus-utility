package venus.portal.au.bs;

import java.util.HashSet;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: qj
 * Date: 13-10-9
 * Time: 上午10:46
 * To change this template use File | Settings | File Templates.
 */
public interface IAuRelationBS {
    public void saveOrUpdateDoctypeRelations(String pType, String partyId, String websiteID, String[] doctypes);

    public List queryDoctypeRelations(String pType, String partyId, String websiteID);

    public List queryDoctypes(String pType, String partyId, String websiteID);

    public HashSet queryDoctypes(List<String> partyIdUsers, List<String> partyIdRoles, String websiteID);
}
