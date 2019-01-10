package vmp.company.vexmall.shoplist.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import soft.neunge.com.library.utils.BaseVH;
import vmp.company.vexmall.R;
import util.data.Shop;

/**
 * Project   : VexMall
 * Packages  : vmp.company.vexmall.shoplist.adapter
 * Author    : Marty
 * Date      : 2018-12-27 / 오후 2:01
 * Comment   :
 */
public class ShopListVH extends BaseVH<Shop> {
    @BindView(R.id.shop_list_item_img)
    ImageView shopListItemImg;
    @BindView(R.id.shop_list_item_title)
    TextView shopListItemTitle;
    @BindView(R.id.shop_list_item_price)
    TextView shopListItemPrice;
    @BindView(R.id.shop_list_item_cutprice)
    TextView shopListItemCutprice;
    @BindView(R.id.shop_list_favor_btn)
    TextView shopListFavorBtn;
    @BindView(R.id.shop_list_item_freetrans)
    TextView shopListItemFreetrans;
    @BindView(R.id.shop_list_item_rl)
    RelativeLayout shopListItemRl;

    public ShopListVH(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        util.Utils.setCancelLine(shopListItemCutprice);
    }
    public void setOnClcickView(View.OnClickListener onClcickView){
        itemView.setOnClickListener(onClcickView);
    }

    @Override
    public void bind(Shop shop, Context context) {
        Glide.with(context).load(shop.it_img1).into(shopListItemImg);
        shopListItemTitle.setText(shop.it_name);
        shopListItemPrice.setText(shop.it_price);
        shopListItemCutprice.setText(shop.it_cust_price);
        shopListItemFreetrans.setText(shop.it_sc_type);
        itemView.setTag(shop.it_id);
    }
}
