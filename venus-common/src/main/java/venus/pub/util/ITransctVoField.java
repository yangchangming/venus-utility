package venus.pub.util;

import org.springframework.beans.BeanWrapper;

import java.beans.PropertyDescriptor;

public interface ITransctVoField {
	public int transctVo(BeanWrapper bw, PropertyDescriptor pd);
}
