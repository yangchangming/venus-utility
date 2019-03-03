//for Alexander
function getWorkflowOrgTree(inputArray,path,deeptreePath,width,height){
	if(path == undefined) {
		path = "../../";
	}
	if(width == undefined) {
		width = 550;
	}
	if(height == undefined) {
		height = 550;
	}
	var myObject = new Object();
	var rtObj = window.showModalDialog(deeptreePath,myObject,'dialogHeight='+height+'px;dialogWidth='+width+'px;');
	toDoWriteWorkflowOrgTree(inputArray, rtObj);
}

//for Alexander
function toDoWriteWorkflowOrgTree(inputArray,rtObj){
	var textValue = inputArray[0];
	var textName = inputArray[1];
	if(rtObj != undefined && rtObj.length > 0){
		var allTextValue = "";
		var allTextName = "";
		for(var i=0; i<rtObj.length-1; i++) {
			allTextValue += rtObj[i]['returnValue'] + ";";
			allTextName += rtObj[i]['childName'] + venus.authority._1;
		}
		allTextValue += rtObj[rtObj.length-1]['returnValue'];
		allTextName += rtObj[rtObj.length-1]['childName'];
		textValue.value = allTextValue;
		textName.value = allTextName;
	} else {
		//TODO
		return;
	}
}

