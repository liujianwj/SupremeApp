package zs.com.supremeapp.model;

import java.util.List;

/**
 * Created by liujian on 2018/10/9.
 */

public class NearbyResultDO extends BaseDO{

    private List<NearbyDO> list;

    public List<NearbyDO> getList() {
        return list;
    }

    public void setList(List<NearbyDO> list) {
        this.list = list;
    }
}
