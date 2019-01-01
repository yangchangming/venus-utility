package venus.portal.timerelease.dao.impl;

import venus.portal.timerelease.dao.IEwpDocTimeReleaseDao;
import venus.portal.timerelease.model.EwpDocumentTimeRelease;
import venus.frames.base.dao.BaseHibernateDao;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by ethan on 13-10-15.
 */
public class EwpDocTimeReleaseDaoImpl extends BaseHibernateDao implements IEwpDocTimeReleaseDao {
    public List<EwpDocumentTimeRelease> queryByDate(Timestamp date) {
        return super.find("FROM EwpDocumentTimeRelease where docPreReleaseTime<=?", date);
    }

    public List<EwpDocumentTimeRelease> queryByDocId(String docId) {
        return super.find("FROM EwpDocumentTimeRelease where docId='" + docId + "'");
    }

    public void update(EwpDocumentTimeRelease ewpDocumentTimeRelease) {
        super.update(ewpDocumentTimeRelease);
    }

    public void save(EwpDocumentTimeRelease ewpDocumentTimeRelease) {
        super.save(ewpDocumentTimeRelease);
    }

    public void delete(EwpDocumentTimeRelease ewpDocumentTimeRelease) {
        super.delete(ewpDocumentTimeRelease);
    }

}
