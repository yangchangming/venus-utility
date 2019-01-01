/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.portal.tree.util;


public interface ITreeConstants {

    /**
     * 树的递进空格
     */
    public final static String TREE_SPACE = "&nbsp;&nbsp;&nbsp;";

    public final static String TREE_IMAGEFOLD = "/images/tree/parent.gif";
    public final static String TREE_IMAGEROOT = "/images/tree/root.png";
    public final static String TREE_IMAGEPARENTOPEN = "/images/tree/parentopen.gif";
    public final static String TREE_IMAGLINE = "/images/tree/leaf.gif";
    public final static String TREE_LINK_HREF = "href";
    public final static String TREE_LINK_TARGET = "target";

    /**
     * 逻辑 -- 是
     */
    public final static String CONSTANTS_TRUE = "1";
    /**
     * 逻辑 -- 否
     */
    public final static String CONSTANTS_FLASE = "0";

    /**
     * 栏目树的选择类型为单选框
     */
    public final static String CHECKTYPE_RADIOBOX = "RADIO";
    /**
     * 栏目树的选择类型为复选框
     */
    public final static String CHECKTYPE_CHECKBOX = "CHECK";
    /**
     * 栏目树的选择类型为无
     */
    public final static String CHECKTYPE_NONE = "NONE";
    /**
     * 栏目树节点图片的css样式
     */
    public final static String TREE_ICONSKIN = "node";
}
