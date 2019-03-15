<%@ page import="venus.oa.sysparam.vo.SysParamVo" %>
<%@ page import="venus.oa.util.GlobalConstants" %>
<%
	SysParamVo organizeTooltip = GlobalConstants.getSysParam(GlobalConstants.ORGANIZETOOLTIP);
%>
<style type="text/css">
	#massage_box{ 
		position:absolute; 
		left:expression(body.scrollWidth/3); 
		top:expression(body.scrollHeight/3); 
		width:expression(body.clientWidth/2); 
		height:expression(body.clientHeight/5); 
		filter:dropshadow(color=#666666,offx=3,offy=3,positive=2);
		 z-index:2; 
		 visibility:hidden
	}
	.massage_content{
		border:#206BA4 solid; 
		border-width:1 1 3 1;
		width:95%; 
		height:95%;
		background:#fff; 
		color:#000; 
		font-size:12px; 
		line-height:150%
	}
	.message_header{
		background:#B9DEF7; 
		height:10%; 
		font-family:Verdana, Arial, Helvetica, sans-serif; 
		font-size:12px;
		font-weight:bold;
		padding:3 5 0 5; 
		color:#206BA4
	}
</style>
<script language="javascript">
	var targetObj = '';
	document.onmouseup=mouseUp
	document.onmousemove=mouseMove
	var msgbox;
	var org_content;
	var optItem = '<%=(organizeTooltip == null) ? "" : organizeTooltip.getValue() %>';
	
	function mouseDown(Object){
		targetObj=Object.id
		document.all(targetObj).setCapture()
		pX=event.x-document.all(targetObj).style.pixelLeft;
		pY=event.y-document.all(targetObj).style.pixelTop;
	}
	
	function mouseMove(){
		if(targetObj!=''){
		  document.all(targetObj).style.left=event.x-pX;
		  document.all(targetObj).style.top=event.y-pY;
		  }
	}
	
	function mouseUp(){
		if(targetObj!=''){
		  document.all(targetObj).releaseCapture();
		  targetObj='';
		  }
	}
	
	function showMessageBox(id) {
		org_content = document.getElementById("org_content");
		if (id=='' || id == null || optItem == '') {
			org_content.innerText = "<fmt:message key='venus.authority.Not_given_parameters' bundle='${applicationAuResources}' />";
			return;
		}
		msgbox = document.getElementById("massage_box");
		var x = document.body.scrollLeft+event.clientX;
		var y = document.body.scrollTop+event.clientY;
		var left = x < (1/2 * document.body.scrollWidth) ? x + 15 : x - (document.body.clientWidth/2-20);
		var top = y < (1/2 * document.body.scrollHeight) ? y : y - (document.body.clientHeight/5-10);
		msgbox.style.left = left;
		msgbox.style.top = top;
		msgbox.style.visibility='visible';
		org_content.innerText = "<fmt:message key='venus.authority.Data_Loading' bundle='${applicationAuResources}' />......";
		var thisHidden = getLayoutHiddenObjectById(id);	
		jQuery.ajax({
			type: "GET",
			url: "<%=request.getContextPath()%>/auPartyRelation/getOrgName",
			data: "partyId=" + thisHidden.party_id,
			async: true,
			//cache: false,
			//timeout: 5000,
			success: function(data,textStatus) {
				if(data == '' || data == null || data == undefined || data == 'null')
					org_content.innerText = "<fmt:message key='venus.authority.Did_not_find_their_respective_organizations' bundle='${applicationAuResources}' />";
				else if (data.indexOf("<fmt:message key='venus.authority.You_are_logged_in_the_system' bundle='${applicationAuResources}' />") != -1)
					org_content.innerText = "<fmt:message key='venus.authority.You_are_logged_in_the_system' bundle='${applicationAuResources}' />,<fmt:message key='venus.authority.Or_login_has_timed_out' bundle='${applicationAuResources}' />,<fmt:message key='venus.authority.Please_login_system' bundle='${applicationAuResources}' />!";
				else 
					org_content.innerText = data;
			},
			error: function(xmlhttp,textStatus,errorThrown) {
				org_content.innerText = textStatus;
			}
		});
	}

	function closeMessageBox() {
		if (optItem == '')
			return;		
		 msgbox = document.getElementById("massage_box");
		 msgbox.style.visibility='hidden';
		 org_content = document.getElementById("org_content");
		 org_content.innerText = '';
	}
	
	document.write('<div id="massage_box">');
	document.write('<div class="massage_content">');
	document.write('<div class="message_header" onmousedown=mouseDown(massage_box)>');
	document.write("<div style='display:inline; width:150px; position:absolute'><fmt:message key='venus.authority.Organization' bundle='${applicationAuResources}' /></div>");
	document.write('<span onclick=closeMessageBox() style="float:right; display:inline; cursor:hand">×</span>');
	document.write('</div>');
	document.write("<div id='org_content' style='overflow: auto'><fmt:message key='venus.authority.Data_Loading' bundle='${applicationAuResources}' />......</div>");
	document.write('</div>');
	document.write('</div>');
</script>

