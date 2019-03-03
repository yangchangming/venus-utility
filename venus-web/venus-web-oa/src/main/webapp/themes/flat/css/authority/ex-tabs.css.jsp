.tabs-header{
	background:filter:alpha(opacity=0);
	border-bottom:0px;
	position:relative;
	overflow:hidden;
	padding:0px;
	overflow:hidden;
}
.tabs-header-plain{
	border:0px;
	background:transparent;
}
.tabs-scroller-left{
	position:absolute;
	left:0px;
	top:-1px;
	width:18px;
	height:28px!important;
	height:30px;
	font-size:1px;
	display:none;
	cursor:pointer;
	background:#E0ECFF url('<%=request.getContextPath()%>/css/jquery/i/tabs_leftarrow.png') no-repeat 1px 5px;
}
.tabs-scroller-right{
	position:absolute;
	right:0;
	top:-1px;
	width:18px;
	height:28px!important;
	height:30px;
	font-size:1px;
	display:none;
	cursor:pointer;
	background:#E0ECFF url('<%=request.getContextPath()%>/css/jquery/i/tabs_rightarrow.png') no-repeat 2px 5px;
}
.tabs-header-plain .tabs-scroller-left{
	top:2px;
	height:25px!important;
	height:27px;
}
.tabs-header-plain .tabs-scroller-right{
	top:2px;
	height:25px!important;
	height:27px;
}
.tabs-scroller-over{
	background-color:#ECF9F9;
}
.tabs-wrap{
	position:relative;
	left:0px;
	overflow:hidden;
	width:100%;
	margin:0px;
	padding:0px;
	background-color:#87a2ad;
}
.tabs-scrolling{
	margin-left:18px;
	margin-right:18px;
}
.tabs{
	list-style-type:none;
	height:37px;
	margin:0px;
	padding:0px;
	padding-left:4px;
	font-size:12px;
	width:5000px;
	border-bottom: 1px solid #597a87;
}
.tabs li{
	float:left;
	display:inline;
	margin1:0px 1px;
	margin-right:4px;
	margin-bottom:-1px;
	padding:0;
	position:relative;	
	padding-top:11px;
}
.tabs li a.tabs-inner{
	display:inline-block;
	text-decoration:none;
	color:#e0e0e0;	
	margin:0px;
	padding:0px 10px;
	height:25px;
	line-height:25px;
	text-align:center;
	white-space:nowrap;
}
.tabs li a.tabs-inner:hover{
	 color:#ffffff;
}
.tabs li.tabs-selected{
	border-top1:2px solid #8DB2E3;
}
.tabs li.tabs-selected a{
	color:#000000;
    background-color:#e0d6cc;
    border-top:1px solid #597a87;
    border-left:1px solid #597a87;
    border-right:1px solid #597a87;
    border-bottom: 1px solid #e0d6cc;
}
.tabs li.tabs-selected a:hover{
    color:#000000;
	cursor:default;
	pointer:default;
}
.tabs-with-icon{
	padding-left:20px;
}
.tabs-icon{
	position:absolute;
	width:16px;
	height:16px;
	left:10px;
	top:5px;
}
.tabs-closable{
	padding-right:8px;
}
.tabs li a.tabs-close{
	position:absolute;
	font-size:1px;
	display:block;
	padding:0px;
	width:11px;
	height:11px;
	top:13px;
	right:3px;
	opacity:0.3;
	filter:alpha(opacity=30);
	background:url('../../images/au/tabs_close.gif') no-repeat;
	border: 0 solid;
}
.tabs li a:hover.tabs-close{
	opacity:1;
	filter:alpha(opacity=100);
	cursor:hand;
	cursor:pointer;
}


.tabs-panels{
	margin:0px;
	padding:0px;
	border:0px solid #8DB2E3;
	border-top:0px;
	overflow:hidden;
}
