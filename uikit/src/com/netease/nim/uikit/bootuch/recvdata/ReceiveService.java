package com.netease.nim.uikit.bootuch.recvdata;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.creative.base.InputStreamReader;
import com.creative.base.OutputStreamSender;
import com.netease.nim.uikit.bootuch.bluetooth.MyBluetooth;

import java.io.IOException;


/**
 * 后台服务类，在后台接收数据及监听各类广播消息
 * 
 */
public class ReceiveService extends Service {

	private MyBluetooth myBluetooth;
	
	@Override
	public void onCreate() {
		super.onCreate();
		init();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver();
		myBluetooth.disConnected();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private void init() {
		registerReceiver();// 注册广播接收器
		myBluetooth = new MyBluetooth(this, mHandler);// 初始化蓝牙操作
	}

	/**
	 * 接收蓝牙和数据读取中发送的消息
	 */
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MyBluetooth.BLUETOOTH_MSG_OPENING: {
				sendBroadcast(BLU_ACTION_STATE_CHANGE, "OPENING");
			}
				break;
			case MyBluetooth.BLUETOOTH_MSG_OPENINGFILE: {
				sendBroadcast(BLU_ACTION_STATE_CHANGE, "OPENINGFILE");
			}
				break;
			case MyBluetooth.BLUETOOTH_MSG_DISCOVERYING: {
				sendBroadcast(BLU_ACTION_STATE_CHANGE, "DISCOVERYING");
			}
				break;
			case MyBluetooth.BLUETOOTH_MSG_CONNECTING: {
				sendBroadcast(BLU_ACTION_STATE_CHANGE, "CONNECTING");
			}
				break;
			case MyBluetooth.BLUETOOTH_MSG_CONNECTED: {
				sendBroadcast(BLU_ACTION_STATE_CHANGE, "CONNECTED");
				//开始接收数据
				startRece(true);
			}
				break;
			case MyBluetooth.BLUETOOTH_MSG_CONNECTFILE: {
				sendBroadcast(BLU_ACTION_STATE_CHANGE, "CONNECTFILE");
			}
			case MyBluetooth.BLUETOOTH_MSG_DISCOVERYED: {
				sendBroadcast(BLU_ACTION_STATE_CHANGE, "DISCOVERYED");
			}
				break;
			}
		}
	};

	/**
	 * 开始接收设备数据
	 * 
	 * @param start
	 *            是否开启
	 */
	private void startRece(boolean start) {
		if (start) {
			try {
				if (MyBluetooth.blueSocket != null) {
					String conDeviceName = MyBluetooth.blueSocket
							.getRemoteDevice().getName();

					InputStreamReader reader = new InputStreamReader(
							MyBluetooth.blueSocket.getInputStream());
					OutputStreamSender sender = new OutputStreamSender(
							MyBluetooth.blueSocket.getOutputStream());
					StaticReceive.startReceive(this, conDeviceName, reader,
							sender, mHandler);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			StaticReceive.StopReceive();
		}
	}

	/**
	 * 注册广播接收器
	 */
	private void registerReceiver() {
		IntentFilter filter = new IntentFilter();
		// 蓝牙相关广播，监听蓝牙开
		filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);

		this.registerReceiver(bluetoothReceiver, filter);
		filter = null;
		filter = new IntentFilter();
		filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
		filter.addAction(Intent.ACTION_MEDIA_EJECT);
		filter.addAction(Intent.ACTION_MEDIA_REMOVED);
		filter.addDataScheme("file");
		this.registerReceiver(bluetoothReceiver, filter);

		filter = new IntentFilter();
		filter.setPriority(Integer.MAX_VALUE);
		filter.addAction(BLU_ACTION_STARTDISCOVERY);
		filter.addAction(BLU_ACTION_STOPDISCOVERY);
		filter.addAction(BLU_ACTION_DISCONNECT);
		filter.addAction(ACTION_BLU_DISCONNECT);
		filter.addAction(ACTION_USER_EXIT);
		this.registerReceiver(bluetoothReceiver, filter);
	}

	/**
	 * 注销广播接收器
	 */
	private void unregisterReceiver() {
		this.unregisterReceiver(bluetoothReceiver);
	}

	private BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
				int state = intent.getExtras().getInt(
						BluetoothAdapter.EXTRA_STATE);
				if (state == BluetoothAdapter.STATE_OFF) {
					sendBroadcast(ACTION_BLUETOOH_OFF);
				} else if (state == BluetoothAdapter.STATE_ON) {
					// sendBroadcast(ACTION_BLUETOOH_ON);
				}
			} else if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {// 存储设备挂载
				sendBroadcast(ACTION_MEDIA_MOUNTED);
				
			} else if (action.equals(Intent.ACTION_MEDIA_EJECT)) {// 存储设备被卸载
				sendBroadcast(ACTION_MEDIA_EJECT);
				
			} else if (action.equals(Intent.ACTION_MEDIA_REMOVED)) {// 存储设备被移除
				sendBroadcast(ACTION_MEDIA_EJECT);
				
			} else if (action.equals(BLU_ACTION_STARTDISCOVERY)) {// 开始连接设备
				int deviceName = intent.getExtras().getInt("device");
				myBluetooth.startDiscovery(deviceName);
				
			} else if (action.equals(BLU_ACTION_STOPDISCOVERY)) {// 停止连接设备				
				myBluetooth.stopDiscovery();
				
			} else if (action.equals(BLU_ACTION_DISCONNECT)
					|| action.equals(ACTION_BLU_DISCONNECT)
					|| action.equals(ACTION_USER_EXIT)) {// 断开与设备的连接
				myBluetooth.disConnected();
				startRece(false);
			}
		}
	};

	/**
	 * 蓝牙关闭广播
	 */
	public static final String ACTION_BLUETOOH_OFF = "bluetooth_off";

	/**
	 * 蓝牙打开广播
	 */
	public static final String ACTION_BLUETOOH_ON = "bluetooth_on";

	/**
	 * 存储设备被卸载
	 */
	public static final String ACTION_MEDIA_EJECT = "media_eject";

	/**
	 * 蓝牙连接断开
	 */
	public static final String ACTION_BLU_DISCONNECT = "disconnect";

	/**
	 * 存储设备已挂载
	 */
	public static final String ACTION_MEDIA_MOUNTED = "media_mounted";

	/**
	 * 蓝牙连接状态改变
	 */
	public static final String BLU_ACTION_STATE_CHANGE = "state_change";

	/**
	 * 蓝牙广播 开始连接设备
	 */
	public static final String BLU_ACTION_STARTDISCOVERY = "startDiscovery";

	/**
	 * 蓝牙广播 停止连接设备
	 */
	public static final String BLU_ACTION_STOPDISCOVERY = "stopDiscovery";

	/**
	 * 蓝牙广播 断开与设备的连接
	 */
	public static final String BLU_ACTION_DISCONNECT = "disconnect";

	/**
	 * 用户广播————当前用户退出登录
	 */
	public static final String ACTION_USER_EXIT = "userexit";

	/**
	 * 发送广播 主要用于通知应用蓝牙状态及设备存储卡的状态
	 * 
	 * @param action
	 */
	private void sendBroadcast(String action) {
		Intent intent = new Intent(action);
		this.sendBroadcast(intent);
	}

	/**
	 * 发送广播 主要用于通知应用蓝牙当前的连接状态
	 * 
	 * @param arg
	 */
	private void sendBroadcast(String... arg) {
		Intent i = new Intent(arg[0]);
		for (int j = 1; j < arg.length; j++) {
			i.putExtra("arg" + j, arg[j]);
		}
		this.sendBroadcast(i);
	}

}
