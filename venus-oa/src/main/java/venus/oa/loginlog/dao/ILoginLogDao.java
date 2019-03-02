/*
 * 系统名称:单表模板 --> sample
 * 
 * 文件名称: venus.authority.login.loginlog.dao --> ILoginLogDao.java
 * 
 * 功能描述:
 * 
 * 版本历史: 2007-10-16 10:29:59.989 创建1.0.0版 (甘硕)
 *  
 */

package venus.oa.loginlog.dao;

import venus.oa.loginlog.vo.LoginLogVo;
import venus.oa.login.vo.LoginSessionVo;
import venus.oa.util.SqlBuilder;
import venus.pub.lang.OID;

import java.util.List;

/**
 * 功能、用途、现存BUG:
 * 
 * @author 甘硕
 * @version 1.0.0
 * @see 需要参见的其它类
 * @since 1.0.0
 */

public interface ILoginLogDao {
    
    /**
     * 插入单条记录，用Oid作主键，把null全替换为""
     * 
     * @param vo 用于添加的VO对象
     * @return 若添加成功，返回新生成的Oid
     */
    public OID insert(LoginLogVo vo);

    /**
     * 删除单条记录
     * 
     * @param id 用于删除的记录的id
     * @return 成功删除的记录数
     */
    public int delete(String id);

    /**
     * 删除多条记录
     * 
     * @param id 用于删除的记录的id
     * @return 成功删除的记录数
     */
    public int delete(String id[]);
    
    /**
     * 删除全部记录
     * @return 成功删除的记录数
     */
    public int deleteAll();

    /**
     * 根据Id进行查询
     * 
     * @param id 用于查找的id
     * @return 查询到的VO对象
     */
    public LoginLogVo find(String id);

    /**
     * 更新单条记录
     * 
     * @param vo 用于更新的VO对象
     * @return 成功更新的记录数
     */
    public int update(LoginLogVo vo);

    /**
     * 查询总记录数，带查询条件
     * 
     * @param queryCondition 查询条件
     * @param authorizedContext
     * @return 总记录数
     */
    public int getRecordCount(String queryCondition, LoginSessionVo authorizedContext);
    
    /**
     * 查询总记录数，带查询条件
     * 
     * @param queryCondition 查询条件
     * @param authorizedContext
     * @return 总记录数
     */
    public int getRecordCount(SqlBuilder sql);

    /**
     * 不带条件查询，也即查询获得所有的VO对象列表，不带翻页，默认排序
     *
     * @return 查询到的VO列表
     */
    public List queryByCondition();
    
    /**
     * 通过查询条件获得所有的VO对象列表，不带翻页查全部
     *
     * @param queryCondition 查询条件
     * @return 查询到的VO列表
     */
    public List queryByCondition(String queryCondition);
    
    /**
     * 通过查询条件获得所有的VO对象列表，不带翻页查全部
     *
     * @param queryCondition 查询条件
     * @return 查询到的VO列表
     */
    public List queryByCondition(SqlBuilder sql);
    
    /**
     * 通过查询条件获得所有的VO对象列表，不带翻页查全部，带排序字符
     *
     * @param queryCondition 查询条件
     * @param orderStr 排序字符
     * @return 查询到的VO列表
     */
    public List queryByCondition(String queryCondition, String orderStr);
    
    /**
     * 通过查询条件获得所有的VO对象列表，带翻页
     * 
     * @param no 当前页数
     * @param size 每页记录数
     * @param queryCondition 查询条件
     * @return 查询到的VO列表
     */
    public List queryByCondition(int no, int size, String queryCondition, LoginSessionVo AuthorizedContext);
    
    /**
     * 通过查询条件获得所有的VO对象列表，带翻页，带排序字符
     * 
     * @param no 当前页数
     * @param size 每页记录数
     * @param queryCondition 查询条件
     * @param orderStr 排序字符 
     * @return 查询到的VO列表
     */
    public List queryByCondition(int no, int size, String queryCondition, String orderStr, LoginSessionVo AuthorizedContext);

    /**
     * 通用的方法，执行更新，返回更新的记录条数
     *
     * @param strsql 要执行的sql语句
     * @return 更新记录条数
     */
    public int doUpdate(String strsql);
}

