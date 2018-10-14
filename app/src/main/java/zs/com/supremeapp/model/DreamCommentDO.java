package zs.com.supremeapp.model;

/**
 * Created by liujian on 2018/10/12.
 */

public class DreamCommentDO extends BaseDO{

    private String user_id;
    private String user_name;
    private String user_avatar;
    private String user_status;
    private String dream_comment_id;
    private String dream_comment_comment;
    private String dream_comment_dreamid;
    private String dream_comment_time;

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

    public String getDream_comment_id() {
        return dream_comment_id;
    }

    public void setDream_comment_id(String dream_comment_id) {
        this.dream_comment_id = dream_comment_id;
    }

    public String getDream_comment_comment() {
        return dream_comment_comment;
    }

    public void setDream_comment_comment(String dream_comment_comment) {
        this.dream_comment_comment = dream_comment_comment;
    }

    public String getDream_comment_dreamid() {
        return dream_comment_dreamid;
    }

    public void setDream_comment_dreamid(String dream_comment_dreamid) {
        this.dream_comment_dreamid = dream_comment_dreamid;
    }

    public String getDream_comment_time() {
        return dream_comment_time;
    }

    public void setDream_comment_time(String dream_comment_time) {
        this.dream_comment_time = dream_comment_time;
    }
}
