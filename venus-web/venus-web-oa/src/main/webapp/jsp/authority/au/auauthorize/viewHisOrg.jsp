<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/jsp/include/global.jsp" %>
<%@ page import="venus.authority.util.VoHelperTools" %>
<%@ page import="venus.authority.au.auauthorize.bs.IAuAuthorizeBS"%>
<%@ page import="venus.authority.au.auauthorize.util.IConstants"%>
<%@ page import="venus.authority.au.auauthorize.vo.AuAuthorizeVo"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.Set"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="venus.frames.mainframe.util.Helper"%>
<%@ page import = "venus.commons.xmlenum.EnumRepository" %>
<%@ page import = "venus.commons.xmlenum.EnumValueMap" %>

<%
 	String vCode = request.getParameter("vCode");
 	String rType = request.getParameter("rType");
	String isUser = request.getParameter("isUser");
    String partyId = request.getParameter("partyId");
     	
 	IAuAuthorizeBS auBs = (IAuAuthorizeBS) Helper.getBean(IConstants.BS_KEY);
 	Map map = new HashMap();
	if ("1".equals(isUser)){ 	
 		map =  auBs.getAuByPartyId(partyId, rType);
 	} else {
 		map =  auBs.getAuByVisitorCode(vCode,rType);
 	}
 	Map codeMap = new HashMap();
    for(Iterator it=map.keySet().iterator(); it.hasNext(); ) {
    	AuAuthorizeVo auVo = (AuAuthorizeVo)map.get((String)it.next());
    	codeMap.put(auVo.getResource_code(),"");
    } 	
   Set keySet = codeMap.keySet();
   
	EnumRepository er = EnumRepository.getInstance();
	er.loadFromDir();
	EnumValueMap typeMap = er.getEnumValueMap("OperateType");   
   
%>
<title><fmt:message key='venus.authority.Query_Template' bundle='${applicationAuResources}' /></title>

<script>
	function tableOnclick() { //点击列表某列时,禁止自动选择
		return false;
	}
</script>
</head>
<body>
<form name="form" method="post">

<div id="auDivParent1"> 
<table class="table_div_control">
	<tr > 
		<td>
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('auDivChild1',this,'<venus:base/>/themes/<venus:theme/>/')">&nbsp;<fmt:message key='venus.authority.Details_Form' bundle='${applicationAuResources}' />
		</td>
	</tr>
</table>
</div>

<div id="auDivChild1"> 
<table class="table_div_content">
	<tr>
		<td>
		<layout:collection name="beans" id="wy1" styleClass="listCss" width="100%" indexId="orderNumber" align="center" sortAction="0" >
			<layout:collectionItem title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Authorize") %>' width="40" style="text-align:center;">
				<bean:define id="wy3" name="wy1" property="source_partyid"/>
				<bean:define id="sourceCode" name="wy1" property="source_code"/>
					<%if(keySet.contains(sourceCode)) {%>√<%}%>
					<input type="checkbox" name="checkbox_template" disabled style="display:none" value="<%=wy3%>"/>
			</layout:collectionItem>
			<%="<a onClick='javascript:tableOnclick();'>"%>
			<layout:collectionItem width="20"  title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Sequence") %>' style="text-align:center;">
				<venus:sequence/>
			</layout:collectionItem>
			<layout:collectionItem  title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Resource_Name") %>' property="source_name" />
			<layout:collectionItem  title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Original_organization") %>' property="source_orgtree" />
			<layout:collectionItem  title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Operation_Name") %>' property="operate_name" />
			<layout:collectionItem  title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Operating_time") %>' property="operate_date" />
			<layout:collectionItem width="60" title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Action_Type") %>' style="text-align:center;">
				<bean:define id="wy5" name="wy1" property="operate_type"/>
			    <%=typeMap.getLabel(wy5.toString())%>
			 </layout:collectionItem>
			 <%="</a>"%>
		</layout:collection>
		<jsp:include page="/jsp/include/page.jsp" />
		</td>
	</tr>
</table>
</div>
</form>
</body>
</html>
<%  //表单回写
	if(request.getAttribute("writeBackFormValues") != null) {
		out.print("<script language=\"javascript\">\n");
		out.print(VoHelperTools.writeBackMapToForm((java.util.Map)request.getAttribute("writeBackFormValues")));
		out.print("writeBackMapToForm();\n");
		out.print("</script>");
	}
%>

