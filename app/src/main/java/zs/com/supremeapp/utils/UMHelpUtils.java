package zs.com.supremeapp.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import zs.com.supremeapp.SupremeApplication;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by liujian on 2018/10/16.
 */

public class UMHelpUtils {

    public static void shareWebToWX(final Activity activity, final String url, final String title, final String des, final String pic){

        new ShareAction(activity)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                .addButton("复制链接", "copy_link", "share_copy", "share_copy")
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                        if (share_media == null){
                            //根据key来区分自定义按钮的类型，并进行对应的操作
                            if (snsPlatform.mKeyword.equals("copy_link")){
                                ClipboardManager myClipboard = (ClipboardManager)activity.getSystemService(CLIPBOARD_SERVICE);
                                ClipData myClip = ClipData.newPlainText("url", url);
                                myClipboard.setPrimaryClip(myClip);
                                Toast.makeText(SupremeApplication.getInstance(),"复制成功",Toast.LENGTH_SHORT).show();
                            }

                        }
                        else {//社交平台的分享行为
//                            new ShareAction(activity)
//                                    .setPlatform(share_media)
//                                    .setCallback(shareListener)
//                                    .withText("多平台分享")
//                                    .share();
                            UMImage umImage = new UMImage(activity, pic);
                            UMWeb web = new UMWeb(url);
                            web.setTitle(title);//标题
                            web.setThumb(umImage);  //缩略图
                            web.setDescription(des);//描述
                            new ShareAction(activity)
                                    .setPlatform(share_media)
                                    .withMedia(web)
                                    .setCallback(shareListener)
                                    .share();
                        }
                    }
                })
             //   .setCallback(shareListener)
                .open();
    }

    public static void shareWebToWX(final Activity activity, final String url, final String title,final String des){

        new ShareAction(activity)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                .addButton("复制链接", "copy_link", "share_copy", "share_copy")
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                        if (share_media == null){
                            //根据key来区分自定义按钮的类型，并进行对应的操作
                            if (snsPlatform.mKeyword.equals("copy_link")){
                                ClipboardManager myClipboard = (ClipboardManager)activity.getSystemService(CLIPBOARD_SERVICE);
                                ClipData myClip = ClipData.newPlainText("url", url);
                                myClipboard.setPrimaryClip(myClip);
                                Toast.makeText(SupremeApplication.getInstance(),"复制成功",Toast.LENGTH_SHORT).show();
                            }

                        }
                        else {//社交平台的分享行为
                            UMWeb web = new UMWeb(url);
                            web.setTitle(title);//标题
                            web.setDescription(des);//描述
                            new ShareAction(activity)
                                    .setPlatform(share_media)
                                    .withMedia(web)
                                    .setCallback(shareListener)
                                    .share();
                        }
                    }
                })
             //   .setCallback(shareListener)
                .open();
    }

    private static ShareBoardlistener shareBoardlistener = new  ShareBoardlistener() {

        @Override
        public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {

        }
    };

    private static UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {
        }
        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
          //  Toast.makeText(mContext,"成功了",Toast.LENGTH_LONG).show();
        }
        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
           // Toast.makeText(mContext,"失败"+t.getMessage(),Toast.LENGTH_LONG).show();
        }
        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
          //  Toast.makeText(mContext,"取消了",Toast.LENGTH_LONG).show();
        }
    };

}
