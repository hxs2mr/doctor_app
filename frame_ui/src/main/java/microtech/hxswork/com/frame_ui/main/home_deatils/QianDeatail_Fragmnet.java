package microtech.hxswork.com.frame_ui.main.home_deatils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import microtech.hxswork.com.frame_core.middle.MiddleFragment;
import microtech.hxswork.com.frame_core.net.RestClent;
import microtech.hxswork.com.frame_core.net.callback.IFailure;
import microtech.hxswork.com.frame_core.net.callback.ISuccess;
import microtech.hxswork.com.frame_core.ui.OrderQianFields;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleItemEntity;
import microtech.hxswork.com.frame_core.util.SAToast;
import microtech.hxswork.com.frame_ui.R;
import microtech.hxswork.com.frame_ui.main.order.OrderBinDetailDataConverter;
import microtech.hxswork.com.frame_ui.photoAdapter.PhotoAdapter;
import microtech.hxswork.com.frame_ui.photoAdapter.RecyclerItemClickListener;
import microtech.hxswork.com.photopicker.PhotoPreview;

import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.mtype;

/**
 * Created by microtech on 2017/12/1.
 */

public class QianDeatail_Fragmnet extends MiddleFragment implements View.OnClickListener {

    AppCompatImageView qian_back=null;
    AppCompatImageView qian_edit= null;

    AppCompatTextView qian_name=null;
    AppCompatTextView qian_sex=null;
    AppCompatTextView qian_age=null;
    AppCompatTextView qian_zhu=null;
    AppCompatTextView qian_bin=null;
    AppCompatTextView qian_phone=null;
    AppCompatTextView qian_number=null;
    AppCompatTextView qian_address=null;
    AppCompatTextView qian_ren=null;
    AppCompatTextView qian_time=null;
    LinearLayoutCompat qian_back_linear = null;
    LinearLayoutCompat qian_edit_linear = null;

    RecyclerView qian_recycleview = null;

    public  static  ArrayList<String> selectedPhotos;//=new ArrayList<>();
    public PhotoAdapter photoAdapter;
    public List<String> photos= null;
    public static QianDeatail_Fragmnet qiand_fragment  ;
    public static ArrayList<String> person_edit;//签约的信息资料 保存下来 传递给下一个fragment
    public static QianDeatail_Fragmnet create(){
        return new QianDeatail_Fragmnet();
    }
    Bundle mEdit;//用于传值到下一个fragment
   public static   String[] data;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arg = getArguments();
        data = arg.getStringArray("data");

        person_edit=new ArrayList<>();
        mEdit = new Bundle();
        qiand_fragment = new QianDeatail_Fragmnet();
        selectedPhotos=new ArrayList<>();
    }

    @Override
    public Object setLayout() {
        return R.layout.qian_item_detail;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        initbtid();
    }

    private void initbtid() {
        qian_back_linear = bind(R.id.qian_back_linear);
        qian_back = bind(R.id.qian_back);
        qian_edit = bind(R.id.qian_edit);
        qian_name = bind(R.id.qian_d_name);
        qian_sex = bind(R.id.qian_d_sex);
        qian_age = bind(R.id.qian_d_age);
        qian_zhu = bind(R.id.qian_d_zhu);
        qian_bin = bind(R.id.qian_d_bin);
        qian_phone = bind(R.id.qian_d_phone);
        qian_number = bind(R.id.qian_d_number);
        qian_address = bind(R.id.qian_d_address);
        qian_ren = bind(R.id.qian_d_ren);
        qian_time = bind(R.id.qian_d_time);
        qian_edit_linear = bind(R.id.qian_edit_linear);
        qian_recycleview = bind(R.id.qian_d_recycleview);
        qian_back_linear.setOnClickListener(this);
        qian_edit_linear.setOnClickListener(this);
       // new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        Map<String, String> headers = new HashMap<>();
        //System.out.println("region_id******"+data[0]);
        RestClent.builder()
                .url("signsInfo")
                .loader(getContext())
                .params("region_id",data[0])
                .params("gov_id",data[1])
                .params("doctor_id",data[2])
                .params("signs_id",data[3])
                .headrs(headers)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {

                        System.out.println("***************签约详情***************"+response);
                        List<MultipleItemEntity> entity = new OrderBinDetailDataConverter().setJsonData(response).CONVERT();
                        int code = entity.get(0).getField(OrderQianFields.CODE);
                        if(code == 200) {
                            show_detail(entity);
                        }
                    }
                })
                .failure(new IFailure() {
                    @Override
                    public void onIFailure() {

                    }
                })
                .build()
                .post();
    }

    @SuppressLint("SetTextI18n")
    private void show_detail(List<MultipleItemEntity> entity) {
        MultipleItemEntity e = entity.get(0);
        qian_name.setText(e.getField(OrderQianFields.NAME).toString());
        String i = e.getField(OrderQianFields.SEX);
        if(i.equals("1"))
        {
            qian_sex.setText("男");
        }else {
            qian_sex.setText("女");
        }
        qian_age.setText(e.getField(OrderQianFields.AGE).toString());
        qian_zhu.setText(e.getField(OrderQianFields.ZHU).toString());
        qian_bin.setText(e.getField(OrderQianFields.BIN).toString());
        qian_phone.setText(e.getField(OrderQianFields.PHONE).toString());
        qian_number.setText(e.getField(OrderQianFields.ID_CARD).toString());
        qian_address.setText(e.getField(OrderQianFields.ADDRESS).toString());
        qian_time.setText(e.getField(OrderQianFields.TIME_STOP).toString());
        qian_ren.setText("签约人:"+e.getField(OrderQianFields.PERSON_QIAN).toString());
        selectedPhotos = e.getField(OrderQianFields.IMAGE_ARRAY);

        init_personEdit(e);

       // System.out.println("**********selectPhont"+selectedPhotos.get(1));
        photoAdapter = new PhotoAdapter(getContext(),selectedPhotos,0);
        qian_recycleview.setLayoutManager(new StaggeredGridLayoutManager(4, OrientationHelper.VERTICAL));//设置recycleview的布局

        qian_recycleview.setAdapter(photoAdapter);
        qian_recycleview.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(selectedPhotos.size()>0)
                {
                    //表示图片的查看
                    PhotoPreview.builder()
                            .setPhotos(selectedPhotos)
                            .setCurrentItem(position)
                            .setFlage(0)//0表示网络tu'p
                            .start(getActivity());
                }else {
                    SAToast.makeText(getContext(),"没有签约资料").show();
                }

            }
        }));

    }
    private void init_personEdit( MultipleItemEntity e) {
        person_edit.add(e.getField(OrderQianFields.THUMB).toString());
        person_edit.add(e.getField(OrderQianFields.NAME).toString());
        person_edit.add(e.getField(OrderQianFields.ZHU).toString());
        person_edit.add(e.getField(OrderQianFields.SEX).toString());
        person_edit.add(e.getField(OrderQianFields.AGE).toString());
        person_edit.add(e.getField(OrderQianFields.BIN).toString());
        person_edit.add(e.getField(OrderQianFields.PHONE).toString());
        person_edit.add(e.getField(OrderQianFields.ID_CARD).toString());
        person_edit.add(e.getField(OrderQianFields.ADDRESS).toString());
        person_edit.add(e.getField(OrderQianFields.REGON_ID).toString());
        person_edit.add(e.getField(OrderQianFields.GOV_ID).toString());
        person_edit.add(e.getField(OrderQianFields.DOCTOR_ID).toString());
        person_edit.add(e.getField(OrderQianFields._ID).toString());
        person_edit.add(e.getField(OrderQianFields.THUMB).toString());
        person_edit.add(e.getField(OrderQianFields.BIRTHDAY).toString());
        person_edit.add(e.getField(OrderQianFields.USER_ID).toString());
        person_edit.add(e.getField(OrderQianFields.TIME_STOP).toString());
        person_edit.add(e.getField(OrderQianFields.TIME).toString());
        person_edit.add(e.getField(OrderQianFields.NUMBER).toString());
        person_edit.add(e.getField(OrderQianFields.STATUE).toString());
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.qian_back_linear) {
            getSupportDelegate().pop();
        }else if(i == R.id.qian_edit_linear)
        {
            mtype =0;
            Intent intent = new Intent(getActivity(),QianDeatail_Edit_Activity.class);
            mEdit.putStringArrayList("edit",person_edit);
            intent.putExtras(mEdit);
            startActivityForResult(intent, 103);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode ==103) {//添加成功

            getSupportDelegate().pop();
        }
    }
}
