package venus.oa.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import use.tools.tookit.webfile.FileInfo;
import use.tools.tookit.webfile.FileUploador;
import use.tools.tookit.webfile.FileUploadorFactory;
import use.tools.tookit.webfile.FilenameFilter;
import venus.frames.base.action.IRequest;
import venus.frames.mainframe.util.PathMgr;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * @author maxiao
 *
 */
public class FileUpLoad {
    private static Log logger = LogFactory.getLog(FileUpLoad.class);
    private String uploadedFilePath = null;
    
    /**
     * @return 返回 uploadedFilePath。
     */
    public String getUploadedFilePath() {
        return uploadedFilePath;
    }
    /**
     * @param uploadedFilePath 要设置的 uploadedFilePath。
     */
    public void setUploadedFilePath(String uploadedFilePath) {
        this.uploadedFilePath = uploadedFilePath;
    }
    /**
     * 文件上传
     */
    public String onFileUpload(IRequest request) throws Exception{
        
        HttpServletRequest hRequest=(HttpServletRequest) request.getServletRequest();
        String uploadedFileName = "";
        
        boolean hasUpload = false;
                
        //创建上传附件解析器，目前只支持默认创建
        FileUploador fu = FileUploadorFactory.createInstance();
        
        //设置临时目录
        //fu.setRepository("d:");
        uploadedFilePath = PathMgr.getSingleton().getRealPath("/WEB-INF/conf/xmport/classes/");
        
        //设置缓冲区大小
        //fu.setSizeThreshold(512*1024);//512K
        
        //设置最大只允许附件大小
        fu.setSizeMax(10*1024*1024*10); //100M
        
        //设置文件类型过滤器
        FilenameFilter nf = new FilenameFilter(new String[]{"class","java","jar"});
        
        try {
            //fu.parse(hRequest);
            //fu.parse(request,"utf-8");    //设置解析编码为utf-8，默认是GBK
            fu.parse(hRequest,nf);         //设置过滤器
            //fu.parse(request,nf,"utf-8"); //设置过滤器和解析编码
            
            //获取所有的附件对象
            //List list = fu.getFileInfos();
            
            //获取附件对象.
            //如果附件对象不存在，或者大小、类型不符合要求，则返回null
            FileInfo fi = fu.getFileInfo("filename");
            if(fi!=null){
                hasUpload = true;
            }else{
                throw new Exception("仅支持jar、class和java扩展名文件！");
            }
            if(hasUpload){
                
                uploadedFileName += fu.getFieldFileName("filename");
                fu.uploadFile(fi,uploadedFilePath,uploadedFileName);

            }
            int beginIndex = uploadedFileName.lastIndexOf('.');
            String subStr = uploadedFileName.substring(0, beginIndex);
            request.setAttribute("filename", fi.getFieldFullPath());
            request.setAttribute("java_name",subStr);
            //return uploadedFilePath+"/"+uploadedFileName;
            return uploadedFilePath+File.separator+uploadedFileName;
        } catch (Exception e) {
            logger.error(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Exception_information_")+e);
            throw new Exception(e.getMessage());
        }
    }
}

