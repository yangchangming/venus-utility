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

	//参数：是否全部节点都带checkbox，0否，1是
	String submit_all = request.getParameter("submit_all");
	if(submit_all == null || submit_all.length()==0) {
		submit_all = "0";
	}

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
		dataLimit = LoginHelper.getOwnerOrgWithoutHistory(request);//取数据权限
	}
	DeepTreeXmlHandler dt = new DeepTreeXmlHandler();
	if(dataLimit!=null && dataLimit.length>0) {//有数据权限
		//从数据库取数据
		String strsql = "select * from au_partyrelation where parent_code='"+parent_code+"' ";

		String partyTypeComp = GlobalConstants.getPartyType_comp();//团体类型－公司
		String partyTypeDept = GlobalConstants.getPartyType_dept();//团体类型－部门
		String partyTypePosi = GlobalConstants.getPartyType_posi();//团体类型－岗位
		String partyTypeEmpl = GlobalConstants.getPartyType_empl();//团体类型－员工
		if("1".equals(tree_level)) {
			strsql += "and partytype_id='"+partyTypeComp+"' ";//查询到公司
		}else if("2".equals(tree_level)) {
			strsql += "and partytype_id in('"+partyTypeComp+"','"+partyTypeDept+"') ";//查询到部门
		}else if("3".equals(tree_level)) {
			strsql += "and partytype_id in('"+partyTypeComp+"','"+partyTypeDept+"','"+partyTypePosi+"') ";//查询到岗位
		}

		strsql += "order by order_code";
		//将partyrelation表的数据以Map形式存放到List中。
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
		//从将List中数据name选项按中文排序。（打开下面注释即可）
		/*
		Object[] objs=lParty.toArray();
		Arrays.sort(objs,new PinYinTools().new OrganizeComparator());
		lParty=Arrays.asList(objs);
		*/
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
			String partyTypeId = (String)m_Party.get("partytype_id");

			if("-3".equals(tree_level) && partyTypePosi.equals(partyTypeId) ) { //如果是去除岗位
				//从数据库取数据
//				String strsql_3 = "select * from au_partyrelation where parent_code like '"+parentCode+"%' and partytype_id='"+partyTypeEmpl+"' order by order_code";
                String strsql_3 = "select * from au_partyrelation where parent_code = '"+code+"' and partytype_id='"+partyTypeEmpl+"' order by order_code";

                List lParty_3 = ProjTools.getCommonBsInstance().doQuery(strsql_3, new RowMapper() {
			            public Object mapRow(ResultSet rs, int no) throws SQLException {
			                HashMap map = new HashMap();
			                map.put("id",rs.getString("id"));
							map.put("name",rs.getString("name"));
							map.put("code",rs.getString("code"));
							map.put("partyid",rs.getString("partyid"));
							map.put("partytype_id",rs.getString("partytype_id"));
							return map;
			            }
			        });
				for(Iterator it_3 = lParty_3.iterator(); it_3.hasNext(); ) {
					HashMap m_Party_3 = (HashMap) it_3.next();
					String code_3 = (String)m_Party_3.get("code");

					//判断节点的数据权限(假设数据权限控制到人员)
					int auth_3 = 0; //表示权限的类型：0无权限，1只读，2可写
					for(int i=0; i<dataLimit.length; i++) {
						if(code_3.length()>dataLimit[i].length()) { //当前节点为授权节点的下级节点

                            if(LoginHelper.getIsAdmin(request)){//判断是否为超级管理员,是的话,赋予可写权限
                                auth_3 = 2;
                            }else if("1".equals(data_limit)){ //数据权限受控
                                continue;
                            }

							if(code_3.substring(0,dataLimit[i].length()).equals(dataLimit[i])) {
								auth_3 = 2;
								break;
							}
						}
						else if(code_3.length()==dataLimit[i].length()){ //当前节点为授权节点的同级节点
							if(code_3.equals(dataLimit[i])) {
								auth_3 = 2;
								break;
							}
						}else { //当前节点为授权节点的上级节点
							if(code_3.equals(dataLimit[i].substring(0,code_3.length()))) {
								auth_3 = 1;
								continue;
							}
						}
					}
					if(auth_3==0) {
						continue; //没有权限，直接跳过该节点的构造
					}

					//构造节点
					DeepTreeVo dtv = new DeepTreeVo(code_3, (String)m_Party_3.get("name"), "0", "");

					//设置返回值
					if("id".equals(return_type)) {
						dtv.setReturnValue((String)m_Party_3.get("id"));
					}else if("party_id".equals(return_type)) {
						dtv.setReturnValue((String)m_Party_3.get("partyid"));
					}else if("code".equals(return_type)){
						dtv.setReturnValue(code_3);
					}

					//返回节点的团体类型
					dtv.setDetailedType((String)m_Party_3.get("partytype_id"));

					//判断是否有写的权限
					if(auth_3 != 2) {
						dtv.setIsSubmit("0");
					}
					dt.addTreeNode(dtv);
				}
				continue;//继续最外层的for循环
			}

			//判断节点的数据权限
			int auth = 0; //表示权限的类型：0无权限，1只读，2可写
			for(int i=0; i<dataLimit.length; i++) {
				if(code.length()>dataLimit[i].length()) { //当前节点为授权节点的下级节点

                    if(LoginHelper.getIsAdmin(request)){//判断是否为超级管理员,是的话,赋予可写权限
                        auth = 2;
                    }if("1".equals(data_limit)){ //数据权限受控
                        continue;
                    }

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

			//判断叶子节点
			if("1".equals(tree_level)) {
				isLeaf = typeIsLeaf;
			}else if("2".equals(tree_level)) {
				if( partyTypeDept.equals(partyTypeId) ) {
					isLeaf = typeIsLeaf;
				}
			}else if("3".equals(tree_level)) {
				if( partyTypePosi.equals(partyTypeId) ) {
					isLeaf = typeIsLeaf;
				}
			}
			//构造节点
			DeepTreeVo dtv = new DeepTreeVo(code, name, "1".equals(isLeaf) ? "0" : "1",
				request.getContextPath()+"/jsp/authority/tree/orgTree.jsp?parent_code="+code
				+"&submit_all="+submit_all+"&return_type="+return_type+"&tree_level="+tree_level+"&data_limit="+data_limit);

			//设置返回值
			if("id".equals(return_type)) {
				dtv.setReturnValue(id);
			}else if("party_id".equals(return_type)) {
				dtv.setReturnValue(partyId);
			}else if("code".equals(return_type)){
				dtv.setReturnValue(code);
			}

			//设置节点提示信息,如果不设置此项,默认是节点名称
			//dtv.setTitle(partyId);

			//返回节点的团体类型
			dtv.setDetailedType(partyTypeId);

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
    out.print(xmlStr);
} catch(Exception e) {
	e.printStackTrace();
}
%>

