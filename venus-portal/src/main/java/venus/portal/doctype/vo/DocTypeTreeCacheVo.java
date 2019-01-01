package venus.portal.doctype.vo;

import venus.frames.base.bo.BaseBusinessObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * Created by ethan on 14-1-2.
 */
public class DocTypeTreeCacheVo extends BaseBusinessObject {

    private Map<String, DocTypeTreeNodeVo> docTypeTreeMap = new HashMap<String, DocTypeTreeNodeVo>();

    public DocTypeTreeCacheVo(HashMap<String, DocTypeVo> originTreeMap) {

        for (String key : originTreeMap.keySet()) {
            DocTypeVo dtv = originTreeMap.get(key);
            DocTypeTreeNodeVo docTypeTreeNodeVo = new DocTypeTreeNodeVo();
            docTypeTreeNodeVo.setId(dtv.getId());
            docTypeTreeNodeVo.setParentId(dtv.getParentID());

            if (docTypeTreeMap.containsKey(dtv.getParentID())) {
                docTypeTreeNodeVo.setLevel(docTypeTreeMap.get(dtv.getParentID()).getLevel()+1);
            } else {
                analyzeLevel(dtv);
                docTypeTreeNodeVo.setLevel(dtv.getLevel().intValue());
            }

            if (dtv.getParent() != null) {
                HashSet<String> parentIds = new HashSet<String>();
                for (DocTypeVo parentVo : dtv.getParent()) {
                    parentIds.add(parentVo.getId());
                }
                docTypeTreeNodeVo.setParent(parentIds);
            }

            addNode(docTypeTreeNodeVo);
        }
    }

    private void analyzeLevel(DocTypeVo currentNode) {
        Set<DocTypeVo> parents = currentNode.getParent();
        DocTypeVo parentNode = null;

        if (parents == null) {
            currentNode.setLevel(1L);
            return;
        }

        for (DocTypeVo parent : parents) {
            if (parent.getId().equals(currentNode.getParentID())) {
                parentNode = parent;
                break;
            }
        }

        if (parentNode != null) {
            if (parentNode.getLevel() == null) {
                analyzeLevel(parentNode);
            }
            currentNode.setLevel(parentNode.getLevel().intValue() + 1L);
            return;
        }
    }

    public void addNode(DocTypeTreeNodeVo nodeVo) {
        docTypeTreeMap.put(nodeVo.getId(), nodeVo);
    }

    public void removeNode(DocTypeTreeNodeVo nodeVo) {
        docTypeTreeMap.remove(nodeVo);
    }

    public DocTypeTreeNodeVo get(String key) {
        return docTypeTreeMap.get(key);
    }

    public boolean isShow(String currentKey, Set<String> auDocTypes) {
        DocTypeTreeNodeVo currentNode = docTypeTreeMap.get(currentKey);
        boolean flag = false;

        for (String docTypeId : auDocTypes) {
            HashSet<String> parentSet = docTypeTreeMap.get(docTypeId).getParent();
            // 栏目根节点
            if (parentSet == null) {
                return true;
            }

            for (String parentId : parentSet) {
                if (currentNode.getLevel() > docTypeTreeMap.get(parentId).getLevel() + 1) {
                    DocTypeTreeNodeVo hangNode = getHangNodeByParentId(docTypeId, parentId);
                    flag = find(hangNode, currentNode);
                } else if (currentNode.getLevel() < docTypeTreeMap.get(parentId).getLevel() + 1) {
                    flag = find(currentNode, docTypeTreeMap.get(parentId));
                } else {
                    flag = docTypeId.equals(currentNode.getId());
                }
                if (flag) {
                    return flag;
                }
            }
        }
        return false;
    }

    private DocTypeTreeNodeVo getHangNodeByParentId(String hangId, String parentId) {
        DocTypeTreeNodeVo hangNode = new DocTypeTreeNodeVo();
        hangNode.setId(hangId);
        hangNode.setLevel(docTypeTreeMap.get(parentId).getLevel());
        return hangNode;
    }

    private boolean find(DocTypeTreeNodeVo lowLevelNode, DocTypeTreeNodeVo highLevelNode) {
        DocTypeTreeNodeVo highLevelNodeTemp = highLevelNode;
        while (highLevelNodeTemp.getLevel() > lowLevelNode.getLevel()) {
            highLevelNodeTemp = docTypeTreeMap.get(highLevelNodeTemp.getParentId());
        }
        return highLevelNodeTemp.getId().equals(lowLevelNode.getId());

    }
}
