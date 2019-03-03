
//当前选中的页签
var CurrSelNo = 0;
//页签的数目
var MaxTabs=tabs.length;

function HovIt(objname) {
	if(CurrSelNo!=objname) {
		var btn1='tabInfotd'+objname;
		var btn2=btn1+'R';
		document.getElementById(btn1).className = 'tabInfoHovT';
		document.getElementById(btn2).className = "tabInfoHovTR";
	}
}
function DefIt(objname) {
	if(CurrSelNo!=objname) {
		var btn1='tabInfotd'+objname;
		var btn2=btn1+'R';
		document.getElementById(btn1).className = 'tabInfoDefT';
		document.getElementById(btn2).className = "tabInfoDefTR";
	}
}
function SelIt(objname,strurl) {

	var btn1;
	var btn2;
	for(i=0;i<MaxTabs;i++) {
		btn1='tabInfotd'+i;
		btn2=btn1+'R';
		document.getElementById(btn1).className = 'tabInfoDefT';
		document.getElementById(btn2).className = "tabInfoDefTR";
	}
	btn1='tabInfotd'+objname;
	btn2=btn1+'R';
	document.getElementById(btn1).className = 'tabInfoSelT';
	document.getElementById(btn2).className = "tabInfoSelTR";
	document.getElementById('tabInfo_frame').src = strurl;
	CurrSelNo = objname;
}
function setTab(objname) {
	var btn1;
	var btn2;
	for(i=0;i<MaxTabs;i++) {
		btn1='tabInfotd'+i;
		btn2=btn1+'R';
		document.getElementById(btn1).className = 'tabInfoDefT';
		document.getElementById(btn2).className = "tabInfoDefTR";
	}
	btn1='tabInfotd'+objname;
	btn2=btn1+'R';
	document.getElementById(btn1).className = 'tabInfoSelT';
	document.getElementById(btn2).className = "tabInfoSelTR";
	CurrSelNo = objname;
}

function writeTabs(frameHeight){
	var tabstr = "";
	tabstr += "<table border='0' align='center' cellspacing='0' cellpadding='0' id='igtabtabInfo' width='100%' height='100%' style='Z-INDEX:108;'  class='table001' >";
	tabstr += "<tr>";
	tabstr += "<td>";
	tabstr += "<table id='tabInfo_tbl' cellspacing='0' cellpadding='0' border='0' width='100%'>";
	tabstr += "<tr valign='center'>";
	for(var i=0;i<MaxTabs;i++){
		tabstr += "<td>";
		tabstr += "<table cellspacing='0' cellpadding='0' border='0' height='100%' width='100%'>";
		tabstr += "<tr>";
		if(i==0){
			tabstr += "<td nowrap id='tabInfotd"+ i +"' align='center' class='tabInfoSelT' height='22px'  style='padding-left:6px;' unselectable='on' onmouseover='HovIt(\""+ i +"\")' onmouseout='DefIt(\""+ i +"\")' onclick='SelIt(\""+ i +"\",\""+ tabs[i][1] +"\")'>"+ tabs[i][0] +"</td>";
			tabstr += "<td nowrap id='tabInfotd"+ i +"R' class='tabInfoSelTR' width='6px' unselectable='on' onmouseover='HovIt(\""+ i +"\")' onmouseout='DefIt(\""+ i +"\")' onclick='SelIt(\""+ i +"\",\""+ tabs[i][1] +"\")'>　</td>";
		}else{
			tabstr += "<td nowrap id='tabInfotd"+ i +"' align='center' class='tabInfoDefT' height='22px' style='padding-left:6px;' unselectable='on' onmouseover='HovIt(\""+ i +"\")' onmouseout='DefIt(\""+ i +"\")' onclick='SelIt(\""+ i +"\",\""+ tabs[i][1] +"\")'>"+ tabs[i][0] +"</td>";
			tabstr += "<td nowrap id='tabInfotd"+ i +"R' class='tabInfoDefTR' width='6px' unselectable='on' onmouseover='HovIt(\""+ i +"\")' onmouseout='DefIt(\""+ i +"\")' onclick='SelIt(\""+ i +"\",\""+ tabs[i][1] +"\")'>　</td>";
		}
		tabstr += "</tr>";
		tabstr += "</table>";
		tabstr += "</td>";
	}
	tabstr += "<td nowrap style='font-size:2px;cursor:default;border-width:0px 0px 1px 0px;border-color:#7ebaff; border-style:Solid;width:90%;'>　</td>";
	tabstr += "</tr>";
	tabstr += "</table>";
	tabstr += "</td>";
	tabstr += "</tr>";
	tabstr += "<tr>";
	tabstr += "<td id='tabInfo_cp' bgcolor='#FEFCFD' style='border-width:0px 1px 1px 1px;border-color:#7ebaff; border-style:Solid;background-color:#FEFCFD;padding:3px 0px 0px 0px;height:"+ frameHeight +"px;width:100%;' align='center'>";
	tabstr += "<iframe id='tabInfo_frame' src='"+ tabs[0][1] +"' style='' frameborder='0' width='100%' height='100%' scrolling='yes'></iframe>";
	tabstr += "<div width='100%' height='100%' id='tabInfo_empty' style='display:none'>　</div>";
	tabstr += "</td>";
	tabstr += "</tr>";
	tabstr += "</table>";
	tabsDiv.innerHTML = tabstr;
}
function writeTabs1(frameHeight,iSelect){
	var tabstr = "";
	tabstr += "<table border='0' align='center' cellspacing='0' cellpadding='0' id='igtabtabInfo' width='98%' height='100%' style='Z-INDEX:108;'>";
	tabstr += "<tr>";
	tabstr += "<td>";
	tabstr += "<table id='tabInfo_tbl' cellspacing='0' cellpadding='0' border='0' width='100%'>";
	tabstr += "<tr valign='center'>";
	for(var i=0;i<MaxTabs;i++){
		tabstr += "<td>";
		tabstr += "<table cellspacing='0' cellpadding='0' border='0' height='100%' width='100%'>";
		tabstr += "<tr>";
		if(i==iSelect){
			tabstr += "<td nowrap id='tabInfotd"+ i +"' align='center' class='tabInfoSelT' height='22px' unselectable='on' onmouseover='HovIt(\""+ i +"\")' onmouseout='DefIt(\""+ i +"\")' onclick='SelIt(\""+ i +"\",\""+ tabs[i][1] +"\")'>"+ tabs[i][0] +"</td>";
			tabstr += "<td nowrap id='tabInfotd"+ i +"R' class='tabInfoSelTR' width='6px' unselectable='on' onmouseover='HovIt(\""+ i +"\")' onmouseout='DefIt(\""+ i +"\")' onclick='SelIt(\""+ i +"\",\""+ tabs[i][1] +"\")'>　</td>";
		}else{
			tabstr += "<td nowrap id='tabInfotd"+ i +"' align='center' class='tabInfoDefT' height='22px' unselectable='on' onmouseover='HovIt(\""+ i +"\")' onmouseout='DefIt(\""+ i +"\")' onclick='SelIt(\""+ i +"\",\""+ tabs[i][1] +"\")'>"+ tabs[i][0] +"</td>";
			tabstr += "<td nowrap id='tabInfotd"+ i +"R' class='tabInfoDefTR' width='6px' unselectable='on' onmouseover='HovIt(\""+ i +"\")' onmouseout='DefIt(\""+ i +"\")' onclick='SelIt(\""+ i +"\",\""+ tabs[i][1] +"\")'>　</td>";
		}
		tabstr += "</tr>";
		tabstr += "</table>";
		tabstr += "</td>";
	}
	tabstr += "<td nowrap style='font-size:2px;cursor:default;border-width:0px 0px 1px 0px;border-color:#7ebaff; border-style:Solid;width:90%;'>　</td>";
	tabstr += "</tr>";
	tabstr += "</table>";
	tabstr += "</td>";
	tabstr += "</tr>";
	tabstr += "<tr>";
	tabstr += "<td id='tabInfo_cp' bgcolor='#FEFCFD' style='border-width:0px 1px 1px 1px;border-color:#7ebaff; border-style:Solid;background-color:#FEFCFD;padding:3px 0px 0px 0px;height:"+ frameHeight +"px;width:100%;' align='center'>";
	tabstr += "<iframe id='tabInfo_frame' src='"+ tabs[iSelect][1] +"' style='' frameborder='0' width='100%' height='100%'></iframe>";
	tabstr += "<div width='100%' height='100%' id='tabInfo_empty' style='display:none'>　</div>";
	tabstr += "</td>";
	tabstr += "</tr>";
	tabstr += "</table>";
	tabsDiv.innerHTML = tabstr;
}

