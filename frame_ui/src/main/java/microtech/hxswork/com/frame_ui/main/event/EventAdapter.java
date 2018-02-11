package microtech.hxswork.com.frame_ui.main.event;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.common.util.C;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.yokeyword.fragmentation.SupportFragmentDelegate;
import microtech.hxswork.com.frame_core.net.RestClent;
import microtech.hxswork.com.frame_core.net.callback.ISuccess;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipViewHolder;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleItemEntity;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleRecyclerAdapter;
import microtech.hxswork.com.frame_core.util.SAToast;
import microtech.hxswork.com.frame_ui.R;
import microtech.hxswork.com.frame_ui.main.order.OrderAdapter;
import microtech.hxswork.com.frame_ui.widget.SlidingButtonView;

import static microtech.hxswork.com.frame_ui.login.LoginHandler.fristBean;
import static microtech.hxswork.com.frame_ui.login.LoginHandler.userBean;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.EVENT_FRAGMENT;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.NIM_Flage_Edit;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.event_number;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.event_number_total;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.event_right;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.sq;

/**
 * Created by microtech on 2017/12/12.
 */

public class EventAdapter extends MultipleRecyclerAdapter implements SlidingButtonView.IonSlidingButtonListener {
    private SlidingButtonView mMenu = null;
    private Bundle mArgs=  null;
    private SupportFragmentDelegate FRAGMENT = null;
    //设置图片的加载测曰
    private static final RequestOptions RECYCLER_OPTIONS=
            new RequestOptions()
                    .centerCrop()
                    .error(R.mipmap.start_head_url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate();

    public EventAdapter(List<MultipleItemEntity> data,SupportFragmentDelegate fragment) {
        super(data);
        this.FRAGMENT = fragment;
        addItemType(EventItemType.EVENT_ITEM, R.layout.event_item);//文本布局
    }

    public void refresh_delete_data() {
        getData().clear();
        notifyDataSetChanged();
    }
    public void delete_postion(int i) {
        remove(i);//删除
        // notifyDataSetChanged();
    }
    @Override
    protected void convert(final MultipViewHolder helper, final MultipleItemEntity item) {
        super.convert(helper, item);
        switch (helper.getItemViewType())
        {
            case EventItemType.EVENT_ITEM:
                CircleImageView image=helper.getView(R.id.event_item_image);
                final AppCompatTextView title =helper.getView(R.id.event_item_title);
                AppCompatTextView content =helper.getView(R.id.event_item_content);
                AppCompatTextView time =helper.getView(R.id.event_item_time);
                AppCompatTextView event_delete= helper.getView(R.id.bisniss_delete);
                LinearLayoutCompat infomation_width_linear = helper.getView(R.id.infomation_width_linear);
                final AppCompatTextView infomation_item_nums = helper.getView(R.id.infomation_item_nums);
                final AppCompatTextView event_item_account =helper.getView(R.id.event_item_account);//患者申请的同意按钮
                final String flage= item.getField(EventFilds.FLAGE);//这点有点问题
                System.out.println("flage************"+flage);
                String s_title  = item.getField(EventFilds.TITLE);
                String s_cont  = item.getField(EventFilds.CONTENT);
                String s_time  = item.getField(EventFilds.TIME);
                String read = item.getField(EventFilds.READ);
                String qita = item.getField(EventFilds.QITA);
                final String user_id  = item.getField(EventFilds.USER_ID);
                if(flage.equals("2") )//表示患者申请的消息
                {
                    System.out.println("事件的user_id************"+user_id);
                    event_item_account.setVisibility(View.VISIBLE);
                    time.setVisibility(View.GONE);
                    image.setImageResource(R.mipmap.icon_apply1);
                }else if(flage.equals("0"))//表示患者关注
                {
                    event_item_account.setVisibility(View.GONE);
                    time.setVisibility(View.VISIBLE);
                    time.setText(s_time);
                    image.setImageResource(R.mipmap.guanzhu);
                }
                else if(flage.equals("9"))//表示系统通知
                {
                    event_item_account.setVisibility(View.GONE);
                    time.setVisibility(View.VISIBLE);
                    time.setText(s_time);
                    image.setImageResource(R.mipmap.tonzhi);
                }else if(flage.equals("5"))//表示患者消息
                {
                    event_item_account.setVisibility(View.GONE);
                    time.setVisibility(View.VISIBLE);
                    time.setText(s_time);
                    if(!qita.equals("")){
                        if(read.equals("0"))
                        {
                            infomation_item_nums.setVisibility(View.VISIBLE);
                            infomation_item_nums.setText(qita);
                        }
                    }else {
                        if(read.equals("1")){
                            infomation_item_nums.setVisibility(View.GONE);
                        }
                    }
                    Glide.with(mContext)
                            .load(item.getField(EventFilds.IMAGE_URL))
                            .apply(RECYCLER_OPTIONS)
                            .into(image);


                }
                else if(flage.equals("1"))//随访提醒
                {
                    event_item_account.setVisibility(View.GONE);
                    time.setVisibility(View.VISIBLE);
                    time.setText(s_time);
                    image.setImageResource(R.mipmap.icon_visit1);
                }else if(flage.equals("4"))
                {
                    event_item_account.setVisibility(View.GONE);
                    time.setVisibility(View.VISIBLE);
                    time.setText(s_time);
                    image.setImageResource(R.mipmap.icon_task1);
                }else if(flage.equals("3"))
                {
                    event_item_account.setVisibility(View.GONE);
                    time.setVisibility(View.VISIBLE);
                    time.setText(s_time);
                    image.setImageResource(R.mipmap.icon_crisis1);
                }
                title.setText(s_title);
                if(read.equals("1"))
                {
                    title.setTextColor(Color.parseColor("#C1CFD6"));
                }
                if(read.equals("3"))
                {
                    title.setTextColor(Color.parseColor("#C1CFD6"));
                    if(flage.equals("2"))
                    {
                        event_item_account.setText("已同意");
                        event_item_account.setTextColor(Color.parseColor("#000000"));
                        event_item_account.setBackgroundResource(R.drawable.event_item_shape1);
                    }
                }
                if(flage.equals("2"))
                {
                    content.setText(s_cont+"申请与您建立随访关系");
                }else if(flage.equals("1"))
                {
                    content.setText("您明天即将随访“"+ s_cont+"”家庭，及时做好出行");
                }
                else {
                    content.setText(s_cont);
                }

                WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
                int width = wm.getDefaultDisplay().getWidth(); //获取屏幕的宽度

                LinearLayoutCompat.LayoutParams linearParams =(LinearLayoutCompat.LayoutParams) infomation_width_linear.getLayoutParams(); //取控件textView当前的布局参数 linearParams.height = 20;// 控件的高强制设成20

                linearParams.width = width;// 控件的宽强制设成30

                infomation_width_linear.setLayoutParams(linearParams);

                ((SlidingButtonView) helper.itemView).setSlidingButtonListener(EventAdapter.this);//左划删除

                event_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!flage.equals("5"))
                        {
                        if(item.getField(EventFilds.USER_ID)!=null)
                        {
                            System.out.println("删除进来了!!!!!!!!!!!!");
                            delete(item.getField(EventFilds.MESSAGE_ID).toString());
                        }
                        }else {
                            delete_accid(item.getField(EventFilds.ACC_ID)+"");
                        }
                        delete_postion(helper.getLayoutPosition());
                        System.out.println("点击了删除!!!!!!!!!!!!");
                    }
                });

                //TITLE
                event_item_account.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!event_item_account.getText().toString().equals("已同意"))
                        {
                            System.out.println("点击了同意******");
                            item.setField(EventFilds.READ,3+"");
                            init_net(item.getField(EventFilds.USER_ID)+"",event_item_account,item.getField(EventFilds.MESSAGE_ID)+"");
                        }
                    }
                });

                if(flage.equals("5")){
                    infomation_width_linear.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            int number = Integer.parseInt(item.getField(EventFilds.QITA).toString());
                            item.setField(EventFilds.READ,1+"");
                            item.setField(EventFilds.QITA,"0");
                            number = Integer.parseInt(infomation_item_nums.getText().toString()) - number;

                            infomation_item_nums.setText("0");
                            infomation_item_nums.setVisibility(View.GONE);
                            if(number<=0)
                            {
                                event_right.setVisibility(View.GONE);
                                event_number.setText("0");
                            }else {
                                event_number.setText(number+"");
                                event_right.setVisibility(View.VISIBLE);
                            }
                            doLogin(item.getField(EventFilds.ACC_ID).toString() ,item.getField(EventFilds.TITLE).toString());
                            title.setTextColor(Color.parseColor("#C1CFD6"));
                            update_sql(item.getField(EventFilds.ACC_ID).toString());
                        }
                    });
                }else {
                    infomation_width_linear.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            item.setField(EventFilds.READ,1+"");
                            if(Integer.parseInt(flage) != 2&& Integer.parseInt(flage)!=5&& Integer.parseInt(flage)!=9&& Integer.parseInt(flage)!=0) {
                                final String[] a = new String[4];
                                String flage = item.getField(EventFilds.FLAGE);
                                a[0] =flage+"";
                                a[1]=item.getField(EventFilds.CONTENT);
                                a[2]=item.getField(EventFilds.USER_ID);
                                a[3]=item.getField(EventFilds.QITA);
                                mArgs = new Bundle();
                                final EventOrderFragment fragment = new EventOrderFragment();
                                mArgs.putStringArray("data",a);
                                fragment.setArguments(mArgs);
                                EVENT_FRAGMENT.start(fragment);
                            }
                            title.setTextColor(Color.parseColor("#C1CFD6"));
                            put_net(item.getField(EventFilds.MESSAGE_ID)+"");
                        }
                    });
                }

                break;
            default:
                    break;
        }
    }


    private void update_sql(String accid)
    {
/*
//修改SQL语句
        String sql = "update event set du = 1 where user_id ="+user_id;
//执行SQL
        sq.execSQL(sql);
*/
    ContentValues values = new ContentValues();
//在values中添加内容
        values.put("du","1");
//修改条件
        String whereClause = "accid_id=?";
//修改添加参数
        String[] whereArgs={String.valueOf(accid)};
//修改
        sq.update("event",values,whereClause,whereArgs);
    }

    private void put_net(String  message_id) {

        List<String> list = new ArrayList<>();
        list.add(message_id);
        RestClent.builder()
                .url("messageSubmit")
                .params("message_id",JSON.toJSON(list))
                .params("gov_id",fristBean.getGov_id())
                .params("status",3)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        System.out.println("点击返回的内容*******"+response);
                    }
                })
                .build()
                .post();
    }
    public void doLogin(String accid,String name)
    {
        NIM_Flage_Edit.putString("nim_flage", "1");
        NIM_Flage_Edit.putString("nim_acc",accid);
        NIM_Flage_Edit.commit();
        NimUIKit.startP2PSession(mContext,accid,name);//对方的accid 如：医生的accid
    }

    private void init_net(final String user_id, final AppCompatTextView event_item_account,String messgae_id) {

       /* System.out.println("region_id*****"+fristBean.getRegion_id());
        System.out.println("gov_id*****"+fristBean.getGov_id());
        System.out.println("doctor_id*****"+userBean.getId());
        System.out.println("user_id*****"+user_id);*/
        if(user_id!=null)
        {
            RestClent.builder()
                    .url("confirm_audit")
                    .loader(mContext)
                    .params("region_id",fristBean.getRegion_id())
                    .params("gov_id",fristBean.getGov_id())
                    .params("doctor_id",userBean.getId())
                    .params("user_id",user_id)
                    .params("status",2)
                    .params("message_id",messgae_id)
                    .success(new ISuccess() {
                        @Override
                        public void onSuccess(String response) {
                            System.out.println("患者申请返回的结果********"+response);
                            event_item_account.setText("已同意");
                            event_item_account.setTextColor(Color.parseColor("#000000"));
                            event_item_account.setBackgroundResource(R.drawable.event_item_shape1);
                            //update_sql(user_id);
                        }
                    })
                    .build()
                    .post();
        }else {
            SAToast.makeText(mContext,"该用户存在信息不全问题").show();
        }

    }


    private void delete_accid(String acc_id) {//删除数据
//删除条件
               String whereClause = "accid_id=?";
                String[] whereArgs = {String.valueOf(acc_id)};
                sq.delete("event",whereClause,whereArgs); //改为重服务器获取
//执行删除
    }
    private void delete(String message_id) {//删除数据
//删除条件
              /*  String whereClause = "user_id=?";
                String[] whereArgs = {String.valueOf(USER)};
                sq.delete("event",whereClause,whereArgs); //改为重服务器获取
//执行删除*/
        System.out.println("删除的message_id*******"+message_id);
        List<String> list = new ArrayList<>();
        list.add(message_id);
        RestClent.builder()
                .url("messageSubmit")
                .loader(mContext)
                .params("message_id", JSON.toJSON(list))
                .params("gov_id",fristBean.getGov_id())
                .params("status",2)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                    System.out.println("删除返回的内容*******"+response);
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
