<%@ include file="/jsp/authority/tree/include/globalTreeCache.jsp" %>
<%@ page contentType="text/xml;charset=UTF-8" language="java" %>
<%@ page import="venus.authority.util.tree.DeepTreeXmlHandler"%>
<%@ page import="org.springframework.jdbc.core.RowMapper"%>
<%@ page import="java.sql.ResultSet"%>
<%@ page import="java.sql.SQLException"%>
<%@ page import="venus.authority.util.tree.DeepTreeVo"%>
<%@ page import="venus.authority.util.ProjTools"%>
<%@ page import="venus.authority.au.auauthorize.bs.IAuAuthorizeBS"%>
<%@ page import="venus.authority.au.auauthorize.util.IConstants"%>
<%@ page import="venus.authority.au.auauthorize.vo.AuAuthorizeVo"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.Set"%>
<%@ page import="java.util.HashSet"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="venus.authority.helper.LoginHelper"%>
<%@ page import="venus.frames.mainframe.util.Helper"%>
<%@ page import="venus.authority.util.GlobalConstants"%>

<%
try {
	//父级编码
	String parent_code = request.getParameter("parent_code");
	String vCode = request.getParameter("vCode");
	String isUser = request.getParameter("isUser");
    String partyId = request.getParameter("partyId");

    IAuAuthorizeBS auBs = (IAuAuthorizeBS) Helper.getBean(IConstants.BS_KEY);
    //获取拥有权限的节点
    Map allMap = null;
    if ("1".equals(isUser)){
        allMap = auBs.getOrgAuByPartyIdWithOutHistory(partyId,parent_code.substring(0,19));
    }else{
        allMap = auBs.getOrgAuByVisitorCodeWithOutHistory(vCode,parent_code.substring(0,19));
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
    
    DeepTreeXmlHandler dt = new DeepTreeXmlHandler();
    if(null!=allMap&&allMap.size()>0){
	    HashMap allCodeMap = new HashMap();
	    for(Iterator it=allMap.keySet().iterator(); it.hasNext(); ) {
	        AuAuthorizeVo auVo = (AuAuthorizeVo)allMap.get((String)it.next());
	        allCodeMap.put(auVo.getResource_code(),"");
	    }
	    Set allKeySet = allCodeMap.keySet();
	    Set allKeySplitSet = new HashSet();
	    for(Iterator keyIt=allKeySet.iterator();keyIt.hasNext();){
	        String keyCode = (String)keyIt.next();
	        String tempCodes[]=ProjTools.splitTreeCode(keyCode);
	        for(int i = 0; i < tempCodes.length ; i++){            
	            allKeySplitSet.add(tempCodes[i]);
	        }
	    }
	    
	    String partyTypeId = "";
        if(parent_code.startsWith(GlobalConstants.getRelaType_role())){
            partyTypeId = GlobalConstants.getPartyType_role();
        }else if(parent_code.startsWith(GlobalConstants.getRelaType_proxy())){
            partyTypeId = GlobalConstants.getPartyType_proxy();
        }
        String strsql = "select * from au_partyrelation where parent_code like '"+parent_code+"%' "+("".equals(partyTypeId)?"":("and partytype_id='"+partyTypeId+"' "))+ "and (1=2 ";
        for(Iterator codeIt=allKeySplitSet.iterator();codeIt.hasNext();){
		    strsql += " or code = '"+(String)codeIt.next()+"'";
		}
		strsql +=") order by code";
		List lParty = ProjTools.getCommonBsInstance().doQuery(strsql, new RowMapper() {
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
					return map;
	            }
	        });
	        
		for(Iterator it = lParty.iterator(); it.hasNext(); ) {
			//获取节点的数据
			HashMap m_Party = (HashMap) it.next();
			String id = (String)m_Party.get("id");
			String name = (String)m_Party.get("name");
			String code = (String)m_Party.get("code");
			String parentCode = (String)m_Party.get("parent_code");
			String isLeaf = (String)m_Party.get("is_leaf");
			String typeIsLeaf = (String)m_Party.get("type_is_leaf");
			String relationType = (String)m_Party.get("relationtype_id");
			String partyType = (String)m_Party.get("partytype_id");
			
			//判断节点的数据权限
	        int auth = 0; //表示权限的类型：0无权限，1只读，2可写
	        for(int i=0; i<dataLimit.length; i++) {
	            if(code.length()>dataLimit[i].length()) { //当前节点为授权节点的下级节点
	                if(code.substring(0,dataLimit[i].length()).equals(dataLimit[i])) {
	                    auth = 2;
	                    break;
	                }
	            }
	            else if(code.length()==dataLimit[i].length()){ //当前节点为授权节点的同级节点
	                if(code.equals(dataLimit[i])) {
	                    auth = 2;
	                    break;
	                }
	            }else { //当前节点为授权节点的上级节点
	                if(code.equals(dataLimit[i].substring(0,code.length()))) {
	                    auth = 1;
	                    continue;
	                }
	            }
	        }
	        if(auth==0) {
	            continue; //没有权限，直接跳过该节点的构造
	        }
			
			//构造节点
			DeepTreeVo dtv = new DeepTreeVo(code, name, "1".equals(isLeaf) ? "0" : "1", 
				request.getContextPath()+"/jsp/authority/au/auauthorize/viewOrgExpand.jsp?parent_code="+code+"&vCode="+vCode+"&isUser="+isUser+"&partyId="+partyId+"&data_limit="+data_limit);
			//勾中已授权的节点
			if(allKeySet.contains(code)) {
				dtv.setIsSelected("1");//自身拥有权限的节点置为选中状态			
			}
			//打开已授权的节点
			if(allKeySplitSet.contains(code)) {
		        dtv.setDefaultOpen("1");//自身拥有权限的节点置为打开状态            
		    }	
			//设置只读
			dtv.setIsDisable("1");
			//设置返回值
			dtv.setReturnValue(code);
			//设置返回类型
			dtv.setDetailedType(partyType);
			if(parent_code.equals(parentCode)){//如果是根节点
	            dt.addTreeNode(dtv);
	        }else{
	            dt.addTreeNode(parentCode,dtv);
	        }		
		} 
    }else {//没有数据权限
	    DeepTreeVo dtv = new DeepTreeVo("", venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Had_not_been_granted_permission_"), "0" , "");
	    dtv.setIsSubmit("0");
	    dt.addTreeNode(dtv);
	}
    String xmlStr = dt.getStringFromDocument();
    out.print(xmlStr);
} catch(Exception e) {
	e.printStackTrace();
}
%>

