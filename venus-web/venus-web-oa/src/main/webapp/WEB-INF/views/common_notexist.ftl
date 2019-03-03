<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
</head>

<script>
    function getContextPath(){
        var contextPath = document.location.pathname;
        var index =contextPath.substr(1).indexOf("/");
        contextPath = contextPath.substr(0,index+1);
        return contextPath;
    }
</script>

<body style="background-color:#f5f5f5;">
<div style="width:560px; height:200px; background-color:#f5f5f5; margin:100px auto;">
<div style="width:320px; height:160px; float:left; padding:20px;">
<h4 style=" font-family:'微软雅黑', '宋体', Arial; font-size:18px;">非常抱歉 !  您访问的页面不存在 . . .</h4>
<p style="font-size:12px; color:#7f7f7f;">请您检查确认输入的地址是否正确<br /><br />
点击以下链接继续浏览网页<br /><br />>>&nbsp;&nbsp;<a href="javascript:history.go(-1);" onclick="returnBack();">返回上一页</a><br />>>&nbsp;&nbsp;<a id="frontpage" href="#">进入首页</a></p>
</div>
<div style="width:200px; height:200px; float:right;">
    <img id="error" src="" /></div>
</div>
<script>
    document.getElementById("error").src = getContextPath() + "/images/itari/Error.png";
    document.getElementById("frontpage").href = getContextPath();
</script>
</body>
</html>