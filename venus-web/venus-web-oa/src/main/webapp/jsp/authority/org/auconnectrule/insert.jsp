<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/jsp/include/global.jsp"%>
<%@ page
	import="java.util.List,java.util.Iterator,java.util.ArrayList,java.util.HashMap,java.util.Map"%>
<%@ page import="org.apache.struts.util.LabelValueBean"%>
<%@ page import="venus.authority.org.auconnectrule.vo.AuConnectRuleVo"%>
<%@ page import="venus.authority.org.auconnectrule.util.IConstants"%>
<%@ page import="venus.authority.util.VoHelperTools"%>
<%@ page import="venus.commons.xmlenum.EnumRepository"%>
<%@ page import="venus.commons.xmlenum.EnumValueMap"%>
<%@ page import="venus.authority.util.GlobalConstants"%>
<%
	String linkage = GlobalConstants.getLinkage();
	Map relationMap = (Map) request.getAttribute(IConstants.REQUEST_RELATION_TYPE);
	Map chilePartyMap = (Map) request.getAttribute(IConstants.REQUEST_CHILD_PARTYTYPE);
	Map parentPartyMap = (Map) request.getAttribute(IConstants.REQUEST_PARENT_PARTYTYPE);
	EnumRepository er = EnumRepository.getInstance();
	er.loadFromDir();
	//团体关系类型分类
	EnumValueMap relationTypeMap = er.getEnumValueMap("RelationTypeStatus");
	List partyRelationKeyList = relationTypeMap.getEnumList();
	//团体类型分类
	EnumValueMap typeMap = er.getEnumValueMap("TypeStatus");
	List partyKeyList = typeMap.getEnumList();
    //判断是否为修改页面
	boolean isModify = false;
	if(request.getParameter("isModify") != null) {
		isModify = true;
	}
%>
<title><%=isModify?venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Edit_Template"):venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.New_Template")%></title>
<script>
	function insert_onClick(){  //增加记录
		var checkSelectValue = document.getElementById("parent_partytype_id");
		if(checkSelectValue.value==''){
			alert("<fmt:message key='venus.authority.Please_select_the_type_of_parent_groups' bundle='${applicationAuResources}' />!");
			return;
		}
		checkSelectValue = document.getElementById("child_partytype_id");
		if(checkSelectValue.value==''){
			alert("<fmt:message key='venus.authority.Please_select_the_type_of_sub_groups' bundle='${applicationAuResources}' />!");
			return;
		}
		if(checkAllForms()){
    		<%--form.action = "<venus:base/>/AuConnectRuleAction.do?cmd=insert";--%>
			form.action = "<venus:base/>/auConnectRule/insert";
    		form.submit();
    	}
  	}
  	function update_onClick(){
  		<%--form.action = "<venus:base/>/AuConnectRuleAction.do?cmd=update";--%>
		form.action = "<venus:base/>/auConnectRule/update";
  		form.submit();
  	}
	function CLASS_LIANDONG_YAO(array)
  {
   //数组，联动的数据源
  	this.array=array; 
  	this.indexName='';
  	this.obj='';
  	//设置子SELECT
	// 参数：当前onchange的SELECT ID，要设置的SELECT ID
      this.subSelectChange=function(selectName1,selectName2)
  	{
  	//try
  	//{
    var obj1=document.all[selectName1];
    var obj2=document.all[selectName2];
    var objName=this.toString();
    var me=this;
  
    obj1.onchange=function()
    {
    	
    	me.optionChange(this.options[this.selectedIndex].value,obj2.id)
    }

  	}
  	//设置第一个SELECT
	// 参数：indexName指选中项,selectName指select的ID
  	this.firstSelectChange=function(indexName,selectName)  
  	{
  	this.obj=document.all[selectName];
  	this.indexName=indexName;
  	this.optionChange(this.indexName,this.obj.id)

  	}
  
  // indexName指选中项,selectName指select的ID
  	this.optionChange=function (indexName,selectName)
  	{
    var obj1=document.all[selectName];
    var me=this;
    obj1.length=0;
    obj1.options[0]=new Option("<fmt:message key='venus.authority.Please_select' bundle='${applicationAuResources}' />",'');
    for(var i=0;i<this.array.length;i++)
    {	
    
    	if(this.array[i][1]==indexName)
    	{
    	//alert(this.array[i][1]+" "+indexName);
      obj1.options[obj1.length]=new Option(this.array[i][2],this.array[i][0]);
    	}
    }
  	}
  	
  }
</script>

</head>
<body>
<script language="javascript">
	writeTableTop('<%=isModify?venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Modify_page"):venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.New_page")%>','<venus:base/>/themes/<venus:theme/>/');
</script>

<form name="form" method="post"><input type="hidden" name="id" value="">
<table class="table_noframe">
	<tr>
		<td valign="middle"><input name="button_save" type="button"
			class="button_ellipse" value="<fmt:message key='venus.authority.Save' bundle='${applicationAuResources}' />"
			onClick="javascript:<%=isModify?"update_onClick()":"insert_onClick()"%>;">
		<input name="button_cancel" type="button" class="button_ellipse"
			value="<fmt:message key='venus.authority.Cancel' bundle='${applicationAuResources}' />" onClick="javascript:returnBack();"></td>
	</tr>
</table>

<div id="auDivParent1">
<table class="table_div_control">
	<tr>
		<td><img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif"
			class="div_control_image"
			onClick="javascript:hideshow('auDivChild1',this,'<venus:base/>/themes/<venus:theme/>/')"><%=isModify?venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Modify_the_rules_of_group_connection"):venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.New_groups_joining_rules")%>
		</td>
	</tr>
</table>
</div>

<div id="auDivChild1">
<table class="table_div_content">
	<tr>
		<td align="right" width="20%" nowrap><span class="style_required_red">*
		</span><fmt:message key='venus.authority.Name' bundle='${applicationAuResources}' /></td>
		<td align="left"><input name="Id" type="hidden" value=""> <input
			name="name" type="text" class="text_field" inputName="<fmt:message key='venus.authority.Name' bundle='${applicationAuResources}' />"
			validate="notNull;isSearch" maxlength=50 value=""></td>
	</tr>
	<%if("1".equals(linkage)){ %>
	<tr>
		<td align="right"><fmt:message key='venus.authority.Group_type_of_relationship' bundle='${applicationAuResources}' /></td>
		<td align="left"><select id='relation_type_id' name="relation_type_id">
			<OPTION selected></OPTION>
		</select></td>
	</tr>
	<tr>
		<td align="right"><fmt:message key='venus.authority.Types_of_parent_groups' bundle='${applicationAuResources}' /></td>
		<td align="left"><select id='parent_partytype_id'
			name="parent_partytype_id">
			<OPTION selected></OPTION>
		</select></td>
	</tr>
	<tr>
		<td align="right"><fmt:message key='venus.authority.Sub_group_type' bundle='${applicationAuResources}' /></td>
		<td align="left"><select id='child_partytype_id'
			name="child_partytype_id">
			<OPTION selected></OPTION>
		</select></td>
	</tr>
	<script language="javascript">
	//数据源
  var array=new Array();
  <%  	
  	int index=0;
  	List subPartyTypeList=(List)chilePartyMap.get(typeMap.getValue(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Staff")));
	if(subPartyTypeList==null)subPartyTypeList=new ArrayList();
	for (int i = 0; i < partyRelationKeyList.size(); i++) {
		String value= partyRelationKeyList.get(i).toString();
        String key = relationTypeMap.getValue(value);
		List relationTypeList=(List)relationMap.get(key);
		List partyTypeList=(List)parentPartyMap.get(key);
		for(int j=0;j<relationTypeList.size();j++){
			  LabelValueBean tmp = (LabelValueBean) relationTypeList.get(j);
	%>
			       		array[<%=index++%>]=new Array("<%=tmp.getLabel()%>","<fmt:message key='venus.authority.Root' bundle='${applicationAuResources}' />","<%=tmp.getValue()%>");
			       		<%
			       		for(int k=0;k<partyTypeList.size();k++){
			       		LabelValueBean partytmp = (LabelValueBean) partyTypeList.get(k);
			       		%>
			       		array[<%=index++%>]=new Array("<%=partytmp.getLabel()%>","<%=tmp.getLabel()%>","<%=partytmp.getValue()%>");
			       		<%}
			      }

			       		for(int k=0;k<partyTypeList.size();k++){
			       		LabelValueBean partytmp = (LabelValueBean) partyTypeList.get(k);
			       		for(int h=0;h<partyTypeList.size();h++){
			       		LabelValueBean subpartytmp = (LabelValueBean) partyTypeList.get(h);
			       		%>
			       		array[<%=index++%>]=new Array("<%=subpartytmp.getLabel()%>","<%=partytmp.getLabel()%>","<%=subpartytmp.getValue()%>");
			       		<%}
			       		for(int l=0;l<subPartyTypeList.size();l++){
			       		LabelValueBean persontmp = (LabelValueBean) subPartyTypeList.get(l);
			       		%>
			       		array[<%=index++%>]=new Array("<%=persontmp.getLabel()%>","<%=partytmp.getLabel()%>","<%=persontmp.getValue()%>");
			       		<%}
			       		}
			   }
	%>
	
  //这是调用代码
  var liandong=new CLASS_LIANDONG_YAO(array) //设置数据源
  liandong.firstSelectChange("<fmt:message key='venus.authority.Root' bundle='${applicationAuResources}' />","relation_type_id"); //设置第一个选择框
  liandong.subSelectChange("relation_type_id","parent_partytype_id"); //设置子级选择框
  liandong.subSelectChange("parent_partytype_id","child_partytype_id");
	
	</script>
	<%} else { %>
	<tr>
		<td align="right"><fmt:message key='venus.authority.Group_type_of_relationship' bundle='${applicationAuResources}' /></td>
		<td align="left"><select name="relation_type_id">
			<%  
				for (int i = 0; i < partyRelationKeyList.size(); i++) {
					String value= partyRelationKeyList.get(i).toString();
            		String key = relationTypeMap.getValue(value);
					List relationTypeList=(List)relationMap.get(key);
				%>
			<optgroup label="<%=value%>">
				<%
			     	for(int j=0;j<relationTypeList.size();j++){
			       		LabelValueBean tmp = (LabelValueBean) relationTypeList.get(j);
			   %>
				<option value="<%=tmp.getLabel()%>"><%=tmp.getValue()%></option>
				<%}}%>
		</select></td>
	</tr>
	<tr>
		<td align="right"><fmt:message key='venus.authority.Types_of_parent_groups' bundle='${applicationAuResources}' /></td>
		<td align="left"><select name="parent_partytype_id">
			<%  
				for (int i = 0; i < partyRelationKeyList.size(); i++) {
					String value= partyRelationKeyList.get(i).toString();
            		String key = relationTypeMap.getValue(value);
					List partyTypeList=(List)parentPartyMap.get(key);
				%>
			<optgroup label="<%=value%>">
				<%
			     	for(int j=0;j<partyTypeList.size();j++){
			       		LabelValueBean tmp = (LabelValueBean) partyTypeList.get(j);
			   %>
				<option value="<%=tmp.getLabel()%>"><%=tmp.getValue()%></option>
				<%}}%>
		</select></td>
	</tr>
	<tr>
		<td align="right"><fmt:message key='venus.authority.Sub_group_type' bundle='${applicationAuResources}' /></td>
		<td align="left"><select name="child_partytype_id">
			<%  
				for (int i = 0; i < partyKeyList.size(); i++) {
					String value= partyKeyList.get(i).toString();
            		String key = typeMap.getValue(value);
					List partyTypeList=(List)chilePartyMap.get(key);
				%>
			<optgroup label="<%=value%>">
				<%
			     	for(int j=0;j<partyTypeList.size();j++){
			       		LabelValueBean tmp = (LabelValueBean) partyTypeList.get(j);
			   %>
				<option value="<%=tmp.getLabel()%>"><%=tmp.getValue()%></option>
				<%}}%>
		</select></td>
	</tr>
	<%} %>
	<tr>
		<td align="right" width="10%" nowrap><fmt:message key='venus.authority.Notes' bundle='${applicationAuResources}' /></td>
		<td align="left"><textarea name="remark" cols="60" rows="5"
			class="textarea_limit_words"></textarea></td>

	</tr>
</table>
</div>
</form>
<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>/');
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

