package venus.portal.doctype.util;

import venus.portal.doctype.model.DocType;

import java.io.Serializable;
import java.util.Comparator;

/**
 * @author zhangrenyang
 *
 */
public class DocTypeComparator implements Comparator ,Serializable {

    public int compare(Object o1, Object o2) {
        if(o1 instanceof DocType && o2 instanceof DocType){
         Long s1 = ((DocType)o1).getSortNum();
         Long s2 = ((DocType)o2).getSortNum();
         int res =  s1.intValue() - s2.intValue();
         if(res == 0){
              int h1 = ((DocType)o1).hashCode();
              int h2 = ((DocType)o2).hashCode();
             return h1-h2;
         }else{
             return res;
         }
        }
        return 0;
     }
}
