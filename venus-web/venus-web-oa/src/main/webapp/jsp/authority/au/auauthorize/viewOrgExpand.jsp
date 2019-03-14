<%@ include file="/jsp/authority/tree/include/globalTreeCache.jsp" %>
<%@ page contentType="text/xml;charset=UTF-8" language="java" %>
<%@ page import="venus.oa.util.tree.DeepTreeXmlHandler"%>
<%@ page import="org.springframework.jdbc.core.RowMapper"%>
<%@ page import="java.sql.ResultSet"%>
<%@ page import="java.sql.SQLException"%>
<%@ page import="venus.oa.util.tree.DeepTreeVo"%>
<%@ page import="venus.oa.util.ProjTools"%>
<%@ page import="venus.oa.util.GlobalConstants"%>
<%@ page import="venus.oa.authority.auauthorize.bs.IAuAuthorizeBS"%>
<%@ page import="venus.oa.authority.auauthorize.util.IConstants"%>
<%@ page import="venus.oa.authority.auauthorize.vo.AuAuthorizeVo"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.Set"%>
<%@ page import="java.util.HashSet"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="venus.frames.mainframe.oid.Helper"%>
<%@ page import="venus.springsupport.BeanFactoryHelper" %>

<%
try {
	//父级编码
	String parent_code = request.getParameter("parent_code");
	String vCode = request.getParameter("vCode");
	String isUser = request.getParameter("isUser");
    String partyId = request.getParameter("partyId");

    String rType = GlobalConstants.getResType_orga();//记录
    IAuAuthorizeBS auBs = (IAuAuthorizeBS) BeanFactoryHelper.getBean("auAuthorizeBS");
    //获取拥有权限的节点
    Map allMap = null;
    if ("1".equals(isUser)){
        allMap = auBs.getAuByPartyId(partyId, rType);
    }else{
        allMap = auBs.getAuByVisitorCode(vCode,rType);
    }
    HashMap allCodeMap = new HashMap();
    for(Iterator it=allMap.keySet().iterator(); it.hasNext(); ) {
        AuAuthorizeVo auVo = (AuAuthorizeVo)allMap.get((String)it.next());
        allCodeMap.put(auVo.getResource_code(),"");
    }
    Set allKeySet = allCodeMap.keySet();
    Set allKeySplitSet = new HashSet();
    for(Iterator keyIt=allKeySet.iterator();keyIt.hasNext();){
        String keyCode = (String)keyIt.next();
        String tempCodes[]=ProjTools.splitTreeCode(keyCode);
        for(int i = 0; i < tempCodes.length ; i++){            
            allKeySplitSet.add(tempCodes[i]);
        }
    }
    
    String partyTypeId = "";
    if(parent_code.startsWith(GlobalConstants.getRelaType_role())){
        partyTypeId = GlobalConstants.getPartyType_role();
    }else if(parent_code.startsWith(GlobalConstants.getRelaType_proxy())){
        partyTypeId = GlobalConstants.getPartyType_proxy();
    }
    String strsql = "select * from au_partyrelation where parent_code = '"+parent_code+"' "+("".equals(partyTypeId)?"":("and partytype_id='"+partyTypeId+"' "))+" order by order_code";
    List lParty = ProjTools.getCommonBsInstance().doQuery(strsql, new RowMapper() {
            public Object mapRow(ResultSet rs, int no) throws SQLException {
                HashMap map = new HashMap();
                map.put("id",rs.getString("id"));
				map.put("name",rs.getString("name"));
				map.put("code",rs.getString("code"));
				map.put("parent_code",rs.getString("parent_code"));
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
		String parentCode = (String)m_Party.get("parent_code");
		String isLeaf = (String)m_Party.get("is_leaf");
		String typeIsLeaf = (String)m_Party.get("type_is_leaf");
		String relationType = (String)m_Party.get("relationtype_id");
		String partyType = (String)m_Party.get("partytype_id");
			
		//构造节点
		DeepTreeVo dtv = new DeepTreeVo(code, name, "1".equals(isLeaf) ? "0" : "1", 
			request.getContextPath()+"/jsp/authority/au/auauthorize/viewOrg.jsp?parent_code="+code+"&vCode="+vCode+"&isUser="+isUser+"&partyId="+partyId);
		//勾中已授权的节点
		if(allKeySet.contains(code)) {
			dtv.setIsSelected("1");//自身拥有权限的节点置为选中状态			
		}
		//打开已授权的节点
		if(allKeySplitSet.contains(code)) {
	        dtv.setDefaultOpen("1");//自身拥有权限的节点置为打开状态            
	    }	
		//设置只读
		dtv.setIsDisable("1");
		//设置返回值
		dtv.setReturnValue(code);
		//设置返回类型
		dtv.setDetailedType(partyType);
		if(parent_code.equals(parentCode)){//如果是根节点
            dt.addTreeNode(dtv);
        }else{
            dt.addTreeNode(parentCode,dtv);
        }		
	} 
    String xmlStr = dt.getStringFromDocument();
    out.print(xmlStr);
} catch(Exception e) {
	e.printStackTrace();
}
%>

