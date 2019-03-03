<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/global.jsp" %>
<%@ page import = "java.util.List,java.util.Iterator,java.util.ArrayList,java.util.HashMap"%>
<%@ page import="venus.authority.org.aupartytype.vo.AuPartyTypeVo"%>
<%@ page import = "venus.authority.util.VoHelperTools"%>
<%@ page import = "venus.authority.util.GlobalConstants"%>
<%@ page import="venus.commons.xmlenum.EnumRepository"%>
<%@ page import="venus.commons.xmlenum.EnumValueMap"%>
	<%  //判断是否为修改页面
		boolean isModify = false;
		if(request.getParameter("isModify") != null) {
			isModify = true;
		}
		EnumRepository er = EnumRepository.getInstance();
        er.loadFromDir();
        EnumValueMap typeMap = er.getEnumValueMap("TypeStatus");
        List al = typeMap.getEnumList();
        String keywordconst = request.getParameter("keywordconst");
	%>
<title><%=isModify?venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Edit_Template"):venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.New_Template")%></title>

<script type='text/javascript' src='<venus:base/>/js/au/jquery/jSquared/jquery.jsquared-0.1.js'></script>

<script>
function assemblerTable(){
    var paraArray = new Array();
    var paraValue = new Array();
    jQuery('#squared').find('tr:first').each( function() {
        jQuery(this).find('th:not(:last)').each(function(){
            var paraValue = jQuery(this).attr('id');
            paraArray.push(paraValue);
        });
    });
    var validate = true;
    jQuery('#squared').find('tr:not(:first):not(:last)').each( function() {
        var tdArry = new Array();
        jQuery(this).find('td:not(:last)').each(function(){
            var paraValue = jQuery(this).find('select').val();
            if(paraValue === undefined)
                paraValue = jQuery(this).find('input').val();   
            if(paraValue === undefined)
                paraValue = jQuery(this).find('span').text();
            if(paraValue === undefined)
                paraValue = '';
            if(paraValue==''){
                validate = false;
            }
            tdArry.push(paraValue);
        });
        paraValue.push(tdArry);
    });
    if(paraValue.length>0){
        document.form.paraArray.value = paraArray.join();
        document.form.paraValue.value = paraValue.join();
    }
    if(!validate)
        alert("缺少属性数据，请检查自定义属性的表单数据!");
    return validate;
}
	function insert_onClick(){  //增加记录
		if(checkAllForms()){
		    if(assemblerTable()){
	    		<%--form.action = "<venus:base/>/AuPartyTypeAction.do?cmd=insert";--%>
				form.action = "<venus:base/>/auPartyType/insert";
	    		form.submit();
    		}
    	}
  	}
  	function update_onClick(){
  	    if(assemblerTable()){
  		    <%--form.action = "<venus:base/>/AuPartyTypeAction.do?cmd=update";--%>
			form.action = "<venus:base/>/auPartyType/update";
  		    form.submit();
        }
  	}
  	function getBuildList() {
		<%--var obj = window.showModalDialog('<venus:base/>/AuPartyTypeAction.do?cmd=queryBuild','','dialogHeight:350px;dialogWidth:450px;center:yes;help:no;resizable:yes;scroll:yes;status:no;');--%>
		var obj = window.showModalDialog('<venus:base/>/auPartyType/queryBuild','','dialogHeight:350px;dialogWidth:450px;center:yes;help:no;resizable:yes;scroll:yes;status:no;');
		if(obj) {
			document.form.build_name.value = obj[1];
			document.form.build_Id.value = obj[0];
		}
	}
	function queryAll_onClick(){  //查询全部数据列表
        if (typeof(parent.frames["myList"]) == "undefined") {
            returnBack();
        } else {
            <%--window.location="<venus:base/>/AuPartyTypeAction.do?cmd=simpleQuery&keyword="+document.form.keywordconst.value;--%>
			window.location="<venus:base/>/auPartyType/simpleQuery?&keyword="+document.form.keywordconst.value;
		}
    }
</script>

</head>
<body>
<script language="javascript">
	writeTableTop('<%=isModify?venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Modify_page"):venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.New_page")%>','<venus:base/>/themes/<venus:theme/>/');
</script>

<form name="form" method="post">
<input type="hidden" name="id" value="">
<input type="hidden" name="keywordconst" value="<%=keywordconst %>">
<input type="hidden" name="paraArray" value="">
<input type="hidden" name="paraValue" value="">
<%if(isModify){ %>
<input type="hidden" name="table_name" value="">
<%} %>
<table class="table_noframe">
	<tr>
		<td valign="middle">
			<input name="button_save" type="button" class="button_ellipse" value="<fmt:message key='venus.authority.Save' bundle='${applicationAuResources}' />" onClick="javascript:<%=isModify?"update_onClick()":"insert_onClick()"%>;">
			<input name="button_cancel" type="button" class="button_ellipse" value="<fmt:message key='venus.authority.Cancel' bundle='${applicationAuResources}' />"  onClick="javascript:queryAll_onClick();">
		</td>
	</tr>
</table>

<div id="auDivParent1"> 
<table class="table_div_control">
	<tr> 
		<td>
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('auDivChild1',this,'<venus:base/>/themes/<venus:theme/>/')"><%=isModify?venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Body_type_changes"):venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Add_Group_Type")%>
		</td>
	</tr>
</table>
</div>

<div id="auDivChild1"> 
<table class="table_div_content">
	<tr> 
		<td align="right" width="10%" nowrap><span class="style_required_red">* </span><fmt:message key='venus.authority.Name' bundle='${applicationAuResources}' /></td>
		<td align="left">
			<input name="Id" type="hidden" value="">
			<input name="name" type="text" class="text_field" inputName="<fmt:message key='venus.authority.Name' bundle='${applicationAuResources}' />" maxlength=50 validate="notNull;isSearch" value="">
		</td>		
	</tr>
	<tr>
	    <td align="right" width="10%" nowrap><span class="style_required_red">* </span><fmt:message key='venus.authority.Type_Classification' bundle='${applicationAuResources}' /></td>
	    <td align="left">
			<select name="keyword">
				<%for(int i=0;i<al.size();i++){%>
				<option value="<%=typeMap.getValue(al.get(i).toString())%>" <%=typeMap.getValue(al.get(i).toString()).equals(keywordconst)?"selected":""%>><%=al.get(i)%></option>
     			 <%}%>
			</select>
		</td>
	</tr>
	<!-- 
	<tr>
	    <td align="right" width="10%" nowrap><fmt:message key='venus.authority.Which_tables_mapping' bundle='${applicationAuResources}' /></td>
	    <td align="left">
			<input name="table_name" type="text" class="text_field" inputName="<fmt:message key='venus.authority.Which_tables_mapping' bundle='${applicationAuResources}' />" maxlength=50 value="">
		</td>
	</tr>	
	 -->
	<tr>
	    <td align="right" width="10%" nowrap><fmt:message key='venus.authority.Notes' bundle='${applicationAuResources}' /></td>
	    <td align="left">
			<textarea name="remark" cols="60" rows="5" class="textarea_limit_words"></textarea>
		</td>		
	</tr>
	<tr> 
        <td colspan="2"><hr align="center" width="95%" color="#c5d2da" SIZE="1"/></td>             
    </tr>
    <tr> 
        <td colspan="2"><br/></td>             
    </tr>
	<%if(!isModify){ %>
	<tr> 
        <td align="right" width="10%" nowrap><span class="style_required_red">* </span>表名称</td>
        <td align="left">
            <input name="table_name" type="text" class="text_field" inputName="表名称" maxlength=50 validate="notNull;isSearch" value="">
        </td>       
    </tr>
	<tr>
	   <td align="right" width="10%" nowrap>属性</td>
	   <td align="left">
	       <table class="table_div_content">
			    <tr>
			        <td>
				       <table id="squared" class="listCss" cellspacing="1" cellpadding="1">
				           <tr>
				               <th class='listCss fixed' id="attrName">属性名称</th>
				               <th class='listCss fixed' id="fieldName">字段名称</th>
				               <th class='listCss fixed' width="150px" id="attrType">属性类型</th>
				               <th class='listCss fixed' width="150px" id="en">英文显示内容</th>
				           </tr>
				       </table>
	               </td>
	           </tr>
	       </table>
	   </td>
	</tr>
	<%}else{ %>
	<tr>
       <td align="right" width="10%" nowrap>属性</td>
       <td align="left">
           <table class="table_div_content">
                <tr>
                    <td>
                       <table id="squared" class="listCss" cellspacing="1" cellpadding="1">
                           <tr>
                               <th class='listCss fixed' id="attrName">属性名称</th>
                               <th class='listCss fixed' id="fieldName">字段名称</th>
                               <th class='listCss fixed' width="150px" id="attrType">属性类型</th>
                               <th class='listCss fixed' width="150px" id="en">英文显示内容</th>
                           </tr>
                       </table>
                   </td>
               </tr>
           </table>
       </td>
    </tr>
	<%} %>
</table>
</div>
</form>
<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>/');
</script>

<script language="javascript">
jQuery('#squared').jSquared( { 
        addColumnText : '<img src="<venus:base/>/images/au/add.gif" class="div_control_image">',
        addRowText : '<img src="<venus:base/>/images/au/add.gif" class="div_control_image">',
        removeColumnText : '<img src="<venus:base/>/images/au/delete.gif" class="div_control_image">',
        removeRowText : '<img src="<venus:base/>/images/au/delete.gif" class="div_control_image">',
        onRowAdded  : function(textCell, text, dataCells) {
                var index = 0;
                dataCells.each( function() {
                    index++;
                    if(2==index){
                        jQuery(this).html('<select><option value="text">文字</option><option value="id">唯一标识</option><option value="date">日期时间</option><option value="number">数字</option><option value="boolean">真或假</option></select>');
                    }else{
                        jQuery(this).html('<input type="text"/>');
                    }                     
                });
        },
        onColumnAdded  : function(textCell, text, dataCells) {
                var index = 0;
                dataCells.each( function() {
                    index++;
                    jQuery(this).html('<input type="text"/>');
                });  
        }
} );
</script>

</body>
</html>
<%  //表单回写
	if(request.getAttribute("writeBackFormValues") != null) {
		out.print("<script language=\"javascript\">\n");
		out.print(VoHelperTools.writeBackMapToForm((java.util.Map)request.getAttribute("writeBackFormValues")));
		out.print("writeBackMapToForm();\n");
		out.print("</script>");
	}
%>

