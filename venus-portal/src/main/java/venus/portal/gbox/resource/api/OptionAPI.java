package venus.portal.gbox.resource.api;

import venus.frames.mainframe.util.Helper;
import venus.portal.gbox.resource.option.bs.IOptionBs;
import venus.portal.gbox.resource.option.util.IOptionConstants;
import venus.portal.gbox.resource.option.vo.OptionVo;

import java.util.ArrayList;
import java.util.List;

public class OptionAPI {

    private final static String INVALID_FILETYPE_SEPARATOR = ";";

    private static List optionList = null; //系统配置缓存变量
    private static String ACCESSIBLE_UPLOADPATH_ID = "1099200600000000001";
    private static String INVALID_FILETYPE_ID = "1099200600000000002";
    private static String RESOURCE_SERVERSPATH_ID = "1099200600000000003";

    /**
     * 获得可访问的上传文件路径
     *
     * @return 可访问的上传文件路径
     */
    public static String getAccessibleUploadPathId() {
        return ACCESSIBLE_UPLOADPATH_ID;
    }

    /**
     * 获得禁止上传的文件类型
     *
     * @return 禁止上传的文件类型
     */
    public static String getInvalidFileTypeId() {
        return INVALID_FILETYPE_ID;
    }

    /**
     * 获得静态资源服务器地址
     *
     * @return 静态资源服务器地址
     */
    public static String getResourceServersPathId() {
        return RESOURCE_SERVERSPATH_ID;
    }

    /**
     * 重新加载系统配置列表
     *
     * @return 重新加载是否成功
     */
    public static boolean refresh() {
        optionList = null;
        return (getOptionList() != null) ? true : false;
    }

    /**
     * 获得系统配置列表
     *
     * @return List 系统配置列表
     */
    public static List<OptionVo> getOptionList() {
        if (optionList == null || optionList.isEmpty()) {
            optionList = new ArrayList();
            IOptionBs bs = (IOptionBs) Helper.getBean(IOptionConstants.BS_KEY);
            optionList = bs.queryAll();
        }
        return optionList;
    }

    /**
     * 获得系统配置名称
     *
     * @param id 系统配置ID
     * @return String 系统配置名称
     */
    public static String getOptionName(String id) {
        if (id == null || "".equals(id))
            return null;
        List<OptionVo> list = getOptionList();
        for (int i = 0; i < list.size(); i++) {
            OptionVo vo = list.get(i);
            if (vo.getId().equals(id))
                return vo.getName();
        }
        return null;
    }

    /**
     * 获得系统配置数值
     *
     * @param id 系统配置ID
     * @return String 系统配置数值
     */
    public static String getOptionValue(String id) {
        if (id == null || "".equals(id))
            return null;
        List<OptionVo> list = getOptionList();
        for (int i = 0; i < list.size(); i++) {
            OptionVo vo = list.get(i);
            if (vo.getId().equals(id))
                return vo.getValue();
        }
        return null;
    }

    /**
     * 判断指定文件是否为非法上传文件类型(系统配置)
     *
     * @param fileName 文件名
     * @return boolean 是否为非法类型
     */
    public static boolean isValidResourceType(String fileName) {
        if (fileName == null || "".equals(fileName))
            return false;
        String invalidFileStr = OptionAPI.getOptionValue(OptionAPI.getInvalidFileTypeId());
        if (invalidFileStr == null && "".equals(invalidFileStr))
            return true;
        String[] invalidFileArr = invalidFileStr.split(INVALID_FILETYPE_SEPARATOR);
        for (int i = 0; i < invalidFileArr.length; i++) {
            if (fileName.toLowerCase().lastIndexOf(invalidFileArr[i]) != -1)
                return false;
        }
        return true;
    }
}
