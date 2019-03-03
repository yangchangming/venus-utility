<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/jsp/include/global.jsp"%>

<title><fmt:message key='venus.authority.Registration_page' bundle='${applicationAuResources}' /></title>
</head>
<script language="javascript">
<!--
function onFrmSubmit() {
	if(document.form.password.value==document.form.confirmpassword.value) {
		return true;
	}else{
		alert("<fmt:message key='venus.authority.Two_password_the_importation_is_inconsistent_' bundle='${applicationAuResources}' />");
		return false;
	}
}
function validate() {
    if(document.form.login_id.value==""){
		alert("<fmt:message key='venus.authority.Please_enter_the_login_name_' bundle='${applicationAuResources}' />");
		return<fmt:message key='venus.authority._7' bundle='${applicationAuResources}' />
	}
	var topValue=screen.width/2-200;
	var leftValue=screen.height/2;
	window.open("<venus:base/>/auUser/validate?login_id="+document.form.login_id.value,"validateResult","height=100, width=80, top="+topValue+", left="+leftValue+",toolbar=no,menubar=no,resizable=yes,location=no,status=no");
}
//密码强度;
function PasswordStrength(showed){	
	this.showed = (typeof(showed) == "boolean")?showed:true;
	this.styles = new Array();	
	this.styles[0] = {backgroundColor:"#EBEBEB",borderLeft:"solid 1px #FFFFFF",borderRight:"solid 1px #BEBEBE",borderBottom:"solid 1px #BEBEBE"};	
	this.styles[1] = {backgroundColor:"#FF4545",borderLeft:"solid 1px #FFFFFF",borderRight:"solid 1px #BB2B2B",borderBottom:"solid 1px #BB2B2B"};
	this.styles[2] = {backgroundColor:"#FFD35E",borderLeft:"solid 1px #FFFFFF",borderRight:"solid 1px #E9AE10",borderBottom:"solid 1px #E9AE10"};
	this.styles[3] = {backgroundColor:"#95EB81",borderLeft:"solid 1px #FFFFFF",borderRight:"solid 1px #3BBC1B",borderBottom:"solid 1px #3BBC1B"};
	
	this.labels= ["<fmt:message key='venus.authority.Weak' bundle='${applicationAuResources}' />","<fmt:message key='venus.authority.In' bundle='${applicationAuResources}' />","<fmt:message key='venus.authority.Strength' bundle='${applicationAuResources}' />"];

	this.divName = "pwd_div_"+Math.ceil(Math.random()*100000);
	this.minLen = 5;
	
	this.width = "150px";
	this.height = "16px";
	
	this.content = "";
	
	this.selectedIndex = 0;
	
	this.init();	
}
PasswordStrength.prototype.init = function(){
	var s = '<table cellpadding="0" id="'+this.divName+'_table" cellspacing="0" style="width:'+this.width+';height:'+this.height+';">';
	s += '<tr>';
	for(var i=0;i<3;i++){
		s += '<td id="'+this.divName+'_td_'+i+'" width="33%" align="center"><span style="font-size:1px">&nbsp;</span><span id="'+this.divName+'_label_'+i+'" style="display:none;font-family: Courier New, Courier, mono;font-size: 12px;color: #000000;">'+this.labels[i]+'</span></td>';
	}	
	s += '</tr>';
	s += '</table>';
	this.content = s;
	if(this.showed){
		document.write(s);
		this.copyToStyle(this.selectedIndex);
	}	
}
PasswordStrength.prototype.copyToObject = function(o1,o2){
	for(var i in o1){
		o2[i] = o1[i];
	}
}
PasswordStrength.prototype.copyToStyle = function(id){
	this.selectedIndex = id;
	for(var i=0;i<3;i++){
		if(i == id-1){
			this.$(this.divName+"_label_"+i).style.display = "inline";
		}else{
			this.$(this.divName+"_label_"+i).style.display = "none";
		}
	}
	for(var i=0;i<id;i++){
		this.copyToObject(this.styles[id],this.$(this.divName+"_td_"+i).style);			
	}
	for(;i<3;i++){
		this.copyToObject(this.styles[0],this.$(this.divName+"_td_"+i).style);
	}
}
PasswordStrength.prototype.$ = function(s){
	return document.getElementById(s);
}
PasswordStrength.prototype.setSize = function(w,h){
	this.width = w;
	this.height = h;
}
PasswordStrength.prototype.setMinLength = function(n){
	if(isNaN(n)){
		return ;
	}
	n = Number(n);
	if(n>1){
		this.minLength = n;
	}
}
PasswordStrength.prototype.setStyles = function(){
	if(arguments.length == 0){
		return ;
	}
	for(var i=0;i<arguments.length && i < 4;i++){
		this.styles[i] = arguments[i];
	}
	this.copyToStyle(this.selectedIndex);
}
PasswordStrength.prototype.write = function(s){
	if(this.showed){
		return ;
	}
	var n = (s == 'string') ? this.$(s) : s;
	if(typeof(n) != "object"){
		return ;
	}
	n.innerHTML = this.content;
	this.copyToStyle(this.selectedIndex);
}
PasswordStrength.prototype.update = function(s){
	if(s.length < this.minLen){
		this.copyToStyle(0);
		return;
	}
	var ls = -1;
	if (s.match(/[a-z]/ig)){
		ls++;
	}
	if (s.match(/[0-9]/ig)){
		ls++;
	}
 	if (s.match(/(.[^a-z0-9])/ig)){
		ls++;
	}
	if (s.length < 6 && ls > 0){
		ls--;
	}
	 switch(ls) { 
		 case 0:
			 this.copyToStyle(1);
			 break;
		 case 1:
			 this.copyToStyle(2);
			 break;
		 case 2:
			 this.copyToStyle(3);
			 break;
		 default:
			 this.copyToStyle(0);
	 }
}
//-->
</script>
<script language="javascript">
	writeTableTop("<fmt:message key='venus.authority.Registration_page' bundle='${applicationAuResources}' />",'<venus:base/>/themes/<venus:theme/>/');  //显示本页的页眉
</script>
<form name="form" method="post" action="<venus:base/>/auUser/register" onSubmit="return onFrmSubmit()">
	<input type="hidden" name="cmd" value="register">
	<input type="hidden" name="is_admin" value="0">
	<input type="hidden" name="enable_status" value="1">

<table align=center width="100%" height="100%" border="0">
	<tr>
		<td nowrap align=center><fmt:message key='venus.authority.Login_Name' bundle='${applicationAuResources}' /></td>
		<td><input type="text" class="text_field" style="width:140px"
			name="login_id" maxLength="50">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input name="btnValidate" type="button"
			value="<fmt:message key='venus.authority.Verify_use' bundle='${applicationAuResources}' />" class="button_ellipse" onclick="validate()"></td>
	</tr>
	<tr>

		<td align=center><fmt:message key='venus.authority.Password_strength' bundle='${applicationAuResources}' /></td>

		<td><script language="javascript">

			var ps = new PasswordStrength();

			//设置显示大小；

			ps.setSize("200","20");

			//设置最少检测字符长度；

			ps.setMinLength(5);

		</script>
		
		</td>

	</tr>

	<tr>
		<td align=center><fmt:message key='venus.authority.Password0' bundle='${applicationAuResources}' /></td>
		<td><input type="password" class="text_field" style="width:140px"
			name="password" maxLength="24" onKeyUp="ps.update(this.value);"></td>

	</tr>
	<tr>
		<td align=center><fmt:message key='venus.authority.Confirm_Password' bundle='${applicationAuResources}' /></td>
		<td><input type="password" class="text_field" style="width:140px"
			name="confirmpassword" maxLength="24"></td>

	</tr>
	<tr>
		<td align=center><fmt:message key='venus.authority.Real_Name' bundle='${applicationAuResources}' /></td>
		<td><input type="text" class="text_field" style="width:140px"
			name="name" maxLength="20"></td>

	</tr>
	<tr>
		<td align=center><fmt:message key='venus.authority.Verification_Code' bundle='${applicationAuResources}' /></td>
		<td><input type="text" class="text_field" style="width:140px"
			name="verify" maxLength="20">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img border=0 src="verify.jsp"></td>

	</tr>
	<tr>
		<td colspan=2 align=center><input name="btnSubmit" type="submit"
			value="<fmt:message key='venus.authority.Recognized' bundle='${applicationAuResources}' />" class="button_ellipse"></td>
	</tr>
</table>

<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>/');  //显示本页的页脚
</script></form>
</body>
</html>

