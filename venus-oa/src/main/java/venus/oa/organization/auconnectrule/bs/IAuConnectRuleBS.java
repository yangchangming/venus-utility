package venus.oa.organization.auconnectrule.bs;

import venus.pub.lang.OID;

import java.util.List;

/**
 * 团体连接规则BS
 * @author wumingqiang
 *
 */
public interface IAuConnectRuleBS {
  
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
     * 删除
     * @param id
     * @return
     */
    public int delete(String id);

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
    public Object find(String objid);


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
     * 
     * 功能: 根据团体关系类型\父团体类型\子团体类型查询
     *
     * @param objVo
     * @return
     */
    public List queryByType(Object objVo);
}

