package venus.portal.gbox.common.upload;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import venus.frames.base.action.DefaultDispatchAction;
import venus.frames.base.action.IForward;
import venus.frames.base.action.IRequest;
import venus.frames.base.action.IResponse;
import venus.frames.base.bean.DefaultForm;
import venus.frames.mainframe.util.Helper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.Iterator;
import java.util.List;

public class FileUploadAction extends DefaultDispatchAction {
    
    // 定义文件的上传路径    
    private String uploadPath = Helper.getUploadPath();
    
    // 限制文件的上传大小    
    private int maxPostSize = 2048 * 1024 * 1024;    //最大2G
    
    public IForward upload(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        HttpServletResponse res = (HttpServletResponse)response;
        HttpServletRequest req = (HttpServletRequest)request;
        res.setContentType("text/html;charset=UTF-8");    
        res.setContentType("multipart/mixed stream");
        
        long startPos = 0L;
        long endPos =  req.getContentLength();
        
        PrintWriter pw = res.getWriter();
        //保存文件到服务器中    
        DiskFileItemFactory factory = new DiskFileItemFactory();    
        factory.setSizeThreshold(4096);    
        ServletFileUpload upload = new ServletFileUpload(factory);    
        upload.setHeaderEncoding("utf-8"); 
        upload.setSizeMax(maxPostSize);    
        
        RandomAccessFile raf =null;
        InputStream in = null;
        
        try {    
               List fileItems = upload.parseRequest(req);    
               Iterator iter = fileItems.iterator();    
               while (iter.hasNext()) {    
                       FileItem item = (FileItem) iter.next();    
                       if (!item.isFormField()) {    
                               String name = item.getName(); 
                               System.out.println("upload-------------------" + name);    
                               try {   
                                       //if (startPos == 0) {
                                           File file = new File(uploadPath + name);
                                           if(file.exists()) {
                                               startPos = file.length();
                                               System.out.println("range----------" + startPos);
                                               //file.delete();
                                           }
                                           //file.createNewFile();
                                           //file = null;
                                       //}      
                                       
                                       raf = new RandomAccessFile(uploadPath + name,"rw");
                                       if (startPos > 0){
                                           //raf.setLength(startPos);
                                           raf.seek(startPos);
                                       }
                                       byte[] b = new byte[1024];
                                       in = item.getInputStream();
                                       int nRead = 0;
                                       while( (nRead = in.read(b)) > 0 && (startPos < endPos)) {
                                           raf.write(b,0,nRead);
                                           startPos += nRead;
                                       }            
                                       
                                       pw.write("true"); 
                               } catch (Exception e) {    
                                       e.printStackTrace();    
                                       pw.write("false"); 
                               }    
                       }    
               }    
       } catch (FileUploadException e) {    
               e.printStackTrace();    
               pw.write("false"); 
       }  finally{
           if (pw != null) {
               pw.flush();
               pw.close();
           }
           if (raf != null) {
               raf.close();
           }
           if (in != null) {
               in.close();
           }
       }
       return null;
    } 
    
}
