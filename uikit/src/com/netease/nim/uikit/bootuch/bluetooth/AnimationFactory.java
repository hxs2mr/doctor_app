package com.netease.nim.uikit.bootuch.bluetooth;

import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/**
 * 定义几个常用的动画
 * 
 * @author zougy
 * 
 */
public class AnimationFactory {

	private static final long STARTOFFSET = 200;

	/**
	 * 从左边进入的动画
	 * 
	 * @param startOffset
	 *            开始时的延迟时间
	 * @return
	 */
	public static Animation LeftIn(long startOffset) {
		Animation a = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		a.setDuration(STARTOFFSET);
		a.setStartOffset(startOffset);
		return a;
	}

	/**
	 * 从左边出去的动画
	 * 
	 * @param startOffset
	 *            开始时的延迟时间
	 * @return
	 */
	public static Animation LeftOut(long startOffset) {
		Animation a = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		a.setDuration(STARTOFFSET);
		a.setStartOffset(startOffset);
		return a;
	}

	/**
	 * 从右边进入的动画
	 * 
	 * @param startOffset
	 *            开始时的延迟时间
	 * @return
	 */
	public static Animation RightIn(long startOffset) {
		Animation a = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		a.setDuration(STARTOFFSET);
		a.setStartOffset(startOffset);
		return a;
	}

	/**
	 * 从右边出去的动画
	 * 
	 * @param startOffset
	 *            开始时的延迟时间
	 * @return
	 */
	public static Animation RightOut(long startOffset) {
		Animation a = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		a.setDuration(STARTOFFSET);
		a.setStartOffset(startOffset);
		return a;
	}

	/**
	 * 从上面进入的动画
	 * 
	 * @param startOffset
	 *            开始时的延迟时间
	 * @return
	 */
	public static Animation TopIn(long startOffset) {
		Animation a = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				-1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		a.setDuration(STARTOFFSET);
		a.setStartOffset(startOffset);
		return a;
	}

	/**
	 * 从上面出去的动画
	 * 
	 * @param startOffset
	 *            开始时的延迟时间
	 * @return
	 */
	public static Animation TopOut(long startOffset) {
		Animation a = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, -1.0f);
		a.setDuration(STARTOFFSET);
		a.setStartOffset(startOffset);
		return a;
	}

	/** 列表进入动画 */
	public static LayoutAnimationController ListIn(long startOffset) {
		LayoutAnimationController layoutan;
		TranslateAnimation tanmiation = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT,
				0, Animation.RELATIVE_TO_PARENT, 0,
				Animation.RELATIVE_TO_PARENT, 0);
		tanmiation.setDuration(startOffset);
		layoutan = new LayoutAnimationController(tanmiation);
		layoutan.setDelay(0.2f);
		layoutan.setOrder(LayoutAnimationController.ORDER_NORMAL);
		return layoutan;
	}

	/**
	 * 相对自身的旋转动画
	 * 
	 * @return
	 */
	public static Animation rotate2Self() {
		Animation rotateAnimation = new RotateAnimation(0f, 359f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotateAnimation.setDuration(1000);
		rotateAnimation.setRepeatCount(-1);
		LinearInterpolator lin = new LinearInterpolator();
		rotateAnimation.setInterpolator(lin);
		return rotateAnimation;
	}

	/** 相对自身的缩放动画 先大后还原 */
	public static Animation zoom2Self() {
		AnimationSet set = new AnimationSet(false);
		Animation zoomAnimation1 = new ScaleAnimation(1f, 1.2f, 1f, 1.2f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		zoomAnimation1.setDuration(500);

		Animation zoomAnimation2 = new ScaleAnimation(1.2f, 1.0f, 1.2f, 1.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		zoomAnimation2.setDuration(500);
		zoomAnimation2.setStartOffset(500);
		set.addAnimation(zoomAnimation1);
		set.addAnimation(zoomAnimation2);
		return set;
	}

}
