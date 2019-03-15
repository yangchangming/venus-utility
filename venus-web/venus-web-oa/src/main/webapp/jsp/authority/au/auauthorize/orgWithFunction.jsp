<%@ include file="/jsp/authority/tree/include/globalTreeCache.jsp" %>
<%@ page contentType="text/xml;charset=UTF-8" language="java" %>
<%@ page import="venus.oa.util.tree.DeepTreeXmlHandler"%>
<%@ page import="org.springframework.jdbc.core.RowMapper"%>
<%@ page import="java.sql.ResultSet"%>
<%@ page import="java.sql.SQLException"%>
<%@ page import="venus.oa.util.tree.DeepTreeVo"%>
<%@ page import="venus.oa.util.ProjTools"%>
<%@ page import="venus.oa.util.GlobalConstants"%>
<%@ page import="venus.oa.authority.appenddata.bs.IAppendDataBs"%>
<%@ page import="venus.oa.authority.appenddata.vo.AuAppendVo"%>
<%@ page import="venus.oa.authority.auauthorize.bs.IAuAuthorizeBS"%>
<%@ page import="venus.oa.authority.auauthorize.vo.AuAuthorizeVo"%>
<%@ page import="venus.oa.authority.auvisitor.bs.IAuVisitorBS"%>
<%@ page import="venus.oa.authority.auvisitor.vo.AuVisitorVo"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.Set"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="venus.frames.mainframe.util.Helper"%>
<%@ page import="venus.oa.helper.LoginHelper"%>
<%@ page import="venus.oa.authority.appenddata.util.IConstantsimplements" %>
<%@ page import="venus.springsupport.BeanFactoryHelper" %>
<%@ page import="org.springframework.context.annotation.Bean" %>
<%
try {
	//父级编码
	String parent_code = request.getParameter("parent_code");
	String relId = request.getParameter("relId");
	String pType = request.getParameter("pType");
	String authorizeId = request.getParameter("resource_id");
	IAuAuthorizeBS auAuthorizeBs = (IAuAuthorizeBS) BeanFactoryHelper.getBean("auAuthorizeBS");
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
	IAuVisitorBS visiBs = (IAuVisitorBS) BeanFactoryHelper.getBean("auVisitorBS");
    AuVisitorVo visiVo = visiBs.queryByRelationId(relId, pType);
    
	String rType = GlobalConstants.getResType_orga();//记录
	IAppendDataBs auBs = (IAppendDataBs) BeanFactoryHelper.getBean("appendDataBs");
	//获取该访问者自身拥有权限的节点
    Map selIdMap = auBs.getAppendByAuthorizeId(authorizeId);
    HashMap selCodeMap = new HashMap();
    for(Iterator it=selIdMap.keySet().iterator(); it.hasNext(); ) {
    	AuAppendVo auVo = (AuAppendVo)selIdMap.get((String)it.next());
    	if(null==selCodeMap.get(auVo.getAppend_value()))
    		selCodeMap.put(auVo.getAppend_value(),"");
    }
    Set selSet = selCodeMap.keySet();
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
		
		//取当前操作者的数据权限，实现分级授权
		int authorize = 0; //表示：0不显示，1显示但不可操作，2可操作
		String[] data = null;
		if(LoginHelper.getIsAdmin(request)) {//超级管理员
			authorize = 2;
		}else {
			data = LoginHelper.getOwnerOrgWithoutHistory(request);
			if( data!=null) {
				for(int i=0; i<data.length; i++) {
					if(code.length()>data[i].length()) {
						if(code.substring(0,data[i].length()).equals(data[i])) {
							authorize = 2;
							break;
						}
					}
					else if(code.length()==data[i].length()){
						if(code.equals(data[i])) {
							authorize = 2;
							break;
						}
					}else {
						if(code.equals(data[i].substring(0,code.length()))) {
							authorize = 1;
							continue;
						}
					}
				}
			}
		}
		if(authorize==0) {
			continue; 
		}
				
		//构造节点
		DeepTreeVo dtv = new DeepTreeVo(code, name, "1".equals(isLeaf) ? "0" : "1", 
			request.getContextPath()+"/jsp/authority/au/auauthorize/orgWithFunction.jsp?parent_code="+code+"&relId="+relId+"&pType="+pType+"&resource_id="+authorizeId);
		
		//获取该访问者继承权限的节点
	    Map extIdMap = auBs.getExtendAppendAuByVisitorCode(new String[]{visiVo.getCode()}, null==authorizeVo?null:authorizeVo.getResource_id(), code);
	    HashMap extCodeMap = new HashMap();
	    for(Iterator extIt=extIdMap.keySet().iterator(); extIt.hasNext(); ) {
	    	AuAppendVo[] auVo = (AuAppendVo[])extIdMap.get((String)extIt.next());
	    	for(int i=0;i<auVo.length;i++)
		    	extCodeMap.put(auVo[i].getAppend_value(),"");
	    }
	    Set extSet = extCodeMap.keySet();
		//勾中已授权的节点
		if(extSet.contains(code)) {
			dtv.setIndeterminate("1");//因继承而拥有权限的节点置为不确定状态
		}
		if(selSet.contains(code)) {
			dtv.setIsSelected("1");//自身拥有权限的节点置为选中状态
		}

		if(authorize < 2) { //判断当前操作者是否有权限授权
			dtv.setIsSubmit("0");
		}
		
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

