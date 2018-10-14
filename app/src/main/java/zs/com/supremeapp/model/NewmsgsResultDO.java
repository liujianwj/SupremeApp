package zs.com.supremeapp.model;

import java.util.List;

/**
 * Created by liujian on 2018/10/10.
 */

public class NewmsgsResultDO extends BaseDO{

    private List<ZanDO> zhans;
    private List<CommentDO> mcms_comments;

    public List<ZanDO> getZhans() {
        return zhans;
    }

    public void setZhans(List<ZanDO> zhans) {
        this.zhans = zhans;
    }

    public List<CommentDO> getMcms_comments() {
        return mcms_comments;
    }

    public void setMcms_comments(List<CommentDO> mcms_comments) {
        this.mcms_comments = mcms_comments;
    }
}
