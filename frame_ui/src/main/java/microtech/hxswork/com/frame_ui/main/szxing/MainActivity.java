package microtech.hxswork.com.frame_ui.main.szxing;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

import microtech.hxswork.com.frame_core.middle.MiddleFragment;


public class MainActivity extends MiddleFragment {
	private static final String tag = "BootuchActivity";
	private int REQUEST_CODE=1;
	private TextView tvResult;
	private static byte[] bytes;
	private static String extension;
	private final int IMPORT_CODE=1;
	private final int TAKEPHOTO_CODE=2;
	private LinearLayout ll_progress;
	public static final String action="idcard.scan";

	@Override
	public Object setLayout() {
		return null;
	}

	@Override
	public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {

	}
	/**
	 * 拍照
	 */
	@SuppressLint("WrongConstant")
	@Override
	public void onActivityResult(int arg0, int arg1, Intent data) {
		super.onActivityResult(arg0, arg1, data);
		if(data==null){
			return;
		}
		Uri uri = data.getData();
		if(arg1==Activity.RESULT_OK){
			switch (arg0) {
			case IMPORT_CODE:
				if(uri==null){
					return;
				}
				try {
					Log.d("bytes:  ", bytes.length+"");
					if(!(bytes.length>(1000*1024*5))){
						new MyAsynTask().execute();
					}else{
						Toast.makeText(getContext(), "图片太大！！！", 1).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case TAKEPHOTO_CODE:
				if(tvResult.getVisibility()==View.GONE){
					tvResult.setVisibility(View.VISIBLE);
				}
				tvResult.setText("");
				String result = data.getStringExtra("result");
				Log.d(tag, "result:  "+result);
				tvResult.setText(result);
				break;
			}
		}
	}
	
	/**
	 * 根据路径获取文件扩展名
	 * @param path
	 */
	private String getExtensionByPath(String path) {
		if(path!=null){
			return path.substring(path.lastIndexOf(".")+1);
		}
		return null;
	}



	class MyAsynTask extends AsyncTask<Void, Void, String>{
		
		@Override
		protected void onPreExecute() {
			ll_progress.setVisibility(View.VISIBLE);
		}

		@Override
		protected String doInBackground(Void... params) {
			return startScan();
		}
		
		@Override
		protected void onPostExecute(String result) {
			System.out.println("result:   "+result);
			if(result!=null){
				ll_progress.setVisibility(View.GONE);
				handleResult(result);
			}
		}

	}
	
	/**
	 * 处理服务器返回的结果
	 * @param result
	 */
	private void handleResult(String result) {
		tvResult.setVisibility(View.VISIBLE);
		tvResult.setText(result);
	}
	
	public static String startScan(){
		//String xml = HttpUtil.getSendXML(action,extension);
		return "测试***";
	}
}
