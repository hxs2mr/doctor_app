package microtech.hxswork.com.frame_core.util;

import android.content.Context;
import android.widget.Toast;

/**
 * 代替系统的Toast,解决用户关闭通知不显示
 * 
 * @author Lucio
 * 
 */
public class SAToast {

	private Context mContext;
	public static final int LENGTH_SHORT = 1500;
	public static final int LENGTH_LONG = 3000;
	private static Toast ts;

	public SAToast(Context context) {
		mContext = context.getApplicationContext();
	}

	public static Toast makeText(Context context, CharSequence text) {
		return makeText(context, text, LENGTH_SHORT);
	}

	public static Toast makeText(Context context, CharSequence text,
								 int duration) {
		if (ts == null) {
			ts = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		}
		ts.setText(text);
		return  ts;
	}



	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}
}
