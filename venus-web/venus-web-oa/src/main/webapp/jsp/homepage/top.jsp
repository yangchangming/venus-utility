<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/jsp/include/global.jsp" %>
<%@ taglib uri="/WEB-INF/gap-authority.tld" prefix="au" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>VENUS Authority System</title>
<style>
    .skinblue {
        width: 10px;
    }

    .skinblue img {
        border: 1px solid #ffffff;
    }

    .skinred {
        width: 70px;
        padding-left: 10px;
    }

    .skinred img {
        border: 1px solid #ffffff;
    }
</style>
<link href="<%=request.getContextPath()%>/themes/<venus:theme/>/css/gap31_mainstyle.css" type="text/css"
      rel="stylesheet" charset='UTF-8'>
<script language="javascript">
    function showHelp() {
        //window.open("../../htm/handbook.mht");
        // Link your help system
    }
    function modifyPassword() {
        window.showModalDialog("../../jsp/authority/au/auuser/modifyPasswordFrame.jsp",
                new Object(), 'dialogWidth=640px;dialogHeight=480px;resizable:yes;status:no;scroll:no;');
    }

    function changeLocal(local) {
        if (!local || local == "") return;
        var url = "../../jsp/common/changeLocal.jsp?local=" + local;
        jQuery.ajax({url: url, async: false, cache: false});
        top.window.location.reload();
    }

    function changeThemes(theme){
        if(theme == "blue"){
            theme = "default";
        }
        var url = "<%=request.getContextPath()%>/jsp/include/setTheme.jsp?theme=" + theme;
        jQuery.get(url, function () {
            top.window.location.reload();
        });
    }

</script>
</head>
<body>
<table width="100%" height="99" border="0" cellpadding="0" cellspacing="0"
       background="<%=request.getContextPath()%>/themes/<venus:theme/>/images/au/di1_1.gif">
    <tr>
        <td valign="top">
            <table width="100%" height="22" border="0" cellpadding="0" cellspacing="0">
                <tr>
                    <td class="date">&nbsp;<font id="rmDynamicClock" title="祝您工作愉快！"></font><img
                            src="<venus:base/>/themes/<venus:theme/>/images/au/ufida_logo<au:i18next/>.gif"/></td>
                    <td>&nbsp;</td>
                    <td width="633" align="right">
                        <table width="50%" border="0" cellpadding="0" cellspacing="0" align="right">
                            <tr>
                                <td class="toptext"></td>
                                <td nowrap="nowrap"><span class="topfont"><!-- &nbsp;&nbsp;|&nbsp;VENUS0.1&nbsp;Edition --></span>
                                </td>
                                <td align="right">
                                    <!-- <img src="../../images/index/di1_2.gif" width="23" height="22"/> --></td>

                                <td align="left" nowrap="nowrap" class="skinblue" style="padding-right:10px;">
                                    <a href="javascript:void(0);" onclick="changeThemes('flat');">
                                        <img src="<%=request.getContextPath()%>/images/skin/au/cyan-blue.gif"/>
                                    </a>
                                </td>

                                <td align="right" nowrap="nowrap" class="skinblue">
                                    <a href="javascript:void(0);" onclick="changeThemes('blue');">
                                        <img src="<%=request.getContextPath()%>/images/skin/au/blue.gif"/>
                                    </a>
                                </td>

                                <td align="left" nowrap="nowrap" class="skinred">
                                    <a href="javascript:void(0);" onclick="changeThemes('red');">
                                        <img src="<%=request.getContextPath()%>/images/skin/au/red.gif"/>
                                    </a>
                                </td>

                                <td width="100%" nowrap="nowrap" align="right">
                                    <a href="../main.jsp" target="_parent" style="color:#f2f2f2; text-decoration:none;">
                                        <span><fmt:message key='venus.authority.Homepage'
                                                           bundle='${applicationAuResources}'/></span></a>
                                    <img src="<%=request.getContextPath()%>/themes/<venus:theme/>/images/au/in_in.gif"
                                         width="2" height="12"/>
                                    <a href="javascript:modifyPassword();" style="color:#f2f2f2; text-decoration:none;">
                                        <span><fmt:message key='venus.authority.Change_Password'
                                                           bundle='${applicationAuResources}'/></span></a>
                                    <img src="<%=request.getContextPath()%>/themes/<venus:theme/>/images/au/in_in.gif"
                                         width="2" height="12"/>
                                    <a href="../../jsp/login/login.jsp?isExit=1" target="_parent"
                                       style="color:#f2f2f2; text-decoration:none;">
                                        <span><fmt:message key='venus.authority.Logout'
                                                           bundle='${applicationAuResources}'/></span></a>
                                    <img src="<%=request.getContextPath()%>/themes/<venus:theme/>/images/au/in_in.gif"
                                         width="2" height="12"/>
                                    <a href="javascript:showHelp();" style="color:#f2f2f2; text-decoration:none;">
                                        <span><fmt:message key='venus.authority.Help'
                                                           bundle='${applicationAuResources}'/></span></a>
                                    <img src="<%=request.getContextPath()%>/themes/<venus:theme/>/images/au/in_in.gif"
                                         width="2" height="12"/>
                                    <a href="javascript:changeLocal('zh');"
                                       style="color:#f2f2f2; text-decoration:none;"> <span>中文</span></a>
                                    <img src="<%=request.getContextPath()%>/themes/<venus:theme/>/images/au/in_in.gif"
                                         width="2" height="12"/>
                                    <a href="javascript:changeLocal('en');"
                                       style="color:#f2f2f2; text-decoration:none;"> <span>English</span></a>
                                </td>
                            </tr>
                        </table>
                    </td>
                    <td width="1%">&nbsp;</td>
                </tr>
            </table>
            <table width="100%" height="77" border="0" cellpadding="0" cellspacing="0">
                <tr>
                    <td height="56"></td>
                    <td>&nbsp;</td>
                    <td width="504" align="right" valign="top"></td>
                    <td width="1%">&nbsp;</td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</body>
</html>
<!--
<script src="../../js/au/clock.js" type=text/javascript></script>
-->
