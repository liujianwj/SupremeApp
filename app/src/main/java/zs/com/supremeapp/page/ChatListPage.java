package zs.com.supremeapp.page;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import zs.com.supremeapp.R;
import zs.com.supremeapp.adapter.ChatListAdapter;

/**
 * 聊天列表页面
 * Created by liujian on 2018/8/6.
 */

public class ChatListPage extends BasePage{

    @BindView(R.id.chatRecyclerView)
    RecyclerView chatRecyclerView;

    private ChatListAdapter chatListAdapter;
    private List<Conversation> conversations;

    public ChatListPage(Context context) {
        super(context);
    }

    @Override
    public View initView(LayoutInflater layoutInflater) {
        View view = layoutInflater.inflate(R.layout.view_chat_list, null);
        ButterKnife.bind(this, view);

        conversations = new ArrayList<>();
        chatListAdapter = new ChatListAdapter(context, conversations);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        chatRecyclerView.setLayoutManager(layoutManager);
        chatRecyclerView.setAdapter(chatListAdapter);
        return view;
    }

    @Override
    public void initData() {
        jianConnect();
    }

    public void jianConnect(){
        String token = "PjsSLMYPo9cyCjtMilWw3aYSYOP9+As0ItQwtH16mltTrcQONwKz8kvo1GGyckS4fxfOXT0xUc71W9/69JF+9Q==";
        RongIMClient.connect(token, new RongIMClient.ConnectCallback() {
            /**
             * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
             *                            2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
             */
            @Override
            public void onTokenIncorrect() {
                Toast.makeText(context, "Token 错误", Toast.LENGTH_SHORT).show();
            }

            /**
             * 连接融云成功
             * @param userid 当前 token 对应的用户 id
             */
            @Override
            public void onSuccess(String userid) {
                Toast.makeText(context, userid + "连接成功", Toast.LENGTH_SHORT).show();
                getConversation();
            }

            /**
             * 连接融云失败
             * @param errorCode 错误码，可到官网 查看错误码对应的注释
             */
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Toast.makeText(context, "连接失败" + errorCode, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getConversation(){
        /**
         * <p> 获取当前用户本地会话列表的默认方法，该方法返回的是以下类型的会话列表：私聊，群组，系统会话。如果
         * 您需要获取其它类型的会话列表,可以使用{@link #getConversationList(ResultCallback, Conversation.ConversationType...)} 方法。
         * <strong>注意：</strong>当更换设备或者清除缓存后，拉取到的是暂存在融云服务器中该账号当天收发过消息的会话列表。</p>
         *
         * @param callback 获取会话列表的回调。
         */
        RongIMClient.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversationList) {
                if(conversationList != null){
//                    for(Conversation conversation : conversations){
//                        Log.d("conversation", conversation.getLatestMessage().toString());
//                    }
                    conversations.clear();
                    conversations.addAll(conversationList);
                    chatListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.d("onError", errorCode.getMessage());
            }
        });
    }
}
