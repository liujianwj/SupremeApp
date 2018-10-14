package zs.com.supremeapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import zs.com.supremeapp.R;
import zs.com.supremeapp.model.CommentDO;
import zs.com.supremeapp.model.MessageDO;
import zs.com.supremeapp.model.NewmsgsResultDO;
import zs.com.supremeapp.model.ZanDO;
import zs.com.supremeapp.utils.DataUtils;
import zs.com.supremeapp.utils.DateUtils;

/**
 * Created by liujian on 2018/10/11.
 */

public class FriendNewsListAdapter extends BaseAdapter{

    private Context context;
    private NewmsgsResultDO newmsgsResultDO;

    public FriendNewsListAdapter(Context context, NewmsgsResultDO newmsgsResultDO) {
        this.context = context;
        this.newmsgsResultDO = newmsgsResultDO;
    }

    @Override
    public int getCount() {
        int count = 0;
        if(!DataUtils.isListEmpty(newmsgsResultDO.getZhans())){
            count += newmsgsResultDO.getZhans().size();
        }
        if(!DataUtils.isListEmpty(newmsgsResultDO.getMcms_comments())){
            count += newmsgsResultDO.getMcms_comments().size();
        }
        return count;
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
            view = LayoutInflater.from(context).inflate(R.layout.view_friend_news_list, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        if(!DataUtils.isListEmpty(newmsgsResultDO.getZhans()) && position < newmsgsResultDO.getZhans().size()){
            ZanDO zanDO = newmsgsResultDO.getZhans().get(position);
            holder.headImg.setImageURI(zanDO.getUser_avatar());
            holder.nameTv.setText(zanDO.getUser_name());
            holder.contentTv.setVisibility(View.GONE);
            holder.zanImg.setVisibility(View.VISIBLE);
          //  holder.timeTv.setText(DateUtils.getTimeRange(zanDO.getCreatTime()));
        }else {
            int pos = position - (DataUtils.isListEmpty(newmsgsResultDO.getZhans()) ? 0 : newmsgsResultDO.getZhans().size());
            CommentDO commentDO = newmsgsResultDO.getMcms_comments().get(pos);
            holder.headImg.setImageURI(commentDO.getUser_avatar());
            holder.nameTv.setText(commentDO.getUser_name());
            holder.contentTv.setVisibility(View.VISIBLE);
            holder.contentTv.setText(commentDO.getComment_content());
            holder.zanImg.setVisibility(View.GONE);
            holder.timeTv.setText(commentDO.getComment_time());
        }
        return view;
    }

    static class ViewHolder{

        @BindView(R.id.headImg)
        SimpleDraweeView headImg;
        @BindView(R.id.nameTv)
        TextView nameTv;
        @BindView(R.id.contentTv)
        TextView contentTv;
        @BindView(R.id.zanImg)
        ImageView zanImg;
        @BindView(R.id.timeTv)
        TextView timeTv;

        public ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
