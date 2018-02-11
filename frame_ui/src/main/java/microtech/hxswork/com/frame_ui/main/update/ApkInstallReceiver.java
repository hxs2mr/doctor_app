package microtech.hxswork.com.frame_ui.main.update;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;


/**
 * Created by Song on 2016/11/2.
 */
public class ApkInstallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            long downloadApkId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            installApk(context, downloadApkId);
        }
    }

    /**
     * 安装apk
     */
    private void installApk(Context context,long downloadId) {

        long downId = SystemParams.getInstance().getLong(DownloadManager.EXTRA_DOWNLOAD_ID, -1L);
        if(downloadId == downId) {
            DownloadManager downManager= (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri downloadUri = downManager.getUriForDownloadedFile(downloadId);
            SystemParams.getInstance().setString("downloadApk",downloadUri.getPath());
            if (downloadUri != null) {
                Intent install= new Intent(Intent.ACTION_VIEW);
                install.setDataAndType(downloadUri, "application/vnd.android.package-archive");
                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(install);
            } else {
                Toast.makeText(context, "下载失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
