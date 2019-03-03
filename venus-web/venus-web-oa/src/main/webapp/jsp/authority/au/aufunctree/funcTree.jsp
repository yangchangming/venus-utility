<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="venus.authority.au.aufunctree.bs.IAuFunctreeBs"%>
<%@ page import="venus.authority.au.aufunctree.vo.AuFunctreeVo"%>
<%@ page import="venus.authority.au.aufunctree.util.IAuFunctreeConstants"%>
<%@ page import="venus.frames.mainframe.util.Helper"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Iterator"%>
<%
try {
	String call_code = ranmin.project.IToolsConstants.AUTHORIZE_CALL_CODE_FUNCTREE;
	IAuFunctreeBs bs = (IAuFunctreeBs) Helper.getBean(IAuFunctreeConstants.BS_KEY);
	List lTree = bs.queryByCondition("TOTAL_CODE like'101%' and TYPE='0'", "tree_level,order_code");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="<%=request.getContextPath()%>/css/ranmin.css.jsp" rel="stylesheet" type="text/css">
<title><fmt:message key='venus.authority.Function_menu' bundle='${applicationAuResources}' /></title>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" >
	<tr>
		<td valign="top">
			<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td valign="top" bgcolor="#FFFFFF" nowrap>
						<table width="98%" border="0" cellpadding="0" cellspacing="0" align="center">
							<tr>
								<td width="10">&nbsp;</td>
								<td valign="top">&nbsp;</td>
							</tr> 
							<tr>
								<td>&nbsp;</td>
								<td valign="top">
								<script>
									var USETEXTLINKS = 1;
									var STARTALLOPEN = 0;
									classPath = "<%=request.getContextPath()%>/js/newtree/icon";
									ftv2blank = "ftv2blank.gif";
									ftv2doc = "ftv2doc.gif";
									ftv2folderclosed = "ftv2folderclosed.gif";
									ftv2folderopen = "ftv2folderopen.gif";
									ftv2lastnode = "ftv2lastnode.gif";
									ftv2link = "ftv2link.gif";
									ftv2mlastnode = "ftv2mlastnode.gif";
									ftv2mnode = "ftv2mnode.gif";
									ftv2node = "ftv2node.gif";
									ftv2plastnode = "ftv2plastnode.gif";
									ftv2pnode = "ftv2pnode.gif";
									ftv2vertline = "tv2vertline.gif";
									basefrm="detailAuthorize";
									//basefrm = "_blank";
								</script>
								<script src="<%=request.getContextPath() %>/js/newtree/ua.js" type=text/javascript></script>
								<script src="<%=request.getContextPath() %>/js/newtree/ftiens6.js" type=text/javascript></script>
								<script language="javascript">								
									<%
									String htmlCode = "";
									for(Iterator itLTree = lTree.iterator(); itLTree.hasNext(); ) {
								        AuFunctreeVo vo = (AuFunctreeVo) itLTree.next();
								        if(htmlCode.length() == 0) {
								            htmlCode += "funcTree" + vo.getTotal_code() + " = gFld('&nbsp;" + vo.getName() + "','','diffFolder.gif','diffFolder-0.gif');\n";
								            htmlCode += "foldersTree = " + "funcTree" + vo.getTotal_code() + "\n";
								        } else {
								            if("1".equals(vo.getIs_leaf())) {
								                htmlCode += "funcTree" + vo.getTotal_code() + " = insDoc(" + "funcTree" + vo.getParent_code() + ", gLnk('0','&nbsp;" + vo.getName() + "', '" + request.getContextPath()+"/jsp/comm/aufunctree/detailAuthorize.jsp?old_resource_id="+vo.getTotal_code()+"&old_resource_name="+vo.getName()+"&call_code="+call_code + "', 'ftv2link.gif'));\n";
								            } else {
								                htmlCode += "funcTree" + vo.getTotal_code() + " = insFld(" + "funcTree" + vo.getParent_code() + ", gFld ('&nbsp;" + vo.getName() + "', '', 'ftv2folderopen.gif', 'ftv2folderclosed.gif'));\n";
								            }
								        }
								    }
									out.print(htmlCode);
									%>
									try {
										initializeDocument();
									} catch(e) {
									
									}
								</script>
		      					</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</body>
</html>
<%							
	}catch(Exception e) {
		e.printStackTrace();
	}
%>

