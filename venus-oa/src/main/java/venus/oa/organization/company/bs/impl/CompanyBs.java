package venus.oa.organization.company.bs.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import venus.oa.helper.OrgHelper;
import venus.oa.organization.auparty.vo.PartyVo;
import venus.oa.organization.company.bs.ICompanyBs;
import venus.oa.organization.company.dao.ICompanyDao;
import venus.oa.organization.company.util.ICompanyConstants;
import venus.oa.organization.company.vo.CompanyVo;
import venus.oa.util.GlobalConstants;
import venus.frames.base.bs.BaseBusinessService;

import java.util.List;

@Service
public class CompanyBs extends BaseBusinessService implements ICompanyBs, ICompanyConstants {

    @Autowired
    private ICompanyDao companyDao;

    /**
     * 
     * 功能: 添加新记录，同时添加团体、团体关系（如果parentRelId为空则不添加团体关系）
     *
     * @param vo 用于添加的VO对象
     * @param parentRelId 上级节点团体关系主键
     * @return 若添加成功，则返回新添加记录的主键
     */
    public String insert(CompanyVo vo, String parentRelId) {
        //添加新的团体
        PartyVo partyVo = new PartyVo();
        partyVo.setPartytype_id(GlobalConstants.getPartyType_comp());//团体类型表的主键ID
        partyVo.setName(vo.getCompany_name());//团体名称
        partyVo.setEmail(vo.getEmail());//团体EMAIL（对员工类型为必填）	
        partyVo.setRemark(vo.getRemark());//备注	
        partyVo.setEnable_status(vo.getEnable_status());//启用/禁用状态	0禁用,1启用
        partyVo.setCreate_date(vo.getCreate_date());//创建时间
        String partyId = OrgHelper.addParty(partyVo);//调用接口添加团体
        //添加团体关系
        if(parentRelId!=null && !"".equals(parentRelId) && !"null".equals(parentRelId)) {
            String relType = GlobalConstants.getRelaType_comp();//团体关系类型－行政关系
            OrgHelper.addPartyRelation(partyId, parentRelId, relType);//调用接口添加团体关系
        }
        //添加公司
        vo.setId(partyId);
        if(vo.getCompany_no()==null || vo.getCompany_no().length()==0) {//如果用户不手工编号，则系统自动编号
            vo.setCompany_no(partyId);
        }
		companyDao.insert(vo);
        //RmLogHelper.log(TABLE_LOG_TYPE_NAME, "插入了1条记录,id=" + String.valueOf(oid));
		return partyId;
    }
    /**
     * 
     * 功能: 添加新记录，同时添加团体、团体关系根节点
     *
     * @param vo 用于添加的VO对象
     * @return
     */
    public String insertRoot(CompanyVo vo) {
        //添加新的团体
        PartyVo partyVo = new PartyVo();
        partyVo.setPartytype_id(GlobalConstants.getPartyType_comp());//团体类型表的主键ID
        partyVo.setName(vo.getCompany_name());//团体名称
        partyVo.setEmail(vo.getEmail());//团体EMAIL（对员工类型为必填）	
        partyVo.setRemark(vo.getRemark());//备注	
        partyVo.setEnable_status(vo.getEnable_status());//启用/禁用状态	0禁用,1启用
        partyVo.setCreate_date(vo.getCreate_date());//创建时间
        String partyId = OrgHelper.addParty(partyVo);//调用接口添加团体
        //添加团体关系根节点
        String relType = GlobalConstants.getRelaType_comp();//团体关系类型－行政关系
        OrgHelper.addRoot(partyId, relType);//调用接口添加团体关系
        
        //添加公司
        vo.setId(partyId);
        if(vo.getCompany_no()==null || vo.getCompany_no().length()==0) {//如果用户不手工编号，则系统自动编号
            vo.setCompany_no(partyId);
        }
	companyDao.insert(vo);
        //RmLogHelper.log(TABLE_LOG_TYPE_NAME, "插入了1条记录,id=" + String.valueOf(oid));
		return partyId;
    }

    /**
     * 删除单条记录，同时删除团体关系及相关的权限记录
     * 
     * @param id 用于删除的记录的id
     * @return 成功删除的记录数
     */
    public int delete(String partyRelationId) {
    	String partyid = OrgHelper.getPartyIDByRelationID(partyRelationId);
    	int n = companyDao.delete(partyid);
    	OrgHelper.deleteParty(partyid);//调用接口删除相应的团体、团体关系、帐户、权限等记录
		return n;
    }

    /**
     * 删除多条记录，删除自身并同时删除相应的团体、团体关系、帐户、权限等记录
     * 
     * @param id 用于删除的记录的id
     * @return 成功删除的记录数
     */
    public int delete(String id[]) {
		int sum = companyDao.delete(id);
		for(int i=0; i<id.length; i++) {
		    OrgHelper.deleteParty(id[i]);//调用接口删除相应的团体、团体关系、帐户、权限等记录
		}
        //RmLogHelper.log(TABLE_LOG_TYPE_NAME, "删除了" + sum + "条记录,id=" + String.valueOf(id));
		return sum;
    }

    /**
     * 根据Id进行查询
     * 
     * @param id 用于查找的id
     * @return 查询到的VO对象
     */
    public CompanyVo find(String id) {
		CompanyVo vo = companyDao.find(id);
        //RmLogHelper.log(TABLE_LOG_TYPE_NAME, "察看了1条记录,id=" + id);
		return vo;
    }

    /**
     * 更新单条记录，同时调用接口更新相应的团体、团体关系记录
     * 
     * @param vo 用于更新的VO对象
     * @return 成功更新的记录数
     */
    public int update(CompanyVo vo) {
        /**
         * 修改团体
         * PartyVo可以设置以下属性：
         * setId(String id)——团体主键ID （必须设置）
         * setIs_inherit(String is_inherit)——【可选】是否继承权限 0不继承权限,1继承权限	
         * setName(String name)——团体名称
         * setEmail(String email)——团体EMAIL（对员工类型为必填）
         * setRemark(String remark)——备注
         */
        PartyVo partyVo = new PartyVo();
        partyVo.setId(vo.getId());//团体主键
        partyVo.setName(vo.getCompany_name());//团体名称
        partyVo.setEmail(vo.getEmail());//团体EMAIL（对员工类型为必填）
        partyVo.setRemark(vo.getRemark());//备注	
        OrgHelper.updateParty(partyVo);//调用接口进行更新
        
		int sum = companyDao.update(vo);
        //RmLogHelper.log(TABLE_LOG_TYPE_NAME, "更新了" + sum + "条记录,id=" + String.valueOf(vo.getId()));
		return sum;
    }

    /**
     * 查询所有的VO对象列表，不翻页
     * 
     * @return 查询到的VO列表
     */
    public List queryAll() {
		List lResult = companyDao.queryAll();
        //RmLogHelper.log(TABLE_LOG_TYPE_NAME, "查询了多条记录,recordSum=" + lResult.size() + ", cmd=queryAll()");
		return lResult;
    }
    
    /**
     * 查询所有的VO对象列表，不翻页，带排序字符
     * 
     * @param orderStr 排序字符
     * @return 查询到的VO列表
     */
    public List queryAll(String orderStr) {
		List lResult = companyDao.queryAll(orderStr);
		//RmLogHelper.log(TABLE_LOG_TYPE_NAME, "查询了多条记录,recordSum=" + lResult.size() + ", cmd=queryAll(" + orderStr + ")");
		return lResult;
    }

    /**
     * 查询所有的VO对象列表，带翻页
     * 
     * @param no 当前页数
     * @param size 每页记录数
     * @return 查询到的VO列表
     */
    public List queryAll(int no, int size) {
		List lResult = companyDao.queryAll(no, size);
		//RmLogHelper.log(TABLE_LOG_TYPE_NAME, "查询了多条记录,recordSum=" + lResult.size() + ",cmd=queryAll(" + no + ", " + size + ")");
		return lResult;
    }

    /**
     * 查询所有的VO对象列表，带翻页，带排序字符
     * 
     * @param no 当前页数
     * @param size 每页记录数
     * @param orderStr 排序字符
     * @return 查询到的VO列表
     */
    public List queryAll(int no, int size, String orderStr) {
		List lResult = companyDao.queryAll(no, size, orderStr);
		//RmLogHelper.log(TABLE_LOG_TYPE_NAME, "查询了多条记录,recordSum=" + lResult.size() + ", cmd=queryAll(" + no + ", " + size + ", " + orderStr + ")");
		return lResult;
    }

    /**
     * 查询总记录数
     * 
     * @return 总记录数
     */
    public int getRecordCount() {
        int sum = companyDao.getRecordCount();
        //RmLogHelper.log(TABLE_LOG_TYPE_NAME, "查询到了总记录数,sum=" + sum);
        return sum;
    }

    /**
     * 查询总记录数，带查询条件
     * 
     * @param queryCondition 查询条件
     * @return 总记录数
     */
    public int getRecordCount(String queryCondition) {
		int sum = companyDao.getRecordCount(queryCondition);
		//RmLogHelper.log(TABLE_LOG_TYPE_NAME, "查询到了总记录数,sum=" + sum + ", queryCondition=" + queryCondition);
		return sum;
    }
    
    /**
     * 通过查询条件获得所有的VO对象列表，不带翻页查全部
     *
     * @param queryCondition 查询条件
     * @return 查询到的VO列表
     */
    public List queryByCondition(String queryCondition) {
		List lResult = companyDao.queryByCondition(queryCondition);
        //RmLogHelper.log(TABLE_LOG_TYPE_NAME, "按条件查询了多条记录,recordSum=" + lResult.size() + ", queryCondition=" + queryCondition);
		return lResult;
    }
    
    /**
     * 通过查询条件获得所有的VO对象列表，不带翻页查全部，带排序字符
     *
     * @param queryCondition 查询条件
     * @param orderStr 排序字符
     * @return 查询到的VO列表
     */
    public List queryByCondition(String queryCondition, String orderStr) {
		List lResult = companyDao.queryByCondition(queryCondition, orderStr);
		//RmLogHelper.log(TABLE_LOG_TYPE_NAME, "按条件查询了多条记录,recordSum=" + lResult.size() + ", queryCondition=" + queryCondition + ", orderStr=" + orderStr);
		return lResult;
    }
    
    /**
     * 通过查询条件获得所有的VO对象列表，带翻页
     * 
     * @param no 当前页数
     * @param size 每页记录数
     * @param queryCondition 查询条件
     * @return 查询到的VO列表
     */
    public List queryByCondition(int no, int size, String queryCondition) {
		List lResult = companyDao.queryByCondition(no, size, queryCondition);
		//RmLogHelper.log(TABLE_LOG_TYPE_NAME, "按条件查询了多条记录,recordSum=" + lResult.size() + ", no=" + no + ", size=" + size + ", queryCondition=" + queryCondition);
		return lResult;
    }
    
    /**
     * 通过查询条件获得所有的VO对象列表，带翻页，带排序字符
     * 
     * @param no 当前页数
     * @param size 每页记录数
     * @param queryCondition 查询条件
     * @param orderStr 排序字符
     * @return 查询到的VO列表
     */
    public List queryByCondition(int no, int size, String queryCondition, String orderStr) {
		List lResult = companyDao.queryByCondition(no, size, queryCondition, orderStr);
		//RmLogHelper.log(TABLE_LOG_TYPE_NAME, "按条件查询了多条记录,recordSum=" + lResult.size() + ", no=" + no + ", size=" + size + ", queryCondition=" + queryCondition + ", orderStr=" + orderStr);
		return lResult;
    }

}

