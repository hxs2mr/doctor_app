package microtech.hxswork.com.frame_ui.main.sign;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.choices.divider.Divider;
import com.choices.divider.DividerItemDecoration;

import microtech.hxswork.com.frame_core.middle.MiddleFragment;
import microtech.hxswork.com.frame_core.net.RestClent;
import microtech.hxswork.com.frame_core.net.callback.ISuccess;
import microtech.hxswork.com.frame_ui.R;
import microtech.hxswork.com.frame_ui.main.order.OrderQianDataConverter;
import microtech.hxswork.com.frame_ui.main.order.OrderRefreshHandler;

import static microtech.hxswork.com.frame_ui.login.LoginHandler.fristBean;
import static microtech.hxswork.com.frame_ui.login.LoginHandler.userBean;

/**
 * Created by microtech on 2018/1/8.
 */

public class SignFragment extends MiddleFragment implements View.OnClickListener {
    private  String[] sing_last_data;
   // private SwipeRefreshLayout sign_swipe=null;
    public static RecyclerView sign_rv = null;
    private LinearLayoutCompat sign_back_linear = null;
    private SignRefreshHandler mRefreshHandler = null;
    public static LinearLayoutCompat sign_no_data_lienar=null;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        sing_last_data = bundle.getStringArray("data");
    }
    @Override
    public Object setLayout() {
        return R.layout.sign_fragment;
    }
    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        //sign_swipe = bind(R.id.sign_swipe);
        sign_rv = bind(R.id.sign_rv);
        sign_back_linear = bind(R.id.sign_back_linear);
        sign_no_data_lienar =bind(R.id.sign_no_data_lienar);
        sign_back_linear.setOnClickListener(this);
        initRefreshLayout();
        mRefreshHandler = SignRefreshHandler.create(sign_rv,new SignDataConverter(),"1");
    }
    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        mRefreshHandler.firstPage(getContext(),sing_last_data[3]);//第一次加载数据
    }
    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.sign_back_linear) {
            getSupportDelegate().pop();
        }
    }
    private void initRefreshLayout(){//设置刷新的颜色和款式
       /* sign_swipe.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_orange_dark,
                android.R.color.holo_red_dark
        );
        sign_swipe.setProgressViewOffset(true,50,300);*/
        LinearLayoutManager manager =new LinearLayoutManager(getContext());
        sign_rv.setLayoutManager(manager);

        final DividerItemDecoration itemDecoration = new DividerItemDecoration();
        itemDecoration.setDividerLookup(new DividerItemDecoration.DividerLookup() {//添加分隔线
            @Override
            public Divider getVerticalDivider(int position) {
                return null;
            }

            @Override
            public Divider getHorizontalDivider(int position) {
                return new Divider.Builder()
                        .size(24)
                        .margin(5, 5)
                        .color(Color.parseColor("#F0F5F7"))
                        .build();
            }
        });
        sign_rv.addItemDecoration(itemDecoration);
    }
}
