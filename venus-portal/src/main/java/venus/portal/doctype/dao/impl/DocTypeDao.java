package venus.portal.doctype.dao.impl;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import venus.portal.doctype.dao.IDocTypeDao;
import venus.portal.doctype.model.DocType;
import venus.portal.doctype.util.DocTypeVoComparator;
import venus.portal.doctype.util.IConstants;
import venus.portal.doctype.vo.DocTypeTreeVo;
import venus.portal.doctype.vo.DocTypeVo;
import venus.portal.document.model.Document;
import venus.portal.tree.vo.TreeViewObjectVo;
import venus.frames.base.dao.BaseHibernateDao;
import venus.frames.base.exception.BaseDataAccessException;

import java.sql.SQLException;
import java.util.*;

public class DocTypeDao extends BaseHibernateDao implements IDocTypeDao, IConstants {
    /**
     * 查询所有栏目节点
     */
    public List queryAllNode(final String root, final HashSet notIncludeSet, final String site_id) throws BaseDataAccessException {
        HibernateTemplate ht = this.getHibernateTemplate();
        return (List) ht.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                // 查询除根节点下的节点
                Query rootQuery = session.createQuery(QUERY_TREE_NODE);
                rootQuery.setString("site_id", site_id);
                Iterator iter = rootQuery.list().iterator();
                List treeNodeList = new ArrayList();
                Long currentLevel = Long.valueOf(1);
                // 查询下级节点
                if (iter.hasNext()) {
                    DocTypeTreeVo dttv = (DocTypeTreeVo) iter.next();
                    dttv.setLevel(currentLevel);
                    String docTypeID = dttv.getDocTypeID();
                    dttv.setHasShow(CONSTANTS_TRUE);
                    dttv.setHasOperating(CONSTANTS_TRUE);
                    treeNodeList.add(dttv);
                    Query query = session.createQuery(QUERY_TREE_RETRIEVE + "  order by sub.sortNum,sub.name");
                    recursiveTreeNode(dttv, query, notIncludeSet, docTypeID, treeNodeList, (currentLevel + 1), site_id);
                }
                return treeNodeList;
            }
        });
    }

    public List queryAllAuNode(final HashSet<String> au, final HashSet notIncludeSet, final String site_id) throws BaseDataAccessException {
        HibernateTemplate ht = this.getHibernateTemplate();
        return (List) ht.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                // 查询除根节点下的节点
                Query rootQuery = session.createQuery(QUERY_TREE_NODE);
                rootQuery.setString("site_id", site_id);
                Iterator iter = rootQuery.list().iterator();
                List treeNodeList = new ArrayList();
                Map<String,DocTypeTreeVo> treeNodeMap = new HashMap<String,DocTypeTreeVo>();
                Long currentLevel = Long.valueOf(1);
                // 查询下级节点
                if (iter.hasNext()) {
                    DocTypeTreeVo dttv = (DocTypeTreeVo) iter.next();
                    dttv.setLevel(currentLevel);
                    String docTypeID = dttv.getDocTypeID();
                    treeNodeList.add(dttv);
                    treeNodeMap.put(dttv.getId() ,dttv);
                    Query query = session.createQuery(QUERY_TREE_RETRIEVE + "  order by sub.sortNum,sub.name");
                    recursiveAuTreeNode(dttv, query,au, notIncludeSet, docTypeID, treeNodeList,treeNodeMap, (currentLevel + 1), site_id);
                }

                //设置父级栏目显示属性
                for (String doctypeID:au){
                    DocTypeTreeVo currentDoctype = treeNodeMap.get(doctypeID);
                    if(currentDoctype!=null){//被排除的有权限的节点过滤
                        if(currentDoctype.getParentID() != null) {
                            DocTypeTreeVo parentDoctype = treeNodeMap.get(treeNodeMap.get(doctypeID).getParentID());
                            while (parentDoctype != null && CONSTANTS_FLASE.equals(parentDoctype.getHasShow())) {
                                parentDoctype.setHasShow(CONSTANTS_TRUE);
                                String parentParentID = parentDoctype.getParentID();
                                if (parentParentID == null) {
                                    break;
                                }
                                parentDoctype = treeNodeMap.get(parentParentID);
                            }
                        } else {
                            currentDoctype.setHasShow(CONSTANTS_TRUE);
                        }
                    }
                }

                return treeNodeList;
            }
        });
    }

    /*
     * @see udp.ewp.doctype.dao.IDocTypeDao#queryDocTypesByCondition(java.lang.String)
     */
    public List queryDocTypesByCondition(final String condition) {
        HibernateTemplate ht = this.getHibernateTemplate();
        return this.changeDocTypesNullToEmpty((List) ht.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery(QUERY_TREE_QUERY + condition + " order by sortNum,name");
                return query.list();
            }
        }));
    }

    /*
     * @see udp.ewp.doctype.dao.IDocTypeDao#queryDocTypesById(java.lang.String)
     */
    public DocType queryDocTypesById(final String docTypeId) {
        List list = queryDocTypesByCondition(" id = '" + docTypeId + "'");
        if (null != list && list.size() > 0) {
            return (DocType) list.get(0);
        } else {
            return null;
        }
    }

    /**
     * 递归取出树节点对象
     *
     * @param query
     * @param parentID
     * @param treeNodeList
     */
    private void recursiveTreeNode(DocTypeTreeVo parentTree, Query query, HashSet notIncludeSet, String parentID, List treeNodeList, Long currentLevel, String site_id) throws HibernateException {
        query.setString("parentID", parentID);
        List subDTList = query.list();
        List treeList = new ArrayList();
        parentTree.setType(TreeViewObjectVo.NO_SUB_NODE);
        for (Iterator iterator = subDTList.iterator(); iterator.hasNext(); ) {
            DocTypeTreeVo dttv = (DocTypeTreeVo) iterator.next();
            if (notIncludeSet.contains(dttv.getId())) {
                continue;
            }
            if (StringUtils.isBlank(dttv.getId())) {
                continue;
            }
            parentTree.setType(TreeViewObjectVo.HAS_SUB_NODE);
            dttv.setLevel(currentLevel);
            String slefParentID = dttv.getParentID();
            if (!StringUtils.equals(slefParentID, parentID)) {
                dttv.setName(dttv.getName() + "(" + venus.frames.i18n.util.LocaleHolder.getMessage("udp.ewp.doctype.hang") + ")");
            }
            dttv.setHasShow(CONSTANTS_TRUE);
            dttv.setHasOperating(CONSTANTS_TRUE);
            treeList.add(dttv);
            String docTypeID = dttv.getDocTypeID();
            recursiveTreeNode(dttv, query, notIncludeSet, docTypeID, treeList, (currentLevel + 1), site_id);
        }
        treeNodeList.addAll(treeList);
    }

    private void recursiveAuTreeNode(DocTypeTreeVo parentTree, Query query, final HashSet au, HashSet notIncludeSet, String parentID, List treeNodeList, Map<String,DocTypeTreeVo> treeNodeMap, Long currentLevel, String site_id) throws HibernateException {
        query.setString("parentID", parentID);
        List subDTList = query.list();
        List treeList = new ArrayList();
        Map<String,DocTypeTreeVo> treeMap = new HashMap<String,DocTypeTreeVo>();
        parentTree.setType(TreeViewObjectVo.NO_SUB_NODE);
        for (Iterator iterator = subDTList.iterator(); iterator.hasNext();) {
            DocTypeTreeVo dttv = (DocTypeTreeVo) iterator.next();
            if (notIncludeSet.contains(dttv.getId())) {
                continue;
            }
            if (StringUtils.isBlank(dttv.getId())) {
                continue;
            }
            dttv.setLevel(currentLevel);
            String slefParentID = dttv.getParentID();
            if(au.contains(dttv.getId())||parentTree.getHasOperating().equals(CONSTANTS_TRUE)){
                dttv.setHasShow(CONSTANTS_TRUE);
                dttv.setHasOperating(CONSTANTS_TRUE);
                //设置有权限节点的父级栏目显示属性
                if(parentTree!=null&& CONSTANTS_FLASE.equals(parentTree.getHasShow())){
                    parentTree.setHasShow(CONSTANTS_TRUE);
                }
            }
            parentTree.setType(TreeViewObjectVo.HAS_SUB_NODE);
            if (!StringUtils.equals(slefParentID, parentID)) {
                dttv.setName(dttv.getName() + "(" + venus.frames.i18n.util.LocaleHolder.getMessage("udp.ewp.doctype.hang") + ")");
            }
            treeList.add(dttv);
            treeMap.put(dttv.getId(),dttv);
            String docTypeID = dttv.getDocTypeID();
            recursiveAuTreeNode(dttv, query,au, notIncludeSet, docTypeID, treeList,treeMap, (currentLevel + 1), site_id);
        }
        treeNodeList.addAll(treeList);
        treeNodeMap.putAll(treeMap);
    }

    public List queryAllDoc(int currentPage, int pageSize, String orderStr, DocTypeTreeVo dtto) throws BaseDataAccessException {
        StringBuffer hql = new StringBuffer("from doc in " + Document.class + " where doc.isValid='" + CONSTANTS_TRUE + "'");
        if (dtto != null) {
            if (dtto.getDocTypeID() != null && !"".equals(dtto.getDocTypeID())) {
                hql.append(" and doc.docTypeID='" + dtto.getDocTypeID() + "'");
            }
        }
        if (orderStr != null && !"".equals(orderStr)) {
            hql.append(" order by orderStr");
        }
        return this.changeDocTypesNullToEmpty(super.find(hql.toString(), (currentPage - 1) * pageSize, pageSize));
    }

    public int queryDocRecordCountByCondition(DocTypeTreeVo dtto) throws BaseDataAccessException {
        int recordCount = 0;
        StringBuffer hql = new StringBuffer("select count(doc.id) from doc in " + Document.class + " where doc.isValid='" + CONSTANTS_TRUE + "'");
        if (dtto != null) {
            if (dtto.getDocTypeID() != null && !"".equals(dtto.getDocTypeID())) {
                hql.append(" and doc.docTypeID='" + dtto.getDocTypeID() + "'");
            }
        }
        recordCount = ((Long) (super.find(hql.toString()).get(0))).intValue();
        return recordCount;
    }

    public void delete(DocType docType) throws BaseDataAccessException {
        super.delete(docType);
    }

    public void save(DocType docType) throws BaseDataAccessException {
        super.save(docType);
    }

    /**
     * 保存或者更新VO
     *
     * @param docType 栏目VO
     * @throws BaseDataAccessException
     */
    public void saveOrUpdate(DocType docType) throws BaseDataAccessException {
        super.saveOrUpdate(docType);
    }

    public DocType findDocTypeById(String docTypeID) throws BaseDataAccessException {
        return this.changeDocTypeNullToEmpty((DocType) super.get(DocType.class, docTypeID));
    }

    public void update(DocType docType) throws BaseDataAccessException {
        super.update(docType);
    }

    public int getDocTypeNameIsUniqueNumByWebSite(String webSiteId) {
        Integer result = 0;
        StringBuffer queryString = new StringBuffer("SELECT COUNT(*) FROM DocType  dt WHERE dt.name IN ( SELECT dtn.name FROM DocType dtn WHERE dtn.site.id =? ");
        queryString.append("GROUP BY dtn.name HAVING COUNT(*)>1 ) ");
        Object[] values = {webSiteId};
        List temp = this.find(queryString.toString(), values);
        if (temp != null && temp.size() > 0) {
            result = ((Long) temp.get(0)).intValue();
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see udp.ewp.doctype.dao.IDocTypeDao#getDocTypeByCode(java.lang.String)
     */
    public DocType getDocTypeByCode(String code, String siteId) {
        List list = queryDocTypesByCondition(" tempdocType.docTypeCode = '" + code + "'  and tempdocType.site.id= '" + siteId + "'");
        list = this.changeDocTypesNullToEmpty(list);
        if (null != list && list.size() > 0) {
            return (DocType) list.get(0);
        } else {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see udp.ewp.doctype.dao.IDocTypeDao#getDocTypeByName(java.lang.String)
     */
    public DocType getDocTypeByName(String name, String siteId) {
        List list = queryDocTypesByCondition(" tempdocType.name = '" + name + "'  and tempdocType.site.id= '" + siteId + "'");
        list = this.changeDocTypesNullToEmpty(list);
        if (null != list && list.size() > 0) {
            return (DocType) list.get(0);
        } else {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see udp.ewp.doctype.dao.IDocTypeDao#queryAll()
     */
    public HashMap<String, DocTypeVo> queryAllVo() {
//        List<DocType> doctypes = getHibernateTemplate().find("from DocType docType order by docType.sortNum");
        //spring版本升级至4.*，返回类型用泛型<?>代替了
        List<DocType> doctypes = (List<DocType>) getHibernateTemplate().find("from DocType docType order by docType.sortNum");

        doctypes = this.changeDocTypesNullToEmpty(doctypes);
        ArrayList<DocTypeVo> sortedList = exchangeModelToVo(doctypes);
        HashMap<String, DocTypeVo> map = new HashMap<String, DocTypeVo>();
        for (DocTypeVo vo : sortedList) {
            map.put(vo.getId(), vo);
        }
        supplymentParents(doctypes, map);
        HashMap<String, DocTypeVo> returnMap = new HashMap<String, DocTypeVo>();
        for (DocTypeVo Vo : sortedList) {
            returnMap.put(Vo.getDocTypeCode() + Vo.getSite().getWebsiteCode(), Vo);
        }
        return returnMap;
    }

    /**
     * 补充父栏目信息
     */
    private void supplymentParents(List<DocType> doctypes, HashMap<String, DocTypeVo> map) {
        for (DocType vo : doctypes) {
            DocTypeVo docTypeVo = map.get(vo.getId());
            Set<DocType> parent = vo.getParent();
            if(parent != null) {
                for (DocType father : parent) {
                    if (docTypeVo != null) {
                        if (docTypeVo.getParent() == null) {
                            docTypeVo.setParent(new TreeSet<DocTypeVo>(new DocTypeVoComparator()));
                        }
                        docTypeVo.getParent().add(map.get(father.getId()));
                    }
                }
            }
        }
    }

    /**
     * 将model对象转化为Vo对象
     *
     * @param doctypes
     * @return
     */
    private ArrayList exchangeModelToVo(List<DocType> doctypes) {
        ArrayList<DocTypeVo> sortedList = new ArrayList<DocTypeVo>(); // 返回最终包含上下级的栏目VO
        if(doctypes!=null&&doctypes.size()>0){
            for (DocType docType : doctypes) {
                DocTypeVo vo = copyCommonProperties(docType); // 拷贝当前VO基础属性
                SortedSet children = docType.getChildren(); // 取得当前栏目的下级栏目
                TreeSet<DocTypeVo> sortedChildren = new TreeSet<DocTypeVo>(new DocTypeVoComparator());
                if(null != children) {
                    ArrayList<DocTypeVo> list = exchangeModelToVo(new ArrayList(children));
                    for (DocTypeVo docVo : list) {
                        sortedChildren.add(docVo);
                    }
                }
                vo.setChildren(sortedChildren); // 设置子栏目
                sortedList.add(vo);
            }
        }
        return sortedList;
    }

    private DocTypeVo copyCommonProperties(DocType docType) {
        DocTypeVo vo = new DocTypeVo();
        vo.setKeywords(docType.getKeywords());
        vo.setDescription(docType.getDescription());
        vo.setDocTypeCode(docType.getDocTypeCode());
        vo.setId(docType.getId());
        vo.setImagePath(docType.getImagePath());
        vo.setIsLeaf(docType.getIsLeaf());
        vo.setIsNavigateMenu(docType.getIsNavigateMenu());
        vo.setIsValid(docType.getIsValid());
        vo.setLevel(docType.getLevel());
        vo.setName(docType.getName());
        vo.setParentID(docType.getParentID());
        vo.setSharedIds(docType.getSharedIds());
        vo.setSite(docType.getSite());
        vo.setSite_id(docType.getSite().getId());
        vo.setSortNum(docType.getSortNum());
        vo.setTemplate(docType.getTemplate());
        vo.setTemplateId(docType.getTemplateId());
        vo.setDocTemplate(docType.getDocTemplate());
        vo.setDocTemplateId(docType.getDocTemplateId());
        vo.setVersion(docType.getVersion());
        vo.setWhetherLocateToParentNode(docType.getWhetherLocateToParentNode());
        return vo;
    }

    /**
     * 由于oracle对存入的空字符串更改为null，所以初始化为空字符串的字段在使用时可能会出错，故将初始时为空字符串的字段，在查询时重新初始化为空字符串。mysql没有此问题。
     *
     * @param vo
     */
    private DocType changeDocTypeNullToEmpty(DocType vo) {
        if (vo == null) return null;
        if (vo.getImagePath() == null) {
            vo.setImagePath("");
        }
        if (vo.getKeywords() == null) {
            vo.setKeywords("");
        }
        if (vo.getDescription() == null) {
            vo.setDescription("");
        }
        return vo;
    }

    private List<DocType> changeDocTypesNullToEmpty(List<DocType> list) {
        if (list == null) return null;
        for (DocType vo : list) {
            this.changeDocTypeNullToEmpty(vo);
        }
        return list;
    }

    /*
     * (non-Javadoc)
     * 
     * @see udp.ewp.doctype.dao.IDocTypeDao#queryAllBySiteId(java.lang.String)
     */
    public List<DocType> queryAllBySiteId(String siteId) {
//        List<DocType> doctypes = getHibernateTemplate().find("from DocType docType where docType.site.id=?", new Object[]{siteId});

        //spring版本升级至4.*，返回类型用泛型<?>代替了
        List<DocType> doctypes = (List<DocType>) getHibernateTemplate().find("from DocType docType where docType.site.id=?", new Object[]{siteId});

        for (DocType docType : doctypes) {
            docType.setSite_id(siteId);
        }
        return this.changeDocTypesNullToEmpty(doctypes);
    }

    public List queryAllRootNode() {
//        List<DocType> doctypes = getHibernateTemplate().find("from DocType docType where docType.docTypeCode='root'");

        //spring版本升级至4.*，返回类型用泛型<?>代替了
        List<DocType> doctypes = (List<DocType>) getHibernateTemplate().find("from DocType docType where docType.docTypeCode='root'");

        return this.changeDocTypesNullToEmpty(doctypes);
    }
}
