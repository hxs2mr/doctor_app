package microtech.hxswork.com.frame_ui.main.Patient;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.choices.divider.Divider;
import com.choices.divider.DividerItemDecoration;

import microtech.hxswork.com.frame_core.middle.MiddleFragment;
import microtech.hxswork.com.frame_core.net.RestClent;
import microtech.hxswork.com.frame_core.net.callback.ISuccess;
import microtech.hxswork.com.frame_ui.R;
import microtech.hxswork.com.frame_ui.main.home_deatils.Follow_Add_Frgamnet;

import static microtech.hxswork.com.frame_ui.login.LoginHandler.fristBean;
import static microtech.hxswork.com.frame_ui.login.LoginHandler.personBean;
import static microtech.hxswork.com.frame_ui.login.LoginHandler.userBean;

/**
 * Created by microtech on 2017/12/14.
 */

public class PatientFollowFragment extends MiddleFragment implements View.OnClickListener {
    private RecyclerView recyclerView = null;
    private LinearLayoutCompat patient_follow_back_linear = null;
    private PatientFollowAdapter adapter = null;
    private  String[] a;
    private  LinearLayoutCompat no_suifan_linear = null;
    private AppCompatTextView start_follow_tv = null;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        a = bundle.getStringArray("data");
    }

    @Override
    public Object setLayout() {
        return R.layout.patient_follow_fragment;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        recyclerView = bind(R.id.patient_follow_recycle);
        no_suifan_linear= bind(R.id.no_suifan_linear);
        start_follow_tv = bind(R.id.start_follow_tv);
        patient_follow_back_linear = bind(R.id.patient_follow_back_linear);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        patient_follow_back_linear.setOnClickListener(this);
        start_follow_tv.setOnClickListener(this);
        final DividerItemDecoration itemDecoration = new DividerItemDecoration();
        itemDecoration.setDividerLookup(new DividerItemDecoration.DividerLookup() {//添加分隔线
            @Override
            public Divider getVerticalDivider(int position) {
                return null;
            }

            @Override
            public Divider getHorizontalDivider(int position) {
                return new Divider.Builder()
                        .size(1)
                        .margin(16, 16)
                        .color(Color.parseColor("#F0F5F7"))
                        .build();
            }
        });
        recyclerView.addItemDecoration(itemDecoration);
        
    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

        RestClent.builder()
                .url("visits_info")
                .loader(getContext())
                .params("region_id",fristBean.getRegion_id())
                .params("gov_id",fristBean.getGov_id())
                .params("doctor_id",userBean.getId())
                .params("user_id",a[3])
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        System.out.println("随访记录返回的数据*****************:"+response);
                        final JSONObject obj = JSON.parseObject(response).getJSONObject("obj");
                        final JSONArray array = obj.getJSONArray("list");
                        if(array.size() == 0)
                        {
                            no_suifan_linear.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }else {
                            no_suifan_linear.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        adapter = new PatientFollowAdapter( new PatientFollowDataConvert().setJsonData(response).CONVERT(),getActivity());
                        recyclerView.setAdapter(adapter);
                        }

                    }
                })
                .build()
                .post();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.patient_follow_back_linear) {
            getSupportDelegate().pop();
        }else if(i == R.id.start_follow_tv)
        {
            getSupportDelegate().pop();
            getSupportDelegate().start(new Follow_Add_Frgamnet());
        }
    }
}
