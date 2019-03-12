<%@ include file="/jsp/authority/tree/include/globalTreeCache.jsp" %>
<%@ page contentType="text/xml;charset=UTF-8" language="java" %>
<%@ page import="venus.oa.util.tree.DeepTreeXmlHandler"%>
<%@ page import="venus.oa.util.tree.DeepTreeVo"%>
<%@ page import="venus.oa.authority.aufunctree.bs.IAuFunctreeBs"%>
<%@ page import="venus.oa.authority.aufunctree.vo.AuFunctreeVo"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="venus.springsupport.BeanFactoryHelper" %>
<%
try {
	String parent_code = request.getParameter("parent_code");

	IAuFunctreeBs bs = (IAuFunctreeBs) BeanFactoryHelper.getBean("auFunctreeBs");
	String queryCondition = " PARENT_CODE = '"+parent_code+"' and PARENT_CODE<>TOTAL_CODE ";
	List lChild = bs.queryByCondition(queryCondition,"ORDER_CODE");//子节点
	
	DeepTreeXmlHandler dt = new DeepTreeXmlHandler();
	for(Iterator itlChild = lChild.iterator(); itlChild.hasNext(); ) {
		AuFunctreeVo vo = (AuFunctreeVo) itlChild.next();
		DeepTreeVo dtv = new DeepTreeVo(vo.getTotal_code(), vo.getName(), "1".equals(vo.getIs_leaf()) ? "0" : "1", request.getContextPath()+"/jsp/authority/au/aufunctree/xmlData.jsp?parent_code="+vo.getTotal_code());
		dtv.addAttribute("isSubmit", "0");
		dtv.addAttribute("hrefPath",request.getContextPath()+"/auFunctree/detail?id="+vo.getId());
		dtv.addAttribute("target","detailAuFunctree");
		dt.addTreeNode(dtv);
	} 
    String xmlStr = dt.getStringFromDocument();
    out.print(xmlStr);
} catch(Exception e) {
	e.printStackTrace();
}
%>

