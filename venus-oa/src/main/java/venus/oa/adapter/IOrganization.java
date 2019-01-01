package venus.oa.adapter;

import org.dom4j.Document;

import java.util.List;

public interface IOrganization {

	/**
	 * 根据partyId获取对应的Party实例。
	 *
	 * @param partyId 唯一标识。
	 * @return Party Party实例。
	 */
//	public Party findPartyById(String partyId);

	/**
	 * 根据partyId获取该partyId下所有的party实例集合(包含自身)。
	 *
	 * @param partyId 唯一标识。
	 * @return List<Party> 由Party实例组成的集合。
	 */
	public List findAllById(String partyId);

	/**
     * 根据partyId获取该partyId下所有的party实例集合(包含自身)。
     *
     * @param partyId 唯一标识。
     * @return List<Party> 由Party实例组成的集合。
     */
    public List findAllById4Mobile(String partyId);
	
   /**
     * 根据传入的partyId做成所有隶属与该partyId的子集合的xml文档(包含自身)。
     * @param partyId 唯一标识。
     * @return 子集合的xml文档。
     */
    public Document makeXMLDoc(String partyId);

    /**
     * 根据传入的partyId做成所有隶属与该partyId的子集合的xml文档(包含自身)。
     * @param partyId 唯一标识。
     * @return 子集合的xml文档。
     */
    public Document makeXMLDoc(String partyId, boolean isCode);

    /**
     * 返回此团体ID下是否有组织数据。<br/>
     * 例如：此团体ID为人员ID，则必然返回false
     * 
     * @param partyId 唯一标识。
     * @return 是否包含组织数据。
     */
    public boolean hasChildren(String partyId);
}