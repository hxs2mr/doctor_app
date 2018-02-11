package microtech.hxswork.com.frame_ui.photoAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import microtech.hxswork.com.frame_ui.R;
import microtech.hxswork.com.photopicker.utils.AndroidLifecycleUtils;


/**
 * Created by donglua on 15/5/31.
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {
  private static final RequestOptions OPTIONS = new RequestOptions()
          .diskCacheStrategy(DiskCacheStrategy.ALL)
          .centerCrop()
          .placeholder(R.drawable.__picker_ic_photo_black_48dp)
          .error(R.drawable.__picker_ic_broken_image_black_48dp)
          .dontAnimate();
  private ArrayList<String> photoPaths = new ArrayList<String>();
  private LayoutInflater inflater;

  private Context mContext;

  public final static int TYPE_ADD = 1;
  final static int TYPE_PHOTO = 2;

  public final static int MAX = 9;
  private int flage = 0 ;

  public PhotoAdapter(Context mContext, ArrayList<String> photoPaths,int i) {
    this.photoPaths = photoPaths;
    this.mContext = mContext;
    inflater = LayoutInflater.from(mContext);
    this.flage = i;

  }


  @Override
  public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemView = null;
    switch (viewType) {
      case TYPE_ADD:
        itemView = inflater.inflate(R.layout.item_add,parent,false);
        if(flage == 0)
        {
          itemView.setVisibility(View.GONE);
        }else {
          itemView.setVisibility(View.VISIBLE);
        }
        break;
      case TYPE_PHOTO:
        itemView = inflater.inflate(R.layout.__picker_item_photo, parent, false);
        break;
    }
    return new PhotoViewHolder(itemView);
  }


  @Override
  public void onBindViewHolder(final PhotoViewHolder holder, final int position) {

    if (getItemViewType(position) == TYPE_PHOTO) {
     // Uri uri = Uri.fromFile(new File(photoPaths.get(position)));本地文件的操作

      String url = photoPaths.get(position);


      boolean canLoadImage = AndroidLifecycleUtils.canLoadImage(holder.ivPhoto.getContext());


      String new_url = url.substring(0,2);
      System.out.println("********new_url*****"+new_url);

      if (canLoadImage) {
        if(new_url.equals("mk"))
        {
          Glide.with(mContext)
                  .load("http://qn.newmicrotech.cn/"+url+"?imageMogr2/thumbnail/200x/strip/quality/50/format/webp")
                  .apply(OPTIONS)
                  .thumbnail(0.1f)
                  .into(holder.ivPhoto);
        }else {
          Glide.with(mContext)
                  .load(url)
                  .apply(OPTIONS)
                  .thumbnail(0.1f)
                  .into(holder.ivPhoto);
        }
      }
    }
  }

  @Override public int getItemCount() {
    int count = photoPaths.size() + 1;
    if (count > MAX) {
      count = MAX;
    }
    return count;
  }

  @Override
  public int getItemViewType(int position) {
    return (position == photoPaths.size() && position != MAX) ? TYPE_ADD:TYPE_PHOTO;
  }

  public static class PhotoViewHolder extends RecyclerView.ViewHolder {
    private ImageView ivPhoto;
    private View vSelected;
    public PhotoViewHolder(View itemView) {
      super(itemView);
      ivPhoto   = (ImageView) itemView.findViewById(R.id.iv_photo);
      vSelected = itemView.findViewById(R.id.v_selected);
      if (vSelected != null) vSelected.setVisibility(View.GONE);
    }
  }

}
