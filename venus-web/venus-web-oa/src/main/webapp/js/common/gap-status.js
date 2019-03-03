/**********************************************************************
 *Venus web Control JavaScript Code: 状态栏&消息处理机制 
 *@author: zhangzehang
 *@author: huqi has some modify
 *@date 2005-01-07
 **********************************************************************/

/**常量ID**/
var currentAccountElementId = "currentAccount";
var currentOperationElementId = "currentOperation";
var loginPersonnelElementId = "loginPersonnel";
var operationHintElementId = "operationHint";
var messageQueueElementId = "messageQueue";

var venus_Msgs = new Array();
var venus_AlertMsgStrs = new Array();
var venus_Msg_Status = false;

//Frame 名称
var statusFrameName = "statusFrame";

function Messager()
{
	this.alertMessages = new Array();
	/******** 登录名称 ********/
	this.currentAccount;
	/******** 登录人员 ********/
	this.loginPersonnel;
	/******** 当前业务 ********/
	this.currentOperation;
	/******** 操作提示 ********/
	this.operationHint;
}

/**
 * @decription 设置警告信息
 * 
 * @param _message 警告信息
 */
Messager.prototype.setAlert= function(_message)
{
	this.alertMessages.push(_message);
};

/**
 * @decription 获取警告信息
 * 
 * @param 无
 */
Messager.prototype.getAlertMessages= function()
{
	return this.alertMessages;
};

/**
 * @decription 设置当前帐号
 * 
 * @param _loginId 登录字符串
 */
Messager.prototype.setCurrentAccount= function(_currentAccount)
{
	this.currentAccount = _currentAccount;
};

/**
 * @decription 获取当前帐号
 * 
 * @param 无
 */
Messager.prototype.getCurrentAccount= function()
{
	return this.currentAccount;
};

/**
 * @decription 设置当前业务
 * 
 * @param _operation 业务字符串
 */
Messager.prototype.setCurrentOperation= function(_operation)
{
	this.currentOperation = _operation;
}

/**
 * @decription 获取当前业务
 * 
 * @param 无
 */
Messager.prototype.getCurrentOperation= function()
{
	return this.currentOperation;
}

/**
 * @decription 设置已登录人员
 * 
 * @param _loginPersonnel 业务字符串
 */
Messager.prototype.setLoginPersonnel= function(_loginPersonnel)
{
	this.loginPersonnel = _loginPersonnel;
}

/**
 * @decription 获取已登录人员
 * 
 * @param 无
 */
Messager.prototype.getLoginPersonnel= function()
{
	return this.loginPersonnel;
}

/**
 * @decription 设置操作提示
 * 
 * @param _hint 提示内容
 */
Messager.prototype.setOperationHint= function(_operationHint) 
{
	this.operationHint = _operationHint;
}


/**
 * @decription 获取操作提示
 * 
 * @param 无
 */
Messager.prototype.getOperationHint= function() 
{
	return this.operationHint;
}

/**
 * @decription 调用某个桢的函数
 * 
 * @param _frameName 桢名
 * @param _functionName 桢名 
 * @param _args 宽度
 */
function call_Function(_frameName,_functionName,_args)
{
	/**获取桢对象**/
	var frameTemp = getFrameByName(_frameName);
	/**形参数处理**/
	var argsTemp;
	for(var i=0;i<args.length;i++)
	{
		argsTemp = argsTemp + args[i];
		if(args.length != i)
		argsTemp = argsTemp + ',';
	}
	/**调用函数**/
	eval(frameTemp + "." + _functionName + "("+argsTemp+")");
}

/**
 * @decription 修改状态拦信息 触发警告
 * 
 * @param _messager 
 */
function commit(_messager)
{
	/*var alertMessages = _messager.getAlertMessages();
	
	for(var i=0;i<alertMessages.length;i++)
	{
		alert(alertMessages[i]);
	}*/
	if( _messager.getCurrentAccount() != null ) setCurrentAccount(_messager.getCurrentAccount());
	if( _messager.getLoginPersonnel() != null ) setLoginPersonnel(_messager.getLoginPersonnel());
	if( _messager.getCurrentOperation() != null ) setCurrentOperation(_messager.getCurrentOperation());
	if( _messager.getOperationHint() != null ) setOperationHint(_messager.getOperationHint());
}

/**********************************************************************
 *Venus web Control JavaScript Object:桢相关操作函数
 **********************************************************************/

/**
 * @decription 根据祯名获取祯
 * 
 * @param _frameName 祯名
 */
function getFrameByName(_frameName)
{
	var framesObj = window.top.frames;
	for(var i=0;i<framesObj.length;i++)
	{
		if(framesObj[i].name == _frameName) {
			return framesObj[i];
		}
	}
	return null;
}

/**
 * @decription 改变桢大小 window对象的一种类型
 * 
 * @param _frameName 桢名
 * @param _width 宽度
 * @param _height 高度  
 */
function resizeFrame(_frameName,_width,_height)
{
	/**获取桢对象**/
	var frameTemp = getFrameByName(_frameName);
	if(frameTemp == null)
		return;
	/**改变桢大小**/
	frameTemp.resizeTo(_width,_height);
}


/**
 * @decription 信息提示函数
 * 
 * @param _message 信息内容
 */
function refreshFrame(_message)
{
}

/**********************************************************************
 *Venus web Control JavaScript Object:API 可外部直接调用
 **********************************************************************/

/**
 * @decription 设置当前帐号
 * 
 * @param _account 帐号字符串
 */
function setCurrentAccount(_currentAccount)
{
	var statusFrame = getFrameByName(statusFrameName);
	if(statusFrame == null)
		return;	
	statusFrame.document.getElementById('currentAccount').innerHTML = _currentAccount;	
}

/**
 * @decription 设置已登录人员
 * 
 * @param _loginPersonnel 业务字符串
 */
function setLoginPersonnel(_loginPersonnel)
{
	var statusFrame = getFrameByName(statusFrameName);
	if(statusFrame == null)
		return;		
	statusFrame.document.getElementById('loginPersonnel').innerHTML = _loginPersonnel;	
}

/**
 * @decription 设置当前业务
 * 
 * @param _operation 业务字符串
 */
function setCurrentOperation(_currentOperation)
{
	var statusFrame = getFrameByName(statusFrameName);
	if(statusFrame == null)
		return;		
	statusFrame.document.getElementById('currentOperation').innerHTML= _currentOperation;		
}

/**
 * @decription 设置操作提示
 * 
 * @param _hint 提示内容
 */
function setOperationHint(_operationHint)
{
	var statusFrame = getFrameByName(statusFrameName);
	if(statusFrame == null)
		return;		
	statusFrame.document.getElementById('operationHint').innerHTML= _operationHint;	
}

/**********************************************************************
 *Venus web Control JavaScript Object:消息队列 对象 依赖js.Map
 **********************************************************************/

function MessageQueue()
{
}

/**
 * @decription 消息队列初始化
 * 
 * @param _action 服务器端 ACTION 名称
 * @param _params 参数传递
 * @param _frameName 源桢 名称  
 */
MessageQueue.prototype.init = function()
{
	this.messageQueue = new Map();
};

/**
 * @decription 添加消息
 * 
 * @param _message 消息
 */
MessageQueue.prototype.pushMessage= function(_message)
{
}

/**
 * @decription 弹出一条消息
 */
MessageQueue.prototype.popMessage= function()
{
}

/**
 * @decription 弹出所有消息
 */
MessageQueue.prototype.popAllMessage= function()
{
	var messagesTemp = new Array();
	return messagesTemp;
}

/**
 * @decription 信息提示函数
 * 
 * @param _message 信息内容
 */
function showAlertMessage(_message)
{
}

/**
 * @decription 信息提示函数
 * 
 * @param _message 信息内容
 */
function showConfirmMessage(_message)
{
}

/**
 * @decription 信息提示函数
 * 
 * @param _message 信息内容
 */
function showPromptMessage(_message)
{
}

/**
 * @decription 信息提示函数
 * 
 * @param _message 信息内容
 */
function venus_append_commit(_message)
{
	venus_Msgs.push(_message);	
}

/**
 * @decription 信息提示函数
 * 
 * @param _message 信息内容
 */
function venus_append_alert(_messageAlertStr)
{
	venus_AlertMsgStrs.push(_messageAlertStr);
}

/* add by huqi */
function popAlertScript(){
	var msg = "";
	for(i=0;i<venus_AlertMsgStrs.length;i++){
		msg = msg + venus_AlertMsgStrs[i] + "\r\n";
	}
	alert(msg);
}

/**
 * @decription 信息提示函数
 * 
 * @param _message 信息内容
 */
function venus_all_commit()
{
	for(var i=0;i<venus_Msgs.length;i++)
	{
		var _messager = venus_Msgs[i];
		if( _messager.getCurrentAccount() != null ) setCurrentAccount(_messager.getCurrentAccount());
		if( _messager.getLoginPersonnel() != null ) setLoginPersonnel(_messager.getLoginPersonnel());
		if( _messager.getCurrentOperation() != null ) setCurrentOperation(_messager.getCurrentOperation());
		if( _messager.getOperationHint() != null ) setOperationHint(_messager.getOperationHint());
	}
}

/**
 * @decription 信息提示函数
 * 
 * @param _message 信息内容
 */
function venus_all_alert()
{
	for(var i=0;i<venus_AlertMsgStrs.length;i++)
	{
		var mgr = venus_AlertMsgStrs[i];
		alert( mgr );
	}
}