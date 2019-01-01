package venus.portal.gbox.resource.classification.dao.impl;

import org.springframework.jdbc.core.RowMapper;
import venus.frames.base.dao.BaseTemplateDao;
import venus.frames.mainframe.util.Helper;
import venus.frames.mainframe.util.VoHelper;
import venus.portal.gbox.resource.classification.dao.IClassificationDao;
import venus.portal.gbox.resource.classification.util.IClassificationConstants;
import venus.portal.gbox.resource.classification.vo.ClassificationVo;
import venus.pub.lang.OID;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

//import venus.frames.mainframe.util.VoHelper;

public class ClassificationDao extends BaseTemplateDao implements IClassificationDao,IClassificationConstants {

    /**
     * 查找分类节点
     * @param vo
     * @return 分类节点vo
     */
    public ClassificationVo find(ClassificationVo vo) {
        if (vo == null)
            return null;
        String strsql = SQL_QUERY_ALL + DEFAULT_QUERY_WHERE_ENABLE;
        if (vo.getId() != null && !"".equals(vo.getId()))
            strsql += " AND ID ='" + vo.getId() + "'";        
        if (vo.getParentCode() != null && !"".equals(vo.getParentCode()))
            strsql += " AND SELF_CODE ='" + vo.getParentCode() + "'";
        return (ClassificationVo) queryForObject(strsql, new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                ClassificationVo vo = new ClassificationVo();
                Helper.populate(vo, rs);
                return vo;
            }
        });
    }
    
    /**
     * 插入单条记录，用Oid作主键，把null全替换为""
     * 
     * @param vo 用于添加的VO对象
     * @return 若添加成功，返回新生成的Oid
     */    
    public OID insert(ClassificationVo vo) {
        OID oid = Helper.requestOID(TABLE_NAME); //获得oid
        long id = oid.longValue();
        vo.setId(String.valueOf(id));
        VoHelper.null2Nothing(vo); //把vo中null值替换为""
        Object[] obj = { 
                vo.getId(), 
                vo.getName(),
                vo.getSelfCode(),
                vo.getParentCode(),
                vo.getDepth(),
                vo.getIsLeaf(),
                vo.getOrderCode(),
                vo.getPath(),
                vo.getCreateDate(),
                vo.getDescription() };
        update(SQL_INSERT, obj);
        return oid;
    }
    
    /**
     * 更新单条记录
     * 
     * @param vo 用于更新的VO对象
     * @return 成功更新的记录数
     */      
    public int update(ClassificationVo vo) {
        VoHelper.null2Nothing(vo); //把vo中null值替换为""
        Object[] obj = { vo.getName(),vo.getModifyDate(),vo.getDescription(), vo.getId() };
        return update(SQL_UPDATE_BY_ID, obj);        
    }
    
    /**
     * 更新节点叶子状态
     * 
     * @param vo 用于更新的VO对象
     * @return 成功更新的记录数
     */          
    public int updateLeaf(ClassificationVo vo) {
        VoHelper.null2Nothing(vo); //把vo中null值替换为""
        Object[] obj = { vo.getIsLeaf(), vo.getId() };
        return update(SQL_UPDATE_LEAF, obj);         
    }
    
    /**
     * 删除指定id的记录
     * @return 成功删除的记录数
     */      
    public int delete(String id) {
        if (id == null || "".equals(id))
            return 0;
        String strsql = SQL_DELETE_MULTI + " WHERE ID ='" + id + "'";
        return update(strsql);
    }    
    
    /**
     * 删除全部记录
     * @return 成功删除的记录数
     */      
    public int deleteMulti(String selfCode) {
        if (selfCode == null || "".equals(selfCode))
            return 0;
        String strsql = SQL_DELETE_MULTI + " WHERE SELF_CODE LIKE '%" + selfCode + "%'";
        return update(strsql);
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
     * 通过查询条件获得所有的VO对象列表
     * 
     * @param queryCondition 查询条件
     * @return 查询到的VO列表
     */     
    public List<ClassificationVo> queryByCondition(String queryCondition) {
        String strsql = SQL_QUERY_ALL + DEFAULT_QUERY_WHERE_ENABLE;
        if (queryCondition != null && queryCondition.length() > 0 && queryCondition.trim().length() > 0) {
            strsql +=  queryCondition; //where后加上查询条件
        }
        strsql += " ORDER BY IS_LEAF ASC,CREATE_DATE DESC";
         return query(strsql, new RowMapper() {
             public Object mapRow(ResultSet rs, int i) throws SQLException {
                 ClassificationVo vo = new ClassificationVo();
                 Helper.populate(vo, rs);
                 return vo;
             }
         });         
    }
    
    /**
     * 获得节点最大code
     * @param parentCode
     * @return int 获得节点最大code
     */
    public String getChildMaxCode(String parentCode) {
        String strsql = SQL_MAXCODE + DEFAULT_QUERY_WHERE_ENABLE;
        if (parentCode != null && parentCode.length() > 0 && parentCode.trim().length() > 0) {
            strsql +=  " AND PARENT_CODE = '" + parentCode + "'"; //where后加上查询条件
        }        
        
       String maxcode = (String)queryForObject(strsql, new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                String result = rs.getString("maxcode");
                return result;
            }
        });        
       return maxcode;
    }
    
}
