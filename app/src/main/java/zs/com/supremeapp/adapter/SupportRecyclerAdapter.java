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
import zs.com.supremeapp.model.GetOneZanResultDO;

/**
 * Created by liujian on 2018/10/13.
 */

public class SupportRecyclerAdapter extends RecyclerView.Adapter<SupportRecyclerAdapter.ViewHolder>{
    private Context context;
    private List<GetOneZanResultDO.SupportDO> supportDOList;

    public SupportRecyclerAdapter(Context context, List<GetOneZanResultDO.SupportDO> supportDOList) {
        this.context = context;
        this.supportDOList = supportDOList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_support_recycle, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //holder.supportHeader.setImageURI(Uri.parse("res://zs.com.supremeapp/" + R.drawable.tangyan));
        GetOneZanResultDO.SupportDO item = supportDOList.get(position);
        holder.supportHeader.setImageURI(item.getUser_avatar());
        holder.numberTv.setText(item.getDream_zhan_zhan());
    }

    @Override
    public int getItemCount() {
        return supportDOList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.supportHeader)
        SimpleDraweeView supportHeader;
        @BindView(R.id.numberTv)
        TextView numberTv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
