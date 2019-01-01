package venus.frames.base.vo;

import venus.frames.base.IGlobalsKeys;

import java.io.Serializable;


/**
 * @author sundaiyong
 *
 * 值对象基类,所有的值对象都从此派生
 */
public class BaseValueObject implements Serializable ,IGlobalsKeys {

	/**
	 * 
	 */
	public BaseValueObject() {
		super();
	}
	
	/**
	 * validate in vo
	 * if validate not pass throw new BaseApplicationException(msg)
	 */
	public void validate(){
		
		//if validate not pass throw new BaseApplicationException(msg)

	}

}
