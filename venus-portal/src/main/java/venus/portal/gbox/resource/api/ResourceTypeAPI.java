package venus.portal.gbox.resource.api;

import venus.frames.mainframe.util.Helper;
import venus.portal.gbox.resource.resourcetype.bs.IResourceTypeBs;
import venus.portal.gbox.resource.resourcetype.util.IResourceTypeConstants;
import venus.portal.gbox.resource.resourcetype.vo.ResourceTypeVo;
import venus.pub.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class ResourceTypeAPI {
    
    private final static String INVALID_FILETYPE_SEPARATOR = ";";
    
    private static List resourceTypeList = null; //资源类型缓存变量
    private final static String RESOURCETYPE_IMAGE = "1099200400000000001"; //图片类型
    private final static String RESOURCETYPE_VIDEO = "1099200400000000002"; //视频类型
    private final static String RESOURCETYPE_DOC = "1099200400000000003"; //文档类型
    private final static String RESOURCETYPE_AUDIO = "1099200400000000004";  //音频类型    
    
    /**
     * 获得图片类型ID值
     * @return 图片类型ID值
     */
    public static String getImageType() {
        return RESOURCETYPE_IMAGE;
    }
    
    /**
     * 获得视频类型ID值
     * @return 视频类型ID值
     */    
    public static String getVideoType() {
        return RESOURCETYPE_VIDEO;
    }
    
    /**
     * 获得文档类型ID值
     * @return 文档类型ID值
     */      
    public static String getDocType() {
        return RESOURCETYPE_DOC;
    }
    
    /**
     * 获得音频类型ID值
     * @return 音频类型ID值
     */      
    public static String getAudioType() {
        return RESOURCETYPE_AUDIO;
    }    
    
    /**
     * 重新加载资源类型列表
     * @return 重新加载是否成功
     */
    public static boolean refresh() {
        resourceTypeList = null;
        return (getResourceTypeList() != null) ? true : false;
    }
    
    /**
     * 获得资源类型列表
     * @return List 资源类型列表
     */
    public static List<ResourceTypeVo> getResourceTypeList() {
        if (resourceTypeList == null || resourceTypeList.isEmpty()) {
            resourceTypeList = new ArrayList();
            IResourceTypeBs bs = (IResourceTypeBs) Helper.getBean(IResourceTypeConstants.BS_KEY);
            resourceTypeList = bs.queryAll();
        }
        return resourceTypeList;
    }
    
    /**
     * 根据类型ID获得资源类型名称
     * @param id 资源类型ID
     * @return String 资源类型名称
     */
    public static String getResourceTypeName(String id) {
        if (id == null || "".equals(id))
            return null;
        List<ResourceTypeVo> list = getResourceTypeList();
        for (int i = 0; i < list.size(); i++) {
            ResourceTypeVo vo = list.get(i);
            if (vo.getId().equals(id))
                return vo.getName();
        }
        return null;
    }
    
    /**
     * 根据资源类型ID获得资源类型关联格式
     * @param id 资源类型ID
     * @return String 资源类型格式
     */    
    public static String getResourceTypeFormat(String id) {
        if (id == null || "".equals(id))
            return null;
        List<ResourceTypeVo> list = getResourceTypeList();
        for (int i = 0; i < list.size(); i++) {
            ResourceTypeVo vo = list.get(i);
            if (vo.getId().equals(id))
                return vo.getRelevanceFormat();
        }
        return null;
    }    
    
    /**
     * 根据资源类型ID获得资源类型关联格式的字符串数组
     * @param id 资源类型ID
     * @return String 资源类型格式字符串数组
     */    
    public static String[] getResourceTypeFormatArr(String id) {
        if (id == null || "".equals(id))
            return null;
        List<ResourceTypeVo> list = getResourceTypeList();
        String arr = null;
        for (int i = 0; i < list.size(); i++) {
            ResourceTypeVo vo = list.get(i);
            if (vo.getId().equals(id))
                arr =  vo.getRelevanceFormat();
        }
        return (arr == null) ? StringUtil.split(arr, INVALID_FILETYPE_SEPARATOR) : new String[0];
    }    
    
    /**
     * 获得所有资源类型关联格式
     * @param 
     * @return String 资源类型格式
     */    
    public static String getAllResourceTypeFormat() {
        String rs="";
        List<ResourceTypeVo> list = getResourceTypeList();
        for (int i = 0; i < list.size(); i++) {
            ResourceTypeVo vo = list.get(i);
            if(vo.getRelevanceFormat()!=null&&(!"".equals(vo.getRelevanceFormat())))
            rs=rs+vo.getRelevanceFormat()+INVALID_FILETYPE_SEPARATOR;
        }
        if(!"".endsWith(rs)){
            rs=rs.substring(0,rs.length()-1);
        }
        return rs;
    }    
 
    /**
     * 根据类型ID获得资源类型上传路径
     * @param id 资源类型ID
     * @return String 资源类型上传路径
     */        
    public static String getResourceTypeUploadPath(String id) {
        if (id == null || "".equals(id))
            return null;
        List<ResourceTypeVo> list = getResourceTypeList();
        for (int i = 0; i < list.size(); i++) {
            ResourceTypeVo vo = list.get(i);
            if (vo.getId().equals(id))
                return vo.getUploadPath();
        }
        return null;
    }     
}
