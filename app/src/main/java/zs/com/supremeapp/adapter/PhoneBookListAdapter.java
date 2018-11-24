package zs.com.supremeapp.adapter;

import android.content.Context;
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
import zs.com.supremeapp.R;
import zs.com.supremeapp.model.ContrastDO;

/**
 * Created by liujian on 2018/11/18.
 */

public class PhoneBookListAdapter extends BaseAdapter{
    private Context context;
    private List<ContrastDO> contrastDOS;
    private View.OnClickListener onClickListener;

    public PhoneBookListAdapter(Context context, List<ContrastDO> contrastDOS) {
        this.context = context;
        this.contrastDOS = contrastDOS;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public int getCount() {
        return contrastDOS.size();
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
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_phone_book_list, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else {
            holder = (ViewHolder)view.getTag();
        }
        ContrastDO item = contrastDOS.get(position);
        holder.nameTv.setText(item.getUser_name());
        holder.phoneTv.setText(item.getUser_mobile());
        if(TextUtils.isEmpty(item.getUid())){
            holder.callIm.setVisibility(View.GONE);
        }else {
            holder.callIm.setTag(position);
            holder.callIm.setVisibility(View.VISIBLE);
            holder.callIm.setOnClickListener(onClickListener);
        }
        holder.headImg.setImageURI(item.getUser_avatar());
        return view;
    }

    static class ViewHolder{
        @BindView(R.id.nameTv)
        TextView nameTv;
        @BindView(R.id.phoneTv)
        TextView phoneTv;
        @BindView(R.id.callIm)
        View callIm;
        @BindView(R.id.headImg)
        SimpleDraweeView headImg;

        public ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
