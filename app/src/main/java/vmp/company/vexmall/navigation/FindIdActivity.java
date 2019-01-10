package vmp.company.vexmall.navigation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import util.MySingleton;
import util.category.LogTag;
import util.recaptcha.APIExamCaptchaImage;
import util.recaptcha.APIExamCaptchaNkey;
import util.recaptcha.APIExamCaptchaNkeyResult;
import util.recaptcha.CaptchaInfomation;
import vmp.company.vexmall.R;

public class FindIdActivity extends AppCompatActivity {

    APIExamCaptchaNkey nkey;
    APIExamCaptchaImage nImage;
    APIExamCaptchaNkeyResult nResult;
    ImageView f_random;
    LinearLayout recaptcha;
    FrameLayout f_back; //회원정보 찾기 뒤로가기 버튼
    String randomCheck;
    private final String URL = "http://vmp.company/vexMall/back/password_lost2.php";
    final private String TAG = LogTag.getTag(this);

    EditText f_randomcheck;
    EditText f_et1;

    TextView join;
    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id);
        //캡챠관련 객체 생성
        nkey = new APIExamCaptchaNkey();
        nImage = new APIExamCaptchaImage();
        nResult = new APIExamCaptchaNkeyResult();

        //캡차 키 발급
        new CapchaNKeyAsync().execute();


        initUI();
        setListeners();
    }
    private void initUI(){
        Log.d(TAG, "initUI() begins to run...");
        join = findViewById(R.id.f_find);
        f_back = findViewById(R.id.f_back);


        //캡챠 이미지뷰
        f_random = findViewById(R.id.f_random);
        GradientDrawable drawable = (GradientDrawable)getDrawable(R.drawable.capcha_img_round);
        f_random.setBackground(drawable);
        f_random.setClipToOutline(true);

        //캡챠 이미지 새로고침
        recaptcha = findViewById(R.id.s2_iv3);

       f_randomcheck = (EditText) findViewById(R.id.f_randomcheck);
       f_et1 = (EditText) findViewById(R.id.f_et1);
    }
   private void setListeners() {
       Log.d(TAG, "setListeners() begins to run...");


       f_back.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(FindIdActivity.this, LoginActivity.class);
               startActivity(intent);
           }
       });

       join.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               randomCheck = f_randomcheck.getText().toString();
               String find_email = f_et1.getText().toString().trim();

               if (find_email.isEmpty()) {
                   Toast.makeText(FindIdActivity.this, "이메일을 입력해 주세요.", Toast.LENGTH_SHORT).show();

               } else if (randomCheck.isEmpty()) {
                   Toast.makeText(FindIdActivity.this, "자동등록방지 문자를 입력해주세요.", Toast.LENGTH_SHORT).show();
               } else {
                   new FindAsync().execute(find_email);
               }
           }
       });

       //캡차 이미지 새로고침
       recaptcha.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               f_randomcheck.setText("");
               //캡차 키 발급
               new CapchaNKeyAsync().execute();
           }
       });
   }



    //캡차 키 발급
    class CapchaNKeyAsync extends AsyncTask<Void, Void, String> {

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
            f_random.setImageBitmap(s);
        }
    }//CapthaNKey


    class FindAsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... voids) {

            findId(voids[0]);
            return null;
        }
    }

    public void findId(final String find_email) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray obj = new JSONArray(response);
                            String stResult = obj.getJSONObject(0).getString("result");


                            if ("true".equals(stResult)) {
                                Toast.makeText(FindIdActivity.this, "고객님의 아이디와 비밀번호를\n재설정 할 수 있는 메일을 전송했습니다.", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(FindIdActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(FindIdActivity.this, stResult, Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }

        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("mb_email", find_email);
                params.put("key", CaptchaInfomation.result_key);
                params.put("inVal", randomCheck);

                return params;
            }
        };
        stringRequest.setTag(TAG);
        MySingleton.getInstance(FindIdActivity.this).addToRequestQueue(stringRequest);
    }
}