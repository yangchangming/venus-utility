<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/global.jsp" %>
<%@ page import="java.util.*"%>
<%@ page import="udp.quartz.scheduledata.bo.DefinitionManager"%>
<%@ page import="udp.quartz.scheduledata.bo.JobDefinition"%>
<%@ page import="udp.quartz.scheduledata.bo.JobParameter"%>
<%@ page import="udp.quartz.extend.QuartzUtil"%>

<fmt:bundle basename="udp.quartz.quartz_resource" prefix="udp.quartz.">
<%
    DefinitionManager def = (DefinitionManager)session.getServletContext().getAttribute(QuartzUtil.JOB_DEFINITIONS_PROP);
    String pageName = "/jsp/quartz/day.jsp";
    String [] jobGroups = (String[])request.getAttribute("jobGroups");
    Map paraMap = new HashMap();
    Map defMap  = new TreeMap();
    if(def!=null)defMap = def.getDefinitions();
    String jobName = "";
    
%>

<script type="text/javascript" src="<venus:base/>/js/quartz/Ajax.js"></script>
<script language="javascript">
	//动态表格(新-liang)
	//查找页面元素
	function findObj(theObj, theDoc) {
	    var p, i, foundObj;
	    if(!theDoc) theDoc = document;
	    if( (p = theObj.indexOf("?")) > 0 && parent.frames.length)
	    {
	        theDoc = parent.frames[theObj.substring(p+1)].document;
	        theObj = theObj.substring(0,p);
	    }
	    if(!(foundObj = theDoc[theObj]) && theDoc.all) foundObj = theDoc.all[theObj];
	    for (i=0; !foundObj && i < theDoc.forms.length; i++)
	        foundObj = theDoc.forms[i][theObj];
	    for(i=0; !foundObj && theDoc.layers && i < theDoc.layers.length; i++)
	        foundObj = findObj(theObj,theDoc.layers[i].document);
	    if(!foundObj && document.getElementById) foundObj = document.getElementById(theObj);
	    return foundObj;
	}

	function AddRow(){
	    //读取最后一行的行号，存放在txtTRLastIndex文本框中
	    var txtTRLastIndex = findObj("txtTRLastIndex",document);
	    var rowID = parseInt(txtTRLastIndex.value);
	    var jobTable = findObj("jobTable",document);
	    //添加下一行之前先判断当前行参数名是否有值
	    var collObjects = jobTable.rows[jobTable.rows.length-1].cells[1].getElementsByTagName("input");
	    if(collObjects[0].value==""){
	       alert('<fmt:message key="Parameter_Name_Not_Null"/>');
	        return;
	     }
	    //添加行
	    var newTR = jobTable.insertRow(jobTable.rows.length);
	    newTR.id = "Item" + rowID;
	    //添加列
	    var newNameTD=newTR.insertCell(0);
	    var newNameTD=newTR.insertCell(1);
	    newNameTD.innerHTML = "<input type='text' name='jobParameter'  id='jobParameter' class='text_field' >";
	    newNameTD.name="jobParameter";
	    var newNameTD=newTR.insertCell(2);
	    var newNameTD=newTR.insertCell(3);
	    newNameTD.innerHTML = "<input type='text' name='jobPValue'  id='jobPValue' class='text_field' >";
	    newNameTD.name="jobPValue";
	    //添加列:删除按钮
	    var newNameTD=newTR.insertCell(4);
	    //添加列内容
	    newNameTD.innerHTML = "<input type='button' value='<fmt:message key="Delete"/>' class='button_ellipse' onclick=\"DeleteRow('Item" + rowID + "')\">";
	    //将行号推进下一行
	    txtTRLastIndex.value = (rowID + 1).toString() ;
	}

	//删除指定行
	function DeleteRow(rowid){
	    var jobTable = findObj("jobTable",document);
	    var item = findObj(rowid,document);
	    //获取将要删除的行的Index
	    var rowIndex = item.rowIndex;
	    //删除指定Index的行
	    jobTable.deleteRow(rowIndex);
	}

	//选择作业
	function selectJobName(){
	        //打开窗口
	        jQuery("#dialog_window").dialog("open"); 
	        //提交请求
	        jQuery("#dialog_window").load('<venus:base/>/createJobForm.do?cmd=doChooseJobDefines',
	            function(){
	            }
	        );
	}

    //确定按钮
    function button1_onclick() {
        document.form1.jobName.value = jQuery("#jobName").val();
        changeJobOthers(jQuery("#jobName").val()); 
        //checkJobClass();
        closeDialog_onClick();
    }
    
    //关闭层
    function closeDialog_onClick() {
	    jQuery("#dialog_window").dialog("close");
	    jQuery("#dialog_window").html("");
    }

	function checkJobClass(){
	    retrieveURL('<%=request.getContextPath()%>/checkJobDefine.do?1=1','form1');
	}
	
	function changeJobOthers(obj){
	    <%if(defMap!=null)
	    {
	        for(Iterator iter=defMap.entrySet().iterator(); iter.hasNext();){
	            Map.Entry entry = (Map.Entry)iter.next();
	            jobName = (String)entry.getKey();
	            //System.out.println("jobName = "+jobName);
	            %>
	            if(obj=="<%=jobName%>"){
	            document.form1.jobGroup.value="test";
	            document.form1.className.value="<%=((JobDefinition)entry.getValue()).getClassName()%>"
	            //alert(" classname"+"<%=((JobDefinition)entry.getValue()).getClassName()%>");
	            document.form1.className.readOnly=true;
	            document.form1.description.value="<%=((JobDefinition)entry.getValue()).getDescription()%>";
	            <%
	                List paraList = ((JobDefinition)entry.getValue()).getParameters();
	                for(int i=0;i<paraList.size();i++){
	                    JobParameter jobPara = (JobParameter)paraList.get(i);
	                    //todo
	                }
	            %>
	            return;
	            }
	            <%
	        }   
	    }
	    %>
	}

	function nextPart(){
	    //作业组名可为空，为默认组  
	    if(form1.jobName.value==""){
	        alert('<fmt:message key="Please_Input_Job_Name"/>');
	        return;
	    }   
	    if(form1.className.value==""){
	        alert('<fmt:message key="Please_Input_Job_Class"/>');
	        return;
	    }   
	    //retrieveURL('<%=request.getContextPath()%>/checkJobDefine.do?1=1','form1');
	    //alert(document.getElementById(name).innerHTML);
	    //alert(document.all.jobTable.rows[0].cells[3].innerHTML);
	    form1.action = "<venus:base/>/createJob.do";
	    form1.submit();
	}

    var KEYCODE_ESC = 27;
    
    jQuery(function() {
        jQuery("#dialog_window").dialog({ modal: true, resizable:false, autoOpen:false, height:100, width:300, overlay: { opacity: 0.5, background: "black" }});
       
        jQuery(".ui-dialog-titlebar-close").click(function() { //点击关闭图标清空div
            jQuery("#dialog_window").html("");
        });
        
        jQuery(document).keydown(function(event) { //点击ESC清空div
            if (event.keyCode == KEYCODE_ESC) {
                jQuery("#dialog_window").dialog("close");
                jQuery("#dialog_window").html("");
            }
        });
    });

</script>
<head>
<style>
/* 按钮的样式表 */
.button_ellipse2 {
    BORDER-RIGHT: #2C59AA 1px solid; 
    PADDING-RIGHT: 2px; 
    BORDER-TOP: #2C59AA 1px solid; 
    PADDING-LEFT: 2px; 
    FONT-SIZE: 12px; 
    FILTER: progid:DXImageTransform.Microsoft.Gradient(GradientType=0, StartColorStr=#ffffff, EndColorStr=#C3DAF5); 
    BORDER-LEFT: #2C59AA 1px solid; 
    CURSOR: hand; 
    COLOR: black; 
    PADDING-TOP: 2px; 
    BORDER-BOTTOM: #2C59AA 1px solid;
    height: 22px;
}
</style>
</head>

<body>

<script language="javascript">
    writeTableTop('<fmt:message key="Create_Job"/>','<venus:base/>/themes/<venus:theme/>/');
</script>

<form name="form1" id="form1" action="<%=request.getContextPath()%>/createJobForm.do?cmd=doCreateJobForm" method="post" >
<div id="ccParentq"> 
<table class="table_div_control">
    <tr>
        <td>
            <img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" onClick="javascript:hideshow('ccChildq',this,'<venus:base/>/themes/<venus:theme/>/')">
            <fmt:message key="Create_Job"/>
        </td>
    </tr>
</table>
</div>

<div id="ccChildq"><span id="pos"></span>
<table id="jobTable" class="table_div_content" border="0">
    <tr>
        <td width="100" align="right"><fmt:message key="Job_Group"/></td>
        <td width="210"><input name="jobGroup" type="text" class="text_field" inputName='<fmt:message key="Job_Group"/>' validate="isSearch" ></td>
        <td width="100">&nbsp;<!--<input type="button" value="选择" onclick="selectJobGroup()">--></td>
        <td width="210">&nbsp;</td>
        <td>&nbsp;</td>
        <td width="110">&nbsp;</td>
    </tr>
    <tr>
        <td align="right"><fmt:message key="Job_Name"/></td>
        <td><input name="jobName" type="text" class="text_field" readOnly="true" inputName='<fmt:message key="Job_Name"/>' validate="isSearch"  ></td>
        <!--<td><select id="jobName" style="width:150" onclick="byName();">
            <%
                
                for(Iterator iter=defMap.entrySet().iterator();iter.hasNext();){
                    Map.Entry entry = (Map.Entry)iter.next();
                    out.println("<option value='"+entry.getKey()+"'>"+ entry.getKey()+"</option>");
                }
            %>
            </select>
        </td>
        -->
        <td><input type="button" class="button_ellipse" value='<fmt:message key="Choose"/>' onclick="selectJobName()"></td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td align="right"><fmt:message key="Job_Class"/></td>
        <td><input name="className" type="text" class="text_field" inputName='<fmt:message key="Job_Class"/>' validate="isSearch" readOnly="true" onchange="retrieveURL('<%=request.getContextPath()%>/checkJobDefine.do?1=1','form1');"></td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td><font color=red><span id="errorInfo">&nbsp;</span></font></td>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td align="right"><fmt:message key="Job_Description"/></td>
        <td><input name="description" type="text" class="text_field" inputName='<fmt:message key="Job_Description"/>' validate="isSearch" ></td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td align="right"><fmt:message key="Job_Parameter"/></td>
        <td><input name="jobParameter" id="jobParameter" type="text" class="text_field" inputName='<fmt:message key="Job_Parameter"/>' validate="isSearch"  ></td>
        <td align="right"><fmt:message key="Parameter_Values"/></td>
        <td><input name="jobPValue" id="jobPValue" type="text" class="text_field" inputName='<fmt:message key="Parameter_Values"/>' validate="isSearch" ></td>
        <td><input type="button" class="button_ellipse" value='<fmt:message key="Add"/>' onclick="AddRow()"></td>
        <td>&nbsp;</td>
    </tr>
</table>
</div> 
<div>
<input type="button" class="button_ellipse" name="buttonNext" value='<fmt:message key="Next"/>' onClick="javascript:nextPart();" />
<input name='txtTRLastIndex' type='hidden' id='txtTRLastIndex' value="2" />
</div>
</form>
<!-- 参照显示层 -->
<div id="dialog_window" title= '<fmt:message key="Create_Job"/>' ></div>
</fmt:bundle>

<script language="javascript">
    writeTableBottom('<venus:base/>/themes/<venus:theme/>/');
</script>

</body>
</html>
