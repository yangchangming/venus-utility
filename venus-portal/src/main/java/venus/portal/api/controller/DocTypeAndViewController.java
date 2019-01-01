/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.portal.api.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import venus.portal.api.util.IApiConstants;
import venus.portal.doctype.bs.IDocTypeBS;
import venus.portal.doctype.vo.DocTypeVo;

import java.util.HashMap;


/**
 * @author zhaoyapeng 获取文章类型以及跳转控制器，前台调用可传入栏目code（文档类型），
 *         以及模板view参数(view参数可为空) 如果传入了栏目code（文档类型）
 *         则获取文档类型相关数据返回至 view参数指定的模板进行展示
 *         如果没有传入view参数 ，则直接根据code获取栏目信息挂接的模板信息指定的viewcode参数进行跳转
 *         例如，获取获取解决方案栏目以及子栏目的调用方法为:
 *         <@include_page path="/api/doctypeandview.page" params={"code": "jjfa", "view": "doctypeandchildren"}/>
 */
@Controller
public class DocTypeAndViewController {

    @Autowired
    private IDocTypeBS docTypeBS;

    @RequestMapping(value = "/{siteCode}/api/{code}/{viewName}")
    public ModelAndView docTypeAndViewHandle(@PathVariable String siteCode, @PathVariable String code,
                                             @PathVariable String viewName) throws Exception {
        if (StringUtils.isNotBlank(code)) {
            String templateName = "";
            DocTypeVo docType = docTypeBS.getDocTypeFromCache(siteCode, code);
            HashMap<String, Object> map = new HashMap<String, Object>();

            if (docType != null) {
                if (StringUtils.isNotBlank(viewName)) { // 模板view参数优先
                    templateName = viewName;
                } else {
                    templateName = docTypeBS.getTemplateName(siteCode, code);

                    if (templateName.isEmpty()) {
                        if (docType.getParentID() == null) {
                            templateName = IApiConstants.VIEW_DOCTYPE_MENU;
                        } else {
                            templateName = IApiConstants.VIEW_DOCTYPE_COMMON;
                        }
                    }
                }

                HashMap<String, String> docTypeMetaMap = new HashMap<String, String>();
                docTypeMetaMap.put("keywords", docType.getKeywords());
                docTypeMetaMap.put("description", docType.getDescription());

                map.put(IApiConstants.UI_KEY, docType);
                map.put("docTypeInfo", docTypeMetaMap);
            } else {
                templateName = IApiConstants.VIEW_DOCTYPE_COMMON;
            }

            return new ModelAndView(siteCode + "/" + templateName, map);
        } else {
            return new ModelAndView(siteCode + "/" + IApiConstants.VIEW_DOCTYPE_COMMON);
        }
    }

    @RequestMapping(value = "/{siteCode}/articles/{code}", method = RequestMethod.GET)
    public ModelAndView docTypeHandle(@PathVariable String siteCode, @PathVariable String code) throws Exception {
        return docTypeAndViewHandle(siteCode, code, "");
    }
}
