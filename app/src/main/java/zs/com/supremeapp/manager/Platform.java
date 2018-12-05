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
    private String name;
    private String mobile;
    private String imToken;
    private String avatar;
    private String zone_pic;

    private String uid;
    private String umobile;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUmobile() {
        return umobile;
    }

    public void setUmobile(String umobile) {
        this.umobile = umobile;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getZone_pic() {
        return zone_pic;
    }

    public void setZone_pic(String zone_pic) {
        this.zone_pic = zone_pic;
    }

    public String getImToken() {
        return imToken;
    }

    public void setImToken(String imToken) {
        this.imToken = imToken;
    }

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


