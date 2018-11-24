package zs.com.supremeapp.adapter;

import android.content.Context;
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
import zs.com.supremeapp.model.DynamicsResultDO;
import zs.com.supremeapp.utils.DateUtils;

/**
 * Created by liujian on 2018/10/16.
 */

public class InteractListAdapter extends RecyclerView.Adapter<InteractListAdapter.ViewHolder>{

    private Context context;
    private List<DynamicsResultDO.DynamicsDO> dynamicsDOList;
    private View.OnClickListener onClickListener;

    public InteractListAdapter(Context context, List<DynamicsResultDO.DynamicsDO> dynamicsDOList) {
        this.context = context;
        this.dynamicsDOList = dynamicsDOList;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_interact_list, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DynamicsResultDO.DynamicsDO item = dynamicsDOList.get(position);
        holder.headImg.setImageURI(item.getUser_avatar());
        holder.nameTv.setText(item.getUser_name());
        holder.companyTv.setText(item.getUser_company());
        holder.timeTv.setText(DateUtils.toDate(item.getTime(), DateUtils.DATE_FORMAT0));
        holder.itemLayout.setTag(position);
        holder.itemLayout.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return dynamicsDOList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.headImg)
        SimpleDraweeView headImg;
        @BindView(R.id.nameTv)
        TextView nameTv;
        @BindView(R.id.companyTv)
        TextView companyTv;
        @BindView(R.id.timeTv)
        TextView timeTv;
        @BindView(R.id.itemLayout)
        View itemLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
