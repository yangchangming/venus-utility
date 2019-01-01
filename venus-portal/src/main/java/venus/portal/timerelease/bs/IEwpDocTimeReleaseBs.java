package venus.portal.timerelease.bs;

import javax.servlet.ServletRequest;
import java.sql.Timestamp;

/**
 * Created by ethan on 13-10-12.
 */

public interface IEwpDocTimeReleaseBs {

    /**
     * 根据当前时间发布所有需要发布的文档
     */
    void releaseDoc();

    /**
     * 根据 request 传入的 preReleaseDate 的 attribute 得到时间
     * 如果属性不为空，判断是否已有发布计划，如果存在替换旧有计划。
     *
     * @param request
     * @param docId
     */
    void documentTimeReleaseHandle(ServletRequest request, String docId);

    /**
     * 根据传入的文档ID查找该文档的预计发布时间
     *
     * @param docId
     * @return
     */
    Timestamp getReleaseTimeByDocumentId(String docId);
}
