package microtech.hxswork.com.frame_core.net.download;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

import java.io.File;
import java.io.InputStream;

import microtech.hxswork.com.frame_core.init.Frame;
import microtech.hxswork.com.frame_core.net.callback.IRequest;
import microtech.hxswork.com.frame_core.net.callback.ISuccess;
import microtech.hxswork.com.frame_core.util.file.FileUtil;
import okhttp3.ResponseBody;

/**
 * Created by microtech on 2017/11/14.
 */

public class SaveFileTask extends AsyncTask<Object,Void,File> {
    private final IRequest REQUEST;
    private final ISuccess SUCCESS;

    public SaveFileTask(IRequest REQUEST, ISuccess SUCCESS) {
        this.REQUEST = REQUEST;
        this.SUCCESS = SUCCESS;
    }

    @Override
    protected File doInBackground(Object... objects) {
        String downloadDir = (String) objects[0];
        String extension = (String) objects[1];
        final ResponseBody body = (ResponseBody) objects[2];
        String name = (String) objects[3];
        final InputStream is = body.byteStream();
        if(downloadDir == null || downloadDir.equals("")){
            downloadDir = "down_loads";
        }
        if(extension == null || extension.equals(""))
        {
            extension ="";
        }
        if(name == null){
            return FileUtil.writeToDisk(is,downloadDir,extension.toUpperCase(),extension);
        }else {
            return FileUtil.writeToDisk(is,downloadDir,name);
        }
    }

    @Override
    protected void onPostExecute(File file) {//执行完一布回到主线程的操作
        super.onPostExecute(file);
        if(SUCCESS !=null){
            SUCCESS.onSuccess(file.getPath());
        }
        if(REQUEST!= null){
            REQUEST.onRequestEnd();
        }
        autoInstallApk(file);
    }
    private void autoInstallApk(File file){//更新app方法
        if(FileUtil.getExtension(file.getPath()).equals("apk"));
        final Intent install = new Intent();
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        install.setAction(Intent.ACTION_VIEW);
        install.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
        Frame.getApplicationContext().startActivity(install);
    }
}
