package zs.com.supremeapp.model;

/**
 * Created by liujian on 2018/9/4.
 */

public class MemberDO extends BaseDO{

    private String name;
    private String mobile;
    private String im_token;

    public String getIm_token() {
        return im_token;
    }

    public void setIm_token(String im_token) {
        this.im_token = im_token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "MemberDO{" +
                "name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                '}';
    }
}
