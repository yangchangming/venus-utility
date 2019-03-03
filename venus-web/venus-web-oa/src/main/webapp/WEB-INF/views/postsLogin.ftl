<!DOCTYPE html>
<html ng-app="loginManagementApp">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title>Login</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!-- Bootstrap -->
    <link rel="stylesheet" href="${rc.getContextPath()}/css/bootstrap3/bootstrap.min.css">
    <link rel='stylesheet' href="${rc.getContextPath()}/css/ewp/posts/style.css" media='screen'/>

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="${rc.getContextPath()}/js/bootstrap3/ie/html5shiv.min.js"></script>
    <script src="${rc.getContextPath()}/js/bootstrap3/ie/respond.min.js"></script>
    <![endif]-->

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="${rc.getContextPath()}/js/bootstrap3/jquery1.10/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="${rc.getContextPath()}/js/bootstrap3/bootstrap.min.js"></script>

    <!-- Angularjs -->
    <script src="${rc.getContextPath()}/js/angular/angular.js"></script>
    <script src="${rc.getContextPath()}/js/ewp/angular/postsManagement.js"></script>

    <script src="${rc.getContextPath()}/js/bootstrap3/ui-bootstrap-tpls-0.10.0.min.js"></script>
    <script type="text/javascript">
        function changePage(pageId) {
            $('#main>div').hide();
            $('#' + pageId).show();
        }

        $(function(){changePage('loginPage');});
    </script>

</head>
<body>
<section id="wrapper">
    <hgroup>
        <div id="head">
            <div class="hp">
                <div class="logo">
                    <h2><a href="${rc.getContextPath()}/apis/ewp/user/loginInterface">评论管理</a></h2>
                </div>
                <div class="personal" >
                    <ul role="navigation" class="nav">
                        <li class="dropdown">
                            <span><a href="#">网站首页</a></span>&nbsp;&nbsp;
                        </li>
                    </ul>
                </div>
            </div>

        </div>
    </hgroup>

    <div id="main">
        <div id="loginPage">
            <div class="password clearfix">
                <div class="logintitle">
                    <img src="${rc.getContextPath()}/images/ewp/posts/lock.gif"> &nbsp;<span>登录</span>
                </div>
                <div id="login">
                    <form class="form-horizontal" action="${rc.getContextPath()}/apis/ewp/user/loginInterface" method="post">
                        <div class="form-group">
                            <label for="usrName" class="col-sm-3 control-label"></label>
                            <div class="col-sm-3">
                                <#if result?? && result.loginError?? && result.loginError == '1'>
                                    <span style="color: red;">用户名或密码错误</span>
                                </#if>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="usrName" class="col-sm-3 control-label">用户名：</label>
                            <div class="col-sm-3">
                                <input type="text" placeholder="" class="form-control" id="usrName" name="usrName">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="usrPWD" class="col-sm-3 control-label">密&nbsp;码：</label>
                            <div class="col-sm-3">
                                <input type="password" class="form-control" placeholder="" id="usrPWD" name="usrPWD">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-3 control-label"></label>
                            <div class="col-sm-3">
                                <button class="btn post_btn" type="submit">登录</button> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:void(0);" onclick="changePage('registerPage')">新用户注册</a>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <div id="registerPage">
            <div class="password clearfix">
                <div class="logintitle">
                    <img src="${rc.getContextPath()}/images/ewp/posts/login.gif">&nbsp;<span>注册</span>
                </div>
                <div id="login">
                    <form class="form-horizontal" name="regForm" action="${rc.getContextPath()}/apis/ewp/user/registerInterface.html" method="post" novalidate>

                        <div class="form-group">
                            <label for="regName" class="col-sm-3 control-label"><span>*</span>&nbsp;&nbsp;用户名：</label>
                            <div class="col-sm-3">
                                <input type="text" class="form-control" ng-model="regname" placeholder="" id="regName" name="regName" name-unique/>
                            </div>
                            <div class="col-sm-3 control-describe">
                                <span ng-show="regForm.regName.$error.nameUnique" style="color: red;">该用户名已存在</span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="usrGender" class="col-sm-3 control-label">性别：</label>
                            <div class="col-sm-3">
                                <select class="form-control" id="usrGender" name="usrGender">
                                    <option value="1">男</option>
                                    <option value="0">女</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="usrEmail" class="col-sm-3 control-label"><span>*</span>&nbsp;&nbsp;邮&nbsp;箱：</label>
                            <div class="col-sm-3">
                                <input type="email" class="form-control" placeholder="" ng-model="usremail" id="usrEmail" name="usrEmail" required>
                            </div>
                            <div class="col-sm-3 control-describe">
                                <span ng-show="regForm.usrEmail.$error.email" style="color: red;">请输入正确的邮箱地址</span>
                            </div>
                        </div>
                        <input type="hidden" ng-model="isadmin" id="isadmin" name="isadmin" value="false">
                        <div class="form-group">
                            <label for="regPWD" class="col-sm-3 control-label"><span>*</span>&nbsp;&nbsp;密&nbsp;码：</label>
                            <div class="col-sm-3">
                                <input type="password" class="form-control" ng-model="regpwd" placeholder="" id="regPWD" name="regPWD" required>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="repPWD" class="col-sm-3 control-label"><span>*</span>&nbsp;&nbsp;确认密码：</label>
                            <div class="col-sm-3">
                                <input type="password" class="form-control" ng-model="reppwd" placeholder="" id="repPWD" name="repPWD" value-match="regpwd" required>
                            </div>
                            <div class="col-sm-3 control-describe">
                                <span ng-show="regForm.repPWD.$error.valueMatch" style="color: red;">两次密码不一致</span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label"></label>
                            <div class="col-sm-3">
                                <button class="btn post_btn" type="submit" ng-disabled="regForm.$invalid">注册</button> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:void(0);" onclick="changePage('loginPage')">已有账号</a>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <footer>
        <div class="foot">
            <div class="footnav ">
                <ul>
                    <li><a href="#">关于我们</a></li>
                    <li>|</li>
                    <li><a href="#">联系电话</a></li>
                    <li>|</li>
                    <li><a href="#">联系邮箱</a></li>
                </ul>
            </div>
            <div class="copyright">北京瑞友科技股份有限公司</div>
    </footer>
</section>
<script type="text/javascript">
    var loginManagementApp = angular.module('loginManagementApp',
            ['ui.bootstrap', 'gap.ewp.posts.postsManagementModule', 'gap.ewp.posts.util']);


</script>
</body>
</html>