package microtech.hxswork.com.frame_ui.main.baidu_zx;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.sdk.model.IDCardResult;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import microtech.hxswork.com.frame_core.dialogpicker.city_20170724.CityPickerView;
import microtech.hxswork.com.frame_core.dialogpicker.city_20170724.DayTimerPickerView;
import microtech.hxswork.com.frame_core.dialogpicker.city_20170724.PersonPickerView;
import microtech.hxswork.com.frame_core.dialogpicker.city_20170724.bean.CityBean;
import microtech.hxswork.com.frame_core.dialogpicker.city_20170724.bean.DistrictBean;
import microtech.hxswork.com.frame_core.dialogpicker.city_20170724.bean.ProvinceBean;
import microtech.hxswork.com.frame_core.init.Frame;
import microtech.hxswork.com.frame_core.middle.MiddleFragment;
import microtech.hxswork.com.frame_core.net.RestClent;
import microtech.hxswork.com.frame_core.net.callback.ISuccess;
import microtech.hxswork.com.frame_core.util.KeyBordUtil;
import microtech.hxswork.com.frame_ui.R;
import microtech.hxswork.com.frame_ui.main.home.HomeFragment;
import microtech.hxswork.com.frame_ui.main.send.Send_Text_PhotoActivity;

import static microtech.hxswork.com.frame_core.init.Frame.getApplicationContext;
import static microtech.hxswork.com.frame_core.phone.FormatUtil.isIdCardNo;
import static microtech.hxswork.com.frame_ui.login.LoginHandler.fristBean;
import static microtech.hxswork.com.frame_ui.login.LoginHandler.userBean;
import static microtech.hxswork.com.frame_ui.main.Patient.PatientFragment.mRefreshHandler;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.mtype;

/**
 * Created by microtech on 2017/11/29.处理身份证识别后的结果
 */

public class BaiDuFragment extends MiddleFragment implements View.OnClickListener {
    private boolean hasGotToken = false;
    private AlertDialog.Builder alertDialog;
    private ImageView id_card_image = null;
    private AppCompatEditText t_name= null;
    private AppCompatTextView t_sex= null;
    private AppCompatTextView t_zhu= null;
    private AppCompatTextView t_bir= null;
    private AppCompatEditText t_address= null;
    private AppCompatEditText t_number= null;
    private ProgressBar pb = null;
    private String image_Path;
    private AppCompatButton button_next = null;
    private LinearLayoutCompat follow_add_back_linear=null;
    private AppCompatTextView result_city = null;
    //设置图片的加载测曰
    private static final RequestOptions RECYCLER_OPTIONS=
            new RequestOptions()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate();
    String[] data ;
    public static ArrayList<String> list_shen;
    PopupWindow next_pop;
    public static ArrayList<String> mDistrictBeanArrayList1 ;//性别的选取

    private  String province="";
    private  String city="";
    private  String district="";
    @Override
    public Object setLayout() {
        return R.layout.baidu_fragment;
    }


    private boolean checkForm(){ //检查输入的文本是否正确
        final  String name = t_name.getText().toString();
        final  String sex = t_sex.getText().toString();
        final  String zhu = t_zhu.getText().toString();
        final  String bir = t_bir.getText().toString();

        final  String address = t_address.getText().toString();
        final  String city = result_city.getText().toString();
        final  String number = t_number.getText().toString();

        boolean isPass = true;
        if(name.isEmpty()){
            t_name.setError("请输入姓名");
            isPass = false;
        }else {
            t_name.setError(null);
        }
        if(city.isEmpty()){
            result_city.setError("请选择居住地区");
            isPass = false;
        }else {
            result_city.setError(null);
        }
        if(sex.isEmpty()){
            t_sex.setError("性别");
            isPass = false;
        }else {
            t_sex.setError(null);
        }
        if(zhu.isEmpty()){
            t_zhu.setError("请选择名族");
            isPass = false;
        }else {
            t_zhu.setError(null);
        }

        if(bir.isEmpty()){
            t_bir.setError("请选择出生日期");
            isPass = false;
        }else {
            t_bir.setError(null);
        }
        if(address.isEmpty()){
            t_address.setError("请输入地址");
            isPass = false;
        }else {
            t_address.setError(null);
        }
        if(number.isEmpty()){
            t_number.setError("请添加患者身份证号");
            isPass = false;
        }else {
            if(!isIdCardNo(number))
            {
                t_number.setError("身份证格式不对!");
                isPass = false;
            }else {
                t_number .setError(null);
            }
        }
        return isPass;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = new String[2];
        final Bundle args = getArguments();
        data = args.getStringArray("data");
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        list_shen=new ArrayList<>();
        id_card_image = bind(R.id.id_card_image);
        t_name = bind(R.id.result_name);
        t_sex = bind(R.id.result_sex);
        t_zhu = bind(R.id.result_zhu);
        result_city= bind(R.id.result_city);


        follow_add_back_linear = bind(R.id.follow_add_back_linear);
        t_address = bind(R.id.result_address);
        t_number = bind(R.id.result_number);
        t_bir = bind(R.id.result_brithday);
        pb = bind(R.id.reco_recognize_bar);
        button_next = bind(R.id.baidu_next);
        button_next.setOnClickListener(this);
        follow_add_back_linear.setOnClickListener(this);
        t_zhu.setOnClickListener(this);
        t_bir.setOnClickListener(this);
        t_sex.setOnClickListener(this);
        result_city.setOnClickListener(this);
        recIDCard(data[0],data[1]);//加载数据

        mDistrictBeanArrayList1 = new ArrayList<>();
        mDistrictBeanArrayList1.add("男");
        mDistrictBeanArrayList1.add("女");
    }
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 1://表示成功获取到数据
                    IDCardResult   result = (IDCardResult) msg.obj;
                    System.out.println("********身份证扫描结果******"+result);
                    System.out.println("********身份证扫描结果1111******"+result.getName());
                    if(result.getName()!= null) {
                        t_name.setText(result.getName().toString() + "");
                    }

                    if(result.getGender()!= null) {
                        t_sex.setText( result.getGender().toString()+"");
                    }
                    if(result.getEthnic()!= null) {
                        t_zhu.setText(result.getEthnic().toString()+"");
                    }

                    if(result.getAddress()!= null)
                    {
                        if(!result.getAddress().toString().equals(""))
                        {
                            put_address_net(result.getAddress().toString()+"");
                        }
                    }
                    if(result.getBirthday()!= null)
                    {
                        t_bir.setText( result.getBirthday().toString()+"");
                    }
                    if(result.getIdNumber()!= null)
                    {
                        t_number.setText( result.getIdNumber().toString()+"");
                    }
                    Glide.with(getContext())
                            .load(image_Path)
                            .apply(RECYCLER_OPTIONS)
                            .into(id_card_image);
                    pb.setVisibility(View.GONE);
                    break;
                case 2://表示获取数据失败
                    pb.setVisibility(View.GONE);
                    break;
            }
        }
    };

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
       // initAccessTokenWithAkSk();//获取百度授权的识别token
    }

    private void recIDCard(final String idCardSide, final String filePath) {
        image_Path = filePath;
        pb.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                //扫描后返回的而数据
                IDCardParams param = new IDCardParams();
                param.setImageFile(new File(filePath));
                // 设置身份证正反面
                param.setIdCardSide(idCardSide);
                // 设置方向检测
                param.setDetectDirection(true);
                // 设置图像参数压缩质量0-100, 越大图像质量越好但是请求时间越长。 不设置则默认值为20
                param.setImageQuality(20);

                OCR.getInstance().recognizeIDCard(param, new OnResultListener<IDCardResult>() {
                    @Override
                    public void onResult(IDCardResult result) {
                        if (result != null) {

                            Message msg = new Message();
                            msg.what = 1;
                            msg.obj = result;
                            handler.sendMessage(msg);
                        }else {
                            Message msg = new Message();
                            msg.what = 2;
                            handler.sendMessage(msg);
                        }
                    }
                    @Override
                    public void onError(OCRError error) {
                        Message msg = new Message();
                        msg.what = 3;
                        handler.sendMessage(msg);
                    }
                });

            }
        }).start();

    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.baidu_next) {//下一步的操作
            if (checkForm()) {
                list_shen.add(t_name.getText().toString());
                list_shen.add(t_zhu.getText().toString());
                list_shen.add(t_sex.getText().toString());
                list_shen.add(t_bir.getText().toString());
                list_shen.add(result_city.getText().toString()+"|"+t_address.getText().toString());
                list_shen.add(t_number.getText().toString());

                Intent intent = new Intent(getActivity(), BaiduNextActivity.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("user", list_shen);
                intent.putExtras(bundle);
                startActivityForResult(intent, 102);
            }
        } else if (i == R.id.follow_add_back_linear) {
            getSupportDelegate().pop();
        } else if (i == R.id.result_zhu) {
            select_zhu();//选取名族
        } else if (i == R.id.result_brithday) {
            select_bir();//选择生日
        } else if (i == R.id.result_sex) {
            select_sex();//选择性别
        } else if (i == R.id.result_city)
        {
            select_diqu();
        }
    }

    private void show_pop() {
            final View view = LayoutInflater.from(getContext()).inflate(R.layout.baidu_next_pop, null);
            LinearLayoutCompat back = view.findViewById(R.id.next_pop_back_linear);
            next_pop = new PopupWindow(view);
             next_pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
             next_pop.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            next_pop.setAnimationStyle(R.style.BintPopWindowAnim);//设置进入和出场动画
            next_pop.setBackgroundDrawable(this.getResources().getDrawable(R.color.white));
            next_pop.setOutsideTouchable(true);
            next_pop.setFocusable(true);
            next_pop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    next_pop.dismiss();
                    list_shen.clear();
                }
            });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode ==103) {//添加成功
            Bundle bundle = new Bundle();
            bundle.putString("UPDATE_PATIENT","-1");
            setFragmentResult(103, bundle);
            System.out.println("*****出去asdasd*****");
            if(mtype == 1){
                mRefreshHandler.refresh_add(getContext(),fristBean.getRegion_id(),fristBean.getGov_id(),userBean.getId());
            }
            getSupportDelegate().pop();
        }
    }
    private void select_bir()
    {
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

                //返回结果
                int age =  Integer.parseInt(new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date()))-Integer.parseInt(province);
                t_bir.setText( province+"-"+city+"-"+district);
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
    private void select_zhu(){
        mDistrictBeanArrayList1.clear();
        initmingzhu();
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
                t_zhu.setText(district);
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha=1f;
                getActivity().getWindow().setAttributes(lp);
                t_zhu.setError(null);
            }
            @Override
            public void onCancel() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha=1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
    }

    private void select_diqu(){
        new KeyBordUtil().hideSoftKeyboard(result_city);
        String p="贵州省";
        String c="贵阳市";
        String d="观山湖区";
        if(!province.equals(""))
        {
            p=province;
        }
        if(!city.equals(""))
        {
            c= city;
        }
        if (!district.equals(""))
        {
            d=district;
        }
        CityPickerView cityPicker = new CityPickerView.Builder(getActivity(),getContext()).textSize(20)
                .titleTextColor("#000000")
                .backgroundPop(0xa0000000)
                .province(p)
                .city(c)
                .district(d)
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
                result_city.setText(province.getName() + "|" + city.getName() + "|" + district.getName());
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha=1f;
                getActivity().getWindow().setAttributes(lp);
                result_city.setError(null);
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
        mDistrictBeanArrayList1.clear();
        mDistrictBeanArrayList1.add("男");
        mDistrictBeanArrayList1.add("女");
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
                t_sex.setText(district);
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
                        final JSONObject object = JSON.parseObject(response).getJSONObject("obj");
                        final int code = JSON.parseObject(response).getInteger("code");
                        if(code == 200)
                        {
                        t_address.setText(object.getString("address"));
                        result_city.setText(object.getString("province")+"|"+object.getString("city")+"|"+object.getString("district"));
                        province = object.getString("province");
                        city = object.getString("city");
                        district = object.getString("district");
                    }
                    }
                })
                .build()
                .post();
    }

}
