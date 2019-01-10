package vmp.company.vexmall.shoplist.tab_fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vmp.company.vexmall.R;


public class Item_Tabfrag_BasicInform extends Fragment {

    public Item_Tabfrag_BasicInform() {
        // Required empty public constructor
    }


    public static Item_Tabfrag_BasicInform newInstance() {
        Item_Tabfrag_BasicInform fragment = new Item_Tabfrag_BasicInform();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item__tabfrag__basic_inform,container,false);
        return view;
    }

}
