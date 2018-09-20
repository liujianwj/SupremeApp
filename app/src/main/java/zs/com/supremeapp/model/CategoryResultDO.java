package zs.com.supremeapp.model;

import java.util.List;

/**
 * Created by liujian on 2018/9/7.
 */

public class CategoryResultDO extends BaseDO{
    private List<CategoryDO> list;

    public List<CategoryDO> getList() {
        return list;
    }

    public void setList(List<CategoryDO> list) {
        this.list = list;
    }
}
