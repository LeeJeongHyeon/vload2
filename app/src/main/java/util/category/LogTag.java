package util.category;

import android.app.Activity;
import android.app.Fragment;

public class LogTag {
    public static String getTag(Object o){
        return "Sera's " + o.getClass().getSimpleName();
    }
}
