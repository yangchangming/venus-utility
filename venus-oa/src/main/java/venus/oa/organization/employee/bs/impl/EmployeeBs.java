/*
 * 系统名称:
 * 
 * 文件名称: venus.authority.sample.employee.bs.impl --> EmployeeBs.java
 * 
 * 功能描述:
 * 
 * 版本历史: 2007-01-31 14:20:08.338 创建1.0.0版 (甘硕)
 *  
 */

package venus.oa.organization.employee.bs.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import venus.oa.helper.OrgHelper;
import venus.oa.organization.auparty.vo.PartyVo;
import venus.oa.organization.aupartyrelation.vo.AuPartyRelationVo;
import venus.oa.organization.employee.bs.IEmployeeBs;
import venus.oa.organization.employee.dao.IEmployeeDao;
import venus.oa.organization.employee.util.IEmployeeConstants;
import venus.oa.organization.employee.vo.EmployeeVo;
import venus.oa.util.GlobalConstants;
import venus.frames.base.bs.BaseBusinessService;

import java.util.List;

@Service
public class EmployeeBs extends BaseBusinessService implements IEmployeeBs, IEmployeeConstants {
    
    @Autowired
    private IEmployeeDao employeeDao;

    /**
     * 添加新记录，同时添加团体、团体关系（如果parentRelId为空则不添加团体关系）
     * 
     * @param vo 用于添加的VO对象
     * @param parentRelId 上级节点团体关系主键
     * @return 若添加成功，则返回新添加记录的主键
     */
    public String insert(EmployeeVo vo, String parentRelId) {
        //添加新的团体
        PartyVo partyVo = new PartyVo();
        partyVo.setPartytype_id(GlobalConstants.getPartyType_empl());//团体类型表的主键ID
        partyVo.setName(vo.getPerson_name());//团体名称
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
        //添加员工
        vo.setId(partyId);
        if(vo.getPerson_no()==null || vo.getPerson_no().length()==0) {//如果用户不手工编号，则系统自动编号
            vo.setPerson_no(partyId);
        }
		employeeDao.insert(vo);
        //RmLogHelper.log(TABLE_LOG_TYPE_NAME, "插入了1条记录,id=" + String.valueOf(oid));
		return partyId;
    }
    
    /**
     * 如果是一人多岗或人员既属于部门又属于岗位，只删除当前关系
     * 如果是一人一岗，把该人员挂到部门下面
     * 如果是人员只属于部门，彻底删除该人员
     * @param partyRelationId 人员的团体关系id
     * @return 
     */
    public int delete(String partyRelationId) {
    	//注：这里依赖简版连接规则：公司-公司，公司-部门，部门-部门，部门-岗位，部门-人员，岗位-人员
    	int n = 0;
    	String partyid = OrgHelper.getPartyIDByRelationID(partyRelationId);
    	String codes[] = OrgHelper.getRelationCode(partyid);
    	if(codes.length>1) {//如果是一人多岗或人员既属于部门又属于岗位，只删除当前关系
    		boolean result = OrgHelper.deletePartyRelation(partyRelationId);//调用接口删除当前关系和与其相应的权限等记录
    		n = result ? 1 : 0;
    	}else if(codes.length==1) {//如果是一人一岗或人员只属于部门
        	AuPartyRelationVo parentVo = OrgHelper.getUpRelationVoByID(partyRelationId);
        	if(GlobalConstants.getPartyType_posi().equals(parentVo.getPartytype_id())) {//如果父节点是岗位，把该人员挂到部门下面
        		AuPartyRelationVo deptVo = OrgHelper.getUpRelationVoByID(parentVo.getId());//取到部门节点
        		OrgHelper.addPartyRelation(partyid, deptVo.getId(), deptVo.getRelationtype_id());//将当前节点挂到部门节点底下
        		boolean result = OrgHelper.deletePartyRelation(partyRelationId);//删除当前节点
        		n = result ? 1 : 0;
        	}else if(GlobalConstants.getPartyType_dept().equals(parentVo.getPartytype_id())) {//如果父节点是部门，彻底删除该人员
            	n = employeeDao.delete(partyid);
            	OrgHelper.deleteParty(partyid);//调用接口删除相应的团体、团体关系、帐户、权限等记录
        	}
    	}
		return n;
    }

    /**
     * 删除多条记录，删除自身并同时删除相应的团体、团体关系、帐户、权限等记录
     * 
     * @param id 用于删除的记录的id
     * @return 成功删除的记录数
     */
    public int delete(String id[]) {
		int sum = employeeDao.delete(id);
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
    public EmployeeVo find(String id) {
		EmployeeVo vo = employeeDao.find(id);
        //RmLogHelper.log(TABLE_LOG_TYPE_NAME, "察看了1条记录,id=" + id);
		return vo;
    }

    /**
     * 更新单条记录，同时调用接口更新相应的团体、团体关系记录
     * 
     * @param vo 用于更新的VO对象
     * @return 成功更新的记录数
     */
    public int update(EmployeeVo vo) {
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
        partyVo.setName(vo.getPerson_name());//团体名称
        partyVo.setEmail(vo.getEmail());//团体EMAIL（对员工类型为必填）
        partyVo.setRemark(vo.getRemark());//备注	
        OrgHelper.updateParty(partyVo);//调用接口进行更新
        
		int sum = employeeDao.update(vo);
        //RmLogHelper.log(TABLE_LOG_TYPE_NAME, "更新了" + sum + "条记录,id=" + String.valueOf(vo.getId()));
		return sum;
    }

    /**
     * 查询所有的VO对象列表，不翻页
     * 
     * @return 查询到的VO列表
     */
    public List queryAll() {
		List lResult = employeeDao.queryAll();
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
		List lResult = employeeDao.queryAll(orderStr);
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
		List lResult = employeeDao.queryAll(no, size);
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
		List lResult = employeeDao.queryAll(no, size, orderStr);
		//RmLogHelper.log(TABLE_LOG_TYPE_NAME, "查询了多条记录,recordSum=" + lResult.size() + ", cmd=queryAll(" + no + ", " + size + ", " + orderStr + ")");
		return lResult;
    }

    /**
     * 查询总记录数
     * 
     * @return 总记录数
     */
    public int getRecordCount() {
        int sum = employeeDao.getRecordCount();
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
		int sum = employeeDao.getRecordCount(queryCondition);
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
		List lResult = employeeDao.queryByCondition(queryCondition);
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
		List lResult = employeeDao.queryByCondition(queryCondition, orderStr);
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
		List lResult = employeeDao.queryByCondition(no, size, queryCondition);
		//RmLogHelper.log(TABLE_LOG_TYPE_NAME, "按条件查询了多条记录,recordSum=" + lResult.size() + ", no=" + no + ", size=" + size + ", queryCondition=" + queryCondition);
		return lResult;
    }

    /**
     * 通过查询条件查询VO对象列表，支持翻页，针对移动端
     *
     * @param offset         当前记录数偏移量
     * @param pageSize       每页显示记录数
     * @param queryCondition 查询条件
     * @return
     */
    public List queryByCondition4Mobile(int offset, int pageSize, String queryCondition) {
        List resultList = employeeDao.queryByCondition4Mobile(offset,pageSize,queryCondition);
        return resultList;
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
		List lResult = employeeDao.queryByCondition(no, size, queryCondition, orderStr);
		//RmLogHelper.log(TABLE_LOG_TYPE_NAME, "按条件查询了多条记录,recordSum=" + lResult.size() + ", no=" + no + ", size=" + size + ", queryCondition=" + queryCondition + ", orderStr=" + orderStr);
		return lResult;
    }

}

