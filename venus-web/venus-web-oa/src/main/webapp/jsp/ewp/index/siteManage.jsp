<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/gap-html.tld" prefix="venus" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="venus.frames.i18n.util.LocaleHolder" %>
<%@ page import="venus.frames.mainframe.util.Helper"%>
<%@ page import="udp.ewp.website.bs.IWebsiteBs"%>
<%@ page import="udp.ewp.website.model.Website"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="java.util.TreeMap"%>
<%@ page import="java.util.List"%>
<script language="javascript" src="<venus:base/>/js/jquery/jquery-1.6.4.min.js"></script>
<link rel="stylesheet" href="<venus:base/>/js/jquery/plugin/jquery-ui/css/default/jquery.ui.dialog.css" type="text/css">
<%
//取出所有的站点
IWebsiteBs websiteBs = (IWebsiteBs)Helper.getBean("IWebsiteBs");
Website defaultWebsite =  null;
List websites =  websiteBs.queryAll();
if(websites != null && websites.size()>0){
    String site_id =  (String)session.getAttribute("site_id");
    for (int i = 0; i < websites.size(); i++) {
            Website website = (Website)websites.get(i);
            if(StringUtils.equals(site_id,website.getId())){
                defaultWebsite = website;
                break;
            }
    }
    if(defaultWebsite == null){
        for (int i = 0; i < websites.size(); i++) {
            if(defaultWebsite == null ){
                Website website = (Website)websites.get(i);
                if(StringUtils.equals("Y",website.getIsDefault())){
                    defaultWebsite = website;
                    break;
                }
            }
        }
    }
    
    if(defaultWebsite == null ){
        defaultWebsite =  (Website)websites.get(0);
    }
}
request.setAttribute("site_id",defaultWebsite.getId());
session.setAttribute(LocaleHolder.LOCAL_IN_SESSION_KEY, defaultWebsite.getLanguage());
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Menu</title>
<link type="text/css" rel="stylesheet" id="leftcss"   href="<%=request.getContextPath()%>/themes/<venus:theme/>/css/index/style.css">
<script type="text/javascript">$(document).ready(function()
{
	$("#firstpane p.menu_head").click(function()
    {
		
		$(this).next("div.menu_body").slideToggle(300).siblings("div.menu_body").slideUp("slow");
		$("#firstpane p.menu_head").css({height:"25px"});		
	});
});
</script>
</head>
<body>

<div class="main">
<br />
  <div id="tc" align="left"><img src="<%=request.getContextPath()%>/themes/<venus:theme/>/images/index/icon.png" />&nbsp;<%=LocaleHolder.getMessage("udp.ewp.website.manage") %></div>
  <div class="side">
    <div id="firstpane" class="menu_list">

	    <p class="menu_head"><%=LocaleHolder.getMessage("udp.ewp.website.manage") %></p>
	    <div class="menu_body">
	      <a href="<%=request.getContextPath()%>/WebsiteAction.do?cmd=queryAll" target="bodyFrame"><%=LocaleHolder.getMessage("udp.ewp.website.manage") %></a>
	    </div>

    </div>
  </div>
  <div id="bc"></div>
</div>
</body>
</html>
