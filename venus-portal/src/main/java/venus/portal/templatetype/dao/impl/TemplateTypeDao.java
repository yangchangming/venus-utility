/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.portal.templatetype.dao.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import venus.portal.document.util.IConstants;
import venus.portal.templatetype.bs.impl.HashMapMapper;
import venus.portal.templatetype.dao.ITemplateTypeDao;
import venus.portal.templatetype.model.TemplateType;
import venus.portal.templatetype.util.ITemplateTypeConstants;
import venus.frames.base.dao.BaseTemplateDao;
import venus.frames.mainframe.util.Helper;
import venus.pub.lang.OID;

import java.util.HashMap;
import java.util.List;

/**
 * @author zhaoyapeng
 *
 */
public class TemplateTypeDao  extends BaseTemplateDao implements ITemplateTypeDao,ITemplateTypeConstants {

    /* (non-Javadoc)
     * @see udp.ewp.templatetype.dao.ITemplateTypeDao#deleteTemplateType(java.lang.String)
     */
    public void deleteTemplateType(String id) {
//        TemplateType temp = this.findTemplateType(id);
//        super.delete(temp);
        
    }

    /* (non-Javadoc)
     * @see udp.ewp.templatetype.dao.ITemplateTypeDao#deleteTemplateTypes(java.util.List)
     */
    public void deleteTemplateTypes(List<String> ids) {
/*       for(String id:ids){
           this.deleteTemplateType(id);
       }*/
        
    }

    /* (non-Javadoc)
     * @see udp.ewp.templatetype.dao.ITemplateTypeDao#findAllTemplateType()
     */
    public List<TemplateType> findAllTemplateType() {
/*        StringBuffer sql = new StringBuffer("  from udp.ewp.templatetype.model.TemplateType ");
      List<TemplateType> result =  this.getHibernateTemplate().find(sql.toString());
        return result;*/
        return null;
    }

    /* (non-Javadoc)
     * @see udp.ewp.templatetype.dao.ITemplateTypeDao#findTemplateType(java.lang.String)
     */
    public TemplateType findTemplateType(String id) {
      /*TemplateType result =(TemplateType)   super.load(TemplateType.class, id);
        return result;*/
        return null;
    }

    /* (non-Javadoc)
     * @see udp.ewp.templatetype.dao.ITemplateTypeDao#saveTemplateType(udp.ewp.templatetype.model.TemplateType)
     */
    public void saveTemplateType(TemplateType templateType) {
      /*  OID oid = Helper.requestOID(TABLE_NAME); //获得oid
        long id = oid.longValue();
        templateType.setId(String.valueOf(id));
        super.save(templateType);*/
        
    }

    /* (non-Javadoc)
     * @see udp.ewp.templatetype.dao.ITemplateTypeDao#updateTemplateType(udp.ewp.templatetype.model.TemplateType)
     */
    public void updateTemplateType(TemplateType templateType) {
       /* super.update(templateType);*/
        
    }

    public List query(String queryString,RowMapper rowMapper){
       return super.query(queryString,rowMapper);
    }
    
    public void insertData() {
        //execute("delete from ewp_document_type_relation");
        //execute("delete from ewp_doctype");
        //execute("delete from ewp_document");
        //文章
        String queryString = "select category,title,content from articles";
        OID oid = Helper.requestOID(venus.portal.doctype.util.IConstants.DOCUMENT_TYPE_OID);
        String doctypeid = String.valueOf(oid);
        String insertString =String.format("insert into ewp_doctype(id,version,name) values (0,'%s','%s')", doctypeid,"我们的能力");
        //execute(insertString);
        String documentid;
        String relationid;
        List<HashMap> rowList = query(queryString, new HashMapMapper()); 
        for (HashMap row : rowList) {
            Integer category =  (Integer)row.get("category");
            if(StringUtils.equals(category.toString(), "1")){
                doctypeid = "1099104100000000008";
            }else if(StringUtils.equals(category.toString(), "2")){
                doctypeid = "1099104100000000009";
            }else if(StringUtils.equals(category.toString(), "3")){
                doctypeid = "1099104100000000018";
            }
            oid = Helper.requestOID(IConstants.DOCUMENT_OID);
            documentid = String.valueOf(oid);
            String title = (String)row.get("title");
            title = title.replaceAll("'", "&acute;");
            String content = (String)row.get("content");
            content = content.replaceAll("'", "&acute;");
            insertString =String.format("insert into ewp_document(id,isvalid,title,abstract,content) values ('%s','1','%s','%s','%s')",documentid, title,"",content);
            execute(insertString);
            oid = Helper.requestOID("ewp_document_type_relation");
            relationid = String.valueOf(oid);
            insertString =String.format("insert into ewp_document_type_relation(id,isactive,doctypeid,documentid) values ('%s','1','%s','%s')", relationid,doctypeid,documentid);
            execute(insertString);
         }
    }
    
}
