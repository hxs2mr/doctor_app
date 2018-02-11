package microtech.hxswork.com.frame_core.ui;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import microtech.hxswork.com.frame_core.R;
import microtech.hxswork.com.frame_core.util.DimenUtil;


/**
 * Created by microtech on 2017/11/14.Loader方法封装
 */

public class LatteLoader {
    private static final int LOADER_SIZE_SCALE = 8;
    private static final int LOADER_OFFSET_SCALE = 10;
    private static final ArrayList<AppCompatDialog> LOADERS=new ArrayList<>();
    private static  final String DEFAULE_LOADER = LoaderStyle.BallClipRotateMultipleIndicator.name();

    public  static void showLoading(Context context,Enum<LoaderStyle> type){
        showLoading(context,type.name());
    }
    public static void showLoading(Context context,String type){
        final AppCompatDialog dialog = new AppCompatDialog(context, R.style.dialog);
        final AVLoadingIndicatorView avLoadingIndicatorView = LoaderCreator.create(type,context);
        dialog.setContentView(avLoadingIndicatorView);
        int devicewidth  = DimenUtil.getScreenWidth();
        int deviceheigh = DimenUtil.getScreenHeight();
        final Window diaologWindow = dialog.getWindow();
        if(diaologWindow != null)
        {
          final   WindowManager.LayoutParams lp = diaologWindow.getAttributes();
            lp.width = devicewidth/LOADER_SIZE_SCALE;
            lp.height = deviceheigh/LOADER_SIZE_SCALE;
            lp.height = lp.height+deviceheigh/LOADER_OFFSET_SCALE;
            lp.gravity = Gravity.CENTER;
        }
        LOADERS.add(dialog);
        dialog.show();
    }

    public static void showLoading(Context context){//Loader方法重载
        showLoading(context,DEFAULE_LOADER);
    }
    public static void stopLoading(){//关闭Loader
        for (AppCompatDialog dialog:LOADERS){
            if(dialog !=null){
                if(dialog.isShowing()){
                    dialog.cancel();
                }
            }
        }
    }
}
