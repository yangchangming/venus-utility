/*
 * 
 * 创建日期 2006-12-11
 *
 */
package venus.oa.service.test.bs.impl;


import venus.oa.service.test.bs.ITestBs;
import venus.oa.service.test.dao.ITestDAO;
import venus.frames.base.bs.BaseBusinessService;

import java.util.List;

/**
 * @author maxiao
 *
 */
public class TestBs extends BaseBusinessService implements ITestBs {
    
    private ITestDAO dao = null;
    
    /**
     * @return 返回 dao。
     */
    public ITestDAO getDao() {
        return dao;
    }
    /**
     * @param dao 要设置的 dao。
     */
    public void setDao(ITestDAO dao) {
        this.dao = dao;
    }
    
    /**
     * 按条件查询,返回LIST
     * @param no
     * @param size
     * @param orderStr
     * @param objVo
     * @return
     */
    public List queryAll(int no, int size, String orderStr,String tableName) {
        return getDao().queryAll(no, size, orderStr,tableName);
    }

    /**
     * 按条件获得记录数
     *
     * @return
     */
    public int getRecordCount(String tableName) {
        return getDao().getRecordCount(tableName);
    }
    /**
     * 查询表名,返回LIST
     * @param orderStr
     * @return
     */
    public List queryAllTable(String orderStr){
        return getDao().queryAllTable(orderStr);
    }
}

