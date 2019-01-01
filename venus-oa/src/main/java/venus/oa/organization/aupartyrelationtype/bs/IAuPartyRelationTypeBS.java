package venus.oa.organization.aupartyrelationtype.bs;

import venus.pub.lang.OID;

import java.util.List;

/**
 * 团体连接关系类型BS
 * @author wumingqiang
 *
 */
public interface IAuPartyRelationTypeBS {
  
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
     * List 中是LabelValueBean
     * @return
     */
    public List getPartyAll();
    
    /**
     * 启用
     * @param id
     * @return
     */
    public int enable(String id);
    
    /**
     * 禁用
     * @param id
     * @return
     */
    public int disable(String id);

    /**
     * 根据KeyWord获得ID
     * @param keyword
     * @return
     */
    public String getIdByKeyWord(String keyword);
    
    /**
     * 
     * 功能: 查询所有启用状态的团体关系类型
     * 当参数no和size小于或等于0时，不翻页
     * 当参数orderStr为null时，不进行排序
     * @param no
     * @param size
     * @param orderStr
     * @return
     */
    public List queryAllEnable(int no, int size, String orderStr);
    /**
     * List 中是LabelValueBean
     * @return
     */
    public List getPartyAllByKeyWord(String keyword);
}

