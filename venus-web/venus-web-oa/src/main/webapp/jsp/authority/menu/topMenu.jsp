<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/global.jsp" %>
<%@ page import="java.util.List" %>
<%@ page import="venus.authority.au.aufunctree.vo.AuFunctreeVo" %>
<%@ page import="venus.authority.helper.LoginHelper" %>
<%@ page import="venus.authority.service.sys.vo.SysParamVo"%>
<%@ page import="venus.authority.util.GlobalConstants"%>
<%@ page import="venus.authority.au.aufunctree.bs.IAuFunctreeBs"%>
<%@ page import="venus.authority.au.aufunctree.util.IAuFunctreeConstants"%>
<%@ page import="venus.frames.mainframe.util.Helper"%>
<%@ page import="venus.authority.helper.OrgHelper"%>
<%@ page import="venus.authority.login.vo.LoginSessionVo"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="venus.authority.org.aupartyrelationtype.bs.IAuPartyRelationTypeBS"%>
<%@ page import="venus.authority.org.aupartyrelation.bs.IAuPartyRelationBs"%>
<%@ page import="venus.authority.org.aupartyrelation.vo.AuPartyRelationVo"%>
<%@ page import="venus.authority.util.tree.DeepTreeSearch"%>
<%@ page import="org.apache.struts.util.LabelValueBean"%>
<%@ page import="org.apache.struts.action.SecurePlugIn"%>
<%@ page import="venus.authority.org.aupartyrelationtype.util.IConstants" %>
<html>
<head>
<title>Top Menu</title>
<base target="funcTreeFrame">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%
	List list = LoginHelper.getFirstLevelMenu(request);
    String totalCode = "";
    if (!list.isEmpty()) {
        AuFunctreeVo fvo = (AuFunctreeVo)list.get(0); 
        totalCode = fvo.getTotal_code();
    } else {
     LoginSessionVo loginSessionVo =(LoginSessionVo)   session.getAttribute("LOGIN_SESSION_VO");
     if(loginSessionVo==null){
         %>
            <script language="javascript">
           alert("<fmt:message key='venus.authority.You_are_logged_in_the_system_or_login_has_timed_out_please_re_login_system_' bundle='${applicationAuResources}' />") 
            window.top.location.href="<%=request.getContextPath()%>/jsp/login/login.jsp";
        </script>  
         <% 
     }else{
		       // session.removeAttribute("LOGIN_SESSION_VO");//session.invalidate();
		%>
		<script language="javascript">
		   alert("<fmt:message key='venus.authority.This_relation_does_not_have_any_rights' bundle='${applicationAuResources}' />") 
		    // window.top.location.href="<%=request.getContextPath()%>/jsp/login/login.jsp";
		</script>
		<%
		 }
    }
	SysParamVo multiTab = GlobalConstants.getSysParam(GlobalConstants.MULTITAB);
%>
<%
    // 关系选择功能
    try {
        IAuFunctreeBs bs = (IAuFunctreeBs) Helper.getBean(IAuFunctreeConstants.BS_KEY);
        List lTree = bs.queryByCondition("TOTAL_CODE like'101%' and TYPE='0'", "tree_level, order_code");
        IAuPartyRelationTypeBS relBS = (IAuPartyRelationTypeBS) Helper.getBean(IConstants.BS_KEY);
        LoginSessionVo sessionVo = LoginHelper.getLoginVo(request);
        List relTypeList = (sessionVo != null) ? relBS.getPartyAll() : new ArrayList();
        String relationtype_id= (sessionVo != null) ? sessionVo.getRelationtype_id() : null;
        if(relationtype_id==null) {
            relationtype_id = "-1";
        }
        //关系处理
        IAuPartyRelationBs relBs = (IAuPartyRelationBs) Helper.getBean(venus.authority.org.aupartyrelation.util.IConstants.BS_KEY);
        String currentCode =  (sessionVo != null) ? sessionVo.getCurrent_code() : null;
        String currentRelStr = "";
        if(null!=currentCode)
            currentRelStr = OrgHelper.getOrgNameByCode(currentCode,false);
        AuPartyRelationVo queryVo = new AuPartyRelationVo();
        queryVo.setPartyid((sessionVo != null) ? sessionVo.getParty_id() : "");
        List relList = (sessionVo != null) ? relBs.queryAuPartyRelation(queryVo) : new ArrayList();//已经order by code了,自然order by relationType
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
        SysParamVo chooseAuRel = GlobalConstants.getSysParam(GlobalConstants.CHOOSEAUREL);
        SysParamVo showRelationTypeChoose = GlobalConstants.getSysParam("SHOWRELATIONFLAG");
%>
<style type="text/css">
<!--

/* topmenu的样式表 */
.topmenu{
    text-align:center;
    cursor:hand;
    cursor:pointer;
    color:#000000;
	padding-top:13px;  
}

.topmenubg{
    text-align:center;
    cursor:hand;
    cursor:pointer;    
    color:#000000;
    padding-top:15px;
    background-image:url("<%=request.getContextPath() %>/images/au/sel_bg.gif");
}
-->
</style>
<script>
var multiTab = '<%= (multiTab == null) ? "" : multiTab.getValue() %>';

function getEventObj(event) {
	if (event == undefined || event == null ) {
		alert("current object is null!");
		return null;
	} else {
		return event.srcElement ? event.srcElement : event.target;
	}
}

function doMouseOver(event) {  //处理鼠标进入事件
	var thisObj = getEventObj(event);
	if (jQuery(thisObj).attr("nodeName") == "IMG" || jQuery(thisObj).attr("nodeName") == "img") 
	   thisObj = thisObj.parentElement ? thisObj.parentElement : thisObj.parentNode;
	if (jQuery(thisObj).attr("pcname") != "doclick"){
		jQuery(thisObj).attr("class","topmenubg");
	}
}

function doMouseOut(event) {  //处理鼠标离开事件
	var thisSpan = window.document.getElementsByTagName("span");
	for (var i=0; i<thisSpan.length; i++) {
		if (jQuery(thisSpan[i]).attr("name") == "undefined") continue;
		if (jQuery(thisSpan[i]).attr("name") == "topmenu" && jQuery(thisSpan[i]).attr("pcname") != "doclick") {
			jQuery(thisSpan[i]).attr("class","topmenu");
		}
	}
}

function initMenu(){
    submitform.action = "<%=request.getContextPath()%>/jsp/authority/menu/leftMenu.jsp?totalCode="+'<%=totalCode%>';
    submitform.target = "funcTreeFrame";
    submitform.submit();
}

function doclick(totalCode,event) {
    submitform.action = "<%=request.getContextPath()%>/jsp/authority/menu/leftMenu.jsp?totalCode="+totalCode;
    var thisObj = getEventObj(event);
    if(jQuery(thisObj).attr("nodeName") == "IMG" || jQuery(thisObj).attr("nodeName") == "img") 
        thisObj = thisObj.parentElement ? thisObj.parentElement : thisObj.parentNode;
    jQuery(thisObj).attr("class","topmenubg");
    jQuery(thisObj).attr("pcname","doclick");
    var thisSpan = window.document.getElementsByTagName("span");
    for (var i=0; i< thisSpan.length; i++) {
        if (jQuery(thisSpan[i]).attr("name") == "undefined") continue;
        if (jQuery(thisSpan[i]).attr("name") == "topmenu" && thisSpan[i].id != thisObj.id) {
            jQuery(thisSpan[i]).attr("class","topmenu");
            jQuery(thisSpan[i]).attr("pcname","");
        }
    }
    event.cancelBubble = true;
    submitform.target = "funcTreeFrame";
    submitform.submit();
}

function linkPage(title,url,event) {
    if (url != "" && url != undefined) {
        if (multiTab != null && multiTab > 0) 
            top.document.getElementById("contentFrame").contentWindow.addTab(title,url);
        else
            top.contentFrame.location = "<%=request.getContextPath()%>" + url;
    }
    var thisObj = getEventObj(event);
    if(jQuery(thisObj).attr("nodeName") == "IMG" || jQuery(thisObj).attr("nodeName") == "img") 
        thisObj = thisObj.parentElement ? thisObj.parentElement : thisObj.parentNode;
    jQuery(thisObj).attr("class","topmenubg");
    jQuery(thisObj).attr("pcname","doclick");
    var thisSpan = window.document.getElementsByTagName("span");
    for (var i=0; i< thisSpan.length; i++) {
        if (jQuery(thisSpan[i]).attr("name") == "undefined") continue;
        if (jQuery(thisSpan[i]).attr("name") == "topmenu" && thisSpan[i].id != thisObj.id) {
            jQuery(thisSpan[i]).attr("class","topmenu");
            jQuery(thisSpan[i]).attr("pcname","");
        }
    }        
}
</script>
<script>
<!-- 关系选择功能js -->

    var selectOptionLen;
    
    function byteLength(sStr){
        aMatch=sStr.match(/[^\x00-\x80]/g);
        return(sStr.length+(!aMatch?0:aMatch.length));
    }
    
    function getEvent() {//同时兼容ie和ff的写法                                                                                                 
       if(document.all) return window.event;                                                                                                    
       func=getEvent.caller;                                                                                                                    
       while(func!=null){                                                                                                                       
       var arg0=func.arguments[0];                                                                                                              
       if(arg0){                                                                                                                                        
         if((arg0.constructor==Event || arg0.constructor ==MouseEvent) || (typeof(arg0)=="object" && arg0.preventDefault && arg0.stopPropagation)){                                                                                                                                        
           return arg0;                                                                                                                             
         }                                                                                                                                        
       }                                                                                                                                        
       func=func.caller;                                                                                                                        
       }                                                                                                                                        
     return null;                                                                                                                             
    }    
    
    function selText(objValue){
        var   strArray=new   Array();   
        strArray=objValue.split(":");  
        document.all.relation_id.value=strArray[0];
        form.submit();
    }
   
    function showDialog(url,x,y){
       var dialogHeight = "<%=(relList.size()*24+35)%>";
       if(document.all) //IE   
       {   
           feature="dialogTop:"+y+";dialogLeft:"+x+";dialogWidth:400px;dialogHeight:"+dialogHeight+"px;status:no;help:no";   
           window.showModalDialog(url,selText,feature);   
       }   
       else   
       {   
           feature ="top="+y+"px,left="+x+"px,width=400px,height="+dialogHeight+"px,menubar=no,toolbar=no,location=no,scrollbars=yes,status=no,modal=yes";
           window.open(url,"chooseRalation",feature);
       }   
    }   
   
    function   showSel(file)   
    {   
        var mouseLocation = getEvent();
        showDialog("chooseRelation.jsp",mouseLocation.screenX,mouseLocation.screenY);
    }
</script>
</head>
<body bgcolor="#111111" onload="initMenu()">
<table width="100%" height="39px" border="0" cellspacing="0" cellpadding="0" background="<%=request.getContextPath() %>/images/au/nav_bg.png">
        <tr>
            <td width="20px">&nbsp;</td>
			<td>
				<table width="800px" height="39px" border="0" cellspacing="0" cellpadding="0">
					<form name="submitform" method="post">
						<tr>
						<%
							for(int i = 0; i < list.size(); i++) {
							    AuFunctreeVo vo = (AuFunctreeVo)list.get(i);
						%>
						<td><img src="<%=request.getContextPath() %>/images/au/leftmenu_point.gif" width="2px" height="39px"></td>
						<td align="center">         
						  <span id="topmenu_<%=vo.getTotal_code() %>" name="topmenu" <% if(i == 0) { %>class="topmenubg" pcname="doclick" <%} else { %> class="topmenu" pcname="" <%} %> onMouseOver="doMouseOver(event)" onMouseOut="doMouseOut(event)"  onclick=<%if ("1".equals(vo.getIs_leaf())) {%>"linkPage('<%=vo.getName() %>','<%=vo.getUrl() %>',event);" <% } else { %>"doclick('<%=vo.getTotal_code()%>',event);" <%} %>><img src="<%=request.getContextPath() %>/images/au/leftmenu_point.gif" border="0" width="16" height="16" align="absmiddle" onMouseOver="doMouseOver(event)" onMouseOut="doMouseOut(event);">&nbsp;&nbsp;<%=vo.getName()%>&nbsp;</span>
						</td>
						<%}%>
						</tr>
					</form>
				</table>
   			</td>
        </tr>
</table>
<table width="100%" height="30px" border="0" cellpadding="0" cellspacing="0"  bgcolor="#F5f5f5">
	<tr>
        <td width="70%" height="30px">
           &nbsp;&nbsp;&nbsp;<img src="<%=request.getContextPath() %>/images/au/door_in.png" align="absmiddle">&nbsp;&nbsp;&nbsp;<span id="menuPath"  style="color:#000000;"></span>
        </td>
        <td align="right">&nbsp;&nbsp;&nbsp;<img src="<%=request.getContextPath() %>/images/au/ren.gif" width="16" height="16" align="absmiddle">&nbsp;&nbsp;<fmt:message key='venus.authority.welcome' bundle='${applicationAuResources}' />, <%=LoginHelper.getLoginName(request) %>&nbsp;&nbsp;&nbsp;&nbsp;</td>
        <td align="right">
        <%if(chooseAuRel!=null&&"true".equals(chooseAuRel.getValue()) &&!LoginHelper.getIsAdmin(request)){ %>
        <fmt:message key='venus.authority.currentrelation' bundle='${applicationAuResources}' />：&nbsp;
        <%} %>
        </td>
        <td align="right">
        <form name="form" method="post" action="<%=request.getContextPath()%>/login" target="_top">
        <%
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
                      <input type=button onclick=showSel(true) value="&nabla;" style="border:1px solid #90b3cf;background-color:#DADFF4;font-family:Webdings;float:right">
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
<script>
    //根据浏览器设置菜单样式
    var thisSpan = window.document.getElementsByTagName("span");
    for (var i=0; i< thisSpan.length; i++) {
        if (jQuery(thisSpan[i]).attr("name") == "undefined") continue;
        if (jQuery(thisSpan[i]).attr("name") == "topmenu") {
            if (jQuery.browser.msie) {
                jQuery(thisSpan[i]).css({display:"inline-block",height:"39px",width:"110px"});
            } else { 
                jQuery(thisSpan[i]).css({display:"inline-block",height:"24px",width:"110px"});
            }
        }
    }  
</script>