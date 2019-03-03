<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/global.jsp" %>
<script>


function closePage()
{
 window.close();
}


function doWhenOpenThePage()
{

   document.getElementById("bgObj").innerHTML =window.dialogArguments;
}

</script>


<body onload="doWhenOpenThePage()">
	<div id="bgObj" class="table_div_content">
	</div>

</body>
</html>
