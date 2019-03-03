<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:bundle basename="udp.quartz.quartz_resource" prefix="udp.quartz.">

<form name="form2" id="form2" method="post" >
<table border=0 cellpadding=0 cellspacing=0  background="../../js/calendar/images/day_title.gif" align=center class=WholeCalendar_>
	<tr style="borderbottom:1 solid black">
		<td class=Title_ ><select id="hour" style="width:45px">
			<%String hour="";
			for(int i=0;i<24;i++){
				if(i<10){
					out.println("<option value="+i+" >0"+i+"</option>");
				}else{
				out.println("<option value="+i+" >"+i+"</option>");
				}
			}
			%>
			
			</select>&nbsp;<fmt:message key="Hour"/>&nbsp;
		</td>
		<td class=Title_><select id="minute" style="width:45px">
			<%for(int i=0;i<60;i++){
				if(i<10){
					out.println("<option value="+i+" >0"+i+"</option>");
				}else{
				out.println("<option value="+i+" >"+i+"</option>");
				}
			}
			%>
			</select>&nbsp;<fmt:message key="Minute"/>&nbsp;</td>
		<td><input type="button" name="button1" class="button_ellipse" value='<fmt:message key="Confirm"/>' class=enter LANGUAGE=javascript onclick="return button1_onclick('<%=request.getParameter("timeElement") %>');">
		</td>
	</tr>
</table>
</form>

</fmt:bundle>   
