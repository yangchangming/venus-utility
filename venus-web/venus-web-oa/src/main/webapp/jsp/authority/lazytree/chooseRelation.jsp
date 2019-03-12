<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="venus.frames.mainframe.util.Helper"%>
<%@ page import="venus.oa.helper.LoginHelper"%>
<%@ page import="venus.oa.login.vo.LoginSessionVo"%>
<%@ page import="venus.oa.util.tree.DeepTreeSearch"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="venus.oa.organization.aupartyrelationtype.bs.IAuPartyRelationTypeBS"%>
<%@ page import="venus.oa.organization.aupartyrelation.bs.IAuPartyRelationBs"%>
<%@ page import="venus.oa.organization.aupartyrelation.vo.AuPartyRelationVo"%>
<%@ page import="org.apache.struts.util.LabelValueBean"%>
<%@ page import="venus.oa.util.GlobalConstants"%>
<%@ page import="venus.oa.organization.aupartyrelationtype.util.IConstants" %>
<%@ page import="venus.springsupport.BeanFactoryHelper" %>
<%
        IAuPartyRelationTypeBS relBS = (IAuPartyRelationTypeBS) BeanFactoryHelper.getBean("auPartyRelationTypeBS");
        List relTypeList = relBS.getPartyAll();
        String relationtype_id= ((LoginSessionVo)LoginHelper.getLoginVo(request)).getRelationtype_id();
        if(relationtype_id==null) {
            relationtype_id = "-1";
        }
        //关系处理
        IAuPartyRelationBs relBs = (IAuPartyRelationBs) BeanFactoryHelper.getBean("auPartyRelationBs");
        String currentCode = ((LoginSessionVo)LoginHelper.getLoginVo(request)).getCurrent_code();
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
%>
<%@ include file="/jsp/include/global.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>relation</title>
<script language="javascript">
function operateRelation(para){
    if(window.opener)
        window.opener.selText(para);
    else{
        var callBack = window.dialogArguments;
	    if (callBack != undefined && callBack != null)
	    {
	        callBack(para);
	    }
    }
    window.close();    
}
</script>
</head>
<body>
<div id="ccChild1"> 
<table class="table_div_content2" width="100%">
    <tr>
        <td>
        <div style="width=100%;overflow-x:visible;overflow-y:visible;">
        <table cellspacing="0" cellpadding="0" border="0" align="center" width="100%" class="listCss">
        <tr><td valign="top">
        <table cellspacing="1" cellpadding="1" border="0" width="100%">
        <tr><th></th><th><fmt:message key='venus.authority.Type_of_relationship' bundle='${applicationAuResources}' /></th><th><fmt:message key='venus.authority.Name_of_relationship' bundle='${applicationAuResources}' /></th></tr>
<%
        Iterator entryIt = relTypeMap.entrySet().iterator();   
        while(entryIt.hasNext())   
        {   
            Map.Entry entry = (Map.Entry) entryIt.next();
            String key = (String)entry.getKey();
            List value = (List)entry.getValue();
        %>                
        <%
            for(int i=0;i<value.size();i++){
                AuPartyRelationVo relationVo = (AuPartyRelationVo)value.get(i);
                String relCode = relationVo.getCode();
                String relName = DeepTreeSearch.getOrgNameByCode(relCode,false);
                relName=relName.replaceAll(";","");
                relName=relName.replaceAll("\n","");
        %>
        <tr>
        <td><input type="radio" name="relation" onclick="operateRelation('<%=relationVo.getId()%>:<%=relName%>')" <%=(relCode.equals(currentCode)?"checked":"") %>></td>
        <td onclick="operateRelation('<%=relationVo.getId()%>:<%=relName%>')"><center><%=(String)relTypeNameMap.get(key)%></center></td>
        <td onclick="operateRelation('<%=relationVo.getId()%>:<%=relName%>')"><%=relName%></td>
        </tr>
        <%
            }
        }
        %>
        </table>
        </td></tr></table></div></td></tr></table>
 </div>
</body>
</html>