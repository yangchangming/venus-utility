package venus.portal.timerelease.dao;


import venus.portal.timerelease.model.EwpDocumentTimeRelease;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by ethan on 13-10-15.
 */
public interface IEwpDocTimeReleaseDao {

    List<EwpDocumentTimeRelease> queryByDate(Timestamp date);

    List<EwpDocumentTimeRelease> queryByDocId(String docId);

    void update(EwpDocumentTimeRelease ewpDocumentTimeRelease);

    void save(EwpDocumentTimeRelease ewpDocumentTimeRelease);

    void delete(EwpDocumentTimeRelease ewpDocumentTimeRelease);
}
