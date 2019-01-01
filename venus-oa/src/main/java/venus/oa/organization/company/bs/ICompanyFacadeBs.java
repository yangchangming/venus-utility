package venus.oa.organization.company.bs;

import venus.oa.login.vo.LoginSessionVo;
import venus.oa.organization.company.vo.CompanyVo;

public interface ICompanyFacadeBs {
    
    	/**
    	 * 添加新记录，同时添加团体、团体关系根节点并记录历史日志
    	 * @param companyVo
    	 * @param vo
    	 * @return
    	 */
    	String insertRoot(CompanyVo companyVo, LoginSessionVo vo);
    
    	/**
    	 * 添加新记录，同时添加团体、团体关系并记录历史日志（如果parentRelId为空则不添加团体关系）
    	 * @param companyVo
    	 * @param parentRelId
    	 * @param vo
    	 * @return
    	 */
    	String insert(CompanyVo companyVo, String parentRelId, LoginSessionVo vo);
    	
    	/**
    	 * 更新单条记录，同时调用接口更新相应的团体、团体关系记录并记下历史日志
    	 * @param companyVo
    	 * @param vo
    	 * @return
    	 */
    	int update(CompanyVo companyVo, LoginSessionVo vo);
    
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

