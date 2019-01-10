package vmp.company.vexmall.shoplist.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import soft.neunge.com.library.utils.BaseAdapter;
import util.data.Shop;

/**
 * Project   : VexMall
 * Packages  : vmp.company.vexmall.shoplist.adapter
 * Author    : Marty
 * Date      : 2018-12-27 / 오후 2:00
 * Comment   :
 */
public class ShopListAdapter extends BaseAdapter<Shop,ShopListVH> {
    public ShopListAdapter(Context mCon, Integer mLayout) {
        super(mCon, mLayout);
    }

    @Override
    public ShopListVH getViewHolder(View view) {
        return new ShopListVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopListVH shopListVH, int i) {
        shopListVH.bind(arrayList.get(i),getmCon());
    }
}
