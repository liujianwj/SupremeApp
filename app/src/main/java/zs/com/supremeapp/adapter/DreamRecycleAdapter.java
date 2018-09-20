package zs.com.supremeapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import zs.com.supremeapp.R;
import zs.com.supremeapp.model.DreamDO;
import zs.com.supremeapp.widget.CompletedView;

/**
 * Created by liujian on 2018/8/5.
 */

public class DreamRecycleAdapter extends RecyclerView.Adapter<DreamRecycleAdapter.ViewHolder>{

    private Context context;
    private List<DreamDO> data;
    private View.OnClickListener onClickListener;

    public DreamRecycleAdapter(Context context, List<DreamDO> data) {
        this.context = context;
        this.data = data;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_dream_recycle, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DreamDO item = data.get(position);
        holder.userNameTv.setText(item.getUser_name());
        holder.headImg.setImageURI(Uri.parse("res://zs.com.supremeapp/" + R.drawable.tangyan));
       // holder.headImg.setImageURI(item.getUser_avatar());
        holder.dreamContentTv.setText(item.getDream_content());
        holder.dreamTitleTv.setText(item.getDream_title());
        holder.serverHeadImg.setImageURI(item.getUser_avatar());
        holder.completedView.setProgress(25);
        holder.itemLayout.setTag(position);
        holder.itemLayout.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.headImg)
        SimpleDraweeView headImg;
        @BindView(R.id.serverHeadImg)
        SimpleDraweeView serverHeadImg;
        @BindView(R.id.completedView)
        CompletedView completedView;
        @BindView(R.id.itemLayout)
        View itemLayout;
        @BindView(R.id.userNameTv)
        TextView userNameTv;
        @BindView(R.id.dreamContentTv)
        TextView dreamContentTv;
        @BindView(R.id.dreamTitleTv)
        TextView dreamTitleTv;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
