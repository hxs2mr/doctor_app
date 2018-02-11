package com.netease.nim.uikit.bootuch.libdemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.creative.SpotCheck.IAP;
import com.creative.SpotCheck.IAP.MCUType;
import com.creative.SpotCheck.IIAPCallBack;
import com.creative.SpotCheck.SpotSendCMDThread;
import com.creative.SpotCheck.StatusMsg;
import com.creative.base.BaseDate;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.bootuch.bluetooth.ConnectActivity;
import com.netease.nim.uikit.bootuch.bluetooth.MyBluetooth;
import com.netease.nim.uikit.bootuch.draw.DrawPC300NIBPRect;
import com.netease.nim.uikit.bootuch.draw.DrawPC300SPO2Rect;
import com.netease.nim.uikit.bootuch.draw.DrawThreadPC300;
import com.netease.nim.uikit.bootuch.recvdata.ReceiveService;
import com.netease.nim.uikit.bootuch.recvdata.StaticReceive;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import me.yokeyword.fragmentation.SwipeBackLayout;
import me.yokeyword.fragmentation_swipeback.SwipeBackActivity;
import microtech.hxswork.com.frame_core.net.RestClent;
import microtech.hxswork.com.frame_core.net.callback.ISuccess;
import microtech.hxswork.com.frame_core.util.SAToast;

import static com.netease.nim.uikit.business.session.activity.P2PMessageActivity.Name;
import static microtech.hxswork.com.frame_core.net.RestClent.TIGE_DOCTOR_ID;
import static microtech.hxswork.com.frame_core.net.RestClent.TIGE_GOV_ID;
import static microtech.hxswork.com.frame_core.net.RestClent.TIGE_REGION_ID;
import static microtech.hxswork.com.frame_core.net.RestClent.TIGE_USERID;
import static microtech.hxswork.com.frame_core.util.TimeUtils.date2TimeStamp1;


/**
 * PC-300,PC200
 */
public class BootuchActivity extends SwipeBackActivity {
	
	private TextView tv_SYS, tv_DIA, tv_SPO, tv_PR, tv_TMP,tv_GLU, tv_MSG,
			tv_MODE,tv_GLU_UNIT,tv_blank,tv_ua,tv_chol,tv_ua_unit,tv_chol_unit;
	private Button bt_BIBP, bt_ECG,bt_update;
	private ImageView img_Pulse, img_Battery;

	private LinearLayout PC300UALayout;

	private int mai_number_code=0;
	private int mai_value = 0 ;
	private int yan_value=0;
	/**
	 * 300绘制线程, draw  wave runnable
	 */
	private DrawThreadPC300 drawRunablePC300;
	
	/**
	 * 300柱状图,draw spo2 rect runnable
	 */
	private DrawPC300SPO2Rect drawPC300SPO2Rect;

	/** 绘制300柱状图线程  ,  draw spo2 rect thread */
	private Thread drawPC300SPO2RectThread;

	/**
	 * 300柱状图绘制线程, draw nibp rect 
	 */
	private DrawPC300NIBPRect drawPC300NIBPRect;
	
	/**
	 * 绘图线程, draw  wave thread
	 */
	private Thread drawThread;

	/**
	 * 电量等级
	 * battery level
	 */
	private int batteryRes[] = { R.mipmap.battery_0, R.mipmap.battery_1, R.mipmap.battery_2,
			R.mipmap.battery_3 };

	/**
	 * 电池充电状态, battery charge
	 */
	private int batteryRes_img[] = { R.mipmap.battery_0_ing, R.mipmap.battery_1_ing, R.mipmap.battery_2_ing,
			R.mipmap.battery_3_ing };

	/**
	 * 血压测量错误结果, nibp error of result
	 */
	private String[] nibpERR;

	/**
	 * 心电测量错误结果, ecg error of resul
	 */
	private String[] ecgERR;

	private Resources myResources;

	private boolean bECGIng = false;
	private boolean bNIBPIng = false;
	private boolean bSpo2Ing = false;
	
	private int curVer =0;
	String record_id ="";
	
	/**
	 * 血糖单位: true->mmol/L ,false->mg/dl
	 */
	private boolean bGluUnitMmol = true;
	private boolean bCHOLUnitMmol,bUAMmol;
		
	private String mGLU_Mgdl,mGLU_Mmol,mCHOL_Mgdl,mCHOL_Mmol,mUA_Mgdl,mUA_Mmol;
	private LinearLayoutCompat bootuch_back_linear = null;
	private AppCompatTextView  title_name = null;
	 private PopupWindow menuWindow = null;
	 private String value="";
	private String end_value1="";
	private String end_value2="";

	 private int type=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bootuch_activity);
		myResources = getResources();
		nibpERR = myResources.getStringArray(R.array.bp_err_new);
		ecgERR = myResources.getStringArray(R.array.ecg_measureres);
		PC300UALayout = (LinearLayout) findViewById(R.id.baijie1_device);
		tv_ua = (TextView) findViewById(R.id.realplay_pc300_tv_ua);
		tv_chol = (TextView) findViewById(R.id.realplay_pc300_tv_chol);
		tv_ua_unit = (TextView) findViewById(R.id.realplay_pc300_tv_ua_unit);
		tv_chol_unit = (TextView) findViewById(R.id.realplay_pc300_tv_chol_unit);	
		tv_SYS = (TextView) findViewById(R.id.realplay_pc300_tv_sys);
		tv_DIA = (TextView) findViewById(R.id.realplay_pc300_tv_dia);
		tv_SPO = (TextView) findViewById(R.id.realplay_pc300_tv_spo);
		tv_PR = (TextView) findViewById(R.id.realplay_pc300_tv_pr);
		tv_TMP = (TextView) findViewById(R.id.realplay_pc300_tv_temp);
		tv_GLU = (TextView) findViewById(R.id.realplay_pc300_tv_glu);
		tv_MSG = (TextView) findViewById(R.id.realplay_pc300_tv_msg);
		tv_MODE = (TextView) findViewById(R.id.realplay_pc300_tv_mode);
		tv_GLU_UNIT = (TextView) findViewById(R.id.tv_glu_unit);
		tv_blank = (TextView) findViewById(R.id.tv_blank);
		bootuch_back_linear = findViewById(R.id.bootuch_back_linear);
		bt_BIBP = (Button) findViewById(R.id.realplay_pc300_bt_nibp);
		bt_ECG = (Button) findViewById(R.id.realplay_pc300_bt_ecg);
		bt_update = (Button) findViewById(R.id.realplay_pc300_bt_update);
		title_name =findViewById(R.id.bootuch_next_name);

		drawRunablePC300 = (DrawThreadPC300) findViewById(R.id.realpaly_pc300_view_draw);
		drawRunablePC300.setmHandler(mHandler);
		drawThread = new Thread(drawRunablePC300, "DrawPC300SNT");
		drawThread.start();

		drawPC300SPO2Rect = (DrawPC300SPO2Rect) findViewById(R.id.realpaly_pc300_draw_spo_rect);
		drawPC300NIBPRect = (DrawPC300NIBPRect) findViewById(R.id.realpaly_pc300_draw_nibp_rect);
		drawPC300SPO2RectThread = new Thread(drawPC300SPO2Rect, "DrawPC300RectThread");
		drawPC300SPO2RectThread.start();

		img_Pulse = (ImageView) findViewById(R.id.realplay_pc300_img_pulse);
		img_Battery = (ImageView) findViewById(R.id.realplay_pc300_img_battery);

		bt_BIBP.setOnClickListener(myOnClickListener);
		bt_ECG.setOnClickListener(myOnClickListener);
		tv_GLU.setOnClickListener(myOnClickListener);
		bt_update.setOnClickListener(myOnClickListener);				
		tv_ua.setOnClickListener(myOnClickListener);
		tv_chol.setOnClickListener(myOnClickListener);
		bootuch_back_linear.setOnClickListener(myOnClickListener);
		title_name.setText("测量"+Name+"体格");
		android6_RequestLocation();//权限

		// connectDevice();
		startService(new Intent(this, ReceiveService.class));
		getSwipeBackLayout().setEdgeOrientation(SwipeBackLayout.EDGE_LEFT); // EDGE_LEFT(默认),EDGE_ALL
		getSwipeBackLayout().setParallaxOffset(0.0f - 1.0f); // （类iOS）滑动退出视觉差，默认0.3
		setSwipeBackEnable(true); // 是否允许滑动

		if (!MyBluetooth.isConnected) {
			// connectDevice();
			Intent i = new Intent(BootuchActivity.this, ConnectActivity.class);
			i.putExtra("device", 0);
			startActivityForResult(i, 0x100);
		}

		bt_update.setVisibility(View.GONE);


		record_id = date2TimeStamp1(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()) ,"yyyy-MM-dd HH:mm:ss");
		System.out.println("****时间戳***:"+record_id);
	}

	private OnClickListener myOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int i1 = v.getId();
			if (i1 == R.id.realplay_pc300_bt_ecg) {
				if (!MyBluetooth.isConnected) {
					// connectDevice();
					Intent i = new Intent(BootuchActivity.this, ConnectActivity.class);
					i.putExtra("device", 0);
					startActivityForResult(i, 0x100);
					return;
				}
				if (StatusMsg.ECG_DEVICE_STATUS == StatusMsg.ECG_LEAD_ON) {
					if (bECGIng) {
						SpotSendCMDThread.SendStopECG();
					} else {
						SpotSendCMDThread.SendStartECG();
					}
				} else {
					Toast.makeText(BootuchActivity.this, R.string.measure_ecg_connect_dev, Toast.LENGTH_SHORT).show();
				}

			} else if (i1 == R.id.realplay_pc300_bt_nibp) {
				if (!MyBluetooth.isConnected) {
					// connectDevice();
					Intent i = new Intent(BootuchActivity.this, ConnectActivity.class);
					i.putExtra("device", 0);
					startActivityForResult(i, 0x100);
					return;
				}
				if (bNIBPIng) {
					SpotSendCMDThread.SendStopMeasure();
				} else {
					SpotSendCMDThread.SendStartMeasure();
				}

			} else if (i1 == R.id.realplay_pc300_tv_glu) {
				setGLUUnit(!bGluUnitMmol);

			} else if (i1 == R.id.realplay_pc300_bt_update) {
				progressDialog();

			} else if (i1 == R.id.realplay_pc300_tv_chol) {
				setCHOLUnit(!bCHOLUnitMmol);

			} else if (i1 == R.id.realplay_pc300_tv_ua) {
				setUAUnit(!bUAMmol);

			} else if(i1 == R.id.bootuch_back_linear){
				finish();
			}
		}
	};


	// private void connectDevice() {
	// new Thread() {
	//
	// @Override
	// public void run() {
	// super.run();
	// try {
	// SerialReader reader = new SerialReader(Application.getSerialPort());
	// SerialWriter writer = new SerialWriter(Application.getSerialPort());
	// StaticReceive.startReceive(BootuchActivity.this, "PC_300SNT", reader,
	// writer, mHandler);
	// StaticReceive.setmHandler(mHandler);
	// MyBluetooth.isConnected = true;
	// } catch (InvalidParameterException e) {
	// e.printStackTrace();
	// } catch (SecurityException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// }.start();
	// }

	
	/**
	 * 升级弹窗
	 */
	private TextView tv_Data;
	private ProgressBar mProgressBar;
	private Button btn_YES;
	private Button btn_NO;
	private void progressDialog() {		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle(getResources().getString(R.string.update_firmware_title));
        View view = LayoutInflater.from(this).inflate(R.layout.my_progress_dialog, null);
        TextView tv_updateInfo = (TextView) view.findViewById(R.id.tv_update_info);
        tv_Data = (TextView) view.findViewById(R.id.tv_update_data);
        mProgressBar = (ProgressBar) view.findViewById(R.id.update_progressBar);
        btn_YES = (Button) view.findViewById(R.id.btnYes);
        btn_NO = (Button) view.findViewById(R.id.btnNo);
        tv_updateInfo.setText("Current version:"+curVer+",whether to update");
        builder.setCancelable(false);      
        builder.setView(view);       
               
        final AlertDialog dialog = builder.show();
               
        btn_YES.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				if("complete".equals(btn_YES.getText().toString())){
					dialog.dismiss();
					return;
				}
				
				//btn_NO.setEnabled(false);
				InputStream fStream = getResources().openRawResource(R.raw.pc_200_iap_1421);		
				
				StaticReceive.Pause();
				if (MyBluetooth.blueSocket!= null && fStream!=null) {
					try {
						InputStream is =MyBluetooth.blueSocket.getInputStream();
						OutputStream os = MyBluetooth.blueSocket.getOutputStream();
						IAP iapp = new IAP(is, os, fStream, MCUType.MAIN, new IAPCallBack());
						new Thread(iapp).start();
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else {
					dialog.dismiss();
				}				
			}
		});
        btn_NO.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});     
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		switch (arg0) {
		case 0x100: {
			if (arg1 == 1) {
				StaticReceive.setmHandler(mHandler);
				Toast.makeText(this, "连接设备成功!", Toast.LENGTH_SHORT).show();
			}
		}
			break;
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case StaticReceive.MSG_DATA_DEVICE_SHUT:
			case StaticReceive.MSG_DATA_DISCON: {
				Toast.makeText(BootuchActivity.this, R.string.connect_connect_off, Toast.LENGTH_LONG).show();
				//sendBroadcast(new Intent(ReceiveService.BLU_ACTION_DISCONNECT));
			}
				break;
			case StaticReceive.MSG_DATA_DEVICE_ID: {
				String str=String.valueOf(msg.obj);	
				if("PC-200".equals(str)){
					//获取iap固件版本
			        SpotSendCMDThread.iap_Version(MCUType.MAIN);
					bt_update.setVisibility(View.VISIBLE);
					tv_blank.setVisibility(View.GONE); 
					PC300UALayout.setVisibility(View.GONE);
				}else {
					bt_update.setVisibility(View.GONE);
					tv_blank.setVisibility(View.VISIBLE);
					PC300UALayout.setVisibility(View.VISIBLE);		
				}	
			}
				break;
			case StaticReceive.MSG_DATA_BATTERY: {
				setBattery(msg.arg1, msg.arg2);
			}
				break;
			case StaticReceive.MSG_DATA_ECG_GAIN:
				drawRunablePC300.setGain(msg.arg2);
				System.out.println("这些心电的数据msg.arg2******"+msg.obj);
				break;
			case StaticReceive.MSG_DATA_ECG_STATUS_CH: {
				if (msg.arg1 == 0) {// 待机,standby state
					bECGIng = true;
					checkECGStatus(true);
					setTVtext(tv_MODE, "ECG");
					if (!bSpo2Ing) {
						setPR("");
					}
				} else if (msg.arg1 == 1) {// 开始测量, begin to measure
					bECGIng = false;
					checkECGStatus(false);
					drawRunablePC300.cleanWaveData();
					System.out.println("这些心电的数据******"+String.valueOf(msg.obj));

				} else if (msg.arg1 == 2) {// 停止, stop
					bECGIng = false;
					checkECGStatus(false);
					Toast.makeText(BootuchActivity.this, ecgERR[msg.arg2], Toast.LENGTH_SHORT).show();
					drawRunablePC300.cleanWaveData();
					if (!bSpo2Ing) {
						System.out.println("这些脉搏的数据1******"+String.valueOf(msg.obj) + "");
						setPR(String.valueOf(msg.obj));
					}
				}
			}
				break;
			case StaticReceive.MSG_DATA_ECG_WAVE: {
				Bundle d = msg.getData();
				if (!d.getBoolean("bLeadoff")) {
					setTVtext(tv_MSG, myResources.getString(R.string.measure_lead_off));
				} else {
					tv_MSG.setText("");
				}

				BaseDate.ECGData date = (BaseDate.ECGData) msg.obj;
				List list = date.data;
				for (int i=0;i<list.size();i++){
					BaseDate.Wave wave = (BaseDate.Wave) list.get(i);
					System.err.println(wave.flag+"***"+wave.data);
				}
				System.out.println(list.size()+"--这些心电的数据******"+date.data);
			}
				break;
			case StaticReceive.MSG_DATA_GLU: {
				if (msg.arg2 == 0) { // 0：血糖单位 mmol/L ,glu unit is mmol/L
					bGluUnitMmol = true;
					if (msg.arg1 == 1) { //过低 , low
						setGLU("L");
					} else if (msg.arg1 == 2) { //过高, high
						setGLU("H");
					} else if (msg.arg1 == 0) { //血糖值正常 , normal
						mGLU_Mmol = String.valueOf( (Float)msg.obj );
						setGLU(mGLU_Mmol);
						//（mmol/L)转换为 ->（mg/dl）
						mGLU_Mgdl = switchGLUUnit(false, (Float)msg.obj );
					}
					tv_GLU_UNIT.setText("mmol/L"); 
					
				}else { // 1:mg/dL				
					bGluUnitMmol = false;
					if (msg.arg1 == 1) {
						setGLU("L");
					} else if (msg.arg1 == 2) {
						setGLU("H");
					} else if (msg.arg1 == 0) {
						mGLU_Mgdl = String.valueOf( (Float)msg.obj );
						setGLU(mGLU_Mgdl);			
						// （mg/dl）转换为-> （mmol/L)
						mGLU_Mmol= switchGLUUnit(true, (Float)msg.obj);

					}
					tv_GLU_UNIT.setText("mg/dl");	
				}
				
			}
				break;
			case StaticReceive.MSG_DATA_NIBP_STATUS_CH: {
				if (msg.arg1 == 1) {
					bNIBPIng = true;
					checkNIBPStatus(true);
					setSYS("0");
					setDIA("0");
					if (!bSpo2Ing)
						setPR("0");
				} else if (msg.arg1 == 2) {
					bNIBPIng = false;
					checkNIBPStatus(false);
					setSYS("0");
					setDIA("0");
				}
			}
				break;
			case StaticReceive.MSG_DATA_NIBP_REALTIME: {
				System.out.println("这是血压的数据**********"+msg.arg2+"");
				setSYS(msg.arg2 + "");
				if (msg.arg1 == 0) {
					showPulse(true);
				}
				drawPC300NIBPRect.setNIBP(msg.arg2, false);
			}
				break;
			case StaticReceive.MSG_DATA_NIBP_END: {
				if(menuWindow!=null)
				{
					menuWindow.dismiss();
				}
				bNIBPIng = false;
				checkNIBPStatus(false);
				Bundle data = msg.getData();
				data.getBoolean("bHR");
				int pr = data.getInt("nPulse");
				int sys = data.getInt("nSYS");
				int dia = data.getInt("nDIA");
				int grade = data.getInt("nGrade");
				int err = data.getInt("nBPErr");
				if (err == StatusMsg.NIBP_ERROR_NO_ERROR) {
					System.out.println("血压数据测量完了*******"+sys + "");
					end_value1="";
					end_value2="";
					if(sys>0&&dia>0)
					{
						end_value1 = sys+"";
						end_value2 = dia+"";
						init_net(sys+ "/"+dia, 0);
					}
					setSYS(sys + "");
					setDIA(dia + "");
					if (!bSpo2Ing && !bECGIng) {
						System.out.println("这些脉搏的数据2******"+String.valueOf(msg.obj) + "");
						setPR(pr + "");
						/*if(pr>0)
						{
							init_net(pr+ "", 1);
						}*/
					}
					drawPC300NIBPRect.setNIBP(grade, true);
				} else {
					setSYS("0");
					setDIA("0");
					if (!bSpo2Ing)
						setPR("0");
					if (err == 15)
						err = nibpERR.length - 1;
					drawPC300NIBPRect.setNIBP(0, false);
				}
			}
				break;
			case StaticReceive.MSG_DATA_SPO2_PARA: {
				if(menuWindow!=null)
				{
					menuWindow.dismiss();
				}
				final Bundle d = msg.getData();
				if (!bECGIng) {
					setTVtext(tv_MODE, "Pleth");
				}
				int err = d.getInt("nBPErr");

				if (!d.getBoolean("bProbe")) {
					bSpo2Ing = false;
					//System.out.println("*************脉搏数据****:"+mai_value);
					//System.out.println("*************脉搏数据****:"+yan_value);
					end_value1="";
					end_value2="";
					if(mai_value>0||yan_value>0)
					{
						end_value1 = yan_value+"";
						end_value2 = mai_value+"";
						init_net(mai_value+ "", 13);
					}
					drawRunablePC300.cleanWaveData();
					setSPO2("");
					setPR("");
				} else {
					bSpo2Ing = true;
					//System.out.println("这些心率的数据******" + d.getInt("nSpO2") + "");
					setSPO2(d.getInt("nSpO2") + "");
					//System.out.println("这些脉搏的数据13******" + d.getInt("nPR") + "");
					if (!bECGIng) {
						setPR(d.getInt("nPR") + "");
						//System.out.println("这些脉搏的数据3******" + d.getInt("nPR") + "");
					}
					mai_value =d.getInt("nPR");
					yan_value = d.getInt("nSpO2");
				}
			}
				break;
			case StaticReceive.MSG_DATA_TEMP: {
				if(menuWindow!=null)
				{
					menuWindow.dismiss();
				}
				Bundle d = msg.getData();
				if (d.getInt("nResultStatus") == 0) {
					setTMP(msg.getData().getFloat("nTmp") + "");
				} else if (d.getInt("nResultStatus") == 1) {
					setTMP("L");
				} else if (d.getInt("nResultStatus") == 2) {
					setTMP("H");
				}
				System.out.println("这些温度的数据******" + msg.getData().getFloat("nTmp"));
				float value = msg.getData().getFloat("nTmp");
				System.out.println("这些温度的数据1******" + value);
				if (value>0f)
				{
					end_value1="";
					end_value2="";
					init_net(value+"",2);
				}
			}
				break;
			case RECEIVEMSG_PULSE_OFF: {
				showPulse(false);
			}
				break;
			case StaticReceive.MSG_DATA_PULSE: {
				showPulse(true);
			}
				break;
			case StaticReceive.MSG_FIRMWARE_VER:{
				curVer=msg.arg1;
				// pc200软件版本号低于1402的,不支持固件升级
				// PC-200 which below of version 1402 was unsupported update
				if(curVer<=1402){ //version 1.4.0.2
					bt_update.setVisibility(View.GONE);
					tv_blank.setVisibility(View.VISIBLE); 
				}									
			}
			break;
			case MSG_FIRMWARE_PROGRAM:{
				int progress = msg.arg1;
				mProgressBar.setProgress(progress);
				tv_Data.setText(progress+"/100");
				if(progress == 100){			
					btn_YES.setText("complete");
					Toast.makeText(BootuchActivity.this, R.string.update_complete, Toast.LENGTH_LONG).show();
				}		
			}
			break;
			case StaticReceive.MSG_DATA_UA:{
				float uaMgdl = (Float) msg.obj;
				mUA_Mgdl = String.format(Locale.US, "%.2f",uaMgdl);
				mUA_Mmol = String.format(Locale.US, "%.2f",uaMgdl /16.81f);			
				setUAUnit(bUAMmol);
			}
				break;
			case StaticReceive.MSG_DATA_CHOL:{
				float cholMgdl = (Float) msg.obj;
				mCHOL_Mgdl = String.format(Locale.US, "%.2f",cholMgdl);
				mCHOL_Mmol = String.format(Locale.US, "%.2f",cholMgdl /38.67f);
				setCHOLUnit(bCHOLUnitMmol);		
			}
				break;
			default:break;
			}
		}

	};

	/**
	 * 改变心电按钮状态 , change state of ECG button
	 * 
	 * @param isStart
	 *            测量状态 :true 正在测量, false 没有测量 <br>
	 *            measure state:true measuring, false:no measure
	 */
	private void checkECGStatus(boolean isStart) {
		if (isStart) {
			bt_ECG.setText(R.string.measure_stop);
		} else {
			bt_ECG.setText(R.string.measure_ecg);
		}
	}

	/**
	 * 改变血压按钮状态, change state of NIBP button
	 * 
	 * @param isStart
	 *            测量状态 true 正在测量 false 没有测量 <br>
	 *            measure state:true measuring, false:no measure
	 */
	private void checkNIBPStatus(boolean isStart) {
		if (isStart) {
			bt_BIBP.setText(R.string.measure_stop);
		} else {
			bt_BIBP.setText(R.string.measure_nibp);
		}
	}

	/** 取消搏动标记, cancel pulse flag */
	public static final int RECEIVEMSG_PULSE_OFF = 0x115;

	/**
	 * 设置搏动标记, setting pulse flag
	 */
	private void showPulse(boolean isShow) {
		if (isShow) {
			img_Pulse.setVisibility(View.VISIBLE);
			new Thread() {
				@Override
				public void run() {
					super.run();
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					mHandler.sendEmptyMessage(RECEIVEMSG_PULSE_OFF);
				}
			}.start();
		} else {
			img_Pulse.setVisibility(View.INVISIBLE);
		}
	}

	private int batteryCnt = 0;

	private long batteryTime = 0L;

	private void setBattery(int battery, int cell) {
		if (cell == 0) {
			batteryCnt = 0;
			img_Battery.setImageResource(batteryRes[battery]);
			if (battery == 0) {
				long time = System.currentTimeMillis();
				if (time - batteryTime < 500) {
					return;
				}
				batteryTime = time;
				int visible = img_Battery.isShown() ? View.INVISIBLE : View.VISIBLE;
				img_Battery.setVisibility(visible);
			} else {
				img_Battery.setVisibility(View.VISIBLE);
			}
		} else if (cell == 1) {
			setImgResource(img_Battery, batteryRes_img[batteryCnt]);
			batteryCnt = (batteryCnt + 1) % batteryRes_img.length;
		} else if (cell == 2) {
			batteryCnt = 0;
			setImgResource(img_Battery, batteryRes_img[2]);
		}
	}

	/**
	 * 设置电量图片, set battery image
	 * 
	 * @param img
	 * @param res
	 */
	private void setImgResource(ImageView img, int res) {
		if (!img.isShown()) {
			img.setVisibility(View.VISIBLE);
		}
		img.setImageResource(res);
	}

	private void setSPO2(String data) {
		setTVtext(tv_SPO, data);
	}

	private void setPR(String data) {
		setTVtext(tv_PR, data);
	}

	private void setSYS(String data) {
		setTVtext(tv_SYS, data);
	}

	private void setDIA(String data) {
		setTVtext(tv_DIA, data);
	}

	private void setTMP(String itemp) {
		setTVtext(tv_TMP, itemp + "");
	}

	private void setGLU(String data) {
		setTVtext(tv_GLU, data);
	}


	private void setTVtext(TextView tv, String msg) {
		if (tv != null) {
			if (msg != null) {
				if (msg.equals("0") || msg.equals("") || msg.equals("0.0")) {
					tv.setText(myResources.getString(R.string.const_data_nodata));
				} else {
					tv.setText(msg);
				}
			}
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (drawThread != null && !drawRunablePC300.isPause()) {
			drawRunablePC300.Pause();
		}
		if (drawPC300SPO2RectThread != null && !drawPC300SPO2Rect.isPause()) {
			drawPC300SPO2Rect.Pause();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (drawThread == null) {
			drawThread = new Thread(drawRunablePC300, "DrawPC300Thread");
			drawThread.start();
		} else if (drawRunablePC300.isPause()) {
			drawRunablePC300.Continue();
		}
		if (drawPC300SPO2RectThread == null) {
			drawPC300SPO2RectThread = new Thread(drawPC300SPO2Rect, "DrawPC300RectThread");
			drawPC300SPO2RectThread.start();
		} else if (drawPC300SPO2Rect.isPause()) {
			drawPC300SPO2Rect.Continue();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		sendBroadcast(new Intent(ReceiveService.BLU_ACTION_DISCONNECT));
		if (!drawRunablePC300.isStop()) {
			drawRunablePC300.Stop();
		}
		drawThread = null;
		if (!drawPC300SPO2Rect.isStop()) {
			drawPC300SPO2Rect.Stop();
		}
		drawPC300SPO2RectThread = null;
		stopService(new Intent(this, ReceiveService.class));
	}
	
	/**
	 * android6.0 Bluetooth request 蓝牙检测
	 */
	private static final int REQUEST_FINE_LOCATION=0;
	private void android6_RequestLocation(){
		if (Build.VERSION.SDK_INT >= 23) {
			int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
			if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
				//判断是否需要 向用户解释，为什么要申请该权限
				if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION))
					Toast.makeText(this, R.string.android6_request_location, Toast.LENGTH_LONG).show();

				//请求权限
				ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_FINE_LOCATION);
				return;
			}
		}
	}
	
	private void setGLUUnit(boolean unit) {
		bGluUnitMmol = unit;
		if (bGluUnitMmol) { //切换
			if(StaticReceive.hasUA){
				SpotSendCMDThread.SetBaijieDevice_Unit(0);
			}
			tv_GLU_UNIT.setText("(mmol/L)");
			setGLU(mGLU_Mmol);
		} else {
			if(StaticReceive.hasUA){
				SpotSendCMDThread.SetBaijieDevice_Unit(1);
			}
			tv_GLU_UNIT.setText("(mg/dl)");	
			setGLU(mGLU_Mgdl);
		}
	}
	
	private void setCHOLUnit(boolean unit){
		bCHOLUnitMmol = unit;
		if(bCHOLUnitMmol){
			//enable PC300 termal to show mmol of data, next measure will be avaliable
			//下次测量数据，终端才会显示响应的单位
			SpotSendCMDThread.SetBaijieDevice_Unit(0);
			tv_chol_unit.setText(getString(R.string.const_chol_mmol_text));
			setTVtext(tv_chol, mCHOL_Mmol);
		}else{
			//enable PC300 termal to show mgdl of data
			SpotSendCMDThread.SetBaijieDevice_Unit(1);
			tv_chol_unit.setText(getString(R.string.const_chol_mgdl_text));
			setTVtext(tv_chol, mCHOL_Mgdl);
		}
	}
	
	private void setUAUnit(boolean unit){
		bUAMmol = unit;
		if(bUAMmol){
			SpotSendCMDThread.SetBaijieDevice_Unit(0);
			tv_ua_unit.setText(getString(R.string.const_ua_mmol_text));
			setTVtext(tv_ua, mUA_Mmol);
		}else{
			SpotSendCMDThread.SetBaijieDevice_Unit(1);
			tv_ua_unit.setText(getString(R.string.const_ua_mgdl_text));
			setTVtext(tv_ua, mUA_Mgdl);
		}
	}
	
	/**
	 * 血糖单位转换
	 * 
	 * @param isL  <br>
	 *            true 将data转换为  （mmol/L)毫摩尔/升 ,data->mmol/L <br>
	 *            false 将data转换为  （mg/dl）毫克/分升 ,data->mg/dl <br>
	 *            
	 * @param data :GLU Value
	 * @return
	 */
	public String switchGLUUnit(boolean isL, float data) {	
		if (isL) {// mmol/L
			return String.format(Locale.US, "%.1f", (data / 18f)); //将float数据转换为精度为0.1
		} else {
			return String.format(Locale.US, "%.1f", (data * 18));
		}
	}
		
	/** 升级进度 */
	public static final int MSG_FIRMWARE_PROGRAM = 0x215;	
	
	class IAPCallBack implements IIAPCallBack {

		@Override
		public void onIAP_enter() {
		}

		@Override
		public void onIAP_start() {
		}

		@Override
		public void onIAP_programing(int program) {
			mHandler.obtainMessage(MSG_FIRMWARE_PROGRAM, program,0).sendToTarget();
		}

		@Override
		public void onIAP_check(String err) {
			
		}

		@Override
		public void onIAP_end() {
			StaticReceive.Continue();
			StaticReceive.QueryDeviceVer();
			System.out.println("mainActivity -->onIAP_end");
		}		
	}

	private void init_net(String mvalue,int mtype){
		final View view = LayoutInflater.from(this).inflate(R.layout.sendpop, null);
		AppCompatTextView send_t1 = view.findViewById(R.id.send_t1);//打开相册
		AppCompatTextView send_t2 =  view.findViewById(R.id.send_t2);//打开照相机
		AppCompatTextView send_ok =  view.findViewById(R.id.send_ok);//打开照相机
		AppCompatTextView send_cancle =  view.findViewById(R.id.send_cancel);//打开照相机

		menuWindow = new PopupWindow(view);

		menuWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
		menuWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha=0.5f;
		getWindow().setAttributes(lp);

		menuWindow.setBackgroundDrawable(this.getResources().getDrawable(R.color.white));
		menuWindow.setOutsideTouchable(true);
		menuWindow.setFocusable(true);
		menuWindow.showAtLocation(view, Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
		value = mvalue;
		type = mtype;
		if(mtype==0)
		{
			send_t1.setText("收缩压:"+end_value1);
			send_t2.setText("舒张压:"+end_value2);
		}else if(mtype==1)
		{
			send_t1.setText("血氧值:"+mvalue);
			send_t2.setVisibility(View.INVISIBLE);
		}else if(mtype==2)
		{
			send_t1.setText("体温值:"+mvalue);
			send_t2.setVisibility(View.INVISIBLE);
		}else if(mtype==13)
		{
			send_t1.setText("血氧/脉搏:"+end_value1+"/"+end_value2);
			send_t2.setVisibility(View.INVISIBLE);
			value =end_value2;
		}
		menuWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha=1f;
				getWindow().setAttributes(lp);
			}
		});

		send_cancle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				WindowManager.LayoutParams lp =getWindow().getAttributes();
				lp.alpha=1f;
				getWindow().setAttributes(lp);
				menuWindow.dismiss();
			}
		});

		send_ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				WindowManager.LayoutParams lp =getWindow().getAttributes();
				lp.alpha=1f;
				getWindow().setAttributes(lp);
				if(type ==13)
				{
					new_put_data(value,3);
					if(!end_value1.equals(""))
					{
						new_put_data(end_value1,1);
					}
				}else {
					new_put_data(value,type);
				}
				menuWindow.dismiss();
			}
		});
	}

	private void new_put_data(String value, final int type)
	{
		RestClent.builder()
				.url("signs_input")
				.params("region_id",TIGE_REGION_ID)
				.params("gov_id",TIGE_GOV_ID)
				.params("user_id",TIGE_USERID)
				.params("doctor_id",TIGE_DOCTOR_ID)
				.params("record_id",record_id)
				.params("value",value)
				.params("type",type)
				.success(new ISuccess() {
					@Override
					public void onSuccess(String response) {
						System.out.println("数据上传成功*****"+response);
					}
				})
				.build()
				.post();
	}

}
