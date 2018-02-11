package com.netease.nim.uikit.bootuch.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;

import com.creative.bluetooth.BluetoothOpertion;
import com.creative.bluetooth.IBluetoothCallBack;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class MyBluetooth {

	private Context mContext;

	/**
	 * SDK中的蓝牙操作类
	 */
	private static BluetoothOpertion bluetoothOper;

	/**
	 * 蓝牙是否连接成功
	 */
	public static  boolean isConnected = false;

	/**
	 * 手动取消搜索
	 */
	public boolean isCancelFind = false;
	/**
	 * 连接上设备的socket
	 */
	public static BluetoothSocket blueSocket;

	/**
	 * 支持的蓝牙设备列表 {"PC-60NW-1", "PC-60NW", "creative wireless" },, { "PC80B" }, {
	 * "POD", "PC68B" }
	 */
	private static String[][] bluetoothArray = { { "PC_300SNT", "PC-200",
			"PC-100" } };

	/**
	 * 当前需要连接的设备名
	 */
	public int conDeviceName = -1;

	/** 需要排除的设备 */
	private List<BluetoothDevice> excludeDev = new ArrayList<BluetoothDevice>();

	/**
	 * 正在打开蓝牙
	 */
	public static final int BLUETOOTH_MSG_OPENING = 0;

	/**
	 * 搜索设备
	 */
	public static final int BLUETOOTH_MSG_DISCOVERYING = 1;

	/**
	 * 正在连接设备
	 */
	public static final int BLUETOOTH_MSG_CONNECTING = 2;

	/**
	 * 连接成功
	 */
	public static final int BLUETOOTH_MSG_CONNECTED = 3;

	/**
	 * 连接失败
	 */
	public static final int BLUETOOTH_MSG_CONNECTFILE = 4;
	/**
	 * 打开蓝牙失败
	 */
	public static final int BLUETOOTH_MSG_OPENINGFILE = 5;

	/**
	 * 搜索完成
	 */
	public static final int BLUETOOTH_MSG_DISCOVERYED = 6; 
	
	/**
	 * 用于通知的Handler
	 */
	private Handler mHandler;

	public MyBluetooth(Context _context, Handler handler) {
		mContext = _context;
		mHandler = handler;
		bluetoothOper = new BluetoothOpertion(mContext,
				new myBluetoothCallBack());
	}

	/**
	 * 当前蓝牙状态——正常
	 */
	public static final int BLU_STATUS_NORMAL = 0;

	/**
	 * 当前蓝牙状态——搜索中
	 */
	public static final int BLU_STATUS_DISCOVERING = 1;	

	/**
	 * 蓝牙状态————搜索完成
	 */
	public static final int BLUETOOTH_STATUS_DISCOVERYED = 2;
	
	/**
	 * 当前蓝牙状态——连接中
	 */
	public static final int BLU_STATUS_CONNECTING = 3; 

	/**
	 * 当前蓝牙状态——连接上
	 */
	public static final int BLU_STATUS_CONNECTED = 4; 

	/**
	 * 当前蓝牙状态
	 */
	public static int blueStatus = 0;

	// private BLEOpertion bleOpertion;

	/**
	 * 连接指定的设备
	 * 
	 * @param name
	 */
	public void startDiscovery(final int name) {
		this.conDeviceName = name;
		// if (conDeviceName == 4) {
		// startDiscoveryBLE();
		// } else {
		// startDiscovery();
		// }
		startDiscovery();
	}

	// /***
	// * 开始搜索蓝牙(低功耗蓝牙 4.0)
	// */
	// public void startDiscoveryBLE() {
	// if (blueStatus == BLU_STATUS_NORMAL) {
	// if (!openBluetooth())
	// return;
	// if (BLEOpertion.isCanUseBLE(mContext)) {
	// if (bleOpertion == null) {
	// try {
	// bleOpertion = new BLEOpertion(mContext,
	// new bleCallBack());
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// bleOpertion.startEnum();
	// }
	// }
	// }

	/**
	 * 搜索蓝牙设备(经典蓝牙 2.0)
	 */
	public void startDiscovery() {
		if (blueStatus == BLU_STATUS_NORMAL) {
			blueSocket = null;
			if (!openBluetooth())
				return;
			Set<BluetoothDevice> bondDev = bluetoothOper.getBondedDevices();
			if (bondDev != null && bondDev.size() > 0) {
				for (BluetoothDevice bluetoothDevice : bondDev) {								
					if (checkName(bluetoothDevice.getName(), conDeviceName)) {
						if (!isExcludeDev(bluetoothDevice)) {
							isDiscovery = false;							
							blueStatus = BLU_STATUS_CONNECTING; 
							mHandler.sendEmptyMessage(BLUETOOTH_MSG_CONNECTING);
							bluetoothOper.connect(bluetoothDevice);
							return;
						}
					}
				}
			}
			blueStatus = BLU_STATUS_DISCOVERING;
			mHandler.sendEmptyMessage(BLUETOOTH_MSG_DISCOVERYING);
			bluetoothOper.discovery();
			isDiscovery = true;
		}
	}

	/**
	 * 打开手机蓝牙
	 * 
	 * @return 是否打开成功
	 */
	private boolean openBluetooth() {
		isOpenBlueTimeOut = false;
		if (!bluetoothOper.isOpen()) {
			mHandler.sendEmptyMessage(BLUETOOTH_MSG_OPENING);
			bluetoothOper.open();
			openBlueTimer = new Timer();
			openBlueTimer.schedule(new TimerTask() {

				@Override
				public void run() {
					isOpenBlueTimeOut = true;
					mHandler.sendEmptyMessage(BLUETOOTH_MSG_OPENINGFILE);
				}
			}, 10 * 1000);
			while (!bluetoothOper.isOpen() && !isOpenBlueTimeOut) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			openBlueTimer.cancel();
		}
		return !isOpenBlueTimeOut;
	}

	/**
	 * 取消搜索
	 */
	public void stopDiscovery() {
		if (blueStatus == BLU_STATUS_DISCOVERING
				|| blueStatus == BLU_STATUS_CONNECTING) {
			isDiscovery = true;
			bluetoothOper.stopDiscovery();
			isCancelFind = true;			
			isConnected = false;
		}
	}

	/**
	 * 本次是否有过搜索
	 */
	private boolean isDiscovery = false;

	/** 当前设备是否在需要排除的设备队列中 */
	private boolean isExcludeDev(BluetoothDevice dev) {
		if (excludeDev.size() > 0) {
			for (BluetoothDevice device : excludeDev) {
				if (device.getAddress().equals(dev.getAddress()))
					return true;
			}
		}
		return false;
	}

	/**
	 * 开启蓝牙超时的定时器
	 */
	private Timer openBlueTimer;

	/** 打开蓝牙是否超时 */
	private boolean isOpenBlueTimeOut = false;

	private class myBluetoothCallBack implements IBluetoothCallBack {

		@Override
		public void onConnectFail(String err) {
			blueSocket = null;
			if (isDiscovery) {
				blueStatus = BLU_STATUS_NORMAL;
				mHandler.sendEmptyMessage(BLUETOOTH_MSG_CONNECTFILE);
			} else {
				blueStatus = BLU_STATUS_DISCOVERING;
				isDiscovery = true;
				mHandler.sendEmptyMessage(BLUETOOTH_MSG_DISCOVERYING);
				bluetoothOper.discovery();
			}			
		}

		@Override
		public void onConnected(BluetoothSocket arg0) {
			blueStatus = BLU_STATUS_CONNECTED;
			isConnected = true;
			blueSocket = arg0;
			/** sdk的蓝牙搜索连接是异步线程，异步回调onConnected()*/
			if(isCancelFind){
				blueStatus = BLU_STATUS_NORMAL;
				disConnected();
				isConnected = false;
				isCancelFind = false;
			}else{			
				mHandler.sendEmptyMessage(BLUETOOTH_MSG_CONNECTED);
			}
		}

		@Override
		public void onDiscoveryCompleted(List<BluetoothDevice> devices) {
			if (blueStatus != BLU_STATUS_CONNECTING
					&& blueStatus != BLU_STATUS_CONNECTED)
				blueStatus = BLU_STATUS_NORMAL;
			mHandler.sendEmptyMessage(BLUETOOTH_MSG_DISCOVERYED);
		}

		@Override
		public void onException(int exception) {
		}

		@Override
		public void onFindDevice(BluetoothDevice arg0) {
			if (arg0 != null) {
				String name = arg0.getName();
				// String addr = arg0.getAddress();
				// if (checkAddress(addr)) {
				if (conDeviceName != -1) {
					if (checkName(name, conDeviceName)) {
						blueStatus = BLU_STATUS_CONNECTING;					
						mHandler.sendEmptyMessage(BLUETOOTH_MSG_CONNECTING);
						bluetoothOper.connect(arg0);
					}
				} else {
					blueStatus = BLU_STATUS_CONNECTING;
					mHandler.sendEmptyMessage(BLUETOOTH_MSG_CONNECTING);
					bluetoothOper.connect(arg0);
				}
				// }
			}
		}

		@Override
		public void onConnectLocalDevice(BluetoothSocket remoteSocket) {
		}
	}


	/**
	 * 断开与当前设备的连接
	 */
	public void disConnected() {
		if (blueSocket != null && isConnected) {
			isConnected = false;
			blueStatus = BLU_STATUS_NORMAL;
			bluetoothOper.disConnect(blueSocket);
		}
	}

	/**
	 * 判断该地址是否在公司蓝牙地址中
	 * 
	 * @param address
	 * @return true 是属于公司蓝牙产品 false 不属于公司蓝牙产品
	 */
	protected boolean checkAddress(String address) {
		String[] prefixs = { "94:21:97", "00:13:EF" };
		for (int i = 0; i < prefixs.length; i++) {
			if (address.startsWith(prefixs[i]))
				return true;
		}
		return false;
	}

	/** 设备名称是否满足 */
	@SuppressWarnings("unused")
	private boolean checkName(String name) {
		if (name != null && !name.equals("")) {
			for (int i = 0; i < bluetoothArray.length; i++) {
				for (int j = 0; j < bluetoothArray[i].length; j++)
					if (name.equals(bluetoothArray[i][j]))
						return true;
			}
		}
		return false;
	}

	/** 设备名称是否满足 */
	public static boolean checkName(String name, int dev) {
		if (name != null && !name.equals("") && dev < bluetoothArray.length) {
			for (int i = 0; i < bluetoothArray[dev].length; i++) {
				System.out.println("name= " + name + "  "
						+ bluetoothArray[dev][i]);
				if (name.equals(bluetoothArray[dev][i]))
					return true;
			}
		}
		return false;
	}

	/**
	 * 获取连接成功的设备
	 * 
	 * @return
	 */
	public static BluetoothDevice getConDevice() {
		if (blueSocket != null)
			return blueSocket.getRemoteDevice();
		return null;
	}
}
