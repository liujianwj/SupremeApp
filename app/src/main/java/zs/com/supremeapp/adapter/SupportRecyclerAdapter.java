package zs.com.supremeapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import zs.com.supremeapp.R;

/**
 * Created by liujian on 2018/10/13.
 */

public class SupportRecyclerAdapter extends RecyclerView.Adapter<SupportRecyclerAdapter.ViewHolder>{
    private Context context;

    public SupportRecyclerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_support_recycle, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.supportHeader.setImageURI(Uri.parse("res://zs.com.supremeapp/" + R.drawable.tangyan));
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.supportHeader)
        SimpleDraweeView supportHeader;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
