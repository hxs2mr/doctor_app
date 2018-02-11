package microtech.hxswork.com.frame_ui.main.home_deatils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import microtech.hxswork.com.frame_core.dialogpicker.city_20170724.CityPickerView;
import microtech.hxswork.com.frame_core.dialogpicker.city_20170724.DayTimerPickerView;
import microtech.hxswork.com.frame_core.dialogpicker.city_20170724.PersonPickerView;
import microtech.hxswork.com.frame_core.dialogpicker.city_20170724.bean.CityBean;
import microtech.hxswork.com.frame_core.dialogpicker.city_20170724.bean.DistrictBean;
import microtech.hxswork.com.frame_core.dialogpicker.city_20170724.bean.ProvinceBean;
import microtech.hxswork.com.frame_core.net.RestClent;
import microtech.hxswork.com.frame_core.net.callback.ISuccess;
import microtech.hxswork.com.frame_core.ui.OrderQianFields;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleItemEntity;
import microtech.hxswork.com.frame_core.util.Auth;
import microtech.hxswork.com.frame_core.util.JacksonUtil;
import microtech.hxswork.com.frame_core.util.KeyBordUtil;
import microtech.hxswork.com.frame_core.util.LoadingDialog;
import microtech.hxswork.com.frame_core.util.SAToast;
import microtech.hxswork.com.frame_ui.R;
import microtech.hxswork.com.frame_ui.main.baidu_zx.Baidu_NoShenfen_Activity;
import microtech.hxswork.com.frame_ui.main.order.OrderFragment;
import microtech.hxswork.com.frame_ui.main.order.OrderListItemType;
import microtech.hxswork.com.frame_ui.photoAdapter.PhotoAdapter;
import microtech.hxswork.com.frame_ui.photoAdapter.RecyclerItemClickListener;
import microtech.hxswork.com.photopicker.PhotoPicker;
import microtech.hxswork.com.photopicker.PhotoPreview;

import static com.blankj.utilcode.util.SnackbarUtils.getView;
import static microtech.hxswork.com.frame_core.phone.FormatUtil.isIdCardNo;
import static microtech.hxswork.com.frame_core.phone.FormatUtil.isMobileNO;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.event_number;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.event_number_total;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.home_qian_number_total;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.mtype;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.qian_number;
import static microtech.hxswork.com.frame_ui.main.home_deatils.QianDeatail_Fragmnet.data;
import static microtech.hxswork.com.frame_ui.main.home_deatils.QianDeatail_Fragmnet.qiand_fragment;
import static microtech.hxswork.com.frame_ui.main.home_deatils.QianDeatail_Fragmnet.selectedPhotos;
import static microtech.hxswork.com.frame_ui.main.order.OrderFragment.Nnumber_status;
import static microtech.hxswork.com.frame_ui.main.order.OrderFragment.Nnumber_time;
import static microtech.hxswork.com.frame_ui.main.order.OrderFragment.Number_no;
import static microtech.hxswork.com.frame_ui.main.order.OrderFragment.no_order_content;
import static microtech.hxswork.com.frame_ui.main.order.OrderFragment.no_order_image;
import static microtech.hxswork.com.frame_ui.main.order.OrderFragment.no_order_linear;
import static microtech.hxswork.com.frame_ui.main.order.OrderFragment.recyclerView;
import static microtech.hxswork.com.frame_ui.main.order.OrderRefreshHandler.mAdapter;

/**
 * Created by microtech on 2017/12/4.编辑患者详情
 */

public class QianDeatail_Edit_Activity extends AppCompatActivity implements View.OnClickListener {
    CircleImageView  our_headimage = null;
    AppCompatEditText our_name = null;
    AppCompatTextView our_zhu = null;
    AppCompatTextView our_sex = null;
    AppCompatTextView our_age = null;
    AppCompatTextView our_bin = null;
    AppCompatEditText our_phone = null;
    AppCompatEditText our_sheng = null;
    AppCompatEditText our_address= null;
    RecyclerView  our_recycleview = null;
    AppCompatTextView our_delete = null;
    public ArrayList<String> person_edit;
    LinearLayoutCompat  edit_back_linear=  null;//返回
    AppCompatTextView edit_ok = null;//保存

    LinearLayoutCompat our_linear1 = null;
    LinearLayoutCompat our_linear2 = null;
    LinearLayoutCompat our_linear3 = null;
    LinearLayoutCompat our_linear4 = null;
    LinearLayoutCompat our_linear5 = null;
    LinearLayoutCompat our_linear6 = null;
    LinearLayoutCompat our_diqu_linear = null;
    private static final RequestOptions RECYCLER_OPTIONS=
            new RequestOptions()
                    .centerCrop()
                    .error(R.mipmap.deadpool)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate();

    String user_id = null;
    public  static  ArrayList<String> Edit_Photos;//=new ArrayList<>();
    public PhotoAdapter photoAdapter;
    public List<String> photos= null;
    public int last_photo_flage= 0 ;
    PopupWindow head_image = null;
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
    public static ArrayList<String> mDistrictBeanArrayList1 ;//性别的选取
    private Boolean isopen =false;

    String AccessKey="YdSHCtErv3CoRgH9Oa7MTiU15g3XVC86snrsSfMa";//七牛模块
    String SecretKey="RHk263O-wGVGtuJlRqmbMRlXa0Rld2f0pFjp6jEv";
    List<String> list_image_add;

    int up_size = 20 ;
    List<String> list_image;
    private  List<String> list_start_image;
    private  List<String> list_select_image;
    int headimage_flage=0;
    int selectimage_flage =0 ;
    String head_image_url="";
    private WeakHashMap<String,Object> put_list;

    private String end_birthday;
    private int birthday_flage=0;
    private PopupWindow mMenuView;
    List<String> list_bin ;//= new ArrayList<>();
    AppCompatTextView our_city;
     private  String head_url="";
     private  int sex_flage = 1;
    private  String imagePath="";
    private LinearLayoutCompat wu_linear;
    private AppCompatTextView result_time;
    private String start_head_url="";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.qian_persson_details);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        person_edit = bundle.getStringArrayList("edit");
        Edit_Photos=new ArrayList<>();
        Edit_Photos = selectedPhotos;
        list_bin = new ArrayList<>();
        last_photo_flage = selectedPhotos.size();
        list_image_add = new ArrayList<>();
        list_image = new ArrayList<>();
        list_start_image = new ArrayList<>();
        put_list=  new WeakHashMap<>();
        list_select_image = new ArrayList<>();
        onBindView();
        /*getSwipeBackLayout().setEdgeOrientation(SwipeBackLayout.EDGE_LEFT); // EDGE_LEFT(默认),EDGE_ALL
        getSwipeBackLayout().setParallaxOffset(0.5f); // （类iOS）滑动退出视觉差，默认0.3
        setSwipeBackEnable(true); // 是否允许滑动*/
    }
    private boolean checkForm(){
        final  String name = our_name.getText().toString();
        final  String sex = our_sex.getText().toString();
        final  String shen = our_sheng.getText().toString();
        final  String city = our_city.getText().toString();
        final  String phone = our_phone.getText().toString();
        final  String zhu = our_zhu.getText().toString();
        //检查输入的文本是否正确
        boolean isPass = true;

        if(zhu.isEmpty()){
            our_zhu.setError("请选择民族!");
            isPass = false;
        }else {
            our_zhu.setError(null);
        }
        if(name.isEmpty()){
            our_name.setError("请填写姓名!");
            isPass = false;
        }else {
            our_name.setError(null);
        }
            if (sex.isEmpty())
            {
            our_sex.setError("请选择性别!");
          isPass = false;
         }else {
            our_sex.setError(null);
            }

        if(shen.isEmpty()){
            our_sheng.setError("请填写身份证号!");
            isPass = false;
        }else {
            if(!isIdCardNo(shen))
            {
                our_sheng.setError("身份证格式不对!");
                isPass = false;
            }else {
                our_sheng.setError(null);
            }
        }
        if(city.isEmpty()){
            our_city.setError("请选择居住地方!");
            isPass = false;
        }else {
            our_city.setError(null);
        }
        if(phone.isEmpty()){
            our_phone.setError("请填写手机号姓名!");
            isPass = false;
        }else {
            if(!isMobileNO(phone))
            {
                our_phone.setError("手机号格式不对!");
                isPass = false;
            }else {
                our_phone.setError(null);
            }
        }
        if(list_bin.size()<=0)
        {
            our_bin.setError("请选择病种!");
            isPass = false;
        }else {
            our_bin.setError(null);
        }
        return isPass;
    }

    public void onBindView() {
        initView();
        initRecycle();
        init_founch();
    }

    private void initView() {
        wu_linear= findViewById(R.id.fuwu_linear);
        result_time = findViewById(R.id.result_time);
        edit_back_linear = findViewById(R.id.edit_back_linear);
        our_city = findViewById(R.id.our_city);
        edit_ok = findViewById(R.id.edit_ok);
        our_headimage = findViewById(R.id.our_headimage);
        our_diqu_linear = findViewById(R.id.our_diqu_linear);
        our_name = findViewById(R.id.our_name);
        our_zhu = findViewById(R.id.our_zhu);
        our_sex = findViewById(R.id.our_sex);
        our_age = findViewById(R.id.our_age);
        our_bin = findViewById(R.id.our_bin);

        our_phone = findViewById(R.id.our_phone);
        our_sheng = findViewById(R.id.our_sheng);
        our_address = findViewById(R.id.our_address);
        our_recycleview = findViewById(R.id.our_recycle);
        our_delete = findViewById(R.id.our_delete);
        our_linear1 = findViewById(R.id.our_linear1);
        our_linear2 = findViewById(R.id.our_linear2);
        our_linear3 = findViewById(R.id.our_linear3);
        our_linear4 = findViewById(R.id.our_linear4);
        our_linear5 = findViewById(R.id.our_linear5);
        our_linear6 = findViewById(R.id.our_linear6);
        Glide.with(getApplicationContext())
                .load("http://qn.newmicrotech.cn/"+person_edit.get(0)+"?imageMogr2/thumbnail/200x/strip/quality/50/format/webp")
                .apply(RECYCLER_OPTIONS)
                .into(our_headimage);
        start_head_url = person_edit.get(0);
        our_name.setText(person_edit.get(1));
        our_zhu.setText(person_edit.get(2));
        if(!person_edit.get(0).equals(""))
        {
            headimage_flage =0;
            head_image_url = person_edit.get(0);
            imagePath = person_edit.get(0);
        }
        if(Edit_Photos.size() >0)
        {
            selectimage_flage = 0;
            list_start_image = Edit_Photos;
        }
        if(person_edit.get(3).equals("1"))
        {
            our_sex.setText("男");
        }else {
            our_sex.setText("女");
        }

        our_age.setText(person_edit.get(4));
        our_bin.setText(person_edit.get(5));

        String[] data_bin = person_edit.get(5).split(",");

        for (int j =0 ; j < data_bin.length ; j++)
        {
            if(data_bin[j].contains("其他"))
            {
                list_bin.add("3");
            }else if(data_bin[j].contains("脑梗"))
            {
                list_bin.add("2");
            }else if(data_bin[j].contains("糖尿病")){
                list_bin.add("0");
            }else if(data_bin[j].contains("高血压")){
                list_bin.add("1");
            }
        }
        our_phone.setText(person_edit.get(6));
        our_sheng.setText(person_edit.get(7));

        String[] a = person_edit.get(8).split("\\|");
        String data="";
        if(a.length>1)
        {
            for (int i = 0 ; i< a.length-1;i++)
            {
                data=data+a[i];
            }
            our_city.setText(data);
            our_address.setText(a[a.length-1]);
        }else  if(a.length==1){
            our_city.setText(a[0]);
            our_address.setText("");
        }


        edit_back_linear.setOnClickListener(this);
        edit_ok.setOnClickListener(this);
        our_diqu_linear.setOnClickListener(this);
        our_linear1.setOnClickListener(this);//头像
        our_linear3.setOnClickListener(this);//民族
        our_linear4.setOnClickListener(this);//性别
        our_age.setOnClickListener(this);//年龄
        our_linear6.setOnClickListener(this);//病种
        our_delete.setOnClickListener(this);
        wu_linear.setOnClickListener(this);
        cachPath=getDiskCacheDir(getApplicationContext())+ "/handimg.jpg";//图片路径

        cacheFile =getCacheFile(new File(getDiskCacheDir(getApplicationContext())),"handimg.jpg");

        mDistrictBeanArrayList1 = new ArrayList<>();
        mDistrictBeanArrayList1.add("男");
        mDistrictBeanArrayList1.add("女");
        end_birthday = person_edit.get(14);
        result_time.setText( person_edit.get(16));
    }


    private void put_address_net(String address)
    {
        String new_address="";
        RestClent.builder()
                .url("findRegion")
                .params("address",address)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        System.out.println("********放回的数据地区****："+response);
                        final com.alibaba.fastjson.JSONObject object = JSON.parseObject(response).getJSONObject("obj");
                        final int code = JSON.parseObject(response).getInteger("code");
                        if(code == 200)
                        {
                            //result_city.setText(object.getString("province")+"|"+object.getString("city")+"|"+object.getString("district"));
                        }
                    }

                })
                .build()
                .post();
    }

    private void initRecycle() {
        photoAdapter = new PhotoAdapter(this,Edit_Photos,1);
        our_recycleview.setLayoutManager(new StaggeredGridLayoutManager(4, OrientationHelper.VERTICAL));//设置recycleview的布局

        our_recycleview.setAdapter(photoAdapter);
        our_recycleview.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if(isopen) {
                    if (photoAdapter.getItemViewType(position) == PhotoAdapter.TYPE_ADD)//表示添加图片
                    {
                        PhotoPicker.builder()
                                .setPhotoCount(PhotoAdapter.MAX)
                                .setShowCamera(true)
                                .setPreviewEnabled(false)
                                .setSelected(Edit_Photos)
                                .start(QianDeatail_Edit_Activity.this);
                    } else {//表示图片的查看
                        PhotoPreview.builder()
                                .setPhotos(Edit_Photos)
                                .setCurrentItem(position)
                                .start(QianDeatail_Edit_Activity.this);
                    }
                }
            }
        }));
    }
    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.edit_back_linear) {//返回
            finish();
        }else if(i == R.id.fuwu_linear){
            new KeyBordUtil().hideSoftKeyboard(wu_linear);
            if(isopen)//处于编辑状态切回
            {
                select_stop_time();
            }
        }
        else if(i == R.id.our_diqu_linear)
        { new KeyBordUtil().hideSoftKeyboard(our_diqu_linear);
            if(isopen)//处于编辑状态切回
            {
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
        }

        else if(i == R.id.edit_ok)//保存的网络请求
        {
            if(!isopen)//处于编辑状态切回
            {
                isopen = true;
                edit_ok.setTextColor(Color.parseColor("#37BBFB"));
                edit_ok.setText("保存");
                init_text_color();
                init_founch();
            }else {

                if( checkForm())
                {
                if(selectimage_flage == 1&& headimage_flage==0) {
                    list_start_image.clear();
                    if(list_image.size()>0) {
                        for (int j = 0; j < list_image.size(); j++) {
                        String new_url = list_image.get(j).substring(0,2);
                            if(new_url.equals("mk"))
                            {
                                list_start_image.add(list_image.get(j));
                            }else {
                                list_select_image.add(list_image.get(j));
                            }
                        }
                        System.out.println("*********没有头像**********");
                        LoadingDialog.showDialogForLoading(this);
                        if(list_select_image.size()>0) {
                            for (int k = 0; k < list_select_image.size(); k++) {
                                System.out.println("****2");
                                getUpimg(list_select_image.get(k), 1);
                            }
                        }else {
                            init_put_list();
                            put_net();
                        }
                    }else {
                        init_put_list();
                        put_net();
                    }
                }
                else if(headimage_flage == 1&&selectimage_flage == 1)
                {
                    list_start_image.clear();
                    if(list_image.size()>0) {
                        for (int j = 0; j < list_image.size(); j++) {
                            String new_url = list_image.get(j).substring(0,2);
                            if(new_url.equals("mk"))
                            {
                                list_start_image.add(list_image.get(j));
                            }else {
                                list_select_image.add(list_image.get(j));
                            }
                        }
                        System.out.println("*********有头像**********");
                        String head_url = imagePath.substring(0,2);
                        if(!head_url.equals("mk"))
                        {
                            list_select_image.add(imagePath);
                        }else {
                            list_start_image.add(imagePath);
                            head_image_url = imagePath;
                            headimage_flage = 0;
                        }
                        LoadingDialog.showDialogForLoading(this);

                        if(list_select_image.size()>0) {
                            for (int k = 0; k < list_select_image.size(); k++) {
                                System.out.println("1****");
                                getUpimg(list_select_image.get(k), 1);
                            }
                        }else {
                            init_put_list();
                            put_net();
                        }
                    }else {
                        init_put_list();
                        put_net();
                    }
                } else if(selectimage_flage == 0&&headimage_flage == 1){
                    list_image = new ArrayList<>();
                    System.out.println("*********只有头像**********");
                    String head_url = imagePath.substring(0,2);
                    LoadingDialog.showDialogForLoading(this);
                    if(!head_url.equals("mk"))
                    {
                        list_select_image.add(imagePath);
                        getUpimg(imagePath,1);
                    }else {
                        head_image_url = imagePath;
                        init_put_list();
                        put_net();
                    }
                    //list_image.add(imagePath);
                  //  getUpimg(imagePath,1);
                }else if(selectimage_flage == 0&&headimage_flage == 0){
                    init_put_list();
                    put_net();
                    System.out.println("*********没有数据变化**********");
                }
            }
            }
        }else if(i == R.id.our_linear1)//头像的编辑
        {  new KeyBordUtil().hideSoftKeyboard(our_linear1);
            if(isopen) {
                showPopuWindow_headimage();
            }
        }else if(i== R.id.our_linear3)//民族的选取
        {  new KeyBordUtil().hideSoftKeyboard(our_linear3);
            if(isopen)
            {
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
                    our_zhu.setText(district);
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha=1f;
                    getWindow().setAttributes(lp);
                    our_zhu.setError(null);
                }
                @Override
                public void onCancel() {
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha=1f;
                    getWindow().setAttributes(lp);
                }
            });
            }
        }
        else if(i== R.id.our_linear4)//选取性别
        {
            new KeyBordUtil().hideSoftKeyboard(our_linear4);
            if(isopen)
            {

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
                    our_sex.setText(district);
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha=1f;
                    getWindow().setAttributes(lp);
                    our_sex.setError(null);
                }
                @Override
                public void onCancel() {
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha=1f;
                    getWindow().setAttributes(lp);
                }
            });
            }
        }else if(i == R.id.our_age){

            if(isopen)
            {

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
                    if(Integer.parseInt("1"+city)>Integer.parseInt("1"+new SimpleDateFormat("MM", Locale.getDefault()).format(new Date())))
                    {
                        city = new SimpleDateFormat("MM", Locale.getDefault()).format(new Date());
                    }

                    if(Integer.parseInt("1"+district)>Integer.parseInt("1"+new SimpleDateFormat("dd", Locale.getDefault()).format(new Date())))
                    {
                        district = new SimpleDateFormat("dd", Locale.getDefault()).format(new Date());
                    }

                    birthday_flage= 1;
                    end_birthday = province+"-"+city+"-"+district;
                          /*  int a =Integer.parseInt(city);
                            if(a>0&&a<10)
                            {
                                city="0"+city;
                            }
                            int b =Integer.parseInt(district);
                            if(b>0&&b<10)
                            {
                                district="0"+district;
                            }*/
                    //返回结果
                    int age =  Integer.parseInt(new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date()))-Integer.parseInt(province);
                    our_age.setText(age+"");
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha=1f;
                    getWindow().setAttributes(lp);
                    our_age.setError(null);
                }
                @Override
                public void onCancel() {
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha=1f;
                   getWindow().setAttributes(lp);
                }
            });
            }
        }else if(i == R.id.our_linear6){
            if(isopen) {
                ManyPopuWindow();
            }
        }else if(i == R.id.our_delete)//删除患者
        {
            show(our_delete);
        }
    }

    private void select_stop_time(){
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
                if(Integer.parseInt("1"+city)<Integer.parseInt("1"+new SimpleDateFormat("MM", Locale.getDefault()).format(new Date())))
                {
                    city = new SimpleDateFormat("MM", Locale.getDefault()).format(new Date());
                }
                if(Integer.parseInt("1"+district)<Integer.parseInt("1"+new SimpleDateFormat("dd", Locale.getDefault()).format(new Date())))
                {
                    district = new SimpleDateFormat("dd", Locale.getDefault()).format(new Date());
                }
                result_time.setText(province+"-"+city+"-"+district);
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha=1f;
                getWindow().setAttributes(lp);
                result_time.setError(null);
            }
            @Override
            public void onCancel() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha=1f;
                getWindow().setAttributes(lp);
            }
        });
    }

    private void init_text_color() {
        our_name.setTextColor(Color.parseColor("#C1CFD6"));
        our_zhu.setTextColor(Color.parseColor("#C1CFD6"));
        our_sex.setTextColor(Color.parseColor("#C1CFD6"));
        our_age.setTextColor(Color.parseColor("#C1CFD6"));
        our_bin.setTextColor(Color.parseColor("#C1CFD6"));
        our_bin.setTextColor(Color.parseColor("#C1CFD6"));
        our_phone.setTextColor(Color.parseColor("#C1CFD6"));
        our_sheng.setTextColor(Color.parseColor("#C1CFD6"));
        result_time.setTextColor(Color.parseColor("#C1CFD6"));
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
            }
            list_image.clear();
            Edit_Photos.clear();
            if (photos != null) {
                selectimage_flage = 1;
                list_image = photos;
                last_photo_flage=  list_image.size();
                Edit_Photos.addAll(photos);
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

                        if(!imagePath.equals(""))
                        {

                        }
                        headimage_flage = 1;
                        Glide.with(QianDeatail_Edit_Activity.this)
                                .load(imagePath)
                                .apply(RECYCLER_OPTIONS)
                                .into(our_headimage);

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

    private void init_founch() {
        if(!isopen) {
            our_name.setFocusable(false);
            our_name.setFocusableInTouchMode(false);//设置不可编辑状态；

            our_bin.setFocusable(false);
            our_bin.setFocusableInTouchMode(false);//设置不可编辑状态；

            our_phone.setFocusable(false);
            our_phone.setFocusableInTouchMode(false);//设置不可编辑状态；

            our_sheng.setFocusable(false);
            our_sheng.setFocusableInTouchMode(false);//设置不可编辑状态；

            our_address.setFocusable(false);
            our_address.setFocusableInTouchMode(false);//设置不可编辑状态；

            our_delete.setFocusable(false);
            our_delete.setFocusableInTouchMode(false);//设置不可编辑状态；

            new KeyBordUtil().hideSoftKeyboard(our_linear4);
        }else {
            our_name.setFocusable(true);
            our_name.setFocusableInTouchMode(true);//设置可编辑状态；

            our_bin.setFocusable(true);
            our_bin.setFocusableInTouchMode(true);//设置可编辑状态；

            our_phone.setFocusable(true);
            our_phone.setFocusableInTouchMode(true);//设置可编辑状态；

            our_sheng.setFocusable(true);
            our_sheng.setFocusableInTouchMode(true);//设置可编辑状态；

            our_address.setFocusable(true);
            our_address.setFocusableInTouchMode(true);//设置可编辑状态；


            our_delete.setFocusable(true);
            our_delete.setFocusableInTouchMode(true);//设置不可编辑状态；
        }
    }


    public void getUpimg(final String imagePath, final int i ) {
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
                                        if(list_select_image.size() == list_image_add.size())
                                        {
                                            if(selectimage_flage == 1 && headimage_flage == 1)
                                            {
                                                head_image_url = list_image_add.get(list_image_add.size()-1);
                                                list_image_add.remove((list_image_add.size())-1);
                                                Message msg =new Message();
                                                msg.what = 1;
                                                handler.sendMessage(msg);
                                            }else if(selectimage_flage == 0 && headimage_flage == 1)
                                            {
                                                head_image_url = hash;
                                                list_image_add.clear();
                                                Message msg =new Message();
                                                msg.what = 1;
                                                handler.sendMessage(msg);
                                            }else if(selectimage_flage==1 && headimage_flage ==0)
                                            {
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
                                        SAToast.makeText(QianDeatail_Edit_Activity.this,"图片上传异常").show();
                                        LoadingDialog.cancelDialogForLoading();
                                    }
                                }
                                catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }, null);

        }

    private  void init_put_list() {


        for(int i = 0 ; i <list_bin.size();i++)
        {
            System.out.println("*******病种****"+list_bin.get(i));
        }
        sex_flage=1;
        if(our_sex.getText().toString().equals("男"))
        {
            sex_flage = 1;
        }else {
            sex_flage = 2;
        }
        if(birthday_flage ==0)
        {
            end_birthday = person_edit.get(14);
        }

         head_url="";
        if(!head_image_url.equals(""))
        {
            head_url = head_image_url;
        }else {
            head_url = start_head_url;
        }
        put_list.put("region_id",person_edit.get(9));
        put_list.put("gov_id",person_edit.get(10));
        put_list.put("doctor_id",person_edit.get(11));
        put_list.put("signs_id",person_edit.get(12));
        put_list.put("avatar",head_url);
        put_list.put("name",our_name.getText().toString().trim());
        put_list.put("sex",sex_flage);
        put_list.put("birthday",end_birthday);
        put_list.put("id_card",our_sheng.getText().toString().trim());
        put_list.put("nation",our_zhu.getText().toString().trim());
        put_list.put("address",our_city.getText().toString()+"|"+our_address.getText().toString().trim());

        put_list.put("diseases", JSON.toJSONString(list_bin));

        put_list.put("phone",our_phone.getText().toString().trim());

        if (list_image_add.size() == 0)
        {
            list_image_add = list_start_image;
            put_list.put("upload_img", JSON.toJSONString(list_image_add));
        }else {
            int size = list_start_image.size();
            for (int i = 0; i < size; i++)
            {
                list_image_add.add(list_start_image.get(i));
            }
            put_list.put("upload_img", JSON.toJSONString(list_image_add));
        }
        put_list.put("singsTime",end_birthday);
        put_list.put("time_stop","");
        put_list.put("channel","android");
        put_list.put("type",mtype);
        put_list.put("time_stop",result_time.getText().toString());
    }
    private void put_net()
    {
        RestClent.builder()
                .url("signAddEdit")
                .loader(this)
                .params(put_list)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {

                        final int  code = JSON.parseObject(response).getInteger("code");
                        if (code == 200)
                        {
                            person_edit.set(0,head_url);
                            Intent data = new Intent();
                            data.putExtra("UPDATE_PATIENT","-1");
                            setResult(103, data);
                            init_orderAdpater();
                            SAToast.makeText(QianDeatail_Edit_Activity.this,"编辑成功").show();
                            finish();
                        }else   if (code == 232){
                            SAToast.makeText(QianDeatail_Edit_Activity.this,"身份证号码有误!").show();
                        }else{
                            SAToast.makeText(QianDeatail_Edit_Activity.this,"上传异常").show();
                        }
                                System.out.println("上传成功之后返回的数据******************"+response);
                    }
                })
                .build()
                .post();

    }

    private void init_orderAdpater() {
        //更新某个postion
        MultipleItemEntity entity = MultipleItemEntity.builder()
                .setItemType(OrderListItemType.ITEM_ORDER_QIAN)
                .setField(OrderQianFields.NAME,our_name.getText().toString().trim())
                .setField(OrderQianFields.SEX,sex_flage)
                .setField(OrderQianFields.BIN,our_bin.getText().toString())
                .setField(OrderQianFields.BIRTHDAY,our_age.getText().toString())
                .setField(OrderQianFields.THUMB,head_url)
                .setField(OrderQianFields.STATUE,person_edit.get(19))
                .setField(OrderQianFields.TIME,person_edit.get(17))
                .setField(OrderQianFields.NUMBER,person_edit.get(18))
                .setField(OrderQianFields.USER_ID,person_edit.get(15))
                .setField(OrderQianFields.GOV_ID,person_edit.get(10))
                .setField(OrderQianFields.DOCTOR_ID,person_edit.get(11))
                .setField(OrderQianFields.REGON_ID,person_edit.get(9))
                .setField(OrderQianFields.IMAGE_ARRAY, list_image_add)
                .setField(OrderQianFields._ID,person_edit.get(12))
                .build();
        mAdapter.setData(Integer.parseInt(data[4]),entity);

    }

    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            System.out.println("图片上传完成********************");
            LoadingDialog.cancelDialogForLoading();
            init_put_list();
            put_net();
        }
    };

    private void ManyPopuWindow() {
         final CheckBox c1,c2,c3,c4;
         AppCompatTextView many_cancel,many_ok;
        final View view = LayoutInflater.from(this).inflate(R.layout.many_check_pop, null);
        mMenuView = new PopupWindow(view);
        mMenuView.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mMenuView.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha=0.5f;
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
                list_bin.clear();
                String bin="";
                int flage =0 ;
                if(c1.isChecked())
                {
                    list_bin.add("0");
                    bin=bin+"糖尿病,";
                    flage  =1;
                }
                if(c2.isChecked())
                {
                    list_bin.add("1");
                    bin=bin+"高血压,";
                    flage  =1;
                }
                if(c3.isChecked())
                {
                    list_bin.add("2");
                    bin=bin+"脑梗,";
                    flage  =1;
                }
                if(c4.isChecked())
                {
                    list_bin.add("3");
                    bin=bin+"其他,";
                    flage  =1;
                }
                our_bin.setText(bin);
                mMenuView.dismiss();
                if(flage == 1)
                {
                    our_bin.setError(null);
                }
            }
        });
    }

    public void show(View v){
        //实例化建造者
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置警告对话框的标题
        builder.setTitle("删除患者");
        //设置警告显示的图片
//        builder.setIcon(android.R.drawable.ic_dialog_alert);
        //设置警告对话框的提示信息
        builder.setMessage("您是否需要删除此患者？");
        //设置”正面”按钮，及点击事件
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                init_delete_sign();
            }
        });
        //设置“反面”按钮，及点击事件
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        //设置“中立”按钮，及点击事件
        builder.setNeutralButton("等等看吧", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        //显示对话框
        builder.show();
    }

    private void init_delete_sign() {//删除患者
    RestClent.builder()
        .url("signDel")
        .loader(this)
        .params("region_id",person_edit.get(9))
        .params("gov_id",person_edit.get(10))
        .params("doctor_id",person_edit.get(11))
        .params("user_id",person_edit.get(15))
        .success(new ISuccess() {
            @Override
            public void onSuccess(String response) {
                System.out.println("删除签约返回的数据******************"+response);
                final int  code = JSON.parseObject(response).getInteger("code");
                if(code == 200)
                {
                    //网络请求
                    Intent data1 = new Intent();
                    data1.putExtra("UPDATE_PATIENT","-1");
                    setResult(103, data1);
                    mAdapter.remove(Integer.parseInt(data[4]));

                    if(home_qian_number_total >0)
                    {
                        home_qian_number_total--;
                        qian_number.setText(home_qian_number_total+"");
                    }else {
                        qian_number.setText("0");
                    }

                    if(mAdapter.getItemCount()<=0)
                    {
                        recyclerView.setVisibility(View.GONE);
                        no_order_linear.setVisibility(View.VISIBLE);
                        no_order_image.setBackgroundResource(R.mipmap.no_qianyue);
                        no_order_content.setText("您还没有签约患者");
                    }

                    finish();
                }else if(code == 325)
                {
                    SAToast.makeText(QianDeatail_Edit_Activity.this,"该患者存在未随访任务!").show();
                }
                    else {
                    SAToast.makeText(QianDeatail_Edit_Activity.this,"删除异常").show();
                }
            }
        })
        .build()
        .post();

    }
}
