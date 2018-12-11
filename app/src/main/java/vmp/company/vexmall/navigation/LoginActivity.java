package vmp.company.vexmall.navigation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONException;
import org.json.JSONObject;

import util.category.LogTag;
import util.recommender.StatusBar;
import vmp.company.vexmall.main.MainActivity;
import util.MySingleton;
import vmp.company.vexmall.R;
import vmp.company.vexmall.signup.SignupActivity;


public class LoginActivity extends AppCompatActivity {


    final private String TAG = LogTag.getTag(this);
    EditText etLogin = null;
    EditText etPassword = null;
    ToggleButton toggleAutoLogin = null;
    String sId;
    TextView btnLogin;
    TextView btnFind;
    TextView btnSignup;
    SharedPreferences pref;

    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pref = PreferenceManager.getDefaultSharedPreferences(this);

        // 상단 상태바 색상 조정
        StatusBar.getInstance().setStatusBar(this, R.color.openingTop);  // 화면 상단 색상
        initUI();
        setListeners();

        Intent in = getIntent();
        if (in != null) {
            sId = in.getStringExtra("mb_id");
            etLogin.setText(sId);
        }
    }


    public void login(final String stLogin, final String stPassword) {
        String url = "http://vmp.company/vexMall/back/loginCheck.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response

                        Log.d(TAG, "login Response : " + response);
                        try {

                            JSONObject obj = new JSONObject(response);
                            String stResult = obj.getString("result");

                            if (!stResult.equals("false")) {
                                String mb_id = obj.getString("mb_id");
                                String mb_name = obj.getString("mb_name");
                                String accountType = obj.getString("accountType");

                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("mb_id", mb_id);
                                editor.putString("mb_name", mb_name);
                                editor.putString("accountType", accountType);

                                if (toggleAutoLogin.isChecked()) {
                                    editor.putBoolean("autoLogin", true);
                                    editor.putString("auto_login_id", mb_id);
                                } else {
                                    editor.remove("autoLogin");
                                    editor.remove("auto_login_id");
                                }
                                editor.apply();
                                Toast.makeText(LoginActivity.this, "로그인에 성공했습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(LoginActivity.this, "비밀번호 혹은 아이디를 확인해주세요.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d(TAG, "Error Response : " + error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mb_info", stLogin);
                params.put("mb_passwd", stPassword);

                return params;
            }
        };

        stringRequest.setTag(TAG);

        MySingleton.getInstance(LoginActivity.this).addToRequestQueue(stringRequest);

    }

    private void initUI() {
        Log.d(TAG, "initUI() begins to run...");
        // 로그인 텍스트
        etLogin = (EditText) findViewById(R.id.etLogin);
        etLogin.setText(pref.getString("mb_id",""));


        // 비밀번호 텍스트
        etPassword = (EditText) findViewById(R.id.etPassword);

        // 자동 로그인 토글
        toggleAutoLogin = (ToggleButton) findViewById(R.id.toggleAutoLogin);

        // 회원가입
        btnSignup = (TextView) findViewById(R.id.l_find);

        // 아이디,비번 찾기
        btnFind = (TextView) findViewById(R.id.l_find2);

        // 로그인 버튼
        btnLogin = (TextView) findViewById(R.id.btnLogin);

    }

    private void setListeners() {
        Log.d(TAG, "setNavListeners() begins to run...");

        // 회원가입
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
        // 아이디,비번 찾기
        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, FindIdActivity.class);
                startActivity(intent);
            }
        });
        // 로그인 버튼
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stLogin = etLogin.getText().toString();
                String stPassword = etPassword.getText().toString();
                if (stLogin.isEmpty() || stPassword.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "로그인 정보를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    login(stLogin, stPassword);
                }
            }
        });
    }

}
