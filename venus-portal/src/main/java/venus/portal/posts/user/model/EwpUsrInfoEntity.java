package venus.portal.posts.user.model;

import venus.frames.base.vo.BaseValueObject;

import java.sql.Timestamp;

/**
 * Created by qj on 14-1-27.
 */
public class EwpUsrInfoEntity extends BaseValueObject implements Cloneable {
    private String id;
    private String realName;
    private Timestamp birthday;
    private String email;
    private String phone;
    private String gender;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Timestamp getBirthday() {
        return birthday;
    }

    public void setBirthday(Timestamp birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


}
