<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="udp.ewp.tools.helper.EwpVoHelper" %>
<%@ page import="udp.ewp.tools.helper.EwpStringHelper" %>
<%@ page import="udp.ewp.requests.vo.RequestsVo" %>
<%@ page import="udp.ewp.requests.model.Requests" %>
<%@ page import="udp.ewp.util.EnumTools"%>
<%@ page import="udp.ewp.requests.util.IRequestsConstants" %>
<%@ include file="/jsp/include/global.jsp" %>
<% 
    Requests resultVo = new Requests() ; 
    boolean isModify = false; 
    if(request.getParameter("isModify") != null) {  
        isModify = true; 
        if(request.getAttribute(IRequestsConstants.REQUEST_BEAN) != null) {  
            resultVo = (Requests)request.getAttribute(IRequestsConstants.REQUEST_BEAN); 
        }
    }
    request.setAttribute("resultVo",resultVo);
    request.setAttribute("wheretoknowMap",EnumTools.getSortedEnumMap(EnumTools.WHERETOKNOW));
%>
<html>
<fmt:bundle basename="udp.ewp.ewp_resource" >
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script language="javascript">
    function insert_onClick(){
       var websiteaddress = jQuery("#websiteaddress").val();
       var flag = isWebSite(websiteaddress);
       if(flag){
            if(!getConfirm()) { 
                return false;
            }
            form.action="<%=request.getContextPath()%>/RequestsAction.do?cmd=insert";
            form.submit();
       }else{
            alert("请输入合法的网址!");
            return false;
       }
    }

    function update_onClick(id){
       var websiteaddress = jQuery("#websiteaddress").val();
       var flag = isWebSite(websiteaddress);
       if(flag){
	        if(!getConfirm()) { 
	            return false;
	        }
	        form.action="<%=request.getContextPath()%>/RequestsAction.do?cmd=update";
	        form.submit();
       }else{
            alert("请输入合法的网址!");
            return false;
       }
    }

    function cancel_onClick(){  
        form.action="<%=request.getContextPath()%>/RequestsAction.do?cmd=queryAll&clearCondition=true";
        form.submit();
    }
    
    function isWebSite(val) { 
        if(val.length ==0 ) 
            return true;    
        var regu = /^.*www.*$/;
        if (regu.exec(val)) {     
            return true;
        } else {
            return false;
        }
    }
</script>
</head>
<body>
<script language="javascript">
    if(<%=isModify%>)
           writeTableTop('<fmt:message key="udp.ewp.requests_modify" />','<venus:base/>/themes/<venus:theme/>/');
    else
           writeTableTop('<fmt:message key="udp.ewp.requests_add" />','<venus:base/>/themes/<venus:theme/>/');
</script>
<form name="form" method="post">
<div id="ccParent1"> 
<table class="table_div_control">
    <tr> 
        <td>
            <img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('ccChild1',this,'<venus:base/>/themes/<venus:theme/>/')">
            <input name="button_save" class="button_ellipse" type="button" value='<fmt:message key="save" bundle="${applicationResources}"/>' onClickTo="javascript:<%=isModify?"update_onClick()":"insert_onClick()"%>">
            <input name="button_cancel" class="button_ellipse" type="button" value='<fmt:message key="cancel" bundle="${applicationResources}"/>'  onClick="javascript:cancel_onClick()" >
        </td>
    </tr>
</table>
</div>

<div id="ccChild1"> 
<table class="table_div_content">
<tr><td> 
    <table class="table_div_content_inner">
        <tr>
            <td align="right"><span class="style_required_red">* </span><fmt:message key="udp.ewp.requests.first_name"/>：</td>
            <td align="left">
                <input type="text" class="text_field" name="first_name" inputName='<fmt:message key="udp.ewp.requests.first_name"/>' value="" maxLength="10" validate="notNull;"/>
            </td>
            <td align="right"></td>
            <td align="left"></td>
        </tr>
        <tr>
            <td align="right"><span class="style_required_red">* </span><fmt:message key="udp.ewp.requests.last_name"/>：</td>
            <td align="left">
                <input type="text" class="text_field" name="last_name" inputName='<fmt:message key="udp.ewp.requests.last_name"/>' value="" maxLength="10" validate="notNull;"/>
            </td>
            <td align="right"></td>
            <td align="left"></td>
        </tr>
        <tr>
            <td align="right"><span class="style_required_red">* </span><fmt:message key="udp.ewp.requests.requestTitle"/>：</td>
            <td align="left">
                <input type="text" class="text_field" name="title" inputName='<fmt:message key="udp.ewp.requests.requestTitle"/>' value="" maxLength="10" validate="notNull;"/>
            </td>
            <td align="right"></td>
            <td align="left"></td>
        </tr>
        <tr>
            <td align="right"><span class="style_required_red">* </span><fmt:message key="udp.ewp.requests.company"/>：</td>
            <td align="left">
                <input type="text" class="text_field" name="company" inputName='<fmt:message key="udp.ewp.requests.company"/>' value="" maxLength="100" validate="notNull;"/>
            </td>
            <td align="right"></td>
            <td align="left"></td>
        </tr>
        <tr>
            <td align="right"><span class="style_required_red">* </span><fmt:message key="udp.ewp.requests.email"/>：</td>
            <td align="left">
                <input type="text" class="text_field" name="email" inputName='<fmt:message key="udp.ewp.requests.email"/>' value="" maxLength="100" validate="notNull;isEmail;"/>
            </td>
            <td align="right"></td>
            <td align="left"></td>
        </tr>
        <tr>
            <td align="right"><span class="style_required_red">* </span><fmt:message key="udp.ewp.requests.phone"/>：</td>
            <td align="left">
                <input type="text" class="text_field" name="phone" inputName='<fmt:message key="udp.ewp.requests.phone"/>' value="" maxLength="10" validate="notNull;isMobile;"/>
            </td>
            <td align="right"></td>
            <td align="left"></td>
        </tr>
        <tr>
            <td align="right"><span class="style_required_red">* </span><fmt:message key="udp.ewp.requests.country"/>：</td>
            <td align="left">
                <input type="text" class="text_field" name="country" inputName="<fmt:message key="udp.ewp.requests.country"/>" value="" maxLength="25" validate="notNull;"/>
            </td>
            <td align="right"></td>
            <td align="left"></td>
        </tr>
        <tr>
            <td align="right"><span class="style_required_red">* </span><fmt:message key="udp.ewp.requests.website"/>：</td>
            <td align="left">
                <input id="websiteaddress" type="text" class="text_field" name="website" inputName='<fmt:message key="udp.ewp.requests.website"/>' value="" maxLength="50" validate="notNull;"/>
            </td>
            <td align="right"></td>
            <td align="left"></td>
        </tr>
        <tr>
            <td align="right"><span class="style_required_red">* </span><fmt:message key="udp.ewp.requests.referer"/>：</td>
            <td align="left">
                        <select name="referer" id="referer" inputName='<fmt:message key="udp.ewp.requests.referer"/>'>
                            <logic:iterate id="tempType" name="wheretoknowMap">
                                <c:if test="${tempType.key eq requestScope.resultVo.referer}">
                                    <option value="<bean:write name="tempType" property="key"/>" selected><bean:write name="tempType" property="value" /></option>
                                </c:if>
                                <c:if test="${tempType.key ne requestScope.resultVo.referer}">
                                    <option value="<bean:write name="tempType" property="key"/>"><bean:write name="tempType" property="value" /></option>
                                </c:if>
                            </logic:iterate>
                        </select>
            </td>
            <td align="right"></td>
            <td align="left"></td>
        </tr>
        <tr>
            <td align="right"><span class="style_required_red">* </span><fmt:message key="udp.ewp.requests.comments"/>：</td>
            <td colspan="3" align="left">
                <textarea class="textarea_limit_words" cols="60" rows="5" name="comments" inputName='<fmt:message key="udp.ewp.requests.comments"/>' maxLength="1000" validate="notNull;"></textarea>
              <span id="charaterLimitSpan"></span>
            </td>
        </tr>
        <tr>
            <td align="right"></td>
            <td align="left"></td>
            <td align="right"></td>
            <td align="left"></td> 
        </tr>
    </table>
</td></tr>
</table>
</div>
            
<input type="hidden" name="id" value="">
<input type="hidden" name="create_date" />

</form>
</fmt:bundle>
<script language="javascript">
    writeTableBottom('<venus:base/>/themes/<venus:theme/>/');  //显示本页的页脚
</script>
</body>
</html>
<script language="javascript">
<% 
    if(isModify) {  //如果本页面是修改页面
        out.print(EwpVoHelper.writeBackMapToForm(EwpVoHelper.getMapFromVo(resultVo)));  //输出表单回写方法的脚本
    }
%>
</script>
