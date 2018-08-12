package zs.com.supremeapp.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import java.util.List;

import butterknife.BindView;
import io.rong.imkit.RongContext;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import zs.com.supremeapp.R;
import zs.com.supremeapp.activity.NearbyPeopleActivity;
import zs.com.supremeapp.adapter.ConversationListAdapterEx;
import zs.com.supremeapp.adapter.PagerBaseAdapter;
import zs.com.supremeapp.page.BasePage;
import zs.com.supremeapp.page.ChatListPage;
import zs.com.supremeapp.page.InteractListPage;

/**
 * 聊天
 * Created by liujian on 2018/8/4.
 */

public class ChatFragment extends BaseFragment implements View.OnClickListener{

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.chatChangeTv)
    TextView chatChangeTv;
    @BindView(R.id.interactChangeTv)
    TextView interactChangeTv;
    @BindView(R.id.nearbyTv)
    TextView nearbyTv;

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
        }
    }
}
