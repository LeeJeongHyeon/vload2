package vmp.company.vexmall.vendormap;

import android.app.ProgressDialog;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import util.Property;
import util.VexMallCallback;
import vmp.company.vexmall.vexmall_application;

/**
 * Project   : VexMall
 * Packages  : vmp.company.vexmall.vendormap
 * Author    : Marty
 * Date      : 2018-12-14 / 오후 5:35
 * Comment   :
 */
public class SampleCode extends AppCompatActivity {

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 화면생성시 로딩시작
        progressDialog.show();
        //로딩 후 위치정보 가져오기 세팅한후 위치정보 동기식 실행
    }

    void getLocation(Location location){
        // 임의로 만든 메소드이지만  lat,lng 주소를 가져왔을떄 (Google FusedLocationService 를통해서)
        // 호출되는 콜백이라고 가정합니다.  ( 위치정보 동기식 완료되었을때를 가정합니다)
        RetrofitSampleCode(location);
    }

    void RetrofitSampleCode(Location location){
        String lat = location.getLatitude() + "",lng = location.getLongitude() + "";
        int distance = Property.SHOP_VENDORMAP_DISTANCE;
        SampleApi API = ((vexmall_application)getApplication()).getRetrofit().create(SampleApi.class);

        ///  VexMallCallback은 제가 따로 Callback을 커스텀해서 사용하는거라
        /// 저걸 보고 쓰셔도 되고  오리지널을 쓰셔도 됩니다.
        API.getStores(lat,lng,distance).enqueue(new VexMallCallback(this,(call,response)->{
            // 데이터 수신성공
            if (response.isSuccessful()) {
                ArrayList<SampleDataClass_VO> arrayList = (ArrayList<SampleDataClass_VO>) response.body();

            }else{ /// 데이터 통신실패

            }
            // 로딩 해제
            progressDialog.dismiss();
        }));


    }
}
