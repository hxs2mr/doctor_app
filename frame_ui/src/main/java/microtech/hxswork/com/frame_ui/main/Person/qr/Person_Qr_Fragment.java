package microtech.hxswork.com.frame_ui.main.Person.qr;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import microtech.hxswork.com.frame_core.init.Frame;
import microtech.hxswork.com.frame_core.middle.MiddleFragment;
import microtech.hxswork.com.frame_core.net.RestClent;
import microtech.hxswork.com.frame_core.net.callback.ISuccess;
import microtech.hxswork.com.frame_core.util.Defaultcontent;
import microtech.hxswork.com.frame_core.util.LoadingDialog;
import microtech.hxswork.com.frame_core.util.SAToast;
import microtech.hxswork.com.frame_core.util.ShareUtils;
import microtech.hxswork.com.frame_ui.R;

import static com.netease.nim.uikit.business.session.activity.P2PMessageActivity.mActivity;
import static microtech.hxswork.com.frame_ui.login.LoginHandler.fristBean;
import static microtech.hxswork.com.frame_ui.login.LoginHandler.personBean;
import static microtech.hxswork.com.frame_ui.login.LoginHandler.userBean;

/**
 * Created by microtech on 2017/12/11.
 */

public class Person_Qr_Fragment extends MiddleFragment implements View.OnClickListener {
    private LinearLayoutCompat qr_back_linear = null;//返回
    private CircleImageView qr_headimage = null;//用户头像
    private AppCompatTextView qr_name = null;//用户新姓名
    private AppCompatImageView qr_ewm = null;//用户的二维码
    private AppCompatImageView qr_ewm1 = null;//用户的二维码
    private AppCompatTextView qr_share_weichat = null;//微信分享
    private AppCompatTextView qr_phon_bao = null;//保存到手机
    private AppCompatTextView weixin_text = null;
    private  ImageView test_image = null;
    private ProgressBar pb = null;

    private ContentFrameLayout pb_bar_contentframe = null;

    //设置图片的加载测曰
    private static final RequestOptions RECYCLER_OPTIONS=
            new RequestOptions()
                    .centerCrop()
                    .error(R.mipmap.deadpool)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate();


    //设置图片的加载测曰
    private static final RequestOptions RECYCLER_OPTIONS1=
            new RequestOptions()
                    .centerCrop()
                    .error(R.mipmap.weixin_no)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate();
    @Override
    public Object setLayout() {
        return R.layout.person_qr_fragment;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        qr_back_linear = bind(R.id.qr_back_linear);
        qr_headimage = bind(R.id.qr_headimage);
        pb_bar_contentframe = bind(R.id.pb_bar_contentframe);
        qr_name = bind(R.id.qr_name);
        qr_ewm = bind(R.id.qr_ewm);
        weixin_text = bind(R.id.weixin_text);
        pb = bind(R.id.pb_bar);
        qr_share_weichat = bind(R.id.qr_share_weichat);
        qr_phon_bao = bind(R.id.qr_phon_bao);
        qr_back_linear.setOnClickListener(this);
        qr_share_weichat.setOnClickListener(this);//分享到微信
        qr_phon_bao.setOnClickListener(this);//保存到手机
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        Glide.with(getContext())
                .load("http://qn.newmicrotech.cn/"+userBean.getAvatar()+"?imageMogr2/thumbnail/200x/strip/quality/50/format/webp")
                .apply(RECYCLER_OPTIONS)
                .into(qr_headimage);
        qr_name.setText(userBean.getName());
        if(userBean.getQr_code() == null)
        {
            weixin_text.setText("暂无二维码");
        }
        Glide.with(getContext())
                .load(userBean.getQr_code())
                .apply(RECYCLER_OPTIONS1)
                .into(qr_ewm);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.qr_back_linear) {//返回
            getSupportDelegate().pop();
        } else if (i == R.id.qr_share_weichat)//分享到微信
        {
            share_wx();
        } else if (i == R.id.qr_phon_bao)//保存到手机
        {
            pb_bar_contentframe.setVisibility(View.VISIBLE);
            if(((BitmapDrawable)qr_ewm.getDrawable())!=null)
            {
                //有问题
                saveMyBitmap(getContext(), ((BitmapDrawable)qr_ewm.getDrawable()).getBitmap());//保存图片到相册
            }else {
                SAToast.makeText(getContext(),"还没有二维码").show();
                pb_bar_contentframe.setVisibility(View.GONE);
            }
        }
    }

    //保存文件到指定路径
    public void saveMyBitmap(Context context, Bitmap bitmap) {
        String sdCardDir = Environment.getExternalStorageDirectory() + "/DCIM/";
        File appDir = new File(sdCardDir, "doctor_qr");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName ="doctor_qr_ewm"+ ".jpg";
        File f = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

 /*       // 通知图库更新
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(appDir);
        intent.setData(uri);
        context.sendBroadcast(intent);*/

        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File("/sdcard/DCIM/doctor_qr/doctor_qr_ewm.jpg"))));

       // pb.setVisibility(View.GONE);

        Frame.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //进行一些网络请求
                pb_bar_contentframe.setVisibility(View.GONE);
            }
        },1000);
        SAToast.makeText(getContext(),"保存成功！").show();
    }

    private void share_wx(){
        //1，创建一个WXWebPagerObject对象  用于封装要发送的url
        String text = "我是"+personBean.getDep_name()+"的"+userBean.getName()+"医生，点击关注“蜂鸟健康”公众号，为您提供及时便捷的家庭健康管理服务";
        ShareUtils.shareWeb(getActivity(), Defaultcontent.url+userBean.getQr_code(), Defaultcontent.title
                ,text,"", R.mipmap.app_logo, SHARE_MEDIA.WEIXIN
                ,0);
    }

    private void share_qq(){
        if(Build.VERSION.SDK_INT>=23){
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE,Manifest.permission.READ_LOGS,Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.SET_DEBUG_APP,Manifest.permission.SYSTEM_ALERT_WINDOW,Manifest.permission.GET_ACCOUNTS,Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(getActivity(),mPermissionList,123);
        }

        ShareUtils.shareWeb(getActivity(), Defaultcontent.url, Defaultcontent.title
                , Defaultcontent.text, Defaultcontent.imageurl, R.mipmap.deadpool, SHARE_MEDIA.QZONE
                ,0);
        LoadingDialog.cancelDialogForLoading();
    }


    private boolean isWebchatAAvaliable() {
        //检测手机上是否安装了微信
        try {
            mActivity.getPackageManager().getPackageInfo("com.tencent.mm", PackageManager.GET_ACTIVITIES);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}