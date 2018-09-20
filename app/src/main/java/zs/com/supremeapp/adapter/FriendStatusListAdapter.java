package zs.com.supremeapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import zs.com.supremeapp.R;
import zs.com.supremeapp.widget.WidgetFlowLayout;

/**
 * Created by liujian on 2018/8/12.
 */

public class FriendStatusListAdapter extends BaseAdapter {
    private Context context;
    private View.OnClickListener onClickListener;
    private SparseArray<View> itemViews = new SparseArray<>();

    public FriendStatusListAdapter(Context context) {
        this.context = context;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public View getItemView(int position) {
        return itemViews.get(position);
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_friend_status_list, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.headImg.setImageURI(Uri.parse("res://zs.com.supremeapp/" + R.drawable.tangyan));
        holder.commentImg.setTag(position);
        holder.commentImg.setOnClickListener(onClickListener);
        holder.zanLayout.removeAllViews();
        for (int i = 0; i < 7; i++) {
            TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.view_friend_zan, holder.zanLayout, false);
          //  textView.setText(position);
            textView.setText(i == 6 ? "周静" : "周静，");
            holder.zanLayout.addView(textView);
        }
        FriendCommentTextListAdapter friendCommentTextListAdapter = new FriendCommentTextListAdapter(context);
        holder.commentTextListView.setAdapter(friendCommentTextListAdapter);
        itemViews.append(position, view);
        return view;
    }

    static class ViewHolder {

        @BindView(R.id.headImg)
        SimpleDraweeView headImg;
        @BindView(R.id.commentImg)
        ImageView commentImg;
        @BindView(R.id.zanLayout)
        WidgetFlowLayout zanLayout;
        @BindView(R.id.commentTextListView)
        ListView commentTextListView;

        public ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
