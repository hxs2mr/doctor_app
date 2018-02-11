package microtech.hxswork.com.frame_ui.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import microtech.hxswork.com.frame_ui.R;

/**
 * Created by microtech on 2017/12/11.
 */

public class SelectDataPopuple extends PopupWindow {
    TextView open_photo,open_caram,open_back;
    private View mMenuView;

    @SuppressLint("InflateParams")
    public SelectDataPopuple(Context context, View.OnClickListener itemsOnClick, final Activity activity) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.head_image_popu, null);

        open_photo = mMenuView.findViewById(R.id.open_image);
        open_caram = mMenuView.findViewById(R.id.open_caram);

        open_back =  mMenuView.findViewById(R.id.open_back);

        open_photo.setOnClickListener(itemsOnClick);
        open_caram.setOnClickListener(itemsOnClick);
        open_back.setOnClickListener(itemsOnClick);


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
  /*      mMenuView.setOnTouchListener(new View.OnTouchListener() {
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
