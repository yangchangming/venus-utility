package venus.oa.organization.aupartytype.bs.impl;

import org.apache.commons.lang.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.springframework.stereotype.Service;
import venus.oa.authority.aufunctree.bs.IAuFunctreeBs;
import venus.oa.authority.aufunctree.util.IAuFunctreeConstants;
import venus.oa.authority.aufunctree.vo.AuFunctreeVo;
import venus.oa.organization.auconnectrule.dao.IAuConnectRuleDao;
import venus.oa.organization.auconnectrule.vo.AuConnectRuleVo;
import venus.oa.organization.auparty.dao.IAuPartyDao;
import venus.oa.organization.auparty.vo.PartyVo;
import venus.oa.organization.aupartytype.bs.IAuPartyTypeBS;
import venus.oa.organization.aupartytype.dao.IAuPartyTypeDao;
import venus.oa.organization.aupartytype.dao.impl.AuPartyTypeDao;
import venus.oa.organization.aupartytype.util.IConstants;
import venus.oa.organization.aupartytype.util.TypeMapper;
import venus.oa.organization.aupartytype.vo.AuPartyTypeVo;
import venus.oa.util.DataBaseDescription;
import venus.oa.util.DateTools;
import venus.frames.base.bs.BaseBusinessService;
import venus.frames.base.exception.BaseApplicationException;
//import venus.frames.mainframe.log.ILog;
//import venus.frames.mainframe.log.LogMgr;
import venus.frames.mainframe.util.Helper;

import java.sql.SQLException;
import java.util.List;

/**
 * 团体类型BS
 * @author wumingqiang
 *
 */
@Service
public class AuPartyTypeBS extends BaseBusinessService implements IAuPartyTypeBS,
        IConstants {

//    private static ILog log = LogMgr.getLogger(AuPartyTypeBS.class);

    private IAuPartyTypeDao dao = null;

    /**
     * 查询所有
     * @param no
     * @param size
     * @param orderStr
     * @return
     */
    public List queryAll(int no, int size, String orderStr) {
        return getDao().queryAll(no, size, orderStr);
    }

    /**
     * 
     * 功能: 查询所有启用状态的团体类型
     * 当参数no和size小于或等于0时，不翻页
     * 当参数orderStr为null时，不进行排序
     * @param no
     * @param size
     * @param orderStr
     * @return
     */
    public List queryAllEnable(int no, int size, String orderStr) {
        return getDao().queryAllEnable(no, size, orderStr);
    }
    
    /**
     * 按条件查询,返回LIST
     * @param no
     * @param size
     * @param orderStr
     * @param objVo
     * @return
     */
    public List simpleQuery(int no, int size, String orderStr, Object objVo) {
        return getDao().simpleQuery(no, size, orderStr, objVo);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    public int delete(String id) {
        return getDao().delete(id);
    }

    /**
     * 获得记录数
     * @return
     */
    public int getRecordCount() {
        return getDao().getRecordCount();
    }


    /**
     * 按条件获得记录数
     * @param objVo
     * @return
     */
    public int getRecordCount(Object objVo) {
        return getDao().getRecordCount(objVo);
    }

    /**
     * @return
     */
    public IAuPartyTypeDao getDao() {
        return dao;
    }

    /**
     * @param dao
     */
    public void setDao(IAuPartyTypeDao dao) {
        this.dao = dao;
    }

    /**
     *  添加
     * @param rvo
     * @return
     */
    public String insert(Object objVo) {
        List list = getDao().queryByName(objVo);
        if (list.size()>0) {
//            log.equals(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Body_type_name_duplication_re_edit_")+((AuPartyTypeVo) objVo).getName());
            throw new BaseApplicationException(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Body_type_name_duplication_re_edit"));
        }
        return getDao().insert(objVo);
    }

    /**
     * 查找
     * @param id
     * @return
     */
    public Object find(String id) {
        return getDao().find(id);
    }

    /**
     * 更新
     * @param objVo
     * @return
     */
    public int update(Object objVo) {
        List list = getDao().queryByName(objVo);
        if (list.size()>1) {
//            log.equals(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Body_type_name_duplication_re_edit_")+((AuPartyTypeVo) objVo).getName());
            throw new BaseApplicationException(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Body_type_name_duplication_re_edit"));
        }
        else if (list.size()==1){
            AuPartyTypeVo vo = (AuPartyTypeVo)list.get(0);
            if (!vo.getId().equals(((AuPartyTypeVo) objVo).getId())) {
//                log.equals(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Body_type_name_duplication_re_edit_")+((AuPartyTypeVo) objVo).getName());
                throw new BaseApplicationException(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Body_type_name_duplication_re_edit"));
            }
        }
        return getDao().update(objVo);
    }

	public List getPartyAll() {
		return getDao().getPartyAll();
	}    
	/**
     * 启用
     * @param id
     * @return
     */
	public int enable(String id) {
		return getDao().enable(id);
	}
	/**
     * 禁用
     * @param id
     * @return
     */
	public int disable(String id) {
	    IAuPartyDao auPartyDao = (IAuPartyDao) Helper.getBean("auparty_dao");
	    PartyVo vo = new PartyVo();
	    List list = auPartyDao.simpleQuery(1,1,null,id,vo);
	    if (list.size()>0) {
	        for(int i=0;i<list.size();i++){
	            PartyVo tmp = (PartyVo)list.get(i);
	            if ("1".equals(tmp.getEnable_status())) {
//	                log.equals(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Groups_of_this_type_have_been_used_Please_delete_the_relevant_data_")+tmp.getId()+venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority._1")+tmp.getName());
	                throw new BaseApplicationException(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Groups_of_this_type_have_been_used_Please_delete_the_relevant_data"));
	            }	            
	        }	        
	    }
	    IAuConnectRuleDao auConnectRuleDao = (IAuConnectRuleDao) Helper.getBean("au_connectrule_dao");
	    AuConnectRuleVo tvo = new AuConnectRuleVo();
	    tvo.setParent_partytype_id(id);
	    list = auConnectRuleDao.queryByType(tvo);
	    if (list.size()>0) {
	        throw new BaseApplicationException(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Groups_of_this_type_have_been_used_to_connect_the_rules_Please_delete_the_relevant_data"));
	    }
	    tvo.setParent_partytype_id("");
	    tvo.setChild_partytype_id(id);
	    list = auConnectRuleDao.queryByType(tvo);
	    if (list.size()>0) {
	        throw new BaseApplicationException(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Groups_of_this_type_have_been_used_to_connect_the_rules_Please_delete_the_relevant_data"));
	    }
		return getDao().disable(id);
	}
	
	/**
     * 根据KeyWord获得ID
     * @param keyword
     * @return
     */
    public String getIdByKeyWord(String keyword){
    	return getDao().getIdByKeyWord(keyword);
    }
    /**
     * 根据KeyWord获得所有该类型的party列表
     * @return
     */
    public List getPartysByKeyWord(String keyword){
        return getDao().getPartysByKeyWord(keyword);
    }

    /**
     * List 中是LabelValueBean
     * @return
     */
    public List getPartyAllByKeyword(String keyword){
        return getDao().getPartyAllByKeyword(keyword);
    }

    public String generatePhysicsCode(String paraValue,String paraArray,AuPartyTypeVo obj) {
        String tableName = obj.getTable_name();
        boolean isGenerateCode = tableName.contains("ORGANIZE_");
        String catalogue = isGenerateCode?tableName.substring("ORGANIZE_".length()):tableName.substring("COLLECTIVE_".length());
        String xmlStr = physicsData(paraValue, paraArray, catalogue, obj);
        //generate party type
//        Through.setValue(xmlStr);

        if(isGenerateCode) {
//            ((ETLProcessor) Helper.getBean("organizeModelEtlprocessor")).process();
        } else {
//            ((ETLProcessor) Helper.getBean("commonModelEtlprocessor")).process();
        }
//        Through.removeValue();

        //设置菜单 TODO 如果考虑机构和权限分离，这部分代码耦合就重了
        AuFunctreeVo vo = new AuFunctreeVo();
        IAuFunctreeBs bs = (IAuFunctreeBs) Helper.getBean(IAuFunctreeConstants.BS_KEY);
        vo.setParent_code("101003");
        vo.setType("0");
        vo.setIs_leaf("1");
        vo.setType_is_leaf("1");
        
        vo.setName(obj.getName()+venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Management"));
        vo.setUrl("/"+String.valueOf(catalogue.charAt(0))+catalogue.substring(1).toLowerCase()+(isGenerateCode?"Organize":"Collective")+"Action.do?cmd=queryAll");
        vo.setCreate_date(DateTools.getSysTimestamp());  //打创建时间
        bs.insert(vo);
        return null;
    }

    public String appendPhysicsCode(String paraValue, String paraArray, AuPartyTypeVo obj) {
        String tableName = obj.getTable_name();
        boolean isGenerateCode = tableName.contains("ORGANIZE_");
        String catalogue = isGenerateCode?tableName.substring("ORGANIZE_".length()):tableName.substring("COLLECTIVE_".length());
        String xmlStr = physicsData(paraValue, paraArray, catalogue, obj);
        //generate party type
//        Through.setValue(xmlStr);
        if(isGenerateCode) {
//            ((ETLProcessor) Helper.getBean("appendOrganizeModelEtlprocessor")).process();
        }else {
//            ((ETLProcessor) Helper.getBean("appendCommonModelEtlprocessor")).process();
        }
//        Through.removeValue();
        return null;
    }
    
    private String physicsData(String paraValue,String paraArray, String catalogue, AuPartyTypeVo obj){
        String paraNames[] = paraArray.split(",");
        String paraValues[] = paraValue.split(",");
        
        Element root = new Element("metamodel");
        root.addContent(new Element("catalogue").setText(catalogue));
        root.addContent(new Element("identification").setText(obj.getId()));
        root.addContent(new Element("name").setText(obj.getName()));
        try {
            root.addContent(new Element("dbproductname").setText(DataBaseDescription.getDatabaseProductName(((AuPartyTypeDao) dao).getDataSource().getConnection())));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Document doc = new Document(root);
        if(StringUtils.isNotEmpty(paraValue)){
            for(int i=0;i<paraValues.length;i++){
                Element triples = new Element("triples");
                String attrNameValue = paraValues[i];
                i++;
                String asciiNameValue = paraValues[i];
                i++;
                String attrTypeValue = paraValues[i];
                i++;
                String en = paraNames[3];
                String enValue = paraValues[i];
                
                Element datapoint = new Element("datapoint");
                datapoint.addContent(new Element("dataname").setText(asciiNameValue));
                datapoint.addContent(new Element("datatype").setText(TypeMapper.getDBType(attrTypeValue)));
                datapoint.addContent(new Element("nullable").setText("true"));
                
                Element javapoint = new Element("javapoint");
                javapoint.addContent(new Element("propertyname").setText(asciiNameValue));
                javapoint.addContent(new Element("propertytype").setText(TypeMapper.getJavaType(attrTypeValue)));
                
                Element i18npoint = new Element("i18npoint");
                i18npoint.addContent(new Element("i18ncaption").setText(asciiNameValue+"_key"));
                i18npoint.addContent(new Element("i18nname").setText(attrNameValue));
                
                Element i18nvalue = new Element("i18nvalue");
                i18nvalue.addContent(new Element("lang").setText(en));
                i18nvalue.addContent(new Element("value").setText(enValue));
                i18npoint.addContent(i18nvalue);
                
                for(int j=4;j<paraNames.length;j++){
                    i++;
                    String lang = paraNames[j];
                    String langValue = paraValues[i];
                    i18nvalue = new Element("i18nvalue");
                    i18nvalue.addContent(new Element("lang").setText(lang));
                    i18nvalue.addContent(new Element("value").setText(langValue));
                    i18npoint.addContent(i18nvalue);
                }                
                
                triples.addContent(datapoint);
                triples.addContent(javapoint);
                triples.addContent(i18npoint);
                root.addContent(triples);                
            }
        }
        XMLOutputter outputter = new XMLOutputter(Format.getCompactFormat());
        return outputter.outputString(doc);
    }
}

