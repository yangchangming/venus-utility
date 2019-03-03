<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/jsp/include/global.jsp"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/gap-html.tld" prefix="venus" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="venus.frames.i18n.util.LocaleHolder" %>
<%@ page import="venus.frames.mainframe.util.Helper"%>
<%@ page import="udp.ewp.util.CommonFieldConstants"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="java.util.TreeMap"%>
<%@ page import="java.util.List"%>
<html>
<fmt:bundle  basename="gap.ewp.ewp_resource" >
<head>
<title><fmt:message key="udp.ewp.website.refwebsite"/></title>
<script>
function confirm(){
    var site_id = jQuery('#site_id').val();
            jQuery.ajax({url:"<venus:base/>/WebsiteAction.do?cmd=changeCurrentSite&site_id="+site_id,async:false,cache:false,dataType:"text",success:function (data, textStatus){
            var jsonResult = eval('('+data+')');
            var isPass = jsonResult.isPass;
            if(isPass=="Y"){
               parent.parent.location.reload();//相对只刷新企业建站平台的父级frame，对于集成测试不刷新整个页面，项目集成时此页面再单独更改。
            }else{
                alert('<fmt:message key="udp.ewp.website.switchwebsite_fail"/>');
            }
        }});
}
</script>
</head>
<body>
<form name="form" method="post" action="">
<input type="hidden" name="cmd" value=""/> 
<script language="javascript">
    writeTableTop('<fmt:message key="udp.ewp.website.switchwebsite" />','<venus:base/>/themes/<venus:theme/>/');  
</script>
<table>
    <tr>
        <td>
		    <select name="site_id" id="site_id" class="span5" onchange="confirm()">
		        <logic:iterate id="web" name="websiteMap"  >
		            <c:if test="${web.key eq site_id}">
		                <option   value='<bean:write name="web" property="key" />' selected="selected"><bean:write name="web" property="value"/></option>
		            </c:if>
		            <c:if test="${web.key ne site_id}">
		                <option   value='<bean:write name="web" property="key" />' ><bean:write name="web" property="value"/></option>
		            </c:if>
		        </logic:iterate>
		    </select>
        </td>
    </tr>
</table>
<script language="javascript">
    writeTableBottom('<venus:base/>/themes/<venus:theme/>/');  //显示本页的页脚
</script>
</form>
</body>
</fmt:bundle>
</html>