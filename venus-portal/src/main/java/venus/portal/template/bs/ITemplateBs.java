package venus.portal.template.bs;



import venus.portal.template.dao.ITemplateDao;
import venus.portal.template.model.EwpTemplate;
import venus.portal.website.model.Website;

import java.util.List;

/**
 * @author zhangrenyang
 */

public interface ITemplateBs {

    /**
     * 设置数据访问接口
     *
     * @return
     */
    public ITemplateDao getDao();

    /**
     * 获取数据访问接口
     *
     * @param dao
     */
    public void setDao(ITemplateDao dao);

    /**
     * 插入单条记录
     *
     * @param vo 用于添加的VO对象
     * @return 若添加成功，返回新生成的Oid
     */
    public void insert(EwpTemplate vo) throws Exception;

    /**
     * 删除多条记录
     *
     * @param id 用于删除的记录的id
     * @return 成功删除的记录数
     */
    public void delete(String ids[]);

    /**
     * 删除单条记录
     *
     * @param id 用于删除的记录的id
     * @return 成功删除的记录数
     */
    public void delete(EwpTemplate ewpTemplate);

    /**
     * 删除站点id下的所有模板
     *
     * @param websiteId 用于删除的记录的站点ID
     * @return 成功删除的记录数
     */
    public void deleteByWebSiteId(String websiteId);

    /**
     * 根据Id进行查询
     *
     * @param id 用于查找的id
     * @return 查询到的VO对象
     */
    public EwpTemplate find(String id);

    /**
     * 更新单条记录
     *
     * @param vo 用于更新的VO对象
     * @return 成功更新的记录数
     */
    public int update(EwpTemplate vo) throws Exception;

    /**
     * 查询总记录数，带查询条件
     *
     * @param queryCondition 查询条件
     * @return 总记录数
     */
    public int getRecordCount(String queryCondition);

    /**
     * 通过查询条件获得所有的VO对象列表，带翻页，带排序字符
     *
     * @param no             当前页数
     * @param size           每页记录数
     * @param queryCondition 查询条件
     * @param orderStr       排序字符
     * @return 查询到的VO列表
     */
    public List queryByCondition(int no, int size, String queryCondition, String orderStr);

    /**
     * 通过查询条件获得所有的VO对象
     *
     * @param siteId 站点ID
     * @return 查询到的VO列表
     */
    public List queryAll(String siteId);

    /**
     * 插入方法
     *
     * @param siteId 站点ID
     * @return 查询到的VO列表
     */
    public void insertWithoutTemplateContent(EwpTemplate bean);


    /**
     * 根据name和siteId进行查询
     *
     * @param name   用于查找的name
     * @param siteId 用于查找的siteId
     * @return 查询到的VO对象
     */
    public EwpTemplate getTemplateByName(String name, String siteId);

    /**
     * 根据viewCode 和siteId进行查询
     *
     * @param viewCode 用于查找的viewCode
     * @param siteId   用于查找的siteId
     * @return 查询到的VO对象
     */
    public EwpTemplate getTemplateByViewCodeName(String viewCode, String siteId);

    /**
     * 更新默认模板
     *
     * @param id 用于查找的id
     * @return void
     */
    public void updateDefaultTemplate(String id) throws Exception;

    /**
     * 查询站点siteId下的默认模板
     *
     * @param is     是否默认
     * @param siteId 站点id
     * @return void
     */
    public List getTemplateByIsDefault(String is, String siteId);

    /**
     * 从缓存中获取所有站点的信息
     *
     * @return
     */
    public List<Website> getWebsiteListFromCache();

    public void createNewWebSiteBaseTemplate(Website website);

}
