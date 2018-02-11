package microtech.hxswork.com.frame_ui.main.szxing.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageToBase64
{
	/**
	 *
	 * @param imgPath
	 * @param
	 * @param
	 * @return
	 */
	public static String imgToBase64(String imgPath) {
		Bitmap bitmap = null;
		if (imgPath !=null && imgPath.length() > 0) {
			bitmap = readBitmap(imgPath);
		}
		if(bitmap == null){
			//bitmap not found!!
		}
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

			out.flush();
			out.close();

			byte[] imgBytes = out.toByteArray();
			return Base64.encodeToString(imgBytes, Base64.DEFAULT);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		} finally {
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static Bitmap readBitmap(String imgPath) {
		try {
			return BitmapFactory.decodeFile(imgPath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		}

	}

	/**
	 *
	 * @param base64Data
	 * @param imgName
	 * @param imgFormat 图片格式
	 */
	public static void base64ToBitmap(String base64Data,String imgName,String imgFormat) {
		byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
		Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

		File myCaptureFile = new File("/sdcard/", imgName);
		FileOutputStream fos = null;
		try
		{
			fos = new FileOutputStream(myCaptureFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean isTu = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
		if (isTu) {
			// fos.notifyAll();
			try {
				fos.flush();
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
