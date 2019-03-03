<%@ page contentType="text/html; charset=UTF-8" %>  
<% 
    String nfid = request.getParameter("fid");
    String ofid = (String)session.getAttribute("_function_id_");
    if ( !ofid.equals( nfid ) )
        session.setAttribute("_function_id_", nfid);
%>