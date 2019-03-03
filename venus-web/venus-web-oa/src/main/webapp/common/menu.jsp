<%@page import="venus.frames.i18n.util.LocaleHolder"%>
<body>
<XML ID="xslModules" SRC="../modules.xsl"></XML>
<XML ID="xmlModules" SRC="../modules.xml"></XML>
<DIV ID="divModules"></DIV>


<script src="<%=request.getContextPath()%>/js/common/TaskMenu.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/jquery/jquery-1.6.4.min.js"></script>
<script language="JavaScript" type="text/javascript" >

	TaskMenu.setStyle("<%=request.getContextPath()%>/css/Blue/blueStyle.css"); 
	TaskMenu.setHeadMenuSpecial(true);
	
	
	var i=0;
	loadXML();
	
	function loadXML(){
		var xmlDoc 
	   jQuery.ajax({//采取同步方式加载xml文件
			  //url: "../modules-<%=LocaleHolder.getLocale().getLanguage()%>.xml",国际化
			  url:"../modules.xml",
			  async: false,
			  cache:false,
			  success:function(data){
			     xmlDoc = data;
			  }
		 }); 
	  
		xmlObj=xmlDoc.documentElement;
		nodes = xmlDoc.documentElement.childNodes;
		var groupArray=new Array();
		var taskMenuArray=new Array();
		var groupStr="";
		for (i=1,j=0;i<xmlObj.childNodes.length;i++){
		   if(xmlObj.childNodes[i].nodeType==3) continue;//firefox
		   var node = xmlObj.childNodes[i].getElementsByTagName("group")[0];
		   var text = node.text?node.text:node.textContent;
			if(groupStr.indexOf(","+text)==-1){
				groupStr+=","+text;
				groupArray[j]=text;
				taskMenuArray[j]=new TaskMenu(text);
				j++;
			}
		}
		
		var wth = screen.availWidth;
		var hth = screen.availHeight;
		var l = ( screen.availWidth - 600 ) / 2;
	    var  t = ( screen.availHeight - 400 ) / 2;
	    
		for (i=1;i<xmlObj.childNodes.length;i++){
		    if(xmlObj.childNodes[i].nodeType==3) continue;//firefox
			module=xmlObj.childNodes[i].getAttribute("module");
			var groupElement = xmlObj.childNodes[i].getElementsByTagName("group")[0];
			var nameElement = xmlObj.childNodes[i].getElementsByTagName("name")[0];
			var urlElement = xmlObj.childNodes[i].getElementsByTagName("url")[0];
			var targetElement = xmlObj.childNodes[i].getElementsByTagName("target")[0];
			group = groupElement.text?groupElement.text:groupElement.textContent;
			name = nameElement.text?nameElement.text:nameElement.textContent;
			url = urlElement.text?urlElement.text:urlElement.textContent;
			target = targetElement.text?targetElement.text:targetElement.textContent;
			
			for(j=0;j<groupArray.length;j++){
				if(groupArray[j]==group){
					if(target=="frame"){
					    taskMenuArray[j].add(new TaskMenuItem(name,"<%=request.getContextPath()%>/images/index/icon_page_next.gif",
					    "parent.parent.window.frames[1].frames[3].location.href='"+"<%=request.getContextPath()%>/"+url+"'"));
					}else if(target=="_blank"){
					    taskMenuArray[j].add(new TaskMenuItem(name,"<%=request.getContextPath()%>/images/index/icon_page_next.gif",
					   "window.open('"+"<%=request.getContextPath()%>/"+url+"','',"+"'left=0,top=0,width="+wth+",height="+ hth+",menubar=yes,status=yes,toolbar=yes,scrollbars=yes,resizable=yes')"));
					}
					break;
				}
			}
		}
		for(m=0;m<groupArray.length;m++){
			taskMenuArray[m].init();
			if(m>0)taskMenuArray[m].click();
		}
		
	}
	
</script>
</body>