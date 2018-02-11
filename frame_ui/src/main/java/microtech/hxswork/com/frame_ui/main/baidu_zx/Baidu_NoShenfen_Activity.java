package microtech.hxswork.com.frame_ui.main.baidu_zx;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.PopupWindow;
import android.widget.TextView;
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
import java.util.WeakHashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import me.yokeyword.fragmentation.SwipeBackLayout;
import me.yokeyword.fragmentation_swipeback.SwipeBackActivity;
import microtech.hxswork.com.frame_core.dialogpicker.city_20170724.CityPickerView;
import microtech.hxswork.com.frame_core.dialogpicker.city_20170724.DayTimerPickerView;
import microtech.hxswork.com.frame_core.dialogpicker.city_20170724.PersonPickerView;
import microtech.hxswork.com.frame_core.dialogpicker.city_20170724.bean.CityBean;
import microtech.hxswork.com.frame_core.dialogpicker.city_20170724.bean.DistrictBean;
import microtech.hxswork.com.frame_core.dialogpicker.city_20170724.bean.ProvinceBean;
import microtech.hxswork.com.frame_core.net.RestClent;
import microtech.hxswork.com.frame_core.net.callback.ISuccess;
import microtech.hxswork.com.frame_core.util.Auth;
import microtech.hxswork.com.frame_core.util.JacksonUtil;
import microtech.hxswork.com.frame_core.util.Json;
import microtech.hxswork.com.frame_core.util.KeyBordUtil;
import microtech.hxswork.com.frame_core.util.LoadingDialog;
import microtech.hxswork.com.frame_core.util.SAToast;
import microtech.hxswork.com.frame_ui.R;
import microtech.hxswork.com.frame_ui.main.home_deatils.QianDeatail_Edit_Activity;
import microtech.hxswork.com.frame_ui.photoAdapter.PhotoAdapter;
import microtech.hxswork.com.frame_ui.photoAdapter.RecyclerItemClickListener;
import microtech.hxswork.com.photopicker.PhotoPicker;
import microtech.hxswork.com.photopicker.PhotoPreview;

import static com.blankj.utilcode.util.SnackbarUtils.getView;
import static microtech.hxswork.com.frame_core.phone.FormatUtil.isIdCardNo;
import static microtech.hxswork.com.frame_core.phone.FormatUtil.isMobileNO;
import static microtech.hxswork.com.frame_core.ui.refresh.RefreshHandler.patient_mAdapter;
import static microtech.hxswork.com.frame_ui.login.LoginHandler.fristBean;
import static microtech.hxswork.com.frame_ui.login.LoginHandler.userBean;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.home_qian_number_total;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.mtype;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.qian_number;
import static microtech.hxswork.com.frame_ui.main.home_deatils.QianDeatail_Fragmnet.selectedPhotos;

/**
 * Created by microtech on 2017/12/5.没有身份证
 */

public class Baidu_NoShenfen_Activity extends AppCompatActivity implements View.OnClickListener {
    CircleImageView no_shenfen_headimage = null;
    AppCompatEditText no_shenfen_name = null;
    AppCompatTextView no_shenfen_zhu = null;
    AppCompatTextView no_shenfen_sex = null;
    AppCompatTextView no_shenfen_age = null;
    AppCompatTextView no_shenfen_bin = null;
    AppCompatEditText no_shenfen_phone = null;
    AppCompatEditText no_shenfen_sheng = null;
    AppCompatEditText no_shenfen_address= null;
    AppCompatTextView fuwu_time = null;
    AppCompatTextView no_shenfen_qian = null;
    AppCompatTextView no_sheng_headText = null;
    LinearLayoutCompat no_shenfen_back_linear=  null;//返回
    AppCompatTextView no_shenfen_edit_ok = null;//保存
    AppCompatTextView no_shenfen_title = null;
    RecyclerView noshenfen_recycle=  null;
    public  static ArrayList<String> noshenfen_Photos;//=new ArrayList<>();
    public PhotoAdapter photoAdapter;
    public List<String> photos= null;
    public static ArrayList<String> mDistrictBeanArrayList1 ;//性别的选取
    LinearLayoutCompat no_linear1 = null;
    PopupWindow head_image = null;

    int headimage_flage=0;
    //动态获取权限监听
    private static PermissionListener mListener;

    private  File cameraFile;

    private Uri imageUri;

    //头像编辑模块
    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;//头像编辑模块
    public static final int CROP_PHOTO = 3;
    private  String cachPath;
    private File cacheFile;
    private String headimage =null;
    int selectimage_flage =0 ;
    List<String> list_image_add;
    public  static  ArrayList<String> Edit_Photos;//=new ArrayList<>();
    List<String> list_image;
    private String end_birthday;
    private int birthday_flage=0;
    private WeakHashMap<String,Object> put_list;
    String head_image_url="";

    String AccessKey="YdSHCtErv3CoRgH9Oa7MTiU15g3XVC86snrsSfMa";//七牛模块
    String SecretKey="RHk263O-wGVGtuJlRqmbMRlXa0Rld2f0pFjp6jEv";
    private PopupWindow mMenuView;
    private      List<String> list_bin ;//= new ArrayList<>();

   private  AppCompatTextView our_city = null;
    private  LinearLayoutCompat our_diqu_linear = null;
   private LinearLayoutCompat end_qian_linear = null;
   private LinearLayoutCompat  no_linear_end = null;
   private String image_path=null;

    //设置图片的加载测曰
    private static final RequestOptions RECYCLER_OPTIONS=
            new RequestOptions()
                    .centerCrop()
                    .error(R.mipmap.deadpool)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.noshenfen_fragment);
        initView();
        cachPath=getDiskCacheDir(getApplicationContext())+ "/handimg.jpg";//图片路径
        cacheFile =getCacheFile(new File(getDiskCacheDir(getApplicationContext())),"handimg.jpg");
       /* getSwipeBackLayout().setEdgeOrientation(SwipeBackLayout.EDGE_LEFT); // EDGE_LEFT(默认),EDGE_ALL
        getSwipeBackLayout().setParallaxOffset(0.5f); // （类iOS）滑动退出视觉差，默认0.3
        setSwipeBackEnable(true); // 是否允许滑动*/
    }
    private boolean checkForm() {
        final String bin = no_shenfen_bin.getText().toString();
        final String phone = no_shenfen_phone.getText().toString();
        final String address = our_city.getText().toString();
        final String name = no_shenfen_name.getText().toString();
        final String age = no_shenfen_age.getText().toString();
        final String number = no_shenfen_sheng.getText().toString();
        final String f_time = fuwu_time.getText().toString();
        final String qian = no_shenfen_sheng.getText().toString();

        final String shenfen_address = no_shenfen_address.getText().toString();

        //检查输入的文本是否正确
        boolean isPass = true;
        if (bin.isEmpty()) {
            no_shenfen_bin.setError("请添加病种");
            isPass = false;
        } else {
            no_shenfen_bin.setError(null);
        }

        if (shenfen_address.isEmpty())
        {
            no_shenfen_address.setError("请输入详细");
            isPass = false;
        }else {
            no_shenfen_address.setError(null);
        }
        if (mtype == 0)
        {
            if(f_time.isEmpty()){
                fuwu_time.setError("请添加服务截止时间");
                isPass = false;
            }else {
                fuwu_time.setError(null);
            }
            if(selectimage_flage == 0 ){
                no_shenfen_qian.setError("请添加患者签约资料");
                isPass = false;
            }else {
                no_shenfen_qian.setError(null);
            }
        }
        if(number.isEmpty()){
            no_shenfen_sheng.setError("请添加患者身份证号");
            isPass = false;
        }else {
            if(isIdCardNo(number))
            {
                no_shenfen_sheng.setError(null);
            }else {
                isPass = false;
                no_shenfen_sheng.setError("身份证格式不对!");
        }
        }

        if(age.isEmpty()){
            no_shenfen_age.setError("请添加患者出生日期");
            isPass = false;
        }else {
            no_shenfen_age.setError(null);
        }
        if(headimage_flage == 0 ){
            no_sheng_headText.setError("请添加患者头像");
            isPass = false;
        }else {
            no_sheng_headText.setError(null);
        }
        if(headimage_flage == 0 ){
            no_shenfen_qian.setError("请添加患者签约资料");
            isPass = false;
        }else {
            no_shenfen_qian.setError(null);
        }

        if(phone.isEmpty()){
            no_shenfen_phone.setError("请添加患者电话");
            isPass = false;
        }else {
            if(!isMobileNO(phone))
            {
                no_shenfen_phone.setError("手机号格式不对!");
                isPass = false;
            }else {
                no_shenfen_phone.setError(null);
            }
        }
        if(address.isEmpty()){
            our_city.setError("请输入地址");
            isPass = false;
        }else {
            our_city.setError(null);
        }if(name.isEmpty())
        {
            no_shenfen_name.setError("请输入姓名");
            isPass = false;
        }else {
            no_shenfen_name.setError(null);
        }
        return isPass;
    }
    private void initView() {
        list_bin = new ArrayList<>();
        Edit_Photos = new ArrayList<>();
        list_image_add = new ArrayList<>();
        list_image = new ArrayList<>();
        put_list = new WeakHashMap<>();
        noshenfen_Photos = new ArrayList<>();
        end_qian_linear = findViewById(R.id.end_qian_linear);
        no_linear_end = findViewById(R.id.no_linear_end);
        our_city = findViewById(R.id.our_city);
        our_diqu_linear = findViewById(R.id.our_diqu_linear);
        no_linear1 = findViewById(R.id.no_linear1);
        no_shenfen_qian = findViewById(R.id.no_shenfen_qian);
        no_sheng_headText = findViewById(R.id.no_sheng_headText);
        no_shenfen_back_linear = findViewById(R.id.no_shenfen_back_linear);
        no_shenfen_edit_ok = findViewById(R.id.no_shenfen_edit_ok);
        no_shenfen_headimage = findViewById(R.id.no_shenfen_headimage);
        no_shenfen_name = findViewById(R.id.no_shenfen_name);
        no_shenfen_zhu = findViewById(R.id.no_shenfen_zhu);
        no_shenfen_sex = findViewById(R.id.no_shenfen_sex);
        no_shenfen_age = findViewById(R.id.no_shenfen_age);
        no_shenfen_title = findViewById(R.id.no_shenfen_title);
        no_shenfen_bin = findViewById(R.id.no_shenfen_bin);
        no_shenfen_phone = findViewById(R.id.no_shenfen_phone);
        no_shenfen_sheng = findViewById(R.id.no_shenfen_sheng);
        no_shenfen_address = findViewById(R.id.no_shenfen_address);
        noshenfen_recycle = findViewById(R.id.noshengfen_recycle);
        fuwu_time = findViewById(R.id.fuwu_time);

        if (mtype == 0)
        {
            end_qian_linear.setVisibility(View.VISIBLE);
            no_linear_end.setVisibility(View.VISIBLE);
            no_shenfen_title.setText("签约患者");
        }else {
            end_qian_linear.setVisibility(View.GONE);
            no_linear_end.setVisibility(View.GONE);
            no_shenfen_title.setText("添加患者");
        }
        no_shenfen_back_linear.setOnClickListener(this);
        no_shenfen_edit_ok.setOnClickListener(this);
        fuwu_time.setOnClickListener(this);
        no_linear1.setOnClickListener(this);
        our_diqu_linear.setOnClickListener(this);
        no_shenfen_back_linear.setOnClickListener(this);
        no_shenfen_zhu.setOnClickListener(this);
        no_shenfen_bin.setOnClickListener(this);
        no_shenfen_sex.setOnClickListener(this);
        no_shenfen_sheng.setOnClickListener(this);
        no_shenfen_age.setOnClickListener(this);
        photoAdapter = new PhotoAdapter(this,noshenfen_Photos,1);
        noshenfen_recycle.setLayoutManager(new StaggeredGridLayoutManager(4, OrientationHelper.VERTICAL));//设置recycleview的布局
        noshenfen_recycle.setAdapter(photoAdapter);
        noshenfen_recycle.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(photoAdapter.getItemViewType(position) == PhotoAdapter.TYPE_ADD)//表示添加图片
                {
                    PhotoPicker.builder()
                            .setPhotoCount(PhotoAdapter.MAX)
                            .setShowCamera(true)
                            .setPreviewEnabled(false)
                            .setSelected(noshenfen_Photos)
                            .start(Baidu_NoShenfen_Activity.this);
                }else {//表示图片的查看
                    PhotoPreview.builder()
                            .setPhotos(noshenfen_Photos)
                            .setCurrentItem(position)
                            .start(Baidu_NoShenfen_Activity.this);
                }
            }
        }));
        mDistrictBeanArrayList1 = new ArrayList<>();
        mDistrictBeanArrayList1.add("男");
        mDistrictBeanArrayList1.add("女");

    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.no_shenfen_back_linear) {//返回
           finish();
        }else if(i == R.id.our_diqu_linear)
        {
            new KeyBordUtil().hideSoftKeyboard(no_shenfen_zhu);
            CityPickerView cityPicker = new CityPickerView.Builder(this,this).textSize(20)
                    .titleTextColor("#000000")
                    .backgroundPop(0xa0000000)
                    .province("贵州省")
                    .city("贵阳市")
                    .district("南明区")
                    .textColor(Color.parseColor("#000000"))
                    .provinceCyclic(true)
                    .cityCyclic(false)
                    .districtCyclic(false)
                    .visibleItemsCount(7)
                    .itemPadding(10)
                    .build();
            cityPicker.show();
            cityPicker.setOnCityItemClickListener(new CityPickerView.OnCityItemClickListener() {
                @Override
                public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                    //返回结果
                    our_city.setText(province.getName() + "|" + city.getName() + "|" + district.getName());
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha=1f;
                    getWindow().setAttributes(lp);
                    our_city.setError(null);
                }

                @Override
                public void onCancel() {
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha=1f;
                    getWindow().setAttributes(lp);
                }
            });

        }
        else  if(i == R.id.no_shenfen_bin)
        {
            new KeyBordUtil().hideSoftKeyboard(no_shenfen_zhu);
            ManyPopuWindow();
        }
        else if(i ==R.id.no_shenfen_zhu)
        {
            new KeyBordUtil().hideSoftKeyboard(no_shenfen_zhu);
            mDistrictBeanArrayList1.clear();
            initmingzhu();
              PersonPickerView SexPicker = new PersonPickerView.Builder(this,this).textSize(20)
                    .titleTextColor("#000000")
                    .backgroundPop(0xffffff)
                    .province("")
                    .city("汉族")
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
                    no_shenfen_zhu.setText(district);
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha=1f;
                    getWindow().setAttributes(lp);
                    no_shenfen_zhu.setError(null);
                }
                @Override
                public void onCancel() {
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha=1f;
                    getWindow().setAttributes(lp);
                }
            });
        }else if(i== R.id.no_linear1){
            new KeyBordUtil().hideSoftKeyboard(no_shenfen_zhu);
            showPopuWindow_headimage();
        }else if(i==R.id.no_shenfen_sex){
            new KeyBordUtil().hideSoftKeyboard(no_shenfen_zhu);
            mDistrictBeanArrayList1.clear();
            mDistrictBeanArrayList1.add("男");
            mDistrictBeanArrayList1.add("女");
            PersonPickerView SexPicker = new PersonPickerView.Builder(this,this).textSize(20)
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
                    no_shenfen_sex.setText(district);
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha=1f;
                    getWindow().setAttributes(lp);
                    no_shenfen_sex.setError(null);
                }
                @Override
                public void onCancel() {
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha=1f;
                    getWindow().setAttributes(lp);
                }
            });
        }else if(i == R.id.no_shenfen_age)
        { new KeyBordUtil().hideSoftKeyboard(no_shenfen_zhu);

            DayTimerPickerView cityPicker = new DayTimerPickerView.Builder(this,this).textSize(20)
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
                    birthday_flage= 1;
                    end_birthday = province+"-"+city+"-"+district;
                    //返回结果
                    int age =  Integer.parseInt(new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date()))-Integer.parseInt(province);
                    no_shenfen_age.setText(end_birthday);
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha=1f;
                    getWindow().setAttributes(lp);
                    no_shenfen_age.setError(null);
                }
                @Override
                public void onCancel() {
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha=1f;
                    getWindow().setAttributes(lp);
                }
            });
        }else if(i == R.id.fuwu_time)
        {
            new KeyBordUtil().hideSoftKeyboard(no_shenfen_zhu);
            DayTimerPickerView cityPicker = new DayTimerPickerView.Builder(this,this).textSize(20)
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

                    if(Integer.parseInt(province)<Integer.parseInt(new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date())))
                    {
                        province = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
                    }
                    if(Integer.parseInt(province)<=Integer.parseInt(new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date())))
                    {
                        if(Integer.parseInt("1"+city)<Integer.parseInt("1"+new SimpleDateFormat("MM", Locale.getDefault()).format(new Date())))
                        {
                            city = new SimpleDateFormat("MM", Locale.getDefault()).format(new Date());
                        }
                        if(Integer.parseInt("1"+city)<=Integer.parseInt("1"+new SimpleDateFormat("MM", Locale.getDefault()).format(new Date())))
                        {
                            if(Integer.parseInt("1"+district)<=Integer.parseInt("1"+new SimpleDateFormat("dd", Locale.getDefault()).format(new Date())))
                            {
                                district= new SimpleDateFormat("dd", Locale.getDefault()).format(new Date());
                            }
                        }
                    }
                    birthday_flage= 1;
                    end_birthday = province+"-"+city+"-"+district;
                    //返回结果
                    fuwu_time.setText(end_birthday);
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha=1f;
                    getWindow().setAttributes(lp);
                    fuwu_time.setError(null);
                }
                @Override
                public void onCancel() {
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha=1f;
                    getWindow().setAttributes(lp);
                }
            });
        }else if(i == R.id.no_shenfen_back_linear)
        {
            finish();
        }else if(i == R.id.no_shenfen_edit_ok) {
            if (checkForm()) {
                if (selectimage_flage == 1 && headimage_flage == 0) {
                    System.out.println("*********没有头像**********");
                    LoadingDialog.showDialogForLoading(this);
                    for (int k = 0; k < list_image.size(); k++) {
                        System.out.println("****2");
                        getUpimg(list_image.get(k));
                    }
                } else if (headimage_flage == 1 && selectimage_flage == 1) {
                    System.out.println("*********有头像**********");
                    list_image.add(cachPath);
                    LoadingDialog.showDialogForLoading(this);
                    for (int k = 0; k < list_image.size(); k++) {
                        System.out.println("1****");
                        getUpimg(list_image.get(k));
                    }
                } else if (selectimage_flage == 0 && headimage_flage == 1) {
                    System.out.println("*********只有头像**********");
                    LoadingDialog.showDialogForLoading(this);
                    list_image.add(cachPath);
                    getUpimg(cachPath);
                } else if (selectimage_flage == 0 && headimage_flage == 0) {
                    put_net();
                    System.out.println("*********没有数据变化**********");
                }
            }else {
                SAToast.makeText(this,"请填写对应的选项").show();
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
                                list_image_add.add(hash);
                                if(list_image.size() == list_image_add.size())
                                {
                                    if(selectimage_flage == 1 && headimage_flage == 1)
                                    {
                                            head_image_url = hash;
                                        list_image_add.remove(list_image_add.size()-1);
                                        Message msg =new Message();
                                        msg.what = 1;
                                        handler.sendMessage(msg);
                                    }else if(selectimage_flage == 0 && headimage_flage == 1)
                                    {
                                        head_image_url = hash;
                                        list_image_add.clear();
                                        if(mtype ==0 )
                                        {
                                            list_image_add = selectedPhotos;
                                        }
                                        Message msg =new Message();
                                        msg.what = 1;
                                        handler.sendMessage(msg);
                                    }else {

                                        Message msg =new Message();
                                        msg.what = 1;
                                        handler.sendMessage(msg);
                                    }
                                }

                            }else
                            {
                                SAToast.makeText(Baidu_NoShenfen_Activity.this,"图片上传异常").show();
                                LoadingDialog.cancelDialogForLoading();
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, null);

    }

    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 1:
                    LoadingDialog.cancelDialogForLoading();
                    put_net();
                    break;
                case  2:

                    Glide.with(Baidu_NoShenfen_Activity.this)
                            .load(image_path)
                            .apply(RECYCLER_OPTIONS)
                            .into(no_shenfen_headimage);
                    break;
            }
        }
    };

    private void put_net()
    {
        init_put_list();
        RestClent.builder()
                .url("signAddEdit")
                .loader(this)
                .params(put_list)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                       // patient_mAdapter
                        System.out.println("上传成功之后返回的数据******************"+response);
                       int code = JSON.parseObject(response).getInteger("code");
                       if(code == 200)
                       {
                           if(mtype==0)
                           {
                               home_qian_number_total++;
                               qian_number.setText(home_qian_number_total+"");
                           }
                               SAToast.makeText(Baidu_NoShenfen_Activity.this,"添加成功！").show();
                               Intent data = new Intent();
                               data.putExtra("UPDATE_PATIENT","-1");
                               setResult(103, data);
                                finish();
                       }else  if(code == 209){
                           list_image_add.clear();
                           SAToast.makeText(Baidu_NoShenfen_Activity.this,"居民已存在！").show();
                           list_image.clear();
                           noshenfen_Photos.clear();
                           noshenfen_Photos.addAll(list_image);
                           photoAdapter.notifyDataSetChanged();
                           selectimage_flage = 0;
                       }else if(code == 232)
                       {
                           list_image_add.clear();
                           SAToast.makeText(Baidu_NoShenfen_Activity.this,"身份证号码格式不对！").show();
                           list_image.clear();
                           noshenfen_Photos.clear();
                           noshenfen_Photos.addAll(list_image);
                           photoAdapter.notifyDataSetChanged();
                           selectimage_flage = 0;
                       }else {
                           list_image_add.clear();
                           SAToast.makeText(Baidu_NoShenfen_Activity.this,"添加失败").show();
                           list_image.clear();
                           noshenfen_Photos.clear();
                           noshenfen_Photos.addAll(list_image);
                           selectimage_flage = 0;
                           photoAdapter.notifyDataSetChanged();
                       }


                    }
                })
                .build()
                .post();

    }

    private  void init_put_list() {

        int sex_flage=0;
        if(no_shenfen_sex.getText().toString().equals("男"))
        {
            sex_flage = 1;
        }else {
            sex_flage = 2;
        }
        if(birthday_flage ==0)
        {
            end_birthday = "";
        }
        String head_url="";
        if(!head_image_url.equals(""))
        {
            head_url = head_image_url;
        }

        put_list.put("region_id",fristBean.getRegion_id());
        put_list.put("gov_id",fristBean.getGov_id());
        put_list.put("doctor_id",userBean.getId());
        put_list.put("signs_id","");

        put_list.put("avatar",head_url);
        put_list.put("name",no_shenfen_name.getText().toString().trim());
        put_list.put("sex",sex_flage);
        put_list.put("birthday",end_birthday);
        put_list.put("id_card",no_shenfen_sheng.getText().toString().trim());
        put_list.put("nation",no_shenfen_zhu.getText().toString().trim());
        put_list.put("address",our_city.getText().toString()+"|"+no_shenfen_address.getText().toString().trim());
        put_list.put("diseases", JSON.toJSON(list_bin));
        put_list.put("phone",no_shenfen_phone.getText().toString().trim());
        if (list_image_add.size() == 0)
        {
            put_list.put("upload_img","");
        }else {
            put_list.put("upload_img", JSON.toJSONString(list_image_add));
        }
        put_list.put("singsTime",end_birthday);
        put_list.put("time_stop",fuwu_time.getText().toString());
        put_list.put("channel","android");
        put_list.put("fmy_name","");
        put_list.put("type",mtype);

    }

    private void initmingzhu() {
        mDistrictBeanArrayList1.add("汉族");
        mDistrictBeanArrayList1.add("蒙古族");
        mDistrictBeanArrayList1.add("回族");
        mDistrictBeanArrayList1.add("藏族");
        mDistrictBeanArrayList1.add("维吾尔族");
        mDistrictBeanArrayList1.add("苗族");
        mDistrictBeanArrayList1.add("彝族");
        mDistrictBeanArrayList1.add("布依族");
        mDistrictBeanArrayList1.add("侗族");
        mDistrictBeanArrayList1.add("土家族");
        mDistrictBeanArrayList1.add("畲族");
        mDistrictBeanArrayList1.add("水族");
        mDistrictBeanArrayList1.add("其他");
    }
    private void showPopuWindow_headimage() {

        final View view = LayoutInflater.from(this).inflate(R.layout.head_image_popu, null);
        TextView open_photo = view.findViewById(R.id.open_image);//打开相册
        TextView open_caram =  view.findViewById(R.id.open_caram);//打开照相机
        TextView open_back = view.findViewById(R.id.open_back);//取消

        head_image = new PopupWindow(view);
        head_image.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        head_image.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        head_image.setAnimationStyle(R.style.LoginPopWindowAnim);//设置进入和出场动画
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha=0.5f;
        getWindow().setAttributes(lp);
        head_image.setBackgroundDrawable(this.getResources().getDrawable(R.color.white));
        head_image.setOutsideTouchable(true);
        head_image.setFocusable(true);
        head_image.showAtLocation(view, Gravity.BOTTOM, 0, 0);

        head_image.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha=1f;
                getWindow().setAttributes(lp);
            }
        });
        open_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhotoForAlbum();//打开相册
                head_image.dismiss();
            }
        });
        open_caram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhotoForCamera();//打开相机
                head_image.dismiss();
            }
        });
        open_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha=1f;
                getWindow().setAttributes(lp);
                head_image.dismiss();
            }
        });

    }


    private void takePhotoForAlbum() {  //打开相册
        String[] permissions={Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
        requestRuntimePermission(permissions,new PermissionListener() {
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

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO); // 打开相册
    }
    //andrpoid 6.0 需要写运行时权限
    public void requestRuntimePermission(String[] permissions, PermissionListener listener) {

        mListener = listener;
        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), 1);
        } else {
            mListener.onGranted();
        }
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

        cameraFile = getCacheFile(new File(getDiskCacheDir(getApplicationContext())),"car_image.jpg");

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            imageUri = Uri.fromFile(cameraFile);
        } else {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            imageUri = FileProvider.getUriForFile(getApplicationContext(), "com.doctor_app.microtech.fileprovider", cameraFile);
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
        Log.i("TAG",getImageContentUri(getApplicationContext(),file)+"裁剪照片的真实地址");
        headimage_flage = 1;
        headimage = getImageContentUri(getApplicationContext(),file)+"";
        System.out.println("headimage******"+headimage);
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(getImageContentUri(getApplicationContext(),file), "image/*");//自己使用Content Uri替换File Uri
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
            Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
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
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath= uriToPath(uri);

        image_path=imagePath;
//        displayImage(imagePath); // 根据图片路径显示图片
        Log.i("TAG","file://"+imagePath+"选择图片的URI"+uri);
        startPhotoZoom(new File(imagePath),350);
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        image_path=imagePath;
//        displayImage(imagePath);
        Log.i("TAG","file://"+imagePath+"选择图片的URI"+uri);
        startPhotoZoom(new File(imagePath),350);
    }

    private String uriToPath(Uri uri) {
        String path=null;
        if (DocumentsContract.isDocumentUri(getApplicationContext(), uri)) {
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
        Cursor cursor =getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK &&
                (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)) {
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                for(int i=0 ; i < photos.size() ; i++) {
                    System.out.println("photos***************:"+photos.get(i));
                }
                list_image = photos;
            }
            noshenfen_Photos.clear();
            if (photos != null) {
                selectimage_flage = 1;
                noshenfen_Photos.addAll(photos);
            }
            photoAdapter.notifyDataSetChanged();
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        // 将拍摄的照片显示出来
                        image_path = uriToPath(Uri.fromFile(cameraFile));
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

                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.fromFile(new File(cachPath))));

                      /*  Message msg =new Message();
                        msg.what = 2;
                        handler.sendMessage(msg);*/
                        Glide.with(Baidu_NoShenfen_Activity.this)
                                .load(image_path)
                                .apply(RECYCLER_OPTIONS)
                                .into(no_shenfen_headimage);
                        //no_shenfen_headimage.setImageBitmap(bitmap);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    private void ManyPopuWindow() {
        final CheckBox c1,c2,c3,c4;
        AppCompatTextView many_cancel,many_ok;
        final View view = LayoutInflater.from(this).inflate(R.layout.many_check_pop, null);
        mMenuView = new PopupWindow(view);
        mMenuView.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mMenuView.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha=0.7f;
        getWindow().setAttributes(lp);

        c1 = view.findViewById(R.id.cb01);
        c2 = view.findViewById(R.id.cb02);
        c3 = view.findViewById(R.id.cb03);
        c4 = view.findViewById(R.id.cb04);
        many_cancel = view.findViewById(R.id.many_cancel);
        many_ok = view.findViewById(R.id.many_ok);
        mMenuView.setAnimationStyle(R.style.PopupAnimation);
        mMenuView.setBackgroundDrawable(this.getResources().getDrawable(R.color.white));
        mMenuView.setOutsideTouchable(true);
        mMenuView.setFocusable(true);
        mMenuView.showAtLocation(view, Gravity.BOTTOM, 0, 0);

        mMenuView.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp  =getWindow().getAttributes();
                lp.alpha=1f;
                getWindow().setAttributes(lp);
            }
        });
        many_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMenuView.dismiss();
            }
        });
        many_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bin="";
                if(c1.isChecked())
                {
                    list_bin.add("0");
                    bin=bin+"糖尿病,";
                }
                if(c2.isChecked())
                {
                    list_bin.add("1");
                    bin=bin+"高血压,";
                }
                if(c3.isChecked())
                {
                    list_bin.add("2");
                    bin=bin+"脑梗,";
                }
                if(c4.isChecked())
                {
                    list_bin.add("3");
                    bin=bin+"其他,";
                }
                no_shenfen_bin.setText(bin);
                mMenuView.dismiss();
            }
        });
    }
}
