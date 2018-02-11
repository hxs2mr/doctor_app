package com.netease.nim.uikit.bootuch.recvdata;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.creative.SpotCheck.ISpotCheckCallBack;
import com.creative.SpotCheck.SpotCheck;
import com.creative.SpotCheck.SpotSendCMDThread;
import com.creative.base.BaseDate.ECGData;
import com.creative.base.BaseDate.Wave;
import com.creative.base.Ireader;
import com.creative.base.Isender;

import java.util.ArrayList;
import java.util.List;

public class StaticReceive {
	private static final String TAG = "StaticReceive";

	/**
	 * PC-300SNT,PC-200
	 */
	private static SpotCheck spotCheck;

	/**
	 * 通知上层各种数据消息
	 */
	private static Handler mHandler;

	protected static Context mContext;
	
	/** 是否使用百捷血糖仪 */
	public static boolean hasUA;

	/**
	 * 开始接收数据
	 * 
	 * @param bluName
	 */
	public static void startReceive(Context context, String bluName, Ireader iReader, Isender iSender,
                                    Handler _handler) {
		if (bluName != null && !bluName.equals("")) {
			start = true;
			mHandler = _handler;
			mContext = context;
			if (bluName.equals("PC_300SNT") || bluName.equals("PC-200") || bluName.equals("PC-100")) {
				spotCheck = new SpotCheck(iReader, iSender, new SpotCallBack());
				spotCheck.Start();
				spotCheck.QueryDeviceVer();			
			}
		}
	}

	/**
	 * 停止接收数据
	 */
	public static void StopReceive() {
		start = false;
		if (spotCheck != null) {
			spotCheck.Stop();
			spotCheck = null;
		}
		HWMajor = HWMinor = SWMajor = SWMinor = ALMajor = ALMinor = 0;
	}

	/**
	 * 暂停数据接收
	 */
	public static void Pause() {
		pause = true;
		if (spotCheck != null) {
			spotCheck.Pause();
		}
		// if (ecg != null) {
		// ecg.Pause();
		// }
	}

	/**
	 * 恢复数据接收
	 */
	public static void Continue() {
		pause = false;
		if (spotCheck != null) {
			spotCheck.Continue();
		}
	}
	
	/**
	 * 和下位机握手通信, reactivate device
	 */
	public static void QueryDeviceVer() {
		if (spotCheck != null) {
			spotCheck.QueryDeviceVer();
		}
	}

	public static boolean pause = false;

	public static boolean start = false;

	public static void setmHandler(Handler mHandler) {
		StaticReceive.mHandler = mHandler;
	}

	/**
	 * 数据类型key——心电文件
	 */
	public static final String DATATYPEKEY_ECG_FILE = "ecgFile";
	/**
	 *  数据类型key——心电波形
	 */
	public static final String DATATYPEKEY_ECG_WAVE = "ecgwave";

	/**
	 * 设备数据消息——设备ID
	 */
	public static final int MSG_DATA_DEVICE_ID = 0x201;

	/**
	 * 设备数据消息——设备版本信息, device version
	 */
	public static final int MSG_DATA_DEVICE_VS = 0x202;

	/**
	 * 设备数据消息——血氧参数数据
	 */
	public static final int MSG_DATA_SPO2_PARA = 0x203;

	/**
	 *  设备数据消息——血氧波形数据
	 */
	public static final int MSG_DATA_SPO2_WAVE = 0x204;

	/**
	 * 设备数据消息——体温数据, temperature
	 */
	public static final int MSG_DATA_TEMP = 0x205;

	/**
	 * 设备数据消息——血糖数据
	 */
	public static final int MSG_DATA_GLU = 0x206;

	/**
	 * 设备数据消息——设备关机, device power off
	 */
	public static final int MSG_DATA_DEVICE_SHUT = 0x207;

	/**
	 * 设备数据消息——连接断开, disconnect
	 */
	public static final int MSG_DATA_DISCON = 0x208;

	/**
	 * 设备数据消息——心电测量状态改变, ECG measure status change
	 */
	public static final int MSG_DATA_ECG_STATUS_CH = 0x209;

	/**
	 * 设备数据消息——血压测量状态改变,NIBP measure status change
	 */
	public static final int MSG_DATA_NIBP_STATUS_CH = 0x20a;

	/**
	 * 设备数据消息——血压测量实时袖袋压,NIBP realtime measure 
	 */
	public static final int MSG_DATA_NIBP_REALTIME = 0x20b;

	/**
	 * 设备数据消息——血压测量结果, NIBP result
	 */
	public static final int MSG_DATA_NIBP_END = 0x20c;

	/**
	 * 设备数据消息——心电波形数据
	 */
	public static final int MSG_DATA_ECG_WAVE = 0x20d;

	/**
	 * 设备数据消息——电池电量
	 */
	public static final int MSG_DATA_BATTERY = 0x20e;

	/**
	 * 设备数据消息——搏动标记, pulse flag
	 */
	public static final int MSG_DATA_PULSE = 0x20f;

	/**
	 * 设备数据消息——ecg心电增益
	 */
	public static final int MSG_DATA_ECG_GAIN = 0x210;
	
	/** 百捷 ua */
	public static final int MSG_DATA_UA = 0x30;
	
	/** 百捷 chol */
	public static final int MSG_DATA_CHOL = 0x31;
	
	/** 血糖设备类型  */
	public static final int MSG_DATA_GLU_DEVICE_TYPE = 0x31;

	/**
	 * 设备版本信息, device version info
	 */
	public static int HWMajor, HWMinor, SWMajor, SWMinor, ALMajor, ALMinor;

	/**
	 * 保存波形数据, list for drawing wave
	 */
	public static List<Wave> DRAWDATA = new ArrayList<Wave>();

	/**
	 * 保存血氧波形数据 用于绘制血氧柱状图,list for drawing spo2 rect
	 */
	public static List<Wave> SPOWAVE = new ArrayList<Wave>();

	/**
	 * 是否是心电波形数据
	 */
	public static boolean isECGData = false;

	/**
	 * 心电波形范围是否是128bit
	 */
	public static boolean is128 = false;

	/** 固件版本 */
	public static final int MSG_FIRMWARE_VER = 0x214;
	
	private static class SpotCallBack implements ISpotCheckCallBack {

		@Override
		public void OnConnectLose() {
			Log.e(TAG, "--connect lost");
		}

		@Override
		public void OnGetDeviceID(final String sDeviceID) {
			Log.d(TAG, "id->"+sDeviceID);
			new Thread(){
				public void run() {
					try {
						sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					mHandler.obtainMessage(MSG_DATA_DEVICE_ID, sDeviceID).sendToTarget();
				};
			}.start();		
		}

		@Override
		public void OnGetDeviceVer(int nHWMajor, int nHWMinor, int nSWMajor, int nSWMinor, int nPower, int nBattery) {
			HWMajor = nHWMajor;
			HWMinor = nHWMinor;
			SWMajor = nSWMajor;
			SWMinor = nSWMinor;			
			mHandler.obtainMessage(MSG_DATA_BATTERY, nPower, nBattery).sendToTarget();
		}
		
		int sendCnt =0;
		/**
		 * 0:待机,standby mode<br>
		 * 1: 开始测量,begin to measure <br>
		 * 2: 停止测量,stop measuring<br>
		 * 3: 0xFF:测量出错，模块故障或模块未接入,measure error。
		 */
		@Override
		public void OnGetECGAction(int status) {
			// must set ECG Bit in standby mode.
			// don't set, default is 8bit ECG
			// only send one commond is ok,
			if(status == 0 && sendCnt<1){			
				//set 12 bit ECG
				SpotSendCMDThread.Send12BitECG();
				System.out.println("send 12 bit cmd");
				
				//set 8 bit ECG
				//SpotSendCMDThread.Send8BitECG();
				//System.out.println("send 8 bit cmd");
				
				sendCnt++;
			}else if(status==1) {
				sendCnt=0;
			}
			mHandler.obtainMessage(MSG_DATA_ECG_STATUS_CH, status, 0).sendToTarget();
		}

		@Override
		public void OnGetECGRealTime(ECGData waveData, int nHR, boolean bLeadoff, int nMax) {
			isECGData = true;
			if (nMax == 255) //8bit ECG data
				is128 = true;
			else // 12bit ECG data, nMax = 4095
				is128 = false;			
			DRAWDATA.addAll(waveData.data);
			Message msg = mHandler.obtainMessage(MSG_DATA_ECG_WAVE, waveData);
			Bundle data = new Bundle();
			data.putBoolean("bLeadoff", bLeadoff);
			data.putInt("nHR", nHR);
			msg.setData(data);
			mHandler.sendMessage(msg);
		}

		@Override
		public void onGetECGGain(int arg0, int arg1) {
			mHandler.obtainMessage(MSG_DATA_ECG_GAIN, arg0, arg1).sendToTarget();
		}

		@Override
		public void OnGetECGResult(int nResult, int nHR) {
			mHandler.obtainMessage(MSG_DATA_ECG_STATUS_CH, 2, nResult, nHR).sendToTarget();
		}

		@Override
		public void OnGetECGVer(int nHWMajor, int nHWMinor, int nSWMajor, int nSWMinor) {
		}

		@Override
		public void OnGetGlu(float nGlu, int nGluStatus, int unit) {
			System.out.println("血糖值：" + nGlu + " " + nGluStatus + "  " + unit);
			mHandler.obtainMessage(MSG_DATA_GLU, nGluStatus, unit, nGlu).sendToTarget();
		}

		@Override
		public void OnGetGluStatus(int nStatus, int nHWMajor, int nHWMinor, int nSWMajor, int nSWMinor) {

		}

		@Override
		public void OnGetNIBPAction(int bStart) {
			mHandler.obtainMessage(MSG_DATA_NIBP_STATUS_CH, bStart, 0).sendToTarget();
		}

		@Override
		public void OnGetNIBPRealTime(boolean bHeartbeat, int nBldPrs) {
			if (bHeartbeat) {
				mHandler.obtainMessage(MSG_DATA_NIBP_REALTIME, 0, nBldPrs).sendToTarget();
			} else {
				mHandler.obtainMessage(MSG_DATA_NIBP_REALTIME, 1, nBldPrs).sendToTarget();
			}
		}

		@Override
		public void OnGetNIBPResult(boolean bHR, int nPulse, int nMAP, int nSYS, int nDIA, int nGrade, int nBPErr) {
			Message msg = mHandler.obtainMessage(MSG_DATA_NIBP_END);
			Bundle data = new Bundle();
			data.putBoolean("bHR", bHR);
			data.putInt("nPulse", nPulse);
			data.putInt("nSYS", nSYS);
			data.putInt("nDIA", nDIA);
			data.putInt("nGrade", nGrade);
			data.putInt("nBPErr", nBPErr);
			msg.setData(data);
			mHandler.sendMessage(msg);
		}

		@Override
		public void OnGetNIBPStatus(int nStatus, int nHWMajor, int nHWMinor, int nSWMajor, int nSWMinor) {
		}

		@Override
		public void OnGetPowerOff() { //bluetooth disconnect , call it first
			Log.d(TAG, "bluetooth disconnect");
			mHandler.sendEmptyMessage(MSG_DATA_DISCON);			
		}

		@Override
		public void OnGetSpO2Param(int nSpO2, int nPR, float nPI, boolean bProbe, int nMode) {
			Message msg = mHandler.obtainMessage(MSG_DATA_SPO2_PARA);
			Bundle data = new Bundle();
			data.putInt("nSpO2", nSpO2);
			data.putInt("nPR", nPR);
			data.putFloat("nPI", nPI);
			data.putBoolean("bProbe", bProbe);
			data.putInt("nMode", nMode);
			msg.setData(data);					
			mHandler.sendMessage(msg);
		}

		@Override
		public void OnGetSpO2Status(int nStatus, int nHWMajor, int nHWMinor, int nSWMajor, int nSWMinor) {
		}

		@Override
		public void OnGetSpO2Wave(List<Wave> waveData) {
			DRAWDATA.addAll(waveData);
			SPOWAVE.addAll(waveData);
			isECGData = false;
		}

		@Override
		public void OnGetTmp(boolean bManualStart, boolean bProbeOff, float nTmp, int nTmpStatus, int nResultStatus) {
			Message msg = mHandler.obtainMessage(MSG_DATA_TEMP);
			Bundle data = new Bundle();
			data.putBoolean("bManualStart", bManualStart);
			data.putBoolean("bProbeOff", bProbeOff);
			data.putFloat("nTmp", nTmp);
			data.putInt("nTmpStatus", nTmpStatus);
			data.putInt("nResultStatus", nResultStatus);
			msg.setData(data);						
			mHandler.sendMessage(msg);
		}

		@Override
		public void OnGetTmpStatus(int nStatus, int nHWMajor, int nHWMinor, int nSWMajor, int nSWMinor) {

		}

		@Override
		public void OnGetNIBPMode(int arg0) {

		} 

		@Override
		public void OnGetSpO2Action(int action) {
		}

		@Override
		public void NIBP_StartStaticAdjusting() {
		}

		@Override
		public void OnGetGLUAction(int status) {
		}

		@Override
		public void OnGetTMPAction(int status) {
		}

		@Override
		public void onIAP_version(int hardVer, int softVer, byte response) {
			System.out.println("iap_ver hard："+hardVer+",soft:"+softVer);
			mHandler.obtainMessage(MSG_FIRMWARE_VER,softVer,hardVer).sendToTarget();
		}

		// Only PC300 baijie GLU device is avaliable
		@Override
		public void OnGetUA(float fUA, int unit) {
			//default unit is mg/dl
			mHandler.obtainMessage(MSG_DATA_UA, fUA).sendToTarget();
		}

		@Override
		public void OnGetCHOL(float fCHOL, int unit) {
			//default unit is mg/dl
			mHandler.obtainMessage(MSG_DATA_CHOL, fCHOL).sendToTarget();
		}

		@Override
		public void OnGetGLU_DeviceType(int type) {
			if(type==2){//GLU device type is baijie
				//send respond command ,then can set data unit for baijie device				
				SpotSendCMDThread.respondGLU_DeviceType();
				hasUA = true;
			}
		}
	}
	
}
