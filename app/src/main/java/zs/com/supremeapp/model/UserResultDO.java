package zs.com.supremeapp.model;

/**
 * Created by liujian on 2018/10/12.
 */

public class UserResultDO extends BaseDO{

    private UserInfoDO userInfo;

    public UserInfoDO getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoDO userInfo) {
        this.userInfo = userInfo;
    }
}
