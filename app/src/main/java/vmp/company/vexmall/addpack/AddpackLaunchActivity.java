package vmp.company.vexmall.addpack;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.kyad.adlibrary.AppAllOfferwallSDK;

import util.category.LogTag;
import vmp.company.vexmall.R;

public class AddpackLaunchActivity extends AppCompatActivity implements AppAllOfferwallSDK.AppAllOfferwallSDKListener {

    private final String TAG = LogTag.getTag(this);
    String stKey = "";
    String stId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpack_launch);
        if(getIntent().hasExtra("id")) {

            stKey = getIntent().getStringExtra("key");
            stId = getIntent().getStringExtra("id");

            AppAllOfferwallSDK.getInstance().initOfferWall(AddpackLaunchActivity.this, stKey, stId);

            Log.d(TAG, "stKey : " + stKey + ", id : " + stId);
        }

    }

    @Override
    public void AppAllOfferwallSDKCallback(int i) {
        switch (i) {
            case AppAllOfferwallSDK.AppAllOfferwallSDK_SUCCES:
//                Toast.makeText(this, "성공", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "성공");
                if (AppAllOfferwallSDK.getInstance().showAppAllOfferwall(this)) {
                    //성공
                    finish();
                } else {
                    Toast.makeText(this, "SDK initialization error.", Toast.LENGTH_SHORT).show();
                }
                break;
            case AppAllOfferwallSDK.AppAllOfferwallSDK_INVALID_USER_ID:
//                Toast.makeText(this, "잘못 된 유저아이디입니다.", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "잘못 된 유저아이디입니다.");
                break;
            case AppAllOfferwallSDK.AppAllOfferwallSDK_INVALID_KEY:
//                Toast.makeText(this, "오퍼월 KEY를 확인해주세요.", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "오퍼월 KEY를 확인해주세요");
                break;
            case AppAllOfferwallSDK.AppAllOfferwallSDK_NOT_GET_ADID:
//                Toast.makeText(this, "고객님의 폰으로는 무료충전소를 이용하실 수 없습니다. 고객센터에 문의해주세요.", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "고객님의 폰으로는 무료충전소를 이용하실 수 없습니다. 고객센터에 문의해주세요.");
                break;
        }
    }
}
