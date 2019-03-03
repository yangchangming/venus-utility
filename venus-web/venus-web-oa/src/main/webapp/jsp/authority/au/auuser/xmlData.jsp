<%@ include file="/jsp/authority/tree/include/globalTreeCache.jsp" %>
<%@ page contentType="text/xml;charset=UTF-8" language="java" %>
<%@ page import="venus.authority.util.tree.DeepTreeXmlHandler"%>
<%@ page import="org.springframework.jdbc.core.RowMapper"%>
<%@ page import="java.sql.ResultSet"%>
<%@ page import="java.sql.SQLException"%>
<%@ page import="venus.authority.util.tree.DeepTreeVo"%>
<%@ page import="venus.authority.util.GlobalConstants"%>
<%@ page import="venus.authority.util.ProjTools"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="venus.authority.helper.LoginHelper"%>
<%
try {
	//参数：上级编码，如果为空则为全部组织的根节点
	String parent_code = request.getParameter("parent_code");
	if(parent_code == null || parent_code.length()==0) {
		parent_code = GlobalConstants.getRelaType_comp();
	}
	
	//是否全部提交
	String submit_all = request.getParameter("submit_all");
	if(submit_all == null) submit_all = "0";	
	
	//参数：返回值是哪个字段，可以选择partyrelation表的id、party_id和code三者之一，默认为code
	String return_type = request.getParameter("return_type");
	if(return_type == null || return_type.length()==0) {
		return_type = "code";
	}
	
	//参数：控制树能展示到的层次，0 全部，1 公司，2 部门，3 岗位，-3 去除岗位，默认为0
	String tree_level = request.getParameter("tree_level");
	if(tree_level == null || tree_level.length()==0) {
		tree_level = "0";
	}	

	String dataLimit[] = LoginHelper.getOwnerOrgWithoutHistory(request);//取数据权限
	DeepTreeXmlHandler dt = new DeepTreeXmlHandler();
	if(dataLimit!=null && dataLimit.length>0) {//有数据权限
		//从数据库取数据
		String strsql = "SELECT * FROM AU_PARTYRELATION WHERE PARENT_CODE='"+parent_code+"' ORDER BY ORDER_CODE";
	
		String partyTypeDept = GlobalConstants.getPartyType_dept();//团体类型－部门
		String partyTypePosi = GlobalConstants.getPartyType_posi();//团体类型－岗位
		String partyTypeEmpl = GlobalConstants.getPartyType_empl();//团体类型－员工
		
		//将partyrelation表的数据以Map形式存放到List中。
		List lParty = ProjTools.getCommonBsInstance().doQuery(strsql, new RowMapper() {
	            public Object mapRow(ResultSet rs, int no) throws SQLException {
	                HashMap map = new HashMap();
	                map.put("id",rs.getString("id"));
					map.put("name",rs.getString("name"));
					map.put("code",rs.getString("code"));
					map.put("partyid",rs.getString("partyid"));
					map.put("is_leaf",rs.getString("is_leaf"));
					map.put("partytype_id",rs.getString("partytype_id"));
					map.put("email",rs.getString("email"));
					return map;
	            }
	        });
		
		//查询机构下的人员(由于扩展版中没有AU_EMPLOYEE表，考虑到通用性，暂不使用该功能)
		/*strsql = "SELECT * FROM AU_EMPLOYEE  WHERE ID IN (SELECT partyid FROM AU_PARTYRELATION WHERE PARENT_CODE ='"+ parent_code + "')";
		List empList = ProjTools.getCommonBsInstance().doQuery(strsql, new RowMapper() {
	            public Object mapRow(ResultSet rs, int no) throws SQLException {
	                HashMap map = new HashMap();
	                map.put("id",rs.getString("id"));
	                map.put("person_no",rs.getString("person_no"));
					map.put("person_name",rs.getString("person_name"));
					map.put("sex",rs.getString("sex"));
					map.put("email",rs.getString("email"));
					return map;
	            }
	        });*/		
		
	    //查询已分配账号的用户code
	    HashMap userCodeMap = new HashMap();
    	strsql = "SELECT  B.CODE code FROM AU_USER A INNER JOIN AU_PARTYRELATION B on A.PARTY_ID=B.PARTYID  WHERE b.relationtype_id = '1099100400000000001' ";
    	List lRole = ProjTools.getCommonBsInstance().doQuery(strsql, new RowMapper() {
            public Object mapRow(ResultSet rs, int no) throws SQLException {
				return rs.getString("code");
            }
        });
        if(lRole!=null && lRole.size()>0) {
	    	for(Iterator it = lRole.iterator(); it.hasNext(); ) {
	    		userCodeMap.put((String)it.next(),null);
	    	}
    	}    	

		for(Iterator it = lParty.iterator(); it.hasNext(); ) {
			//获取节点的数据
			HashMap m_Party = (HashMap) it.next();
			String id = (String)m_Party.get("id");
			String name = (String)m_Party.get("name");
			String code = (String)m_Party.get("code");
			String partyId = (String)m_Party.get("partyid");
			String isLeaf = (String)m_Party.get("is_leaf");
			String partyTypeId = (String)m_Party.get("partytype_id");
			String email = (String)(m_Party.get("email") != null ? m_Party.get("email") : venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.No"));
			
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
				request.getContextPath()+"/jsp/authority/au/auuser/xmlData.jsp?parent_code="+code
				+"&submit_all=" + submit_all + "&return_type="+return_type+"&tree_level=" + tree_level +"&data_limit=1");
			
			//过滤已分配用户的人员
			if(userCodeMap.containsKey(code)) {
			    dtv.setText(dtv.getText() + "("+venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Allocated")+")");
				dtv.setIsDisable("1");
			}			
			
			//设置返回值
			if("id".equals(return_type)) {
				dtv.setReturnValue(id);
			}else if("party_id".equals(return_type)) {
				dtv.setReturnValue(partyId);
			}else if("code".equals(return_type)){
				dtv.setReturnValue(code);
			}
			
			//判断可带checkbox的节点
			if("0".equals(submit_all) || "no".equals(submit_all) || "false".equals(submit_all)) {
				if("1".equals(tree_level)) {
					dtv.setIsSubmit(isLeaf);//对只显示公司的树，仅叶子节点可以提交
				}else if("2".equals(tree_level)) { 
					if( ! partyTypeDept.equals(partyTypeId) ) {
						dtv.setIsSubmit("0");
					}
				}else if("3".equals(tree_level)) { 
					if( ! partyTypePosi.equals(partyTypeId) ) {
						dtv.setIsSubmit("0");
					}
				}else {
					if( ! partyTypeEmpl.equals(partyTypeId) ) {
						dtv.setIsSubmit("0");
					}
				}
			}			
			
			if((GlobalConstants.getPartyType_empl()).equals(partyTypeId)) { //只有人员节点才显示详细信息
			  /*String sex = "男";
				String email = "无";
				for(Iterator i = empList.iterator(); i.hasNext(); ) {
					//获取节点的数据
					HashMap empMap = (HashMap) i.next();
					if(partyId.equals(empMap.get("id")))
					    sex = (String)("1".equals(empMap.get("sex")) ? "男" : "女");
					    email = (String)(empMap.get("email") != null ? empMap.get("email") : "无");
					}*/
				dtv.setTitle(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Number")+":" + partyId + "\n"+venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Name0")+":" + name +  "\n"+venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Mailbox")+":" + email);
			} 
			
			//返回节点的团体类型
			dtv.setDetailedType(partyTypeId);

			//默认打开下一级
			dtv.setDefaultOpen("0");
			
			//判断是否有写的权限
			if(auth != 2) { 
				dtv.setIsSubmit("0");
			}
			dt.addTreeNode(dtv);
		} 
	}else {//没有数据权限
		DeepTreeVo dtv = new DeepTreeVo("", venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Sorry_you_do_not_have_permission_to_view_this_data"), "0" , "");
		dtv.setIsSubmit("0");
		dt.addTreeNode(dtv);
	}
    String xmlStr = dt.getStringFromDocument();
    if (xmlStr.indexOf("TreeNode") == -1) { //没有组织机构数据时的提示
        xmlStr = "<Trees><TreeNode id=''  text= '" + venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Can_not_find_the_data_") + "' hasChild= '0' isSubmit='0' /></Trees>";
    }    
    out.print(xmlStr);
} catch(Exception e) {
	e.printStackTrace();
}
%>

