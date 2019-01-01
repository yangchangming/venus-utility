package venus.portal.timerelease.bs.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import venus.frames.base.bs.BaseBusinessService;
import venus.frames.mainframe.util.Helper;
import venus.portal.document.bs.IDocumentBS;
import venus.portal.document.dao.IDocumentDao;
import venus.portal.document.model.Document;
import venus.portal.document.util.IConstants;
import venus.portal.gbox.util.DateTools;
import venus.portal.timerelease.bs.IEwpDocTimeReleaseBs;
import venus.portal.timerelease.dao.IEwpDocTimeReleaseDao;
import venus.portal.timerelease.dao.IEwpDocTimeReleaseLogDao;
import venus.portal.timerelease.model.EwpDocumentTimeRelease;
import venus.portal.timerelease.model.EwpDocumentTimeReleaseLog;
import venus.pub.lang.OID;

import javax.servlet.ServletRequest;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ethan on 13-10-12.
 */
public class EwpDocTimeReleaseBsImpl extends BaseBusinessService implements IEwpDocTimeReleaseBs,IConstants {

    @Autowired
    private IEwpDocTimeReleaseDao ewpDocTimeReleaseDao;

    @Autowired
    private IEwpDocTimeReleaseLogDao ewpDocTimeReleaseLogDao;

    @Autowired
    private IDocumentDao documentDao;

    @Autowired
    private IDocumentBS documentBS;

    public void releaseDoc() {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        DateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = dft.format(ts);

        Map<String, EwpDocumentTimeRelease> releaseMap = getReleaseMap(ts);

        if (releaseMap != null && releaseMap.size() > 0) {
            List<Document> docList = documentDao.getReleaseDocByDate(ts);
            for (Document doc : docList) {
                doc.setPublishTime(ts);
                doc.setStatus(DOC_STATUS_PUBLISHED);
                documentBS.updatePublish(doc, releaseMap.get(doc.getId()).getWebSiteId());
            }

            deleteReleaseJobAndLogIt(releaseMap);
        }

    }

    public void documentTimeReleaseHandle(ServletRequest request, String docId) {

        String preReleaseDate = request.getParameter("preReleaseDate");

        EwpDocumentTimeRelease ewpDocumentTimeRelease;
        List<EwpDocumentTimeRelease> recodes = ewpDocTimeReleaseDao.queryByDocId(docId);
        if (recodes != null && recodes.size() > 0) {
            // TODO 没有 check 是否只能有一条记录，只是默认选择了第1条记录
            logger.debug("It already had release job! So this time is update! The recodes is :" + recodes.size());
            ewpDocumentTimeRelease = recodes.get(0);

            // 若preReleaseDate为空，说明用户取消了该次定时发布
            if(StringUtils.isBlank(preReleaseDate)) {
                ewpDocTimeReleaseDao.delete(ewpDocumentTimeRelease);
                logger.info("The job " + ewpDocumentTimeRelease.getId() + " has been removed");
            } else {
                ewpDocumentTimeRelease.setDocPreReleaseTime(DateTools.getTimestamp(preReleaseDate));
                ewpDocTimeReleaseDao.update(ewpDocumentTimeRelease);
            }
        } else if (StringUtils.isBlank(preReleaseDate)) {
            logger.info("The attr preReleaseDate is Null, No Job");
        } else {
            String siteId = request.getParameter("siteId");
            OID oid = Helper.requestOID(EWP_DOCUMENT_TIME_RELEASE_ID);
            logger.debug("The new EwpDocumentTimeRelease object's ID is " + oid.toString());
            ewpDocumentTimeRelease = new EwpDocumentTimeRelease();
            ewpDocumentTimeRelease.setId(oid.toString());
            ewpDocumentTimeRelease.setDocId(docId);
            ewpDocumentTimeRelease.setWebSiteId(siteId);
            ewpDocumentTimeRelease.setDocPreReleaseTime(DateTools.getTimestamp(preReleaseDate));
            ewpDocTimeReleaseDao.save(ewpDocumentTimeRelease);
        }

    }

    private Map<String, EwpDocumentTimeRelease> getReleaseMap(Timestamp currentTime) {
        List<EwpDocumentTimeRelease> result = ewpDocTimeReleaseDao.queryByDate(currentTime);

        Map<String, EwpDocumentTimeRelease> map = new HashMap<String, EwpDocumentTimeRelease>(result.size());
        for(EwpDocumentTimeRelease vo : result) {
            map.put(vo.getDocId(), vo);
        }

        return map;
    }

    private void deleteReleaseJobAndLogIt(Map<String, EwpDocumentTimeRelease> releaseMap){
        for (EwpDocumentTimeRelease ewpDocTimeRelease : releaseMap.values()) {
            OID oid = Helper.requestOID(EWP_DOCUMENT_TIME_RELEASE_LOG_ID);
            EwpDocumentTimeReleaseLog ewpDocTRLog = ewpDocTimeRelease.tranToDocTimeReleaseLog();
            ewpDocTRLog.setId(oid.toString());
            ewpDocTimeReleaseLogDao.save(ewpDocTRLog);
            ewpDocTimeReleaseDao.delete(ewpDocTimeRelease);
        }
    }

    public Timestamp getReleaseTimeByDocumentId(String docId) {
        List<EwpDocumentTimeRelease> list = ewpDocTimeReleaseDao.queryByDocId(docId);
        if(list != null && list.size() > 0) {
            return list.get(0).getDocPreReleaseTime();
        }

        return null;
    }
}
