<%@ include file="/jsp/authority/tree/include/globalTreeCache.jsp" %>
<%@ page contentType="text/xml;charset=UTF-8" language="java" %>
<%@ page import="venus.authority.util.tree.DeepTreeXmlHandler"%>
<%@ page import="org.springframework.jdbc.core.RowMapper"%>
<%@ page import="java.sql.ResultSet"%>
<%@ page import="java.sql.SQLException"%>
<%@ page import="venus.authority.util.tree.DeepTreeVo"%>
<%@ page import="venus.authority.util.ProjTools"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Iterator"%>
<%
try {
	//父级编码
	String parent_code = request.getParameter("parent_code");
	//是否全部提交
	String submit_all = request.getParameter("submit_all");
	if(submit_all == null) submit_all = "0";
	//返回值类型
	String return_type = request.getParameter("return_type");
	if(return_type == null) return_type = "id";
	//返回节点类型的类型
	String node_type = request.getParameter("node_type");
	if(node_type == null) node_type = "party_type";
	//是否只展示同一类型的节点
	String same_type = request.getParameter("same_type");
	if(same_type == null) same_type = "0";
	//点击节点要导航到的url
	String url = request.getParameter("url");
	if(url==null) url = "";
	//url的target
	String target = request.getParameter("target");
	if(target==null) target = "";

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
	DeepTreeXmlHandler dt = new DeepTreeXmlHandler();
	for(Iterator it = lParty.iterator(); it.hasNext(); ) {
		//获取节点的数据
		HashMap m_Party = (HashMap) it.next();
		String id = (String)m_Party.get("id");
		String name = (String)m_Party.get("name");
		String code = (String)m_Party.get("code");
		String partyId = (String)m_Party.get("partyid");
		String isLeaf = (String)m_Party.get("is_leaf");
		String typeIsLeaf = (String)m_Party.get("type_is_leaf");
		String relationType = (String)m_Party.get("relationtype_id");
		String partyType = (String)m_Party.get("partytype_id");
		
		if("1".equals(same_type) || "yes".equals(same_type)) {
			isLeaf = typeIsLeaf;//只展示同一类型的节点
		}
		//构造节点
		DeepTreeVo dtv = new DeepTreeVo(code, name, "1".equals(isLeaf) ? "0" : "1", 
			request.getContextPath()+"/jsp/authority/tree/treeData.jsp?parent_code="+code
			+"&submit_all="+submit_all+"&return_type="+return_type+"&node_type="+node_type+"&same_type="+same_type+"&url="+url+"&target="+target);
		//设置返回值
		if("id".equals(return_type)) {
			dtv.setReturnValue(id);
		}else if("party_id".equals(return_type)) {
			dtv.setReturnValue(partyId);
		}else if("code".equals(return_type)){
			dtv.setReturnValue(code);
		}
		//返回节点类型
		if("is_leaf".equals(node_type)) {
			dtv.setDetailedType(isLeaf);
		}else if("party_type".equals(node_type)) {
			dtv.setDetailedType(partyType);
		}else if("relation_type".equals(node_type)){
			dtv.setDetailedType(relationType);
		}
		//处理可提交的节点
		if("0".equals(submit_all) || "no".equals(submit_all)) {
			dtv.setIsSubmit(isLeaf);//仅叶子节点可以提交
		}
		//设置导航url
		if(url.length()>0) {
			if("id".equals(return_type)) {
				dtv.setHrefPath(url+"&id="+id+"&type="+partyType+"&partyId="+partyId);
			}else if("party_id".equals(return_type)) {
				dtv.setHrefPath(url+"&id="+partyId+"&type="+partyType+"&relationId="+id);
			}else if("code".equals(return_type)){
				dtv.setHrefPath(url+"&id="+code+"&type="+partyType+"&relationId="+id);
			}
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

