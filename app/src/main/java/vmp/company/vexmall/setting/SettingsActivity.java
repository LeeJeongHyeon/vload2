package vmp.company.vexmall.setting;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import util.recommender.StatusBar;
import vmp.company.vexmall.R;

public class SettingsActivity extends AppCompatActivity {
    RelativeLayout settings1_layout;
    TextView settings1_goto_vendormap_tv;   // 벤더맵 설정하기
    FrameLayout s1_back;
    String mb_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // 사용자 아이디 받아오기
        mb_id = PreferenceManager.getDefaultSharedPreferences(this).getString("mb_id", "");
        // 상단 상태바 색상 조정
        StatusBar.getInstance().setStatusBar(this, R.color.settings);  // 화면 상단 색상
        initUI();
        setListeners();
    }

    protected void initUI() {
        settings1_layout = findViewById(R.id.settings1_layout);
        settings1_goto_vendormap_tv = findViewById(R.id.settings1_goto_vendormap_tv);
        s1_back = findViewById(R.id.s1_back);
    }

    protected void setListeners() {

        settings1_goto_vendormap_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mb_id.isEmpty()){
                    Toast.makeText(getApplicationContext(), "로그인 후 이용하실 수 있습니다.", Toast.LENGTH_LONG).show();
                } else {
//                    Toast.makeText(getApplicationContext(), "준비중입니다.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SettingsActivity.this, SettingsActivity2.class);
                    startActivity(intent);
                }
            }
        });
        s1_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


}
