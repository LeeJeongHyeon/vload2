package vmp.company.vexmall.shoplist;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import vmp.company.vexmall.R;
import vmp.company.vexmall.navigation.NavActivity;

public class ShopListActivity extends NavActivity {

    @BindView(R.id.shop_top_nav)
    ImageView shopTopNav;
    @BindView(R.id.shop_top_title)
    TextView shopTopTitle;
    @BindView(R.id.shop_top_heart)
    ImageView shopTopHeart;
    @BindView(R.id.shop_top_search)
    ImageView shopTopSearch;
    @BindView(R.id.shop_top_toolbar)
    RelativeLayout shopTopToolbar;
    @BindView(R.id.shop_cat_allimg)
    ImageView shopCatAllimg;
    @BindView(R.id.shop_cat_all_rl)
    RelativeLayout shopCatAllRl;
    @BindView(R.id.shop_cat_passionimg)
    ImageView shopCatPassionimg;
    @BindView(R.id.shop_cat_passion_rl)
    RelativeLayout shopCatPassionRl;
    @BindView(R.id.shop_cat_foodimg)
    ImageView shopCatFoodimg;
    @BindView(R.id.shop_cat_food_rl)
    RelativeLayout shopCatFoodRl;
    @BindView(R.id.shop_cat_livingimg)
    ImageView shopCatLivingimg;
    @BindView(R.id.shop_cat_living_rl)
    RelativeLayout shopCatLivingRl;
    @BindView(R.id.shop_cat_digitalimg)
    ImageView shopCatDigitalimg;
    @BindView(R.id.shop_cat_digital_rl)
    RelativeLayout shopCatDigitalRl;
    @BindView(R.id.shop_cat_sportimg)
    ImageView shopCatSportimg;
    @BindView(R.id.shop_cat_sport_rl)
    RelativeLayout shopCatSportRl;
    @BindView(R.id.shop_category_ll)
    LinearLayout shopCategoryLl;
    @BindView(R.id.baseline1)
    View baseline1;
    @BindView(R.id.shop_album_btn)
    ImageView shopAlbumBtn;
    @BindView(R.id.baseline2)
    View baseline2;
    @BindView(R.id.shop_list_btn)
    ImageView shopListBtn;
    @BindView(R.id.baseline3)
    View baseline3;
    @BindView(R.id.main_nav_view)
    NavigationView mainNavView;
    @BindView(R.id.main_drawaer_view)
    DrawerLayout mainDrawaerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);
        ButterKnife.bind(this);
        super.setNav(mainDrawaerView, mainNavView.getHeaderView(0));

        //textview.setPaintFlags(textview.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }
}
