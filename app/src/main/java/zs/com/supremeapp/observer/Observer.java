package zs.com.supremeapp.observer;

import java.util.Map;

/**
 * Created by 首乌 on 2017/3/4.
 */

public interface Observer {
    /**
     * 更新接口
     * @param params    参数
     */
    public void update(Map<String, Object> params, String key);

}
