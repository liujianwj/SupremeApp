package zs.com.supremeapp.event;

/**
 * Created by liujian on 2018/11/22.
 */

public class NavigationControlEvent {

    private boolean isShow = true;

    public NavigationControlEvent(boolean isShow) {
        this.isShow = isShow;
    }

    public boolean isShow() {
        return isShow;
    }
}
