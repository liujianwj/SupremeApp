package zs.com.supremeapp.widget;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import zs.com.supremeapp.R;

/**
 * Created by liujian on 2018/8/13.
 */

public class FriendCommentPopup extends BasePopupWindow{

    @BindView(R.id.commentLayout)
    View commentLayout;

    private View.OnClickListener onClickListener;
    private int position;

    public FriendCommentPopup(Activity activity) {
        super(activity);
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    @Override
    protected void initPopupWindow() {
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setBgAlpha(1);
    }

    @Override
    protected View initContentView() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.view_comment_popup, null);
        ButterKnife.bind(this, view);

        commentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(mActivity, "aa", Toast.LENGTH_SHORT).show();
                if(onClickListener != null){
                    onClickListener.onClick(view);
                }
            }
        });
        return view;
    }
}
