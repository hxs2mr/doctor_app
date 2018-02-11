package microtech.hxswork.com.frame_ui.main.szxing.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class ImageUtil {
	/**
	 * ��תBitmap
	 * @param b
	 * @param rotateDegree
	 * @return
	 */
	public static Bitmap getRotateBitmap(Bitmap b, float rotateDegree){
		Matrix matrix = new Matrix();
		matrix.postRotate((float)rotateDegree);
		Bitmap rotaBitmap = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, false);
		return rotaBitmap;
	}
}
