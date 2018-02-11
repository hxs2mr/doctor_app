package microtech.hxswork.com.frame_ui.main.szxing.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;


public class HttpUtil {
	
		//获取版本号
		private static String OSVersion=android.os.Build.VERSION.RELEASE;

	    private static String mxh=android.os.Build.MODEL;// 获取手机型号
	    //访问服务器地址
	    public static String ENGINE_URL="http://www.yunmaiocr.com/SrvXMLAPI";
		public static final String USERNAME="84653ec2-5ab9-4952-8144-d6429a6297f5";//开发者平台API帐号
		public static final String PASSWORD="oWWklddDkkDHoLxNIxkyGASxLAWCgf";//开发者平台API密码
		
		public static final String connFail="确认网络连接是否正常！";
		
		/**
		 * 获得发送请求的xml
		 * @param action
		 * @param ext 图片格式
		 * @return
		 */
		
		public static String getSendXML(String action,String ext){
			ArrayList<String[]> arr = new ArrayList<String[]>();
			String key = UUID.randomUUID().toString();
			String time = String.valueOf(System.currentTimeMillis());
			String verify = MD5(action+USERNAME+key+time+PASSWORD);
			arr.add(new String[] { "action", action});
			arr.add(new String[] { "client", USERNAME});
			arr.add(new String[] { "system", OSVersion+mxh});
			arr.add(new String[] { "password", MD5(PASSWORD)});
			arr.add(new String[] { "key", key });	
			arr.add(new String[] { "time",time});
			arr.add(new String[] { "verify", verify });
			arr.add(new String[] { "ext",ext });
			return getXML(arr,false);
		}
		
		public static String getXML(ArrayList<String[]> arr,boolean IsUpper) {
			if (arr == null || arr.size() == 0)
				return "";
			StringBuffer sb = new StringBuffer();
			String tag="";
			for (int idx = 0; idx < arr.size(); idx++) {
				tag=arr.get(idx)[0];
				if(IsUpper){
					tag=tag.toUpperCase();
				}
				sb.append("<");
				sb.append(tag);
				sb.append(">");
				sb.append(arr.get(idx)[1]);
				//sb.append(XMLFunctions.code(arr.get(idx)[1]));
				sb.append("</");
				sb.append(tag);
				sb.append(">");
			}
			return sb.toString();
		}
		
/*		@SuppressWarnings("unused")
		public static String send(String xml,byte[] file){
			byte[] dest = new byte[xml.getBytes().length+file.length+"<file></file>".getBytes().length];
			int pos = 0;
			System.arraycopy(xml.getBytes(), 0, dest, pos, xml.getBytes().length);
			pos += xml.getBytes().length;
			System.arraycopy("<file>".getBytes(), 0, dest, pos, "<file>".getBytes().length);
			pos += "<file>".getBytes().length;
			System.arraycopy(file, 0, dest, pos, file.length);
			pos += file.length;
			System.arraycopy("</file>".getBytes(), 0, dest, pos, "</file>".getBytes().length);
			String requestString = new String(dest);
			try {
				return httpClient(ENGINE_URL, dest);
			} catch (IOException e) {
				return "-1";
			}
		}*/
		
		@SuppressWarnings("finally")
		/*public static String httpClient(String url,byte[] content) throws IOException{
			HttpClient httpClient = new HttpClient();
			HttpClientParams httparams = new HttpClientParams();
			httpClient.setParams(httparams);
			httpClient.setConnectionTimeout(10000);
			httpClient.setTimeout(10000);
			
			PostMethod method = new PostMethod(url);
			RequestEntity requestEntity = new ByteArrayRequestEntity(content);
			method.setRequestEntity(requestEntity);
			String responseBody = null;
			try {
				method.getParams().setContentCharset("utf-8");
				method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
				int statusCode = httpClient.executeMethod(method);
				if (statusCode != HttpStatus.SC_OK) {
					System.out.println("\r\nMethod failed: " + method.getStatusLine() + ",url:\r\n" + url + "\r\n");
				}
				StringBuffer resultBuffer = new StringBuffer();
				BufferedReader in = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(),
						method.getResponseCharSet()));
				String inputLine = null;
				while ((inputLine = in.readLine()) != null) {
					resultBuffer.append(inputLine);
					resultBuffer.append("\r\n");
				}
				in.close();
				responseBody = resultBuffer.toString().trim();
			}catch(ConnectTimeoutException ex){
				responseBody = connFail;
			}catch(SocketTimeoutException e){
				responseBody = connFail;
			}catch(UnknownHostException e){
				responseBody = connFail;
			}catch (Exception e) {
				System.out.println(">>> http请求异常，url=" + url);
				Log.d("tag", "-->"+e);
				e.printStackTrace();
				responseBody = "-2";
			} finally {
				if (method != null) {
					method.releaseConnection();
					method = null;
				}
				return responseBody;
			}

		}*/
	
	public static byte[] Inputstream2byte(InputStream is) {
		if (is == null)
			return null;
		return WriteFromStream(is);
	}
	
	public final static String MD5(String pwd) {
		//用于加密的字符
		char md5String[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] btInput = pwd.getBytes();
			
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			mdInst.update(btInput);
			byte[] md = mdInst.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {   //  i = 0
				byte byte0 = md[i];  //95
				str[k++] = md5String[byte0 >>> 4 & 0xf];    //    5  
				str[k++] = md5String[byte0 & 0xf];   //   F
			}
			return new String(str);
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 将inoutsreame写入到ByteArrayOutputStream
	 * @param is 输入流
	 * @return 
	 * @throws IOException
	 */
	public static byte[] WriteFromStream(InputStream is){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		try {
			while((len = is.read(buffer))!=-1){
				baos.write(buffer, 0, len);
			}
			is.close();
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return baos.toByteArray();
	}

}
