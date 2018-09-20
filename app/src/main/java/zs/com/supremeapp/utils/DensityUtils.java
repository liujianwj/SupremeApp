package zs.com.supremeapp.utils;

import android.util.DisplayMetrics;
import android.util.TypedValue;

import zs.com.supremeapp.SupremeApplication;

/**
 * Created by liujian on 2018/8/13.
 */

public class DensityUtils {

    public static int getScreenWidth(){
        DisplayMetrics dm = SupremeApplication.getInstance().getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getScreenHeight(){
        DisplayMetrics dm = SupremeApplication.getInstance().getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * dp转px
     */
    public static int dip2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, SupremeApplication.getInstance().getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     */
    public static int sp2px(float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, SupremeApplication.getInstance().getResources().getDisplayMetrics());
    }

    /**
     * px转dp
     */
    public static float px2dip(float pxVal) {
        final float scale = SupremeApplication.getInstance().getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }

    /**
     * px转sp
     */
    public static float px2sp(float pxVal) {
        return (pxVal / SupremeApplication.getInstance().getResources().getDisplayMetrics().scaledDensity);
    }

}

