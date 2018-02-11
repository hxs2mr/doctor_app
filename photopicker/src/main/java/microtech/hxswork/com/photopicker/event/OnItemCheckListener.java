package microtech.hxswork.com.photopicker.event;


import microtech.hxswork.com.photopicker.entity.Photo;

public interface OnItemCheckListener {

  boolean onItemCheck(int position, Photo path, int selectedItemCount);
}
