package microtech.hxswork.com.frame_core.middle;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import java.util.jar.Manifest;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by microtech on 2017/11/10.//权限检查
 */
@RuntimePermissions
public abstract class PermissionCheckerFragment extends BaseFragment {

    //不是直接调用的方法
    @NeedsPermission(android.Manifest.permission.CAMERA)
    void startCanera(){

    }


    @SuppressLint("NeedOnRequestPermissionsResult")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    void startScan(BaseFragment fragment){

    }

}
