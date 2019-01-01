package venus.oa.adapter;

//import udp.common.authority.model.Party;
//import udp.common.authority.model.UserDetails;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 权限操作接口。
 */
public interface IAuthorization {

    /**
     * 验证用户信息。
     * @param userName 用户名。
     * @param passwd 密码。
     * @return 用户ID。
     */
    public String findIdByNameAndPass(String userName, String passwd);

    /**
     * 获取用户标识，带有权限信息的。
     * @param request 客户端请求。
     */
    public String fetchUserKey(HttpServletRequest request);

	/**
	 * 从HttpServetRequest中获取用户标识。
	 * @param request 客户端请求。
	 */
	public String fetchUserId(HttpServletRequest request);

    /**
     * 从HttpServetRequest中获取用户信息。
     * @param request 客户端请求。
     */
//    public Party fetchUser(HttpServletRequest request);
	
	/**
	 * 根据用户标识来获取用户所有授权信息。
	 *
	 * @param userId 用户唯一标识。
	 * @return UserDetails 对应用户的授权信息。
	 */
//	public UserDetails findUserDetailsById(String userId);

	/**
	 * 根据用户标识来获取该用户所有授权资源集合。
	 *
	 * @param userId 用户唯一标识。
	 * @return Map<String, List<Resource>> 授权资源的集合，键为资源类型，值为对应类型的集合。
	 */
	public Map findResourcesById(String userId);

	/**
	 * 根据用户标识和资源类型来获取该用户所有授权资源列表。
	 *
	 * @param userId 用户唯一标识。
	 * @param resType 资源类型。
	 * @return List<Resource> 对应资源类型的资源的集合。
	 */
	public List findResourcesByIdAndResType(String userId, String resType);


	/**
	 * 获取所有资源列表。
	 *
	 * @return List<Resource> 资源集合。
	 */
	public List findAllResources();

	/**
	 * 根据用户ID、资源类别、资源组名来获取资源集合。(该接口只给记录、字段使用)
	 *
	 * @param userId 用户ID。
	 * @param resType 资源类别。
	 * @param groupName 资源组名。
	 * @return List<Resource> 符合条件的资源集合。
	 */
	public List findResourcesByConditions(String userId, String resType, String groupName);

	/**
	 * 根据资源类别、资源组名来获取受控资源集合。
	 *
	 * @param groupName 资源组名。
	 * @param resType 资源类别。
	 * @return List<Resource> 符合条件的资源集合。(该接口只给记录、字段使用)
	 */
	public List findResourcesByConditions(String resType, String groupName);

	/**
	 * 注册资源。
	 *
	 * @param resources 需要进行注册的资源集合。
	 */
	public void registerResources(List resources);
}