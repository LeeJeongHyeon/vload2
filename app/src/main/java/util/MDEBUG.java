package util;

import android.util.Log;

import vmp.company.vexmall.BuildConfig;

/**
 * Project   : VexMall
 * Packages  : util
 * Author    : Marty
 * Date      : 2018-12-11 / 오후 2:06
 * Comment   :
 */
public class MDEBUG {
    public static void debug (String msg){
        if (BuildConfig.DEBUG)
            Log.d("<Debug>>>>",buildLogMsg(msg));
    }

    private static String buildLogMsg(String message) {

        StackTraceElement ste = Thread.currentThread().getStackTrace()[4];

        StringBuilder sb = new StringBuilder();

        sb.append("[");
        sb.append(ste.getFileName().replace(".java", ""));
        sb.append("::");
        sb.append(ste.getMethodName());
        sb.append("]");
        sb.append(message);

        return sb.toString();

    }
}
