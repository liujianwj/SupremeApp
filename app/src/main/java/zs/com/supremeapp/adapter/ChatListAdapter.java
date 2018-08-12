package zs.com.supremeapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imlib.model.Conversation;
import zs.com.supremeapp.R;

/**
 * Created by liujian on 2018/8/12.
 */

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder>{
    private Context context;
    private List<Conversation> conversations;

    public ChatListAdapter(Context context, List<Conversation> conversations) {
        this.context = context;
        this.conversations = conversations;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chat_list, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Conversation conversation = conversations.get(position);
        holder.headImg.setImageURI(conversation.getPortraitUrl());
        holder.messageTv.setText(conversation.getLatestMessage().getSearchableWord().get(0));
        holder.nameTv.setText(conversation.getSenderUserName());
        holder.timeTv.setText(String.valueOf(conversation.getReceivedTime()));
    }

    @Override
    public int getItemCount() {
        return conversations.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.headImg)
        SimpleDraweeView headImg;
        @BindView(R.id.nameTv)
        TextView nameTv;
        @BindView(R.id.messageTv)
        TextView messageTv;
        @BindView(R.id.timeTv)
        TextView timeTv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
