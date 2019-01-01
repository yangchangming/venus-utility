/*
 * 系统名称:PlatForm
 * 
 * 文件名称: venus.authority.helper --> AuHelper.java
 * 
 * 功能描述:
 * 
 * 版本历史: 2005-6-28 13:04:51 创建1.0.0版 (ganshuo)
 *  
 */
package venus.oa.helper;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.jdbc.core.RowMapper;
import venus.oa.authority.auauthorize.bs.IAuAuthorizeBS;
import venus.oa.authority.auauthorize.util.IConstants;
import venus.oa.authority.auauthorize.vo.AuAuthorizeVo;
import venus.oa.authority.aufunctree.bs.IAuFunctreeBs;
import venus.oa.authority.aufunctree.util.IAuFunctreeConstants;
import venus.oa.authority.aufunctree.vo.AuFunctreeVo;
import venus.oa.authority.auresource.bs.IAuResourceBs;
import venus.oa.authority.auresource.dao.IAuResourceDao;
import venus.oa.authority.auresource.util.IAuResourceConstants;
import venus.oa.authority.auresource.vo.AuResourceVo;
import venus.oa.login.vo.LoginSessionVo;
import venus.oa.service.sys.vo.SysParamVo;
import venus.oa.util.GlobalConstants;
import venus.oa.util.ProjTools;
import venus.oa.util.StringHelperTools;
import venus.frames.mainframe.util.Helper;

import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * 功能、用途、现存BUG:
 * 
 * @author 甘硕
 * @version 1.0.0
 * @see 需要参见的其它类
 * @since 1.0.0
 */
public class AuHelper {

    /**
     * 构造函数:
     *  
     */
    public AuHelper() {

    }

    /**
     *
     * 功能: 判断是否有权限
     * 
     * @param partyId 用户partyId
     * @param resCode 资源编号（对记录和字段为资源id，其他为树状编码）
     * @param resType 资源类型： 菜单－menu 按钮－button 字段－field 记录－record 组织－org
     * @return
     */
    public static boolean hasPriv(String partyId, String resCode, String resType) {
        IAuResourceBs resBs = (IAuResourceBs) Helper.getBean(IAuResourceConstants.BS_KEY);
        String strSql = "";
        String resTypeCode = "";
        if ("menu".equals(resType)) {
            strSql = "value='" + resCode + "' and resource_type='" + GlobalConstants.getResType_menu() + "'";
            resTypeCode = GlobalConstants.getResType_menu();
        } else if ("button".equals(resType)) {
            strSql = "value='" + resCode + "' and resource_type='" + GlobalConstants.getResType_butn() + "'";
            resTypeCode = GlobalConstants.getResType_butn();
        } else if ("field".equals(resType)) {
            strSql = "id='" + resCode + "' and resource_type='" + GlobalConstants.getResType_fild() + "'";
            resTypeCode = GlobalConstants.getResType_fild();
        } else if ("record".equals(resType)) {
            strSql = "id='" + resCode + "' and resource_type='" + GlobalConstants.getResType_recd() + "'";
            resTypeCode = GlobalConstants.getResType_recd();
        } else if ("org".equals(resType)) {
            strSql = "value='" + resCode + "' and resource_type='" + GlobalConstants.getResType_orga() + "'";
            resTypeCode = GlobalConstants.getResType_orga();
        }

        List list = resBs.queryByCondition(strSql);
        if (list == null || list.size() == 0) {
            return false;
        }
        AuResourceVo resVo = (AuResourceVo) list.get(0);
        if ("1".equals(resVo.getIs_public())) {
            return true;
        }
        IAuAuthorizeBS auBs = (IAuAuthorizeBS) Helper.getBean(IConstants.BS_KEY);
        Map m_all = auBs.getAuByPartyId(partyId, resTypeCode);//TODO:优化
        if (m_all.keySet().contains(resVo.getId())) {
            return true;
        }
        return false;
    }

    /**
     * 
     * 功能: 获取某用户拥有的功能权限列表，包括菜单和按钮
     * 
     * @param partyId 用户partyId
     * @return
     */
    public static Map getFuncPrivList(String partyId) {
        Map m_func = new HashMap();
        IAuAuthorizeBS auBs = (IAuAuthorizeBS) Helper.getBean(IConstants.BS_KEY);
        m_func.putAll(auBs.getAuByPartyId(partyId, GlobalConstants.getResType_menu()));
        m_func.putAll(auBs.getAuByPartyId(partyId, GlobalConstants.getResType_butn()));
        return m_func;
    }

    /**
     * 
     * 功能: 获取某用户拥有的指定类型的功能权限列表
     * 
     * @param partyId 用户partyId
     * @param resType 资源类型： 菜单－menu 按钮－button
     * @return
     */
    public static Map getFuncPrivList(String partyId, String resType) {
        Map m_func = new HashMap();
        IAuAuthorizeBS auBs = (IAuAuthorizeBS) Helper.getBean(IConstants.BS_KEY);
        if ("menu".equals(resType)) {
            m_func = auBs.getAuByPartyId(partyId, GlobalConstants.getResType_menu());
        } else if ("button".equals(resType)) {
            m_func = auBs.getAuByPartyId(partyId, GlobalConstants.getResType_butn());
        }
        return m_func;
    }

    /**
     * 
     * 功能:获取某用户拥有的全部数据权限列表
     * 
     * @param partyId 用户partyId
     * @return
     */
    public static Map getDataPrivList(String partyId) {
        Map m_data = new HashMap();
        IAuAuthorizeBS auBs = (IAuAuthorizeBS) Helper.getBean(IConstants.BS_KEY);
        m_data.putAll(auBs.getAuByPartyId(partyId, GlobalConstants.getResType_fild()));
        m_data.putAll(auBs.getAuByPartyId(partyId, GlobalConstants.getResType_recd()));
        if(m_data!=null) {
	        //只取允许的
	        String AU_PERMIT = GlobalConstants.getAuTypePermit();//权限类型——允许
	        Iterator it=m_data.keySet().iterator();
	        while( it.hasNext() ) {
	        	String key = (String)it.next();
		    	AuAuthorizeVo auVo = (AuAuthorizeVo)m_data.get(key);
		    	String auStatus = auVo.getAuthorize_status();//授权情况
		    	 if( ! AU_PERMIT.equals(auStatus)) { 
		    	 	m_data.remove(key); 
		    	 }
		    }
        }
        m_data.putAll(auBs.getAuByPartyId(partyId, GlobalConstants.getResType_orga()));
        return m_data;
    }

    /**
     * 
     * 功能: 返回指定类型的数据权限
     * 
     * @param partyId 用户partyId
     * @param resType 资源类型：字段－field 记录－record 组织－org
     * @return
     */
    public static Map getDataPrivList(String partyId, String resType) {
        Map m_data = new HashMap();
        IAuAuthorizeBS auBs = (IAuAuthorizeBS) Helper.getBean(IConstants.BS_KEY);
        if ("field".equals(resType)) {
            m_data = auBs.getAuByPartyId(partyId, GlobalConstants.getResType_fild());
        } else if ("record".equals(resType)) {
            m_data = auBs.getAuByPartyId(partyId, GlobalConstants.getResType_recd());
        } else if ("org".equals(resType)) {
            m_data = auBs.getAuByPartyId(partyId, GlobalConstants.getResType_orga());
        }
        
        if(m_data!=null && ! "org".equals(resType)) {
	        //只取允许的
	        String AU_PERMIT = GlobalConstants.getAuTypePermit();//权限类型——允许
	        Iterator it=m_data.keySet().iterator();
	        while( it.hasNext() ) {
	        	String key = (String)it.next();
		    	AuAuthorizeVo auVo = (AuAuthorizeVo)m_data.get(key);
		    	String auStatus = auVo.getAuthorize_status();//授权情况
		    	 if( ! AU_PERMIT.equals(auStatus)) { 
		    	 	m_data.remove(key); 
		    	 }
		    }
        }
        return m_data;
    }

    /**
     * 
     * 功能: 在vo中过滤字段数据权限
     * @deprecated
     * @param vo 要过滤权限的vo
     * @param tableName vo对应的表名
     * @param request HttpServletRequest
     */
    public static void filterFieldPrivInVo(Object vo, String tableName, HttpServletRequest request) {
        IAuResourceBs resBs = (IAuResourceBs) Helper.getBean(IAuResourceConstants.BS_KEY);
        List list = resBs.queryByCondition("table_name='" + tableName + "' and resource_type='"
                + GlobalConstants.getResType_fild() + "'");
        if (list == null || list.size() == 0) {
            return;
        }
        Map m_data = LoginHelper.getOwnerFild(request);
        Map m_filter = new HashMap();
        for (int i = 0; i < list.size(); i++) {
            AuResourceVo resVo = (AuResourceVo) list.get(i);
            if ("1".equals(resVo.getIs_public()) || m_data.keySet().contains(resVo.getId())) {
                continue;
            }
            m_filter.put(resVo.getField_name(), null);
        }

        BeanWrapper bw = new BeanWrapperImpl(vo);
        PropertyDescriptor pd[] = bw.getPropertyDescriptors();
        for (int i = 0; i < pd.length; i++) {
            String name = pd[i].getName();
            if (!"class".equals(name) && m_filter.keySet().contains(name.toUpperCase())) {
                try {
                    bw.setPropertyValue(name, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * 通过表名和字段名获取资源ID
     * @param tableName
     * @param fieldName
     * @param request
     * @return
     */
    private static String getFieldId(String tableName,String fieldName, HttpServletRequest request){
        LoginSessionVo vo= LoginHelper.getLoginVo(request);
        Map allField=vo.getAll_field_map();
        if(allField==null){
            allField=new HashMap();
        }

        if(allField.get(tableName)==null){
            IAuResourceBs resBs = (IAuResourceBs) Helper.getBean(IAuResourceConstants.BS_KEY);
            List list = resBs.queryByCondition("table_name='" + tableName + "' and resource_type='"
                    + GlobalConstants.getResType_fild() + "'");
            allField.put(tableName, list);

            vo.setAll_field_map(allField);
            }
        List fieldList=(List) allField.get(tableName);

        for(int i=0; i<fieldList.size(); i++){
            AuResourceVo resVo = (AuResourceVo) fieldList.get(i);
            if(resVo.getField_name().toLowerCase().equals(fieldName.toLowerCase())){
                return resVo.getId();
            }

        }
        return null;

    }
    /**
     * 判断是否拥有字段读的权限
     * @param tableName
     * @param fieldName
     * @param request
     * @return
     */
    public static boolean hasReadFieldPriv(String tableName, String fieldName, HttpServletRequest request){
        Map m_data = LoginHelper.getOwnerFild(request);
        if(m_data==null || m_data.size()==0){//没有授权字段
            return false;
        }
        String fieldId=getFieldId( tableName, fieldName, request);

        if(fieldId==null){//没有注册授权字段
            return false;
        }

        if(!m_data.keySet().contains(fieldId)){//没有包含授权字段
            return false;
        }else{
            return true;
        }
    }
    /**
     *  判断是否拥有字段写的权限
     * @param tableName
     * @param fieldName
     * @param request
     * @return
     */
    public static boolean hasWriteFieldPriv(String tableName, String fieldName, HttpServletRequest request){
        Map m_data = LoginHelper.getOwnerFild(request);
        if(m_data==null || m_data.size()==0){//没有授权字段
            return false;
        }
        String fieldId=getFieldId( tableName, fieldName, request );

        if(fieldId==null){//没有注册授权字段
            return false;
        }

        if(!m_data.keySet().contains(fieldId)){//没有包括授权字段
            return false;
        }

        AuAuthorizeVo vo=(AuAuthorizeVo) m_data.get(fieldId);
        if(vo.getAccess_type().equals("5")){//只读为3,读写为5
            return true;
        }else{
            return false;
        }

    }
    /**
     * 
     * 功能: 在sql语句中过滤记录数据权限
     * 
     * @param strSql 要过滤权限的sql语句
     * @param request HttpServletRequest
     * @return
     */
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
        //获取resource表的DAO
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
    /**
     * 
     * 功能: 在sql语句中过滤组织机构数据权限
     * 
     * @param strSql 要过滤权限的sql语句（可以为空）
     * @param fieldName 要过滤的字段名称，可以带表名别名，例如 a.fieldname
     * @param request HttpServletRequest
     * @return
     */
    public static String filterOrgPrivInSQL(String strSql, String fieldName, HttpServletRequest request) {
        String[] arrayDataPriv = LoginHelper.getOwnerFunOrg(request);//功能数据优先
        if(arrayDataPriv==null)//如果存在功能数据权限则屏蔽数据权限
        	arrayDataPriv = LoginHelper.getOwnerOrg(request);
        int iLen = 0;
        if (arrayDataPriv != null) {
            iLen = arrayDataPriv.length;
        }
        //获取系统参数 DATAPRIV_INHERIT
        SysParamVo datapriv_inheritVo= GlobalConstants.getSysParam(GlobalConstants.DATAPRIV_INHERIT);
        String datapriv_inherit=datapriv_inheritVo==null?"true":datapriv_inheritVo.getValue();

        String strDataPriv = " (1=2 ";
        for (int i = 0; i < iLen; i++) {
            if ("".equals(arrayDataPriv[i])) {
                return strSql;
            }
            if(arrayDataPriv[i].startsWith(GlobalConstants.getRelaType_role())||arrayDataPriv[i].startsWith(GlobalConstants.getRelaType_proxy())){
                //角色关系和代理关系采取完全匹配方式
                //因为角色和代理在权限过滤时，不应该考虑角色和代理的下级员工节点。
                //因此，角色和代理在授权时要进行及联精确授权。
                strDataPriv += " or " + fieldName + " = '" + arrayDataPriv[i] + "' ";
            }else{
                if("true".equals(datapriv_inherit)){ //数据权限向下继承
                    strDataPriv += " or " + fieldName + " like '" + arrayDataPriv[i] + "%' ";
                } else{  //数据权限不向下继承
                    strDataPriv += " or " + fieldName + " like '" + arrayDataPriv[i] + "' ";
                }

            }
        }
        strDataPriv += " ) ";

        if (strSql != null && strSql.trim().length() > 0) {
            strSql += " and " + strDataPriv;
        } else {
            strSql = strDataPriv;
        }
        return strSql;
    }
    
    /**
	 * 根据request查询当前登陆人所关联的角色的partyid。如果当前登陆人未关联任何角色，则返回null。
	 * @param req
	 * @return 角色 partyid 的数组
	 */
	public static String[]  getRolePartyIdByRequest(HttpServletRequest req) {
		
		//取到登陆人的partyid
		String partyid = LoginHelper.getPartyId(req);
		
		return getRolePartyIdByUserPartyId(partyid);
		
	}
	
	/**
	 * 根据用户partyid查询该用户所关联的角色的partyid。如果该用户未关联任何角色，则返回null。
	 * @param partyid
	 * @return 角色 partyid 的数组
	 */
	public static String[]  getRolePartyIdByUserPartyId(String partyid) {
		String []rolePartyIds = null;
		
		//取到团体关系类型->角色类型的id
		String reltypeid= GlobalConstants.getRelaType_role();
		//查询登陆人父级角色的编号，也就是最底层的角色编号
		String strsql = "select parent_code from au_partyrelation where partyid='"+partyid+"' and RELATIONTYPE_ID='"+reltypeid+"'";
		List lBaseRole = ProjTools.getCommonBsInstance().doQuery(strsql, new RowMapper() {
            public Object mapRow(ResultSet rs, int no) throws SQLException {
				return rs.getString("parent_code");
            }
        });
		//可能有多级角色，而且上级角色有可能重叠
		HashMap roleMap = new HashMap();
		if(lBaseRole!=null && lBaseRole.size()>0) {
        	for(Iterator it = lBaseRole.iterator(); it.hasNext(); ) {
        		String roleCode = (String)it.next();
        		//通过切割得到当前角色编码及其所有上级角色的编码
        		String codes[] = ProjTools.splitTreeCode(roleCode);
        		//去除重复的编码
        		for(int i=0;  i<codes.length; i++) {
	        		if( ! roleMap.containsKey(codes[i]) )
	        			roleMap.put(codes[i],null);
        		}
        	}
        	//查询所有跟登陆人相关的角色的partyid
    		String roleCodes[] = (String[]) roleMap.keySet().toArray(new String[0]);
    		String strsql2 = "select partyid from au_partyrelation where code in (" + StringHelperTools.parseToSQLStringComma(roleCodes) + ")";
    		List lAllRole = ProjTools.getCommonBsInstance().doQuery(strsql2, new RowMapper() {
                public Object mapRow(ResultSet rs, int no) throws SQLException {
    				return rs.getString("partyid");
                }
            });
    		rolePartyIds = (String[]) lAllRole.toArray(new String[0]);
        }
		
		return rolePartyIds;
	}
	
	/**
	 * 根据用户名获取已关联角色的列表
	 * @param userName
	 * @return 
	 */
	public static List getRoles(String userName) {
		List roleList = new ArrayList();
		
		//取到团体关系类型->角色类型的id
		String reltypeid= GlobalConstants.getRelaType_role();
		//查询登陆人父级角色的编号，也就是最底层的角色编号
		String strsql = "select parent_code from au_partyrelation a, au_user b where a.partyid=b.party_id and b.login_id='"+userName+"' and a.RELATIONTYPE_ID='"+reltypeid+"'";
		List lBaseRole = ProjTools.getCommonBsInstance().doQuery(strsql, new RowMapper() {
            public Object mapRow(ResultSet rs, int no) throws SQLException {
				return rs.getString("parent_code");
            }
        });
		//可能有多级角色，而且上级角色有可能重叠
		HashMap roleMap = new HashMap();
		if(lBaseRole!=null && lBaseRole.size()>0) {
        	for(Iterator it = lBaseRole.iterator(); it.hasNext(); ) {
        		String roleCode = (String)it.next();
        		//通过切割得到当前角色编码及其所有上级角色的编码
        		String codes[] = ProjTools.splitTreeCode(roleCode);
        		//去除重复的编码
        		for(int i=0;  i<codes.length; i++) {
	        		if( ! roleMap.containsKey(codes[i]) )
	        			roleMap.put(codes[i],null);
        		}
        	}
        	//将HashMap转成ArrayList
        	for(Iterator it=roleMap.keySet().iterator(); it.hasNext(); roleList.add(it.next())) 
        		;
        }
		return roleList;
	}
	
	/**
	 * 根据菜单名称（中文）查询对该菜单拥有允许访问权限的人员partyId列表
	 * @param name
	 * @return
	 */
	public static String[] getUserPartyIdByMenuName(String name) {
		IAuFunctreeBs bs = (IAuFunctreeBs) Helper.getBean(IAuFunctreeConstants.BS_KEY);
		List list = bs.queryByCondition("name='"+name+"'");
		if(list==null || list.size()==0) {
			return null;
		}
		String totalcode =((AuFunctreeVo)list.get(0)).getTotal_code();
		return getUserPartyIdByMenuTotalCode(totalcode);
	}
	/**
	 * 根据菜单标识查询对该菜单拥有允许访问权限的人员partyId列表
	 * @param keyword
	 * @return
	 */
	public static String[] getUserPartyIdByMenuKeyword(String keyword) {
		IAuFunctreeBs bs = (IAuFunctreeBs) Helper.getBean(IAuFunctreeConstants.BS_KEY);
		List list = bs.queryByCondition("keyword='"+keyword+"'");
		if(list==null || list.size()==0) {
			return null;
		}
		String totalcode =((AuFunctreeVo)list.get(0)).getTotal_code();
		return getUserPartyIdByMenuTotalCode(totalcode);
	}
	/**
	 * 根据菜单编号查询对该菜单拥有允许访问权限的人员partyId列表
	 * @param totalcode
	 * @return
	 */
	public static String[] getUserPartyIdByMenuTotalCode(String totalcode) {
		IAuAuthorizeBS bs = (IAuAuthorizeBS) Helper.getBean(IConstants.BS_KEY);
		return bs.getPartyIdByResourceCode(totalcode);
	}
	
	/**
	 * 根据资源Id查询对该资源拥有允许访问权限的人员partyId列表
	 * @param resourceId 资源Id
	 * @return
	 */
	public static String[] getPartyIdByResourceId(String resourceId){
	    IAuResourceBs resBs = (IAuResourceBs) Helper.getBean(IAuResourceConstants.BS_KEY);
	    AuResourceVo resourceVo = resBs.find(resourceId);
	    IAuAuthorizeBS auBs = (IAuAuthorizeBS) Helper.getBean(IConstants.BS_KEY);
	    return auBs.getPartyIdByResourceCode(resourceVo.getValue());	    
	}

}