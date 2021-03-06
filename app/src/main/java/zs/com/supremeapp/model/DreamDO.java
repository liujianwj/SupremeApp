package zs.com.supremeapp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liujian on 2018/9/7.
 */

public class DreamDO extends BaseDO{
    private String user_id;
    private String user_name;
    private String user_avatar;
    private String user_status;
    private String user_company;
    private String dream_id;
    private String dream_userid;
    private String dream_title;
    private String dream_cateid;
    private String dream_content;
    private String dream_zhan;
    private String dream_target_zhan;
    private String dream_endday;
    private String dream_creattime;
    private String dream_video;
    private String dream_pic;
    private String dream_status;
    private String dream_video_thumb;
    private ArrayList<String> dream_pics;
    private String dream_money;

    public String getDream_money() {
        return dream_money;
    }

    public void setDream_money(String dream_money) {
        this.dream_money = dream_money;
    }

    public ArrayList<String> getDream_pics() {
        return dream_pics;
    }

    public void setDream_pics(ArrayList<String> dream_pics) {
        this.dream_pics = dream_pics;
    }

    public String getDream_video_thumb() {
        return dream_video_thumb;
    }

    public void setDream_video_thumb(String dream_video_thumb) {
        this.dream_video_thumb = dream_video_thumb;
    }

    public String getUser_company() {
        return user_company;
    }

    public void setUser_company(String user_company) {
        this.user_company = user_company;
    }

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

    public String getDream_id() {
        return dream_id;
    }

    public void setDream_id(String dream_id) {
        this.dream_id = dream_id;
    }

    public String getDream_userid() {
        return dream_userid;
    }

    public void setDream_userid(String dream_userid) {
        this.dream_userid = dream_userid;
    }

    public String getDream_title() {
        return dream_title;
    }

    public void setDream_title(String dream_title) {
        this.dream_title = dream_title;
    }

    public String getDream_cateid() {
        return dream_cateid;
    }

    public void setDream_cateid(String dream_cateid) {
        this.dream_cateid = dream_cateid;
    }

    public String getDream_content() {
        return dream_content;
    }

    public void setDream_content(String dream_content) {
        this.dream_content = dream_content;
    }

    public String getDream_zhan() {
        return dream_zhan;
    }

    public void setDream_zhan(String dream_zhan) {
        this.dream_zhan = dream_zhan;
    }

    public String getDream_target_zhan() {
        return dream_target_zhan;
    }

    public void setDream_target_zhan(String dream_target_zhan) {
        this.dream_target_zhan = dream_target_zhan;
    }

    public String getDream_endday() {
        return dream_endday;
    }

    public void setDream_endday(String dream_endday) {
        this.dream_endday = dream_endday;
    }

    public String getDream_creattime() {
        return dream_creattime;
    }

    public void setDream_creattime(String dream_creattime) {
        this.dream_creattime = dream_creattime;
    }

    public String getDream_video() {
        return dream_video;
    }

    public void setDream_video(String dream_video) {
        this.dream_video = dream_video;
    }

    public String getDream_pic() {
        return dream_pic;
    }

    public void setDream_pic(String dream_pic) {
        this.dream_pic = dream_pic;
    }

    public String getDream_status() {
        return dream_status;
    }

    public void setDream_status(String dream_status) {
        this.dream_status = dream_status;
    }
}
