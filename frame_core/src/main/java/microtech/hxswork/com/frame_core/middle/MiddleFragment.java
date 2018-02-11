package microtech.hxswork.com.frame_core.middle;

/**
 * Created by microtech on 2017/11/10中间件 相对于下一级fragmnet属于父类
 */

public abstract class MiddleFragment extends PermissionCheckerFragment {

    @SuppressWarnings("unchecked")
    public <T extends MiddleFragment> T getParentFragmen() {
        return (T) getParentFragment();
    }

}
