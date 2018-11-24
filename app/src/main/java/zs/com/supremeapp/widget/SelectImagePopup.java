package zs.com.supremeapp.widget;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import zs.com.supremeapp.R;

/**
 * Created by liujian on 2018/11/19.
 */

public class SelectImagePopup extends BasePopupWindow{

    private View.OnClickListener onClickListener;

    public SelectImagePopup(Activity activity) {
        super(activity);
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    protected void initPopupWindow() {
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setFocusable(true);
        setBackgroundDrawable(new BitmapDrawable());
    }

    @Override
    protected View initContentView() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.view_select_image_popup, null);
        View selectTv = view.findViewById(R.id.selectTv);
        View cancelTv = view.findViewById(R.id.cancelTv);
        selectTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onClickListener != null){
                    onClickListener.onClick(view);
                }
                SelectImagePopup.this.dismiss();
            }
        });
        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImagePopup.this.dismiss();
            }
        });
        return view;
    }
}
