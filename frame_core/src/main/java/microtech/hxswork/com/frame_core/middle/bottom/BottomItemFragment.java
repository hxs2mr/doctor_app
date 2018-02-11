package microtech.hxswork.com.frame_core.middle.bottom;

import android.content.SharedPreferences;
import android.widget.Toast;

import microtech.hxswork.com.frame_core.middle.MiddleFragment;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by microtech on 2017/11/17.
 */

public abstract class BottomItemFragment extends MiddleFragment {
    // 再点一次退出程序时间设置
    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;

    @Override
    public boolean onBackPressedSupport() {

        if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
            _mActivity.finish();
        } else {
            TOUCH_TIME = System.currentTimeMillis();
            Toast.makeText(_mActivity, "再按一次退出", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}
