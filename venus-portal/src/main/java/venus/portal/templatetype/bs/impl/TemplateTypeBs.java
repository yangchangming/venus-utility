/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.portal.templatetype.bs.impl;


import venus.portal.templatetype.bs.ITemplateTypeBs;
import venus.portal.templatetype.dao.ITemplateTypeDao;
import venus.portal.templatetype.model.TemplateType;

import java.util.List;

/**
 * @author zhaoyapeng
 *
 */
public class TemplateTypeBs implements ITemplateTypeBs {

    private ITemplateTypeDao templateTypeDao;
    
    /**
     * @return the templateTypeDao
     */
    public ITemplateTypeDao getTemplateTypeDao() {
        return templateTypeDao;
    }

    /**
     * @param templateTypeDao the templateTypeDao to set
     */
    public void setTemplateTypeDao(ITemplateTypeDao templateTypeDao) {
        this.templateTypeDao = templateTypeDao;
    }

    /* (non-Javadoc)
     * @see udp.ewp.templatetype.bs.ITemplateTypeBs#deleteTemplateType(java.lang.String)
     */
    public void deleteTemplateType(String id) {
        this.getTemplateTypeDao().deleteTemplateType(id);
    }

    /* (non-Javadoc)
     * @see udp.ewp.templatetype.bs.ITemplateTypeBs#deleteTemplateTypes(java.util.List)
     */
    public void deleteTemplateTypes(List<String> ids) {
       this.getTemplateTypeDao().deleteTemplateTypes(ids);
        
    }

    /* (non-Javadoc)
     * @see udp.ewp.templatetype.bs.ITemplateTypeBs#findAllTemplateType()
     */
    public List<TemplateType> findAllTemplateType() {
        List<TemplateType> result = null;
        result = this.getTemplateTypeDao().findAllTemplateType();
        return result;
    }

    /* (non-Javadoc)
     * @see udp.ewp.templatetype.bs.ITemplateTypeBs#findTemplateType(java.lang.String)
     */
    public TemplateType findTemplateType(String id) {
      TemplateType result = null;
      result = this.getTemplateTypeDao().findTemplateType(id);
        return result;
    }

    /* (non-Javadoc)
     * @see udp.ewp.templatetype.bs.ITemplateTypeBs#saveTemplateType(udp.ewp.templatetype.model.TemplateType)
     */
    public void saveTemplateType(TemplateType templateType) {
        this.getTemplateTypeDao().saveTemplateType(templateType);
        
    }

    /* (non-Javadoc)
     * @see udp.ewp.templatetype.bs.ITemplateTypeBs#updateTemplateType(udp.ewp.templatetype.model.TemplateType)
     */
    public void updateTemplateType(TemplateType templateType) {
     this.getTemplateTypeDao().updateTemplateType(templateType);
        
    }

    /* (non-Javadoc)
     * @see udp.ewp.templatetype.bs.ITemplateTypeBs#importData()
     */
    public void insertData() {
        this.getTemplateTypeDao().insertData();
    }

}
