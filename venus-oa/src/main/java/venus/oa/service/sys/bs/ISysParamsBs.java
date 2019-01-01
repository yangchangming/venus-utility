/*
 * 创建日期 2008-7-31
 */
package venus.oa.service.sys.bs;

import venus.oa.service.sys.dao.ISysParamDao;
import venus.oa.service.sys.vo.SysParamVo;
import venus.pub.lang.OID;

import java.util.List;

/**
 *  2008-7-31
 * @author changming.Y <changming.yang.ah@gmail.com>
 */
public interface ISysParamsBs {

    /**
     * 设置数据访问接口
     * 
     * @return
     */
    public ISysParamDao getDao();

    /**
     * 获取数据访问接口
     * 
     * @param dao
     */
    public void setDao(ISysParamDao dao);

    /**
     * 插入单条记录
     * 
     * @param vo 用于添加的VO对象
     * @return 若添加成功，返回新生成的Oid
     */
    public OID insert(SysParamVo vo);

    /**
     * 删除单条记录
     * 
     * @param id 用于删除的记录的id
     * @return 成功删除的记录数
     */
    public int delete(String id);

    /**
     * 根据Id进行查询
     * 
     * @param id 用于查找的id
     * @return 查询到的VO对象
     */
    public SysParamVo find(String id);

    /**
     * 更新单条记录
     * 
     * @param vo 用于更新的VO对象
     * @return 成功更新的记录数
     */
    public int update(SysParamVo vo);

    /**
     * 通过查询条件获得所有的VO对象列表，不带翻页查全部
     *
     * @param queryCondition 查询条件
     * @return 查询到的VO列表
     */
    public List queryByCondition(String queryCondition);
    
    /**
     * 通过查询条件获得所有的VO对象列表，不带翻页查全部，带排序字符
     *
     * @param queryCondition 查询条件
     * @param orderStr 排序字符
     * @return 查询到的VO列表
     */
    public List queryByCondition(String queryCondition, String orderStr);
}

