package venus.oa.service.test.dao.impl;

import org.springframework.jdbc.core.RowMapper;
import venus.oa.service.test.dao.ITestDAO;
import venus.oa.service.test.util.IConstants;
import venus.oa.service.test.vo.TestVo;
import venus.oa.util.VoHelperTools;
import venus.commons.xmlenum.EnumRepository;
import venus.commons.xmlenum.EnumValueMap;
import venus.frames.base.dao.BaseTemplateDao;
import venus.frames.base.exception.BaseApplicationException;
import venus.frames.mainframe.util.Helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 功能、用途、现存BUG:
 * 
 * @author 刘国华
 * @version 1.0.0
 * @see 需要参见的其它类
 * @since 1.0.0
 */
public class TestDao extends BaseTemplateDao implements ITestDAO,IConstants {

    /**
     * 功能:
     *
     * @see venus.oa.service.test.dao.ITestDAO#getRecordCount(String)
     * @param tableName
     * @return
     */
    public int getRecordCount(String tableName) {
        String strSql = "select count(*) from ("+tableName+")";
        return queryForInt(strSql);
    }

    /**
     * 功能:
     *
     * @see venus.oa.service.test.dao.ITestDAO#queryAll(int, int, String, String)
     * @param no
     * @param size
     * @param orderStr
     * @param tableName
     * @return
     */
    public List queryAll(int no, int size, String strSql, final String tableName) {
        RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                //动态匹配ＶＯ
                EnumRepository er = EnumRepository.getInstance();
                er.loadFromDir();
                EnumValueMap voMap = er.getEnumValueMap("TableVo");
                String classValue = voMap.getValue(tableName);
                Class objClass = null;
                try {
                    objClass = Class.forName(classValue);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }catch (NullPointerException e) {
                    e.printStackTrace();
                    throw new BaseApplicationException(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.This_table_is_not_the_field_may_authorize"));
                }
                Object obj = null;
                try {
                     obj = objClass.newInstance();
                } catch (InstantiationException e1) {
                    e1.printStackTrace();
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                }
                Helper.populate(obj, rs);
                VoHelperTools.null2Nothing(obj);
                return obj;
            }
        };
        List result = query(strSql, rowMapper, (no - 1) * size, size);
        return result;
    }
    /**
     * 查询表名,返回LIST
     * @param orderStr
     * @return
     */
    public List queryAllTable(String orderStr){
        String strSql=QUERY_TABLE_SQL_ORACLE;
        
        RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                TestVo obj = new TestVo();
                obj.setName(rs.getString("table_name"));
                return obj;
            }
        };
        List result = query(strSql, rowMapper);
        return result;
    }

}

