package microtech.hxswork.com.frame_ui.main.send;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.WeakHashMap;

import me.yokeyword.fragmentation.SwipeBackLayout;
import me.yokeyword.fragmentation_swipeback.SwipeBackActivity;
import microtech.hxswork.com.frame_core.middle.MiddleFragment;
import microtech.hxswork.com.frame_core.net.RestClent;
import microtech.hxswork.com.frame_core.net.callback.ISuccess;
import microtech.hxswork.com.frame_core.ui.recyclew.ItemType;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleFields;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleItemEntity;
import microtech.hxswork.com.frame_core.util.Auth;
import microtech.hxswork.com.frame_core.util.LoadingDialog;
import microtech.hxswork.com.frame_core.util.SAToast;
import microtech.hxswork.com.frame_ui.R;
import microtech.hxswork.com.frame_ui.main.baidu_zx.Baidu_NoShenfen_Activity;
import microtech.hxswork.com.frame_ui.main.home_deatils.QianDeatail_Edit_Activity;
import microtech.hxswork.com.frame_ui.main.szxing.utils.NetUtil;
import microtech.hxswork.com.frame_ui.photoAdapter.PhotoAdapter;
import microtech.hxswork.com.frame_ui.photoAdapter.RecyclerItemClickListener;
import microtech.hxswork.com.photopicker.PhotoPicker;
import microtech.hxswork.com.photopicker.PhotoPreview;

import static microtech.hxswork.com.frame_ui.login.LoginHandler.fristBean;
import static microtech.hxswork.com.frame_ui.login.LoginHandler.userBean;
import static microtech.hxswork.com.frame_ui.main.Patient.PatientNextFragment.adapter;
import static microtech.hxswork.com.frame_ui.main.home_deatils.QianDeatail_Fragmnet.selectedPhotos;

/**
 * Created by microtech on 2017/12/8.发送图片和文本的frgament
 */

public class Send_Text_PhotoActivity extends AppCompatActivity implements View.OnClickListener {
    private  LinearLayoutCompat send_activity_photo = null;//发送图片的布局
    private  LinearLayoutCompat send_activity_text = null;//发送文本的布局

    private LinearLayoutCompat send_activity_back_linear = null;//返回
    private AppCompatTextView send_activity_title = null;//标题
    private AppCompatTextView send_activity_ok = null;//保存

    //
    private AppCompatEditText send_activity_photo_edit = null;//发送图片的标题
    private RecyclerView send_activity_photo_recycler = null;

    //
    private AppCompatEditText send_activity_text_edit1 = null;

    String[] str=null;
    public  static ArrayList<String> Select_Photos;//=new ArrayList<>();
    public PhotoAdapter photoAdapter;
    public List<String> photos= null;
    public List<String> list_image = null;
    private WeakHashMap<String,Object> list_params;
    private  List<String> list_image_add = null;
    private  int select_image = 0 ;
    String AccessKey="YdSHCtErv3CoRgH9Oa7MTiU15g3XVC86snrsSfMa";//七牛模块
    String SecretKey="RHk263O-wGVGtuJlRqmbMRlXa0Rld2f0pFjp6jEv";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_text_image_activity);
        Bundle bundle = this.getIntent().getExtras();
        str=bundle.getStringArray("user");//接收传值
        list_params = new WeakHashMap<>();
        list_image_add = new ArrayList<>();
        list_image = new ArrayList<>();
        Select_Photos=new ArrayList<>();
        onBindView();
        initRecycle();
    }

    public void onBindView() {
        send_activity_photo = findViewById(R.id.send_activity_photo);
        send_activity_text = findViewById(R.id.send_activity_text);
        send_activity_back_linear = findViewById(R.id.send_activity_back_linear);
        send_activity_title = findViewById(R.id.send_activity_title);
        send_activity_ok = findViewById(R.id.send_activity_ok);
        send_activity_photo_edit = findViewById(R.id.send_activity_photo_edit);
        send_activity_photo_recycler = findViewById(R.id.send_activity_photo_recycler);
        send_activity_text_edit1 = findViewById(R.id.send_activity_text_edit1);

        send_activity_back_linear.setOnClickListener(this);
        send_activity_title.setText(str[0]);
        if(str[0].equals("文本"))
        {
            send_activity_text.setVisibility(View.VISIBLE);
            send_activity_photo.setVisibility(View.GONE);
        }else {
            send_activity_text.setVisibility(View.GONE);
            send_activity_photo.setVisibility(View.VISIBLE);
        }
        send_activity_ok.setOnClickListener(this);
    }

    private void initRecycle() {
        photoAdapter = new PhotoAdapter(this,Select_Photos,1);
        send_activity_photo_recycler.setLayoutManager(new StaggeredGridLayoutManager(4, OrientationHelper.VERTICAL));//设置recycleview的布局

        send_activity_photo_recycler.setAdapter(photoAdapter);
        send_activity_photo_recycler.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(photoAdapter.getItemViewType(position) == PhotoAdapter.TYPE_ADD)//表示添加图片
                {
                    PhotoPicker.builder()
                            .setPhotoCount(PhotoAdapter.MAX)
                            .setShowCamera(true)
                            .setPreviewEnabled(false)
                            .setSelected(Select_Photos)
                            .start(Send_Text_PhotoActivity.this);
                }else {//表示图片的查看
                    PhotoPreview.builder()
                            .setPhotos(Select_Photos)
                            .setCurrentItem(position)
                            .start(Send_Text_PhotoActivity.this);
                }
            }
        }));
    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.send_activity_back_linear) {
            finish();
        }else if(i == R.id.send_activity_ok)
        {
            if(str[0].equals("文本")) {
                if(!send_activity_text_edit1.getText().toString().equals(""))
                {
                    put_net();
                }else {
                        SAToast.makeText(this,"发送内容不能为空!").show();
                }
            }else if(str[0].equals("图片")){
                    if(select_image == 1&&!send_activity_photo_edit.getText().toString().equals(""))
                    {
                        LoadingDialog.showDialogForLoading(this);
                        for (int j = 0 ; j < list_image.size();j++)
                        {
                            getUpimg(list_image.get(j));
                        }

                    }else {
                        SAToast.makeText(this,"发送标题和图片不能为空!").show();
                    }
            }
        }

    }

    private void put_net() {

        init_params();

        RestClent.builder()
                .url("patients_info_input")
                .loader(this)
                .params(list_params)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final int code = JSON.parseObject(response).getInteger("code");
                        if(code == 200)
                        {
                            SAToast.makeText(Send_Text_PhotoActivity.this,"数据录入成功").show();
                            int type = 0 ;
                            String title ="";
                            String content = "";
                            if(str[0].equals("文本")){
                                type =ItemType.TEXT;
                                content =  send_activity_text_edit1.getText().toString();
                            }else    if(str[0].equals("图片")){
                                type =ItemType.IMAGE;
                                title = send_activity_photo_edit.getText().toString();
                            }

                            final MultipleItemEntity entity = MultipleItemEntity.builder()
                                    .setField(MultipleFields.ITEM_TYPE,type)
                                    .setField(MultipleFields.IMAGE_ARRAY,list_image_add)
                                    .setField(MultipleFields.TIME,new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()))
                                    .setField(MultipleFields.TITLE,title)
                                    .setField(MultipleFields.TEXT,content)
                                    .build();
                            adapter.addData(0,entity);
                            finish();
                        }
                    }
                })
                .build()
                .post();

    }

    private void init_params() {
        String text_data="";
        if(str[0].equals("文本")){
            text_data=send_activity_text_edit1.getText().toString();
        }else {
            text_data="";
        }
        list_params.put("region_id",fristBean.getRegion_id());
        list_params.put("gov_id",fristBean.getGov_id());
        list_params.put("doctor_id",userBean.getId());
        list_params.put("user_id",str[4]);
        list_params.put("text",text_data);

        if(list_image_add.size() == 0 )
        {
            list_params.put("image","");
        }else {
            list_params.put("image",JSON.toJSONString(list_image_add));
        }
        list_params.put("audio","");
        if(str[0].equals("文本"))
        {
            list_params.put("title","");
        }else {
            list_params.put("title",send_activity_photo_edit.getText().toString());
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
                                    put_net();
                                }
                            }else
                            {
                                SAToast.makeText(Send_Text_PhotoActivity.this,"图片上传异常").show();
                                LoadingDialog.cancelDialogForLoading();
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, null);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK &&
                (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)) {
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                select_image = 1;
                list_image = photos;
            }
            Select_Photos.clear();
            if (photos != null) {
                Select_Photos.addAll(photos);
            }
            photoAdapter.notifyDataSetChanged();
        }
    }
}
