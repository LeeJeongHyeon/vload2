package util;

import android.graphics.Paint;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.widget.TextView;

/**
 * Project   : VexMall
 * Packages  : util
 * Author    : Marty
 * Date      : 2018-12-13 / 오후 6:05
 * Comment   :
 */
public class Utils {


    public static SpannableString MakeUnderLine(String msg){
        SpannableString content = new SpannableString("Content");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        return content;
    }

    public static void setCancelLine(TextView textView){
        textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }
}
