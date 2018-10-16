package zs.com.supremeapp.model;

import java.util.List;

/**
 * Created by liujian on 2018/10/15.
 */

public class GetOneZanResultDO extends BaseDO{

    private List<SupportDO> list;

    public List<SupportDO> getList() {
        return list;
    }

    public void setList(List<SupportDO> list) {
        this.list = list;
    }

    public static class SupportDO{
        private String user_id;
        private String user_name;
        private String user_avatar;
        private String user_status;
        private String dream_zhan_id;
        private String dream_zhan_zhan;
        private String dream_zhan_dreamid;
        private String dream_zhan_time;

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

        public String getUser_avatar() {
            return user_avatar;
        }

        public void setUser_avatar(String user_avatar) {
            this.user_avatar = user_avatar;
        }

        public String getUser_status() {
            return user_status;
        }

        public void setUser_status(String user_status) {
            this.user_status = user_status;
        }

        public String getDream_zhan_id() {
            return dream_zhan_id;
        }

        public void setDream_zhan_id(String dream_zhan_id) {
            this.dream_zhan_id = dream_zhan_id;
        }

        public String getDream_zhan_zhan() {
            return dream_zhan_zhan;
        }

        public void setDream_zhan_zhan(String dream_zhan_zhan) {
            this.dream_zhan_zhan = dream_zhan_zhan;
        }

        public String getDream_zhan_dreamid() {
            return dream_zhan_dreamid;
        }

        public void setDream_zhan_dreamid(String dream_zhan_dreamid) {
            this.dream_zhan_dreamid = dream_zhan_dreamid;
        }

        public String getDream_zhan_time() {
            return dream_zhan_time;
        }

        public void setDream_zhan_time(String dream_zhan_time) {
            this.dream_zhan_time = dream_zhan_time;
        }
    }
}
