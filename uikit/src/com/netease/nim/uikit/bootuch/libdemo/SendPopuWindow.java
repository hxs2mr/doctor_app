package com.netease.nim.uikit.bootuch.libdemo;

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

import com.netease.nim.uikit.R;


/**
 * Created by microtech on 2017/12/20.
 */

public class SendPopuWindow extends PopupWindow {
    private AppCompatTextView t1,t2,s1,s2;
    private AppCompatTextView many_cancel,many_ok;
    private View mMenuView;
    @SuppressLint("InflateParams")
    public SendPopuWindow(Context context, View.OnClickListener itemsOnClick, final Activity activity) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.sendpop, null);

        t1 = mMenuView.findViewById(R.id.send_t1);
        t2 = mMenuView.findViewById(R.id.send_t2);
        s1 = mMenuView.findViewById(R.id.send_cancel);
        s2 = mMenuView.findViewById(R.id.send_ok);

        s1.setOnClickListener(itemsOnClick);
        s2.setOnClickListener(itemsOnClick);

        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha=0.5f;
        activity.getWindow().setAttributes(lp);

        this.setContentView(mMenuView);
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.PopupAnimation);
        ColorDrawable dw = new ColorDrawable(0x80000000);
        this.setBackgroundDrawable(dw);
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                lp.alpha=1.0f;
                activity.getWindow().setAttributes(lp);
            }
        });

    }
}
