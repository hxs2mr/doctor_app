package microtech.hxswork.com.photopicker.event;


import microtech.hxswork.com.photopicker.entity.Photo;

/**
 */
public interface Selectable {
  /**
   * Indicates if the item at position position is selected
   *
   * @param photo Photo of the item to check
   * @return true if the item is selected, false otherwise
   */

  /**
   * Toggle the selection status of the item at a given position
   *
   * @param photo Photo of the item to toggle the selection status for
   */
  boolean isSelected(Photo photo);

    void toggleSelection(Photo photo);


    /**
   * Clear the selection status for all items
   */
  void clearSelection();

  /**
   * Count the selected items
   *
   * @return Selected items count
   */
  int getSelectedItemCount();

}
