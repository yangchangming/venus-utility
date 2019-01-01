/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.oa.util.common.vo;

/**
 * @author changming.Y <changming.yang.ah@gmail.com>
 *
 */
public class NullObject {
    private static NullObject instance = new NullObject();
    
    private NullObject(){}
    
    public String toString(){
        return "";
    }

    /**
     * @return self
     */
    public static Object getInstance() {
        return instance;
    }
}
