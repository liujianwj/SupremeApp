package zs.com.supremeapp.model;

/**
 * Created by liujian on 2018/10/14.
 */

public class HaveZansResultDO {

    private MemberZan member_zhan;

    public MemberZan getMember_zhan() {
        return member_zhan;
    }

    public void setMember_zhan(MemberZan member_zhan) {
        this.member_zhan = member_zhan;
    }

    public static class MemberZan{
        private int zhans;
        private String lastfreetime;

        public int getZhans() {
            return zhans;
        }

        public void setZhans(int zhans) {
            this.zhans = zhans;
        }

        public String getLastfreetime() {
            return lastfreetime;
        }

        public void setLastfreetime(String lastfreetime) {
            this.lastfreetime = lastfreetime;
        }
    }
}
