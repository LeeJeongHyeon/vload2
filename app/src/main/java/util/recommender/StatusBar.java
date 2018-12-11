package util.recommender;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;

import vmp.company.vexmall.R;

public class StatusBar {

    private static StatusBar instance;

    private StatusBar(){}

    public static StatusBar getInstance(){
        if(instance == null){
            instance = new StatusBar();
        }
        return instance;
    }


    // 상단 상태바 색상 조정
    public void setStatusBar(Activity activity, int colorId ) {
        View view = activity.getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (view != null) {
                view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                activity.getWindow().setStatusBarColor(activity.getResources().getColor(colorId));
            }
        }
    }
}
