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
<%
try {
	//控制树的节点是否全带checkbox（radio），yes——全带，no——只有树的最末尾一层带
	String submit_all = request.getParameter("submit_all");
	//返回值的类型，分id、code和party_id三种
	String return_type = request.getParameter("return_type");
	//返回节点类型的类型
	String node_type = request.getParameter("node_type");
	//是否只展示同一类型的节点
	String same_type = request.getParameter("same_type");
	if(same_type == null) same_type = "0";

	//点击节点要导航到的url
	String url = request.getParameter("url");
	if(url==null) url = "";
	//url的target
	String target = request.getParameter("target");
	if(target==null) target = "";
	
	String strsql = "select id, name from au_partyrelationtype where enable_status='1'";
	List lParty = ProjTools.getCommonBsInstance().doQuery(strsql, new RowMapper() {
            public Object mapRow(ResultSet rs, int no) throws SQLException {
                HashMap map = new HashMap();
				map.put("id",rs.getString("id"));
				map.put("name",rs.getString("name"));
				return map;
            }
        });
	DeepTreeXmlHandler dt = new DeepTreeXmlHandler();
	for(Iterator it = lParty.iterator(); it.hasNext(); ) {
		//取到节点的数据
		HashMap m_Party = (HashMap) it.next();
		String id = (String)m_Party.get("id");
		String name = (String)m_Party.get("name");
		//构造一个节点
		DeepTreeVo dtv = new DeepTreeVo(id, name, "1", 
			request.getContextPath()+"/jsp/authority/tree/treeData.jsp?parent_code="+id
			+"&submit_all="+submit_all+"&return_type="+return_type+"&node_type="+node_type+"&same_type="+same_type+"&url="+url+"&target="+target);
		//设置节点的属性
		dtv.setDefaultOpen("1");
		dtv.setIsSubmit("0");
		//设置导航url
		if(url.length()>0) {
			dtv.setHrefPath(url+"&id="+id);
		}
		if(target.length()>0) {
			dtv.setTarget(target);
		}
		dt.addTreeNode(dtv);
	}
    String xmlStr = dt.getStringFromDocument();
    out.print(xmlStr);
} catch(Exception e) {
	e.printStackTrace();
}
%>

