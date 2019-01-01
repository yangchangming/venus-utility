package venus.portal.gbox.common.download;

import venus.frames.base.action.DefaultDispatchAction;
import venus.frames.base.action.IForward;
import venus.frames.base.action.IRequest;
import venus.frames.base.action.IResponse;
import venus.frames.base.bean.DefaultForm;
import venus.frames.mainframe.util.Helper;
import venus.frames.web.message.MessageAgent;
import venus.frames.web.message.MessageStyle;
import venus.portal.gbox.common.upload.FileUpload;
import venus.portal.gbox.resource.api.ResourceTypeAPI;
import venus.portal.gbox.resource.resourceinfo.bs.IResourceBs;
import venus.portal.gbox.resource.resourceinfo.util.IResourceConstants;
import venus.portal.gbox.resource.resourceinfo.vo.ResourceVo;

import javax.servlet.http.HttpServletResponse;
import java.io.*;


public class FileDownloadAction extends DefaultDispatchAction {

    public IResourceBs getBs() {
        return (IResourceBs) Helper.getBean(IResourceConstants.BS_KEY);
    }
    
    public IForward download(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        HttpServletResponse res = (HttpServletResponse)response;
        String id = request.getParameter("id");
        ResourceVo vo = getBs().find(id);
        String filePath = vo.getFileName();
        if ("0".equals(vo.getIsExternal())) {
            boolean isProtected = ("1".equals(vo.getIsProtected()) ? true : false);
            filePath = FileUpload.getUploadPath(ResourceTypeAPI.getResourceTypeUploadPath(vo.getType()), isProtected) + vo.getFileName();
        }
        File file = new File(filePath);
        if (!file.exists()) {
            return MessageAgent.sendErrorMessage(request, venus.frames.i18n.util.LocaleHolder.getMessage("udp.gbox.msg.nofoundfile"), MessageStyle.ALERT_AND_BACK);
        }
        String fileName = new String(vo.getName().replaceAll(" ", "").getBytes("GBK"),"ISO8859-1");
        String fileSize = vo.getFileSize();
        OutputStream output = new BufferedOutputStream(res.getOutputStream());
        try {
            InputStream fis = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            res.reset();
            res.setContentType("application/octet-stream;charset=GBK");
            res.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            res.addHeader("Content-Length", "" + fileSize);
            output.write(buffer);
        } catch (Exception e) {
                e.printStackTrace();
        } finally {
            if (output != null) {
                output.flush();
                output.close();
            }
        }
        return null;
    }
    
}
