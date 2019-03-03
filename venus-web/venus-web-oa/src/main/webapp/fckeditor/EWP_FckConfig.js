FCKConfig.AutoDetectLanguage = true ;

//自定义工具栏
FCKConfig.ToolbarSets["EWPToolbar"] = [
	['Source','Style','FontFormat','FontName','FontSize','Preview','Smiley','-'],
	"/",
	['Bold','Italic','Underline','StrikeThrough','-','Subscript','Superscript','-','FitWindow']
];

//自定义字体
FCKConfig.FontNames = 'Arial;Comic Sans MS;Courier New;Tahoma;Times New Roman;Verdana;宋体;黑体;微软雅黑' ;

//回车换行设置
FCKConfig.EnterMode = 'br' ;			// p | div | br
FCKConfig.ShiftEnterMode = 'p' ;	    // p | div | br

//自定义表情图片
FCKConfig.SmileyPath	= FCKConfig.BasePath + 'images/smiley/msn/' ;
FCKConfig.SmileyImages	= ['guoq.jpg','regular_smile.gif','sad_smile.gif','wink_smile.gif','teeth_smile.gif','confused_smile.gif','tounge_smile.gif','embaressed_smile.gif','omg_smile.gif','whatchutalkingabout_smile.gif','angry_smile.gif','angel_smile.gif','shades_smile.gif','devil_smile.gif','cry_smile.gif','lightbulb.gif','thumbs_down.gif','thumbs_up.gif','heart.gif','broken_heart.gif','kiss.gif','envelope.gif'] ;
FCKConfig.SmileyColumns = 8 ;
FCKConfig.SmileyWindowWidth		= 320 ;
FCKConfig.SmileyWindowHeight	= 210 ;


FCKConfig.ImageUploadAllowedExtensions  = ".(jpg|gif|jpeg|png|bmp)$" ;      // empty for all
FCKConfig.FlashUploadAllowedExtensions  = ".(swf|flv)$" ;       // empty for all
FCKConfig.LinkUploadAllowedExtensions   = ".(7z|aiff|asf|avi|bmp|csv|doc|fla|flv|gif|gz|gzip|jpeg|jpg|mid|mov|mp3|mp4|mpc|mpeg|mpg|ods|odt|pdf|png|ppt|pxd|qt|ram|rar|rm|rmi|rmvb|rtf|sdc|sitd|swf|sxc|sxw|tar|tgz|tif|tiff|txt|vsd|wav|wma|wmv|xls|xml|zip)$" ;            // empty for all
