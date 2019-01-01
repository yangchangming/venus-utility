/*
 * 系统名称:PlatForm
 * 
 * 文件名称: venus.authority.helper --> OrgHelper.java
 * 
 * 功能描述:
 * 
 * 版本历史: 2005-6-28 13:04:51 创建1.0.0版 (ganshuo)
 *  
 */
package venus.oa.helper;

import venus.oa.organization.auparty.bs.IAuPartyBs;
import venus.oa.organization.auparty.vo.PartyVo;
import venus.oa.organization.aupartyrelation.bs.IAuPartyRelationBs;
import venus.oa.organization.aupartyrelation.vo.AuPartyRelationVo;
import venus.oa.organization.aupartyrelationtype.bs.IAuPartyRelationTypeBS;
import venus.oa.organization.aupartyrelationtype.util.IConstants;
import venus.oa.organization.aupartytype.bs.IAuPartyTypeBS;
import venus.oa.util.GlobalConstants;
import venus.oa.util.tree.DeepTreeSearch;
import venus.frames.base.exception.BaseApplicationException;
import venus.frames.mainframe.util.Helper;

import java.util.*;

/**
 * 功能、用途、现存BUG:
 * 
 * @author 甘硕
 * @version 1.0.0
 * @see 需要参见的其它类
 * @since 1.0.0
 */
public class OrgHelper {

    /**
     * 构造函数:
     *  
     */
    public OrgHelper() {
        
    }
    /**
     *根据团体id获取其团体关系编号
     * @param partyId 团体id
     * @return
     */
    public static String[] getRelationCode(String partyId) {

	    IAuPartyRelationBs relBs = (IAuPartyRelationBs) Helper.getBean(venus.oa.organization.aupartyrelation.util.IConstants.BS_KEY);
	    AuPartyRelationVo queryVo = new AuPartyRelationVo();
	    queryVo.setPartyid(partyId);
	    //queryVo.setRelationtype_id(GlobalConstants.getRelaType_comp());//2011-05-20 扩展关系范围，通过阅读代码没有发现这次注释会引起问题，需要项目组反馈。
	    List rel = relBs.queryAuPartyRelation(queryVo);

		if(rel==null || rel.size()==0) {
		    return null;//如果没有所属关系
		}else {
			ArrayList codes = new ArrayList();
			for(int i = 0; i < rel.size(); i++) {
	            AuPartyRelationVo relVo = (AuPartyRelationVo)rel.get(i);
	            codes.add(relVo.getCode());
	        }
	        return (String[]) codes.toArray(new String[0]);
		}
	}
    /**
     *根据团体id获取其上级团体的团体关系编号
     * @param partyId 团体id
     * @param partyType 上级团体类型id
     * @return
     */
    public static String[] getRelationCode(String partyId, String partyType) {

	    IAuPartyRelationBs relBs = (IAuPartyRelationBs) Helper.getBean(venus.oa.organization.aupartyrelation.util.IConstants.BS_KEY);
	    AuPartyRelationVo queryVo = new AuPartyRelationVo();
	    queryVo.setPartyid(partyId);
	    List rel = relBs.queryAuPartyRelation(queryVo);

		if(rel==null || rel.size()==0) {
		    return new String[]{GlobalConstants.getRelaType_comp()+"00001"};//如果没有所属关系,返回根节点编号
		}else {
			ArrayList codes = new ArrayList();
			for(int i = 0; i < rel.size(); i++) {
	            AuPartyRelationVo relVo = (AuPartyRelationVo)rel.get(i);
	            List pRel = relBs.queryParentRelation(relVo.getParent_code());
	            for(int j=pRel.size()-1; j>=0; j--) {
			        AuPartyRelationVo pRelVo = (AuPartyRelationVo)pRel.get(j);
			        if(partyType.equals(pRelVo.getPartytype_id())) {
			            codes.add(pRelVo.getCode());
			            break;
			        }
			    }
	        }
	        return (String[]) codes.toArray(new String[0]);
		}
	}
    /**
     * 根据关系ID获得关系Code
     * @param relationId
     * @return
     */
    public static String getRelationCodeByRelationId(String relationId) {
	    IAuPartyRelationBs relBs = (IAuPartyRelationBs) Helper.getBean(venus.oa.organization.aupartyrelation.util.IConstants.BS_KEY);
	    AuPartyRelationVo queryVo = new AuPartyRelationVo();
	    queryVo.setId(relationId);    	
	    List rel = relBs.queryAuPartyRelation(queryVo);
	    if(rel==null || rel.size()==0) {
		    return null; //找不到所属关系
		}else {
			queryVo= (AuPartyRelationVo)rel.get(0);
			return queryVo.getCode();
		}	    
    }
    /**
     * 根据团体id获取其上级公司的团体关系编号
     * @param partyId
     * @return
     */
    public static String[] getCompanyCode(String partyId) {
	        return getRelationCode(partyId,GlobalConstants.getPartyType_comp());
	}
    /**
     * 根据团体id获取其上级部门的团体关系编号
     * @param partyId
     * @return
     */
    public static String[] getDepartmentCode(String partyId) {
        return getRelationCode(partyId,GlobalConstants.getPartyType_dept());
    }
    /**
     * 根据团体id获取其上级岗位的团体关系编号
     * @param partyId
     * @return
     */
    public static String[] getPositionCode(String partyId) {
        return getRelationCode(partyId,GlobalConstants.getPartyType_posi());
    }
    
    /**
     * 
     * 功能: 根据团体关系ID查询团体ID
     *
     * @param id
     * @return
     */
    public static String getPartyIDByRelationID(String id) {
        IAuPartyRelationBs relBs = (IAuPartyRelationBs) Helper.getBean(venus.oa.organization.aupartyrelation.util.IConstants.BS_KEY);
        AuPartyRelationVo vo = new AuPartyRelationVo();
        vo.setId(id);
        List list = relBs.queryAuPartyRelation(vo);
        if(list==null || list.size()==0) {
            throw new BaseApplicationException(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Finding_out_the_data_"));
        }
        return ((AuPartyRelationVo)list.get(0)).getPartyid();
    }
    /**
     * 
     * 功能: 根据上级组织id查询其下一级组织的列表
     *
     * @param id 团体关系表主键
     * @return
     */
    public static List getSubRelationListByID(String id) {
        IAuPartyRelationBs relBs = (IAuPartyRelationBs) Helper.getBean(venus.oa.organization.aupartyrelation.util.IConstants.BS_KEY);
        AuPartyRelationVo vo = new AuPartyRelationVo();
        vo.setId(id);
        List list = relBs.queryAuPartyRelation(vo);
        if(list==null || list.size()==0) {
            return new ArrayList();
        }
        String code = ((AuPartyRelationVo)list.get(0)).getCode();
        vo = new AuPartyRelationVo();
        vo.setParent_code(code);
        return relBs.queryAuPartyRelation(vo);
    }
    /**
     * 
     * 功能: 根据下级组织id查询上一级组织的vo
     *
     * @param id 团体关系表主键
     * @return
     */
    public static AuPartyRelationVo getUpRelationVoByID (String id) {
        IAuPartyRelationBs relBs = (IAuPartyRelationBs) Helper.getBean(venus.oa.organization.aupartyrelation.util.IConstants.BS_KEY);
        
        AuPartyRelationVo vo = new AuPartyRelationVo();
        vo.setId(id);
        List list = relBs.queryAuPartyRelation(vo);
        if(list==null || list.size()==0) {
            return null;
        }
        String parentCode = ((AuPartyRelationVo)list.get(0)).getParent_code();
       
        vo = new AuPartyRelationVo();
        vo.setCode(parentCode);
        list = relBs.queryAuPartyRelation(vo);
        if(list==null || list.size()==0) {
            return null;
        }
        return (AuPartyRelationVo)list.get(0);
    }
    /**
     * 
     * 功能: 根据上级组织编号查询其下一级组织的列表
     *
     * @param code 团体关系编号
     * @return
     */
    public static List getSubRelationListByCode(String code) {
        IAuPartyRelationBs relBs = (IAuPartyRelationBs) Helper.getBean(venus.oa.organization.aupartyrelation.util.IConstants.BS_KEY);
        AuPartyRelationVo vo = new AuPartyRelationVo();
        vo.setParent_code(code);
        return relBs.queryAuPartyRelation(vo);
    }
    /**
     * 
     * 功能: 根据下级组织编号查询上一级组织的vo
     *
     * @param id 团体关系编号
     * @return
     */
    public static AuPartyRelationVo getUpRelationVoByCode (String code) {
        IAuPartyRelationBs relBs = (IAuPartyRelationBs) Helper.getBean(venus.oa.organization.aupartyrelation.util.IConstants.BS_KEY);
        
        AuPartyRelationVo vo = new AuPartyRelationVo();
        vo.setCode(code);
        List list = relBs.queryAuPartyRelation(vo);
        if(list==null || list.size()==0) {
            return null;
        }
        String parentCode = ((AuPartyRelationVo)list.get(0)).getParent_code();
       
        vo = new AuPartyRelationVo();
        vo.setCode(parentCode);
        list = relBs.queryAuPartyRelation(vo);
        if(list==null || list.size()==0) {
            return null;
        }
        return (AuPartyRelationVo)list.get(0);
    }
    /**
     * 
     * 功能: 根据团体主键ID获得该团体的Vo
     *
     * @param partyId 团体主键ID
     * @return
     */
    public static PartyVo getPartyVoByID (String partyId) {
        IAuPartyBs bs = (IAuPartyBs) Helper.getBean(venus.oa.organization.auparty.util.IConstants.BS_KEY);
        return (PartyVo)bs.find(partyId);
    }
    /**
     * 
     * 功能: 根据团体关系编号获得相应团体的Vo
     *
     * @param code 团体关系编号
     * @return
     */
    public static PartyVo getPartyVoByCode (String code) {
        IAuPartyRelationBs relBs = (IAuPartyRelationBs) Helper.getBean(venus.oa.organization.aupartyrelation.util.IConstants.BS_KEY);
        IAuPartyBs ptyBs = (IAuPartyBs) Helper.getBean(venus.oa.organization.auparty.util.IConstants.BS_KEY);
        
        AuPartyRelationVo vo = new AuPartyRelationVo();
        vo.setCode(code);
        List list = relBs.queryAuPartyRelation(vo);
        if(list==null || list.size()==0) {
            return null;
        }
        String partyId = ((AuPartyRelationVo)list.get(0)).getPartyid();
        return (PartyVo)ptyBs.find(partyId);
    }
    /**
     * 
     * 功能: 根据团体主键ID列表获得相应名称列表
     *
     * @param lPartyId 团体id列表
     * @return
     */
    public static Map getNameMapByIDList(ArrayList lPartyId) {
        IAuPartyBs bs = (IAuPartyBs) Helper.getBean(venus.oa.organization.auparty.util.IConstants.BS_KEY);
        return bs.getNameMapByKey(lPartyId);
    }
    /**
     * 
     * 功能: 根据团体关系编号列表获得相应名称列表
     *
     * @param lCode 团体关系编号列表
     * @return
     */
    public static Map getNameMapByCodeList(ArrayList lCode) {
        IAuPartyRelationBs bs = (IAuPartyRelationBs) Helper.getBean(venus.oa.organization.aupartyrelation.util.IConstants.BS_KEY);
        return bs.getNameMapByCode(lCode);
    }
    /**
     * 功能: 查询当前节点的所有上级节点，一直到根节点
     * @param code
     * @return
     */    
    public static List getParentRelation(String code) {
    	IAuPartyRelationBs bs = (IAuPartyRelationBs) Helper.getBean(venus.oa.organization.aupartyrelation.util.IConstants.BS_KEY);
    	return bs.getParentRelation(code);
    }
    /**
     * 
     * 功能: 获取团体类型列表(仅状态为启用的)
     *
     * @return
     */
    public static List getPartyTypeList() {
        IAuPartyTypeBS bs = (IAuPartyTypeBS) Helper.getBean(
                venus.oa.organization.aupartytype.util.IConstants.BS_KEY );
        return bs.queryAllEnable(-1,-1,null);
    }
    /**
     * 
     * 功能: 获取团体关系类型列表(仅状态为启用的)
     *
     * @return
     */
    public static List getPartyRelationTypeList() {
        IAuPartyRelationTypeBS bs = (IAuPartyRelationTypeBS) Helper.getBean(
                IConstants.BS_KEY );
        return bs.queryAllEnable(-1,-1,null);
    }
   
    
    /**
     * 新增团体
     * PartyVo需要设置以下属性：
     * setPartyType_id(String partyType_id) ——团体类型表的主键ID
     * setIs_inherit(String is_inherit)——【可选】是否继承权限 0不继承权限，1继承权限 默认请设置为1 ，也可以为空
     * setIs_real(String is_real)——【可选】是否真实团体 0虚拟团体，1真实团体 默认请设置为1 ，也可以为空
     * setName(String name)——团体名称
     * setEmail(String email)——团体EMAIL（对员工类型为必填）		
     * setEnable_status(String status)——启用/禁用状态	0禁用,1启用	 
     * 
     * @param vo
     * @return 如果添加成功则返回新添加的团体主键ID，如果添加失败则返回null
     */
    public static String addParty(PartyVo vo) {
        IAuPartyBs bs = (IAuPartyBs) Helper.getBean(venus.oa.organization.auparty.util.IConstants.BS_KEY);
        return bs.addParty(vo);
    }

    /**
     * 修改团体
     * PartyVo可以设置以下属性：
     * setParty_id(String id)——团体主键ID （必须设置）
     * setIs_inherit(String is_inherit)——【可选】是否继承权限 0不继承权限,1继承权限	
     * setName(String name)——团体名称
     * setEmail(String email)——团体EMAIL（对员工类型为必填）	
     * @param vo
     * @return 如果修改成功则返回true，否则返回false
     */
    public static boolean updateParty(PartyVo vo) {
        IAuPartyBs bs = (IAuPartyBs) Helper.getBean(venus.oa.organization.auparty.util.IConstants.BS_KEY);
        return bs.updateParty(vo);
    }

    /**
     * 删除团体，同时级联删除与其相关的团体关系、账号以及权限的数据 
     * 
     * @param partyId
     * @return
     */
    public static boolean deleteParty(String partyId) {
        IAuPartyBs bs = (IAuPartyBs) Helper.getBean(venus.oa.organization.auparty.util.IConstants.BS_KEY);
        return bs.delete(partyId);
    }

    /**
     * 
     * 功能: 添加团体关系根节点
     *
     * @param partyId 团体主键
     * @param partyRelationTypeId 团体关系类型主键
     * @return
     */
    public static boolean addRoot(String partyId, String partyRelationTypeId) {
        IAuPartyRelationBs bs = (IAuPartyRelationBs) Helper.getBean(venus.oa.organization.aupartyrelation.util.IConstants.BS_KEY);
        return bs.initRoot(partyId, partyRelationTypeId);
    }
    
    /**
     * 添加新的团体关系
     * @param childPartyId 子团体id
     * @param parentRelId 父团体关系id
     * @param relTypeId 团体关系类型id
     * @return 如果添加失败或该团体关系已经存在则返回false，否则返回true
     */
    public static boolean addPartyRelation(String childPartyId, String parentRelId, String relTypeId) {
        IAuPartyRelationBs bs = (IAuPartyRelationBs) Helper.getBean(venus.oa.organization.aupartyrelation.util.IConstants.BS_KEY);
        return null==bs.addPartyRelation(childPartyId, parentRelId, relTypeId)?false:true;
    }

    /**
     * 删除团体关系
     * @param id 团体关系id
     * @return 如果删除成功则返回true，否则返回false
     */
    public static boolean deletePartyRelation(String id) {
        IAuPartyRelationBs bs = (IAuPartyRelationBs) Helper.getBean(venus.oa.organization.aupartyrelation.util.IConstants.BS_KEY);
        return bs.deletePartyRelation(id);
    }
    
	/**
	 * 通过partyId获得上级所有的机构名称(默认只显示行政关系)
	 * @param partyId
	 * @return 返回上级所有的关系名称并用→隔开
	 */	
	public static String getOrgNameById(String partyId) {
		return DeepTreeSearch.getOrgNameById(partyId);
	}
	
	/**
	 * 通过partyId获得上级所有的机构名称(默认只显示行政关系)
	 * @param partyId
	 * @param showUserYn 是否显示最后一级的用户
	 * @return 返回上级所有的关系名称并用→隔开
	 */
	public static String getOrgNameById(String partyId,boolean showUserYn) {
		return DeepTreeSearch.getOrgNameById(partyId,showUserYn);
	}
	
	/**
	 * 通过partyId获得上级所有的机构名称
	 * @param partyId
	 * @param showUserYn 是否显示最后一级的用户
	 * @param relationTypeId 机构树关系类型ID
	 * @return 返回上级所有的关系名称并用→隔开
	 */
	public static String getOrgNameById(String partyId,boolean showUserYn,String relationTypeId) {
		return DeepTreeSearch.getOrgNameById(partyId,showUserYn,relationTypeId);
	}
	
	/**
	 * 通过code获得上级所有的机构名称
	 * @param code
	 * @return 返回上级所有的关系名称并用→隔开
	 */		
	public static String getOrgNameByCode(String code) {
		return DeepTreeSearch.getOrgNameByCode(code);
	}
	
	/**
	 * 通过code获得上级所有的机构名称
	 * @param code
	 * @param showUserYn 是否显示最后一级的用户
	 * @return 返回上级所有的关系名称并用→隔开
	 */	
	public static String getOrgNameByCode(String code,boolean showUserYn) {
		return DeepTreeSearch.getOrgNameByCode(code,showUserYn);
	}	
	
	/**
	 * 根据关系code获得此关系所属团体的id
	 * @param code
	 * @return String
	 */
	public static String getPartyIdByCode(String code){
	    IAuPartyRelationBs relBs = (IAuPartyRelationBs) Helper.getBean(venus.oa.organization.aupartyrelation.util.IConstants.BS_KEY);
        AuPartyRelationVo vo = new AuPartyRelationVo();
        vo.setCode(code);
        List<AuPartyRelationVo> list = relBs.queryAuPartyRelation(vo);
        if(list==null || list.size()==0) {
            return null;
        }
        String partyId = list.get(0).getPartyid();  
	    return partyId;
	}
	
	/**
	 * 根据给定的code返回所属团体的Id,如果多关系code对应同一partyid则返回一个(已经出去了重复的partyId)
	 * @param codes
	 * @return String[]
	 */
	public static String[] getPartyIdsByCodes(List<String> codes){
	    Set<String> result =new HashSet<String>();
	    for(String code:codes){
	        String partyId=getPartyIdByCode(code);
	        result.add(partyId);
	    }
	    int size=result.size();
	  String[] partyIds = result.toArray(new String[size]);
	    return partyIds;
	}
}

