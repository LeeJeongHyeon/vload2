package vmp.company.vexmall.addpack;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import util.category.LogTag;
import util.recommender.StatusBar;
import vmp.company.vexmall.main.MainActivity;
import vmp.company.vexmall.navigation.NavActivity;
import vmp.company.vexmall.R;


public class Addpack_Activity extends NavActivity {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private final String TAG = LogTag.getTag(this);
    public static String stId = "";
    public static Application app_addpack;
    NavigationView addpack_nav_view;
    RelativeLayout main_bar_0;
    DrawerLayout addpack_drawer_layout;
    ImageView main_bar_1;
    TextView main_bar_2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Sera's", "Addpack_Activity.onCreate() begins to run...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpack);

        addpack_nav_view = findViewById(R.id.addpack_nav_view);
        addpack_drawer_layout = findViewById(R.id.addpack_drawer_layout);
        main_bar_0 = findViewById(R.id.main_bar_0);
        super.setNav(addpack_drawer_layout,addpack_nav_view.getHeaderView(0));

        // 상단 상태바 색상 조정
        StatusBar.getInstance().setStatusBar(this, R.color.addpack);  // 화면 상단 색상

        if (getIntent() != null) {
            if (getIntent().hasExtra("id")) {
                stId = getIntent().getStringExtra("id");
            }
        }
        app_addpack = getApplication();
        setTab();

        main_bar_1 = findViewById(R.id.main_bar_1);
        main_bar_2 = findViewById(R.id.main_bar_2);
        main_bar_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addpack_drawer_layout.openDrawer(addpack_nav_view);
            }
        });
        main_bar_2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Addpack_Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void init() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
    }


    private void setTab() {
        ViewPager viewPager = findViewById(R.id.viewpager);
        //프래그먼트 정의
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        viewPager.setAdapter(mSectionsPagerAdapter);
        viewPager.setOffscreenPageLimit(6);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.addTab(tabLayout.newTab().setText("애드팩 #1"));
        tabLayout.addTab(tabLayout.newTab().setText("애드팩 #2"));
        tabLayout.addTab(tabLayout.newTab().setText("애드팩 #3"));
        tabLayout.addTab(tabLayout.newTab().setText("애드팩 #4"));
        tabLayout.addTab(tabLayout.newTab().setText("애드팩 #5"));
        tabLayout.addTab(tabLayout.newTab().setText("애드팩 #6"));
        tabLayout.setupWithViewPager(viewPager);
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {
        private String[] tabTitles = new String[]{"애드팩 #1", "애드팩 #2", "애드팩 #3", "애드팩 #4", "애드팩 #5", "애드팩 #6"};

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: {
                    //프래그 먼트 생성
                    Fragment frag = new Addpack_Frag5();
                    Bundle bundle = new Bundle();
                    return frag;
                }
                case 1: {
                    //프래그 먼트 생성
                    Fragment frag = new Addpack_Frag2();
                    Bundle bundle = new Bundle();
                    return frag;
                }
                case 2: {
                    //프래그 먼트 생성
                    Fragment frag = new Addpack_Frag3();
                    Bundle bundle = new Bundle();
                    return frag;
                }
                case 3: {
                    //프래그 먼트 생성
                    Fragment frag = new Addpack_Frag4();
                    Bundle bundle = new Bundle();
                    return frag;
                }
                case 4: {
                    //프래그 먼트 생성
                    Fragment frag = new Addpack_Frag1();
                    Bundle bundle = new Bundle();
                    return frag;
                }
                case 5: {
                    //프래그 먼트 생성
                    Fragment frag = new Addpack_Frag6();
                    Bundle bundle = new Bundle();
                    return frag;
                }
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 6;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Addpack_Activity.this, MainActivity.class);
        startActivity(intent);
    }
}
