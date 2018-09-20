package zs.com.supremeapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import zs.com.supremeapp.R;

/**
 * Created by liujian on 2018/8/13.
 */

public class FriendCommentTextListAdapter extends BaseAdapter{

    private Context context;

    public FriendCommentTextListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 5;
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
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_friend_comment_text, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        holder.textView.setText("jian：hahahha----"+position);
        return view;
    }

    static class ViewHolder{
        @BindView(R.id.textView)
        TextView textView;

        public ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
