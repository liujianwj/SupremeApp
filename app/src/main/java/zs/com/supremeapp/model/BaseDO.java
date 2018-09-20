package zs.com.supremeapp.model;

import java.io.Serializable;

/**
 * Created by liujian on 2018/8/12.
 */

public class BaseDO implements Serializable {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
