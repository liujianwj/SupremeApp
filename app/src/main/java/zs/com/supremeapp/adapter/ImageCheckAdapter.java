package zs.com.supremeapp.adapter;

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
import zs.com.supremeapp.widget.BannerViewPagerAdapter;

public class ImageCheckAdapter extends PagerAdapter {
    private Context context;
    private List<BannerDO> imagePaths;
    private int viewHeight;
    private List<SimpleDraweeView> views = new ArrayList<>();
    private BannerViewPagerAdapter.OnPagerItemClickListener onPagerItemClickListener;

    public ImageCheckAdapter(Context context, List<BannerDO> imagePaths) {
        this.context = context;
        this.imagePaths = imagePaths;
        initViews();
    }

    private void initViews(){
        int length = imagePaths.size();
        for (int i = 0; i < length; i++) {
            SimpleDraweeView imageView = (SimpleDraweeView) LayoutInflater.from(context).inflate(R.layout.view_purchase_banner_viewpager, null);
            imageView.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.FIT_XY);
            views.add(imageView);
        }

    }

    public void setOnPagerItemClickListener(BannerViewPagerAdapter.OnPagerItemClickListener onPagerItemClickListener) {
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
        SimpleDraweeView imageView = views.get(position);
        imageView.setImageURI(imagePaths.get(position).getPhotoUrl());
        container.addView(imageView);
        imageView.setTag(position);
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
