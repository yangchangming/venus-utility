<@layout.use  "瑞友科技首页"> <#-- 此处是注释 公司网站首页  引用布局辣 layout.ftl中得use布局显示 -->
    <div id="content">
        <div class="leftBox">
        <div class="homeBanner">
            <div  id="BigPicShowCnt">
            <div id="EffectCnt" style="POSITION: relative">
                <div class="BigPicElement" style="LEFT: 0px; VISIBILITY:visible; POSITION: absolute; TOP: 0px">
                    <div class="Top">
                    <img src="${rc.getContextPath()}/images/ewp/website/use/homeAd1.jpg" alt="Maximize the value of your business" usemap="#Map" />
                        <map name="Map" id="Map">
                          <area shape="rect" coords="240,274,350,319" href="/Approach/" alt="Approach"  />
                        <area shape="rect" coords="356,274,493,321" href="/ODC/" alt="ODC"  />
                        </map>
                  </div>
                </div>
                <div class="BigPicElement" style="LEFT: 0px;VISIBILITY:hidden;POSITION: absolute; TOP: 0px">
                    <div class="Top">
                    <img src="${rc.getContextPath()}/images/ewp/website/use/homeAd2.jpg" alt="Delivering what businee demands"  usemap="#Map2" />
                            <map name="Map2" id="Map2">
                              <area shape="rect" coords="67,200,179,247" href="Approach/" alt="Approach" />
                            <area shape="rect" coords="186,200,317,247" href="ODC/" alt="ODC" />
                            </map>
                  </div>
                </div>
            </div>
            <div class="Buttons"><span>2</span><span class="ActiveButton">1</span></div>
        </div>
        </div>
        <div class="leftCol">
         <#-- 强档推荐显示 -->
               <@include_page  path="${siteCode}/api/qdtj/promotionchannels"/>
        </div>
        <div class="midCol">
             <#-- 我们的服务显示 -->
            <@include_page  path="${siteCode}/api/service_zh/default_doctypeandchildren"/>
        </div>
        <div class="rightCol">
          <#-- 成功案例显示 -->
            <@include_page path="${siteCode}/api/cgal/channelanddocs"/>
        </div>
        <div class="clear"></div>
        <div class="summary">
            <h2 class="blueLinkT"><a href="${rc.getContextPath()}${siteCode}/api/about_us/about_us">关于用友软件工程有限公司</a></h2>
            <p>用友软件工程有限公司 是用友集团主要成员企业，是全球化软件与信息技术服务供应商。我们致力于为全球客户提供量体裁衣、按需定制的专业IT应用规划咨询、软件系统开发及相关IT运营与支持服务。通过向全球客户提供高质量的个性化IT服务TM，最大限度地协助客户创造价值，与客户共赢共荣。</p>
            <p>作为“国家规划布局内重点软件企业”和首批入选的中国科学技术部“中国软件欧美出口工程”（COSEP-A）A类企业，用友软件工程有限公司是率先整体通过美国SEI CMM5级评估的中国公司；2005年，再次整体通过美国SEI CMMI5级评估。2007年，成为通过BSI（英国标准协会）ISO 27001：2005现场认证的首家中国公司。<a href="${rc.getContextPath()}${siteCode}/api/about_us/about_us" class="more">&gt;更多</a></p>
        </div>
        </div>
        <div class="rightBox">
            <form name="form" method="post">
                <input type="text" placeholder="Search" class="text_field" style="margin-top: 10px;margin-left: 15px;" onkeypress="onSearch(this);"/>
            </form>
            <div class="homeNews">
              <#-- 公司新闻的显示 -->
             <@include_page path="${siteCode}/api/company_news/newsanddocs"/>
            </div>
            <div class="homeMedia">
              <#-- 媒体新闻的展示 -->
           <@include_page path="${siteCode}/api/media_news/medianewsanddocs"/>
            </div>
            <div class="homeOther">
            <a href="#"><img src="${rc.getContextPath()}/images/ewp/website/use/inquires.jpg" alt="Inquires" /></a><br />
            <a href="#"><img alt="Certificate" src="${rc.getContextPath()}/images/ewp/website/use/Certificate.jpg" /></a>
          </div>
        </div>
    </div>
    <div class="clear"></div>
</@layout.use>