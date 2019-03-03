/********************************************************************************
*Venus ListApplet Control JavaScript Object:VENUS MAP 数据对象 仿照java.util.Map**
*huqi said that has some bugs****************************************************
*********************************************************************************/

function Map()
{
	this.keySet = new Array();
	this.entrySet = new Array();
}

Map.prototype.clear = function()
{
	this.keySet = new Array();
	this.entrySet = new Array();
};

Map.prototype.containsKey = function(_objectKey)
{
	for(var i=0;i<this.keySet.length;i++)
	{
		if(this.keySet[i] == _objectKey)
			return true;
	}
	return false;
};

Map.prototype.containsValue = function(_objectValue)
{
	for(var i=0;i<this.entrySet;i++)
	{
		if(this.entrySet[i] == _objectValue)
			return true;
	}
	return false;
};

Map.prototype.get = function(_objectKey)
{	
	for(var i=0;i<this.keySet.length;i++)
	{
		if(this.keySet[i] == _objectKey)
			return this.entrySet[i];
	}
	return null;
};

Map.prototype.isEmpty = function()
{
	if(this.keySet.length == 0)
		return true;
	else
		return false;
};

Map.prototype.put = function(_objectKey,_objectValue)
{
	this.keySet.push(_objectKey);
	this.entrySet.push(_objectValue);
};

Map.prototype.remove = function(_objectKey)
{
	alert("not support now.");
};

Map.prototype.size = function()
{
	this.keySet.length;
};