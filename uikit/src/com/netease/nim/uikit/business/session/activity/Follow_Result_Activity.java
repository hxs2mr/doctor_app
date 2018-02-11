package com.netease.nim.uikit.business.session.activity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.view.WindowManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netease.nim.uikit.R;

import java.util.ArrayList;

import me.yokeyword.fragmentation.SwipeBackLayout;
import me.yokeyword.fragmentation_swipeback.SwipeBackActivity;
import microtech.hxswork.com.frame_core.dialogpicker.city_20170724.PersonPickerView;
import microtech.hxswork.com.frame_core.net.RestClent;
import microtech.hxswork.com.frame_core.net.callback.ISuccess;
import microtech.hxswork.com.frame_core.util.KeyBordUtil;
import microtech.hxswork.com.frame_core.util.SAToast;

import static microtech.hxswork.com.frame_core.net.RestClent.TIGE_DOCTOR_ID;
import static microtech.hxswork.com.frame_core.net.RestClent.TIGE_GOV_ID;
import static microtech.hxswork.com.frame_core.net.RestClent.TIGE_REGION_ID;
import static microtech.hxswork.com.frame_core.net.RestClent.TIGE_USERID;

/**
 * Created by microtech on 2017/12/14.
 */

public class Follow_Result_Activity extends SwipeBackActivity implements View.OnClickListener {
    private LinearLayoutCompat result_back_linear = null;//返回
    private AppCompatTextView result_tijiao = null;//提交
    private AppCompatEditText result_miaoshu = null;//描述
    private AppCompatTextView result_xiaoguo = null;//结果
    private AppCompatEditText result_zhidao = null;//指导建议
    private  LinearLayoutCompat follow_add_linear4 = null;
    private AppCompatTextView follow_add_flage = null;
    public static ArrayList<String> mDistrictBeanArrayList1 ;//性别的选取
    private SharedPreferences patient_pre ;//
    private SharedPreferences.Editor patient_edit;//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.follow_result_activity);

        getSwipeBackLayout().setEdgeOrientation(SwipeBackLayout.EDGE_LEFT); // EDGE_LEFT(默认),EDGE_ALL
        getSwipeBackLayout().setParallaxOffset(0.0f - 1.0f); // （类iOS）滑动退出视觉差，默认0.3
        setSwipeBackEnable(true); // 是否允许滑动

        patient_pre= getSharedPreferences("patient_refresh", MODE_PRIVATE);
        patient_edit = patient_pre.edit();
        initview();
        mDistrictBeanArrayList1 = new ArrayList<>();
    }

    private boolean checkForm(){
        final  String bin = result_miaoshu.getText().toString();
        final  String phone = result_xiaoguo.getText().toString();
        final  String address = result_zhidao.getText().toString();
        final  String follow = follow_add_flage.getText().toString();
        //检查输入的文本是否正确
        boolean isPass = true;
        if(bin.isEmpty()){
            result_miaoshu.setError("请添加症状描述");
            isPass = false;
        }else {
            result_miaoshu.setError(null);
        }
        if(phone.isEmpty()){
            result_xiaoguo.setError("请选择康复效果");
            isPass = false;
        }else {
            result_xiaoguo.setError(null);
        }
        if(address.isEmpty()){
            result_zhidao.setError("请填写指导建议");
            isPass = false;
        }else {
            result_zhidao.setError(null);
        }
        if(follow.isEmpty()){
            follow_add_flage.setError("请选择随访方式");
            isPass = false;
        }else {
            follow_add_flage.setError(null);
        }
        return isPass;
    }
    private void initview() {
        result_back_linear = findViewById(R.id.result_back_linear);
        result_tijiao = findViewById(R.id.result_tijiao);
        result_miaoshu = findViewById(R.id.result_miaoshu);
        result_xiaoguo = findViewById(R.id.result_xiaoguo);
        result_zhidao = findViewById(R.id.result_zhidao);
        follow_add_flage = findViewById(R.id.follow_add_flage);
        follow_add_linear4 = findViewById(R.id.follow_add_linear4);
        result_back_linear.setOnClickListener(this);
        result_tijiao.setOnClickListener(this);
        result_xiaoguo.setOnClickListener(this);
        follow_add_linear4.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.result_back_linear) {
            finish();
        }else if(i == R.id.result_tijiao)//提交
        {//
            if(checkForm())
            {
                //[0=正在治疗/1=效果一般/2=有效控制/3=病情恶化/4=已死亡]
                int xiaoguo_type =0 ;
                int phone_type =0  ;
                if(result_xiaoguo.getText().toString().equals("正在治疗")){
                    xiaoguo_type = 0;
                }
                else if(result_xiaoguo.getText().toString().equals("效果一般"))
                {
                    xiaoguo_type = 1 ;
                }else  if(result_xiaoguo.getText().toString().equals("有效控制")){
                    xiaoguo_type = 2 ;
                }else  if(result_xiaoguo.getText().toString().equals("病情恶化")){
                    xiaoguo_type = 3 ;
                }else  if(result_xiaoguo.getText().toString().equals("患者死亡")){
                    xiaoguo_type = 4 ;
                }

                if(follow_add_flage.getText().toString().equals("门诊随访"))
                {
                    phone_type = 0 ;
                }else  if(follow_add_flage.getText().toString().equals("上门随访")){
                    phone_type = 1 ;
                }else  if(follow_add_flage.getText().toString().equals("电话随访")){
                    phone_type = 2 ;
                }
                RestClent.builder()
                        .url("visits_results")
                        .loader(this)
                        .params("region_id",TIGE_REGION_ID)
                        .params("gov_id",TIGE_GOV_ID)
                        .params("user_id",TIGE_USERID)
                        .params("doctor_id",TIGE_DOCTOR_ID)
                        .params("desc",result_miaoshu.getText().toString())
                        .params("suggest",result_zhidao.getText().toString())
                        .params("effect",xiaoguo_type)
                        .params("visitsType",phone_type)
                        .success(new ISuccess() {
                            @Override
                            public void onSuccess(String response) {

                                final int  code = JSON.parseObject(response).getInteger("code");
                                System.out.println("这是随访结果的录入*************"+response);
                                if(code == 200)
                                {
                                    patient_edit.putString("patient","1");
                                    patient_edit.putString("patient_result", result_xiaoguo.getText().toString());
                                    patient_edit.commit();
                                    SAToast.makeText(Follow_Result_Activity.this,"录入成功").show();
                                    finish();
                                }
                            }
                        })
                        .build()
                        .post();
            }else {
                SAToast.makeText(this,"请填写对应的选项").show();
            }

        }else if(i == R.id.result_xiaoguo){//效果

            new KeyBordUtil().hideSoftKeyboard(result_xiaoguo);
            mDistrictBeanArrayList1.clear();
            mDistrictBeanArrayList1.add("效果一般");
            mDistrictBeanArrayList1.add("有效控制");
            mDistrictBeanArrayList1.add("病情恶化");
            mDistrictBeanArrayList1.add("患者已死亡");
            PersonPickerView SexPicker = new PersonPickerView.Builder(this,this).textSize(20)
                    .titleTextColor("#000000")
                    .backgroundPop(0xffffff)
                    .province("")
                    .city("有效控制")
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
                    result_xiaoguo.setText(district);
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha=1f;
                    getWindow().setAttributes(lp);

                }
                @Override
                public void onCancel() {
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha=1f;
                    getWindow().setAttributes(lp);
                }
            });
        }else if(i == R.id.follow_add_linear4)
        {
            new KeyBordUtil().hideSoftKeyboard(follow_add_linear4);
            follow_fanshi();
        }
    }


    private void follow_fanshi()
    {


        mDistrictBeanArrayList1.clear();
        mDistrictBeanArrayList1.add("门诊随访");
        mDistrictBeanArrayList1.add("上门随访");
        mDistrictBeanArrayList1.add("电话随访");
        PersonPickerView SexPicker = new PersonPickerView.Builder(this,this).textSize(20)
                .titleTextColor("#000000")
                .backgroundPop(0xffffff)
                .province("")
                .city("上门随访")
                .district("26")
                .textColor(Color.parseColor("#525F66"))
                .provinceCyclic(true)
                .cityCyclic(false)
                .list(mDistrictBeanArrayList1)//添加用户选择
                .districtCyclic(false)
                .visibleItemsCount(5)
                .itemPadding(20)
                .build();
        SexPicker.show();
        SexPicker.setOnCityItemClickListener(new PersonPickerView.OnCityItemClickListener() {
            @Override
            public void onSelected(String province, String city, String district) {
                //返回结果
                follow_add_flage.setText(district);
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha=1f;
                getWindow().setAttributes(lp);

            }
            @Override
            public void onCancel() {
                WindowManager.LayoutParams lp =  getWindow().getAttributes();
                lp.alpha=1f;
                getWindow().setAttributes(lp);
            }
        });

    }

}
