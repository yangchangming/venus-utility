/*
 * 创建日期 2008-9-10
 */
package venus.oa.util.tree;

import org.springframework.jdbc.core.RowMapper;
import venus.oa.helper.OrgHelper;
import venus.oa.organization.aupartyrelation.vo.AuPartyRelationVo;
import venus.oa.service.sys.vo.SysParamVo;
import venus.oa.util.GlobalConstants;
import venus.frames.base.dao.BaseTemplateDao;
import venus.frames.mainframe.util.Helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *  2008-9-10
 * @author changming.Y <changming.yang.ah@gmail.com>
 */
public class DeepTreeSearch {
	
	public final static String ARROW_SYMBOL = venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority._0"); //组织机构关系连接符号
	public final static String LIST_SEPARATOR = " \n"; //机构路径分隔符号	
	
	/**
	 * 判断该code下所有子节点是否有名字为name的节点，只要有一个就返回true，否则返回false；
	 * @param code
	 * @param name
	 * @return 只要有一个就返回true，否则返回false；
	 */
	public static boolean searchAllSubWhetherLikeName(String code,String name){
		StringBuilder sql=new StringBuilder();
		sql.append("SELECT count(1) FROM AU_PARTYRELATION WHERE PARENT_CODE LIKE '");
		sql.append(code);
		sql.append("%' AND NAME LIKE '%");
		sql.append(name);
		sql.append("%'");
		BaseTemplateDao dao=(BaseTemplateDao) Helper.getBean("aupartyrelationtype_dao");
		int count=dao.queryForInt(sql.toString());
		return count>0;
	}

	/**
	 * 通过partyId获得上级所有的机构名称(默认只显示行政关系)
	 * @param partyId
	 * @return 返回上级所有的关系名称并用→隔开
	 */	
	public static String getOrgNameById(String partyId) {
		return getOrgNameById(partyId,true, GlobalConstants.getRelaType_comp());
	}
	
	/**
	 * 通过partyId获得上级所有的机构名称(默认只显示行政关系)
	 * @param partyId
	 * @param showUserYn 是否显示最后一级的用户
	 * @return 返回上级所有的关系名称并用→隔开
	 */
	public static String getOrgNameById(String partyId,boolean showUserYn) {
		return getOrgNameById(partyId,showUserYn, GlobalConstants.getRelaType_comp());
	}

	/**
	 * 通过partyId获得上级所有的机构名称
	 * @param partyId
	 * @param showUserYn 是否显示最后一级的用户
	 * @param relationTypeId 机构树关系类型ID
	 * @return 返回上级所有的关系名称并用→隔开
	 */
	public static String getOrgNameById(String partyId,boolean showUserYn,String relationTypeId) {
	    StringBuilder sql = new StringBuilder();
	    sql.append("SELECT CODE code FROM AU_PARTYRELATION WHERE PARTYID= '").append(partyId).append("'");
	    SysParamVo sysVo  = GlobalConstants.getSysParam("ORGRELATION");
	    if (sysVo == null) { //没有启用配置项时，按照代码逻辑运行
    		if (relationTypeId != null && relationTypeId.length() != 0) {
    			sql.append(" AND RELATIONTYPE_ID= '").append(relationTypeId).append("'");
    		}
	    } else { //启用配置项后，按照配置项运行，代码逻辑失效
            if(sysVo.getValue().indexOf(",") != -1 ) { //配置多个团体关系类型
                String[] ids = sysVo.getValue().split(",");
                sql.append(" AND (RELATIONTYPE_ID= '").append(GlobalConstants.getRelaType_comp()).append("'");
                for (int i = 0; i < ids.length; i++) {
                    sql.append(" OR RELATIONTYPE_ID= '").append(ids[i]).append("'");
                }
                sql.append(")");
            } else if (!GlobalConstants.ORGRELATION_ALL.equals(sysVo.getValue().toUpperCase())){ //配置一个团体关系类型
                sql.append(" AND (RELATIONTYPE_ID= '").append(GlobalConstants.getRelaType_comp()).append("'");
                sql.append(" OR RELATIONTYPE_ID= '").append(sysVo.getValue()).append("'").append(")"); 
            } 
	    }
	    
		BaseTemplateDao dao=(BaseTemplateDao) Helper.getBean("aupartyrelationtype_dao");
                RowMapper rowMapper = new RowMapper() {
                    public Object mapRow(ResultSet rs, int i) throws SQLException {    
                        return rs.getString("code");
                    }
                };
		List list = dao.query(sql.toString(),rowMapper);
		
		int listSize = list.size();
		StringBuilder returnValue = new StringBuilder();
		if (listSize > 0) {
			StringBuilder retStr = new StringBuilder();
			for (int k = 0; k < listSize; k++) {
				retStr.append(getOrgNameByCode((String)list.get(k),showUserYn));
			}
			if (retStr.toString().length() > 0) {
        			//去掉尾部的分隔符号
        			retStr.replace(retStr.lastIndexOf(LIST_SEPARATOR),retStr.length(),"");	
			}
			returnValue.append(retStr);
		}
		return returnValue.toString();		
	}
	
	/**
	 * 通过code获得上级所有的机构名称
	 * @param code
	 * @return 返回上级所有的关系名称并用→隔开
	 */		
	public static String getOrgNameByCode(String code) {
		return getOrgNameByCode(code,true);
	}	
	
	/**
	 * 通过code获得上级所有的机构名称
	 * @param code
	 * @param showUserYn 是否显示最后一级的用户
	 * @return 返回上级所有的关系名称并用→隔开
	 */	
	public static String getOrgNameByCode(String code,boolean showUserYn) {
		if(code == null || "".equals(code))
			return "";
		StringBuilder retStr = new StringBuilder();
		List list = OrgHelper.getParentRelation(code);
		AuPartyRelationVo vo;
		//由于用户名在最后一级,这里为了不显示用户名,所以i > 0,显示用户名的话 i >=0
		for (int i = list.size() - 1; showUserYn ? i >= 0 : i > 0 ;i--) {
			 vo = (AuPartyRelationVo)list.get(i);
			retStr.append(vo.getName());
			if(i != list.size()){
				retStr.append(ARROW_SYMBOL);
			}			
		}
		if (retStr.toString().length() > 0) {
        		//去掉尾部的箭头符号
        		retStr.replace(retStr.lastIndexOf(ARROW_SYMBOL),retStr.length(),"");
        		retStr.append(LIST_SEPARATOR);	
		}
		return retStr.toString();
	}
	/**
	 * 通过code获得上级所有的机构名称
	 * @param code
	 * @param showUserYn 是否显示最后一级的用户
	 * @return 返回上级所有的关系名称并用→隔开
	 */	
	public static String getOrgNameByHistoryCode(AuPartyRelationVo vo, boolean showUserYn) {
		StringBuilder retStr = new StringBuilder();
		List al = vo.getAll_parent_vo();
		for(int i=0;i<al.size();i++){
			AuPartyRelationVo parentVo = (AuPartyRelationVo)al.get(i);
			retStr.append(parentVo.getName());
			retStr.append(ARROW_SYMBOL);
		}
		if(showUserYn)
			retStr.append(vo.getName());
		else
			retStr.replace(retStr.lastIndexOf(ARROW_SYMBOL),retStr.length(),"");//去掉尾部的箭头符号
		retStr.append(LIST_SEPARATOR);		
		return retStr.toString();
	}
}

