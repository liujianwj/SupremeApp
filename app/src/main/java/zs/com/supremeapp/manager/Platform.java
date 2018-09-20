package zs.com.supremeapp.manager;

/**
 * Created by liujian on 2018/9/4.
 */

public class Platform {

    private static Platform platform = null;

    private Platform() {
    }

    public static Platform getInstance() {
        if (platform == null) {
            synchronized (Platform.class) {
                platform = new Platform();
            }
        }
        return platform;
    }

    private String usrId;
    private String mobile;

    public String getUsrId() {
        return usrId;
    }

    public void setUsrId(String usrId) {
        this.usrId = usrId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}


