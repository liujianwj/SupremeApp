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

public class PayForZanDialog extends Dialog {

    public PayForZanDialog(@NonNull Context context) {
        super(context);
    }

    public PayForZanDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public PayForZanDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder{
        private Context context;
        private ImageView closeIv;
        private TextView pay50Tv;
        private TextView pay100Tv;
        private TextView pay200Tv;
        private TextView payTv;
        private int payNum = 100;

        private View.OnClickListener onClickListener;

        public Builder setOnClickListener(View.OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
            return this;
        }

        public Builder(Context context) {
            this.context = context;
        }

        public PayForZanDialog create(){
            final PayForZanDialog payForZanDialog = new PayForZanDialog(context, R.style.nifty_dialog);
            View dialogView = LayoutInflater.from(context).inflate(R.layout.view_pay_for_zan_dialog, null);
            payForZanDialog.addContentView(dialogView, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            payForZanDialog.setCanceledOnTouchOutside(false);

            closeIv = dialogView.findViewById(R.id.close_iv);
            pay50Tv = dialogView.findViewById(R.id.pay50Tv);
            pay100Tv = dialogView.findViewById(R.id.pay100Tv);
            pay200Tv = dialogView.findViewById(R.id.pay200Tv);
            payTv = dialogView.findViewById(R.id.payTv);

            closeIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    payForZanDialog.dismiss();
                }
            });
            pay50Tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    payNum = 50;
                    pay50Tv.setBackgroundResource(R.drawable.bg_red_cor_border_5);
                    pay100Tv.setBackgroundResource(R.drawable.bg_grey_cor_5);
                    pay200Tv.setBackgroundResource(R.drawable.bg_grey_cor_5);
                }
            });

            pay100Tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    payNum = 100;
                    pay50Tv.setBackgroundResource(R.drawable.bg_grey_cor_5);
                    pay100Tv.setBackgroundResource(R.drawable.bg_red_cor_border_5);
                    pay200Tv.setBackgroundResource(R.drawable.bg_grey_cor_5);
                }
            });

            pay200Tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    payNum = 200;
                    pay50Tv.setBackgroundResource(R.drawable.bg_grey_cor_5);
                    pay100Tv.setBackgroundResource(R.drawable.bg_grey_cor_5);
                    pay200Tv.setBackgroundResource(R.drawable.bg_red_cor_border_5);
                }
            });

            payTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onClickListener != null){
                        view.setTag(payNum);
                        onClickListener.onClick(view);
                    }
                }
            });

            return payForZanDialog;
        }
    }
}
