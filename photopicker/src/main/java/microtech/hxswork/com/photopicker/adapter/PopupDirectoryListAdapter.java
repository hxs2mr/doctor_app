package microtech.hxswork.com.photopicker.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import microtech.hxswork.com.photopicker.R;
import microtech.hxswork.com.photopicker.entity.PhotoDirectory;


/**
 * Created by donglua on 15/6/28.
 */
public class PopupDirectoryListAdapter extends BaseAdapter {

  private final RequestOptions OPTIONS = new RequestOptions()
          .diskCacheStrategy(DiskCacheStrategy.ALL)
          .centerCrop()
          .dontAnimate();
  private List<PhotoDirectory> directories = new ArrayList<>();
  private RequestManager glide;

  public PopupDirectoryListAdapter(RequestManager glide, List<PhotoDirectory> directories) {
    this.directories = directories;
    this.glide = glide;
  }


  @Override public int getCount() {
    return directories.size();
  }


  @Override public PhotoDirectory getItem(int position) {
    return directories.get(position);
  }


  @Override public long getItemId(int position) {
    return directories.get(position).hashCode();
  }


  @Override public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder holder;
    if (convertView == null) {
      LayoutInflater mLayoutInflater = LayoutInflater.from(parent.getContext());
      convertView = mLayoutInflater.inflate(R.layout.__picker_item_directory, parent, false);
      holder = new ViewHolder(convertView);
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }

    holder.bindData(directories.get(position));

    return convertView;
  }

  private class ViewHolder {

    public ImageView ivCover;
    public TextView tvName;
    public TextView tvCount;

    public ViewHolder(View rootView) {
      ivCover = (ImageView) rootView.findViewById(R.id.iv_dir_cover);
      tvName  = (TextView)  rootView.findViewById(R.id.tv_dir_name);
      tvCount = (TextView)  rootView.findViewById(R.id.tv_dir_count);
    }

    public void bindData(PhotoDirectory directory) {
      glide.load(directory.getCoverPath())
              .apply(OPTIONS)
          .thumbnail(0.1f)
          .into(ivCover);
      tvName.setText(directory.getName());
      tvCount.setText(tvCount.getContext().getString(R.string.__picker_image_count, directory.getPhotos().size()));
    }
  }

}
