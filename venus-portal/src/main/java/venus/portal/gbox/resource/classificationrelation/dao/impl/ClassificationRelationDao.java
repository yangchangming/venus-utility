package venus.portal.gbox.resource.classificationrelation.dao.impl;

import org.springframework.jdbc.core.RowMapper;
import venus.frames.base.dao.BaseTemplateDao;
import venus.frames.mainframe.util.Helper;
import venus.oa.util.VoHelperTools;
import venus.portal.gbox.resource.classification.vo.ClassificationVo;
import venus.portal.gbox.resource.classificationrelation.dao.IClassificationRelationDao;
import venus.portal.gbox.resource.classificationrelation.util.IClassificationRelationConstants;
import venus.portal.gbox.resource.classificationrelation.vo.ClassificationRelationVo;
import venus.portal.gbox.resource.resourceinfo.vo.ResourceVo;
import venus.pub.lang.OID;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public class ClassificationRelationDao extends BaseTemplateDao implements IClassificationRelationConstants, IClassificationRelationDao {

    /**
     * 插入单条记录，用Oid作主键，把null全替换为""
     * 
     * @param vo 用于添加的VO对象
     * @return 若添加成功，返回新生成的Oid
     */
    public OID insert(ClassificationRelationVo vo) {
        OID oid = Helper.requestOID(TABLE_NAME); //获得oid
        long id = oid.longValue();
        vo.setId(String.valueOf(id));
        VoHelperTools.null2Nothing(vo); //把vo中null值替换为""
        Object[] obj = { 
                vo.getId(), 
                vo.getClassificationId(),
                vo.getResourceId(),
                vo.getCreateDate()};
        update(SQL_INSERT, obj);
        return oid;
    }
    
    /**
     * 根据条件删除多条记录
     * 
     * @param vo 用于删除的记录的vo
     * @return 成功删除的记录数
     */
    public int delete(ClassificationRelationVo vo) {
        String strsql = SQL_DELETE + DEFAULT_QUERY_WHERE_ENABLE;
        if (vo != null && !"".equals(vo)) {
            if (vo.getResourceId() != null && !"".equals(vo.getResourceId()))
                strsql += " AND RESOURCE_ID = '" + vo.getResourceId() + "'"; //where后加上查询条件
            if (vo.getClassificationId() != null && !"".equals(vo.getClassificationId()))
                strsql += " AND CLASSIFICATION_ID = '" + vo.getClassificationId() + "'"; //where后加上查询条件            
        }
        return update(strsql);
    }
    
    /**
     * 删除全部记录
     * 
     * @return 成功删除的记录数
     */
    public int deleteAll() {
        return update(SQL_DELETE);
    }
    
    /**
     * 查询总记录数，带查询条件
     * 
     * @param queryCondition 查询条件
     * @return 总记录数
     */
    public int getClassifyRecordCount(String queryCondition) {
        String strsql = SQL_CLASSIFY_COUNT;
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
    public List<ClassificationVo> queryClassifyByCondition(int no, int size, String queryCondition, String orderStr) {
        String strsql = SQL_CLASSIFY_QUERY;
        if (queryCondition != null && queryCondition.length() > 0 && queryCondition.trim().length() > 0) {
            strsql +=  queryCondition; //where后加上查询条件
        }
        
        if (orderStr != null) {
            strsql += " ORDER BY " + orderStr;
        } else {
            strsql += " ORDER BY ID DESC";
        }
        if (no == -1 && size == -1) {
            return query(strsql, new RowMapper() {
                public Object mapRow(ResultSet rs, int i) throws SQLException {
                    ClassificationVo vo = new ClassificationVo();
                    Helper.populate(vo, rs);
                    return vo;
                }
            });            
        } else {
             return query(strsql, new RowMapper() {
                 public Object mapRow(ResultSet rs, int i) throws SQLException {
                     ClassificationVo vo = new ClassificationVo();
                     Helper.populate(vo, rs);
                     return vo;
                 }
             }, (no - 1) * size, size);
        }
    }
        
    /**
     * 查询总记录数，带查询条件
     * 
     * @param queryCondition 查询条件
     * @return 总记录数
     */
    public int getResourceRecordCount(String queryCondition) {
        String strsql = SQL_RESOURCE_COUNT;
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
    public List<ResourceVo> queryResourceByCondition(int no, int size, String queryCondition, String orderStr) {
        String strsql = SQL_RESOURCE_QUERY;
        if (queryCondition != null && queryCondition.length() > 0 && queryCondition.trim().length() > 0) {
            strsql +=  queryCondition; //where后加上查询条件
        }
        
        if (orderStr != null) {
            strsql += " ORDER BY " + orderStr;
        } else {
            strsql += " ORDER BY ID DESC";
        }
        if (no == -1 && size == -1) {
            return query(strsql, new RowMapper() {
                public Object mapRow(ResultSet rs, int i) throws SQLException {
                    ResourceVo vo = new ResourceVo();
                    Helper.populate(vo, rs);
                    return vo;
                }
            });             
        } else {
            return query(strsql, new RowMapper() {
                public Object mapRow(ResultSet rs, int i) throws SQLException {
                    ResourceVo vo = new ResourceVo();
                    Helper.populate(vo, rs);
                    return vo;
                }
            }, (no - 1) * size, size); 
        }
    } 
    
    /**
     * 查询总记录数，带查询条件
     * 
     * @param queryCondition 查询条件 map参数为：{classId,name,type}
     * @return 总记录数
     */
    public int getRelationRecordCount(Map queryCondition) {
        String strsql = SQL_RELATIONLIST_COUNT + DEFAULT_QUERY_WHERE_ENABLE;
        if (queryCondition != null && !queryCondition.isEmpty()) {
            String classId = (String)queryCondition.get(QUERYCONDITION_CLASSID);
            strsql += " AND NOT EXISTS (SELECT RESOURCE_ID FROM GBOX_CLASSIFICATION_RELATION  WHERE RESOURCE_ID = A.ID AND CLASSIFICATION_ID = '" + classId + "')";
            String name = (String)queryCondition.get(QUERYCONDITION_NAME);
            if (name != null && !"".equals(name))
                strsql += " AND A.NAME LIKE '%" + name + "%'";
            String type = (String)queryCondition.get(QUERYCONDITION_TYPE);
            if (type != null && !"".equals(type))
                strsql += " AND A.TYPE = '" + type + "'";     
        }
        return queryForInt(strsql);
    }      
    
    /**
     * 通过查询条件获得所有的VO对象列表，带翻页，带排序字符
     * @param no 当前页数
     * @param size 每页记录数
     * @param queryCondition 查询条件 map参数为：{classId,name,type}
     * @param orderStr 排序字符 
     * @return 查询到的VO列表
     */
    public List<ResourceVo> queryRelationByCondition(int no, int size, Map queryCondition, String orderStr) {
        String strsql  = SQL_RELATIONLIST_QUERY + DEFAULT_QUERY_WHERE_ENABLE;
        if (queryCondition != null && !queryCondition.isEmpty()) {
            String classId = (String)queryCondition.get(QUERYCONDITION_CLASSID);
            strsql += " AND NOT EXISTS (SELECT RESOURCE_ID FROM GBOX_CLASSIFICATION_RELATION  WHERE RESOURCE_ID = A.ID AND CLASSIFICATION_ID = '" + classId + "')";
            String name = (String)queryCondition.get(QUERYCONDITION_NAME);
            if (name != null && !"".equals(name))
                strsql += " AND A.NAME LIKE '%" + name + "%'";
            String type = (String)queryCondition.get(QUERYCONDITION_TYPE);
            if (type != null && !"".equals(type))
                strsql += " AND A.TYPE = '" + type + "'";     
        }
        if (orderStr != null) {
            strsql += " ORDER BY " + orderStr;
        } else {
            strsql += " ORDER BY ID DESC";
        }        
        return query(strsql, new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                ResourceVo vo = new ResourceVo();
                Helper.populate(vo, rs);
                return vo;
            }
        }, (no - 1) * size, size);        
    }

}
