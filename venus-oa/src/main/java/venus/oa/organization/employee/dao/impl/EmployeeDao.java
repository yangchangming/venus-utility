package venus.oa.organization.employee.dao.impl;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import venus.dao.provider.BaseTemplateDao;
import venus.frames.mainframe.util.Helper;
import venus.oa.organization.employee.dao.IEmployeeDao;
import venus.oa.organization.employee.util.IEmployeeConstants;
import venus.oa.organization.employee.vo.EmployeeVo;
import venus.oa.util.StringHelperTools;
import venus.oa.util.VoHelperTools;
import venus.pub.lang.OID;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * employee data access object
 */
@Repository
public class EmployeeDao extends BaseTemplateDao implements IEmployeeDao, IEmployeeConstants {

    /**
     * 插入单条记录，用Oid作主键，把null全替换为""
     * 
     * @param vo 用于添加的VO对象
     * @return 若添加成功，返回新生成的Oid
     */
    public OID insert(EmployeeVo vo) {
        
        VoHelperTools.null2Nothing(vo); //把vo中null值替换为""
        Object[] obj = { vo.getId(), vo.getPerson_no(),vo.getPerson_name(),vo.getEnglish_name(),vo.getPerson_type(),vo.getSex(),
                vo.getMobile(),vo.getTel(),vo.getEmail(),vo.getAddress(),vo.getPostalcode(),vo.getRemark(),vo.getEnable_status(),
                vo.getEnable_date(),vo.getCreate_date(),vo.getModify_date(),vo.getColumn1(),vo.getColumn2(),vo.getColumn3() };
        updateWithUniformArgType(SQL_INSERT, obj);
        return null;
    }

    /**
     * 删除单条记录
     * 
     * @param id 用于删除的记录的id
     * @return 成功删除的记录数
     */
    public int delete(String id) {
        return update(SQL_DELETE + " where ID=?", new Object[] { id });
    }

    /**
     * 删除多条记录
     * 
     * @param id 用于删除的记录的id
     * @return 成功删除的记录数
     */
    public int delete(String id[]) {
        String strsql = " WHERE ID IN (";
        if (id == null || id.length == 0)
            return 0;
        strsql += StringHelperTools.parseToSQLStringComma(id) + ")"; //把id数组转换为id1,id2,id3
        strsql = SQL_DELETE + strsql;
        return update(strsql);
    }

    /**
     * 根据Id进行查询
     * 
     * @param id 用于查找的id
     * @return 查询到的VO对象
     */
    public EmployeeVo find(String id) {
        return (EmployeeVo) queryForObject("select * from AU_EMPLOYEE where ID=?", new Object[] { id }, new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                EmployeeVo vo = new EmployeeVo();
                Helper.populate(vo, rs);
                return vo;
            }
        });
    }

    /**
     * 更新单条记录
     * 
     * @param vo 用于更新的VO对象
     * @return 成功更新的记录数
     */
    public int update(EmployeeVo vo) {
        VoHelperTools.null2Nothing(vo); //把vo中null值替换为""
        Object[] obj = { vo.getPerson_no(),vo.getPerson_name(),vo.getEnglish_name(),vo.getPerson_type(),vo.getSex(),vo.getMobile(),vo.getTel(),vo.getEmail(),vo.getAddress(),vo.getPostalcode(),vo.getRemark(),vo.getEnable_status(),vo.getEnable_date(),vo.getCreate_date(),vo.getModify_date(),vo.getColumn1(),vo.getColumn2(),vo.getColumn3(), vo.getId() };
        return updateWithUniformArgType(SQL_UPDATE, obj);
    }

    /**
     * 查询所有的VO对象列表，不翻页
     * 
     * @return 查询到的VO列表
     */
    public List queryAll() {
        return queryAll(-1, -1, null);
    }
    
    /**
     * 查询所有的VO对象列表，不翻页，带排序字符
     * 
     * @param orderStr 排序字符 
     * @return 查询到的VO列表
     */
    public List queryAll(String orderStr) {
        return queryAll(-1, -1, orderStr);
    }

    /**
     * 查询所有的VO对象列表，带翻页
     * 
     * @param no 当前页数
     * @param size 每页记录数
     * @return 查询到的VO列表
     */
    public List queryAll(int no, int size) {
        return queryAll(no, size, null);
    }

    /**
     * 查询所有的VO对象列表，带翻页，带排序字符
     * 
     * @param no 当前页数
     * @param size 每页记录数
     * @param orderStr 排序字符
     * @return 查询到的VO列表
     */
    public List queryAll(int no, int size, String orderStr) {
        String strsql = SQL_QUERY + DEFAULT_DESC_QUERY_WHERE_ENABLE + SQL_COMPANY_TYPE;
        if(orderStr == null ) {
            strsql += DEFAULT_DESC_ORDER_BY_ID;
        } else {
            strsql += " ORDER BY " + orderStr;
        }
        if(no <= 0 || size <= 0) {
            return query(strsql, new RowMapper() {
                public Object mapRow(ResultSet rs, int i) throws SQLException {
                    EmployeeVo vo = new EmployeeVo();
                    Helper.populate(vo, rs);
                    return vo;
                }
            });
        } else {
            return query(strsql, new RowMapper() {
                public Object mapRow(ResultSet rs, int i) throws SQLException {
                    EmployeeVo vo = new EmployeeVo();
                    Helper.populate(vo, rs);
                    return vo;
                }
            }, (no - 1) * size, size);
        }
    }

    /**
     * 查询总记录数
     * 
     * @return 总记录数
     */
    public int getRecordCount() {
        return getRecordCount(null);
    }

    /**
     * 查询总记录数，带查询条件
     * 
     * @param queryCondition 查询条件
     * @return 总记录数
     */
    public int getRecordCount(String queryCondition) {
        String strsql = SQL_COUNT + DEFAULT_DESC_QUERY_WHERE_ENABLE + SQL_COMPANY_TYPE;
        if (queryCondition != null && queryCondition.length() > 0) {
            strsql += " AND " + queryCondition; //where后加上查询条件
        }
        return queryForInt(strsql);
    }
    
    /**
     * 通过查询条件获得所有的VO对象列表，不带翻页查全部
     * 
     * @param queryCondition 查询条件
     * @return 查询到的VO列表
     */
    public List queryByCondition(String queryCondition) {
        return queryByCondition(queryCondition, null);
    }
    
    /**
     * 通过查询条件获得所有的VO对象列表，不带翻页查全部，带排序字符
     * 
     * @param queryCondition 查询条件
     * @param orderStr 排序字符
     * @return 查询到的VO列表
     */
    public List queryByCondition(String queryCondition, String orderStr) {
        return queryByCondition(-1, -1, queryCondition, orderStr);
    }
    
    /**
     * 通过查询条件获得所有的VO对象列表，带翻页
     * 
     * @param no 当前页数
     * @param size 每页记录数
     * @param queryCondition 查询条件
     * @return 查询到的VO列表
     */
    public List queryByCondition(int no, int size, String queryCondition) {
        return queryByCondition(no, size, queryCondition, null);
    }


    /**
     * 通过查询条件获取VO对象列表，支持翻页，针对移动端
     * 为了避免移动端每次查询时，都需要计算一次总记录数，多一次数据库操作
     *
     * @param offset
     * @param pageSize
     * @param queryCondition
     * @return
     */
    public List queryByCondition4Mobile(int offset, int pageSize, String queryCondition) {
        return queryByCondition4Mobile(offset,pageSize,queryCondition,null);
    }

    /**
     * 通过查询条件获得所有的VO对象列表，带翻页，带排序字符
     * 
     * @param no 当前页数
     * @param size 每页记录数
     * @param queryCondition 查询条件
     * @param orderStr 排序字符 
     * @return 查询到的VO列表
     */
    public List queryByCondition(int no, int size, String queryCondition, String orderStr) {
        String strsql = SQL_QUERY + DEFAULT_DESC_QUERY_WHERE_ENABLE + SQL_COMPANY_TYPE;
        if (queryCondition != null && queryCondition.length() > 0) {
            strsql += " AND " + queryCondition; //where后加上查询条件
        }
        if(orderStr == null ) {
            strsql += DEFAULT_DESC_ORDER_BY_ID;
        } else {
            strsql += " ORDER BY " + orderStr;
        }
        if(no <= 0 || size <= 0) {
            return query(strsql, new RowMapper() {
                public Object mapRow(ResultSet rs, int i) throws SQLException {
                    EmployeeVo vo = new EmployeeVo();
                    Helper.populate(vo, rs);
                    return vo;
                }
            });
        } else {
            return query(strsql, new RowMapper() {
                public Object mapRow(ResultSet rs, int i) throws SQLException {
                    EmployeeVo vo = new EmployeeVo();
                    Helper.populate(vo, rs);
                    return vo;
                }
            }, (no - 1) * size, size); 
        }
    }


    /**
     * 通过查询条件获得所有的VO对象列表，带翻页，带排序字符，针对移动端
     *
     * @param offset     当前记录数偏移量
     * @param pageSize   每页显示记录数
     * @param queryCondition 查询条件
     * @param orderStr       排序字段
     * @return
     */
    public List queryByCondition4Mobile(int offset, int pageSize, String queryCondition, String orderStr) {
        String strsql = SQL_QUERY + DEFAULT_DESC_QUERY_WHERE_ENABLE + SQL_COMPANY_TYPE;
        if (queryCondition != null && queryCondition.length() > 0) {
            strsql += " AND " + queryCondition; //where后加上查询条件
        }
        if(orderStr == null ) {
            strsql += DEFAULT_DESC_ORDER_BY_ID;
        } else {
            strsql += " ORDER BY " + orderStr;
        }
        if(offset <= 0 || pageSize <= 0) {
            return query(strsql, new RowMapper() {
                public Object mapRow(ResultSet rs, int i) throws SQLException {
                    EmployeeVo vo = new EmployeeVo();
                    Helper.populate(vo, rs);
                    return vo;
                }
            },0,pageSize);
        } else {
            return query(strsql, new RowMapper() {
                public Object mapRow(ResultSet rs, int i) throws SQLException {
                    EmployeeVo vo = new EmployeeVo();
                    Helper.populate(vo, rs);
                    return vo;
                }
            }, offset, pageSize);
        }
    }

}
	

