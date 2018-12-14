package vmp.company.vexmall;

import android.app.Application;

import com.igaworks.IgawCommon;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class vexmall_application extends Application {
    private static Retrofit retrofit = null;

    public Retrofit getRetrofit() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://vmp.company/vexMall/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }
    vexmall_application getAPP(){
        return this;
    }
    @Override
    public void onCreate(){
        super.onCreate();
        IgawCommon.autoSessionTracking(vexmall_application.this);
        // 어플리케이션 클래스에서는 autoSessionTracking API 외의 어떤 애드브릭스 API도 호출해서는 안됩니다.
    }
}
