//TODO: fckeditor在chrome下不可用
jQuery(function(){
    //gap-readOnly.htc
    jQuery(".text_field_readonly").attr("readOnly","true");
    
    //gap-readOnlyClear.htc
    jQuery(".text_field_reference_readonly,.text_field_half_reference_readonly").attr("readOnly","true").live("mousedown",text_field_reference_readonly_mousedown);
   
   //gap-textareaLimitWords.htc
    jQuery(".textarea_limit_words").live("keydown",function(event){checkMaxInput(getEventObj(event))}).live("keyup",function(event){checkMaxInput(getEventObj(event))});
  
    //gap-button.htc
    jQuery(".button_ellipse,.btn3_mouseout,.btn3_mouseover,.btn3_mousedown,.btn3_mouseup").
        live("mouseover",function(){jQuery(this).attr("class","").addClass("btn3_mousedown");}).live("mouseout",function(){jQuery(this).attr("class","").addClass("btn3_mouseout");})
       .live("mousedown",function(){jQuery(this).attr("class","").addClass("btn3_mouseover");}).live("mouseup",function(){jQuery(this).attr("class","").addClass("btn3_mouseup");})
       .live("click",buttonEclipse_doClick);
       
   //gap-strutsLayout.htc
   getTbodyColor();
   jQuery("table.listCss,table.listcssgd,table.listCssgd,table.listcss").live("mouseover",layoutDoMouseOver).live("mouseout",layoutDoMouseOut).live("click",layoutDoClick);
       
   //gap-textareaFckEditor.htc
   initFCKeditor();
   
   //Alex/initA.htc
   jQuery("a").live("focus",function(){this.blur();});
   
   
});

function text_field_reference_readonly_mousedown(event){
            eval(jQuery(this).attr("onPush"));//调用onPush自定义事件
            if(event.button == 2 && confirm(i18n.sure_clear_data)) {
                jQuery(this).val("");
                try {
                    if(jQuery(this).attr("hiddenInputId") != null) {
                        var aHidden = jQuery(this).attr("hiddenInputId").split(",");
                        for(var i=0; i<aHidden.length; i++) {
                            if(window.document.getElementById(aHidden[i]) != null) {
                                window.document.getElementById(aHidden[i]).value = "";
                            }
                        }
                    }
                } catch(e) {
                    alert(e.message);
                }
                event.stopPropagation();
                return false;
        }
}
function checkMaxInput(target) {//检查最大输入数
    if(target == undefined || target == null)
        return false;
    var maxLength = 500;
    if(jQuery(target).attr("maxLength")) {
        maxLength = jQuery(target).attr("maxLength");
    }
    if(target.value.length > maxLength) {
        target.value = target.value.substring(0,maxLength);
        writeValidateInfoAfterObject(i18n.input_too_long, target);
    } else {
        writeValidateInfoAfterObject(i18n.you_can_input_character(maxLength - target.value.length), target );
    }
}
function buttonEclipse_doClick(event) {
    if( jQuery(this).attr("type")=="reset")
        return;
    var onclickto = jQuery(this).attr("onclickto");
    var onPush = jQuery(this).attr("onPush");
    if( onclickto == null || (typeof onclickto.length== "undefined") || onclickto.length == 0 ){
        return;
    }
    beginValidate = true;
    if (!checkAllForms()) {
        event.stopPropagation();
        beginValidate = false;
        return false;
    }       
    beginValidate = false;
    //disableAllButton();
    var rtValue = eval(onclickto);
    if( rtValue != null && !rtValue ){
        event.stopPropagation();
        return false;       
    }
    showWait(event);
    if(jQuery(onPush)) eval(onPush);
}


//struts layout htc改造开始
var layoutConfig={
    defaultColor:"9fc3d4",  //默认列表颜色
    focusColor:"d4d0d0",    //默认聚焦颜色
    selectedColor:"a5bacf", //默认单击颜色
    theadColor:"B8D5F5",    //默认表头颜色
    oddColor:"e9e9e9",  //默认表体奇数行颜色
    evenColor:"ffffff"  //默认表体偶数行颜色  //E9E9E6
}

function layoutDoMouseOver(event) {  //处理鼠标进入事件
    
   var thisObj = getEventObj(event);
   var onPush = jQuery(thisObj).attr("onPush");
    if (thisObj.tagName.toLowerCase()=="td"&&jQuery(thisObj).attr("class")!=null){
        thisObj.style.cursor = "pointer";
        var thisTr = jQuery(thisObj).parent();
        /*jQuery1.3
        if(thisTr.attr("tagName").toLowerCase() != "tr") {
            thisTr = thisTr.parent();
        }*/
        //jQuery1.6.4
        if(thisTr.get(0).tagName.toLowerCase() != "tr") {
            thisTr = thisTr.parent();
        }
         if(!thisTr.attr("isClick"))thisTr.attr("oldColor",thisTr.css("background-color")).css("background-color",layoutConfig.focusColor);
    } else {
        if(jQuery(onPush)) eval(onPush);
    }
}

function layoutDoMouseOut(event) {  //处理鼠标离开事件
   var thisObj = getEventObj(event);
    if (thisObj.tagName.toLowerCase() == "td") {
       // getTbodyColor();
       var tr = jQuery(thisObj).parent();
        if(!tr.attr("isClick")){
            tr.css("background-color",tr.attr("oldColor"));
        }else{
            tr.removeAttr("isClick");
        }
    } else {
    
    }
}
function layoutDoClick(event) {  //处理鼠标单击事件
    var thisObj = getEventObj(event);
    var thisTag = undefined;
    if (thisObj.tagName.toLowerCase() != "input" && thisObj.tagName.toLowerCase() != "img") {
        if (thisObj.firstChild != null && thisObj.firstChild != "") {
            thisTag = thisObj.firstChild.tagName;
        }
    }
   //表头复选框多选
    if(thisObj.tagName.toLowerCase() == "input" && jQuery(thisObj).attr("type") == "checkbox" && jQuery(thisObj).attr("pdType") == "control") {
        var tempControl = jQuery(thisObj).attr("control");
        var aCheckbox = getObjectByName(tempControl);
        if ( !aCheckbox || aCheckbox.size == 0 )
             aCheckbox = getObjectById(tempControl);
        if(aCheckbox == null) {
        
        } else if(aCheckbox.length == null) {
            aCheckbox.checked = thisObj.checked;
        } else {
            for(var i=0; i<aCheckbox.length; i++) {
                aCheckbox[i].checked = thisObj.checked;
            }
        }
    } else if(thisObj.tagName.toLowerCase() == "td" && (thisTag == undefined || thisTag != undefined && thisTag.toLowerCase() != "table")) {
        var thisTr = jQuery(thisObj).parent();
        thisTr.attr("isClick",true).css("background-color",layoutConfig.selectedColor);
        var thisCheckbox = getCheckboxFromTr(thisTr[0]);
        if(thisCheckbox != undefined && thisCheckbox != null) {
            thisCheckbox.checked = !thisCheckbox.checked;
        }
    } else {
    
    }
    var tables = jQuery(thisObj).parents("table").get(0);
    initTableColor(jQuery(tables));
    event.stopPropagation();
}
function showMessage(message) {  //处理事件
}

function getEventObj(thisEvent) {
   if(thisEvent == undefined || thisEvent == null ) {
        alert("当前的对象为空!");
        return null;
    } else if(thisEvent.srcElement){
        return thisEvent.srcElement;
    }else{
        return thisEvent.target;
    }
}

function getCheckboxFromTr(thisTr) {
    if(thisTr.childNodes.length == 0) {
        return null;
    }
    if(jQuery(":checkbox",thisTr)[0])
        return jQuery(":checkbox",thisTr)[0];
    return jQuery(":radio",thisTr)[0];
}

function getObjectByName(name) {
    return window.document.getElementsByName(name);
}

function getObjectById(id) {
    var tempobjs = jQuery("input");
    var objs = [];
    if ( tempobjs.size()==0 )
        return jQuery("#"+id);
    tempobjs.each(function(){
        if ( this.type == "checkbox" && this.id == id )
            objs.push( this);
    });
    return objs;
}

//设置表体颜色（奇数和偶数行）
function getTbodyColor(element,selector) {
    var tables;
    if(element&&jQuery(element).size>0)
            tables= jQuery("table.listcssgd,table.listCss,table.listCssgd,table.listcss,"+selector,jQuery(element)).find("td > table")
    else
            tables= jQuery("table.listcssgd,table.listCss,table.listCssgd,table.listcss,"+selector).find("td > table")
   
    initTableColor(tables);
}
function initTableColor(tables){
     tables.each(function(){
        var trs = jQuery(this).children().children().filter(function(i){return i!=0});
        
        trs.each(function(i){
                if (i%2 == 1) {
                    jQuery(this).css("background-color",layoutConfig.evenColor);
                } else {
                    jQuery(this).css("background-color",layoutConfig.oddColor);
                }//选中后保留颜色
                var thisCheckbox = getCheckboxFromTr(this);
                if(thisCheckbox&&thisCheckbox.checked) {
                   jQuery(this).css("background-color",layoutConfig.selectedColor);
                }
        });
    })
}

//从Table标签里面发现Th标签
function getThFromTable(thisObj) {
    if (thisObj != undefined && thisObj != null && (typeof thisObj == "object") ) {
        if (!thisObj.hasChildNodes()) {
            return null;
        } else {
            for(var i=0; i<thisObj.childNodes.length; i++) {
                var thisChild = thisObj.childNodes[i];
                if (thisChild.tagName != undefined) {
                    if (thisChild.tagName.toLowerCase() == "th") {
                        return thisChild;
                    } else {
                        var tempResult = getThFromTable(thisChild);
                        if (tempResult != null) {
                            return tempResult;
                        }
                    }
                }
            }
        }
    }
}
//struts layout htc改造结束

function initFCKeditor(){//一个页面中可能会有多个fckeditor，所以需要循环，但建议不要放多个
    jQuery(".textarea_fckEditor").each(function(){
        initTextarea4FCKeditor(this);
    })
}
var status = {
    isLoad: false
};
var oFCKeditor = null;
function initTextarea4FCKeditor(thisObj) {
    try {
        if(status.isLoad) {
            return;
        }
        var sBasePath = dir_base + '/jsp/include/FCKeditor/';
        oFCKeditor = new FCKeditor(thisObj.name);
        oFCKeditor.BasePath = sBasePath ;
        var skin = jQuery(thisObj).attr("skin")
        if ( skin != null && (skin =="default" || skin =="office2003" || skin =="silver")) {
            oFCKeditor.Config['SkinPath'] = sBasePath + 'editor/skins/' + skin + '/' ;
        }
        oFCKeditor.ReplaceTextarea();
        status.isLoad = true;
    } catch(e) {
        alert(e.message);
    }
}
