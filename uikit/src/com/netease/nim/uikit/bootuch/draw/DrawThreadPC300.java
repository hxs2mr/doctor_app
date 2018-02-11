package com.netease.nim.uikit.bootuch.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;

import com.creative.base.BaseDate.Wave;
import com.netease.nim.uikit.bootuch.recvdata.StaticReceive;


/**
 * 绘制PC300界面的实时波形
 * 
 * draw by view
 * @author zougy
 * 
 */
public class DrawThreadPC300 extends BaseDraw {

	/** 血氧波形高度的最大值 */
	private final int ySpo2Max = 130;
	/** 心电波形高度缩放比例 */
	private float zoomECGforMm = 0.0f;
	/** 当前波形增益 */
	protected int gain = 2;
	/** 血氧波形高度缩放比例 */
	private float zoomSpo2 = 0.0f;
	/** 当前view的高度 (mm) */
	private float heightMm = 0;
	private String msg;

	public DrawThreadPC300(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public DrawThreadPC300(Context context) {
		super(context);
	}

	public DrawThreadPC300(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public synchronized void Continue() {
		super.Continue();
		cleanWaveData();
	}

	@Override
	public void cleanWaveData() {
		StaticReceive.DRAWDATA.clear();
		StaticReceive.SPOWAVE.clear();
		super.cleanWaveData();
	}

	@Override
	public void run() {
		super.run();
		synchronized (this) {
			while (!stop) {
				try {
					if (pause) {
						this.wait();
					}
					if (StaticReceive.DRAWDATA.size() > 0) {
						Wave data = StaticReceive.DRAWDATA.remove(0);
						addData(data.data);
						if (data.flag == 1) {
							mHandler.sendEmptyMessage(StaticReceive.MSG_DATA_PULSE);
						}
						if (!StaticReceive.isECGData) {
							if (StaticReceive.DRAWDATA.size() > 20) {
								Thread.sleep(18);
							} else {
								Thread.sleep(25);
							}
						} else {
							if (StaticReceive.DRAWDATA.size() > 25) {
								Thread.sleep(6);
							} else {
								Thread.sleep(9);
							}
						}
					} else {
						Thread.sleep(500);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			cleanWaveData();
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(isInEditMode()){
			return;
		}
		
		if (msg != null && !msg.equals(""))
			drawMsg(canvas);
		paint.setPathEffect(cornerPathEffect);
		paint.setStyle(Style.STROKE);
		paint.setColor(Color.RED);
		paint.setStrokeWidth(dm.density);
		Path path = new Path();
		path.moveTo(0, gethPx(data2draw[0]));
		for (int i = 0; i < data2draw.length; i++) {
			path.lineTo(i * stepx, gethPx(data2draw[i]));
		}
		canvas.drawPath(path, paint);
		paint.setColor(Color.WHITE);
		paint.setStrokeWidth(5);
		
		canvas.drawLine(arraycnt * stepx, 0, arraycnt * stepx, height, paint);
	}

	private void drawMsg(Canvas canvas) {
		Paint mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setStrokeWidth(dm.density * 2);
		mPaint.setColor(Color.BLACK);
		mPaint.setTextSize(dm.density * 20);
		canvas.drawText(msg, (weight - mPaint.measureText(msg)) / 2, height / 2, mPaint);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		zoomECGforMm = (float) (BackGround.gridHeigh / 114.3f);// 114.3f ,394f
		zoomSpo2 = (float) (height / ySpo2Max);
		heightMm = BackGround.fPXgetMMforY(BackGround.mHeight);
		
	}

	/**
	 * 获取该点在Y轴上的像素坐标
	 */
	private float gethPx(int data) {
		if (StaticReceive.isECGData) {
			return gethMm(data);
		} else {
			return height - zoomSpo2 * data;
		}
	}
	

	/**
	 * 获取该点在Y轴上的mm坐标
	 */
	private float gethMm(int data) {
		// System.out.println("gethMm:" + data + " " + gain + " " +
		// StaticReceive.is128);
		float d = 0;
		if (StaticReceive.is128) { //wave Y nMax = 255
			data -= 128;			
			//d = heightMm / 2 - zoomECGforMm * (data * gain + 2048);
			//d = height - (data * gain + 128) / 256f * height;
									
			d = heightMm / 2 - zoomECGforMm * (data * gain);
			return BackGround.fMMgetPxfory(d);				
		} else { //wave Y nMax = 4095
			data -= 2048;
			d = height - (data * gain + 2048) / 4096f * height;
		}
		
		//System.out.println("height:" + height + "    d:" + d + "  " + data);
		return d;
		
		//return BackGround.fMMgetPxfory(d);
	}

	/**
	 * 设置波形增益
	 * 
	 * @param gain
	 */
	public void setGain(int gain) {
		this.gain = gain == 0 ? 2 : gain;
	}

	public void drawMsg(String msg) {
		this.msg = msg;
		postInvalidate();
	}
}
