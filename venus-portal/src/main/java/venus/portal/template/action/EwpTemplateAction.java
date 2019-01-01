package venus.portal.template.action;

import org.apache.commons.lang.StringUtils;
import venus.frames.base.action.DefaultDispatchAction;
import venus.frames.base.action.IForward;
import venus.frames.base.action.IRequest;
import venus.frames.base.action.IResponse;
import venus.frames.base.bean.DefaultForm;
import venus.frames.mainframe.util.Helper;
import venus.frames.web.page.PageVo;
import venus.portal.doctype.bs.IDocTypeBS;
import venus.portal.document.bs.IDocumentBS;
import venus.portal.document.util.IConstants;
import venus.portal.helper.EwpJspHelper;
import venus.portal.helper.EwpVoHelper;
import venus.portal.template.bs.ITemplateBs;
import venus.portal.template.model.EwpTemplate;
import venus.portal.template.util.ITemplateConstants;
import venus.portal.templatetype.bs.ITemplateTypeBs;
import venus.portal.templatetype.util.ITemplateTypeConstants;
import venus.portal.util.BooleanConstants;
import venus.portal.util.EnumTools;
import venus.portal.util.FileSystemConstants;
import venus.portal.util.SqlHelper;
import venus.portal.website.bs.IWebsiteBs;
import venus.portal.website.model.Website;
import venus.portal.website.util.IWebsiteConstants;

import java.io.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import static org.apache.commons.lang.ArrayUtils.isEmpty;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static venus.frames.i18n.util.LocaleHolder.getMessage;
import static venus.portal.util.CommonFieldConstants.BACKFLAG;
import static venus.portal.util.CommonFieldConstants.ID;
import static venus.portal.util.EnumTools.LOGICBOOLEAN;
import static venus.portal.util.EnumTools.TEMPLATETYPE;
import static venus.portal.util.IEwpToolsConstants.DEFAULT_FORWARD;

/**
 * 企业建站平台模板管理Action
 *
 * @author zhangrenyang
 * @date 2011-9-28
 */
public class EwpTemplateAction extends DefaultDispatchAction implements ITemplateConstants {

    /**
     * 网站根目录
     */
    private String realPath = "";

    /**
     * 取得模板业务处理类
     *
     * @return ITemplateBs 模板业务处理类
     */
    public ITemplateBs getBs() {
        return (ITemplateBs) Helper.getBean(BS_KEY);
    }

    /**
     * 取得模板类型业务处理类
     *
     * @return ITemplateTypeBs 模板类型业务处理类
     */
    private ITemplateTypeBs getTemplateTypeBs() {
        return (ITemplateTypeBs) Helper.getBean(ITemplateTypeConstants.BS_KEY);
    }

    /**
     * 取得网站业务处理类
     *
     * @return IWebsiteBs 网站业务处理类
     */
    private IWebsiteBs getWebSiteBs() {
        return (IWebsiteBs) Helper.getBean(IWebsiteConstants.BS_KEY);
    }

    private IDocTypeBS getDocTypeBs() {
        IDocTypeBS docTypeBs = (IDocTypeBS) Helper.getBean(venus.portal.doctype.util.IConstants.DOCTYPE_BS);
        return docTypeBs;
    }

    /**
     * 返回此服务器的文件根路径
     *
     * @return String 文件根路径
     */
    private String getRealPath() {
        if (isNotBlank(realPath)) {
            return realPath;
        } else {
            String path = getServlet().getServletContext().getRealPath(FileSystemConstants.FILE_SYSTEM_SEPARATOR);
            if (!path.endsWith(FileSystemConstants.FILE_SYSTEM_SEPARATOR)) {
                return path + FileSystemConstants.FILE_SYSTEM_SEPARATOR;
            }
            return path;

        }
    }

    /**
     * 从页面表单获取信息注入模板vo，并插入单条记录，同时保存模板内容到文件系统.
     *
     * @param formBean 前台封闭的数据对象
     * @param request  请求
     * @param response 响应
     * @return IForward　业务完成后的跳转对象
     * @throws Exception 抛出保存模板中出现的异常
     */
    public IForward insert(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        String viewname = request.getParameter("template_viewname");
        EwpTemplate vo = new EwpTemplate();
        Helper.populate(vo, request);
        EwpVoHelper.markCreateStamp(request, vo);

        Website webSite = null;
        if (isNotBlank(vo.getWebSiteId())) {
            webSite = getWebSiteBs().find(vo.getWebSiteId());
            if (webSite != null) {
                vo.setWebSite(webSite);
            }
        }
        //获取根路径
        //设置站点的模板保存路径
        if (webSite != null) {
            vo.setDirectory(getRealPath() + FileSystemConstants.VIEW_PATH + webSite.getWebsiteCode());
            vo.setFilepath(getRealPath() + FileSystemConstants.VIEW_PATH + webSite.getWebsiteCode() + FileSystemConstants.FILE_SYSTEM_SEPARATOR + viewname + FileSystemConstants.FREEMARKER_SUFFIX);
        } else {
            vo.setDirectory(getRealPath() + FileSystemConstants.VIEW_PATH);
            vo.setFilepath(getRealPath() + FileSystemConstants.VIEW_PATH + viewname + FileSystemConstants.FREEMARKER_SUFFIX);
        }
        vo.setIsDefault("N");
        //保存模板
        getBs().insert(vo);

        EwpJspHelper.transctPageVo(request, 1);
        return request.findForward(FORWARD_TO_QUERY_ALL);
    }

    /**
     * 从页面的表单获取多条记录id，并删除多条记录
     *
     * @param formBean 前台封闭的数据对象
     * @param request  请求
     * @param response 响应
     * @return IForward　业务完成后的跳转对象
     * @throws Exception 抛出保存模板中出现的异常
     */
    public IForward deleteMulti(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        String[] ids = EwpJspHelper.getArrayFromRequest(request, REQUEST_IDS); // 从request获取多条记录id
        if (!isEmpty(ids)) {
            for (String id : ids) {
                EwpTemplate ewpTemplate = getBs().find(id);
                if (StringUtils.equals(ewpTemplate.getIsSystem(), BooleanConstants.NUM_TRUE)) {
                    String message = getMessage("udp.ewp.template.template_issystem");
                    throw new Exception(message);
                } else {
                    if (ewpTemplate.getWebSite() != null) {
                        ewpTemplate.setOldfilepath(getRealPath() + FileSystemConstants.VIEW_PATH + ewpTemplate.getWebSite().getWebsiteCode() + FileSystemConstants.FILE_SYSTEM_SEPARATOR + ewpTemplate.getTemplate_viewname() + FileSystemConstants.FREEMARKER_SUFFIX);
                    } else {
                        ewpTemplate.setOldfilepath(getRealPath() + FileSystemConstants.VIEW_PATH + ewpTemplate.getTemplate_viewname() + FileSystemConstants.FREEMARKER_SUFFIX);
                    }
                    IDocTypeBS docTypeBs = getDocTypeBs();
                    String condition = " 1=1 and tempdocType.template.id='" + id + "'";
                    List docTypes = docTypeBs.queryDocTypesByCondition(condition);
                    if (docTypes != null && docTypes.size() > 0) {//如果模板已经跟文档类型进行了挂接，则不进行删除。
                        String message = getMessage("udp.ewp.template.template_has_bean_mount_doctype");
                        throw new Exception(message);
                    } else {
                        getBs().delete(ewpTemplate);
                    }
                }
            }
        }
        return request.findForward(FORWARD_TO_QUERY_ALL);
    }

    /**
     * 跳转到新增页面
     *
     * @param formBean 前台封闭的数据对象
     * @param request  请求
     * @param response 响应
     * @return IForward　业务完成后的跳转对象
     * @throws Exception 抛出保存模板中出现的异常
     */
    public IForward findForInsert(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        request.setAttribute(REQUEST_WEBSITES, getBs().getWebsiteListFromCache());
        return request.findForward(FORWARD_INSERT_PAGE);
    }

    /**
     * 从页面的表单获取单条记录id，查出这条记录的值，并跳转到修改页面
     *
     * @param formBean 前台封闭的数据对象
     * @param request  请求
     * @param response 响应
     * @return IForward　业务完成后的跳转对象
     * @throws Exception 抛出保存模板中出现的异常
     */
    public IForward findForUpdateByID(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        EwpTemplate bean = getBs().find(request.getParameter(REQUEST_ID)); // 通过id获取vo
        //取得站点
        Website webSite = bean.getWebSite();
        String filepath = getRealPath() + FileSystemConstants.VIEW_PATH + bean.getTemplate_viewname() + FileSystemConstants.FREEMARKER_SUFFIX;
        if (webSite != null) {
            filepath = getRealPath() + FileSystemConstants.VIEW_PATH + webSite.getWebsiteCode() + FileSystemConstants.FILE_SYSTEM_SEPARATOR + bean.getTemplate_viewname() + FileSystemConstants.FREEMARKER_SUFFIX;
        }
        File file = new File(filepath);
        if (file != null && file.exists()) {
            InputStreamReader reader = null;
            BufferedReader br = null;
            try {
                reader = new InputStreamReader(new FileInputStream(file), FileSystemConstants.CHARSET);
                br = new BufferedReader(reader);
                StringBuffer sb = new StringBuffer();
                String str = null;
                do {
                    str = br.readLine();
                    if (str != null) {
                        sb.append(str + "\r\n");
                    }
                } while (str != null);
                bean.setTemplate_content(sb.toString());
            } finally {
                br.close();
                reader.close();
            }
        }
        request.setAttribute(REQUEST_BEAN, bean); // 把vo放入request
        request.setAttribute(REQUEST_WEBSITES, getBs().getWebsiteListFromCache());
        return request.findForward(FORWARD_UPDATE_PAGE);
    }

    /**
     * 从页面表单获取信息注入VO，并修改单条记录
     *
     * @param formBean 前台封闭的数据对象
     * @param request  请求
     * @param response 响应
     * @return IForward　业务完成后的跳转对象
     * @throws Exception 抛出保存模板中出现的异常
     */
    public IForward update(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        EwpTemplate bean = getBs().find(request.getParameter(REQUEST_ID)); // 通过id获取vo
        //取得站点ID
        Website webSite = bean.getWebSite();
        if (webSite != null) {
            bean.setOldfilepath(getRealPath() + FileSystemConstants.VIEW_PATH + webSite.getWebsiteCode() + FileSystemConstants.FILE_SYSTEM_SEPARATOR + bean.getTemplate_viewname() + FileSystemConstants.FREEMARKER_SUFFIX);
        } else {
            bean.setOldfilepath(getRealPath() + FileSystemConstants.VIEW_PATH + bean.getTemplate_viewname() + FileSystemConstants.FREEMARKER_SUFFIX);
        }

        Helper.populate(bean, request); // 从request中注值进去vo
        EwpVoHelper.markModifyStamp(request, bean); // 打修改时间,IP戳

        if (webSite != null) {
            bean.setFilepath(getRealPath() + FileSystemConstants.VIEW_PATH + webSite.getWebsiteCode() + FileSystemConstants.FILE_SYSTEM_SEPARATOR + bean.getTemplate_viewname() + FileSystemConstants.FREEMARKER_SUFFIX);
        } else {
            bean.setFilepath(getRealPath() + FileSystemConstants.VIEW_PATH + bean.getTemplate_viewname() + FileSystemConstants.FREEMARKER_SUFFIX);
        }

        getBs().update(bean); // 更新单条记录
        EwpJspHelper.transctPageVo(request); // 翻页重载
        return request.findForward(FORWARD_TO_QUERY_ALL);
    }

    /**
     * 查询全部记录，分页显示，支持页面上触发的后台排序
     *
     * @param formBean 前台封闭的数据对象
     * @param request  请求
     * @param response 响应
     * @return IForward　业务完成后的跳转对象
     * @throws Exception 抛出保存模板中出现的异常
     */
    public IForward queryAll(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        request.setAttribute(REQUEST_QUERY_CONDITION, "");
        simpleQuery(formBean, request, response);
        return request.findForward(FORWARD_LIST_PAGE);
    }

    /**
     * 导入模板
     *
     * @param formBean 前台封闭的数据对象
     * @param request  请求
     * @param response 响应
     * @return IForward　业务完成后的跳转对象
     * @throws Exception 抛出保存模板中出现的异常
     */
    public IForward importTemplates(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        //读取文件并导入到模板表中
        Integer addCount = 0;//新加模板数量
        List<Website> webSiteLsit = getBs().getWebsiteListFromCache();
        for (Website site : webSiteLsit) {
            addCount += doImportTemplate(site);
        }

        request.setAttribute("addCount", addCount);
        request.setAttribute(REQUEST_QUERY_CONDITION, "");
        return simpleQuery(formBean, request, response);
    }

    /**
     * 查询全部记录，分页显示，支持页面上触发的后台排序
     *
     * @param formBean 前台封闭的数据对象
     * @param request  请求
     * @param response 响应
     * @return IForward　业务完成后的跳转对象
     * @throws Exception 抛出保存模板中出现的异常
     */
    public IForward queryReference(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        request.setAttribute(EwpTemplate.TEMPLATE_NAME, request.getParameter(EwpTemplate.TEMPLATE_NAME));
        request.setAttribute(EwpTemplate.TEMPLATE_CONTENT, request.getParameter(EwpTemplate.TEMPLATE_CONTENT));
        String orderStr = Helper.findOrderStr(request); // 得到排序信息
        String siteId = request.getParameter("siteId");
        String templateType = request.getParameter("templateType");
        String queryCondition = " template.template_type='" + templateType + "' ";
        if (!isBlank(siteId)) {
            queryCondition += " and template.webSite.id ='" + siteId + "' ";
        }

        List beans = getBs().queryByCondition(1, 10000, queryCondition, orderStr); // 按条件查询全部,带排序

        Helper.saveOrderStr(orderStr, request); // 保存排序信息
        request.setAttribute(REQUEST_BEANS, beans); // 把结果集放入request
        return request.findForward(FORWARD_REFERENCE_PAGE);
    }

    /**
     * 从页面的表单获取单条记录id，并察看这条记录的详细信息
     *
     * @param formBean 前台封闭的数据对象
     * @param request  请求
     * @param response 响应
     * @return IForward　业务完成后的跳转对象
     * @throws Exception 抛出保存模板中出现的异常
     */
    public IForward detail(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        EwpTemplate bean = getBs().find(request.getParameter(REQUEST_ID)); // 通过id获取vo

        IDocumentBS docBs = (IDocumentBS) Helper.getBean(IConstants.DOCUMENT_BS);
        List docList = docBs.queryAllDocByWebSiteId("", bean.getWebSiteId());

        String filepath = getRealPath() + FileSystemConstants.VIEW_PATH + bean.getTemplate_viewname() + FileSystemConstants.FREEMARKER_SUFFIX;
        if (bean.getWebSite() != null) {
            filepath = getRealPath() + FileSystemConstants.VIEW_PATH + bean.getWebSite().getWebsiteCode() + FileSystemConstants.FILE_SYSTEM_SEPARATOR + bean.getTemplate_viewname() + FileSystemConstants.FREEMARKER_SUFFIX;
        }
        File file = new File(filepath);
        if (file != null && file.exists()) {
            InputStreamReader reader = null;
            BufferedReader br = null;
            try {
                reader = new InputStreamReader(new FileInputStream(file), FileSystemConstants.CHARSET);
                br = new BufferedReader(reader);
                StringBuffer sb = new StringBuffer();
                String str = null;
                do {
                    str = br.readLine();
                    if (str != null) {
                        sb.append(str + "\r\n");
                    }
                } while (str != null);
                bean.setTemplate_content(sb.toString());
            } finally {
                if (br != null) {
                    br.close();
                }
                if (reader != null) {
                    reader.close();
                }
            }
        }

        EwpVoHelper.replaceToHtml(bean);

        TreeMap<String, String> templateTypeMap = EnumTools.getSortedEnumMap(TEMPLATETYPE);
        bean.setTemplate_type(templateTypeMap.get(bean.getTemplate_type()));    //设置模板类型多语

        TreeMap<String, String> booleanMap = EnumTools.getSortedEnumMap(LOGICBOOLEAN);
        bean.setIsDefault(booleanMap.get(bean.getIsDefault()));

        request.setAttribute(REQUEST_BEAN, bean); // 把vo放入request
        request.setAttribute(DOCUMENT_BEANS, docList);
        EwpJspHelper.transctPageVo(request); // 翻页重载
        return request.findForward(FORWARD_DETAIL_PAGE);
    }

    /**
     * 简单查询，分页显示，支持表单回写
     *
     * @param formBean 前台封闭的数据对象
     * @param request  请求
     * @param response 响应
     * @return IForward　业务完成后的跳转对象
     * @throws Exception 抛出保存模板中出现的异常
     */
    public IForward simpleQuery(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        String back_flag = request.getParameter(BACKFLAG);
        StringBuffer queryCondition = new StringBuffer();
        if (isNotBlank(back_flag) && StringUtils.equals(back_flag, BooleanConstants.TRUE)) {
            request.setAttribute(EwpTemplate.WEBSITEID, request.getParameter(EwpTemplate.WEBSITEID));
            request.setAttribute(EwpTemplate.TEMPLATE_NAME, request.getParameter(EwpTemplate.TEMPLATE_NAME));
            request.setAttribute(EwpTemplate.TEMPLATE_CONTENT, request.getParameter(EwpTemplate.TEMPLATE_CONTENT));
            request.setAttribute(EwpTemplate.TEMPLATE_TYPE, request.getParameter(EwpTemplate.TEMPLATE_TYPE));
            queryCondition.append(getQueryCondition(request));
        }
        String webSiteId = request.getParameter("website_id");
        if (webSiteId != null && !isBlank(webSiteId)) {
            if (isNotBlank(queryCondition.toString())) {
                queryCondition.append(" and  template.webSite.id ='" + webSiteId + "' ");
            } else {
                queryCondition.append("  template.webSite.id ='" + webSiteId + "' ");
            }
        }

        if (!EwpJspHelper.transctPageVo(request)) {
            EwpJspHelper.transctPageVo(request, 0, getBs().getRecordCount(queryCondition.toString()));
        }
        PageVo pageVo = Helper.findPageVo(request);
        String orderStr = Helper.findOrderStr(request);

        List beans = getBs().queryByCondition(pageVo.getCurrentPage(), pageVo.getPageSize(), queryCondition.toString(), orderStr);
        Helper.saveOrderStr(orderStr, request);
        if (null != beans && beans.size() > 0) {
            TreeMap<String, String> templateTypeMap = EnumTools.getSortedEnumMap(TEMPLATETYPE);
            for (Object bean : beans) {
                EwpTemplate vo = (EwpTemplate) bean;
                vo.setTemplate_type(templateTypeMap.get(vo.getTemplate_type()));    //设置模板类型多语
                String filepath = getRealPath() + FileSystemConstants.VIEW_PATH + vo.getTemplate_viewname() + FileSystemConstants.FREEMARKER_SUFFIX;
                File file = new File(filepath);
                if (null != file && file.exists()) {
                    FileReader fr = null;
                    BufferedReader br = null;
                    try {
                        fr = new FileReader(file);
                        br = new BufferedReader(fr);
                        StringBuffer sb = new StringBuffer();
                        String str = null;
                        do {
                            str = br.readLine();
                            if (str != null) {
                                sb.append(str + "\r\n");
                            }
                        } while (str != null);
                        vo.setTemplate_content(sb.toString());
                    } finally {
                        br.close();
                        fr.close();
                    }
                }
            }
        }
        request.setAttribute(REQUEST_BEANS, beans); // 把结果集放入request
        request.setAttribute(REQUEST_WEBSITES, getBs().getWebsiteListFromCache());
        return request.findForward(FORWARD_LIST_PAGE);
    }

    /**
     * 从request获得查询条件并拼装成查询的SQL语句
     *
     * @param request 查询对象
     * @return 查询的SQL语句
     */
    private String getQueryCondition(final IRequest request) {
        String[] conditions = new String[]{
                SqlHelper.pushCondition(request, EwpTemplate.TEMPLATE_NAME),
                SqlHelper.pushCondition(request, EwpTemplate.TEMPLATE_CONTENT),
                SqlHelper.pushCondition(request, EwpTemplate.TEMPLATE_TYPE),
        };

        return SqlHelper.build(conditions);
    }

    /**
     * 验证前台传过来的模板编码在此站点下是否唯一
     *
     * @param formBean 前台封闭的数据对象
     * @param request  请求
     * @param response 响应
     * @return IForward 跳转对象，此方法为ajax调用，所以返回对象可以不设置.真正有意义的是流输出的是否验证通过消息.
     */
    public IForward checkViewCodeIsUnique(DefaultForm formBean, IRequest request, IResponse response) throws Exception {

        String template_viewname = request.getParameter("template_viewname"); //取得模板视图编码

        String webSiteId = request.getParameter("webSiteId"); //取得站点ID

        EwpTemplate ewpTemplateVo = null;
        String returnMessage = null;
        String isPass = BooleanConstants.YES;

        //取得此站点下的此编码模板VO
        if (isNotBlank(webSiteId)) {
            ewpTemplateVo = getBs().getTemplateByViewCodeName(template_viewname, webSiteId);
        } else {
            returnMessage = getMessage(UDP_EWP_DOCTYPE_SESSION_CLOSE);
        }

        if (ewpTemplateVo != null) {
            returnMessage = getMessage(UDP_EWP_TEMPLATE_VIEWCODE_EXISTS);
            isPass = BooleanConstants.NO;
        } else {
            returnMessage = getMessage(UDP_EWP_TEMPLATE_VIEWCODE_DOES_NOT_EXIST);
            isPass = BooleanConstants.YES;
        }
        response.getServletResponse().setContentType("text/plain; charset=UTF-8");
        String returnValue = "{isPass:\"" + isPass + "\",returnMessage:\"" + returnMessage + "\"}";
        response.getServletResponse().getWriter().print(returnValue);
        response.getServletResponse().getWriter().flush();

        return DEFAULT_FORWARD;
    }

    /**
     * 判断是否为默认模板
     *
     * @param formBean 前台封闭的数据对象
     * @param request  请求
     * @param response 响应
     * @return IForward  业务完成后的跳转对象，此方法为ajax调用
     * @throws Exception 抛出保存模板中出现的异常
     */
    public IForward isDefaultTemplate(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        String id = request.getParameter(ID);
        String isPass = "wrong";

        EwpTemplate temp = getBs().find(id);
        if (temp.getTemplate_type().equals(DOCTYPE_TEMPLATE) || temp.getTemplate_type().equals(DOCUMENT_TEMPLATE)) {
            if (temp.getIsDefault().equals(BooleanConstants.NO)) {
                isPass = BooleanConstants.NO;
            } else {
                isPass = BooleanConstants.YES;
            }
        }

        response.getServletResponse().setContentType("text/plain; charset=UTF-8");
        String returnValue = "{isPass:\"" + isPass + "\"}";
        response.getServletResponse().getWriter().print(returnValue);
        response.getServletResponse().getWriter().flush();
        return DEFAULT_FORWARD;
    }

    /**
     * 设置默认模板，分页显示，支持表单回写
     *
     * @param formBean 前台封闭的数据对象
     * @param request  请求
     * @param response 响应
     * @return IForward  业务完成后的跳转对象
     * @throws Exception 抛出保存模板中出现的异常
     */
    public IForward updateDefaultTemplate(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        getBs().updateDefaultTemplate(request.getParameter(REQUEST_ID));
        return simpleQuery(formBean, request, response);
    }

    private int doImportTemplate(Website webSite) throws Exception {
        int addCount = 0;
        String filepath = getRealPath() + FileSystemConstants.VIEW_PATH + webSite.getWebsiteCode();
        File file = new File(filepath);
        if (file != null && file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null && files.length > 0) {
                    List<EwpTemplate> exists = getBs().queryAll(webSite.getId());
                    HashSet set = new HashSet();
                    //  Map<String,EwpTemplate> tempExist= new HashMap<String,EwpTemplate>();//用map存储数据库中已有模板数据
                    for (EwpTemplate temp : exists) {
                        set.add(temp.getTemplate_viewname());
                        //  tempExist.put(temp.getTemplate_viewname(), temp);//数据库中得 viewname与文件系统中得模板名字相同
                    }
                    TreeMap<String, String> treeMap = EnumTools.getSortedEnumMap(EnumTools.TEMPLATETYPE);
                    Iterator it = treeMap.entrySet().iterator();
                    String firstTemplateTyep = null;
                    if (it.hasNext()) {
                        firstTemplateTyep = (String) ((Entry) it.next()).getKey();
                    }
                    treeMap.entrySet().iterator().next();
                    for (File f : files) {
                        if (!f.isFile()) {
                            continue;
                        }
                        String filename = f.getName();
                        StringBuffer fileContent = new StringBuffer();
                        BufferedReader reader = new BufferedReader(new FileReader(f));
                        String tempContent = reader.readLine();
                        while (tempContent != null && !"".equals(tempContent)) {
                            fileContent.append(tempContent);
                            fileContent.append("\r\n");
                            tempContent = reader.readLine();
                        }
                        reader.close();
                        int suffixIndex = filename.indexOf(".ftl");
                        if (suffixIndex != -1) {
                            filename = filename.substring(0, suffixIndex);
                            if (!set.contains(filename)) {//新增模板数据
                                EwpTemplate bean = new EwpTemplate();
                                bean.setTemplate_viewname(filename);
                                bean.setTemplate_name(filename);
                                bean.setTemplate_type(firstTemplateTyep);
                                bean.setWebSite(webSite);
                                bean.setIsDefault("N");
                                bean.setTemplate_content(fileContent.toString()); //2012-02-03新加模板内容
                                bean.setCreate_date(new Timestamp(f.lastModified()));//应用创建时间来存储文档的最后更改时间
                                getBs().insertWithoutTemplateContent(bean);
                                addCount++;//新加模板数据
                            }
                        }
                    }
                }
            }
        }
        return addCount;
    }
}
