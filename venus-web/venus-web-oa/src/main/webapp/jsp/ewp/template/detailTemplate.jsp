<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/global.jsp" %>
<%@ page import="udp.ewp.tools.helper.EwpVoHelper" %>
<%@ page import="udp.ewp.tools.helper.EwpStringHelper" %>
<%@ page import="udp.ewp.template.model.EwpTemplate" %>
<%@ page import="udp.ewp.template.util.ITemplateConstants" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="udp.ewp.document.model.Document" %>
<%@ page import="venus.frames.i18n.util.LocaleHolder"%>

<% 
    EwpTemplate resultVo  = (EwpTemplate)request.getAttribute(ITemplateConstants.REQUEST_BEAN);

	request.setAttribute("ewpTemplate",resultVo);
%>
<html>
<fmt:bundle basename="udp.ewp.ewp_resource" >
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><fmt:message key="udp.ewp.detail_template"/></title>
<link href="<%=request.getContextPath()%>/js/gbox/ymprompt/ymPrompt.css" type="text/css" rel="stylesheet">
<script language="javascript" src="<%=request.getContextPath()%>/js/gbox/ymprompt/ymPrompt.js"></script>
<script  type="text/javascript" src="<venus:base/>/js/ewp/gap-ewp-tree-ext.js"></script>
<script  type="text/javascript" src="<venus:base/>/js/ewp/gap-ewp-tree-main.js"></script>
<script  type="text/javascript" src="<venus:base/>/js/ewp/doctype/tree.js"></script>
<script language="javascript">
    var chooseControllerDialog;
    var site_id =  jQuery(parent.document.getElementById("site_id")).val();
    if(site_id == null || site_id == "undefined"){
       site_id = "";
    }


    jQuery(document).ready(function(){ 
         chooseControllerDialog =  jQuery("#chooseController").dialog({ modal: true, height:580,autoOpen:false,resizable:false,width:372,overlay: { opacity: 0.3, background: "black" }});
    });
    
    function chooseController_onClick(){
         chooseControllerDialog.dialog("open");
    }
    
    function selectController_onClick(){
        chooseControllerDialog.dialog("close");
        preview_onClick();
    }
    function cancelController_onClick(){
        chooseControllerDialog.dialog("close");
    }
    
    function preview_onClick(){ //预览
      var controller = jQuery("#controller").val();
       if(controller == "welcome"){
           var refPath="<%=request.getContextPath()%>/"+controller+".page?view=${ewpTemplate.template_viewname}";
           ymPrompt.win(refPath,700,500,'<fmt:message key="udp.ewp.template.previewresult"/>',callback,null,null,true);
       }else if(controller == "doctypeandview" || controller == "documents"){
          var id = jQuery('input[name=selectDocType]:checked').val();
	      var refPath="<%=request.getContextPath()%>/api/"+controller+".page?view=${ewpTemplate.template_viewname}&id="+id;
	      ymPrompt.win(refPath,700,500,'<fmt:message key="udp.ewp.template.previewresult"/>',callback,null,null,true);
       }else if(controller == "document"){
          var id = jQuery('input[name=selectDocument]:checked').val();
          var refPath="<%=request.getContextPath()%>/api/"+controller+".page?view=${ewpTemplate.template_viewname}&id="+id;
          ymPrompt.win(refPath,700,500,'<fmt:message key="udp.ewp.template.previewresult"/>',callback,null,null,true);
       }else{
          var refPath="<%=request.getContextPath()%>/api/"+controller+".page?view=${ewpTemplate.template_viewname}";
          ymPrompt.win(refPath,700,500,'<fmt:message key="udp.ewp.template.previewresult"/>',callback,null,null,true);
       }
       hideAllSupplementDivs();
       jQuery("#controller").val("welcome");
    }
    
    function callback(){}
    function hideAllSupplementDivs(){
         jQuery("#docTypeAndViewDiv").css("display","none");
         jQuery("#documentDiv").css("display","none");
    }
    
    function back_onClick(){  //返回列表页面
        form.action="<%=request.getContextPath()%>/EwpTemplateAction.do?cmd=queryAll";
        form.submit();
    }
    
    function supplement(){
         hideAllSupplementDivs();
         var controller = jQuery("#controller").val();
         if(controller == "doctypeandview" || controller == "documents"){
              jQuery("#docTypeAndViewDiv").css("display","block");
         }else if(controller == "document"){
              jQuery("#documentDiv").css("display","block");
         }
    }
    
      jQuery(document).ready(function(){ 
          var refPath ='<%=request.getContextPath()%>/jsp/ewp/docType/emptyReferenceDocType.jsp?site_id='+site_id+'&tree_href=none&method=move_save_onClick&checktype=RADIO&selectedValues=&notIncludeValues=';
           jQuery("#docTypeAndViewDiv").load(refPath); 
      });
</script>
</head>
<body>
<script language="javascript">
	writeTableTop('<fmt:message key="view_page"  bundle="${applicationResources}"/>','<venus:base/>/themes/<venus:theme/>/');  //显示本页的页眉
</script>
<form name="form" method="post">
<div id="ccParent0"> 
<table class="table_div_control">
	<tr> 
		<td>
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('ccChild0',this,'<venus:base/>/themes/<venus:theme/>/')">
			<!--<input name="button_preview" class="button_ellipse" type="button" value='<fmt:message key="udp.ewp.preview" />'  onclick="javascript:chooseController_onClick();" >-->
			<input name="button_back" class="button_ellipse" type="button" value='<fmt:message key="return"  bundle="${applicationResources}"/>'  onclick="javascript:back_onClick();" >
		</td>
	</tr>
</table>
</div>

<div id="ccChild0"> 
	<table class="viewlistCss" style="width:100%">
        <tr>
            <td align="right" width="10%" nowrap><fmt:message key="udp.ewp.website.name"/>：</td>
            <td align="left">${ewpTemplate.webSite.websiteName}</td>
        </tr>
		<tr>
			<td align="right" width="10%" nowrap><fmt:message key='udp.ewp.template_name'/>：</td>
			<td align="left">${ewpTemplate.template_name}</td>
		</tr>
		<tr>
            <td align="right" width="10%" nowrap><fmt:message key='udp.ewp.template_viewname'/>：</td>
            <td align="left">${ewpTemplate.template_viewname}</td>
        </tr>
        <tr>
            <td align="right" width="10%" nowrap><fmt:message key='udp.ewp.template_type_name'/>：</td>
            <td align="left">${ewpTemplate.template_type}</td>
        </tr>
        <tr>
            <td align="right" width="10%" ><fmt:message key='udp.ewp.template.isdefault' />：</td>
            <td align="left">${ewpTemplate.isDefault}</td>
        </tr>
		<tr>
			<td align="right" width="10%" nowrap><fmt:message key='udp.ewp.template_content'/>：</td>
			<td align="left">${ewpTemplate.template_content}</td>
		</tr>
	</table>
</div>

<input type="hidden" name="id" value="<%=EwpStringHelper.prt(resultVo.getId())%>">

</form>
</fmt:bundle>
<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>/');  //显示本页的页脚
</script>
<div id="chooseController"  title='<%=LocaleHolder.getMessage("udp.ewp.template.pleasechooseapi") %>'>
<input name="button_preview" class="button_ellipse" type="button" value='<fmt:message key="confirm" bundle="${applicationResources}"/>'  onclick="javascript:selectController_onClick();" >
<input name="button_preview" class="button_ellipse" type="button" value='<fmt:message key="cancel" bundle="${applicationResources}"/>'  onclick="javascript:cancelController_onClick();" ><br/><br/>
<select id="controller" onchange="javascript:supplement();">
 <option   value='welcome' >/welcome.page 首页</option>
 <option   value='documents' >/documents.page</option>
 <option   value='document' >/document.page</option>
 <option   value='doctypeandview' >/doctypeandview.page</option>
 <option   value='navigateMenu' >/navigateMenu.page</option>
</select>
<br/><br/>
<div id="docTypeAndViewDiv" style="display:none"></div>
<div id="documentDiv"  style="display:none">
<select id="selectDocument">
   <%
       List result = (List) request.getAttribute(ITemplateConstants.DOCUMENT_BEANS);
       Iterator it = result.iterator();
       while(it.hasNext()){
           Document vo = (Document) it.next();
           %>
            <option value="<%=vo.getId() %>"><%=vo.getTitle() %></option>
           <%
       }
   %>
</select>
</div>
</div>
<div id="previewDiv" style="overflow:auto"><div id="content"  title="<fmt:message key='udp.ewp.template.previewresult'/>"></div></div>
<form name="form_treebasic" action="" method="post">
<input  id="webModel" name="webModel" type="hidden" class="text_field"  inputName="" value="<venus:base/>" readonly="true">
<input id="tree_href" name="tree_href" type="hidden" class="text_field" value="" readonly="true"> 
<input id="tree_onclick"    name="tree_onclick" type="hidden" class="text_field" value="doClick(this)" readonly="true">
<input id="tree_target" name="tree_target"  type="hidden" class="text_field" value="" readonly="true">
</form>
</body>
</html>
