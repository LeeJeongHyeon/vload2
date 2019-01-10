package vmp.company.vexmall.shoplist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import soft.neunge.com.library.utils.BaseAdapter;
import soft.neunge.com.library.utils.BaseCallback;
import soft.neunge.com.library.utils.BaseVH;
import util.MDEBUG;
import util.customview.CheckImageView;
import util.data.MainCategory;
import util.data.Shop;
import util.data.ShopCategory;
import util.data.SubCategory;
import vmp.company.vexmall.R;
import vmp.company.vexmall.api.ShopAPI;
import vmp.company.vexmall.navigation.NavActivity;
import vmp.company.vexmall.shoplist.adapter.ShopAlbumAdapter;
import vmp.company.vexmall.shoplist.adapter.ShopListAdapter;
import vmp.company.vexmall.vexmall_application;

import static util.Property.SHOPLIST_CAT_EXTRANAME;
import static util.Property.SHOP_CATEGORY_ALL;

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
    @BindView(R.id.shop_category_ll)
    LinearLayout shopCategoryLl;
    @BindView(R.id.baseline3)
    View baseline3;
    @BindView(R.id.main_nav_view)
    NavigationView mainNavView;
    @BindView(R.id.main_drawaer_view)
    DrawerLayout mainDrawaerView;
    @BindView(R.id.shop_cat_list)
    RecyclerView shopCatList;
    @BindView(R.id.shop_subcat_tl)
    TabLayout shopSubcatTl;
    @BindView(R.id.shop_subcat_rl)
    RelativeLayout shopSubcatRl;
    @BindView(R.id.shop_list_recyclerview)
    RecyclerView shopListRecyclerview;
    @BindView(R.id.shop_album_btn)
    CheckImageView shopAlbumBtn;
    @BindView(R.id.shop_list_btn)
    CheckImageView shopListBtn;
    ///// widget

    ArrayList<ShopCategory> shopCategories;
    ShopCategory mCurrentCat = null;
    ArrayList<Shop> ShopList = null;
    String mCurrentCatId = "";
    ShopAPI api = null;

    ShopListAdapter listAdapter;
    ShopAlbumAdapter albumAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);
        ButterKnife.bind(this);
        super.setNav(mainDrawaerView, mainNavView.getHeaderView(0));
        api = ((vexmall_application) getApplication()).getRetrofit().create(ShopAPI.class);
        initIntentData();
        initUI();


    }

    private void initUI() {


        shopAlbumBtn.setSelector(R.drawable.ic_album_selected, R.drawable.ic_album_unselected);
        shopAlbumBtn.setchecked(false);
        shopAlbumBtn.setOnClickListener(v -> {
            if (!shopAlbumBtn.ischecked()) {
                shopListRecyclerview.setAdapter(albumAdapter);
                shopAlbumBtn.toggleCheck();
                shopListBtn.toggleCheck();
            }

        });
        shopListBtn.setSelector(R.drawable.ic_list_selected, R.drawable.ic_list_unselected);
        shopListBtn.setchecked(true);
        shopListBtn.setOnClickListener(v -> {

            if (!shopListBtn.ischecked()) {
                shopListRecyclerview.setAdapter(listAdapter);
                shopAlbumBtn.toggleCheck();
                shopListBtn.toggleCheck();
            }
        });
        initCatList();
        getShopList(mCurrentCatId);
    }
    private void initIntentData() {
        Intent data = getIntent();
        mCurrentCatId = data.getStringExtra(SHOPLIST_CAT_EXTRANAME) == null ? "90" : data.getStringExtra(SHOPLIST_CAT_EXTRANAME);
    }

    public void initCatList() {
        ShopAPI api = ((vexmall_application) getApplication()).getRetrofit().create(ShopAPI.class);
        api.getCategories().enqueue(new BaseCallback<MainCategory>(this, (call, res) -> {

            shopCategories = (ArrayList<ShopCategory>) res.body().categories;
            shopCategories.add(0, new ShopCategory(new ArrayList<SubCategory>(), "90", "", ""));
            ShopCatAdapter mCatadapter = new ShopCatAdapter(getApplicationContext(), R.layout.shop_cat_listitem);
            mCatadapter.setList(shopCategories);
            mCatadapter.setCatselected(mCurrentCatId);
            shopCatList.setAdapter(mCatadapter);
            setCategory(mCurrentCatId, shopCategories);
        }));
    }

    private void getShopList(String mCurrentCatId) {
        api.getShops(mCurrentCatId).enqueue(new BaseCallback<ArrayList<Shop>>(this, (call, res) -> {
            setListAdapters(res.body());
        }));

    }

    private void setListAdapters(ArrayList<Shop> shopList) {
        ShopList = shopList;
        if (listAdapter == null)
            listAdapter = new ShopListAdapter(getApplicationContext(), R.layout.shop_type_listitem);
        if (albumAdapter == null)
            albumAdapter = new ShopAlbumAdapter(getApplicationContext(), R.layout.shop_listtype_album);

        listAdapter.setList(ShopList);
        albumAdapter.setList(ShopList);

        shopListRecyclerview.setAdapter(shopListBtn.ischecked() ? listAdapter : albumAdapter);
    }

    private void setCategory(String mCurrentShop, ArrayList<ShopCategory> categories) {
        MDEBUG.debug(mCurrentShop + " is it null?");
        for (ShopCategory shop : categories) {
            if (shop.ca_id.equals(mCurrentShop)) {
                mCurrentCat = shop;
                break;
            } else if (mCurrentShop.equals(SHOP_CATEGORY_ALL)) {
                mCurrentCat = categories.get(0);
                break;
            }
        }
        initSubCat(mCurrentCat);
    }

    private void initSubCat(ShopCategory cat) {

        shopSubcatTl.removeAllTabs();
        TabLayout.Tab alltab = shopSubcatTl.newTab();
        alltab.setText("전체");

        if (mCurrentCatId.equals(SHOP_CATEGORY_ALL)) {
            alltab.setTag(SHOP_CATEGORY_ALL);
            shopSubcatTl.addTab(alltab);
        } else {
            alltab.setTag(cat.ca_id);
            shopSubcatTl.addTab(alltab);
            for (SubCategory sub : cat.sub) {
                TabLayout.Tab tabs = shopSubcatTl.newTab();
                tabs.setText(sub.ca_name);
                tabs.setTag(sub.ca_id);
                shopSubcatTl.addTab(tabs);
            }
        }
        shopSubcatTl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                getShopList((String) tab.getTag());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


    }

    @OnClick({R.id.shop_top_nav, R.id.shop_top_heart, R.id.shop_top_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.shop_top_nav:
                mainDrawaerView.openDrawer(mainNavView);
                break;
            case R.id.shop_top_heart:
                break;
            case R.id.shop_top_search:
                break;
        }
    }


    class ShopCatAdapter extends BaseAdapter<ShopCategory, ShopCatViewholder> {
        String catselected;

        public ShopCatAdapter(Context mCon, Integer mLayout) {
            super(mCon, mLayout);
        }

        public String getCatselected() {
            return catselected;
        }

        public void setCatselected(String catselected) {
            this.catselected = catselected;
        }

        @Override
        public ShopCatViewholder getViewHolder(View view) {
            ShopCatViewholder vh = new ShopCatViewholder(view);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull ShopCatViewholder shopCatViewholder, int i) {
            shopCatViewholder.bind(arrayList.get(i), getmCon());
            shopCatViewholder.mCatindicator.setVisibility(arrayList.get(i).ca_id.equals(catselected) ? View.VISIBLE : View.INVISIBLE);
            shopCatViewholder.setOnClcickView(v -> {
                catselected = (String) v.getTag();
                notifyDataSetChanged();
                mCurrentCat = arrayList.get(i);
                mCurrentCatId = mCurrentCat.ca_id;
                initSubCat(mCurrentCat);
            });
        }
    }

    class ShopCatViewholder extends BaseVH<ShopCategory> {
        ImageView mCatimg;
        ImageView mCatindicator;
        TextView mCattv;
        View layout;

        @Override
        public void bind(ShopCategory shopCategory, Context context) {
            layout.setTag(shopCategory.ca_id);

            if (shopCategory.imagePath.isEmpty())
                mCatimg.setImageResource(R.drawable.all);
            else
                Glide.with(context).load(shopCategory.imagePath).into(mCatimg);
            mCattv.setText(shopCategory.ca_name);
        }

        public void setOnClcickView(View.OnClickListener onClcickView) {
            layout.setOnClickListener(onClcickView);
        }

        public ShopCatViewholder(@NonNull View itemView) {
            super(itemView);
            layout = itemView;
            mCatindicator = layout.findViewById(R.id.shop_cat_indi);
            mCatimg = layout.findViewById(R.id.shop_cat_allimg);
            mCattv = layout.findViewById(R.id.shop_cat_tv);
        }
    }

    @Override
    public void onBackPressed() {
        if (mainDrawaerView.isDrawerOpen(mainNavView)){
            mainDrawaerView.closeDrawer(mainNavView);
            return;
        }
        super.onBackPressed();

    }
}



