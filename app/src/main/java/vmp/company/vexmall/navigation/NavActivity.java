package vmp.company.vexmall.navigation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import util.ComingSoon;
import util.category.LogTag;
import util.MySingleton;
import vmp.company.vexmall.R;
import vmp.company.vexmall.addpack.AddpackLaunchActivity;
import vmp.company.vexmall.addpack.Addpack_Activity;
import vmp.company.vexmall.setting.SettingsActivity;
import vmp.company.vexmall.signup.SignupActivity;

public class NavActivity extends AppCompatActivity {
    private final String TAG = LogTag.getTag(this);

    DrawerLayout rootDrawerLayout;
    View rootView;
    LinearLayout nav_menu_1_alarm;
    LinearLayout nav_menu_2_like;
    LinearLayout nav_menu_3_advertising;
    LinearLayout nav_menu_4_meeting;
    LinearLayout nav_menu_5_teamwork;
    LinearLayout nav_menu_6_my;
    LinearLayout nav_menu_7_vip;
    ImageView close;
    ImageView setting;
    GridLayout nav_menus;
    TextView nav_login, nav_join, nav_logout;


    RelativeLayout joinLayout;
    RelativeLayout loginedLayout;


    TextView tvIdTitle;
    TextView tvVcoin;
    TextView tvVmc;
    TextView tvVmp;
    TextView txt_notice;
    TextView user_name;
    TextView nav_welcome;

    protected SharedPreferences pref;
    String mb_id = "";
    String mb_name = "";
    String accountType;

    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_main);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        checkAutoLogin();
    } //onCreate()

    protected void initNav(DrawerLayout rootDrawerLayout, View view) {
        this.rootDrawerLayout = rootDrawerLayout;
        this.rootView = view;
        Log.d(TAG, "initNav() begins to run...");
        close = view.findViewById(R.id.nav_close);
        nav_menu_1_alarm = view.findViewById(R.id.nav_menu_1_alarm);
        nav_menu_2_like= view.findViewById(R.id.nav_menu_2_like);
        nav_menu_3_advertising = view.findViewById(R.id.nav_menu_3_advertising);
        nav_menu_4_meeting = view.findViewById(R.id.nav_menu_4_meeting);
        nav_menu_5_teamwork =  view.findViewById(R.id.nav_menu_5_teamwork);
        nav_menu_6_my =  view.findViewById(R.id.nav_menu_6_my);
        nav_login = view.findViewById(R.id.nav_login);
        nav_logout = view.findViewById(R.id.nav_logout);

        //recommend = view.findViewById(R.id.recommend);
        nav_join = view.findViewById(R.id.nav_join);

        joinLayout = view.findViewById(R.id.joinLayout);
        nav_welcome = view.findViewById(R.id.nav_welcome);
        SpannableStringBuilder ssb = new SpannableStringBuilder("V로드에 오신 것을 환영합니다.");
        ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#383b65")), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        nav_welcome.setText(ssb);
        loginedLayout = view.findViewById(R.id.loginedLayout);

        tvIdTitle = view.findViewById(R.id.tvIdTitle);
        tvVcoin = view.findViewById(R.id.tvVcoin);
        tvVmc = view.findViewById(R.id.tvVmc);
        tvVmp = view.findViewById(R.id.tvVmp);
        txt_notice = view.findViewById(R.id.txt_notice);
        user_name = view.findViewById(R.id.user_name);
        nav_menus = view.findViewById(R.id.nav_menus);
        nav_menu_5_teamwork = view.findViewById(R.id.nav_menu_5_teamwork);
        nav_menu_7_vip = nav_menus.findViewById(R.id.nav_menu_7_crown);
        setting = view.findViewById(R.id.nav_setting);

    }

    protected void setNavListeners() {

        // X 버튼
        close.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rootDrawerLayout != null) {
                    rootDrawerLayout.closeDrawers();
                }
            }
        });

        // Setting 버튼
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NavActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        // 로그인
        nav_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NavActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        // 로그아웃
        nav_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor edit = pref.edit();
                edit.clear();
                edit.apply();
                Toast.makeText(getApplicationContext(), "로그아웃을 완료하였습니다.", Toast.LENGTH_SHORT).show();
                getUserInfo();
            }
        });

        // 회원가입
        nav_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NavActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        nav_menu_1_alarm.setOnClickListener(ComingSoon.comingSoonListener(this));

        nav_menu_2_like.setOnClickListener(ComingSoon.comingSoonListener(this));

        // 애드팩
        nav_menu_3_advertising.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isLogined()) {
                    Toast.makeText(NavActivity.this, "로그인 후 이용해 주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent in = new Intent(NavActivity.this, Addpack_Activity.class);
                    in.putExtra("id", mb_id);
                    startActivity(in);
                }
            }
        });
        nav_menu_4_meeting.setOnClickListener(ComingSoon.comingSoonListener(this));
        // V로드 추천하기
        nav_menu_5_teamwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InviteDialog dialog = new InviteDialog(NavActivity.this);
                dialog.show();
            }
        });
        nav_menu_6_my.setOnClickListener(ComingSoon.comingSoonListener(this));
        // VIP 전용관
        nav_menu_7_vip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mb_id == null || mb_id.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "로그인 후 이용해 주세요.", Toast.LENGTH_SHORT).show();
                } else if (!"VM".equals(accountType) && !("admin".equals(mb_id))) {
                    Toast.makeText(getApplicationContext(), "이용권한이 없습니다.", Toast.LENGTH_SHORT).show();
                } else {

                    Intent in = new Intent(NavActivity.this, AddpackLaunchActivity.class);
                    in.putExtra("key", "77e8aac8d5d3843c82bded71a58d9a3fba2cadba");
                    in.putExtra("id", mb_id);
                    startActivity(in);
                }
            }
        });

        // 고객센터
        txt_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "고객센터", Toast.LENGTH_SHORT).show();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://vmp.support/"));
                startActivity(browserIntent);
            }
        });

        // 설정
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NavActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });


    }


    protected void setNav(DrawerLayout rootDrawerLayout, View view) {
        // 모든 UI 초기화
        initNav(rootDrawerLayout, view);

        // 모든 이벤트 리스너 초기화
        setNavListeners();
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle( this, rootDrawerLayout,null,0,0) {



            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getUserInfo();
            }
        };

        // Set the drawer toggle as the DrawerListener
        rootDrawerLayout.setDrawerListener(mDrawerToggle);
    }


    private void checkAutoLogin() {
        Log.d(TAG, "checkAutoLogin() begins to run...");
        Log.d(TAG, "check AutoLogin() mb_id : " + pref.getString("mb_id", ""));
        SharedPreferences.Editor edit = pref.edit();
        if (pref.getBoolean("autoLogin", false)) { // 자동 로그인 설정인 경우
            edit.putString("mb_id", pref.getString("auto_login_id", ""));
        }
        edit.apply();
    }

    // getUserInfo() 내부에 updateNavView()가 호출됩니다.
    protected void updateNavView() {

        if (isLogined()) {
            mb_id = pref.getString("mb_id", "");
            accountType = pref.getString("accountType", "");
            mb_name = pref.getString("mb_name", "");

            //로그아웃 버튼 활성화
            nav_logout.setVisibility(View.VISIBLE);
            joinLayout.setVisibility(View.GONE);
            loginedLayout.setVisibility(View.VISIBLE);
        } else {

            //로그아웃 버튼 비활성
            nav_logout.setVisibility(View.GONE);
            joinLayout.setVisibility(View.VISIBLE);
            loginedLayout.setVisibility(View.GONE);
        }
    }


    // 포인트와 유저 이름을 서버에서 받아온 후 네비게이션 뷰를 업데이트 합니다.
    private void getUserInfo() {
        Log.d(TAG, "getUserInfo() begins to run...");
        if (!pref.getString("mb_id", "").isEmpty()) {
            String url = "http://vmp.company/vexMall/back/pointLookup.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response

                            Log.d(TAG, "pointLookup Response : " + response);
                            try {

                                JSONObject obj = new JSONObject(response);
                                String VMC = obj.getString("VCash");
                                String VMP = obj.getString("VPay");
                                String V = obj.getString("V");


                                long iVMC = Long.parseLong(VMC);
                                long iVMP = Long.parseLong(VMP);
                                long iV = Long.parseLong(V);

                                DecimalFormat formatter = new DecimalFormat("###,###");

                                VMC = formatter.format(iVMC);
                                VMP = formatter.format(iVMP);
                                V = formatter.format(iV);

                                tvIdTitle.setText(mb_name + "님 반갑습니다.");

                                tvVcoin.setText(V + "개");
                                tvVmp.setText(VMP + "원");
                                tvVmc.setText(VMC + "원");
                                user_name.setText(mb_name);
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
                    params.put("mb_id", mb_id);
                    return params;
                }
            };

            stringRequest.setTag(TAG);
            MySingleton.getInstance(this).addToRequestQueue(stringRequest);
        }
        updateNavView();

    }

    protected boolean isLogined() {
        return !pref.getString("mb_id", "").isEmpty();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "Nav onResume()");
        super.onResume();
        getUserInfo();
    }
}
