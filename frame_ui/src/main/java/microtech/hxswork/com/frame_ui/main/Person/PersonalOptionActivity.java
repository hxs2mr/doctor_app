package microtech.hxswork.com.frame_ui.main.Person;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.WeakHashMap;

import microtech.hxswork.com.frame_core.net.RestClent;
import microtech.hxswork.com.frame_core.net.callback.ISuccess;
import microtech.hxswork.com.frame_core.util.Auth;
import microtech.hxswork.com.frame_core.util.LoadingDialog;
import microtech.hxswork.com.frame_core.util.SAToast;
import microtech.hxswork.com.frame_ui.R;
import microtech.hxswork.com.frame_ui.main.baidu_zx.Baidu_NoShenfen_Activity;
import microtech.hxswork.com.frame_ui.photoAdapter.PhotoAdapter;
import microtech.hxswork.com.frame_ui.photoAdapter.RecyclerItemClickListener;
import microtech.hxswork.com.photopicker.PhotoPicker;
import microtech.hxswork.com.photopicker.PhotoPreview;

import static microtech.hxswork.com.frame_ui.login.LoginHandler.fristBean;
import static microtech.hxswork.com.frame_ui.login.LoginHandler.userBean;
import static microtech.hxswork.com.frame_ui.main.home_deatils.QianDeatail_Fragmnet.selectedPhotos;


/**
 * Created by microtech on 2017/9/5.吐槽入口
 */

public class PersonalOptionActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView option_recycleview;

    LinearLayoutCompat option_back_linear;
    ImageView next_back;
    public ArrayList<String> selectedPhotos=new ArrayList<>();
    public PhotoAdapter photoAdapter;
    public List<String> photos= null;
    String AccessKey="YdSHCtErv3CoRgH9Oa7MTiU15g3XVC86snrsSfMa";//七牛模块
    String SecretKey="RHk263O-wGVGtuJlRqmbMRlXa0Rld2f0pFjp6jEv";
    String our_Data[]={null};
    String option_url="https://doc.newmicrotech.cn/otsmobile/app/feedbacks?";
    AppCompatEditText option_edittext;
    AppCompatEditText option_phone;
    List<String> list_image;
    List<String> list_endimage;
    List<String> list_image_add;
    AppCompatTextView option_tijiao;
    WeakHashMap<String,Object> list_params;
    Handler handler;
    PopupWindow pop;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_option_fragment);
        initView();
    }


    private boolean checkForm(){
        final  String bin = option_edittext.getText().toString();
        final  String phone = option_phone.getText().toString();
        //检查输入的文本是否正确
        boolean isPass = true;
        if(bin.isEmpty()){
            option_edittext.setError("请填写反馈意见");
            isPass = false;
        }else {
            option_edittext.setError(null);
        }
        return isPass;
    }
    protected void initView() {
        list_params = new WeakHashMap<>();
        list_image = new ArrayList<>();
        list_endimage = new ArrayList<>();
        list_image_add = new ArrayList<>();
        option_recycleview = findViewById(R.id.option_recycleview);
        option_back_linear = findViewById(R.id.option_back_linear);
        option_edittext  = findViewById(R.id.option_edittext);
        option_phone =  findViewById(R.id.option_phone);
        option_tijiao = findViewById(R.id.option_tijiao);

        option_back_linear.setOnClickListener(this);
        option_tijiao.setOnClickListener(this);

        photoAdapter = new PhotoAdapter(this,selectedPhotos,1);

        option_recycleview.setLayoutManager(new StaggeredGridLayoutManager(4, OrientationHelper.VERTICAL));//设置recycleview的布局

        option_recycleview.setAdapter(photoAdapter);

        option_recycleview.addOnItemTouchListener(new RecyclerItemClickListener(getBaseContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(photoAdapter.getItemViewType(position) == PhotoAdapter.TYPE_ADD)
                {
                    PhotoPicker.builder()
                            .setPhotoCount(PhotoAdapter.MAX)
                            .setShowCamera(true)
                            .setPreviewEnabled(false)
                            .setSelected(selectedPhotos)
                            .start(PersonalOptionActivity.this);
                }else {
                    PhotoPreview.builder()
                            .setPhotos(selectedPhotos)
                            .setCurrentItem(position)
                            .setFlage(1)
                            .start(PersonalOptionActivity.this);
                }
            }
        }));
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
            selectedPhotos.clear();
            if (photos != null) {
                selectedPhotos.addAll(photos);
            }
            photoAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View view) {
        int i1 = view.getId();
        if (i1 == R.id.option_back_linear) {
            finish();

        } else if (i1 == R.id.option_tijiao) {

                if(checkForm()) {
                if (list_image.size() > 0) {
                    LoadingDialog.showDialogForLoading(this);
                    for (int i = 0; i < list_image.size(); i++) {
                        getUpimg(list_image.get(i));
                    }
                } else {
                    thread_noimgae();
                }
                } else {
                SAToast.makeText(this, "反馈意见不能为空!").show();
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
                                    LoadingDialog.cancelDialogForLoading();
                                    thread_noimgae();
                                }
                            }else
                            {
                                SAToast.makeText(PersonalOptionActivity.this,"图片上传异常").show();
                                LoadingDialog.cancelDialogForLoading();
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, null);

    }

    private void thread_noimgae()
    {
        init_params();
        RestClent.builder()
                .url("opinion")
                .loader(this)
                .params(list_params)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        System.out.println("意见反馈的结果**********"+response);
                        int code  = JSON.parseObject(response).getInteger("code");
                        if(code == 200)
                        {
                            dialog_photo();
                        }
                    }
                })
                .build()
                .post();


    }

    private void init_params() {
        list_params.put("region_id", fristBean.getRegion_id());
        list_params.put("gov_id", fristBean.getGov_id());
        list_params.put("doctor_id", userBean.getId());
        list_params.put("text", option_edittext.getText().toString());
        if (list_image_add.size() == 0)
        {
            list_params.put("images","");
        }else {
            list_params.put("images",JSON.toJSONString(list_image_add));
        }
        list_params.put("phone",option_phone.getText().toString());
        list_params.put("channel","android");
    }

    private void toast(final String msg) {
          runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(PersonalOptionActivity.this,msg,Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void dialog_photo()//查看头像的大图
    {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View imgEntryView = inflater.inflate(R.layout.option_pop, null); // 加载自定义的布局文件
        LinearLayoutCompat bind_linear_back = imgEntryView.findViewById(R.id.option_pop_back_linear);

        TextView qianok_back = imgEntryView.findViewById(R.id.qianok_back);
        pop = new PopupWindow(imgEntryView);
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setAnimationStyle(R.style.BintPopWindowAnim);//设置进入和出场动画
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);
        pop.setBackgroundDrawable(getResources().getDrawable(R.color.white));
        pop.setOutsideTouchable(true);
        pop.setFocusable(true);
        pop.showAtLocation(imgEntryView, Gravity.BOTTOM, 0, 0);
        // 点击布局文件（也可以理解为点击大图）后关闭dialog，这里的dialog不需要按钮
        bind_linear_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                WindowManager.LayoutParams lp =getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
                pop.dismiss();
                finish();
            }
        });
        qianok_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
                pop.dismiss();
                finish();
            }
        });
    }
}
