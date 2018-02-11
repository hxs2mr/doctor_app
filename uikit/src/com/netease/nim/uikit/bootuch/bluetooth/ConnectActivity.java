package com.netease.nim.uikit.bootuch.bluetooth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.bootuch.recvdata.ReceiveService;


public class ConnectActivity extends Activity {

	/** 显示当前进度 */
	private TextView tvStatus;

	/** 圆形进度提示图片 */
	private ImageView imgProgress;

	/**
	 * 需要连接的设备名
	 */
	private int conDeviceName;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.progdialog);
		tvStatus = (TextView) findViewById(R.id.dialog_text);
		imgProgress = (ImageView) findViewById(R.id.dialog_pro);
		//点圆形进度条重连
		imgProgress.setOnClickListener(reConListener);
		conDeviceName = getIntent().getExtras().getInt("device");

		IntentFilter filter = new IntentFilter(
				ReceiveService.BLU_ACTION_STATE_CHANGE);
		filter.addAction(ReceiveService.ACTION_BLUETOOH_OFF);
		
		registerReceiver(receiver, filter);
		// 在API11以后可以直接调用该方法 设置以dialog形式出现的activity点击外部时是否消失
		if (Build.VERSION.SDK_INT >= 11)
			setFinishOnTouchOutside(false);
		startCon();
	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(ReceiveService.BLU_ACTION_STATE_CHANGE)) {
				tvStatus.setVisibility(View.VISIBLE);
				String state = intent.getExtras().getString("arg1");
				if (state.equals("OPENING")) {
					tvStatus.setText(R.string.connect_opening);
				} else if (state.equals("OPENINGFILE")) {
					tvStatus.setText(R.string.connect_openfail);
					imgProgress.clearAnimation();
					imgProgress.setImageResource(R.mipmap.bluecon_0);
					dismiss(5);
				} else if (state.equals("DISCOVERYING")) {
					tvStatus.setText(R.string.connect_startseach);
				} else if (state.equals("CONNECTING")) {
					// tvStatus.setText(R.string.connect_startconnect);
					tvStatus.setText(R.string.connect_startconnect);
				} else if (state.equals("CONNECTED")) {
					tvStatus.setText(R.string.connect_success);
					imgProgress.clearAnimation();
					imgProgress.setImageResource(R.mipmap.bluecon_1);
					dismiss(1);
				} else if (state.equals("CONNECTFILE")
						|| state.equals("DISCOVERYED")) {
					if (MyBluetooth.blueStatus == MyBluetooth.BLU_STATUS_NORMAL) {
						tvStatus.setText(R.string.connect_fail);
						imgProgress.clearAnimation();
						imgProgress.setImageResource(R.mipmap.bluecon_0);
					}
				}
			} else if (action.equals(ReceiveService.ACTION_BLUETOOH_OFF)) {
				tvStatus.setText(R.string.connect_close);
				imgProgress.clearAnimation();
				imgProgress.setImageResource(R.mipmap.bluecon_0);
				sendBroadcast(new Intent(
						ReceiveService.BLU_ACTION_STOPDISCOVERY));
				sendBroadcast(new Intent(ReceiveService.BLU_ACTION_DISCONNECT));
			}
		}
	};

	private void startCon() {
		imgProgress.setImageResource(R.mipmap.progress);
		imgProgress.startAnimation(AnimationFactory.rotate2Self());
		sendBroadcast(new Intent(ReceiveService.BLU_ACTION_STARTDISCOVERY)
				.putExtra("device", conDeviceName));
	}

	private OnClickListener reConListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (!MyBluetooth.isConnected) {
				sendBroadcast(new Intent(ReceiveService.ACTION_BLU_DISCONNECT));
				startCon();
			}
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}

	/**
	 * 当前蓝牙连接的设备名
	 */
	public static final String CONNECTED_DEVICE_NAME = "CONNAME";

	public void dismiss(final long time) {
		new Thread() {
			public void run() {
				try {
					Thread.sleep(time * 1000);
					BluetoothDevice d = MyBluetooth.getConDevice();
					if (MyBluetooth.isConnected && d != null) {
						Intent i = new Intent();
						i.putExtra(CONNECTED_DEVICE_NAME, d.getName());
						ConnectActivity.this.setResult(1, i);
					}
					ConnectActivity.this.finish();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (MyBluetooth.blueStatus == MyBluetooth.BLU_STATUS_CONNECTING) {
				// Toast.makeText(ConnectActivity.this,
				// R.string.connect_connecting, Toast.LENGTH_SHORT).show();
				return false;
			} else {
				sendBroadcast(new Intent(
						ReceiveService.BLU_ACTION_STOPDISCOVERY));
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
