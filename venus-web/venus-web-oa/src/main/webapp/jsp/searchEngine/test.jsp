<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/global.jsp" %>

<fmt:bundle basename="udp.searchengine.searchengine_resource" prefix="udp.searchengine.">
<html>
<head>
<title><fmt:message key='search_engine_test_page'/></title>
<SCRIPT LANGUAGE="JScript">
  function   String.prototype.Trim()   {return   this.replace(/(^\s*)|(\s*$)/g,"");}   
  function   String.prototype.Ltrim(){return   this.replace(/(^\s*)/g,   "");}   
  function   String.prototype.Rtrim(){return   this.replace(/(\s*$)/g,   "");}   

	function toInitIndex_onClick() {  //初始化索引
			form.action="<venus:base/>/SearchEngineAction.do";
			form.cmd.value="initIndex";
			form.target="body1";
			form.submit();
	}
	function toSearch_onClick() {  //搜索关键字
			//var str=form.searchKey.value.Trim();
			//if(str.length==0){
			//     alert("输入框不能为空!");
			//     form.searchKey.focus();
			//     return false;
			//}
			
			form.action="<venus:base/>/SearchEngineAction.do";
			form.cmd.value="onSearch";
			form.target="body1";
			form.submit();
	}
	function toLastPage_onClick() {  //翻到最后一页
			var str=form.searchKey.value.Trim();
			if(str.length==0){
			     alert("<fmt:message key='input_can_not_be_empty'/>");
			     form.searchKey.focus();
			     return false;
			}
			
			form.action="<venus:base/>/SearchEngineAction.do";
			form.cmd.value="onLastPage";
			form.target="body1";
			form.submit();
	}
	function toPreviousPage_onClick() {  //向前一页
			var str=form.searchKey.value.Trim();
			if(str.length==0){
			     alert("<fmt:message key='input_can_not_be_empty'/>");
			     form.searchKey.focus();
			     return false;
			}
			
			form.action="<venus:base/>/SearchEngineAction.do?startPlace=2";
			form.cmd.value="onPreviousPage";
			form.submit();
	}
	function toNextPage_onClick() {  //向后一页
			var str=form.searchKey.value.Trim();
			if(str.length==0){
			     alert("<fmt:message key='input_can_not_be_empty'/>");
			     form.searchKey.focus();
			     return false;
			}
			
			form.action="<venus:base/>/SearchEngineAction.do?startPlace=4";
			form.cmd.value="onNextPage";
			form.submit();
	}
	function operatFile() { 
			
			form.action="<venus:base/>/SearchEngineAction.do";
			form.cmd.value="onQueryFileIndex";
			form.target="body1";
			form.submit();
	}
	function dbCreatIndex() { 
			form.action="<venus:base/>/TestAction.do";
			form.cmd.value="onQuery";
			form.target="body1";
			form.submit();
			//location.href="<venus:base/>/TestAction.do?cmd=onQuery";
            //location.href="<venus:base/>/jsp/test/insert.jsp";
	}
</SCRIPT>
</head>
<style type="text/css">
<!--
body {
	background-color: #004d9f;
}
-->
</style>
<body>
<form name="form" method="post" action="<venus:base/>/SearchEngineAction.do">
<input type="hidden" name="cmd" value="">
			
    <table cellpadding="0" cellspacing="0" class="table_div_content_frame" height="30">
      <tr>
        <td width="11" valign="bottom" align="left"><img src="<%=request.getContextPath()%>/images/templatestyle/main0_15.gif" width="11" height="11"></td>
        <td >
		<table align="left">
				<!--<tr> 
					<td class="button_ellipse" onClick="javascript:toInitIndex_onClick();"><img src="<venus:base/>/themes/<venus:theme/>/images/icon/add.gif" class="div_control_image">初始化索引库</td>
					
				</tr>-->
				<tr> 
					<td class="button_ellipse" onClick="javascript:toSearch_onClick();"><img src="<venus:base/>/themes/<venus:theme/>/images/icon/arrow.gif" class="div_control_image"><fmt:message key='search_keyword'/></td>
					<td class="button_ellipse" onClick="javascript:operatFile();"><img src="<venus:base/>/themes/<venus:theme/>/images/icon/arrow.gif" class="div_control_image"><fmt:message key='document_operation'/></td>
					<td class="button_ellipse" onClick="javascript:dbCreatIndex();"><img src="<venus:base/>/themes/<venus:theme/>/images/icon/arrow.gif" class="div_control_image"><fmt:message key='create_database_index'/></td>
				</tr>
		</table>
		</td>
        <td width="11" align="right" valign="bottom"><img src="<%=request.getContextPath()%>/themes/<venus:theme/>/images/templatestyle/main0_18.gif" width="11" height="11"></td>
      </tr>
    </table>

</form>
</body>
</html>
</fmt:bundle>