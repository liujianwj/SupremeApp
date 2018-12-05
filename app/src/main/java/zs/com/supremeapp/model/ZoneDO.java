package zs.com.supremeapp.model;

import java.util.List;

/**
 * Created by liujian on 2018/9/25.
 */

public class ZoneDO extends BaseDO{
    private String content;
    private String creatTime;
    private String views;
    private String pics;
    private String video;
    private String zhan;  //点赞数量
    private String comments;
    private String user_id;
    private String user_name;
    private String user_avatar;
    private List<ZanDO> zhans;
    private List<CommentDO> commentslist;
    private List<AlbumDO> album;


    public List<AlbumDO> getAlbum() {
        return album;
    }

    public void setAlbum(List<AlbumDO> album) {
        this.album = album;
    }

    public List<CommentDO> getCommentslist() {
        return commentslist;
    }

    public void setCommentslist(List<CommentDO> commentslist) {
        this.commentslist = commentslist;
    }

    public List<ZanDO> getZhans() {
        return zhans;
    }

    public void setZhans(List<ZanDO> zhans) {
        this.zhans = zhans;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getPics() {
        return pics;
    }

    public void setPics(String pics) {
        this.pics = pics;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getZhan() {
        return zhan;
    }

    public void setZhan(String zhan) {
        this.zhan = zhan;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
