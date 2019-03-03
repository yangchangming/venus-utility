<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<fmt:bundle  basename="gap.ewp.ewp_resource" >
<html>
<head>
<%@ include file="/jsp/include/global.jsp" %>
<%
 String checktype = request.getParameter("checktype");
 String selectedValues = request.getParameter("selectedValues");
 String tree_href = request.getParameter("tree_href");
 String notIncludeValues = request.getParameter("notIncludeValues");
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>选择栏目</title>
  <%
response.setHeader("Cache-Control","no-cache");
response.setHeader("Pragma","no-cache");
response.setDateHeader("Expires",0);
%>  
<script src="<%=request.getContextPath()%>/dwrewp/interface/EwpTreeControl.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/dwr/engine.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/dwr/util.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/ewp/gap-ewp-tree-ext.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/ewp/doctype/doctype.js"  type="text/javascript" ></script>
<script language="javascript">
    //返回父栏目的值
    function returnValueName(){
       var ids=new Array();
       var chinesenames = new Array();
       var allDocType = document.getElementsByName("selectDocType");
       for(var i=0;i<allDocType.length;i++){
           if(allDocType[i].checked){
             ids.push(allDocType[i].value);
             chinesenames.push(allDocType[i].getAttribute("chinesename"));
           }
       }
       var idnames = '{ids:"'+ids+'",chinesenames:"'+chinesenames+'"}';
       window.returnValue = idnames;
       window.close();
    }
    
    //树的根节点
    var rootID = "root";
    //用户调用的beanID
    var beanId="docTypeBS";
    //用户自定义初始化树
    function onLoadTree(){
     webModel=form_treebasic.webModel.value;
        var tree_onclick ="doClick(this)";  //单击事件方法名
        var tree_param = "docTypeID";           //链接参数名
        var site_id =  jQuery("#reference_site_id").val();
        var paramJson = '{tree_href:"<%=tree_href%>",tree_onclick:"'+tree_onclick+'",tree_param:"",site_id:"'+site_id+'",webModel:"'+webModel+'",tree_target:"",checkType:"<%=checktype%>",notIncludeValues:"<%=notIncludeValues%>",selectedValues:"<%=selectedValues%>",treeid:"tree"}';
        initTree(rootID,'tree',paramJson);
    }

    //单击展开/收缩 事件
    function expandAndCollapse(triggeredEvent){
        webModel=form_treebasic.webModel.value;
        if(jQuery.browser.mozilla){
            if (triggeredEvent.target.id=="foldheader" || triggeredEvent.target.id=="deptheader") {
            var $current = jQuery(triggeredEvent.target.nextSibling.nextSibling);
            var $next = $current.parent().next();
            var imagesrc = triggeredEvent.target.src;
            if($next.css("display")==null || $next.css("display")=="none"){
               $next.css("display","block");
                if(imagesrc.indexOf("root.png")==-1){
                     triggeredEvent.target.src= webModel+"/images/tree/parentopen.gif";
                }
            }else{
               $next.css("display","none");
                if(imagesrc.indexOf("root.png")==-1){
                    triggeredEvent.target.src= webModel+"/images/tree/parent.gif";
               }
            }
          }
        }else{
            if (window.event.srcElement.id=="foldheader" || window.event.srcElement.id=="deptheader") {
            var $current = jQuery(window.event.srcElement.nextSibling.nextSibling);
            var $next = $current.parent().next();
            var imagesrc = window.event.srcElement.src;
            if($next.css("display")==null || $next.css("display")=="none"){
               $next.css("display","block");
               if(imagesrc.indexOf("root.png")==-1){
                   window.event.srcElement.src= webModel+"/images/tree/parentopen.gif";
               }
            }else{
               $next.css("display","none");
               if(imagesrc.indexOf("root.png")==-1){
                 window.event.srcElement.src= webModel+"/images/tree/parent.gif";
               }
            }
         }
        }
    }
    
</script>
</head>
<body onload="javascript:onLoadTree();">
<script language="javascript">
    writeTableTop('<fmt:message key="reference_page" bundle="${applicationResources}"/>','<venus:base/>/themes/<venus:theme/>/');
</script>
<form name="form_treebasic" action="" method="post">
<input id="webModel" name="webModel" type="hidden" class="text_field" inputName="发布目录" value="<%=request.getContextPath()%>" readonly="true">
<input type="hidden" id = "reference_site_id" name="reference_site_id" value="${sessionScope.site_id}">
<table class="table_noFrame">
    <tr>
        <td>&nbsp;&nbsp;
            <input name="button_ok" class="button_ellipse" type="button" value='<fmt:message key="confirm" bundle="${applicationResources}"/>'  onClick="javascript:returnValueName();">
            <input name="button_cancel" class="button_ellipse" type="button" value='<fmt:message key="cancel" bundle="${applicationResources}"/>' onclick="javascript:window.close();" >
        </td>
    </tr>
</table>
        <div id="tree" align="left" style="width:100%; height: 100%; background-color: #c8cdd3; border: 0px solid #90b3cf;"></div>
</form>
</body>
</html>
</fmt:bundle>
