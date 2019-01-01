package venus.portal.posts.user.model;

import venus.frames.base.vo.BaseValueObject;
import venus.portal.posts.user.util.ILoginUsrInfoConstants;

/**
 * Created by qj on 14-1-27.
 */
public class EwpLoginEntity extends BaseValueObject implements Cloneable, ILoginUsrInfoConstants {
    private String id;
    private String name;
    private String pwd;
    private String enable = LOGIC_TRUE;
    private String isadmin = LOGIC_FALSE;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public String getIsadmin() {
        return isadmin;
    }

    public void setIsadmin(String isadmin) {
        this.isadmin = isadmin;
    }

}
