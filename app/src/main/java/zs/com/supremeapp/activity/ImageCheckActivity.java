package zs.com.supremeapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import zs.com.supremeapp.R;
import zs.com.supremeapp.adapter.ImageCheckAdapter;
import zs.com.supremeapp.model.BannerDO;
import zs.com.supremeapp.widget.BannerViewPagerAdapter;

public class ImageCheckActivity extends BaseActivity {

    @BindView(R.id.banner_viewpager)
    ViewPager bannerViewpager;
    @BindView(R.id.indicatorLayout)
    LinearLayout indicatorLayout;
    @BindView(R.id.backLayout)
    View backLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initActivity(R.layout.activity_image_check);
        super.onCreate(savedInstanceState);

        ArrayList<String> pics = getIntent().getStringArrayListExtra("pics");
        int position = getIntent().getIntExtra("position", 1);
        List<BannerDO> bannerDOS = new ArrayList<>();
        for (String pic : pics) {
            BannerDO bannerDO = new BannerDO();
            bannerDO.setPhotoUrl(pic);
            bannerDOS.add(bannerDO);
        }

        ImageCheckAdapter bannerViewPagerAdapter = new ImageCheckAdapter(this, bannerDOS);
        bannerViewPagerAdapter.setOnPagerItemClickListener(new BannerViewPagerAdapter.OnPagerItemClickListener() {
            @Override
            public void onPagerItemClick(View view, int position) {
                // BannerDO bannerVo = bannerList.get(position);
                //  goCampaignList(bannerVo);
            }
        });
        setIndexIndicatorView(bannerDOS);
        bannerViewpager.setAdapter(bannerViewPagerAdapter);
        bannerViewpager.setCurrentItem(position);
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 设置圆点指示器
     */
    public void setIndexIndicatorView(List<BannerDO> bannerDOS) {
        for (int i = 0; i < bannerDOS.size(); i++) {
            ImageView imageView = new ImageView(this);
            if (i == 0) {
                imageView.setImageResource(R.drawable.buy_circle_white90_bg);
            } else {
                imageView.setImageResource(R.drawable.buy_circle_white30_bg);
            }
            indicatorLayout.addView(imageView);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();
            layoutParams.width = dip2px(this, 8);
            layoutParams.height = dip2px(this, 8);
            layoutParams.leftMargin = dip2px(this, 10);
            imageView.setLayoutParams(layoutParams);
        }

        bannerViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (indicatorLayout.getChildCount() > 0) {
                    for (int i = 0; i < indicatorLayout.getChildCount(); i++) {
                        ImageView imageView = (ImageView) indicatorLayout.getChildAt(i);
                        imageView.setImageResource(R.drawable.buy_circle_white30_bg);
                    }
                    ImageView imageView = (ImageView) indicatorLayout.getChildAt(position);
                    imageView.setImageResource(R.drawable.buy_circle_white90_bg);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
