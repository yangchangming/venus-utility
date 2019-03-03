if(!jQuery.browser.msie){
    Node.prototype.transformNode = function (oXslDom) {
      var oProcessor = new XSLTProcessor();
      oProcessor.importStylesheet(oXslDom);
        
      var oResultDom = oProcessor.transformToDocument(this);
      var sResult ; 
      if(oResultDom.xml)
        sResult = oResultDom.xml
      else{
        var oSerializer = new XMLSerializer();
        sResult = oSerializer.serializeToString(oResultDom,"text/xml");
      }
        
      if (sResult.indexOf("<transformiix:result") > -1) {
      sResult = sResult.substring(sResult.indexOf(">") + 1, sResult.lastIndexOf("<"));
      }
      
      return sResult;   
    };
}
jQuery(function(){
    element = jQuery(".deeptree").get(0);
    jQuery.ajax({//采取同步方式加载xml文件
      url:Config.SyncXSLsrc,
      async: false,
      cache:false,
      complete :function(XmlHttp){
         if(XmlHttp.readyState==4){
            if(XmlHttp.status==200){
                var txt1 =jQuery.trim(XmlHttp.responseText);
                xsldoc = createDoc(txt1);
            }   
        }
      }
}); 
    jQuery(".deeptree").live("click",MouseClick).live("dblclick",MouseDblClick).live("mouseover",MouseOver).live("mouseout",MouseOut).live("selectstart",SelectStart);
    Init();
    autoExpand();
});

function SelectStart(){  //不允许选择
    window.event.cancelBubble=true;
    window.event.returnValue=false;
    return false;
}

var oCurrentNode=null;//高亮度显示时当前节点
var oTempClass=null;//定义鼠标样式的临时变量
var icon={ //定义节点显示的图片
    leaf:treeImagePath + "/ftv2link.gif",
    closeRoot:treeImagePath + "/diffFolder-0.gif",
    openRoot:treeImagePath + "/diffFolder.gif",
    closeParent:treeImagePath + "/ftv2folderclosed.gif",
    openParent:treeImagePath + "/ftv2folderopen.gif"    
};
var Config={
    loading:"数据加载中...",  //加载时显示信息
    unavaible:"无相应数据！",  //加载失败时显示信息
    Service:rootXmlSource,  //节点数据文件
    SyncXSLsrc:xslPath,  //一次加载时xsl文件
    isExpandOne:expandType  //是否自动关闭兄弟节点
};
function PreLoad(iconIsString){  //预下载图片
    for(i in icon){
        var temp = iconIsString ? icon[i] : icon[i].src;
        icon[i]=new Image();
        icon[i].src=temp;
    }
    //setTimeout("PreLoad()",10000)
}
PreLoad(true);


/**********************　开始deeptree的树展现相关方法 **********************/

function StateXML(state){  //显示加载的状态 (加载中或加载失败)
    var str="<message text='"+state+"'/>";
    //var doc=new ActiveXObject("Microsoft.XMLDOM");
    //doc.async=false;
    //doc.loadXML(str);
    return str;
}

function Init(){  //加载初始化
    if(xmlSourceType == "nodeCode") {
        getXmlValue(element, Config.Service + defaultNodeCodeGetCurrent,false);
    } else {
        getXmlValue(element, Config.Service,false);
    }
}

function autoExpand(){  //自动展开
    var oRootNode = GetElement(element,"div");
    ExpandNode(oRootNode);
}

function ExpandObject() {
    var currentObject = getObjectById(currentObjectId_cookie);
    if(currentObject != null)
        ExpandNode(currentObject);
}

function getXmlValue(objContainer, xmlSource, isSelected, id, isAppand) {  //接受xmlSource异步，动态的加载数据，返回值，修正后
    var temp = jQuery(objContainer);
    if(!isAppand)
        temp.html(StateXML(Config.loading));
    temp.attr("send","true");
    var targetUrl = xmlSource;
    if(xmlSourceType == "nodeCode") {  //如果传过来的xml数据获取方式为nodeCode，则加上默认xmlPath路径
        targetUrl = defaultXmlUrlPath + targetUrl;
    }
    jQuery.ajax({
              url:targetUrl,
              async: false,
              cache:false,
              complete :function(XmlHttp){
                 if(XmlHttp.readyState==4){
                    if(XmlHttp.status==200){
                        var txt =jQuery.trim(XmlHttp.responseText);
                        var Xmldoc = createDoc(txt);
                        if(Xmldoc.documentElement&&Xmldoc.documentElement.hasChildNodes()){
                            if(isAppand) {
                                temp.append(Xmldoc.transformNode(xsldoc));
                            } else {
                                temp.html(Xmldoc.transformNode(xsldoc));
                            }
                            addChildrenToId(Xmldoc, isSelected, id);
                        }else{
                            if(!isAppand) {
                                temp.html(StateXML(Config.loading));
                                //隐藏提示
                                noTranslateParentToLeaf(objContainer);
                            }
                        }
                    }else{
                        if(!isAppand)
                            temp.html(StateXML(Config.unavaible));
                    }
                }
              }
         }); 
    
    
}

function translateParentToLeaf(thisDiv) {  //把父节点变成叶子
    thisDiv.style.display = "none";
    var thisParent = jQeury(thisDiv).prev().get(0);
    if(thisParent == undefined || thisParent == null || jQeury(thisParent).attr("type") != "parent") {
        return false;
    }
    thisParent.type = "leaf";
    var thisTreeImage = getObjectById(prefixTreeImg + thisParent.id.substring(prefixDiv.length));
    thisTreeImage.src = icon.leaf.src;
}

function noTranslateParentToLeaf(thisDiv) {  //不把父节点变成叶子
    thisDiv.style.display = "none";
    if(thisDiv){
        return false;
    }
    var thisParent = jQeury(thisDiv).prev().get(0);
    if(thisParent == undefined || thisParent == null || jQeury(thisParent).attr("type") != "parent") {
        return false;
    }
    thisParent.type = "leaf";
    var thisTreeImage = getObjectById(prefixTreeImg + thisParent.id.substring(prefixDiv.length));
    thisTreeImage.src = icon.openRoot.src;
}

function getXmlDataIslandValue() {  //动态新增节点
    var currentObject = getObjectById(currentObjectId); //这句话得到的是当前的span
    if(currentObject == null)
        return false;
    var objContainer = currentObject.parentElement.parentElement;
    var isSelected = false;
    if(inputType == "checkbox") {
        isSelected = getObjectById(prefixCheckbox + gradeMapParent[currentObject.id.substring(prefixSpan.length)]).checked; 
    }
    var id = gradeMapParent[currentObject.id.substring(prefixSpan.length)];
    if(currentItem_Drag != null) {
        var objContainer = currentObject.parentElement;
        if(objContainer.hasChild == "1") {
            objContainer.nextSibling.insertAdjacentHTML("afterEnd", currentItem_Drag);
        } else {
            objContainer.insertAdjacentHTML("afterEnd", currentItem_Drag);
        }
        var thisMapId = _dt_itemMouseDown.downObject.id.substring(prefixSpan.length);
        var tempOldParent = gradeMapParent[thisMapId];
        var tempNewParent = gradeMapParent[objContainer.id.substring(prefixDiv.length)];
        gradeMapParent[thisMapId] = tempNewParent;
        if(gradeMapChild[tempNewParent] == undefined) {
            gradeMapChild[tempNewParent] = new Object();
        } 
        gradeMapChild[tempNewParent].push(thisMapId);
        gradeMapChild[tempOldParent] = getArrayDeleteString(gradeMapChild[tempOldParent], thisMapId);
    } else {
        var Xmldoc = getObjectById("xmlDataIsland");
        var tempDiv = window.document.createElement("div");
        tempDiv.innerHTML = Xmldoc.transformNode(xsldoc);
        tempDiv = tempDiv.childNodes[0];
        currentObject.parentElement.insertAdjacentElement("afterEnd", tempDiv);
        addChildrenToId(Xmldoc, isSelected, id);
    }
}

function getMoreTreeNode(thisSpan) {  //更多的展开节点
    var objDiv = thisSpan.parentElement;
    var objContainer = objDiv.parentElement;
    var xmlSource = thisSpan.xmlSource;
    var isSelected = false;
    if(inputType == "checkbox") {
        isSelected = getObjectById(prefixCheckbox + objDiv.parentElement.previousSibling.id.substring(prefixDiv.length)).checked;
    } 
    var id = objDiv.parentElement.previousSibling.id.substring(prefixDiv.length);
    objDiv.removeNode(true);
    getXmlValue(objContainer, xmlSource, isSelected, id, true);
}

function addChildrenToId(oXmlDoc, isSelected, parentId) {  //把取出的子节点加到当前节点的属性中
    if( oXmlDoc == null || oXmlDoc.documentElement == null) {
        return false;
    }
    var thisRoot = oXmlDoc.documentElement;  //取到根节点
    var cs = thisRoot.childNodes;  //取第二级子节点
    recursiveAddChildrenToId(cs, isSelected, parentId);
}

function recursiveAddChildrenToId(cs, isSelected, parentId) {
    var tempArrayString = new Array();
    var iXsltIndex = 1;
    for (var i = 0; i < cs.length; i++) {
        if (cs[i].tagName == prefixTreeNode) {  //注意：这里和xslt的规则绑死了, todo ，这里一定要改
            tempArrayString.push(cs[i].attributes[0].value + prefixConcat_xsltNumber + iXsltIndex);
            iXsltIndex ++;
        }
    }
    
    var tempParentId = prefixRootMapCode;
    if(parentId != undefined)
        tempParentId = parentId;
    var tempIndex = indexObject++;
    var tempParentIdOld = tempParentId;
    tempParentId = prefixConcat_index + tempIndex + tempParentId;
    for(var a=0; a<tempArrayString.length; a++) {
        var thisDiv = getObjectById(prefixDiv + tempArrayString[a]);;
        thisDiv.id = thisDiv.id + prefixConcat_parent + tempParentId;
        for(var b=0; b<thisDiv.children.length; b++) {
            var tempInnerObj = thisDiv.children[b];
            tempInnerObj.id = tempInnerObj.id + prefixConcat_parent + tempParentId;
        }
    }
    for(var i=0; i<tempArrayString.length; i++) {
        tempArrayString[i] += prefixConcat_parent + tempParentId;  //要把父节点id放在子节点后面保证全局唯一
        if(tempParentId == prefixConcat_index + tempIndex + prefixRootMapCode) {  //parentId未定义，说明正在处理根节点
            rootNode.push(tempArrayString[i] );  //设置rootNode
            gradeMapParent[tempArrayString[i]] = prefixRootMapCode;
        } else {
            gradeMapParent[tempArrayString[i]] = tempParentIdOld;;
        }
    }
    if(tempParentId == prefixConcat_index + tempIndex + prefixRootMapCode) {
        if(gradeMapChild[prefixRootMapCode] == undefined) {
            gradeMapChild[prefixRootMapCode] = new Array();     
        }
        for(var b=0; b<tempArrayString.length; b++) {
            gradeMapChild[prefixRootMapCode].push(tempArrayString[b]);
        }
    } else {
        if(gradeMapChild[tempParentIdOld] == undefined) {
            gradeMapChild[tempParentIdOld] = new Array();       
        }
        for(var b=0; b<tempArrayString.length; b++) {
            gradeMapChild[tempParentIdOld].push(tempArrayString[b]);
        }
    }
    
    //把子节点的checkbox定义为和父节点一样
    if(nodeRelationType == "hasRelation") {
        for(var i=0; i<tempArrayString.length; i++) {
            var tempCheckbox = getObjectById(prefixCheckbox + tempArrayString[i]);
            if(tempCheckbox != null) {
                tempCheckbox.checked = isSelected;
            }
        }
    }
    
    //递归处理
    var n=0;
    for(var m=0; m<cs.length; m++) {
        if(cs[m].tagName != prefixTreeNode) {
            continue;
        }
        if(cs[m].childNodes.length > 0) {
            recursiveAddChildrenToId(cs[m].childNodes, isSelected,  tempArrayString[n]);
        }
        n ++;
    }
}

function SelectNode(obj,open){  //选择节点，让节点高亮度显示
    var objNode=GetElement(obj,"span")
    var oLink=GetElement(objNode,"a")
    if(!oCurrentNode){
        oCurrentNode=objNode;
        objNode.className="HighLight";
    } else{
        oCurrentNode.className=jQuery(oCurrentNode).attr("type")=="load"?"load":"";
        objNode.className="HighLight";
        oCurrentNode=objNode;
    }
    currentObjectId = objNode.id;  //放入全局变量中
    if (oLink!=null && multiTab != null && multiTab > 0) { 
            self.parent.document.getElementById("contentFrame").contentWindow.addTab(objNode.title,oLink.name);
    } else {
        if(oLink!=null && open)
            window.open(oLink.href,oLink.target);
    }
}



function GetElement(objParent, objTag){  //从objParent中得到类型为objTag的元素
    var objNode=null;
    if(objParent == null)
        return false;
    var oChildren=objParent.children;
    if(oChildren){
        for(i=0;i<oChildren.length;i++){
            if(oChildren[i].tagName.toUpperCase()==objTag.toUpperCase()){
                objNode=oChildren[i];
                break;
            }
        }
    }
    return objNode;
}

function Toggle(node){  //节点展开，或者收拢
    var objNode = jQuery(node);
    
    if(objNode.attr("type")=="parent"){
        if(objNode.attr("opened")=="false") {
            ExpandNode(node);
            if(enableCookie)
                SetCookie(objNode.attr("id").substring(prefixDiv.length), cookieEnable, exp);           
        } else {
            CollapseNode(node);
            if(enableCookie)
                DeleteCookie(objNode.attr("id").substring(prefixDiv.length));       
        }
            
    }
}

function ExpandNode(objNode){  //展开节点
    var node = jQuery(objNode);
    if(objNode == null || node.attr("type") != "parent")
        return false;
    var oTreeImg=getObjectById(prefixTreeImg + objNode.id.substring(prefixDiv.length));
    var oSpan=getObjectById(prefixSpan + objNode.id.substring(prefixDiv.length));
    var oCheckbox=getObjectById(prefixCheckbox + objNode.id.substring(prefixDiv.length));
    var thisType = node.attr("thisType");

    if(oTreeImg==null || oSpan==null)
        return false;
    var oChild=node.next();
    //根据节点类型(root,parent)区分打开节点的图片样式
    if (thisType == "root") {
        oTreeImg.src=icon.openRoot.src;
    } else {
        oTreeImg.src=icon.openParent.src;
    }
    oChild.css("display","block");
    oChild.css("padding","0 0 0 10");
    node.attr("open","true");
    node.attr("opened","true");
        
    if(oCheckbox != null) {
        node.attr("isSelected",jQuery(oCheckbox).attr("checked"));
    }
    if(oChild.attr("send")=="false"){
        getXmlValue(oChild.get(0), node.attr("xmlSource"), node.attr("isSelected"), objNode.id.substring(prefixDiv.length))
    }
    if(Config.isExpandOne){
        var oContainer=node.parent();
        var oChildren=oContainer.children;
        for(i=0;i<oChildren.length;i++) {
            if(oChildren[i].open=="true" && oChildren[i]!=objNode) {
                CollapseNode(oChildren[i]); 
                if(enableCookie)    
                    DeleteCookie(oChildren[i].id.substring(prefixDiv.length));
            }
        }   
    }
}

function CollapseNode(node) {  //收拢节点
    var objNode = jQuery(node);
    if(objNode.attr("type") != "parent")
        return;
    var oTreeImg=getObjectById(prefixTreeImg + objNode.attr("id").substring(prefixDiv.length));
    var oSpan=getObjectById(prefixSpan + objNode.attr("id").substring(prefixDiv.length));
    var oCheckbox=getObjectById(prefixCheckbox + objNode.attr("id").substring(prefixDiv.length));
    var thisType = objNode.attr("thisType");
    
    if(oTreeImg==null || oSpan==null)
        return false;
    var oChild=objNode.next();
    //根据节点类型(root,parent)区分收拢节点的图片样式
    if (thisType == "root") {
        oTreeImg.src=icon.closeRoot.src;
    } else {
        oTreeImg.src=icon.closeParent.src;
    }
    oChild.css("display","none");
    objNode.attr("open","false");
    objNode.attr("opened","false");
    if(oCheckbox != null) {
        objNode.attr("isSelected",oCheckbox.checked);
    }
}
/**********************　结束deeptree的树展现相关方法 **********************/

/**********************　开始监听事件相关方法 **********************/
function MouseClick(event){  //鼠标事件定义
    var target= event.srcElement?event.srcElement:event.target;
    var RealObj = jQuery(target);
    /*jQuery1.3*/
    //if(RealObj.attr("tagName").toUpperCase()=="A"){
    //jQuery1.6
    if(RealObj.get(0).tagName.toUpperCase()=="A"){
        var objSpan=RealObj.parent()
        if(objSpan.attr("type")=="label"){
            RealObj.trigger("blur") 
            var objNode=objSpan.parent().get(0)
            Toggle(objNode) 
            SelectNode(objNode)
        }
    }
    else if(RealObj.attr("type")=="img" && RealObj.attr("use")=="tree"){
        var objNode=RealObj.parent();
        if(objNode){
            if(objNode.attr("type")=="leaf"){
                SelectNode(objNode.get(0),true)
            }
            else{
                Toggle(objNode.get(0))
            }
        }
    } else if(RealObj.attr("type")=="label" || RealObj.attr("type")=="load"){
        var objNode=RealObj.parent().get(0);
        Toggle(objNode);
        SelectNode(objNode,true);
    } else if(RealObj.attr("type")=="more"){
        getMoreTreeNode(RealObj.get(0));
    } else if(RealObj.attr("type")=="checkbox"){
        var objNode=RealObj.parent();
        objNode.attr("isSelected",RealObj.attr("checked")) ; //将checkbox的状态给父容器对象
        //胡奇需求
        if(nodeRelationType == "hasRelation") {
            if(!arrayHasString(rootNode, RealObj.id.substring(prefixCheckbox.length))) {
                setParentState(RealObj.id);        //设置父节点的checkbox为不确定状态或者选或者不选
            }
            if(objNode.type=="parent"){
                if(objNode.opened=="true"){    //对于已经打开的，继续选择子节点
                    checkChild(RealObj, gradeMapChild[RealObj.id.substring(prefixCheckbox.length)], objNode.isSelected);
                }
            }       
        }
        if(!RealObj.checked) {
            try {
                addRemoveIds(RealObj.value);
            }catch(e) {
            }
        }
    }
    if(typeof(toDoMouseClick) == "function") {
        toDoMouseClick(event);
    }
    return true;
}

function MouseDblClick(event){  //双击事件
    var target=event.srcElement?event.srcElement:event.target;
    var RealObj = jQuery(target);
    if(RealObj.attr("type")=="label"||RealObj.attr("type")=="load"){
        var objNode=RealObj.parent();
        if(objNode.id!=""){
            window.returnValue = objNode.attr("returnValue")+","+objNode.text();
        }
    }
    if(outputType == "doubleClick") {
        window.close();
    }
    if(typeof(toDoMouseDbClick) == "function") {
        toDoMouseDbClick(event);
    }
    return true;
}

function MouseOver(event){
    var target=event.srcElement?event.srcElement:event.target;
    var RealObj = jQuery(target);
    //jQuery1.3
    //if(RealObj.attr("tagName").toUpperCase()=="A")
    
    //jQuery1.6
    if(RealObj.get(0).tagName.toUpperCase()=="A")
        RealObj=RealObj.parent();
    if(RealObj){
        if(RealObj.attr("type")=="label"||RealObj.attr("type")=="load"){
            oTempClass=RealObj.attr("class");
            RealObj.attr("class","MouseOver");
        }
    }
    if(typeof(toDoMouseOver) == "function") {
        toDoMouseOver(event);
    }
    return true;
}

function MouseOut(event){
    var target=event.srcElement?event.srcElement:event.target;
    var RealObj = jQuery(target);
    //jQuery1.3
    //if(RealObj.attr("tagName").toUpperCase()=="A")
    
    //jQuery1.6   
    if(RealObj.get(0).tagName.toUpperCase()=="A")
        RealObj=RealObj.parent();
    if(RealObj){
        if(RealObj.attr("type")=="label"||RealObj.attr("type")=="load"){
            if(RealObj.get(0)==oCurrentNode)
                RealObj.attr("class","HighLight");
            else
                RealObj.attr("class",oTempClass);
        }
    }
    if(typeof(toDoMouseOut) == "function") {
        toDoMouseOut(event);
    }
    return true;
}
/**********************　结束监听事件相关方法 **********************/

/**********************　开始checkbox状态维护提交方法 **********************/  
function checkChild(oChk,arrayOfStrings,state){  //check子节点的状态
    if(arrayOfStrings == undefined || arrayOfStrings == null)
        return false;
    for(var i=0;i<arrayOfStrings.length;i++){
        var obj = getObjectById(prefixCheckbox + arrayOfStrings[i]);
        if(obj != null) {
            obj.indeterminate = false;
            obj.checked = state;
        } else {
            continue;
        }           
        if(obj.parentElement.type=="parent" && obj.parentElement.opened=="true"){
            checkChild(obj,gradeMapChild[obj.id.substring(prefixCheckbox.length)], obj.checked);
        }
    }
}
    
function setParentState(checkboxid){  //设置父checkbox为不确定状态或者选或者不选, checkboxid是待处理节点id，父可能变灰、选、不选
    if(checkboxid == undefined || checkboxid == null)
        return false;
    var parentNode = null;
    var tempBrotherArray = null;
    var parentState = 30; //2,不选 3,不确定 5,选
    var tempState = null;
    if(!arrayHasString(rootNode, checkboxid.substring(prefixCheckbox.length))) {
        var thisObj = getObjectById(checkboxid);
        
        parentNode = gradeMapParent[checkboxid.substring(prefixCheckbox.length)];
        if(parentNode == undefined || parentNode == null)
        parentNode = eval("gradeMapParent['\"' + checkboxid + '\"']");
            
        tempBrotherArray = gradeMapChild[parentNode];
        if(tempBrotherArray == undefined || tempBrotherArray == null)
        tempBrotherArray = eval(gradeMapChild['"' + parentNode + '"']);
            
        for(var i=0; i<tempBrotherArray.length; i++) {
            var tempObj = getObjectById(prefixCheckbox + tempBrotherArray[i]);
            if((tempState != null && tempState != tempObj.checked) || (tempObj.indeterminate)) {
                parentState = 3;
            }
            tempState = tempObj.checked;
        }
        
        if(parentState != 3 && tempState != null && tempState == true) {
            parentState = 5;
        } else if(parentState != 3 && tempState != null && tempState == false) {
            parentState = 2;
        }
        
        if(parentState == 3) {
            getObjectById(prefixCheckbox + parentNode).checked = false;
            getObjectById(prefixCheckbox + parentNode).indeterminate = true;
            setState(prefixCheckbox + parentNode);
        } else if(parentState == 2) {
            getObjectById(prefixCheckbox + parentNode).indeterminate = false;
            getObjectById(prefixCheckbox + parentNode).checked = false;
            setParentState(prefixCheckbox + parentNode);
        } else if(parentState == 5) {
            getObjectById(prefixCheckbox + parentNode).indeterminate = false;
            getObjectById(prefixCheckbox + parentNode).checked = true;
            setParentState(prefixCheckbox + parentNode);
        }
    } else {
    }
}
    
function setState(checkboxid) {  //设置父checkbox为不确定状态, checkboxid是父节点id，保证变灰
    if(!arrayHasString(rootNode, checkboxid.substring(prefixCheckbox.length))) {
        var obj = getObjectById(checkboxid);
        obj.checked = false;
        obj.indeterminate = true;
        setState(prefixCheckbox + gradeMapParent[obj.id.substring(prefixCheckbox.length)]);         
    } else {
        var obj = getObjectById(checkboxid);
        obj.checked = false;
        obj.indeterminate = true;
    }
}

function createDoc(text){  
    var xmlDoc = null;  
    try //Internet Explorer  
    {  
      xmlDoc=new ActiveXObject("Microsoft.XMLDOM");  
      xmlDoc.async="false";  
      xmlDoc.loadXML(text);  
    }  
    catch(e)  
    {  
      try //Firefox, Mozilla, Opera, etc.  
        {  
        parser=new DOMParser();  
        xmlDoc=parser.parseFromString(text,"text/xml");  
        }  
      catch(e) {alert("current browser can't create xml document")}  
    }  
    return xmlDoc;  
}  
/**********************　结束checkbox状态维护提交方法 **********************/  
