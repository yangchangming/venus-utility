package venus.portal.gbox.resource.resourceimport.dao.impl;

import org.springframework.jdbc.core.RowMapper;
import venus.frames.base.dao.BaseTemplateDao;
import venus.frames.mainframe.util.Helper;
import venus.frames.mainframe.util.VoHelper;
import venus.portal.gbox.resource.resourceimport.dao.IResourceImportDao;
import venus.portal.gbox.resource.resourceimport.util.IResourceImportConstants;
import venus.portal.gbox.resource.resourceimport.vo.ResourceImportVo;
import venus.pub.lang.OID;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class ResourceImportDao extends BaseTemplateDao implements IResourceImportConstants, IResourceImportDao {
    
    /**
     * 插入单条记录，用Oid作主键，把null全替换为""
     * 
     * @param vo 用于添加的VO对象
     * @return 若添加成功，返回新生成的Oid
     */
    public OID insert(ResourceImportVo vo){
        OID oid = Helper.requestOID(TABLE_NAME); //获得oid
        long id = oid.longValue();
        vo.setId(String.valueOf(id));
        VoHelper.null2Nothing(vo); //把vo中null值替换为""
        Object[] obj = { 
                vo.getId(), 
                vo.getClassificationId(),
                vo.getResourcePath(),
                vo.getIsScan(),
                vo.getDescription() };
            update(SQL_INSERT, obj);
            return oid;
    }

    /**
     * 删除多条记录
     * 
     * @param id 用于删除的记录的id
     * @return 成功删除的记录数
     */
    public int delete(String id) {
        if (id == null || "".equals(id))
            return 0;
        String strsql = SQL_DELETE + " WHERE ID = '" + id + "'";
        return update(strsql);
    }

    /**
     * 更新单条记录
     * 
     * @param vo 用于更新的VO对象
     * @return 成功更新的记录数
     */
    public int update(ResourceImportVo vo) {
        VoHelper.null2Nothing(vo); //把vo中null值替换为""
        Object[] obj = { vo.getResourcePath(),vo.getDescription(), vo.getId() };
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
     * @param queryCondition 查询条件
     * @return 查询到的VO列表
     */
    public List<ResourceImportVo> queryByCondition(String queryCondition) {
        String strsql = SQL_QUERY_ALL + DEFAULT_QUERY_WHERE_ENABLE;
        if (queryCondition != null && queryCondition.length() > 0 && queryCondition.trim().length() > 0) {
            strsql +=  queryCondition; //where后加上查询条件
        }
         return query(strsql, new RowMapper() {
             public Object mapRow(ResultSet rs, int i) throws SQLException {
                 ResourceImportVo vo = new ResourceImportVo();
                 Helper.populate(vo, rs);
                 return vo;
             }
         });          
    }
}
