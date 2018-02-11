package microtech.hxswork.com.frame_ui.main.Patient;

import android.app.Activity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import microtech.hxswork.com.frame_core.ui.recyclew.ItemType;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipViewHolder;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleFields;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleItemEntity;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleRecyclerAdapter;
import microtech.hxswork.com.frame_ui.R;
import microtech.hxswork.com.frame_ui.photoAdapter.PhotoAdapter;
import microtech.hxswork.com.frame_ui.photoAdapter.RecyclerItemClickListener;
import microtech.hxswork.com.photopicker.PhotoPreview;

/**
 * Created by microtech on 2017/12/8.
 */

public class PatientNextAdapter extends MultipleRecyclerAdapter{

    public  static  ArrayList<String> selectedPhotos=new ArrayList<>();
    public  PhotoAdapter photoAdapter;
    Activity mActivity;


    public void add(int i,int layoutResId ) {
        addItemType(i,layoutResId);//删除
        // notifyDataSetChanged();
    }


    protected PatientNextAdapter(List<MultipleItemEntity> data,Activity activity) {
        super(data);
        this.mActivity = activity;
        addItemType(ItemType.TEXT, R.layout.patient_item_text);//文本布局
        addItemType(ItemType.IMAGE, R.layout.patient_item_image);//图片布局
        addItemType(ItemType.VOICE, R.layout.patient_item_recode);//语音布局
        openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
    }
    @Override
    protected void convert(MultipViewHolder helper, MultipleItemEntity item) {
        ArrayList<String> images =new ArrayList<>();;
        switch (helper.getItemViewType()) {
            case ItemType.TEXT://文本
               final AppCompatTextView time = helper.getView(R.id.patietn_text_time);
                final AppCompatTextView data = helper.getView(R.id.patietn_text_data);
                time.setText(item.getField(MultipleFields.TIME).toString());
                data.setText(item.getField(MultipleFields.TEXT).toString());

                break;
            case ItemType.IMAGE://图片

                final AppCompatTextView i_time = helper.getView(R.id.patient_image_time);
                final AppCompatTextView i_title = helper.getView(R.id.patient_image_title);
                final RecyclerView i_recycle = helper.getView(R.id.patient_image_recycle);
                i_time.setText(item.getField(MultipleFields.TIME).toString());
                i_title.setText(item.getField(MultipleFields.TITLE).toString());
                images = item.getField(MultipleFields.IMAGE_ARRAY);

                photoAdapter = new PhotoAdapter(mContext,images,0);
                i_recycle.setLayoutManager(new StaggeredGridLayoutManager(4, OrientationHelper.VERTICAL));//设置recycleview的布局

                i_recycle.setAdapter(photoAdapter);
                final ArrayList<String> finalImages = images;
                i_recycle.addOnItemTouchListener(new RecyclerItemClickListener(mContext, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //表示图片的查看
                        PhotoPreview.builder()
                                .setPhotos(finalImages)
                                .setCurrentItem(position)
                                .start(mActivity);

                    }
                }));

                break;
            case ItemType.VOICE://语音
                final AppCompatTextView v_time = helper.getView(R.id.patient_voice_time);
                final AppCompatTextView v_title = helper.getView(R.id.patient_voice_title);
                v_time.setText(item.getField(MultipleFields.TIME).toString());
                v_title.setText(item.getField(MultipleFields.TITLE).toString());
                break;
        }
    }
}
