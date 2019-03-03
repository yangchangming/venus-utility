<!-- header -->
<div class=" headtop">
    <div class="container">
        <nav class="collapse navbar-collapse bs-navbar-collapse" role="navigation">
            <ul class="nav navbar-nav">
                <li ng-show="loginInfo.loginState == '0'">
                    <a href="#" ng-click="open()">登录</a>
                </li>
                <li ng-show="loginInfo.loginState != '0'">
                    <a href="${rc.getContextPath()}/apis/ewp/user/loginInterface" >管理</a>
                </li>

            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li>
                    <form action="${rc.getContextPath()}${siteCode}/search" id="infoForm" name="infoForm" class="navbar-form navbar-left" role="search" method="post">
                        <label class="label"  >站内搜索：</label>
                        <div class="form-group">
                            <input name="condition" type="text" class="form-control" placeholder="请输入关键字...">
                        </div>
                        <button type="submit" class="btn btn-default">Search</button>
                    </form>
                </li>
            </ul>
        </nav>
    </div>
</div>
<nav class="navbar navbar-default" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a href="${rc.getContextPath()}${siteCode}" class="navbar-brand">
                <img src="${rc.getContextPath()}/images/itari/itari/logo.png" alt="ITARI">
            </a>
        </div>
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav navbar-right">
            <@include_page  path="${siteCode}/api/nav/headerNavigate"/>

                <li class=""><a href="http://gap.use.com.cn" target=_blank>GAP维基</a></li>
                <li class=""><a href="http://q.itari.com.cn" target=_blank>交流社区</a></li>
            </ul>
        </div>
    </div>
</nav>
