<#macro use currentLocation="website">
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <#include ".${siteCode}/inc/head.ftl">

<body>
<div id="container">
    <#include ".${siteCode}/inc/header.ftl">
    <#nested />

    <#include ".${siteCode}/inc/footer.ftl">
</div>
</body>
</html>
</#macro>

<#--布局模板-->
<#macro uselayout currentLocation="搜索结果">
    <@layout.use  currentLocation>
    <div class="cBanner2">
        <img src="${rc.getContextPath()}/images/ewp/website/use/BannerNewroom.jpg"
             alt="Newsroom UFIDA Software Engineering"/></div>
    <div class="contentArea2">
        <div class="midArea2 blueLink">

            <div class="textContent">
            <#--新闻中心中间内容展示区-->
               <#nested />
            </div>
        </div>
        <div class="clear"></div>
    </div>
    <div class="clear"></div>
    </@layout.use>
</#macro>

<#--新闻中心布局模板-->
<#macro usenews currentLocation="新闻中心">
    <@layout.use  currentLocation>
    <div class="cBanner2">
        <img src="${rc.getContextPath()}/images/ewp/website/use/BannerNewroom.jpg"
             alt="Newsroom UFIDA Software Engineering"/></div>
    <div class="contentArea2">
        <div class="leftArea2">
            <div class="menuList yellowLink">
            <#--新闻中心以及子栏目-->
               <@include_page path="${siteCode}/api/news/newsDocTypeAndChildren"/>
            </div>
            <div class="contactBox">
                <h2 class="titie2"><span><a href="${rc.getContextPath()}${siteCode}/api/contact_us/contact_us">联系我们</a></span>
                </h2>
                <ul>
                    <li>电话:86-10-62431717</li>
                    <li>传真:86-10-62431800</li>
                    <li>Email:info@use.com.cn</li>
                </ul>
            </div>
            <div class="menuList2 greenLink">
                <h2 class="menuTitle2">Our Customers</h2>

                <div class="center"><img src="../images/Customers.jpg"/></div>
            </div>
        </div>
        <div class="midArea2 blueLink">

            <div class="textContent">
            <#--新闻中心中间内容展示区-->
               <#nested />
            </div>
        </div>
        <div class="rightArea2">
            <div class="menuList2 rightCBox">
                <h2 class="menuTitle2"><span>客户证言</span></h2>

                <p class="p1">“我们选择供应商，一定是这个行业里的前三，无论中外都是如此。在我们的合作中，用友软件工程表现了高水平的研发能力、专业的项目管理能力……”</p>

                <p class="signature">某保险公司IT总监</p>
            </div>

            <div class="rightCBox blueLink">
            <#--新闻中心-成功案例标题-->
               <@include_page path="${siteCode}/api/cgal/casestudytitles"/>
            </div>
        </div>
        <div class="clear"></div>
    </div>
    <div class="clear"></div>
    </@layout.use>
</#macro>

<#--我们服务布局模板-->
<#macro useouerservice currentLocation= "我们的服务">
    <@layout.use  currentLocation>
    <div id="cContent">
        <div class="toparea">
            <div class="cBanner"><img src="${rc.getContextPath()}/images/ewp/website/use/bannerServices.jpg"
                                      alt="About UFIDA Software Engineering"/></div>
            <div class="testimonials">
                <h2 class="title1"><span>客户证言</span></h2>

                <p class="p1">“我们选择供应商，一定是这个行业里的前三，无论中外都是如此。在我们的合作中，用友软件工程表现了高水平的研发能力、专业的项目管理能力……”</p>

                <p class="signature">某保险公司IT总监</p>
            </div>
        </div>
        <div class="contentArea">
            <div class="leftArea">
                <div class="menuList yellowLink">
                <#--我们的服务以及子栏目-->
                <@include_page path="${siteCode}/api/service_zh/ouerservice_doctypelist"/>
                </div>
                <div class="blueLine"></div>
                <div class="menuList3 yellowLink">
                <#--解决方案以及子栏目-->
                  <@include_page path="${siteCode}/api/jjfa/doctypeandchildren"/>
                </div>
                <div class="yellowBgEnd"></div>
                <div class="contactBox">
                    <h2 class="titie2"><span><a
                            href="${rc.getContextPath()}${siteCode}/api/contact_us/contact_us">联系我们</a></span></h2>
                    <ul>
                        <li>电话:86-10-62431717</li>
                        <li>传真:86-10-62431800</li>
                        <li>Email:info@use.com.cn</li>
                    </ul>
                </div>
            </div>
            <div class="midArea blueLink">
                <div class="navLink">
                    <!--
                        <ul>
                            <li class="navLinkFir"><a href="#">USE首页</a></li>
                            <li><a href="#">我们的服务</a></li>
                            <li><a href="#">简介</a></li>
                        </ul>
                        -->
                    <div class="clear"></div>
                </div>
                <div class="textContent">
                <#--我们的服务数据展示区-->
            <#nested />
                </div>
                <br/>

                <div class="clear"></div>
            </div>
            <div class="rightArea">
                <div class="greenBox whiteLink">
                <#--右侧成功案例标题-->
               <@include_page path="${siteCode}/api/cgal/ouerservice_casestudytitles"/>
                </div>
                <div class="greenBgEnd"></div>
                <div class="newsEvents blueLink">
                    <h2 class="titie3"><span>下载</span></h2>
                    <ul class="rightLi">
                        <li><a href="#">物业收费系统 </a></li>
                        <li><a href="#">矿业运销管理系统 </a></li>
                        <li><a href="#">房地产销售管理系统</a></li>
                        <li><a href="#">再保险业务处理系统</a></li>
                    </ul>
                    <div class="readMore"><a href="#"> 更多</a></div>
                </div>
            </div>
            <div class="clear"></div>
        </div>
    </div>
    <div class="clear"></div>
    </@layout.use>
</#macro>

<#-- 成功案例布局模板-->
<#macro usecasestudy  currentLocation="成功案例">
    <@layout.use  currentLocation>
    <div class="toparea">
        <div class="cBanner"><img src="${rc.getContextPath()}/images/ewp/website/use/bannerCase.jpg" alt="Case  Study"/>
        </div>
        <div class="testimonials">
            <h2 class="title1"><span>客户证言</span></h2>

            <p class="p1">“我们集团的信息化起步晚，但是着眼点高，通过整体规划，分布实施，现在集团财务、运销、供应等几个系统已经上线运行，效果很好。……”</p>

            <p class="signature">某矿业集团CIO</p>
        </div>
    </div>
    <div class="contentArea">
        <div class="leftArea">
            <div class="menuList yellowLink">
                <@include_page  path="${siteCode}/api/qdtj/promotionchannels"/>
            </div>
            <div class="blueLine"></div>
            <div class="menuList3 yellowLink">
            <#--解决方案-->
                <@include_page path="${siteCode}/api/jjfa/doctypeandchildren"/>
            </div>
            <div class="yellowBgEnd"></div>
            <div class="contactBox">
                <h2 class="titie2"><span><a href="${rc.getContextPath()}${siteCode}/api/contact_us/contact_us">联系我们</a></span>
                </h2>
                <ul>
                    <li>电话:86-10-62431717</li>
                    <li>传真:86-10-62431800</li>
                    <li>Email:info@use.com.cn</li>
                </ul>
            </div>
            <div class="certificates">
                <img alt="cmmi5_isms" src="${rc.getContextPath()}/images/ewp/website/use/channel_21.jpg"/>
            </div>
        </div>
        <div class="midArea blueLink">
            <div class="navLink">
                <!--
                <ul>
                    <li class="navLinkFir"><a href="/">USE首页</a></li>
                    <li><a href="/case_study/">成功案例</a></li>
                    <li><a href="#">简介</a></li>
                </ul>
                -->
                <div class="clear"></div>
            </div>
            <div class="textContent">
                <#nested />
            </div>
            <br/>
        </div>
        <div class="rightArea">
            <div class="greenBox whiteLink">
                <h2 class="title1"><span>下载中心</span></h2>
                <ul>
                    <li><a href="#">物业收费系统 </a></li>
                    <li><a href="#">矿业运销管理系统 </a></li>
                    <li><a href="#">房地产销售管理系统</a></li>
                    <li><a href="#">再保险业务处理系统</a></li>
                </ul>
                <div class="readMore"><a href="#">更多</a></div>
            </div>
            <div class="greenBgEnd"></div>
            <div class="newsEvents blueLink">
            <#--成功案例页面展示中，新闻中心展示-->
              <@include_page path="${siteCode}/api/company_news/casestudy_newscenter"/>
            </div>
        </div>
        <div class="clear"></div>
    </div>
    <div class="clear"></div>
    </@layout.use>
</#macro>

<#-- 关于我们布局模板-->
<#macro about_us currentLocation="关于我们">
    <@layout.use  currentLocation>
    <div id="cContent">
        <div class="toparea">
            <div class="cBanner"><img alt="About UFIDA Software Engineering" src="${rc.getContextPath()}/images/ewp/website/use/aboutBanner.jpg"/></div>
            <div class="testimonials">
                <h2 class="title1"><span>客户证言</span></h2>

                <p class="p1"><a href="../Customers/testimonials.html">“我们选择供应商，一定是这个行业里的前三，无论中外都是如此。在我们的合作中，用友软件工程表现了高水平的研发能力、专业的项目管理能力……”</a>
                </p>

                <p class="signature">某保险公司IT总监</p>
            </div>
        </div>
        <div class="contentArea">
            <div class="leftArea">
                <div class="menuList yellowLink">
                    <@include_page path="${siteCode}/api/about_us/about_us_list"/>
                </div>
                <div class="yellowBgEnd"></div>
                <div class="picGallery">
                    <h2 class="titie2"><span>图片长廊</span></h2>

                    <div class="picBox"><img src="${rc.getContextPath()}/images/ewp/website/use/picture_gallery.gif"
                                             alt="Picture Gallery" width="216" height="135"/></div>
                </div>
                <div class="certificates"><img alt="cmmi5_isms"
                                               src="${rc.getContextPath()}/images/ewp/website/use/channel_21.jpg"/>
                </div>
            </div>
            <div class="midArea blueLink">
                <#nested />
            </div>
            <div class="rightArea">
                <div class="greenBox whiteLink">
                    <@include_page path="${siteCode}/api/cgal/ouerservice_casestudytitles"/>
                </div>
                <div class="greenBgEnd"></div>
                <div class="newsEvents blueLink">
                    <@include_page path="${siteCode}/api/company_news/about_us_news"/>
                </div>
            </div>
            <div class="clear"></div>
        </div>
    </div>
    </@layout.use>
</#macro>

<#--IT研究院布局模板-->
<#macro itari currentLocation="IT研究院">
    <@layout.use  currentLocation>
    <div id="cContent2">
        <div class="cBanner2">
            <img src="${rc.getContextPath()}/images/ewp/website/use/BannerITARI.jpg"
                 alt="ITARI UFIDA Software Engineering"/>
        </div>
        <div class="contentArea2">
            <div class="leftArea2">
                <div class="menuList2 yellowLink2">
                    <@include_page path="${siteCode}/api/itari/itari_list"/>
                </div>
                <div class="menuList2 yellowLink2">
                    <h2 class="menuTitle2">相关链接</h2>
                    <ul>
                        <li>
                            <a href="http://www.chinaccia.org.cn">中国计算机学会</a>
                        </li>
                    </ul>
                </div>
                <div class="certificates">
                    <img src="${rc.getContextPath()}/images/ewp/website/use/certificates2.jpg" alt="cmmi5_isms"/>
                </div>
            </div>
            <div class="midArea2 blueLink">
                <#nested />
            </div>
            <div class="rightArea2">
                <div class="rightCBox blueLink">
                    <@include_page path="${siteCode}/api/company_news/itari_news"/>
                </div>
                <div class="rightCBox">
                    <h2 class="titie4">Picture Gallery</h2>

                    <div class="picBox2">
                        <img src="${rc.getContextPath()}/images/ewp/website/use/picture_gallery.gif"
                             alt="Picture Gallery" width="216" height="135"/>
                    </div>
                </div>
            </div>
            <div class="clear"/>
        </div>
    </div>
    </@layout.use>
</#macro>


<#--联系我们布局模板-->
<#macro contact_us currentLocation="联系我们">
    <@layout.use  currentLocation>
    <div id="cContent2">
        <div class="cBanner2">
            <img src="${rc.getContextPath()}/images/ewp/website/use/BannerContact.jpg"
                 alt="Contact UFIDA Software Engineering"/>
        </div>
        <div class="contentArea2">
            <div class="leftArea2">
                <div class="menuList2 yellowLink2">
                    <@include_page path="${siteCode}/api/contact_us/contact_us_list"/>
                </div>
                <div class="menuList2 yellowLink2">
                    <a href="#">
                        <img alt="Inquires" src="${rc.getContextPath()}/images/ewp/website/use/inquires.jpg"/>
                    </a>
                </div>
                <div class="menuList2 yellowLink2">
                    <h2 class="menuTitle2">图片长廊</h2>

                    <div class="picGallery2">
                        <img src="${rc.getContextPath()}/images/ewp/website/use/picture_gallery.gif"
                             alt="Picture Gallery" width="170" height="106"/>
                    </div>
                </div>
                <div class="certificates">
                    <img src="${rc.getContextPath()}/images/ewp/website/use/certificates2.jpg" alt="cmmi5_isms"/>
                </div>
            </div>
            <div class="midArea2 blueLink">
                <#nested />
            </div>
            <div class="rightArea2">
                <div class="rightCBox blueLink">
                    <@include_page path="${siteCode}/api/company_news/contact_us_news"/>
                </div>
                <div class="rightCBox blueLink">
                    <h2 class="titie4"><span class="readMore2 blueLink2"><a href="/downloads/">更多</a></span>资料下载</h2>
                    <ul class="rightLi rightNews2">

                    </ul>
                </div>
            </div>
            <div class="clear"/>
        </div>
    </div>
    </@layout.use>
</#macro>