package vmp.company.vexmall.signup;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import util.category.LogTag;
import util.recommender.StatusBar;
import vmp.company.vexmall.navigation.LoginActivity;
import util.MySingleton;
import vmp.company.vexmall.R;

public class SignupActivity3 extends AppCompatActivity {
    private final String URL = "http://vmp.company/vexMall/back/memberJoin.php";
    private final String TAG = LogTag.getTag(this);

//    APIExamCaptchaNkey nkey;
//    APIExamCaptchaImage nImage;
//    APIExamCaptchaNkeyResult nResult;
//    ImageView s3_6_captcha_image;
//    LinearLayout s3_7_captcha_refresh;
//    EditText s3_7_captcha_input;

    RelativeLayout s3_layout;
    Spinner s3_1_select_bank;
    EditText s3_2_account_holder;
    EditText s3_3_account_number;
    CheckBox s3_4_accept_mailing_service;
    CheckBox s3_5_accept_sms_service;

    TextView s3_join;
    FrameLayout s3_back;

    String stCaptchaInput;
    String[] extras;
    Intent intent;


    String bankName;
    String mb_accountHolder;
    String mb_accountNumber;
    String mb_mailing;
    String mb_sms;

    protected void initUI() {
        Log.d(TAG, "initUI() begins to run...");
        String[] banks = getResources().getStringArray(R.array.bank_name);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, banks);
        s3_1_select_bank = findViewById(R.id.s3_1_select_bank);
        s3_1_select_bank.setAdapter(adapter);


        s3_layout = findViewById(R.id.s3_layout);
        s3_2_account_holder = findViewById(R.id.s3_2_account_holder);
        s3_3_account_number = findViewById(R.id.s3_3_account_number);
        s3_4_accept_mailing_service = findViewById(R.id.s3_4_accept_mailing_service);
        s3_5_accept_sms_service = findViewById(R.id.s3_5_accept_sms_service);
        s3_join = findViewById(R.id.s3_join);
        s3_back = findViewById(R.id.s_back);

        s3_2_account_holder.addTextChangedListener(onKey);
        s3_3_account_number.addTextChangedListener(onKey);
    }

    protected void setListeners() {
        Log.d(TAG, "setNavListeners() begins to run...");
        s3_1_select_bank.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        bankName = (String) s3_1_select_bank.getAdapter().getItem(position);
                        if (position == 0) {
                            bankName = "";
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        Toast.makeText(getApplicationContext(), "은행을 선택해 주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        s3_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        s3_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mb_accountHolder = s3_2_account_holder.getText().toString().trim();
                mb_accountNumber = s3_3_account_number.getText().toString().trim();
                mb_mailing = s3_4_accept_mailing_service.isChecked() ? "1" : "0";
                mb_sms = s3_5_accept_sms_service.isChecked() ? "1" : "0";

                if (!checkBank()) {
                    Toast.makeText(getApplicationContext(), "은행을 선택해 주세요.", Toast.LENGTH_SHORT).show();
                } else if (!checkAccounteHolder()) {
                    Toast.makeText(getApplicationContext(), "예금주를 정확히 입력해 주세요.", Toast.LENGTH_SHORT).show();
                } else if (!checkAccountNumber()) {
                    Toast.makeText(getApplicationContext(), "계좌번호를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                } else {

                    intent = getIntent();
                    extras = new String[]{
                            "mb_password",
                            "mb_password_re",
                            "accountType",
                            "reg_mb_recommender",
                            "reg_mb_recommender_no",
                            "mb_name",
                            "mb_hp",
                            "mb_email",
                            "mb_birth",
                            "mb_zip",
                            "mb_addr1",
                            "mb_addr2"
                    };
                    new JoinAsync().execute();
                }
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup3);
        StatusBar.getInstance().setStatusBar(this, R.color.signup);  // 화면 상단 색상
//        //캡챠관련 객체 생성
//        nkey = new APIExamCaptchaNkey();
//        nImage = new APIExamCaptchaImage();
//        nResult = new APIExamCaptchaNkeyResult();

//        캡차 키 발급
//        new capchaNKeyAsync().execute();

        initUI();
        setListeners();

    }

    //    //캡차 키 발급
//    class capchaNKeyAsync extends AsyncTask<Void, Void, String> {
//
//        @Override
//        protected String doInBackground(Void... voids) {
//            return nkey.getNkey();
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            CaptchaInfomation.result_key = s;
//            Log.i("MY", "result_key:" + CaptchaInfomation.result_key);
//            //발급받은 키로 이미지 생성
//            new CapchaImageAsync().execute();
//            Log.i("MY", "result_key2:" + CaptchaInfomation.result_key);
//        }
//    }//CapthaNKey
//
//    //발급받은 키로 이미지 생성
//    class CapchaImageAsync extends AsyncTask<Void, Void, Bitmap> {
//
//        @Override
//        protected Bitmap doInBackground(Void... voids) {
//            return nImage.getCaptchaImage();
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap s) {
//            s3_6_captcha_image.setImageBitmap(s);
//        }
//    }//CapthaNKey
//
    class JoinAsync extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            sendJoin();
            return null;
        }
    }

    public void sendJoin() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray obj = new JSONArray(response);
                            String stResult = obj.getJSONObject(0).getString("result");


                            if (!stResult.equals("true")) {
                                Toast.makeText(getApplicationContext(), stResult, Toast.LENGTH_SHORT).show();
                            } else {
                                String mb_id = obj.getJSONObject(0).getString("mb_id");
                                Toast.makeText(getApplicationContext(), "회원가입에 감사드립니다.\n귀하의 아이디 : " + mb_id, Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(SignupActivity3.this, LoginActivity.class);
                                intent.putExtra("mb_id", mb_id);
                                startActivity(intent);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error
            }

        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                // 이전 페이지에 입력한 데이터(이름, 이메일 등)
                if (intent != null) {
                    for (String param : extras) {
                        params.put(param, intent.getExtras().getString(param));
                    }
                }
                params.put("bankName", bankName);
                params.put("mb_accountHolder", mb_accountHolder);
                params.put("mb_accountNumber", mb_accountNumber);
                params.put("mb_mailing", mb_mailing);
                params.put("mb_sms", mb_sms);
                params.put("num", AuthHpDialog.user_num);
                return params;
            }
        };
        stringRequest.setTag(TAG);
        MySingleton.getInstance(SignupActivity3.this).addToRequestQueue(stringRequest);
    } // sendJoin

    TextWatcher onKey = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            //입력되는 텍스트에 변화가 있을때
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            //입력이 끝났을 때
            checkAvailable();
        }

        private void checkAvailable() {
            bankName = s3_1_select_bank.getSelectedItem().toString();
            mb_accountHolder = s3_2_account_holder.getText().toString().trim();
            mb_accountNumber = s3_3_account_number.getText().toString().trim();
//            stCaptchaInput = s3_7_captcha_input.getText().toString().trim();
            mb_mailing = s3_4_accept_mailing_service.isChecked() ? "1" : "0";
            mb_sms = s3_5_accept_sms_service.isChecked() ? "1" : "0";

            if (checkBank() && checkAccounteHolder() && checkAccountNumber() /*&& checkStCaptcha()*/) {
                Drawable drawable = getApplicationContext().getDrawable(R.color.signup);
                s3_join.setBackground(drawable);
            } else {
                Drawable drawable = getApplicationContext().getDrawable(R.color.disabled);
                s3_join.setBackground(drawable);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            //입력하기 전에 호출
        }
    };

    boolean checkBank() {
        return s3_1_select_bank.getSelectedItemPosition() != 0;
    }

    boolean checkAccounteHolder() {
        return mb_accountHolder.length() >= 2 && Pattern.matches("^[a-zA-Z가-힣]*$", mb_accountHolder);
    }

    boolean checkAccountNumber() {
        return !mb_accountNumber.isEmpty() && Pattern.matches("^[0-9]*$", mb_accountNumber);
    }
}
