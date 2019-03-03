<%@ page contentType="text/html; charset=UTF-8" language="java"%>
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
    try {
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
        //IAuAuthorizeBS authorizeBs = (IAuAuthorizeBS)Helper.getBean(venus.authority.au.auauthorize.util.IConstants.BS_KEY);
%>
<%@ include file="/jsp/include/global.jsp" %>
<jsp:include page="include/globalLazyTree.jsp?returnTextType=plugParent" />
<title>懒加载功能菜单</title>


<script language="javascript">  

    var treeImagePath = "image"; 
    var xslPath = "<venus:base/>/jsp/authority/lazytree/lazytree_xsl.jsp?inputType=" + inputType + "&treeImagePath=" + treeImagePath;
    var defaultXmlUrlPath = "deeptree_data.jsp?rootnode=";  //xmlSourceType为nodeCode时有效, 默认xml主路径, 转义影射：&-->%26, =-->%3D
    var defaultNodeCodeGetCurrent = "&getcurrent=1";  //xmlSourceType为nodeCode时有效, 默认取节点本身数据参数标识：&-->%26, =-->%3D
    


    function returnValueName() {  //获得所有选择的checkbox
        currentId = "";
        var checkedArray = new Array(0);        
        var submitStringArray = new Array(0);
        var submitObjectArray = new Array(0);
                
        var obj = null;
        if(inputType == "checkbox") {
            obj = window.document.getElementsByName(prefixCheckbox);
            for(i=0;i<obj.length;i++){
                if(obj[i].checked){
                    checkedArray.push(obj[i].id.substring(prefixCheckbox.length));
                }
            }
            
            if(submitType == "parentPriority") {
                submitStringArray = clearChild(checkedArray);  //全面扫描checkedArray,把其中冗余的字节点信息去掉,Id列表放入submitStringArray
            } else {
                submitStringArray = filterHidden(checkedArray);
            }
        } else if(inputType == "radio") {
            obj = window.document.getElementsByName(prefixRadio);
            for(i=0;i<obj.length;i++){
                if(obj[i].checked){
                    checkedArray.push(obj[i].id.substring(prefixRadio.length));
                }
            }
            submitStringArray = checkedArray;
        }
        for(var i=0; i<submitStringArray.length; i++) {  //把要提交的checkbox属性填满放入submitObjectArray
            var tempObj = new Object();
            var thisObj = getObjectById(prefixDiv + submitStringArray[i]);
            var parentObj = getParentObject(thisObj);
    
            tempObj["childName"] = thisObj.text;
            tempObj["childId"] = thisObj.returnValue;
            tempObj["returnValue"] = thisObj.returnValue;
            tempObj["parentName"] = parentObj["parentName"];
            tempObj["parentId"] = parentObj["parentId"];
            tempObj["detailedType"] = thisObj.detailedType; 
            
            submitObjectArray[submitObjectArray.length] = tempObj;
        }
        
        if(submitObjectArray.length == 0) {
            alert("<fmt:message key='venus.authority.Please_select_a_record' bundle='${applicationAuResources}' />!");
            return false;
        }
        window.returnValue = submitObjectArray;
        if(outputType == "doubleClick") {
            parent.close();
        } else {
            parent.close();
        }
    }


</script>
<script>
    var selectOptionLen;
    function byteLength(sStr){
        aMatch=sStr.match(/[^\x00-\x80]/g);
        return(sStr.length+(!aMatch?0:aMatch.length));
    }
    function selText(objValue){
        var strArray=new Array();   
        strArray=objValue.split(":");  
        document.all.relation_id.value=strArray[0];
        form.submit();
    }
    
    if (!window.createPopup) {  
	    var __createPopup = function() {  
	        var SetElementStyles = function( element, styleDict ) {  
	            var style = element.style ;  
	            for ( var styleName in styleDict )
	               style[ styleName ] = styleDict[ styleName ] ;   
	        }  
	        var eDiv = document.createElement( 'div' );   
	        SetElementStyles( eDiv, { 'position': 'absolute', 'top': 0 + 'px', 'left': 0 + 'px', 'width': 0 + 'px', 'height': 0 + 'px', 'zIndex': 1000, 'display' : 'none', 'overflow' : 'hidden' } ) ;  
	        eDiv.body = eDiv;
	        var opened = false ;  
	        var setOpened = function( b ) {  
	            opened = b;   
	        }  
	        var getOpened = function() {  
	            return opened ;   
	        }  
	        var getCoordinates = function( oElement ) {  
	            var coordinates = {x:0,y:0} ;   
	            while( oElement ) {  
	                coordinates.x += oElement.offsetLeft ;  
	                coordinates.y += oElement.offsetTop ;  
	                oElement = oElement.offsetParent ;  
	            }  
	            return coordinates ;  
	        }  
	        return {
	                htmlTxt : '', 
	                document : eDiv, 
	                isOpen : getOpened(), 
	                isShow : false, 
	                hide : function() { 
	                       SetElementStyles( eDiv, { 'top': 0 + 'px', 'left': 0 + 'px', 'width': 0 + 'px', 'height': 0 + 'px', 'display' : 'none' } ) ; 
	                       eDiv.innerHTML = '' ; 
	                       this.isShow = false ; 
	                   }, 
	                show : function( iX, iY, iWidth, iHeight, oElement ) { 
	                       if (!getOpened()) { 
	                           document.body.appendChild( eDiv ) ; 
	                           setOpened( true ) ; 
	                       } ; 
	                       this.htmlTxt = eDiv.innerHTML ; 
	                       if (this.isShow) { 
	                           this.hide() ; 
	                       } ; 
	                       eDiv.innerHTML = this.htmlTxt ; 
	                       var coordinates = getCoordinates ( oElement ) ; 
	                       eDiv.style.top = ( iX + coordinates.x ) + 'px' ; 
	                       eDiv.style.left = ( iY + coordinates.y ) + 'px' ; 
	                       eDiv.style.width = iWidth + 'px' ; 
	                       eDiv.style.height = iHeight + 'px' ; 
	                       eDiv.style.display = 'block' ; 
	                       this.isShow = true ; 
	                   }
	             }  
	    }  
	    window.createPopup = function() {  
	        return __createPopup();   
	    }  
	}  
    var   oPopup = window.createPopup();
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
	var __is_ff = (navigator.userAgent.indexOf("Firefox")!=-1);//Firefox
	function getMouseLocation(){
		e = getEvent();
		var mouseX = 0;
		var mouseY = 0;
		
		if(__is_ff){
		    mouseX = e.layerX + document.body.scrollLeft; 
		    mouseY = e.layerY + document.body.scrollLeft; 
		}else{
		    mouseX = e.x + document.body.scrollLeft; 
		    mouseY = e.y + document.body.scrollTop;  
		}
		return {x:mouseX,y:mouseY};
	}

    function showDialog(url,x,y){
       var dialogHeight = "<%=(relList.size()*24+35)%>";
       if(document.all) //IE   
	   {   
	       feature="dialogTop:"+y+";dialogLeft:"+x+";dialogWidth:400px;dialogHeight:"+dialogHeight+"px;status:no;help:no";   
	       window.showModalDialog(url,selText,feature);   
	   }   
	   else   
	   {   
	       feature ="top="+y+"px,left="+x+"px,width=400px,height="+dialogHeight+"px,menubar=no,toolbar=no,location=no,scrollbars=yes,status=no,modal=yes";
	       window.open(url,"chooseRalation",feature);
	   }   
	}
    function showSel()   
    {   
        var mouseLocation = getEvent();
        showDialog("chooseRelation.jsp",mouseLocation.screenX,mouseLocation.screenY);
        /*
        var textContent="";
        textContent=textContent+"<select id=qswh size=<%=selectOpetionSize%> style='position:absolute;display:true' onchange='parent.selText(this.options[this.options.selectedIndex].value);document.all.qswh.style.display=\"none\";parent.oPopup.hide()'>";
        <%
        Iterator entryIt = relTypeMap.entrySet().iterator();   
        while(entryIt.hasNext())   
        {   
            Map.Entry entry = (Map.Entry) entryIt.next();
            String key = (String)entry.getKey();
            List value = (List)entry.getValue();
        %>
        textContent=textContent+"<OPTGROUP label=<%=(String)relTypeNameMap.get(key)%>>";
        <%
            for(int i=0;i<value.size();i++){
                AuPartyRelationVo relationVo = (AuPartyRelationVo)value.get(i);
                String relCode = relationVo.getCode();
                String relName = DeepTreeSearch.getOrgNameByCode(relCode,false);
                relName=relName.replaceAll(";","");
                relName=relName.replaceAll("\n",""); 
                selectOpetionMaxLengthStr = selectOpetionMaxLengthStr.length()>relName.length()?selectOpetionMaxLengthStr:relName;
        %>
        textContent=textContent+"<option value=<%=relationVo.getId()%>:<%=relName%>><%=relName%></option>";
        <%
            }
        %>
        textContent=textContent+"</OPTGROUP>";
        <%
        }
        %>
        textContent=textContent+"</select>";
        oPopup.document.body.innerHTML=textContent;
        var mouseLocation = getMouseLocation();
        if(window.XMLHttpRequest){
            oPopup.show(8,mouseLocation.y+10 ,byteLength('<%=selectOpetionMaxLengthStr%>')*6.5+45,16*<%=selectOpetionSize%>+3,document.body);
        }else{
            oPopup.show(8,mouseLocation.y+10 ,byteLength('<%=selectOpetionMaxLengthStr%>')*6.5+68,16*<%=selectOpetionSize%>+3,document.body);
        }
        */
    }
</script>
<script type="text/javascript" src="<venus:base/>/jsp/authority/lazytree/lazytree.js"></script>
</head>

<body onLoad="doOnLoad()" topmargin=0 leftmargin=0>
<table class="table_noFrame">
    <!-- 关系选择开始 -->
    <tr>
        <td valign="top" height="20">
        <form name="form" method="post" action="<%=request.getContextPath()%>/login" target="_top">
    <%
    SysParamVo chooseAuRel = GlobalConstants.getSysParam(GlobalConstants.CHOOSEAUREL);
    SysParamVo showRelationTypeChoose = GlobalConstants.getSysParam("SHOWRELATIONFLAG");
    if(chooseAuRel!=null&&"true".equals(chooseAuRel.getValue())){
        if(!relTypeMap.isEmpty()){
    %>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                    <td height="25">
                      <input type="hidden" name=relation_id>
                          <div id="outDiv" style="border:1px solid #90b3cf;width:160px">
                              <table border="0" cellpadding="0" cellspacing="0">
                                  <tr>
                                      <td width="140px" style="BORDER:0px">
                                          <div id="relDiv" style="background-color:#FFFFFF">
                                            <marquee onmouseover=this.stop() onmouseout=this.start() scrollamount=3><%=currentRelStr%></marquee>
                                          </div>
                                      </td>
                                      <td width="20px" style="BORDER:0px">
                                        <input type="button" onclick="showSel()" value="&nabla;" style="border:1px solid #90b3cf;background-color:#DADFF4;text-align:center;float:right">
                                      </td>
                                  </tr>
                              </table>
                          </div>
                    </td>                    
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
    </tr>   
    <!-- 关系选择结束 -->
    <!--树开始-->    
    <tr> 
       <td width="*" valign="top" align="left"> 
        <div name="deeptree" id="deeptree" class="deeptree"></div>
      </td>
    </tr>
    <!--树结束-->
</table>
</body>
</html>
<%                          
    }catch(Exception e) {
        e.printStackTrace();
    }
%>