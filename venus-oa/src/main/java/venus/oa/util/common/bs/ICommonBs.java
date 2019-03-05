/*
 * 系统名称: acl
 * 
 * 文件名称: venus.authority.util.common.bs --> ICommonBs.java
 * 
 * 功能描述:
 * 
 * 版本历史:
 * 2006-10-24 10:06:10 创建1.0.0版 (ganshuo)
 * 
 */
package venus.oa.util.common.bs;

import org.springframework.jdbc.core.RowMapper;

import java.util.List;

public interface ICommonBs {

    /**
     * 通用的方法，返回自己控制的对象
     *
     * @param strsql 要执行的sql语句
     * @param rowMapper 回调方法
     * @return 自己控制的对象列表
     */
    public List doQuery(String strsql, RowMapper rowMapper);
    
    /**
     * 通用的方法，返回自己控制的对象
     *
     * @param strsql 要执行的sql语句
     * @param rowMapper 回调方法
     * @param no 当前页数
     * @param size 每页记录数
     * @return 自己控制的对象列表
     */
    public List doQuery(String strsql, RowMapper rowMapper, int no, int size);
    
    /**
     * 通用的方法，返回自己控制的对象
     *
     * @param strsql 要执行的sql语句
     * @param rowMapper 回调方法
     * @return 自己控制的对象
     */
    public Object doQueryForObject(String strsql, RowMapper rowMapper);
    
    /**
     * 通用的方法，执行查询，返回int
     *
     * @param strsql 要执行的sql语句
     * @return 查询结果int
     */
    public int doQueryForInt(String strsql);
    
    /**
     * 通用的方法，执行查询，返回long
     *
     * @param strsql 要执行的sql语句
     * @return 查询结果long
     */
    public long doQueryForLong(String strsql);
    
    /**
     * 通用的方法，执行更新，返回更新的记录条数
     *
     * @param strsql 要执行的sql语句
     * @return 更新记录条数
     */
    public int doUpdate(String strsql);
    
    /**
     * 通用的方法，执行sql语句
     *
     * @param strsql 要执行的sql语句
     */
    public void doExecute(String strsql);
    
}

