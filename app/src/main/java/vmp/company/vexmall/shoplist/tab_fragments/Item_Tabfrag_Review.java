package vmp.company.vexmall.shoplist.tab_fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vmp.company.vexmall.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Item_Tabfrag_Review extends Fragment {


    public Item_Tabfrag_Review() {

    }

    public static Item_Tabfrag_Review newInstance() {
        Bundle args = new Bundle();
        Item_Tabfrag_Review fragment = new Item_Tabfrag_Review();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_tabfrag_review, container, false);
        return view;
    }

}
