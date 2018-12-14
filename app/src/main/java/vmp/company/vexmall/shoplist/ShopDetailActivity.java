package vmp.company.vexmall.shoplist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import util.MDEBUG;
import util.Property;
import vmp.company.vexmall.R;
import vmp.company.vexmall.shoplist.adapter.ItemTabAdapter;
import vmp.company.vexmall.shoplist.callback.OnTabSelectedListener;

/**
 * Project   : VexMall
 * Packages  : vmp.company.vexmall.shoplist
 * Author    : Marty
 * Date      : 2018-12-12 / 오후 4:11
 * Comment   :
 */
public class ShopDetailActivity extends AppCompatActivity implements OnTabSelectedListener {
    @BindView(R.id.item_top_image)
    ImageView itemTopImage;
    @BindView(R.id.item_top_bar)
    Toolbar itemTopBar;
    @BindView(R.id.item_favor_btn)
    ImageView itemFavorBtn;
    @BindView(R.id.item_bottom_btn_rl)
    RelativeLayout itemBottomBtnRl;
    @BindView(R.id.item_collapse_l)
    CollapsingToolbarLayout itemCollapseL;
    @BindView(R.id.item_appbar)
    AppBarLayout itemAppbar;
    @BindView(R.id.back_arrow)
    ImageView backArrow;
    @BindView(R.id.item_top_favor_btn)
    ImageView itemTopFavorBtn;
    @BindView(R.id.item_top_search_btn)
    ImageView itemTopSearchBtn;
    @BindView(R.id.item_top_title_tv)
    TextView itemTopTitleTv;
    @BindView(R.id.item_title_tv)
    TextView itemTitleTv;
    @BindView(R.id.item_preprice_tv)
    TextView itemPrepriceTv;
    @BindView(R.id.item_price_tv)
    TextView itemPriceTv;
    @BindView(R.id.item_share_btn)
    RelativeLayout itemShareBtn;
    @BindView(R.id.item_cardbenefit_text)
    TextView itemCardbenefitText;
    @BindView(R.id.item_cardbenefit_tv)
    TextView itemCardbenefitTv;
    @BindView(R.id.item_trans_text)
    TextView itemTransText;
    @BindView(R.id.item_trans_tv)
    TextView itemTransTv;
    @BindView(R.id.item_transprice_text)
    TextView itemTranspriceText;
    @BindView(R.id.item_transprice_tv)
    TextView itemTranspriceTv;
    @BindView(R.id.item_tab_l)
    TabLayout itemTabL;
    @BindView(R.id.item_detail_vp)
    ViewPager itemDetailVp;
    @BindView(R.id.item_detail_scrollv)
    NestedScrollView itemDetailScrollv;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);
        ButterKnife.bind(this);
        setSupportActionBar(itemTopBar);
        itemAppbar.addOnOffsetChangedListener((appbar, state) -> {
            float offset = Math.abs((float) state);
            offset /= 1000;
            itemTopTitleTv.setAlpha(offset);
        });

        initUI();
    }


    public void initUI() {
        itemDetailScrollv.setFillViewport(true);

        // init Tab
        TabLayout.Tab[] Tabs = new TabLayout.Tab[Property.SHOP_DETAIL_TABS.length];
        for (int i = 0; i < Tabs.length; i++) {
            Tabs[i] = itemTabL.newTab().setText(Property.SHOP_DETAIL_TABS[i]);
            Tabs[i].setTag(i);
            itemTabL.addTab(Tabs[i]);
        }

        // init ViewPager
        itemDetailVp.setAdapter(new ItemTabAdapter(getSupportFragmentManager(), itemTabL.getTabCount()));
        itemDetailVp.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(itemTabL));
        itemTabL.addOnTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        MDEBUG.debug(tab.getPosition() + "Tab Position ");
        itemDetailVp.setCurrentItem(tab.getPosition());
    }
}



