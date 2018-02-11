package com.netease.nim.uikit.bootuch.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

public class BaseDraw extends View implements Runnable {

	protected boolean stop = false;
	protected boolean pause = false;
	protected Handler mHandler;
	/** 保存一整屏数据的数组 */
	protected int[] data2draw;
	/** X轴上两点的间隔 */
	protected float stepx = 2;
	protected DisplayMetrics dm;
	/** 当前数组插入点 */
	protected int arraycnt = 0;
	/** 当前view的高度 (px) */
	protected float height = 0;

	protected float weight = 0;

	protected Paint paint;

	protected CornerPathEffect cornerPathEffect = new CornerPathEffect(20);

	/**
	 * 设置前景色
	 */
	public void setcForecolor(int cForecolor) {
	}

	public BaseDraw(Context context) {
		super(context);
		init(context);
	}

	public BaseDraw(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public BaseDraw(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public BaseDraw(Context context, int w, int h) {
		super(context);
	}

	private void init(Context context) {
		WindowManager wmManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		dm = new DisplayMetrics();
		wmManager.getDefaultDisplay().getMetrics(dm);
		paint = new Paint();
		paint.setAntiAlias(true);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		stepx = dm.density;
		height = h;
		weight = w;
		if(isInEditMode()){
			return;
		}
		data2draw = new int[(int) (w / stepx)];
		for (int i = 0; i < data2draw.length; i++) {
			data2draw[i] = -1;
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

	public void setmHandler(Handler mHandler) {
		this.mHandler = mHandler;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

	@Override
	public void run() {

	}

	/**
	 * 将需要绘制的数据添加到数组中
	 * 
	 * @param data
	 */
	public void addData(int data) {
		if (data2draw != null) {
			data2draw[arraycnt] = data;
			arraycnt = (arraycnt + 1) % data2draw.length;
			postInvalidate();
		}
	}

	/**
	 * 清除绘制的波形
	 */
	public void cleanWaveData() {
		if (data2draw == null)
			return;
		arraycnt = 0;
		for (int i = 0; i < data2draw.length; i++) {
			data2draw[i] = -1;
		}
		postInvalidate();
	}

}
