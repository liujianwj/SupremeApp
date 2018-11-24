package zs.com.supremeapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import zs.com.supremeapp.R;
import zs.com.supremeapp.adapter.PhoneBookListAdapter;
import zs.com.supremeapp.model.ContrastDO;
import zs.com.supremeapp.utils.SerializeToFlatByte;

/**
 *
 * Created by liujian on 2018/11/18.
 */

public class PhoneBookActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.backLayout)
    View backLayout;

    private List<ContrastDO> contrastDOS;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.initActivity(R.layout.activity_phone_book);
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        contrastDOS = new ArrayList<>();
        if (bundle != null) {
            Object object = SerializeToFlatByte.restoreObject(getIntent().getExtras().getByteArray("list"));
            if (object != null) {
                contrastDOS = (ArrayList<ContrastDO>) object;
            }
        }
        backLayout.setOnClickListener(this);

        PhoneBookListAdapter phoneBookListAdapter = new PhoneBookListAdapter(this, contrastDOS);
        phoneBookListAdapter.setOnClickListener(this);
        listView.setAdapter(phoneBookListAdapter);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if(viewId == R.id.backLayout){
            finish();
        }else if(viewId == R.id.callIm){
            int pos = (Integer) view.getTag();
            ContrastDO contrastDO = contrastDOS.get(pos);
            RongIM.getInstance().startConversation(this, Conversation.ConversationType.PRIVATE, contrastDO.getUid(), contrastDO.getUser_name());
        }
    }
}
