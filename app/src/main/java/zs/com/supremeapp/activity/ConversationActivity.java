package zs.com.supremeapp.activity;


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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initActivity(R.layout.activity_conversation);
        super.onCreate(savedInstanceState);

        String title = getIntent().getData().getQueryParameter("title");
        if(!TextUtils.isEmpty(title)){
            nameTv.setText(title);
        }

    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if(viewId == R.id.backLayout){
            finish();
        }
    }
}
