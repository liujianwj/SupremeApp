package zs.com.supremeapp.model;

/**
 * Created by liujian on 2018/10/9.
 */

public class ContrastDO extends BaseDO{

    private String user_name;
    private String user_mobile;
    private String user_used_app;

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_mobile() {
        return user_mobile;
    }

    public void setUser_mobile(String user_mobile) {
        this.user_mobile = user_mobile;
    }

    public String getUser_used_app() {
        return user_used_app;
    }

    public void setUser_used_app(String user_used_app) {
        this.user_used_app = user_used_app;
    }
}
