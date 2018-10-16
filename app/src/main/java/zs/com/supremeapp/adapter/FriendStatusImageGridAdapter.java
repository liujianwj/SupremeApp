package zs.com.supremeapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import zs.com.supremeapp.R;
import zs.com.supremeapp.model.AlbumDO;

/**
 * Created by liujian on 2018/10/16.
 */

public class FriendStatusImageGridAdapter extends BaseAdapter{

    private Context context;
    private List<AlbumDO> albumDOS;

    public FriendStatusImageGridAdapter(Context context, List<AlbumDO> albumDOS) {
        this.context = context;
        this.albumDOS = albumDOS;
    }

    @Override
    public int getCount() {
        return albumDOS.size();
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
            view = LayoutInflater.from(context).inflate(R.layout.item_friend_status_image_grid, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        AlbumDO item = albumDOS.get(position);
        holder.imageView.setImageURI(item.getSource_uri());
        return view;
    }

    static class ViewHolder{

        @BindView(R.id.imageView)
        SimpleDraweeView imageView;

        public ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
