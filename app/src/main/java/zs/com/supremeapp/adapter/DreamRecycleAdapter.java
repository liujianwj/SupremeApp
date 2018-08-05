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
import zs.com.supremeapp.widget.CompletedView;

/**
 * Created by liujian on 2018/8/5.
 */

public class DreamRecycleAdapter extends RecyclerView.Adapter<DreamRecycleAdapter.ViewHolder>{

    private Context context;
    private List<String> data;
    private View.OnClickListener onClickListener;

    public DreamRecycleAdapter(Context context, List<String> data) {
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
        holder.headImg.setImageURI(Uri.parse("res://zs.com.supremeapp/" + R.drawable.tangyan));
        holder.serverHeadImg.setImageURI(Uri.parse("res://zs.com.supremeapp/" + R.drawable.tangyan));
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

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
