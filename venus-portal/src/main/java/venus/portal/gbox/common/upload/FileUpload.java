package venus.portal.gbox.common.upload;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import venus.portal.gbox.resource.api.OptionAPI;
import venus.portal.gbox.util.DateTools;
import venus.portal.gbox.util.FileTools;
import venus.frames.base.action.IRequest;
import venus.frames.base.action.IResponse;
import venus.frames.mainframe.util.Helper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class FileUpload {
    
    private int maxPostSize = 2048 * 1024 * 1024;  // 限制文件的上传大小，最大2G
    private long startPos = 0L;
    private long endPos =  0L;   
    private HttpServletResponse res = null;
    private HttpServletRequest req = null;
    private PrintWriter pw = null;
    private RandomAccessFile raf =null;
    private InputStream in = null;
    
    public FileUpload(IRequest request, IResponse response) throws Exception {
        res = (HttpServletResponse)response;
        req = (HttpServletRequest)request;
        res.setContentType("text/html;charset=UTF-8");    
        res.setContentType("multipart/mixed stream");        
        endPos = req.getContentLength(); 
        pw = res.getWriter();
    }
    
    /**
     * 返回日期路径，格式为:YYYY/MM
     * @return String 日期路径
     */
    public static String getDatePath() {
        return DateTools.getYear() + FileTools.FILE_SEPARATOR + DateTools.getMonth() + FileTools.FILE_SEPARATOR;
    }
    
    /**
     * 返回系统默认的上传路径，即WEB-INF/upload
     * @return String 返回系统默认的上传路径
     */
    public static String getDefaultUploadPath() {
        return Helper.getUploadPath();
    }
    
    /**
     * 返回指定的上传路径
     * @param filePath
     * @return String 指定的上传路径
     */
    public static String getUploadPath(String filePath) {
        return getUploadPath(filePath,true);
    }
    
    /**
     * 返回指定的上传路径
     * @param filePath
     * @param isProtectedFile
     * @return String 指定的上传路径
     */
    public static String getUploadPath(String filePath,boolean isProtectedFile) {
        if (filePath == null || "".equals(filePath))
            return getDefaultUploadPath();
        if (isProtectedFile)
            return (filePath.endsWith(FileTools.FILE_SEPARATOR)) ? (getDefaultUploadPath() + filePath) : (getDefaultUploadPath() + filePath + FileTools.FILE_SEPARATOR);
        else
            return (filePath.endsWith(FileTools.FILE_SEPARATOR)) ? (getAccessibleUploadPath() + filePath) : (getAccessibleUploadPath() + filePath + FileTools.FILE_SEPARATOR);
    }    
    
    /**
     * 返回可直接访问的上传路径
     * @return String 返回上传路径
     */
    public static String getAccessibleUploadPath() {
        String uploadpath = OptionAPI.getOptionValue(OptionAPI.getAccessibleUploadPathId());
        return (uploadpath.endsWith(FileTools.FILE_SEPARATOR)) ? uploadpath : uploadpath + FileTools.FILE_SEPARATOR;
    }
    
    /**
     * 返回系统默认的缩略图路径
     * @return String 系统默认的缩略图路径
     */
    private static String getDefaultThumbsPath() {
        return getDefaultUploadPath() + getThumbsPath();
    }    
        
    /**
     * 返回缩略图路径
     * @return String 缩略图路径
     */
    public static String getThumbsPath() {
        return "thumbs" + FileTools.FILE_SEPARATOR;
    }

    /**
     * 返回上传路径中指定路径下的缩略图路径
     * @param filePath 上传目录中的文件路径
     * @return String 缩略图路径
     */
    public static String getThumbsPath(String filePath) {
        return getThumbsPath(filePath,true);
    }    
    
    /**
     * 返回上传路径中指定路径下的缩略图路径
     * @param filePath 上传目录中的文件路径
     * @param isProtectedFile
     * @return String 缩略图路径
     */
    public static String getThumbsPath(String filePath,boolean isProtectedFile) {
        if (filePath == null || "".equals(filePath))
            return getDefaultThumbsPath();
        if (isProtectedFile)
            return (filePath.endsWith(FileTools.FILE_SEPARATOR)) ? (getDefaultUploadPath() + filePath + getThumbsPath()) : (getDefaultUploadPath() + filePath + FileTools.FILE_SEPARATOR + getThumbsPath());
        else
            return (filePath.endsWith(FileTools.FILE_SEPARATOR)) ? (getAccessibleUploadPath() + filePath + getThumbsPath()) : (getAccessibleUploadPath() + filePath + FileTools.FILE_SEPARATOR + getThumbsPath());
    }
    
    /**
     * 获得request中上传的文件对象集合
     * @return List 文件对象集合
     */
    public List<FileItem> getFileItems() {
        DiskFileItemFactory factory = new DiskFileItemFactory();    
        factory.setSizeThreshold(4096);    
        ServletFileUpload upload = new ServletFileUpload(factory);    
        upload.setHeaderEncoding("utf-8"); 
        upload.setSizeMax(maxPostSize);
        if (this.req == null)
            return null;
        try {
            List<FileItem> tempList = upload.parseRequest(req);
            List<FileItem> returnList = new ArrayList<FileItem>();
            for (int i = 0; i < tempList.size(); i++) { //过滤表单的请求
                FileItem fileItem = tempList.get(i);
                if (!fileItem.isFormField())
                    returnList.add(fileItem);
            }
            return returnList;
        } catch (FileUploadException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 将文件上传到服务器
     * @param fileItem
     * @return boolean 是否上传成功
     */    
    public boolean upload(FileItem fileItem) {
        return upload(getDefaultUploadPath(),null,fileItem);
    }
    
    /**
     * 将文件上传到服务器
     * @param dirPath
     * @param fileName
     * @param fileItem
     * @return boolean 是否上传成功
     */
    public boolean upload(String dirPath,String fileName,FileItem fileItem) {
        if (dirPath == null || "".equals(dirPath) || fileItem == null || fileItem.isFormField())
            return false;
        File uploadDir = new File(dirPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        File thumbsDir = new File(dirPath + getThumbsPath());
        if (!thumbsDir.exists())
            thumbsDir.mkdir();
        try {   
            String name = (fileName == null || "".equals(fileName)) ? fileItem.getName() : fileName; 
            File file = new File(dirPath + name);
            if (file.exists()) {
                startPos = file.length();
            }
            raf = new RandomAccessFile(dirPath + name,"rw");
            if (startPos > 0) {
                raf.seek(startPos);
            }
            byte[] b = new byte[1024];
            in = fileItem.getInputStream();
            int nRead = 0;
            while( (nRead = in.read(b)) > 0 && (startPos < endPos)) {
                raf.write(b,0,nRead);
                startPos += nRead;
            }            
            pw.write("true");
            pw.flush();
        } catch (Exception e) {    
            e.printStackTrace();    
            pw.write("false");
            pw.flush();
            return false;
        } finally {
            try {
                if (in != null)
                    in.close();
                if (raf != null)
                    raf.close();
                if (pw != null)
                    pw.close();                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }
    
    /**
     * 删除服务器上的文件
     * @param filePath
     * @return boolean 删除是否成功
     */    
    public static boolean delete(String filePath) {
        return FileTools.delete(filePath);
    }
    
    /**
     * 删除服务器上的文件夹及下面所有文件
     * @param dirPath 文件夹路径
     * @return boolean 删除是否成功
     */
    public static boolean deleteDir(String dirPath) {
        return FileTools.deleteDir(dirPath);
    }    
    
    /**
     * 删除服务器上的文件夹及下面所有文件
     * @param dirPath 文件夹路径
     * @param deleteDir 是否删除文件夹
     * @return boolean 删除是否成功
     */    
    public static boolean deleteDir(String dirPath,boolean deleteDir) {
        return FileTools.deleteDir(dirPath, deleteDir);
    }    
    
}
