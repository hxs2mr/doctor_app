package microtech.hxswork.com.frame_ui.main.event;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import microtech.hxswork.com.frame_core.middle.MiddleFragment;
import microtech.hxswork.com.frame_core.net.RestClent;
import microtech.hxswork.com.frame_core.net.callback.ISuccess;
import microtech.hxswork.com.frame_ui.R;
import microtech.hxswork.com.frame_ui.main.szxing.utils.NetUtil;

import static microtech.hxswork.com.frame_ui.login.LoginHandler.fristBean;
import static microtech.hxswork.com.frame_ui.login.LoginHandler.userBean;

/**
 * Created by microtech on 2017/12/12.
 */

public class EventOrderFragment extends MiddleFragment implements View.OnClickListener {
    private LinearLayoutCompat event_order_back_linear = null;
    private AppCompatTextView  event_order_title = null;

    private LinearLayoutCompat event_order_follow= null;//随访模块
    private LinearLayoutCompat event_order_crisis= null;//危机模块
    private LinearLayoutCompat event_order_task= null;//任务模块
    private  Bundle bu  = null;
    private String[] data ;
    private String URL="";
    private AppCompatTextView crisis_title = null;
    private AppCompatTextView crisis_bin= null;
    private AppCompatTextView crisis_phone = null;
    private AppCompatTextView crisis_address= null;

    //随访
    private AppCompatTextView follow_time= null;
    private AppCompatTextView follow_name= null;
    private AppCompatTextView follow_phone= null;
    private AppCompatTextView follow_address= null;
    private AppCompatTextView follow_fuwu= null;
    private AppCompatTextView follow_ren= null;

    @Override
    public Object setLayout() {
        return R.layout.event_order_fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bu= getArguments();
        Bundle arg = getArguments();
        data = arg.getStringArray("data");
     /*   if(data[1].equals("0"))
        {

        }else*/
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        event_order_back_linear = bind(R.id.event_order_back_linear);
        event_order_title = bind(R.id.event_order_title);
        event_order_follow = bind(R.id.event_order_follow);
        event_order_crisis = bind(R.id.event_order_crisis);
        event_order_task = bind(R.id.event_order_task);

        crisis_title = bind(R.id.crisis_title);
        crisis_bin = bind(R.id.crisis_bin);
        crisis_phone = bind(R.id.crisis_phone);
        crisis_address = bind(R.id.crisis_address);

        follow_time = bind(R.id.follow_time);
        follow_name = bind(R.id.follow_name);
        follow_phone = bind(R.id.follow_phone);
        follow_address = bind(R.id.follow_address);
        follow_fuwu = bind(R.id.follow_fuwu);
        follow_ren = bind(R.id.follow_ren);

        event_order_back_linear.setOnClickListener(this);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        if(data[0].equals("1"))//随访提醒
        {
            URL="visitsInfo";
        }else if(data[0].equals("4"))//任务提醒
        {
            URL="";
        }else if(data[0].equals("3"))//危机指标
        {

        }
        init_vis();

        if(data[0].equals("1")) {
            RestClent.builder()
                    .url(URL)
                    .loader(getContext())
                    .params("region_id",fristBean.getRegion_id())
                    .params("gov_id",fristBean.getGov_id())
                    .params("doctor_id",userBean.getId())
                    .params("visits_id", data[2])
                    .success(new ISuccess() {
                        @Override
                        public void onSuccess(String response) {
                            System.out.println("随访提醒返回的结果************" + response);
                            JSONObject object = JSON.parseObject(response).getJSONObject("obj");
                            if (object.getString("times") != null)
                            {
                                follow_time.setText(object.getString("times") );
                            }
                            follow_name.setText(data[1]);
                            if (object.getString("phone") != null)
                            {
                                follow_phone.setText(object.getString("phone") );
                            }
                            if (object.getString("address") != null)
                            {
                                follow_address.setText(object.getString("address") );
                            }

                            if (object.getString("text") != null)
                            {
                                follow_fuwu.setText(object.getString("text") );
                            }
                            follow_ren.setText("随访人:"+object.getString("doctor_name") );

                        }
                    })
                    .build()
                    .post();
        }
    }

    private void init_vis() {
        if(data[0].equals("1"))
        {
            event_order_title.setText("随访提醒");
            event_order_follow.setVisibility(View.VISIBLE);
            event_order_crisis.setVisibility(View.GONE);
            event_order_task.setVisibility(View.GONE);
        }else  if(data[0].equals("4")){
            event_order_title.setText("任务提醒");
            event_order_follow.setVisibility(View.GONE);
            event_order_crisis.setVisibility(View.GONE);
            event_order_task.setVisibility(View.VISIBLE);
        }else  if(data[0].equals("3")){

            event_order_title.setText("危机提醒");
            event_order_follow.setVisibility(View.GONE);
            event_order_crisis.setVisibility(View.VISIBLE);
            event_order_task.setVisibility(View.GONE);
            String[] crisis_data = data[3].split("#");

            crisis_bin.setText(crisis_data[1]);
            crisis_address.setText(crisis_data[2]);
            crisis_phone.setText(crisis_data[3]);

            String new_data = crisis_data[0].replace("@"," ");

            SpannableStringBuilder style=new SpannableStringBuilder(new_data);
            int f1 =0;
            int f2=0;
            int flage =0;
            for(int i=0;i<crisis_data[0].length();i++) {
                if (crisis_data[0].charAt(i) == '@') {
                    if(flage == 0)
                    {
                        f1 = i;
                        flage = 1;
                    }else {
                        f2=i;
                    }

                }
            }
        if(f1!=0&&f2!=0)
           {
            style.setSpan(new ForegroundColorSpan(Color.parseColor("#FF597D")), f1,f2 + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
           }

            crisis_title.setText(style);
        }
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.event_order_back_linear) {
             getSupportDelegate().pop();
        }
    }
}
