
/*
 * 系统名称:PlatForm
 * 
 * 文件名称: venus.authority.au.auuser.dao.impl --> AuUserDao.java
 * 
 * 功能描述:
 * 
 * 版本历史: 2006-06-09 15:32:04.528 创建1.0.0版 (甘硕)
 *  
 */

package venus.oa.authority.auuser.dao.impl;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import venus.oa.authority.auuser.dao.IAuUserDao;
import venus.oa.authority.auuser.util.IAuUserConstants;
import venus.oa.authority.auuser.vo.AuUserVo;
import venus.oa.util.Encode;
import venus.oa.util.GlobalConstants;
import venus.oa.util.StringHelperTools;
import venus.oa.util.VoHelperTools;
import venus.frames.base.dao.BaseTemplateDao;
import venus.frames.mainframe.util.Helper;
import venus.pub.lang.OID;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
//import venus.pub.util.Encode;

@Repository
public class AuUserDao extends BaseTemplateDao implements IAuUserDao, IAuUserConstants {

    /**
     * 插入单条记录，用Oid作主键，把null全替换为""
     * 
     * @param vo 用于添加的VO对象
     * @return 若添加成功，返回新生成的Oid
     */
    public OID insert(AuUserVo vo) {
        OID oid = Helper.requestOID(TABLE_NAME); //获得oid
        long id = oid.longValue();
        vo.setId(String.valueOf(id));
        vo.setPassword(Encode.encode(vo.getPassword()));//密码加密存储
        VoHelperTools.null2Nothing(vo); //把vo中null值替换为""
        if(null==vo.getRetire_date()){
        	Object[] obj = { vo.getId(), vo.getParty_id(),vo.getLogin_id(),vo.getPassword(),vo.getName(),vo.getIs_admin(),vo.getAgent_status(),vo.getEnable_status(), vo.getSystem_code(),vo.getCreate_date() };
	        update(SQL_INSERT_WITHOUTRETIREDATE, obj);
        }else{
	        Object[] obj = { vo.getId(), vo.getParty_id(),vo.getLogin_id(),vo.getPassword(),vo.getName(),vo.getIs_admin(),vo.getAgent_status(),vo.getEnable_status(), vo.getSystem_code(),vo.getCreate_date(),vo.getRetire_date() };
	        update(SQL_INSERT, obj);
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
     * 根据Id进行查询
     * 
     * @param id 用于查找的id
     * @return 查询到的VO对象
     */
    public AuUserVo find(String id) {
        return (AuUserVo) queryForObject(SQL_FIND_BY_ID, new Object[] { id }, new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                AuUserVo vo = new AuUserVo();
                Helper.populate(vo, rs);
                vo.setPassword(vo.getPassword());//Encode.decode(vo.getPassword()));//密码解密
                return vo;
            }
        });
    }
    /**
     * 根据partyId进行查询
     * 
     * @param partyid 用于查找的partyid
     * @return 查询到的VO对象
     */
    public AuUserVo getByPartyId(String partyid) {
        return (AuUserVo) queryForObject(SQL_QUERY_ALL+" where party_id=?", new Object[] { partyid }, new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                AuUserVo vo = new AuUserVo();
                Helper.populate(vo, rs);
                vo.setPassword(vo.getPassword());//Encode.decode(vo.getPassword()));//密码解密
                return vo;
            }
        });
    }
    /**
     * 获得未创建用户的人员列表
     * @param code
     * @return
     */
    public List queryNoAccountUser(String code) {
	String strsql = SQL_QUERY_NOACCOUNTUSER;
	strsql += " WHERE code LIKE '" + code + "%' AND partytype_id = '" + GlobalConstants.getPartyType_empl() +"' AND partyid NOT IN (" + SQL_FIND_PARTYID + ")";
        return query(strsql, new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                AuUserVo vo = new AuUserVo();
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
    public int update(AuUserVo vo) {
        vo.setPassword(vo.getPassword());//Encode.encode(vo.getPassword()));//密码加密存储
        VoHelperTools.null2Nothing(vo); //把vo中null值替换为""
        Object[] obj = { vo.getParty_id(),vo.getLogin_id(),vo.getPassword(),vo.getName(),vo.getIs_admin(),vo.getAgent_status(),vo.getEnable_status(),vo.getSystem_code(),vo.getModify_date(),vo.getFailed_times(),vo.getRetire_date(), vo.getId() };
        int type[] = {Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.TIMESTAMP,Types.INTEGER,Types.TIMESTAMP,Types.VARCHAR,};
        int rowNum = update(SQL_UPDATE_BY_ID, obj, type);
        //vo.setPassword(Encode.decode(vo.getPassword()));//密码解密
        return rowNum;
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
        String strsql = SQL_QUERY_ALL + DEFAULT_DESC_QUERY_WHERE_ENABLE;
        if(orderStr == null ) {
            strsql += DEFAULT_DESC_ORDER_BY_ID;
        } else {
            strsql += ORDER_BY_SYMBOL + orderStr;
        }
        if(no <= 0 || size <= 0) {
            return query(strsql, new RowMapper() {
                public Object mapRow(ResultSet rs, int i) throws SQLException {
                    AuUserVo vo = new AuUserVo();
                    Helper.populate(vo, rs);
                    vo.setPassword(vo.getPassword());//Encode.decode(vo.getPassword()));//密码解密
                    return vo;
                }
            });
        } else {
            return query(strsql, new RowMapper() {
                public Object mapRow(ResultSet rs, int i) throws SQLException {
                    AuUserVo vo = new AuUserVo();
                    Helper.populate(vo, rs);
                    vo.setPassword(vo.getPassword());//Encode.decode(vo.getPassword()));//密码解密
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
        return queryForInt(SQL_COUNT);
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
        String strsql = SQL_QUERY_ALL + DEFAULT_DESC_QUERY_WHERE_ENABLE;
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
                    AuUserVo vo = new AuUserVo();
                    Helper.populate(vo, rs);
                    vo.setPassword(vo.getPassword());//Encode.decode(vo.getPassword()));//密码解密
                    return vo;
                }
            });
        } else {
            return query(strsql, new RowMapper() {
                public Object mapRow(ResultSet rs, int i) throws SQLException {
                    AuUserVo vo = new AuUserVo();
                    Helper.populate(vo, rs);
                    vo.setPassword(vo.getPassword());//Encode.decode(vo.getPassword()));//密码解密
                    return vo;
                }
            }, (no - 1) * size, size); 
        }
    }
    /**
     * 可以控制数据权限的查询
     * @param queryCondition
     * @return
     */
    public int getRecordCount4Limit(String queryCondition) {
        String strsql = SQL_COUNT_LIMIT + DEFAULT_DESC_QUERY_WHERE_ENABLE;
        if (queryCondition != null && queryCondition.length() > 0) {
            strsql += " AND " + queryCondition; //where后加上查询条件
        }
        return queryForInt(strsql);
    }
    /**
     * 可以控制数据权限的查询
     * @param no
     * @param size
     * @param queryCondition
     * @return
     */
    public List queryByCondition4Limit(int no, int size, String queryCondition) {
        String strsql = SQL_QUERY_ALL_LIMIT + DEFAULT_DESC_QUERY_WHERE_ENABLE;
        if (queryCondition != null && queryCondition.length() > 0) {
            strsql += " AND " + queryCondition; //where后加上查询条件
        }
        strsql += " ORDER BY A.ID DESC ";
        if(no <= 0 || size <= 0) {
            return query(strsql, new RowMapper() {
                public Object mapRow(ResultSet rs, int i) throws SQLException {
                    AuUserVo vo = new AuUserVo();
                    Helper.populate(vo, rs);
                    vo.setPassword(vo.getPassword());//Encode.decode(vo.getPassword()));//密码解密
                    return vo;
                }
            });
        } else {
            return query(strsql, new RowMapper() {
                public Object mapRow(ResultSet rs, int i) throws SQLException {
                    AuUserVo vo = new AuUserVo();
                    Helper.populate(vo, rs);
                    vo.setPassword(vo.getPassword());//Encode.decode(vo.getPassword()));//密码解密
                    return vo;
                }
            }, (no - 1) * size, size); 
        }
    }
    
    /**
     * 查询总记录数，带查询条件
     * 关联了组织机构，可以按公司或部门查
     * 
     * @param queryCondition 查询条件
     * @return 总记录数
     */
    public int getRecordCount4Org(String queryCondition) {
        String strsql = SQL_COUNT_ORG;
        if (queryCondition != null && queryCondition.length() > 0) {
            strsql += " AND " + queryCondition; //where后加上查询条件
        }
        return queryForInt(strsql);
    }
    /**
     * 通过查询条件获得所有的VO对象列表，带翻页，带排序字符
     * 关联了组织机构，可以按公司或部门查
     * @param no
     * @param size
     * @param queryCondition
     * @param orderStr
     * @return
     */
    public List queryByCondition4Org(int no, int size, String queryCondition, String orderStr) {
        String strsql = SQL_QUERY_ALL_ORG;
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
                    AuUserVo vo = new AuUserVo();
                    Helper.populate(vo, rs);
                    vo.setPassword(vo.getPassword());//Encode.decode(vo.getPassword()));//密码解密
                    return vo;
                }
            });
        } else {
            return query(strsql, new RowMapper() {
                public Object mapRow(ResultSet rs, int i) throws SQLException {
                    AuUserVo vo = new AuUserVo();
                    Helper.populate(vo, rs);
                    vo.setPassword(vo.getPassword());//Encode.decode(vo.getPassword()));//密码解密
                    return vo;
                }
            }, (no - 1) * size, size); 
        }
    }
    /* (non-Javadoc)
     * @see venus.authority.au.auuser.dao.IAuUserDao#findByLoginId(java.lang.String)
     */
    public AuUserVo findByLoginId(String loginId) {
        return (AuUserVo) queryForObject(SQL_FIND_BY_LOGIN_ID, new Object[] { loginId }, new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                AuUserVo vo = new AuUserVo();
                Helper.populate(vo, rs);                
                return vo;
            }
        });
    }
}
	

