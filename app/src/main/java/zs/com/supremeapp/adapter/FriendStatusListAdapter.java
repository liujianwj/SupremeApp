package zs.com.supremeapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import zs.com.supremeapp.R;
import zs.com.supremeapp.activity.DreamDetailActivity;
import zs.com.supremeapp.activity.ImageCheckActivity;
import zs.com.supremeapp.model.AlbumDO;
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
        holder.headImg.setTag(position);
        holder.headImg.setOnClickListener(onClickListener);
        holder.contentLayout.setTag(position);
        holder.contentLayout.setOnClickListener(onClickListener);
        holder.nameTv.setText(item.getUser_name());
        if(!TextUtils.isEmpty(item.getContent())){
            holder.contentTv.setVisibility(View.VISIBLE);
            holder.contentTv.setText(Html.fromHtml(item.getContent()));
        }else {
            holder.contentTv.setVisibility(View.GONE);
        }

        if(!TextUtils.isEmpty(item.getVideo())){
            holder.singleImg.setVisibility(View.VISIBLE);
            holder.imageGridView.setVisibility(View.GONE);
          //  holder.singleImg.setImageURI(item.getVideo());
            holder.singleImg.setTag(item.getVideo());
            holder.singleImg.setOnClickListener(onClickListener);
        }else {
            if(!DataUtils.isListEmpty(item.getAlbum())) {
                holder.singleImg.setVisibility(View.GONE);
                holder.imageGridView.setVisibility(View.VISIBLE);
                FriendStatusImageGridAdapter friendStatusImageGridAdapter = new FriendStatusImageGridAdapter(context, item.getAlbum());
                holder.imageGridView.setAdapter(friendStatusImageGridAdapter);
                holder.imageGridView.setOnItemClickListener(new MyOnItemClickListener(item.getAlbum()));
            }else {
                holder.singleImg.setVisibility(View.GONE);
                holder.imageGridView.setVisibility(View.GONE);
            }
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

    private class MyOnItemClickListener implements AdapterView.OnItemClickListener{
        private ArrayList<String> pics = new ArrayList<>();

        public MyOnItemClickListener(List<AlbumDO> albumDOS) {
            for(AlbumDO item : albumDOS){
                pics.add(item.getSource_url());
            }
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(context, ImageCheckActivity.class);
            intent.putStringArrayListExtra("pics", pics);
            intent.putExtra("position", i);
            context.startActivity(intent);
        }
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
        @BindView(R.id.singleImg)
        SimpleDraweeView singleImg;
        @BindView(R.id.imageGridView)
        GridView imageGridView;
        @BindView(R.id.contentLayout)
        View contentLayout;

        public ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
