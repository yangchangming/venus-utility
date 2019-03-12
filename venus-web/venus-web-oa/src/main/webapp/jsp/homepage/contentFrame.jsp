<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="venus.oa.sysparam.vo.SysParamVo"%>
<%@ page import="venus.oa.util.GlobalConstants"%>
<%
    SysParamVo multiTab = GlobalConstants.getSysParam(GlobalConstants.MULTITAB);
    boolean isTab = false;
    if (multiTab != null){
        try{
            if ( Integer.parseInt(multiTab.getValue()) > 0 )
                isTab = true;
        }catch (Exception e){}
    }

    if ( isTab ) { //启用多页签
%>       
<script>
    window.location.href="<%=request.getContextPath()%>/jsp/homepage/body.jsp";
</script>
<% } else { //不使用多页签 %>
<script>
    window.location.href="<%=request.getContextPath()%>/jsp/homepage/simpleBody.jsp";
</script>            
<% } %>