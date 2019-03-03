<%@ page import="venus.authority.util.GlobalConstants" %>
<%@ page import="java.util.List" %>
<%@ page import="venus.authority.org.aupartyrelation.vo.AuPartyRelationVo" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<%
    List rslist_ewp = (List) (request.getAttribute(venus.authority.org.auparty.util.IConstants.REQUEST_LIST_VALUE));
    if (rslist_ewp.size() > 0) {
        String pType_id = ((AuPartyRelationVo) (rslist_ewp.get(0))).getPartytype_id();
        if (GlobalConstants.isPerson(pType_id) || GlobalConstants.isRole(pType_id)) {
%>
<td nowrap="true" class="button_ellipse" title="<%=msgResourceMap.getLabel("viewAuDesc") %>"
    onClick="javascript:toDoctypeTreePage();">
    <img src="<venus:base/>/themes/<venus:theme/>/images/icon/add.gif" class="div_control_image">
    <fmt:message key='venus.authority.Doctype_Tree_Data_Rights' bundle='${applicationAuResources}'/></td>
<%
        }
    }
%>