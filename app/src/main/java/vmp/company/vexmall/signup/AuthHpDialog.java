package vmp.company.vexmall.signup;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import util.MySingleton;
import util.category.LogTag;
import util.recaptcha.APIExamCaptchaImage;
import util.recaptcha.APIExamCaptchaNkey;
import util.recaptcha.APIExamCaptchaNkeyResult;
import util.recaptcha.CaptchaInfomation;
import vmp.company.vexmall.R;

public class AuthHpDialog extends Dialog {
    private final String TAG = LogTag.getTag(this);
    LinearLayout s2_auth_outer_ll1;
    LinearLayout s2_auth_outer_ll2;



    TextView s2_auth_mb_hp_tv;
    ImageView s2_auth_captcha_iv;
    LinearLayout s2_auth_captcha_refresh_ll;
    EditText s2_auth_captcha_input_et;
    TextView s2_auth_sending_tv;

    TextView s2_auth_step2_message_tv;
    TextView s2_auth_step2_message2_tv;
    EditText s2_auth_num_input_et;
    TextView s2_auth_sending_final_tv;
    String mb_hp;

    ///////////////문자 인증/////////////////
    String key;   // 네이버 API로 생성한 캡챠 키를 전달 바람.
    String inVal; // 사용자가 캡챠 이미지 보고 입력한 문자 전달.
    String result; // 값이 true면 성공. true 아니면 값으로 에러메시지가 담김.
    String num; // 회원에게 문자로 전송한 SMS 인증번호 4자리가 값으로 담김.
    public static String user_num; // 회원이 직접 입력한 4자리 인증번호
    APIExamCaptchaNkey nkey;
    APIExamCaptchaImage nImage;
    APIExamCaptchaNkeyResult nResult;
    ////////////////////////////////////////

    EditText s2_12_contact;

    // Constructor
    public AuthHpDialog(Context context, EditText s2_12_contact, String mb_hp) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Log.d(TAG, "AuthHpDialog() begins to start...");
        setContentView(R.layout.signup_mb_hp_auth_dialog);

        //캡챠관련 객체 생성
        nkey = new APIExamCaptchaNkey();
        nImage = new APIExamCaptchaImage();
        nResult = new APIExamCaptchaNkeyResult();

        //캡차 키 발급
        new AuthHpDialog.capchaNKeyAsync().execute();

        this.mb_hp = mb_hp;
        this.s2_12_contact = s2_12_contact;
        initUI();
        setListeners();
    }


    private void initUI() {
        Log.d(TAG, "initUI() begins to start...");
        s2_auth_outer_ll1 = findViewById(R.id.s2_auth_outer_ll1);
        s2_auth_outer_ll2 = findViewById(R.id.s2_auth_outer_ll2);

        s2_auth_step2_message_tv = findViewById(R.id.s2_auth_step2_message_tv);
        s2_auth_step2_message2_tv = findViewById(R.id.s2_auth_step2_message2_tv);
        s2_auth_num_input_et = findViewById(R.id.s2_auth_num_input_et);
        s2_auth_sending_final_tv = findViewById(R.id.s2_auth_sending_final_tv);

        s2_auth_mb_hp_tv = findViewById(R.id.s2_auth_mb_hp_tv);
        s2_auth_mb_hp_tv.setText(mb_hp);
        s2_auth_captcha_iv = findViewById(R.id.s2_auth_captcha_iv);
        s2_auth_captcha_refresh_ll = findViewById(R.id.s2_auth_captcha_refresh_ll);
        s2_auth_captcha_input_et = findViewById(R.id.s2_auth_captcha_input_et);
        s2_auth_sending_tv = findViewById(R.id.s2_auth_sending_tv);
        GradientDrawable drawable =
                (GradientDrawable) getContext().getDrawable(R.drawable.capcha_img_round);
    }

    private void setListeners() {

        //캡차 이미지 새로고침
        s2_auth_captcha_refresh_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "캡챠 새로고침 is clicked!");
                s2_auth_captcha_input_et.setText("");
                //캡차 키 발급
                new AuthHpDialog.capchaNKeyAsync().execute();
            }
        });
        s2_auth_captcha_input_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {  }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "문자 인증 텍스트 has changed!");
                if(!s2_auth_captcha_input_et.getText().toString().trim().isEmpty()){
                    s2_auth_sending_tv.setBackground(getContext().getDrawable(R.color.signup));
                } else {
                    s2_auth_sending_tv.setBackground(getContext().getDrawable(R.color.disabled));
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {  }
        });
        s2_auth_sending_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "step1 인증문자 전송 is clicked!");
                inVal = s2_auth_captcha_input_et.getText().toString().trim();
                if (inVal.isEmpty()) {
                    Toast.makeText(getContext(), "자동등록방지 문자를 입력해주세요.", Toast.LENGTH_LONG).show();
                    s2_auth_captcha_input_et.requestFocus();
                } else {
                    new AuthAsync().execute();
                }
            }
        });
        s2_auth_num_input_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {  }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                user_num = s2_auth_num_input_et.getText().toString().trim();
                Log.d(TAG, "인증문자 입력 텍스트 has changed! user_num : " + user_num);
                if(user_num.length() == 4 && user_num.equals(num)){
                    s2_auth_sending_final_tv.setBackground(getContext().getDrawable(R.color.signup));
                } else {
                    s2_auth_sending_final_tv.setBackground(getContext().getDrawable(R.color.disabled));
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {  }
        });


        s2_auth_sending_final_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "step2 최종확인 전송 is clicked!");
                if(user_num.length() == 4 && user_num.equals(num)){
                    s2_12_contact.setEnabled(false);
                    Toast.makeText(getContext(), "문자인증이 완료되었습니다.\n회원가입 화면으로 돌아갑니다.", Toast.LENGTH_LONG).show();
                    dismiss();
                } else {
                    Toast.makeText(getContext(), "인증에 실패하였습니다.\n인증문자를 다시 확인해주세요.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    //캡차 키 발급
    class capchaNKeyAsync extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            return nkey.getNkey();
        }

        @Override
        protected void onPostExecute(String s) {
            CaptchaInfomation.result_key = s;
            //발급받은 키로 이미지 생성
            new CapchaImageAsync().execute();
        }
    }//CapthaNKey

    //발급받은 키로 이미지 생성
    class CapchaImageAsync extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... voids) {
            return nImage.getCaptchaImage();
        }

        @Override
        protected void onPostExecute(Bitmap s) {
            s2_auth_captcha_iv.setImageBitmap(s);
        }


    }//CapthaNKey

    class AuthAsync extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            requestAuthNumber();
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        dismiss();
    }

    // 회원가입시 문자 인증번호 전송 요청을 처리하고 결과로 전송한 인증번호를 알려줌
    private void requestAuthNumber() {
        String url = "http://vmp.company/vexMall/back/memberJoinSMS.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            JSONObject obj = new JSONObject(response);
                            result = obj.getString("result");
                            Log.d(TAG, "requestAuthNumber().response : " + response);
                            if (!result.equals("true")) {
                                Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
                            } else {
                                String message = "'" + mb_hp + "'로 전송한 인증번호 4자리를" ;
                                String message2 ="입력해주세요.";
                                num = obj.getString("num");
                                s2_auth_step2_message_tv.setText(message);
                                s2_auth_step2_message2_tv.setText(message2);
                                s2_auth_outer_ll1.setVisibility(View.GONE);
                                s2_auth_outer_ll2.setVisibility(View.VISIBLE);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
//               mb_hp : 휴대폰 번호 전달 바람. 하이픈 없이
//               key : 네이버 API로 생성한 캡챠 키를 전달 바람.
//               inVal : 사용자가 캡챠 이미지 보고 입력한 문자 전달.
                Map<String, String> params = new HashMap<String, String>();

                params.put("mb_hp", mb_hp);
                params.put("key", CaptchaInfomation.result_key);
                params.put("inVal", inVal);
                Log.d(TAG, "param : " + mb_hp + ", " + key + ", "  +inVal);
                return params;
            }
        };

        stringRequest.setTag(TAG);

        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

}
