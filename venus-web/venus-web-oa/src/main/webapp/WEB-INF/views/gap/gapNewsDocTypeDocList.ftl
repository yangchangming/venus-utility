 <#--gap 新闻栏目以及栏目表述，栏目下一定文章展示页-->
  <h4>${result.name ! ""}</h4><br/>
                        <div class="row">
                            <div class="col-md-2">
                                <img src="${rc.getContextPath()}/images/ewp/website/gap/gap05.jpg"/>
                            </div>
                            <div class="col-md-3">
                                <p>
                                       ${result.description!""}
                                    </p>
                            </div>
                        </div>
                        <br/>
                        <ul>
                           <@include_page path="${siteCode}/api/articles/${result.id}/gapNewsDocTypeDocTitles2"/>
                        </ul>