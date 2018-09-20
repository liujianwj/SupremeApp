package zs.com.supremeapp.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import zs.com.supremeapp.R;

/**
 * 自定义点赞dialog
 * Created by liujian on 2018/9/2.
 */

public class ZanDialog extends Dialog{

    public ZanDialog(@NonNull Context context) {
        super(context);
    }

    public ZanDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public ZanDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder{
        private Context context;

        private TextView moreZanTv;
        private ImageView closeIv;

        public Builder(Context context) {
            this.context = context;
        }

        public ZanDialog create(){
            final ZanDialog zanDialog = new ZanDialog(context, R.style.nifty_dialog);

            View dialogView = LayoutInflater.from(context).inflate(R.layout.view_zan_dialog, null);
            zanDialog.addContentView(dialogView, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            moreZanTv = dialogView.findViewById(R.id.more_zan_tv);
            closeIv = dialogView.findViewById(R.id.close_iv);
            //您当前总有 15 个点赞机会，如何获取更多机会。
            String text = "您当前总有 15 个点赞机会，如何获取更多机会。";
            SpannableString spannableString = new SpannableString(text);
            spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.common_red)), 6, text.length() - 16, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ClickableSpan() {

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(context.getResources().getColor(R.color.common_red));       //设置文件颜色
                    ds.setUnderlineText(true);      //设置下划线
                }

                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "hah", Toast.LENGTH_SHORT).show();
                }
            }, text.length() - 9, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            moreZanTv.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明，否则会一直出现高亮
            moreZanTv.setText(spannableString);
            moreZanTv.setMovementMethod(LinkMovementMethod.getInstance());//开始响应点击事件
            closeIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    zanDialog.dismiss();
                }
            });
            return zanDialog;
        }
    }
}
