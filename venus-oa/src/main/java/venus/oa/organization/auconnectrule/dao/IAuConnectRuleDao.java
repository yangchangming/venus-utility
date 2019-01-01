package venus.oa.organization.auconnectrule.dao;

import venus.pub.lang.OID;

import java.util.List;

/**
 * 团体连接规则DAO
 * @author wumingqiang
 *
 */
public interface IAuConnectRuleDao {

    /**
     * 查询所有
     * @param no
     * @param size
     * @param orderStr
     * @return
     */
    public List queryAll(int no, int size, String orderStr);

    /**
     * 获得记录数
     * @return
     */
    public int getRecordCount();

    /**
     * 删除
     * @param id
     * @return
     */
    public int delete(String id);

    /**
     * 按条件获得记录数
     * @param queryCondition
     * @return
     */
    public int getRecordCount(String queryCondition);

    /**
     * 按条件查询,返回LIST
     * @param no
     * @param size
     * @param orderStr
     * @param objVo
     * @return
     */
    public List simpleQuery(int no, int size, String orderStr, Object objVo);

    /**
     *  添加
     * @param rvo
     * @return
     */
    public OID insert(Object objVo);

    /**
     * 查找
     * @param id
     * @return
     */
    public Object find(String id);


    /**
     * 更新
     * @param objVo
     * @return
     */
    public int update(Object objVo);

  
    /**
     *按条件查询获取记录数
     * @param m
     * @param n
     * @return
     */
    public int getRecordCount(String m, String n);
    
    /**
     * 按条件查询,返回LIST
     * @param no
     * @param size
     * @param orderStr
     * @param objVo
     * @return
     */
    public List queryByName(Object objVo);
    
    /**
     * 按条件查询,返回LIST
     * @param objVo
     * @return
     */
    public List queryByType(Object objVo);
    
}

