package venus.oa.authority.aufunctree.bs.impl;

import org.springframework.stereotype.Service;
import venus.oa.authority.aufunctree.bs.IAuFunctreeBs;
import venus.oa.authority.aufunctree.dao.IAuFunctreeDao;
import venus.oa.authority.aufunctree.util.AuFunctreeConstants;
import venus.oa.authority.aufunctree.util.IAuFunctreeConstants;
import venus.oa.authority.aufunctree.vo.AuFunctreeVo;
import venus.oa.authority.auresource.bs.IAuResourceBs;
import venus.oa.authority.auresource.util.IAuResourceConstants;
import venus.oa.authority.auresource.vo.AuResourceVo;
import venus.oa.util.DateTools;
import venus.oa.util.GlobalConstants;
import venus.oa.util.ProjTools;
import venus.frames.base.bs.BaseBusinessService;
import venus.frames.mainframe.util.Helper;
import venus.pub.lang.OID;

import java.util.Iterator;
import java.util.List;

/**
 * 功能、用途、现存BUG:
 * 
 * @author 甘硕
 * @version 1.0.0
 * @see 需要参见的其它类
 * @since 1.0.0
 */

@Service
public class AuFunctreeBs extends BaseBusinessService implements IAuFunctreeBs, IAuFunctreeConstants {
    
    /**
     * dao 表示: 数据访问层的实例
     */
    private IAuFunctreeDao dao = null;

    /**
     * 设置数据访问接口
     * 
     * @return
     */
    public IAuFunctreeDao getDao() {
        return dao;
    }

    /**
     * 获取数据访问接口
     * 
     * @param dao
     */
    public void setDao(IAuFunctreeDao dao) {
        this.dao = dao;
    }

    /**
     * 插入单条记录
     * 
     * @param vo 用于添加的VO对象
     * @return 若添加成功，返回新生成的Oid
     */
    public OID insert(AuFunctreeVo vo) {
		//第一步：判断父节点是否为叶子节点，如果是则将它改为非叶子节点
        List myList = getDao().queryByCondition("TOTAL_CODE='"+vo.getParent_code()+"'");
		AuFunctreeVo parentVo = (AuFunctreeVo)myList.get(0);
        if( "1".equals(parentVo.getIs_leaf()) || "1".equals(parentVo.getType_is_leaf()) ) {
        	boolean isUpdate = false;
        	if("1".equals(parentVo.getIs_leaf())) {
        		parentVo.setIs_leaf("0");
        		isUpdate = true;
        	}
        	if("1".equals(parentVo.getType_is_leaf()) && vo.getType().equals(parentVo.getType())) {
        		parentVo.setType_is_leaf("0");
        		isUpdate = true;
        	}
        	if(isUpdate) {
        	    parentVo.setModify_date(DateTools.getSysTimestamp());
        		getDao().update(parentVo);
        	}
        }

        //第二步：先插入au_resource表
        String code = ProjTools.getTreeCode(AuFunctreeConstants.getDifferentInstance().TABLE_NAME,"TOTAL_CODE",3,vo.getParent_code()); //获得code
        String orderCode = ProjTools.getTreeCode(AuFunctreeConstants.getDifferentInstance().TABLE_NAME,"ORDER_CODE",3,vo.getParent_code());
        
        IAuResourceBs resBs = (IAuResourceBs) Helper.getBean(IAuResourceConstants.BS_KEY);
        AuResourceVo resVo = new AuResourceVo();
        resVo.setResource_type(vo.getType());
        resVo.setName(vo.getName());
        resVo.setIs_public("0");
        resVo.setCreate_date(vo.getCreate_date());
        resVo.setEnable_status("1");
        resVo.setAccess_type("1");
        resVo.setValue(code);
        resVo.setField_name(vo.getKeyword());
        resVo.setFilter_type(vo.getHot_key());
        resVo.setTable_name(vo.getUrl());
        resVo.setHelp(vo.getHelp());
        //resVo.setEnable_date();
        //resVo.setModify_date();
        //resVo.setId();
        //resVo.setParty_type();
		//resVo.setTable_chinesename();
        //resVo.setField_chinesename();
        
        OID oid = resBs.insert(resVo);
        //第三步：插入新节点
        vo.setTotal_code(code);
        vo.setOrder_code(orderCode);//排序编号
        if(vo.getKeyword()==null || vo.getKeyword().trim().equals("")) {
            vo.setKeyword(code);
        }
        vo.setTree_level(code.length()/3);
        vo.setCode(code.substring(code.length()-3));
        long id = oid.longValue();
        vo.setId(String.valueOf(id));
        getDao().insert(vo);
        //RmLogHelper.log(TABLE_LOG_TYPE_NAME, "插入了1条记录,id=" + String.valueOf(oid));
		return oid;
    }

    /**
     * 删除单条记录
     * 
     * @param id 用于删除的记录的id
     * @return 成功删除的记录数
     */
    public int delete(String id) {
        AuFunctreeVo vo = getDao().find(id);
        String pCode = vo.getParent_code();
        //第一步：判断删除的节点是否最后一个子节点，如果是则将其父节点改为叶子节点
        boolean is_leaf = false;
        boolean type_is_leaf = false;
		int count = getDao().getRecordCount("PARENT_CODE ='"+pCode+"'");
		if( count==1 ) {
			is_leaf = true;
			type_is_leaf = true;
        }else if(vo.getType().equals(GlobalConstants.getResType_menu())){//当前删除的是菜单，且菜单只有一个
			count = getDao().getRecordCount("PARENT_CODE ='"+pCode+"' and TYPE='" + GlobalConstants.getResType_menu() + "'");
			if( count==1 ) {
				type_is_leaf = true;
	        }
        }
		if(is_leaf || type_is_leaf) {
			List pList = getDao().queryByCondition("TOTAL_CODE ='"+pCode+"'");
		    AuFunctreeVo parentVo = (AuFunctreeVo)pList.get(0);
			if(is_leaf) {
				 parentVo.setIs_leaf("1");
			}
			if(type_is_leaf) {
				parentVo.setType_is_leaf("1");
			}
			parentVo.setModify_date(DateTools.getSysTimestamp());
			getDao().update(parentVo);	
		}
		//第二步：删除节点
		int sum = getDao().delete(id);
		
		//同步删除au_resource

        // 去掉了虚拟站点功能，这些代码全部屏蔽
		//AU_FUNCTREE的所有虚拟站点表均没有数据，则删除AU_RESOURCE表
//		IVirtualSiteBs virtualSiteBs = (IVirtualSiteBs) Helper.getBean(IConstant.VIRTUALSITEBS);
//		if(!virtualSiteBs.isLastResourceInVirtualSite(id)){
//            IAuResourceBs resBs = (IAuResourceBs) Helper.getBean(IAuResourceConstants.BS_KEY);
//            resBs.delete(id);
    		//RmLogHelper.log(TABLE_LOG_TYPE_NAME, "删除了" + sum + "条记录,id=" + String.valueOf(id));
//		}
		return sum;
    }


    /**
     * 删除多条记录
     * 
     * @param id 用于删除的记录的id
     * @return 成功删除的记录数
     */
    public int delete(String id[]) {
		int sum = getDao().delete(id);
		//同步删除au_resource
        IAuResourceBs resBs = (IAuResourceBs) Helper.getBean(IAuResourceConstants.BS_KEY);
        resBs.delete(id);
        //RmLogHelper.log(TABLE_LOG_TYPE_NAME, "删除了" + sum + "条记录,id=" + String.valueOf(id));
		return sum;
    }

    /**
     * 根据Id进行查询
     * 
     * @param id 用于查找的id
     * @return 查询到的VO对象
     */
    public AuFunctreeVo find(String id) {
		AuFunctreeVo vo = getDao().find(id);
        //RmLogHelper.log(TABLE_LOG_TYPE_NAME, "察看了1条记录,id=" + id);
		return vo;
    }

    /**
     * 更新单条记录
     * 
     * @param vo 用于更新的VO对象
     * @return 成功更新的记录数
     */
    public int update(AuFunctreeVo vo) {
		int sum = getDao().update(vo);
		//同步更新au_resource
		IAuResourceBs resBs = (IAuResourceBs) Helper.getBean(IAuResourceConstants.BS_KEY);
        AuResourceVo resVo = resBs.find(vo.getId());
        boolean isUpdate = false;
        if(vo.getName()!=null && ! vo.getName().equals(resVo.getName())) {
	        resVo.setName(vo.getName());
	        isUpdate = true;
        }
        if(vo.getKeyword()!=null && ! vo.getKeyword().equals(resVo.getField_name())) {
	        resVo.setField_name(vo.getKeyword());
	        isUpdate = true;
        }
        if(vo.getHot_key()!=null && ! vo.getHot_key().equals(resVo.getFilter_type())) {
	        resVo.setFilter_type(vo.getHot_key());
	        isUpdate = true;
        }
        if(vo.getUrl()!=null && ! vo.getUrl().equals(resVo.getTable_name())) {
	        resVo.setTable_name(vo.getUrl());
	        isUpdate = true;
        }
        if(vo.getHelp()!=null && ! vo.getHelp().equals(resVo.getHelp())) {
	        resVo.setHelp(vo.getHelp());
	        isUpdate = true;
        }
        if(isUpdate) {
            resVo.setModify_date(vo.getModify_date());
	        resBs.update(resVo);
        }
        //RmLogHelper.log(TABLE_LOG_TYPE_NAME, "更新了" + sum + "条记录,id=" + String.valueOf(vo.getId()));
		return sum;
    }

    /**
     * 
     * 更新多条记录
     *
     * @param lChang 用于更新的列表(String[2]组成的list)
     * @return 成功更新的记录数
     */
    public int update(List lChange) {
		int sum = 0;
		for(Iterator itlChange = lChange.iterator(); itlChange.hasNext(); ) {
			String[] param = (String[]) itlChange.next();
			String strSql = "update "+ AuFunctreeConstants.getDifferentInstance().TABLE_NAME+" set ORDER_CODE='"+param[0]+"' where id='"+param[1]+"'";
			sum += getDao().update(strSql);				
		} 
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

