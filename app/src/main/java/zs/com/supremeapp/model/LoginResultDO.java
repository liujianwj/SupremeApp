package zs.com.supremeapp.model;

/**
 * Created by liujian on 2018/9/4.
 */

public class LoginResultDO extends BaseDO{
    MemberDO member;

    public MemberDO getMember() {
        return member;
    }

    public void setMember(MemberDO member) {
        this.member = member;
    }

    @Override
    public String toString() {
        return "LoginResultDO{" +
                "member=" + member.toString() +
                '}';
    }
}
