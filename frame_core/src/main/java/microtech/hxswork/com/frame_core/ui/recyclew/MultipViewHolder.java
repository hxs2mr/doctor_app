package microtech.hxswork.com.frame_core.ui.recyclew;

import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by microtech on 2017/11/20.
 */

public class MultipViewHolder extends BaseViewHolder {

    public MultipViewHolder(View view) {
        super(view);
    }
    public static MultipViewHolder create(View view){
        return new MultipViewHolder(view);
    }
}
