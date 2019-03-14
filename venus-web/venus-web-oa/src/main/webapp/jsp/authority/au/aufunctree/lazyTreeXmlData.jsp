<%@ include file="/jsp/authority/tree/include/globalTreeCache.jsp" %>
<%@ page contentType="text/xml;charset=UTF-8" language="java" %>
<%@ page import="venus.oa.util.tree.DeepTreeXmlHandler"%>
<%@ page import="venus.oa.util.tree.DeepTreeVo"%>
<%@ page import="venus.oa.authority.aufunctree.bs.IAuFunctreeBs"%>
<%@ page import="venus.oa.authority.aufunctree.vo.AuFunctreeVo"%>
<%@ page import="venus.oa.helper.LoginHelper"%>
<%@ page import="venus.frames.mainframe.oid.Helper"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Iterator"%> 
<%@ page import="java.util.Map"%> 
<%@ page import="org.apache.struts.action.SecurePlugIn"%>
<%@ page import="venus.springsupport.BeanFactoryHelper" %>
<%
try {
	String parent_code = request.getParameter("parent_code");

	IAuFunctreeBs bs = (IAuFunctreeBs) BeanFactoryHelper.getBean("auFunctreeBs");
	String queryCondition = " PARENT_CODE = '"+parent_code+"' AND PARENT_CODE<>CODE ";
	List lChild = bs.queryByCondition(queryCondition,"ORDER_CODE");//子节点
	
	SecurePlugIn securePlugIn = (SecurePlugIn) application.getAttribute(SecurePlugIn.SECURE_PLUGIN);
	
	DeepTreeXmlHandler dt = new DeepTreeXmlHandler();
	for(Iterator itlChild = lChild.iterator(); itlChild.hasNext(); ) {
		AuFunctreeVo vo = (AuFunctreeVo) itlChild.next();
		if (!"0".equals(vo.getType())) //过滤非功能菜单，功能菜单的Type为0
			continue;		
		String strUrl = "";
		if ("1".equals(vo.getIs_leaf())) {
			if (vo.getUrl() == null || "".equals(vo.getUrl())) {
				strUrl = request.getContextPath() + "/null";
			} else if (vo.getUrl().indexOf("javascript:") != -1) {
				strUrl = vo.getUrl();
			} else {
				strUrl = request.getContextPath() + vo.getUrl() + ((vo.getUrl().indexOf("?")>0)?"&":"?") +"_function_id_=" + vo.getId();
				if("1".equals(vo.getIs_ssl())){
				    strUrl ="https://"+request.getServerName()+":"+securePlugIn.getHttpsPort()+strUrl+"&jsessionid="+request.getSession().getId();
                }
			}
		}
		DeepTreeVo dtv = new DeepTreeVo(vo.getTotal_code(), vo.getName(), "1".equals(vo.getIs_leaf()) ? "0" : "1", request.getContextPath()+"/jsp/authority/au/aufunctree/lazyTreeXmlData.jsp?parent_code="+vo.getTotal_code());
		dtv.addAttribute("isSubmit", "0");
		//设置菜单链接页面的URL
		dtv.addAttribute("hrefPath",strUrl);
		dtv.addAttribute("thisType","parent");
		dtv.addAttribute("target","contentFrame");
		if(!LoginHelper.getIsAdmin(request)) { //如果不是管理,要进行权限过滤 
			Map map = LoginHelper.getOwnerMenu(request);
			if(map.keySet().contains(vo.getTotal_code())||"1".equals(vo.getIs_public())) {
				dt.addTreeNode(dtv);
			}
		} else {
			dt.addTreeNode(dtv);
		}
	} 
    String xmlStr = dt.getStringFromDocument();
    out.print(xmlStr);
} catch(Exception e) {
	e.printStackTrace();
}
%>

