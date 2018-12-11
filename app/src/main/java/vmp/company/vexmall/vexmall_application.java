package vmp.company.vexmall;

import android.app.Application;

import com.igaworks.IgawCommon;

public class vexmall_application extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        IgawCommon.autoSessionTracking(vexmall_application.this);
        // 어플리케이션 클래스에서는 autoSessionTracking API 외의 어떤 애드브릭스 API도 호출해서는 안됩니다.
    }
}
