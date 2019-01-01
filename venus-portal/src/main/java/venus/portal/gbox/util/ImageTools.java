package venus.portal.gbox.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;

public class ImageTools {

    /**
     * 将图片的尺寸重置
     * @param source 图片流
     * @param width 图片宽度
     * @param height 图片高度
     * @return BufferedImage 缓存图片对象
     */
    public static BufferedImage resize(BufferedImage source, int width,int height) {
        int type = source.getType();
        BufferedImage target = null;
        double sx = (double) width / source.getWidth();
        double sy = (double) height / source.getHeight();
        // 这里想实现在width，height范围内实现等比缩放。如果不需要等比缩放则将下面的if else语句注释即可
        if (sx > sy) {
            sx = sy;
            width = (int)(sx * source.getWidth());
        } else {
            sy = sx;
            height = (int)(sy * source.getHeight());
        }
        if (type != BufferedImage.TYPE_CUSTOM) {
            target = new BufferedImage(width, height, type);
        } else {
            ColorModel cm = source.getColorModel();
            WritableRaster raster = cm.createCompatibleWritableRaster(width,height);
            target = new BufferedImage(cm, raster, cm.isAlphaPremultiplied(), null);
        }
        Graphics2D g = target.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
        g.drawRenderedImage(source, AffineTransform.getScaleInstance(sx, sy));
        g.dispose();
        return target;
    }

    /**
     * 将图片生成缩略图
     * @param fromFileStr 源文件路径
     * @param saveToFileStr 缩略图文件路径
     */
    public static void saveAsThumbs(String fromFileStr, String saveToFileStr) {
        saveAsThumbs(fromFileStr,saveToFileStr,GlobalConstant.getThumbsWidth(),GlobalConstant.getThumbsHeight());
    }
    
    /**
     * 将图片生成缩略图
     * @param fromFileStr 源文件路径
     * @param saveToFileStr 缩略图文件路径
     * @param width 缩略图宽度
     * @param hight 缩略图高度
     */
    public static void saveAsThumbs(String fromFileStr, String saveToFileStr, int width, int hight) {
        BufferedImage srcImage;
        String imgType = "JPEG";
        //if (fromFileStr.toLowerCase().endsWith(".png")) {
           // imgType = "PNG";
        //}
        File fromFile = new File(fromFileStr);
        File saveFile = new File(saveToFileStr);
        try {
            srcImage = ImageIO.read(fromFile);
            if (width > 0 && hight > 0) {
                srcImage = resize(srcImage, width, hight);
            }
            ImageIO.write(srcImage, imgType, saveFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断图片文件是否为缩略图尺寸
     * @param filePath 图片路径
     * @return boolean 是否为缩略图
     */
    public static boolean isThumbs(String filePath) {
        if (filePath == null || "".equals(filePath))
            return false;
        BufferedImage srcImage;
        File file = new File(filePath);
        try {
            srcImage = ImageIO.read(file);
            if (srcImage.getWidth() <= GlobalConstant.getThumbsWidth() && srcImage.getHeight() <= GlobalConstant.getThumbsHeight())
                return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
    
    public static void main(String argv[]) {
//        try {
//            // 参数1(from),参数2(to),参数3(宽),参数4(高)
//            ImageTools.saveAsThumbs("d:/003.jpg", "d:/thumbs_003.jpg",50, 50);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}