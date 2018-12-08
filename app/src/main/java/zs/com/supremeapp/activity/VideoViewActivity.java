package zs.com.supremeapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;
import zs.com.supremeapp.R;

public class VideoViewActivity extends AppCompatActivity {

    @BindView(R.id.playerstandard)
    JZVideoPlayerStandard playerstandard;
    @BindView(R.id.backLayout)
    View backLayout;

   // private String videoUrl = "https://key002.ku6.com/xy/d7b3278e106341908664638ac5e92802.mp4";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);

        ButterKnife.bind(this);

        String videoUrl = getIntent().getStringExtra("videoUrl");

       // videoUrl = "http://rbv01.ku6.com/omtSn0z_PTREtneb3GRtGg.mp4";

        if(!TextUtils.isEmpty(videoUrl)){
            playerstandard.setUp(videoUrl, JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL);
            playerstandard.startVideo();
        }

        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JZVideoPlayer.releaseAllVideos();
                finish();
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }

}
