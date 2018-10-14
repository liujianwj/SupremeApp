package zs.com.supremeapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import zs.com.supremeapp.R;
import zs.com.supremeapp.model.DreamCommentDO;
import zs.com.supremeapp.utils.DateUtils;

/**
 * Created by liujian on 2018/10/12.
 */

public class DreamDetailCommentAdapter extends BaseAdapter{

    private Context context;
    private List<DreamCommentDO> commentDOS;

    public DreamDetailCommentAdapter(Context context, List<DreamCommentDO> commentDOS) {
        this.context = context;
        this.commentDOS = commentDOS;
    }

    @Override
    public int getCount() {
        return commentDOS.size();
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
            view = LayoutInflater.from(context).inflate(R.layout.item_dream_detail_comment, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        DreamCommentDO item = commentDOS.get(position);
        holder.nameTv.setText(item.getUser_name() + "：");
        holder.contentTv.setText(item.getDream_comment_comment());
        holder.timeTv.setText(DateUtils.timeStamp2Date(item.getDream_comment_time(), null));
        return view;
    }

    static class ViewHolder{

        @BindView(R.id.nameTv)
        TextView nameTv;
        @BindView(R.id.contentTv)
        TextView contentTv;
        @BindView(R.id.timeTv)
        TextView timeTv;

        public ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
