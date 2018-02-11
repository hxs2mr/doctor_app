package com.netease.nim.uikit.bootuch.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;

import com.creative.base.BaseDate.Wave;
import com.netease.nim.uikit.bootuch.recvdata.StaticReceive;


/**
 * @deprecated
 */
public class DrawThreadPC80B extends BaseDraw {

	/** 接收到蓝牙消息——PC80B发送文件数据 */
	public static final int MSG_80B_FILE = 0x201;
	/** 接收到蓝牙消息——PC80B发送实时波形数据 */
	public static final int MSG_80B_WAVE = 0x202;

	/** 当前view的高度 (mm) */
	private float heightMm = 0;
	/** 心电波形高度缩放比例 */
	private float zoomECGforMm = 0.0f;
	/** 当前波形增益 */
	protected int gain = 2;
	private CornerPathEffect cornerPathEffect = new CornerPathEffect(20);

	public DrawThreadPC80B(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public DrawThreadPC80B(Context context) {
		super(context);
	}

	public DrawThreadPC80B(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 恢复读取数据线程
	 */
	public synchronized void Continue() {
		this.pause = false;
		StaticReceive.DRAWDATA.clear();
		this.notify();
	}

	@Override
	public void run() {
		super.run();
		synchronized (this) {
			StaticReceive.DRAWDATA.clear();
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
						if (StaticReceive.DRAWDATA.size() > 20) {
							Thread.sleep(5);
						} else {
							Thread.sleep(10);
						}
					} else {
						Thread.sleep(100);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			StaticReceive.DRAWDATA.clear();
			cleanWaveData();
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		paint.setPathEffect(cornerPathEffect);
		paint.setColor(Color.RED);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(dm.density);
		Path path = new Path();
		path.moveTo(0, gethPx(data2draw[0]));
		for (int i = 0; i < data2draw.length; i++) {
			path.lineTo(i * stepx, gethPx(data2draw[i]));
		}
		canvas.drawPath(path, paint);
		paint.setColor(Color.WHITE);
		paint.setStrokeWidth(5);
		canvas.drawLine(arraycnt * stepx - 1, 0, arraycnt * stepx - 1, height,
				paint);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		heightMm = BackGround.fPXgetMMforY(h);
		zoomECGforMm = (float) (BackGround.gridHeigh / 416f);
	}

	/**
	 * 获取该点在Y轴上的像素坐标
	 */
	private float gethPx(int data) {
		return BackGround.fMMgetPxfory(gethMm(data));
	}

	/**
	 * 获取该点在Y轴上的mm坐标
	 */
	protected float gethMm(int data) {
		data -= 2048;
		return heightMm / 2 - zoomECGforMm * (data * gain);
	}

	/**
	 * 设置波形增益
	 * 
	 * @param gain
	 */
	public void setGain(int gain) {
		this.gain = gain == 0 ? 2 : gain;
	}

}
