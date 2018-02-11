package microtech.hxswork.com.frame_core.middle.bottom;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.joanzapata.iconify.widget.IconTextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import me.yokeyword.fragmentation.ISupportFragment;
import microtech.hxswork.com.frame_core.R;
import microtech.hxswork.com.frame_core.middle.MiddleFragment;

/**
 * Created by microtech on 2017/11/17.底部图标容器
 */

public  abstract  class BaseBottomFragment extends MiddleFragment implements View.OnClickListener {

    private final ArrayList<BottomItemFragment> ITEM_FRAGMENT=new ArrayList<>();
    private final ArrayList<BottomTabBean> TAB_BEANS = new ArrayList<>();
    private final LinkedHashMap<BottomTabBean,BottomItemFragment> ITEMS= new LinkedHashMap<>();
    private  int mCurrentfragment = 0 ;

    private int mIndexFragment =0;
    private int mClickedColor= Color.RED;

    public abstract LinkedHashMap<BottomTabBean,BottomItemFragment> setItems(ItemBuilder builder);

    public abstract int setIndexFragment();

    LinearLayoutCompat mBottomBarLinear =null;//= bind(R.id.bottom_bar);
    @Override
    public Object setLayout() {
        return R.layout.fragment_botom;
    }

    @ColorInt
    public abstract int setClickedColor();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIndexFragment = setIndexFragment();
        if(setClickedColor() !=0){
            mClickedColor = setClickedColor();
        }
        final  ItemBuilder builder = ItemBuilder.builder();
        final  LinkedHashMap<BottomTabBean,BottomItemFragment> items =setItems(builder);
        ITEMS.putAll(items);
        for(Map.Entry<BottomTabBean,BottomItemFragment> item:ITEMS.entrySet()){
            final  BottomTabBean key = item.getKey();
            final BottomItemFragment value = item.getValue();
            TAB_BEANS.add(key);
            ITEM_FRAGMENT.add(value);
        }
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        mBottomBarLinear = bind(R.id.bottom_bar);
        final  int size = ITEMS.size();
        for(int i = 0; i < size;i++){
            LayoutInflater.from(getContext()).inflate(R.layout.bottom_text_top_icon_layout,mBottomBarLinear);
            final RelativeLayout item = (RelativeLayout) mBottomBarLinear.getChildAt(i);
            //设置每个item的点击事件
            item.setTag(i);
            item.setOnClickListener(this);
            final IconTextView itemIcon = (IconTextView) item.getChildAt(0);
            final AppCompatTextView itemTitle = (AppCompatTextView) item.getChildAt(1);
            final BottomTabBean bean = TAB_BEANS.get(i);
            //初始化数据
            itemIcon.setText(bean.getICON());
            itemTitle.setText(bean.getTITLE());
            if(i == mIndexFragment){
                itemIcon.setTextColor(mClickedColor);
                itemTitle.setTextColor(mClickedColor);
            }
        }
        final ISupportFragment[] fragmentArray = ITEM_FRAGMENT.toArray(new ISupportFragment[size]);
        getSupportDelegate().loadMultipleRootFragment(R.id.bottom_fragment_continuer,mIndexFragment,fragmentArray);

    }

    private void resetColor(){
        final  int count = mBottomBarLinear.getChildCount();
        for(int i =0 ; i < count; i ++){
            final  RelativeLayout item = (RelativeLayout) mBottomBarLinear.getChildAt(i);
            final  IconTextView itemIcon = (IconTextView) item.getChildAt(0);
            itemIcon.setTextColor(Color.GRAY);
            final AppCompatTextView itemTitle = (AppCompatTextView) item.getChildAt(1);
            itemTitle.setTextColor(Color.GRAY);
        }

    }
    @Override
    public void onClick(View view) {
        final  int tag = (int) view.getTag();
        resetColor();
        final RelativeLayout item = (RelativeLayout) view;
        final  IconTextView itemIcon = (IconTextView) item.getChildAt(0);
        itemIcon.setTextColor(mClickedColor);
        final  AppCompatTextView itemTitle = (AppCompatTextView) item.getChildAt(1);
        itemTitle.setTextColor(mClickedColor);

        //注意先后的顺序
        getSupportDelegate().showHideFragment(ITEM_FRAGMENT.get(tag),ITEM_FRAGMENT.get(mCurrentfragment));
        mCurrentfragment=tag;
    }
}
