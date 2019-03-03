<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/jsp/include/global.jsp"%>
<%@ page import="udp.ewp.tools.helper.EwpVoHelper"%>
<%@ page import="udp.ewp.util.EnumTools"%>
<%@ page import="venus.frames.i18n.util.LocaleHolder"%>
<%@ page import="udp.ewp.website.util.IWebsiteConstants"%>
<%@ page import="java.util.*"%>
<%@ page import="venus.commons.xmlenum.EnumRepository" %>
<%@ page import="venus.commons.xmlenum.EnumValueMap" %>
<%
    EnumRepository er = EnumRepository.getInstance();
    er.loadFromDir();

    EnumValueMap languageMap = er.getEnumValueMap("Language");
    List languageList = languageMap.getEnumList();
    TreeMap langMap = new TreeMap(new Comparator() {
        public int compare(Object languageOne, Object languageTwo) {
            return EnumTools.getOrderNum((String) languageOne) - EnumTools.getOrderNum((String) languageTwo);
        }
    });
    for (int i = 0; i < languageList.size(); i++) {
        langMap.put(languageMap.getValue(languageList.get(i).toString()), languageList.get(i));
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
    <title><fmt:message key="udp.ewp.website.manage"/></title>
    <link href="<%=request.getContextPath()%>/js/gbox/ymprompt/ymPrompt.css" type="text/css" rel="stylesheet">
    <script language="javascript" src="<%=request.getContextPath()%>/js/gbox/ymprompt/ymPrompt.js"></script>
    <script language="javascript">

    //获得选中的站点
    function findSelections() {
         var ids = null;
         jQuery("input[name='checkbox_template']:checkbox").each(function(){
            if(jQuery(this).is(":checked")){
                if(ids == null){
                    ids = new Array(0);
                }
                ids.push(jQuery(this).attr("id"));
            }
         });
           return ids;
    }

    //转入修改站点界面
    function toMofidy_onClick() {
        var ids = findSelections();
        if(ids == null) {
            alert('<fmt:message key="udp.ewp.select_one_record"/>');
            return;
        }
        if(ids.length > 1) {
            alert('<fmt:message key="udp.ewp.only_can_a_record"/>');
            return;
        }

        jQuery("form").attr("action","<venus:base/>/WebsiteAction.do?id=" + ids);
        jQuery("input[name='cmd']").val("find");
        jQuery("form").submit();
    }

    //转入预览界面
    function toPreview_onClick() {
        var ids = findSelections();
        if(ids == null) {
            alert('<fmt:message key="udp.ewp.select_one_record"/>');
            return;
        }
        if(ids.length > 1) {
            alert('<fmt:message key="udp.ewp.only_can_a_record"/>');
            return;
        }
        var siteCode = $("#" + ids[0]).attr("value");
        var refPath = "<%=request.getContextPath()%>/" + siteCode;
        window.open(refPath, '_blank');
         <%--ymPrompt.win(refPath,500,300,'<fmt:message key="udp.ewp.template.previewresult"/>',callback,null,null,true);--%>
    }
     function callback(){}

    //删除多个站点
    function deleteMulti_onClick(){
        var ids = findSelections();
        if(ids == null) {
            alert('<fmt:message key="udp.ewp.select_records" />');
            return;
        }
        if(confirm('<fmt:message key="whether_to_delete_the_data_completely" bundle="${applicationResources}"/>')) {
                jQuery("form").attr("action","<venus:base/>/WebsiteAction.do?cmd=deleteMulti&ids="+ids);
                jQuery("input[name='backFlag']").val("false");
                jQuery("form").submit();
        }
    }

    //为站点初始化索引
    function insertIndexMulti_onClick(){
        var ids = findSelections();
        if(ids == null) {
            alert('<fmt:message key="udp.ewp.select_records" />');
            return;
        }
        jQuery.ajax({
            url:"<venus:base/>/document.do?cmd=insertDocIndexAllByWebSiteIds&websiteIds="+ids
            ,async:false
            ,cache:false
            ,dataType:"text"
            ,success:function (data, textStatus){
                var jsonResult = eval('('+data+')');
                if(jsonResult.result=="Y"){
                    alert("<fmt:message key="operation_successful" bundle="${applicationResources}"/>");
                }else{
                    alert("<fmt:message key="operation_failed" bundle="${applicationResources}"/>");
                }
            }
        });
    }

    //查询
    function simpleQuery_onClick(){
        jQuery("form").attr("action","<venus:base/>/WebsiteAction.do?backFlag=true");
        jQuery("input[name='cmd']").val("simpleQuery");
        jQuery("form").submit();
    }

    //重置查询条件
    function resetForm(){
        jQuery("input[name='websiteName']").val('');
        jQuery("input[name='description']").val('');
        jQuery("select[name='language']").val('');
    }

    //转入增加界面
    function toAdd_onClick() {
        jQuery("form").attr("action","<venus:base/>/jsp/ewp/website/insertWebsite.jsp");
        jQuery("form").submit();
    }

    //设置默认站点
    function setDefaultWebsite_onClick(){
        var ids = findSelections();
        if(ids == null) {
            alert('<fmt:message key="udp.ewp.select_one_record"/>');
            return;
        }
        if(ids.length > 1) {
            alert('<fmt:message key="udp.ewp.only_can_a_record"/>');
            return;
        }

        jQuery.ajax({url:"<venus:base/>/WebsiteAction.do?cmd=isDefaultWebsite&Action=get&id="+ids,async:false,cache:false,dataType:"text",success:function (data, textStatus){
            var jsonResult = eval('('+data+')');
            var isPass = jsonResult.isPass;
            if(isPass=="Y"){
                alert('<fmt:message key="udp.ewp.Is_the_default_site"/>');
            }else{
                 if(confirm('<fmt:message key="udp.ewp.website.whether_to_set_default_website"/>')) {
                     jQuery("form").attr("action", "<venus:base/>/WebsiteAction.do?id=" + ids);
                     jQuery("input[name='cmd']").val("updateDefaultWebsite");
                     jQuery("input[name='backFlag']").val("true");
                     jQuery("form").submit();
                }
            }
        }});
    }

    //查看详细信息
    function detail_onClick(){
        var ids = findSelections();
        if(ids == null) {
            alert('<fmt:message key="udp.ewp.select_one_record"/>');
            return;
        }
        if(ids.length > 1) {
            alert('<fmt:message key="udp.ewp.only_can_a_record"/>');
            return;
        }
        jQuery("form").attr("action","<venus:base/>/WebsiteAction.do?id=" + ids);
        jQuery("input[name='cmd']").val("detail");
        jQuery("form").submit();
    }

</script>
<script language="javascript">
    jQuery(function () {
        jQuery("td table tr").bind("dblclick", function () {
            var id = jQuery(this).children("td").children("input[name='checkbox_template'][type='checkbox']").attr('id');
            if (id != null && id != "undefined") {
                form.action = "<venus:base/>/WebsiteAction.do?id=" + id;
                form.cmd.value = "detail";
                form.submit();
            }
            return false;
        })
    });
</script>
</head>
<body>
<div style="width:100%;">
    <script language="javascript">
    writeTableTop('<fmt:message key="udp.ewp.website.manage" />','<venus:base/>/themes/<venus:theme/>/');
</script>
	<form name="form" method="post" action="<venus:base/>/WebsiteAction.do?cmd=queryAll"><input type="hidden" name="cmd" value=""> <input type="hidden" name="backFlag" value="true">
	<div id="ccParent0">
	<table class="table_div_control">
		<tr>
			<td><img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('ccChild0',this,'<venus:base/>/themes/<venus:theme/>/')">&nbsp;<fmt:message key="query_with_condition"
				bundle="${applicationResources}" /></td>
		</tr>
	</table>
	</div>
	<div id="ccChild0">
	<table class="table_div_content">
		<tr>
			<td>
			<table class="table_noFrame" width="100%">
				<tr>
					<td align="center"><fmt:message key="udp.ewp.website.name" />
					<input type="text" class="text_field" name="websiteName" inputName="websiteName" maxLength="50" value="${requestScope.websiteName}" />
						 <fmt:message key="udp.ewp.website.description" />
						 <input type="text" class="text_field" name="description" inputName="description" maxLength="32767.5" value="${requestScope.description}" />
						<select name="language" id="language">
						<option value=""><fmt:message key="udp.ewp.website.chooselanguage" /></option>
						<logic:iterate id="lang" name="langMap">
						          <c:if test="${ requestScope.language eq lang.key }">
                                   <option value="<bean:write name="lang" property="key"/>" selected><bean:write name="lang" property="value" /></option>
                                </c:if>
                               <c:if test="${ requestScope.language ne lang.key }">
                                   <option value="<bean:write name="lang" property="key"/>"><bean:write name="lang" property="value" /></option>
                                </c:if>
						</logic:iterate>
					</select> <input name="button_ok" class="button_ellipse" type="button" value='<fmt:message key="query" bundle="${applicationResources}"/>' onClickTo="javascript:simpleQuery_onClick()"> <input
						name="button_reset" class="button_ellipse" type="button" value='<fmt:message key="clear" bundle="${applicationResources}"/>' onClick="javascript:resetForm()"></td>
				</tr>
			</table>
			</td>
		</tr>
	</table>
	</div>
	<div id="ccParent1">
	<table class="table_div_control">
		<tr>
			<td><img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('ccChild1',this,'<venus:base/>/themes/<venus:theme/>/')">&nbsp;<fmt:message key="list_page"
				bundle="${applicationResources}" /></td>
			<td>
			<table align="right">
				<tr>
					<td><input name="button_insert_index_website" class="button_ellipse" type="button" value='<fmt:message key="udp.ewp.website.insert_index_website" />' onClick="javascript:insertIndexMulti_onClick();"></td>
					<td><input name="button_set_default_website" class="button_ellipse" type="button" value='<fmt:message key="udp.ewp.website.set_default_website" />' onClick="javascript:setDefaultWebsite_onClick();"></td>
					<td><input name="button_view" class="button_ellipse" type="button" value='<fmt:message key="view" bundle="${applicationResources}"/>' onClick="javascript:detail_onClick();"></td>
					<td><input name="button_add" class="button_ellipse" type="button" value='<fmt:message key="add" bundle="${applicationResources}"/>' onClick="javascript:toAdd_onClick();"></td>
					<td><input name="button_delete" class="button_ellipse" type="button" value='<fmt:message key="delete" bundle="${applicationResources}"/>' onClick="javascript:deleteMulti_onClick();"></td>
					<td><input name="button_modify" class="button_ellipse" type="button" value='<fmt:message key="modify" bundle="${applicationResources}"/>' onClick="javascript:toMofidy_onClick();"></td>
					<td><input name="button_modify" class="button_ellipse" type="button" value='<fmt:message key="udp.ewp.preview" />' onClick="javascript:toPreview_onClick();"></td>
				</tr>
			</table>
			</td>
		</tr>
	</table>
	</div>
	<div id="ccChild1">
	<table class="table_div_content2">
		<tr>
			<td><layout:collection name="beans" id="website" styleClass="listCss" width="100%" indexId="orderNumber" align="center" sortAction="0">
				<layout:collectionItem width="1%" title="<input type='checkbox' pdType='control' control='checkbox_template'/>" style="text-align:center;">
					<input type="checkbox" name="checkbox_template" id="${pageScope.website.id}" value="${pageScope.website.websiteCode}"/>
				</layout:collectionItem>
				<layout:collectionItem width="5%" title='<%=LocaleHolder.getMessage("sequence") %>' style="text-align:center;">
					<venus:sequence />
				</layout:collectionItem>
				<layout:collectionItem width="14%" title='<%=LocaleHolder.getMessage("udp.ewp.website.code") %>' property="websiteCode" sortable="false" />
				<layout:collectionItem width="14%" title='<%=LocaleHolder.getMessage("udp.ewp.website.name") %>' property="websiteName" sortable="false" />
				<layout:collectionItem width="14%" title='<%=LocaleHolder.getMessage("udp.ewp.website.description") %>' property="description" sortable="false" />
				<layout:collectionItem width="10%" title='<%=LocaleHolder.getMessage("udp.ewp.website.language") %>' property="language" sortable="false">
					<bean:define id="lang" name="website" property="language" />
					<%=langMap.get(String.valueOf(lang))%>
				</layout:collectionItem>
				<layout:collectionItem width="14%" title='<%=LocaleHolder.getMessage("udp.ewp.website.isdefault") %>' property="isDefault" sortable="false">
					<bean:define id="isDefault" name="website" property="isDefault" />
					<%=booleanMap.get(isDefault) == null ? LocaleHolder.getMessage("udp.ewp.website.no") : booleanMap.get(isDefault)%>
				</layout:collectionItem>
				<layout:collectionItem width="14%" title='<%=LocaleHolder.getMessage("udp.ewp.website.nameisunique") %>' property="nameIsUnique" sortable="false">
					<bean:define id="nameIsUnique" name="website" property="nameIsUnique" />
					<%=booleanMap.get(nameIsUnique) == null ? LocaleHolder.getMessage("udp.ewp.website.no") : booleanMap.get(nameIsUnique)%>
				</layout:collectionItem>
			</layout:collection> <jsp:include page="/jsp/include/page.jsp" /></td>
		</tr>
	</table>
	</div>
	</form>
	<script language="javascript">
    writeTableBottom('<venus:base/>/themes/<venus:theme/>/');  //显示本页的页脚
</script>
</div>
	</body>
</fmt:bundle>
</html>
