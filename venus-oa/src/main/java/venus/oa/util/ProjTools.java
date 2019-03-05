package venus.oa.util;

import org.springframework.jdbc.core.RowMapper;
import venus.frames.base.dao.BaseTemplateDao;
import venus.oa.util.common.bs.ICommonBs;
import venus.springsupport.BeanFactoryHelper;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ProjTools {
    
    /**
     * 功能: 根据父编码生成树状编码的子编码
     * 
     * @param tableName 需要编码的表格名称
     * @param columnName 需要编码的列名
     * @param stepLen 每一级的编码长度
     * @param parentCode 父编码
     * @return 子编码
     */
    public static String getTreeCode(String tableName, String columnName, int stepLen, String parentCode) {
        int pLen = parentCode.length();
        String wildcard = "";//通配符，采用通配符是为了避免大数据量的查询，从而提高查询速度
        for(int i=0; i<stepLen; i++) {
            wildcard += "_";
        }
        //旧的非标准SQL：String strSql = "select NVL(TO_NUMBER(SUBSTR(MAX("+columnName+"),"+(pLen+1)+","+stepLen+"))+1,1) as maxid from "+tableName+" t where "+columnName+" like'"+parentCode+"%' and LENGTH("+columnName+")="+(pLen+stepLen);
        String strSql = "select MAX("+columnName+") maxcode from "+tableName+" t where "+columnName+" like'"+parentCode+wildcard+"'";
        String maxcode = (String) ProjTools.getCommonBsInstance().doQueryForObject(strSql, new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                return rs.getString("maxcode");
            }
        });
        String code = null;
        if(maxcode==null || maxcode.length()==0) {
            code = "1";
        }else {
            code = String.valueOf( Integer.parseInt(maxcode.substring(pLen)) + 1 );
        }
        while(code.length() < stepLen) {
			code = "0" + code;
		}
		return parentCode+code;
    }
    
    /**
     * 功能: 根据父编码生成树状编码的子编码(保证节点被调级或删除后，新生成的节点编码不会与历史节点编码重复)
     * 
     * @param stepLen 每一级的编码长度
     * @param parentCode 父编码
     * @return 子编码
     */    
    public static String getNewTreeCode(int stepLen, String parentCode) {
        int pLen = parentCode.length();
        String wildcard = "";//通配符，采用通配符是为了避免大数据量的查询，从而提高查询速度
        for(int i=0; i<stepLen; i++) {
            wildcard += "_";
        }
        StringBuffer strSql = new StringBuffer();
        strSql.append("SELECT MAX(code) maxcode FROM (");
        strSql.append( "SELECT code  code FROM au_partyrelation WHERE code like'").append(parentCode).append(wildcard).append( "'");
        strSql.append(" UNION ");
        //只有删除和调级操作时，才取历史表里code的最大值.保持新增和修改记录时，关系表与历史表记录code同步
        strSql.append("SELECT source_code  code FROM au_history WHERE operate_type IN ('")
        	  .append(GlobalConstants.HISTORY_LOG_ADJUST)
        	  .append("','").append(GlobalConstants.HISTORY_LOG_DELETE)
        	  .append("') AND source_code like'").append(parentCode).append(wildcard).append("'");
        strSql.append(") a");
        String maxcode = (String) ProjTools.getCommonBsInstance().doQueryForObject(strSql.toString(), new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                return rs.getString("maxcode");
            }
        });        
        String code = null;
        if(maxcode==null || maxcode.length()==0) {
            code = "1";
        }else {
            code = String.valueOf( Integer.parseInt(maxcode.substring(pLen)) + 1 );
        }
        while(code.length() < stepLen) {
			code = "0" + code;
		}
		return parentCode+code;        
    }
    
    /**
     * 功能: 逐层切割编码，获得其所有上级编码的数组
     * 
     * @param code 需要切割的编码
     * @param rootLen 根节点编码长度
     * @param stepLen 每一级的编码长度
     * @return 编码数组
     */
    public static String[] splitTreeCode(String code, int rootLen, int stepLen) {
        if(code==null || stepLen==0) {
            return null;
        }
        int pLen = code.length();
        int arrLen = (pLen-rootLen)/stepLen + 1;
        String [] codeArray = new String[arrLen];
        for(int i=0; i<arrLen; i++) {
             code = code.substring(0,pLen);
             codeArray[i] = code;
             pLen = pLen-stepLen;
		}
		return codeArray;
    }
    
    /**
     * 功能: 逐层切割编码，获得其所有上级编码的数组（本系统专用）
     * 
     * @param code 需要切割的编码
     * @return 编码数组
     */
    public static String[] splitTreeCode(String code) {
		return splitTreeCode(code, 19, 5);
    }
    
    public static ICommonBs getCommonBsInstance() {
        return (ICommonBs)BeanFactoryHelper.getBean("CommonBs");
    }

    /**
     * 
     * 功能:判断参数1是否可以通过乘积拆分成参数2 
     *
     * @return
     */
    public static boolean judgeNum(int bigNum, int smallNum) {
        if(smallNum<1)
    		return false;
        if(smallNum==1)
            return true;
    	for(int i=1; (i*smallNum)<=bigNum; i++) {
    		if((i*smallNum)==bigNum)
    			return true;
    	}
    	return false;
    }
    
    /**
     * 数据库类型
     * @author changming.Y <changming.yang.ah@gmail.com>
     *
     */
    public static interface DataBaseProductType{
        public static final String ORACLE = "Oracle";
        public static final String DB2 = "DB2";
        public static final String SQLSERVER = "SQLServer";
        public static final String MYSQL = "MySQL";
        public static final String HSQL = "HSQL";
        public static final String INFORMIX = "Informix";
    }
    
    private static Map currentDataBase = new HashMap();
    
    public static String getDataBaseProductType(String daoName) {
        if(currentDataBase.containsKey(daoName))
            return (String)currentDataBase.get(daoName);
        String databaseProductName = null;
        BaseTemplateDao baseDao = null;
        if(null==daoName)
//            baseDao = (BaseTemplateDao) getCommonBsInstance().getDao();
            baseDao = (BaseTemplateDao)BeanFactoryHelper.getBean("commonDao");
        else
//            baseDao = (BaseTemplateDao) Helper.getBean(daoName);
            baseDao = (BaseTemplateDao) BeanFactoryHelper.getBean(daoName);
        Connection con = null;
        try {
            con = baseDao.getDataSource().getConnection();
            DatabaseMetaData meta = con.getMetaData();
            databaseProductName = meta.getDatabaseProductName();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        Class typeClass = null;
        try {
            typeClass = Class.forName(DataBaseProductType.class.getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Field typeFields[] = typeClass.getFields();
        for(int i=0;i<typeFields.length;i++){
            Field field = typeFields[i];
            String fieldValue = null;
            try {
                fieldValue = (String)field.get(null);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if(databaseProductName.replaceAll(" ", "").toLowerCase().contains(fieldValue.toLowerCase())){
                currentDataBase.put(daoName, fieldValue);
                return fieldValue;
            }
        }
        return null;
    }
    
    
    
}

