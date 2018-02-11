package microtech.hxswork.com.frame_core.ui.recyclew;

import android.support.annotation.ColorInt;

import com.choices.divider.DividerItemDecoration;

/**
 * Created by microtech on 2017/11/20.
 */

public class BaseDecoration extends DividerItemDecoration {

    public BaseDecoration(@ColorInt int color, int size) {
        setDividerLookup(new DividerLookupImpl(color,size));
    }
    public static BaseDecoration create(@ColorInt int color,int size){
        return new BaseDecoration(color,size);
    }
}
