package venus.oa.util.common.bs.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import venus.oa.util.common.bs.ICommonBs;
import venus.oa.util.common.dao.ICommonDao;
import venus.frames.base.bs.BaseBusinessService;

import java.util.List;

@Service
public class CommonBs extends BaseBusinessService implements ICommonBs {
    
    @Autowired
    private ICommonDao commonDao;

    /**
     * 通用的方法，返回自己控制的对象
     *
     * @param strsql 要执行的sql语句
     * @param rowMapper 回调方法
     * @return 自己控制的对象列表
     */
    public List doQuery(String strsql, RowMapper rowMapper) {
        return commonDao.doQuery(strsql, rowMapper);
    }
    
    /**
     * 通用的方法，返回自己控制的对象
     *
     * @param strsql 要执行的sql语句
     * @param rowMapper 回调方法
     * @param no 当前页数
     * @param size 每页记录数
     * @return 自己控制的对象列表
     */
    public List doQuery(String strsql, RowMapper rowMapper, int no, int size) {
        return commonDao.doQuery(strsql, rowMapper, no, size);
    }
    
    /**
     * 通用的方法，返回自己控制的对象
     *
     * @param strsql 要执行的sql语句
     * @param rowMapper 回调方法
     * @return 自己控制的对象
     */
    public Object doQueryForObject(String strsql, RowMapper rowMapper) {
        return commonDao.doQueryForObject(strsql, rowMapper);
    }
    
    /**
     * 通用的方法，执行查询，返回int
     *
     * @param strsql 要执行的sql语句
     * @return 查询结果int
     */
    public int doQueryForInt(String strsql) {
        return commonDao.doQueryForInt(strsql);
    }
    
    /**
     * 通用的方法，执行查询，返回long
     *
     * @param strsql 要执行的sql语句
     * @return 查询结果long
     */
    public long doQueryForLong(String strsql) {
        return commonDao.doQueryForLong(strsql);
    }
    
    /**
     * 通用的方法，执行更新，返回更新的记录条数
     *
     * @param strsql 要执行的sql语句
     * @return 更新记录条数
     */
    public int doUpdate(String strsql) {
        return commonDao.doUpdate(strsql);
    }
    
    /**
     * 通用的方法，执行sql语句
     *
     * @param strsql 要执行的sql语句
     */
    public void doExecute(String strsql) {
        commonDao.doExecute(strsql);
    }

}

