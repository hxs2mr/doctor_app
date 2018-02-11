package microtech.hxswork.com.photopicker.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import microtech.hxswork.com.photopicker.R;
import microtech.hxswork.com.photopicker.utils.AndroidLifecycleUtils;


/**
 * Created by donglua on 15/6/21.
 */
public class PhotoPagerAdapter extends PagerAdapter {
  private final RequestOptions OPTIONS = new RequestOptions()
          .diskCacheStrategy(DiskCacheStrategy.ALL)
          .centerCrop()
          .override(800, 800)
          .placeholder(R.drawable.__picker_ic_photo_black_48dp)
          .error(R.drawable.__picker_ic_broken_image_black_48dp)
          .dontAnimate();
  private List<String> paths = new ArrayList<>();
  private RequestManager mGlide;
  private int mFalge;

  public PhotoPagerAdapter(RequestManager glide, List<String> paths,int flage) {
    this.paths = paths;
    this.mGlide = glide;
    this.mFalge = flage;
  }

  @Override public Object instantiateItem(ViewGroup container, int position) {
    final Context context = container.getContext();
    View itemView = LayoutInflater.from(context)
        .inflate(R.layout.__picker_picker_item_pager, container, false);

    final ImageView imageView = (ImageView) itemView.findViewById(R.id.iv_pager);

    final String path = paths.get(position);
    String new_url = path.substring(0,2);
    System.out.println("********new_url*****"+new_url);

    Uri uri = null;
      String image_url = null;
    if(!new_url.equals("mk") )
    {
    if (path.startsWith("http")) {
      uri = Uri.parse(path);
    } else {
      uri = Uri.fromFile(new File(path));
    }
    }else {
      image_url ="http://qn.newmicrotech.cn/"+path+"?imageMogr2/thumbnail/500x/strip/quality/50/format/webp";
    }

    boolean canLoadImage = AndroidLifecycleUtils.canLoadImage(context);

    if (canLoadImage) {
      if (!new_url.equals("mk") ) {
        mGlide.load(uri)
                .thumbnail(0.1f)
                .apply(OPTIONS)
                .into(imageView);
      }else {
        mGlide.load(image_url)
                .thumbnail(0.1f)
                .apply(OPTIONS)
                .into(imageView);
      }
    }

    imageView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        if (context instanceof Activity) {
          if (!((Activity) context).isFinishing()) {
            ((Activity) context).onBackPressed();
          }
        }
      }
    });

    container.addView(itemView);

    return itemView;
  }


  @Override public int getCount() {
    return paths.size();
  }


  @Override public boolean isViewFromObject(View view, Object object) {
    return view == object;
  }


  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {
    container.removeView((View) object);
    mGlide.clear((View) object);
  }

  @Override
  public int getItemPosition (Object object) { return POSITION_NONE; }

}
