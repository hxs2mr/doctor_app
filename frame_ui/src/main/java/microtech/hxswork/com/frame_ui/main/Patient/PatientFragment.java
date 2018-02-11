package microtech.hxswork.com.frame_ui.main.Patient;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
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

import java.util.ArrayList;
import java.util.List;

import microtech.hxswork.com.frame_core.init.Frame;
import microtech.hxswork.com.frame_core.middle.PermissionCheckerFragment;
import microtech.hxswork.com.frame_core.middle.bottom.BottomItemFragment;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleRecyclerAdapter;
import microtech.hxswork.com.frame_core.ui.refresh.RefreshHandler;
import microtech.hxswork.com.frame_core.util.UtilsStyle;
import microtech.hxswork.com.frame_ui.R;
import microtech.hxswork.com.frame_ui.main.ExBottomFragment;
import microtech.hxswork.com.frame_ui.main.Search.Search_Activity;
import microtech.hxswork.com.frame_ui.main.baidu_zx.BaiDuFragment;
import microtech.hxswork.com.frame_ui.main.baidu_zx.Baidu_NoShenfen_Activity;
import microtech.hxswork.com.frame_ui.main.baidu_zx.FileUtil;
import microtech.hxswork.com.frame_ui.main.szxing.ACameraFragment;
import microtech.hxswork.com.frame_ui.main.dbdatabase.Patient_sql;

import static microtech.hxswork.com.frame_core.init.Frame.getApplicationContext;
import static microtech.hxswork.com.frame_ui.login.LoginHandler.fristBean;
import static microtech.hxswork.com.frame_ui.login.LoginHandler.userBean;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.mSearchtype;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.mtype;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.search_xuanzhe;

/**
 * Created by microtech on 2017/11/17.主页
 */

public class PatientFragment extends BottomItemFragment implements View.OnClickListener, View.OnFocusChangeListener {
    private static final int REQUEST_CODE_PICK_IMAGE_FRONT = 201;
    private static final int REQUEST_CODE_PICK_IMAGE_BACK = 202;
    private static final int REQUEST_CODE_CAMERA = 102;
    private static final int UPDATE_PATIENT = 103;
    SwipeRefreshLayout mRefreshLayout = null;

    RecyclerView mRecyclerView=null;

    Toolbar mToolbar= null;

    IconTextView home_icon_shao=null;//扫描

    IconTextView home_icon_message=null;//t通知

    String baidu_token=null;//获取百度的身份证识别token
    public static RefreshHandler mRefreshHandler = null;
    AppCompatEditText patient_search_edit = null;
    MultipleRecyclerAdapter adapter;
    AppCompatTextView patient_order_right = null;
    AppCompatImageView shi_image = null;
    Patient_sql patient = null;
    private void StartOrderType()
    {
        BaiDuFragment fragment = new BaiDuFragment();
        //fragment.setArguments(mArgs);
        getParentFragmen().getSupportDelegate().start(fragment);//父布局跳转 去除底部的导航
    }
    int qiehuan_flage = 0 ;
    Bundle mArgs = null;
    public static String head_patinent_image="";
    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        mArgs = new Bundle();
        UtilsStyle.setStatusBarMode((Activity) getContext(),true);//设置状态栏的图标为黑色
        initAccessTokenWithAkSk();
       // patient = new Patient_sql(getContext(),"patient_person.db",null,1);//创建数据库
/*        sq = patient.getWritableDatabase();
        sq = patient.getReadableDatabase();//可读可写*/
         mRefreshLayout = bind(R.id.home_swipe);
        patient_order_right = bind(R.id.patient_order_right);
         mRecyclerView= bind(R.id.home_rv);
         shi_image = bind(R.id.shi_image);
         mToolbar=bind(R.id.home_tb);
        patient_search_edit = bind(R.id.patient_search_edit);
        patient_order_right.setOnClickListener(this);
        shi_image.setOnClickListener(this);
        patient_search_edit.setOnFocusChangeListener(this);
        mRefreshHandler = RefreshHandler.create(mRefreshLayout,mRecyclerView,new PatientDataConVerter());
    }

    private void initRefreshLayout(){//设置刷新的颜色和款式
        mRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_orange_dark,
                android.R.color.holo_red_dark);
        mRefreshLayout.setProgressViewOffset(true,50,300);
    }
    private void initRecyclerView(){
        final GridLayoutManager manager = new GridLayoutManager(getContext(),2);
        mRecyclerView.setLayoutManager(manager);
        //按照fragment跳转
       final ExBottomFragment exBottomFragment =  getParentFragmen();
        mRecyclerView.addOnItemTouchListener(PatientItemClickListener.create(exBottomFragment));
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initRefreshLayout();
        initRecyclerView();
        mRefreshHandler.firstPage("patients",getContext(),fristBean.getRegion_id(),fristBean.getGov_id(),userBean.getId());
    }
    @Override
    public Object setLayout() {
        return R.layout.home_fragment;
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
      /*  if(i == R.id.shi_image)
        {    adapter = mRefreshHandler.return_adapter();
            if(adapter!=null) {
                if(qiehuan_flage == 0)
                {
                //网格
                mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
                //需要显示加载更多则加上下面这句   从新关联recycler
                adapter.onAttachedToRecyclerView(mRecyclerView);
                    shi_image.setBackgroundResource(R.mipmap.shi_double);
                qiehuan_flage = 1;
                } else {
                    //网格
                    mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                    //需要显示加载更多则加上下面这句   从新关联recycler
                    adapter.onAttachedToRecyclerView(mRecyclerView);
                    shi_image.setBackgroundResource(R.mipmap.shi_dan);
                    qiehuan_flage = 0;
                }
            }
        }else*/ if(i == R.id.patient_order_right)
        {
            if(baidu_token != null)
            {
                mtype= 1;
                start_activity();//跳转
            }
        }
    }

    public void start(PermissionCheckerFragment fragment) {
        //F;
        ACameraFragment aCameraFragment = new ACameraFragment();
        fragment.getSupportDelegate().start(aCameraFragment);
    }

    @Override
    public void onFocusChange(View view, boolean b) {//判断获取焦点
        int i = view.getId();
        if (i == R.id.patient_search_edit) {
            if(b) {
                mSearchtype = 1;
                search_xuanzhe = 1;
                patient_search_edit.setFocusable(false);
                patient_search_edit.setFocusableInTouchMode(true);
                Intent intent = new Intent(_mActivity, Search_Activity.class);
            /*    Bundle bundle = new Bundle();
                bundle.putString("flage", "patient");
                intent.putExtras(bundle);*/
                startActivityForResult(intent, 0x12);
            }
        }
    }

    private void initAccessTokenWithAkSk() {//获取百度身份证识别的Token
        OCR.getInstance().initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                baidu_token = result.getAccessToken();
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


    /* @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {//坑比
        super.onFragmentResult(requestCode, resultCode, data);
            System.out.println("*****刷新了***********");
    }*/

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
        if (resultCode ==123123) {//添加成功
            System.out.println("*****出去*****");
        }else if(resultCode == UPDATE_PATIENT)
        {
            System.out.println("*****刷新了***********");
            mRefreshHandler.refresh_add(getContext(),fristBean.getRegion_id(),fristBean.getGov_id(),userBean.getId());
        }
        else if(resultCode ==111)
        {
            System.out.println("*****没有身份证**************");
            Intent intent = new Intent(_mActivity, Baidu_NoShenfen_Activity.class);
            startActivityForResult(intent, REQUEST_CODE_CAMERA);
        }else if(resultCode == 999)//搜寻返回的结果
        {
            Bundle b=data.getExtras(); //data为B中回传的Intent
            String[] a = new String[6];
            a=b.getStringArray("Form_search");//str即为回传的值
            System.out.println("*************============"+a[0]);
            PatientNextFragment fragment = PatientNextFragment.create();
            mArgs.putStringArray("user",a);//这里传入用户的user_id
            fragment.setArguments(mArgs);
            getParentFragmen().getSupportDelegate().start(fragment);
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
        getParentFragmen().getSupportDelegate().startForResult(fragment,103);

    }
}
