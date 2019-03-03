<%@ include file="/jsp/authority/tree/include/globalTreeCache.jsp" %>
<%@ page contentType="text/xml;charset=UTF-8" language="java" %>
<%@ page import="venus.authority.util.tree.DeepTreeXmlHandler"%>
<%@ page import="org.springframework.jdbc.core.RowMapper"%>
<%@ page import="java.sql.ResultSet"%>
<%@ page import="java.sql.SQLException"%>
<%@ page import="venus.authority.util.tree.DeepTreeVo"%>
<%@ page import="venus.authority.util.ProjTools"%>
<%@ page import="venus.authority.util.StringHelperTools"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Iterator"%>
<%
try {
	//获得partyrelation的id
	String relation_id = request.getParameter("id");
	//获得party的id
	String party_id = request.getParameter("party_id");
	
	//控制树的节点是否全带checkbox（radio），yes——全带，no——只有树的最末尾一层带
	String submit_all = request.getParameter("submit_all");
	//返回值的类型，分id、code和party_id三种
	String return_type = request.getParameter("return_type");
	//返回节点类型的类型
	String node_type = request.getParameter("node_type");
	//点击节点要导航到的url
	String url = request.getParameter("url");
	if(url==null) url = "";
	//url的target
	String target = request.getParameter("target");
	if(target==null) target = "";
	
	String strsql = "";
	if(relation_id!=null && relation_id.length()>0) { //如果传入的是partyrelationid
		strsql = "select code from au_partyrelation where id='"+relation_id+"'";
	}else { //如果传入的是partyid
		strsql = "select code from au_partyrelation where partyid='"+party_id+"'";
	}
	List lRelType = ProjTools.getCommonBsInstance().doQuery(strsql, new RowMapper() {
        public Object mapRow(ResultSet rs, int no) throws SQLException {
            HashMap map = new HashMap();
			map.put("code",rs.getString("code"));
			return map;
        }
    });
        
    DeepTreeXmlHandler dt = new DeepTreeXmlHandler();
    
	if(lRelType==null || lRelType.size()==0) {
		DeepTreeVo dtv = new DeepTreeVo("1", venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Does_not_have_a_relationship"), "1", "");
		dt.addTreeNode(dtv);
	}else {
		HashMap m_treeNode = new HashMap();
		for(int i=0; i<lRelType.size(); i++) {
			String codeData=(String)((HashMap)lRelType.get(i)).get("code");
			String tempCodes[]=ProjTools.splitTreeCode(codeData);
			
			//查询所属团体关系类型
			//strsql = "select id, name from au_partyrelationtype where enable_status='1' and id='"+tempCodes[tempCodes.length-1]+"'";
			//lRelType = ProjTools.getCommonBsInstance().doQuery(strsql, new RowMapper() {
		    //        public Object mapRow(ResultSet rs, int no) throws SQLException {
		    //            HashMap map = new HashMap();
			//			map.put("id",rs.getString("id"));
			//			map.put("name",rs.getString("name"));
			//			return map;
		    //        }
		    //    });
		    //HashMap m_relType = (HashMap)lRelType.get(0);
			//构造一个节点
			//DeepTreeVo rel_dtv = new DeepTreeVo((String)m_relType.get("id"), (String)m_relType.get("name"), "1", "");
			//rel_dtv.setDefaultOpen("1");
			//rel_dtv.setIsSubmit("0");
			//dt.addTreeNode(rel_dtv);
			
			//查询所有上级节点
			strsql = "select id,name,parent_code,code,partyid,is_leaf from au_partyrelation where code in("
				+StringHelperTools.parseToSQLStringComma(tempCodes)+") order by code";
			List lParty = ProjTools.getCommonBsInstance().doQuery(strsql, new RowMapper() {
	            public Object mapRow(ResultSet rs, int no) throws SQLException {
	                HashMap map = new HashMap();
	                map.put("id",rs.getString("id"));
					map.put("name",rs.getString("name"));
					map.put("parent_code",rs.getString("parent_code"));
					map.put("code",rs.getString("code"));
					map.put("partyid",rs.getString("partyid"));
					map.put("is_leaf",rs.getString("is_leaf"));
					return map;
	            }
	        });
			for(Iterator it = lParty.iterator(); it.hasNext(); ) {
				//取到节点的数据
				HashMap m_Party = (HashMap) it.next();
				String id = (String)m_Party.get("id");
				String name = (String)m_Party.get("name");
				String parent_code = (String)m_Party.get("parent_code");
				String code = (String)m_Party.get("code");
				String partyId = (String)m_Party.get("partyid");
				String is_leaf = (String)m_Party.get("is_leaf");
				
				if( ! m_treeNode.keySet().contains(code)) {
					//构造一个节点
					DeepTreeVo dtv = null;
					if(code.equals(codeData)) {
					    dtv = new DeepTreeVo(code, name, "1".equals(is_leaf) ? "0" : "1", 
						request.getContextPath()+"/jsp/authority/tree/treeData.jsp?parent_code="+code
						+"&submit_all="+submit_all+"&return_type="+return_type+"&node_type="+node_type+"&url="+url+"&target="+target);
					}else {
					    dtv = new DeepTreeVo(code, name, "1", "");
					}
					m_treeNode.put(code,null);
				
					//设置返回值
					if("id".equals(return_type)) {
						dtv.setReturnValue(id);
					}else if("party_id".equals(return_type)) {
						dtv.setReturnValue(partyId);
					}else if("code".equals(return_type)){
						dtv.setReturnValue(code);
					}
					dtv.setDefaultOpen("1");
					//设置导航url
					if(url.length()>0) {
						dtv.setHrefPath(url+"&id="+id);
					}
					if(target.length()>0) {
						dtv.setTarget(target);
					}
					if(parent_code.equals(tempCodes[tempCodes.length-1])){//如果是根节点
						dt.addTreeNode(dtv);
					}else{
						dt.addTreeNode(parent_code,dtv);
					}
				}
			}
		}
	}
	//生成xml
    String xmlStr = dt.getStringFromDocument();
	out.print(xmlStr);
} catch(Exception e) {
	e.printStackTrace();
}
%>

