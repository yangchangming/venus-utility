package venus.oa.authority.aufunctree.dao.impl;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import venus.dao.provider.BaseTemplateDao;
import venus.oa.authority.aufunctree.dao.IAuFunctreeDao;
import venus.oa.authority.aufunctree.util.AuFunctreeConstants;
import venus.oa.authority.aufunctree.util.IAuFunctreeConstants;
import venus.oa.authority.aufunctree.vo.AuFunctreeVo;
import venus.oa.util.StringHelperTools;
import venus.oa.util.VoHelperTools;
import venus.frames.mainframe.util.Helper;
import venus.pub.lang.LangException;
import venus.pub.lang.OID;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class AuFunctreeDao extends BaseTemplateDao implements IAuFunctreeDao, IAuFunctreeConstants {

    /**
     * 插入单条记录，用Oid作主键，把null全替换为""
     * 
     * @param vo 用于添加的VO对象
     * @return 若添加成功，返回新生成的Oid
     */
    public OID insert(AuFunctreeVo vo) {
        VoHelperTools.null2Nothing(vo); //把vo中null值替换为""
        Object[] obj = { vo.getId(), vo.getKeyword(), vo.getType(),vo.getCode(),vo.getParent_code(),vo.getTotal_code(),vo.getName(),vo.getHot_key(),vo.getHelp(),vo.getUrl(),vo.getIs_leaf(),vo.getType_is_leaf(),vo.getOrder_code(),vo.getSystem_id(),vo.getCreate_date(),String.valueOf(vo.getTree_level()),vo.getIs_ssl(),vo.getIs_public(),vo.getTree_id() };
        update(AuFunctreeConstants.getDifferentInstance().SQL_INSERT, obj);
        OID oid = null;
        try {
            oid = new OID(vo.getId());
        } catch (LangException e) {
            e.printStackTrace();
        }
        return oid;
    }

    /**
     * 删除单条记录
     * 
     * @param id 用于删除的记录的id
     * @return 成功删除的记录数
     */
    public int delete(String id) {
        return update(AuFunctreeConstants.getDifferentInstance().SQL_DELETE_BY_ID, new Object[] { id });
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
        strsql = AuFunctreeConstants.getDifferentInstance().SQL_DELETE_MULTI_BY_IDS + strsql;
        return update(strsql);
    }

    /**
     * 根据Id进行查询
     * 
     * @param id 用于查找的id
     * @return 查询到的VO对象
     */
    public AuFunctreeVo find(String id) {
        return (AuFunctreeVo) queryForObject(AuFunctreeConstants.getDifferentInstance().SQL_FIND_BY_ID, new Object[] { id }, new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                AuFunctreeVo vo = new AuFunctreeVo();
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
    public int update(AuFunctreeVo vo) {
        VoHelperTools.null2Nothing(vo); //把vo中null值替换为""
        Object[] obj = { vo.getKeyword(), vo.getType(),vo.getCode(),vo.getParent_code(),vo.getTotal_code(),vo.getName(),vo.getHot_key(),vo.getHelp(),vo.getUrl(),vo.getIs_leaf(),vo.getType_is_leaf(),vo.getOrder_code(),vo.getSystem_id(),vo.getModify_date(),vo.getIs_ssl(), vo.getIs_public(), vo.getId() };
        return update(AuFunctreeConstants.getDifferentInstance().SQL_UPDATE_BY_ID, obj);
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
        String strsql = AuFunctreeConstants.getDifferentInstance().SQL_QUERY_ALL + AuFunctreeConstants.getDifferentInstance().DEFAULT_DESC_QUERY_WHERE_ENABLE;
        if(orderStr == null ) {
            strsql += AuFunctreeConstants.getDifferentInstance().DEFAULT_DESC_ORDER_BY_ID;
        } else {
            strsql += " ORDER BY " + orderStr;
        }
        if(no <= 0 || size <= 0) {
            return query(strsql, new RowMapper() {
                public Object mapRow(ResultSet rs, int i) throws SQLException {
                    AuFunctreeVo vo = new AuFunctreeVo();
                    Helper.populate(vo, rs);
                    return vo;
                }
            });
        } else {
            return query(strsql, new RowMapper() {
                public Object mapRow(ResultSet rs, int i) throws SQLException {
                    AuFunctreeVo vo = new AuFunctreeVo();
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
        String strsql = AuFunctreeConstants.getDifferentInstance().SQL_COUNT + AuFunctreeConstants.getDifferentInstance().DEFAULT_DESC_QUERY_WHERE_ENABLE;
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
        String strsql = AuFunctreeConstants.getDifferentInstance().SQL_QUERY_ALL + AuFunctreeConstants.getDifferentInstance().DEFAULT_DESC_QUERY_WHERE_ENABLE;
        if (queryCondition != null && queryCondition.length() > 0) {
            strsql += " AND " + queryCondition; //where后加上查询条件
        }
        if(orderStr == null ) {
            strsql += AuFunctreeConstants.getDifferentInstance().DEFAULT_DESC_ORDER_BY_ID;
        } else {
            strsql += " ORDER BY " + orderStr;
        }
        if(no <= 0 || size <= 0) {
            return query(strsql, new RowMapper() {
                public Object mapRow(ResultSet rs, int i) throws SQLException {
                    AuFunctreeVo vo = new AuFunctreeVo();
                    Helper.populate(vo, rs);
                    return vo;
                }
            });
        } else {
            return query(strsql, new RowMapper() {
                public Object mapRow(ResultSet rs, int i) throws SQLException {
                    AuFunctreeVo vo = new AuFunctreeVo();
                    Helper.populate(vo, rs);
                    return vo;
                }
            }, (no - 1) * size, size); 
        }
    }
}
	

