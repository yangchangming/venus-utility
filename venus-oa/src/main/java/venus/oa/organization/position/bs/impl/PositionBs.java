/*
 * 系统名称:
 * 
 * 文件名称: venus.authority.sample.position.bs.impl --> PositionBs.java
 * 
 * 功能描述:
 * 
 * 版本历史: 2007-01-31 14:20:06.947 创建1.0.0版 (甘硕)
 *  
 */

package venus.oa.organization.position.bs.impl;


import venus.oa.helper.OrgHelper;
import venus.oa.organization.auparty.vo.PartyVo;
import venus.oa.organization.position.bs.IPositionBs;
import venus.oa.organization.position.dao.IPositionDao;
import venus.oa.organization.position.util.IPositionConstants;
import venus.oa.organization.position.vo.PositionVo;
import venus.oa.util.GlobalConstants;
import venus.frames.base.bs.BaseBusinessService;

import java.util.List;

/**
 * 功能、用途、现存BUG:
 * 
 * @author 甘硕
 * @version 1.0.0
 * @see 需要参见的其它类
 * @since 1.0.0
 */

public class PositionBs extends BaseBusinessService implements IPositionBs, IPositionConstants {
    
    /**
     * dao 表示: 数据访问层的实例
     */
    private IPositionDao dao = null;

    /**
     * 设置数据访问接口
     * 
     * @return
     */
    public IPositionDao getDao() {
        return dao;
    }

    /**
     * 获取数据访问接口
     * 
     * @param dao
     */
    public void setDao(IPositionDao dao) {
        this.dao = dao;
    }

    /**
     * 添加新记录，同时添加团体、团体关系（如果parentRelId为空则不添加团体关系）
     * 
     * @param vo 用于添加的VO对象
     * @param parentRelId 上级节点团体关系主键
     * @return 若添加成功，则返回新添加记录的主键
     */
    public String insert(PositionVo vo, String parentRelId) {
        //添加新的团体
        PartyVo partyVo = new PartyVo();
        partyVo.setPartytype_id(GlobalConstants.getPartyType_posi());//团体类型表的主键ID
        partyVo.setName(vo.getPosition_name());//团体名称
        partyVo.setRemark(vo.getRemark());//备注	
        partyVo.setEnable_status(vo.getEnable_status());//启用/禁用状态
        partyVo.setCreate_date(vo.getCreate_date());//创建时间
        String partyId = OrgHelper.addParty(partyVo);//调用接口添加团体
        //添加团体关系
        if(parentRelId!=null && !"".equals(parentRelId) && !"null".equals(parentRelId)) {
            String relType = GlobalConstants.getRelaType_comp();//团体关系类型－行政关系
            OrgHelper.addPartyRelation(partyId, parentRelId, relType);//调用接口添加团体关系
        }
        //添加岗位
        vo.setId(partyId);
        if(vo.getPosition_no()==null || vo.getPosition_no().length()==0) {//如果用户不手工编号，则系统自动编号
            vo.setPosition_no(partyId);
        }
		getDao().insert(vo);
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
    	int n = getDao().delete(partyid);
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
		int sum = getDao().delete(id);
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
    public PositionVo find(String id) {
		PositionVo vo = getDao().find(id);
        //RmLogHelper.log(TABLE_LOG_TYPE_NAME, "察看了1条记录,id=" + id);
		return vo;
    }

    /**
     * 更新单条记录，同时调用接口更新相应的团体、团体关系记录
     * 
     * @param vo 用于更新的VO对象
     * @return 成功更新的记录数
     */
    public int update(PositionVo vo) {
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
        partyVo.setName(vo.getPosition_name());//团体名称
        partyVo.setRemark(vo.getRemark());//备注	
        OrgHelper.updateParty(partyVo);//调用接口进行更新
        
		int sum = getDao().update(vo);
        //RmLogHelper.log(TABLE_LOG_TYPE_NAME, "更新了" + sum + "条记录,id=" + String.valueOf(vo.getId()));
		return sum;
    }

    /**
     * 查询所有的VO对象列表，不翻页
     * 
     * @return 查询到的VO列表
     */
    public List queryAll() {
		List lResult = getDao().queryAll();
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
		List lResult = getDao().queryAll(orderStr);
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
		List lResult = getDao().queryAll(no, size);
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
		List lResult = getDao().queryAll(no, size, orderStr);
		//RmLogHelper.log(TABLE_LOG_TYPE_NAME, "查询了多条记录,recordSum=" + lResult.size() + ", cmd=queryAll(" + no + ", " + size + ", " + orderStr + ")");
		return lResult;
    }

    /**
     * 查询总记录数
     * 
     * @return 总记录数
     */
    public int getRecordCount() {
        int sum = getDao().getRecordCount();
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
		int sum = getDao().getRecordCount(queryCondition);
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
		List lResult = getDao().queryByCondition(queryCondition);
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
		List lResult = getDao().queryByCondition(queryCondition, orderStr);
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
		List lResult = getDao().queryByCondition(no, size, queryCondition);
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
		List lResult = getDao().queryByCondition(no, size, queryCondition, orderStr);
		//RmLogHelper.log(TABLE_LOG_TYPE_NAME, "按条件查询了多条记录,recordSum=" + lResult.size() + ", no=" + no + ", size=" + size + ", queryCondition=" + queryCondition + ", orderStr=" + orderStr);
		return lResult;
    }

}
