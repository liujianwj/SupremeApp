package zs.com.supremeapp.model;

/**
 * Created by liujian on 2018/9/23.
 */

public class BannerDO extends BaseDO{
    private String dream_id;
    private String dream_title;
    private String dream_thumb;
    private String dream_ad_pic;

    public String getDream_id() {
        return dream_id;
    }

    public void setDream_id(String dream_id) {
        this.dream_id = dream_id;
    }

    public String getDream_title() {
        return dream_title;
    }

    public void setDream_title(String dream_title) {
        this.dream_title = dream_title;
    }

    public String getDream_thumb() {
        return dream_thumb;
    }

    public void setDream_thumb(String dream_thumb) {
        this.dream_thumb = dream_thumb;
    }

    public String getDream_ad_pic() {
        return dream_ad_pic;
    }

    public void setDream_ad_pic(String dream_ad_pic) {
        this.dream_ad_pic = dream_ad_pic;
    }
}
