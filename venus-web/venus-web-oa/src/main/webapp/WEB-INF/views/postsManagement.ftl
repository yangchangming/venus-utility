<html ng-app="postsManagementApp">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title>Login</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!-- Bootstrap -->
    <link rel="stylesheet" href="${rc.getContextPath()}/css/bootstrap3/bootstrap.min.css">
    <link rel="stylesheet" href="${rc.getContextPath()}/css/angularjs/grid/ng-grid.min.css">
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

    <script src="${rc.getContextPath()}/js/bootstrap3/ui-bootstrap-tpls-0.10.0.min.js"></script>
    <script src="${rc.getContextPath()}/js/angular/grid/ng-grid-2.0.7.min.js"></script>

    <script src="${rc.getContextPath()}/js/ewp/blocksit.min.js"></script>

    <script src="${rc.getContextPath()}/js/ewp/angular/postsManagement.js"></script>
    <script src="${rc.getContextPath()}/js/ewp/angular/postsUserInfoUpdate.js"></script>
    <script src="${rc.getContextPath()}/js/ewp/angular/postsUserInfoManagement.js"></script>
    <script src="${rc.getContextPath()}/js/ewp/angular/postsUserPostManagement.js"></script>
    <script src="${rc.getContextPath()}/js/ewp/angular/postsUserPostPersonalManagement.js"></script>

    <style>
        body {
            background-color: #ffffff;
        }
    </style>

</head>

<body>
<section id="wrapper" ng-controller="ManagementCtrl">
    <div id="head" class="container" ng-controller="ManagementHeadCtrl">
        <div class="hp">
            <div class="logo" ng-click="main_tabs[0].active = true">
                <h2><a href="${rc.getContextPath()}/apis/ewp/user/loginInterface">评论管理</a></h2>
            </div>
            <div class="personal">
                <ul class="nav" role="navigation">
                    <li class="dropdown"><a data-toggle="dropdown" class="dropdown-toggle" role="button" id="drop2"
                                            href="#">个人中心<b class="caret"></b></a>
                        <ul aria-labelledby="drop2" role="menu" class="dropdown-menu">
                            <li role="presentation">
                                <a href="#" tabindex="-1" role="menuitem" ng-click="activeUserInfo(0)">个人信息</a>
                            </li>
                            <li role="presentation">
                                <a href="#" tabindex="-1" role="menuitem" ng-click="activeUserInfo(1)">修改密码</a>
                            </li>
                            <li role="presentation">
                                <a href="#" tabindex="-1" role="menuitem" ng-click="logout()">&nbsp;&nbsp;&nbsp;&nbsp;退出&nbsp;&nbsp;&nbsp;&nbsp;</a>
                            </li>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>
    </div>
    <div class="container">
        <div class="maintop">
            <img src="${rc.getContextPath()}/images/ewp/posts/man.gif">&nbsp;当前用户： <span>{{loginInfo.name}}</span>&nbsp;&nbsp;
        </div>

        <div id="main" ng-controller="ManagementTabsCtrl">
            <tabset>
                <tab ng-show="false" active="group.active" heading="{{group.title}}" ng-repeat="group in main_tabs">
                    <ng-include
                            src="group.modelName"></ng-include>
                </tab>
            </tabset>
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
<script>
        var postsManagementApp = angular.module('postsManagementApp',
                ['ui.bootstrap',
                    'gap.ewp.posts.postsUserInfoManagementModel',
                    'gap.ewp.posts.postsManagementModule',
                    'gap.ewp.posts.postsUserInfoUpdateModel',
                    'gap.ewp.posts.postPersonalManagementModel',
                    'gap.ewp.posts.postManagementModel',
                    'gap.ewp.posts.util']);

        postsManagementApp.controller('ManagementCtrl', function ($scope, $http,webContextPathService) {

            webContextPathService.setWebContextPath("${rc.getContextPath()}");

            $scope.login = function (name, pwd) {
                $http({
                    method: 'POST',
                    url: webContextPathService.getWebContextPath()+'/apis/ewp/user/login',
                    data: {"usrName": name, "usrPWD": pwd},
                    headers: {
                        'Content-type': 'application/json'
                    }
                }).success(function (data) {
                    $scope.loginInfo = data;
                });
            }
            $scope.logout = function () {
                $http({
                    method: 'POST',
                    url: webContextPathService.getWebContextPath()+'/apis/ewp/user/logout',
                    headers: {
                        'Content-type': 'application/json'
                    }
                }).success(function (data) {
                    $scope.loginInfo = data;

                    location.href=webContextPathService.getWebContextPath()+'/apis/ewp/user/loginInterface';
                });
            }

            $scope.login(null, null);


            $scope.templeteName = "";
            $scope.main_tabs = [
                {
                    title: "评论管理",
                    modelName: "${rc.getContextPath()}/apis/ewp/user/getTempleteInfo?templeteName=postsPostManagement",
                    isAdmin: 0
                },
                {
                    title: "个人中心",
                    modelName: "${rc.getContextPath()}/apis/ewp/user/getTempleteInfo?templeteName=postsUserInfo",
                    isAdmin: 0
                }
            ];

            $scope.activeUserInfo = function (index) {
                $scope.main_tabs[1].active = true;
                $('#info>.mainleft>ul>li').eq(index).click();
            };

            $scope.changeModel = function (modelName) {
                $scope.templeteName = modelName;
            }
        });
        postsManagementApp.controller('ManagementHeadCtrl', function ($scope, $http,webContextPathService) {

        });
        postsManagementApp.controller('ManagementTabsCtrl', function ($scope, $http,webContextPathService) {

        });

    </script>

</body>
</html>