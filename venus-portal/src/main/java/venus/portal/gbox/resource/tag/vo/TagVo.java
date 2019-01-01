package venus.portal.gbox.resource.tag.vo;

import venus.frames.base.vo.BaseValueObject;

import java.sql.Timestamp;

public class TagVo extends BaseValueObject {
    
    private String id;

    private String name;
    
    private int clicks;
    
    private Timestamp createDate;

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

    public int getClicks() {
        return clicks;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }
    
}
