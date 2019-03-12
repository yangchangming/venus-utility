<%@ include file="/jsp/authority/tree/include/globalTreeCache.jsp" %>
<%@ page contentType="text/xml;charset=UTF-8" language="java" %>
<%@ page import="venus.oa.util.tree.DeepTreeXmlHandler"%>
<%@ page import="org.springframework.jdbc.core.RowMapper"%>
<%@ page import="java.sql.ResultSet"%>
<%@ page import="java.sql.SQLException"%>
<%@ page import="venus.oa.util.tree.DeepTreeVo"%>
<%@ page import="venus.oa.util.ProjTools"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="venus.oa.util.GlobalConstants"%>
<%@ page import="venus.oa.helper.LoginHelper"%>
<%
try {
	//标识是做导航还是做选择
	String has_checkbox = request.getParameter("has_checkbox");
	if(has_checkbox == null) has_checkbox = "0";

	String parent_code = request.getParameter("parent_code");//父级编码
	if(parent_code == null) parent_code = GlobalConstants.getRelaType_role();
	
	String userPartyId = request.getParameter("partyId");
	if(userPartyId == null) userPartyId = "";
	
	//参数：是否控制数据权限,0 否，1 是
    String data_limit = request.getParameter("data_limit");
    if(data_limit == null || data_limit.length()==0) {
        data_limit = "0";
    }
    String dataLimit[] = null;
    if("0".equals(data_limit) || "no".equals(data_limit) || "false".equals(data_limit)) {
        dataLimit = new String[1];
        dataLimit[0] = parent_code;//如果不控制数据权限，则默认有全部权限
    }else {
        dataLimit = LoginHelper.getOwnerFunOrg(request);//取功能数据权限
        if(null==dataLimit)//如果没有功能数据权限，则取数据权限
            dataLimit = LoginHelper.getOwnerOrgWithoutHistory(request);//取数据权限
    }
	
	String partyType = GlobalConstants.getPartyType_role();
	
	String strsql = "select id, name, code, partyid, type_is_leaf from au_partyrelation where parent_code='"
					+ parent_code + "' and partytype_id='" + partyType + "' order by order_code";
	List lParty = ProjTools.getCommonBsInstance().doQuery(strsql, new RowMapper() {
            public Object mapRow(ResultSet rs, int no) throws SQLException {
                HashMap map = new HashMap();
                map.put("id",rs.getString("id"));
				map.put("name",rs.getString("name"));
				map.put("code",rs.getString("code"));
				map.put("partyid",rs.getString("partyid"));
				map.put("type_is_leaf",rs.getString("type_is_leaf"));
				return map;
            }
        });
    
    //查询已关联的角色
    HashMap roleMap = new HashMap();
    if(userPartyId!=null && userPartyId.length()>0) {
    	strsql = "select parent_code from au_partyrelation where partyid='" + userPartyId + "'";
    	List lRole = ProjTools.getCommonBsInstance().doQuery(strsql, new RowMapper() {
            public Object mapRow(ResultSet rs, int no) throws SQLException {
				return rs.getString("parent_code");
            }
        });
        if(lRole!=null && lRole.size()>0) {
        	for(Iterator it = lRole.iterator(); it.hasNext(); ) {
        		roleMap.put((String)it.next(),null);
        	}
        }
    }
    
	DeepTreeXmlHandler dt = new DeepTreeXmlHandler();
	for(Iterator it = lParty.iterator(); it.hasNext(); ) {
		//获取节点的数据
		HashMap m_Party = (HashMap) it.next();
		String id = (String)m_Party.get("id");
		String name = (String)m_Party.get("name");
		String code = (String)m_Party.get("code");
		String partyId = (String)m_Party.get("partyid");
		String typeIsLeaf = (String)m_Party.get("type_is_leaf");
		
		//判断节点的数据权限
        int auth = 0; //表示权限的类型：0无权限，1只读，2可写
        for(int i=0; i<dataLimit.length; i++) {
            if(code.length()>dataLimit[i].length()) { //当前节点为授权节点的下级节点
                if(code.substring(0,dataLimit[i].length()).equals(dataLimit[i])) {
                    auth = 2;
                    break;
                }
            }
            else if(code.length()==dataLimit[i].length()){ //当前节点为授权节点的同级节点
                if(code.equals(dataLimit[i])) {
                    auth = 2;
                    break;
                }
            }else { //当前节点为授权节点的上级节点
                if(code.equals(dataLimit[i].substring(0,code.length()))) {
                    auth = 1;
                    continue;
                }
            }
        }
        if(auth==0) {
            continue; //没有权限，直接跳过该节点的构造
        }
		
		//构造节点
		DeepTreeVo dtv = new DeepTreeVo(code, name, "1".equals(typeIsLeaf) ? "0" : "1", 
			request.getContextPath()+"/jsp/authority/au/aurole/xmlData.jsp?parent_code="+code+"&has_checkbox="+has_checkbox+"&partyId="+userPartyId+"&data_limit="+data_limit);
		
		if("1".equals(has_checkbox) || "yes".equals(has_checkbox)) {
			dtv.setReturnValue(id);//设置返回值
			dtv.setDetailedType(partyType);//设置返回类型
			if(roleMap.containsKey(code)) {
			    //dtv.setText(dtv.getText() + "(<fmt:message key='venus.authority.Has_been_associated' bundle='${applicationAuResources}' />)"); //已关联的角色
				//dtv.setIsSubmit("0");
			    dtv.setIsSelected("1");//20100528人寿修改
			}
		}else {
			//设置导航url
			dtv.setHrefPath(request.getContextPath()+"/auRole/detailWithPrivilage?id="+partyId+"&relationId="+id);
			dtv.setTarget("detail");
		}
		
		if(LoginHelper.getPartyId(request).equals(userPartyId)) { //当前用户不可以为自己分配角色
		    dtv.setIsDisable("1");
		} else {
		    dtv.setIsDisable("0");
		}
		
		dt.addTreeNode(dtv);
	} 
    String xmlStr = dt.getStringFromDocument();
    out.print(xmlStr);
} catch(Exception e) {
	e.printStackTrace();
}
%>

