package venus.portal.timerelease.dao;

import venus.portal.timerelease.model.EwpDocumentTimeReleaseLog;

/**
 * Created by ethan on 13-10-15.
 */
public interface IEwpDocTimeReleaseLogDao {

    void update(EwpDocumentTimeReleaseLog ewpDocumentTimeReleaseLog);

    void save(EwpDocumentTimeReleaseLog ewpDocumentTimeReleaseLog);

    void delete(EwpDocumentTimeReleaseLog ewpDocumentTimeReleaseLog);
}
