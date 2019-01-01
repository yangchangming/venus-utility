/*
 * 创建日期 2006-10-24
 *
 */
package venus.oa.service.test.dao;

import java.util.List;

/**
 * @author maxiao
 *
 */
public interface ITestDAO {
    
    /**
     * 按条件获得记录数
     * @param queryCondition
     * @return
     */
    public int getRecordCount(String tableName);

    /**
     * 按条件查询,返回LIST
     * @param no
     * @param size
     * @param orderStr
     * @param objVo
     * @return
     */
    public List queryAll(int no, int size, String orderStr, String tableName);
    /**
     * 查询表名,返回LIST
     * @param orderStr
     * @return
     */
    public List queryAllTable(String orderStr);
}

