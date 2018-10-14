package zs.com.supremeapp.model;

/**
 * Created by liujian on 2018/10/11.
 */

public class AlbumDO extends BaseDO{

    private String thumbnail_320;
    private String thumbnail_640;
    private String source_uri;

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

    public String getSource_uri() {
        return source_uri;
    }

    public void setSource_uri(String source_uri) {
        this.source_uri = source_uri;
    }
}
