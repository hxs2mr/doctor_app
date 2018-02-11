package microtech.hxswork.com.frame_core.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by microtech on 2017/12/18.密码的MD5加密
 */

public class MD5 {
    public static String MD51(String plainText ) {
        StringBuffer buf = new StringBuffer("");
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();
            int i;
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if(i<0) i+= 256;
                if(i<16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            System.out.println("result: " + buf.toString());//32位的加密

            System.out.println("result: " + buf.toString().substring(8,24));//16位的加密

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return   buf.toString();
    }

}
