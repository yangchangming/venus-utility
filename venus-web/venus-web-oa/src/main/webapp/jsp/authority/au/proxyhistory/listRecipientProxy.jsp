<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="venus.oa.authority.auproxy.util.IConstants" %>
<%@ page import="venus.oa.authority.auproxy.vo.ProxyHistoryVo" %>
<%@page import="venus.oa.util.StringHelperTools"%>
<%@ page import="venus.oa.util.VoHelperTools" %>
<%@ page import="java.util.List" %>
<%@ include file="/jsp/include/global.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><fmt:message key='venus.authority.Acting_History' bundle='${applicationAuResources}' /></title>
<script language="javascript">
    function findSelections(checkboxName, idName) {  //从列表中找出选中的id值列表
        var elementCheckbox = document.getElementsByName(checkboxName);  //通过name取出所有的checkbox
        var number = 0;  //定义游标
        var ids = null;  //定义id值的数组
        for(var i=0;i<elementCheckbox.length;i++){  //循环checkbox组
            if(elementCheckbox[i].checked) {  //如果被选中
                number += 1;  //游标加1
                if(ids == null) {
                    ids = new Array(0);
                }
                ids.push(elementCheckbox[i].value);  //加入选中的checkbox
            }
        }
        return ids;
    }
    function simpleQuery_onClick(){  //简单的模糊查询
        form.action="<%=request.getContextPath()%>/auProxyHistory/simpleQuery?forwardPage=recipient";
        form.submit();
    }
    function refresh_onClick(){  //刷新本页
        window.location.reload();
    }
    function detail_onClick(thisId){  //实现转到详细页面
         var urlStr="<%=request.getContextPath()%>/jsp/authority/au/proxyhistory/viewAllAu.jsp?proxyhistoryid="+thisId+"&dialogFlag=dialog";
         showIframeDialog("iframeDialog","", urlStr, 800, 600);
    }
    
    function clean_onClick() {  //清空查询条件
        form.sponsor.value = "";
        form.recipient.value = "";
        form.proxy.value = "";
        form.operater_type[0].checked = false;
        form.operater_type[1].checked = false;
    }

    function view_onClick(){//查看详细页面
        var ids = findSelections("checkbox_template","id");  //取得多选框的选择项
        if(ids == null) {  //如果ids为空
            alert("<fmt:message key='venus.authority.Please_select_a_record' bundle='${applicationAuResources}' />!")
            return;
        }
        if(ids.length > 1) {  //如果ids有2条以上的纪录
            alert("<fmt:message key='venus.authority.Can_only_select_a_record' bundle='${applicationAuResources}' />!")
            return;
        }
        detail_onClick(ids);
    }
</script>
</head>
<body>
<script language="javascript">
    writeTableTop("<fmt:message key='venus.authority.List_pages' bundle='${applicationAuResources}' />",'<venus:base/>/themes/<venus:theme/>/');  //显示本页的页眉
</script>
<form name="form" method="post">
<div id="ccParent0"> 
<table class="table_div_control">
    <tr> 
        <td>
            <img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('ccChild0',this,'<%=request.getContextPath()%>/themes/<venus:theme/>/')" >&nbsp;<fmt:message key='venus.authority.Conditional_Query' bundle='${applicationAuResources}' />
        </td>
    </tr>
</table>
</div>
<div id="ccChild0"> 
<table class="table_div_content">
    <tr>
        <td>
            <table class="table_noFrame" width="100%">
                <tr>
                    <td align="right"><fmt:message key='venus.authority.Agent0' bundle='${applicationAuResources}' /></td>
                    <td align="left">
                        <input type="text" class="text_field" validate="isSearch" name="proxy" inputName="<fmt:message key='venus.authority.Proxy' bundle='${applicationAuResources}' />" maxLength="50"/>
                    </td>
                    <td align="right"><fmt:message key='venus.authority.Operation' bundle='${applicationAuResources}' /></td>
                    <td align="left">
                        <input type="radio" name="operater_type" inputName="<fmt:message key='venus.authority.Operating' bundle='${applicationAuResources}' />" value="1"><fmt:message key='venus.authority.Proxy' bundle='${applicationAuResources}' />
                        <input type="radio" name="operater_type" inputName="<fmt:message key='venus.authority.Operating' bundle='${applicationAuResources}' />" value="2"><fmt:message key='venus.authority.Revocation' bundle='${applicationAuResources}' />
                    </td>
                </tr>
                <tr>
                    <td width="15%" align="right"><fmt:message key='venus.authority.Authorized' bundle='${applicationAuResources}' /></td>
                    <td width="35%" align="left">
                        <input type="text" class="text_field" validate="isSearch" name=sponsor inputName="<fmt:message key='venus.authority.Authorized' bundle='${applicationAuResources}' />" maxLength="50"/>
                    </td>
                    <td align="right"></td>
                    <td>
                        <input name="button_ok" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Inquiry' bundle='${applicationAuResources}' />" onClickTo="javascript:simpleQuery_onClick()">
                        <input name="button_ok" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Empty' bundle='${applicationAuResources}' />" onClick="javascript:clean_onClick()">
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</div>
<div id="ccParent1"> 
<table class="table_div_control">
    <tr> 
        <td>
            <img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('ccChild1',this,'<%=request.getContextPath()%>/themes/<venus:theme/>/')">&nbsp;<fmt:message key='venus.authority.Details_Form' bundle='${applicationAuResources}' />
        </td>
        <td> 
            <table align="right">
                <tr> 
                    <td class="button_ellipse" onClick="javascript:view_onClick();"><img src="<venus:base/>/themes/<venus:theme/>/images/icon/view.gif" class="div_control_image"><fmt:message key='venus.authority.View' bundle='${applicationAuResources}' /></td>
                    <td class="button_ellipse" onClick="javascript:refresh_onClick();"><img src="<venus:base/>/themes/<venus:theme/>/images/icon/refresh.gif" class="div_control_image"><fmt:message key='venus.authority.Refresh' bundle='${applicationAuResources}' /></td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</div>
<div id="ccChild1"> 
<table class="table_div_content2" width="100%">

    <tr>
        <td>
            <div style="width=100%;overflow-x:visible;overflow-y:visible;">
                <table cellspacing="0" cellpadding="0" border="0" align="center" width="100%" class="listCss">
                    <tr>
                        <td valign="top">
                            <table cellspacing="1" cellpadding="1" border="0" width="100%">
                                <tr valign="top">
                                    <th class="listCss" ></th>

                                    <th class="listCss" width="30"><fmt:message key='venus.authority.Sequence' bundle='${applicationAuResources}' /></th>
                                    <th class="listCss"><fmt:message key='venus.authority.Authorized' bundle='${applicationAuResources}' /></th>
                                    <th class="listCss"><fmt:message key='venus.authority.Proxy' bundle='${applicationAuResources}' /></th>
                                    <th class="listCss"><fmt:message key='venus.authority.Proxy0' bundle='${applicationAuResources}' /></th>
                                    <th class="listCss"><fmt:message key='venus.authority.Status' bundle='${applicationAuResources}' /></th>
                                    <th class="listCss"><fmt:message key='venus.authority.Acting_time' bundle='${applicationAuResources}' /></th>
                                    <th class="listCss"><fmt:message key='venus.authority.Agency_operator' bundle='${applicationAuResources}' /></th>
                                    <th class="listCss"><fmt:message key='venus.authority.Withdrawal_time' bundle='${applicationAuResources}' /></th>
                                    <th class="listCss"><fmt:message key='venus.authority.Revocation_of_the_operator' bundle='${applicationAuResources}' /></th>
                                </tr>
                                <%
                                    List beans = (List) request.getAttribute("beans");
                                    for(int i=0;  i<beans.size();) {
                                        ProxyHistoryVo vo= (ProxyHistoryVo)beans.get(i);
                                        i++;
                                %>
                                <tr>
                                    <td class="listCss" align="center">
                                        <input type="radio" name="checkbox_template" value="<%=vo.getId()%>"/>
                                    </td>
                                    <td class="listCss" align="center"><%=i%><input type="hidden" signName="hiddenId" value="<%=vo.getId()%>" /></td>
                                    <td class="listCss"><%=StringHelperTools.prt(vo.getSponsor())%></td>
                                    <td class="listCss"><%=StringHelperTools.prt(vo.getProxy())%></td>
                                    <td class="listCss"><%=StringHelperTools.prt(vo.getRecipient())%></td>
                                    <td class="listCss">
                                        <%="1".equals(vo.getOperater_type())?venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Proxy"):venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Revocation")%>
                                    </td>
                                    <td class="listCss"><%=StringHelperTools.prt(vo.getOperater_date(), 19)%></td>
                                    <td class="listCss"><%=StringHelperTools.prt(vo.getOperater_name())%></td>
                                    <td class="listCss"><%=StringHelperTools.prt(vo.getCanel_date(), 19)%></td>
                                    <td class="listCss"><%=StringHelperTools.prt(vo.getCanel_name())%></td>
                                </tr>
                                <%
                                    }
                                %>
                            </table>
                        </td>
                    </tr>
                </table>
                <jsp:include page="/jsp/include/page.jsp" />
            </div>
        </td>
    </tr>




    <%--<tr>--%>
        <%--<td>--%>
        <%--<layout:collection onRowDblClick="detail_onClick(getRowHiddenId())"  name="beans" id="rmBean" styleClass="listCss" width="100%" indexId="orderNumber" align="center" sortAction="0">--%>
            <%--<layout:collectionItem width="30" title="" style="text-align:center;">--%>
                <%--<bean:define id="rmValue" name="rmBean" property="id"/>--%>
                <%--<input type="radio" name="checkbox_template" value="<%=rmValue%>"/>--%>
            <%--</layout:collectionItem>--%>
            <%--<layout:collectionItem width="30"  title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Sequence") %>' style="text-align:center;">--%>
                <%--<venus:sequence/>--%>
                <%--<bean:define id="rmValue" name="rmBean" property="id"/>--%>
                <%--<input type="hidden" signName="hiddenId" value="<%=rmValue%>"/>--%>
            <%--</layout:collectionItem>--%>
            <%--<layout:collectionItem title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Authorized") %>' property="sponsor"/>--%>
            <%--<layout:collectionItem title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Proxy") %>' property="proxy"/>--%>
            <%--<layout:collectionItem title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Proxy0") %>' property="recipient"/>--%>
            <%--<layout:collectionItem title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Status") %>' style="text-align:center;">--%>
                <%--<bean:define id="sex" name="rmBean" property="operater_type"/>--%>
                <%--<%="1".equals(sex)?venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Proxy"):venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Revocation")%>--%>
            <%--</layout:collectionItem>--%>
            <%--<layout:collectionItem title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Acting_time") %>'>--%>
                <%--<bean:define id="create_date" name="rmBean" property="operater_date"/>--%>
                <%--<%=StringHelperTools.prt(create_date,19)%>--%>
            <%--</layout:collectionItem>--%>
            <%--<layout:collectionItem title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Agency_operator") %>' property="operater_name"/>--%>
            <%--<layout:collectionItem title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Withdrawal_time") %>'>--%>
            <%--<logic:notEmpty name="rmBean" property="canel_date">--%>
                <%--<bean:define id="canel_date" name="rmBean" property="canel_date"/>--%>
                <%--<%=StringHelperTools.prt(canel_date,19)%>--%>
            <%--</logic:notEmpty>--%>
            <%--</layout:collectionItem>--%>
            <%--<layout:collectionItem title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Revocation_of_the_operator") %>' property="canel_name"/>--%>
            <%--</layout:collection>--%>
        <%--<jsp:include page="/jsp/include/page.jsp" />--%>
        <%--</td>--%>
    <%--</tr>--%>
</table>
</div>
<input type="hidden" name="id" value="">
<input type="hidden" name="forwardPage" value="recipient">
<input type="hidden" name="recipient" value="">
</form>
<!-- 参照显示层 -->
<div id="iframeDialog" style="display:none"></div>
<script language="javascript">
    writeTableBottom('<venus:base/>/themes/<venus:theme/>/');  //显示本页的页脚
</script>
</body>
</html>
<%  //表单回写
    if(request.getAttribute(IConstants.REQUEST_WRITE_BACK_FORM_VALUES) != null) {  //如果request中取出的bean不为空
        out.print("<script language=\"javascript\">\n");  //输出script的声明开始
        out.print(VoHelperTools.writeBackMapToForm((java.util.Map)request.getAttribute(IConstants.REQUEST_WRITE_BACK_FORM_VALUES)));  //输出表单回写方法的脚本
        out.print("writeBackMapToForm();\n");  //输出执行回写方法
        out.print("</script>");  //输出script的声明结束
    }
%>

