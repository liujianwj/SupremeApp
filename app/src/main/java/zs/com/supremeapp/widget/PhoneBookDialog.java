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
 * Created by liujian on 2018/10/9.
 */

public class PhoneBookDialog extends Dialog{

    public PhoneBookDialog(@NonNull Context context) {
        super(context);
    }

    public PhoneBookDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public PhoneBookDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder{
        private Context context;
        private ImageView closeIv;
        private TextView matchPhoneTv;
        private View.OnClickListener onClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setOnClickListener(View.OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
            return this;
        }

        public PhoneBookDialog create(){
            final PhoneBookDialog phoneBookDialog = new PhoneBookDialog(context, R.style.nifty_dialog);

            View dialogView = LayoutInflater.from(context).inflate(R.layout.view_phone_book_dialog, null);
            phoneBookDialog.addContentView(dialogView, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            closeIv = dialogView.findViewById(R.id.close_iv);
            matchPhoneTv = dialogView.findViewById(R.id.match_phone_tv);
            closeIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    phoneBookDialog.dismiss();
                }
            });
            matchPhoneTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onClickListener != null){
                        onClickListener.onClick(view);
                        phoneBookDialog.dismiss();
                    }
                }
            });

            return phoneBookDialog;
        }
    }
}
