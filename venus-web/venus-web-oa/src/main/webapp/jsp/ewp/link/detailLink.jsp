<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/global.jsp" %>
<%@ page import="udp.ewp.tools.helper.EwpVoHelper" %>
<%@ page import="udp.ewp.tools.helper.EwpStringHelper" %>
<%@ page import="udp.ewp.link.model.Link" %>
<%@ page import="udp.ewp.link.util.ILinkConstants" %>
<%
    Link resultVo = null; 
    resultVo = (Link)request.getAttribute(ILinkConstants.REQUEST_BEAN); 
    EwpVoHelper.replaceToHtml(resultVo); 
%>
<html>
<fmt:bundle basename="udp.ewp.ewp_resource">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script language="javascript">
    function back_onClick(){
        form.action="<%=request.getContextPath()%>/LinkAction.do?cmd=queryAll";
        form.submit();
    }
</script>
</head>
<body>
<script language="javascript">
    writeTableTop('<fmt:message key="udp.ewp.link_detail" />','<venus:base/>/themes/<venus:theme/>/');  
</script>
<form name="form" method="post">
<div id="ccParent0"> 
<table class="table_div_control">
    <tr> 
        <td>
            <img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('ccChild0',this,'<venus:base/>/themes/<venus:theme/>/')" />
            <input name="button_back" class="button_ellipse" type="button" value='<fmt:message key="return" bundle="${applicationResources}"/>'  onclick="javascript:back_onClick();" >
        </td>
    </tr>
</table>
</div>

<div id="ccChild0"> 
    <table class="viewlistCss" style="width:100%">
        <tr>
            <td align="right" width="10%" nowrap><fmt:message key="udp.ewp.link.category"/>：</td>
            <td align="left"><%=EwpStringHelper.prt(resultVo.getCategory())%></td>
        </tr>
        <tr>
            <td align="right" width="10%" nowrap><fmt:message key="udp.ewp.link.linkTitle"/>：</td>
            <td align="left"><%=EwpStringHelper.prt(resultVo.getTitle())%></td>
        </tr>
        <tr>
            <td align="right" width="10%" nowrap><fmt:message key="udp.ewp.link.content"/>：</td>
            <td align="left" >
            <div style="height:150px;width:300px;overflow:auto;word-wrap:break-word;word-spacing:normal;"><%=EwpStringHelper.prt(resultVo.getContent())%></div>
            </td>.
        </tr>
        <tr>
            <td align="right" width="10%" nowrap><fmt:message key="udp.ewp.link.website"/>：</td>
            <td align="left"><%=EwpStringHelper.prt(resultVo.getWebsite())%></td>
        </tr>
    </table>
</div>

<input type="hidden" name="id" value="<%=EwpStringHelper.prt(resultVo.getId())%>">

</form>
</fmt:bundle>
</body>
</html>
