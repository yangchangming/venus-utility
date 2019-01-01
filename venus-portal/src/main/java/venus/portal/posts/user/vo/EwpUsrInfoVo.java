package venus.portal.posts.user.vo;

import venus.frames.base.vo.BaseValueObject;

import java.sql.Date;

/**
 * Created by liangc on 14-3-17.
 *
 */
public class EwpUsrInfoVo extends BaseValueObject {
    private String id;
    private String realName;
    private Date birthday;
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

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
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
