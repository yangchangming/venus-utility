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
<%@ page import="venus.frames.mainframe.util.Helper"%>
<%@ page import="venus.authority.org.aupartytype.bs.IAuPartyTypeBS"%>
<%@ page import="venus.authority.org.aupartytype.vo.AuPartyTypeVo"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="venus.authority.org.aupartytype.util.IConstants" %>
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
	
	//参数：控制树能展示到的层次，根据partytypeid来控制团体类型（所有需要显示的partytypeid用半角逗号分开），默认为0，即不过滤团体类型
	String tree_level = request.getParameter("tree_level");
	if(tree_level == null || tree_level.length()==0) {
		tree_level = "0";
	}
	
	//参数：是否控制数据权限,0 否，1 是
	String data_limit = request.getParameter("data_limit");
	if(data_limit == null || data_limit.length()==0) {
		data_limit = "0";
	}
	
	//参数：是否显示的层级数目，默认-1为全显示
    String hierarchy = request.getParameter("hierarchy");
    if(hierarchy == null || hierarchy.length()==0) {
        hierarchy = "-1";
    }
    
    //参数：控制某些属性的数据被显示，默认为不控制
    String attributesFilter = request.getParameter("attributesFilter");
    if(attributesFilter == null || attributesFilter.length()==0) {
        attributesFilter = "{}";
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
		String strsql = "select * from au_partyrelation where parent_code like '"+parent_code+"%' ";
		if(!"0".equals(tree_level)){
			String levels[] = tree_level.split(",");
			strsql += "and partytype_id in(";
			for(int i=0;i<levels.length;i++){
			    strsql += ("'"+levels[i]+"'");
			    if(i!=levels.length-1){
			        strsql +=",";
			    }
	 		}
			strsql +=")";
		}
		
		if(!"-1".equals(hierarchy)){
		    strsql += ("and type_level <="+hierarchy);
		}
		strsql += " order by order_code";
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
					map.put("type_level",rs.getString("type_level"));
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
			String type_level = (String)m_Party.get("type_level");
			String isLeaf = (String)m_Party.get("is_leaf");
			//String typeIsLeaf = (String)m_Party.get("type_is_leaf");
			String partyTypeId = (String)m_Party.get("partytype_id");
			
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
			
			//判断叶子节点			
			if(!"0".equals(tree_level)) {
			    String descendantSql = "select count(1) from au_partyrelation where parent_code = '"+code+"' ";
			    String levels[] = tree_level.split(",");
			    descendantSql += "and partytype_id in(";
			    for(int k=0;k<levels.length;k++){
			        descendantSql += ("'"+levels[k]+"'");
	                if(k!=levels.length-1){
	                    descendantSql +=",";
	                }
	            }
			    descendantSql +=")";
			    int descendantNum = ProjTools.getCommonBsInstance().doQueryForInt(descendantSql);
			    isLeaf = descendantNum>0?"0":"1";
			}
			if(!"-1".equals(hierarchy)){
                if(type_level.equals(hierarchy)){
                    isLeaf = "1";
                }
            }
			//构造节点
			DeepTreeVo dtv = new DeepTreeVo(code, name, "1".equals(isLeaf) ? "0" : "1", 
				request.getContextPath()+"/jsp/authority/tree/orgChooseTree.jsp?parent_code="+code
				+"&submit_all="+submit_all+"&return_type="+return_type+"&tree_level="+tree_level+"&data_limit="+data_limit+"&hierarchy="+hierarchy+"&attributesFilter="+attributesFilter);
			
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
			    dtv.setIsSubmit(isLeaf);//仅叶子节点可以提交				
			}
			//判断属性是否符合过滤条件，如果被过滤，则不再允许操作
            if(!"{}".equals(attributesFilter)){
                String tableName = ((AuPartyTypeVo)((IAuPartyTypeBS)Helper.getBean(IConstants.BS_KEY)).find(partyTypeId)).getTable_name();
                if(StringUtils.isNotEmpty(tableName)){
                    final String filters = attributesFilter.replace("{","").replace("}","");                    
                    Boolean shoot = (Boolean)ProjTools.getCommonBsInstance().doQueryForObject("select * from "+tableName+" where id='"+partyId+"' ",new RowMapper() {
                        public Object mapRow(ResultSet rs, int no) throws SQLException {
                            String attrifilter[] = filters.split(",");                          
                            for(String afilter:attrifilter){
                                String keyValue[] = afilter.split(":");
                                if(!keyValue[1].equals(rs.getString(keyValue[0])))
                                    return false;//不符合
                            }
                            return true;//全部符合
                        }
                    });
                    if(!shoot){
                        dtv.setIsSubmit("0");//存在不符合的情况
                    }
                }else{
                    dtv.setIsSubmit("0");//强制验证，不能不存在
                }               
            }
			
			//默认打开下一级
			dtv.setDefaultOpen("0");
			
			//判断是否有写的权限
			if(auth != 2) { 
				dtv.setIsSubmit("0");
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

