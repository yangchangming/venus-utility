<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="venus.oa.authority.aufunctree.bs.IAuFunctreeBs"%>
<%@ page import="venus.oa.authority.aufunctree.vo.AuFunctreeVo"%>
<%@ page import="venus.oa.authority.aufunctree.util.IAuFunctreeConstants"%>
<%@ page import="venus.frames.mainframe.util.Helper"%>
<%@ page import="venus.oa.helper.LoginHelper"%>
<%@ page import="venus.oa.helper.OrgHelper"%>
<%@ page import="venus.oa.login.vo.LoginSessionVo"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="venus.oa.organization.aupartyrelationtype.bs.IAuPartyRelationTypeBS"%>
<%@ page import="venus.oa.organization.aupartyrelation.bs.IAuPartyRelationBs"%>
<%@ page import="venus.oa.organization.aupartyrelation.vo.AuPartyRelationVo"%>
<%@ page import="venus.oa.util.tree.DeepTreeSearch"%>
<%@ page import="org.apache.struts.util.LabelValueBean"%>
<%@ page import="venus.oa.sysparam.vo.SysParamVo"%>
<%@ page import="venus.oa.util.GlobalConstants"%>
<%@ page import="org.apache.struts.action.SecurePlugIn"%>
<%@ page import="venus.springsupport.BeanFactoryHelper" %>
<%
	try {
		IAuFunctreeBs bs = (IAuFunctreeBs) BeanFactoryHelper.getBean("auFunctreeBs");
		List lTree = bs.queryByCondition("TOTAL_CODE like'101%' and TYPE='0'", "tree_level, order_code");
		IAuPartyRelationTypeBS relBS = (IAuPartyRelationTypeBS) BeanFactoryHelper.getBean("auPartyRelationTypeBS");
		List relTypeList = relBS.getPartyAll();
		String relationtype_id= ((LoginSessionVo)LoginHelper.getLoginVo(request)).getRelationtype_id();
		if(relationtype_id==null) {
			relationtype_id = "-1";
		}
		//关系处理
		IAuPartyRelationBs relBs = (IAuPartyRelationBs) BeanFactoryHelper.getBean("auPartyRelationBs");
		String currentCode = ((LoginSessionVo)LoginHelper.getLoginVo(request)).getCurrent_code();
		String currentRelStr = "";
		if(null!=currentCode)
			currentRelStr = OrgHelper.getOrgNameByCode(currentCode,false);
		AuPartyRelationVo queryVo = new AuPartyRelationVo();
        queryVo.setPartyid(((LoginSessionVo)LoginHelper.getLoginVo(request)).getParty_id());
		List relList = relBs.queryAuPartyRelation(queryVo);//已经order by code了,自然order by relationType
		Map relTypeMap = new HashMap();
		Map relTypeNameMap = new HashMap();
		for(int i = 0;i<relList.size();i++){
			AuPartyRelationVo relVo = (AuPartyRelationVo)relList.get(i);
			String relTypeId = relVo.getRelationtype_id();
			if(relTypeId.equals(GlobalConstants.getRelaType_role()))
				continue;//跳过角色关系
			if(relTypeMap.containsKey(relTypeId)){
				((List)relTypeMap.get(relTypeId)).add(relVo);
			}else{
				List al = new ArrayList();
				al.add(relVo);
				relTypeMap.put(relTypeId,al);
			}
		}
		for(int i=0;i<relTypeList.size();i++){
			LabelValueBean tmp = (LabelValueBean) relTypeList.get(i);
			relTypeNameMap.put(tmp.getLabel(),tmp.getValue());
		}
		int selectOpetionSize = relList.size()+relTypeMap.size();
		String selectOpetionMaxLengthStr = "";
		SecurePlugIn securePlugIn = (SecurePlugIn) application.getAttribute(SecurePlugIn.SECURE_PLUGIN);
%>
<%@ include file="include/global.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><fmt:message key='venus.authority.Function_menu' bundle='${applicationAuResources}' /></title>
<style type="text/css">
<!--
a:link {
	color: #000000;
}
a:visited {
	color: #000000;
}
a:Hover {
	text-decoration: underline;
	position: relative;
	color:#000099;
}-->
</style>
<script>
    
	
	var selectOptionLen;
	
	function byteLength(sStr){
		aMatch=sStr.match(/[^\x00-\x80]/g);
		return(sStr.length+(!aMatch?0:aMatch.length));
	}
	function selText(objValue){
		var   strArray=new   Array();   
    	strArray=objValue.split(":");  
    	document.all.relation_id.value=strArray[0];
    	form.submit();
    	//var div = document.getElementById("relDiv"); 
		//div.innerHTML = "<marquee onmouseover=this.stop() onmouseout=this.start() scrollamount=3>"+strArray[1]+"</marquee>"; 
	}
	var   oPopup = window.createPopup();
	function   showSel(file)   
  	{   
		var textContent="";
  		textContent=textContent+"<select id=qswh size=<%=selectOpetionSize%> style='position:absolute;display:true' onchange='parent.selText(this.options[this.options.selectedIndex].value);document.all.qswh.style.display=\"none\";parent.oPopup.hide()'>";
  		<%
  		Iterator entryIt = relTypeMap.entrySet().iterator();   
		while(entryIt.hasNext())   
		{   
			Map.Entry entry = (Map.Entry) entryIt.next();
			String key = (String)entry.getKey();
			List value = (List)entry.getValue();
		%>
		textContent=textContent+"<OPTGROUP label=<%=(String)relTypeNameMap.get(key)%>>";
		<%
			for(int i=0;i<value.size();i++){
				AuPartyRelationVo relationVo = (AuPartyRelationVo)value.get(i);
				String relCode = relationVo.getCode();
				String relName = DeepTreeSearch.getOrgNameByCode(relCode,false);
				relName=relName.replaceAll(";","");
				relName=relName.replaceAll("\n",""); 
				selectOpetionMaxLengthStr = selectOpetionMaxLengthStr.length()>relName.length()?selectOpetionMaxLengthStr:relName;
		%>
		textContent=textContent+"<option value=<%=relationVo.getId()%>:<%=relName%>><%=relName%></option>";
		<%
			}
		%>
		textContent=textContent+"</OPTGROUP>";
		<%
		}
  		%>
  		textContent=textContent+"</select>";
  		oPopup.document.body.innerHTML=textContent;
  		if(window.XMLHttpRequest)
  			oPopup.show(8,window.event.y+10 ,byteLength('<%=selectOpetionMaxLengthStr%>')*6.5+45,16*<%=selectOpetionSize%>+3,document.body);
  		else
  			oPopup.show(8,window.event.y+10 ,byteLength('<%=selectOpetionMaxLengthStr%>')*6.5+68,16*<%=selectOpetionSize%>+3,document.body);
  	}
</script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0"	cellspacing="0" style="padding-left:8px;" class="treebgcolor">
	<tr>
		<td valign="top" height="20">
		<form name="form" method="post" action="<%=request.getContextPath()%>/login" target="_top">
	<%
	SysParamVo chooseAuRel = GlobalConstants.getSysParam(GlobalConstants.CHOOSEAUREL);
	SysParamVo showRelationTypeChoose = GlobalConstants.getSysParam("SHOWRELATIONFLAG");
	if(chooseAuRel!=null&&"true".equals(chooseAuRel.getValue())){
		if(!relTypeMap.isEmpty()){
	%>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td height="25">
					  <input type="hidden" name=relation_id>
					  <div id="outDiv" style="border:1px solid #90b3cf;width:160px">
					  <table border="0" cellpadding="0" cellspacing="0">
					  <tr>
					  <td width="140px" style="BORDER:0px">
					  <div id="relDiv" style="background-color:#FFFFFF">
					  <marquee onmouseover=this.stop() onmouseout=this.start() scrollamount=3><%=currentRelStr%></marquee>
					  </div>
					  </td>
					  <td width="20px" style="BORDER:0px">
					  <input type=button onclick=showSel(true) value="6" style="border:1px solid #90b3cf;background-color:#DADFF4;font-family:Webdings;float:right">
					  </td>
					  </tr>
					  </table>
					  </div>
  					</td>
					</form>
				</tr>
			</table>
	<%}
	}else{%>
	<%if(relTypeList.size()>=Integer.parseInt(null==showRelationTypeChoose?"4":showRelationTypeChoose.getValue())) {%>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td height="25">
						<select name="relationtype_id" style={width:150px} onChange="javascript:form.submit()">
							<option value="-1" <%=relationtype_id.equals("-1")?"selected":""%> ><fmt:message key='venus.authority.Whole' bundle='${applicationAuResources}' /></option>
						<%
						    for(int i=0; i<relTypeList.size(); i++) {
						       	LabelValueBean tmp = (LabelValueBean) relTypeList.get(i);
						%>
							<option value="<%=tmp.getLabel()%>" <%=relationtype_id.equals(tmp.getLabel())?"selected":""%> ><%=tmp.getValue()%></option>
						<%  }
						%>
						</select>
					</td>
				</tr>
			</table>
	<%}	
	}%>
		</form>
		</td>
	</tr>				
	<tr>
		<td valign="top" nowrap>
			<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td valign="top" nowrap>
						<table style="behavior:url('<venus:base/>/js/au/gap-treeColor.htc')" width="100%" border="0" cellpadding="0" cellspacing="0">
							<tr>
								<td valign="top">&nbsp;</td>
							</tr> 
							<tr>
								<td valign="top">
								<script language="javascript">
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
									basefrm="contentFrame";
									//basefrm = "_blank";
								</script>
								<script src="<%=request.getContextPath() %>/js/newtree/ua.js" type=text/javascript></script>
								<script src="<%=request.getContextPath() %>/js/newtree/ftiens6.js" type=text/javascript></script>
								<script language="javascript">								
									<%
									if( ! LoginHelper.getIsAdmin(request)) {
										//过滤权限
										java.util.Map auMap = LoginHelper.getOwnerMenu(request);
										Iterator itLTree = lTree.iterator();
										if(itLTree.hasNext()) {
											itLTree.next();//根节点不过滤权限
										}
										while( itLTree.hasNext() ) {
											AuFunctreeVo vo = (AuFunctreeVo) itLTree.next();
											if(!auMap.keySet().contains(vo.getTotal_code())&&!"1".equals(vo.getIs_public())) {
												itLTree.remove();
											}
										}
									}
									String htmlCode = "";
								    for(Iterator it = lTree.iterator(); it.hasNext(); ) {
								        AuFunctreeVo vo = (AuFunctreeVo) it.next();
								        if(htmlCode.length() == 0) {
								            htmlCode += "folderTree" + vo.getTotal_code() + " = gFld('&nbsp;" + vo.getName() + "','','diffFolder.gif','diffFolder-0.gif');\n";
								            htmlCode += "foldersTree = folderTree" + vo.getTotal_code() + "\n";
								        } else {
								            htmlCode+="var folderTree"+vo.getParent_code()+";\n";//2009-8-31 add 解决is_public 时父节点为undefined问题，有待优化。
								            if("1".equals(vo.getType_is_leaf())) {
								            	String url = "null"; //如果url为空，将url地址指向一个空页面
								            	if (vo.getUrl() != null) {
								            	    url =  request.getContextPath() + vo.getUrl() + ((vo.getUrl().indexOf("?")>0)?"&":"?") +"_function_id_=" + vo.getId();
								            	    if("1".equals(vo.getIs_ssl())){
								            	        url ="https://"+request.getServerName()+":"+securePlugIn.getHttpsPort()+url+"&jsessionid="+request.getSession().getId();
								            	    }
                                                    if(vo.getUrl().indexOf("javascript:") != -1) { //支持点击叶子节点执行js脚本
                                                        url = vo.getUrl();
                                                    }								            	    
								            	}
								            	htmlCode += "folderTree" + vo.getTotal_code() + " = insDoc(" + "folderTree" + vo.getParent_code() + ", gLnk('0','&nbsp;" + vo.getName() + "', '" +url+"', 'ftv2link.gif'));\n";
								            } else {
								                htmlCode += "folderTree" + vo.getTotal_code() + " = insFld(" + "folderTree" + vo.getParent_code() + ", gFld ('&nbsp;" + vo.getName() + "', '', 'ftv2folderopen.gif', 'ftv2folderclosed.gif'));\n";
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

