package vmp.company.vexmall.signup;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import util.category.LogTag;
import util.recommender.StatusBar;
import vmp.company.vexmall.R;


public class SignupActivity extends AppCompatActivity {
    final private String TAG = LogTag.getTag(this);
    RelativeLayout s1_layout;
    TextView s1_join;
    CheckBox s1_cb1;
    CheckBox s1_cb2;
    CheckBox s1_cb3;
    FrameLayout s1_back;

    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        StatusBar.getInstance().setStatusBar(this, R.color.signup);  // 화면 상단 색상
        initUI();
        setListeners();
    }

    protected void initUI() {
        Log.d(TAG, "initUI() begins to run...");
        s1_layout = findViewById(R.id.s1_layout);
        s1_join = findViewById(R.id.s1_join);
        s1_cb1 = findViewById(R.id.s1_cb1);
        s1_cb2 = findViewById(R.id.s1_cb2);
        s1_cb3 = findViewById(R.id.s1_cb3);
        s1_back = findViewById(R.id.s_back);
    }

    protected void setListeners() {
        Log.d(TAG, "setListeners() begins to run...");
        s1_cb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (s1_cb1.isChecked()) {
                    s1_cb2.setChecked(true);
                    s1_cb3.setChecked(true);
                } else {
                    s1_cb2.setChecked(false);
                    s1_cb3.setChecked(false);
                }
                checkAvailiable();
            }
        });

        s1_cb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    s1_cb1.setChecked(false);
                }
                checkAvailiable();
            }
        });
        s1_cb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    s1_cb1.setChecked(false);
                }
                checkAvailiable();
            }
        });
        s1_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        s1_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkAvailiable()) {
                    goToSignupActivity2();
                } else {
                    Toast.makeText(getApplicationContext(), "전체동의가 필요합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean checkAvailiable() {
        Drawable drawable = null;
        if (s1_cb1.isChecked() || (s1_cb2.isChecked() && s1_cb3.isChecked())) {
            s1_cb1.setChecked(true);
            s1_cb2.setChecked(true);
            s1_cb3.setChecked(true);
            drawable = getApplicationContext().getDrawable(R.color.signup);
            s1_join.setBackground(drawable);
            return true;
        }
        drawable = getApplicationContext().getDrawable(R.color.disabled);
        s1_join.setBackground(drawable);
        return false;
    }

    // 수신 뒤 할 일
    private void goToSignupActivity2() {

        Intent inviteIntent = getIntent();
        Intent intent = new Intent(SignupActivity.this, SignupActivity2.class);
        if (inviteIntent != null) {
            intent.putExtra("recommender_id", inviteIntent.getStringExtra("recommender_id"));
        }
        startActivity(intent);
    }
}