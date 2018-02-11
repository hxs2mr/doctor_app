package microtech.hxswork.com.frame_ui.main.home_deatils;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import microtech.hxswork.com.frame_core.dialogpicker.city_20170724.DayTimerPickerView;
import microtech.hxswork.com.frame_core.dialogpicker.city_20170724.PersonPickerView;
import microtech.hxswork.com.frame_core.middle.MiddleFragment;
import microtech.hxswork.com.frame_core.net.RestClent;
import microtech.hxswork.com.frame_core.net.callback.ISuccess;
import microtech.hxswork.com.frame_core.swipe_back.BaseSwipeBackFragment;
import microtech.hxswork.com.frame_core.ui.OrderQianFields;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleItemEntity;
import microtech.hxswork.com.frame_core.util.SAToast;
import microtech.hxswork.com.frame_ui.R;
import microtech.hxswork.com.frame_ui.main.Search.Search_Activity;
import microtech.hxswork.com.frame_ui.main.order.OrderListItemType;

import static microtech.hxswork.com.frame_core.util.TimeUtils.date2TimeStamp1;
import static microtech.hxswork.com.frame_core.util.TimeUtils.timestampFromDateStr;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.follow_user_id;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.home_follow_number_total;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.mSearchtype;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.search_xuanzhe;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.suifan_number;
import static microtech.hxswork.com.frame_ui.main.home_deatils.QianDeatail_Fragmnet.data;
import static microtech.hxswork.com.frame_ui.main.order.OrderRefreshHandler.mAdapter;

/**
 * Created by microtech on 2017/12/5.
 */

public class Follow_Add_Frgamnet extends MiddleFragment implements View.OnClickListener {

    LinearLayoutCompat follow_add_back_linear = null;

    LinearLayoutCompat follow_add_linear1 = null;
    LinearLayoutCompat follow_add_linear2 = null;
    LinearLayoutCompat follow_add_linear3 = null;
    LinearLayoutCompat follow_add_linear4 = null;
    LinearLayoutCompat follow_add_linear5 = null;

    AppCompatTextView follow_add_ok = null;
    AppCompatTextView follow_add_name = null;
    AppCompatTextView follow_add_date = null;
    AppCompatTextView follow_add_time = null;
    AppCompatTextView follow_add_flage = null;
    AppCompatTextView follow_add_content = null;
    public static ArrayList<String> mDistrictBeanArrayList1;//性别的选取
    private PopupWindow mMenuView;
    private int mYear=0,mMoth=0,mDd=0;
    String[]  str;
    public static String  follow_phone="";
    @Override
    public Object setLayout() {
        return R.layout.follow_add_fragment;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        //Toast.makeText(getContext(), "重新建造", Toast.LENGTH_SHORT).show();
        initView();
        mSearchtype = 0;
    }


    private boolean checkForm(){
        final  String name = follow_add_name.getText().toString();
        final  String date = follow_add_date.getText().toString();
        final  String time = follow_add_time.getText().toString();
        final  String suifan = follow_add_flage.getText().toString();

        //检查输入的文本是否正确
        boolean isPass = true;
        if(name.isEmpty()){
            follow_add_name.setError("请选择患者");
            isPass = false;
        }else {
            follow_add_name.setError(null);
        }
        if(date.isEmpty()){
            follow_add_date.setError("请选择随访日期");
            isPass = false;
        }else {
            follow_add_date.setError(null);
        }
        if(time.isEmpty()){
            follow_add_time.setError("请选择随访时间");
            isPass = false;
        }else {
            follow_add_time.setError(null);
        }
        if(suifan.isEmpty()){
            follow_add_flage.setError("请选择随访方式");
            isPass = false;
        }else {
            follow_add_flage.setError(null);
        }
        return isPass;
    }
    private void initView() {
        follow_add_back_linear = bind(R.id.follow_add_back_linear);
        follow_add_linear1 = bind(R.id.follow_add_linear1);
        follow_add_linear2 = bind(R.id.follow_add_linear2);
        follow_add_linear3 = bind(R.id.follow_add_linear3);
        follow_add_linear4 = bind(R.id.follow_add_linear4);
        follow_add_linear5 = bind(R.id.follow_add_linear5);

        follow_add_ok = bind(R.id.follow_add_ok);
        follow_add_name = bind(R.id.follow_add_name);
        follow_add_date = bind(R.id.follow_add_date);
        follow_add_time = bind(R.id.follow_add_time);
        follow_add_flage = bind(R.id.follow_add_flage);
        follow_add_content = bind(R.id.follow_add_content);

        follow_add_back_linear.setOnClickListener(this);

        follow_add_linear1.setOnClickListener(this);//选择患者
        follow_add_linear2.setOnClickListener(this);
        follow_add_linear3.setOnClickListener(this);
        follow_add_linear4.setOnClickListener(this);
        follow_add_linear5.setOnClickListener(this);
        follow_add_ok.setOnClickListener(this);//保存按钮
        mDistrictBeanArrayList1 = new ArrayList<>();
        mDistrictBeanArrayList1.add("门诊随访");
        mDistrictBeanArrayList1.add("上门随访");
        mDistrictBeanArrayList1.add("电话随访");
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.follow_add_back_linear) {//返回
            getSupportDelegate().pop();

        } else if (i == R.id.follow_add_linear1) {
            Intent intent = new Intent(getActivity(), Search_Activity.class);
            search_xuanzhe = 0;
            startActivityForResult(intent, 0x11);
        } else if (i == R.id.follow_add_linear2) {//日期
            date();
        } else if (i == R.id.follow_add_linear3) {//随访时间
            follow_time();
        } else if (i == R.id.follow_add_linear4) {//随访方式
            follow_fanshi();
        } else if (i == R.id.follow_add_linear5) {//服务内容
            ManyPopuWindow();
        } else if (i == R.id.follow_add_ok) {//保存
            //发起网络请求
            if(checkForm())
            {
                put_net();
            }
        }
    }

    private void put_net() {
        String suifan = follow_add_flage.getText().toString();
        String suifan_flage="0";
        if(suifan.equals("上门随访"))
        {
            suifan_flage="0";
        }else if(suifan.equals("电话随访")){
            suifan_flage="1";
        }else {
            suifan_flage="2";
        }
        //.System.out.println("**************时间为："+follow_add_date.getText().toString()+" "+follow_add_time.getText().toString());
        RestClent.builder()
                .url("visits_add")
                .loader(getContext())
                .params("region_id",str[2])
                .params("gov_id",str[3])
                .params("doctor_id",str[4])
                .params("user_id",str[1])
                .params("visits_data",follow_add_date.getText().toString()+" "+follow_add_time.getText().toString())
                .params("visits_type",suifan_flage)
                .params("visits_text",follow_add_content.getText().toString())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                            int code = JSON.parseObject(response).getInteger("code");
                            if(code == 200)
                            {
                                SAToast.makeText(getContext(),"添加随访成功").show();
                                Bundle data = new Bundle();
                                data.putString("UPDATE_PATIENT","-1");
                                setFragmentResult(103, data);
                                getSupportDelegate().pop();
                                follow_user_id = str[1];
                                home_follow_number_total++;
                                suifan_number.setText(home_follow_number_total+"");
                                init_orderAdpater();
                            }else if(code == 209)
                            {
                                SAToast.makeText(getContext(),"该随访任务也存在").show();
                            }
                            System.out.println("*****添加新随访返回的数据*****"+response);


                    }
                })
                .build()
                .post();

    }

    private void init_orderAdpater(){

        int sui_flage=0;
        if(follow_add_flage.getText().toString().equals("上门随访"))
        {
            sui_flage =0;
        }else    if(follow_add_flage.getText().toString().equals("电话随访")){
            sui_flage =1;
        }else {
            sui_flage =2;
        }

        String time="";
        time = follow_add_date.getText().toString()+" "+follow_add_time.getText().toString();

         SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        int shen_time =0;

        //yyyy-MM-dd HH
         shen_time = (int) dateDiff(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()),  follow_add_date.getText().toString(),"yyyy-MM-dd");
         System.out.println("剩余多少天*********L:"+shen_time);
        String end_time="";
         if(shen_time==0)
         {
             if(follow_add_time.getText().toString().equals("20:00 "))
             {
                 shen_time=20-Integer.parseInt(new SimpleDateFormat("HH", Locale.getDefault()).format(new Date()));
                 if(Integer.parseInt(new SimpleDateFormat("HH", Locale.getDefault()).format(new Date()))>0){
                     shen_time = 0;
                 }
             }else  if(follow_add_time.getText().toString().equals("10:00 ")){
                 shen_time=10-Integer.parseInt(new SimpleDateFormat("HH", Locale.getDefault()).format(new Date()));
                 if(Integer.parseInt(new SimpleDateFormat("HH", Locale.getDefault()).format(new Date()))>0){
                     shen_time = 0;
                 }
             }else {
                 shen_time=Integer.parseInt(follow_add_time.getText().toString().replace("0","").replace(":","").replace(" ",""))-Integer.parseInt(new SimpleDateFormat("HH", Locale.getDefault()).format(new Date()));
                 if(Integer.parseInt(new SimpleDateFormat("HH", Locale.getDefault()).format(new Date()))>0){
                     shen_time = 0;
                 }
             }
             end_time=shen_time+"时";
         }else {
             end_time=shen_time+"天";
         }

        //更新某个postion
        MultipleItemEntity entity = MultipleItemEntity.builder()
                .setItemType(OrderListItemType.ITEM_ORDER_FOLLOE_UP)
                .setField(OrderQianFields.NAME,follow_add_name.getText().toString())
                .setField(OrderQianFields.SUI_FLAGE,sui_flage+"")
                .setField(OrderQianFields.TIME,time)
                .setField(OrderQianFields.PHONE,follow_phone)
                .setField(OrderQianFields.SHEN_TIME,end_time)
                .setField(OrderQianFields.GOV_ID,str[3])
                .setField(OrderQianFields.REGON_ID,str[2])
                .setField(OrderQianFields.DOCTOR_ID,str[4])
                .setField(OrderQianFields._ID,str[1])
                .setField(OrderQianFields.STATUE,"0")
                .build();
        mAdapter.addData(0,entity);
    }
    private  long dateDiff(String startTime, String endTime, String format) {
        // 按照传入的格式生成一个simpledateformate对象
        SimpleDateFormat sd = new SimpleDateFormat(format);
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long nh = 1000 * 60 * 60;// 一小时的毫秒数
        long nm = 1000 * 60;// 一分钟的毫秒数
        long ns = 1000;// 一秒钟的毫秒数
        long diff;
        long day = 0;
        try {
            // 获得两个时间的毫秒时间差异
            diff = sd.parse(endTime).getTime()
                    - sd.parse(startTime).getTime();
            day = diff / nd;// 计算差多少天
            long hour = diff % nd / nh;// 计算差多少小时
            long min = diff % nd % nh / nm;// 计算差多少分钟
            long sec = diff % nd % nh % nm / ns;// 计算差多少秒
            // 输出结果
            System.out.println("时间相差：" + day + "天" + hour + "小时" + min
                    + "分钟" + sec + "秒。");
            if (day>=1) {
                return day;
            }else {
                if (day==0) {
                    return 0;
                }else {
                    return 0;
                }

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;

    }
    private void follow_time()
    {
        mDistrictBeanArrayList1.clear();
        mDistrictBeanArrayList1.add("07:00 ");
        mDistrictBeanArrayList1.add("08:00 ");
        mDistrictBeanArrayList1.add("09:00 ");
        mDistrictBeanArrayList1.add("10:00 ");
        mDistrictBeanArrayList1.add("11:00 ");
        mDistrictBeanArrayList1.add("12:00 ");
        mDistrictBeanArrayList1.add("13:00 ");
        mDistrictBeanArrayList1.add("14:00 ");
        mDistrictBeanArrayList1.add("15:00 ");
        mDistrictBeanArrayList1.add("16:00 ");
        mDistrictBeanArrayList1.add("17:00 ");
        mDistrictBeanArrayList1.add("18:00 ");
        mDistrictBeanArrayList1.add("19:00 ");
        mDistrictBeanArrayList1.add("20:00 ");
        mDistrictBeanArrayList1.add("21:00 ");
        mDistrictBeanArrayList1.add("22:00 ");
        System.out.println("当前时间********"+new SimpleDateFormat("HH", Locale.getDefault()).format(new Date()));
        PersonPickerView SexPicker = new PersonPickerView.Builder(getActivity(),getContext()).textSize(20)
                .titleTextColor("#000000")
                .backgroundPop(0xffffff)
                .province("")
                .city(new SimpleDateFormat("HH", Locale.getDefault()).format(new Date()))
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

                if(!district.equals("10:00 ")&&!district.equals("20:00 "))
                {
                    if(Integer.parseInt(district.replace("0","").replace(":","").replace(" ",""))<=Integer.parseInt(new SimpleDateFormat("HH", Locale.getDefault()).format(new Date())))
                    {
                        district=new SimpleDateFormat("HH", Locale.getDefault()).format(new Date())+":00";
                    }
                }else {
                    if(district.equals("10:00 "))
                    {
                        if(10<=Integer.parseInt(new SimpleDateFormat("HH", Locale.getDefault()).format(new Date())))
                        {
                            district=new SimpleDateFormat("HH", Locale.getDefault()).format(new Date())+":00";
                        }
                    }else  if(district.equals("20:00 ")){
                        if(20<=Integer.parseInt(new SimpleDateFormat("HH", Locale.getDefault()).format(new Date())))
                        {
                            district=new SimpleDateFormat("HH", Locale.getDefault()).format(new Date())+":00";
                        }
                    }
                }
                follow_add_time.setText(district);
                WindowManager.LayoutParams lp =  getActivity().getWindow().getAttributes();
                lp.alpha=1f;
                getActivity().getWindow().setAttributes(lp);

            }
            @Override
            public void onCancel() {
                WindowManager.LayoutParams lp =  getActivity().getWindow().getAttributes();
                lp.alpha=1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
    }
    private void follow_fanshi()
    {
        mDistrictBeanArrayList1.clear();
        mDistrictBeanArrayList1.add("门诊随访");
        mDistrictBeanArrayList1.add("上门随访");
        mDistrictBeanArrayList1.add("电话随访");
        PersonPickerView SexPicker = new PersonPickerView.Builder(getActivity(),getContext()).textSize(20)
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
                WindowManager.LayoutParams lp =  getActivity().getWindow().getAttributes();
                lp.alpha=1f;
                getActivity().getWindow().setAttributes(lp);

            }
            @Override
            public void onCancel() {
                WindowManager.LayoutParams lp =  getActivity().getWindow().getAttributes();
                lp.alpha=1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });

    }

    private void date(){
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

                if(Integer.parseInt(province)<Integer.parseInt(new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date())))
                {
                    province = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
                }
                if(Integer.parseInt(province)<=Integer.parseInt(new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date())))
                {
                if(Integer.parseInt("1"+city)<Integer.parseInt("1"+new SimpleDateFormat("MM", Locale.getDefault()).format(new Date())))
                {
                    city = new SimpleDateFormat("MM", Locale.getDefault()).format(new Date());
                }
                if(Integer.parseInt("1"+city)<=Integer.parseInt("1"+new SimpleDateFormat("MM", Locale.getDefault()).format(new Date())))
                {
                    if(Integer.parseInt("1"+district)<=Integer.parseInt("1"+new SimpleDateFormat("dd", Locale.getDefault()).format(new Date())))
                    {
                        district= new SimpleDateFormat("dd", Locale.getDefault()).format(new Date());
                    }
                }
                }
                follow_add_date.setText(province+ "-" + city+ "-" + district);
                WindowManager.LayoutParams lp =  getActivity().getWindow().getAttributes();
                lp.alpha=1f;
                getActivity().getWindow().setAttributes(lp);

            }
            @Override
            public void onCancel() {
                WindowManager.LayoutParams lp =  getActivity().getWindow().getAttributes();
                lp.alpha=1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
            case 999:
                Bundle b=data.getExtras(); //data为B中回传的Intent
                  str=b.getStringArray("Form_search");//str即为回传的值
               // Toast.makeText(getContext(),"返回的数据是："+str,Toast.LENGTH_SHORT).show();
                follow_add_name.setText(str[0]);
                for (int i =0 ; i < str.length;i++)
                {
                    System.out.println("*****数据:"+str[i]);
                }
            default:
                break;
        }
    }


    private void ManyPopuWindow() {
        final CheckBox c1,c2,c3,c4,c5,c6;
        AppCompatTextView many_cancel,many_ok;
        final View view = LayoutInflater.from(getContext()).inflate(R.layout.wufu_pop, null);
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
        c5 = view.findViewById(R.id.cb05);
        c6 = view.findViewById(R.id.cb06);

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
                WindowManager.LayoutParams lp  =getActivity().getWindow().getAttributes();
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
                String fuwu="";
                if(c1.isChecked())
                {
                    fuwu=fuwu+"测血压，";
                }
                if(c2.isChecked())
                {
                    fuwu=fuwu+"测脉率，";
                }
                if(c3.isChecked())
                {
                    fuwu=fuwu+"测体温，";
                }
                if(c4.isChecked())
                {
                    fuwu=fuwu+"测心电";
                }
                if(c5.isChecked())
                {
                    fuwu=fuwu+"测血糖";
                }
                if(c6.isChecked())
                {
                    fuwu=fuwu+"其他项";
                }
                follow_add_content.setText(fuwu);
                mMenuView.dismiss();
            }
        });
    }

}
