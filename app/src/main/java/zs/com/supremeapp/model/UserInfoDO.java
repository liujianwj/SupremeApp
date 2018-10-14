package zs.com.supremeapp.model;

/**
 * Created by liujian on 2018/10/12.
 */

public class UserInfoDO extends BaseDO{

    private String name;
    private String avatar;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
