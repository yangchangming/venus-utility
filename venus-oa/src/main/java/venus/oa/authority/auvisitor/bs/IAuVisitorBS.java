package venus.oa.authority.auvisitor.bs;

import venus.oa.authority.auvisitor.vo.AuVisitorVo;

import java.util.List;

/**
 * 团体类型BS
 * @author wumingqiang
 *
 */
public interface IAuVisitorBS {
    
    /**
     * 通过团体类型分类查询所有
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
     * 删除
     * @param id
     * @return
     */
    public int delete(String id);
    /**
     * 
     * 功能: 根据访问者原始ID删除
     *
     * @param origId
     * @return
     */
    public int deleteByOrigId(String origId) ;

    /**
     *  添加
     * @param rvo
     * @return
     */
    public String insert(Object objVo);

    /**
     * 更新
     * @param objVo
     * @return
     */
    public int update(Object objVo);   
 
    /**
     * 
     * 功能: 根据团体关系ID和团体类型查询相应的访问者Vo，如果查不到则自动生成一个访问者vo并添加到访问者表中，然后返回新添加的访问者vo
     *
     * @param relId 团体关系ID
     * @param pType 团体类型
     * @return
     */
    public AuVisitorVo queryByRelationId(String relId, String pType);
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
     */
    public AuVisitorVo find(String visitorId);
}

