package com.netease.nim.uikit.bootuch.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.creative.base.BaseDate.Wave;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.bootuch.recvdata.StaticReceive;


public class DrawPC300SPO2Rect extends View implements Runnable {

	/**
	 * 血氧柱状图
	 */
	private RectF spoRect;

	private Paint mPaint = new Paint();

	private DisplayMetrics dm;

	/**
	 * 血氧数据缩放比例
	 */
	private float scaleSPO = 0.0f;

	/** 当前血氧值 */
	private int spo = 0;

	protected boolean stop = false;
	protected boolean pause = false;

	public DrawPC300SPO2Rect(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public DrawPC300SPO2Rect(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public DrawPC300SPO2Rect(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		WindowManager wmManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		dm = new DisplayMetrics();
		wmManager.getDefaultDisplay().getMetrics(dm);
		mPaint.setAntiAlias(true);
		mPaint.setStrokeWidth(dm.density * 3);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		spoRect = new RectF(0, 0, w, h);
		scaleSPO = spoRect.height() / 127f;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (spoRect != null) {
			mPaint.setColor(getResources().getColor(R.color.data_spo2));
			mPaint.setStyle(Style.STROKE);
			canvas.drawRect(spoRect, mPaint);
			DrawSpo(canvas);
		}
	}

	private void DrawSpo(Canvas canvas) {
		mPaint.setColor(Color.rgb(0x03, 0x87, 0x06));
		mPaint.setStyle(Style.FILL);
		canvas.drawRect(spoRect.left + 5, getSPO(spo) - 5, spoRect.right - 5,
				spoRect.bottom - 5, mPaint);
	}

	/**
	 * 计算 血氧数据的绘制高度
	 */
	private float getSPO(int d) {
		return spoRect.bottom - scaleSPO * d;
	}

	@Override
	public void run() {
		synchronized (this) {
			while (!stop) {
				try {
					if (pause) {
						this.wait();
					}
					if (StaticReceive.SPOWAVE.size() > 0) {
						Wave data = StaticReceive.SPOWAVE.remove(0);
						this.spo = data.data;
						postInvalidate();
						if (StaticReceive.SPOWAVE.size() > 25) {
							Thread.sleep(17);
						} else {
							Thread.sleep(20);
						}
					} else {
						Thread.sleep(100);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void Stop() {
		this.stop = true;
	}

	public void Pause() {
		this.pause = true;
	}

	public boolean isPause() {
		return this.pause;
	}

	public boolean isStop() {
		return this.stop;
	}

	public synchronized void Continue() {
		this.pause = false;
		this.notify();
	}

}
