/*
 *  Copyright 2015-2018 DataVens, Inc.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package venus.oa.util;

import venus.frames.mainframe.util.Helper;
import venus.oa.authority.auresource.dao.IAuResourceDao;
import venus.oa.authority.auresource.vo.AuResourceVo;
import venus.oa.login.vo.LoginSessionVo;

import java.util.*;

/**
 * <p> Au helper </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-03-04 10:18
 */
public class AuHelper {

    public static String filterRecordPrivInSQL(String strSql, LoginSessionVo authorizedContext) {
        //TODO 解析strSql得到表名、表名别名，根据表名查询相应权限，然后逐一进行过滤
        /*
         * String relFieldName = fieldName; int index = relFieldName.indexOf('.'); if( index != -1) { relFieldName =
         * relFieldName.substring(index+1, relFieldName.length()); } IAuResourceBs resBs = (IAuResourceBs)
         * Helper.getBean(IAuResourceConstants.BS_KEY); List list = resBs.queryByCondition("table_name='"+tableName+"'
         * and field_name='"+relFieldName +"' and resource_type='"+GlobalConstants.getResType_recd()+"'"); if(list==null ||
         * list.size()==0) { return strSql; } //Map m_data = LoginHelper.getOwnerRecd(request); Map m_filter = new
         * HashMap();
         *
         * int iLen = 0; if(arrayDataPriv!=null) { iLen = arrayDataPriv.length; } String strDataPriv = " (1=2 "; for(int
         * i=0;i <iLen;i++){ if("".equals(arrayDataPriv[i])) { return strSql; } strDataPriv += " or "+fieldName+" like
         * '"+arrayDataPriv[i]+"%' "; } strDataPriv += " ) ";
         *
         * if (strSql != null && strSql.trim().length() > 0) { strSql += " and "+strDataPriv; }else{ strSql =
         * strDataPriv; }
         */
        strSql=strSql.toLowerCase();
        String tempSql = strSql;
        int index = tempSql.lastIndexOf("from");
        tempSql = tempSql.substring(index + 4);
        index = tempSql.indexOf("where");
        if (index != -1) {
            tempSql = tempSql.substring(0, index);
        }//将tempSql截取成from子句并准备提取表名
        index = tempSql.indexOf("order");
        if (index != -1) {
            tempSql = tempSql.substring(0, index);
        }//将tempSql截取成from子句并准备提取表名
        Map map = new HashMap();
        final String patternStr = ",";
        final String patternAs = "as";
        final String patternBlank = " ";
        String tempPattern = "";
        String[] fields = tempSql.split(patternStr);
        //fields为表名、表名和别名、表名加as加别名的集合
        for (int i = 0; i < fields.length; i++) {
            String tempField = fields[i].trim();
            if (tempField.indexOf(patternAs) != -1) {
                tempPattern = patternAs;
            } else if (tempField.indexOf(patternBlank) != -1) {
                tempPattern = patternBlank;
            }
            if (!"".equals(tempPattern)) {
                String[] table = tempField.split(tempPattern);
                map.put(table[1].trim(), table[0].trim());
            } else {
                map.put(tempField, tempField);
            }
        }//map里放置别名和表名序列
        //获取该用户的记录权限Vo
        Map recordMap = authorizedContext.getOwner_recd_map();
        IAuResourceDao auResourceDao = (IAuResourceDao) Helper.getBean("AuResource_dao");
        Set valueSet = map.keySet();
        Iterator valueIt = valueSet.iterator();

        //每一个表都去查它在resource表对应的记录权限设置
        int orderbyIndex = strSql.indexOf("order");
        String strSqlBeforeOrderby = orderbyIndex==-1?strSql:strSql.substring(0, orderbyIndex);
        String strSqlAfterOrderby = orderbyIndex==-1?"":strSql.substring(orderbyIndex);

        while (valueIt.hasNext()) {
            String nameOfName = (String) valueIt.next();//传入sql的查询表的别名
            String nameOfTable = (String) map.get(nameOfName);//传入sql的查询表的名称
            //通过表名和资源类型得到resource表的记录过滤的ID
            AuResourceVo auResourceVo = new AuResourceVo();
            auResourceVo.setTable_name(nameOfTable.toUpperCase());
            auResourceVo.setResource_type("4");
            List IdList = auResourceDao.queryIdByTableNameAndResourceType(auResourceVo);
            //每一个resource表对应的记录权限设置都去拼装sql
            for (int i = 0; i < IdList.size(); i++) {
                AuResourceVo auResourceVoForSQL = (AuResourceVo) IdList.get(i);
                if (recordMap.containsKey(auResourceVoForSQL.getId())) {
                    strSqlBeforeOrderby=sqlAssembly(auResourceVoForSQL,strSqlBeforeOrderby,nameOfName,nameOfTable);
                }
            }
        }
        strSql = strSqlBeforeOrderby + " " + strSqlAfterOrderby;
        return strSql;
    }

    public static String sqlAssembly(AuResourceVo auResourceVoForSQL, String strSqlBeforeOrderby, String nameOfName, String nameOfTable){
        String value = auResourceVoForSQL.getValue();
        //判断用户是否设置了记录权限

        //取元数据类型，判断DATA日期型数据
            /*ICommonBs MetaDataBs = ProjTools.getCommonBsInstance();
            RowMapper rowMapper = new RowMapper() {
                public Object mapRow(ResultSet rs, int i) throws SQLException {
                    String SqlType=String.valueOf(rs.getMetaData().getColumnType(1));
                    return SqlType;
                }
            };
            String metaData = (String) MetaDataBs.doQueryForObject("select min("
                    + auResourceVoForSQL.getField_name() + ") from " + nameOfTable ,
                    rowMapper);*/
        String fieldName = auResourceVoForSQL.getField_name();
        //是日期型
        //废弃日期类型判断，以提高数据库兼容性
            /*if (metaData.equals(String.valueOf(java.sql.Types.TIMESTAMP))) {
                fieldName = "to_char(" + nameOfName + "." + fieldName + ",'yyyy-mm-dd hh24:mi:ss')";
            //不是日期型
            } else {*/
        fieldName = nameOfName + "." + fieldName;
        //}

        //废弃filter_type，用value直接表示过滤条件，以提高灵活性： modify by ganshuo 2008-6-23
            /*if ("in".equals(auResourceVoForSQL.getFilter_type())) {
                value = " (" + value + ")";//todo没有考虑数据的类型如Date型，也没有考虑数据是否用''括起来
            } else if ("like".equals(auResourceVoForSQL.getFilter_type())) {
                value = " '%" + value + "%' ";
            } else {
                value = " '" + value + "' ";
            }*/

        String fileter_type = auResourceVoForSQL.getFilter_type() == null ? "" : auResourceVoForSQL.getFilter_type();

        //废弃filter_type后filter_type为null，这里保留filter_type是为了跟之前版本兼容
        if (strSqlBeforeOrderby.indexOf("where") != -1) {
            strSqlBeforeOrderby = strSqlBeforeOrderby + " and " + fieldName + " "
                    + fileter_type + " " + value;
        } else {
            strSqlBeforeOrderby = strSqlBeforeOrderby + " where " + fieldName + " "
                    + fileter_type + " " + value;
        }

        return strSqlBeforeOrderby;
    }

}
