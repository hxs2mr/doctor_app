package microtech.hxswork.com.frame_ui.main.order;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import microtech.hxswork.com.frame_core.net.RestClent;
import microtech.hxswork.com.frame_core.net.callback.ISuccess;
import microtech.hxswork.com.frame_core.ui.OrderQianFields;
import microtech.hxswork.com.frame_core.ui.recyclew.DataConVerter;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipViewHolder;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleFields;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleItemEntity;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleRecyclerAdapter;
import microtech.hxswork.com.frame_core.util.KeyBordUtil;
import microtech.hxswork.com.frame_core.util.SAToast;
import microtech.hxswork.com.frame_ui.R;
import microtech.hxswork.com.frame_ui.widget.SlidingButtonView;

import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.home_follow_number_total;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.suifan_number;

/**
 * Created by microtech on 2017/11/30.
 */

public class OrderAdapter extends MultipleRecyclerAdapter implements SlidingButtonView.IonSlidingButtonListener {
    private SlidingButtonView mMenu = null;
    //设置图片的加载测曰
    private static final RequestOptions RECYCLER_OPTIONS=
            new RequestOptions()
                    .centerCrop()
                    .error(R.mipmap.deadpool)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate();

    public void delete(int i) {
        remove(i);//删除
      // notifyDataSetChanged();
    }
    protected OrderAdapter(List<MultipleItemEntity> data,String type) {
        super(data);
        init(type);
    }
    private void init(String type)
    {
        if(type.equals("qian")){
            //代表是签约的界面
            addItemType(OrderListItemType.ITEM_ORDER_QIAN, R.layout.qian_item);
        }else if(type.equals("follow")){
            addItemType(OrderListItemType.ITEM_ORDER_FOLLOE_UP, R.layout.businss_item);//随访
        }else if(type.equals("busniss_shape"))
        {
            addItemType(OrderListItemType.ITEM_ORDER_FOLLOE_BUNINESS, R.layout.bottom_text_top_icon_layout);//业绩
        }

        openLoadAnimation();
        //多次执行动画
        isFirstOnly(false);
    }
    public static OrderAdapter create(DataConVerter verter,String type){
        return new OrderAdapter(verter.CONVERT(),type);
    }

    @Override
    protected void convert(final MultipViewHolder helper, final MultipleItemEntity item) {
        super.convert(helper, item);
        switch (helper.getItemViewType()){
            case OrderListItemType.ITEM_ORDER_FOLLOE_BUNINESS://业绩
                break;
            case OrderListItemType.ITEM_ORDER_FOLLOE_UP://随访

                AppCompatImageView follow_staties = helper.getView(R.id.follow_staties);
                AppCompatTextView  bisniss_sui_flage= helper.getView(R.id.bisniss_sui_flage);
                AppCompatTextView bisniss_shen_time= helper.getView(R.id.bisniss_shen_time);
                AppCompatTextView bisniss_name= helper.getView(R.id.bisniss_name);
                AppCompatTextView bisniss_time= helper.getView(R.id.bisniss_time);
                AppCompatTextView bisniss_phone= helper.getView(R.id.bisniss_phone);
                LinearLayoutCompat bisniss_phone_linear= helper.getView(R.id.bisniss_phone_linear);
                AppCompatTextView bisniss_delete= helper.getView(R.id.bisniss_delete);
                LinearLayoutCompat infomation_width_linear= helper.getView(R.id.infomation_width_linear);
                if(item.getField(OrderQianFields.SUI_FLAGE).toString().equals("0"))
                {
                    bisniss_sui_flage.setText("上门随访");
                }else if(item.getField(OrderQianFields.SUI_FLAGE).toString().equals("1"))
                {
                    bisniss_sui_flage.setText("电话随访");
                }else {
                    bisniss_sui_flage.setText("门诊随访");
                }
                String day =item.getField(OrderQianFields.SHEN_TIME).toString();
                String botm = day;
                int shi_botm=0;
                day = day.replace("天","").replace("时","");
                int end_day = Integer.parseInt(day);
                shi_botm = end_day;
                if(botm.contains("时")){
                    end_day = 0 ;
                }
                if(item.getField(OrderQianFields.STATUE).equals("2"))
                {
                    bisniss_shen_time.setText("已随访");
                    follow_staties.setBackgroundResource(R.drawable.loginbutton_back_shape);

                }else if(item.getField(OrderQianFields.STATUE).equals("1")) {
                    bisniss_shen_time.setText("已过期");
                    follow_staties.setBackgroundResource(R.drawable.follow_guoqi);
                }else {
                    if(end_day>0)
                    {
                        bisniss_shen_time.setText(end_day+"天剩余");
                        follow_staties.setBackgroundResource(R.drawable.loginbutton_back_shape);
                    }else {
                        if(botm.contains("时")){
                            if(shi_botm<0)
                            {
                                bisniss_shen_time.setText("已过期");
                                follow_staties.setBackgroundResource(R.drawable.follow_guoqi);
                            }else {
                                bisniss_shen_time.setText(shi_botm + "时剩余");
                                follow_staties.setBackgroundResource(R.drawable.follow_masan);
                            }
                        }else {
                            bisniss_shen_time.setText("0天剩余");
                            follow_staties.setBackgroundResource(R.drawable.follow_masan);
                        }
                    }
                }

                bisniss_name.setText(item.getField(OrderQianFields.NAME).toString());
                bisniss_time.setText(item.getField(OrderQianFields.TIME).toString());
                bisniss_phone.setText(item.getField(OrderQianFields.PHONE).toString());

                WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
                int width = wm.getDefaultDisplay().getWidth(); //获取屏幕的宽度

                LinearLayoutCompat.LayoutParams linearParams =(LinearLayoutCompat.LayoutParams) infomation_width_linear.getLayoutParams(); //取控件textView当前的布局参数 linearParams.height = 20;// 控件的高强制设成20

                linearParams.width = width;// 控件的宽强制设成30

                infomation_width_linear.setLayoutParams(linearParams);
                ((SlidingButtonView) helper.itemView).setSlidingButtonListener(OrderAdapter.this);
                bisniss_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        init_delete_net(item,helper);
                    }
                });
                break;
            case OrderListItemType.ITEM_ORDER_QIAN://签约
                CircleImageView imageView = helper.getView(R.id.qian_thum);
                AppCompatImageView qian_lable= helper.getView(R.id.qian_lable);
                AppCompatTextView  name = helper.getView(R.id.qian_name);
                AppCompatTextView  sex = helper.getView(R.id.qian_sex);
                AppCompatTextView  age = helper.getView(R.id.qian_age);
                AppCompatTextView  bin = helper.getView(R.id.qian_bin);
                AppCompatTextView  statue = helper.getView(R.id.qian_status);
                AppCompatTextView  number = helper.getView(R.id.qian_number);
                AppCompatTextView  time = helper.getView(R.id.qian_time);

                name.setText(item.getField(OrderQianFields.NAME).toString());

                if(item.getField(OrderQianFields.SEX).toString().equals("1"))
                {
                    sex.setText("男");
                }else {
                    sex.setText("女");
                }
                age.setText(item.getField(OrderQianFields.BIRTHDAY).toString());
                bin.setText(item.getField(OrderQianFields.BIN).toString());
                statue.setText(item.getField(OrderQianFields.STATUE).toString());
                number.setText(item.getField(OrderQianFields.NUMBER).toString());
                time.setText(item.getField(OrderQianFields.TIME).toString());

                if(item.getField(OrderQianFields.STATUE).toString().equals("正在治疗"))
                {
                    qian_lable.setBackgroundResource(microtech.hxswork.com.frame_core.R.mipmap.label);
                }else if(item.getField(OrderQianFields.STATUE).toString().equals("有效控制"))
                {
                    qian_lable.setBackgroundResource(microtech.hxswork.com.frame_core.R.mipmap.lable2);
                }else  if(item.getField(OrderQianFields.STATUE).toString().equals("病情恶化")){
                    qian_lable.setBackgroundResource(microtech.hxswork.com.frame_core.R.mipmap.lable3);
                }else if(item.getField(OrderQianFields.STATUE).toString().equals("患者死亡")){
                    qian_lable.setBackgroundResource(microtech.hxswork.com.frame_core.R.mipmap.lable3);
                }else if(item.getField(OrderQianFields.STATUE).toString().equals("效果一般")){
                    qian_lable.setBackgroundResource(microtech.hxswork.com.frame_core.R.mipmap.label);
                }

                //头像模块
             /*   Glide.with(mContext)
                        .load("http://qn.newmicrotech.cn/"+item.getField(OrderQianFields.THUMB)+"?imageMogr2/thumbnail/200x/strip/quality/50/format/webp")
                        .apply(RECYCLER_OPTIONS)
                        .into(imageView);*/
                break;
        }
    }

    private void init_delete_net(MultipleItemEntity item, final MultipViewHolder helper) {
        RestClent.builder()
                .url("visits_del")
                .loader(mContext)
                .params("region_id",item.getField(OrderQianFields.REGON_ID))
                .params("gov_id",item.getField(OrderQianFields.GOV_ID))
                .params("doctor_id",item.getField(OrderQianFields.DOCTOR_ID))
                .params("visits_id",item.getField(OrderQianFields._ID))
                .success(new ISuccess() {

                    @Override
                    public void onSuccess(String response) {

                        System.out.println("随访删除返回的***********"+response);
                         int code = JSON.parseObject(response).getInteger("code");
                         if(code == 200) {
                             delete(helper.getLayoutPosition()-1);
                             System.out.println("点击了第几个*****"+helper.getLayoutPosition());
                             home_follow_number_total--;
                             suifan_number.setText(home_follow_number_total+"");
                         }
                    }
                })
                .build()
                .post();

    }


    @Override
    public void onMenuIsOpen(View view) {
        mMenu = (SlidingButtonView) view;
    }

    @Override
    public void onDownOrMove(SlidingButtonView slidingButtonView) {
        if(menuIsOpen()){
            if(mMenu != slidingButtonView){
                closeMenu();
            }
        }
    }

    public Boolean menuIsOpen() {
        if(mMenu != null){
            return true;
        }
        return false;
    }
    public void closeMenu() {
        mMenu.closeMenu();
        mMenu = null;
    }
}
