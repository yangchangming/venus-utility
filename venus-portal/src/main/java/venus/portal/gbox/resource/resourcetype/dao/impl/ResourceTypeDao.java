package venus.portal.gbox.resource.resourcetype.dao.impl;

import org.springframework.jdbc.core.RowMapper;
import venus.frames.base.dao.BaseTemplateDao;
import venus.frames.mainframe.util.Helper;
import venus.oa.util.VoHelperTools;
import venus.portal.gbox.resource.resourcetype.dao.IResourceTypeDao;
import venus.portal.gbox.resource.resourcetype.util.IResourceTypeConstants;
import venus.portal.gbox.resource.resourcetype.vo.ResourceTypeVo;
import venus.pub.lang.OID;
import venus.pub.util.StringHelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ResourceTypeDao extends BaseTemplateDao implements IResourceTypeDao, IResourceTypeConstants {

    /**
     * 插入单条记录，用Oid作主键，把null全替换为""
     * 
     * @param vo 用于添加的VO对象
     * @return 若添加成功，返回新生成的Oid
     */
    public OID insert(ResourceTypeVo vo) {
        OID oid = Helper.requestOID(TABLE_NAME); //获得oid
        long id = oid.longValue();
        vo.setId(String.valueOf(id));
        VoHelperTools.null2Nothing(vo); //把vo中null值替换为""
        Object[] obj = { vo.getId(), vo.getName(),vo.getRelevanceFormat(),vo.getUploadPath(),vo.getSingleMaximum(),vo.getTotalMaximum(),vo.getCreateDate(),vo.getEnableStatus(),vo.getDescription() };
        update(SQL_INSERT, obj);
        return oid;
    }

    /**
     * 删除多条记录
     * 
     * @param id 用于删除的记录的id
     * @return 成功删除的记录数
     */
    public int delete(String id[]) {
        String strsql = SQL_DELETE_MULTI_BY_IDS + " AND ID IN (";
        if (id == null || id.length == 0)
            return 0;
        strsql += StringHelper.parseToSQLStringComma(id) + ")"; //把id数组转换为id1,id2,id3
        return update(strsql);
    }

    /**
     * 根据Id进行查询
     * 
     * @param id 用于查找的id
     * @return 查询到的VO对象
     */
    public ResourceTypeVo find(String id) {
        return (ResourceTypeVo) queryForObject(SQL_FIND_BY_ID, new Object[] { id }, new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                ResourceTypeVo vo = new ResourceTypeVo();
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
    public int update(ResourceTypeVo vo) {
        VoHelperTools.null2Nothing(vo); //把vo中null值替换为""
        Object[] obj = { vo.getName(),vo.getRelevanceFormat(),vo.getUploadPath(),vo.getSingleMaximum(),vo.getTotalMaximum(),vo.getModifyDate(),vo.getEnableStatus(),vo.getDescription(), vo.getId() };
        return update(SQL_UPDATE_BY_ID, obj);
    }

    /**
     * 查询总记录数，带查询条件
     * 
     * @param queryCondition 查询条件
     * @return 总记录数
     */
    public int getRecordCount(String queryCondition) {
        String strsql = SQL_COUNT + DEFAULT_QUERY_WHERE_ENABLE;
        if (queryCondition != null && queryCondition.length() > 0 && queryCondition.trim().length() > 0) {
            strsql += queryCondition; //where后加上查询条件
        }
        return queryForInt(strsql);
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
    public List<ResourceTypeVo> queryByCondition(int no, int size, String queryCondition, String orderStr) {
        String strsql = SQL_QUERY_ALL + DEFAULT_QUERY_WHERE_ENABLE;
        if (queryCondition != null && queryCondition.length() > 0 && queryCondition.trim().length() > 0) {
            strsql +=  queryCondition; //where后加上查询条件
        }
        
        if (orderStr != null) {
            strsql += " ORDER BY " + orderStr;
        } else {
            strsql += " ORDER BY ID DESC";
        }

         return query(strsql, new RowMapper() {
             public Object mapRow(ResultSet rs, int i) throws SQLException {
                 ResourceTypeVo vo = new ResourceTypeVo();
                 Helper.populate(vo, rs);
                 return vo;
             }
         }, (no - 1) * size, size); 
    }
    
    /**
     * 获得所有的VO对象列表
     */
    public List<ResourceTypeVo> queryAll() {
        String strsql = SQL_QUERY_ALL;
        return query(strsql, new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                ResourceTypeVo vo = new ResourceTypeVo();
                Helper.populate(vo, rs);
                return vo;
            }
        }); 
    }
    
}
	