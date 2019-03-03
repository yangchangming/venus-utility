<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/jsp/include/global.jsp" %>
<%@ taglib uri="/WEB-INF/gap-authority.tld" prefix="au" %>
<%@ page import="venus.authority.au.aufunctree.bs.IAuFunctreeBs"%>
<%@ page import="venus.authority.au.aufunctree.vo.AuFunctreeVo"%>
<%@ page import="venus.authority.au.aufunctree.util.IAuFunctreeConstants"%>
<%@ page import="venus.frames.mainframe.util.Helper"%>
<%@ page import="venus.authority.helper.LoginHelper"%>
<%@ page import="venus.authority.helper.OrgHelper"%>
<%@ page import="venus.authority.login.vo.LoginSessionVo"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="venus.authority.org.aupartyrelationtype.bs.IAuPartyRelationTypeBS"%>
<%@ page import="venus.authority.org.aupartyrelation.bs.IAuPartyRelationBs"%>
<%@ page import="venus.authority.org.aupartyrelation.vo.AuPartyRelationVo"%>
<%@ page import="venus.authority.util.tree.DeepTreeSearch"%>
<%@ page import="org.apache.struts.util.LabelValueBean"%>
<%@ page import="venus.authority.service.sys.vo.SysParamVo"%>
<%@ page import="venus.authority.util.GlobalConstants"%>
<%@ page import="venus.authority.org.aupartyrelationtype.util.IConstants" %>

<%
    String loginName = LoginHelper.getLoginName(request);

    IAuFunctreeBs bs = (IAuFunctreeBs) Helper.getBean(IAuFunctreeConstants.BS_KEY);
    List lTree = bs.queryByCondition("TOTAL_CODE like'101%' and TYPE='0'", "tree_level, order_code");
    IAuPartyRelationTypeBS relBS = (IAuPartyRelationTypeBS) Helper.getBean(IConstants.BS_KEY);
    List relTypeList = relBS.getPartyAll();
    String relationtype_id= ((LoginSessionVo)LoginHelper.getLoginVo(request)).getRelationtype_id();
    if(relationtype_id==null) {
        relationtype_id = "-1";
    }
    //关系处理
    IAuPartyRelationBs relBs = (IAuPartyRelationBs) Helper.getBean(venus.authority.org.aupartyrelation.util.IConstants.BS_KEY);
    String currentCode = ((LoginSessionVo)LoginHelper.getLoginVo(request)).getCurrent_code();
    String currentRelStr = "";
    if(null!=currentCode)
        currentRelStr = OrgHelper.getOrgNameByCode(currentCode,false);
    AuPartyRelationVo queryVo = new AuPartyRelationVo();
    queryVo.setPartyid(((LoginSessionVo)LoginHelper.getLoginVo(request)).getParty_id());
    List relList = relBs.queryAuPartyRelation(queryVo);//已经order by code了,自然order by relationType
    Map relTypeMap = new HashMap();
    Map relTypeNameMap = new HashMap();
    for(int i = 0;i<relList.size();i++){
        AuPartyRelationVo relVo = (AuPartyRelationVo)relList.get(i);
        String relTypeId = relVo.getRelationtype_id();
        if(relTypeId.equals(GlobalConstants.getRelaType_role()))
            continue;//跳过角色关系
        if(relTypeMap.containsKey(relTypeId)){
            ((List)relTypeMap.get(relTypeId)).add(relVo);
        }else{
            List al = new ArrayList();
            al.add(relVo);
            relTypeMap.put(relTypeId,al);
        }
    }
    for(int i=0;i<relTypeList.size();i++){
        LabelValueBean tmp = (LabelValueBean) relTypeList.get(i);
        relTypeNameMap.put(tmp.getLabel(),tmp.getValue());
    }
    int selectOpetionSize = relList.size()+relTypeMap.size();
    String selectOpetionMaxLengthStr = "";
    List list = LoginHelper.getFirstLevelMenu(request);
    String name = "";
    String totalCode = "";
    if (!list.isEmpty()) {
        AuFunctreeVo fvo = (AuFunctreeVo)list.get(0); 
        name = fvo.getName();
        totalCode = fvo.getTotal_code();
    } else {     LoginSessionVo loginSessionVo =(LoginSessionVo)   session.getAttribute("LOGIN_SESSION_VO");
    if(loginSessionVo==null){
        %>
           <script language="javascript">
          alert("<fmt:message key='venus.authority.You_are_logged_in_the_system_or_login_has_timed_out_please_re_login_system_' bundle='${applicationAuResources}' />") 
           window.top.location.href="<%=request.getContextPath()%>/jsp/login/login.jsp";
       </script>  
        <% 
    }else{
              // session.removeAttribute("LOGIN_SESSION_VO");//session.invalidate();
       %>
       <script language="javascript">
          alert("<fmt:message key='venus.authority.This_relation_does_not_have_any_rights' bundle='${applicationAuResources}' />") 
           // window.top.location.href="<%=request.getContextPath()%>/jsp/login/login.jsp";
       </script>
       <%
        }
}
    SysParamVo multiTab = GlobalConstants.getSysParam(GlobalConstants.MULTITAB);
%>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>VENUS Authority System</title>
<style>
*{
    margin:0;
    padding:0;
}
.top_bg{ background-image:url('<%=request.getContextPath()%>/themes/<venus:theme/>/images/au/di1_1.gif'); background-repeat:repeat-x; height:72px;}
.top_nav{ height:69px;  }/*logo右侧内容*/
.top_nav_b{ color:#fff}
.top_nav_b a:link{ color:#d4d2d2; text-decoration:none;}
.top_nav_b a:visited{color:#d4d2d2; text-decoration:none;}
.top_nav_b a:hover{color:#ffffff; text-decoration: underline;}
.top_nav_b a:active{color:#d4d2d2; text-decoration:none;}
.logo{ height:70px; border:none;width:424px;}
td{ font-size:14px; color:#000;white-space: nowrap;}
.bottom{
    font-size:14px;
    background-image:url('<%=request.getContextPath()%>/themes/<venus:theme/>/images/au/button1-2.gif');
    background-repeat:repeat-x;
    line-height:29px;
}
.bottom:hover{ font-size:14px; line-height:29px; color:#ffffff;}
.bottom-hover{background-image:url('<%=request.getContextPath()%>/themes/<venus:theme/>/images/au/button2-2.gif'); background-repeat:repeat-x;}
.bottom a{ font-size:14px; text-decoration:none; color:#000000;}
.bottom a:visited{font-size:14px; text-decoration:none; color:#ffffff;}
.bottom a:hover{font-size:14px; text-decoration:none; color:#ffffff;}
.bottom a:active{font-size:14px; text-decoration:none; color:#ffffff;}
.arrow{border-left:1px solid #a9a4a4; width:20px; height:20px; background-image:url('<%=request.getContextPath()%>/images/index/Arrow.gif'); background-repeat:no-repeat;}
.arrow:hover{width:20px; height:20px; background-image:url('<%=request.getContextPath()%>/images/index/Arrow-2.gif'); background-repeat:no-repeat;text-decoration:none;}
a:link{ color:#ffffff; text-decoration: none;}
a:visited{ color:#ffffff; text-decoration: none;}
a:hover{ color:#ffffff; text-decoration: none;
}
.skinblue{ width:10px; }
.skinblue img{border:1px solid #ffffff; }
.skinred{ width:70px; padding-left:10px; }
.skinred img{border:1px solid #ffffff;}
</style>
<link href="<%=request.getContextPath()%>/themes/<venus:theme/>/css/gap31_mainstyle.css" type="text/css" rel="stylesheet" charset='UTF-8'>

<script language="javascript">
var multiTab = '<%= (multiTab == null) ? "" : multiTab.getValue() %>';

//link your help system
function showHelp() {
    //window.open("../../htm/handbook.mht");
    //chplay();
}
//modify password function
function modifyPassword() {
    window.showModalDialog("<%=request.getContextPath()%>/jsp/authority/au/auuser/modifyPasswordFrame.jsp", 
        new Object(),'dialogWidth=640px;dialogHeight=480px;resizable:yes;status:no;scroll:no;');
}
//localization
function changeLocal(local){
        if(!local||local=="") return;
        var url = "<%=request.getContextPath()%>/jsp/common/changeLocal.jsp?local="+local;
        jQuery.ajax({url:url,async:false,cache:false});
        top.window.location.reload();
}

function changeThemes(theme) {
    if (theme == "blue") {
        theme = "default";
    }
    var url = "<%=request.getContextPath()%>/jsp/include/setTheme.jsp?theme=" + theme;
    jQuery.get(url, function () {
        top.window.location.reload();
    });
}

function openPage(title,url) {
    if (url != "" && url != undefined) {
        if (multiTab != null && multiTab > 0) 
            top.document.getElementById("contentFrame").contentWindow.addTab(title,"<%=request.getContextPath()%>" + url);
        else
            top.contentFrame.location = "<%=request.getContextPath()%>" + url;
    }    
}    

//show function tree of target frame named by "funcTreeFrame"
function linkPage(tabname,totalCode) {
    submitform.action = "<%=request.getContextPath()%>/jsp/authority/menu/leftMenu.jsp?totalCode=" + totalCode;
    document.all.tabname.value = tabname;
    submitform.target = "funcTreeFrame";
    submitform.submit();
}

//show function tree of target frame named by "funcTreeFrame"
/*function initMenu(){
    submitform.action = "<%=request.getContextPath()%>/jsp/authority/menu/leftMenu.jsp?totalCode="+'<%=totalCode%>';
    submitform.target = "funcTreeFrame";
    submitform.submit();
}*/    

function getEvent() {//同时兼容ie和ff的写法
    if(document.all) return window.event;
    func=getEvent.caller;
    while(func!=null){
        var arg0=func.arguments[0];
        if(arg0){
            if((arg0.constructor==Event || arg0.constructor ==MouseEvent) || (typeof(arg0)=="object" && arg0.preventDefault && arg0.stopPropagation)){
                return arg0;
            }
        }
        func=func.caller;
    }
    return null;
}

function selText(objValue){
    var strArray=new Array();   
    strArray=objValue.split(":");  
    document.all.relation_id.value=strArray[0];
    submitform.action = "<%=request.getContextPath()%>/login";
    submitform.target = "_top";
    submitform.submit();
}

function showDialog(url,x,y){
   var dialogHeight = "<%=(relList.size()*24+35)%>";
   if(document.all){//IE
      feature="dialogTop:"+y+";dialogLeft:"+x+";dialogWidth:400px;dialogHeight:"+dialogHeight+"px;status:no;help:no";   
       window.showModalDialog(url,selText,feature);   
   }else{
       feature ="top="+y+"px,left="+x+"px,width=400px,height="+dialogHeight+"px,menubar=no,toolbar=no,location=no,scrollbars=yes,status=no,modal=yes";
       window.open(url,"chooseRalation",feature);
   }
}
function showSel() {
    var mouseLocation = getEvent();
    showDialog("chooseRelation.jsp",mouseLocation.screenX,mouseLocation.screenY);
}
//在标签页中显示新打开的页面
function showTabPage(title,link) {
   top.document.getElementById("contentFrame").contentWindow.addTab(title,link);
}

function changeBg(obj){
    var parentObj = jQuery(obj).parent();
    var preImgObj = jQuery(parentObj).prev().find("img");
    var nextImgObj = jQuery(parentObj).next().find("img");
    var preSrcStr = jQuery(preImgObj).attr("src");
    var nextSrcStr = jQuery(nextImgObj).attr("src");
    var thisSrcStr = jQuery(obj).find("img").attr("src");
    jQuery(preImgObj).attr("src",preSrcStr.indexOf("1-1")<0?preSrcStr.replace("2-1","1-1"):preSrcStr.replace("1-1","2-1"));
    if(!jQuery(parentObj).hasClass("bottom-hover")){
        jQuery(parentObj).addClass("bottom-hover");
    }else{
        jQuery(parentObj).removeClass("bottom-hover");
    }
    jQuery(nextImgObj).attr("src",nextSrcStr.indexOf("1-3")<0?nextSrcStr.replace("2-3","1-3"):nextSrcStr.replace("1-3","2-3"));
}

function replaceMeunImageSrc(){

}

</script>
</head>
<body onload="linkPage('<%=name%>','<%=totalCode%>')">
<table width="100%"  class="top_bg"  border="0" cellspacing="0" cellpadding="0">
<form name="submitform" method="post">
<input type="hidden" name="tabname">
  <tr>

      <td class="logo"  align="left" valign="top">
          <%--<img src="<venus:base/>/themes/<venus:theme/>/images/au/ufida_logo<au:i18next/>.gif" />--%>
      </td>

    <td  align="right"  valign="top" class="top_nav" >
      <table  border="0" cellspacing="0" cellpadding="0" >
        <tr height="40">
          <td  colspan="3" align="right"  nowrap="nowrap">
                <!-- 关系选择开始-->
                <table class="table_noFrame" >

                    <%--<td align="left" nowrap="nowrap" class="skinblue" style="padding-right:10px;">--%>
                       <%--<a href="javascript:void(0);" onclick="changeThemes('flat');"><img src="<%=request.getContextPath()%>/images/skin/au/cyan-blue.gif"/></a>--%>
                    <%--</td>--%>
                    <%--<td align="right"  nowrap="nowrap"  class="skinblue">--%>
                        <%--<a href="javascript:void(0);" onclick="changeThemes('blue');"><img src="<%=request.getContextPath()%>/images/skin/au/blue.gif" /></a>--%>
                    <%--</td>--%>
                    <%--<td align="left"  nowrap="nowrap" class="skinred">--%>
                        <%--<a href="javascript:void(0);" onclick="changeThemes('red');"><img src="<%=request.getContextPath()%>/images/skin/au/red.gif" /></a>--%>
                    <%--</td>--%>

                <td>
                    <img class="avatar" src="<venus:base/>/themes/<venus:theme/>/images/au/ren.gif" />&nbsp;<span class="welcome"><fmt:message key='venus.authority.welcome' bundle='${applicationAuResources}' />,</span> &nbsp;<span class="loginName"><%=loginName %></span>
                </td>
                <td height="20">
                    <form name="form" method="post" action="<%=request.getContextPath()%>/login" target="_top">
                <%
                SysParamVo chooseAuRel = GlobalConstants.getSysParam(GlobalConstants.CHOOSEAUREL);
                SysParamVo showRelationTypeChoose = GlobalConstants.getSysParam("SHOWRELATIONFLAG");
                if(chooseAuRel!=null&&"true".equals(chooseAuRel.getValue())){
                    if(!relTypeMap.isEmpty()){
                %>
                        <table width="100%" border="0" cellpadding="0" cellspacing="0">
                            <tr>
                                <td >
                                  <input type="hidden" name=relation_id>
                                      <div id="outDiv" style="border:0px solid #90b3cf;width:160px">
                                          <table border="0" cellpadding="0" cellspacing="0">
                                              <tr>
                                                  <td height="20" style="BORDER:0px" algin="center">
                                                      <div id="relDiv" style="background-color:#E9E9E8; height:20px;line-height:20px;width:140px;">
                                                        <marquee onmouseover=this.stop() onmouseout=this.start() scrollamount=3><%=currentRelStr%></marquee>
                                                      </div>
                                                  </td>
                                                  <td align="center"  class="arrow" onclick="showSel()">
                                                  </td>
                                              </tr>
                                          </table>
                                      </div>
                                </td>
                                </form>
                            </tr>
                        </table>
                <%}
                }else{%>
                <%if(relTypeList.size()>Integer.parseInt(null==showRelationTypeChoose?"4":showRelationTypeChoose.getValue())) {%>
                        <table width="100%" border="0" cellpadding="0" cellspacing="0">
                            <tr>
                                <td height="25">
                                    <select name="relationtype_id" style={width:150px} onChange="javascript:form.submit()">
                                        <option value="-1" <%=relationtype_id.equals("-1")?"selected":""%> >全部</option>
                                    <%
                                        for(int i=0; i<relTypeList.size(); i++) {
                                            LabelValueBean tmp = (LabelValueBean) relTypeList.get(i);
                                    %>
                                        <option value="<%=tmp.getLabel()%>" <%=relationtype_id.equals(tmp.getLabel())?"selected":""%> ><%=tmp.getValue()%></option>
                                    <%  }
                                    %>
                                    </select>
                                </td>
                            </tr>
                        </table>
                <%} 
                }%>
                    </form>
                    </td>
                    <td alient="right" class="top_navigator">
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="<%=request.getContextPath()%>/jsp/main.jsp" target="_parent" >
              <fmt:message key='venus.authority.Homepage' bundle='${applicationAuResources}' /></a> 
              <img src="<%=request.getContextPath()%>/themes/<venus:theme/>/images/au/in_in.gif" width="2" height="12" /> <a href="javascript:modifyPassword();" >
              <fmt:message key='venus.authority.Change_Password' bundle='${applicationAuResources}' /></a>
              <img src="<%=request.getContextPath()%>/themes/<venus:theme/>/images/au/in_in.gif" width="2" height="12" /> <a href="<%=request.getContextPath()%>/jsp/login/login.jsp?isExit=1" target="_parent" >
              <fmt:message key='venus.authority.Logout' bundle='${applicationAuResources}' /></a>

              <%--<img src="<%=request.getContextPath()%>/themes/<venus:theme/>/images/au/in_in.gif" width="2" height="12" />  <a href="javascript:showHelp();" >--%>
              <%--<fmt:message key='venus.authority.Help' bundle='${applicationAuResources}' /></a>--%>
              <%--<img src="<%=request.getContextPath()%>/themes/<venus:theme/>/images/au/in_in.gif" width="2" height="12" /> <a href="javascript:changeLocal('zh');" >--%>
              <%--中文</a>--%>
              <%--<img src="<%=request.getContextPath()%>/themes/<venus:theme/>/images/au/in_in.gif" width="2" height="12" /><a href="javascript:changeLocal('en');" >--%>
              <%--English</a>--%>

                    </td>
                </tr>                 
                </table>
              </td>
          </td>
        </tr>
        <tr align="right" valign="bottom">
        <td width="">&nbsp;</td>
          <td colspan="2" align="right" valign="bottom">
          <table border="0" cellspacing="0" cellpadding="0">
            <tr>
              <%
                for(int i = 0; i < list.size(); i++) {
                  AuFunctreeVo vo = (AuFunctreeVo)list.get(i);
              %>
              <td><img src="<%=request.getContextPath()%>/themes/<venus:theme/>/images/au/button1-1.gif"></td>
              <td align="center" class="bottom" >
                 <a onmouseover="javascript:changeBg(this);" onmouseout="javascript:changeBg(this);" href="javascript:<%if ("1".equals(vo.getIs_leaf())) {%>openPage('<%=vo.getName() %>','<%=vo.getUrl() %>');<% } else { %>linkPage('<%=vo.getName() %>','<%=vo.getTotal_code() %>')<%}%>"><%=vo.getName() %></a>
              </td>
              <td><img src="<%=request.getContextPath()%>/themes/<venus:theme/>/images/au/button1-3.gif"></td>
              <% } %>
              <td> </td>
            </tr>

          </table>
</td>
        </tr>
      </table>
    </td>
  </tr>
</form>
</table>

</body>
</html>