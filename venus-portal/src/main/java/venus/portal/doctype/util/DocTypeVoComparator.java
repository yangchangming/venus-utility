/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.portal.doctype.util;

import venus.portal.doctype.vo.DocTypeVo;

import java.io.Serializable;
import java.util.Comparator;

/**
 * @author zhangrenyang
 *
 */
public class DocTypeVoComparator implements Comparator ,Serializable {

    public int compare(Object o1, Object o2) {
        if(o1 instanceof DocTypeVo && o2 instanceof DocTypeVo){
         Long s1 = ((DocTypeVo)o1).getSortNum();
         Long s2 = ((DocTypeVo)o2).getSortNum();
         int res =  s1.intValue() - s2.intValue();
         if(res == 0){
              int h1 = ((DocTypeVo)o1).hashCode();
              int h2 = ((DocTypeVo)o2).hashCode();
             return h1-h2;
         }else{
             return res;
         }
        }
        return 0;
     }
}
