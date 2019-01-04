/*
 * 系统名称:单表模板 --> test
 * 
 * 文件名称: venus.authority.sample.company.dao.impl --> CompanyDao.java
 * 
 * 功能描述:
 * 
 * 版本历史: 2007-01-31 14:20:11.307 创建1.0.0版 (甘硕)
 *  
 */

package venus.oa.organization.company.dao.impl;

import org.springframework.jdbc.core.RowMapper;
import venus.oa.organization.company.dao.ICompanyDao;
import venus.oa.organization.company.util.ICompanyConstants;
import venus.oa.organization.company.vo.CompanyVo;
import venus.oa.util.StringHelperTools;
import venus.oa.util.VoHelperTools;
import venus.frames.base.dao.BaseTemplateDao;
import venus.frames.mainframe.util.Helper;
import venus.pub.lang.OID;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 功能、用途、现存BUG:
 * 
 * @author 甘硕
 * @version 1.0.0
 * @see 需要参见的其它类
 * @since 1.0.0
 */

public class CompanyDao extends BaseTemplateDao implements ICompanyDao, ICompanyConstants {

    /**
     * 插入单条记录，用Oid作主键，把null全替换为""
     * 
     * @param vo 用于添加的VO对象
     * @return 若添加成功，返回新生成的Oid
     */
    public OID insert(CompanyVo vo) {
        VoHelperTools.null2Nothing(vo); //把vo中null值替换为""
        Object[] obj = { vo.getId(), vo.getCompany_no(),vo.getCompany_name(),vo.getShort_name(),vo.getCompany_flag(),vo.getCompany_type(),vo.getCompany_level(),vo.getArea(),vo.getLinkman(),vo.getTel(),vo.getFax(),vo.getPostalcode(),vo.getAddress(),vo.getEmail(),vo.getWeb(),vo.getRemark(),vo.getEnable_status(),vo.getEnable_date(),vo.getCreate_date(),vo.getModify_date(),vo.getColumn1(),vo.getColumn2(),vo.getColumn3() };
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
    public CompanyVo find(String id) {
        return (CompanyVo) queryForObject("select * from AU_COMPANY where ID=?", new Object[] { id }, new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                CompanyVo vo = new CompanyVo();
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
    public int update(CompanyVo vo) {
        VoHelperTools.null2Nothing(vo); //把vo中null值替换为""
        Object[] obj = { vo.getCompany_no(),vo.getCompany_name(),vo.getShort_name(),vo.getCompany_flag(),vo.getCompany_type(),vo.getCompany_level(),vo.getArea(),vo.getLinkman(),vo.getTel(),vo.getFax(),vo.getPostalcode(),vo.getAddress(),vo.getEmail(),vo.getWeb(),vo.getRemark(),vo.getEnable_status(),vo.getEnable_date(),vo.getCreate_date(),vo.getModify_date(),vo.getColumn1(),vo.getColumn2(),vo.getColumn3(), vo.getId() };
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
        String strsql = SQL_QUERY + DEFAULT_DESC_QUERY_WHERE_ENABLE;
        if(orderStr == null ) {
            strsql += DEFAULT_DESC_ORDER_BY_ID;
        } else {
            strsql += ORDER_BY_SYMBOL + orderStr;
        }
        if(no <= 0 || size <= 0) {
            return query(strsql, new RowMapper() {
                public Object mapRow(ResultSet rs, int i) throws SQLException {
                    CompanyVo vo = new CompanyVo();
                    Helper.populate(vo, rs);
                    return vo;
                }
            });
        } else {
            return query(strsql, new RowMapper() {
                public Object mapRow(ResultSet rs, int i) throws SQLException {
                    CompanyVo vo = new CompanyVo();
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
        String strsql = SQL_COUNT + DEFAULT_DESC_QUERY_WHERE_ENABLE;
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
     * 通过查询条件获得所有的VO对象列表，带翻页，带排序字符
     * 
     * @param no 当前页数
     * @param size 每页记录数
     * @param queryCondition 查询条件
     * @param orderStr 排序字符 
     * @return 查询到的VO列表
     */
    public List queryByCondition(int no, int size, String queryCondition, String orderStr) {
        String strsql = SQL_QUERY + DEFAULT_DESC_QUERY_WHERE_ENABLE;
        if (queryCondition != null && queryCondition.length() > 0) {
            strsql += " AND " + queryCondition; //where后加上查询条件
        }
        if(orderStr == null ) {
            strsql += DEFAULT_DESC_ORDER_BY_ID;
        } else {
            strsql += ORDER_BY_SYMBOL + orderStr;
        }
        if(no <= 0 || size <= 0) {
            return query(strsql, new RowMapper() {
                public Object mapRow(ResultSet rs, int i) throws SQLException {
                    CompanyVo vo = new CompanyVo();
                    Helper.populate(vo, rs);
                    return vo;
                }
            });
        } else {
            return query(strsql, new RowMapper() {
                public Object mapRow(ResultSet rs, int i) throws SQLException {
                    CompanyVo vo = new CompanyVo();
                    Helper.populate(vo, rs);
                    return vo;
                }
            }, (no - 1) * size, size); 
        }
    }
}
	
