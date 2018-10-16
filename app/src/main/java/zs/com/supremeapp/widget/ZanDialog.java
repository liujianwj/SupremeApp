package zs.com.supremeapp.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

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
        private View.OnClickListener onClickListener;
        private EditText inputZanEt;
        private TextView zan1Tv;
        private TextView zan10Tv;
        private TextView zanTv;
        private SimpleDraweeView headImg;
        private String headImgUri;
        private OnGetMoreZanListener onGetMoreZanListener;


        private int zanNum = 10;
        private int havaZanNum = 0;

        public Builder setOnGetMoreZanListener(OnGetMoreZanListener onGetMoreZanListener) {
            this.onGetMoreZanListener = onGetMoreZanListener;
            return this;
        }

        public Builder setOnClickListener(View.OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
            return this;
        }

        public Builder setHeadImgUri(String headImgUri) {
            this.headImgUri = headImgUri;
            return this;
        }

        public Builder setHavaZanNum(int havaZanNum) {
            this.havaZanNum = havaZanNum;
            return this;
        }

        public Builder(Context context) {
            this.context = context;
        }

        public ZanDialog create(){
            final ZanDialog zanDialog = new ZanDialog(context, R.style.nifty_dialog);

            View dialogView = LayoutInflater.from(context).inflate(R.layout.view_zan_dialog, null);
            zanDialog.addContentView(dialogView, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            zanDialog.setCanceledOnTouchOutside(false);

            moreZanTv = dialogView.findViewById(R.id.more_zan_tv);
            closeIv = dialogView.findViewById(R.id.close_iv);
            inputZanEt = dialogView.findViewById(R.id.inputZanEt);
            zan1Tv = dialogView.findViewById(R.id.zan1Tv);
            zan10Tv = dialogView.findViewById(R.id.zan10Tv);
            zanTv = dialogView.findViewById(R.id.zanTv);
            headImg = dialogView.findViewById(R.id.headImg);
            headImg.setImageURI(headImgUri);
            String text = "您当前总有 " + havaZanNum +" 个点赞机会，如何获取更多机会。";
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
                   // Toast.makeText(context, "hah", Toast.LENGTH_SHORT).show();
                    if(onGetMoreZanListener != null){
                        onGetMoreZanListener.onGetMoreZan();
                    }
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
            zanTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(zanNum <= 0){
                        Toast.makeText(context, "点赞数不能为0", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    zanDialog.dismiss();
                    if(onClickListener != null){
                        view.setTag(zanNum);
                        onClickListener.onClick(view);
                    }
                }
            });
            zan1Tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    zanNum = 1;
                    zanTv.setText("点赞 X " + zanNum);
                    zan1Tv.setBackgroundResource(R.drawable.bg_red_cor_border_5);
                    zan10Tv.setBackgroundResource(R.drawable.bg_grey_cor_5);
                    inputZanEt.setBackgroundResource(R.drawable.bg_grey_cor_5);
                }
            });
            zan10Tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    zanNum = 10;
                    zanTv.setText("点赞 X " + zanNum);
                    zan1Tv.setBackgroundResource(R.drawable.bg_grey_cor_5);
                    zan10Tv.setBackgroundResource(R.drawable.bg_red_cor_border_5);
                    inputZanEt.setBackgroundResource(R.drawable.bg_grey_cor_5);
                }
            });
            inputZanEt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if(editable != null && !TextUtils.isEmpty(editable.toString())){
                        zanNum = Integer.valueOf(editable.toString());
                        zanTv.setText("点赞 X " + zanNum);
                        zan1Tv.setBackgroundResource(R.drawable.bg_grey_cor_5);
                        zan10Tv.setBackgroundResource(R.drawable.bg_grey_cor_5);
                        inputZanEt.setBackgroundResource(R.drawable.bg_red_cor_border_5);
                    }else {
                        zanNum = 0;
                        zanTv.setText("点赞 X " + zanNum);
                        zan1Tv.setBackgroundResource(R.drawable.bg_grey_cor_5);
                        zan10Tv.setBackgroundResource(R.drawable.bg_grey_cor_5);
                        inputZanEt.setBackgroundResource(R.drawable.bg_red_cor_border_5);
                    }
                }
            });
            return zanDialog;
        }
    }

    public interface OnGetMoreZanListener{
        void onGetMoreZan();
    }
}
