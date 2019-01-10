package vmp.company.vexmall.addpack;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fpang.lib.FpangSession;

import vmp.company.vexmall.R;

import static vmp.company.vexmall.addpack.Addpack_Activity.stId;

public class Addpack_Frag4 extends android.support.v4.app.Fragment {
    private static View tab4_parent;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_addpack_tab4, container, false);
        tab4_parent = rootView;
        //** adsync 등록 완료되면 아래 userid와 메타데이터에 key 값 수정해야 함
        //변수 초기화
        init(rootView);

        return rootView;
    }

    public void init(View rootView){
        FpangSession.init( getActivity() );
        FpangSession.setDebug( true ); // 배포시 false 로 설정
        FpangSession.setUserId(stId); // 사용자 ID 설정
        //추가 정보 설정(options)
        FpangSession.setAge(25); // 0 이면 값없음
        FpangSession.setGender("M"); // M:남자, F:여자, A:값없음

        FpangSession.showAdSyncFragment(getActivity(), R.id.frag4_view, "AdSync2");
    }

    public void myOnKeyDown(int key_code){
        //do whatever you want here
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            init(tab4_parent);
        }else{
            // fragment is no longer visible
        }
    }
}





