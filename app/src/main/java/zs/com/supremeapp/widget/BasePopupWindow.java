package zs.com.supremeapp.widget;

import android.app.Activity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * Created by liujian on 2018/8/13.
 */
public abstract class BasePopupWindow extends PopupWindow {
    private float bgAlpha = 0.7f; //0.0-1.0
    protected Activity mActivity;
    protected View rootView;
    private static final String POP_TAG = "popwindow";

    public BasePopupWindow(Activity activity) {
        mActivity = activity;
        initPopupWindow();
        rootView = initContentView();
        setContentView(rootView);
        //因为某些机型是虚拟按键的,所以要加上以下设置防止挡住按键.
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }


    /**
     * 初始化popup设置
     */
    protected abstract void initPopupWindow();

    /**
     * 出事化ContentView
     */
    protected abstract View initContentView();

    /**
     * 设置背景灰度色
     *
     * @param bgAlpha
     */
    public void setBgAlpha(float bgAlpha) {
        this.bgAlpha = bgAlpha;
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        setBackgroundAlpha(bgAlpha);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        super.showAsDropDown(anchor, xoff, yoff);
//        setBackgroundAlpha(0.7f);
    }


    @Override
    public void dismiss() {
        super.dismiss();
        setBackgroundAlpha(1f);

    }

    /**
     * 设置Popup window 弹起时背景灰度
     *
     * @param bgAlpha
     */
    private void setBackgroundAlpha(float bgAlpha) {
        try {
            if (mActivity != null) {
                WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
                lp.alpha = bgAlpha;
                mActivity.getWindow().setAttributes(lp);
                mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    public interface OnPopwindowStateChangeListener()
}