package zs.com.supremeapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import zs.com.supremeapp.R;
import zs.com.supremeapp.utils.DensityUtils;


/**
 * Created by 首乌 on 2017/6/20.
 */

public class DreamImageRecycleAdapter extends RecyclerView.Adapter<DreamImageRecycleAdapter.ViewHolder>{
    private Context context;
    private LayoutInflater layoutInflater;
    private List<String> pics;
    private OnRecycleItemClickListener onRecycleItemClickListener;
    private int width;

    public DreamImageRecycleAdapter(Context context, List<String> pics) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.pics = pics;
        int screenWidth = DensityUtils.getScreenWidth();
        double num = 3.5;
        if(pics != null && pics.size() <= 3){
            num = 3;
        }
        width = (int)(((double)screenWidth)/num) - DensityUtils.dip2px(20);
    }

    public void setOnRecycleItemClickListener(OnRecycleItemClickListener onRecycleItemClickListener) {
        this.onRecycleItemClickListener = onRecycleItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_dream_image, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String pic = pics.get(position);
        holder.image.setImageURI(pic);
        holder.itemView.setTag(position);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)holder.image.getLayoutParams();
        layoutParams.width = width;
        if(position != 0){
            layoutParams.leftMargin = DensityUtils.dip2px(10);
        }
        holder.image.setLayoutParams(layoutParams);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onRecycleItemClickListener != null){
                    onRecycleItemClickListener.onRecycleItemClick(v, (int)v.getTag());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return pics.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        SimpleDraweeView image;
        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageView);
        }
    }

    public interface OnRecycleItemClickListener {
        /**
         * item点击回调
         * @param view
         * @param position
         */
        void onRecycleItemClick(View view, int position);
    }

}
