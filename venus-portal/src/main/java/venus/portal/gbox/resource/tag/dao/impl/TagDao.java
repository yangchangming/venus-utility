package venus.portal.gbox.resource.tag.dao.impl;

import org.springframework.jdbc.core.RowMapper;
import venus.frames.base.dao.BaseTemplateDao;
import venus.frames.mainframe.util.Helper;
import venus.frames.mainframe.util.VoHelper;
import venus.portal.gbox.resource.imagelib.vo.ImageVo;
import venus.portal.gbox.resource.tag.dao.ITagDao;
import venus.portal.gbox.resource.tag.util.ITagConstants;
import venus.portal.gbox.resource.tag.vo.TagVo;
import venus.pub.lang.OID;
import venus.pub.util.StringHelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class TagDao extends BaseTemplateDao implements ITagDao, ITagConstants {
    
    /**
     * 插入单条记录，用Oid作主键，把null全替换为""
     * 
     * @param vo 用于添加的VO对象
     * @return 若添加成功，返回新生成的Oid
     */
    public OID insert(TagVo vo) {
        VoHelper.null2Nothing(vo); //把vo中null值替换为""
        OID oid = Helper.requestOID(TABLE_NAME); //获得oid
        long id = oid.longValue();
        vo.setId(String.valueOf(id));
        Object[] obj = {vo.getId(), vo.getName(), vo.getCreateDate()};
        update(SQL_INSERT, obj);
        return oid;
    }
    
    /**
     * 删除单条记录
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
     * 根据name进行查询
     * 
     * @param name 用于查找的name
     * @return 查询到的VO对象
     */
    public TagVo find(String name) {
        return (TagVo) queryForObject(SQL_FIND_BY_NAME, new Object[] { name }, new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                TagVo vo = new TagVo();
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
    public int update(TagVo vo) {
        VoHelper.null2Nothing(vo); //把vo中null值替换为""
        Object[] obj = { vo.getName(), vo.getClicks(),vo.getId() };
        return update(SQL_UPDATE_BY_ID, obj);
    }    
    
    /**
     * 更新tag点击数
     * 
     * @param vo 用于更新的VO对象
     * @return 成功更新的记录数
     */
    public int updateClicks(TagVo vo) {
        VoHelper.null2Nothing(vo); //把vo中null值替换为""
        Object[] obj = { vo.getName()};
        return update(SQL_UPDATECLICKS, obj);
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
    public List<TagVo> queryByCondition(String queryCondition) {
        String strsql = SQL_QUERY_ALL + DEFAULT_QUERY_WHERE_ENABLE;
        if (queryCondition != null && queryCondition.length() > 0 && queryCondition.trim().length() > 0) {
            strsql +=  queryCondition; //where后加上查询条件
        }
            return query(strsql, new RowMapper() {
                public Object mapRow(ResultSet rs, int i) throws SQLException {
                    ImageVo vo = new ImageVo();
                    Helper.populate(vo, rs);
                    return vo;
                }
            }); 
        }
    
}
