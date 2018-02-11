package microtech.hxswork.com.frame_core.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.widget.Toast;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
import java.util.WeakHashMap;

import microtech.hxswork.com.frame_core.util.token.Token;

/**
 * Created by microtech on 2017/12/18.
 */

public class Mac {

    private TelephonyManager tm;
    public String uniqueId = "";
    Activity activity;
    public String androidId = "", phone_id = "";
    public  String tmDevice="", tmSerial="";
    public String os_id = "5.0";//系统版本

    public  LocationManager locationManager;
    public  Location location;
    public String locationProvider;//获取经纬度
    public double d_j = 0.0;
    public double d_w = 0.0;
    private WeakHashMap<String,Object> list;

    public Mac(Activity mactivity) {
        this.activity = mactivity;
        tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        getUuid();
    }

    public String getMac() {//获取手机的mac地址
        try {
            for (Enumeration e = NetworkInterface.getNetworkInterfaces(); e.hasMoreElements(); ) {
                NetworkInterface item = (NetworkInterface) e.nextElement();
                byte[] mac = item.getHardwareAddress();
                if (mac != null && mac.length > 0) {
                    return byte2hex(mac);
                }
            }
        } catch (Exception e) {
        }
        return "";
    }

    public static String byte2hex(byte[] b) {
        StringBuffer hs = new StringBuffer(b.length);
        String stmp = "";
        int len = b.length;
        for (int n = 0; n < len; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            if (stmp.length() == 1)
                hs = hs.append("0").append(stmp);
            else {
                hs = hs.append(stmp);
            }
        }
        return String.valueOf(hs);
    }

    @SuppressLint("HardwareIds")
    public void getUuid() {

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

                    ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            return;
        }

        tmDevice = "" + tm.getDeviceSoftwareVersion();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(activity.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        os_id = Build.VERSION.RELEASE;//系统版本
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        phone_id = Build.MODEL;//手机型号
        uniqueId = deviceUuid.toString();//设备号
        getloaction();
    }

    public void getloaction() {
        //获取地理位置管理器
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        //获取所有可用的位置提供器
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是GPS
            locationProvider = LocationManager.NETWORK_PROVIDER;
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                ActivityCompat.requestPermissions(activity, new String[]{ android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
                return;
            }
            location = locationManager.getLastKnownLocation(locationProvider);
                if(location != null) {
                    d_j = location.getLongitude();
                    d_w = location.getLatitude();
                }else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
                    //如果是Network
                    locationProvider = LocationManager.NETWORK_PROVIDER;
                    location = locationManager.getLastKnownLocation(locationProvider);
                    if(location != null) {
                        d_j = location.getLongitude();
                        d_w = location.getLatitude();
                    }
                } else {
                    Toast.makeText(activity, "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
                }
            }
        }


}
