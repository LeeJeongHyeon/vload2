package vmp.company.vexmall.signup;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import util.MySingleton;
import util.Permission;
import util.category.LogTag;
import util.recommender.RecVO;
import util.recommender.StatusBar;
import vmp.company.vexmall.R;


public class SignupActivity2 extends AppCompatActivity {

    final private String TAG = LogTag.getTag(this);

    Dialog recommender_dialog;

    LinearLayout s2_layout;  // 전체 레이아웃
    EditText s2_1_password; // 비밀번호
    EditText s2_2_repassword; // 비밀번호 확인
    CheckBox s2_3_is_cu; // 계정 종류 - 일반
    CheckBox s2_4_is_vd; // 계정 종류 - 판매자

    CheckBox s2_5_have_no_recommender;
    EditText s2_6_search_recommender_text;
    Button s2_7_search_recommender;
    EditText s2_8_recommender_name;
    EditText s2_9_recommender_email;
    EditText s2_10_recommender_id;
    ArrayList<RecVO> recList;//추천인 목록

    EditText s2_11_name;            // 이름
    EditText s2_12_contact;         // 연락처
    TextView s2_12_2_sending_auth_num_tv; // 인증번호 전송
    EditText s2_13_email;           // 이메일
    EditText s2_14_birth;           // 생년월일(6자리)
    EditText s2_15_zipcode;         // 우편번호
    Button s2_16_search_zipcode;    // 우편번호 검색 버튼
    EditText s2_17_address1;        // 주소
    EditText s2_18_address2;        // 상제 주소
    FrameLayout s2_back;
    TextView s2_join;               // 정보 입력하기 버튼 (최하위 버튼)

    LinearLayout s2_19_recommender_list;

    String accountType = "";
    String stPassword, stPasswordCheck;
    String recommenderName, recommenderId, recommenderEmail;
    String mb_name, mb_hp = "", mb_email, mb_birth, mb_zip, mb_addr1, mb_addr2;

    boolean isAutoRecommend = false;


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

        @Override
        public void afterTextChanged(Editable editable) {
            //입력하기 전에 호출
        }
    };

    ArrayList<CheckVO> checkList = new ArrayList<>();

    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);

        StatusBar.getInstance().setStatusBar(this, R.color.signup);  // 화면 상단 색상
        initUI();
        setLinsteners();

        Intent intent = getIntent();
        if(intent!=null){
            new Permission().checkPermission(SignupActivity2.this); // 리캡챠 위한 권한 확인
            recommenderId = intent.getStringExtra("recommender_id");
            Log.d(TAG, "S2 get recommender's id is found in def preference : " + recommenderId);
            isAutoRecommend = recommenderId != null && !recommenderId.isEmpty();
            searchRecommender("아이디", recommenderId);
        }
    }

    protected void initUI() {
        Log.d(TAG, "initUI() begins to run...");
        // 전체 레이아웃
        s2_layout = findViewById(R.id.s2_layout);

        //  비밀번호 입력 + 재입력
        s2_1_password = findViewById(R.id.s2_1_password); // 비밀번호
        s2_2_repassword = findViewById(R.id.s2_2_repassword); // 비밀번호 확인

        s2_1_password.setTransformationMethod(PasswordTransformationMethod.getInstance());

        // 계정 종류
        s2_3_is_cu = findViewById(R.id.s2_3_is_cu); // 계정 종류 - 일반
        s2_4_is_vd = findViewById(R.id.s2_4_is_vd); // 계정 종류 - 판매자

        // '추천인 없음' 기능 활성화 되면 주석 해제해서 사용하세요.
        // s2_5_have_no_recommender = findViewById(R.id.s2_5_have_no_recommender);
        s2_6_search_recommender_text = findViewById(R.id.s2_6_search_recommender_text);
        s2_7_search_recommender = findViewById(R.id.s2_7_search_recommender);
        s2_8_recommender_name = findViewById(R.id.s2_8_recommender_name);
        s2_9_recommender_email = findViewById(R.id.s2_9_recommender_email);
        s2_10_recommender_id = findViewById(R.id.s2_10_recommender_id);

        // 개인정보
        s2_11_name = findViewById(R.id.s2_11_name);
        s2_12_contact = findViewById(R.id.s2_12_contact);
        s2_12_2_sending_auth_num_tv = findViewById(R.id.s2_12_2_sending_auth_num_tv);
        s2_13_email = findViewById(R.id.s2_13_email);
        s2_14_birth = findViewById(R.id.s2_14_birth);
        s2_15_zipcode = findViewById(R.id.s2_15_zipcode);
        s2_16_search_zipcode = findViewById(R.id.s2_16_search_zipcode);
        s2_17_address1 = findViewById(R.id.s2_17_address1);
        s2_18_address2 = findViewById(R.id.s2_18_address2);

        s2_8_recommender_name.setEnabled(false);
        s2_9_recommender_email.setEnabled(false);
        s2_10_recommender_id.setEnabled(false);
        s2_15_zipcode.setEnabled(false);
        s2_17_address1.setEnabled(false);


        s2_join = findViewById(R.id.s2_join);

        s2_layout.setClickable(true);
        s2_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAvailable();
            }
        });
        s2_back = findViewById(R.id.s_back);
        s2_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        s2_1_password.addTextChangedListener(onKey);
        s2_2_repassword.addTextChangedListener(onKey);
        s2_11_name.addTextChangedListener(onKey);
        s2_12_contact.addTextChangedListener(onKey);
        s2_13_email.addTextChangedListener(onKey);
        s2_14_birth.addTextChangedListener(onKey);
        s2_15_zipcode.addTextChangedListener(onKey);
        s2_17_address1.addTextChangedListener(onKey);
        s2_18_address2.addTextChangedListener(onKey);
        s2_1_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    protected void setLinsteners() {
        Log.d(TAG, "setListeners() begins to run...");
        s2_3_is_cu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    s2_4_is_vd.setChecked(false);
                    accountType = "CU";
                } else if (!s2_4_is_vd.isChecked()) {
                    accountType = "";
                }
            }
        });
        s2_4_is_vd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    s2_3_is_cu.setChecked(false);
                    accountType = "VD";
                } else if (!s2_3_is_cu.isChecked()) {
                    accountType = "";
                }
            }
        });

        // '추천인 없음' 기능 활성화 되면 주석 해제해서 사용하세요.
//        s2_5_have_no_recommender.setOnCheckedChangeListener(
//                new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//                        haveNoRecommender = isChecked;
//                        s2_6_search_recommender_text.setEnabled(!isChecked);
//                    }
//                }
//        );
        s2_7_search_recommender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mb_info = s2_6_search_recommender_text.getText().toString().trim();
                if (mb_info.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "추천인 이름, 아이디 혹은 이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    s2_6_search_recommender_text.requestFocus();
                } else {
                    String type;
                    if (Pattern.matches("^[0-9]+$", mb_info)) {
                        type = "아이디";
                    } else if (mb_info.contains("@")) {
                        type = "이메일";
                    } else {
                        type = "이름";
                    }
                    searchRecommender(type, mb_info);
                }
            }
        });

        // 커스텀 다이얼로그 호출할 클릭 이벤트 리스너 정의 ( 문자 인증 )
        s2_12_2_sending_auth_num_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mb_hp.isEmpty()){
                    Toast.makeText(getApplicationContext(), "휴대폰 번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    AuthHpDialog authHpDialog = new AuthHpDialog(SignupActivity2.this, s2_12_contact, mb_hp);
                    authHpDialog.show();
                }
            }
        });


        // 커스텀 다이얼로그 호출할 클릭 이벤트 리스너 정의
        s2_16_search_zipcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DaumZipDialog daumZipDialog = new DaumZipDialog(SignupActivity2.this, R.layout.activity_daum_zip, s2_15_zipcode, s2_17_address1, R.color.signup);
                daumZipDialog.show();
                s2_18_address2.requestFocus();
            }
        });


        s2_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckVO wrongVo = checkAvailable();
                if (wrongVo != null) {
                    String message = "";
                    message = wrongVo.column + "(을)를 확인해주세요.";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    if (wrongVo.view instanceof EditText) {
                        ((EditText) wrongVo.view).setText("");
                    }
                    wrongVo.view.requestFocus();

                } else {
                    Intent intent = new Intent(SignupActivity2.this, SignupActivity3.class);
                    intent.putExtra("mb_password", stPassword);
                    intent.putExtra("mb_password_re", stPasswordCheck);
                    intent.putExtra("accountType", accountType);
                    intent.putExtra("reg_mb_recommender", recommenderName);
                    intent.putExtra("reg_mb_recommender_no", recommenderId);
                    intent.putExtra("reg_mb_recommender_mail", recommenderEmail);
                    intent.putExtra("mb_name", mb_name);
                    intent.putExtra("mb_hp", mb_hp);
                    intent.putExtra("mb_email", mb_email);
                    intent.putExtra("mb_birth", mb_birth);
                    intent.putExtra("mb_zip", mb_zip);
                    intent.putExtra("mb_addr1", mb_addr1);
                    intent.putExtra("mb_addr2", mb_addr2);
                    startActivity(intent);
                }
            }
        });
    }


    // 잘못된 항목을 찾아 해당 CheckVo를 return, 모두 정상일 경우 null을 return;
    private CheckVO checkAvailable() {
        Drawable drawable = getApplicationContext().getDrawable(R.color.disabled);

        stPassword = s2_1_password.getText().toString().trim();
        stPasswordCheck = s2_2_repassword.getText().toString().trim();

        recommenderName = s2_8_recommender_name.getText().toString().trim();
        recommenderEmail = s2_9_recommender_email.getText().toString().trim();
        recommenderId = s2_10_recommender_id.getText().toString().trim();

        mb_name = s2_11_name.getText().toString().trim();
        mb_hp = s2_12_contact.getText().toString().trim();
        mb_email = s2_13_email.getText().toString().trim();
        mb_birth = s2_14_birth.getText().toString().trim();
        mb_zip = s2_15_zipcode.getText().toString().trim();
        mb_addr1 = s2_17_address1.getText().toString().trim();
        mb_addr2 = s2_18_address2.getText().toString().trim();

        setCheckList();

        for (CheckVO vo : checkList) {
            if (!vo.check) {
                checkList.clear();
                s2_join.setBackground(drawable);
                return vo;
            }
        }

        drawable = getApplicationContext().getDrawable(R.color.signup);
        s2_join.setBackground(drawable);
        return null;


    }



    // 추천인 검색 받아오기
    public void searchRecommender(final String type, final String mb_info) {
        String url = "http://vmp.company/vexMall/back/recommenderLookup.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response

                        try {

                            recommender_dialog = new Dialog(SignupActivity2.this);
                            recommender_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            recommender_dialog.setContentView(R.layout.signup_recommender_list_dialog);

                            s2_19_recommender_list = recommender_dialog.findViewById(R.id.s2_19_recommender_list);
                            s2_19_recommender_list.setVisibility(View.VISIBLE); // 테이블 보여주기

                            JSONArray array = new JSONArray(response); //JSONArray형식으로 파싱하기 위해 새로 선언해주며 eventArray를 집어 넣어준다.
                            JSONObject obj = null;

                            LayoutInflater linf = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                            View v = linf.inflate(R.layout.signup_recommender_nie_set, null);

                            TextView tv_message = recommender_dialog.findViewById(R.id.s2_20_message);
                            LinearLayout nie_set = v.findViewById(R.id.nie_set);//추천인 검색시 표현할 이름, 아이디, 이메일
                            LinearLayout recommender_records = recommender_dialog.findViewById(R.id.s2_20_recommender_records);

                            if (array.length() == 0) {
                                tv_message.setVisibility(View.VISIBLE);
                                nie_set.setVisibility(View.GONE);
                                Log.d("MY", "empty");
                                recommender_dialog.show();
                                return;
                            }

                            recList = new ArrayList<>();

                            for (int i = 0; i < array.length(); ++i) {
                                tv_message.setVisibility(View.GONE);
                                nie_set.setVisibility(View.VISIBLE);
                                obj = (JSONObject) array.get(i);

                                v = linf.inflate(R.layout.signup_recommender_nie_set, null);

                                TextView tv_name = v.findViewById(R.id.s2_20_mb_name);
                                TextView tv_id = v.findViewById(R.id.s2_20_mb_id);
                                TextView tv_email = v.findViewById(R.id.s2_20_mb_email);

                                String name = obj.getString("mb_name");
                                String id = obj.getString("mb_id");
                                String email = obj.getString("mb_email");

                                tv_name.setText(name);
                                tv_id.setText(id);
                                tv_email.setText(email);

                                recList.add(new RecVO(name, id, email));

                                tv_name.setTag(i);
                                tv_id.setTag(i);
                                tv_email.setTag(i);

                                tv_name.setOnClickListener(recommenderClick);
                                tv_id.setOnClickListener(recommenderClick);
                                tv_email.setOnClickListener(recommenderClick);

                                recommender_records.addView(v);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(isAutoRecommend) {
                            s2_8_recommender_name.setText(recList.get(0).getName());
                            s2_9_recommender_email.setText(recList.get(0).getEmail());
                            s2_10_recommender_id.setText(recList.get(0).getId());
                        } else {
                            recommender_dialog.show();
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
                Map<String, String> params = new HashMap<String, String>();
                params.put("type", type);
                params.put("mb_info", mb_info);
                return params;
            }
        };

        stringRequest.setTag(TAG);

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }

    View.OnClickListener recommenderClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            int i = (int) view.getTag();

            if (recList != null) {

                s2_8_recommender_name.setText(recList.get(i).getName());
                s2_9_recommender_email.setText(recList.get(i).getEmail());
                s2_10_recommender_id.setText(recList.get(i).getId());

                recommender_dialog.dismiss();

            }

        }
    };

    // 유효성 검사
    private void setCheckList() {
        int month = 0;
        int date = 0;
        int maxDate = 0;
        if (mb_birth.length() == 6) {
            month = Integer.parseInt(mb_birth.substring(2, 4));
            date = Integer.parseInt(mb_birth.substring(4, 6));
        }
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                maxDate = 31;
                break;
            case 2:
                maxDate = 29;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                maxDate = 30;
        }

        checkList.add(new CheckVO("비밀번호", stPassword.length() >= 3 && Pattern.matches("^[a-zA-Z0-9_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?~`]+$", stPassword) && stPassword.equals(stPasswordCheck), s2_1_password)); // 비밀번호
        checkList.add(new CheckVO("계정 종류", !accountType.isEmpty(), s2_3_is_cu));
        // '추천인 없음' 기능 활성화 되면 주석 해제해서 사용하세요.
        // checkList.add(new CheckVO("추천인", haveNoRecommender || (!recommenderName.isEmpty() && !recommenderId.isEmpty() && !recommenderEmail.isEmpty()), s2_5_have_no_recommender));
        checkList.add(new CheckVO("추천인", !recommenderName.isEmpty() && !recommenderId.isEmpty() && !recommenderEmail.isEmpty(), s2_8_recommender_name));
        checkList.add(new CheckVO("이름", mb_name.length() >= 2 && Pattern.matches("^[a-zA-Z가-힣]*$", mb_name), s2_11_name));
        checkList.add(new CheckVO("연락처", !mb_hp.isEmpty(), s2_12_contact));
        checkList.add(new CheckVO("이메일", Pattern.matches("^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$", mb_email), s2_13_email));
        checkList.add(new CheckVO("생년월일", mb_birth.length() == 6 && (month >= 1 && month <= 12) && (date >= 1 && date <= maxDate), s2_14_birth));
        checkList.add(new CheckVO("우편번호", !mb_zip.isEmpty(), s2_15_zipcode));
        checkList.add(new CheckVO("주소", !mb_addr1.isEmpty(), s2_17_address1));
        checkList.add(new CheckVO("상세 주소", !mb_addr2.isEmpty(), s2_18_address2));
    }

    class CheckVO {
        String column;
        boolean check;
        View view;

        CheckVO(String column, boolean check, View view) {
            this.column = column;
            this.check = check;
            this.view = view;
        }
    }
}
