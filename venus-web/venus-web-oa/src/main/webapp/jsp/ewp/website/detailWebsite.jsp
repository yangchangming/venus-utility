<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/global.jsp" %>
<%@ page import="udp.ewp.tools.helper.EwpVoHelper" %>
<%@ page import="udp.ewp.website.model.Website" %>
<%@ page import="udp.ewp.website.util.IWebsiteConstants" %>
<%@ page import="venus.commons.xmlenum.EnumRepository" %>
<%@ page import="venus.commons.xmlenum.EnumValueMap"%>
<%@ page import="java.util.*" %>
<% //取出本条记录
    Website resultVo = null;  //定义一个临时的vo变量
    resultVo = (Website) request.getAttribute(IWebsiteConstants.REQUEST_BEAN);  //从request中取出vo, 赋值给resultVo
    EwpVoHelper.replaceToHtml(resultVo);  //把vo中的每个值过滤
    EnumRepository er = EnumRepository.getInstance();
    er.loadFromDir();
    EnumValueMap languageMap = er.getEnumValueMap("Language");
    List isValideList = languageMap.getEnumList();
    TreeMap langMap = new TreeMap();
    for (int i = 0; i < isValideList.size(); i++) {
        langMap.put(languageMap.getValue(isValideList.get(i).toString()), isValideList.get(i));
    }
    request.setAttribute("langMap", langMap);

    EnumValueMap logicBooleanMap = er.getEnumValueMap("LogicBoolean");
    List logicBooleanList = logicBooleanMap.getEnumList();
    HashMap booleanMap = new HashMap();
    for (int i = 0; i < logicBooleanList.size(); i++) {
        booleanMap.put(logicBooleanMap.getValue(logicBooleanList.get(i).toString()), logicBooleanList.get(i));
    }
%>
<html>
<fmt:bundle basename="udp.ewp.ewp_resource">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><fmt:message key="udp.ewp.website.websitedetail"/></title>
    <script language="javascript">
        function back_onClick() {  //返回列表页面
            jQuery("form").attr("action", "<venus:base/>/WebsiteAction.do?cmd=queryAll");
            jQuery("form").submit();
        }
    </script>
</head>
<body>
<script language="javascript">
    writeTableTop('<fmt:message key="udp.ewp.website_detail"/>', '<venus:base/>/themes/<venus:theme/>/');  //显示本页的页眉
</script>
<form name="form" method="post">
    <div id="ccParent0">
        <table class="table_div_control">
            <tr>
                <td>
                    <img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image"
                         onClick="javascript:hideshow('ccChild0',this,'<venus:base/>/themes/<venus:theme/>/')">&nbsp;&nbsp;<input
                        name="button_back" class="button_ellipse" type="button"
                        value='<fmt:message key="return" bundle="${applicationResources}"/>'
                        onclick="javascript:back_onClick();">
                </td>
            </tr>
        </table>
    </div>
    <div id="ccChild0">
        <table class="viewlistCss" style="width: 100%" style="border:1px solid">
            <tr>
                <td align="right" width="10%"><fmt:message key='udp.ewp.website.name'/>：</td>
                <td align="left">${requestScope.bean.websiteName}</td>
            </tr>
            <tr>
                <td align="right" width="10%"><fmt:message key='udp.ewp.website.code'/>：</td>
                <td align="left">${requestScope.bean.websiteCode}</td>
            </tr>
            <tr>
                <td align="right" width="20%"><fmt:message key="udp.ewp.keyword"/>：</td>
                <td align="left" width="80%">${requestScope.bean.keywords}</td>
            </tr>
            <tr>
                <td align="right" width="10%"><fmt:message key='udp.ewp.website.description'/>：</td>
                <td align="left">${requestScope.bean.description}</td>
            </tr>
            <tr>
                <td align="right" width="10%"><fmt:message key='udp.ewp.website.language'/>：</td>
                <td align="left"><%=langMap.get(resultVo.getLanguage())%> </select></td>
            </tr>
            <tr>
                <td align="right" width="10%"><fmt:message key='udp.ewp.website.isdefault'/>：</td>
                <td align="left"><%=booleanMap.get(resultVo.getIsDefault())%> </select></td>
            </tr>
            <tr>
                <td align="right" width="10%"><fmt:message key='udp.ewp.website.nameisunique'/>：</td>
                <td align="left"><%=booleanMap.get(resultVo.getNameIsUnique())%> </select></td>
            </tr>
        </table>
    </div>
    <input type="hidden" name="id" value="${requestScope.bean.id}"></form>
</fmt:bundle>
<script language="javascript">
    writeTableBottom('<venus:base/>/themes/<venus:theme/>/');  //显示本页的页脚
</script>
</body>
</html>
