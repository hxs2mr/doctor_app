package microtech.hxswork.com.photopicker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.fragmentation.SwipeBackLayout;
import me.yokeyword.fragmentation_swipeback.SwipeBackActivity;
import microtech.hxswork.com.photopicker.entity.Photo;
import microtech.hxswork.com.photopicker.event.OnItemCheckListener;
import microtech.hxswork.com.photopicker.fragment.ImagePagerFragment;
import microtech.hxswork.com.photopicker.fragment.PhotoPickerFragment;

import static android.widget.Toast.LENGTH_LONG;
import static microtech.hxswork.com.photopicker.PhotoPicker.DEFAULT_COLUMN_NUMBER;
import static microtech.hxswork.com.photopicker.PhotoPicker.DEFAULT_MAX_COUNT;
import static microtech.hxswork.com.photopicker.PhotoPicker.EXTRA_GRID_COLUMN;
import static microtech.hxswork.com.photopicker.PhotoPicker.EXTRA_MAX_COUNT;
import static microtech.hxswork.com.photopicker.PhotoPicker.EXTRA_ORIGINAL_PHOTOS;
import static microtech.hxswork.com.photopicker.PhotoPicker.EXTRA_PREVIEW_ENABLED;
import static microtech.hxswork.com.photopicker.PhotoPicker.EXTRA_SHOW_CAMERA;
import static microtech.hxswork.com.photopicker.PhotoPicker.EXTRA_SHOW_GIF;
import static microtech.hxswork.com.photopicker.PhotoPicker.KEY_SELECTED_PHOTOS;

public class PhotoPickerActivity extends AppCompatActivity {

  private PhotoPickerFragment pickerFragment;
  private ImagePagerFragment imagePagerFragment;
  private MenuItem menuDoneItem;

  private int maxCount = DEFAULT_MAX_COUNT;

  /** to prevent multiple calls to inflate menu */
  private boolean menuIsInflated = false;

  private boolean showGif = false;
  private int columnNumber = DEFAULT_COLUMN_NUMBER;
  private ArrayList<String> originalPhotos = null;


  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    boolean showCamera      = getIntent().getBooleanExtra(EXTRA_SHOW_CAMERA, true);
    boolean showGif         = getIntent().getBooleanExtra(EXTRA_SHOW_GIF, false);
    boolean previewEnabled  = getIntent().getBooleanExtra(EXTRA_PREVIEW_ENABLED, true);
    setShowGif(showGif);
    setContentView(R.layout.__picker_activity_photo_picker);
    StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this,R.color.__picker_black_41));
    Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(mToolbar);
    setTitle(R.string.__picker_title);
    setTitleColor(Color.parseColor("#ffffff"));
    ActionBar actionBar = getSupportActionBar();

    assert actionBar != null;
    actionBar.setDisplayHomeAsUpEnabled(true);
/*    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      actionBar.setElevation(25);
    }*/

    maxCount = getIntent().getIntExtra(EXTRA_MAX_COUNT, DEFAULT_MAX_COUNT);
    columnNumber = getIntent().getIntExtra(EXTRA_GRID_COLUMN, DEFAULT_COLUMN_NUMBER);
    originalPhotos = getIntent().getStringArrayListExtra(EXTRA_ORIGINAL_PHOTOS);

    pickerFragment = (PhotoPickerFragment) getSupportFragmentManager().findFragmentByTag("tag");
    if (pickerFragment == null) {
      pickerFragment = PhotoPickerFragment
          .newInstance(showCamera, showGif, previewEnabled, columnNumber, maxCount, originalPhotos);
      getSupportFragmentManager()
          .beginTransaction()
          .replace(R.id.container, pickerFragment, "tag")
          .commit();
      getSupportFragmentManager().executePendingTransactions();
    }

/*    getSwipeBackLayout().setEdgeOrientation(SwipeBackLayout.EDGE_LEFT); // EDGE_LEFT(默认),EDGE_ALL
    getSwipeBackLayout().setParallaxOffset(0.5f); // （类iOS）滑动退出视觉差，默认0.3
    setSwipeBackEnable(true); // 是否允许滑动*/

    pickerFragment.getPhotoGridAdapter().setOnItemCheckListener(new OnItemCheckListener() {
      @Override public boolean onItemCheck(int position, Photo photo, final int selectedItemCount) {

        menuDoneItem.setEnabled(selectedItemCount > 0);

        if (maxCount <= 1) {
          List<String> photos = pickerFragment.getPhotoGridAdapter().getSelectedPhotos();
          if (!photos.contains(photo.getPath())) {
            photos.clear();
            pickerFragment.getPhotoGridAdapter().notifyDataSetChanged();
          }
          return true;
        }

        if (selectedItemCount > maxCount) {
          Toast.makeText(getActivity(), getString(R.string.__picker_over_max_count_tips, maxCount),
              LENGTH_LONG).show();
          return false;
        }
        menuDoneItem.setTitle(getString(R.string.__picker_done_with_count, selectedItemCount, maxCount));
        return true;
      }
    });

  }

  /**
   * Overriding this method allows us to run our exit animation first, then exiting
   * the activity when it complete.
   */



  public void addImagePagerFragment(ImagePagerFragment imagePagerFragment) {
    this.imagePagerFragment = imagePagerFragment;
    getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.container, this.imagePagerFragment)
        .addToBackStack(null)
        .commit();
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    if (!menuIsInflated) {
      getMenuInflater().inflate(R.menu.__picker_menu_picker, menu);
      menuDoneItem = menu.findItem(R.id.done);
      if (originalPhotos != null && originalPhotos.size() > 0) {
        menuDoneItem.setEnabled(true);
        menuDoneItem.setTitle(
                getString(R.string.__picker_done_with_count, originalPhotos.size(), maxCount));
      } else {
        menuDoneItem.setEnabled(false);
      }
      menuIsInflated = true;
      return true;
    }
    return false;
  }
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      super.onBackPressed();
      return true;
    }
    if (item.getItemId() == R.id.done) {
      Intent intent = new Intent();
      ArrayList<String> selectedPhotos = pickerFragment.getPhotoGridAdapter().getSelectedPhotoPaths();
      intent.putStringArrayListExtra(KEY_SELECTED_PHOTOS, selectedPhotos);
      setResult(RESULT_OK, intent);
      finish();
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  public PhotoPickerActivity getActivity() {
    return this;
  }

  public boolean isShowGif() {
    return showGif;
  }

  public void setShowGif(boolean showGif) {
    this.showGif = showGif;
  }
}
