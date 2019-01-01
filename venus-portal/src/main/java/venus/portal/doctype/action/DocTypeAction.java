package venus.portal.doctype.action;

import org.apache.commons.lang.StringUtils;
import use.tools.tookit.webfile.FileInfo;
import use.tools.tookit.webfile.FileUploador;
import use.tools.tookit.webfile.FileUploadorFactory;
import venus.frames.base.action.DefaultDispatchAction;
import venus.frames.base.action.IForward;
import venus.frames.base.action.IRequest;
import venus.frames.base.action.IResponse;
import venus.frames.base.bean.DefaultForm;
import venus.frames.mainframe.util.Helper;
import venus.oa.helper.LoginHelper;
import venus.portal.doctype.bs.IDocTypeBS;
import venus.portal.doctype.model.DocType;
import venus.portal.doctype.util.IConstants;
import venus.portal.util.BooleanConstants;
import venus.portal.util.CommonFieldConstants;
import venus.portal.util.HTMLTagConstants;
import venus.portal.util.MultiLanguageConstants;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

public class DocTypeAction extends DefaultDispatchAction implements IConstants {

    private IDocTypeBS getDocTypeBs() {
        IDocTypeBS docTypeBs = (IDocTypeBS) Helper.getBean(IConstants.DOCTYPE_BS);
        return docTypeBs;
    }

    /**
     * 进入欢迎页面
     *
     * @param formBean 前台封闭的数据对象
     * @param request  请求
     * @param response 响应
     * @return IForward  业务完成后的跳转对象
     * @throws Exception 抛出保存模板中出现的异常
     */
    public IForward queryAll(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        boolean isAdmin = LoginHelper.getIsAdmin(getHttpServletRequest(request));
        if (isAdmin) {
            return request.findForward(FORWARD_LIST_PAGE);
        }

        if (getDocTypeBs().checkAuthority(getHttpServletRequest(request))) {
            return request.findForward(FORWARD_LIST_PAGE);
        } else {
            return request.findForward(FORWARD_NOPOWER_PAGE);
        }
    }


    /**
     * 新增和修改时上传栏目图片
     *
     * @param form     前台封闭的数据对象
     * @param request  请求
     * @param response 响应
     * @return IForward 跳转对象，此方法为ajax调用，所以返回对象可以不设置.真正有意义的是流输出的是否验证通过消息.
     * @throws
     */
    public IForward upLoadDocTypePic(DefaultForm form, IRequest request, IResponse response) throws Exception {
        HttpServletRequest hRequest = (HttpServletRequest) request.getServletRequest();
        String uploadedFileName = UUID.randomUUID().toString();
        String uploadedFilePath = this.getServlet().getServletContext().getRealPath("/") + Helper.DEFAULT_UPLOAD_PATH;
        boolean hasUpload = false;
        FileUploador fu = FileUploadorFactory.createInstance();
        fu.setSizeMax(10 * 1024 * 1024 * 10); // 100M
        try {
            fu.parse(hRequest);
            FileInfo fi = fu.getFileInfo(UPLOADFILENAME);
            if (fi != null) {
                hasUpload = true;
                String contentType = fi.getContentType();
                if (null != contentType && StringUtils.isNotBlank(contentType)) {
                    contentType = contentType.toLowerCase();
                    if (contentType.endsWith(IMAGETYPE_IMAGE_PJPEG) || contentType.endsWith(IMAGETYPE_IMAGE_JPEG)
                            || contentType.endsWith(IMAGETYPE_IMAGE_JPG) || contentType.endsWith(IMAGETYPE_JPEG)) {
                        uploadedFileName = uploadedFileName + "." + IMAGETYPE_JPG;
                    } else if (contentType.endsWith(IMAGETYPE_IMAGE_GIF) || contentType.endsWith(IMAGETYPE_GIF)) {
                        uploadedFileName = uploadedFileName + "." + IMAGETYPE_GIF;
                    } else if (contentType.endsWith(IMAGETYPE_IMAGE_PNG) || contentType.endsWith(IMAGETYPE_PNG)) {
                        uploadedFileName = uploadedFileName + "." + IMAGETYPE_PNG;
                    } else if (contentType.endsWith(IMAGETYPE_IMAGE_BMP) || contentType.endsWith(IMAGETYPE_BMP)) {
                        uploadedFileName = uploadedFileName + "." + IMAGETYPE_BMP;
                    } else {
                        response.getServletResponse().setCharacterEncoding("utf-8");
                        response.getServletResponse().setContentType("text/plain");
                        String message = venus.frames.i18n.util.LocaleHolder.getMessage("udp.ewp.website.notsupportedimage");
                        response.getServletResponse().getWriter().print("{flag:\"fail\",message:\"" + message + "!\"}");
                        response.getServletResponse().getWriter().close();
                        return null;
                    }
                    if (hasUpload) {
                        fu.uploadFile(fi, uploadedFilePath, uploadedFileName);
                    }
                    response.getServletResponse().setCharacterEncoding("utf-8");
                    response.getServletResponse().setContentType("text/plain");
                    response.getServletResponse().getWriter().print("{flag:\"success\",message:\"" + uploadedFileName + "\"}");
                    response.getServletResponse().getWriter().close();
                }
            }
        } catch (Exception e) {
            logger.error("Exception Message：" + e);
        }
        return null;
    }

    /**
     * 验证前台传过来的栏目编码在此站点下是否唯一
     *
     * @param request  请求
     * @param response 响应
     * @return IForward 跳转对象，此方法为ajax调用，所以返回对象可以不设置.真正有意义的是流输出的是否验证通过消息.
     * @throws
     */
    public IForward checkViewCodeIsUnique(DefaultForm from, IRequest request, IResponse response) throws Exception {
        String checkResultMessage = getViewCodeCheckResultMessage(request);
        outputMessageToResponse(checkResultMessage, response);
        return null;
    }

    /**
     * 验证前台传过来的栏目名称在此站点下是否唯一
     *
     * @param request  请求
     * @param response 响应
     * @return IForward 跳转对象，此方法为ajax调用，所以返回对象可以不设置.真正有意义的是流输出的是否验证通过消息.
     * @throws
     */
    public IForward checkNameIsUnique(DefaultForm from, IRequest request, IResponse response) throws Exception {
        String checkResultMessage = getNameCheckResultMessage(request);
        outputMessageToResponse(checkResultMessage, response);
        return null;
    }

    /**
     * 把字符串输出到输出流
     *
     * @param message
     * @param response
     * @throws IOException 如网络错误时会抛出IO异常
     * @throws
     */
    private void outputMessageToResponse(String message, IResponse response) throws IOException {
        response.getServletResponse().setContentType(HTMLTagConstants.PLAIN＿CONTENTTYPE);
        response.getServletResponse().getWriter().print(message);
        response.getServletResponse().getWriter().close();
    }

    /**
     * 取得校验结果
     *
     * @param request
     * @return 校验结果字符串
     */
    private String getViewCodeCheckResultMessage(IRequest request) {
        String docTypeCode = request.getParameter(CommonFieldConstants.WAITING_FOR_CHECK_CODE);
        String webSiteId = request.getParameter(CommonFieldConstants.WEBSITE_ID);
        DocType docType = null;
        String checkMessage = null;
        String isUnique = BooleanConstants.YES;
        if (StringUtils.isNotBlank(webSiteId)) {
            docType = getDocTypeBs().getDocTypeByCode(docTypeCode, webSiteId);
        } else {
            checkMessage = venus.frames.i18n.util.LocaleHolder.getMessage(MultiLanguageConstants.UDP_EWP_DOCTYPE_SESSION_CLOSE);
        }
        if (docType != null) {
            checkMessage = venus.frames.i18n.util.LocaleHolder.getMessage(MultiLanguageConstants.UDP_EWP_DOCTYPE_VIEWCODE_HAS_EXIST);
            isUnique = BooleanConstants.NO;
        } else {
            checkMessage = venus.frames.i18n.util.LocaleHolder.getMessage(MultiLanguageConstants.UDP_EWP_DOCTYPE_VIEWCODE_HAS_NOEXIST);
            isUnique = BooleanConstants.YES;
        }
        return String.format(new StringBuffer().append("{").append(CommonFieldConstants.IS_UNIQUE).append(":\"%s\",").append(CommonFieldConstants.CHECK_MESSAGE).append(":\"%s\"}").toString(),
                isUnique, checkMessage);
    }

    /**
     * 取得栏目名称的校验结果
     *
     * @param request
     * @return 校验结果字符串
     */
    private String getNameCheckResultMessage(IRequest request) {
        String docTypeName = request.getParameter(CommonFieldConstants.WAITING_FOR_CHECK_NAME);
        String webSiteId = request.getParameter(CommonFieldConstants.WEBSITE_ID);
        DocType docType = null;
        String checkMessage = null;
        String isUnique = BooleanConstants.YES;
        if (StringUtils.isNotBlank(webSiteId)) {
            docType = getDocTypeBs().getDocTypeByName(docTypeName, webSiteId);
        } else {
            checkMessage = venus.frames.i18n.util.LocaleHolder.getMessage(MultiLanguageConstants.UDP_EWP_DOCTYPE_SESSION_CLOSE);
        }
        if (docType != null) {
            checkMessage = venus.frames.i18n.util.LocaleHolder.getMessage(MultiLanguageConstants.UDP_EWP_DOCTYPE_VIEWNAME_HAS_EXIST);
            isUnique = BooleanConstants.NO;
        } else {
            checkMessage = venus.frames.i18n.util.LocaleHolder.getMessage(MultiLanguageConstants.UDP_EWP_DOCTYPE_VIEWNAME_HAS_NOEXIST);
            isUnique = BooleanConstants.YES;
        }
        return String.format(new StringBuffer().append("{").append(CommonFieldConstants.IS_UNIQUE).append(":\"%s\",").append(CommonFieldConstants.CHECK_MESSAGE).append(":\"%s\"}").toString(),
                isUnique, checkMessage);
    }
}
