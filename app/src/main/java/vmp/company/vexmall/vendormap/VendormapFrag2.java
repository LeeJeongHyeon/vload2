package vmp.company.vexmall.vendormap;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import util.category.LogTag;
import util.vendormap.VendorMapVO;
import vmp.company.vexmall.R;

public class VendormapFrag2 extends Fragment {

    final private String TAG = LogTag.getTag(this);
    private static final String ARG_PARAM1 = "mapList";

    private ArrayList<VendorMapVO> mParam1;

    private OnFragmentInteractionListener mListener;

    private ItemAdapter itemAdapter;
    private ListView vendormap_frag2_lv;

    public VendormapFrag2() {
        Log.d(TAG, "contsructor called.");
    }

    public static VendormapFrag2 newInstance(ArrayList<VendorMapVO> param1) {
        VendormapFrag2 fragment = new VendormapFrag2();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM1, param1);
        fragment.setArguments(args);
        Log.d(LogTag.getTag(VendormapFrag2.class), "VendormapFrag2 puts param1 to args : " + param1);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getParcelableArrayList(ARG_PARAM1);
            Log.d(TAG, "VendormapFrag2 gets param1 from args : " + mParam1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_vendormap_frag2, container, false);
        vendormap_frag2_lv = rootView.findViewById(R.id.vendormap_frag2_lv);
        inflateItems();
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void inflateItems() {

        itemAdapter = new ItemAdapter(getActivity(), R.layout.item_vendormap2, mParam1);

        vendormap_frag2_lv.setAdapter(itemAdapter);

    }

    private class ItemAdapter extends ArrayAdapter<VendorMapVO> {
        private Context mContext;
        private int mResource;
        private ArrayList<VendorMapVO> mList;
        private LayoutInflater mInflater;


        public ItemAdapter(@NonNull Context context, int resource, @NonNull List<VendorMapVO> objects) {
            super(context, resource, objects);

            this.mContext = context;
            this.mResource = resource;
            this.mList = (ArrayList<VendorMapVO>) objects;
            this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(mResource, null);
            }
            if (mList == null || mList.size() == 0) {

                TextView f_unavailable_tv = convertView.findViewById(R.id.f_unavailable_tv);
                f_unavailable_tv.setVisibility(View.VISIBLE);
                return convertView;
            }
            VendorMapVO vo = mList.get(position);



            if (vo != null) {
                ImageView f_iv = (ImageView) convertView.findViewById(R.id.f_iv);
                TextView f_company_name_tv = (TextView) convertView.findViewById(R.id.f_company_name_tv);
                TextView f_industry_tv = (TextView) convertView.findViewById(R.id.f_industry_tv);
                TextView f_address_tv = (TextView) convertView.findViewById(R.id.f_address_tv);
                TextView f_vm_tv = (TextView) convertView.findViewById(R.id.f_vm_tv);
                //TextView f_time_tv = (TextView) convertView.findViewById(R.id.f_time_tv);

                f_iv.setImageBitmap(vo.getBitmap());
                f_company_name_tv.setText(vo.getCompanyName());
                f_address_tv.setText(String.valueOf(vo.getAddr1() + " " + vo.getAddr2()));
                f_industry_tv.setText(vo.getIndustry());
                f_vm_tv.setText(vo.getVm());
                // TODO f_time_tv에 거리 소요시간 적기

            }

            return convertView;
        }

        @Override
        public int getCount() {
            return mList == null? 0 : mList.size();
        }
    }
}
