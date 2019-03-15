<%@ include file="/jsp/authority/tree/include/globalTreeCache.jsp" %>
<%@ page contentType="text/xml;charset=UTF-8" language="java" %>
<%@ page import="venus.oa.util.tree.DeepTreeXmlHandler"%>
<%@ page import="org.springframework.jdbc.core.RowMapper"%>
<%@ page import="java.sql.ResultSet"%>
<%@ page import="java.sql.SQLException"%>
<%@ page import="venus.oa.util.tree.DeepTreeVo"%>
<%@ page import="venus.oa.util.ProjTools"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Map"%>
<%@ page import="venus.oa.util.transform.json.JsonDataTools"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.HashSet"%>
<%@ page import="java.util.Set"%>
<%@ page import="venus.oa.helper.LoginHelper"%>
<%@ page import="venus.oa.helper.OrgHelper"%>
<%@ page import="venus.oa.history.bs.IHistoryLogBs"%>
<%@ page import="venus.oa.history.vo.HistoryLogVo"%>
<%@ page import="venus.oa.util.GlobalConstants"%>
<%@ page import="venus.oa.sysparam.vo.SysParamVo" %>
<%@ page import="venus.springsupport.BeanFactoryHelper" %>
<%
try {
    //父级编码
    String parent_code = request.getParameter("parent_code");
    //是否全部提交
    String submit_all = request.getParameter("submit_all");
    if(submit_all == null) submit_all = "0";
    //返回值类型
    String return_type = request.getParameter("return_type");
    if(return_type == null) return_type = "id";
    //返回节点类型的类型
    String node_type = request.getParameter("node_type");
    if(node_type == null) node_type = "party_type";
    //是否只展示同一类型的节点
    String same_type = request.getParameter("same_type");
    if(same_type == null) same_type = "0";
    //点击节点要导航到的url
    String url = request.getParameter("url");
    if(url==null) url = "";
    //url的target
    String target = request.getParameter("target");
    if(target==null) target = "";
    //是否全部提交
    String recall = request.getParameter("recall");
    if(recall == null) recall = "false";
    //当前需要展开的au_partyrelation表的id
    String currentRelid = request.getParameter("currentRelid");
    if(currentRelid==null) currentRelid = "";
    String currentCode = null;
    if(!"".equals(currentRelid))
        currentCode = OrgHelper.getRelationCodeByRelationId(currentRelid);
    if(!"".equals(currentRelid)&&null==currentCode){
        HistoryLogVo logVo = (HistoryLogVo)((IHistoryLogBs) BeanFactoryHelper.getBean("historyLogBs")).queryByCondition("a.source_id='"+currentRelid+"' and a.operate_type='"+GlobalConstants.HISTORY_LOG_DELETE+"'"," a.operate_date desc").get(0);
        String deleteCodes[]=ProjTools.splitTreeCode(logVo.getSource_code());
        currentCode = deleteCodes.length>0?deleteCodes[1]:"";
        //currentCode = logVo.getSource_code();
    }   
    //参数：没权限时，触发动作条件
    String[] excWithYesAu = request.getParameterValues("excWithYesAu");
    if(excWithYesAu == null || excWithYesAu.length==0) {
        excWithYesAu = new String[0];
    }
    //参数：没权限时，不触发动作条件
    String[] excWithNoAu = request.getParameterValues("excWithNoAu");
    if(excWithNoAu == null || excWithNoAu.length==0) {
        excWithNoAu = new String[0];
    }
    String excYesStr = "";
    for(int i = 0;i<excWithYesAu.length;i++){
        excYesStr += ("&excWithYesAu="+excWithYesAu[i]);
    }
    String excNoStr = "";
    for(int i = 0;i<excWithNoAu.length;i++){
        excNoStr += ("&excWithNoAu="+excWithNoAu[i]);
    }
    //参数：是否控制数据权限,0 否，1 是
    String data_limit = request.getParameter("data_limit");
    if(data_limit == null || data_limit.length()==0) {
        data_limit = "0";
    }
    String dataLimit[] = null;
    if("0".equals(data_limit) || "no".equals(data_limit) || "false".equals(data_limit)) {
        dataLimit = new String[1];
        dataLimit[0] = parent_code;//如果不控制数据权限，则默认有全部权限
    }else {
        dataLimit = LoginHelper.getOwnerFunOrg(request);//取功能数据权限
        if(null==dataLimit)//如果没有功能数据权限，则取数据权限
            dataLimit = LoginHelper.getOwnerOrgWithoutHistory(request);//取数据权限
    }

    //获取系统参数 DATAPRIV_INHERIT的值
    SysParamVo datapriv_inheritVo= GlobalConstants.getSysParam(GlobalConstants.DATAPRIV_INHERIT);
    String datapriv_inherit=datapriv_inheritVo==null?"true":datapriv_inheritVo.getValue();

    DeepTreeXmlHandler dt = new DeepTreeXmlHandler();
    if(dataLimit!=null && dataLimit.length>0) {//有数据权限
        //2011-9-21 支持动态根 begin
        List lParty  = new java.util.ArrayList();
        final Set tempParentCodes = new HashSet();
        if(parent_code.length()>19&&!"true".equals(recall)){
            
            String splitCodes[] = ProjTools.splitTreeCode(parent_code);
            String allParentSql = "select * from au_partyrelation where 1=2 ";
            for(int codeIndex=0;codeIndex<splitCodes.length;codeIndex++){
                allParentSql += (" or (code='"+ splitCodes[codeIndex] +"' and type_level<>2) ");
            }
            allParentSql += " order by order_code";
            lParty.addAll(ProjTools.getCommonBsInstance().doQuery(allParentSql, new RowMapper() {
                public Object mapRow(ResultSet rs, int no) throws SQLException {
                    HashMap map = new HashMap();
                    map.put("id",rs.getString("id"));
                    map.put("name",rs.getString("name"));
                    map.put("code",rs.getString("code"));
                    map.put("parent_code",rs.getString("parent_code"));
                    map.put("partyid",rs.getString("partyid"));
                    map.put("is_leaf",rs.getString("is_leaf"));
                    map.put("type_is_leaf",rs.getString("type_is_leaf"));
                    map.put("relationtype_id",rs.getString("relationtype_id"));
                    map.put("partytype_id",rs.getString("partytype_id"));
                    tempParentCodes.add(rs.getString("code"));
                    return map;
                }
            }));
        }
        //2011-9-21 支持动态根 over
        
        //从数据库取数据
        String strsql = "select * from au_partyrelation where parent_code='"+parent_code+"' and type_level<>2 order by order_code";     
        lParty.addAll(ProjTools.getCommonBsInstance().doQuery(strsql, new RowMapper() {
                public Object mapRow(ResultSet rs, int no) throws SQLException {
                    HashMap map = new HashMap();
                    map.put("id",rs.getString("id"));
                    map.put("name",rs.getString("name"));
                    map.put("code",rs.getString("code"));
                    map.put("parent_code",rs.getString("parent_code"));
                    map.put("partyid",rs.getString("partyid"));
                    map.put("is_leaf",rs.getString("is_leaf"));
                    map.put("type_is_leaf",rs.getString("type_is_leaf"));
                    map.put("relationtype_id",rs.getString("relationtype_id"));
                    map.put("partytype_id",rs.getString("partytype_id"));
                    tempParentCodes.add(rs.getString("code"));
                    return map;
                }
            }));
        tempParentCodes.add(parent_code);
        String strsql4view = "select * from au_partyrelation where 1=2 ";
        Set parentCodes = new HashSet();
        String tempCodes[]=null==currentCode?new String[0]:ProjTools.splitTreeCode(currentCode);
         for(int i =0 ;i < tempCodes.length;i++){
            if(!tempParentCodes.contains(tempCodes[i])){
                parentCodes.add(tempCodes[i]);
            }
        }        
        for(Iterator it4Parent=parentCodes.iterator();it4Parent.hasNext();){
            String comcode =(String)it4Parent.next();
            strsql4view+=" or parent_code='"+comcode+"'";
            strsql4view+=" or code='"+comcode+"'";
        }
        //strsql4view+=" or parent_code='109910040000000000100001'";
        if((parent_code.length()>19&&!"true".equals(recall))){//删除了条件    ||"true".equals(recall)
            strsql4view+=" or code='"+parent_code.substring(0,19+5+5)+"'";//关系类型
        }else{
            strsql4view+=" or (type_level=2 and relationtype_id='"+parent_code.substring(0,19)+"')";//关系类型
        }
        strsql4view+=" order by order_code ";
        lParty.addAll(ProjTools.getCommonBsInstance().doQuery(strsql4view, new RowMapper() {
            public Object mapRow(ResultSet rs, int no) throws SQLException {
                HashMap map = new HashMap();
                map.put("id", rs.getString("id"));
                map.put("name", rs.getString("name"));
                map.put("code", rs.getString("code"));
                map.put("parent_code", rs.getString("parent_code"));
                map.put("partyid", rs.getString("partyid"));
                map.put("is_leaf", rs.getString("is_leaf"));
                map.put("type_is_leaf", rs.getString("type_is_leaf"));
                map.put("relationtype_id", rs.getString("relationtype_id"));
                map.put("partytype_id", rs.getString("partytype_id"));
                return map;
            }
        }));
        for(Iterator it = lParty.iterator(); it.hasNext(); ) {
            //获取节点的数据
            HashMap m_Party = (HashMap) it.next();
            String id = (String)m_Party.get("id");
            String name = (String)m_Party.get("name");
            String code = (String)m_Party.get("code");
            String parentCode = (String)m_Party.get("parent_code");
            String partyId = (String)m_Party.get("partyid");
            String isLeaf = (String)m_Party.get("is_leaf");
            String typeIsLeaf = (String)m_Party.get("type_is_leaf");
            String relationType = (String)m_Party.get("relationtype_id");
            String partyType = (String)m_Party.get("partytype_id");
            
            //判断节点的数据权限
            int auth = 0; //表示权限的类型：0无权限，1只读，2可写
            for(int i=0; i<dataLimit.length; i++) {
                if(code.length()>dataLimit[i].length()) { //当前节点为授权节点的下级节点
                    if(code.substring(0,dataLimit[i].length()).equals(dataLimit[i])) {
                        if("true".equals(datapriv_inherit))   {//数据权限向下继承,设置为可写
                            auth = 2;
                        }else{
                            auth = 0;
                        }


                        if(LoginHelper.getIsAdmin(request)){//判断是否为超级管理员,是的话,赋予可写权限
                            auth = 2;
                        }
                    }
                }
                else if(code.length()==dataLimit[i].length()){ //当前节点为授权节点的同级节点
                    if(code.equals(dataLimit[i])) {
                        auth = 2;                        
                    }
                }else { //当前节点为授权节点的上级节点
                    if(code.equals(dataLimit[i].substring(0,code.length()))) {
                        auth = 1;
                    }
                }
                if(auth == 0)
                    continue;
                //2011-11-2 支持个性化设置授权节点是否可以超链接 begin
                if(0!=excWithYesAu.length||0!=excWithNoAu.length){
                    String subPartyTableName = (String)ProjTools.getCommonBsInstance().doQueryForObject("select table_name from au_partytype where id = '"+partyType+"'",new RowMapper(){
                        public Object mapRow(ResultSet rs, int no) throws SQLException {
                            return rs.getString("table_name");
                        }
                    });
                    if(null!=subPartyTableName){                                
                        String[][] excWithAu = new String[][]{excWithYesAu,excWithNoAu};                        
                        for(int index = 0;index<excWithAu.length;index++){
                            if(0!=excWithAu[index].length){
                                for(int count = 0;count<excWithAu[index].length;count++){
	                                Map jsonMap = new JsonDataTools().parse(excWithAu[index][count]);
	                                Iterator  jsonIt = jsonMap.entrySet().iterator();
	                                String subPartySql = "select count(1) from "+subPartyTableName+" where id = '"+partyId+"' ";
	                                while   (jsonIt.hasNext()){ 
	                                    Map.Entry entry = (Map.Entry)jsonIt.next(); 
	                                    String fieldName = (String)entry.getKey();
	                                    String fieldValue = (String)entry.getValue();
	                                    subPartySql += (" and "+fieldName+" = '"+fieldValue+"'");
	                                }
	                                Integer subPartyCount = (Integer)ProjTools.getCommonBsInstance().doQueryForObject(subPartySql,new RowMapper(){
	                                    public Object mapRow(ResultSet rs, int no) throws SQLException {
	                                        return rs.getInt(1);
	                                    }
	                                });
	                                if(index==0)
	                                    auth = subPartyCount.intValue()>0?2:auth;
	                                else
	                                    auth = subPartyCount.intValue()>0?1:auth;
	                             }
                            }
                        }                               
                    }
                }
                //2011-11-2 支持个性化设置授权节点是否可以超链接 over
                if(auth == 2)
                    break;
            }
            if(auth==0) {
                continue; //没有权限，直接跳过该节点的构造
            }
            
            if("1".equals(same_type) || "yes".equals(same_type)) {
                isLeaf = typeIsLeaf;//只展示同一类型的节点
            }
            
            String urlEncode =java.net.URLEncoder.encode(url,"UTF-8");
            
            //构造节点
            DeepTreeVo dtv = new DeepTreeVo(code, name, "1".equals(isLeaf) ? "0" : "1", 
                request.getContextPath()+"/jsp/authority/tree/treeLimitData.jsp?parent_code="+code
                +"&currentRelid="+currentRelid+"&submit_all="+submit_all+"&data_limit="+data_limit+excYesStr+excNoStr+"&return_type="+return_type+"&node_type="+node_type+"&same_type="+same_type+"&target="+target+"&recall=true&url="+urlEncode);
            //设置返回值
            if("id".equals(return_type)) {
                dtv.setReturnValue(id);
            }else if("party_id".equals(return_type)) {
                dtv.setReturnValue(partyId);
            }else if("code".equals(return_type)){
                dtv.setReturnValue(code);
            }
            //返回节点类型
            if("is_leaf".equals(node_type)) {
                dtv.setDetailedType(isLeaf);
            }else if("party_type".equals(node_type)) {
                dtv.setDetailedType(partyType);
            }else if("relation_type".equals(node_type)){
                dtv.setDetailedType(relationType);
            }
            //处理可提交的节点
            if("0".equals(submit_all) || "no".equals(submit_all)) {
                dtv.setIsSubmit(isLeaf);//仅叶子节点可以提交
            }
            //设置导航url
            if(url.length()>0) {
                //判断是否有权限
                if(auth == 2) {
                    if("id".equals(return_type)) {
//                        dtv.setHrefPath(url+"&id="+id+"&type="+partyType+"&partyId="+partyId);
                        dtv.setHrefPath(url+"?id="+id+"&type="+partyType+"&partyId="+partyId);
                    }else if("party_id".equals(return_type)) {
//                        dtv.setHrefPath(url+"&id="+partyId+"&type="+partyType+"&relationId="+id);
                        dtv.setHrefPath(url+"?id="+partyId+"&type="+partyType+"&relationId="+id);
                    }else if("code".equals(return_type)){
//                        dtv.setHrefPath(url+"&id="+code+"&type="+partyType+"&relationId="+id);
                        dtv.setHrefPath(url+"?id="+code+"&type="+partyType+"&relationId="+id);
                    }
                }
            }
            if(target.length()>0) {
                dtv.setTarget(target);
            }
            
            if(auth==1) {
                dtv.setIsDisable("1"); //只读权限
            }
            
            //判断是否展开（在上次已经操作过的前提下，刷新时再次展开）
            if(null!=currentCode&&currentCode.indexOf(code)!=-1){//&&!currentCode.equals(code)
                dtv.setDefaultOpen("1");
            }
            //被选中，回显用
            if(code.equals(currentCode)&&!"1".equals(dtv.getIsDisable())){//只读权限不回显
                dtv.setIsSelected("1");
            }
            if(parent_code.length()>19&&!"true".equals(recall)){//2011-9-21 支持动态根
                parent_code = parent_code.substring(0,19);
            }
            if(parent_code.equals(parentCode)){//如果是根节点
                dt.addTreeNode(dtv);
            }else{
                dt.addTreeNode(parentCode,dtv);
            }
        } 
    }else {//没有数据权限
        DeepTreeVo dtv = new DeepTreeVo("", venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Sorry_you_do_not_have_permission_to_view_this_data"), "0" , "");
        dtv.setIsSubmit("0");
        dt.addTreeNode(dtv);
    }
    String xmlStr = dt.getStringFromDocument();
    out.print(xmlStr);
} catch(Exception e) {
    e.printStackTrace();
}
%>