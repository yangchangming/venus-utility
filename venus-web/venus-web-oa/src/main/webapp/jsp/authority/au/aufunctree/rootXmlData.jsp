<%@ include file="/jsp/authority/tree/include/globalTreeCache.jsp" %>
<%@ page contentType="text/xml;charset=UTF-8" language="java" %>
<%@ page import="venus.oa.util.tree.DeepTreeXmlHandler"%>
<%@ page import="venus.oa.util.tree.DeepTreeVo"%>
<%@ page import="venus.oa.authority.aufunctree.bs.IAuFunctreeBs"%>
<%@ page import="venus.oa.authority.aufunctree.vo.AuFunctreeVo"%>
<%@ page import="java.util.List"%>
<%@ page import="venus.springsupport.BeanFactoryHelper" %>
<%
try {
	String root_code = request.getParameter("root_code");
	
	IAuFunctreeBs bs = (IAuFunctreeBs) BeanFactoryHelper.getBean("auFunctreeBs");
	String queryCondition = " TOTAL_CODE = '"+root_code+"'";
	List lParent = bs.queryByCondition(queryCondition);//根节点
	
	DeepTreeXmlHandler dt = new DeepTreeXmlHandler();
    if(lParent!=null && lParent.size()>0) {
		AuFunctreeVo vo = (AuFunctreeVo) lParent.get(0);
		DeepTreeVo dtv = new DeepTreeVo(vo.getTotal_code(), vo.getName(), "1".equals(vo.getIs_leaf()) ? "0" : "1", request.getContextPath()+"/jsp/authority/au/aufunctree/xmlData.jsp?parent_code="+vo.getTotal_code());
		dtv.setDefaultOpen("1");
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

