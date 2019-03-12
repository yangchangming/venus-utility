<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="venus.oa.organization.aupartyrelationtype.vo.AuPartyRelationTypeVo"%>
<%@ page import="venus.oa.organization.aupartyrelationtype.util.IConstants"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Iterator"%>
<%
	try {
		List lTree = (List)request.getAttribute(IConstants.REQUEST_BEAN_VALUE);  //从request中取出list
%>
<html>
<head>
<%@ include file="/jsp/include/global.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><fmt:message key='venus.authority.Function_menu' bundle='${applicationAuResources}' /></title>
</head>
<body>
<table width="175" height="100%" border="0" cellpadding="0"	cellspacing="0">
	<tr>
		<td valign="top" background="<%=request.getContextPath()%>/images/tree_bg.gif" nowrap>
			<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#AFAFAF">
				<tr>
					<td valign="top" bgcolor="#F1F8FB" nowrap>
						<table style="behavior:url('<venus:base/>/js/au/gap-treeColor.htc')" width="100%" border="0" cellpadding="0" cellspacing="0">
							<tr>
								<td valign="top">&nbsp;</td>
							</tr> 
							<tr>
								<td valign="top">
								<script>
									var USETEXTLINKS = 1;
									var STARTALLOPEN = 0;
									classPath = "<%=request.getContextPath()%>/js/newtree/icon/";
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
									basefrm="detail";
									//basefrm = "_blank";
								</script>
								<script src="<%=request.getContextPath() %>/js/newtree/ua.js" type=text/javascript></script>
								<script src="<%=request.getContextPath() %>/js/newtree/ftiens6.js" type=text/javascript></script>
								<script language="javascript">								
									<%
									String htmlCode = "folderTree0 = gFld('&nbsp;"+venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Relationship_as_type_list")+"','','diffFolder.gif','diffFolder-0.gif');\n";
								    htmlCode += "foldersTree = folderTree0\n";
								    htmlCode += "folderTree3 = insFld(folderTree0, gFld('&nbsp;"+venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Agency")+"', '', 'ftv2folderopen.gif', 'ftv2folderclosed.gif'));\n";
								    htmlCode += "folderTree2 = insFld(folderTree0, gFld('&nbsp;"+venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Role")+"', '', 'ftv2folderopen.gif', 'ftv2folderclosed.gif'));\n";
								    htmlCode += "folderTree4 = insFld(folderTree0, gFld('&nbsp;"+venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Other")+"', '', 'ftv2folderopen.gif', 'ftv2folderclosed.gif'));\n";
								    for(Iterator it = lTree.iterator(); it.hasNext(); ) {
								        AuPartyRelationTypeVo vo = (AuPartyRelationTypeVo) it.next();
							            htmlCode += "folderTree" + vo.getId() + " = insDoc(folderTree" + vo.getKeyword() + ", gLnk('0','&nbsp;" + vo.getName() + "', '" + request.getContextPath() + "/auPartyRelation/hasRoot?partyrelationtype_id="+vo.getId()+"', 'ftv2link.gif'));\n";
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

