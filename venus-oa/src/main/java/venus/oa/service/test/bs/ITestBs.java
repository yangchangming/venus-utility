/*
 * 创建日期 2006-12-11
 *
 */
package venus.oa.service.test.bs;

import java.util.List;

/**
 * @author tony
 *
 */
public interface ITestBs {
    
    /**
     *  通过表名获得记录数
     * @param tableName
     * @return
     */
    public int getRecordCount(String tableName);

    /**
     * 按表名查询,返回LIST
     * @param no
     * @param size
     * @param tableName
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

