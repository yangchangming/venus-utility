angular.module('gap.ewp.posts.postsUserInfoUpdateModel', ['gap.ewp.posts.util'])
    .controller('UserController', function ($scope, $http,webContextPathService) {
        $scope.dateOptions = {'starting-day': 0, 'show-weeks': false};
        $scope.gender = [{value:'0',name:'女'},{value:'1',name:'男'}];

    $http({
        method: 'GET',
        url: webContextPathService.getWebContextPath()+'/apis/ewp/user/get'
    }).success(function (data) {
        if (data.flag == 'true') {
            $scope.user = data.userInfo;

            if ($scope.user.gender == '1') {
                $scope.user.gender = $scope.gender[1].value;
            } else {
                $scope.user.uGender = $scope.gender[0].value;
            }

            if ($scope.user.birthday == undefined || $scope.user.birthday == null ) {
                $scope.user.birthday = "";
            } else if ($scope.user.birthday != ""){
                $scope.user.birthday = new Date($scope.user.birthday);
            }
        } else {
            $scope.error = data.message;
        }
    });

    $scope.formSubmit = function () {
        if($scope.user.birthday != "") {
            $scope.user.birthdayLong = Date.parse($scope.user.birthday) + "";
        } else {
            $scope.user.birthdayLong = "";
        }

        $http({
            method: 'POST',
            url: webContextPathService.getWebContextPath()+'/apis/ewp/user/update',
            data: $scope.user
        }).success(function (data) {
            alert("保存成功");
        }).error(function (data, status) {
            alert("保存失败，服务器访问错误" + status);
        });
    };
}).controller('PwdController', function ($scope, $http,webContextPathService) {
    $scope.errorMsg = "";
    $scope.formSubmit = function () {
        $http({
            method: 'POST',
            url: webContextPathService.getWebContextPath()+'/apis/ewp/user/updatePwd',
            data: $scope.user
        }).success(function (data) {
            if (data.flag == "false") {
                $scope.errorMsg = data.message;
            } else {
                $scope.errorMsg = "";
                alert("修改成功");
                $scope.user.originalPwd = "";
                $scope.user.newPwd = "";
                $scope.user.repPwd = "";
            }
        }).error(function (data, status) {
            $scope.errorMsg = status;
        });
    };
});