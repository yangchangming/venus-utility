<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/global.jsp" %>
<%@ page import="udp.ewp.tools.helper.EwpVoHelper" %>
<%@ page import="udp.ewp.tools.helper.EwpStringHelper" %>
<%@ page import="udp.ewp.tag.model.Tag" %>
<%@ page import="udp.ewp.tag.util.ITagConstants" %>
<%  //取出本条记录
Tag resultVo = null;  //定义一个临时的vo变量
    resultVo = (Tag)request.getAttribute(ITagConstants.REQUEST_BEAN);  //从request中取出vo, 赋值给resultVo
    EwpVoHelper.replaceToHtml(resultVo);  //把vo中的每个值过滤
%>
<html>
<fmt:bundle basename="udp.ewp.ewp_resource">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script language="javascript">
    function back_onClick(){  //返回列表页面
        form.action="<venus:base/>/TagAction.do?cmd=queryAll";
        form.submit();
    }
</script>
</head>
<body>
<script language="javascript">
     writeTableTop('<fmt:message key="udp.ewp.tag_manage_detail" />','<venus:base/>/themes/<venus:theme/>/');
</script>
<form name="form" method="post">
<div id="ccParent0"> 
<table class="table_div_control">
    <tr> 
        <td>
            <img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('ccChild0',this,'<venus:base/>/themes/<venus:theme/>/')">&nbsp;&nbsp;<input name="button_back" class="button_ellipse" type="button" value='<fmt:message key="return" bundle="${applicationResources}"/>'  onclick="javascript:back_onClick();" >
        </td>
    </tr>
</table>
</div>

<div id="ccChild0"> 
    <table class="viewlistCss" style="width:100%">
        <tr>
            <td align="right" width="10%" nowrap><fmt:message key="udp.ewp.tag_name"/>：</td>
            <td align="left"><%=EwpStringHelper.prt(resultVo.getName())%></td>
        </tr>
    </table>
</div>

<input type="hidden" name="id" value="<%=EwpStringHelper.prt(resultVo.getId())%>">

</form>
</fmt:bundle>
</body>
</html>
