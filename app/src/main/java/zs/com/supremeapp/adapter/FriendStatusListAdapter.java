package zs.com.supremeapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import zs.com.supremeapp.R;
import zs.com.supremeapp.model.ZoneDO;
import zs.com.supremeapp.utils.DataUtils;
import zs.com.supremeapp.utils.DateUtils;
import zs.com.supremeapp.widget.WidgetFlowLayout;

/**
 * Created by liujian on 2018/8/12.
 */

public class FriendStatusListAdapter extends BaseAdapter {
    private Context context;
    private View.OnClickListener onClickListener;
    private SparseArray<View> itemViews = new SparseArray<>();
    private List<ZoneDO> zoneDOList;

    public FriendStatusListAdapter(Context context, List<ZoneDO> zoneDOList) {
        this.context = context;
        this.zoneDOList = zoneDOList;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public View getItemView(int position) {
        return itemViews.get(position);
    }

    @Override
    public int getCount() {
        return zoneDOList.size();
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
        ZoneDO item = zoneDOList.get(position);
        holder.headImg.setImageURI(item.getUser_avatar());
        holder.nameTv.setText(item.getUser_name());
        if(!TextUtils.isEmpty(item.getContent())){
            holder.contentTv.setVisibility(View.VISIBLE);
            holder.contentTv.setText(item.getContent());
        }else {
            holder.contentTv.setVisibility(View.GONE);
        }
        holder.commentImg.setTag(position);
        holder.commentImg.setOnClickListener(onClickListener);

        holder.timeTv.setText(DateUtils.getTimeRange(item.getCreatTime()));


        if(!DataUtils.isListEmpty(item.getZhans())){
            holder.zanGroup.setVisibility(View.VISIBLE);
            holder.zanLayout.removeAllViews();
            for (int i = 0; i < item.getZhans().size(); i++) {
                TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.view_friend_zan, holder.zanLayout, false);
                //  textView.setText(position);
                textView.setText(i == item.getZhans().size() - 1 ? item.getZhans().get(i).getUser_name() : item.getZhans().get(i).getUser_name() + "，");
                holder.zanLayout.addView(textView);
            }
        }else {
            holder.zanGroup.setVisibility(View.GONE);
        }
        if(!DataUtils.isListEmpty(item.getCommentslist())){
            holder.commentTextListView.setVisibility(View.VISIBLE);
            FriendCommentTextListAdapter friendCommentTextListAdapter = new FriendCommentTextListAdapter(context, item.getCommentslist());
            holder.commentTextListView.setAdapter(friendCommentTextListAdapter);
        }else {
            holder.commentTextListView.setVisibility(View.GONE);
        }

        if(holder.zanGroup.getVisibility() == View.GONE && holder.commentTextListView.getVisibility() == View.GONE){
            holder.commentGroup.setVisibility(View.GONE);
        }else {
            holder.commentGroup.setVisibility(View.VISIBLE);
        }

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
        @BindView(R.id.nameTv)
        TextView nameTv;
        @BindView(R.id.contentTv)
        TextView contentTv;
        @BindView(R.id.zanGroup)
        View zanGroup;
        @BindView(R.id.commentGroup)
        View commentGroup;
        @BindView(R.id.timeTv)
        TextView timeTv;

        public ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
