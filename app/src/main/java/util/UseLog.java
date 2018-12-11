package util;

import android.util.Log;

import util.category.LogTag;

public class UseLog {

    final private String TAG = LogTag.getTag(this);
    public static final boolean isVisible = false;

    public static void setLog( String tag, String msg, boolean isVisible ){

        if( isVisible ){
            Log.d(tag, msg);
        }
    }

}
