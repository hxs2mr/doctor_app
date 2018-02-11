package microtech.hxswork.com.frame_ui.main.baidu_zx;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.PopupWindow;

import com.alibaba.fastjson.JSON;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import microtech.hxswork.com.frame_core.dialogpicker.city_20170724.DayTimerPickerView;
import microtech.hxswork.com.frame_core.net.RestClent;
import microtech.hxswork.com.frame_core.net.callback.ISuccess;
import microtech.hxswork.com.frame_core.util.Auth;
import microtech.hxswork.com.frame_core.util.JacksonUtil;
import microtech.hxswork.com.frame_core.util.LoadingDialog;
import microtech.hxswork.com.frame_core.util.SAToast;
import microtech.hxswork.com.frame_ui.R;
import microtech.hxswork.com.frame_ui.main.home_deatils.QianDeatail_Edit_Activity;
import microtech.hxswork.com.frame_ui.photoAdapter.PhotoAdapter;
import microtech.hxswork.com.frame_ui.photoAdapter.RecyclerItemClickListener;
import microtech.hxswork.com.photopicker.PhotoPicker;
import microtech.hxswork.com.photopicker.PhotoPreview;

import static microtech.hxswork.com.frame_core.phone.FormatUtil.isMobileNO;
import static microtech.hxswork.com.frame_ui.login.LoginHandler.fristBean;
import static microtech.hxswork.com.frame_ui.login.LoginHandler.userBean;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.home_qian_number_total;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.mtype;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.qian_number;
import static microtech.hxswork.com.frame_ui.main.home_deatils.QianDeatail_Fragmnet.selectedPhotos;

/**
 * Created by microtech on 2017/12/20.
 */

public class BaiduNextActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayoutCompat bin_linear = null;
    private AppCompatTextView result_bin = null;

    private LinearLayoutCompat phone_linear = null;
    private TextInputEditText  result_phone = null;

    private LinearLayoutCompat fuwu_linear = null;
    private AppCompatTextView result_time = null;
    private LinearLayoutCompat next_pop_back_linear = null;
    private AppCompatTextView next_pop_ok = null;
    private RecyclerView baidunext_recycle = null;

    public  static ArrayList<String> Edit_Photos_Baidu;//=new ArrayList<>();
    public PhotoAdapter photoAdapter;
    public List<String> photos= null;
    List<String> list_image;
    int selectimage_flage =0 ;
    private List<String> list_shen;
    AppCompatTextView baidunext_qian;
    List<String> list_image_add;
    String AccessKey="YdSHCtErv3CoRgH9Oa7MTiU15g3XVC86snrsSfMa";//七牛模块
    String SecretKey="RHk263O-wGVGtuJlRqmbMRlXa0Rld2f0pFjp6jEv";
    private PopupWindow mMenuView;
    private      List<String> list_bin ;//= new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baidu_next_pop);
        initView();
        Bundle bundle = this.getIntent().getExtras();
        list_shen=bundle.getStringArrayList("user");//接收传值

    }

    private void initView() {
        Edit_Photos_Baidu=new ArrayList<>();
        list_image_add = new ArrayList<>();
        list_bin = new ArrayList<>();
        photos = new ArrayList<>();
        bin_linear = findViewById(R.id.bin_linear);
        result_bin = findViewById(R.id.result_bin);
        phone_linear = findViewById(R.id.phone_linear);
        result_phone  = findViewById(R.id.result_phone);
        fuwu_linear = findViewById(R.id.fuwu_linear);
        result_time=  findViewById(R.id.result_time);
        next_pop_back_linear = findViewById(R.id.next_pop_back_linear);
        next_pop_ok = findViewById(R.id.next_pop_ok);
        baidunext_recycle = findViewById(R.id.baidunext_recycle);
        baidunext_qian = findViewById(R.id.baidunext_qian);
        next_pop_back_linear.setOnClickListener(this);
        next_pop_ok.setOnClickListener(this);
        bin_linear.setOnClickListener(this);
        fuwu_linear.setOnClickListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        baidunext_recycle.setLayoutManager(manager);

        photoAdapter = new PhotoAdapter(this,Edit_Photos_Baidu,1);
        baidunext_recycle.setLayoutManager(new StaggeredGridLayoutManager(4, OrientationHelper.VERTICAL));//设置recycleview的布局

        baidunext_recycle.setAdapter(photoAdapter);

        baidunext_recycle.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                    if (photoAdapter.getItemViewType(position) == PhotoAdapter.TYPE_ADD)//表示添加图片
                    {
                        PhotoPicker.builder()
                                .setPhotoCount(PhotoAdapter.MAX)
                                .setShowCamera(true)
                                .setPreviewEnabled(false)
                                .setSelected(Edit_Photos_Baidu)
                                .start(BaiduNextActivity.this);
                    } else {//表示图片的查看
                        PhotoPreview.builder()
                                .setPhotos(Edit_Photos_Baidu)
                                .setCurrentItem(position)
                                .start(BaiduNextActivity.this);
                    }
            }
        }));
    }
    private boolean checkForm(){
        final  String phone = result_phone.getText().toString();

        //检查输入的文本是否正确
        boolean isPass = true;
        if(selectimage_flage==0){
            baidunext_qian.setError("请添加患者签约资料");
            isPass = false;
        }else {
            baidunext_qian.setError(null);
        }
        if(phone.isEmpty()){
            result_phone.setError("输入手机号");
            isPass = false;
        }else {
            if(!isMobileNO(phone))
            {
                result_phone.setError("手机号格式不对!");
                isPass = false;
            }else {
                result_phone.setError(null);
            }
            //result_phone.setError(null);
        }
        if(list_bin.size()<=0)
        {
            result_bin.setError("请选择患者病种!");
            isPass = false;
        }else {
            result_bin.setError(null);
        }
        return isPass;
    }
    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.next_pop_back_linear) {
            finish();
        }else if(i ==R.id.next_pop_ok){//网络请求
            if(checkForm()) {

                if(list_image.size()>0)
                {
                    LoadingDialog.showDialogForLoading(this);
                    for (int k = 0; k < list_image.size(); k++) {
                        System.out.println("****2");
                        getUpimg(list_image.get(k));
                    }
                }else {
                    SAToast.makeText(BaiduNextActivity.this,"请添加签约资料!").show();
                }
               // put_net();
            }else {
                SAToast.makeText(BaiduNextActivity.this,"请填写完资料在提交!").show();
            }
        }else if(i == R.id.bin_linear)//病种选择
        {
            ManyPopuWindow();
        }else if(i == R.id.fuwu_linear)//服务日期选择
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
    }

    private void put_net(){

        int sex_flage=0;
        if(list_shen.get(2).equals("男"))
        {
            sex_flage = 1;
        }else {
            sex_flage = 2;
        }

        RestClent.builder()
                .url("signAddEdit")
                .loader(this)
                .params("region_id",fristBean.getRegion_id())
                .params("gov_id",fristBean.getGov_id())
                .params("doctor_id",userBean.getId())
                .params("signs_id","")
                .params("avatar","")
                .params("name",list_shen.get(0))
                .params("sex",sex_flage)
                .params("birthday",list_shen.get(3))
                .params("id_card",list_shen.get(5))
                .params("nation",list_shen.get(1))
                .params("address",list_shen.get(4))
                .params("diseases", JSON.toJSONString(list_bin))
                .params("phone",result_phone.getText().toString())
                .params("upload_img", JSON.toJSONString(list_image_add))
                .params("singsTime","")
                .params("time_stop",result_time.getText().toString())
                .params("channel","android")
                .params("fmy_name","")
                .params("type",mtype)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        System.out.println("签约患者返回的数据**********"+response);
                        int code = JSON.parseObject(response).getInteger("code");
                        if(code == 200)
                        {
                                SAToast.makeText(BaiduNextActivity.this,"添加成功！").show();
                                Intent data = new Intent();
                                data.putExtra("UPDATE_PATIENT","-1");
                                setResult(103, data);

                                if(mtype==0)
                                {
                                    home_qian_number_total++;
                                    qian_number.setText(home_qian_number_total+"");
                                }
                            finish();
                        }else if (code == 209){
                            list_image_add.clear();
                            SAToast.makeText(BaiduNextActivity.this,"用户以存在！").show();
                        }else  if(code == 232)
                        {
                            list_image_add.clear();
                            SAToast.makeText(BaiduNextActivity.this,"身份证号码有误！").show();
                        }else
                            {
                            list_image_add.clear();
                            SAToast.makeText(BaiduNextActivity.this,"添加失败！").show();
                        }

                    }
                })
                .build()
                .post();
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
            }else {
                list_image.clear();
                selectimage_flage = 0;
            }
            Edit_Photos_Baidu.clear();
            if (photos != null) {
                selectimage_flage = 1;
                Edit_Photos_Baidu.addAll(photos);
            }
            photoAdapter.notifyDataSetChanged();
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

                                if(list_image_add.size() == list_image.size())
                                {
                                    LoadingDialog.cancelDialogForLoading();
                                    put_net();
                                }
                            }else
                            {
                                SAToast.makeText(BaiduNextActivity.this,"图片上传异常").show();
                                LoadingDialog.cancelDialogForLoading();
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, null);

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
                list_bin.clear();
                String bin="";
                int flage =0 ;
                if(c1.isChecked())
                {
                    list_bin.add("0");
                    bin=bin+"糖尿病,";
                    flage =1;
                }
                if(c2.isChecked())
                {
                    list_bin.add("1");
                    bin=bin+"高血压,";
                    flage =1;
                }
                if(c3.isChecked())
                {
                    list_bin.add("2");
                    bin=bin+"脑梗,";
                    flage =1;
                }
                if(c4.isChecked())
                {
                    list_bin.add("3");
                    bin=bin+"其他,";
                    flage =1;
                }
                result_bin.setText(bin);
                mMenuView.dismiss();
                if(flage==1)
                {
                    result_bin.setError(null);
                }
            }
        });
    }
}
