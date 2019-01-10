package vmp.company.vexmall.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import util.recommender.StatusBar;
import vmp.company.vexmall.R;

public class OpeningActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening);
        StatusBar.getInstance().setStatusBar(this, R.color.openingTop);  // 화면 상단 색상
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(OpeningActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }

        }, 2000);


    }

}
