package zs.com.supremeapp.model;

/**
 * Created by liujian on 2018/10/9.
 */

public class NearbyDO extends BaseDO{

    private String user_id;
    private String user_name;
    private String user_zone_url;
    private String user_mobile;
    private String user_avatar;
    private GpsDO gps;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_zone_url() {
        return user_zone_url;
    }

    public void setUser_zone_url(String user_zone_url) {
        this.user_zone_url = user_zone_url;
    }

    public String getUser_mobile() {
        return user_mobile;
    }

    public void setUser_mobile(String user_mobile) {
        this.user_mobile = user_mobile;
    }

    public String getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    public GpsDO getGps() {
        return gps;
    }

    public void setGps(GpsDO gps) {
        this.gps = gps;
    }
}
