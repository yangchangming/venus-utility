package venus.portal.gbox.servlet;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import venus.frames.mainframe.util.Helper;
import venus.portal.doctype.util.IConstants;
import venus.portal.gbox.common.upload.FileUpload;
import venus.portal.gbox.resource.api.ResourceTypeAPI;
import venus.portal.gbox.resource.resourceinfo.bs.IResourceBs;
import venus.portal.gbox.resource.resourceinfo.util.IResourceConstants;
import venus.portal.gbox.resource.resourceinfo.vo.ResourceVo;
import venus.portal.gbox.util.FileTools;
import venus.portal.util.CommonFieldConstants;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * @author zhaoyapeng
 *
 * 此类作为fckeditor与资源库整合时作为图片资源流方式调用的servlet
 *
 */
public class ResourceOutPutImageServlet extends HttpServlet{
    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        outputImage(request, response);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache");
    }
    
    public void outputImage(HttpServletRequest request, HttpServletResponse response){
        String  resourceId= request.getParameter("id");
        IResourceBs bs = (IResourceBs)Helper.getBean(IResourceConstants.BS_KEY);
        ResourceVo vo = bs.findAll(resourceId);

        String resourceName = vo.getFileName();
        String resourceType = ResourceTypeAPI.getResourceTypeUploadPath(vo.getType());
        if ("1".equals(vo.getIsProtected())) {
            resourceName = FileUpload.getDefaultUploadPath() + resourceType + FileTools.FILE_SEPARATOR +  vo.getFileName();
        }

        File fileItem = new File(resourceName);
        if (fileItem.exists()) {
           String  filename = fileItem.getName();
           filename =filename.toLowerCase();
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
    }
    
    
    private static void outJpg(HttpServletResponse response, File fileItem){
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
            ImageIO.write(image, "jpeg", output);
            imageIn.close();
        }catch (IOException e) {
            e.printStackTrace();
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            try{
                imageIn.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    private static void outGif(HttpServletResponse response, File fileItem){
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
            e.printStackTrace();
        }finally{
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
    
    private static void outPng(HttpServletResponse response, File fileItem){
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
            e.printStackTrace();
        }finally{
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
    
    private static void outBmp(HttpServletResponse response, File fileItem){
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
            e.printStackTrace();
        }finally{
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
