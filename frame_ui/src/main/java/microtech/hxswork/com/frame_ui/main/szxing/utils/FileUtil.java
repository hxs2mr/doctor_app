package microtech.hxswork.com.frame_ui.main.szxing.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class FileUtil {
	
	public static String newImageName(){
		String uuidStr = UUID.randomUUID().toString();
		return uuidStr.replaceAll("-", "") + ".jpg";
	}

	@SuppressWarnings("resource")
	public static byte[] getByteFromPath(String filePath) {
		byte[] bytes=null;
		try {
			File file = new File(filePath);
			if(file.length()>Integer.MAX_VALUE){
				throw new IOException("File is to large :"+file.getName());
			}
			if(file.exists()){
				int offset = 0;
				int numRead = 0;
				InputStream is = new FileInputStream(file);
				bytes=new byte[(int)file.length()];
				while(offset < bytes.length && 
						(numRead = is.read(bytes, offset, bytes.length - offset)) >= 0){
					offset += numRead;
				}
				if (offset < bytes.length) {
					throw new IOException("Could not completely read file "
							+ file.getName());
				}
				is.close();
				return bytes;
			}
		}catch(Exception e) {
			e.printStackTrace();
			bytes=null;
		}
		finally{
			bytes = null;
		}
		
		return null;
	}

}
