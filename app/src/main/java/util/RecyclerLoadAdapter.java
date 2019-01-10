package util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import soft.neunge.com.library.utils.BaseAdapter;
import soft.neunge.com.library.utils.BaseVH;

/**
 * Project   : VexMall
 * Packages  : util
 * Author    : Marty
 * Date      : 2018-12-28 / 오전 11:13
 * Comment   :
 */
public class RecyclerLoadAdapter<T,S extends RecyclerView.ViewHolder> extends BaseAdapter {
    public RecyclerLoadAdapter(Context mCon, Integer mLayout) {
        super(mCon, mLayout);
    }

    @Override
    public BaseVH getViewHolder(View view) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }
}
