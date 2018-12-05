package zs.com.supremeapp.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by liujian on 2018/9/7.
 */

public class DataUtils {

    public static <T> boolean isListEmpty(List<T> list){
        return (list == null || list.isEmpty());
    }

    public static String nullToEmpty(String value){
        if(value == null){
            return "";
        }
        return value;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static  Bitmap createVideoThumbnail(String url, int width, int height) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        int kind = MediaStore.Video.Thumbnails.MINI_KIND;
        try {
            // if (Build.VERSION.SDK_INT >= 14) {
            retriever.setDataSource(url, new HashMap<String, String>());
//            } else {
//                retriever.setDataSource(url);
//            }
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }
        if (kind == MediaStore.Images.Thumbnails.MICRO_KIND && bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }

    public static void loadImage(final String url, final int width, final int height, final CircleImageView circleImageView, final Activity context){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = createVideoThumbnail(url, width, height);
//                Message message = Message.obtain();
//                message.arg1 = 8888;
//                message.obj = bitmap;
//                handler.sendMessage(message);
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        circleImageView.setImageBitmap(bitmap);
                    }
                });
            }
        });
    }
}
