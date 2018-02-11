package microtech.hxswork.com.frame_ui.main.update;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Set;

/**
 * 
 * 保存系统信息
 * @author song
 * @date 2015年11月4日
 */
public class SystemParams {

	private static SystemParams instance;
	private static SharedPreferences sharedPrederences = null;

	private SystemParams() {
	}

	//在Application初始化
	public static void init(Context context) {
		sharedPrederences = context.getSharedPreferences("hobbees", Context.MODE_PRIVATE);
	}
	public static SystemParams getInstance() {
		
		if(instance == null) {
			synchronized (SystemParams.class) {
				if(instance == null) {
					 instance = new SystemParams();
				}
			}
		}
		return instance;
	}
	
	/**get**/
	public int getInt(String key){
		return sharedPrederences.getInt(key, 0);
	}
	
	public int getInt(String key,int defValue){
		return sharedPrederences.getInt(key, defValue);
	}	

	public float getFloat(String key){
		return sharedPrederences.getFloat(key, 0);
	}
	
	public float getFloat(String key,float defValue) {
		return sharedPrederences.getFloat(key, defValue);
	}	
	
	public long getLong(String key){
		return sharedPrederences.getLong(key, 0);
	}
	
	public long getLong(String key,long defValue) {
		return sharedPrederences.getLong(key, defValue);
	}		

	public String getString(String key){
		return sharedPrederences.getString(key, null);
	}
	
	public String getString(String key,String defValue) {
		return sharedPrederences.getString(key, defValue);
	}	
	
	public boolean getBoolean(String key){
		return sharedPrederences.getBoolean(key, false);
	}
	
	public boolean getBoolean(String key,boolean defValue) {
		return sharedPrederences.getBoolean(key, defValue);
	}
	
	/**set**/
	public void setInt(String key,int value) {
		Editor editor = sharedPrederences.edit();
		editor.putInt(key, value);
		editor.commit();
	}
	
	public void setFloat(String key,float value) {
		Editor editor = sharedPrederences.edit();
		editor.putFloat(key, value);
		editor.commit();
	}
	
	public void setLong(String key,long value) {
		Editor editor = sharedPrederences.edit();
		editor.putLong(key, value);
		editor.commit();
	}
	
	public void setString(String key,String value) {
		Editor editor = sharedPrederences.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	public void setBoolean(String key,boolean value) {
		Editor editor = sharedPrederences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	
	public void setSetString(String key,Set<String> values) {
		Editor editor = sharedPrederences.edit();
		editor.putStringSet(key, values);
		editor.commit();
	}
	
	public void remove(String key) {
		Editor editor = sharedPrederences.edit();
		editor.remove(key);
		editor.commit();
	}
	
	public void clear() {
		Editor editor = sharedPrederences.edit();
		editor.clear().commit();
	}
}
