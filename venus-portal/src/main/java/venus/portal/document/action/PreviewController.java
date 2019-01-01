package venus.portal.document.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import venus.portal.api.util.IApiConstants;
import venus.portal.document.bs.IDocumentBS;
import venus.portal.document.model.Document;
import venus.portal.helper.EwpVoHelper;

/**
 * Created with IntelliJ IDEA.
 * User: cj
 * Date: 13-9-24
 * Time: 上午9:34
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/{siteCode}/article/preview")
public class PreviewController {
    @Autowired
    private IDocumentBS documentBS;

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView preview(@PathVariable String siteCode, Document document) {
        EwpVoHelper.null2Nothing(document);
        // 处理热词
        if (documentBS.isShowHotWords(document, siteCode)) {
            documentBS.renderHotWords(document, siteCode);
        }
        // 获取模板名称
        String viewName = documentBS.getDocTemplateName(siteCode, document.getDocTypeID());

        if (viewName.isEmpty()) {
            return new ModelAndView(siteCode + "/" + IApiConstants.VIEW_DOCTYPE_COMMON);
        }
        return new ModelAndView(siteCode + "/" + viewName, IApiConstants.UI_KEY, document);
    }
}
