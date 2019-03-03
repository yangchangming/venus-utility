var rootIcon       = basePath+"/js/au/tree_img/root_close.gif"; 	//根目录关起时状态图标
var openRootIcon   = basePath+"/js/au/tree_img/root_open.gif"; 	//根目录展开时状态图标
var folderIcon     = basePath+"/js/au/tree_img/folder_close.gif";	//子文件夹关起时状态图标
var openFolderIcon = basePath+"/js/au/tree_img/folder_open.gif";	//子文件夹展开时状态图标
var fileIcon       = basePath+"/js/au/tree_img/file.gif";	//链接叶子节点图标
var iIcon          = basePath+"/js/au/tree_img/i_line.gif";		//普通的连接线（直的）
var lIcon          = basePath+"/js/au/tree_img/l_line.gif";		//最后一个节点的连接线
var lMinusIcon     = basePath+"/js/au/tree_img/l_point_open.gif";	//最后一个文件夹节点（展开时的）图标
var lPlusIcon      = basePath+"/js/au/tree_img/l_point_close.gif";//最后一个文件夹节点（关起时的）图标
var tIcon          = basePath+"/js/au/tree_img/t_line.gif";		//中间节点的连接线
var tMinusIcon     = basePath+"/js/au/tree_img/t_point_open.gif";	//中间文件夹节点（展开时的）图标
var tPlusIcon      = basePath+"/js/au/tree_img/t_point_close.gif";//中间文件夹节点（关起时的）图标
var blankIcon      = basePath+"/js/au/tree_img/blank.gif";		//中间空白的图标图标

var defaultText    = "Tree Item";
//var defaultAction  = "";	//javascript:void(0);
var defaultAction  = window.parent.document.getElementById('orgTreeSrc').value;
var targetStr="main";
var bgcolor_sel="#FFCC99";
var selectedObj = null;
var treeBkColor="F3F3F3";
var inputArray = new Array(0);

var webFXTreeHandler = {
	idCounter : 0,
	idPrefix  : "webfx-tree-object-",
	all       : {},
	behavior  : "classic",
	getId     : function () { return this.idPrefix + this.idCounter++; },
	toggle    : function (oItem) { this.all[oItem.id.replace("-plus","")].toggle(); },
	select    : function (oItem) { this.all[oItem.id.replace("-icon","")].select(); },
	focus     : function (oItem) { this.all[oItem.id.replace("-anchor","")].focus(); },
	blur      : function (oItem) { this.all[oItem.id.replace("-anchor","")].blur(); }
};

function WebFXTree(sText, sAction, sBehavior) {
	this._subItems = [];
	this.id        = webFXTreeHandler.getId();
	this.text      = sText || defaultText;
	this.action    = sAction || defaultAction;
	this._wasLast  = false; // Used to keep track of the last item in each sub tree
	this.open      = true;//(getCookie(this.id.substr(18,this.id.length - 18)) == "0")?false:true;
	this.icon      = rootIcon;
	this.openIcon  = openRootIcon;
	this.target	   = targetStr;
	webFXTreeHandler.behavior =  sBehavior || "classic";
	webFXTreeHandler.all[this.id] = this;
}

WebFXTree.prototype.setBehavior = function (sBehavior) {
	webFXTreeHandler.behavior =  sBehavior;
};
WebFXTree.prototype.getBehavior = function (sBehavior) {
	return webFXTreeHandler.behavior;
};

WebFXTree.prototype.add = function (treeItem) {
	treeItem.parent = this;
	this._subItems[this._subItems.length] = treeItem;
};

WebFXTree.prototype.toString = function () {
	var str = "<div id=\"" + this.id + "\" ondblclick=\"webFXTreeHandler.toggle(this);\" class=\"webfx-tree-item\">";
	str += "<table height=\"10\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"480\"><tr><td width=\"10\">";
	str += "<img id=\"" + this.id + "-icon\" src=\"" + ((webFXTreeHandler.behavior == 'classic' && this.open)?this.openIcon:this.icon) + "\" onclick=\"webFXTreeHandler.select(this);\">";
	str += "</td><td valign=\"bottom\" nowrap>&nbsp;<a href=\"####\" id=\"" + this.id + "-anchor\"  onclick=\"folder_Click();\" onfocus=\"webFXTreeHandler.focus(this);\">" + this.text + "</a></td>";// onclick=\"treeitem_Click('" + this.action + "');\"
	str += "<td width=\"200\" nowrap valign=\"bottom\" align=\"center\">&nbsp;"+venus.authority.Authorization+"</td>";
	str += "</tr></table></div>";
	str += "<div id=\"" + this.id + "-cont\" class=\"webfx-tree-container\" style=\"display: " + ((this.open)?'block':'none') + ";\">";
	for (var i = 0; i < this._subItems.length; i++) {
		str += this._subItems[i].toString(i,this._subItems.length);
	}
	str += "</div>";	
	return str;
};

WebFXTree.prototype.getSelected = function () {
	if (selectedObj) { return selectedObj.id; }
	else { return null; }
}

WebFXTree.prototype.toggle = function () {
	if (this.open) { this.collapse(); }
	else { this.expand(); }
}

WebFXTree.prototype.select = function () {
	document.getElementById(this.id + '-anchor').focus();
}

WebFXTree.prototype.focus = function () {
	if (selectedObj) { selectedObj.blur(); }
	selectedObj = this;
	if ((this.openIcon) && (webFXTreeHandler.behavior != 'classic')) { document.getElementById(this.id + '-icon').src = this.openIcon; }
	//document.getElementById(this.id + '-anchor').style.backgroundColor = 'highlight';
	//document.getElementById(this.id + '-anchor').style.border = '1px dotted threedshadow';
	document.getElementById(this.id + '-anchor').style.color = '#E77B39';
}

WebFXTree.prototype.blur = function () {
	if ((this.openIcon) && (webFXTreeHandler.behavior != 'classic')) { document.getElementById(this.id + '-icon').src = this.icon; }
	//document.getElementById(this.id + '-anchor').style.backgroundColor = bgcolor_sel;
	//document.getElementById(this.id + '-anchor').style.border = '0px';
	document.getElementById(this.id + '-anchor').style.color = '#000000';
}

WebFXTree.prototype.expand = function () {
	if (webFXTreeHandler.behavior == 'classic') {
		document.getElementById(this.id + '-icon').src = this.openIcon;
	}
	document.getElementById(this.id + '-cont').style.display = 'block';
	this.open = true;
	setCookie(this.id.substr(18,this.id.length - 18), '1');
}

WebFXTree.prototype.collapse = function () {
	if (webFXTreeHandler.behavior == 'classic') {
		document.getElementById(this.id + '-icon').src = this.icon;
	}
	document.getElementById(this.id + '-cont').style.display = 'none';
	this.open = false;
	setCookie(this.id.substr(18,this.id.length - 18), '0');
}

WebFXTree.prototype.expandAll = function () {
	this.expandChildren();
	this.expand();
}

WebFXTree.prototype.expandChildren = function () {
	for (var i = 0; i < this._subItems.length; i++) {
		this._subItems[i].expandAll();
	}
}

WebFXTree.prototype.collapseAll = function () {
	this.collapse();
	this.collapseChildren();
}

WebFXTree.prototype.collapseChildren = function () {
	for (var i = 0; i < this._subItems.length; i++) {
		this._subItems[i].collapseAll();
	}
}

function WebFXTreeItem(sText,sTitle,treeID,selCode,selTxt,selVal,selWhoEx,selWho,isDisable,isFolder) {
	this._subItems  = [];
	this._wasLast   = false;
	this.text       = sText || defaultText;
	this.title      = sTitle || defaultText;
	this.action     = defaultAction;
	this.id         = webFXTreeHandler.getId();
	this.open       = true;//(getCookie(this.id.substr(18,this.id.length - 18)) == '1')?true:false;
	this.selCode	= selCode;
	this.selTxt		= selTxt;
	this.selNum		= selTxt==null?0:selTxt.length;
	this.selVal		= selVal;
	this.selWho		= selWho;
	this.selWhoEx	= selWhoEx;
	this.treeID		= treeID;
	this.isDisable  = isDisable;
	this.target		= targetStr;
	this.folder       = isFolder;
	webFXTreeHandler.all[this.id] = this;
};

WebFXTreeItem.prototype.add = function (treeItem) {
	treeItem.parent = this;
	this._subItems[this._subItems.length] = treeItem;
};

WebFXTreeItem.prototype.toggle = function () {
	if (this.open) { this.collapse(); }
	else { this.expand(); }
}

WebFXTreeItem.prototype.select = function () {
	try{
		document.getElementById(this.id + '-anchor').focus();
	}catch(e) {
	}
}

WebFXTreeItem.prototype.focus = function () {
	if (selectedObj) { selectedObj.blur(); }
	selectedObj = this;
	if ((this.openIcon) && (webFXTreeHandler.behavior != 'classic')) { document.getElementById(this.id + '-icon').src = this.openIcon; }
	//document.getElementById(this.id + '-anchor').style.backgroundColor = 'highlight';
	//document.getElementById(this.id + '-anchor').style.border = '1px dotted threedshadow';
	document.getElementById(this.id + '-anchor').style.color = '#E77B39';
	//OrgAuthorize 20080928 add link
	//window.parent.document.getElementById('orgTree').src=this.action+"%26resource_id%3D"+this.treeID+"&resource_id="+this.treeID;
}

WebFXTreeItem.prototype.blur = function () {
	if ((this.openIcon) && (webFXTreeHandler.behavior != 'classic')) { document.getElementById(this.id + '-icon').src = this.icon; }
	//document.getElementById(this.id + '-anchor').style.backgroundColor = bgcolor_sel;
	//document.getElementById(this.id + '-anchor').style.border = '0px';
	document.getElementById(this.id + '-anchor').style.color = 'menutext';
}

WebFXTreeItem.prototype.expand = function () {
	if (this._subItems.length > 0) { 
		document.getElementById(this.id + '-cont').style.display = 'block';
	}
	if (webFXTreeHandler.behavior == 'classic') {
		document.getElementById(this.id + '-icon').src = this.openIcon;
	}
	document.getElementById(this.id + '-plus').src = this.minusIcon;
	this.open = true;
	setCookie(this.id.substr(18,this.id.length - 18), '1');
}

WebFXTreeItem.prototype.collapse = function () {
	if (this._subItems.length > 0) {
		document.getElementById(this.id + '-cont').style.display = 'none';
	}
	if (webFXTreeHandler.behavior == 'classic') {
		document.getElementById(this.id + '-icon').src = this.icon;
	}
	document.getElementById(this.id + '-plus').src = this.plusIcon;
	this.open = false;
	setCookie(this.id.substr(18,this.id.length - 18), '0');
}

WebFXTreeItem.prototype.expandAll = function () {
	this.expandChildren();
	this.expand();
}

WebFXTreeItem.prototype.expandChildren = function () {
	for (var i = 0; i < this._subItems.length; i++) {
		this._subItems[i].expandAll();
	}
}

WebFXTreeItem.prototype.collapseAll = function () {
	this.collapse();
	this.collapseChildren();
}

WebFXTreeItem.prototype.collapseChildren = function () {
	for (var i = 0; i < this._subItems.length; i++) {
		this._subItems[i].collapseAll();
	}
}

WebFXTreeItem.prototype.toString = function (nItem,nItemCount) {
	var foo = this.parent;
	var indent = '';
	if (nItem + 1 == nItemCount) { this.parent._wasLast = true; }
	while (foo.parent) {
		foo = foo.parent;
		indent = "<img src=\"" + ((foo._wasLast)?blankIcon:iIcon) + "\">" + indent;
	}
	if (this.folder) {
		if (!this.icon) { this.icon = folderIcon; }
		if (!this.openIcon) { this.openIcon = openFolderIcon; }
		var str = "<div id=\"" + this.id + "\" ondblclick=\"webFXTreeHandler.toggle(this);\" class=\"webfx-tree-item\">";
		str += "<table height=\"10\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"480\">";
		str += "<tr><td nowrap valign=\"bottom\" height=\"10\">"+indent;
		str += "<img id=\"" + this.id + "-plus\" src=\"" + ((this.open)?((this.parent._wasLast)?lMinusIcon:tMinusIcon):((this.parent._wasLast)?lPlusIcon:tPlusIcon)) + "\" onclick=\"webFXTreeHandler.toggle(this);\">"
		str += "<img id=\"" + this.id + "-icon\" src=\"" + ((webFXTreeHandler.behavior == 'classic' && this.open)?this.openIcon:this.icon) + "\" onclick=\"webFXTreeHandler.select(this);\">";
		str += "&nbsp;<a href=\"#\" id=\"" + this.id + "-anchor\" onclick=\"folder_Click();\" onfocus=\"webFXTreeHandler.focus(this);\" title=\""+this.title+"\">" + this.text + "</a></td>";
		str += "<td width=\"200\" nowrap valign=\"bottom\" align=\"center\">&nbsp;";
		str +="</td></tr></table></div>";
		str += "<div id=\"" + this.id + "-cont\" class=\"webfx-tree-container\" style=\"display: " + ((this.open)?'block':'none') + ";\">";
		for (var i = 0; i < this._subItems.length; i++) {
			if (this._subItems[i].text !="@@@"){
				str += this._subItems[i].toString(i,this._subItems.length);
			}
		}
		str += "</div>";
	}
	else {
		if (!this.icon) {
			if (webFXTreeHandler.behavior == 'classic') {
				this.icon = fileIcon;
			}
			else {
				this.icon = folderIcon;
				this.openIcon = openFolderIcon;
			}
		}
		var str = "<div id=\"" + this.id + "\" class=\"webfx-tree-item\">";
		str += "<table  border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"480\">";
		str += "<tr><td nowrap valign=\"bottom\" height=\"10\">"+indent;
		str += "<img id=\"" + this.id + "-plus\" src=\"" + ((this.parent._wasLast)?lIcon:tIcon) + "\">"
		str += "<img id=\"" + this.id + "-icon\" src=\"" + this.icon + "\" onclick=\"webFXTreeHandler.select(this);\">";
		str += "&nbsp;<a href=\"#\" onclick=\"treeitem_Click('"+this.action + "','"+this.treeID+"');\" id=\"" + this.id + "-anchor\" onfocus=\"webFXTreeHandler.focus(this);\" title=\""+this.title+"\">" + this.text + "</a></td>";
		str += "<td width=\"200\" nowrap valign=\"bottom\" align=\"center\">&nbsp;";
		//授权情况
		if(this.selWho[0]=="" && this.selWho[1]=="") {
				str += venus.authority.Unauthorized;
		}else {
			for(var i=0; i<this.selNum; i++) {
				if(this.selWho[0]==this.selVal[i] && this.selWho[0]!="2") {
					str += this.selTxt[i]+" ";
				}
				if(judgeNum(this.selWho[1],this.selVal[i])) {//判断参数1是否可以通过乘积拆分成参数2
					str += this.selTxt[i]+" ";
				}
			}
		}
		str +="</td></tr></table></div>";
	}
	this.plusIcon = ((this.parent._wasLast)?lPlusIcon:tPlusIcon);
	this.minusIcon = ((this.parent._wasLast)?lMinusIcon:tMinusIcon);
	return str;
}
function setCookie(key, value) {
	//document.cookie = key + "=" + escape(value);
}

function getCookie(key) {
	if (document.cookie.length) {
		var cookies = ' ' + document.cookie;
		var start = cookies.indexOf(' ' + key + '=');
		if (start == -1) { return null; }
		var end = cookies.indexOf(";", start);
		if (end == -1) { end = cookies.length; }
		end -= start;
		var cookie = cookies.substr(start,end);
		return unescape(cookie.substr(cookie.indexOf('=') + 1, cookie.length - cookie.indexOf('=') + 1));
	}
	else { return null; }
}

function treeitem_Click(strAct,resourceId){
	window.parent.document.getElementById('orgTree').src=strAct+"%26resource_id%3D"+resourceId+"&resource_id="+resourceId;
}

function folder_Click(){
	alert(venus.authority.Please_select_the_leaf_node_operation_);
}

//判断参数1是否可以通过乘积拆分成参数2
function judgeNum(bigNum, smallNum) {
	if(bigNum=="" || smallNum<2)
		return false;
	for(var i=1; (i*smallNum)<=bigNum; i++) {
		if((i*smallNum)==bigNum)
			return true;
	}
	return false;
}

