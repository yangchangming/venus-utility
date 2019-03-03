<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>搜索页面</title>
    <link rel="stylesheet" href="${rc.getContextPath()}/css/ewp/website/search.css"/>
    <script lanuage="Javascript">
        function changePage(page,pageCount) {
            if ((page!=null) && (page>0) && (page<=pageCount)){
                document.getElementById('currentPage').value = page;
                document.getElementById('infoForm').submit();
            }
        }

        function turnPage(pageNoObj,pageCount) {
            var page = document.getElementById('pageNo').value;
            page = parseInt(page) + pageNoObj;
            if ((page!=null) && (page>0) && (page<=pageCount)){
                document.getElementById('currentPage').value = page;
                document.getElementById('infoForm').submit();
            }
        }

        function btmSubmit(){
            document.getElementById('top_condition').value = document.getElementById('bottom_condition').value;
            document.getElementById('infoForm').submit();
        }
    </script>
</head>

<body>
<div id="wrapper">

    <form action="" id="infoForm" name="infoForm" method="post">
        <input type="hidden" id="currentPage" name="currentPage"/>

        <div id="header">
            <div class="columns2">

                <div>
                    <div class="scform">
                        <h1><a href="${rc.getContextPath()}/itari" title="website"><img
                                src="${rc.getContextPath()}/images/ewp/website/searchLogo.png"></a></h1>

                        <div class="scform_form">
                            <div class="td_srchtxt">
                                <input type="text" class="scform_srchtxt" id="top_condition" name="condition" value="${result.condition}">
                            </div>
                            <div class="td_srchbtn">
                                <input type="submit" class="scform_submit" value="搜索">
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div id="main">
            <div class="clearfix">
                <div class="result">
                    <div class="allnum">
                    <#if result.pageResults.results?has_content>
                        搜到 <span>${result.pageResults.totalCount}</span> 个帖子
                    <#else>
                        搜到 <span>0</span> 个帖子
                    </#if>

                    </div>

                    <div id="result-items">
                        <ul>
                        <#if !(result.pageResults.results?has_content)>
                            <li>抱歉搜索不到您的内容</li>

                            <li>请您检查搜索内容</li>
                            <li>并返回&nbsp;&nbsp;&nbsp;&nbsp;<a href="${rc.getContextPath()}${siteCode}">网站首页</a></li>
                        </#if>
                        <#list result.pageResults.results as doc>
                            <li>
                                <h3 class="title1">
                                    <a href="${rc.getContextPath()}/${doc.fields.path.value}" target="_blank">
                                        <#if doc.fields.title.value?exists>
                                            ${doc.fields.title.value}
                                        </#if>
                                    </a>
                                </h3>

                                <p class="content">
                                    <#if doc.fields.contents.value?exists>
                                        ${doc.fields.contents.value}
                                    </#if>
                                </p>

                                <p class="meta">
                                    <#if doc.fields.publishtime.value?exists>
                                        ${doc.fields.publishtime.value}
                                    </#if>
                                </p>
                            </li>

                            <br/>
                        </#list>
                        </ul>
                    </div>
                </div>
                <br>
            <#function MAX x y>
                <#if (x<y)><#return y><#else><#return x></#if>
            </#function>
            <#function MIN x y>
                <#if (x<y)><#return x><#else><#return y></#if>
            </#function>

            <#assign maxSize = 10>
            <#assign currentPage = result.pageResults.currentPage>
            <#assign totalPages = result.pageResults.pageCount>
            <#assign startPage = (((currentPage / maxSize)?ceiling - 1) * maxSize) + 1>
            <#assign endPage = MIN(startPage + maxSize - 1, totalPages)>

            <#if result.pageResults.results?has_content>
                <span class="pg">
                          <a href="#" class="prev" onclick="changePage(1,${result.pageResults.pageCount})"
                             <#if result.pageResults.currentPage==1>disabled</#if>>首页</a>
                          <a href="#" class="prev" onclick="turnPage(-1,${result.pageResults.pageCount})"
                             <#if result.pageResults.currentPage==1>disabled</#if>>上一页</a>
                    <#list startPage..endPage as i>
                            <a href="#" onclick="changePage(${i},${result.pageResults.pageCount})" <#if i==currentPage> class="active" </#if>>${i}</a>
                    </#list>
                          <a href="#" class="nxt" onclick="turnPage(+1,${result.pageResults.pageCount})"
                             <#if result.pageResults.currentPage==result.pageResults.pageCount>disabled</#if>>下一页</a>
                          <a href="#" class="nxt" onclick="changePage(${result.pageResults.pageCount},${result.pageResults.pageCount})"
                             <#if result.pageResults.currentPage==result.pageResults.pageCount>disabled</#if>>末页</a>
                      </span>
                共${result.pageResults.totalCount}条记录 共${result.pageResults.pageCount}页
                这是第
                <SELECT id="pageNo" name="pageNo" onchange="changePage(this.options[this.options.selectedIndex].value,${result.pageResults.pageCount})">
                    <#list 1..result.pageResults.pageCount as page>
                        <OPTION value="${page}" <#if result.pageResults.currentPage==page>
                                selected</#if>>${page}</OPTION>
                    </#list>
                </SELECT>
                页 每页${result.pageResults.pageSize}条记录
            </#if>
            </div>
            <div class="bottombox">
                <div class="bot_form" cellspacing="0" cellpadding="0">
                    <div class="bot_srchtxt">
                        <input type="text" class="scform_srchtxt" id="bottom_condition" name="condition"
                               value="${result.condition}">
                    </div>
                    <div class="td_srchbtn">
                        <input type="button" class="scform_submit" onclick="btmSubmit()" value="搜索">
                    </div>
                </div>
            </div>
        </div>

    </form>
</div>
<div class="foot">© 2015-2018 贵州众创未来科技有限公司(GuiZhou DataVens Co., Ltd.). © 版权所有</div>
</body>
</html>
