package venus.oa.organization.employee.bs;

import venus.oa.login.vo.LoginSessionVo;
import venus.oa.organization.employee.vo.EmployeeVo;

public interface IEmployeeFacadeBs {
    
    	/**
    	 * 添加新记录，同时添加团体、团体关系并记录历史日志（如果parentRelId为空则不添加团体关系）
    	 * @param empVo
    	 * @param parentRelId
    	 * @param vo
    	 * @return
    	 */
    	String insert(EmployeeVo empVo, String parentRelId, LoginSessionVo vo);
    	
    	/**
    	 * 更新单条记录，同时调用接口更新相应的团体、团体关系记录并记录历史日志
    	 * @param empVo
    	 * @param vo
    	 * @return
    	 */
    	int update(EmployeeVo empVo, LoginSessionVo vo) ;
	
	/**
	 * 负责删除团体和记录删除日志的功能
	 * @param partyId 团体id
	 * @param vo 当前上下文件中的用户信息
	 * @return 
	 */
	int delete(String partyId[], LoginSessionVo vo);
	
	/**
	 * 负责删除团体关系和记录删除日志的功能
	 * @param relationId 关系id
	 * @param vo 当前上下文件中的用户信息
	 * @return
	 */
	int delete(String relationId, LoginSessionVo vo);
	
}
