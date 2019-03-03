<#macro gap_ewp_document_posts websiteCode postDocID >
<div style="width: 100%">

<div class="center-block comment-body" ng-controller="PostListCtrl">
    <!-- Modal post-->
    <div>
        <div class="comment_form">
            <h4>文章评论</h4>
            <textarea id="comment_input" class="comment_area" ng-model="post.content"></textarea>
            <div class="post-comment">
                <div class="np-user" style="float:left;display: inline-block">
                    <a href="${rc.getContextPath()}/apis/ewp/user/loginInterface" target="_blank" ng-show="loginInfo.loginState != '0'">{{loginInfo.name}}</a><span ng-show="loginInfo.loginState == '0'">登录后参与评论</span>
                </div>
                <!-- Modal login -->
                <div>
                    <script type="text/ng-template" id="myModalLogin.html">
                        <div class="modal-header">
                            <h3 class="modal-title">登录</h3>
                        </div>
                        <div class="modal-body">
                            <div id="login" ng-show="isLogin">
                                <form class="form-horizontal modal-form">
                                    <div class="form-group">
                                        <label for="usrName" class="col-sm-4 control-label">用户名</label>
                                        <div class="col-sm-5">
                                            <input type="text" placeholder="" class="form-control" ng-model="postLogin.name"  ng-keypress="($event.which === 13)?log():0">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="usrPWD" class="col-sm-4 control-label">密&nbsp;&nbsp;码</label>
                                        <div class="col-sm-5">
                                            <input type="password" class="form-control" placeholder="" ng-model="postLogin.pwd" ng-keypress="($event.which === 13)?log():0">
                                        </div>
                                    </div>
                                </form>
                            </div>
                            <div id="register" ng-show="isRegister">
                                <form class="form-horizontal" name="regForm" novalidate>
                                    <div class="form-group">
                                        <label for="regName" class="col-sm-4 control-label"><span>*</span>&nbsp;&nbsp;用户名</label>
                                        <div class="col-sm-5">
                                            <input type="text" class="form-control" ng-model="postRegister.regName" placeholder="" id="regName" name="regName" name-unique/>
                                        </div>
                                        <div class="col-sm-4 control-describe">
                                            <span ng-show="regForm.regName.$error.nameUnique" style="color: red;">该用户名已存在</span>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="usrGender" class="col-sm-4 control-label">性别</label>
                                        <div class="col-sm-5">
                                            <select class="form-control" ng-model="postRegister.usrGender" id="usrGender" name="usrGender">
                                                <option value="1">男</option>
                                                <option value="0">女</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="usrEmail" class="col-sm-4 control-label"><span>*</span>&nbsp;&nbsp;邮&nbsp;箱</label>
                                        <div class="col-sm-5">
                                            <input type="email" class="form-control" placeholder="" ng-model="postRegister.usrEmail" id="usrEmail" name="usrEmail" required>
                                        </div>
                                        <div class="col-sm-4 control-describe">
                                            <span ng-show="regForm.usrEmail.$error.email" style="color: red;">请输入正确的邮箱地址</span>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="regPWD" class="col-sm-4 control-label"><span>*</span>&nbsp;&nbsp;密&nbsp;码</label>
                                        <div class="col-sm-5">
                                            <input type="password" class="form-control" ng-model="postRegister.regPWD" placeholder="" id="regPWD" name="regPWD" required>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="repPWD" class="col-sm-4 control-label"><span>*</span>&nbsp;&nbsp;确认密码</label>
                                        <div class="col-sm-5">
                                            <input type="password" class="form-control" ng-model="postRegister.repPWD" placeholder="" id="repPWD" name="repPWD" value-match="regPWD" required>
                                        </div>
                                        <div class="col-sm-4 control-describe">
                                            <span ng-show="regForm.repPWD.$error.valueMatch" style="color: red;">两次密码不一致</span>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <span style="float:left;" ng-show="isLogin">还没账号？<a href="javascript:void(0)" ng-click="isLogin=false;isRegister=true;" >注册</a></span>
                            <span style="float:left;" ng-show="isRegister">已有账号？<a href="javascript:void(0)" ng-click="isLogin=true;isRegister=false;" >登录</a></span>
                            <span ng-show="loginError" class="col-sm-5" style="font-size: 14px;color: red;">用户名或密码错误</span>
                            <button class="btn btn-default btn-primary" ng-show="isRegister" ng-click="reg()">注册</button>
                            <button class="btn btn-default btn-primary" ng-show="isLogin" ng-click="log()">登录</button>
                            <button class="btn btn-default btn-warning" ng-click="cancel()">取消</button>
                        </div>
                    </script>
                </div>
                <a href="javascript:void(0)" class="np-btn" ng-show="loginInfo.loginState == '0'" ng-click="open()">登录</a>
                <a href="javascript:void(0)" class="np-btn" ng-show="loginInfo.loginState != '0'" ng-click="postCommit()">发表评论</a>
                <div style="clear:both"></div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="row" style="width:100%">
            <div class="review" ng-repeat="post in posts.results" >
                <div class="reply-user">{{post.usrName}}<span ng-show="post.isreply == '1'">回复</span>{{post.replytoUserName}}</div>
                <div class="reply-time"><dataformat date=post.pubdate></dataformat></div>
                <p ng-bind-html="post.htmlContent"></p>

                <div class="reply">
                    <a href="javascript:void(0)" ng-click="reply($index)">回复</a>
                    <a href="javascript:void(0)" ng-show="loginInfo.loginState != '0' && post.usrId == loginInfo.id" confirmed-click="deletePost($index)" ng-confirm-click="确认删除?">删除</a>
                </div>
                <div id="reply{{$index}}" class="zm-comment-form zm-comment-box-ft expanded" style="display: none">
                <#--<div contenteditable="true" id="edit{{$index}}" class="zm-comment-editable editable" aria-label="写下你的评论…"></div>-->
                    <textarea id="reply_input{{$index}}" class="reply_area" reply-repeat></textarea>
                    <div class="zm-command">
                        <a class="zm-command-cancel" name="closeform" ng-click="reply($index)">取消</a>
                        <a class="zg-right zg-btn-blue" name="addnew" ng-click="replyCommit($index)">回复</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="list" ng-show="!isLastPage" >
        <a href="javascript:void(0)" ng-click="getPosts(posts.currentPage+1)">显示更多评论</a>
    </div>
</div>
<script src="${rc.getContextPath()}/js/ewp/angular/postsManagement.js"></script>
<script language="JavaScript">
$('#comment_input').flexText();

var postsApp1 = angular.module('postsApp', ['ui.bootstrap','ngSanitize','gap.ewp.posts.postsManagementModule','gap.ewp.posts.util'])
        .directive('dataformat', function (dateFilter) {
            return {
                restrict: 'E',
                replace: true,
                scope: {
                    date: '='
                },
                link: function (scope, element, attrs) {
                    scope.$watch('date', function (newval) {
                        if (scope.date) {
                            element.text(dateFilter(new Date(scope.date), 'yyyy-M-d HH:mm:ss'));
                        }
                    });
                }
            }
        })
        .directive('replyRepeat', function() {
            return function(scope, element, attrs) {
                angular.element(element).flexText();
            };
        });
postsApp1.controller('PostListCtrl', function ($scope, $http,$modal, $log, webContextPathService) {
    webContextPathService.setWebContextPath("${rc.getContextPath()}");

    $scope.checkLogin = function() {
        if($scope.loginInfo.loginState == '0') {
            $scope.open();
            return false;
        }
        return true;
    };

    $scope.getPosts = function (page) {
        if (page == 0)page = 1;
        var cprsInit = {'conditions': {'docId': '${postDocID}'}, 'results': null,
            'totalCount': 0, 'pageSize': 5, 'currentPage': page, 'orderBy': 'pubdate desc'};
        var cprs = cprsInit;

        $http({
            method: 'POST',
            url: webContextPathService.getWebContextPath()+'/apis/ewp/posts/getPosts',
            data: cprs,
            headers: {
                'Content-type': 'application/json'
            }
        }).success(function (data) {
            if($scope.posts == undefined) {
                $scope.posts = data;
            } else {
                var results = $scope.posts.results;
                $scope.posts = data;
                $scope.posts.results = results.concat(data.results);
            }

            $scope.checkLastPage();
        });
    };

    $scope.checkLastPage = function (){
        var totalPage = Math.ceil($scope.posts.totalCount/$scope.posts.pageSize);
        if (totalPage != 0 && totalPage != $scope.posts.currentPage) {
            $scope.isLastPage = false;
        } else {
            $scope.isLastPage = true;
        }
    };

    $scope.selectPage = function (page) {
        $scope.getPosts(page);
    };

    $scope.login = function (name, pwd, callback) {
        $http({
            method: 'POST',
            url: webContextPathService.getWebContextPath()+'/apis/ewp/user/login',
            data: {"usrName": name, "usrPWD": pwd},
            headers: {
                'Content-type': 'application/json'
            }
        }).success(function (data) {
            if(callback != undefined && callback != null) {
                callback(data);
            }
            $scope.loginInfo = data;
        });
    };

    $scope.register = function (regInfo) {
        $http({
            method: 'POST',
            url: webContextPathService.getWebContextPath()+'/apis/ewp/user/userRegister',
            data: regInfo,
            headers: {
                'Content-type': 'application/json'
            }
        }).success(function (data) {
            $scope.loginInfo = data;
        });
    };

    $scope.postCommit = function() {
        if($scope.post.content == "") {
            alert("您还未填写评论哦！");
            return;
        }

        $http({
            method: 'POST',
            url: webContextPathService.getWebContextPath()+'/apis/ewp/posts/addPost/${websiteCode}/${postDocID}',
            data: $scope.post.content,
            headers: {
                'Content-type': 'text/plain;charset=UTF-8'
            }
        }).success(function (data) {
            $scope.post.content="";
            $scope.posts.results.splice(0,0,data);
            $scope.posts.totalCount += 1;

            $scope.checkLastPage();
        });
    };

    $scope.reply = function (postIndex){
        var replyDiv = $("#reply" + postIndex);
        if (replyDiv.css("display") == "none") {
            replyDiv.show();
        }
        else {
            replyDiv.hide();
        }
    };

    $scope.replyCommit = function (postIndex){
        if($scope.checkLogin()) {
            var replyContent = $("#reply_input" + postIndex).val();
            $http({
                method: 'POST',
                url: webContextPathService.getWebContextPath()+'/apis/ewp/posts/replyPost/'+$scope.posts.results[postIndex].id,
                data: {siteCode:"${websiteCode}",docId:"${postDocID}",toUser:$scope.posts.results[postIndex].usrName,content:replyContent}
            }).success(function (data) {
                $("#reply_input" + postIndex).val("");
                $scope.reply(postIndex);
                $scope.posts.results.splice(0,0,data);
                $scope.posts.totalCount += 1;

                $scope.checkLastPage();
            });
        }
    };

    $scope.deletePost = function (postIndex){
        $http({
            method: 'POST',
            url: webContextPathService.getWebContextPath()+'/apis/ewp/posts/deletePosts',
            data: [$scope.posts.results[postIndex].id]
        }).success(function (data) {
            if (data.state == '0') {
                alert("对不起，删除失败");
            } else {
                $scope.posts.results.splice(postIndex,1);
                alert("评论删除成功");
            }
        });
    };

    $scope.open = function () {
        var modalInstancelogin = $modal.open({
            templateUrl: 'myModalLogin.html',
            controller: ModalInstanceLoginCtrl,
            resolve: {
                postLogin: function () {
                    return {name: "", pwd: "", loginInfo: $scope.loginInfo, login: $scope.login};
                },
                postRegister: function () {
                    return {regName:'',regGender:'',regEmail:'',regPwd:'',repPwd:''};
                }
            }
        });

        modalInstancelogin.result.then(function (result) {
            if (!result.isLogin) {
                $scope.register(result.data);
            }
        }, function () {});
    };

    $scope.post = {content:""};
    $scope.login(null, null);
    $scope.getPosts(0);
});

var ModalInstanceLoginCtrl = function ($scope, $modalInstance, postLogin, postRegister) {
    $scope.isLogin = true;
    $scope.loginError = false;
    $scope.isRegister = false;
    $scope.postLogin = postLogin;
    $scope.postRegister = postRegister;

    $scope.log = function () {
        postLogin.login($scope.postLogin.name, $scope.postLogin.pwd, function(data){
            if(data.loginError == '0') {
                $modalInstance.close({data:$scope.postLogin, isLogin:true});
            } else {
                $scope.loginError = true;
            }
        });
    };

    $scope.reg = function () {
        $modalInstance.close({data:$scope.postRegister, isLogin:false});
    };

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };
};

</script>
</div>

</#macro>



<#macro gap_ewp_document_posts_body_itari websiteCode postDocID >
<div style="width: 100%">

<div class="center-block comment-body">
    <!-- Modal post-->
    <div>
        <div class="comment_form">
            <h4>文章评论</h4>
            <textarea id="comment_input" class="comment_area" ng-model="post.content"></textarea>
            <div class="post-comment">
                <div class="np-user" style="float:left;display: inline-block">
                    <a href="${rc.getContextPath()}/apis/ewp/user/loginInterface" target="_blank" ng-show="loginInfo.loginState != '0'">{{loginInfo.name}}</a><span ng-show="loginInfo.loginState == '0'">登录后参与评论</span>
                </div>
                <a href="javascript:void(0)" class="np-btn" ng-show="loginInfo.loginState == '0'" ng-click="open()">登录</a>
                <a href="javascript:void(0)" class="np-btn" ng-show="loginInfo.loginState != '0'" ng-click="postCommit()">发表评论</a>
                <div style="clear:both"></div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="row" style="width:100%">
            <div class="review" ng-repeat="post in posts.results" >
                <div class="reply-user">{{post.usrName}}<span ng-show="post.isreply == '1'">回复</span>{{post.replytoUserName}}</div>
                <div class="reply-time"><dataformat date=post.pubdate></dataformat></div>
                <p ng-bind-html="post.htmlContent"></p>

                <div class="reply">
                    <a href="javascript:void(0)" ng-click="reply($index)">回复</a>
                    <a href="javascript:void(0)" ng-show="loginInfo.loginState != '0' && post.usrId == loginInfo.id" confirmed-click="deletePost($index)" ng-confirm-click="确认删除?">删除</a>
                </div>
                <div id="reply{{$index}}" class="zm-comment-form zm-comment-box-ft expanded" style="display: none">
                <#--<div contenteditable="true" id="edit{{$index}}" class="zm-comment-editable editable" aria-label="写下你的评论…"></div>-->
                    <textarea id="reply_input{{$index}}" class="reply_area" reply-repeat></textarea>
                    <div class="zm-command">
                        <a class="zm-command-cancel" name="closeform" ng-click="reply($index)">取消</a>
                        <a class="zg-right zg-btn-blue" name="addnew" ng-click="replyCommit($index)">回复</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="list" ng-show="!isLastPage" >
        <a href="javascript:void(0)" ng-click="getPosts(posts.currentPage+1)">显示更多评论</a>
    </div>
</div>
</div>
</#macro>
<#macro gap_ewp_document_posts_init_itari websiteCode postDocID >
<!-- Modal login -->
<div>
    <script type="text/ng-template" id="myModalLogin.html">
        <div class="modal-header">
            <h3 class="modal-title">登录</h3>
        </div>
        <div class="modal-body">
            <div id="login" ng-show="isLogin">
                <form class="form-horizontal modal-form">
                    <div class="form-group">
                        <label for="usrName" class="col-sm-4 control-label">用户名</label>
                        <div class="col-sm-5">
                            <input type="text" placeholder="" class="form-control" ng-model="postLogin.name"  ng-keypress="($event.which === 13)?log():0">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="usrPWD" class="col-sm-4 control-label">密&nbsp;&nbsp;码</label>
                        <div class="col-sm-5">
                            <input type="password" class="form-control" placeholder="" ng-model="postLogin.pwd" ng-keypress="($event.which === 13)?log():0">
                        </div>
                    </div>
                </form>
            </div>
            <div id="register" ng-show="isRegister">
                <form class="form-horizontal" name="regForm" novalidate>
                    <div class="form-group">
                        <label for="regName" class="col-sm-4 control-label"><span>*</span>&nbsp;&nbsp;用户名</label>
                        <div class="col-sm-5">
                            <input type="text" class="form-control" ng-model="postRegister.regName" placeholder="" id="regName" name="regName" name-unique/>
                        </div>
                        <div class="col-sm-4 control-describe">
                            <span ng-show="regForm.regName.$error.nameUnique" style="color: red;">该用户名已存在</span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="usrGender" class="col-sm-4 control-label">性别</label>
                        <div class="col-sm-5">
                            <select class="form-control" ng-model="postRegister.usrGender" id="usrGender" name="usrGender">
                                <option value="1">男</option>
                                <option value="0">女</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="usrEmail" class="col-sm-4 control-label"><span>*</span>&nbsp;&nbsp;邮&nbsp;箱</label>
                        <div class="col-sm-5">
                            <input type="email" class="form-control" placeholder="" ng-model="postRegister.usrEmail" id="usrEmail" name="usrEmail" required>
                        </div>
                        <div class="col-sm-4 control-describe">
                            <span ng-show="regForm.usrEmail.$error.email" style="color: red;">请输入正确的邮箱地址</span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="regPWD" class="col-sm-4 control-label"><span>*</span>&nbsp;&nbsp;密&nbsp;码</label>
                        <div class="col-sm-5">
                            <input type="password" class="form-control" ng-model="postRegister.regPWD" placeholder="" id="regPWD" name="regPWD" required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="repPWD" class="col-sm-4 control-label"><span>*</span>&nbsp;&nbsp;确认密码</label>
                        <div class="col-sm-5">
                            <input type="password" class="form-control" ng-model="postRegister.repPWD" placeholder="" id="repPWD" name="repPWD" value-match="postRegister.regPWD" required>
                        </div>
                        <div class="col-sm-4 control-describe">
                            <span ng-show="regForm.repPWD.$error.valueMatch" style="color: red;">两次密码不一致</span>
                        </div>
                    </div>
                </form>
            </div>
        </div>
        <div class="modal-footer">
            <span style="float:left;" ng-show="isLogin">还没账号？<a href="javascript:void(0)" ng-click="isLogin=false;isRegister=true;" >注册</a></span>
            <span style="float:left;" ng-show="isRegister">已有账号？<a href="javascript:void(0)" ng-click="isLogin=true;isRegister=false;" >登录</a></span>
            <span ng-show="loginError" class="col-sm-5" style="font-size: 14px;color: red;">用户名或密码错误</span>
            <button class="btn btn-default btn-primary" ng-show="isRegister" ng-click="reg()" ng-disabled="regForm.$invalid">注册</button>
            <button class="btn btn-default btn-primary" ng-show="isLogin" ng-click="log()">登录</button>
            <button class="btn btn-default btn-warning" ng-click="cancel()">取消</button>
        </div>
    </script>
</div>
<script src="${rc.getContextPath()}/js/ewp/angular/postsManagement.js"></script>
<script language="JavaScript">
$('#comment_input').flexText();

var postsApp1 = angular.module('postsApp', ['ui.bootstrap','ngSanitize','gap.ewp.posts.postsManagementModule','gap.ewp.posts.util'])
        .directive('dataformat', function (dateFilter) {
            return {
                restrict: 'E',
                replace: true,
                scope: {
                    date: '='
                },
                link: function (scope, element, attrs) {
                    scope.$watch('date', function (newval) {
                        if (scope.date) {
                            element.text(dateFilter(new Date(scope.date), 'yyyy-M-d HH:mm:ss'));
                        }
                    });
                }
            }
        })
        .directive('replyRepeat', function() {
            return function(scope, element, attrs) {
                angular.element(element).flexText();
            };
        });
postsApp1.controller('PostListCtrl', function ($scope, $http,$modal, $log, webContextPathService) {

    webContextPathService.setWebContextPath("${rc.getContextPath()}");

    $scope.checkLogin = function() {
        if($scope.loginInfo.loginState == '0') {
            $scope.open();
            return false;
        }
        return true;
    };

    $scope.getPosts = function (page) {
        if (page == 0)page = 1;
        var cprsInit = {'conditions': {'docId': '${postDocID}'}, 'results': null,
            'totalCount': 0, 'pageSize': 5, 'currentPage': page, 'orderBy': 'pubdate desc'};
        var cprs = cprsInit;

        $http({
            method: 'POST',
            url: webContextPathService.getWebContextPath()+'/apis/ewp/posts/getPosts',
            data: cprs,
            headers: {
                'Content-type': 'application/json'
            }
        }).success(function (data) {
            if($scope.posts == undefined) {
                $scope.posts = data;
            } else {
                var results = $scope.posts.results;
                $scope.posts = data;
                $scope.posts.results = results.concat(data.results);
            }

            $scope.checkLastPage();
        });
    };

    $scope.checkLastPage = function (){
        var totalPage = Math.ceil($scope.posts.totalCount/$scope.posts.pageSize);
        if (totalPage != 0 && totalPage != $scope.posts.currentPage) {
            $scope.isLastPage = false;
        } else {
            $scope.isLastPage = true;
        }
    };

    $scope.selectPage = function (page) {
        $scope.getPosts(page);
    };

    $scope.login = function (name, pwd, callback) {
        $http({
            method: 'POST',
            url: webContextPathService.getWebContextPath()+'/apis/ewp/user/login',
            data: {"usrName": name, "usrPWD": pwd},
            headers: {
                'Content-type': 'application/json'
            }
        }).success(function (data) {
            if(callback != undefined && callback != null) {
                callback(data);
            }
            $scope.loginInfo = data;
        });
    };

    $scope.register = function (regInfo) {
        $http({
            method: 'POST',
            url: webContextPathService.getWebContextPath()+'/apis/ewp/user/userRegister',
            data: regInfo,
            headers: {
                'Content-type': 'application/json'
            }
        }).success(function (data) {
            $scope.loginInfo = data;
        });
    };

    $scope.postCommit = function() {
        if($scope.post.content == "") {
            alert("您还未填写评论哦！");
            return;
        }

        $http({
            method: 'POST',
            url: webContextPathService.getWebContextPath()+'/apis/ewp/posts/addPost${websiteCode}/${postDocID}',
            data: $scope.post.content,
            headers: {
                'Content-type': 'text/plain;charset=UTF-8'
            }
        }).success(function (data) {
            $scope.post.content="";
            $scope.posts.results.splice(0,0,data);
            $scope.posts.totalCount += 1;

            //$scope.post.content清空后，未能及时反映到模板上，所以人工清理提交框内容，并重新更改大小。
            $('#comment_input').val('');
            $('#comment_input').trigger('change');
            $scope.checkLastPage();
        });
    };

    $scope.reply = function (postIndex){
        var replyDiv = $("#reply" + postIndex);
        if (replyDiv.css("display") == "none") {
            replyDiv.show();
        }
        else {
            replyDiv.hide();
        }
    };

    $scope.replyCommit = function (postIndex){
        if($scope.checkLogin()) {
            var replyContent = $("#reply_input" + postIndex).val();
            $http({
                method: 'POST',
                url: webContextPathService.getWebContextPath()+'/apis/ewp/posts/replyPost/'+$scope.posts.results[postIndex].id,
                data: {siteCode:"${websiteCode}",docId:"${postDocID}",toUser:$scope.posts.results[postIndex].usrName,content:replyContent}
            }).success(function (data) {
                $("#reply_input" + postIndex).val("");
                $scope.reply(postIndex);
                $scope.posts.results.splice(0,0,data);
                $scope.posts.totalCount += 1;
                //$scope.post.content清空后，未能及时反映到模板上，所以人工清理提交框内容，并重新更改大小。
                $('#comment_input').val('');
                $('#comment_input').trigger('change');
                $scope.checkLastPage();
            });
        }
    };

    $scope.deletePost = function (postIndex){
        $http({
            method: 'POST',
            url: webContextPathService.getWebContextPath()+'/apis/ewp/posts/deletePosts',
            data: [$scope.posts.results[postIndex].id]
        }).success(function (data) {
            if (data.state == '0') {
                alert("对不起，删除失败");
            } else {
                $scope.posts.results.splice(postIndex,1);
                alert("评论删除成功");
            }
        });
    };

    $scope.open = function () {
        var modalInstancelogin = $modal.open({
            templateUrl: 'myModalLogin.html',
            controller: ModalInstanceLoginCtrl,
            resolve: {
                postLogin: function () {
                    return {name: "", pwd: "", loginInfo: $scope.loginInfo, login: $scope.login};
                },
                postRegister: function () {
                    return {regName:'',regGender:'',regEmail:'',regPwd:'',repPwd:''};
                }
            }
        });

        modalInstancelogin.result.then(function (result) {
            if (!result.isLogin) {
                $scope.register(result.data);
            }
        }, function () {});
    };

    $scope.post = {content:""};
    $scope.login(null, null);
    $scope.getPosts(0);
});

var ModalInstanceLoginCtrl = function ($scope, $modalInstance, postLogin, postRegister) {
    $scope.isLogin = true;
    $scope.loginError = false;
    $scope.isRegister = false;
    $scope.postLogin = postLogin;
    $scope.postRegister = postRegister;

    $scope.log = function () {
        postLogin.login($scope.postLogin.name, $scope.postLogin.pwd, function(data){
            if(data.loginError == '0') {
                $modalInstance.close({data:$scope.postLogin, isLogin:true});
            } else {
                $scope.loginError = true;
            }
        });
    };

    $scope.reg = function () {
        $modalInstance.close({data:$scope.postRegister, isLogin:false});
    };

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };
};

</script>

</#macro>