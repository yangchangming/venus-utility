/**
 *@author zhangrenyang 
 *@date  2011-9-28
*/
package venus.portal.util;

import org.springframework.beans.BeanWrapper;

import java.beans.PropertyDescriptor;

/**
 *@author zhangrenyang 
 *@date  2011-9-28
 */
public interface IEwpTransctVoField {
    public int transctVo(BeanWrapper bw, PropertyDescriptor pd);
}
