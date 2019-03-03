angular.module('gap.ewp.posts.postPersonalManagementModel', ['ngGrid', 'gap.ewp.posts.util'])
    .controller('gap.posts.user.postPersonalManagementCtrl', function ($scope, $http, $log, $modal,webContextPathService) {
        $scope.cprsInit = {'conditions': {'CONTENT': '', 'WEB_SITE_ID': ''}, 'results': null,
            'totalCount': 0, 'pageSize': 10, 'currentPage': 1, 'orderBy': ''};
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
                url: webContextPathService.getWebContextPath()+'/apis/ewp/posts/getPostsByUser',
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

        $scope.removePosts = function (selectedRowsName) {
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
                url: webContextPathService.getWebContextPath()+'/apis/ewp/posts/deletePosts',
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


    });