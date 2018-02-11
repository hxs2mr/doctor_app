package microtech.hxswork.com.frame_ui.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;
import android.widget.TextView;

import microtech.hxswork.com.frame_ui.R;

/**
 * Created by microtech on 2017/12/11.
 */

public class SelectSharePopup extends PopupWindow {
    LinearLayoutCompat wechat_linear,qq_linear,wechat_com_linear;
    AppCompatTextView cancel;
    private View mMenuView;

    @SuppressLint({"InflateParams", "WrongViewCast"})
    public SelectSharePopup(Context context, View.OnClickListener itemsOnClick, final Activity activity) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.bisniss_share_pop, null);

        wechat_linear = mMenuView.findViewById(R.id.wechat_linear);
        qq_linear = mMenuView.findViewById(R.id.qq_linear);
        wechat_com_linear =  mMenuView.findViewById(R.id.wechat_com_linear);
        cancel =  mMenuView.findViewById(R.id.cancle);

        wechat_com_linear.setOnClickListener(itemsOnClick);
        wechat_linear.setOnClickListener(itemsOnClick);
        qq_linear.setOnClickListener(itemsOnClick);
        cancel.setOnClickListener(itemsOnClick);
        mMenuView.setOnClickListener(itemsOnClick);

        wechat_linear.startAnimation(AnimationUtils.loadAnimation(context, R.anim.l1));//加载动画
        qq_linear.startAnimation(AnimationUtils.loadAnimation(context, R.anim.l2));//加载动画
        wechat_com_linear.startAnimation(AnimationUtils.loadAnimation(context, R.anim.l3));//加载动画

        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha=0.5f;
        activity.getWindow().setAttributes(lp);

        this.setContentView(mMenuView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.PopupAnimation);
        ColorDrawable dw = new ColorDrawable(0x80000000);
        this.setBackgroundDrawable(dw);
/*        mMenuView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            @SuppressLint("ClickableViewAccessibility")
            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                    if (y < height) {

                        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                        lp.alpha=1.0f;
                        activity.getWindow().setAttributes(lp);
                        dismiss();
                    }
                return true;
            }
        });*/
        this.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                lp.alpha=1.0f;
                activity.getWindow().setAttributes(lp);
            }
        });

    }
}
