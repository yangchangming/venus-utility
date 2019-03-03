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
    selectItemInit('management');
</script>

<div id="management" class="mainbottom clearfix">
    <div class="mainleft">
        <ul>
            <li class="selected"><a href="#">个人管理</a></li>
            <li ng-show="loginInfo.isAdmin==1"><a href="#">评论管理</a></li>
            <li ng-show="loginInfo.isAdmin==1"><a href="#">用户管理</a></li>
        </ul>
    </div>
    <div class="mainrigh">
        <div ng-controller="gap.posts.user.postPersonalManagementCtrl">
            <div class="heading">
                <div class="search">
                    <form class="navbar-search pull-left">
                        内容:<input type="text" class="search-query" ng-model="myData.conditions.CONTENT"
                                   autofocus="addModalShown"/>
                        网站:<gap-ewp-website-list class="search-query" selectedid="myData.conditions"
                                             ></gap-ewp-website-list>
                    </form>
                    <a href="#" ng-click="search()"><img src="${rc.getContextPath()}/images/ewp/posts/searchbj.gif"></a></div>
            </div>
            <div class="graphic">
                <div class="graphicbtn ">

                    <!-- 图文列表页签切换 -->
                    <div class="bs-docs-example">

                        <div class="tab-content" id="myTabContent">
                            <div id="profile" class="tab-pane fade in active">
                                <button class="btn" type="button" ng-click="getPagedDataAsync()">
                                    &nbsp;&nbsp;刷新
                                </button>
                                <button class="btn" type="button"
                                        ng-click="removePosts('selectedRowsPersonalPost')"><img
                                        src="${rc.getContextPath()}/images/ewp/posts/delete.gif">&nbsp;&nbsp;删除
                                </button>
                                <table class="table table-hover ">
                                    <tr>
                                        <th><input type="checkbox" ng-model="changeall"></th>
                                        <th>网站</th>
                                        <th>发布日期</th>
                                        <th>内容</th>
                                        <th><a href="#">查看文档</a></th>
                                    </tr>

                                    <tr ng-repeat="row in myData.results" >
                                        <td><input name="selectedRowsPersonalPost" value="{{row.id}}" type="checkbox" ng-checked="changeall" /></td>
                                        <td>{{row.websiteName}}</td>
                                        <td><birthdayformat  date="row.pubdate"></birthdayformat></td>
                                        <td><div class="short-content">{{row.content}}</div></td>
                                        <td><a target='_blank' href='${rc.getContextPath()}/{{row.websiteCode}}/article/{{row.docId}}'>查看文档</a></td>
                                    </tr>
                                </table>

                                <pagination total-items="myData.totalCount" page="pagingOptions.currentPage" max-size="10"
                                            items-per-page="pagingOptions.pageSize"
                                            previous-text="&lsaquo;" next-text="&rsaquo;"
                                            first-text="&laquo;" last-text="&raquo;"
                                            boundary-links="true" rotate="false" num-pages="numPages"
                                            on-select-page="getPagedDataAsync()">
                                </pagination>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>


        <div ng-controller="gap.posts.user.postManagementCtrl">
            <div class="heading">
                <div class="search">
                    <form class="navbar-search pull-left">
                        用户:<input type="text" class="search-query" ng-model="myData.conditions.usrName"
                                  autofocus="addModalShown"/>
                        内容:<input type="text" class="search-query" ng-model="myData.conditions.CONTENT"
                                  autofocus="addModalShown"/>
                        网站:<gap-ewp-website-list class="search-query" selectedid="myData.conditions"
                            ></gap-ewp-website-list>
                    </form>
                    <a href="#" ng-click="search()"><img src="${rc.getContextPath()}/images/ewp/posts/searchbj.gif"></a></div>
            </div>
            <div class="graphic">
                <div class="graphicbtn ">

                    <!-- 图文列表页签切换 -->
                    <div class="bs-docs-example">

                        <div class="tab-content" id="myTabContent">
                            <div id="profile" class="tab-pane fade in active">
                                <button class="btn" type="button" ng-click="getPagedDataAsync()">
                                    &nbsp;&nbsp;刷新
                                </button>
                                <button class="btn" type="button"
                                        ng-click="removePosts('selectedRowsPost')"><img
                                        src="${rc.getContextPath()}/images/ewp/posts/delete.gif">&nbsp;&nbsp;删除
                                </button>
                                <table class="table table-hover ">
                                    <tr>
                                        <th><input type="checkbox" ng-model="changeall"></th>
                                        <th>网站</th>
                                        <th>用户</th>
                                        <th>发布日期</th>
                                        <th>内容</th>
                                        <th><a href="#">查看文档</a></th>
                                    </tr>

                                    <tr ng-repeat="row in myData.results" >
                                        <td><input name="selectedRowsPost" value="{{row.id}}" type="checkbox" ng-checked="changeall" /></td>
                                        <td>{{row.websiteName}}</td>
                                        <td>{{row.usrName}}</td>
                                        <td><birthdayformat  date="row.pubdate"></birthdayformat></td>
                                        <td><div class="short-content">{{row.content}}</div></td>
                                        <td><a target='_blank' href='${rc.getContextPath()}/{{row.websiteCode}}/article/{{row.docId}}'>查看文档</a></td>
                                    </tr>
                                </table>

                                <pagination total-items="myData.totalCount" page="pagingOptions.currentPage" max-size="10"
                                            items-per-page="pagingOptions.pageSize"
                                            previous-text="&lsaquo;" next-text="&rsaquo;"
                                            first-text="&laquo;" last-text="&raquo;"
                                            boundary-links="true" rotate="false" num-pages="numPages"
                                            on-select-page="getPagedDataAsync()">
                                </pagination>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>


        <div ng-controller="gap.posts.user.ManagementCtrl">
            <div class="heading">
                <div class="search">
                    <form class="navbar-search pull-left">
                        姓名:<input type="text" class="search-query" ng-model="myData.conditions.REAL_NAME"
                                  autofocus="addModalShown"/>
                        性别:<select class="search-query" ng-model="myData.conditions.GENDER">
                                <option value="">选择</option>
                                <option value="1">男</option>
                                <option value="0">女</option>
                            </select>
                    </form>
                    <a href="#" ng-click="search()"><img src="${rc.getContextPath()}/images/ewp/posts/searchbj.gif"></a></div>
            </div>
            <div class="graphic">
                <div class="graphicbtn ">

                    <!-- 图文列表页签切换 -->
                    <div class="bs-docs-example">

                        <div class="tab-content" id="myTabContent">
                            <div id="profile" class="tab-pane fade in active">
                                <button class="btn" type="button" ng-click="getPagedDataAsync()">
                                    &nbsp;&nbsp;刷新
                                </button>
                                <button class="btn" type="button" ng-disabled="selectedRows.length==0"
                                        ng-click="resetPassword('selectedRowsUser')">
                                    &nbsp;&nbsp;重置密码
                                </button>
                                <button class="btn" type="button" ng-click="showAddDialog()">
                                    &nbsp;&nbsp;新建
                                </button>
                                <button class="btn" type="button"
                                        ng-click="removeUser('selectedRowsUser')"><img
                                        src="${rc.getContextPath()}/images/ewp/posts/delete.gif">&nbsp;&nbsp;删除
                                </button>
                                <table class="table table-hover ">
                                    <tr>
                                        <th><input type="checkbox" ng-model="changeall"></th>
                                        <th>真实姓名</th>
                                        <th>email</th>
                                        <th>电话</th>
                                        <th>生日</th>
                                        <th>性别</th>
                                        <th>管理员</th>
                                    </tr>
                                    <tr ng-repeat="row in myData.results" >
                                        <td><input name="selectedRowsUser" value="{{row.id}}" type="checkbox" ng-checked="changeall" /></td>
                                        <td>{{row.realName}}</td>
                                        <td>{{row.email}}</td>
                                        <td>{{row.phone}}</td>
                                        <td><birthdayformat  date="row.birthday"></birthdayformat></td>
                                        <td>
                                            <div ng-switch on="{{row.gender}}">
                                                <div class="animate-switch" ng-switch-when="1">男</div>
                                                <div class="animate-switch" ng-switch-when="0">女</div>
                                                <div class="animate-switch" ng-switch-default></div>
                                            </div>
                                        </td>
                                        <td>
                                            <div ng-switch on="{{row.isadmin}}">
                                                <div class="animate-switch" ng-switch-when="1">是</div>
                                                <div class="animate-switch" ng-switch-when="0">否</div>
                                                <div class="animate-switch" ng-switch-default></div>
                                            </div>
                                        </td>
                                    </tr>
                                </table>

                                <pagination total-items="myData.totalCount" page="pagingOptions.currentPage" max-size="10"
                                            items-per-page="pagingOptions.pageSize"
                                            previous-text="&lsaquo;" next-text="&rsaquo;"
                                            first-text="&laquo;" last-text="&raquo;"
                                            boundary-links="true" rotate="false" num-pages="numPages"
                                            on-select-page="getPagedDataAsync()">
                                </pagination>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <script type="text/ng-template" id="ewp.posts.user.addModal.html">
                <div>
                    <div class="modal-header">
                        <button class="close pull-right" ng-click="cancel()">&times;</button>
                        <h3>添加用户</h3>
                    </div>
                    <div class="modal-body">
                        <form class="bs-docs-example form-horizontal" id="formId" name="infoForm" ng-submit="formSubmit()" novalidate>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">登录名:</label>
                                <div class="col-sm-3 label-show">
                                    <input name="usrName" type="text" style="width:100%" class="input input-normal" ng-model="user.usrName"
                                           autofocus="addModalShown" required name-unique ng-focus/>
                                </div>
                                <div class="col-sm-6 control-describe">
                                    <span ng-show="infoForm.usrName.$error.required && !infoForm.usrName.$focused" style="color: red;">登陆名不能为空！</span>
                                </div>
                                <div class="col-sm-6 control-describe">
                                    <span ng-show="infoForm.usrName.$error.nameUnique && !infoForm.usrName.$focused" style="color: red;">该用户名已存在!</span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">密码:</label>
                                <div class="col-sm-3 label-show">
                                    <input name="usrPWD" type="password" style="width:100%" class="input input-normal" ng-model="user.usrPWD"
                                           autofocus="addModalShown" required ng-focus/>
                                </div>
                                <div class="col-sm-6 control-describe">
                                    <span ng-show="infoForm.usrPWD.$error.required && !infoForm.usrPWD.$focused" style="color: red;">密码不能为空！</span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">管理员:</label>
                                <div class="col-sm-3 label-show">
                                    <select class="input input-normal" ng-model="user.usrIsAdmin" autofocus="addModalShown">
                                        <option value="0" selected="selected">否</option>
                                        <option value="1">是</option>
                                    </select>
                                </div>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-default btn-primary" ng-disabled="infoForm.$invalid" ng-click="createUser()">提交</button>
                        <a class="btn" ng-click="cancel()">关闭</a>
                    </div>
                </div>
            </script>
        </div>



    </div>
</div>