package zs.com.supremeapp.model;

/**
 * Created by liujian on 2018/9/26.
 */

public class UploadImageDO {
    private String source_url;
    private int source_width;
    private int source_height;
    private String thumbnail_320;
    private String thumbnail_640;

    public String getSource_url() {
        return source_url;
    }

    public void setSource_url(String source_url) {
        this.source_url = source_url;
    }

    public int getSource_width() {
        return source_width;
    }

    public void setSource_width(int source_width) {
        this.source_width = source_width;
    }

    public int getSource_height() {
        return source_height;
    }

    public void setSource_height(int source_height) {
        this.source_height = source_height;
    }

    public String getThumbnail_320() {
        return thumbnail_320;
    }

    public void setThumbnail_320(String thumbnail_320) {
        this.thumbnail_320 = thumbnail_320;
    }

    public String getThumbnail_640() {
        return thumbnail_640;
    }

    public void setThumbnail_640(String thumbnail_640) {
        this.thumbnail_640 = thumbnail_640;
    }
}
