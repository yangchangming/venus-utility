package venus.oa.orgadjust.bs;

public interface IAdjustOrganizeBs {
	
	/**
	 * 调用删除和增加机构的业务方法，实现机构调级操作
	 * @param orgId 操作者机构ID
	 * @param oldNodeCode 旧机构的节点CODE
	 * @param newNodeCode 新机构的节点CODE
	 */
	public void updateRelation(String orgId, String oldNodeCode, String newNodeCode);
	
}

