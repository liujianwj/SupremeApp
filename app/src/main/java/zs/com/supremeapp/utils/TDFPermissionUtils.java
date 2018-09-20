package zs.com.supremeapp.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限适配工具类
 */
public class TDFPermissionUtils {

    private static int mRequestCode = -1;

    private static OnPermissionListener mOnPermissionListener;

    public interface OnPermissionListener{
        void onPermissionGranted();
        void onPermissionDenied();
    }

    public static void needPermission(android.app.Fragment context, int reqCode, String[] permissions, OnPermissionListener callback) {
        checkPermission(context, reqCode, permissions, callback);
    }

    public static void needPermission(android.support.v4.app.Fragment context, int reqCode, String[] permissions, OnPermissionListener callback) {
        checkPermission(context, reqCode, permissions, callback);
    }


    public static void needPermission(Activity context, int reqCode, String[] permissions, OnPermissionListener callback) {
        checkPermission(context, reqCode, permissions, callback);
    }


    @TargetApi(Build.VERSION_CODES.M)
    private static void checkPermission(Object object, int requestCode, String[] permissions, OnPermissionListener callback) {

        if (!checkCallingObjectSuitability(object)){
            return;
        }
        mOnPermissionListener = callback;
        boolean granted = hasPermission(getContext(object), permissions);
        //检查权限
        if(granted && mOnPermissionListener != null){
            mOnPermissionListener.onPermissionGranted();
        } else {
            List<String> deniedPermissions = getDeniedPermissions(getContext(object), permissions);
            if(deniedPermissions.size() > 0){
                mRequestCode = requestCode;
                if(object instanceof Activity){
                    ((Activity) object).requestPermissions(deniedPermissions
                            .toArray(new String[deniedPermissions.size()]), requestCode);
                }else if(object instanceof android.app.Fragment){
                    ((android.app.Fragment) object).requestPermissions(deniedPermissions
                            .toArray(new String[deniedPermissions.size()]), requestCode);
                }else if(object instanceof android.support.v4.app.Fragment){
                    ((android.support.v4.app.Fragment) object).requestPermissions(deniedPermissions
                            .toArray(new String[deniedPermissions.size()]), requestCode);
                }
            }
        }
    }

    /**
     * 检查所传递对象的正确性
     * @param object 必须为 activity or fragment
     */
    private static boolean checkCallingObjectSuitability(Object object) {
        if (object == null) {
            Log.e("TDFPermissionUtils", "Activity or Fragment should not be null");
            return false;
        }

        boolean isActivity = object instanceof Activity;
        boolean isSupportFragment = object instanceof android.support.v4.app.Fragment;
        boolean isAppFragment = object instanceof android.app.Fragment;

        if(!(isActivity || isSupportFragment || isAppFragment)){
            Log.e("TDFPermissionUtils", "Caller must be an Activity or a Fragment");
            return false;
        }
        return true;
    }

    /**
     * 获取上下文
     * @param object
     * @return
     */
    private static Context getContext(Object object) {
        Context context;
        if(object instanceof android.app.Fragment){
            context = ((android.app.Fragment) object).getActivity();
        }else if(object instanceof android.support.v4.app.Fragment){
            context = ((android.support.v4.app.Fragment) object).getActivity();
        }else{
            context = (Activity) object;
        }
        return context;
    }

    /**
     * 是否有权限
     * @param context
     * @param permissions
     * @return
     */
    private static boolean hasPermission(Context context, String... permissions) {
        //安卓6.0以下
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        for (String permission : permissions) {
            if(ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED){
                return false;
            }
        }
        return true;
    }

    /**
     * 获取权限列表中所有需要授权的权限
     * @param context       上下文
     * @param permissions   权限列表
     * @return
     */
    private static List<String> getDeniedPermissions(Context context, String... permissions){
        List<String> deniedPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if(ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED){
                deniedPermissions.add(permission);
            }
        }
        return deniedPermissions;
    }

    /**
     * 请求权限结果，对应onRequestPermissionsResult()方法。
     * @param requestCode
     * @param grantResults
     */
    public static void onRequestPermissionsResult(int requestCode, int[] grantResults) {
        if(requestCode == mRequestCode && mOnPermissionListener != null){
            if(verifyPermissions(grantResults)){
                mOnPermissionListener.onPermissionGranted();
            } else {
                mOnPermissionListener.onPermissionDenied();
            }
        }
    }

    /**
     * 验证权限是否都已经授权
     * @param grantResults
     * @return
     */
    private static boolean verifyPermissions(int[] grantResults) {
        // 如果请求被取消，则结果数组为空
        if(grantResults.length <= 0)
            return false;
        // 循环判断每个权限是否被拒绝
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}
