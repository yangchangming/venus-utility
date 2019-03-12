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
<%@ page import="venus.oa.helper.AuHelper"%>
<%
try {
	//标识是做导航还是做选择
	String has_checkbox = request.getParameter("has_checkbox");
	if(has_checkbox == null) has_checkbox = "0";

	String parent_code = request.getParameter("parent_code");//父级编码
	if(parent_code == null) parent_code = GlobalConstants.getRelaType_proxy();
	
	String userPartyId = request.getParameter("partyId");
	if(userPartyId == null) userPartyId = "";
	
	String partyType = GlobalConstants.getPartyType_proxy();
	String withoutAdminStr = LoginHelper.getIsAdmin(request)?"":("' and p.OWNER_ORG='"+LoginHelper.getPartyId(request));
	
	String strsql4root = "select r.id, r.name, r.code, r.partyid, r.type_is_leaf from au_partyrelation r where r.parent_code='"
        + parent_code + "' and r.partytype_id='" + partyType +"' order by order_code";
	String strsql4node = "select r.id, r.name, r.code, r.partyid, r.type_is_leaf,r.order_code from au_partyrelation r,au_party p where r.partyid=p.id and r.parent_code='"
					+ parent_code + "' and r.partytype_id='" + partyType +withoutAdminStr+"' union select r.id, r.name, r.code, r.partyid, r.type_is_leaf,r.order_code from au_partyrelation r where r.partytype_id ='";
	strsql4node+=partyType+"' and r.code <> '"+parent_code+"' ";
	strsql4node=AuHelper.filterOrgPrivInSQL(strsql4node, "r.CODE",request);//控制数据权限;
	strsql4node="select * from ("+strsql4node+") t order by t.order_code";
	String strsql = GlobalConstants.getRelaType_proxy().equals(parent_code)?strsql4root:strsql4node;
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
    
    //查询已关联的代理
    HashMap proxyMap = new HashMap();
    if(userPartyId!=null && userPartyId.length()>0) {
    	strsql = "select parent_code from au_partyrelation where partyid='" + userPartyId + "'";
    	List lProxy = ProjTools.getCommonBsInstance().doQuery(strsql, new RowMapper() {
            public Object mapRow(ResultSet rs, int no) throws SQLException {
				return rs.getString("parent_code");
            }
        });
        if(lProxy!=null && lProxy.size()>0) {
        	for(Iterator it = lProxy.iterator(); it.hasNext(); ) {
        		proxyMap.put((String)it.next(),null);
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
		
		//构造节点
		DeepTreeVo dtv = new DeepTreeVo(code, name, "1".equals(typeIsLeaf) ? "0" : "1", 
			request.getContextPath()+"/jsp/authority/au/auproxy/xmlData.jsp?parent_code="+code+"&has_checkbox="+has_checkbox+"&partyId="+userPartyId);
		
		if("1".equals(has_checkbox) || "yes".equals(has_checkbox)) {
			dtv.setReturnValue(id);//设置返回值
			dtv.setDetailedType(partyType);//设置返回类型
			if(proxyMap.containsKey(code)) {
			    dtv.setText(dtv.getText() + "(<fmt:message key='venus.authority.Has_been_associated' bundle='${applicationAuResources}' />)"); //已关联的代理
				dtv.setIsSubmit("0");
			}
		}else {
			//设置导航url
			dtv.setHrefPath(request.getContextPath()+"/auProxy/detail?id="+partyId+"&relationId="+id);
			dtv.setTarget("detail");
		}
		
		if(LoginHelper.getPartyId(request).equals(userPartyId)) { //当前用户不可以为自己分配代理
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

