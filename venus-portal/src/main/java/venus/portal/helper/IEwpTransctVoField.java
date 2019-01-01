package venus.portal.helper;

import org.springframework.beans.BeanWrapper;

import java.beans.PropertyDescriptor;

/**
 * 功能、用途、现存BUG:
 * 
 * @author 白小勇
 * @version 1.0.0
 * @see 需要参见的其它类
 * @since 1.0.0
 */
public interface IEwpTransctVoField {
    public int transctVo(BeanWrapper bw, PropertyDescriptor pd);
}
