<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.net.URLDecoder" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="venus.oa.util.DateTools" %>
<%
if(StringUtils.isBlank(request.getParameter("data"))){
    if(StringUtils.isNotBlank((String)session.getAttribute("_layout_json_data_"))){
        String json = URLDecoder.decode((String)session.getAttribute("_layout_json_data_"),"UTF-8");
        response.setContentType("application/octet-stream");  
        response.setHeader("Content-Disposition","attachment;filename = layout-"+DateTools.getSysDate()+".txt");
        session.removeAttribute("_layout_json_data_");
        out.write(json);
    }else{
        out.write("no data !");
    }
}else{
    if(StringUtils.isNotBlank((String)session.getAttribute("_layout_json_data_"))){
        session.removeAttribute("_layout_json_data_");
    }
    session.setAttribute("_layout_json_data_",request.getParameter("data"));
    out.write("ok");
}
%>