<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/global.jsp" %>
<HTML><HEAD>
<TITLE><fmt:message key="date_reference" bundle="${applicationResources}"/> </TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK href="<venus:base/>/themes/<venus:theme/>/js/calendar/calendar.css" rel=stylesheet type=text/css>
<script language="javascript" src="<venus:base/>/themes/<venus:theme/>/js/calendar/calendar.js"></script>
<SCRIPT ID=clientEventHandlersJS LANGUAGE=javascript>

	var uniqueID = "";
	function cal_ondblclick() {
       window.parent.jQuery("input[name='<%=request.getParameter("textDate")%>']").val(fnGetValue());  
        window.parent.jQuery("#<%=request.getParameter("dialogDivid")%>").dialog("close");
	}
	function hidden_button1(){
	   document.getElementsByName("button1")[0].style.display="none";
	}
	function button1_onclick(){
		window.returnValue=fnGetValue();
		self.close()
	}
	function window_onload() {
		//if (isSpace(window.dialogArguments)==false)
		//	cal.value=window.dialogArguments
	}
	function ok_onkeydown(){
	    if (event.keyCode==13){
		      cal_ondblclick();
		}
	}
	jQuery(function(){
	    element = jQuery(".calendarDiv")[0];
	    jQuery(element).bind("selectstart",fnOnSelectStart).bind("click",fnOnClick);
	    fnGetPropertyDefaults();
		fnCreateCalendarHTML();
		fnUpdateTitle();
		fnUpdateDayTitles();
		fnBuildMonthSelect();
		fnBuildYearSelect();
		fnFillInCells();
	})

</SCRIPT>

</HEAD>

<BODY scroll="no"  LANGUAGE=javascript border="0"  onload="return window_onload()" onkeydown="return ok_onkeydown()">

<CENTER>
<div class="calendarDiv" id="cal" style="width:100%;height:100%" ondblclick="return cal_ondblclick()">
</div>
</CENTER>
<P></P>

</BODY>

</HTML>
