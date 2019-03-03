<%@ page contentType="text/html; charset=UTF-8" language="java" %>

/*****************内部样式重定义*****************/

/*内容样式*/
BODY {  
    FONT-SIZE: 9pt;
    COLOR: #000000;
    FONT-FAMILY: 宋体,Arial;
    background-color: #f0efef;

    margin-bottom: 0px;
    margin-left: 0px;
    margin-top: 0px;
    margin-right: 0px;

    /*滚动样式*/
    SCROLLBAR-FACE-COLOR: #babdc2;
    SCROLLBAR-HIGHLIGHT-COLOR: #d0d5d9;
    SCROLLBAR-SHADOW-COLOR: #abafb3;
    SCROLLBAR-3DLIGHT-COLOR: #abafb3;
    SCROLLBAR-ARROW-COLOR: #dbdee1;
    SCROLLBAR-TRACK-COLOR: #c8cdd3;
    SCROLLBAR-DARKSHADOW-COLOR: #c3c7cb;
    overflow:auto;
}
body, table, td {
    font-size:9pt
}


/* 链接样式 */
A {
    text-decoration: none;
}
A:link {
    text-decoration:none;color:#000000
}
A:visited {
    font-size: 9pt;
    color: #666666;
    text-decoration: none;
    font-family: "Arial";
}
A:Hover {
    text-decoration: underline;
    clip: rect();
    crosshair;Color:#99ccff;
    color:#FF9900;
}


/* 表单样式 */
FORM {
    FONT-WEIGHT: normal;
    LINE-HEIGHT: normal;
    FONT-STYLE: normal;
    FONT-FAMILY: MS Shell Dlg,tahoma;
}

/* 表单的列样式 */
TD {
    FONT-SIZE: 9pt;
    FONT-FAMILY: Verdana,Helvetica,Arial;
}


/* 输入控件样式 */
INPUT {
    font-family: "Arial";
    font-size: 9pt;
    color: #003300;
    height: 20px;
    vertical-align: middle;
    border: 0px solid #90b3cf;
}


/* 文本输入域样式 */
TEXTAREA {
    font-family: "Arial";
    font-size: 9pt;
    color: #163752;
    text-decoration: none;
    border: 1px solid #7a858f;
    -moz-transition: border 0.2s linear 0s, box-shadow 0.2s linear 0s;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1) inset;
    border-radius: 3px 3px 3px 3px;
    background-color: #f3f4f4;
    overflow:auto;
}


/* 下拉控件样式 */
SELECT {
    font-family: "Arial";
    font-size: 9pt;
    color: #003300;
    text-decoration: none;
    background-color: #f3f4f4;
    -moz-transition: border 0.2s linear 0s, box-shadow 0.2s linear 0s;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1) inset;
    border-radius: 3px 3px 3px 3px;
    vertical-align: middle;
    border: 1px solid #7a858f;
    width: 204px;
}



/* 表单基本元素的样式表 */
.text_field {
    /* 最长的text */
    font-size: 9pt;
    color: #163752;
    -moz-transition: border 0.2s linear 0s, box-shadow 0.2s linear 0s;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1) inset;
    border-radius: 3px 3px 3px 3px;
    height: 22px;
    vertical-align: middle;
    border: 1px solid #7a858f;
    width: 204px;
    background-color: #f3f4f4;
    margin-right:5px;
}

/* 表单基本元素的样式表 */
.text_field_half {
    /* 最长的text */
    font-size: 9pt;
    color: #163752;
    height: 20px;
    vertical-align: middle;
    border: 1px solid #90b3cf;
    width: 102px;
    background-color: #ffffff;
    margin-right:5px;
}

.text_field_reference {  
    /*参照左边的text，可写，长度为 (最长-参照width)*/
    font-size: 9pt;
    color: #003300;
    -moz-transition: border 0.2s linear 0s, box-shadow 0.2s linear 0s;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1) inset;
    border-radius: 3px 3px 3px 3px;
    height: 22px;
    vertical-align: middle;
    border-top: 1px solid #7a858f;
    border-right: 1px solid #7a858f;
    border-bottom: 1px solid #7a858f;
    border-left: 1px solid #7a858f;
    line-height: 13px;
    width: 184px;
    background-color: #f3f4f4;
}
.text_field_readonly {
    /*参照左边的text，只读，长度为 (最长-参照width)*/
    font-size: 9pt;
    color: #003300;
    height: 20px;
    vertical-align: middle;
    border-top: 1px solid #bacedd;
    border-right: 1px solid #bacedd;
    border-bottom: 1px solid #bacedd;
    border-left: 1px solid #bacedd;
    line-height: 13px;
    -moz-transition: border 0.2s linear 0s, box-shadow 0.2s linear 0s;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1) inset;
    border-radius: 3px 3px 3px 3px;
    background-color: #f3f4f4;
    width: 204px;
}
.text_field_reference_readonly { //参照左边的text，只读，长度为 (最长-参照width)
    font-size: 9pt;
    color: #003300;
    height: 20px;
    vertical-align: middle;
    border: 1px solid #7a858f;
    line-height: 13px;
    -moz-transition: border 0.2s linear 0s, box-shadow 0.2s linear 0s;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1) inset;
    border-radius: 3px 3px 3px 3px;
    background-color: #f3f4f4;
    width: 186px;
}
.text_field_half_reference_readonly { //参照左边的text，只读，长度为 (最长-参照width)
    font-size: 9pt;
    color: #003300;
    height: 20px;
    vertical-align: middle;
    border-top: 1px solid #bacedd;
    border-right: 1px solid #bacedd;
    border-bottom: 1px solid #bacedd;
    border-left: 1px solid #bacedd;
    line-height: 13px;
    background-color: #d6e8f4;
    width: 93px;
}
.textarea_limit_words {
    -moz-transition: border 0.2s linear 0s, box-shadow 0.2s linear 0s;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1) inset;
    border-radius: 3px 3px 3px 3px;
    background-color: #f3f4f4;
}
.textarea_fckEditor {
    WIDTH: 100%;
    HEIGHT: 600px;
}


/* Div块区域的样式表 */
.table_div_control {
    font-family: "Arial";
    font-size: 9pt;
    color: #000;
    text-decoration: none;
    margin-top: 5px;
  
   border-right: 1px solid #969eb0;
    border-top: 1px solid #969eb0;
    border-left: 1px solid #969eb0;
    width: 100%;
    border-spacing: 0px;
    padding: 0px;
    
    background-position:left top;
    background-repeat:repeat-x;
    background-image:url('../images/templatestyle/bg.jpg');
    height:33px;
   
}
.table_div_content {
    width: 100%;
    border-spacing: 0px;
    border-collapse: collapse;
    /* 折行 */
    /*table-layout:fixed;*/
    /*word-wrap: break-word;*/
    word-break: break-all;
   background-color:#c8cdd3;
   border-right: 1px solid #969eb0;
    border-bottom: 1px solid #969eb0;
    border-left: 1px solid #969eb0;
    background-position:left bottom;
    background-repeat:repeat-x;
}
.table_div_content td {
    
    height:30px;
    
}
.table_div_content2 {
    border: 1px solid #969EB0;
    border-top: 0px none;
    width: 100%;
    background-color: #C8CDD3;
    background-position:left bottom;
    background-repeat:repeat-x;
}
.table_div_content2 td {
    padding: 0px;
}
.div_control_image {
    width: 16px;
    height: 16px;
    vertical-align : middle;
    cursor: pointer;
    border: 0px;
}
.table_div_content_frame {
    border: 0px solid #bdd5e3;
    border-spacing: 0px;
    border-collapse: collapse;
    /* 折行 */
    table-layout:fixed;
    word-wrap: break-word;
    word-break: break-all;
    background-color: #f4fafe;
    background-image:url('../images/templatestyle/main0_12.jpg');
    background-position:left bottom;
    background-repeat:repeat-x;
}


/* 按钮的样式表 */
.button_ellipse {
    border-radius: 3px 3px 3px 3px;
    border:1px solid #8e8e8e;
    font-size:12px;
    color:#382c2c;
    cursor: pointer; 
    padding-left: 5px; 
    padding-right: 5px;
    padding-bottom:0px; 
    height: 20px;
    min-width:40px;
    background-image:url("../images/templatestyle/btnbg.jpg");
    background-position:left bottom;
    background-repeat:repeat-x;
    background-color:#ffffff;
    text-align:center;
}
.btn3_mouseout { 
    border:1px solid #607c95;
    font-size:12px;
    color:#382c2c;
    border-radius: 3px 3px 3px 3px;
    cursor: pointer; 
    padding-left: 5px; 
    padding-right: 5px; 
    height: 20px;
    min-width:40px;
    background-image:url("../images/templatestyle/btnbg.jpg");
    background-position:left bottom;
    background-repeat:repeat-x;
    background-color:#ffffff;
     text-align:center;
} 
.btn3_mouseover { 
 border-radius: 3px 3px 3px 3px;
    border:1px solid #9ce4fd;
    font-size:12px;
    color:#1f7497;
    cursor: pointer; 
    padding-left: 5px; 
    padding-right: 5px; 
    height: 20px;
    min-width:40px;
    background-image:url("../images/templatestyle/btnbg.jpg");
    background-position:left bottom;
    background-repeat:repeat-x;
    background-color:#ffffff;
    text-align:center;
} 
.btn3_mousedown 
{ 
border-radius: 3px 3px 3px 3px;
    border:1px solid #9ce4fd;
    font-size:12px;
    color:#1f7497;
    cursor: pointer; 
    padding-left: 5px; 
    padding-right: 5px; 
    height: 20px;
    min-width:40px;
    background-image:url("../images/templatestyle/btnbg.jpg");
    background-position:left bottom;
    background-repeat:repeat-x;
    background-color:#ffffff;
     text-align:center;
} 
.btn3_mouseup { 
border-radius: 3px 3px 3px 3px;
    border:1px solid #9ce4fd;
    font-size:12px;
    color:#1f7497;
    cursor: pointer; 
    padding-left: 5px; 
    padding-right: 5px; 
    height: 20px;
    min-width:40px;
    background-image:url("../images/templatestyle/btnbg.jpg");
    background-position:left bottom;
    background-repeat:repeat-x;
    background-color:#ffffff;
     text-align:center;
}


/* 列表的样式表 */

.listCss td{padding:0px;}

table.listCss {
    vertical-align:middle;
    border-width:0px;
    border-spacing:px;
    border-collapse:1px;
    line-height: 130%;
    background-color: #c9caca;
}
th.listCss {
    

    background-color: #089ec4;
    border-spacing:0px;
    border-collapse:0px;
    margin: 0px auto;
    padding: 0px;
    line-height: 25px;;
   
    color:#f0f0f0;
}
tr.listCss {
    /*background-color: #FFFFFF;*/
}
td.listCss {
    vertical-align:middle;
    border:1px solid #ff000;
    border-width:0px;
    border-spacing:1px;
    border-collapse:1px;
    margin: 1px auto;
    padding: 0px;
    line-height: 130%;
    height: 22px;
    text-align:center;
}

/*新增查看样式*/
.viewlistCss{
    background-color: #babbbb;
    width:100%;
}
.viewlistCss td{
    background-color: #d3d7dd;
    margin: 1px auto;
    padding: 2px 8px;
    line-height: 130%;
 }
.viewlistCss th{
    background-color: #c8cdd3;
    color:#000; 
    font-weight:normal;
    margin: 1px auto;
    padding: 2px 8px;;
    line-height: 130%;
    height: 22px;
}

.img_1 {
    width: 18px;
    height: 20px;
    vertical-align: middle;
    cursor: pointer;
    border: 0px;
    margin-left:1px;
}

/* For RM FrameWork */
.refButtonClass{
    width: 18px;
    height: 20px;
    vertical-align: middle;
    cursor: pointer;
    border: 0px;
}
/*以下为新做*/
.maintopbg01{

background-position:left top;
background-repeat:no-repeat;
}

.maintopbg02{
    background-position:right top;
    background-repeat:no-repeat;
}

.maintop_title_bg{
   background-color:#e9e9e8;
    background-position:left top;
    background-repeat:repeat-x;
}

.maintop_title_text{
    color:#000000;
    font-weight:bold;
    line-height:25px;
}
.page_text{color:#3b4349;  margin-left:10px;}
.page_text01{color:#e35406;}


/* 列表的样式表  固定表头和列*/

table.listCssgd {
    vertical-align:middle;
    border-width:0px;
    border-spacing:px;
    border-collapse:1px;
    line-height: 130%;
    background-color: #0AD7E7;
}



th.listCssgdtitle {
    vertical-align:middle;
    border:0px solid #9EB4CD;
    border-width:0px;
    border-spacing:0px;
    border-collapse:0px;
    margin: 1px auto;
    padding: 1px;
    line-height: 130%;
    background-color: #b9def7;
    color:#206ba4;
    
    POSITION: relative; 
    LEFT: expression(this.parentElement.offsetParent.scrollLeft); 
    WHITE-SPACE: nowrap;    

}

th.listCssgd {
    vertical-align:middle;
    border:1px solid #ff0000;
    border-width:0px;
    border-spacing:0px;
    border-collapse:0px;
    margin: 1px auto;
    /**padding: 1px;**/
    line-height: 130%;
    background-color: #5CA0B8;
    color:#ffffff;
    text-align:center;
}

tr.listCssgd {
    background-color: #FFFFFF;
    text-align:center;
}

td.listCssgdtitle {
    vertical-align:middle;
    border:0px solid #9EB4CD;
    border-width:0px;
    border-spacing:0px;
    border-collapse:0px;
    margin: 1px auto;
    padding: 1px;
    line-height: 130%;
    height: 22px;
    text-align:center;
    POSITION: relative; 
    LEFT: expression(this.parentElement.parentElement.parentElement.parentElement.offsetParent.parentElement.parentElement.scrollLeft);  
    WHITE-SPACE: nowrap; 
}

td.listCssgd {
    vertical-align:middle;
    border:0px solid #9EB4CD;
    border-width:0px;
    border-spacing:0px;
    border-collapse:0px;
    margin: 1px auto;
    padding: 1px;
    line-height: 130%;
    height: 22px;
     text-align:center;
}

#box {
    background-color: #f0efef;
    
    border: 1px solid #d4d3d3;   
     border: 1px solid #ffffff; 
    bottom: 1px;
    font-size: 12px;
    left: 0px;
    right: 0px;
    top: 1px;
    width:100%;
    height:100%;
}
.box2{
    background-color: #f0efef;
    
    bottom: 5px;
    font-size: 12px;
    left: 6px; 
    right: 5px;
    top: 12px;
    width:100%;
    margin:0 auto;
    height:100%;
}
h1 {
    clear:both;
    font-family: 黑体;
    font-size: 28px;
    line-height:20px;
    text-align:center;
    padding-top:8px;
    height:10px;
}
.box3{ 
 margin:0 auto; 
 text-align:center; 
 
 height:20px;
 }
#ccChild1{
clear:both;
 width:100%;
 }
 
 .treestyle{
 width:270px;
 height:500px;
 background-color:#c8cdd3;
 border:0px solid #90b3cf;
 overflow:auto;
 
 }

.bottom a:link{font-size:14px; text-decoration:none; color:#000000;}

<%--欢迎词样式--%>
.table_noFrame .welcome {
color: #000000;
}
<%--用户名样式--%>
.table_noFrame .loginName {
color: #00eaff;
}
<%--二级菜单欢迎词样式--%>
.user-frame{
background-color: #e9e9e8;
}