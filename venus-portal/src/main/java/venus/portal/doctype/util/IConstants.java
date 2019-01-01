/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.portal.doctype.util;

import venus.portal.doctype.model.DocType;

/**
 * @author yangchangming
 */
public interface IConstants {

    //逻辑 -- 是
    public final static String CONSTANTS_TRUE = "1";
    //逻辑 -- 否
    public final static String CONSTANTS_FLASE = "0";


    //查询树单个节点对象
    public final static String QUERY_TREE_NODE = "select new udp.ewp.doctype.vo.DocTypeTreeVo(dt.id,dt.id,dt.name,dt.parentID) " +
            "from dt in " + DocType.class + " where dt.docTypeCode = 'root' and dt.site.id = :site_id ";

    //查询所有子节点对象
    public final static String QUERY_TREE_RETRIEVE = "select new udp.ewp.doctype.vo.DocTypeTreeVo(sub.id,sub.id,sub.name,sub.parentID) " +
            " from dt in " + DocType.class + " left join dt.children sub" +
            " where dt.id = :parentID  ";

    /**
     * 根据条件查询对象
     */
    public final static String QUERY_TREE_QUERY = " from DocType  tempdocType  where  ";


    public final static String DOCTYPE_BS = "docTypeBS";

    public final static String DOCUMENT_TYPE_OID = "ewp_doctype";

    //注入错误提示消息
    public final static String POPULATE_ERROR_MSG_STR = "VO POPULATE ERROR!";
    //回写表单KEY
    public final static String WRITE_BACK_FORM_VALUES_KEY = "writeBackFormValues";

    public final static String SUCCESS = "success";
    /**
     * 图片格式gif
     */
    public final static String IMAGETYPE_IMAGE_GIF = "image/gif";
    /**
     * 图片格式bmp
     */
    public final static String IMAGETYPE_IMAGE_BMP = "image/bmp";
    /**
     * 图片格式jpg
     */
    public final static String IMAGETYPE_IMAGE_PJPEG = "image/pjpeg";
    /**
     * 图片格式jpg
     */
    public final static String IMAGETYPE_IMAGE_JPG = "image/pjpeg";
    /**
     * 图片格式jpeg
     */
    public final static String IMAGETYPE_IMAGE_JPEG = "image/jpeg";
    /**
     * 图片格式png
     */
    public final static String IMAGETYPE_IMAGE_PNG = "image/png";

    /**
     * 图片格式gif后缀
     */
    public final static String IMAGETYPE_GIF = "gif";
    /**
     * 图片格式bmp后缀
     */
    public final static String IMAGETYPE_BMP = "bmp";
    /**
     * 图片格式jpg后缀
     */
    public final static String IMAGETYPE_JPEG = "jpeg";
    /**
     * 图片格式jpeg后缀
     */
    public final static String IMAGETYPE_JPG = "jpg";
    /**
     * 图片格式png后缀
     */
    public final static String IMAGETYPE_PNG = "png";


    /**
     * 上传的文件域名称
     */
    public final static String UPLOADFILENAME = "uploadFileName";


    /**
     * 成功跳转
     */
    public final static String FORWARD_SUCCESS = "success";
    /**
     * 失败跳转
     */
    public final static String FORWARD_FAIL = "fail";

    public final static String DOC_TYPE_CACHE = "docTypeCache";

    public final static String FORWARD_LIST_PAGE = "toQueryAll";
    public final static String FORWARD_NOPOWER_PAGE = "toNoPowerPage";

    public final static String SHOW_ONLY = "1";

    public final static String HAS_OPERATING = "2";
}
