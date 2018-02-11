package microtech.hxswork.com.frame_ui.main.Search;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.choices.divider.Divider;
import com.choices.divider.DividerItemDecoration;
import com.joanzapata.iconify.widget.IconTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import freemarker.template.utility.StringUtil;
import me.yokeyword.fragmentation.SupportFragmentDelegate;
import me.yokeyword.fragmentation.SwipeBackLayout;
import me.yokeyword.fragmentation_swipeback.SwipeBackActivity;
import microtech.hxswork.com.frame_core.middle.MiddleFragment;
import microtech.hxswork.com.frame_core.net.RestClent;
import microtech.hxswork.com.frame_core.net.callback.ISuccess;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleItemEntity;
import microtech.hxswork.com.frame_core.util.KeyBordUtil;
import microtech.hxswork.com.frame_core.util.SAToast;
import microtech.hxswork.com.frame_core.util.storage.LattePreference;
import microtech.hxswork.com.frame_ui.R;
import microtech.hxswork.com.frame_ui.login.LoginFragment;
import microtech.hxswork.com.frame_ui.main.order.OrderItemClickListener;

import static android.view.View.FOCUSABLE;
import static com.blankj.utilcode.util.SnackbarUtils.getView;
import static microtech.hxswork.com.frame_ui.login.LoginHandler.fristBean;
import static microtech.hxswork.com.frame_ui.login.LoginHandler.userBean;
import static microtech.hxswork.com.frame_ui.main.home.HomeFragment.mSearchtype;

/**
 * Created by microtech on 2017/12/7.
 */

public class Search_Activity extends SwipeBackActivity implements View.OnClickListener, View.OnFocusChangeListener {
    RecyclerView recyclerView = null;
    LinearLayoutCompat back = null;
    AppCompatEditText edit_search = null;
    SearhAdapter adapter = null;
    List<MultipleItemEntity> data = null;
    AppCompatTextView search_hisroy = null;
    AppCompatImageView search_delete = null;
    AppCompatTextView search_result = null;
    public  static  int search_flage= 0 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.huanperson_fragment);
        getSwipeBackLayout().setEdgeOrientation(SwipeBackLayout.EDGE_LEFT); // EDGE_LEFT(默认),EDGE_ALL
        getSwipeBackLayout().setParallaxOffset(0.0f - 1.0f); // （类iOS）滑动退出视觉差，默认0.3
        setSwipeBackEnable(true); // 是否允许滑动
        onBindView();
    }

    public void onBindView() {
        search_hisroy = findViewById(R.id.search_hisroy);
        search_delete = findViewById(R.id.search_delete);
        recyclerView = findViewById(R.id.search_recycle);
        back = findViewById(R.id.next_pop_back_linear);
        edit_search = findViewById(R.id.search_edit);
        search_result = findViewById(R.id.search_result);
        back.setOnClickListener(this);
        search_delete.setOnClickListener(this);
        edit_search.setOnFocusChangeListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        search_result.setVisibility(View.GONE);
         data= new SearchDataConvert().CONVERT();
        adapter  = new SearhAdapter(data);
        search_flage =0 ;
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(ListerSearchOnclick.create(this));//点击事件

        final DividerItemDecoration itemDecoration = new DividerItemDecoration();
        itemDecoration.setDividerLookup(new DividerItemDecoration.DividerLookup() {//添加分隔线
            @Override
            public Divider getVerticalDivider(int position) {
                return null;
            }

            @Override
            public Divider getHorizontalDivider(int position) {
                return new Divider.Builder()
                        .size(2)
                        .margin(20, 20)
                        .color(Color.GRAY)
                        .build();
            }
        });
        recyclerView.addItemDecoration(itemDecoration);
        edit_search.addTextChangedListener(new Edittext_change_username());//监听edittext的输入

        edit_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                        instart_thread();
                    return true;
                }
                return false;
            }
        });

        edit_search.requestFocus();
        Search_Activity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }


    class Edittext_change_username implements TextWatcher {
        //设置edittext的监听时间
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            search_hisroy.setVisibility(View.GONE);
            search_delete.setVisibility(View.GONE);
            search_result.setVisibility(View.VISIBLE);
        }
        @Override
        public void afterTextChanged(Editable editable) {//文本改变之后   发起网络请求
            //saveItem(edit_search.getText().toString());
        }
    }
    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.next_pop_back_linear) {
            finish();
        }else if(i == R.id.search_delete)//删除历史记录
        {
            final String jsonStr = LattePreference.getCustomAppProfile("search_history");
            if(!jsonStr.equals("")) {
                show(search_delete);
            }else {
                SAToast.makeText(Search_Activity.this,"暂无历史记录").show();
            }
        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        int i = view.getId();
        if (i == R.id.search_edit) {
            data.clear();
            adapter.refresh(data,edit_search);
        }
    }

    /*
    * 患者 0
    * 随访任务 1
    * */
    private void instart_thread(){
        RestClent.builder()
                .url("patients")
                .loader(this)
                .params("region_id",fristBean.getRegion_id())
                .params("gov_id",fristBean.getGov_id())
                .params("doctor_id",userBean.getId())
                .params("conditions",edit_search.getText().toString())
                .params("next_start", "")
                .params("item_count",10)
                .params("type",mSearchtype)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {

                         int code = JSON.parseObject(response).getInteger("code");
                        System.out.println("搜索返回的至***********"+response);
                        if(code == 200)
                        {
                            JSONObject object = JSON.parseObject(response).getJSONObject("obj");
                            JSONArray array = object.getJSONArray("list");
                            int size = array.size();
                            if(size<=0)
                            {
                                SAToast.makeText(Search_Activity.this,"暂时没有该患者!").show();
                            }
                            search_flage = 1;
                            data.clear();

                            data=new SearchNewDataConvert().setJsonData(response).CONVERT();
                            adapter.refresh(data,edit_search);
                            adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
                        }else if(code == 500){
                            SAToast.makeText(Search_Activity.this,"操作异常!").show();
                        }

                    }
                })
                .build()
                .post();
    }
    /**
     * 正则表达式：判断是否数字
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    public void show(View v){
        //实例化建造者
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置警告对话框的标题
        builder.setTitle("清除");
        //设置警告显示的图片
//        builder.setIcon(android.R.drawable.ic_dialog_alert);
        //设置警告对话框的提示信息
        builder.setMessage("是否要清空搜索历史？");
        //设置”正面”按钮，及点击事件
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    LattePreference.getDeleteAppProfile("search_history");//删除操作
                    data.clear();
                    adapter.refresh(data,edit_search);
                    SAToast.makeText(Search_Activity.this,"也清空历史记录").show();
            }
        });
        //设置“反面”按钮，及点击事件
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });
        //设置“中立”按钮，及点击事件
        builder.setNeutralButton("等等看吧", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        //显示对话框
        builder.show();
    }
}