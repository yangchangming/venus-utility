package venus.portal.gbox.util;

public class GlobalConstant {

    private final static String ROOTNODE_CODE = "101"; //根节点代码
    private final static String INCREASENODE_CODE = "001"; //下级节点增长代码

    private static int THUMBS_WIDTH = 200; //定义缩略图宽度
    private static int THUMBS_HEIGHT = 150; //定义缩略图高度
    
    public final static String RETURNSTATUS_SUCCESS = "success"; //成功状态
    public final static String RETURNSTATUS_FAILURE = "failure "; //失败状态
    
    /**
     * 获得缩略图宽度
     * @return int 缩略图宽度
     */
    public static int getThumbsWidth() {
        return THUMBS_WIDTH;
    }
    
    /**
     * 获得缩略图高度
     * @return int 缩略图高度
     */
    public static int getThumbsHeight() {
        return THUMBS_HEIGHT;
    }
    
    /**
     * 获得分类树根节点code
     * @return String 获得分类树根节点code
     */
    public static String getRootNodeCode() {
        return ROOTNODE_CODE;
    }
    
    /**
     * 获得下级节点增长代码
     * @return String 下级节点增长code
     */
    public static String getIncreaseNodeCode() {
        return INCREASENODE_CODE;
    }

}
