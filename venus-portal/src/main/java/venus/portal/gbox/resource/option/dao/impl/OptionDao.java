package venus.portal.gbox.resource.option.dao.impl;

import org.springframework.jdbc.core.RowMapper;
import venus.frames.base.dao.BaseTemplateDao;
import venus.frames.mainframe.util.Helper;
import venus.oa.util.VoHelperTools;
import venus.portal.gbox.resource.option.dao.IOptionDao;
import venus.portal.gbox.resource.option.util.IOptionConstants;
import venus.portal.gbox.resource.option.vo.OptionVo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OptionDao extends BaseTemplateDao implements IOptionDao, IOptionConstants {

    /**
     * 根据Id进行查询
     * 
     * @param id 用于查找的id
     * @return 查询到的VO对象
     */
    public OptionVo find(String id){
        return (OptionVo) queryForObject(SQL_FIND_BY_ID, new Object[] { id }, new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                OptionVo vo = new OptionVo();
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
    public int update(OptionVo vo) {
        VoHelperTools.null2Nothing(vo); //把vo中null值替换为""
        Object[] obj = {vo.getValue(),  vo.getModifierName(),vo.getModifyDate(), vo.getId() };
        return update(SQL_UPDATE_BY_ID, obj);
    }    
    
    /**
     * 通过查询条件获得所有的VO对象列表，带翻页，带排序字符
     * 
     * @return 查询到的VO列表
     */
    public List<OptionVo> queryAll() {
        String strsql = SQL_QUERY_ALL + DEFAULT_QUERY_WHERE_ENABLE;
        return query(strsql, new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                OptionVo vo = new OptionVo();
                Helper.populate(vo, rs);
                return vo;
            }
        });        
    }
    
}
