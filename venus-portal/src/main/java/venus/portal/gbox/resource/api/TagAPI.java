package venus.portal.gbox.resource.api;

import venus.frames.mainframe.util.Helper;
import venus.portal.gbox.resource.tag.bs.ITagBs;
import venus.portal.gbox.resource.tag.util.ITagConstants;
import venus.portal.gbox.resource.tag.vo.TagVo;
import venus.portal.gbox.util.DateTools;
import venus.pub.lang.OID;

public class TagAPI {
    
    private static ITagBs getBs() {
        return (ITagBs) Helper.getBean(ITagConstants.BS_KEY);
    }

    /**
     * 添加tag
     * @param tag tag名称
     * @return 添加tag是否成功
     */
    public static boolean addTag(String tag) {
        if (tag == null || "".equals(tag))
            return false;
        String queryCondition = " AND NAME = '" + tag + "'";
        OID oid = null;
        if (getBs().getRecordCount(queryCondition) == 0) { //数据库中不存在该标签则创建
            TagVo tagVo = new TagVo();
            tagVo.setName(tag);
            tagVo.setCreateDate(DateTools.getSysTimestamp());
            oid = getBs().insert(tagVo);
        }        
        return (oid != null) ? true : false;
    }
    
    /**
     * 删除tag
     * @param tag tag名称
     * @return 删除tag是否成功
     */
    public static boolean deleteTag(String tag) {
        if (tag == null || "".equals(tag))
            return false;
        TagVo vo = getBs().find(tag);
        if (vo == null || "".equals(vo))
            return false;
        return (getBs().delete(vo.getId()) > 0) ? true : false;
    }
    
    /**
     * 获得链接查询功能的tag字符串
     * @param contextPath
     * @param tags
     * @return 获得链接查询功能的tag字符串
     */
    public static String getTagLinks(String contextPath,String tags) {
        if (tags == null)
            return "";
        String[] tag = tags.split(" ");
        String links = "";
        for (int i = 0; i < tag.length; i++) {
            links +=  "&nbsp;<a href=" + contextPath + "/ResourceAction.do?cmd=simpleQuery&tag=" + tag[i] + ">" + tag[i] + "</a>&nbsp;";
        }
        return links;
    }
    
}
