package zs.com.supremeapp.model;

/**
 * Created by liujian on 2018/9/4.
 */

public class LoginResultDO extends BaseDO{
    private MemberDO member;
    private CustomDO custom;

    public MemberDO getMember() {
        return member;
    }

    public void setMember(MemberDO member) {
        this.member = member;
    }

    public CustomDO getCustom() {
        return custom;
    }

    public void setCustom(CustomDO custom) {
        this.custom = custom;
    }

    @Override
    public String toString() {
        return "LoginResultDO{" +
                "member=" + member.toString() +
                '}';
    }
}
