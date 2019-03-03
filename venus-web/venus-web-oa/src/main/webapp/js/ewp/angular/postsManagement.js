/**
 * Created by itari-cj on 14-3-18.
 */
angular.module('gap.ewp.posts.postsManagementModule', ['ui.bootstrap','gap.ewp.posts.util'])
    .directive('nameUnique', function ($http,webContextPathService) {
        return {
            require: 'ngModel',
            link: function (scope, elm, attrs, modelCtrl) {
                modelCtrl.$parsers.unshift(function (viewValue) {
                    if (viewValue != '') {
                        $http({
                            method: 'POST',
                            url: webContextPathService.getWebContextPath()+'/apis/ewp/user/checkName',
                            data: {uName: viewValue},
                            headers: {
                                'Content-type': 'application/json',
                                'Accept': 'text/plain'
                            }
                        }).success(function (data) {
                            if (data == "0") {
                                modelCtrl.$setValidity('nameUnique', true);
                            } else {
                                modelCtrl.$setValidity('nameUnique', false);
                            }
                        });
                    }
                    return viewValue;
                });
            }
        }
    });

angular.module('gap.ewp.posts.util', [])
    .directive('birthdayformat', function (dateFilter) {
        return {
            restrict: 'E',
            replace: true,
            scope: {
                date: '='
            },
            link: function (scope, element, attrs) {
                //element.text(attrs.date+dateFilter(new Date(), 'yyyy-M-d'));
                scope.$watch('date', function (newval) {
                    if (scope.date) {
                        element.text(dateFilter(new Date(scope.date), 'yyyy-M-d'));
                    }
                });
            }
        }
    }).directive('gapEwpWebsiteList', function () {
        return{
            restrict: 'E',
            replace: true,
            scope: {
                selectedid: "="
            },
            template: '<select ng-model="x" ng-change="change(x)" ng-options="website.websiteName for website in websites"></select>',
            controller: function ($scope, $http,webContextPathService) {
                $scope.websites = [];
                $http({
                    method: 'GET',
                    url: webContextPathService.getWebContextPath()+'/apis/ewp/website/getAll'
                }).success(function (data) {
                    $scope.websites = data;
                });

                $scope.change = function (option) {
                    $scope.selectedid.WEB_SITE_ID = option.id;
                };
            }
        }
    }).directive('ngConfirmClick', [
        function () {
            return {
                link: function (scope, element, attr) {
                    var msg = attr.ngConfirmClick || "确定?";
                    var clickAction = attr.confirmedClick;
                    element.bind('click', function (event) {
                        if (window.confirm(msg)) {
                            scope.$eval(clickAction);
                        }
                    });
                }
            };
        }
    ]).directive('ngFocus', [function() {
        var FOCUS_CLASS = "ng-focused";
        return {
            restrict: 'A',
            require: 'ngModel',
            link: function(scope, element, attrs, ctrl) {
                ctrl.$focused = true;
                element.bind('blur', function(evt) {
                    scope.$apply(function() {ctrl.$focused = false;});
                });
            }
        }
    }]).directive('valueMatch', function ($parse) {
        return {
            restrict: 'A',
            require: 'ngModel',
            link: function (scope, elm, attrs, modelCtrl) {
                if (!modelCtrl) return;
                if (!attrs['valueMatch']) return;

                var newPwd = $parse(attrs['valueMatch']);

                var validator = function (value) {
                    var temp = newPwd(scope);
                    var match = value === temp;
                    modelCtrl.$setValidity('valueMatch', match);
                };

                elm.bind('blur', function () {
                    scope.$apply(validator(modelCtrl.$viewValue));
                });
            }
        }
    }).factory('webContextPathService', function ($rootScope) {
        var webContextPathService = {};
        $rootScope.webContextPath = "";
        webContextPathService.setWebContextPath = function (webContextPath) {
            $rootScope.webContextPath = webContextPath;
        };
        webContextPathService.getWebContextPath = function () {
            return $rootScope.webContextPath;
        };
        return webContextPathService;
    });