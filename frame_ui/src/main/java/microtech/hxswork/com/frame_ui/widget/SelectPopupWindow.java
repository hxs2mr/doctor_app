package microtech.hxswork.com.frame_ui.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;


import java.util.Calendar;
import java.util.TimeZone;

import microtech.hxswork.com.frame_ui.R;


/**
 * 关注pop弹窗窗体
 */
public class SelectPopupWindow extends PopupWindow {

	private TextView tv_text,tv_photo,tv_speech,tv_video, tv_follow;
	private ImageView iv_cancle;
	private View mMenuView;

	@SuppressLint("InflateParams")
	public SelectPopupWindow(Context context, OnClickListener itemsOnClick, final Activity activity) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.layout_dialog_send, null);
		tv_text = mMenuView.findViewById(R.id.tv_send_text);
		tv_photo = mMenuView.findViewById(R.id.tv_send_photo);

		tv_speech =  mMenuView.findViewById(R.id.tv_send_speech);
		tv_video =  mMenuView.findViewById(R.id.tv_send_video);

		tv_follow =mMenuView.findViewById(R.id.tv_send_follow);
		iv_cancle = mMenuView.findViewById(R.id.patient_next_cancel);

/*
		Calendar calendar=Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		tv_day.setText(calendar.get(Calendar.DAY_OF_MONTH)+"");
		tv_year.setText((calendar.get(Calendar.MONTH)+1)+"/"+calendar.get(Calendar.YEAR));
		String week=String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));

		if("1".equals(week)){
			week ="天";
		}else if("2".equals(week)){
			week ="一";
		}else if("3".equals(week)){
			week ="二";
		}else if("4".equals(week)){
			week ="三";
		}else if("5".equals(week)){
			week ="四";
		}else if("6".equals(week)){
			week ="五";
		}else if("7".equals(week)){
			week ="六";
		}
		tv_week.setText("星期"+week);*/

		tv_text.setOnClickListener(itemsOnClick);
		tv_photo.setOnClickListener(itemsOnClick);
		tv_speech.setOnClickListener(itemsOnClick);
		tv_video.setOnClickListener(itemsOnClick);
		tv_follow.setOnClickListener(itemsOnClick);
		iv_cancle.setOnClickListener(itemsOnClick);

		tv_text.startAnimation(AnimationUtils.loadAnimation(context, R.anim.l1));//加载动画
		tv_photo.startAnimation(AnimationUtils.loadAnimation(context, R.anim.l2));//加载动画
		tv_speech.startAnimation(AnimationUtils.loadAnimation(context, R.anim.l3));//加载动画
		tv_video.startAnimation(AnimationUtils.loadAnimation(context, R.anim.l4));//加载动画
		tv_follow.startAnimation(AnimationUtils.loadAnimation(context, R.anim.l5));//加载动画

		iv_cancle.startAnimation(AnimationUtils.loadAnimation(context, R.anim.xuan));//加载动画

		WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
		lp.alpha=0.5f;
		activity.getWindow().setAttributes(lp);

		this.setContentView(mMenuView);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		this.setAnimationStyle(R.style.PopupAnimation);
		ColorDrawable dw = new ColorDrawable(0x80000000);
		this.setBackgroundDrawable(dw);
		mMenuView.setOnTouchListener(new OnTouchListener() {
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
