package com.netease.nim.uikit.bootuch.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

public class BackGround extends View {

	public static DisplayMetrics dm;
	/** x轴上每个像素点占的实际mm长度 */
	public static float xPX2MMUnit = 0.0f;
	/** y轴上每个像素点占的实际mm长度 */
	public static float yPX2MMUnit = 0.0f;
	/** 当前view的宽度 (mm) */
	private float width = 0.0f;
	/** 当前view的高度 (mm) */
	public static float height = 0.0f;

	/** 背景标尺的最小高度（mm） */
	protected static final int minHeight = 4;

	/** 背景标尺的实际高度(mm) */
	public static float gridHeigh = 0.0f;

	/** 总共需要多少个网格 */
	public static int gridCnt = 0;

	/** 绘制区域的宽度(像素) */
	public static int mWidth = 0;
	/** 绘制区域的高度(像素) */
	public static int mHeight = 0;

	private int backgroundColor = 0;

	private Paint mPaint;

	public BackGround(Context context) {
		super(context);
		initScreen(context);
	}

	public BackGround(Context context, AttributeSet attrs) {
		super(context, attrs);
		initScreen(context);
	}

	public BackGround(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initScreen(context);
	}

	private void initScreen(Context context) {
		WindowManager wmManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		dm = new DisplayMetrics();
		wmManager.getDefaultDisplay().getMetrics(dm);
		xPX2MMUnit = 25.4f / dm.densityDpi;
		yPX2MMUnit = 25.4f / dm.densityDpi;
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setStrokeWidth(1);
		backgroundColor = Color.WHITE;
	}

	/**
	 * 设置背景颜色
	 */
	public void setBackgroundColor(int backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	private boolean isDrawBG = true;

	/**
	 * 设置是否绘制背景
	 * 
	 * @param isDrawBG
	 */
	public void setDrawBG(boolean isDrawBG) {
		this.isDrawBG = isDrawBG;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (!isDrawBG)
			return;
		canvas.drawColor(backgroundColor);
		if (gridCnt < 2) {
			return;
		}
		mPaint.setStrokeWidth(1);
		mPaint.setColor(Color.rgb(0xba, 0xba, 0xba));
		// 绘制纵坐标
		for (float i = 0; i < width; i += gridHeigh) {
			canvas.drawLine(fMMgetPxforX(i), 0, fMMgetPxforX(i), mHeight,
					mPaint);
		}

		int i = gridCnt / 2;
		for (int j = 0; j < i; j++) {
			canvas.drawLine(0, fMMgetPxfory(fPXgetMMforY(mHeight / 2)
					- gridHeigh * j), mWidth,
					fMMgetPxfory(fPXgetMMforY(mHeight / 2) - gridHeigh * j),
					mPaint);
		}

		for (int j = 0; j < i; j++) {
			canvas.drawLine(0, fMMgetPxfory(fPXgetMMforY(mHeight / 2)
					+ gridHeigh * j), mWidth,
					fMMgetPxfory(fPXgetMMforY(mHeight / 2) + gridHeigh * j),
					mPaint);
		}
		drawScale(canvas);
	}

	/**
	 * 是否绘制增益标尺
	 */
	private boolean isDrawScale = false;

	/**
	 * 设置是否绘制增益标尺
	 */
	public void setDrawScale(boolean isDrawScale) {
		this.isDrawScale = isDrawScale;
	}

	/** 绘制标尺 */
	private void drawScale(Canvas canvas) {
		if (gridHeigh > 1 && isDrawScale) {
			int h = mHeight / gridCnt;// 一格的高度
			mPaint.setColor(Color.BLUE);
			mPaint.setStrokeWidth(dm.density);
			float i = (h * gain) / 2f;
			canvas.drawLine(0, mHeight / 2 - i, h / 2, mHeight / 2 - i, mPaint);
			canvas.drawLine(0, mHeight / 2 + i, h / 2, mHeight / 2 + i, mPaint);
			canvas.drawLine(h / 4, mHeight / 2 - i, h / 4, mHeight / 2 + i,
					mPaint);
		}
	}

	private float gain = 2;

	public float getGain() {
		return gain;
	}

	public void setGain(float gain) {
		if (gain == 0) {
			this.gain = 0.5f;
		} else
			this.gain = gain;
		postInvalidate();
	}

	/**
	 * 通过mm单位获取该距离上X轴的像素点数
	 * 
	 * @param mm
	 * @return
	 */
	public static float fMMgetPxforX(float mm) {
		return mm / xPX2MMUnit;
	}

	/**
	 * 将指定的像素点数目得到这些像素点的mm长度（X轴）
	 * 
	 * @param px
	 * @return
	 */
	public static float fPXgetMMforX(int px) {
		return px * xPX2MMUnit;
	}

	/**
	 * 通过mm单位获取该距离上Y轴的像素点数
	 * 
	 * @param mm
	 * @return
	 */
	public static float fMMgetPxfory(float mm) {
		return mm / yPX2MMUnit;
	}

	/**
	 * 将指定的像素点数目得到这些像素点的mm长度（Y轴）
	 * 
	 * @param px
	 * @return
	 */
	public static float fPXgetMMforY(int px) {
		return px * yPX2MMUnit;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		setViewHeight(w, h);
	}

	public void setViewHeight(int w, int h) {
		// 将屏幕的像素都转换为毫米单位
		this.width = fPXgetMMforX(w);
		height = fPXgetMMforY(h);
		mHeight = h;
		mWidth = w;

		gridCnt = 6;
		gridHeigh = (float) (height / gridCnt);
		postInvalidate();
	}

	/** 获取总共需要多少个网格 */
	public int getGridCnt() {
		return gridCnt;
	}

	/** 设置总共需要多少个网格 */
	public void setGridCnt(int gridCnt) {
		BackGround.gridCnt = gridCnt;
	}

	/** 获取背景标尺的实际高度 (mm) */
	public float getGridHeigh() {
		return gridHeigh;
	}

	/** 获取背景标尺的实际高度 (mm) */
	public void setGridHeigh(float gridHeigh) {
		BackGround.gridHeigh = gridHeigh;
	}

}
