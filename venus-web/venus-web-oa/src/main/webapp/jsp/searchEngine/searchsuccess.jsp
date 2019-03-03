<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/global.jsp" %>

<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="udp.searchengine.vo.ResultSetVo"%>
<%@ page import="udp.searchengine.vo.ResultSetBo"%>
<%@ page import="udp.searchengine.util.ISearchEngineConstant"%>
<logic:notEmpty  name="local_in_sesson_key" scope="session">
    <fmt:setLocale value="${local_in_sesson_key}" scope="session"/>
</logic:notEmpty>
<fmt:setBundle basename="ApplicationResources" scope="session" var="applicationResources"/>

<fmt:bundle basename="udp.searchengine.searchengine_resource" prefix="udp.searchengine.">
<html>
<head>

<title><fmt:message key='search_page'/></title>

<SCRIPT LANGUAGE="JavaScript">
	function Trim(string) {return string.replace(/(^\s*)|(\s*$)/g,"");}
	function Ltrim(string) {return string.replace(/(^\s*)/g,   "");}
	function Rtrim(string) {return string.replace(/(\s*$)/g,   "");}

	//按查询按钮
    function toSearchIndex() {
		var str=Trim(form.searchKey.value);
		if(str.length==0){
			alert("<fmt:message key='input_can_not_be_empty'/>");
			form.searchKey.focus();
			return false;
		}
		form.action="<venus:base/>/SearchEngineAction.do";
		form.cmd.value="onSearch";
		form.submit();
    }

    function initFocus(){
        var ctrl=document.getElementById("searchKey");
        ctrl.focus();
    }
</SCRIPT>
</head>
<body onload="initFocus()">
<script language="javascript">
	writeTableTop("<fmt:message key='query_result'/>",'<venus:base/>/themes/<venus:theme/>//');
</script>
<%

ResultSetBo bo=(ResultSetBo)request.getAttribute("result");

List result=bo.getResult();
String previousPageNum=bo.getPreviousPageNum();
String nextPageNum=bo.getNextPageNum();
String searchKey=bo.getSearchKey();
int currentPage=bo.getCurrentPage();
int pageSum=bo.getPageSum();
%>
<form action="" method="post" name="form">
<input type="hidden" name="cmd" value="">
    <table  border="0" cellspacing="0" cellpadding="0" width="100%" >
    <tr height="50" >
      <td width="50%" align="right" nowrap><input style="width:350px;" class="text_field"  id="searchKey" name="searchKey" type="text" value="<%=searchKey%>"></td>
      <td width="50%" nowrap onClick="javascript:toSearchIndex();">
          <div class="button_ellipse" style="width:80px; margin-left:10px;">
          <img src="<venus:base/>/themes/<venus:theme/>/images/icon/search.gif" class="div_control_image"><fmt:message key='search'/>
          </div>
      </td>
     </tr>

  </table>
</form>
<table width="100%"  cellSpacing="0" cellPadding="0" border="0">



<%
for(int i=0;i<10;i++){
	if(i<result.size()){
	ResultSetVo vo=(ResultSetVo)result.get(i);
	if(vo.getIndexType().equals(ISearchEngineConstant.file_index_type)){
%>
  <tr>
    <td align="center" >

		    <table border=0 cellpadding=0 cellspacing=0>
				<tr>
					<td>
						>> <a href="<venus:base/>/SearchEngineDownLoad.do?path=<%=vo.getPath()%>&file=<%=vo.getFileName()%>"><%=vo.getFileName()%></a>
					</td>
				</tr>
				<tr>
					<td width="700px">
						<font size=-1><%=vo.getContents()%></font>
			 		</td>
			 	</tr>
		 </table>
    </td>
  </tr>
  <%}else{%>

    <tr>
    <td align="center" >

			<table border=0 cellpadding=0 cellspacing=0>
				<tr>
					<td height="30">
						>> <a style="font-weight: bold; text-decoration:underline; "
                              <%if(!(vo.getPath()==null || vo.getPath().length()==0)){%>target="_blank"<%}%>
                              href='<%if(vo.getPath()==null || vo.getPath().length()==0){%>javascript:sAlert("<%=vo.getContents()%>");<%}else{%><venus:base/>/api/document.page?id=<%=vo.getDataId()%><%}%>'><%=(vo.getPath()!=null&&!"".equals(vo.getPath()))?vo.getPath():searchKey%>
                        </a>
					</td>
				</tr>
				<tr>
					<td width="700px">
						<font size=-2><%=vo.getContents()%></font>
			 		</td>
			 	</tr>
			 </table>
    </td>
  </tr>
  <%}%>
  <tr>
    <td align="left">&nbsp;</td>
  </tr>


<%
}else{
%>


<%
}
}
%>
</table>
<hr color=#dfdfdf SIZE=1/>
<table border="0" cellspacing="0" cellpadding="0" width="100%"  >
<tr height="30">
    <td width="50%" nowrap align="right">
        <fmt:message key='about_items_accord_with_query_conditions_current_item_current_page_total_pages'>
            <fmt:param>
                <b><%=bo.getResultLength()%></b>
            </fmt:param>
            <fmt:param>
                <b><%=searchKey%></b>
            </fmt:param>
            <fmt:param>
                <b><%=bo.getStartItem()%></b>-<b><%=bo.getEndItem()%></b>
            </fmt:param>
            <fmt:param>
                <%=currentPage%>
            </fmt:param>
            <fmt:param>
                <%=pageSum%>
            </fmt:param>
        </fmt:message>
    </td>
    <td nowrap style="padding-left:100px;">
        <a href="javascript:onFirstPage();"><img src="<venus:base/>/themes/<venus:theme/>/images/icon_page_frist.gif"   width="10" height="12" border='0' alt="<fmt:message bundle='${applicationResources}' key='home_page' />"></a>
        <a href="javascript:onPreviousPage();"><img src="<venus:base/>/themes/<venus:theme/>/images/icon_page_prev.gif"  width="10" height="12" border='0' alt="<fmt:message bundle='${applicationResources}' key='previous_page' />" ></a>
        <a href="javascript:onNextPage();"><img src="<venus:base/>/themes/<venus:theme/>/images/icon_page_next.gif"  width="10" height="12" border='0' alt="<fmt:message bundle='${applicationResources}' key='next_page' />" ></a>
        <a href="javascript:onLastPage();"><img src="<venus:base/>/themes/<venus:theme/>/images/icon_page_last.gif" width="10" height="12" border='0' alt="<fmt:message bundle='${applicationResources}' key='last_page' />" ></a>
    </td>
</tr>
</table>
<%
if(!searchKey.equals("")){



%>
<!--
<table width="100%"  border="0">
<tr>
<td  align="center">
<table width="50%"  border="0">
  <tr>
    <td align="center"><a href="<venus:base/>/SearchEngineAction.do?cmd=onFirstPage&searchKey=<%=searchKey%>">第一页</a></td>
    <td align="center"><a href="<venus:base/>/SearchEngineAction.do?cmd=onPreviousPage&searchKey=<%=searchKey%>&startPlace=<%=previousPageNum%>">上一页</a></td>
    <td align="center"><a href="<venus:base/>/SearchEngineAction.do?cmd=onNextPage&searchKey=<%=searchKey%>&startPlace=<%=nextPageNum%>">下一页</a></td>
    <td align="center"><a href="<venus:base/>/SearchEngineAction.do?cmd=onLastPage&searchKey=<%=searchKey%>">最后一页</a></td>
  </tr>
</table>
</td>
</tr>
</table>
-->
<%}%>
<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>//');
</script>
</body>
</html>
<script LANGUAGE="JScript">
 function sAlert(contents){
   var ob= new Array();
   ob[0] = contents;
   window.showModalDialog("<venus:base/>/jsp/searchEngine/success.jsp", ob);
 }
		//第一页
	function onFirstPage(){
		var str=Trim(form.searchKey.value);
		if(str.length!=0){
			location.href="<venus:base/>/SearchEngineAction.do?cmd=onFirstPage&searchKey=<%=searchKey%>";
		}


	}
	//上一页
	function onPreviousPage(){
		var str=Trim(form.searchKey.value);
		if(str.length!=0){
			location.href="<venus:base/>/SearchEngineAction.do?cmd=onPreviousPage&searchKey=<%=searchKey%>&startPlace=<%=previousPageNum%>";
		}


	}
	//下一页
	function onNextPage(){
		var str=Trim(form.searchKey.value);
		if(str.length!=0){
			location.href="<venus:base/>/SearchEngineAction.do?cmd=onNextPage&searchKey=<%=searchKey%>&startPlace=<%=nextPageNum%>";
		}


	}
	//最后一页
	function onLastPage(){
		var str=Trim(form.searchKey.value);
		if(str.length!=0){
			location.href="<venus:base/>/SearchEngineAction.do?cmd=onLastPage&searchKey=<%=searchKey%>";
		}


	}
</script>
</fmt:bundle>
