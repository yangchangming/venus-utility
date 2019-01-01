package venus.portal.gbox.resource.imagelib.util;

import venus.portal.gbox.util.ImageTools;

public class ImageThead implements Runnable {
    
    private String from;
    private String to;
    
    public ImageThead(String fromFileStr, String saveToFileStr) {
        from = fromFileStr;
        to = saveToFileStr;
    }
    
    /**
     * 批量生成缩略图的线程实现方法
     */
    public void run() {
        ImageTools.saveAsThumbs(from, to);
    }

}
