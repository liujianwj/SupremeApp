package zs.com.supremeapp.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import zs.com.supremeapp.R;

/**
 * Created by liujian on 2018/10/14.
 */

public class GetZanDialog extends Dialog{

    public GetZanDialog(@NonNull Context context) {
        super(context);
    }

    public GetZanDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public GetZanDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder{
        private Context context;
        private ImageView closeIv;
        private TextView getZanTv;
        private View.OnClickListener onClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setOnClickListener(View.OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
            return this;
        }

        public GetZanDialog create(){
            final GetZanDialog getZanDialog = new GetZanDialog(context, R.style.nifty_dialog);
            View dialogView = LayoutInflater.from(context).inflate(R.layout.view_get_zan_dialog, null);
            getZanDialog.addContentView(dialogView, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            getZanDialog.setCanceledOnTouchOutside(false);

            closeIv = dialogView.findViewById(R.id.close_iv);
            getZanTv = dialogView.findViewById(R.id.getZanTv);

            closeIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getZanDialog.dismiss();
                }
            });
            getZanTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getZanDialog.dismiss();
                    if(onClickListener != null){
                        onClickListener.onClick(view);
                    }
                }
            });
            return getZanDialog;
        }
    }
}
