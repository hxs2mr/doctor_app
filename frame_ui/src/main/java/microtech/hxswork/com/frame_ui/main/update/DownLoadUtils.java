package microtech.hxswork.com.frame_ui.main.update;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;

/**
 * 封装 DownLoadManager 下载
 * Created by Song on 2016/11/2.
 */
public class DownLoadUtils {

    private Context mContext;
    private DownloadManager mDownloadManager;
    private static volatile DownLoadUtils instance;

    private DownLoadUtils(Context context) {
        this.mContext = context.getApplicationContext();
        mDownloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    /**
     * 获取单例对象
     * @param context
     * @return
     */
    public static DownLoadUtils getInstance(Context context) {

        if(instance == null) {
            synchronized (DownLoadUtils.class) {
                if(instance == null) {
                    instance = new DownLoadUtils(context);
                    return instance;
                }
            }
        }
        return instance;
    }

    /**
     * 下载
     * @param uri
     * @param title
     * @param description
     * @param appName
     * @return downloadId
     */
    public long download(String uri, String title, String description,String appName) {

        //1.构建下载请求
        DownloadManager.Request downloadRequest = new DownloadManager.Request(Uri.parse(uri));
        downloadRequest.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        downloadRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        /**设置漫游状态下是否可以下载*/
        downloadRequest.setAllowedOverRoaming(false);
        /**如果我们希望下载的文件可以被系统的Downloads应用扫描到并管理，
         我们需要调用Request对象的setVisibleInDownloadsUi方法，传递参数true.*/
        downloadRequest.setVisibleInDownloadsUi(true);
        //文件保存位置
        //file:///storage/emulated/0/Android/data/your-package/files/Download/appName.apk
        downloadRequest.setDestinationInExternalFilesDir(mContext, Environment.DIRECTORY_DOWNLOADS, appName + ".apk");
        // 设置一些基本显示信息
        downloadRequest.setTitle(title);
        downloadRequest.setDescription(description);
        //req.setMimeType("application/vnd.android.package-archive");
        return mDownloadManager.enqueue(downloadRequest);//异步请求
    }

    /**
     * 获取文件下载路径
     * @param downloadId
     * @return
     */
    public String getDownloadPath(long downloadId) {

        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor c = mDownloadManager.query(query);
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    return c.getString(c.getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_URI));
                }
            } finally {
                c.close();
            }
        }

        return null;
    }

    /**
     * 获取文件保存的地址
     * @param downloadId
     * @return
     */
    public Uri getDownloadUri(long downloadId) {
        return mDownloadManager.getUriForDownloadedFile(downloadId);
    }

    public DownloadManager getDownloadManager() {
        return mDownloadManager;
    }

    /**
     * 获取下载状态
     * @param downloadId
     * @return
     */
    public int getDownloadStatus(long downloadId) {

        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor c = mDownloadManager.query(query);
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    return c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));
                }
            } finally {
                c.close();
            }
        }
        return -1;
    }

    /**
     * 判断下载管理程序是否可用
     * @return
     */
    public boolean canDownload() {

        try {
            int state = mContext.getPackageManager().getApplicationEnabledSetting("com.android.providers.downloads");
            if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 进入 启用/禁用 下载管理程序界面
     */
    public void skipToDownloadManager() {
        String packageName = "com.android.providers.downloads";
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + packageName));
        mContext.startActivity(intent);
    }


}
