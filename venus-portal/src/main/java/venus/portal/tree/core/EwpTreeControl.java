package venus.portal.tree.core;

import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.collection.PersistentSortedSet;
import uk.ltd.getahead.dwr.ExecutionContext;
import uk.ltd.getahead.dwr.WebContext;
import uk.ltd.getahead.dwr.WebContextFactory;
import venus.frames.base.exception.BaseApplicationException;
import venus.frames.mainframe.util.Helper;
import venus.oa.helper.LoginHelper;
import venus.portal.cache.data.DataCache;
import venus.portal.doctype.bs.IDocTypeBS;
import venus.portal.doctype.model.DocType;
import venus.portal.doctype.vo.DocTypeTreeVo;
import venus.portal.document.bs.IDocumentBS;
import venus.portal.document.util.IConstants;
import venus.portal.tree.util.ITreeConstants;
import venus.portal.tree.vo.TreeParamVO;
import venus.portal.tree.vo.TreeViewObjectVo;
import venus.portal.util.BooleanConstants;
import venus.portal.util.CommonFieldConstants;
import venus.portal.website.bs.IWebsiteBs;
import venus.portal.website.model.Website;
import venus.portal.website.util.IWebsiteConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

//import venus.authority.helper.LoginHelper;

/**
 * 树形结构ajax操作实现类
 */
public class EwpTreeControl implements ITreeConstants {

    private static Log logger = LogFactory.getLog(EwpTreeControl.class);

    protected Long level = null;
    protected int multiply = 1;
    protected String space = null;
    protected String imgfold = null;
    protected String imgroot = null;
    protected String imgopen = null;
    protected String imgline = null;
    protected String param = null;
    protected String href = null;
    protected String target = null;
    protected String site_id = null;
    protected String treeid = null;
    protected String currentid = null;
    protected String web_context_path = null;
    protected String onclick = null;
    protected String ondblclick = null;
    protected String onmousedown = null;

    // 缓存
    private DataCache dataCache;

    /**
     * @param dataCache the dataCache to set
     */
    public void setDataCache(DataCache dataCache) {
        this.dataCache = dataCache;
    }

    public EwpTreeControl() {
        if (StringUtils.isBlank(this.space)) {
            this.space = TREE_SPACE;
        }

        if (StringUtils.isBlank(imgfold)) {
            this.imgfold = TREE_IMAGEFOLD;
        }
        if (StringUtils.isBlank(imgopen)) {
            this.imgopen = TREE_IMAGEPARENTOPEN;
        }

        if (StringUtils.isBlank(imgline)) {
            this.imgline = TREE_IMAGLINE;
        }
        if (StringUtils.isBlank(imgroot)) {
            this.imgroot = TREE_IMAGEROOT;
        }

    }

    /**
     * 初始化树
     *
     * @param root      树的根节点的ID名
     * @param beanID    获取数据源操作 bean
     * @param paramJson 前台传入的参数字符串
     * @return 树的ID和名称的Map
     */
    @SuppressWarnings("deprecation")
    public Map initTree(String root, String beanID, String paramJson) {
        IDocTypeBS docTypeBs = (IDocTypeBS) Helper.getBean(beanID);
        String notInclude = null;
        String disableItemID = null;
        String checkType = null;
        String selectedValues = null;
        HashSet setSelectedValue = new HashSet();
        Map dataMap = new HashMap();
        HashSet selectedParentSet = new HashSet();
        HashSet notIncludeSet = new HashSet();
        HashSet disableItemIDSet = new HashSet();
        DocType checkedParentDocType = null;
        // 初始化树相关参数
        if (paramJson != null) {
            JSONObject jsonParam = JSONObject.fromObject(paramJson);
            TreeParamVO paraVO = (TreeParamVO) JSONObject.toBean(jsonParam, TreeParamVO.class);
            this.href = paraVO.getTree_href();
            this.onclick = paraVO.getTree_onclick();
            this.param = paraVO.getTree_param();
            this.target = paraVO.getTree_target();
            this.site_id = paraVO.getSite_id();
            this.treeid = paraVO.getTreeid();
            this.currentid = paraVO.getCurrentid();
            if (StringUtils.isBlank(this.site_id) || StringUtils.equals(CommonFieldConstants.UNDEFINED, this.site_id)) {
                fillSiteId();
            }
            this.web_context_path = paraVO.getWebModel();
            notInclude = paraVO.getNotIncludeValues();
            disableItemID = paraVO.getDisableItemID();
            checkType = paraVO.getCheckType();
            if (null != paraVO.getSelectedValues()) {
                selectedValues = paraVO.getSelectedValues();
                String[] selectedValueArray = selectedValues.split(",");
                for (String selectedValue : selectedValueArray) {
                    setSelectedValue.add(selectedValue);
                    checkedParentDocType = docTypeBs
                            .queryDocTypesById(selectedValue);
                    if (null != checkedParentDocType) {
                        do {
                            selectedParentSet.add(checkedParentDocType
                                    .getParentID());
                            checkedParentDocType = docTypeBs
                                    .queryDocTypesById(checkedParentDocType
                                            .getParentID());
                        } while (null != checkedParentDocType
                                && checkedParentDocType.getParentID() != null);
                    }
                }
            }
        }
        try {
            notIncludeSet.add(notInclude);
            notIncludeSet.add(currentid);
            disableItemIDSet.add(disableItemID);

            WebContext ctx = WebContextFactory.get();
            HttpServletRequest request = ctx.getHttpServletRequest();
            boolean isAdmin = LoginHelper.getIsAdmin(request);

            List treeNodeList = docTypeBs.queryAllAuNode(notIncludeSet, site_id, request);
            dataMap.put("divid", treeid);
            dataMap.put("treevalue", loadTreeHTML(treeNodeList, treeid, checkType, selectedParentSet, setSelectedValue, disableItemIDSet, isAdmin));
        } catch (Exception e) {

            logger.error(e.getMessage());
            throw new BaseApplicationException(e.getMessage());
        }
        return dataMap;
    }

    /**
     * 组装树形HTML
     *
     * @param treeNodeList     树节点VO集合
     * @param treeid
     * @param checkType        选择类型 NONE 为无选择框 CHECK 为复选框 RADIO 为单选框
     * @param openedDivs
     * @param setSelectedValue 默认选中的值
     * @param disableItemIDSet
     * @return
     */
    protected String loadTreeHTML(List treeNodeList, String treeid, String checkType, HashSet openedDivs, HashSet setSelectedValue, HashSet disableItemIDSet, boolean isAdmin) {
        StringBuffer treeHTML = new StringBuffer();
        if (treeNodeList == null || treeNodeList.size() == 0) {
            String nodoctype = venus.frames.i18n.util.LocaleHolder.getMessage("udp.ewp.website.nodoctype");
            return "<div style='width: 280px; height: 100%; background-color: #c8cdd3; border: 0px solid rgb(144, 179, 207); overflow: auto;' align='left'><ul class='baseNode'><div>&nbsp;&nbsp;&nbsp;<img onclick='' src='/cms/images/tree/root.png' align='ABSMIDDLE'/><a id='' href='' onclick=''>"
                    + nodoctype + "</a><br/></div></ul></div>";
        }
        Iterator it = treeNodeList.iterator();
        treeHTML.append("<div>\n");
        this.tree(it, treeHTML, Long.valueOf("1"), checkType, openedDivs, setSelectedValue, disableItemIDSet, isAdmin);
        treeHTML.append("</div>\n");
        return treeHTML.toString();
    }

    /**
     * 组装树的HTML
     *
     * @param it               树的迭代器
     * @param treeHTML         生成的树形HTML
     * @param level            级次
     * @param checkType        选择类型
     * @param setSelectedValue 默认选中值
     */
    protected void tree(Iterator it, StringBuffer treeHTML, Long level, String checkType, HashSet openedDivs, HashSet setSelectedValue, HashSet disableItemIDSet, boolean isAdmin) {
        while (it.hasNext()) {
            DocTypeTreeVo obj = (DocTypeTreeVo) it.next();
            if (obj.getLevel() < level) {
                for (int i = 0; i < (level - obj.getLevel()); i++) {
                    treeHTML.append("</div>\n");
                }
            }
            level = obj.getLevel();
            //判断此节点是否可显示
            if (CONSTANTS_TRUE.equals(obj.getHasShow()) || isAdmin == true) {
                // 有子节点
                if (obj.getType() == TreeViewObjectVo.HAS_SUB_NODE) {
                    treeHTML.append("<div style=\"margin-bottom :4.5px;margin-top :4.5px;\">\n");
                    for (int i = 0; i < level; i++) {
                        for (int j = 0; j < multiply; j++) {
                            treeHTML.append(space);
                        }
                    }
                    if (level == 1) {
                        treeHTML.append("<img id=\"foldheader\" onclick=\"expandAndCollapse(event)\" align=\"ABSMIDDLE\" src=\""
                                + (Helper.WEB_CONTEXT_PATH != null ? Helper.WEB_CONTEXT_PATH : web_context_path)
                                + (openedDivs.contains(obj.getId()) ? imgroot : imgroot) + "\"/>\n");
                    } else {
                        treeHTML.append("<img id=\"foldheader\" onclick=\"expandAndCollapse(event)\" align=\"ABSMIDDLE\" src=\""
                                + (Helper.WEB_CONTEXT_PATH != null ? Helper.WEB_CONTEXT_PATH : web_context_path)
                                + (openedDivs.contains(obj.getId()) ? imgopen : imgfold) + "\"/>\n");
                    }
                    nodeOperation(treeHTML, checkType, setSelectedValue, disableItemIDSet, isAdmin, obj);
                    treeHTML.append("</div>\n");
                    treeHTML.append("<div id=\"container_" + obj.getId() + "\" style=\"display:" + ((openedDivs.contains(obj.getId()) || level == 1) ? "block" : "none")
                            + ";margin-bottom :4px;margin-top :4px;\">\n");
                }
                // 没有子节点
                else {
                    for (int i = 0; i < obj.getLevel(); i++) {
                        for (int j = 0; j < multiply; j++) {
                            treeHTML.append(space);
                        }
                    }
                    if (level == 1) {
                        treeHTML.append("<img align=\"ABSMIDDLE\" onclick=\"expandAndCollapse(event)\"  src=\""
                                + (Helper.WEB_CONTEXT_PATH != null ? Helper.WEB_CONTEXT_PATH : web_context_path) + imgroot + "\"/>\n");
                    } else {
                        treeHTML.append("<img align=\"ABSMIDDLE\" onclick=\"expandAndCollapse(event)\"  src=\""
                                + (Helper.WEB_CONTEXT_PATH != null ? Helper.WEB_CONTEXT_PATH : web_context_path) + imgline + "\"/>\n");
                    }
                    nodeOperation(treeHTML, checkType, setSelectedValue, disableItemIDSet, isAdmin, obj);
                    treeHTML.append("<br/>\n");
                }
            }
            this.tree(it, treeHTML, level, checkType, openedDivs, setSelectedValue, disableItemIDSet, isAdmin);
        }
    }

    /**
     * 树结点的操作添加。
     *
     * @param treeHTML
     * @param checkType
     * @param setSelectedValue
     * @param disableItemIDSet
     * @param isAdmin
     * @param obj
     */
    private void nodeOperation(StringBuffer treeHTML, String checkType, HashSet setSelectedValue, HashSet disableItemIDSet, boolean isAdmin, DocTypeTreeVo obj) {

        //radiobox or checkbox add
        String boxItemDisableString = "";
        boxItemDisableString = (disableItemIDSet.contains(obj.getId()) ? "disabled " : "");
        if (!(CONSTANTS_TRUE.equals(obj.getHasOperating()) || isAdmin == true)) {
            boxItemDisableString = "disabled";
        }
        if (StringUtils.equals(checkType, ITreeConstants.CHECKTYPE_CHECKBOX)) {
            treeHTML.append("<input name=\"selectDocType\" chinesename=\"" + obj.getName() + "\" value=\"" + obj.getId() + "\" id=\""
                    + obj.getId() + "\" type=\"checkbox\"  style=\"height:10\" onclick=\"selectAllSimilar(this);\" " + boxItemDisableString + (setSelectedValue.contains(obj.getId()) ? "checked" : "") + " >");
        }
        if (StringUtils.equals(checkType, ITreeConstants.CHECKTYPE_RADIOBOX)) {
            treeHTML.append("<input name=\"selectDocType\"  chinesename=\"" + obj.getName() + "\"  value=\"" + obj.getId() + "\" id=\""
                    + obj.getId() + "\" type=\"radio\"  style=\"height:10\"  " + boxItemDisableString + (setSelectedValue.contains(obj.getId()) ? "checked" : "") + " >");
        }

        //herf add
        if (StringUtils.isNotBlank(this.href) && StringUtils.equals(this.href, "none")) {
            treeHTML.append(obj.getName() + "\n");
        } else {
            this.tagA(treeHTML, obj, isAdmin);
        }
    }

    /**
     * 输出A标记
     *
     * @param sb  树形HTMl
     * @param obj 当前节点
     */
    private void tagA(StringBuffer sb, DocTypeTreeVo obj, boolean isAdmin) {
        sb.append("<a id=\"");
        sb.append(obj.getId());

        if (CONSTANTS_TRUE.equals(obj.getHasOperating()) || isAdmin == true) {
            sb.append("\" href=\"" + this.href + "&docTypeID=" + obj.getId());

            if (StringUtils.isNotBlank(onclick)) {
                sb.append("\" onClick=\"");
                sb.append(onclick);
            }
            if (StringUtils.isNotBlank(ondblclick)) {
                sb.append("\" onDblClick=\"");
                sb.append(ondblclick);
            }
            if (StringUtils.isNotBlank(onmousedown)) {
                sb.append("\" onMouseDown=\"");
                sb.append(onmousedown);
            }
        } else {
            sb.append("\" href=\"javascript:void(0);");
        }

        if (StringUtils.isNotBlank(target)) {
            sb.append("\" target =\"");
            sb.append(target);
        }
        if (StringUtils.isNotBlank(treeid)) {
            sb.append("\" treeId=\"");
            sb.append(treeid);
        }
        sb.append("\">");
        sb.append(obj.getName());
        sb.append("</a>\n");
    }

    /**
     * 加载树节点对象
     *
     * @param docTypeID
     * @param beanID
     * @return 返回map给页面，map中存储对象各个属性
     */
    public String showTreeNodeDetail(String docTypeID, String beanID) {
        IDocTypeBS docTypeBs = (IDocTypeBS) Helper.getBean(beanID);
        JSONObject jsonDocType = new JSONObject();
        StringBuffer sharedIds = new StringBuffer();
        StringBuffer sharedNames = new StringBuffer();
        String template_id = "";
        String template_name = "";
        String document_template_id = "";
        String document_template_name = "";
        String parentName = null;
        try {
            Object dt = docTypeBs.findDocTypeById(docTypeID);
            if (dt != null) {
                DocType docType = (DocType) dt;
                jsonDocType.put("id", docType.getId());
                jsonDocType.put("name", docType.getName());
                jsonDocType.put("keywords", docType.getKeywords());
                jsonDocType.put("description", docType.getDescription());
                jsonDocType.put("parentID", docType.getParentID());
                jsonDocType.put("sortNum", docType.getSortNum());
                jsonDocType.put("isValid", docType.getIsValid());
                jsonDocType.put("isNavigateMenu", docType.getIsNavigateMenu());
                jsonDocType.put("isNavigateMenuName", StringUtils.equals(BooleanConstants.NUM_TRUE,
                        docType.getIsNavigateMenu()) ? "是页头导航栏目" : "不是页头导航栏目");
                jsonDocType.put("isValidName", StringUtils.equals(BooleanConstants.NUM_TRUE,
                        docType.getIsValid()) ? "启用" : "停用");
                jsonDocType.put("docTypeCode", docType.getDocTypeCode());
                jsonDocType.put("imagePath", docType.getImagePath());
                if (docType.getTemplate() != null) {
                    template_id = docType.getTemplate().getId();
                    template_name = docType.getTemplate().getTemplate_name();
                }
                if (docType.getDocTemplate() != null) {
                    document_template_id = docType.getDocTemplate().getId();
                    document_template_name = docType.getDocTemplate().getTemplate_name();
                }
                String parentID = docType.getParentID();
                if (null == parentID || StringUtils.equals("", parentID)) {
                    parentName = "";
                } else {
                    //DocType parentDocType = docTypeBs.findDocTypeById(parentID);
                    //parentName = parentDocType.getName();
                    parentName = docTypeBs.getParentPath(docType);
                }
                // 组装父栏目
                Set<DocType> set = docType.getParent();
                Iterator<DocType> it = set.iterator();
                DocType parentDocType = null;
                while (it.hasNext()) {
                    parentDocType = it.next();
                    if (!StringUtils.equals(parentID, parentDocType.getId())) {
                        sharedIds.append(parentDocType.getId() + ",");
                        sharedNames.append(parentDocType.getName() + ",");
                    }
                }
                if (sharedIds.length() > 0) {
                    sharedNames.deleteCharAt(sharedNames.length() - 1);
                    sharedIds.deleteCharAt(sharedIds.length() - 1);
                }
            }
            jsonDocType.put("template_id", template_id);
            jsonDocType.put("template_name", template_name);
            jsonDocType.put("document_template_id", document_template_id);
            jsonDocType.put("document_template_name", document_template_name);
            jsonDocType.put("parentName", parentName);
            jsonDocType.put("sharedIds", sharedIds.toString());
            jsonDocType.put("sharedNames", sharedNames.toString());
            return jsonDocType.toString();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new BaseApplicationException(e.getMessage());
        }
    }

    /**
     * 更新树节点对象
     *
     * @param newDoctType
     * @param beanID
     * @return 是否成功
     */
    public String updateTreeNode(String newDoctType, String beanID) {
        IDocTypeBS docTypeBs = (IDocTypeBS) Helper.getBean(beanID);
        try {
            JSONObject jsonParam = JSONObject.fromObject(newDoctType);
            DocType newDocType = (DocType) JSONObject.toBean(jsonParam, DocType.class);
            this.site_id = newDocType.getSite_id();
            if (StringUtils.isBlank(this.site_id) || StringUtils.equals(CommonFieldConstants.UNDEFINED, this.site_id)) {
                newDocType.setSite_id(fillSiteId());
            }
            String result = docTypeBs.updateTreeNode(newDocType);
            // 刷新缓存
            dataCache.refreshData();
            return result;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new BaseApplicationException(e.getMessage());
        }
    }

    /**
     * 移动树节点对象
     *
     * @param newDoctType
     * @param beanID
     * @return 是否成功
     */
    public String moveTreeNode(String newDoctType, String beanID) {
        IDocTypeBS docTypeBs = (IDocTypeBS) Helper.getBean(beanID);
        try {
            JSONObject jsonParam = JSONObject.fromObject(newDoctType);
            DocType newDocType = (DocType) JSONObject.toBean(jsonParam, DocType.class);
            this.site_id = newDocType.getSite_id();
            if (StringUtils.isBlank(this.site_id) || StringUtils.equals(CommonFieldConstants.UNDEFINED, this.site_id)) {
                newDocType.setSite_id(fillSiteId());
            }
            String result = docTypeBs.moveTreeNode(jsonParam);
            // 刷新缓存
            dataCache.refreshData();
            return result;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new BaseApplicationException(e.getMessage());
        }
    }

    /**
     * 增加树节点
     *
     * @param newDoctType
     * @param beanID
     */
    public String addTreeNode(String newDoctType, String beanID) {
        IDocTypeBS docTypeBs = (IDocTypeBS) Helper.getBean(beanID);
        try {
            DocType docType = docTypeBs.addTreeNode(newDoctType);
            // 刷新缓存
            dataCache.refreshData();
            JSONObject jsonDocType = new JSONObject();
            jsonDocType.put("id", docType.getId());
            jsonDocType.put("name", docType.getName());
            jsonDocType.put("siteId", docType.getSite_id());
            jsonDocType.put("pId", docType.getParentID());
            jsonDocType.put("iconSkin", ITreeConstants.TREE_ICONSKIN);
            //将要展开的IDs
//            StringBuffer expandIds = new StringBuffer();
//            if (docType != null && docType.getChildren() != null && docType.getChildren().size() > 0) {
//                expandIds.append(docType.getId() + ",");
//            }
//            getParentString(expandIds, docType, docTypeBs);
//            if (expandIds.length() > 0) {
//                expandIds = expandIds.deleteCharAt(expandIds.length() - 1);
//            }
//            jsonDocType.put("parentids", expandIds.toString());
            return jsonDocType.toString();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new BaseApplicationException(e.getMessage());
        }
    }

    /**
     * 取得一直到根的父节点IDs
     *
     * @param expandIds
     * @param doctype
     * @param docTypeBs
     */
    private void getParentString(StringBuffer expandIds, DocType doctype, IDocTypeBS docTypeBs) {
        Set<DocType> parents = doctype.getParent();
        if (parents != null && parents.size() > 0) {
            for (DocType parent : parents) {
                expandIds.append(parent.getId() + ",");
                do {
                    if (parent != null) {
                        getParentString(expandIds, parent, docTypeBs);
                    }
                    expandIds.append(parent.getParentID() + ",");
                    parent = docTypeBs.queryDocTypesById(parent.getParentID());
                } while (parent != null && parent.getParentID() != null);
            }
        }
    }

    /**
     * 删除栏目
     *
     * @param nodeID 节点id
     * @param beanID 后台业务类
     */
    public String deleteTreeNode(String nodeID, String beanID) {
        IDocTypeBS docTypeBs = (IDocTypeBS) Helper.getBean(beanID);
        String returnValue = "";
        try {
            DocType dt = docTypeBs.findDocTypeById(nodeID);
            this.site_id = dt.getSite_id();
            if (StringUtils.isBlank(this.site_id) || StringUtils.equals(CommonFieldConstants.UNDEFINED, this.site_id)) {
                dt.setSite_id(fillSiteId());
            }
            String parentID = dt.getParentID();
            if (dt.getChildren() != null && dt.getChildren().size() > 0) {
                returnValue = "{flag:false,message:\"请先删除子栏目!\"}";
                return returnValue;
            }
            IDocumentBS documentBS = (IDocumentBS) Helper.getBean("documentBS");
            int count = documentBS.getDocCountByDocTypeNotPublished(dt.getId(), IConstants.DOCTYPE_DOC_ACTIVE);
            if (count > 0) {
                returnValue = "{flag:false,message:\"请先移除类型下包含的文档!\"}";
                return returnValue;
            }
            // 更新与父栏目的联系
            DocType parent = docTypeBs.findDocTypeById(dt.getParentID());
            PersistentSortedSet childSet = parent.getChildren();
            childSet.remove(dt); // 父栏目断开与当前栏目关联
            dt.setParent(new HashSet<DocType>()); // 当前栏目断开与父栏目关联
            docTypeBs.update(dt);
            // 删除栏目
            docTypeBs.delete(dt);
            StringBuffer expandIds = new StringBuffer();
            dt = docTypeBs.queryDocTypesById(parentID);
            if (null != dt) {
                if (dt != null && dt.getChildren().size() > 0) {
                    expandIds.append(dt.getId() + ",");
                }
                getParentString(expandIds, dt, docTypeBs);
                if (expandIds.length() > 0) {
                    expandIds = expandIds.deleteCharAt(expandIds.length() - 1);
                }
            }
            // 刷新缓存
            dataCache.refreshData();
            return "{flag:true,id:\"" + parentID + "\"}";

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new BaseApplicationException(e.getMessage());
        }
    }

    /**
     * 获得树节点的级别
     *
     * @param nodeID
     * @param beanID
     * @return
     */
    public int getTreeNodeLevle(String nodeID, String beanID) {
        IDocTypeBS docTypeBs = (IDocTypeBS) Helper.getBean(beanID);
        try {
            DocType dt = docTypeBs.findDocTypeById(nodeID);
            return dt.getLevel().intValue();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new BaseApplicationException(e.getMessage());
        }
    }

    private String fillSiteId() {
        if (StringUtils.isBlank(this.site_id) || StringUtils.equals(CommonFieldConstants.UNDEFINED, this.site_id)) {
            HttpSession session = ExecutionContext.get().getSession();
            this.site_id = (String) session.getAttribute("site_id");
            if (StringUtils.isBlank(this.site_id) || StringUtils.equals(CommonFieldConstants.UNDEFINED, this.site_id)) {
                IWebsiteBs webSiteBs = (IWebsiteBs) Helper.getBean(IWebsiteConstants.BS_KEY);
                List<Website> websites = webSiteBs.queryAll();
                Website website = null;
                if (websites != null && websites.size() > 0) {
                    for (Website site : websites) {
                        if (StringUtils.equals(BooleanConstants.YES, site.getIsDefault())) {
                            website = site;
                            break;
                        }
                    }
                    if (website == null) {
                        website = websites.get(0);
                    }
                    this.site_id = website.getId();
                }
            }
        }
        return this.site_id;
    }
}
