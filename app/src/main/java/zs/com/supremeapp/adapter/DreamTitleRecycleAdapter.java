package zs.com.supremeapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import zs.com.supremeapp.R;
import zs.com.supremeapp.model.CategoryDO;

/**
 * Created by liujian on 2018/8/5.
 */

public class DreamTitleRecycleAdapter extends RecyclerView.Adapter<DreamTitleRecycleAdapter.ViewHolder>{
    private Context context;
    private List<CategoryDO> data;
    private int selectPosition;
    private View.OnClickListener onClickListener;

    public DreamTitleRecycleAdapter(Context context, List<CategoryDO> data) {
        this.context = context;
        this.data = data;
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }

    public int getSelectPosition() {
        return selectPosition;
    }

    public String getSelectId(){
        return data.get(selectPosition).getId();
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_dream_title_recycle, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.titleTv.setText(data.get(position).getCateName());
        if(position == selectPosition){
            holder.titleTv.setTextColor(context.getResources().getColor(R.color.black));
        }else {
            holder.titleTv.setTextColor(context.getResources().getColor(R.color.grey_title));
        }
        holder.titleTv.setTag(position);
        holder.titleTv.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.titleTv)
        TextView titleTv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
