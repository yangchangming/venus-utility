package venus.portal.website.bs;

import venus.portal.doctype.bs.IDocTypeBS;
import venus.portal.website.dao.IWebsiteDao;
import venus.portal.website.model.Website;

import java.util.List;

/**
 * @author zhangrenyang
 */

public interface IWebsiteBs {

    /**
     * 设置数据访问接口
     *
     * @return
     */
    public IWebsiteDao getDao();

    /**
     * 获取数据访问接口
     *
     * @param dao
     */
    public void setDao(IWebsiteDao dao);

    /**
     * 插入单条记录
     *
     * @param vo 用于添加的VO对象
     * @return 若添加成功，返回新生成的Oid
     */
    public void insert(Website vo);

    /**
     * 删除多条记录
     *
     * @param ids 用于删除的记录的ids
     * @return 成功删除的记录数
     */
    public int deleteMulti(String ids[]) throws Exception;

    /**
     * 删除单条记录
     *
     * @param vo 用于删除的记录VO
     * @return 成功删除的记录数
     */
    public void delete(Website vo) throws Exception;

    /**
     * 根据Id进行查询
     *
     * @param id 用于查找的id
     * @return 查询到的VO对象
     */
    public Website find(String id);

    /**
     * 更新单条记录
     *
     * @param vo 用于更新的VO对象
     * @return 成功更新的记录数
     */
    public void update(Website vo);

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
     * 查询所有的站点
     *
     * @param
     * @return void
     * @throws
     * @author zhangrenyang
     * @date 2011-10-20上午06:21:42
     */
    public List queryAll();


    /**
     * 根据name进行查询
     *
     * @param name 用于查找的name
     * @return 查询到的VO对象
     */
    public Website getWebsiteByName(String name);

    /**
     * 更新默认站点
     *
     * @param id 用于查找的id
     * @return void
     */
    public void updateDefaultWebsite(String id);

    /**
     * 取得栏目的业务处理类
     *
     * @param
     * @return void
     * @throws
     * @author zhangrenyang
     * @date 2011-10-27上午03:07:33
     */
    public IDocTypeBS getDocTypeBS();

    public void setDocTypeBS(IDocTypeBS docTypeBS);

    /**
     * 判断websiteCode是否唯一
     *
     * @param websiteCode 用于判断的websiteCode
     * @return 判断结果
     */
    boolean checkWebsiteCodeIsUnique(String websiteCode);

}
