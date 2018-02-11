package microtech.hxswork.com.frame_ui.main.szxing;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import microtech.hxswork.com.frame_core.middle.MiddleFragment;
import microtech.hxswork.com.frame_ui.R;
import microtech.hxswork.com.frame_ui.main.szxing.utils.FileUtil;
import microtech.hxswork.com.frame_ui.main.szxing.utils.HttpUtil;
import microtech.hxswork.com.frame_ui.main.szxing.utils.NetUtil;
import microtech.hxswork.com.frame_ui.main.szxing.views.CameraHelper;
import microtech.hxswork.com.frame_ui.main.szxing.views.MaskSurfaceView;
import microtech.hxswork.com.frame_ui.main.szxing.views.OnCaptureCallback;

import static microtech.hxswork.com.frame_ui.main.szxing.clent.Demo.BeiJingapitestAsyncTest;
import static microtech.hxswork.com.frame_ui.main.szxing.utils.ImageToBase64.imgToBase64;


public class ACameraFragment extends MiddleFragment implements OnCaptureCallback{
	protected static final String tag = "ACameraActivity";
	private SurfaceView mSurfaceView;
	private ProgressBar pb;
	private ImageButton mShutter;
	private SurfaceHolder mSurfaceHolder;
	private String flashModel = Parameters.FLASH_MODE_OFF;
	private byte[] jpegData = null;
	private String base64Data= null;
	private String filepath ;
	private MaskSurfaceView surfaceView=null;
	@SuppressLint("HandlerLeak")
	private Handler mHandler=new Handler(){
		
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(getContext(), "拍照失败", Toast.LENGTH_SHORT).show();
				break;
			case 1:
				jpegData= FileUtil.getByteFromPath((String) msg.obj);
				base64Data = imgToBase64((String) msg.obj);
				if(jpegData!=null && jpegData.length>0){
					pb.setVisibility(View.VISIBLE);
					new Thread(new Runnable() {//开启身份证验证
						@Override
						public void run() {
						if((jpegData.length>(1000*1024*5))){
							mHandler.sendMessage(mHandler.obtainMessage(3, "图片太大!"));
							return;
						}	
						String result=null;
						boolean isavilable= NetUtil.isNetworkConnectionActive(getContext());
						if(isavilable){
							System.out.println( "到这部了***************************");
							BeiJingapitestAsyncTest(base64Data);

						}else{
							mHandler.sendMessage(mHandler.obtainMessage(3, "无网络，请确定网络是否连接!"));
							pb.setVisibility(View.GONE);
						}
						}
					}).start();
				}
				break;
			case 3:
				pb.setVisibility(View.GONE);
				String str=msg.obj+"";
				Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
				mShutter.setEnabled(true);
				break;
			case 4:
				mShutter.setEnabled(true);
				pb.setVisibility(View.GONE);
				String result=msg.obj+"";
				Intent intent=new Intent();
				intent.putExtra("result", result);
				/*setResult(Activity.RESULT_OK,intent);
				finish();*/
				break;
			case 5:
				String filePath=msg.obj+"";
				byte[] data= FileUtil.getByteFromPath(filePath);

				if(data!=null && data.length>0){
					mHandler.sendMessage(mHandler.obtainMessage(1,filePath));
				}else{
					mHandler.sendMessage(mHandler.obtainMessage(0));
				}
				break;
			case 6:
				Toast.makeText(getContext(), "请插入存储卡！", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	@Override
	public Object setLayout() {
		return R.layout.ocr_camera;
	}

	@Override
	public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
		initViews();
		if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			Toast.makeText(getContext(), "请插入存储卡", Toast.LENGTH_LONG).show();
			getSupportDelegate().pop();
		}
		File dir = new File(CameraManager.strDir);
		if(!dir.exists()){
			dir.mkdir();
		}
	}

	private void initViews() {
		pb =  bind(R.id.reco_recognize_bar);
		mShutter = bind(R.id.camera_shutter);
		surfaceView =bind(R.id.surfaceView);

		WindowManager manager = getActivity().getWindowManager();
		DisplayMetrics outMetrics = new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(outMetrics);
		int width = outMetrics.widthPixels;
		int height = outMetrics.heightPixels;

		//surfaceView.setMaskSize(width-200,height-400);
/*		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(oncallback);
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);*/
		mShutter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CameraHelper.getInstance().getHandler(mHandler);
				CameraHelper.getInstance().tackPicture(ACameraFragment.this);
				mShutter.setEnabled(false);

			}
		});
	}


	/**
	 * 删除图片文件呢
	 */
	private void deleteFile(){
		if(this.filepath==null || this.filepath.equals("")){
			return;
		}
		File f = new File(this.filepath);
		if(f.exists()){
			f.delete();
		}
	}

	@Override
	public void onCapture(boolean success, String filePath) {
		filepath = filePath;
		System.out.println("啊速度啦时间来得及ask来得及看==========="+filePath);
		Message msg = new Message();
		msg.what = 5;
		msg.obj =filepath;
		System.out.println("图片的路径为****"+filepath);
		mHandler.sendMessage(msg);
	}
}
