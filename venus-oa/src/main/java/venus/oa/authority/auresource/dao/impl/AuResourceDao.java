/*
 * 系统名称:PlatForm
 * 
 * 文件名称: venus.authority.au.auresource.dao.impl --> AuResourceDao.java
 * 
 * 功能描述:
 * 
 * 版本历史: 2006-06-09 15:32:17.2 创建1.0.0版 (甘硕)
 *  
 */

package venus.oa.authority.auresource.dao.impl;

import org.springframework.jdbc.core.RowMapper;
import venus.oa.authority.auresource.dao.IAuResourceDao;
import venus.oa.authority.auresource.util.IAuResourceConstants;
import venus.oa.authority.auresource.vo.AuResourceVo;
import venus.oa.util.StringHelperTools;
import venus.oa.util.VoHelperTools;
import venus.frames.base.dao.BaseTemplateDao;
import venus.frames.mainframe.util.Helper;
import venus.pub.lang.OID;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * 功能、用途、现存BUG:
 * 
 * @author 甘硕
 * @version 1.0.0
 * @see
 * @since 1.0.0
 */

public class AuResourceDao extends BaseTemplateDao implements IAuResourceDao, IAuResourceConstants {

    /**
     * 插入单条记录，用Oid作主键，把null全替换为""
     * 
     * @param vo 用于添加的VO对象
     * @return 若添加成功，返回新生成的Oid
     */
    public OID insert(AuResourceVo vo) {
        /*String strSql = " resource_type='"+vo.getResource_type()+"' and field_name='"+vo.getField_name()+"' and table_name='"+vo.getTable_name()+"'";
        List al=queryByCondition(strSql,null);
        if(al.size()>0){
            throw new BaseApplicationException("添加的记录已经存在！");
        }*/
        OID oid = Helper.requestOID(TABLE_NAME); //获得oid
        long id = oid.longValue();
        vo.setId(String.valueOf(id));
        VoHelperTools.null2Nothing(vo); //把vo中null值替换为""
        //System.out.println(vo.getId()+","+vo.getResource_type()+","+vo.getIs_public()+","+vo.getAccess_type()+","+vo.getName()+","+vo.getValue()+","+vo.getTable_name()+","+vo.getParty_type()+","+vo.getHelp()+","+vo.getEnable_status()+","+vo.getEnable_date()+","+vo.getCreate_date()+","+vo.getModify_date());
        Object[] obj = { vo.getId(), vo.getResource_type(), vo.getIs_public(), new Integer(vo.getAccess_type()), vo.getName(), vo.getValue(), vo.getTable_name(), vo.getParty_type(), vo.getHelp(), vo.getFilter_type(),vo.getField_chinesename(),vo.getField_name(),vo.getTable_chinesename(),vo.getEnable_status(), vo.getCreate_date() };
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
    public AuResourceVo find(String id) {
        return (AuResourceVo) queryForObject(SQL_FIND_BY_ID, new Object[] { id }, new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                AuResourceVo vo = new AuResourceVo();
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
    public int update(AuResourceVo vo) {
        /*String strSql = " resource_type='"+vo.getResource_type()+"' and field_name='"+vo.getField_name()+"' and table_name='"+vo.getTable_name()+"' and id<>'"+vo.getId()+"'";
        List al=queryByCondition(strSql,null);
        if(al.size()>0){
            throw new BaseApplicationException("添加的记录已经存在！");
        }*/
        VoHelperTools.null2Nothing(vo); //把vo中null值替换为""
        Object[] obj = { vo.getResource_type(), vo.getIs_public(), new Integer(vo.getAccess_type()), vo.getName(), vo.getValue(), vo.getTable_name(), vo.getParty_type(), vo.getHelp(), vo.getFilter_type(),vo.getField_chinesename(),vo.getField_name(),vo.getTable_chinesename(),vo.getEnable_status(), vo.getModify_date(), vo.getId() };
        return update(SQL_UPDATE_BY_ID, obj);
    }
    /**
     * 更新所有中文表名
     * 
     * @param vo 用于更新的VO对象
     * @return 成功更新的记录数
     */
    public int updateTableName(AuResourceVo vo){
    	 Object[] obj = {vo.getTable_chinesename(), vo.getResource_type(), vo.getTable_name() };
    	 return update(SQL_UPDATE_TABLE_CHINESENAME, obj);
    }
    /**
     * 通过name查询所有的VO对象列表，不翻页
     * 
     * @return 查询到的VO列表
     */
    public List queryPartyByNameAndResourceType(AuResourceVo vo) {
        return query(SQL_QUERY_ALL + " where NAME = '" + vo.getName()+"' and RESOURCE_TYPE='" + vo.getResource_type() + "'", new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                AuResourceVo vo = new AuResourceVo();
                Helper.populate(vo, rs);
                return vo;
            }
        });
    }
    /**
     * 通过tablename查询所有的VO对象列表，不翻页
     * 
     * @return 查询到的VO列表
     */
    public List queryIdByTableNameAndResourceType(AuResourceVo vo) {
        return query(SQL_QUERY_ALL + " where TABLE_NAME = '" + vo.getTable_name()+"' and RESOURCE_TYPE='" + vo.getResource_type() + "'", new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                AuResourceVo vo = new AuResourceVo();
                Helper.populate(vo, rs);
                return vo;
            }
        });
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
                    AuResourceVo vo = new AuResourceVo();
                    Helper.populate(vo, rs);
                    return vo;
                }
            });
        } else {
            return query(strsql, new RowMapper() {
                public Object mapRow(ResultSet rs, int i) throws SQLException {
                    AuResourceVo vo = new AuResourceVo();
                    Helper.populate(vo, rs);
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
        return getRecordCount(null);
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
                    AuResourceVo vo = new AuResourceVo();
                    Helper.populate(vo, rs);
                    VoHelperTools.null2Nothing(vo);
                    return vo;
                }
            });
        } else {
            return query(strsql, new RowMapper() {
                public Object mapRow(ResultSet rs, int i) throws SQLException {
                    AuResourceVo vo = new AuResourceVo();
                    Helper.populate(vo, rs);
                    return vo;
                }
            }, (no - 1) * size, size); 
        }
    }
    /**
     * 通过资源类型查询所有的表名列表，不翻页
     * 
     * @param resourceType 资源类型
     * @return 查询到的VO列表
     */
    public List queryAllTableName(String resourceType){
        Connection dbcon=null;
        DatabaseMetaData dbmd=null;
        ResultSet resultSet=null;
        try {
            dbcon=getDataSource().getConnection();
            dbmd =dbcon.getMetaData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Map tableNames=new HashMap();
        String[] types = {"TABLE"};
        try {
        	 resultSet = dbmd.getTables(null, dbmd.getUserName(), "%", types);
             while (resultSet.next()) {
                 // Get the table name
                 String tableName = resultSet.getString(3);
                 tableNames.put(tableName,tableName);
             } 
        } catch (SQLException e1) {
            e1.printStackTrace();
        }finally{
            try {
                resultSet.close();
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
            try {
                dbcon.close();
            } catch (SQLException e1) {
               e1.printStackTrace();
            }
        }
        String strsql = SQL_QUERY_ALL_TABLE + resourceType;
        List resourceVoList= query(strsql, new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                AuResourceVo vo = new AuResourceVo();
                vo.setTable_name(rs.getString("table_name"));
                vo.setTable_chinesename(rs.getString("table_chinesename"));
                return vo;
            }
        });

        for(int i=0;i<resourceVoList.size();i++){
            AuResourceVo auResourceVo=(AuResourceVo)resourceVoList.get(i);
            if(tableNames.containsKey(auResourceVo.getTable_name())){
                tableNames.remove(auResourceVo.getTable_name());
            }
        }

        //physics table name list
        List physicsTableNameList = new ArrayList();

        Iterator it = tableNames.keySet().iterator();
        while(it.hasNext()){
            String leftTableName = (String)it.next();
            AuResourceVo leftvo = new AuResourceVo();
            leftvo.setTable_name(leftTableName);
            leftvo.setTable_chinesename((String)tableNames.get(leftTableName));
            physicsTableNameList.add(leftvo);
        }

        //comparable the tableName list
        Collections.sort(physicsTableNameList, new Comparator() {
            public int compare(Object object1, Object object2) {
                int flag = 0;
                if (object1 instanceof AuResourceVo && object2 instanceof AuResourceVo) {
                    flag = ((AuResourceVo)object1).getTable_name().compareToIgnoreCase(((AuResourceVo)object2).getTable_name());
                    if(flag==0){
                        flag = ((AuResourceVo)object1).getTable_chinesename().compareToIgnoreCase(((AuResourceVo)object2).getTable_chinesename());
                    }
                }
                return flag;
            }
        });
        return physicsTableNameList;
    }


    /**
     * 通过表名和条件查询所有的列名，不翻页
     * 
     * @param tableName      表名
     * @param queryCondition 查询条件
     * @return  查询到的资源VO列表
     */
    public List queryTableField(String tableName,String queryCondition){
    	//20080711下面内容是不显示已经授过权的字段，但业务上应该需要将同一字段授予不同的权限再分配给不同的人，因此下面代码似无必要。
    	/*String conditionfield = " WHERE TABLE_NAME='" + tableName + "' ";
    	if(null!=queryCondition){
    		conditionfield+=queryCondition;
    	}
        String strsql = SQL_QUERY_ALL_FIELD_TABLE + conditionfield;
        List usedFields= query(strsql, new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                AuResourceVo vo = new AuResourceVo();
                vo.setField_name(rs.getString("field_name"));
                vo.setField_chinesename(rs.getString("field_name"));
                return vo;
            }
        });*/
        List allFields=new ArrayList();
        Connection dbcon=null;
        DatabaseMetaData dbmd=null;
        ResultSet resultSet=null;
        try {
            dbcon=getDataSource().getConnection();
            dbmd =dbcon.getMetaData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //String[] types = {"TABLE"};
        try {
        	 resultSet = dbmd.getColumns(null,null,tableName, "%");
        	 while(resultSet.next()) {
                 // Get the columns name
        	 	 String columnsName = resultSet.getString("COLUMN_NAME");
        	 	 if(!allFields.contains(columnsName)){
        	 	 	allFields.add(columnsName);
        	 	 }
             } 
        } catch (SQLException e1) {
            e1.printStackTrace();
        }finally{
            try {
                resultSet.close();
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
            try {
                dbcon.close();
            } catch (SQLException e1) {
               e1.printStackTrace();
            }
        }
        //20080711下面内容是不显示已经授过权的字段，但业务上应该需要将同一字段授予不同的权限再分配给不同的人，因此下面代码似无必要。
		/*for(int i=0;i<usedFields.size();i++){
			AuResourceVo auResourceVo=(AuResourceVo)usedFields.get(i);
            if(allFields.contains(auResourceVo.getField_name())){
            	allFields.remove(auResourceVo.getField_name());
            }  
		}*/
		List allUsedFields=new ArrayList();
		for(int i=0;i<allFields.size();i++){
			AuResourceVo auResourceVo=new AuResourceVo();
			auResourceVo.setField_name((String)allFields.get(i));
			auResourceVo.setField_chinesename((String)allFields.get(i));
			VoHelperTools.null2Nothing(auResourceVo);
			allUsedFields.add(auResourceVo);
		}
		return allUsedFields;
    }
}
	

