<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import = "java.util.List,java.util.Iterator,java.util.ArrayList,java.util.HashMap"%>
<%@ page import="venus.authority.au.auresource.vo.AuResourceVo"%>
<%@ page import="venus.authority.au.auresource.util.IAuResourceConstants"%>
<%@ page import = "venus.authority.util.VoHelperTools"%>
<%@ include file="/jsp/include/global.jsp" %>
<title><fmt:message key='venus.authority.Increase_in_template' bundle='${applicationAuResources}' /></title>
<script>
<!--
  	function update_onClick(){
		if(check_checkBox()){
  			form.action = "<venus:base/>/auResource/updatefiled?forward=queryAll";
  			form.submit();
		}
  	}
	function test_onClick(i){
		if(document.getElementsByName('value')[i].value==''){
  			alert("<fmt:message key='venus.authority.Please_fill_in_the_filter_value_' bundle='${applicationAuResources}' />");
			document.getElementsByName('value')[i].focus();
			return false;
		}
		var height=window.screen.height/2-50;
		var width=window.screen.width/2-50;
		var urlTest="<venus:base/>/auResource/testSQL?tableName="+document.getElementsByName('table_name')[0].value+"&fieldName="+document.getElementsByName('field_name')[i].value+"&fieldType="+document.getElementsByName('filter_type')[i].value+"&fieldValue="+document.getElementsByName('value')[i].value;
		window.open (urlTest, 'testwindow', 'height=120, width=100, top='+height+', left='+width+', toolbar=no, menubar=no, scrollbars=no, resizable=no,location=n o, status=no');
  	}
    function getFieldName(objV){
        this.location="<venus:base/>/auResource/initinsert?resource_type=4&name="+objV.value+"&forward=<%=IAuResourceConstants.REQUEST_QUERY_ALL_RECORD%>";
	}
	function cancel(){
		this.location="<venus:base/>/auResource/queryAll";
	}
  	function check_checkBox(){
  	
  		if(document.form.table_chinesename.value==''){
			alert("<fmt:message key='venus.authority.List_of_Chinese_names_can_not_be_empty_' bundle='${applicationAuResources}' />");
			document.form.table_chinesename.focus();
			return false;
		}
		
  		var aa = document.getElementsByName('checkme');
		var checked=false;
		for (var i=0; i<aa.length; i++){
			if(aa[i].checked){
				checked=true;
				if(document.getElementsByName('name')[i].value==''){
					alert("<fmt:message key='venus.authority.Name_can_not_be_empty_' bundle='${applicationAuResources}' />");
					document.getElementsByName('name')[i].focus();
					return false;
				}
				if(document.getElementsByName('field_name')[i].value==''){
					alert("<fmt:message key='venus.authority.Field_names_can_not_be_empty_' bundle='${applicationAuResources}' />");
					document.getElementsByName('field_name')[i].focus();
					return false;
				}
				if(document.getElementsByName('value')[i].value==''){
					alert("<fmt:message key='venus.authority.Filter_value_can_not_be_empty_' bundle='${applicationAuResources}' />");
					document.getElementsByName('value')[i].focus();
					return false;
				}
			}
		}
		if(!checked){
			alert("<fmt:message key='venus.authority.You_have_not_checked_any_box' bundle='${applicationAuResources}' />");
			return false;
		}
		return true;
	}


    var global_options_array = new Array();
    $(document).ready(function(){
        var j = 0;
        $("option").each(function(){
            global_options_array[j++] = $(this).attr("value");
        });
    });

    /**
     * 快速定位
     */
    function quicklyQuery_onClick(){
        try{
            var i=0;
            var tempArr = new Array();
            var query_keyword_value = document.getElementById("query_keyword_id").value;

            if(query_keyword_value==null || ""==query_keyword_value.trim()){
                alert("<fmt:message key='venus.authority.Filter_value_can_not_be_empty_' bundle='${applicationAuResources}' />");
                return;
            }else{
                var regExp = new RegExp("^[0-9a-zA-Z_]{1,}$");
                if(!query_keyword_value.match(regExp)){
                    alert("<fmt:message key='venus.authority.regular_expression_error' bundle='${applicationAuResources}' />");
                    return;
                }
            }

            for(var k=0;k<global_options_array.length;k++){
                if(match(query_keyword_value,global_options_array[k])){
                    tempArr[i++] = global_options_array[k];
                }
            }

            if(tempArr.length==0){
                alert("<fmt:message key='venus.authority.no_match_item' bundle='${applicationAuResources}' />");
                return;
            }else{
                var selectObj = $("select[name='table_name']");
                selectObj.empty();
                for(var j=0;j<tempArr.length;j++){
                    selectObj.append("<option value=" + tempArr[j] + ">"+ tempArr[j] +"</option>");
                }

                var _selectedObj = document.getElementsByName("table_name");
                if(_selectedObj != null && _selectedObj.length>0){
                    getFieldName(_selectedObj[0]);
                }
            }

        }catch (e){
            alert(e.message);
        }
    }

    /**
     * 正则模糊匹配
     * @regExp 正则需要进行过滤验证,不能输入特殊字符
     */
    function match(regExp,targetStr){
        return eval("/.*" + regExp.trim() + ".*/").test(targetStr);
    }


//-->
</script>
</head>
<body>
<script language="javascript">
	writeTableTop("<fmt:message key='venus.authority.Increase_the_page' bundle='${applicationAuResources}' />",'<venus:base/>/themes/<venus:theme/>/');
</script>

<form name="form" method="post">
<table class="table_noframe">
	<tr>
		<td valign="middle">
			<input name="button_save" type="button" class="button_ellipse" value="<fmt:message key='venus.authority.Save' bundle='${applicationAuResources}' />" onClick="javascript:update_onClick();">
			<input name="button_cancel" type="button" class="button_ellipse" value="<fmt:message key='venus.authority.Cancel' bundle='${applicationAuResources}' />"  onClick="javascript:cancel();">
		</td>
	</tr>
</table>
<%
	String tableName=(String)request.getAttribute(IAuResourceConstants.REQUEST_NAME_VALUE);
    List listTable = (List) request.getAttribute(IAuResourceConstants.REQUEST_BEANS_VALUE);
    List listField=new ArrayList();
    if(listTable.size()>0){
	AuResourceVo firstVo=(AuResourceVo)listTable.get(0);
	 listField = (List) request.getAttribute(IAuResourceConstants.REQUEST_BEAN_FIELD_VALUE);
	}
%>
<div id="auDivParent"> 
<table class="table_div_control">
	<tr> 
		<td>
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('auDivChild',this,'<venus:base/>/themes/<venus:theme/>/')"><fmt:message key='venus.authority.Table_of_resources_to_increase' bundle='${applicationAuResources}' />
		</td>
	</tr>
</table>
</div>

<div id="auDivChild"> 
<table class="table_div_content">
	<tr>
	    <td align="right" width="10%" nowrap><fmt:message key='venus.authority.Table_name' bundle='${applicationAuResources}' /></td>
	    <td align="left"  width="50%">
	        <select name="table_name" onchange="javascript:getFieldName(this)">
			   <%int index=0;
				for(int i=0;i<listTable.size();i++){
			       AuResourceVo tmp = (AuResourceVo) listTable.get(i);
			   %>
				<option value="<%=tmp.getTable_name()%>"  <%if(tmp.getTable_name().equals(tableName)){out.write("selected");index=i;} %>><%=tmp.getTable_name()%></option>
				<%}
				String table_chineseName="";
				if(listTable.size()>0){
					table_chineseName=((AuResourceVo)listTable.get(index)).getTable_chinesename();
				}
				%>
			</select>
			[<fmt:message key='venus.authority.Tips_Please_select_a_table' bundle='${applicationAuResources}' />]
		</td>

        <td align="left">
            <table <%--class="table_div_content"--%> >
                <tr>
                    <%--<td align="right" width="10%" nowrap><fmt:message key='venus.authority.Name' bundle='${applicationAuResources}' /></td>--%>
                    <td align="right" width="10%" nowrap><fmt:message key='venus.authority.quick_query' bundle='${applicationAuResources}' /></td>
                    <td>
                        <input name="query_keyword" id="query_keyword_id" type="text" class="text_field" inputName="<fmt:message key='venus.authority.Name' bundle='${applicationAuResources}' />" validate="isSearch">
                        <input type="button" name="Submit" class="button_ellipse" value="<fmt:message key='venus.authority.Inquiry' bundle='${applicationAuResources}' />" onClick="javascript:quicklyQuery_onClick();">
                    </td>
                </tr>
            </table>
        </td>

	</tr>
	<tr>
	    <td align="right" width="10%" nowrap><fmt:message key='venus.authority.Table_Chinese_name' bundle='${applicationAuResources}' /></td>
	    <td align="left">
			<input name="table_chinesename" type="text" class="text_field" inputName="<fmt:message key='venus.authority.Table_Chinese_name' bundle='${applicationAuResources}' />" maxlength="150"  value="<%=table_chineseName%>">
		</td>		
	</tr>
</table>
</div>
<%for(int i=0;i<listField.size();i++){
      AuResourceVo tmpfield = (AuResourceVo) listField.get(i);%>
<div id="auDivParent<%=i%>"> 
<table class="table_div_control">
	<tr> 
		<td>
<input type="checkbox" name="checkme" value=<%=i%>>&nbsp;<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('auDivChild<%=i%>',this,'<venus:base/>/themes/<venus:theme/>/')"><fmt:message key='venus.authority.Increase_the_recording_resources' bundle='${applicationAuResources}' />:<%=tmpfield.getField_name()%>
		</td>
	</tr>
</table>
</div>
<div id="auDivChild<%=i%>">
		<table class="table_div_content">
	<tr >
		<td >
		<table class="table_div_content">
		<tr >
		<td >
			<table >
				<tr >
					<td align="right" width="10%" nowrap ><fmt:message key='venus.authority.Name' bundle='${applicationAuResources}' /></td>
					<td align="left" >
						<input name="Id" type="hidden" value="<%=tmpfield.getId()%>">
						<input name="name" type="text" class="text_field" inputName="<fmt:message key='venus.authority.Name' bundle='${applicationAuResources}' />" maxlength="150" value="<%=tmpfield.getName()%>">
					</td>
				</tr>
				<tr >	
	    			<td align="right" width="10%" nowrap ><fmt:message key='venus.authority.Field_name' bundle='${applicationAuResources}' /></td>
	    			<td align="left" >
						<input name="field_name" type="text" class="text_field" inputName="<fmt:message key='venus.authority.Field_name' bundle='${applicationAuResources}' />" maxlength="150"  value="<%=tmpfield.getField_name()%>">
					</td>		
				</tr>
				<tr> 
					<td align="right" width="10%" nowrap><fmt:message key='venus.authority.Filters' bundle='${applicationAuResources}' /></td>
					<td align="left">
						<textarea name="value" class="textarea_limit_words" cols="55" rows="6" maxLength="300" id="valueId" inputName="<fmt:message key='venus.authority.Filters' bundle='${applicationAuResources}' />"><%=tmpfield.getValue()%></textarea>
			            <input name="field_chinesename" type="hidden">
			            <input name="help" type="hidden">
			            <input name="filter_type" type="hidden">
					</td>		
				</tr>
			</table>
		</td>
	</tr>
	</table>
		</td>
	</tr>	
</table>
</div>
<%}%>
<input name="access_type" type="hidden" value="1">
<input name="enable_status" type="hidden" value="1">
<input name="resource_type" type="hidden" value="4">
</form>
<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>/');
</script>
</body>
</html>

