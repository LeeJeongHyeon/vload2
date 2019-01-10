package vmp.company.vexmall.addpack;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.nextapps.naswall.NASWall;
import com.nextapps.naswall.NASWallAdInfo;

import java.util.ArrayList;

import vmp.company.vexmall.R;

import static vmp.company.vexmall.addpack.Addpack_Activity.stId;

public class Addpack_Frag3 extends android.support.v4.app.Fragment {
    private ArrayList<NASWallAdInfo> adList = null;
    //private AdArrayAdapter adapter;

    private String userData = stId;

    private static View tab3_parent;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_addpack_tab3, container, false);
        tab3_parent = rootView;
        //변수 초기화
        init(rootView);

        return rootView;
    }

    public void init(View rootView){
        boolean testMode = false;
        NASWall.init(getContext(), testMode);
        //베포 버전의 경우 false로 변경
        RelativeLayout embedView = (RelativeLayout)rootView.findViewById(R.id.embedView);
        NASWall.embed(getActivity(), embedView, userData);

    }

    public void myOnKeyDown(int key_code){
        //do whatever you want here
    }

    @Override
    public void onResume() {
        super.onResume();
        //User_pk로 설정
        NASWall.embedOnResume();
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            init(tab3_parent);
        }else{
            // fragment is no longer visible
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NASWall.ACTIVITY_RESULT_IMAGE_PICKER) {
            NASWall.embedOnActivityResult(getActivity(), requestCode, resultCode, data);
        }
    }
}

