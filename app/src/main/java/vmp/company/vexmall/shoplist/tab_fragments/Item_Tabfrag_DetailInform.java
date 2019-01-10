package vmp.company.vexmall.shoplist.tab_fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import util.customview.ZoomImageView;
import vmp.company.vexmall.R;

/**
 * Project   : VexMall
 * Packages  : vmp.company.vexmall.shoplist.tab_fragments
 * Author    : Marty
 * Date      : 2018-12-13 / 오후 12:53
 * Comment   :
 */
public class Item_Tabfrag_DetailInform extends Fragment {


    @BindView(R.id.item_detail_zoom_text)
    TextView itemDetailZoomText;
    @BindView(R.id.item_detail_zoomin)
    RelativeLayout itemDetailZoomin;
    @BindView(R.id.item_detail_image)
    ZoomImageView itemDetailImage;
    Unbinder unbinder;

    public static Item_Tabfrag_DetailInform newInstance() {
        Bundle args = new Bundle();
        Item_Tabfrag_DetailInform fragment = new Item_Tabfrag_DetailInform();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_detail_info, container, false);

        unbinder = ButterKnife.bind(this, view);

        itemDetailZoomText.setText(Html.fromHtml(getResources().getString(R.string.item_detail_zoom_text)));
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
