//用户调用的beanID
var beanId = "docTypeBS";
//树的根栏目
var rootID = "root";

//--------------初始化页面--------------------------------------------------------------
function init(setting) {
    //onLoadTree();
    jQuery.fn.zTree.init(jQuery("#tree"), setting);
    jQuery("#docTypeDetailDiv").hide();
    jQuery("#moveDocTypeDiv").hide();
    jQuery("#addDocTypeDiv").hide();
    jQuery("#docTypeUpdateDiv").hide();
    jQuery("#defalutPageDiv").show();
}

//用户自定义初始化树
//    function onLoadTree(){
//    webModel=form_treebasic.webModel.value;
//        var tree_href = jQuery("#tree_href").val();         //栏目链接
//        var tree_onclick = jQuery("#tree_onclick").val();   //单击事件方法名
//        var tree_param = "id";             //链接参数名
//        var tree_target = jQuery("#tree_target").val();     //链接打开的目标窗口
//        var site_id =  jQuery("#site_id").val();
//        var treeid = "tree";
//        var paramJson = '{tree_href:"'+tree_href+'",tree_onclick:"'+tree_onclick+'",treeid:"'+treeid+'",tree_param:"'+tree_param+'",site_id:"'+site_id+'",webModel:"'+webModel+'",tree_target:"'+tree_target+'"}';
//        initTree(rootID,'tree',paramJson);
//    }

//单击展开/收缩 事件
function expandAndCollapse(triggeredEvent) {
    webModel = form_treebasic.webModel.value;
    if (jQuery.browser.mozilla) {
        if (triggeredEvent.target.id == "foldheader" || triggeredEvent.target.id == "deptheader") {
            var $current = jQuery(triggeredEvent.target.nextSibling.nextSibling);
            var $next = $current.parent().next();
            var imagesrc = triggeredEvent.target.src;
            if ($next.css("display") == null || $next.css("display") == "none") {
                $next.slideDown(200);
                if (imagesrc.indexOf("root.png") == -1) {
                    triggeredEvent.target.src = webModel + "/images/tree/parentopen.gif";
                }
            } else {
                $next.slideUp(200);
                if (imagesrc.indexOf("root.png") == -1) {
                    triggeredEvent.target.src = webModel + "/images/tree/parent.gif";
                }
            }
        }
    } else {
        if (window.event.srcElement.id == "foldheader" || window.event.srcElement.id == "deptheader") {
            var $current = jQuery(window.event.srcElement.nextSibling.nextSibling);
            var $next = $current.parent().next();
            var imagesrc = window.event.srcElement.src;
            if ($next.css("display") == null || $next.css("display") == "none") {
                $next.slideDown(200);
                if (imagesrc.indexOf("root.png") == -1) {
                    window.event.srcElement.src = webModel + "/images/tree/parentopen.gif";
                }
            } else {
                $next.slideUp(200);
                if (imagesrc.indexOf("root.png") == -1) {
                    window.event.srcElement.src = webModel + "/images/tree/parent.gif";
                }
            }
        }
    }
}

String.prototype.trim = function () {
    //   用正则表达式将前后空格
    //   用空字符串替代。
    return   this.replace(/(^\s*)|(\s*$)/g, "");
}

//用户调用的beanID
var beanId = "docTypeBS";
//树的根节点
var rootID = "root";
//新增根节点和新增节点时，记住根节点信息或者父节点信息
var nowData;


//---------------树节点单击事件--------------------------------------------------------------------
function doClick(obj) {
    //每次点击树节点都要设置父窗口中的隐藏域docTypeID的值，方便条件查询
    // parent.document.forms[0].elements['docTypeID'].value = obj.id;
    //高亮显示所选节点
    treeNodeOnfocus(obj);
}

function doDblclick() {
}
function doMousedown() {
}

//显示当前节点的信息
function displayNode(nodeId, divid) {
    //隐藏新增信息
    divExtend("newTreeNodeDiv", "none");
    //显示修改信息
    divExtend("treeNodeDiv", "block");
    //隐藏用户页面
    divExtend('treeNodeMessageDiv', "block");
    //载入修改页面
    includeEditPage(nodeId, divid);
}

//保存修改后的节点信息
function updateNode() {
    if (confirm(i18n4ajaxList.confirm_to_update_node)) {
        var nodeid = form_treebasic.id.value;
        var divid = form_treebasic.divid.value;
        var nodeName = form_treeNode.nodeName.value.trim();
        var locations = document.getElementById("locations");
        if (checkFormsForName("form_treeNode") == true) {
            DWREngine.setErrorHandler(eh);
            var map = {"id": nodeid, "divid": divid, "name": nodeName, "nodeName": nodeName, "locations": locations.value};//属性值id,divid是map中必须有的值。
            EwpTreeControl.updateNode(map, beanId, function (data) {
                populateSpan(data);
                nowData = data;
                alert(i18n4ajaxList.update_success);
            });
        }
    }
}
//删除选中的节点
function deleteNode() {
    var nodeid = form_treebasic.id.value;
    var divid = form_treebasic.divid.value;
    var parentId = form_treebasic.parentId.value;
    var nodeName = form_treeNode.nodeName.value.trim();
    DWREngine.setErrorHandler(eh);
    var map = {"id": nodeid, "divid": divid, "parentId": parentId};//属性值id,divid和parentId是map中必须有的值。

    EwpTreeControl.deleteNode(map, beanId, function (data) {
        setUlOrLiForDisabled(data);//删除节点时，使树节点隐藏
        changeNodeAttribute(data);
    });
    setButtonForNoDisabled("addButton", "addButtonTd", null, true);//设置新增按钮disabled,同时修改该按钮所在td的样式
    setButtonForNoDisabled("deleteButton", "deleteButtonTd", null, true);//设置删除按钮disabled,同时修改该按钮所在td的样式
}
//在选中的节点下增加子节点
function addNode() {
    var nodeid = form_treebasic.id.value;
    var divid = form_treebasic.divid.value;
    var newNodeName = form_treeNode.nodeName.value.trim();
    //获取用户的过滤条件
    var locations = document.getElementById("locations");
    var location_query = document.getElementById("location_query");
    if (checkFormsForName("form_treeNode") == true) {
        DWREngine.setErrorHandler(eh);
        var map = {"parentId": nodeid, "divid": divid, "name": newNodeName, "locations": locations.value, "location_query": location_query.value};//属性值parentId和divid是map中必须有的值。
        EwpTreeControl.addNode(map, beanId, function (data) {
            if (data.treevalue != "") {
                populateDiv(data);
                changeNodeAttribute(data);
                setForms(nowData);
            }
        });
        //隐藏新增信息
        divExtend('newTreeNodeDiv', "none");
        //隐藏修改信息
        divExtend('treeNodeDiv', "block");
    }
}
//增加根节点,方法名称+S是为了避免关键字。
function addRoots() {
    var newNodeName = form_treeNode.nodeName.value.trim();
    if (checkFormsForName("form_treeNode") == true) {
        DWREngine.setErrorHandler(eh);
        var map = {"name": newNodeName};//属性值name是map中必须有的值。
        EwpTreeControl.addRoot(map, beanId, function (data) {
            if (data.treevalue != "") {
                populateDiv(data);
                setForms(nowData);
            }
        });
    }
    //隐藏新增根节点按钮信息
    divExtend('newTreeRootDiv', "none");
    //隐藏修改节点按钮信息
    divExtend('treeNodeDiv', "none");
    //隐藏用户修改信息
    divExtend('treeNodeMessageDiv', "none");
}

//载入新增节点页面
function includeAddPage() {
    var pageobj = "/jsp/ajaxList/tree/treeIncludeOfDB.jsp";///新增页面，用户修改
    EwpTreeControl.getInclude(pageobj, function (data) {
        dwr.util.setValue('treeNodeMessageDiv', data, { escapeHtml: false });
    });
}

//载入新增根节点页面
function includeAddRootPage() {
    var pageobj = "/jsp/ajaxList/tree/treeIncludeOfDB.jsp";///新增页面，用户修改
    EwpTreeControl.getInclude(pageobj, function (data) {
        dwr.util.setValue('treeNodeMessageDiv', data, { escapeHtml: false });
    });
}

//载入修改页面
function includeEditPage(nodeId, divid) {
    var pageobj = "/jsp/ajaxList/tree/treeIncludeOfDB.jsp";//新增页面，用户修改
    EwpTreeControl.getInclude(pageobj, function (data) {
        dwr.util.setValue('treeNodeMessageDiv', data, { escapeHtml: false });
        DWREngine.setErrorHandler(eh);
        form_treebasic.id.value = nodeId;
        form_treebasic.divid.value = divid;
        EwpTreeControl.getNodeMessage(nodeId, beanId, function (data) {
            setForms(data);
            nowData = data;
        });
    });
}


/*
 *往页面填充值
 *@param data 后台往前台传的vo
 */
function setForms(data) {
    for (var property in data) {
        dwr.util.setValue(property, data[property], { escapeHtml: false });
    }
}

function checkFormsForName(formName) {//校验名称为formName值的form 中的控件
    var checkResult = true;
    rmTempStatusIsAlert = false;
    rmTempStatusIsFocus = false;

    setAllVenusInputsDefault();
    for (var i = 0; i < document.forms.length; i++) {
        if (formName != document.forms[i].name) {
            continue;
        }
        for (var j = 0; j < document.forms[i].elements.length; j++) {
            var thisInput = document.forms[i].elements[j];
            if (thisInput.type != "hidden" && thisInput.type != "button" && !( thisInput.id.indexOf("TF_") >= 0 && thisInput.id.indexOf("_TF") > 0 )) {
                var rtValue = check(thisInput);
                if (checkResult && rtValue == false) {
                    checkResult = false;
                }
            }
        }
    }
    return checkResult;
}
function extendsParamsMap(paramsMap) {//用户扩展过滤条件
    //获取用户的过滤条件
    var locations = document.getElementById("location_query");
    if (locations.value != "") {
        //用户自定义的过滤条件
        paramsMap["location_query"] = locations.value;
    }
}
//===========================================用户自定义方法
function simpleQuery_onClick() {
    nodeDivHide();//隐藏节点信息 
    setButtonForNoDisabled("addButton", "addButtonTd", null, true);//新增按钮失效
    setButtonForNoDisabled("deleteButton", "deleteButtonTd", null, true);//删除按钮失效
    var paramsMap = {};
    //获取用户的过滤条件
    var locations = document.getElementById("location_query");
    if (locations.value != "") {
        //用户自定义的过滤条件
        paramsMap["location_query"] = locations.value;
    }
    initTree('root', 'tree', paramsMap);
}
