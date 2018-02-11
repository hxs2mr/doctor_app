package microtech.hxswork.com.frame_ui.main.Patient;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import javax.crypto.spec.DESedeKeySpec;

import microtech.hxswork.com.frame_core.ui.recyclew.ItemType;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipViewHolder;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleFields;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleItemEntity;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleRecyclerAdapter;
import microtech.hxswork.com.frame_ui.R;

/**
 * Created by microtech on 2017/12/14.
 */

public class PatientFollowAdapter extends MultipleRecyclerAdapter {
    boolean isOpen = true;
    private int mHiddenViewMeasuredHeight;
    Activity mActivity = null;
    private float mDensity;
    protected PatientFollowAdapter(List<MultipleItemEntity> data,Activity activity) {
        super(data);
        this.mActivity = activity;
        addItemType(PatientItemType.PATIENT_FOLLOW, R.layout.patient_follow_item);
        openLoadAnimation(BaseQuickAdapter.ALPHAIN);

    }

    @Override
    protected void convert(MultipViewHolder helper, MultipleItemEntity item) {
        super.convert(helper, item);
        switch (helper.getItemViewType())
        {
            case PatientItemType.PATIENT_FOLLOW:
                LinearLayoutCompat patinent_follow_title_linear =  helper.getView(R.id.patinent_follow_title_linear);//标题
                final LinearLayoutCompat patient_follow_item_linear =  helper.getView(R.id.patient_follow_item_linear);//其余信息

                patient_follow_item_linear.setVisibility(View.GONE);
                AppCompatTextView title = helper.getView(R.id.item_title);
                AppCompatTextView item_time = helper.getView(R.id.item_time);
                final ImageView image_xia = helper.getView(R.id.item_xia);

                AppCompatTextView item_bin = helper.getView(R.id.item_bin);
                AppCompatTextView item_xiaoguo = helper.getView(R.id.item_xiaoguo);
                AppCompatTextView item_zhidao = helper.getView(R.id.item_zhidao);
                AppCompatTextView item_suif = helper.getView(R.id.item_suif);
                AppCompatTextView item_suip = helper.getView(R.id.item_suip);
                AppCompatTextView item_xueya = helper.getView(R.id.item_xueya);
                AppCompatTextView item_xueya_flage = helper.getView(R.id.item_xueya_flage);
                AppCompatTextView item_xuetan = helper.getView(R.id.item_xuetan);
                AppCompatTextView item_xuetan_flage = helper.getView(R.id.item_xuetan_flage);
                AppCompatTextView item_xueyan = helper.getView(R.id.item_xueyan);
                AppCompatTextView item_xueyan_flage = helper.getView(R.id.item_xueyan_flage);
                AppCompatTextView item_xinlu = helper.getView(R.id.item_xinlu);
                AppCompatTextView item_xinlu_flage = helper.getView(R.id.item_xinlu_flage);

                helper.setText(R.id.item_title, "第"+item.getField(MultipleFields.TITLE).toString()+"次随访");

                helper.setText(R.id.item_time, item.getField(MultipleFields.TIME).toString());

                helper.setText(R.id.item_bin, item.getField(MultipleFields.BIN).toString());

               // helper.setText(R.id.item_content, item.getField(MultipleFields.CONTENT).toString());

                helper.setText(R.id.item_xiaoguo, item.getField(MultipleFields.EFFECT).toString());
                helper.setText(R.id.item_zhidao, item.getField(MultipleFields.GUIDANCE).toString());
                helper.setText(R.id.item_suif, item.getField(MultipleFields.FOLLOW).toString());
                helper.setText(R.id.item_suip, item.getField(MultipleFields.FOLLOW_PEOPLE).toString());

              /*  helper.setText(R.id.item_xueya, item.getField(MultipleFields.PRESSURE).toString());血压数据模块
                helper.setText(R.id.item_xueya_flage, item.getField(MultipleFields.PRESSURE_FLAGE).toString());

                helper.setText(R.id.item_xuetan, item.getField(MultipleFields.SUGAR).toString());
                helper.setText(R.id.item_xuetan_flage, item.getField(MultipleFields.SUGAR_FLAGE).toString());
*/
                patient_follow_item_linear.measure(0,0);
//获取组件的高度
                int height=patient_follow_item_linear.getMeasuredHeight();//获取组件的高度
                // 获取布局的高度
                mHiddenViewMeasuredHeight =height;
                patinent_follow_title_linear.setOnClickListener(new View.OnClickListener() {
                    int durationMillis = 350;//动画持续时间
                    @Override
                    public void onClick(View view) {
                        if (patient_follow_item_linear.getVisibility()==View.VISIBLE) {
                            animateClose(patient_follow_item_linear);//关闭动画

                            RotateAnimation animation = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                            animation.setDuration(durationMillis);
                            animation.setFillAfter(true);
                            image_xia.startAnimation(animation);
                        } else {
                            animateOpen(patient_follow_item_linear);//打开动画
                            RotateAnimation animation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                            animation.setDuration(durationMillis);
                            animation.setFillAfter(true);
                            image_xia.startAnimation(animation);
                        }
                    }
                });
                break;
            default:
                break;

        }
    }

    @SuppressLint("NewApi")
    private void animateOpen(final View view) {
        view.setVisibility(View.VISIBLE);
        ValueAnimator animator = createDropAnimator(view, 0,
                mHiddenViewMeasuredHeight);
        animator.start();
    }

    private void animateClose(final View view) {
        int origHeight = view.getHeight();
        ValueAnimator animator = createDropAnimator(view, origHeight, 0);
        animator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                // 动画结束时隐藏view
                view.setVisibility(View.GONE);
            }
        });
        animator.start();
    }

    private ValueAnimator createDropAnimator(final View view, int start, int end) {
        // 创建一个数值发生器
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = value;
                view.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }
}
