package zs.com.supremeapp.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;


import butterknife.BindView;
import zs.com.supremeapp.R;

/**
 * 聊天会话界面
 * Created by liujian on 2018/8/12.
 */

public class ConversationActivity extends BaseActivity implements View.OnClickListener{
    private String TAG = ConversationActivity.class.getSimpleName();

    @BindView(R.id.nameTv)
    TextView nameTv;
    @BindView(R.id.backLayout)
    View backLayout;
    @BindView(R.id.userHomeTv)
    View userHomeTv;

    private String mTargetId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initActivity(R.layout.activity_conversation);
        super.onCreate(savedInstanceState);

        String title = getIntent().getData().getQueryParameter("title");
        mTargetId = getIntent().getData().getQueryParameter("targetId");
        if(!TextUtils.isEmpty(title)){
            nameTv.setText(title);
        }
        backLayout.setOnClickListener(this);
        userHomeTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if(viewId == R.id.backLayout){
            finish();
        }else if(viewId == R.id.userHomeTv){
            String url = "http://app.cw2009.com/s/" + mTargetId + ".html";
            Intent intent = new Intent(this, WebActivity.class);
            intent.putExtra("url", url);
            startActivity(intent);
        }
    }
}
