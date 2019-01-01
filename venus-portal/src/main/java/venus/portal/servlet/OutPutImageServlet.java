package venus.portal.servlet;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.apache.commons.lang.StringUtils;
import venus.frames.mainframe.log.ILog;
import venus.frames.mainframe.log.LogMgr;
import venus.frames.mainframe.util.Helper;
import venus.portal.doctype.bs.IDocTypeBS;
import venus.portal.doctype.util.IConstants;
import venus.portal.util.CommonFieldConstants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;


/**
 * 输出图片
 *
 *@author zhangrenyang 
 *@date  2011-9-19
 */
public class OutPutImageServlet extends HttpServlet{
    
    private static ILog log= LogMgr.getLogger(OutPutImageServlet.class);
    
    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        outputImage(request, response);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache");
    }
    
    public void outputImage(HttpServletRequest request, HttpServletResponse response){
        //如果有要删除的图片,先删除之
        String toBeDeletedFilename = request.getParameter("toBeDeletedFilename");
        if(StringUtils.isNotBlank(toBeDeletedFilename)){
            String toBeDeletedFilePath = this.getServletContext().getRealPath("/")+ Helper.DEFAULT_UPLOAD_PATH+File.separator+toBeDeletedFilename;
            File fileItem = new File(toBeDeletedFilePath);
            log.debug("toBeDeletedFilePath:"+toBeDeletedFilePath);
            log.debug("toBeDeletedFilePath fileItem:"+fileItem.getPath());
            log.debug("toBeDeletedFilePath fileItem.exists():"+fileItem.exists());
            if (fileItem.exists()) {
                try{
                    fileItem.delete();
                }catch(Exception ex){
                    log.error(ex.getMessage());
                   ex.printStackTrace();
                }
            }
        }
        //再上传新的图片
        String filename = request.getParameter("filename");
        if(StringUtils.isBlank(filename)|| StringUtils.equals(filename, CommonFieldConstants.UNDEFINED)){
            filename = CommonFieldConstants.DEFAULT_DOCTYPE_LOGO;
        }
        String name ="";
        if(StringUtils.equals(filename, CommonFieldConstants.DEFAULT_DOCTYPE_LOGO)){
             name = this.getServletContext().getRealPath("/")+"images"+File.separator+"ewp"+File.separator+filename;
        }else{
            name = this.getServletContext().getRealPath("/")+ Helper.DEFAULT_UPLOAD_PATH+File.separator+filename;
        }
        log.debug("name:"+name);
        File fileItem = new File(name);
        log.debug("fileItem:"+fileItem.getPath());
        log.debug("fileItem.exists():"+fileItem.exists());
        if (fileItem.exists()) {
             filename = fileItem.getName();
            if(filename.endsWith("."+ IConstants.IMAGETYPE_JPG) || filename.endsWith("."+ IConstants.IMAGETYPE_JPEG)){
                outJpg(response, fileItem);
            }else if(filename.endsWith("."+ IConstants.IMAGETYPE_GIF)){
                outGif(response, fileItem);
            }else if(filename.endsWith("."+ IConstants.IMAGETYPE_PNG)){
                outPng(response, fileItem);
            }else if(filename.endsWith("."+ IConstants.IMAGETYPE_BMP)){
                outBmp(response, fileItem);
            }
            response.setCharacterEncoding(CommonFieldConstants.DEFAULT_ENCODING);
            response.setHeader("Cache-Control", "no-cache");
        }
        //更新栏目图片
        String id = request.getParameter(CommonFieldConstants.ID);
        if(StringUtils.isNotBlank(id)){
            IDocTypeBS docTypeBs = (IDocTypeBS)Helper.getBean(IConstants.DOCTYPE_BS);
            docTypeBs.updateImagePath(id, filename);
        }
    }
    
    public static void outJpg(HttpServletResponse response, File fileItem){
        log.debug("outJpg() start!");
        OutputStream output = null;
        InputStream imageIn = null;
        try{
            response.setContentType("image/jpeg;charset=UTF-8");
            output = response.getOutputStream();
            imageIn = new FileInputStream(fileItem); 
            JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(imageIn);
            BufferedImage image = decoder.decodeAsBufferedImage();
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(output); 
            encoder.encode(image);
            imageIn.close();
        }catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }catch(Exception ex){
            log.error(ex.getMessage());
            ex.printStackTrace();
        }finally{
            log.debug("outJpg() end!");
            try{
                imageIn.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    public static void outGif(HttpServletResponse response, File fileItem){
        log.debug("outGif() start!");
        OutputStream output = null;
        InputStream imageIn = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            response.setContentType("image/gif;charset=UTF-8");
            output = response.getOutputStream();
            imageIn = new FileInputStream(fileItem); 
            bis = new BufferedInputStream(imageIn);
            bos = new BufferedOutputStream(output);
            
            byte data[] = new byte[4096];
            int size = bis.read(data); 
            
            while(size!=-1){
                bos.write(data,0,size);
                size=bis.read(data);
            }      
        }catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }finally{
            log.debug("outGif() end!");
            try{
                bis.close();    
                bos.flush();
                bos.close(); 
                output.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    public static void outPng(HttpServletResponse response, File fileItem){
        log.debug("outPng() start!");
        OutputStream output = null;
        InputStream imageIn = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            response.setContentType("image/png;charset=UTF-8");
            output = response.getOutputStream();
            imageIn = new FileInputStream(fileItem); 
            bis = new BufferedInputStream(imageIn);
            bos = new BufferedOutputStream(output);
            
            byte data[] = new byte[4096];
            int size = bis.read(data); 
            
            while(size!=-1){
                bos.write(data,0,size);
                size=bis.read(data);
            }      
        }catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }finally{
            log.debug("outPng() end!");
            try{
                bis.close();    
                bos.flush();
                bos.close(); 
                output.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    public static void outBmp(HttpServletResponse response, File fileItem){
        log.debug("outBmp() end!");
        OutputStream output = null;
        InputStream imageIn = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            response.setContentType("image/bmp;charset=UTF-8");
            output = response.getOutputStream();
            imageIn = new FileInputStream(fileItem); 
            bis = new BufferedInputStream(imageIn);
            bos = new BufferedOutputStream(output);
            
            byte data[] = new byte[4096];
            int size = bis.read(data); 
            
            while(size!=-1){
                bos.write(data,0,size);
                size=bis.read(data);
            }      
        }catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }finally{
            log.debug("outBmp() end!");
            try{
                bis.close();    
                bos.flush();
                bos.close(); 
                output.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
}
