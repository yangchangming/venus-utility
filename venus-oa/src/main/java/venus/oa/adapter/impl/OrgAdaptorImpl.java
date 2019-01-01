package venus.oa.adapter.impl;

import org.dom4j.Document;
import venus.oa.adapter.IOrganization;

import java.util.List;

/**
 * 组织系统对外部的api，临时实现方式，需要重构
 */
public class OrgAdaptorImpl implements IOrganization {

    @Override
    public List findAllById(String partyId) {
        return null;
    }

    @Override
    public List findAllById4Mobile(String partyId) {
        return null;
    }

    @Override
    public Document makeXMLDoc(String partyId) {
        return null;
    }

    @Override
    public Document makeXMLDoc(String partyId, boolean isCode) {
        return null;
    }

    @Override
    public boolean hasChildren(String partyId) {
        return false;
    }


    //    /*
//     * （非 Javadoc）
//     *
//     * @see udp.common.authority.service.IOrganization#findPartyById(java.lang.String)
//     */
//    public Party findPartyById(String id) {
//        if (null == id || "".equals(id)) {
//            IAuPartyRelationBs prBs = (IAuPartyRelationBs) Helper
//                    .getBean(IConstants.BS_KEY);
//            AuPartyRelationVo vo = new AuPartyRelationVo();
//            vo.setCode(GlobalConstants.getRelaType_comp() + "00001");
//            List selfList = prBs.queryAuPartyRelation(vo);//查询组织机构根节点
//            if (selfList == null || selfList.size() == 0) {
//                throw new RuntimeException("没有找到组织机构");
//            } else {
//                AuPartyRelationVo selfVo = (AuPartyRelationVo) selfList.get(0);
//                Party party = new Party();
//                party.setId(selfVo.getPartyid());
//                party.setName(selfVo.getName());
//                party.setEmail(selfVo.getEmail());
//                party
//                        .setType(GlobalConstants.getPartyType_empl().equals(
//                                selfVo.getPartytype_id()) ? IAuthorityConstants.PARTY_TYPE_PERSON
//                                : selfVo.getPartytype_id());
//                return party;
//            }
//        }
//
//        //20100108 proxy based on relationid begin
//        //sysid长度
//        int SYS_ID_LENGTH = OidMgr.getSYSCode().length();
//        //conf.xml中，获取TableCodeLen
//        Node node = ConfMgr.getNode("oid-conf").getAttributes().getNamedItem("TableCodeLen");
//        //oid.xml中，表AU_RELATION的code
//        String AU_RELATION = String.valueOf(OidMgr.getSingleton().getOIDGenerator("AU_PARTYRELATION").getTablePrefix());
//        //oid.xml中，表AU_PARTY的code
//        String AU_PARTY = String.valueOf(OidMgr.getSingleton().getOIDGenerator("AU_PARTY_ID").getTablePrefix());
//        //代理关系的授权人vo
//        PartyVo proxyVo = null;
//        AuPartyRelationVo relVo = null;
//        if(id.substring(SYS_ID_LENGTH).startsWith(AU_RELATION.substring(SYS_ID_LENGTH,SYS_ID_LENGTH+Integer.parseInt(node.getNodeValue())))){//relationid
//            IAuPartyRelationBs relBs = (IAuPartyRelationBs) Helper.getBean(IConstants.BS_KEY);
//            relVo = relBs.find(id);//relatinVo
//            if(GlobalConstants.getRelaType_proxy().equals(relVo.getRelationtype_id())){//代理关系，获取授权人id
//                PartyVo proxyPartyVo = OrgHelper.getPartyVoByID(relVo.getParent_partyid());
//                proxyVo = OrgHelper.getPartyVoByID(proxyPartyVo.getOwner_org());
//                id = relVo.getPartyid();
//            }else{//非代理关系，获取当前id
//                id = OrgHelper.getPartyIDByRelationID(relVo.getId());//将relationid替换为partyid
//            }
//            //继续按原有方式执行
//        }else if(id.substring(SYS_ID_LENGTH).startsWith(AU_PARTY.substring(SYS_ID_LENGTH,SYS_ID_LENGTH+Integer.parseInt(node.getNodeValue())))){//partyid
//            //继续按原有方式执行
//        }else{
//            //TODO unsupported.
//            throw new BaseApplicationException("既不是关系id，也不是团体id！");
//        }
//        //20100108 proxy based on relationid end
//        Party party = new Party();
//        if(null!=proxyVo){
//            party.setId(proxyVo.getId());//此时返回partyid
//            party.setName(proxyVo.getName());
//            party.setEmail(proxyVo.getEmail());
//            party.setType(GlobalConstants.getPartyType_empl().equals(
//                    proxyVo.getPartytype_id()) ? IAuthorityConstants.PARTY_TYPE_PERSON
//                    : proxyVo.getPartytype_id());
//            party.setProxyId(relVo.getId());//此时返回relationid
//            party.setProxyName(relVo.getName());
//        }else{
//            PartyVo partyVo = (PartyVo) OrgHelper.getPartyVoByID(id);
//            party.setId(partyVo.getId());//此时返回partyid
//            party.setName(partyVo.getName());
//            party.setEmail(partyVo.getEmail());
//            party.setType(GlobalConstants.getPartyType_empl().equals(
//                    partyVo.getPartytype_id()) ? IAuthorityConstants.PARTY_TYPE_PERSON
//                    : partyVo.getPartytype_id());
//        }
//        return party;
//    }
//
//    /*
//     * （非 Javadoc）
//     *
//     * @see udp.common.authority.service.IOrganization#findAllById(java.lang.String)
//     */
//    public List findAllById(String ids) {
//        List al = new ArrayList();
//        String id[] = ids.split(",");
//        for(int k = 0;k<id.length;k++){
//            al.add(findPartyById(id[k]));//自身
//
//            IAuPartyRelationBs prBs = (IAuPartyRelationBs) Helper.getBean(IConstants.BS_KEY);
//            AuPartyRelationVo vo = new AuPartyRelationVo();
//            vo.setPartyid(id[k]);
//            vo.setRelationtype_id(GlobalConstants.getRelaType_comp());
//            List list = prBs.queryAuPartyRelation(vo);//查询id的根
//            for(int j=0;j<list.size();j++){
//                AuPartyRelationVo   relVo=(AuPartyRelationVo)list.get(j);
//                //2010-11-19 将getSubRelationListByCode 改为 getAllSubRelationListByCode
//                //List relList = OrgHelper.getSubRelationListByCode(relVo.getCode());
//                List relList = ProjTools.getCommonBsInstance().doQuery(IConstants.QUERY_AU_PARTYRELATION_SQL+" where PARENT_CODE like '"+relVo.getCode()+"%' ", new RowMapper() {
//                    public Object mapRow(ResultSet rs, int i) throws SQLException {
//                        AuPartyRelationVo vo = new AuPartyRelationVo();
//                        Helper.populate(vo, rs);
//                        return vo;
//                    }
//                });
//               //2010-11-19 end
//                for (int i = 0; i < relList.size(); i++) {
//                    al.add(findPartyById(((AuPartyRelationVo) relList.get(i))
//                            .getPartyid()));//子，注意每个子的父（的父……）也要获取
//                }
//            }
//        }
//        return al;
//    }
//
//    public Document makeXMLDoc(String partyId){
//        return makeXML(partyId);
//    }
//    private Document makeXML(String partyID) {
//        Document document = DocumentHelper.createDocument();
//        IAuPartyRelationBs prBs = (IAuPartyRelationBs) Helper.getBean(IConstants.BS_KEY);
//        AuPartyRelationVo vo = new AuPartyRelationVo();
//        if (partyID == null || "".equals(partyID)) {
//            vo.setCode(GlobalConstants.getRelaType_comp() + "00001");
//            List selfList = prBs.queryAuPartyRelation(vo);//查询组织机构根节点
//            if(selfList==null || selfList.size()==0) {
//                throw new RuntimeException("没有找到组织机构");
//            }else {
//                AuPartyRelationVo selfVo = (AuPartyRelationVo)selfList.get(0);
//                vo = new AuPartyRelationVo();
//                vo.setParent_code(selfVo.getCode());
//                List parties = prBs.queryAuPartyRelation(vo);
//                //List parties = prBs.queryAllByCode(selfVo.getCode());
//                if (parties == null)
//                    parties = new ArrayList();
//                Element root = document.addElement("Role").addAttribute("ID",
//                        selfVo.getPartyid()).addAttribute("Name",
//                        selfVo.getName()).addAttribute("Type",
//                                IAuthorityConstants.PARTY_TYPE_VIEW);
//                buildRole(root, parties);
//            }
//        } else {
//            vo.setPartyid(partyID);
//            vo.setRelationtype_id(GlobalConstants.getRelaType_comp());
//            List selfList = prBs.queryAuPartyRelation(vo);//查询personID自身的实例
//            if(selfList==null || selfList.size()==0) {
//                return null;
//            }else {
//                for(int i=0; i<selfList.size(); i++) {
//                    AuPartyRelationVo selfVo = (AuPartyRelationVo)selfList.get(i);
//                    if(GlobalConstants.isPerson(selfVo.getPartytype_id())) {
//                        /**
//                         * <Relation ID="00001" Type="Relation" ChildID="theID19"
//                         * Name="直接上级" />
//                         * <p>
//                         * <Relation ID="00002" Type="Relation" ChildID="theID19"
//                         * Name="所属直接部门" />
//                         */
//                        /**
//                         * root
//                         */
//                        /*PersonInfo person = new PersonInfo();
//                        person.setId(selfVo.getPartyid());
//                        person.setTypeId(ParticipantTypeType.HUMAN);
//                        person.setEmail(selfVo.getEmail());
//                        person.setName(selfVo.getName());*/
//
//                        Element root = document.addElement("Role").addAttribute("ID",
//                                selfVo.getPartyid()).addAttribute("Name", selfVo.getName())
//                                .addAttribute("Email", selfVo.getEmail()).addAttribute(
//                                        "Type", IAuthorityConstants.PARTY_TYPE_PERSON);
//
//                        root.addElement("Relation").addAttribute("ID", "00001")
//                                .addAttribute("Type", "Relation").addAttribute(
//                                        "ChildID", null)
//                                .addAttribute("Name", "直接上级");
//                        root.addElement("Relation").addAttribute("ID", "00002")
//                                .addAttribute("Type", "Relation").addAttribute(
//                                        "ChildID", null)
//                                .addAttribute("Name", "所属直接部门");
//                    }else {
//                        /**
//                         * <Role ID="1004010200000000004" Name="胡奇"
//                         * Email="huqi@use.com.cn" Type="HUMAN">
//                         * <p>
//                         * <Role ID="1002010200000000001" Name="用友公司"
//                         * Type="ORGANIZATIONAL_UNIT">
//                         */
//                        //List parties = prBs.queryAllByCode(selfVo.getCode());
//                        vo = new AuPartyRelationVo();
//                        vo.setParent_code(selfVo.getCode());
//                        List parties = prBs.queryAuPartyRelation(vo);
//                        if (parties == null)
//                            return null;
//                        /**
//                         * root
//                         */
//                        Element root = document.addElement("Role").addAttribute("ID",
//                                selfVo.getPartyid()).addAttribute("Name",
//                                selfVo.getName()).addAttribute("Type",
//                                        IAuthorityConstants.PARTY_TYPE_ORGANIZATION);
//
//                        this.buildRole(root, parties);
//                    }
//                }
//            }
//        }
//
//        return document;
//    }
//
//    private void buildRole(Element root, List parties) {
//        for (Iterator iter = parties.iterator(); iter.hasNext();) {
//            AuPartyRelationVo child = (AuPartyRelationVo) iter.next();
//
//            if (GlobalConstants.isPerson(child.getPartytype_id())) {
//                //人员
//                root.addElement("Role").addAttribute("ID", child.getPartyid())
//                        .addAttribute("Name", child.getName())
//                        .addAttribute("Email", child.getEmail())
//                        .addAttribute("isLeaf", child.getIs_leaf())//增加代码,判断当前节点是否为叶子节点
//                        .addAttribute("Type", IAuthorityConstants.PARTY_TYPE_PERSON);
//            } else {
//                //公司、部门、岗位
//                root.addElement("Role").addAttribute("ID", child.getPartyid())
//                        .addAttribute("Name", child.getName())
//                        .addAttribute("isLeaf", child.getIs_leaf())//增加代码,判断当前节点是否为叶子节点
//                        .addAttribute("Type", IAuthorityConstants.PARTY_TYPE_ORGANIZATION);
//            }
//        }
//    }
//    /**
//     * @deprecated
//     */
//    public boolean hasChildren(String partyId) {
//        //通过PARTYID查询出当前节点的CODE
//        String sql="select CODE from AU_PARTYRELATION where 1=1 and PARTYID='"+partyId+"' ";
//
//        String parentCode=(String) ProjTools.getCommonBsInstance().doQueryForObject(sql, new RowMapper() {
//            public Object mapRow(ResultSet rs, int i) throws SQLException {
//                return rs.getString("CODE");
//            }
//        });
//        //通过PARENT_CODE来查询子节点的个数
//        sql="select count(1) from AU_PARTYRELATION where 1=1 and PARENT_CODE like '"+parentCode+"%' ";
//
//        int count=ProjTools.getCommonBsInstance().doQueryForInt(sql);
//
//        if(count>0){
//            return true;
//        }else{
//            return false;
//        }
//    }
//
//    /* (non-Javadoc)
//     * @see udp.common.authority.service.IOrganization#findAllById4Mobile(java.lang.String)
//     */
//    public List findAllById4Mobile(String id) {
//        return findAllById(id);
//    }
//
//    /* (non-Javadoc)
//     * @see udp.common.authority.service.IOrganization#makeXMLDoc(java.lang.String, boolean)
//     */
//    public Document makeXMLDoc(String partyId, boolean isCode) {
//        if(isCode)
//            partyId = OrgHelper.getPartyVoByCode(partyId).getId();
//        return makeXMLDoc(partyId);
//    }
}