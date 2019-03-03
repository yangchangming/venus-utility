<#--ITARI平台首页-->
<@layout.rayoo_itari_index  "瑞友科技IT应用研究院">
<header class="jumbotron">
    <script>
        function CarouselDemoCtrl($scope) {
            $scope.myInterval = 5000;
            var slides = $scope.slides = [];
            <#list 2..4 as imgCount>
            slides.push({
                image: '${rc.getContextPath()}/images/itari/bootstrap-mdo-sfmoma-0${imgCount}.png'
            });
            </#list>
        }
    </script>

    <div ng-controller="CarouselDemoCtrl">
            <carousel interval="myInterval">
                <slide ng-repeat="slide in slides" active="slide.active">
                    <img ng-src="{{slide.image}}">
                </slide>
            </carousel>
    </div>
</header>
<section class="row features">
    <div class="col-md-6">
        <div class="well">
            <@include_page path="${siteCode}/api/news/rayoo_itari_index_news"/>
        </div>
    </div>
    <div class="col-md-6">
        <div class="well">
            <@include_page path="${siteCode}/api/solution/rayoo_itari_index_tech"/>
        </div>
    </div>
</section>
</@layout.rayoo_itari_index>
