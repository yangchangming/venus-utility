package venus.portal.tree.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import venus.frames.i18n.util.LocaleHolder;
import venus.portal.doctype.bs.IDocTypeBS;
import venus.portal.doctype.model.DocType;
import venus.portal.doctype.vo.DocTypeTreeVo;
import venus.portal.tree.util.ITreeConstants;
import venus.portal.tree.vo.DocTypeTreeObjectVo;
import venus.portal.tree.vo.TreeViewObjectVo;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cj on 13-12-18.
 *
 */

@Controller
public class DocTypeTreeController {
    @Autowired
    private IDocTypeBS docTypeBS;

    @RequestMapping(value="/doctype/tree")
    @ResponseBody
    public List<DocTypeTreeObjectVo> getAuNodeByParentId(HttpServletRequest request) {
        String pId = request.getParameter("pId");
        String siteId = request.getParameter("siteId");
        List<DocTypeTreeObjectVo> nodeList = new ArrayList<DocTypeTreeObjectVo>();

        if (pId == null || pId.isEmpty()) {
            getRootNodes(nodeList, request);
        } else if (siteId != null && !siteId.isEmpty()) {
            getSubNodes(pId, siteId, request, nodeList);
        }

        return nodeList;
    }

    private void getRootNodes(List<DocTypeTreeObjectVo> nodeList, HttpServletRequest request) {
        List<DocType> rootList = docTypeBS.queryAllRootNode(request);

        for (int i = 0; i < rootList.size(); i++) {
            DocType root = rootList.get(i);

            DocTypeTreeObjectVo node = new DocTypeTreeObjectVo();
            node.setId(root.getId());
            node.setSiteId(root.getSite().getId());
            node.setName(root.getName());
            node.setRootId(node.getId());
            node.setIsParent(root.getChildren().size() > 0 ? "true" : "false");
            node.setIcon(request.getContextPath() + ITreeConstants.TREE_IMAGEROOT);
            addLinkInfo(node, root.getId(), request);

            nodeList.add(node);
        }
    }

    private void getSubNodes(String parentId, String siteId, HttpServletRequest request,
                             List<DocTypeTreeObjectVo> nodeList) {
        List<DocTypeTreeVo> dtList = docTypeBS.querySubAuNode(parentId, siteId, request);

        for (int i = 0; i < dtList.size(); i++) {
            DocTypeTreeVo dttv = dtList.get(i);

            DocTypeTreeObjectVo node = new DocTypeTreeObjectVo();
            node.setId(dttv.getId());
            node.setSiteId(siteId);
            node.setName(dttv.getName());
            node.setIsParent(dttv.getType() == TreeViewObjectVo.HAS_SUB_NODE ? "true" : "false");
            node.setIconSkin(ITreeConstants.TREE_ICONSKIN);
            if (dttv.getName().contains(LocaleHolder.getMessage("udp.ewp.doctype.hang"))) {
                node.setDrag("false");
            }
            addLinkInfo(node, dttv.getId(), request);

            nodeList.add(node);
        }
    }

    private void addLinkInfo(DocTypeTreeObjectVo node, String docTypeId, HttpServletRequest request) {
        String href = request.getParameter(ITreeConstants.TREE_LINK_HREF);
        String target = request.getParameter(ITreeConstants.TREE_LINK_TARGET);

        if (href != null && !href.isEmpty()) {
            node.setUrl(request.getContextPath() + href + "&docTypeID=" + docTypeId);
        }
        if (target != null && !target.isEmpty()) {
            node.setTarget(target);
        }
    }
}
