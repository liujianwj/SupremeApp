package zs.com.supremeapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import zs.com.supremeapp.R;

/**
 * Created by liujian on 2018/8/12.
 */

public class FriendStatusListAdapter extends BaseAdapter{
    private Context context;

    public FriendStatusListAdapter(Context context) {
        this.context = context;
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
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_friend_status_list, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        holder.headImg.setImageURI(Uri.parse("res://zs.com.supremeapp/" + R.drawable.tangyan));
        return view;
    }

    static class ViewHolder{

        @BindView(R.id.headImg)
        SimpleDraweeView headImg;
        @BindView(R.id.commentImg)
        ImageView commentImg;

        public ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
