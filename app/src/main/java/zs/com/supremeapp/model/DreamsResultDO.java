package zs.com.supremeapp.model;

import java.util.List;

/**
 * Created by liujian on 2018/9/20.
 */

public class DreamsResultDO extends BaseDO{

    private List<DreamDO> list;

    public List<DreamDO> getList() {
        return list;
    }

    public void setList(List<DreamDO> list) {
        this.list = list;
    }
}
