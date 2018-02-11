package microtech.hxswork.com.frame_ui.main.Patient;

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
import android.os.Message;
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
import microtech.hxswork.com.frame_core.dialogpicker.city_20170724.CityPickerView;
import microtech.hxswork.com.frame_core.dialogpicker.city_20170724.DayTimerPickerView;
import microtech.hxswork.com.frame_core.dialogpicker.city_20170724.PersonPickerView;
import microtech.hxswork.com.frame_core.dialogpicker.city_20170724.bean.CityBean;
import microtech.hxswork.com.frame_core.dialogpicker.city_20170724.bean.DistrictBean;
import microtech.hxswork.com.frame_core.dialogpicker.city_20170724.bean.ProvinceBean;
import microtech.hxswork.com.frame_core.middle.MiddleFragment;
import microtech.hxswork.com.frame_core.net.RestClent;
import microtech.hxswork.com.frame_core.net.callback.ISuccess;
import microtech.hxswork.com.frame_core.ui.OrderQianFields;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleFields;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleItemEntity;
import microtech.hxswork.com.frame_core.util.Auth;
import microtech.hxswork.com.frame_core.util.KeyBordUtil;
import microtech.hxswork.com.frame_core.util.LoadingDialog;
import microtech.hxswork.com.frame_core.util.SAToast;
import microtech.hxswork.com.frame_core.util.TimeUtils;
import microtech.hxswork.com.frame_core.wechat.LatteWeChat;
import microtech.hxswork.com.frame_core.wechat.callbacks.IWeChatLoginCallBack;
import microtech.hxswork.com.frame_ui.R;
import microtech.hxswork.com.frame_ui.main.baidu_zx.Baidu_NoShenfen_Activity;
import microtech.hxswork.com.frame_ui.main.home_deatils.QianDeatail_Edit_Activity;
import microtech.hxswork.com.photopicker.PhotoPicker;
import microtech.hxswork.com.photopicker.PhotoPreview;

import static microtech.hxswork.com.frame_core.phone.FormatUtil.isIdCardNo;
import static microtech.hxswork.com.frame_core.phone.FormatUtil.isMobileNO;
import static microtech.hxswork.com.frame_core.ui.refresh.RefreshHandler.patient_mAdapter;
import static microtech.hxswork.com.frame_ui.login.LoginHandler.userBean;
import static microtech.hxswork.com.frame_ui.main.Patient.PatientFragment.head_patinent_image;
import static microtech.hxswork.com.frame_ui.main.Patient.PatientItemClickListener.chat;
import static microtech.hxswork.com.frame_ui.main.Patient.PatientItemClickListener.onclick_flage;
import static microtech.hxswork.com.frame_ui.main.Patient.PatientNextFragment.patient_next_name;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.mtype;
import static microtech.hxswork.com.frame_ui.main.home_deatils.QianDeatail_Fragmnet.selectedPhotos;

/**
 * Created by microtech on 2017/12/14.患者个人信息详情
 */

public class Patient_Detail_Fragment extends MiddleFragment implements View.OnClickListener {
    AppCompatEditText detail_name = null;
    AppCompatTextView detail_zhu = null;
    AppCompatTextView detail_sex = null;
    AppCompatTextView detail_age = null;
    AppCompatTextView detail_bin = null;
    AppCompatEditText detail_phone = null;
    AppCompatEditText detail_sheng = null;
    AppCompatEditText detail_address= null;
    AppCompatEditText detail_family = null;
   // AppCompatTextView our_delete = null;

    AppCompatTextView detail_team_person= null;
    AppCompatTextView detail_team= null;

    LinearLayoutCompat detail_back_linear=  null;//返回
    AppCompatTextView detail_ok = null;//保存

    LinearLayoutCompat detail_linear1 = null;
    LinearLayoutCompat detail_linear2 = null;
    LinearLayoutCompat detail_linear3 = null;
    LinearLayoutCompat our_linear5 = null;
    LinearLayoutCompat our_linear6 = null;
    LinearLayoutCompat our_linear1 = null;
    LinearLayoutCompat  xinxi_linear = null;
    LinearLayoutCompat  team_linear = null;
    CircleImageView our_headimage= null;
    LinearLayoutCompat our_diqu_linear = null;
    AppCompatTextView our_city = null;
    private String end_birthday="";
    private PopupWindow mMenuView;
   private PopupWindow head_image = null;
    int flage= 0 ;
    private boolean isopen = false;
    int headimage_flage=0;

    //头像
    //头像编辑模块
    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;//头像编辑模块
    public static final int CROP_PHOTO = 3;
    private  String cachPath;
    private File cacheFile;
    //动态获取权限监听
    private static PermissionListener mListener;

    private  File cameraFile;

    private Uri imageUri;
    private String headimage =null;
    public static ArrayList<String> mDistrictBeanArrayList1 ;//性别的选取
    private WeakHashMap<String,Object> put_list;// ;
    String head_image_url="";

    String AccessKey="YdSHCtErv3CoRgH9Oa7MTiU15g3XVC86snrsSfMa";//七牛模块
    String SecretKey="RHk263O-wGVGtuJlRqmbMRlXa0Rld2f0pFjp6jEv";
    String imagePath="";
    String pervince="";
    private  List<MultipleItemEntity>  list_data ;
    //设置图片的加载测曰
    private static final RequestOptions RECYCLER_OPTIONS=
            new RequestOptions()
                    .centerCrop()
                    .error(R.mipmap.deadpool)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate();

    String[] user;
    private   List<String> list_bin ;//= new ArrayList<>()
     private  String start_head_url="";//保存开始的头像
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user  = new String[6];
        Bundle arg = getArguments();
        user = arg.getStringArray("user");
        System.out.println("******已经收到数据"+user[0]);
    }

    @Override
    public Object setLayout() {
        return R.layout.patient_detail_fragment;
    }

    private boolean checkForm(){
        final  String name = detail_name.getText().toString();
        final  String sex = detail_sex.getText().toString();
        final  String shen = detail_sheng.getText().toString();
        final  String city = our_city.getText().toString();
        final  String phone = detail_phone.getText().toString();
        final String failm = detail_family.getText().toString();
        final  String bin = detail_bin.getText().toString();
        final String zhu = detail_zhu.getText().toString();
        //检查输入的文本是否正确
        boolean isPass = true;

        if(zhu.isEmpty()){
            detail_zhu.setError("请选择民族!");
            isPass = false;
        }else {
            detail_zhu.setError(null);
        }
        if(failm.isEmpty()){
            detail_family.setError("请填写所属家庭!");
            isPass = false;
        }else {
            detail_family.setError(null);
        }
        if(list_bin.size()<=0)
        {
            detail_bin.setError("请选择病种!");
            isPass = false;
        }else {
            detail_bin.setError(null);
        }
        if(name.isEmpty()){
            detail_name.setError("请填写姓名!");
            isPass = false;
        }else {
            detail_name.setError(null);
        }
        if (sex.isEmpty())
        {
            detail_sex.setError("请选择性别!");
            isPass = false;
        }else {
            detail_sex.setError(null);
        }

        if(shen.isEmpty()){
            detail_sheng.setError("请填写身份证号!");
            isPass = false;
        }else {
            if(isIdCardNo(shen))
            {
                detail_sheng.setError(null);
            }else {
                detail_sheng.setError("身份证格式不对!");
                isPass = false;
            }
        }
        if(city.isEmpty()){
            our_city.setError("请选择居住地方!");
            isPass = false;
        }else {
            our_city.setError(null);
        }
        if(phone.isEmpty()){
            detail_phone.setError("请填写手机号姓名!");
            isPass = false;
        }else {
            if(!isMobileNO(phone))
            {
                detail_phone.setError("手机号格式不对!");
                isPass = false;
            }else {
                detail_phone.setError(null);
            }
        }
        return isPass;
    }


    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        our_diqu_linear = bind(R.id.our_diqu_linear);
        our_city = bind(R.id.our_city);
        our_headimage = bind(R.id.our_headimage);
        detail_name= bind(R.id.detail_name);
        detail_zhu= bind(R.id.detail_zhu);
        detail_sex= bind(R.id.detail_sex);
        detail_age= bind(R.id.detail_age);
        detail_bin= bind(R.id.detail_bin);
        detail_phone= bind(R.id.detail_phone);
        detail_sheng= bind(R.id.detail_sheng);
        detail_address= bind(R.id.detail_address);
        detail_team_person = bind(R.id.detail_team_person);
        detail_team = bind(R.id.detail_team);
        detail_back_linear = bind(R.id.detail_back_linear);
        detail_ok = bind(R.id.detail_ok);
        detail_family = bind(R.id.detail_family);
        detail_linear1 = bind(R.id.detail_linear1);//民族
        detail_linear2 = bind(R.id.detail_linear2);//性别
        detail_linear3 = bind(R.id.detail_linear3);//年龄
        our_linear1 = bind(R.id.our_linear1);
        team_linear = bind(R.id.team_linear);
        xinxi_linear = bind(R.id.xinxi_linear);
        detail_back_linear.setOnClickListener(this);
        detail_ok.setOnClickListener(this);
        mDistrictBeanArrayList1 = new ArrayList<>();
        put_list = new WeakHashMap<>();
        mDistrictBeanArrayList1.add("男");
        mDistrictBeanArrayList1.add("女");
        our_diqu_linear.setOnClickListener(this);
        detail_linear1.setOnClickListener(this);
        detail_linear2.setOnClickListener(this);
        detail_age.setOnClickListener(this);
        detail_bin.setOnClickListener(this);
        our_linear1.setOnClickListener(this);
        list_bin = new ArrayList<>();
        cachPath=getDiskCacheDir(getContext())+ "/handimg.jpg";//图片路径

        cacheFile =getCacheFile(new File(getDiskCacheDir(getContext())),"handimg.jpg");
        init_founch();
        //init_data_view();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        init_put_net();
    }

    private void init_put_net()
    {

        RestClent.builder()
                .url("userInfo")
                .loader(getContext())
                .params("region_id",user[2])
                .params("gov_id",user[3])
                .params("doctor_id",user[4])
                .params("user_id",user[1])
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        System.out.println("居民详情返回的数据******："+response);
                        int  code  = JSON.parseObject(response).getInteger("code");
                        if(code == 200)
                        {
                           list_data= new Patient_Detail_DataConvert().setJsonData(response).CONVERT();
                           init_data_view(list_data);
                        }else{
                            SAToast.makeText(getContext(),"服务器异常").show();
                        }
                    }
                })
                .build()
                .post();
    }


    private void init_data_view(List<MultipleItemEntity>  list_data) {
        MultipleItemEntity entity = list_data.get(0);
         detail_name.setText(entity.getField(MultipleFields.NAME).toString());
        detail_zhu.setText(entity.getField(OrderQianFields.ZHU).toString());
        if(entity.getField(MultipleFields.SEX).equals("1"))
        {
            detail_sex.setText("男");
        }else {
            detail_sex.setText("女");
        }
        detail_age.setText(new TimeUtils().stampToDate(entity.getField(MultipleFields.BIRTHDAY).toString(),"yyyy-MM-dd"));
        //detail_age.setText(patient_detail.get(3)
        // );

        detail_bin.setText(entity.getField(MultipleFields.BIN).toString());

        String[] data_bin =entity.getField(MultipleFields.BIN).toString().split(",");

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
        detail_phone.setText(entity.getField(OrderQianFields.PHONE).toString());


        detail_family.setText(entity.getField(OrderQianFields.FAMILY_NAME).toString());
        detail_sheng.setText(entity.getField(OrderQianFields.ID_CARD).toString());

        //detail_address.setText(patient_detail.get(8));

        if(!entity.getField(OrderQianFields.ADDRESS).toString().equals("")) {
            String[] a =entity.getField(OrderQianFields.ADDRESS).toString().split("\\|");
            String data = "";
            if (a.length > 1) {
                for (int i = 0; i < a.length - 1; i++) {
                    data = data + a[i];
                }
                our_city.setText(data);
                detail_address.setText(a[a.length - 1]);
            } else if (a.length == 1) {
                our_city.setText(a[0]);
                detail_address.setText("");
            }
        }
        detail_team_person.setText(entity.getField(OrderQianFields.MESSAGE_SOCURE).toString());
        detail_team.setText(entity.getField(OrderQianFields.TEAM_NAME).toString());
        start_head_url = entity.getField(MultipleFields.THUMB).toString();
        Glide.with(getContext())
                .load("http://qn.newmicrotech.cn/"+start_head_url+"?imageMogr2/thumbnail/200x/strip/quality/50/format/webp")
                .apply(RECYCLER_OPTIONS)
                .into(our_headimage);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.detail_back_linear) {
            getSupportDelegate().pop();
        }else if(i == R.id.detail_ok){
        if(flage == 0 )//处于编辑状态
        {
            isopen = true;
            detail_ok.setText("保存");
            detail_ok.setTextColor(Color.parseColor("#37BBFB"));
            flage = 1;
            init_ok();
            init_founch();
            xinxi_linear.setVisibility(View.GONE);
            team_linear.setVisibility(View.GONE);
            //our_delete.setVisibility(View.VISIBLE);
        }else {//网络请求

            if(checkForm())
            {
                if(headimage_flage == 1)
                {
                    LoadingDialog.showDialogForLoading(getActivity());
                    getUpimg(cachPath);
                }else {
                    put_net();
                }
            }
        }
        }else if(i == R.id.detail_linear1)//选择民族
        {
            mDistrictBeanArrayList1.clear();
            initmingzhu();
            if(isopen) {
                new KeyBordUtil().hideSoftKeyboard(detail_linear1);
                select_zhu();
            }
        }else if(i == R.id.detail_linear2)//选择性别
        {
            mDistrictBeanArrayList1.clear();
            mDistrictBeanArrayList1.add("男");
            mDistrictBeanArrayList1.add("女");
            if(isopen) {
                new KeyBordUtil().hideSoftKeyboard(detail_linear2);
                select_sex();
            }
        }else if(i == R.id.detail_age)//选择生日
        {
            if(isopen) {
                new KeyBordUtil().hideSoftKeyboard(detail_age);
                select_birthday();
            }
        }else if(i == R.id.detail_bin)
        {
            if(isopen) {
                new KeyBordUtil().hideSoftKeyboard(detail_bin);
                select_bin();
            }
        }
        else if(i== R.id.our_linear1)
        {
            if(isopen) {
                new KeyBordUtil().hideSoftKeyboard(our_linear1);
                showPopuWindow_headimage();//编辑头像
            }
        }else if(i == R.id.our_diqu_linear)
        {     if(isopen) {
            new KeyBordUtil().hideSoftKeyboard(our_diqu_linear);
            select_diqu();
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
                            if(info.isOK()){
                                System.out.println("******七牛upimg********" + res + info);
                                final String hash  = JSON.parseObject(res.toString()).getString("key");
                                head_image_url = hash;
                                LoadingDialog.cancelDialogForLoading();
                                put_net();
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
    private void put_net()
    {
        init_put_list();
        RestClent.builder()
                .url("signAddEdit")
                .loader(getContext())
                .params(put_list)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        System.out.println("上传后返回的数据******************"+response);
                        int code =  JSON.parseObject(response).getInteger("code");
                        if(code == 200)
                        {
                            if(!head_image_url.equals(""))
                            {
                                head_patinent_image = head_image_url;
                            }
                            patient_next_name.setText(detail_name.getText().toString());
                            init_list_data();
                            patient_mAdapter.setData(onclick_flage,list_data.get(0));
                            getSupportDelegate().pop();
                            SAToast.makeText(getContext(),"保存成功!").show();
                        }else if(code == 232) {
                            SAToast.makeText(getContext(),"身份证号码格式不对!").show();
                        }else {
                            SAToast.makeText(getContext(),"保存异常!").show();
                        }
                    }
                })
                .build()
                .post();
    }

    private void init_list_data() {
        list_data.get(0).setField(MultipleFields.NAME,detail_name.getText().toString());
        String sex="0";
        if(detail_sex.getText().toString().equals("男"))
        {
            sex="1";
        }else {
            sex="2";
        }
        list_data.get(0).setField(MultipleFields.SEX,sex);
        list_data.get(0).setField(MultipleFields.BIN,detail_bin.getText().toString());
        pervince =  detail_age.getText().toString().substring(0, 4);
        list_data.get(0).setField(OrderQianFields.AGE,Integer.parseInt(new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date()))-Integer.parseInt(pervince)+"");

        list_data.get(0).setField(MultipleFields.CHAT,chat);
    }

    private  void init_put_list() {
        mtype= 1;
        int sex_flage=1;
        if(detail_sex.getText().toString().equals("男"))
        {
            sex_flage = 1;
        }else {
            sex_flage = 2;
        }
        String head_url="";
        if(!head_image_url.equals(""))
        {
            head_url = head_image_url;
        }else {
            head_url = start_head_url;
        }

        put_list.put("region_id",user[2]);
        put_list.put("gov_id",user[3]);
        put_list.put("doctor_id",user[4]);
        put_list.put("signs_id",user[1]);
        put_list.put("avatar",head_url);
        put_list.put("name",detail_name.getText().toString().trim());
        put_list.put("sex",sex_flage);
        put_list.put("birthday",detail_age.getText().toString());
        put_list.put("id_card",detail_sheng.getText().toString().trim());
        put_list.put("nation",detail_zhu.getText().toString().trim());
        put_list.put("address",our_city.getText().toString()+"|"+detail_address.getText().toString().trim());
        put_list.put("diseases", JSON.toJSONString(list_bin));
        put_list.put("phone",detail_phone.getText().toString().trim());
        put_list.put("upload_img","");
        put_list.put("singsTime","");
        put_list.put("time_stop","");
        put_list.put("channel","android");
        put_list.put("fmy_name",detail_family.getText().toString().trim());
        put_list.put("type",mtype);
    }

    private void showPopuWindow_headimage() {
        final View view = LayoutInflater.from(getContext()).inflate(R.layout.head_image_popu, null);
        TextView open_photo = view.findViewById(R.id.open_image);//打开相册
        TextView open_caram =  view.findViewById(R.id.open_caram);//打开照相机
        TextView open_back = view.findViewById(R.id.open_back);//取消

        head_image = new PopupWindow(view);
        head_image.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        head_image.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        head_image.setAnimationStyle(R.style.LoginPopWindowAnim);//设置进入和出场动画
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha=0.5f;
        getActivity().getWindow().setAttributes(lp);
        head_image.setBackgroundDrawable(this.getResources().getDrawable(R.color.white));
        head_image.setOutsideTouchable(true);
        head_image.setFocusable(true);
        head_image.showAtLocation(view, Gravity.BOTTOM, 0, 0);

        head_image.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha=1f;
                getActivity().getWindow().setAttributes(lp);
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
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha=1f;
                getActivity().getWindow().setAttributes(lp);
                head_image.dismiss();
            }
        });

    }

    private void select_diqu(){
        CityPickerView cityPicker = new CityPickerView.Builder(getActivity(),getContext()).textSize(20)
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
    private void select_bin() {

        final CheckBox c1,c2,c3,c4;
        AppCompatTextView many_cancel,many_ok;
        final View view = LayoutInflater.from(getContext()).inflate(R.layout.many_check_pop, null);
        mMenuView = new PopupWindow(view);
        mMenuView.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mMenuView.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha=0.5f;
        getActivity().getWindow().setAttributes(lp);

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
                WindowManager.LayoutParams lp  = getActivity().getWindow().getAttributes();
                lp.alpha=1f;
                getActivity().getWindow().setAttributes(lp);
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
                int flage=0;
                if(c1.isChecked())
                {
                    list_bin.add("0");
                    bin=bin+"糖尿病,";
                    flage = 1;
                }
                if(c2.isChecked())
                {
                    list_bin.add("1");
                    bin=bin+"高血压,";
                    flage = 1;
                }
                if(c3.isChecked())
                {
                    list_bin.add("2");
                    bin=bin+"脑梗,";
                    flage = 1;
                }
                if(c4.isChecked())
                {
                    list_bin.add("3");
                    bin=bin+"其他,";
                    flage =1;
                }
                detail_bin.setText(bin);
                mMenuView.dismiss();
                if(flage ==1)
                {
                    detail_bin.setError(null);
                }
            }
        });
    }


    private void select_birthday(){
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
                if(Integer.parseInt("1"+city)>Integer.parseInt("1"+new SimpleDateFormat("MM", Locale.getDefault()).format(new Date())))
                {
                    city = new SimpleDateFormat("MM", Locale.getDefault()).format(new Date());
                }

                if(Integer.parseInt("1"+district)>Integer.parseInt("1"+new SimpleDateFormat("dd", Locale.getDefault()).format(new Date())))
                {
                    district = new SimpleDateFormat("dd", Locale.getDefault()).format(new Date());
                }

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
                pervince= province;
                int age =  Integer.parseInt(new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date()))-Integer.parseInt(province);
                detail_age.setText(end_birthday);
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha=1f;
                getActivity().getWindow().setAttributes(lp);
                detail_age.setError(null);
            }
            @Override
            public void onCancel() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha=1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
    }

    private void select_sex(){
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
                detail_sex.setText(district);
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha=1f;
                getActivity().getWindow().setAttributes(lp);
                detail_sex.setError(null);
            }
            @Override
            public void onCancel() {
                WindowManager.LayoutParams lp =    getActivity().getWindow().getAttributes();
                lp.alpha=1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
    }

    private void select_zhu(){
        PersonPickerView SexPicker = new PersonPickerView.Builder(getActivity(),getContext()).textSize(20)
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
                detail_zhu.setText(district);
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha=1f;
                getActivity().getWindow().setAttributes(lp);
                detail_zhu.setError(null);
            }
            @Override
            public void onCancel() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha=1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });

    }

    private void init_ok() {
        detail_name.setTextColor(Color.parseColor("#B1B9BD"));
        detail_zhu.setTextColor(Color.parseColor("#B1B9BD"));
        detail_sex.setTextColor(Color.parseColor("#B1B9BD"));
        detail_age.setTextColor(Color.parseColor("#B1B9BD"));
        detail_bin.setTextColor(Color.parseColor("#B1B9BD"));
        detail_phone.setTextColor(Color.parseColor("#B1B9BD"));
        detail_sheng.setTextColor(Color.parseColor("#B1B9BD"));
        detail_team_person.setTextColor(Color.parseColor("#B1B9BD"));
        detail_team.setTextColor(Color.parseColor("#B1B9BD"));
        detail_family.setTextColor(Color.parseColor("#B1B9BD"));
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
    private void init_founch() {
        if(!isopen) {
            detail_bin.setFocusable(false);
            detail_bin.setFocusableInTouchMode(false);//设置不可编辑状态；

            detail_phone.setFocusable(false);
            detail_phone.setFocusableInTouchMode(false);//设置不可编辑状态；

            detail_sheng.setFocusable(false);
            detail_sheng.setFocusableInTouchMode(false);//设置不可编辑状态；

            detail_address.setFocusable(false);
            detail_address.setFocusableInTouchMode(false);//设置不可编辑状态；

            detail_family.setFocusable(false);
            detail_family.setFocusableInTouchMode(false);//设置不可编辑状态；

            detail_team_person.setFocusable(false);
            detail_team_person.setFocusableInTouchMode(false);//设置不可编辑状态；

            detail_team.setFocusable(false);
            detail_team.setFocusableInTouchMode(false);//设置不可编辑状态；
        }else {
            detail_bin.setFocusable(true);
            detail_bin.setFocusableInTouchMode(true);//设置不可编辑状态；

            detail_phone.setFocusable(true);
            detail_phone.setFocusableInTouchMode(true);//设置不可编辑状态；

            detail_sheng.setFocusable(true);
            detail_sheng.setFocusableInTouchMode(true);//设置不可编辑状态；

            detail_address.setFocusable(true);
            detail_address.setFocusableInTouchMode(true);//设置不可编辑状态；

            detail_family.setFocusable(true);
            detail_family.setFocusableInTouchMode(true);//设置不可编辑状态；

            detail_team_person.setFocusable(true);
            detail_team_person.setFocusableInTouchMode(true);//设置不可编辑状态；

            detail_team.setFocusable(true);
            detail_team.setFocusableInTouchMode(true);//设置不可编辑状态；
        }
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
        requestRuntimePermission(permissions,new PermissionListener() {
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
        headimage_flage = 1;
        headimage = getImageContentUri(getContext(),file)+"";
        System.out.println("headimage******"+headimage);
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

                       /* Bitmap bitmap = BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(Uri.fromFile(new File(imagePath))));
                        our_headimage.setImageBitmap(bitmap);
*/
                        Glide.with(getContext())
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



}
