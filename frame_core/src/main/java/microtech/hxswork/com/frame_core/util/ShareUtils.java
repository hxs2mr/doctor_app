package microtech.hxswork.com.frame_core.util;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.utils.Log;

/**
 * Created by microtech on 2017/10/26.
 */

public class ShareUtils {
    /**
     * 分享链接
     */
    public static void shareWeb(final Activity activity, String WebUrl, String title, String description, String imageUrl, int imageID, SHARE_MEDIA platform ,int i) {
        UMWeb web = new UMWeb(WebUrl);//连接地址
        web.setTitle(title);//标题
        web.setDescription(description);//描述
        if (TextUtils.isEmpty(imageUrl)) {
            web.setThumb(new UMImage(activity, imageID));  //本地缩略图
        } else {
            web.setThumb(new UMImage(activity, imageUrl));  //网络缩略图
        }
        if(i==0) {
            new ShareAction(activity)
                    .setPlatform(platform)
                    .withMedia(web)
                    .setCallback(new UMShareListener() {
                        @Override
                        public void onStart(SHARE_MEDIA share_media) {
                            LoadingDialog.showDialogForLoading(activity, "加载中...", true);
                        }

                        @Override
                        public void onResult(final SHARE_MEDIA share_media) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (share_media.name().equals("WEIXIN_FAVORITE")) {
                                        Toast.makeText(activity, share_media + " 收藏成功", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(activity, " 分享成功", Toast.LENGTH_SHORT).show();
                                    }
                                    LoadingDialog.cancelDialogForLoading();
                                }
                            });
                        }
                        @Override
                        public void onError(final SHARE_MEDIA share_media, final Throwable throwable) {
                            if (throwable != null) {
                                Log.d("throw", "throw:" + throwable.getMessage());
                            }
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity, share_media.hashCode()+ " 分享失败", Toast.LENGTH_SHORT).show();
                                    LoadingDialog.cancelDialogForLoading();
                                }
                            });
                        }

                        @Override
                        public void onCancel(final SHARE_MEDIA share_media) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity, "取消分享", Toast.LENGTH_SHORT).show();
                                    LoadingDialog.cancelDialogForLoading();
                                }
                            });
                        }
                    })
                    .share();
        }else {
            new ShareAction(activity)
                    .setPlatform(platform)
                    .withText(description + " " + WebUrl)
                    .withMedia(new UMImage(activity, imageID))
                    .setCallback(new UMShareListener() {
                        @Override
                        public void onStart(SHARE_MEDIA share_media) {
                            LoadingDialog.showDialogForLoading(activity, "加载中...", true);
                        }

                        @Override
                        public void onResult(final SHARE_MEDIA share_media) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (share_media.name().equals("WEIXIN_FAVORITE")) {
                                        Toast.makeText(activity, share_media + " 收藏成功", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(activity, " 分享成功", Toast.LENGTH_SHORT).show();
                                    }
                                    LoadingDialog.cancelDialogForLoading();
                                }
                            });
                        }

                        @Override
                        public void onError(final SHARE_MEDIA share_media, final Throwable throwable) {
                            if (throwable != null) {
                                Log.d("throw", "throw:" + throwable.getMessage());
                            }
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity, share_media.toString()+ " 分享失败", Toast.LENGTH_SHORT).show();
                                    LoadingDialog.cancelDialogForLoading();
                                }
                            });
                        }

                        @Override
                        public void onCancel(final SHARE_MEDIA share_media) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity, "取消分享", Toast.LENGTH_SHORT).show();
                                    LoadingDialog.cancelDialogForLoading();
                                }
                            });
                        }
                    })
                    .share();
        }
    }
}
