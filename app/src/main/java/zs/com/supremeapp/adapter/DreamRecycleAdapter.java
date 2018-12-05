package zs.com.supremeapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import zs.com.supremeapp.R;
import zs.com.supremeapp.model.DreamDO;
import zs.com.supremeapp.utils.DataUtils;
import zs.com.supremeapp.utils.DateUtils;
import zs.com.supremeapp.utils.DensityUtils;
import zs.com.supremeapp.widget.CompletedView;

/**
 * Created by liujian on 2018/8/5.
 */

public class DreamRecycleAdapter extends BaseAdapter {

    private Activity context;
    private List<DreamDO> data;

    public DreamRecycleAdapter(Activity context, List<DreamDO> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_dream_recycle, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        DreamDO item = data.get(position);
        holder.userNameTv.setText(item.getUser_name());
        if(!TextUtils.isEmpty(item.getDream_video())){
            holder.headImg.setVisibility(View.VISIBLE);
            holder.dreamImg.setVisibility(View.GONE);
          //  DataUtils.loadImage(item.getDream_video(), DensityUtils.dip2px(70), DensityUtils.dip2px(70), holder.headImg, context);
         //   holder.headImg.setImageBitmap(DataUtils.createVideoThumbnail(item.getDream_video(), DensityUtils.dip2px(70), DensityUtils.dip2px(70)));
        }else {
            holder.headImg.setVisibility(View.GONE);
            holder.dreamImg.setVisibility(View.VISIBLE);
            holder.dreamImg.setImageURI(item.getDream_pic());
        }
        if(TextUtils.equals(item.getDream_status(), "1")){
            holder.userLayout.setVisibility(View.VISIBLE);
        }else {
            holder.userLayout.setVisibility(View.GONE);
        }
        holder.dreamContentTv.setText(item.getDream_content());
        holder.dreamTitleTv.setText(item.getDream_title());
        holder.serverHeadImg.setImageURI(item.getUser_avatar());
        if(!TextUtils.isEmpty(item.getDream_endday())){
            holder.timeTv.setVisibility(View.VISIBLE);
            //  holder.timeTv.setText(DateUtils.toDate(Long.parseLong(item.getDream_endday()), DateUtils.DATE_FORMAT0));
            holder.timeTv.setText(item.getDream_endday());
        }else {
            holder.timeTv.setVisibility(View.GONE);
        }
        holder.zanTv.setText(context.getResources().getString(R.string.zan_num, item.getDream_zhan()));
        holder.zanTargetTv.setText(context.getResources().getString(R.string.zan_target_num, item.getDream_target_zhan()));
        int process = 0;
        try{
            int zanNum = Integer.valueOf(item.getDream_zhan());
            int zanTargetNum = Integer.valueOf(item.getDream_target_zhan());
            if(zanTargetNum != 0){
                process = zanNum * 100 /zanTargetNum;
                process = process > 100 ? 100 : process;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        holder.completedView.setProgress(process);
        holder.processTv.setText(process + "%");
        return view;
    }

    static class ViewHolder{

        @BindView(R.id.headImg)
        CircleImageView headImg;
        @BindView(R.id.dreamImg)
        SimpleDraweeView dreamImg;
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
        @BindView(R.id.timeTv)
        TextView timeTv;
        @BindView(R.id.zanTv)
        TextView zanTv;
        @BindView(R.id.zanTargetTv)
        TextView zanTargetTv;
        @BindView(R.id.processTv)
        TextView processTv;
        @BindView(R.id.userLayout)
        View userLayout;

        ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
