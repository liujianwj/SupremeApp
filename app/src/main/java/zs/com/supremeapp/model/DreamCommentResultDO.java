package zs.com.supremeapp.model;

import java.util.List;

/**
 * Created by liujian on 2018/10/12.
 */

public class DreamCommentResultDO extends BaseDO{

    private List<DreamCommentDO> list;

    public List<DreamCommentDO> getList() {
        return list;
    }

    public void setList(List<DreamCommentDO> list) {
        this.list = list;
    }
}
