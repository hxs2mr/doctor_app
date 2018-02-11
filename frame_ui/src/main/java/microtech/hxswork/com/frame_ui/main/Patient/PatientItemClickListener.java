package microtech.hxswork.com.frame_ui.main.Patient;

import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;

import java.util.ArrayList;
import java.util.List;

import microtech.hxswork.com.frame_core.init.Frame;
import microtech.hxswork.com.frame_core.middle.MiddleFragment;
import microtech.hxswork.com.frame_core.ui.OrderQianFields;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleFields;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleItemEntity;
import static  microtech.hxswork.com.frame_core.ui.refresh.RefreshHandler.patient_postion;
import static microtech.hxswork.com.frame_core.net.RestClent.TIGE_USERID;
import static microtech.hxswork.com.frame_ui.main.Patient.PatientFragment.head_patinent_image;

/**
 * Created by microtech on 2017/11/20.
 */

public class PatientItemClickListener extends SimpleClickListener {
    private final MiddleFragment FRAGMENT;
    public static  int  onclick_flage =0;
    public static int chat = -1;
    private PatientItemClickListener(MiddleFragment fragment){
        this.FRAGMENT = fragment;
    }
    public static SimpleClickListener create(MiddleFragment fragment){
        return new PatientItemClickListener(fragment);
    }
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        onclick_flage = position;
        patient_postion = position;
        Bundle args = new Bundle();
        PatientNextFragment fragment = PatientNextFragment.create();
        MultipleItemEntity entity = (MultipleItemEntity) adapter.getItem(position);
        String[] a = new String[7];
        System.out.println("******下一步*****");
        System.out.println("***********"+entity.getField(MultipleFields.NAME).toString());

        if(!entity.getField(MultipleFields.CHAT).equals(""))
        {
            chat= Integer.parseInt(entity.getField(MultipleFields.CHAT).toString());
        }
        a[0] = (entity.getField(MultipleFields.NAME)).toString();
        a[1] = (entity.getField(MultipleFields.USER_ID)).toString();
        a[2] = (entity.getField(OrderQianFields.REGON_ID)).toString();
        a[3] = (entity.getField(OrderQianFields.GOV_ID)).toString();
        a[4] = (entity.getField(OrderQianFields.DOCTOR_ID)).toString();
        a[5] = (entity.getField(MultipleFields.ACCID)).toString();
        a[6] = (entity.getField(MultipleFields.TOKEN)).toString();
        TIGE_USERID = (entity.getField(MultipleFields.USER_ID)).toString();
        args.putStringArray("user",a);
        fragment.setArguments(args);
        FRAGMENT.getSupportDelegate().start(fragment);
        head_patinent_image = entity.getField(MultipleFields.THUMB).toString();//这点有问题闪退
       // Toast.makeText(Frame.getApplicationContext(), "点击"+position, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {

    }
}
