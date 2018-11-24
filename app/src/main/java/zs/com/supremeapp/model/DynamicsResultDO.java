package zs.com.supremeapp.model;

import java.util.List;

/**
 * Created by liujian on 2018/10/16.
 */

public class DynamicsResultDO extends BaseDO{

    private List<DynamicsDO> list;

    public List<DynamicsDO> getList() {
        return list;
    }

    public void setList(List<DynamicsDO> list) {
        this.list = list;
    }

    public static class DynamicsDO{
        private String user_id;
        private String user_name;
        private String user_avatar;
        private String user_company;
        private long time;

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

        public String getUser_company() {
            return user_company;
        }

        public void setUser_company(String user_company) {
            this.user_company = user_company;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }
    }


}
