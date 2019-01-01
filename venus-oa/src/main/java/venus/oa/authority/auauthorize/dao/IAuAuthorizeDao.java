package venus.oa.authority.auauthorize.dao;

import venus.oa.authority.auauthorize.vo.AuAuthorizeVo;

import java.util.List;

/**
 * 团体类型DAO
 * @author wumingqiang
 *
 */
public interface IAuAuthorizeDao {
    /**
     * 根据id删除授权情况（包括附加数据）
     * 
     * @param id
     * @return
     */
    public int delete(String id);

    /**
     * 根据访问者ID删除授权情况（包括附加数据）
     * 
     * @param id
     * @return
     */
    public int deleteByVisitorId(String visitorId);

    /**
     * 根据资源ID删除授权情况（包括附加数据）
     * 
     * @param id
     * @return
     */
    public int deleteByResourceId(String resourceId);

    /**
     * 添加记录到权限表
     * 
     * @param vo
     * @return
     */
    public String insert(AuAuthorizeVo vo);

    /**
     * 根据id更新权限表
     * 
     * @param vo
     * @return
     */
    public int update(AuAuthorizeVo vo);

    /**
     * 
     * 功能: 根据访问者ID和资源类型查询该访问者自身拥有的权限
     * 		如果资源类型为null，则查询全部资源类型的权限
     *
     * @param visitorId 访问者ID
     * @param resType 资源类型
     * @return
     */
    public List queryByVisitorId(String visitorId, String resType);
    
    /**
     * 功能: 根据访问者ID和资源类型查询该访问者自身拥有的权限
     * @param visitorId
     * @return
     */
    public List queryByVisitorId(String visitorId);
   
    /**
     * 
     * 功能: 根据访问者ID和资源类型查询该访问者自身拥有的历史权限
     * 		如果资源类型为null，则查询全部资源类型的权限
     *
     * @param visitorId 访问者ID
     * @param resType 资源类型
     * @return
     */        
    public List queryHistoryAuByVisitorId(String visitorId, String resType);
    
    /**
     * 
     * 功能: 根据访问者Code数组和资源类型查询该访问者自身拥有的权限+在同一团体关系类型内它所继承的权限
     *		如果资源类型为null，则查询全部资源类型的权限
     *
     * @param visiCodes 访问者编号数组
     * @param resType 资源类型
     * @return
     */
    public List queryByVisitorCode(String[] visiCodes, String resType);
    
    /**
     * 
     * 功能: 根据partyId和relationTypeId获取相应的访问者列表，
     * relationTypeId为可选参数，当为null或""时不起作用
     *
     * @param partyId
     * @param relationTypeId （可选）
     * @return
     */
    public List parsePartyIdToVisitor(String partyId, String relationTypeId);
    
    /**
     * 功能: 根据资源Code数组和访问者类型查询拥有资源权限的访问者；
     *	如果访问者类型为null，则查询全部类型的访问者
     * @param resCodes 资源编号数组
     * @param visiType 访问者类型
     * @return
     */
    public List queryByResourceCode(String[] resCodes, String visiType);
    
    /**
     * 功能: 根据访问者编号数组和团体类型获取相关的（自身及下属）partyid列表；
     * partyTypeId为可选参数，当为null或""时不起作用，将查询全部类型
     * @param visitorCode 访问者编号数组
     * @param partyTypeId 团体类型
     * @return
     */
    public List parseVisitorToPartyId(String visitorCode[], String partyTypeId);
    
    /**
     * 功能: 根据访问者编号数组获取相关的（仅自身）partyid列表
     * @param visitorCode 访问者编号数组
     * @return
     */
    public List parseVisitorToPartyId(String visitorCode[]); 
    
    /**
     * 功能: 根据访问者编号数组和团体类型获取相应的partyrelation表自身及所有下级的code；
     * partyTypeId为可选参数，当为null或""时不起作用，将查询全部类型
     * @param visitorCode 访问者编号数组
     * @param partyTypeId 团体类型
     * @return
     */
    public List parseVisitorToRelCode(String visitorCode[], String partyTypeId) ;
    
    /**
     * 功能: 根据访问者编号数组获取相应的partyrelation表自身的code
     * @param visitorCode 访问者编号数组
     * @return
     */
    public List parseVisitorToRelCode(String visitorCode[]);

	/**
	 * 通过主键获取vo
	 * @param authorizeId
	 * @return
	 */
	public AuAuthorizeVo find(String authorizeId);

    /**
     * 
     * 功能: 根据访问者Code数组和资源类型查询该访问者自身拥有的数据权限+在同一团体关系类型内它所继承的权限
     *      剔除历史数据权限
     *
     * @param visiCodeArray 访问者编号数组
     * @param resType 资源类型
     * @return
     */
    public List queryByVisitorCodeWithOutHistory(String[] visiCodeArray, String resType);

 }

