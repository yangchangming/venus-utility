package venus.portal.gbox.resource.resourceinfo.dao.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import venus.frames.base.dao.BaseTemplateDao;
import venus.frames.mainframe.util.Helper;
import venus.oa.util.VoHelperTools;
import venus.portal.gbox.resource.resourceinfo.dao.IResourceDao;
import venus.portal.gbox.resource.resourceinfo.util.IResourceConstants;
import venus.portal.gbox.resource.resourceinfo.vo.ResourceVo;
import venus.portal.gbox.util.DateTools;
import venus.pub.lang.OID;
import venus.pub.util.StringHelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class ResourceDao extends BaseTemplateDao implements IResourceConstants, IResourceDao {
  
    /**
     * 插入单条记录，用Oid作主键，把null全替换为""
     * 
     * @param vo 用于添加的VO对象
     * @return 若添加成功，返回新生成的Oid
     */
    public OID insert(ResourceVo vo) {
        OID oid = Helper.requestOID(TABLE_NAME); //获得oid
        long id = oid.longValue();
        vo.setId(String.valueOf(id));
        VoHelperTools.null2Nothing(vo); //把vo中null值替换为""
        Object[] obj = { vo.getId(), vo.getName(), vo.getCode(), vo.getTag(), vo.getType(), vo.getIsProtected(), vo.getIsExternal(),vo.getFileName(),vo.getFileSize(),vo.getFileFormat(),vo.getCreatorName(), vo.getCreateDate(),vo.getDescription() };
        update(SQL_INSERT, obj);
        return oid;
    }
    
    /**
     * 删除一条记录
     * 
     * @param vo 用于删除的记录的vo
     * @return 成功删除的记录数
     */    
    public int delete(ResourceVo vo) {
        String strsql = SQL_DELETE + DEFAULT_QUERY_WHERE_ENABLE;
        if (vo == null || "".equals(vo))
            return 0;
        if (vo.getId() != null && !"".equals(vo.getId())) {
            strsql += " AND ID = '" + vo.getId() + "'";
        }
        if (vo.getType() != null && !"".equals(vo.getType())) {
            strsql += " AND TYPE = '" + vo.getType() + "'";
        }
        return update(strsql);
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
        strsql += StringHelper.parseToSQLString(id) + ")"; //把id数组转换为id1,id2,id3
        strsql = SQL_DELETE + strsql;
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
     * 根据Id进行查询
     * 
     * @param id 用于查找的id
     * @return 查询到的VO对象
     */
    public ResourceVo find(String id) {
        return (ResourceVo) queryForObject(SQL_FIND_BY_ID, new Object[] { id }, new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                ResourceVo vo = new ResourceVo();
                Helper.populate(vo, rs);
                return vo;
            }
        });
    }
    
    /**
     * 根据Id进行查询，该方法可获得完整文件路径
     * 
     * @param id 用于查找的id
     * @return 查询到的VO对象
     */
    public ResourceVo findAll(String id) {
        return (ResourceVo) queryForObject(SQL_FIND_BY_ID, new Object[] { id }, new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                ResourceVo vo = new ResourceVo();
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
    public int update(ResourceVo vo) {
        VoHelperTools.null2Nothing(vo); //把vo中null值替换为""
        Object[] obj = { vo.getName(), vo.getCode(), vo.getModifierName(),vo.getModifyDate(), vo.getEnableStatus(), vo.getDescription(), vo.getId() };
        return update(SQL_UPDATE_BY_ID, obj);
    }
    
    /**
     * 更新tag
     * 
     * @param vo 用于更新的VO对象
     * @return 成功更新的记录数
     */    
    public int updateTag(ResourceVo vo) {
        VoHelperTools.null2Nothing(vo); //把vo中null值替换为""
        Object[] obj = { vo.getTag(),vo.getModifierName(),vo.getModifyDate(), vo.getId() };
        return update(SQL_UPDATETAG_BY_ID, obj);        
    }

    /**
     * 查询总记录数，带查询条件
     * 
     * @param vo 查询条件
     * @return 总记录数
     */
    public int getRecordCount(ResourceVo vo) {
        String strsql = SQL_COUNT + DEFAULT_QUERY_WHERE_ENABLE;;
        List objList = new ArrayList();
        if (vo != null) {
            VoHelperTools.null2Nothing(vo); //把vo中null值替换为""
            if (StringUtils.isNotBlank(vo.getName())) {
                objList.add(vo.getName());
                strsql += " AND NAME LIKE ?";
            }
            if (StringUtils.isNotBlank(vo.getType())) {
                strsql += " AND TYPE = ?";
                objList.add(vo.getType());
            }     
            if (StringUtils.isNotBlank(vo.getTag())) {
                objList.add(vo.getTag());
                strsql += " AND TAG LIKE ?";
            }

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            if (vo.getStardDate() != null) {
                String start_time = format.format(vo.getStardDate()) + " 00:00:00.0";
                strsql += " AND CREATE_DATE >= ?";
                objList.add(DateTools.getTimestamp(start_time));
            }
            if (vo.getEndDate() != null) {
                String end_time = format.format(vo.getEndDate()) +" 23:59:59.0";
                strsql += " AND CREATE_DATE <= ?";
                objList.add(DateTools.getTimestamp(end_time));
            }
        }

        return queryForInt(strsql,objList.toArray());

    }
    
    /**
     * 通过查询条件获得所有的VO对象列表，带翻页，带排序字符
     * 
     * @param no 当前页数
     * @param size 每页记录数
     * @param vo 查询条件
     * @param orderStr 排序字符 
     * @return 查询到的VO列表
     */
    public List<ResourceVo> queryByCondition(int no, int size, ResourceVo vo, String orderStr) {
        String strsql = SQL_QUERY_ALL + DEFAULT_QUERY_WHERE_ENABLE;
        List objList = new ArrayList();
        if (vo != null) {
            VoHelperTools.null2Nothing(vo); //把vo中null值替换为""
            if (StringUtils.isNotBlank(vo.getName())) {
                objList.add(vo.getName());
                strsql += " AND NAME LIKE ?";
            }
            if (StringUtils.isNotBlank(vo.getType())) {
                strsql += " AND TYPE = ?";
                objList.add(vo.getType());
            }      
            if (StringUtils.isNotBlank(vo.getTag())) {
                objList.add(vo.getTag());
                strsql += " AND TAG LIKE ?";
            }

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            if (vo.getStardDate() != null) {
                String start_time = format.format(vo.getStardDate()) + " 00:00:00.0";
                strsql += " AND CREATE_DATE >= ?";
                objList.add(DateTools.getTimestamp(start_time));
            }
            if (vo.getEndDate() != null) {
                String end_time = format.format(vo.getEndDate()) +" 23:59:59.0";
                strsql += " AND CREATE_DATE <= ?";
                objList.add(DateTools.getTimestamp(end_time));
            }
        }        
        if (orderStr != null) {
            strsql += " ORDER BY " + orderStr;
        } else {
            strsql += " ORDER BY ID DESC";
        }
         return query(strsql,objList.toArray(), new RowMapper() {
             public Object mapRow(ResultSet rs, int i) throws SQLException {
                 ResourceVo vo = new ResourceVo();
                 Helper.populate(vo, rs);
                 return vo;
             }
         }, (no - 1) * size, size); 
    }
    
}
