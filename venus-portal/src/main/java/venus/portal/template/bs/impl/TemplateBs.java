package venus.portal.template.bs.impl;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import venus.frames.base.bs.BaseBusinessService;
import venus.frames.base.exception.BaseApplicationException;
import venus.frames.i18n.util.LocaleHolder;
import venus.frames.mainframe.util.PathMgr;
import venus.portal.cache.data.DataCache;
import venus.portal.helper.EwpDateHelper;
import venus.portal.template.bs.ITemplateBs;
import venus.portal.template.dao.ITemplateDao;
import venus.portal.template.model.EwpTemplate;
import venus.portal.template.util.ITemplateConstants;
import venus.portal.util.BooleanConstants;
import venus.portal.util.EwpFileUtils;
import venus.portal.util.model.CopyFileModel;
import venus.portal.website.model.Website;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.apache.commons.lang.ArrayUtils.isEmpty;


public class TemplateBs extends BaseBusinessService implements ITemplateBs, ITemplateConstants {

    private static String BASE_FTL_TEMPLATE_PATH = "/WEB-INF/ftlTemplate";
    private static String INCLUDE_FTL_TEMPLATE_PATH = "/WEB-INF/ftlTemplate/include";
    private static String SITE_FTL_TEMPLATE_PATH = "/WEB-INF/ftlTemplate/webSite";

    /**
     * 模板数据访问层的实例
     */
    private ITemplateDao dao = null;

    /**
     * 数据缓存
     */
    private DataCache dataCache;

    /**
     * 设置数据访问接口
     *
     * @return
     */
    public ITemplateDao getDao() {
        return dao;
    }

    /**
     * 获取数据访问接口
     *
     * @param dao
     */
    public void setDao(ITemplateDao dao) {
        this.dao = dao;
    }

    /**
     * 设置数据缓存
     *
     * @return
     */
    public void setDataCache(DataCache dataCache) {
        this.dataCache = dataCache;
    }

    /**
     * 插入单条模板记录
     *
     * @param vo 模板VO对象
     * @return 若添加成功，返回新生成的主键对象OID
     * @throws IOException 读写文件失败时会抛出此异常
     */
    public void insert(EwpTemplate ewpTemplateVO) throws Exception {

        // 模板VO保存到了数据库
        getDao().insert(ewpTemplateVO);

        // 把模板内容保存到文件系统
        String filepath = ewpTemplateVO.getFilepath(); // 文件路径
        String directory = ewpTemplateVO.getDirectory(); // 文件目录
        OutputStreamWriter fw = null;
        BufferedWriter bw = null;
        try {
            // 如果目录不存在则创建目录,不创建目录直接创建文件会抛出FileNotFoundException
            File dir = new File(directory);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            // 保存模板内容文件
            fw = new OutputStreamWriter(new FileOutputStream(new File(filepath)), "UTF-8");
            bw = new BufferedWriter(fw);
            bw.write(ewpTemplateVO.getTemplate_content());
        } catch (IOException e) {
            throw e;
        } finally {
            if (bw != null) {
                bw.close();
            }
            if (fw != null) {
                fw.close();
            }
        }
    }

    /**
     * 插入方法
     *
     * @param siteId 站点ID
     * @return 查询到的VO列表
     */
    public void insertWithoutTemplateContent(EwpTemplate bean) {
        getDao().insert(bean);
    }

    /**
     * 删除多条记录
     *
     * @param id 用于删除的记录的id
     * @return 成功删除的记录数
     */
    public void delete(String ids[]) {
        if (!isEmpty(ids)) {
            for (String id : ids) {
                EwpTemplate ewpTemplate = find(id);

                delete(ewpTemplate);
            }
        }
    }

    /**
     * 删除单条记录
     *
     * @param id 用于删除的记录的id
     * @return 成功删除的记录数
     */
    public void delete(EwpTemplate ewpTemplate) {
        String filepath = ewpTemplate.getOldfilepath();
        if (filepath != null && StringUtils.isNotBlank(filepath)) {
            File file = new File(filepath);
            if (file.exists()) {
                try {
                    file.delete();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        getDao().delete(ewpTemplate);
    }

    /**
     * 删除站点id下的所有模板
     *
     * @param websiteId 用于删除的记录的站点ID
     * @return 成功删除的记录数
     */
    public void deleteByWebSiteId(String websiteId) {
        getDao().deleteByWebSiteId(websiteId);
    }

    /**
     * 根据Id进行查询
     *
     * @param id 用于查找的id
     * @return 查询到的VO对象
     */
    public EwpTemplate find(String id) {
        EwpTemplate vo = getDao().findEwpTemplateById(id);
        return vo;
    }

    /**
     * 更新单条记录
     *
     * @param vo 用于更新的VO对象
     * @return 成功更新的记录数
     * @throws IOException
     */
    public int update(EwpTemplate vo) throws Exception {
        getDao().update(vo);
        // 保存到文件系统
        String oldfilepath = vo.getOldfilepath();
        String filepath = vo.getFilepath();
        try {
            if (oldfilepath != null && StringUtils.isNotBlank(oldfilepath) && (!StringUtils.equals(oldfilepath, filepath))) {
                File file = new File(oldfilepath);
                if (file.exists()) {
                    try {
                        file.delete();
                    } catch (Exception ex) {
                        throw ex;
                    }
                }
            }
            OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(new File(filepath)), "UTF-8");
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(vo.getTemplate_content());
            bw.close();
            fw.close();
        } catch (IOException e) {
            throw e;
        }
        return 0;
    }

    /**
     * 查询总记录数，带查询条件
     *
     * @param queryCondition 查询条件
     * @return 总记录数
     */
    public int getRecordCount(String queryCondition) {
        int sum = getDao().getRecordCount(queryCondition);
        return sum;
    }

    /**
     * 通过查询条件获得所有的VO对象列表，带翻页，带排序字符
     *
     * @param no             当前页数
     * @param size           每页记录数
     * @param queryCondition 查询条件
     * @param orderStr       排序字符
     * @return 查询到的VO列表
     */
    public List queryByCondition(int no, int size, String queryCondition, String orderStr) {
        List lResult = getDao().queryByCondition(no, size, queryCondition, orderStr);
        return lResult;
    }

    /**
     * 通过查询条件获得所有的VO对象
     *
     * @param siteId 站点ID
     * @return 查询到的VO列表
     */
    public List queryAll(String siteId) {
        List lResult = getDao().queryAll(siteId);
        return lResult;
    }


    public EwpTemplate getTemplateByName(String name, String siteId) {
        EwpTemplate result = getDao().getTemplateByName(name, siteId);
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see udp.ewp.template.bs.ITemplateBs#getTemplateByViewCodeName(java.lang.String)
     */
    public EwpTemplate getTemplateByViewCodeName(String viewCode, String siteId) {
        EwpTemplate result = getDao().getTemplateByViewCodeName(viewCode, siteId);
        return result;
    }

    public void updateDefaultTemplate(String id) throws Exception {
        EwpTemplate beanYes = getDao().findEwpTemplateById(id);
        List list = getDao().getTemplateByIsDefault(BooleanConstants.YES, beanYes.getWebSiteId());

        for (int i = 0; i < list.size(); i++) {
            EwpTemplate beanNo = (EwpTemplate) list.get(i);
            if (id.equals(beanNo.getId())) {
                return;
            }

            if (beanNo.getTemplate_type().equals(beanYes.getTemplate_type())) {
                beanNo.setIsDefault(BooleanConstants.NO);
                getDao().update(beanNo);
                break;
            }
        }

        beanYes.setIsDefault(BooleanConstants.YES);
        beanYes.setVersion(null);
        getDao().update(beanYes);

        dataCache.refreshWebsites();
    }

    public List getTemplateByIsDefault(String is, String siteId) {
        return dao.getTemplateByIsDefault(is, siteId);
    }

    public List<Website> getWebsiteListFromCache() {
        return dataCache.getWebsitesData();
    }

    public void createNewWebSiteBaseTemplate(Website website) {
        File layoutDir = new File(PathMgr.getRealRootPath() + "/WEB-INF/layouts/" + website.getWebsiteCode());
        File viewDir = new File(PathMgr.getRealRootPath() + "/WEB-INF/views/" + website.getWebsiteCode());

        checkDir(layoutDir, website.getWebsiteCode());
        checkDir(viewDir, website.getWebsiteCode());

        try {
            File srcDir = new File(PathMgr.getRealRootPath() + BASE_FTL_TEMPLATE_PATH);
            copyLayoutTemplateFtl(srcDir, website.getWebsiteCode());
            addCurrentSiteLayoutToGlobalLayoutFile(website.getWebsiteCode());

            srcDir = new File(PathMgr.getRealRootPath() + INCLUDE_FTL_TEMPLATE_PATH);
            copyIncludeTemplate(srcDir, website.getWebsiteCode());

            srcDir = new File(PathMgr.getRealRootPath() + SITE_FTL_TEMPLATE_PATH);
            copySiteTemplate(srcDir, website.getWebsiteCode());

            initDefaultTemplate(website);
        } catch (IOException e) {
            throw new BaseApplicationException("IOException occurs during copy layoutTemplates,Pls check it!", e);
        }

    }

    private void checkDir(File dir, String siteCode) {
        if (!dir.exists()) {
            dir.mkdir();
        }

        if (!dir.isDirectory()) {
            throw new BaseApplicationException(dir.getAbsolutePath() + " is not a directory! Pls check it!");
        }
        if (!ArrayUtils.isEmpty(dir.listFiles())) {
            throw new BaseApplicationException(LocaleHolder.getMessage(UDP_EWP_TEMPLATE_FOLDER_IS_EXIST, siteCode));
        }
    }

    private void copyLayoutTemplateFtl(File srcDir, String websiteCode) throws IOException {
        File destDir = new File(PathMgr.getRealRootPath() + "/WEB-INF/layouts/");
        LinkedList<CopyFileModel> models = new LinkedList<CopyFileModel>();
        models.add(new CopyFileModel("template_layout.ftl", websiteCode + "_layout.ftl"));
        EwpFileUtils.copyFilesByPattern(srcDir, destDir, models);
    }

    private void addCurrentSiteLayoutToGlobalLayoutFile(String websiteCode) throws IOException {
        File globalLayoutFile = new File(PathMgr.getRealRootPath() + "/WEB-INF/layouts/layouts.ftl");
        FileWriter fileWriter;
        BufferedWriter bufferWriter = null;
        try {
            fileWriter = new FileWriter(globalLayoutFile.getAbsolutePath(), true);
            bufferWriter = new BufferedWriter(fileWriter);
            bufferWriter.write("\n");
            bufferWriter.write("<#include \"" + websiteCode + "_layout.ftl\">");

        } finally {
            bufferWriter.close();
        }
    }

    private void copyIncludeTemplate(File srcDir, String websiteCode) throws IOException {
        File destDir = new File(PathMgr.getRealRootPath() + "/WEB-INF/layouts/" + websiteCode + "/include");
        if (!destDir.exists()) {
            destDir.mkdir();
        }

        LinkedList<CopyFileModel> models = new LinkedList<CopyFileModel>();
        models.add(new CopyFileModel("header.ftl"));
        models.add(new CopyFileModel("footer.ftl"));
        models.add(new CopyFileModel("head.ftl"));
        EwpFileUtils.copyFilesByPattern(srcDir, destDir, models);
    }

    private void copySiteTemplate(File srcDir, String websiteCode) throws IOException {
        File destDir = new File(PathMgr.getRealRootPath() + "/WEB-INF/views/" + websiteCode);
        if (!destDir.exists()) {
            destDir.mkdir();
        }

        LinkedList<CopyFileModel> models = new LinkedList<CopyFileModel>();
        models.add(new CopyFileModel("menu.ftl"));
        models.add(new CopyFileModel("welcome.ftl"));
        models.add(new CopyFileModel("document.ftl"));
        models.add(new CopyFileModel("common_notexist.ftl"));
        EwpFileUtils.copyFilesByPattern(srcDir, destDir, models);
    }

    private void initDefaultTemplate(Website website) {
        ArrayList<String> tplList = new ArrayList<String>();
        tplList.add("menu");
        tplList.add("welcome");
        tplList.add("document");
        tplList.add("common_notexist");

        for (String tplName : tplList) {
            EwpTemplate tpl = new EwpTemplate(tplName, website, EwpDateHelper.getSysTimestamp());
            getDao().insert(tpl);
        }

    }
}
