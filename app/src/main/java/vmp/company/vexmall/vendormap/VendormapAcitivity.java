package vmp.company.vexmall.vendormap;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import util.CustomImageLoad;
import util.category.LogTag;
import util.recommender.StatusBar;
import util.vendormap.VendorMapVO;
import vmp.company.vexmall.main.MainActivity;
import vmp.company.vexmall.navigation.NavActivity;
import vmp.company.vexmall.R;

public class VendormapAcitivity extends NavActivity implements VendormapFrag1.OnFragmentInteractionListener, VendormapFrag2.OnFragmentInteractionListener {

    // 로드가 되었는 지
    static boolean checkDataFromServer;

    // frag1, 2와 공유할 이미지, 매장 정보
    private ArrayList<VendorMapVO> mapList = new ArrayList<>();

    final private String TAG = LogTag.getTag(this);

    // UI
    private ImageButton vendormap_1_flag_ib;
    private ImageButton vendormap_1_list_ib;
    private DrawerLayout vendormap_1_drawer_layout;
    private NavigationView vendormap_1_nav_view;
    private ProgressBar progress_circular;

    // CurrentFragment
    static Fragment frag;


    //
    private boolean isMapType;

    // GPS
    private double user_latitude;
    private double user_longitude;


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate() begins to run..");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendormap);
        progress_circular = findViewById(R.id.progress_circular);
        progress_circular.setVisibility(View.VISIBLE);

        checkDataFromServer = false;

        Intent intent = getIntent();
        intent.setExtrasClassLoader(VendorMapVO.class.getClassLoader());
        if(intent != null){
            user_longitude = intent.getDoubleExtra("longitude", 0.0);
            user_latitude = intent.getDoubleExtra("latitude", 0.0);
            mapList = intent.getParcelableArrayListExtra("mapList");
            isMapType = intent.getBooleanExtra("isMapType", false);
        }

        // 드로우어
        vendormap_1_drawer_layout = findViewById(R.id.vendormap_1_drawer_layout);

        // 네비 뷰
        vendormap_1_nav_view = findViewById(R.id.vendormap_1_nav_view);

        // 네비게이션 생성
        super.setNav(vendormap_1_drawer_layout, vendormap_1_nav_view.getHeaderView(0));

        // 이미지로더 초기화
        CustomImageLoad.getInstance().initImageLoader(VendormapAcitivity.this);

        initUI();
        setListeners();
        setFragment(isMapType);
    }

    private void setFragment(boolean isMapType){
        Log.d(TAG, "setFragment() begins to run..");

        if(isMapType){
            frag = new VendormapFrag1();
            frag.setArguments(setBundle());
            progress_circular.setVisibility(View.GONE);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment, frag)
                    .commit();
        } else {
            frag = VendormapFrag2.newInstance(mapList);
            progress_circular.setVisibility(View.GONE);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment, frag)
                    .commit();
        }

    }

    private void initUI() {
        Log.d(TAG, "initUI() begins to run..");

        // 상단 상태바 색상 조정
        StatusBar.getInstance().setStatusBar(this, R.color.vendormap);  // 화면 상단 색상

        // 벤더맵 버튼 배경 변경
        Drawable vendormap_vendormap = ((TextView) findViewById(R.id.vendormap_vendormap)).getBackground();
        vendormap_vendormap.setAlpha(20);

        // 리스트 버튼
        vendormap_1_flag_ib = findViewById(R.id.vendormap_1_flag_ib);

        // 맵 버튼
        vendormap_1_list_ib = findViewById(R.id.vendormap_1_list_ib);
    }

    private void setListeners() {
        Log.d(TAG, "setListeners() begins to run..");

        // 리스트형으로 보기 버튼 리스너
        vendormap_1_list_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "vendormap_1_list_ib.Listener invoked..");
                //setFragment(false);
                Intent intent = new Intent(VendormapAcitivity.this, GpsActivity.class);
                intent.setExtrasClassLoader(VendorMapVO.class.getClassLoader());
                intent.putExtra("isMapType", false);
                startActivity(intent);
            }
        });


        // 맵 버튼 리스너
        vendormap_1_flag_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "vendormap_1_flag_ib.Listener invoked..");
                //setFragment(true);
                Intent intent = new Intent(VendormapAcitivity.this, GpsActivity.class);
                intent.setExtrasClassLoader(VendorMapVO.class.getClassLoader());
                intent.putExtra("isMapType", true);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) { /* 내용 없음 */ }

    private Bundle setBundle() {
        Log.d(TAG, "setBundle() begins to run..");

        Bundle bundle = new Bundle();
        bundle.setClassLoader(VendorMapVO.class.getClassLoader());
        bundle.putParcelableArrayList("mapList", mapList);
        bundle.putDouble("longitude", user_longitude);
        bundle.putDouble("latitude", user_latitude);
        Log.d(TAG, "setBundle().user_longitude : " + user_longitude);
        Log.d(TAG, "setBundle().user_latitude : " + user_latitude);
        return bundle;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        progress_circular.setVisibility(View.GONE);
        Intent intent = new Intent( VendormapAcitivity.this , MainActivity.class );
        startActivity(intent);
    }
}
