<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="venus.oa.sysparam.vo.SysParamVo"%>
<%@ page import="venus.oa.util.GlobalConstants"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/gap-html.tld" prefix="venus" %>
<%
	SysParamVo multiTab = GlobalConstants.getSysParam(GlobalConstants.MULTITAB);
%>
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="<%=request.getContextPath()%>/themes/<venus:theme/>/css/gap31_mainstyle.css" type="text/css" rel="stylesheet" charset="GBK">
<link href="<%=request.getContextPath()%>/themes/<venus:theme/>/css/authority/ex-tabs.css.jsp" type="text/css" rel="stylesheet">
<script language="javascript" src="<%=request.getContextPath()%>/js/jquery/jquery-1.6.4.min.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/jquery/plugin/ex.tabs.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/jquery/plugin/jquery.contextmenu.r2.js"></script>
<style type="text/css">
    .bodytop{
        position:absolute;
        width:100%;
    }
</style>
</head>
<script type="text/javascript">
    function addContext() {
        $('li.tabs-selected').contextMenu('tabContext', {
            bindings: {
                'refresh': function(t) {
                    var tabTitle = $(t).children(".tabs-inner").children("span").text();
                    $('div[title='+tabTitle+']').children("iframe")[0].contentWindow.location.reload(true);
                },
                'close': function(t) {
                    var title = $(t).children(".tabs-inner").children("span").text();
                    $( "#tabPanel" ).tabs( 'close', title );
                    arr.removeByIndex(arr.getIndexByValue(title));
                    count--;
                },
                'closeothers': function(t) {
                    var title = $(t).children(".tabs-inner").children("span").text();
                    for ( var i = 0; i < arr.length; i++ ){
                        if ( arr[i] != title ){                             
                            $( "#tabPanel" ).tabs( 'close', arr[i] );
                            arr.removeByIndex(arr.getIndexByValue(arr[i]));
                            i--;
                            count--;
                        }
                    }
                },
                'closeall': function(t) {
                    for ( var i = 0; i < arr.length; i++ ){                             
                        $( "#tabPanel" ).tabs( 'close', arr[i] );
                        arr.removeByIndex(arr.getIndexByValue(arr[i]));
                        i--;
                        count--;
                    }
                }
            }
        });
    }


		Array.prototype.getIndexByValue = function(value) {
		    var idx = -1;
		    for (var i = 0;i < this.length; i++) {
		        if (this[i] == value) {
		            idx = i;
		            break;
		        }
		    }
		    return idx;
		} 

		Array.prototype.removeByIndex = function(idx) {
		    this.splice(idx,1);
		}

        var count = 0;
        var arr = new Array();
        var multiTab = '<%= multiTab.getValue() %>';

        //修改了addTab方法，当加载帮助时，选中并刷新页面。
        function addTab(title,url) {
            var openIdx = arr.getIndexByValue(title);
            if (openIdx == -1) { 
                if (count < multiTab)
                    arr.push(title);
            } else {
                $('#tabPanel').tabs('select',title);
                if ( title == "帮助" ){
                    var helpFrame = $('div[title='+title+']').children("iframe");
                    $( helpFrame ).attr( "src", "<%=request.getContextPath()%>/jsp/help/help.jsp" );
                    //$('div[title='+title+']').children("iframe").attr("contentWindow").location.reload(true);
                }
                return false;
            }        
            if (count == multiTab) {
                alert('<fmt:message key='venus.authority.Maximum_Tab'  bundle='${applicationAuResources}' ><fmt:param value="<%= multiTab.getValue() %>"/></fmt:message>!');
                return false;
            }
            count++;
            $('#tabPanel').tabs('add',{
                title:title,
                content:'<iframe name="frame' + count + '" scrolling="yes" frameborder="1"    src=' + url + ' style="width:100%;height:100%;border:1px solid #8DB2E3;"></iframe>',
                closable:true
            });    
                
          $("a.tabs-close").bind("click.tabs",function() { //创建Tab后绑定关闭事件
                count--;
                var elem = $(this).parent()[0];
                arr.removeByIndex(arr.getIndexByValue($(elem).text()));
           })                  
           
           if($.browser.msie && $.browser.version == "6.0") { //针对IE6需要手动刷新iframe的处理
                document.frames['frame' + count].location.reload();
            }     
            addContext();
        
             
        }
        
        //设置外部文件加载完成后标签页高度tabPanel，并刷新标签。
        function  setTabsHeight(parentHeight){
            document.getElementById('tabPanel').style.height = parentHeight + 'px';
            $('div.tabs-container', document.getElementById('tabPanel')).tabs('resize');  
        }
        
</script>
<body  height="100%" onResize="jQuery('#tabPanel').tabs('resize');" > 
<div id="bodyTop" class="bodytop">
	<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td height="38" align="center">
          <table width="100%" border="0" cellpadding="0" cellspacing="0" background-color="#e9e9e8">
            <tr>
           <!--     <td width="420" valign="top"  class="indextitlebg">&nbsp;</td>-->
              <td background-color="#e9e9e8"></td>
              <!-- <td width="403" align="right" class="indextitlebg_right">&nbsp;</td> -->
            </tr>
          </table>
        </td>
      </tr>
      </table>
</div>
<!--当tabPanel的parent嵌套列式的frameset时，必须通过frame的id寻找到正确的frame并获取高度。framesetOfFrameId属性记录此id-->
<div id="tabPanel" class="tabs-container"  framesetOfFrameId="contentFrame")>
    <div title="<fmt:message key='venus.authority.Mainpage'  bundle='${applicationAuResources}' />" style="padding:20px;">
               <table align="center"> 
                 <tr>
                   <td align="center" valign="top"><br>
                       <br>
                       <!-- main backgroud image -->
                         <table width="580" height="283" border="0" align="center" cellpadding="0" cellspacing="0" background="<%=request.getContextPath()%>/themes/<venus:theme/>/images/au/di1_6.gif">
                     <tr>
                       <td><table width="88%" height="23" border="0" cellpadding="0" cellspacing="0">
                           <tr>
                             <td width="26%" height="23"></td>
                             <td width="34%" valign="bottom"> </td>
                             <td width="40%"><a href="main0.htm" class="a_white"></a></td>
                           </tr>
                       </table></td>
                     </tr>
                   </table></td>
                 </tr>      
                 </table>   	        
                 <table align="center">
		         <tr>
		           <td>&nbsp;</td>
		           <td align="center" class="leftfont_Content">
                       <fmt:message key='venus.authority.All_rights_reserved' bundle='${applicationAuResources}' /> &copy;2018 <fmt:message key='venus.authority.UF_Software_Engineering_Co_Ltd_GuiZhou' bundle='${applicationAuResources}' />
                   </td>
		           <td>&nbsp;</td>
		         </tr>    	                      
                 </table>
    </div>
</div>
<div class="contextMenu" id="tabContext">
    <ul>
      <li id="refresh"> &nbsp;&nbsp;刷新</li>
      <li id="close"> &nbsp;&nbsp;关闭</li>
      <li id="closeothers"> &nbsp;&nbsp;关闭其他</li>
      <li id="closeall"> &nbsp;&nbsp;关闭所有</li>
    </ul>
</div>
</body>
</html>
<script type="text/javascript">
setTabsHeight($('#contentFrame', parent.document).outerHeight()-17);
</script>
