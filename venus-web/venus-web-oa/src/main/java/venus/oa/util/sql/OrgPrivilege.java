package venus.oa.util.sql;

import venus.oa.helper.LoginHelper;
import venus.oa.util.SqlBuilder;

import javax.servlet.http.HttpServletRequest;

/**
 *  2008-11-7
 * @author changming.Y <changming.yang.ah@gmail.com>
 */

/**
 * 
 * 功能: 在sql语句中过滤组织机构数据权限
 * 
 * @param strSql 要过滤权限的sql语句（可以为空）
 * @param fieldName 要过滤的字段名称，可以带表名别名，例如 a.fieldname
 * @param request HttpServletRequest
 * @return
 */
public final class OrgPrivilege  implements SqlBuilder.Expression{
	private String fieldName;
	private HttpServletRequest request;
	
	/**
	 * @param fieldName 要设置的 fieldName。
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * @param request 要设置的 request。
	 */
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	/* （非 Javadoc）
	 * @see venus.authority.util.SqlBuilder.Expression#bulidSql()
	 */
	public String bulidSql() {
		String[] arrayDataPriv = LoginHelper.getOwnerFunOrg(request);//功能数据优先
        if(arrayDataPriv==null)//如果存在功能数据权限则屏蔽数据权限
        	arrayDataPriv = LoginHelper.getOwnerOrg(request);
		int iLen = 0;
        if (arrayDataPriv != null) {
            iLen = arrayDataPriv.length;
        }
        String strDataPriv = " (1=2 ";
        for (int i = 0; i < iLen; i++) {
            if ("".equals(arrayDataPriv[i])) {
                return " 1=1 ";
            }
            strDataPriv += " or " + fieldName + " like '" + arrayDataPriv[i] + "%' ";
        }
        strDataPriv += " ) ";
        return strDataPriv;
	}
	
}    

