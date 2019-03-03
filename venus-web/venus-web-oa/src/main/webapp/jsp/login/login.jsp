<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="venus.authority.util.GlobalConstants" %>
<%@ page import="venus.authority.service.sys.vo.SysParamVo" %>
<%@ page import="venus.authority.login.tools.OnLineUser" %>
<%--<%@ taglib uri="/WEB-INF/sslext.tld" prefix="sslext"%>--%>
<%@ include file="/jsp/include/global.jsp" %>

<%
	String isExit = request.getParameter("isExit");//注销标志
	String login_id = (String)session.getAttribute(venus.authority.util.GlobalConstants.getCasFilterUser());
	
	if(login_id!=null) {//如果是通过单点登录过来的
		if("1".equals(isExit)) {
			try{
	            session.invalidate();
	        }catch(Exception e) {
	            //e.printStackTrace();
	        }
			String logoutUrl = venus.authority.util.GlobalConstants.getPortalLogoutUrl();
			response.sendRedirect(logoutUrl);
		}
%>
	<script language="javascript">
		window.location.href="<%=request.getContextPath()%>/login?login_id=<%=login_id%>&isPortalLogin=1";
	</script>
<%
	}
	if("1".equals(isExit)) {//如果是注销
		try{
            OnLineUser.logout(session.getId());
		    session.invalidate();
        }catch(Exception e) {
            //e.printStackTrace();
        }
        response.sendRedirect(request.getContextPath()+"/jsp/login/login.jsp");
	}else{
%>
<logic:notEmpty  name="local_in_sesson_key" scope="session">
    <fmt:setLocale value="${local_in_sesson_key}" scope="session"/>
</logic:notEmpty>
<fmt:setBundle basename="ApplicationResources" scope="application" var="applicationResources"/>
<fmt:setBundle basename="venus.authority.authority_resource" scope="application" var="applicationAuResources"/>
<%
	//校验码
    SysParamVo checkCodeVo = GlobalConstants.getSysParam("CHECKCODE");
    String checkCode = null==checkCodeVo?"":checkCodeVo.getValue();
    
    SysParamVo loginStgVo = GlobalConstants.getSysParam(GlobalConstants.LOGINSTRATEGY);
    String loginStg = null==loginStgVo?"":loginStgVo.getValue();
	
	if(venus.authority.helper.LoginHelper.getLoginId(request)!=null) {//如果是已经登录的
%>
	<script language="javascript">
		window.location.href="<%=request.getContextPath()%>/jsp/main.jsp";//index.jsp";//openNewWindow
	</script>
<%
	}
	String message = (String)request.getAttribute("Message");
	if(message!=null && ! message.equals("")) {
		out.println("<script language='javascript'>alert('"+message+"')</script>");
	}
	String errorType = request.getParameter("errorType");//错误标志
	if("1".equals(errorType)) {
%>
	<script language="javascript">
		alert("<fmt:message key='venus.authority.You_are_logged_in_the_system_or_login_has_timed_out_please_re_login_system_' bundle='${applicationAuResources}' />")
	    var v = window.top.location.href;
	    if(v.indexOf('main.jsp')==-1)
	    {
	     window.top.opener.top.location.href="<%=request.getContextPath()%>/jsp/login/login.jsp";
	    window.top.close();
	    }else{
	     window.top.location.href="<%=request.getContextPath()%>/jsp/login/login.jsp";
	    }
	</script>
<%
	}else if("2".equals(errorType)) {
	    %>
	    <script language="javascript">
	        alert("<fmt:message key='venus.authority.Access_denied_please_contact_the_administrator_' bundle='${applicationAuResources}' />")
	        window.top.location.href="<%=request.getContextPath()%>/jsp/login/login.jsp";
	    </script>
	<%
	}
%>
<title>VENUS <fmt:message key='venus.authority.Organizational_competence_system' bundle='${applicationAuResources}' /></title>
<link href="<venus:base/>/themes/<venus:theme/>/css/authority/login.css" type="text/css" rel="stylesheet" charset='UTF-8'>

<script language="javascript">
<!--
document.onkeydown=function(event) {
    var event = (event) ? event : ((window.event) ? window.event : "")
    var key = event.keyCode ? event.keyCode:event.which;
    if( key == 13 ) {
        //if(!(navigator.userAgent.toLowerCase().match(/chrome\/([\d.]+)/)))// not chrome
        if(onFrmSubmit()){
          document.forms.loginForm.submit();
        }
        //}
    }
}

function onFrmSubmit()
{
	if(loginForm.login_id.value == "") {
		alert("<fmt:message key='venus.authority.Please_enter_a_user_name_' bundle='${applicationAuResources}' />");
		loginForm.login_id.focus();
		return false;
	}
	if(loginForm.password.value == "") {
		alert("<fmt:message key='venus.authority.Please_enter_your_password_' bundle='${applicationAuResources}' />");
		loginForm.password.focus();
		return false;
	}
	return true;
}

function onFrmReset()
{
	loginForm.reset();
}

function register()
{
	var topValue=screen.width/2-400;
	var leftValue=screen.height/2-70;
	window.open("<%=request.getContextPath()%>/jsp/login/register.jsp","newwindow","height=500, width=350, top="+topValue+", left="+leftValue+",toolbar=no,menubar=no,resizable=yes,location=no,status=no");
}

function showHelp()
{
	//window.open("<%=request.getContextPath()%>/htm/index.html");
}

function crml(nid) 
{ 
	try { 
		nid=new ActiveXObject("Agent.Control.2"); 
		nid.Connected = true; 
		nid.Characters.Load(""); 
		return nid;
	} 
	catch (err) { 
		return false; 
	}
}

function chplay () 
{ 
	if (ml=crml ("ml"))
	{ 
		var MerlinID; 
		var MerlinACS; 
		Merlin = ml.Characters.Character(MerlinID); 
		Merlin.MoveTo(700,400);
		Merlin.Show(); 
		Merlin.Play("Explain"); 
		
		Merlin.Speak("<fmt:message key='venus.authority.Welcome_to_the_system_' bundle='${applicationAuResources}' />"); 
		Merlin.Play("Announce"); 
		Merlin.Speak("<fmt:message key='venus.authority.Thank_you_' bundle='${applicationAuResources}' />");
		Merlin.Play("Announce"); 
		Merlin.Speak("<fmt:message key='venus.authority.Wish_you_a_happy_work_' bundle='${applicationAuResources}' />");
		Merlin.Play("Surprised"); 
		
		//Merlin.Hide();
	}
} 
function doLoginSubmit() {
    if(onFrmSubmit()){
        document.forms.loginForm.submit();
    }
}
//-->
</script>
<script>
function freshImage(){
	var t=new Date(); 
	document.getElementById("validateimg").src="<%=request.getContextPath()%>/checkcode/freshImage?time="+t;
}

function enableScript() {
	window.location = "<%=request.getContextPath()%>/jsp/login/download.jsp?type=1";
}

function undoScript() {
	window.location = "<%=request.getContextPath()%>/jsp/login/download.jsp?type=0";
}
</script>
<% 
	SysParamVo loginStrategy = GlobalConstants.getSysParam(GlobalConstants.LOGINSTRATEGY);
	if (loginStrategy != null && "1".equals(loginStrategy.getValue())) {
%>

<object id=locator classid=CLSID:76A64158-CB41-11D1-8B02-00600806D9B6 viewastext></object>
<object id=clientObj classid=CLSID:75718C9A-F029-11d1-A1AC-00C04FB6C223></object>

<script language="javascript">
   var service = locator.ConnectServer();
   var MACAddr ;
   service.Security_.ImpersonationLevel=3;
   service.InstancesOfAsync(clientObj, 'Win32_NetworkAdapterConfiguration');
</script>
<script language="javascript" event="OnCompleted(hResult,pErrorObject, pAsyncContext)" for=clientObj>
	document.forms[0].login_mac.value = unescape(MACAddr);
</script> 
<script language="javascript" event=OnObjectReady(objObject,objAsyncContext) for=clientObj>
   if(objObject.IPEnabled != null && objObject.IPEnabled != undefined && objObject.IPEnabled == true) {
	    if(objObject.MACAddress != null && objObject.MACAddress != "undefined")
	    	MACAddr = objObject.MACAddress;
    }
</script>
<% } %>
</head>

<body oncontextmenu="return false">

<%--<sslext:form name="loginForm" method="post" onsubmit="return onFrmSubmit();" target="_parent" action="/LoginAction.do" >--%>
<form name="loginForm" method="post" onsubmit="return onFrmSubmit();" target="_parent" action="/login" >

<input type="hidden" name="login_mac" value="">
<div  align="center">
    <div class="container">
        <div class="name" align="left">
			<%--<img src="<%=request.getContextPath()%>/themes/<venus:theme/>/images/login/name.png" />--%>
		</div>
        <div class="content">
            <div style=" padding:100px 184px;">
                    <div style="width:52px; float:left; height:44x; line-height:24px;">用户名:</div>
                    <div style=" height:44px;">
                            <input class="in_pass" type="text" style=" height:24px;width:220px; margin-bottom:10px; border:1px solid #bfbfbf;" autocomplete="off" tabindex="1" maxlength="50"  name="login_id" />
                    </div>
                    <div style="width:52px; float:left; height:44px; line-height:24px;">密&nbsp;&nbsp;&nbsp;&nbsp;码:</div>
					<div style=" height:44px;">
					       <input class="in_pass" type="password" style="height:24px; width:220px; margin-bottom:10px; border:1px solid #bfbfbf;" autocomplete="off" tabindex="2" maxlength="50"  name="password" />
					</div>

					<!--启用校验码-->
				    <%if("true".equals(checkCode)){%>
				    <div style="width:52px; float:left; height:44x; line-height:24px;">验证码:</div>
						<div style=" height:44px; ">
						      <div style="float:left;height:45px;padding-left:4px;">
								  <input  type='text' name='j_captcha_response'  maxLength="50" tabindex="3" autocomplete="off">
							  </div>
						      <div>
								  <%--<img style="float:left;height:30px;margin-left:6px;width:80px;" id="validateimg" src="<%=request.getContextPath()%>/jcaptcha" alt="<fmt:message key='venus.authority.Click_Refresh' bundle='${applicationAuResources}' />"  onClick="javascript:freshImage();"  style="cursor:hand"  tabindex="6">--%>
								  <img style="float:left;height:30px;margin-left:6px;width:80px;" id="validateimg" src="<%=request.getContextPath()%>/checkcode/freshImage" alt="<fmt:message key='venus.authority.Click_Refresh' bundle='${applicationAuResources}' />"  onClick="javascript:freshImage();"  style="cursor:hand"  tabindex="6">
							  </div>
						</div>
					<%}%>  

					<!--启用登录互斥-->
				    <%if("1".equals(loginStg)){%>
						<div style="height:35px;padding-left:50px;">
							<img src="<%=request.getContextPath()%>/images/login/script.png" width="15" height="16" />
				        &nbsp;<a href="javascript:enableScript();" title="<fmt:message key='venus.authority.Log_exclusive_feature_is_enabled_on_a_trusted_site_to_allow_the_implementation_of' bundle='${applicationAuResources}' />ActiveX<fmt:message key='venus.authority.Control_Script' bundle='${applicationAuResources}' />"><fmt:message key='venus.authority.Enable_scripting' bundle='${applicationAuResources}' /></a>&nbsp;&nbsp;<a href="javascript:undoScript();" title="<fmt:message key='venus.authority.Disable_log_mutex_function_restoration_of_trusted_sites' bundle='${applicationAuResources}' />ActiveX<fmt:message key='venus.authority.Control_Script' bundle='${applicationAuResources}' />"><fmt:message key='venus.authority.Recovery_script' bundle='${applicationAuResources}' /></a>
				        </div>
					<%}%>  

					<div style="float:left; padding-left:57px;clear:both;">
						<input class="button" type="button" style=" background-image:url(<%=request.getContextPath()%>/themes/<venus:theme/>/images/login/button.gif); background-repeat: no-repeat; height: 23px; width: 72px; color: rgb(255, 255, 255); font-size: 12px; border: 0pt none;" value="登 录" tabindex="3" onClick="javascript:doLoginSubmit();">
						<input class="button" type="reset" style="background-image:url(<%=request.getContextPath()%>/themes/<venus:theme/>/images/login/button.gif); background-repeat: no-repeat; height: 23px; width: 72px; color: rgb(255, 255, 255); font-size: 12px; border: 0pt none;" value="重 置" tabindex="3">
					</div>

                <div style=" clear:both; float:left; font-size:12px; margin-top:30px; color:#999; margin-left:10px;"><fmt:message key='venus.authority.All_rights_reserved' bundle='${applicationAuResources}' /> &copy;2018 <fmt:message key='venus.authority.UF_Software_Engineering_Co_Ltd_GuiZhou' bundle='${applicationAuResources}' /></div>
            </div>
        </div>
        <div class="foot"><img src="<%=request.getContextPath()%>/themes/<venus:theme/>/images/login/login-bgdy.png" /></div>
    </div>
</div>

	<!-- 图片国际化示例 -->
	<%--<img src="<%=request.getContextPath()%>/images/login/ufida_logo1<au:i18next/>.gif" width="291" height="26"/>--%>

</form>
<%--</sslext:form>--%>

<script language="javascript">
    document.loginForm.login_id.focus();
</script>

</body>
</html>
<%} %>
