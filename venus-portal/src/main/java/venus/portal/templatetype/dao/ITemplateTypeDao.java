/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.portal.templatetype.dao;

import venus.portal.templatetype.model.TemplateType;

import java.util.List;

/**
 * @author zhaoyapeng
 *
 */
public interface ITemplateTypeDao {


    /**
     * 保存templateType
     * @param templateType
     */
    public void   saveTemplateType(TemplateType templateType);
    
    /**
     * 根据模板类型ID 删除一模板类型
     * @param id
     */
    public void deleteTemplateType(String id);
    
    /**
     * 批量根据模板ID 删除模板类型
     * @param ids
     */
    public void deleteTemplateTypes(List<String> ids);
    
    /**
     * 更新以模板类型
     * @param templateType
     */
    public  void  updateTemplateType(TemplateType templateType);
    
    /**
     * 根据模板类型ID 获取模板类型数据
     * @param id
     * @return
     */
    public TemplateType findTemplateType(String id);
    
    /**
     * 获取所有模板类型
     * @return
     */
    public List<TemplateType> findAllTemplateType();
    
    /**
     * 导入语句
     * @return
     */
    public void insertData();
}
