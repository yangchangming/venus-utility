package venus.oa.authority.auvisitor.dao;

import venus.oa.authority.auvisitor.vo.AuVisitorVo;

import java.util.List;

/**
 * 团体类型DAO
 * @author wumingqiang
 *
 */
public interface IAuVisitorDao {
  
    /**
     * 通过团体类型分利查询所有
     * @param no
     * @param size
     * @param orderStr
     * @return
     */
    public List queryAllByTypes(int no, int size, String orderStr);

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
    public int getRecordCount(Object objVo);

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
     * 通过团体类型分类按条件查询,返回LIST
     * @param no
     * @param size
     * @param orderStr
     * @param objVo
     * @return
     */
    public List simpleQueryByTypes(int no, int size, String orderStr, Object objVo, String condition);

    /**
     *  添加
     * @param rvo
     * @return
     */
    public String insert(Object objVo);

    /**
     * 查找
     * @param id
     * @return
     */
    public Object findByOrgId(String id);
    
    public Object findByOrgId(String id, String type);

    /**
     * 更新
     * @param objVo
     * @return
     */
    public int update(Object objVo);   
    /**
     * 按条件获得记录数
     * 
     * @param queryCondition
     * @return
     */
    public int getRecordCountByTypes(String partyTypes);
    /**
     * 按条件获得记录数
     * 
     * @param queryCondition
     * @return
     */
    public int getRecordCountByTypes(String partyTypes, Object objVo, String condition);

    /**
     * @param visitorId
     * @return
     */
    public AuVisitorVo find(String visitorId);
 }

