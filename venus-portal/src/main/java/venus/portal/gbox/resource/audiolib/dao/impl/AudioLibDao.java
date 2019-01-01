package venus.portal.gbox.resource.audiolib.dao.impl;

import org.springframework.jdbc.core.RowMapper;
import venus.frames.base.dao.BaseTemplateDao;
import venus.frames.mainframe.util.Helper;
import venus.frames.mainframe.util.VoHelper;
import venus.portal.gbox.resource.audiolib.dao.IAudioLibDao;
import venus.portal.gbox.resource.audiolib.util.IAudioLibConstants;
import venus.portal.gbox.resource.audiolib.vo.AudioVo;
import venus.pub.lang.LangException;
import venus.pub.lang.OID;
import venus.pub.util.StringHelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AudioLibDao extends BaseTemplateDao implements IAudioLibDao, IAudioLibConstants {

    /**
     * 插入单条记录，用Oid作主键，把null全替换为""
     * 
     * @param vo 用于添加的VO对象
     * @return 若添加成功，返回新生成的Oid
     */
    public OID insert(AudioVo vo) {
        VoHelper.null2Nothing(vo); //把vo中null值替换为""
        OID oid = null;
        try {
            oid = new OID(vo.getId());
        } catch (LangException e) {
            oid = null;
        }
        Object[] obj = { 
                vo.getId(), 
                vo.getName(),
                vo.getCode(),
                vo.getTag(),
                vo.getAuthor(),
                vo.getIsOriginal(),
                vo.getIsExternal(),
                vo.getIsProtected(),
                vo.getOrigin(),
                vo.getFileName(),
                vo.getFileSize(),
                vo.getFileFormat(),
                vo.getFileStatus(),
                vo.getFileObject(),
                vo.getCreatorName(),
                vo.getCreateDate(),
                vo.getDescription() };
        update(SQL_INSERT, obj);
        return oid;
    }
    
    /**
     * 删除全部记录
     * @return 成功删除的记录数
     */
    public int delete() {
        String strsql = SQL_DELETE;
        return update(strsql);
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
    public AudioVo find(String id) {
        return (AudioVo) queryForObject(SQL_FIND_BY_ID, new Object[] { id }, new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                AudioVo vo = new AudioVo();
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
    public int update(AudioVo vo) {
        VoHelper.null2Nothing(vo); //把vo中null值替换为""
        Object[] obj = { vo.getName(),vo.getCode(),vo.getModifierName(),vo.getModifyDate(),vo.getEnableStatus(),vo.getDescription(), vo.getId() };
        return update(SQL_UPDATE_BY_ID, obj);
    }
    
    /**
     * 更新单条记录
     * 
     * @param vo 用于更新的VO对象
     * @return 成功更新的记录数
     */
    public int updateTag(AudioVo vo) {
        VoHelper.null2Nothing(vo); //把vo中null值替换为""
        Object[] obj = {vo.getTag(),vo.getModifierName(),vo.getModifyDate(), vo.getId() };
        return update(SQL_UPDATETAG_BY_ID, obj);
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
     * @param queryCondition 当前页数
     * @return 查询到的VO列表
     */
    public List<AudioVo> queryByCondition(String queryCondition) {
        return queryByCondition(-1,-1,queryCondition,null);
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
    public List<AudioVo> queryByCondition(int no, int size, String queryCondition, String orderStr) {
        String strsql = SQL_QUERY_ALL + DEFAULT_QUERY_WHERE_ENABLE;
        if (queryCondition != null && queryCondition.length() > 0 && queryCondition.trim().length() > 0) {
            strsql +=  queryCondition; //where后加上查询条件
        }
        
        if (orderStr != null) {
            strsql += " ORDER BY " + orderStr;
        } else {
            strsql += " ORDER BY ID DESC";
        }

        if (no != -1 && size != -1) {
            return query(strsql, new RowMapper() {
                public Object mapRow(ResultSet rs, int i) throws SQLException {
                    AudioVo vo = new AudioVo();
                    Helper.populate(vo, rs);
                    return vo;
                }
            }, (no - 1) * size, size);             
        } else {
            return query(strsql, new RowMapper() {
                public Object mapRow(ResultSet rs, int i) throws SQLException {
                    AudioVo vo = new AudioVo();
                    Helper.populate(vo, rs);
                    return vo;
                }
            }); 
        }
    }
    
}
