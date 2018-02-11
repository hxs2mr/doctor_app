package microtech.hxswork.com.frame_ui.main.Person;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import microtech.hxswork.com.frame_core.dialogpicker.city_20170724.DayTimerPickerView;
import microtech.hxswork.com.frame_core.dialogpicker.city_20170724.PersonPickerView;
import microtech.hxswork.com.frame_core.middle.MiddleFragment;
import microtech.hxswork.com.frame_core.net.RestClent;
import microtech.hxswork.com.frame_core.net.callback.ISuccess;
import microtech.hxswork.com.frame_core.util.Auth;
import microtech.hxswork.com.frame_core.util.JacksonUtil;
import microtech.hxswork.com.frame_core.util.LoadingDialog;
import microtech.hxswork.com.frame_core.util.SAToast;
import microtech.hxswork.com.frame_ui.R;
import microtech.hxswork.com.frame_ui.main.baidu_zx.BaiduNextActivity;
import microtech.hxswork.com.frame_ui.main.home_deatils.QianDeatail_Edit_Activity;
import microtech.hxswork.com.frame_ui.main.send.Send_Text_PhotoActivity;
import microtech.hxswork.com.frame_ui.widget.SelectDataPopuple;
import microtech.hxswork.com.frame_ui.widget.SelectPopupWindow;
import microtech.hxswork.com.photopicker.PhotoPicker;
import microtech.hxswork.com.photopicker.PhotoPreview;

import static microtech.hxswork.com.frame_core.util.TimeUtils.stampToDate;
import static microtech.hxswork.com.frame_ui.login.LoginHandler.fristBean;
import static microtech.hxswork.com.frame_ui.login.LoginHandler.personBean;
import static microtech.hxswork.com.frame_ui.login.LoginHandler.userBean;
import static microtech.hxswork.com.frame_ui.main.Person.PersonFragment.person_name;
import static microtech.hxswork.com.frame_ui.main.Person.PersonFragment.personal_headimage;

/**
 * Created by microtech on 2017/12/11.
 */

public class PersonData_Fragment extends MiddleFragment implements View.OnClickListener {
    private LinearLayoutCompat data_back_linear = null;
    private AppCompatTextView data_ok =null;
    private LinearLayoutCompat data_linear1 = null;//选择头像
    private CircleImageView  data_headimage = null;
    private AppCompatEditText  data_name = null;//姓名
    private LinearLayoutCompat data_linear2 = null;//性别
    private  AppCompatTextView data_sex = null;
    private LinearLayoutCompat data_linear3 = null;//生日
    private AppCompatTextView  data_birthday = null;
    private LinearLayoutCompat data_linear4 = null;
    private AppCompatTextView data_bumen = null;//部门
    private LinearLayoutCompat data_linear5 = null;//职务
    private AppCompatTextView data_zhiwu = null;
    private AppCompatEditText data_jianjie_edit = null;//简介
    private  SelectDataPopuple  menuWindow = null;
    //头像编辑模块
    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;//头像编辑模块
    public static final int CROP_PHOTO = 3;
    private  String cachPath;

    private File cacheFile;
    private String headimage =null;

    private  File cameraFile;

    private Uri imageUri;
    //动态获取权限监听
    private static PermissionListener mListener;
    public static ArrayList<String> mDistrictBeanArrayList1 ;//性别的选取
    private Boolean check_flage= false;
    private int head_flage= 0  ;
    private String head_image_url="";
    String AccessKey="YdSHCtErv3CoRgH9Oa7MTiU15g3XVC86snrsSfMa";//七牛模块
    String SecretKey="RHk263O-wGVGtuJlRqmbMRlXa0Rld2f0pFjp6jEv";

    String imagePath="";
    private String start_head_url="";
    //设置图片的加载测曰
    private static final RequestOptions RECYCLER_OPTIONS=
            new RequestOptions()
                    .centerCrop()
                    .error(R.mipmap.deadpool)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate();
    @Override
    public Object setLayout() {
        return R.layout.person_data_fragment;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        initView();
    }
    private void initView() {
        data_back_linear = bind(R.id.data_back_linear);
        data_ok = bind(R.id.data_ok);
        data_linear1 = bind(R.id.data_linear1);
        data_headimage = bind(R.id.data_headimage);
        data_name = bind(R.id.data_name);
        data_linear2 = bind(R.id.data_linear2);
        data_sex = bind(R.id.data_sex);
        data_linear3 = bind(R.id.data_linear3);
        data_birthday = bind(R.id.data_birthday);
        data_linear4 = bind(R.id.data_linear4);
        data_bumen = bind(R.id.data_bumen);
        data_linear5 = bind(R.id.data_linear5);
        data_zhiwu = bind(R.id.data_zhiwu);
        data_jianjie_edit = bind(R.id.data_jianjie_edit);


        data_back_linear.setOnClickListener(this);
        data_linear1.setOnClickListener(this);
        data_linear2.setOnClickListener(this);
        data_linear3.setOnClickListener(this);
        data_linear4.setOnClickListener(this);
        data_linear5.setOnClickListener(this);
        data_ok.setOnClickListener(this);
        cachPath=getDiskCacheDir(getContext())+ "/handimg.jpg";//图片路径

        cacheFile =getCacheFile(new File(getDiskCacheDir(getContext())),"handimg.jpg");

        mDistrictBeanArrayList1 = new ArrayList<>();
        mDistrictBeanArrayList1.add("男");
        mDistrictBeanArrayList1.add("女");
        init_founch();
        show_view();
    }

    private void show_view() {

        data_name.setText(userBean.getName());
        if(userBean.getGender().equals("1"))
        {
            data_sex.setText("男");
        }else {
            data_sex.setText("女");
        }
       // stampToDate(userBean.getBirthday(),"MM-dd");

        data_birthday.setText(userBean.getBirthday());

        start_head_url=userBean.getAvatar();
        Glide.with(getContext())
                .load("http://qn.newmicrotech.cn/"+userBean.getAvatar()+"?imageMogr2/thumbnail/200x/strip/quality/50/format/webp")
                .apply(RECYCLER_OPTIONS)
                .into(data_headimage);
        data_jianjie_edit.setText(personBean.getDesc());
        data_bumen.setText(personBean.getDep_name());
        data_zhiwu.setText(personBean.getDep_title());
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.data_back_linear) {//退出
            getSupportDelegate().pop();
        }else if(i == R.id.data_linear1){//头像选取框
            if(check_flage) {
                menuWindow = new SelectDataPopuple(getContext(), itemsOnClick,getActivity());
                menuWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white));
                menuWindow.showAtLocation(getView(),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        }else if(i == R.id.data_linear2){//性别选取
            if(check_flage) {
                select_sex();
            }
        }else if(i == R.id.data_linear3)//出生日期
        {
            if(check_flage) {
                select_date();
            }
        }else if(i == R.id.data_linear4)//选择部门
        {

            SAToast.makeText(getContext(),"不能变更部门").show();
            /*if(check_flage) {
                select_bumen();
            }*/
        }else if(i == R.id.data_linear5)//选择职务
        {
            SAToast.makeText(getContext(),"不能变更职务").show();
          /*  if(check_flage) {
                select_zhiwu();
            }*/
        }else if(i == R.id.data_ok)//编辑或保存
        {
            if(check_flage)//处于保存状态网络请求
            {
                if(head_flage == 1)
                {
                    LoadingDialog.showDialogForLoading(getActivity());
                    getUpimg(cachPath);
                }else {
                    put_net();
                }
                //check_flage = false;
            }else {//处于编辑状态
                data_ok.setTextColor(Color.parseColor("#37BBFB"));
                data_ok.setText("保存");
                check_flage = true;
                init_founch();
                init_text();//设置文本的颜色
            }
        }
    }


    public void getUpimg(final String imagePath) {
        // 图片上传到七牛 重用 uploadManager。一般地，只需要创建一个 uploadManager 对象

        UploadManager uploadManager = new UploadManager();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String key = "mk" + System.currentTimeMillis()+".png";
        //Auth.create(AccessKey, SecretKey);
        uploadManager.put(imagePath, key, Auth.create(AccessKey, SecretKey).uploadToken("microtech"),//可以加入命名key值
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info,
                                         JSONObject res) {

                        // res 包含hash、key等信息，具体字段取决于上传策略的设置。
                        try {
                            if(info.isOK()) {
                                System.out.println("******七牛upimg********" + res + info);
                                final String hash  = JSON.parseObject(res.toString()).getString("key");
                                head_image_url = hash;
                                put_net();
                                LoadingDialog.cancelDialogForLoading();
                            }else
                            {
                                SAToast.makeText(getContext(),"图片上传异常").show();
                                LoadingDialog.cancelDialogForLoading();
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, null);

    }
    private void init_text() {
        data_name.setTextColor(Color.parseColor("#B1B9BD"));
        data_sex.setTextColor(Color.parseColor("#B1B9BD"));
        data_birthday.setTextColor(Color.parseColor("#B1B9BD"));
        data_bumen.setTextColor(Color.parseColor("#B1B9BD"));
        data_zhiwu.setTextColor(Color.parseColor("#B1B9BD"));
    }

    private void init_founch() {
        if(!check_flage) {

            data_name.setFocusable(false);
            data_name.setFocusableInTouchMode(false);//设置不可编辑状态；

            data_jianjie_edit.setFocusable(false);
            data_jianjie_edit.setFocusableInTouchMode(false);//设置不可编辑状态；

        }else {
            data_name.setFocusable(true);
            data_name.setFocusableInTouchMode(true);//设置不可编辑状态；

            data_jianjie_edit.setFocusable(true);
            data_jianjie_edit.setFocusableInTouchMode(true);//设置不可编辑状态；
        }
    }


    private  void select_zhiwu(){
        mDistrictBeanArrayList1.clear();
        mDistrictBeanArrayList1.add("外科医生");
        mDistrictBeanArrayList1.add("口腔医生");
        mDistrictBeanArrayList1.add("内科医生");
        mDistrictBeanArrayList1.add("护士医生");
        mDistrictBeanArrayList1.add("药材医生");
        mDistrictBeanArrayList1.add("医学医生");
        mDistrictBeanArrayList1.add("心里医生");
        mDistrictBeanArrayList1.add("测量医生");

        PersonPickerView SexPicker = new PersonPickerView.Builder(getActivity(),getContext()).textSize(20)
                .titleTextColor("#000000")
                .backgroundPop(0xffffff)
                .province("")
                .city("外科医生")
                .district("26")
                .textColor(Color.parseColor("#525F66"))
                .provinceCyclic(true)
                .cityCyclic(false)
                .list(mDistrictBeanArrayList1)//添加用户选择
                .districtCyclic(false)
                .visibleItemsCount(5)
                .itemPadding(15)
                .build();
        SexPicker.show();
        SexPicker.setOnCityItemClickListener(new PersonPickerView.OnCityItemClickListener() {
            @Override
            public void onSelected(String province, String city, String district) {
                //返回结果
                data_zhiwu.setText(district);
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha=1f;
                getActivity().getWindow().setAttributes(lp);

            }
            @Override
            public void onCancel() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha=1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
    }
    private void select_bumen(){
        mDistrictBeanArrayList1.clear();
        mDistrictBeanArrayList1.add("门诊部");
        mDistrictBeanArrayList1.add("外科部");
        mDistrictBeanArrayList1.add("内科部");
        mDistrictBeanArrayList1.add("妇科部");
        mDistrictBeanArrayList1.add("药材部");
        mDistrictBeanArrayList1.add("口腔部");
        mDistrictBeanArrayList1.add("学术部");
        mDistrictBeanArrayList1.add("护士部");

        PersonPickerView SexPicker = new PersonPickerView.Builder(getActivity(),getContext()).textSize(20)
                .titleTextColor("#000000")
                .backgroundPop(0xffffff)
                .province("")
                .city("门诊部")
                .district("26")
                .textColor(Color.parseColor("#525F66"))
                .provinceCyclic(true)
                .cityCyclic(false)
                .list(mDistrictBeanArrayList1)//添加用户选择
                .districtCyclic(false)
                .visibleItemsCount(5)
                .itemPadding(15)
                .build();
        SexPicker.show();
        SexPicker.setOnCityItemClickListener(new PersonPickerView.OnCityItemClickListener() {
            @Override
            public void onSelected(String province, String city, String district) {
                //返回结果
                data_bumen.setText(district);
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha=1f;
                getActivity().getWindow().setAttributes(lp);

            }
            @Override
            public void onCancel() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha=1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
    }
    private void select_date(){
        DayTimerPickerView cityPicker = new DayTimerPickerView.Builder(getActivity(),getContext()).textSize(20)
                .titleTextColor("#000000")
                .backgroundPop(0xffffff)
                .province(new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date()))
                .city(new SimpleDateFormat("MM", Locale.getDefault()).format(new Date()))
                .district(new SimpleDateFormat("dd", Locale.getDefault()).format(new Date()))
                .textColor(Color.parseColor("#000000"))
                .provinceCyclic(true)
                .cityCyclic(false)
                .districtCyclic(false)
                .visibleItemsCount(5)
                .itemPadding(15)
                .build();
        cityPicker.show();
        cityPicker.setOnCityItemClickListener(new DayTimerPickerView.OnCityItemClickListener() {
            @Override
            public void onSelected(String province, String city, String district) {

                if(Integer.parseInt(province)>Integer.parseInt(new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date())))
                {
                    province = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
                }
                if(Integer.parseInt(province)>=Integer.parseInt(new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date())))
                {
                    if(Integer.parseInt("1"+city)>Integer.parseInt("1"+new SimpleDateFormat("MM", Locale.getDefault()).format(new Date())))
                    {
                        city = new SimpleDateFormat("MM", Locale.getDefault()).format(new Date());
                    }
                    if(Integer.parseInt("1"+city)>=Integer.parseInt("1"+new SimpleDateFormat("MM", Locale.getDefault()).format(new Date())))
                    {
                        if(Integer.parseInt("1"+district)>=Integer.parseInt("1"+new SimpleDateFormat("dd", Locale.getDefault()).format(new Date())))
                        {
                            district= new SimpleDateFormat("dd", Locale.getDefault()).format(new Date());
                        }
                    }
                }
                //返回结果
                int age =  Integer.parseInt(new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date()))-Integer.parseInt(province);
                data_birthday.setText(province+"-"+city+"-"+district);
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha=1f;
                 getActivity().getWindow().setAttributes(lp);

            }
            @Override
            public void onCancel() {
                WindowManager.LayoutParams lp =  getActivity().getWindow().getAttributes();
                lp.alpha=1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
    }

    private void select_sex() {

        mDistrictBeanArrayList1.clear();
        mDistrictBeanArrayList1.add("男");
        mDistrictBeanArrayList1.add("女");
        PersonPickerView SexPicker = new PersonPickerView.Builder(getActivity(),getContext()).textSize(20)
                .titleTextColor("#000000")
                .backgroundPop(0xffffff)
                .province("")
                .city("男")
                .district("26")
                .textColor(Color.parseColor("#525F66"))
                .provinceCyclic(true)
                .cityCyclic(false)
                .list(mDistrictBeanArrayList1)//添加用户选择
                .districtCyclic(false)
                .visibleItemsCount(5)
                .itemPadding(15)
                .build();
        SexPicker.show();
        SexPicker.setOnCityItemClickListener(new PersonPickerView.OnCityItemClickListener() {
            @Override
            public void onSelected(String province, String city, String district) {
                //返回结果
                data_sex.setText(district);
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha=1f;
                getActivity().getWindow().setAttributes(lp);

            }
            @Override
            public void onCancel() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha=1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });

    }


    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // menuWindow.dismiss();
            int i = v.getId();
            if (i == R.id.open_image)//打开相册
            {
                takePhotoForAlbum();//打开相册
                menuWindow.dismiss();
            } else if (i == R.id.open_caram) {//打开相机拍照
                takePhotoForCamera();//打开相机
                menuWindow.dismiss();
            } else if (i == R.id.open_back) {//返回
                menuWindow.dismiss();
            }
        }
    };
    private void takePhotoForAlbum() {  //打开相册
        String[] permissions={Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
        requestRuntimePermission(permissions, new PermissionListener() {
            @Override
            public void onGranted() {
                openAlbum();
            }

            @Override
            public void onDenied(List<String> deniedPermission) {
                //没有获取到权限，什么也不执行，看你心情
            }
        });
    }


    //andrpoid 6.0 需要写运行时权限
    public void requestRuntimePermission(String[] permissions,PermissionListener listener) {

        mListener = listener;
        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), permissionList.toArray(new String[permissionList.size()]), 1);
        } else {
            mListener.onGranted();
        }
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO); // 打开相册
    }


    public interface PermissionListener {
        /**
         * 成功获取权限
         */
        void onGranted();

        /**
         * 为获取权限
         * @param deniedPermission
         */
        void onDenied(List<String> deniedPermission);

    }

    private void takePhotoForCamera() {  //打开相机
        String[] permissions={Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        requestRuntimePermission(permissions, new PermissionListener() {
            @Override
            public void onGranted() {
                openCamera();
            }

            @Override
            public void onDenied(List<String> deniedPermission) {
                //有权限被拒绝，什么也不做好了，看你心情
            }
        });
    }

    private void openCamera() {

        cameraFile = getCacheFile(new File(getDiskCacheDir(getContext())),"car_image.jpg");

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            imageUri = Uri.fromFile(cameraFile);
        } else {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            imageUri = FileProvider.getUriForFile(getContext(), "com.doctor_app.microtech.fileprovider", cameraFile);
        }
        // 启动相机程序
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    private  File getCacheFile(File parent, String child) {
        // 创建File对象，用于存储拍照后的图片
        File file = new File(parent, child);

        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public String getDiskCacheDir(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }
    /**
     * 剪裁图片
     */
    private void startPhotoZoom(File file, int size) {
        Log.i("TAG",getImageContentUri(getContext(),file)+"裁剪照片的真实地址");
        head_flage = 1;
        headimage = getImageContentUri(getContext(),file)+"";
        Uri uri =  Uri.fromFile(cacheFile);
        //imagePath= uriToPath(uri);
        System.out.println("imagePath  is******"+imagePath);
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(getImageContentUri(getContext(),file), "image/*");//自己使用Content Uri替换File Uri
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 180);
            intent.putExtra("outputY", 180);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(cacheFile));//定义输出的File Uri
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            intent.putExtra("noFaceDetection", true);
            startActivityForResult(intent, CROP_PHOTO);
        } catch (ActivityNotFoundException e) {
            String errorMessage = "Your device doesn't support the crop action!";
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 转化地址为content开头
     * @param context
     * @param imageFile
     * @return
     */
    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID },
                MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        // 将拍摄的照片显示出来
                        // 将拍摄的照片显示出来
                        imagePath = uriToPath(Uri.fromFile(cameraFile));
                        startPhotoZoom(cameraFile, 350);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;

            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    } else {
                        // 4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                }
                break;

            case CROP_PHOTO:
                try {
                    if (resultCode == RESULT_OK) {
                        System.out.println("****cachPath***********" + cachPath);
                        System.out.println("****cachPath***********" + cachPath);

                       // Bitmap bitmap = BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(Uri.fromFile(new File(cachPath))));
                       // data_headimage.setImageBitmap(bitmap);
                        Glide.with(getContext())
                                .load(imagePath)
                                .apply(RECYCLER_OPTIONS)
                                .into(data_headimage);
                        //cachPath=getDiskCacheDir(getContext())+ "/handimg.jpg";//图片路径
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        Uri uri = data.getData();
         imagePath= uriToPath(uri);
//        displayImage(imagePath); // 根据图片路径显示图片
        startPhotoZoom(new File(imagePath),350);
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        imagePath = getImagePath(uri, null);
//        displayImage(imagePath);
        Log.i("TAG","file://"+imagePath+"选择图片的URI"+uri);
        startPhotoZoom(new File(imagePath),350);
    }
    private String uriToPath(Uri uri) {
        String path=null;
        if (DocumentsContract.isDocumentUri(getContext(), uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                path = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                path = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            path = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            path = uri.getPath();
        }
        return  path;
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor =getContext().getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void put_net(){

        int sex_flage=0;
        if(data_sex.getText().toString().equals("男"))
        {
            sex_flage = 1;
        }else {
            sex_flage = 2;
        }
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");

        String head_url="";
        if(!head_image_url.equals(""))
        {
            head_url =head_image_url;
            userBean.setAvatar(head_image_url);
        }else {
            head_url = start_head_url;
        }
        RestClent.builder()
                .url("userEdit")
                .loader(getContext())
                .params("region_id",fristBean.getRegion_id())
                .params("gov_id",fristBean.getGov_id())
                .params("doctor_id",userBean.getId())
                .params("avatar",head_url)
                .params("name",data_name.getText().toString())
                .params("sex",sex_flage)
                .params("birthday",data_birthday.getText().toString())
                .params("dept_id","")//部门id
                .params("title",data_zhiwu.getText().toString())//职务
                .params("desc",data_jianjie_edit.getText().toString())//简介
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        System.out.println("******用户资料编辑********" + response);
                        int code = JSON.parseObject(response).getInteger("code");
                        if(code == 200) {
                            SAToast.makeText(getContext(), "编辑成功").show();
                            userBean.setName(data_name.getText().toString());
                            personBean.setDesc(data_jianjie_edit.getText().toString());
                            if (data_sex.getText().toString().equals("男")) {
                                userBean.setGender("1");
                            } else {
                                userBean.setGender("2");
                            }
                            userBean.setName(data_name.getText().toString());
                            userBean.setBirthday(data_birthday.getText().toString());
                            if (head_flage == 1) {
                                userBean.setAvatar(head_image_url);

                                Glide.with(getContext())
                                        .load("http://qn.newmicrotech.cn/" + head_image_url + "?imageMogr2/thumbnail/200x/strip/quality/50/format/webp")
                                        .apply(RECYCLER_OPTIONS)
                                        .into(personal_headimage);
                            }

                            person_name.setText(data_name.getText().toString());
                            if (!userBean.getTeam_name().equals("无"))
                            {
                                userBean.setTeam_name(data_name.getText().toString()+"的团队");
                                userBean.setTeam_doctor_name(data_name.getText().toString());
                            }
                            getSupportDelegate().pop();
                        }
                    }
                })
                .build()
                .post();
    }

}
