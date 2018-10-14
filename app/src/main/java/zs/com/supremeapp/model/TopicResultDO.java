package zs.com.supremeapp.model;

import java.util.List;

/**
 * Created by liujian on 2018/9/23.
 */

public class TopicResultDO extends BaseDO{

    private List<BannerDO> list;

    public List<BannerDO> getList() {
        return list;
    }

    public void setList(List<BannerDO> list) {
        this.list = list;
    }
}
