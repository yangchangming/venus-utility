/*
 * 创建日期 2006-12-11
 *
 */
package venus.oa.service.test.vo;

import venus.frames.base.vo.BaseValueObject;

/**
 * @author tony
 *
 */
public class TestVo extends BaseValueObject {
    //表名
    private String name=null;
    
    /**
     * @return 返回 name。
     */
    public String getName() {
        return name;
    }
    /**
     * @param name 要设置的 name。
     */
    public void setName(String name) {
        this.name = name;
    }
}

