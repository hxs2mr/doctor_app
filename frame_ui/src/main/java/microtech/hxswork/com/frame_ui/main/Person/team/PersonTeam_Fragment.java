package microtech.hxswork.com.frame_ui.main.Person.team;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import microtech.hxswork.com.frame_core.middle.MiddleFragment;
import microtech.hxswork.com.frame_core.net.RestClent;
import microtech.hxswork.com.frame_core.net.callback.ISuccess;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleItemEntity;
import microtech.hxswork.com.frame_ui.R;
import microtech.hxswork.com.frame_ui.main.order.OrderBinDetailDataConverter;

import static microtech.hxswork.com.frame_ui.login.LoginHandler.userBean;

/**
 * Created by microtech on 2017/12/11.我都团队
 */

public class PersonTeam_Fragment extends MiddleFragment implements View.OnClickListener {
   private LinearLayoutCompat team_back_linear = null;
   private AppCompatTextView team_title = null;
   private AppCompatTextView team_person = null;
   private AppCompatTextView team_date = null;
   private RecyclerView team_recyclew = null;
   private TeamAdapter adapter = null;
    @Override
    public Object setLayout() {
        return R.layout.person_team_fragment;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        team_back_linear = bind(R.id.team_back_linear);//返回
        team_title = bind(R.id.team_title);//团队名称
        team_person = bind(R.id.team_person);//负责人
        team_date = bind(R.id.team_date);//创建时间
        team_recyclew = bind(R.id.team_recyclew);//团队的成员

        team_back_linear.setOnClickListener(this);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        team_recyclew.setLayoutManager(manager);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.team_back_linear) {
            getSupportDelegate().pop();
        }
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
                        adapter = new TeamAdapter(userBean.getTeam_list());
                        team_recyclew.setAdapter(adapter);
        team_title.setText(userBean.getTeam_name());
        team_person.setText(userBean.getTeam_doctor_name());
        team_date.setText(userBean.getTimes());
    }
}
