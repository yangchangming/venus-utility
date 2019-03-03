<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*"%>
<%@ page import="udp.quartz.scheduledata.bo.DefinitionManager"%>
<%@ page import="udp.quartz.extend.QuartzUtil"%>

<%
    DefinitionManager def = (DefinitionManager)session.getServletContext().getAttribute(QuartzUtil.JOB_DEFINITIONS_PROP);
    Map defMap = def.getDefinitions();
    String jobName = "";
    if(defMap ==null)
    {
        System.out.println("defMap is null--------");
    }
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:bundle basename="udp.quartz.quartz_resource" prefix="udp.quartz.">

<form name="form2" id="form2" method="post" >

<div id="ccChildq"><span id="pos"></span>
<table id="jobTable" class="table_div_content" border="0">
    <tr >
        <td><select id="jobName" style="width:140">
            <%
            for(Iterator iter=defMap.entrySet().iterator();iter.hasNext();){
                Map.Entry entry = (Map.Entry)iter.next();
                out.println("<option value="+entry.getKey()+" >"+entry.getKey()+"</option>");
            }
            %>
            </select>&nbsp;&nbsp;
        </td>
        <td><input type="button" class="button_ellipse" value='<fmt:message key="Confirm"/>' onclick="return button1_onclick();">
        </td>
    </tr>
</table>
</div> 
</form>
</fmt:bundle>
