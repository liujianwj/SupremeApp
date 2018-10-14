package zs.com.supremeapp.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.lang.ref.WeakReference;

import zs.com.supremeapp.R;


/**
 * Created by liujian on 2018/09/23.
 */

public class WidgetCycleViewPager extends ViewPager {
    //轮播间隔时间
    protected static final long MSG_DELAY = 3000;
    private CycleViewPagerHandler cycleViewPagerHandler;
    private boolean isPress;
    private LinearLayout linearLayout;
    private int dataSize;
    private OnPageSelectedListener onPageSelectedListener;

    public WidgetCycleViewPager(Context context) {
        this(context,null);
    }

    public WidgetCycleViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initCycleModel();
    }

    public void setOnPageSelectedListener(OnPageSelectedListener onPageSelectedListener) {
        this.onPageSelectedListener = onPageSelectedListener;
    }

    private void initCycleModel() {
        setOffscreenPageLimit(0);
        if (cycleViewPagerHandler == null) {
            cycleViewPagerHandler = new CycleViewPagerHandler(this);
        }
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isPress = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        isPress = false;
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        isPress = false;
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    public void setDataSize(int dataSize) {
        this.dataSize = dataSize;
    }

    public int getDataSize() {
        return dataSize;
    }

    public void setIndicatorView(LinearLayout v){
        setIndexIndicatorView(v, false);
    }

    /**
     * 设置圆点指示器
     * @param v
     */
    public void setIndexIndicatorView(LinearLayout v, final boolean isIndex) {
        if (dataSize <= 1) {
            return;
        }
        this.linearLayout = v;
        for (int i = 0; i < dataSize; i++) {
            ImageView imageView = new ImageView(getContext());
            if (i == 0) {
                imageView.setImageResource(isIndex ? R.drawable.buy_circle_black90_bg : R.drawable.buy_circle_white90_bg);
            } else {
                imageView.setImageResource(isIndex ? R.drawable.buy_circle_white90_bg : R.drawable.buy_circle_white30_bg);
            }
            linearLayout.addView(imageView);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();
            layoutParams.width = dip2px(getContext(), 8);
            layoutParams.height = dip2px(getContext(), 8);
            layoutParams.leftMargin = dip2px(getContext(), 10);
            if(isIndex && i == dataSize-1){
                layoutParams.rightMargin = dip2px(getContext(), 10);
            }
            imageView.setLayoutParams(layoutParams);
        }

        this.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int pageIndex = position;
                if (position == 0) {
                    // 当视图在第一个时，将页面号设置为图片的最后一张。
                    pageIndex = dataSize;
                } else if (position == dataSize + 1) {
                    // 当视图在最后一个是,将页面号设置为图片的第一张。
                    pageIndex = 1;
                }
                if (position != pageIndex) {
                    setCurrentItem(pageIndex, false);
                }

                if (linearLayout != null) {
                    if (linearLayout.getChildCount() > pageIndex - 1) {
                        for (int i = 0; i < linearLayout.getChildCount(); i++) {
                            ImageView imageView = (ImageView) linearLayout.getChildAt(i);
                            imageView.setImageResource(isIndex ? R.drawable.buy_circle_black90_bg : R.drawable.buy_circle_white90_bg);
                        }
                        ImageView imageView = (ImageView) linearLayout.getChildAt(pageIndex - 1);
                        imageView.setImageResource(isIndex ? R.drawable.buy_circle_white90_bg : R.drawable.buy_circle_white30_bg);
                    }
                }
                if (onPageSelectedListener != null) {
                    onPageSelectedListener.onPageSelected(pageIndex - 1);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void setOffscreenPageLimit(int limit) {
        super.setOffscreenPageLimit(limit);
    }

    public void goNextView() {
        if (!isPress && this.getCurrentItem() != this.getAdapter().getCount() - 1) {
            this.setCurrentItem(this.getCurrentItem() + 1, true);
        }
    }

    /**
     * 开始自动轮播播放
     */
    public void start(){
        if(this.getAdapter() != null && this.getDataSize() > 1){
            Message message = Message.obtain();
            message.what = 0x001;
            cycleViewPagerHandler.sendMessageDelayed(message, MSG_DELAY);
        }
    }

    /**
     * 停止自动播放
     */
    public void stop(){
        if(cycleViewPagerHandler != null){
            cycleViewPagerHandler.removeMessages(0x001);
        }
    }

    private static class CycleViewPagerHandler extends Handler {

        private WeakReference<WidgetCycleViewPager> weakReference;


        public CycleViewPagerHandler(WidgetCycleViewPager widgetCycleViewPager) {
            this.weakReference = new WeakReference<WidgetCycleViewPager>(widgetCycleViewPager);
        }

        @Override
        public void handleMessage(Message msg) {
            WidgetCycleViewPager widgetCycleViewPager = weakReference.get();
            if (widgetCycleViewPager == null) {
                return;
            }
            widgetCycleViewPager.goNextView();
            Message message = Message.obtain();
            message.what = 0x001;
            sendMessageDelayed(message, MSG_DELAY);
        }
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public interface OnPageSelectedListener{
        void onPageSelected(int position);
    }
}