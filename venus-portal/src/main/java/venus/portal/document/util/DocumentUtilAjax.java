/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.portal.document.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import venus.frames.base.exception.BaseApplicationException;
import venus.frames.mainframe.util.Helper;
import venus.portal.doctype.bs.IDocTypeBS;
import venus.portal.doctype.model.DocType;
import venus.portal.document.bs.IDocumentBS;
import venus.portal.document.model.Document;

/**
 * AJAX实现 文档工具类
 * @author yangchangming
 */
public class DocumentUtilAjax implements IConstants{

    private static final Log logger = LogFactory.getLog(DocumentUtilAjax.class);

    /**
     * 获取document对象
     * @param docID 对象id
     * @return
     */
    public Document findDocById(String docID){
        try{
            IDocumentBS docmentBs = (IDocumentBS)Helper.getBean(DOCUMENT_BS);
            return docmentBs.findDocById(docID);
        }catch(Exception e){
            logger.error(e.getMessage());
            throw new BaseApplicationException(e.getMessage());
        }
    }

    /**
     * 判断文档类型是否是叶子节点
     * @param docTypeID
     * @return
     */
    public boolean isLeafNode(String docTypeID){
       try{
           IDocTypeBS docTypeBs = (IDocTypeBS)Helper.getBean(DOCUMENT_TYPE_BS);
           if(docTypeID!=null && !"".equals(docTypeID)){
               DocType docType = docTypeBs.findDocTypeById(docTypeID);
               if(docType!=null){
                   return docType.isLeafNode();
               }else{
                   return false;
               }
           }else{
               return false;
           }
       }catch(Exception e){
           logger.error(e.getMessage());
           throw new BaseApplicationException(e.getMessage());
       }
    }
}
