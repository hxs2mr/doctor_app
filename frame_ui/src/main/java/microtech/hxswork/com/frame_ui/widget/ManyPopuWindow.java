package microtech.hxswork.com.frame_ui.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.PopupWindow;

import microtech.hxswork.com.frame_ui.R;

/**
 * Created by microtech on 2017/12/20.
 */

public class ManyPopuWindow extends PopupWindow {
    private CheckBox c1,c2,c3,c4;
    private AppCompatTextView many_cancel,many_ok;
    private View mMenuView;
    @SuppressLint("InflateParams")
    public ManyPopuWindow(Context context, View.OnClickListener itemsOnClick, final Activity activity) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.many_check_pop, null);

        c1 = mMenuView.findViewById(R.id.cb01);
        c2 = mMenuView.findViewById(R.id.cb02);
        c3 = mMenuView.findViewById(R.id.cb03);
        c4 = mMenuView.findViewById(R.id.cb04);
        many_cancel = mMenuView.findViewById(R.id.many_cancel);
        many_ok = mMenuView.findViewById(R.id.many_ok);

        c1.setOnClickListener(itemsOnClick);
        c2.setOnClickListener(itemsOnClick);
        c3.setOnClickListener(itemsOnClick);
        c4.setOnClickListener(itemsOnClick);
        many_cancel.setOnClickListener(itemsOnClick);
        many_ok.setOnClickListener(itemsOnClick);

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
        mMenuView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            @SuppressLint("ClickableViewAccessibility")
            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {

                        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                        lp.alpha=1.0f;
                        activity.getWindow().setAttributes(lp);
                        dismiss();
                    }
                }
                return true;
            }
        });
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
