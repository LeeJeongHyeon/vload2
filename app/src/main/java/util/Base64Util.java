package util;

import java.io.UnsupportedEncodingException;
import android.util.Base64;
/**
 * <pre>
 * net.sjava.android.util.Base64Util.java
 * </pre>
 *
 * @author : mcsong@gmail.com
 * @version :
 * @data : 2011. 10. 2. 오후 3:34:23
 *
 */
public class Base64Util {

    /**
     * Encode txt
     * @param txt
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String encode(String txt) throws UnsupportedEncodingException {
        byte[] data = txt.getBytes("UTF-8");
        return Base64.encodeToString(data, Base64.DEFAULT);
    }

    /**
     * Decode txt
     * @param txt
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String decode(String txt) throws UnsupportedEncodingException {
        return new String(Base64.decode(txt, Base64.DEFAULT),"UTF-8");
    }
}