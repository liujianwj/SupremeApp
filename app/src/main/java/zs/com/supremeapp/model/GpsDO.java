package zs.com.supremeapp.model;

/**
 * Created by liujian on 2018/10/9.
 */

public class GpsDO extends BaseDO{

    private double lat;
    private double lng;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
