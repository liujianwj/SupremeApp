package zs.com.supremeapp.fragment;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.rong.imkit.RongContext;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import zs.com.supremeapp.R;
import zs.com.supremeapp.activity.LoginActivity;
import zs.com.supremeapp.activity.NearbyPeopleActivity;
import zs.com.supremeapp.adapter.ConversationListAdapterEx;
import zs.com.supremeapp.adapter.PagerBaseAdapter;
import zs.com.supremeapp.api.ChatApi;
import zs.com.supremeapp.manager.Platform;
import zs.com.supremeapp.model.ContrastResultDO;
import zs.com.supremeapp.network.INetWorkCallback;
import zs.com.supremeapp.page.BasePage;
import zs.com.supremeapp.page.ChatListPage;
import zs.com.supremeapp.page.InteractListPage;
import zs.com.supremeapp.utils.DataUtils;
import zs.com.supremeapp.utils.TDFPermissionUtils;
import zs.com.supremeapp.widget.PhoneBookDialog;

/**
 * 聊天
 * Created by liujian on 2018/8/4.
 */

public class ChatFragment extends BaseFragment implements View.OnClickListener{

    private static final int CONTACTS_PERMISSION_REQUEST_CODE = 888;

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.chatChangeTv)
    TextView chatChangeTv;
    @BindView(R.id.interactChangeTv)
    TextView interactChangeTv;
    @BindView(R.id.nearbyTv)
    TextView nearbyTv;
    @BindView(R.id.phone_book_tv)
    TextView phoneBookTv;

  //  private List<BasePage> mPageList = new ArrayList<>();

    private List<Fragment> mFragment = new ArrayList<>();
    private Conversation.ConversationType[] mConversationsTypes = null;

    /**
     * 会话列表的fragment
     */
    private ConversationListFragment mConversationListFragment = null;

    /**
     * userId  654321  用户在 App 中的唯一标识码
     * name    jian    用户名称
     * portraitUri  https://goss.veer.com/creative/vcg/veer/800water/veer-133566041.jpg  用户头像 URI
     *
     *
     *
     *{"code":200,"userId":"654321","token":"PjsSLMYPo9cyCjtMilWw3aYSYOP9+As0ItQwtH16mltTrcQONwKz8kvo1GGyckS4fxfOXT0xUc71W9/69JF+9Q=="}
     */

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mContentView == null){
            super.initFragment(R.layout.fragment_chat);
            return super.onCreateView(inflater, container, savedInstanceState);
        }
        return mContentView;
    }

    @Override
    void initView() {
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                mPageList.get(position).initData();
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
        chatChangeTv.setOnClickListener(this);
        interactChangeTv.setOnClickListener(this);
        nearbyTv.setOnClickListener(this);
        phoneBookTv.setOnClickListener(this);
    }

    @Override
    void initData() {
//        ChatListPage chatListPage = new ChatListPage(mContext);
//        mPageList.add(chatListPage);
//        InteractListPage interactListPage = new InteractListPage(mContext);
//        mPageList.add(interactListPage);
//        PagerBaseAdapter pagerBaseAdapter = new PagerBaseAdapter(mPageList);
//        viewPager.setAdapter(pagerBaseAdapter);

        Fragment conversationList = initConversationList();
        mFragment.add(conversationList);
        mFragment.add(new MineFragment());
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getActivity().getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragment.get(position);
            }

            @Override
            public int getCount() {
                return mFragment.size();
            }
        };
        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.setOffscreenPageLimit(2);
    }

    private Fragment initConversationList() {
        if (mConversationListFragment == null) {
            ConversationListFragment listFragment = new ConversationListFragment();
            RongContext.init(mContext);
            listFragment.setAdapter(new ConversationListAdapterEx(RongContext.getInstance()));
            Uri uri;

            uri = Uri.parse("rong://" + mContext.getApplicationInfo().packageName).buildUpon()
                    .appendPath("conversationlist")
                    .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                    .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
                    .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                    .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                    .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//系统
                    .build();
            mConversationsTypes = new Conversation.ConversationType[]{Conversation.ConversationType.PRIVATE,
                    Conversation.ConversationType.GROUP,
                    Conversation.ConversationType.PUBLIC_SERVICE,
                    Conversation.ConversationType.APP_PUBLIC_SERVICE,
                    Conversation.ConversationType.SYSTEM
            };
            listFragment.setUri(uri);
            mConversationListFragment = listFragment;
            return listFragment;
        } else {
            return mConversationListFragment;
        }
    }






    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if(viewId == R.id.chatChangeTv){ //聊天
            chatChangeTv.setBackgroundResource(R.drawable.bg_white_left_cor_5);
            chatChangeTv.setTextColor(mContext.getResources().getColor(R.color.red_07));
            interactChangeTv.setBackgroundResource(R.drawable.bg_red_right_cor_5);
            interactChangeTv.setTextColor(mContext.getResources().getColor(R.color.white));
            viewPager.setCurrentItem(0);
        }else if(viewId == R.id.interactChangeTv){ //互动
            chatChangeTv.setBackgroundResource(R.drawable.bg_red_left_cor_5);
            chatChangeTv.setTextColor(mContext.getResources().getColor(R.color.white));
            interactChangeTv.setBackgroundResource(R.drawable.bg_white_right_cor_5);
            interactChangeTv.setTextColor(mContext.getResources().getColor(R.color.red_07));
            viewPager.setCurrentItem(1);
        }else if(viewId == R.id.nearbyTv){ //附近的人
            startActivity(new Intent(getActivity(), NearbyPeopleActivity.class));
        }else if(viewId == R.id.phone_book_tv){ //通讯录
            PhoneBookDialog phoneBookDialog = new PhoneBookDialog.Builder(mContext).setOnClickListener(this).create();
            phoneBookDialog.show();
        }else if(viewId == R.id.match_phone_tv){ //匹配本地通讯录
            showProcessDialog(true);
            TDFPermissionUtils.needPermission(this, CONTACTS_PERMISSION_REQUEST_CODE,
                    new String[]{android.Manifest.permission.READ_CONTACTS}, new TDFPermissionUtils.OnPermissionListener() {
                @Override
                public void onPermissionGranted() {

                    String[] contacts = getContacts();
                    StringBuilder mobiles = new StringBuilder();
                    Log.d("contacts", contacts.toString());
                    Map<String, String> params = new HashMap<>();
                    for(int i = 0 ; i < contacts.length; i++){
                        mobiles.append(contacts[i]);
                        if(i < contacts.length - 1){
                            mobiles.append(",");
                        }
                    }
                    params.put("mobiles", mobiles.toString());
                    params.put("userid", Platform.getInstance().getUsrId());
                    new ChatApi().getContrast(params, new INetWorkCallback<ContrastResultDO>() {
                        @Override
                        public void success(ContrastResultDO contrastResultDO, Object... objects) {
                            showProcessDialog(false);
                            if(contrastResultDO != null && !DataUtils.isListEmpty(contrastResultDO.getList())){
                                Toast.makeText(getActivity(), "已同步"+contrastResultDO.getList().size()+"人", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void failure(int errorCode, String message) {
                            showProcessDialog(false);
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onPermissionDenied() {
                    showProcessDialog(false);
                    Toast.makeText(getContext(), "获取通讯录服务未开启，请设置开启", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private String[] getContacts() {
        //联系人的Uri，也就是content://com.android.contacts/contacts
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        //指定获取_id和display_name两列数据，display_name即为姓名
        String[] projection = new String[] {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME
        };
        //根据Uri查询相应的ContentProvider，cursor为获取到的数据集
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if(cursor == null){
            return new String[]{};
        }
        String[] arr = new String[cursor.getCount()];
        int i = 0;
        if (cursor.moveToFirst()) {
            do {
                Long id = cursor.getLong(0);
                //获取姓名
                String name = cursor.getString(1);
                //指定获取NUMBER这一列数据
                String[] phoneProjection = new String[] {
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                };
             //   arr[i] = id + " , 姓名：" + name;

                //根据联系人的ID获取此人的电话号码
                Cursor phonesCusor = getActivity().getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        phoneProjection,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id,
                        null,
                        null);

                //因为每个联系人可能有多个电话号码，所以需要遍历
                if (phonesCusor != null && phonesCusor.moveToFirst()) {
                    do {
                        String num = phonesCusor.getString(0);
                        arr[i] = num;
                    }while (phonesCusor.moveToNext());
                }
                i++;
            } while (cursor.moveToNext());
        }
        return arr;
    }

}
