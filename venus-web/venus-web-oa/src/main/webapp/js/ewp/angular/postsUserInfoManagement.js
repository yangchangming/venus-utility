angular.module('gap.ewp.posts.postsUserInfoManagementModel', ['ngGrid', 'gap.ewp.posts.util'])
    .controller('gap.posts.user.ManagementCtrl', function ($scope, $http, $log, $modal,webContextPathService) {

        $scope.cprsInit = {'conditions': {'REAL_NAME': '', 'GENDER': ''}, 'results': null,
            'totalCount': 0, 'pageSize': 5, 'currentPage': 1, 'orderBy': ''};
        $scope.myData = $scope.cprsInit;

        $scope.pagingOptions = {
            pageSizes: [5, 10, 20],
            pageSize: $scope.myData.pageSize,
            currentPage: $scope.myData.currentPage
        };
        $scope.setPagingData = function (data) {
            $scope.myData = data;
        };

        $scope.getPagedDataAsync = function () {

            var data = $scope.myData;

            data.pageSize = $scope.pagingOptions.pageSize;
            data.currentPage = $scope.pagingOptions.currentPage;

            $http({
                method: 'POST',
                url: webContextPathService.getWebContextPath()+'/apis/ewp/user/getUserInfos',
                data: data,
                headers: {
                    'Content-type': 'application/json'
                }
            }).success(function (largeLoad) {
                data = largeLoad;

                $scope.setPagingData(data);
            });

        };

        $scope.getPagedDataAsync();

        $scope.search = function () {
            $scope.getPagedDataAsync();
        }

        $scope.$watch('pagingOptions', function (newVal, oldVal) {
            if (newVal !== oldVal && newVal.currentPage !== oldVal.currentPage) {
                $scope.getPagedDataAsync();
            }
        }, true);

        $scope.showAddDialog = function () {
            var modalInstance = $modal.open({
                templateUrl: 'ewp.posts.user.addModal.html',
                controller: 'gap.posts.user.AddUserCtrl',
                resolve: {
                    post: function () {
                        return {content: ""};
                    }
                }
            });

            modalInstance.result.then(function (content) {
                $scope.content = content;
                $scope.getPagedDataAsync();
            }, function () {
                $log.info('Modal dismissed at: ' + new Date());
            });
        };

        $scope.removeUser = function (selectedRowsName) {
            var selectedId = [];
            $("input[type=checkbox][name='" + selectedRowsName + "']:checked").each(function () {
                selectedId.push($(this).val());
            })

            if(selectedId.length<=0){
                alert("请选择删除项！");
                return;
            }

            if (!window.confirm("确认删除？")) {
                return;
            }

            $http({
                method: 'POST',
                url: webContextPathService.getWebContextPath()+'/apis/ewp/user/removeUser',
                data: selectedId,
                headers: {
                    'Content-type': 'application/json'
                }
            }).success(function (largeLoad) {
                var data = largeLoad;
                if (data.state != '1') {
                    return;
                }
                alert('删除成功');
                $scope.getPagedDataAsync();
            }).error(function () {
                alert('操作错误');
                return;
            });
        };

        $scope.resetPassword = function (selectedRowsName) {
            var selectedId = [];
            $("input[type=checkbox][name='" + selectedRowsName + "']:checked").each(function () {
                selectedId.push($(this).val());
            })

            if(selectedId.length<=0){
                alert("请选择修改项！");
                return;
            }

            if (!window.confirm("确认重置密码为123456？")) {
                return;
            }

            $http({
                method: 'POST',
                url: webContextPathService.getWebContextPath()+'/apis/ewp/user/resetPassword',
                data: selectedId,
                headers: {
                    'Content-type': 'application/json'
                }
            }).success(function (largeLoad) {
                var data = largeLoad;
                if (data.state != '1') {
                    return;
                }
                alert('重置密码成功');
                $scope.getPagedDataAsync();
            }).error(function () {
                alert('重置密码操作错误');
                return;
            });
        };


    }).controller('gap.posts.user.AddUserCtrl', function ($scope, $http, $modalInstance,webContextPathService) {
        $scope.user = {};

        $scope.createUser = function () {
            $http({
                method: 'POST',
                url: webContextPathService.getWebContextPath()+'/apis/ewp/user/addUser',
                data: $scope.user,
                headers: {
                    'Content-type': 'application/json'
                }
            }).success(function (largeLoad) {
                var data = largeLoad;
                if (data.state != '1') {
                    return;
                }

            }).error(function () {
                alert('保存错误');
                return;
            });

            $modalInstance.close("");
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    });
