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
public class ShopAlbumVH extends BaseVH<Shop> {
    @BindView(R.id.shop_album_item_img)
    ImageView shopAlbumItemImg;
    @BindView(R.id.shop_album_item_subtitle)
    TextView shopAlbumItemSubtitle;
    @BindView(R.id.shop_album_item_title)
    TextView shopAlbumItemTitle;
    @BindView(R.id.shop_album_item_price)
    TextView shopAlbumItemPrice;
    @BindView(R.id.shop_list_album_preprice)
    TextView shopListAlbumPreprice;
    @BindView(R.id.shop_list_album_freetrans)
    TextView shopListAlbumFreetrans;
    @BindView(R.id.shop_album_favor_btn)
    TextView shopAlbumFavorBtn;
    @BindView(R.id.shop_album_item_rl)
    RelativeLayout shopAlbumItemRl;

    public ShopAlbumVH(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
    public void setOnClcickView(View.OnClickListener onClcickView){
        itemView.setOnClickListener(onClcickView);
    }


    @Override
    public void bind(Shop shop, Context context) {
        itemView.setTag(shop.it_id);
        shopAlbumItemTitle.setText(shop.it_name);
        shopAlbumItemSubtitle.setText(shop.it_basic);
        shopAlbumItemPrice.setText(shop.it_price);
        shopListAlbumPreprice.setText(shop.it_cust_price);
        shopListAlbumFreetrans.setText(shop.it_sc_type);
        Glide.with(context).load(shop.it_img1).into(shopAlbumItemImg);
    }
}
