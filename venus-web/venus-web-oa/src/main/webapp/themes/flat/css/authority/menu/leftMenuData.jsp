<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ include file="/jsp/include/global.jsp"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="venus.oa.helper.LoginHelper" %>
<%@ page import="venus.oa.authority.aufunctree.vo.AuFunctreeVo"%>
<%@ page import="venus.oa.service.sys.vo.SysParamVo"%>
<%@ page import="venus.oa.util.GlobalConstants"%>
<%
    String  totalCode = request.getParameter("totalCode");
    SysParamVo multiTab = GlobalConstants.getSysParam(GlobalConstants.MULTITAB);
%>
<%!
    private List removeDupList(List list) {
        Map map = new HashMap();
        for (int i = 0; i < list.size(); i++) {
            AuFunctreeVo vo = (AuFunctreeVo)list.get(i);
            map.put(vo.getTotal_code(),vo);
        }
        List newList = new ArrayList();
        for (Iterator it = map.keySet().iterator();it.hasNext(); ) {
            AuFunctreeVo vo = (AuFunctreeVo)map.get(it.next());
            newList.add(vo);
        }
        return newList;
    }
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="<%=request.getContextPath()%>/themes/<venus:theme/>/css/authority/menu.css" type="text/css" rel="stylesheet" charset="UTF-8">
<script language=javascript>
    var old_menu = '';
    var old_cell = '';


    //点击二级菜单时调用
    function menu_onclick(submenu ,cellbar,i) {
        if (submenu == null || submenu == undefined || cellbar == null || cellbar == undefined)
            return false;
        if( old_menu != submenu ) {
            if( old_menu !='' ) { //旧菜单缩回
                jQuery("#"+old_menu).slideUp(200);
                jQuery("#"+old_cell).attr("src", "<%=request.getContextPath()%>/images/au/leftmenu_close.gif");
            }
            //新菜单伸出
            jQuery(".tree_Menu:eq("+i+")").css('display','block');//td修改css
            jQuery("#"+submenu).slideDown(200);
            jQuery("#"+cellbar).attr("src",  "<%=request.getContextPath()%>/images/au/leftmenu_open.gif" );
            old_menu = submenu;
            old_cell = cellbar;
        } else {
            //新菜单收回
            jQuery("#"+submenu).slideUp(200);
            jQuery("#"+cellbar).attr("src",  "<%=request.getContextPath()%>/images/au/leftmenu_close.gif" );
            old_menu = '';
            old_cell = '';
             tr_num='';
        }
    }

    function showTabPage(title,link) {
       top.document.getElementById("contentFrame").contentWindow.addTab(title,link);
    }

    //点击节点菜单时，显示菜单路径
    function submenu_onclick(totalCode) {
	    jQuery.ajax({
	        type: "GET",
	        url:"<%=request.getContextPath()%>/jsp/authority/menu/navMenuData.jsp",
	        data: "menuCode=" + totalCode,
	        async: true,
	        success: function(result) {
	                if (result != null ) {
	                   writeTableTopList(result);
	                }
	        },
	        error: function(xmlhttp,result) {
	            alert("request error!");
	            writeTableTopList();
	        }
	    });
    }

    //在地址栏显示菜单路径
	function writeTableTopList(pageTitle) {
	    if(pageTitle == undefined) {
	       pageTitle = "";
	    }
	    var varFrame;
	    if (document.all)
	       varFrame = parent.parent.frames["headFrame"].document;
	    else
	       varFrame = parent.parent.document.getElementById("headFrame").contentWindow.document;
	    if(varFrame != undefined && varFrame != null) {
	        var mySpan = varFrame.getElementById("menuPath");
	         if(mySpan != undefined && mySpan != null) {
	            mySpan.innerHTML= pageTitle;
	        }
	    }
	}

    //这是把事件动作绑定到菜单上的函数
    function attachXMenu(objId) {
        var tds=objId.getElementsByTagName('td');
        for(var i=0;i<tds.length;i++) {
            with(tds[i]) {
                onmouseover=function() {
                    with(this) {
                        style.background='#'; //这是鼠标移上去时的背景颜色
                        style.border='0px solid #8fb4d1'; //边框
                        style.color='#ff0000'; //文字颜色
                    }
                }
                onmouseout=function() {
                    with(this) {
                        style.background='#'; //这是鼠标离开时的背景颜色
                        style.border='0px solid #afcce2'; //边框
                        style.color=''; //文字颜色
                    }
                }
            }
        }
    }
</script>
</head>
<%
     List twoLevelMenu  = new ArrayList();
     twoLevelMenu  = LoginHelper.getNextLevelMenu(request,totalCode);
     if (twoLevelMenu != null && twoLevelMenu.size() > 0 ) {
%>
<body class="mybody" onload = "menu_onclick('submenu0','bar0','0')">
<%
     List folderMenu = new ArrayList();
     List leafMenu = new ArrayList(); //确保只有二级的菜单项在最上面
     for (int i = 0; i < twoLevelMenu.size(); i++) {
         AuFunctreeVo vo = (AuFunctreeVo)twoLevelMenu.get(i);
         if ("1".equals(vo.getIs_leaf())) {
             leafMenu.add(LoginHelper.getPreviousLevelMenu(request,vo.getTotal_code()));
         } else {
             folderMenu.add(vo);
         }
     }
     leafMenu = removeDupList(leafMenu);
     leafMenu.addAll(folderMenu);
     for(int i = 0; i < leafMenu.size(); i++) { //二级菜单
         boolean flag = false;
         AuFunctreeVo svo = (AuFunctreeVo)leafMenu.get(i);
         List threeLevelMenuList = LoginHelper.getNextLevelMenu(request,svo.getTotal_code());
         if(threeLevelMenuList != null) {
             flag = true;
%>
    <table id="tree<%=i %>" align="center" border="0" cellpadding="0" cellspacing="0" class="tree_table">
        <tr>
            <td  class="tree_Navigation" title="<%=svo.getName()%>"  onClick="menu_onclick('submenu<%=i%>','bar<%=i%>','<%=i %>' );">
            &nbsp;&nbsp;<img id="bar<%=i%>" src="<%=request.getContextPath()%>/images/au/leftmenu_close.gif" width="16" height="16" border="0" align="absmiddle" />&nbsp;<%=svo.getName()%>
            </td>
        </tr>
        <tr>
            <td  class="tree_Menu" >
            <div id="submenu<%=i%>">
                <table class="xmenu" id="xmenu<%=i%>" width="100%" border="0" align="center"  cellpadding="0" cellspacing="3"  bgcolor="#f9fcff">
		        <%
		                }
		                for(int j = 0; j < threeLevelMenuList.size(); j++) {  // 三级菜单
		                    AuFunctreeVo tvo = (AuFunctreeVo)threeLevelMenuList.get(j);
		                    if (!"1".equals(tvo.getIs_leaf()))
		                        continue;
		        %>
	                <tr>
	                    <td>
                            <% if (tvo.getUrl() != null) {
                                boolean isTab = false;
                                if (multiTab != null) {
                                    try {
                                        if (Integer.parseInt(multiTab.getValue()) > 0)
                                            isTab = true;
                                    } catch (Exception e) {
                                    }
                                }
                                if ( isTab ) {
	                   %>
	                    <a href="javascript:void(0);" style="display:block;width:100%;" class="leftmenu" target="contentFrame" onclick="submenu_onclick('<%= tvo.getTotal_code() %>');showTabPage('<%= tvo.getName()%>','<%=request.getContextPath() %><%=tvo.getUrl()+ ((tvo.getUrl().indexOf("?")>0)?"&":"?") +"_function_id_=" + tvo.getId() %>')">
	                    <% } else{ %>
	                    <a href="<%=request.getContextPath()%><%=tvo.getUrl()%>" style="display:block;width:100%;" class="leftmenu" target="contentFrame" onclick="submenu_onclick('<%= tvo.getTotal_code() %>');">
	                    <% } }%>
	                    <img src="<%=request.getContextPath()%>/images/au/leftmenu_point.gif" width="19" height="25" border="0" align="absmiddle"/><%=tvo.getName()%>
	                    <% if (tvo.getUrl() != null) { %>
	                    </a>
	                    <%  }%>
	                  </td>
	              </tr>
              <%}  if(flag) {%>
                </table>
                <script>attachXMenu(xmenu<%=i%>);</script>
                </div>
            </td>
        </tr>
    </table>
        <%
              }
            }%>
</body>
 <% } else {%>
<body class="mybody">
    <table class="tree_table" id="tree"  border="0" cellpadding="0" cellspacing="0" align="center">
    &nbsp;&nbsp;<fmt:message key='venus.authority.nochildnode' bundle='${applicationAuResources}' />
    </table>
</body>
<%  } %>
</html>
