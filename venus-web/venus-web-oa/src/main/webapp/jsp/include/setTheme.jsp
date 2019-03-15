<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="venus.frames.mainframe.util.Helper"%>
<%@ page import="venus.VenusHelper"%>
<%
    String theme=(String)request.getParameter("theme");

    if(theme!=null && !"".equals(theme)){
        //System.out.println("theme="+theme+"    "+Helper.getTheme());
        VenusHelper.THEME=theme;
        out.println("设置成功");
    }else{
     out.println("请传递参数theme");

    }
%>
