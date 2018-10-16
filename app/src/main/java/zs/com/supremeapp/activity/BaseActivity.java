package zs.com.supremeapp.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import zs.com.supremeapp.R;
import zs.com.supremeapp.manager.ActivityStackManager;
import zs.com.supremeapp.utils.ProcessDialogUtils;
import zs.com.supremeapp.utils.TDFPermissionUtils;

/**
 * activity基类
 * Created by liujian on 2018/8/4.
 */

public class BaseActivity extends FragmentActivity {

    private FrameLayout contentLayout;
    private View titleLayout;

    private int titleResourceId = -1;
    private int contentViewResourceId = -1;

    protected ProcessDialogUtils processDialogUtils;

    protected void initActivity(int titleResourceId, int contentViewResourceId){
        this.titleResourceId = titleResourceId;
        this.contentViewResourceId = contentViewResourceId;
    }

    protected void initActivity(int contentViewResourceId){
        this.contentViewResourceId = contentViewResourceId;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStackManager.getInstance().addStackActivity(this);
        setContentView(R.layout.activity_base);
        contentLayout = findViewById(R.id.contentLayout);
        attachContentView();
        ButterKnife.bind(this);
        processDialogUtils = new ProcessDialogUtils(this);
    }

    protected void showProcessDialog(boolean isShow){
        showProcessDialog(isShow, ProcessDialogUtils.LOAD_TYPE_COMMON);
    }

    protected void showProcessDialog(boolean isShow, Integer processType){
        if(isShow){
            processDialogUtils.createAndShow("正在加载", processType, ProcessDialogUtils.DEFAULT_SHOW_TIME, false, false);
        }else {
            processDialogUtils.dismissDialog();
        }

    }

    private void attachContentView(){
        if(contentViewResourceId != -1){
            View contentView = getLayoutInflater().inflate(contentViewResourceId, contentLayout, true);
        }
        titleLayout = findViewById(R.id.titleLayout);
        if(titleResourceId != -1){

        }else {
            titleLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        TDFPermissionUtils.onRequestPermissionsResult(requestCode, grantResults);
    }

    @Override
    protected void onDestroy() {
        processDialogUtils.dismissDialog();
        ActivityStackManager.getInstance().popActivity(this);
        super.onDestroy();
    }
}
