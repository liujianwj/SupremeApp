package zs.com.supremeapp.widget;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import zs.com.supremeapp.R;
import zs.com.supremeapp.model.BannerDO;


/**
 * Created by liujian on 2018/09/23.
 */

public class BannerViewPagerAdapter extends PagerAdapter {
    private Context context;
    private List<BannerDO> imagePaths;
    private int viewHeight;
    private List<SimpleDraweeView> views = new ArrayList<>();
    private OnPagerItemClickListener onPagerItemClickListener;

    public BannerViewPagerAdapter(Context context, List<BannerDO> imagePaths) {
        this.context = context;
        this.imagePaths = imagePaths;
        initViews();
    }

    private void initViews(){
        int length = imagePaths.size();
        if(imagePaths.size() > 1){
            length += 2;
        }
        for (int i = 0; i < length; i++) {
            SimpleDraweeView imageView = (SimpleDraweeView) LayoutInflater.from(context).inflate(R.layout.view_purchase_banner_viewpager, null);
            imageView.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.FIT_XY);
            views.add(imageView);
        }

    }

    public void setOnPagerItemClickListener(OnPagerItemClickListener onPagerItemClickListener) {
        this.onPagerItemClickListener = onPagerItemClickListener;
    }

    public int getDataSize() {
        return imagePaths == null ? 0 : imagePaths.size();
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int pathPos = position - 1;
        if (position == 0) {
            pathPos = imagePaths.size() - 1;
        } else if (position == (views.size() - 1)) {
            pathPos = 0;
        }
        SimpleDraweeView imageView = views.get(position);
        imageView.setImageURI(imagePaths.get(pathPos).getDream_ad_pic());
        container.addView(imageView);
        imageView.setTag(pathPos);
        if(onPagerItemClickListener != null){
            views.get(position).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPagerItemClickListener.onPagerItemClick(v, (int)v.getTag());
                }
            });
        }
        return views.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ImageView view = views.get(position);
        container.removeView(view);
        view.setImageBitmap(null);
    }

    public interface OnPagerItemClickListener{
        void onPagerItemClick(View view, int position);
    }

}
