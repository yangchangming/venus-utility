package venus.oa.organization.aupartytype.bs;

import venus.oa.organization.aupartytype.vo.AuPartyTypeVo;

import java.util.List;

/**
 * 团体类型BS
 * @author wumingqiang
 *
 */
public interface IAuPartyTypeBS {
  
    /**
     * 查询所有
     * @param no
     * @param size
     * @param orderStr
     * @return
     */
    public List queryAll(int no, int size, String orderStr);

    /**
     * 
     * 功能: 查询所有启用状态的团体类型
     * 当参数no和size小于或等于0时，不翻页
     * 当参数orderStr为null时，不进行排序
     * @param no
     * @param size
     * @param orderStr
     * @return
     */
    public List queryAllEnable(int no, int size, String orderStr);
    
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
    public String insert(Object objVo);

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
     * 根据KeyWord获得所有该类型的party列表
     * @return
     */
    public List getPartysByKeyWord(String keyword);
    /**
     * List 中是LabelValueBean
     * @return
     */
    public List getPartyAllByKeyword(String keyword);
    
    /**
     * 生成物理程序
     * @return
     */
    public String generatePhysicsCode(String paraValue, String paraArray, AuPartyTypeVo obj);

    /**
     * 新增物理程序
     * @param paraValue
     * @param paraArray
     * @param obj
     */
    public String appendPhysicsCode(String paraValue, String paraArray, AuPartyTypeVo obj);
}

