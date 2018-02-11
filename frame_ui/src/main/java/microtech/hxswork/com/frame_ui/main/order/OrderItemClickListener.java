package microtech.hxswork.com.frame_ui.main.order;

import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;

import me.yokeyword.fragmentation.SupportFragmentDelegate;
import microtech.hxswork.com.frame_core.init.Frame;
import microtech.hxswork.com.frame_core.middle.MiddleFragment;
import microtech.hxswork.com.frame_core.ui.OrderQianFields;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleItemEntity;
import microtech.hxswork.com.frame_core.util.SAToast;
import microtech.hxswork.com.frame_ui.R;
import microtech.hxswork.com.frame_ui.main.home_deatils.QianDeatail_Fragmnet;


/**
 * Created by microtech on 2017/11/20.
 */

public class OrderItemClickListener extends SimpleClickListener {
    private final SupportFragmentDelegate FRAGMENT;
    private Bundle mArgs=  null;
    private OrderItemClickListener(SupportFragmentDelegate fragment){
        this.FRAGMENT = fragment;
    }
    public static SimpleClickListener create(SupportFragmentDelegate fragment){
        return new OrderItemClickListener(fragment);
    }
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, final int position) {
        mArgs = new Bundle();

        QianDeatail_Fragmnet fragment = new QianDeatail_Fragmnet();
        MultipleItemEntity entity= (MultipleItemEntity) adapter.getItem(position);
        String[] a = new String[5];
        a[0] = entity.getField(OrderQianFields.REGON_ID);
        a[1] = entity.getField(OrderQianFields.GOV_ID);
        a[2] = entity.getField(OrderQianFields.DOCTOR_ID);
        a[3] = entity.getField(OrderQianFields._ID);
        a[4] = position+"";
        mArgs.putStringArray("data",a);
        fragment.setArguments(mArgs);
        FRAGMENT.start(fragment);


        //Toast.makeText(Frame.getApplicationContext(), "点击"+position+"  "+entity.getField(OrderQianFields.NAME), Toast.LENGTH_SHORT).show();
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
