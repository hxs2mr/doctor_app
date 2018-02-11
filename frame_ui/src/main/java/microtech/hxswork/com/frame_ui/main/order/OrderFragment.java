package microtech.hxswork.com.frame_ui.main.order;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.joanzapata.iconify.widget.IconTextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import microtech.hxswork.com.frame_core.middle.MiddleFragment;
import microtech.hxswork.com.frame_core.util.UtilsStyle;
import microtech.hxswork.com.frame_ui.R;
import microtech.hxswork.com.frame_ui.main.baidu_zx.BaiDuFragment;
import microtech.hxswork.com.frame_ui.main.baidu_zx.Baidu_NoShenfen_Activity;
import microtech.hxswork.com.frame_ui.main.baidu_zx.FileUtil;
import microtech.hxswork.com.frame_ui.main.home.HomeFragment;
import microtech.hxswork.com.frame_ui.main.home_deatils.Follow_Add_Frgamnet;

import static microtech.hxswork.com.frame_core.init.Frame.getApplicationContext;
import static microtech.hxswork.com.frame_ui.login.LoginHandler.fristBean;
import static microtech.hxswork.com.frame_ui.login.LoginHandler.userBean;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.mtype;
import static microtech.hxswork.com.frame_ui.main.order.OrderRefreshHandler.mAdapter;

/**
 * Created by microtech on 2017/11/30.
 */

public class OrderFragment extends MiddleFragment implements View.OnClickListener {
    private static final int REQUEST_CODE_PICK_IMAGE_FRONT = 201;
    private static final int REQUEST_CODE_PICK_IMAGE_BACK = 202;
    private static final int REQUEST_CODE_CAMERA = 102;
    private static final int UPDATE_PATIENT = 103;
    public static String mType = null;//根据这个来判断是哪个界面
    public static String Number_no = null;//
    public static String Nnumber_status = null;//
    public static String Nnumber_time = null;//根据这个来判断是哪个界面
    IconTextView back =null;
    AppCompatTextView order_right=null;
     public  static  RecyclerView recyclerView = null;
    SwipeRefreshLayout home_swipe = null;
    Toolbar  toolbar_qian = null;
    Toolbar  toolbar= null;
    AppCompatTextView  order_title = null;
    private boolean hasGotToken = false;
    AppCompatImageView order_right_follow = null;

    LinearLayoutCompat toolbar_follow_linear= null;

    AppCompatTextView  order_day= null;
    AppCompatTextView  order_xin = null;
    AppCompatTextView  order_year = null;

    LinearLayoutCompat order_back_linear = null;
    private String URL = null;
    private OrderRefreshHandler mRefreshHandler = null;


    public  static LinearLayoutCompat no_order_linear = null;
    public  static AppCompatImageView no_order_image = null;
    public  static AppCompatTextView no_order_content = null;

    public static int order_content_flage ;

    private View nodata_view = null;
    String baidu_token=null;//获取百度的身份证识别token
    Bundle mArgs = null;
    View vHeadder = null;
    @Override
    public Object setLayout() {
        return R.layout.order_qian_fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UtilsStyle.setStatusBarMode((Activity) getContext(),true);//设置状态栏的图标为黑色
        mArgs = new Bundle();//用于传递扫描的结果
        final Bundle args = getArguments();
        mType = args.getString(HomeFragment.ORDER_TYPE);
        if(mType.equals("qian")){
            order_content_flage =0;
            URL= "signings";
            initAccessTokenWithAkSk();//获取百度授权的识别token
        }else if(mType.equals("follow")){
            order_content_flage =0;
            URL= "visits";
        }else {
            URL= "signings";
        }
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
            order_back_linear = bind(R.id.order_back_linear);
            no_order_linear = bind(R.id.no_order_linear);
            no_order_image = bind(R.id.no_order_image);
            no_order_content = bind(R.id.no_order_content);
            back = bind(R.id.order_back);
            recyclerView = bind(R.id.order_recyclerview);
             home_swipe =bind(R.id.home_swipe);
            order_right = bind(R.id.order_right);
            toolbar = bind(R.id.toolbar);
            order_title = bind(R.id.order_title);
            order_right_follow = bind(R.id.order_right_follow);
             toolbar_follow_linear = bind(R.id.toolbar_follow_linear);

            order_day = bind(R.id.order_day);
            order_xin = bind(R.id.order_xin);
            order_year = bind(R.id.order_year);

            order_back_linear.setOnClickListener(this);
            order_right.setOnClickListener(this);
            order_right_follow.setOnClickListener(this);
            recyclerView.setVisibility(View.VISIBLE);
            no_order_linear.setVisibility(View.GONE);

        if(mType.equals("qian")){
            mRefreshHandler = OrderRefreshHandler.create(home_swipe,recyclerView,new OrderQianDataConverter(),mType);
        }else if(mType.equals("follow")){
            mRefreshHandler = OrderRefreshHandler.create(home_swipe,recyclerView,new OrderFollowUpDataConverter(),mType);
        }else {
            mRefreshHandler = OrderRefreshHandler.create(home_swipe,recyclerView,new OrderBusnissDataContverter(),mType);
        }
        vHeadder = View.inflate(getContext(),R.layout.order_follow_head,null);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initRefreshLayout();
        initRecyclerView();
        mRefreshHandler.firstPage(URL,getContext());
        if(mType.equals("follow")){
            init_follow_toolbar();
        }
    }

    private void initRefreshLayout(){//设置刷新的颜色和款式
        home_swipe.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_orange_dark,
                android.R.color.holo_red_dark
        );
        home_swipe.setProgressViewOffset(true,120,300);
    }

    private void initRecyclerView(){
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
       // recyclerView.addItemDecoration(BaseDecoration.create(ContextCompat.getColor(getContext(),R.color.app_backgraound),5));//间距
        //按照fragment跳转 recyclerview的带年纪事件

        System.out.println("**********************************"+getSupportDelegate());
        if(mType.equals("qian")){
            recyclerView.addOnItemTouchListener(OrderItemClickListener.create(getSupportDelegate()));
        }
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.order_back_linear) {
            getSupportDelegate().pop();
        } else if (i == R.id.order_right) {
            mtype = 0;
            if(baidu_token != null)
            {
                start_activity();//跳转
            }
        }else if(i == R.id.order_right_follow)
        {
            getSupportDelegate().startForResult(new Follow_Add_Frgamnet(),103);
        }
    }

    private void init_follow_toolbar()
    {
        toolbar.setBackgroundColor(Color.parseColor("#37BBFB"));
        back.setTextColor(Color.parseColor("#ffffff"));
        order_title.setText("随访任务");
        order_title.setTextColor(Color.parseColor("#ffffff"));
        order_right.setVisibility(View.INVISIBLE);
        order_right_follow.setVisibility(View.VISIBLE);
        //toolbar_follow_linear.setVisibility(View.VISIBLE);
    }

    private void initAccessTokenWithAkSk() {
        OCR.getInstance().initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                baidu_token = result.getAccessToken();
                hasGotToken = true;
                System.out.println("licence方式获取token成功");
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                System.out.println("licence方式获取token失败");
            }
        }, getApplicationContext(), "PmCalZudTfGmglpgbrc3ehiN", "A6zL14wM8GRoUbSFTc4YGj8YcD5y7a3d");

    }

    private void start_activity(){
        Intent intent = new Intent(_mActivity, CameraActivity.class);
        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                FileUtil.getSaveFile(getContext()).getAbsolutePath());
        intent.putExtra(CameraActivity.KEY_NATIVE_TOKEN,
                OCR.getInstance().getLicense());
        intent.putExtra(CameraActivity.KEY_NATIVE_ENABLE,
                true);
        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if(resultCode == UPDATE_PATIENT)
        {
            System.out.println("*****刷新了***********");
            if(recyclerView.getVisibility()==View.GONE)
            {
                recyclerView.setVisibility(View.VISIBLE);
            }
            if(no_order_linear.getVisibility()==View.VISIBLE)
            {
                no_order_linear.setVisibility(View.GONE);
            }
            //mRefreshHandler.refresh_add(getContext(),fristBean.getRegion_id(),fristBean.getGov_id(),userBean.getId());
        }

    }

    //获取扫描后的信息
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_IMAGE_FRONT && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            String filePath = getRealPathFromURI(uri);
            recIDCard(IDCardParams.ID_CARD_SIDE_FRONT, filePath);
        }
        if (requestCode == REQUEST_CODE_PICK_IMAGE_BACK && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            String filePath = getRealPathFromURI(uri);
            recIDCard(IDCardParams.ID_CARD_SIDE_BACK, filePath);
        }
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE);
                String filePath = FileUtil.getSaveFile(getContext()).getAbsolutePath();
                if (!TextUtils.isEmpty(contentType)) {
                    if (CameraActivity.CONTENT_TYPE_ID_CARD_FRONT.equals(contentType)) {
                        recIDCard(IDCardParams.ID_CARD_SIDE_FRONT, filePath);
                    } else if (CameraActivity.CONTENT_TYPE_ID_CARD_BACK.equals(contentType)) {
                        recIDCard(IDCardParams.ID_CARD_SIDE_BACK, filePath);
                    }
                }
            }
        }
        if (resultCode ==123123) {
            System.out.println("*****出去*****");
        }else if(resultCode == UPDATE_PATIENT)
        {
            System.out.println("*****刷新了***********");
            recyclerView.setVisibility(View.VISIBLE);
            no_order_linear.setVisibility(View.GONE);
            mRefreshHandler.refresh_add(getContext(),fristBean.getRegion_id(),fristBean.getGov_id(),userBean.getId());
        }else if(resultCode ==111)
        {
            System.out.println("*****没有身份证**************");
            Intent intent = new Intent(_mActivity, Baidu_NoShenfen_Activity.class);
            startActivityForResult(intent, REQUEST_CODE_CAMERA);
        }
    }
    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContext().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    private void recIDCard(final String idCardSide, final String filePath) {//扫描返回的结果  //跳转
        BaiDuFragment fragment = new BaiDuFragment();
        String[] a =new String[2];
        a[0] = idCardSide;
        a[1] = filePath;
        mArgs.putStringArray("data",a);
        fragment.setArguments(mArgs);
        getSupportDelegate().startForResult(fragment,103);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        // 释放内存资源
        if(mType.equals("qian")){
           // OCR.getInstance().release();
        }
    }

}
