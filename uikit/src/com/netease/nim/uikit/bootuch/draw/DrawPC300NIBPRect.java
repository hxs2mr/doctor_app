package com.netease.nim.uikit.bootuch.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.netease.nim.uikit.R;


public class DrawPC300NIBPRect extends View {

	private Paint mPaint = new Paint();
	private DisplayMetrics dm;
	private int height, width;

	/** 血压等级颜色定义 */
	private int[] nibpGradeColor = {
			getResources().getColor(R.color.color_main_histogram1),
			getResources().getColor(R.color.color_main_histogram2),
			getResources().getColor(R.color.color_main_histogram3),
			getResources().getColor(R.color.color_main_histogram4),
			getResources().getColor(R.color.color_main_histogram5),
			getResources().getColor(R.color.color_main_histogram6) };

	/** 血压等级标记 */
	private String[] nibpGradeString;

	/** 当前袖带压力值 */
	private int nibp = 0;

	/** 血压柱子的间隔 */
	private int stepx = 0;

	/** 每个柱子的宽度 */
	private int rectW = 0;

	/** 血压数据缩放比例 */
	private float scaleNIBP = 0.0f;

	/** 是否是测量结果 */
	private boolean isResult = false;

	public DrawPC300NIBPRect(Context context) {
		super(context);
		init(context);
	}

	public DrawPC300NIBPRect(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public DrawPC300NIBPRect(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		WindowManager wmManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		dm = new DisplayMetrics();
		wmManager.getDefaultDisplay().getMetrics(dm);
		mPaint.setAntiAlias(true);
		if(isInEditMode()){
			return;
		}
		nibpGradeString = getResources().getStringArray(R.array.bp_rank);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		height = h;
		width = w;
		stepx = (int) (dm.density * 5);
		if(isInEditMode()){
			return;
		}
		rectW = (width - stepx * nibpGradeColor.length) / nibpGradeColor.length;
		scaleNIBP = (float) (rectW / 50f);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawBG(canvas);
		drawData(canvas);
	}

	private void drawBG(Canvas canvas) {
		for (int i = 0; i < nibpGradeColor.length; i++) {
			Rect r = new Rect(0, 0, 0, height);
			r.left = i * (rectW + stepx);
			r.right = r.left + rectW;
			mPaint.setColor(nibpGradeColor[i]);
			canvas.drawRect(r, mPaint);
		}
	}

	private void drawData(Canvas canvas) {
		if (!isResult) {
			mPaint.setColor(Color.argb(0xbb, 0xff, 0xff, 0xff));
			float data = getNIBP(nibp);
			int stepCnt = (int) (data / rectW);
			RectF r = new RectF();
			r.left = 0;
			r.right = (rectW + stepx) * stepCnt - stepx + data % rectW;
			r.top = 0;
			r.bottom = height;
			canvas.drawRect(r, mPaint);
		} else {
			mPaint.setColor(Color.WHITE);
			mPaint.setTextSize(dm.density * 15);
			if (nibp > 0) {
				nibp--;
			}
			String msg = nibpGradeString[nibp];
			float x = (((rectW + stepx) * nibp) + (rectW / 2))
					- (mPaint.measureText(msg) / 2);
			FontMetrics fmt = mPaint.getFontMetrics();
			canvas.drawText(msg, x, height - (fmt.descent - fmt.ascent) / 2,
					mPaint);
		}
	}

	/** 计算 血压数据的绘制宽度 */
	private float getNIBP(int d) {
		return scaleNIBP * d;
	}

	/**
	 * 设置当前血压值
	 * 
	 * @param nibp
	 *            袖袋压力值
	 * @param isResult
	 *            是否为测量结果
	 */
	public void setNIBP(int nibp, boolean isResult) {
		if (this.isResult == isResult && this.nibp == nibp)
			return;
		this.isResult = isResult;
		this.nibp = nibp;
		postInvalidate();
	}
}
