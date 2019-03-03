<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/gap-html.tld" prefix="venus"%>
<%
    String referenceChecktype = request.getParameter("checktype");
    String referenceSelectedValues = request.getParameter("selectedValues");
    String referenceTree_href = request.getParameter("tree_href");
    String referenceSite_id = request.getParameter("site_id");
    String referenceCurrentid = request.getParameter("currentid");
    String referenceNotIncludeValues = request.getParameter("notIncludeValues");
%>
<fmt:bundle  basename="gap.ewp.ewp_resource" >
<html>
<title>参照页面<%=referenceChecktype%></title>
<script language="javascript">
    //树的根节点
    var referenceRootID = "root";
    //用户调用的beanID
    var referenceBeanId="docTypeBS";
    //用户自定义初始化树
    function onLoadReferenceTree(){
        var referenceWebModel=referenceForm_treebasic.referenceWebModel.value;
        var referenceTree_onclick ="doClick(this)";  //单击事件方法名
        var referenceTree_param = "docTypeID";           //链接参数名
        var referenceSite_id =  jQuery(parent.document.getElementById("site_id")).val();
        var paramJson = '{tree_href:"<%=referenceTree_href%>",tree_onclick:"'+referenceTree_onclick+'",treeid:"referenceTree",site_id:"'+referenceSite_id+'",webModel:"'+referenceWebModel+'",tree_param:"",tree_target:"",checkType:"<%=referenceChecktype%>",notIncludeValues:"<%=referenceNotIncludeValues%>",currentid:"<%=referenceCurrentid%>",selectedValues:"<%=referenceSelectedValues%>"}';
        initTree(referenceRootID,'referenceTree',paramJson);
    }
    
</script>
<script language="javascript">
  jQuery(document).ready(function(){ 
       onLoadReferenceTree();
   });
  </script>
<base target="_self">
</head>
<body>

<form name="referenceForm_treebasic" action="" method="post"><input type="hidden" name="referenceCmd" value=""> <input type="hidden" id="reference_site_id" name="reference_site_id"
    value="<%=referenceSite_id%>"> <input id="referenceWebModel" name="referenceWebModel" type="hidden" class="text_field" inputName="发布目录" value="<%=request.getContextPath()%>" readonly="true">
<table bgcolor="#c8cdd3">
    <tr>
        <td>
        <div id="referenceTree" align="left" style="width: 330px; height: 400px; background-color: #c8cdd3; border: 0px solid #90b3cf; overflow: auto;"></div>
        </td>
    </tr>
</table>

</form>
</body>
</html>
</fmt:bundle>