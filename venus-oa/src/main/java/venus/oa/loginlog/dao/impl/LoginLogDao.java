/*
 * 系统名称:单表模板 --> sample
 * 
 * 文件名称: venus.authority.login.loginlog.dao.impl --> LoginLogDao.java
 * 
 * 功能描述:
 * 
 * 版本历史: 2007-10-16 10:29:59.895 创建1.0.0版 (甘硕)
 *  
 */

package venus.oa.loginlog.dao.impl;

import org.springframework.jdbc.core.RowMapper;
import venus.oa.helper.AuHelper;
import venus.oa.loginlog.dao.ILoginLogDao;
import venus.oa.loginlog.util.ILoginLogConstants;
import venus.oa.loginlog.vo.LoginLogVo;
import venus.oa.login.vo.LoginSessionVo;
import venus.oa.util.SqlBuilder;
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

public class LoginLogDao extends BaseTemplateDao implements ILoginLogDao, ILoginLogConstants {

    /**
     * 插入单条记录，用Oid作主键，把null全替换为""
     * 
     * @param vo 用于添加的VO对象
     * @return 若添加成功，返回新生成的Oid
     */
    public OID insert(LoginLogVo vo) {
        OID oid = Helper.requestOID(TABLE_NAME); //获得oid
        long id = oid.longValue();
        vo.setId(String.valueOf(id));
        VoHelperTools.null2Nothing(vo); //把vo中null值替换为""
        Object[] obj = { vo.getId(), vo.getLogin_id(),vo.getName(),vo.getLogin_ip(),vo.getParty_id(),vo.getIe(),vo.getOs(),vo.getHost(),vo.getLogout_type(),vo.getLogin_time(),vo.getLogout_time(),vo.getLogin_state(),vo.getLogin_mac() };
        updateWithUniformArgType(SQL_INSERT, obj);
        return oid;
    }

    /**
     * 删除单条记录
     * 
     * @param id 用于删除的记录的id
     * @return 成功删除的记录数
     */
    public int delete(String id) {
        return update(SQL_DELETE_BY_ID, new Object[] { id });
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
        strsql = SQL_DELETE_MULTI_BY_IDS + strsql;
        return update(strsql);
    }
    
    /**
     * 删除全部记录
     * @return 成功删除的记录数
     */
    public int deleteAll() {
    	return update(SQL_DELETE_MULTI_BY_IDS);
    }

    /**
     * 根据Id进行查询
     * 
     * @param id 用于查找的id
     * @return 查询到的VO对象
     */
    public LoginLogVo find(String id) {
        return (LoginLogVo) queryForObject(SQL_FIND_BY_ID, new Object[] { id }, new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                LoginLogVo vo = new LoginLogVo();
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
    public int update(LoginLogVo vo) {
        VoHelperTools.null2Nothing(vo); //把vo中null值替换为""
        Object[] obj = { vo.getLogin_id(),vo.getName(),vo.getLogin_ip(),vo.getParty_id(),vo.getIe(),vo.getOs(),vo.getHost(),vo.getLogout_type(),vo.getLogout_time(),vo.getLock_time(),vo.getLogin_mac(), vo.getId() };
        return updateWithUniformArgType(SQL_UPDATE_BY_ID, obj);
    }

    /**
     * 查询总记录数，带查询条件
     * 
     * @param queryCondition 查询条件
     * @return 总记录数
     */
    public int getRecordCount(String queryCondition,LoginSessionVo authorizedContex) {
        String strsql = SQL_COUNT;
        if (queryCondition != null && queryCondition.length() > 0 && queryCondition.trim().length() > 0) {
            strsql += " AND " + queryCondition; //where后加上查询条件
        }
        if(null!=authorizedContex)
            strsql= AuHelper.filterRecordPrivInSQL(strsql, authorizedContex);
        return queryForInt(strsql);
    }
    
    /**
     * 不带条件查询，也即查询获得所有的VO对象列表，不带翻页，默认排序
     *
     * @return 查询到的VO列表
     */
    public List queryByCondition() {
        return queryByCondition(null,null);
    }
    
    /**
     * 通过查询条件获得所有的VO对象列表，不带翻页，默认排序
     * 
     * @param queryCondition 查询条件
     * @return 查询到的VO列表
     */
    public List queryByCondition(String queryCondition) {
        return queryByCondition(queryCondition, null);
    }
    
    /**
     * 通过查询条件获得所有的VO对象列表，不带翻页，带排序字符
     * 
     * @param queryCondition 查询条件
     * @param orderStr 排序字符
     * @return 查询到的VO列表
     */
    public List queryByCondition(String queryCondition, String orderStr) {
        return queryByCondition(-1, -1, queryCondition, orderStr,null);
    }
    
    /**
     * 通过查询条件获得所有的VO对象列表，带翻页，默认排序
     * 
     * @param no 当前页数
     * @param size 每页记录数
     * @param queryCondition 查询条件
     * @return 查询到的VO列表
     */
    public List queryByCondition(int no, int size, String queryCondition,LoginSessionVo AuthorizedContext) {
        return queryByCondition(no, size, queryCondition, null,AuthorizedContext);
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
    public List queryByCondition(int no, int size, String queryCondition, String orderStr,LoginSessionVo AuthorizedContext) {
        String strsql = SQL_QUERY_ALL + DEFAULT_QUERY_WHERE_ENABLE;
        if (queryCondition != null && queryCondition.length() > 0 && queryCondition.trim().length() > 0) {
            strsql += " AND " + queryCondition; //where后加上查询条件
        }
        if(orderStr == null ) {
            strsql += ORDER_BY_SYMBOL + DEFAULT_ORDER_CODE;
        } else {
            strsql += ORDER_BY_SYMBOL + orderStr;
        }
        if(null!=AuthorizedContext)
            strsql= AuHelper.filterRecordPrivInSQL(strsql,AuthorizedContext);
        if(no <= 0 || size <= 0) {
            return query(strsql, new RowMapper() {
                public Object mapRow(ResultSet rs, int i) throws SQLException {
                    LoginLogVo vo = new LoginLogVo();
                    Helper.populate(vo, rs);
                    return vo;
                }
            });
        } else {
            return query(strsql, new RowMapper() {
                public Object mapRow(ResultSet rs, int i) throws SQLException {
                    LoginLogVo vo = new LoginLogVo();
                    Helper.populate(vo, rs);
                    return vo;
                }
            }, (no - 1) * size, size); 
        }
    }
    
    /**
     * 通用的方法，执行更新，返回更新的记录条数
     *
     * @param strsql 要执行的sql语句
     * @return 更新记录条数
     */
    public int doUpdate(String strsql) {
        return update(strsql);
    }

	/* (non-Javadoc)
	 * @see venus.authority.login.loginlog.dao.ILoginLogDao#getRecordCount(venus.authority.util.SqlBuilder)
	 */
	public int getRecordCount(SqlBuilder sql) {
		return queryForInt(SQL_COUNT + " AND " + sql.bulidSql(),
				sql.getData().toArray());
	}

	public List queryByCondition(SqlBuilder sql) {
		String strSql = SQL_QUERY_ALL + " AND " + sql.bulidSql() + " ORDER BY LOGIN_TIME DESC ";
		return query(strSql,sql.getData().toArray(),new RowMapper() {
				public Object mapRow(ResultSet rs, int i) throws SQLException {
					 LoginLogVo vo = new LoginLogVo();
	                 Helper.populate(vo, rs);
	                 return vo;
				}
			},sql.getCountBegin(), sql.getCountEnd()
		);
	}
}
	

