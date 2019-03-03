<script type="text/javascript">
    function selectItemInit(id) {
        var $items = $('#' + id + '>.mainleft>ul>li');
        $items.click(function (item) {
            $items.removeClass('selected');
            $(this).addClass('selected');

            var index = $items.index($(this));
            $('#' + id + '>.mainrigh>div').hide().eq(index).show();
        });
        $items.eq(0).click();
    }
    selectItemInit('info');
</script>

<div id="info">
    <div class="mainleft">
        <ul>
            <li class="selected"><a href="#">个人信息</a></li>
            <li><a href="#">修改密码</a></li>
        </ul>
    </div>
    <div class="mainrigh" >
        <div ng-controller="UserController" >
            <form class="bs-docs-example form-horizontal" id="formId" name="infoForm" ng-submit="formSubmit()" novalidate>
                <div class="form-group">
                    <label class="col-sm-3 control-label">用户名:</label>
                    <div class="col-sm-3 label-show">
                        {{loginInfo.name}}
                    </div>
                </div>
                <div class="form-group">
                    <label for="usrGender" class="col-sm-3 control-label">性别:</label>
                    <div class="col-sm-3">
                        <select class="form-control" id="usrGender" name="usrGender" ng-model="user.gender" ng-options="g.value as g.name for g in gender"></select>
                    </div>
                </div>
                <div class="form-group">
                    <label for="realName" class="col-sm-3 control-label">真实姓名:</label>
                    <div class="col-sm-3 ">
                        <input type="text" class="form-control" ng-model="user.realName" placeholder="" id="realName">
                    </div>
                </div>
                <div class="form-group">
                    <label for="email" class="col-sm-3 control-label">邮箱:</label>
                    <div class="col-sm-3 ">
                        <input type="email" id="email" name="email" ng-model="user.email" class="form-control" />
                    </div>
                    <div class="col-sm-3 control-describe">
                        <span ng-show="infoForm.email.$error.email" style="color: red;">请输入正确的邮箱</span>
                    </div>
                </div>
                <div class="form-group">
                    <label for="phone" class="col-sm-3 control-label">电话:</label>
                    <div class="col-sm-3 ">
                        <input type="text" class="form-control" ng-model="user.phone" placeholder="" id="phone">
                    </div>
                </div>
                <div class="form-group">
                    <label for="birthday" class="col-sm-3 control-label">生日:</label>
                    <div class="col-sm-3 ">
                        <input type="text" class="form-control" placeholder="" id="birthday" datepicker-popup="yyyy-MM-dd" ng-model="user.birthday" datepicker-options="dateOptions" show-button-bar="false" is-open="opened" close-text="Close" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label"></label>
                    <div class="col-sm-3 ">
                        <button class="btn post_btn" type="submit">保存</button>
                    </div>
                </div>
            </form>
        </div>
        <div ng-controller="PwdController" >
            <form class="bs-docs-example form-horizontal" name="pwdForm" ng-submit="formSubmit()" novalidate>
                <div class="form-group">
                    <label class="col-sm-3 control-label">用户名:</label>
                    <div class="col-sm-3 label-show">
                        {{loginInfo.name}}
                    </div>
                </div>
                <div class="form-group">
                    <label for="originalPwd" class="col-sm-3 control-label"><span>*</span>&nbsp;&nbsp;旧密码:</label>
                    <div class="col-sm-3">
                        <input type="password" class="form-control" placeholder="" id="originalPwd" ng-model="user.originalPwd" required>
                    </div>
                    <div class="col-sm-3 control-describe">
                        <span ng-show="errorMsg != ''" style="color: red;">旧密码不正确</span>
                    </div>
                </div>
                <div class="form-group">
                    <label for="newPwd" class="col-sm-3 control-label"><span>*</span>&nbsp;&nbsp;新密码:</label>
                    <div class="col-sm-3">
                        <input type="password" class="form-control" placeholder="" id="newPwd" ng-model="user.newPwd" required>
                    </div>
                </div>
                <div class="form-group">
                    <label for="repPwd" class="col-sm-3 control-label"><span>*</span>&nbsp;&nbsp;确认密码:</label>
                    <div class="col-sm-3">
                        <input type="password" placeholder="" class="form-control" id="repPwd" name="repPwd" ng-model="user.repPwd" value-match="user.newPwd"/>
                    </div>
                    <div class="col-sm-3 control-describe">
                        <span ng-show="pwdForm.repPwd.$error.valueMatch" style="color: red;">两次密码不一致</span>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label"></label>
                    <div class="col-sm-3 ">
                        <button class="btn post_btn" type="submit" ng-disabled="pwdForm.$invalid">确定</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>