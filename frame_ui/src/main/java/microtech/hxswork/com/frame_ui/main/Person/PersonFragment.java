package microtech.hxswork.com.frame_ui.main.Person;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import de.hdodenhof.circleimageview.CircleImageView;
import microtech.hxswork.com.frame_core.middle.bottom.BottomItemFragment;
import microtech.hxswork.com.frame_core.ui.OrderQianFields;
import microtech.hxswork.com.frame_ui.R;
import microtech.hxswork.com.frame_ui.main.Person.qr.Person_Qr_Fragment;
import microtech.hxswork.com.frame_ui.main.Person.setting.PersonSetting_Fragment;
import microtech.hxswork.com.frame_ui.main.Person.team.PersonTeam_Fragment;

import static microtech.hxswork.com.frame_ui.login.LoginHandler.personBean;
import static microtech.hxswork.com.frame_ui.login.LoginHandler.userBean;


/**
 * Created by microtech on 2017/11/17.
 */

public class PersonFragment extends BottomItemFragment implements View.OnClickListener {
   private   LinearLayoutCompat person_option_linear = null ;
   private LinearLayoutCompat person_setting_linear = null;
   private LinearLayoutCompat person_qr_linear = null;
    private LinearLayoutCompat person_team_linear = null;//团对
    private LinearLayoutCompat personal_ourdata_linear = null;
    private AppCompatTextView  person_address=null;
    public static AppCompatTextView  person_name=null;
    public static CircleImageView personal_headimage = null;

    //设置图片的加载测曰
    private static final RequestOptions RECYCLER_OPTIONS=
            new RequestOptions()
                    .centerCrop()
                    .error(R.mipmap.deadpool)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate();
    @Override
    public Object setLayout() {
        return R.layout.personal_fragment;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        person_address = bind(R.id.person_address);
        person_name = bind(R.id.person_name);
        personal_headimage = bind(R.id.personal_headimage);
        person_option_linear = bind(R.id.person_option_linear);
        person_setting_linear = bind(R.id.person_setting_linear);
        person_qr_linear = bind(R.id.person_qr_linear);
        person_team_linear = bind(R.id.person_team_linear);
        personal_ourdata_linear = bind(R.id.personal_ourdata_linear);
        person_option_linear.setOnClickListener(this);
        person_setting_linear.setOnClickListener(this);
        person_qr_linear.setOnClickListener(this);
        person_team_linear.setOnClickListener(this);
        personal_ourdata_linear.setOnClickListener(this);
        if(userBean.getAddress()!=null)
        {
            person_address.setText(personBean.getDep_name()+"   "+personBean.getDep_title());
        }
        if(userBean.getName()!=null)
        {
            person_name.setText(userBean.getName());

        }

        Glide.with(getContext())
                .load("http://qn.newmicrotech.cn/"+userBean.getAvatar()+"?imageMogr2/thumbnail/200x/strip/quality/50/format/webp")
                .apply(RECYCLER_OPTIONS)
                .into(personal_headimage);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.person_option_linear) {
            Intent intent = new Intent(getActivity(),PersonalOptionActivity.class);
            startActivity(intent);
        }else if(i == R.id.person_setting_linear)
        {
            getParentFragmen().getSupportDelegate().start(new PersonSetting_Fragment());

        }else if(i == R.id.person_qr_linear)
        {
            getParentFragmen().getSupportDelegate().start(new Person_Qr_Fragment());

        }else if(i == R.id.person_team_linear)
        {
         getParentFragmen().getSupportDelegate().start(new PersonTeam_Fragment());

        }else if(i == R.id.personal_ourdata_linear)
        {
            getParentFragmen().getSupportDelegate().start(new PersonData_Fragment());

        }
    }
}
