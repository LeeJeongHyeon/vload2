package vmp.company.vexmall.main;


import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.v4.widget.DrawerLayout;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.tnkfactory.ad.TnkSession;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import util.ComingSoon;
import util.CustomImageLoad;
import util.Permission;
import util.category.CategoryVO;
import util.category.LogTag;
import util.recommender.StatusBar;
import util.category.GridAdapter;
import vmp.company.vexmall.navigation.InviteDialog;
import util.MySingleton;
import vmp.company.vexmall.R;
import vmp.company.vexmall.navigation.NavActivity;
import vmp.company.vexmall.signup.SignupActivity;
import vmp.company.vexmall.vendormap.GpsActivity;


public class MainActivity extends NavActivity { // 네비게이션 메뉴를 사용하는 모든 액티비티는 NavActivity를 상속 받는다.
    static boolean isFirst = true;
    static int num = 3; //3초안에 뒤로가기를 한번 더 누르면 종료
    private final String TAG = LogTag.getTag(this);
    private final String CATEGORY_URL = "http://vmp.company/vexMall/back/categoryJoin.php";
    private final String BANNER_URL = "http://vmp.company/vexMall/back/appBanner.php";
    private String code;
    public static Application app_addpack;

    public static int top_count = 0;  // 탑 배너의 개수
    static int bottom_count = 0; // 바텀 배너의 개수

    DrawerLayout main_drawer_layout; // 전체 레이아웃
    RelativeLayout main_main_view;   // 메인 레이아웃
    NavigationView main_nav_view;    // 네비게이션 레이아웃
    GridView ll_category;            // 카테고리 레이아웃 (동적)

    RelativeLayout main_bar_0;          // 상단 밴더맵 로고 레이아웃
    ImageView main_bar_1;
    TextView main_bar_2;
    LinearLayout store, reserve, map;  // 헤더 레이아웃
    ViewPager viewPager;                // 상단 배너 (뷰페이저)
    Handler handler;                    // 상단 배너 (뷰페이저)
    Thread thread;                      // 상단 배너 (뷰페이저)
    ImageView[] img;                    // 상단 배너 (이미지 뷰)
    ImageLoaderConfiguration config;    // 상단 배너 (이미지 뷰)
    ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance


    Bitmap[] bannerBitmaps, bannerBitmapsBottom; // 배너 이미지비트맵 배열
    LinearLayout bottomBannerLinear;   // 하단 배너 레이아웃

    FrameLayout f1, f2, f3, f4, f5; // 푸터
    //3초 카운트 핸들러
    static Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            //1초에 한번씩 핸들러가 반복적으로 수행
            mHandler.sendEmptyMessageDelayed(0, 1000);

            if (num > 0) {
                --num;
            } else {
                num = 3;
                mHandler.removeMessages(0);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Permission().checkPermission(MainActivity.this);
        if (isFirst) {
            TnkSession.prepareInterstitialAd(this, TnkSession.CPC);
            TnkSession.showInterstitialAd(this);
            isFirst = false;
        }


        // 배너 이미지 로드
        CustomImageLoad.getInstance().initImageLoader(MainActivity.this);


        initMainUI();
        setMainListeners();
        super.setNav(main_drawer_layout, main_nav_view.getHeaderView(0));

        // 배너, 아이콘 받아오기
        getIconUrls();
        getBannerUrls("mainTop");
        getBannerUrls("mainBottom");
        handleDeepLink();

    }//onCreate()

    protected void initMainUI() {
        Log.d(TAG, "initUI() begins to run...");


        // 상단 상태바 색상 조정
        StatusBar.getInstance().setStatusBar(this, R.color.colorPrimary);  // 화면 상단 색상

        main_drawer_layout = findViewById(R.id.main_drawer_layout);
        main_main_view = findViewById(R.id.main_main_view);
        store = findViewById(R.id.main_store_ll);
        reserve = findViewById(R.id.main_reservation_ll);
        map = findViewById(R.id.main_map_ll);
        main_bar_0 = findViewById(R.id.main_bar_0);
        main_bar_2 = findViewById(R.id.main_bar_2);


        main_nav_view = findViewById(R.id.main_nav_view);
        ll_category = findViewById(R.id.main_ca_layout);
        bottomBannerLinear = findViewById(R.id.main_bottom_banner_ll);

        f1 = findViewById(R.id.main_footer_1);
        f2 = findViewById(R.id.main_footer_2);
        f3 = findViewById(R.id.main_footer_3);
        f4 = findViewById(R.id.main_footer_4);
        f5 = findViewById(R.id.main_footer_5);

        // viewPager와 imageView[]는 따로 생성

    }

    protected void setMainListeners() {
        Log.d(TAG, "setListeners() begins to run...");

        main_bar_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main_drawer_layout.openDrawer(main_nav_view);
            }
        });
        main_bar_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        // TODO : 나머지 리스너 향후 추가

        store.setOnClickListener(ComingSoon.comingSoonListener(this));
        reserve.setOnClickListener(ComingSoon.comingSoonListener(this));
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GpsActivity.class);
                startActivity(intent);
            }
        });
        f1.setOnClickListener(ComingSoon.comingSoonListener(this));
        f2.setOnClickListener(ComingSoon.comingSoonListener(this));
        f3.setOnClickListener(ComingSoon.comingSoonListener(this));
        f4.setOnClickListener(ComingSoon.comingSoonListener(this));
        f5.setOnClickListener(ComingSoon.comingSoonListener(this));
        ll_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this, "준비 중입니다.", Toast.LENGTH_SHORT).show();
            }
        });

    }


    // 동적 카테고리 생성
    public void getIconUrls() {
        String url = CATEGORY_URL;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray obj = new JSONArray(response);
                            ArrayList<CategoryVO> categoryArr = new ArrayList<>();

                            for (int i = 0; i < ll_category.getNumColumns() * 2; i++) {

                                if (i >= obj.length()) {
                                    CategoryVO vo = new CategoryVO("", "");
                                    categoryArr.add(vo);
                                    continue;
                                }

                                CategoryVO vo = new CategoryVO(obj.getJSONObject(i).getString("imagePath"), obj.getJSONObject(i).getString("ca_name"));
                                categoryArr.add(vo);
                            }

                            ll_category.setAdapter(new GridAdapter(MainActivity.this, R.layout.category_input_frame, categoryArr));
                            int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
                            ll_category.measure(0, expandSpec);
                            ll_category.getLayoutParams().height = ll_category.getMeasuredHeight();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        );

        stringRequest.setTag(TAG);
        MySingleton.getInstance(MainActivity.this).addToRequestQueue(stringRequest);
    }

    public void getBannerUrls(final String type) {
        String url = BANNER_URL;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            if (type.equals("mainTop")) {

                                JSONArray array = new JSONArray(response); //JSONArray형식으로 파싱하기 위해 새로 선언해주며 eventArray를 집어 넣어준다.
                                top_count = array.length();

                                bannerBitmaps = new Bitmap[top_count];
                                img = new ImageView[top_count];

                                for (int i = 0; i < top_count; i++) {
                                    //상단 배너 로드
                                    bannerLoad(array, i);
                                }//for

                            } else if (type.equals("mainBottom")) {

                                JSONArray array = new JSONArray(response); //JSONArray형식으로 파싱하기 위해 새로 선언해주며 eventArray를 집어 넣어준다.
                                bottom_count = array.length();

                                bannerBitmapsBottom = new Bitmap[bottom_count];

                                for (int i = 0; i < bottom_count; i++) {
                                    //하단 배너 로드
                                    bannerLoadBottom(array, i);
                                }//for

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.i("MY", "err:" + e.getMessage());
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
                params.put("type", type);
                return params;
            }
        };

        stringRequest.setTag(TAG);
        MySingleton.getInstance(MainActivity.this).addToRequestQueue(stringRequest);

    }//getBannerUrls()

    private void bannerLoad(JSONArray array, final int i) {

        try {
            imageLoader.loadImage((String) array.get(i), new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    bannerBitmaps[i] = loadedImage;

                    // View Pager 시작
                    viewPager = findViewById(R.id.main_event_viewpager);

                    final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), bannerBitmaps);
                    CirclePageIndicator circlePageIndicator = findViewById(R.id.main_circlepageindicator);
                    viewPager.setAdapter(viewPagerAdapter);
                    viewPager.setOnClickListener(ComingSoon.comingSoonListener(MainActivity.this));

                    //서클 인디케이터
                    circlePageIndicator.setViewPager(viewPager);
                    viewPagerAdapter.notifyDataSetChanged();

                    if (i == top_count - 1) {
                        handler = new Handler() {

                            public void handleMessage(android.os.Message msg) {
                                viewPager.setCurrentItem((viewPager.getCurrentItem() + 1) % top_count);
                            }
                        };

                        thread = new Thread() {
                            //run은 jvm이 쓰레드를 채택하면, 해당 쓰레드의 run메서드를 수행한다.
                            public void run() {
                                super.run();
                                while (true) {
                                    try {
                                        Thread.sleep(3000);
                                        handler.sendEmptyMessage(0);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                        break;
                                    }
                                }
                            }
                        };
                        thread.start();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }//bannerLoad()

    private void bannerLoadBottom(JSONArray array, final int i) {
        try {
            imageLoader.loadImage((String) array.get(i), new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    bannerBitmapsBottom[i] = loadedImage;

                    ImageView img = new ImageView(MainActivity.this);
                    img.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    img.setScaleType(ImageView.ScaleType.FIT_XY);
                    img.setImageBitmap(bannerBitmapsBottom[i]);

                    ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(img.getLayoutParams());
                    margin.setMargins(0, 15, 0, 0);
                    img.setLayoutParams(new LinearLayout.LayoutParams(margin));

                    bottomBannerLinear.addView(img);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }//bannerLoad()


    @Override
    protected void onResume() {

        String tmp = pref.getString("mb_id", "");
        Log.d(TAG, "Main onResume() mb_id tmp: " + tmp);
        handleDeepLink();
        super.onResume();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onBackPressed() {
        if (num != 3) {
            SharedPreferences.Editor editor = pref.edit();
            editor.remove("mb_id");
            editor.apply();
            finishAffinity();
        } else {
            Toast.makeText(getApplicationContext(), "한 번 더 누르면 종료합니다.", Toast.LENGTH_SHORT).show();
        }

        mHandler.sendEmptyMessage(0);

    }

    // DeepLink 수신
    private void handleDeepLink() {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        if (pendingDynamicLinkData == null) {
                            Log.d("Sera's", "No have dynamic link");
                            return;
                        }
                        Uri deepLink = pendingDynamicLinkData.getLink();
                        Log.d(TAG, "deepLink: " + deepLink);
                        code = deepLink.getQueryParameter(InviteDialog.PARAMETER_KEY);
                        if (code != null || !code.isEmpty()) {
                            Log.d(TAG, "handleDeepLink : mb_id : " + PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getString("mb_id", ""));
                            if (!PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getString("mb_id", "").isEmpty()) {
                                Toast.makeText(MainActivity.this, "이미 로그인한 계정입니다.\n로그아웃 후 다시 링크에 접속해주세요.", Toast.LENGTH_LONG).show();

                            } else {
                                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                                intent.putExtra("recommender_id", code);
                                startActivity(intent);
                            }
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "getDynamicLink:onFailure", e);
                    }
                });
    }
}
