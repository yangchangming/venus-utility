<%@ include file="/jsp/authority/tree/include/globalTreeCache.jsp" %>
<%@ page contentType="text/xml;charset=UTF-8" language="java" %>
<%@ page import="venus.authority.util.tree.DeepTreeXmlHandler"%>
<%@ page import="org.springframework.jdbc.core.RowMapper"%>
<%@ page import="java.sql.ResultSet"%>
<%@ page import="java.sql.SQLException"%>
<%@ page import="venus.authority.util.tree.DeepTreeVo"%>
<%@ page import="venus.authority.util.ProjTools"%>
<%@ page import="venus.authority.util.GlobalConstants"%>
<%@ page import="venus.authority.au.appenddata.bs.IAppendDataBs"%>
<%@ page import="venus.authority.au.appenddata.vo.AuAppendVo"%>
<%@ page import="venus.authority.au.auauthorize.bs.IAuAuthorizeBS"%>
<%@ page import="venus.authority.au.auauthorize.vo.AuAuthorizeVo"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.Set"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="venus.frames.mainframe.util.Helper"%>
<%@ page import="venus.authority.au.auauthorize.util.IConstants" %>
<%@ page import="venus.authority.au.appenddata.util.IConstantsimplements" %>

<%
try {
	//父级编码
	String parent_code = request.getParameter("parent_code");
	String vCode = request.getParameter("vCode");
	String isUser = request.getParameter("isUser");
    String partyId = request.getParameter("partyId");
    String authorizeId = request.getParameter("resource_id");
    IAuAuthorizeBS auAuthorizeBs = (IAuAuthorizeBS) Helper.getBean(IConstants.BS_KEY);
	AuAuthorizeVo authorizeVo=auAuthorizeBs.find(authorizeId);
	String strsql = "select * from au_partyrelation where parent_code='"+parent_code+"' order by order_code";
	List lParty = ProjTools.getCommonBsInstance().doQuery(strsql, new RowMapper() {
            public Object mapRow(ResultSet rs, int no) throws SQLException {
                HashMap map = new HashMap();
                map.put("id",rs.getString("id"));
				map.put("name",rs.getString("name"));
				map.put("code",rs.getString("code"));
				map.put("partyid",rs.getString("partyid"));
				map.put("is_leaf",rs.getString("is_leaf"));
				map.put("type_is_leaf",rs.getString("type_is_leaf"));
				map.put("relationtype_id",rs.getString("relationtype_id"));
				map.put("partytype_id",rs.getString("partytype_id"));
				return map;
            }
        });
        
	IAppendDataBs auBs = (IAppendDataBs) Helper.getBean(IConstantsimplements.BS_KEY);
    
	DeepTreeXmlHandler dt = new DeepTreeXmlHandler();
	for(Iterator it = lParty.iterator(); it.hasNext(); ) {
		//获取节点的数据
		HashMap m_Party = (HashMap) it.next();
		String id = (String)m_Party.get("id");
		String name = (String)m_Party.get("name");
		String code = (String)m_Party.get("code");
		String isLeaf = (String)m_Party.get("is_leaf");
		String typeIsLeaf = (String)m_Party.get("type_is_leaf");
		String relationType = (String)m_Party.get("relationtype_id");
		String partyType = (String)m_Party.get("partytype_id");
			
		//构造节点
		DeepTreeVo dtv = new DeepTreeVo(code, name, "1".equals(isLeaf) ? "0" : "1", 
			request.getContextPath()+"/jsp/authority/au/auauthorize/viewFuncOrg.jsp?parent_code="+code+"&vCode="+vCode+"&isUser="+isUser+"&partyId="+partyId+"&resource_id="+authorizeId);
		//获取拥有权限的节点	 
		Map allMap = null;
    	if("1".equals(isUser))
			allMap = auBs.getExtendAppendAuByPartyId(partyId,null==authorizeVo?null:authorizeVo.getResource_id(), code);
		else
			allMap = auBs.getExtendAppendAuByVisitorCode(new String[]{vCode}, null==authorizeVo?null:authorizeVo.getResource_id(), code);
   	    HashMap allCodeMap = new HashMap();
	    for(Iterator extIt=allMap.keySet().iterator(); extIt.hasNext(); ) {
	   		AuAppendVo[] auVo = (AuAppendVo[])allMap.get((String)extIt.next());
	    	for(int i=0;i<auVo.length;i++)
		    	allCodeMap.put(auVo[i].getAppend_value(),"");
	    }
	    Set allKeySet = allCodeMap.keySet();
		//勾中已授权的节点
		if(allKeySet.contains(code)) {
			dtv.setIsSelected("1");//自身拥有权限的节点置为选中状态
		}
		//设置只读
		dtv.setIsDisable("1");
		//设置返回值
		dtv.setReturnValue(code);
		//设置返回类型
		dtv.setDetailedType(partyType);
		
		dt.addTreeNode(dtv);
	} 
    String xmlStr = dt.getStringFromDocument();
    out.print(xmlStr);
} catch(Exception e) {
	e.printStackTrace();
}
%>

