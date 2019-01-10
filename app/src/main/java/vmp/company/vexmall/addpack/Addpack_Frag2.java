package vmp.company.vexmall.addpack;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.igaworks.IgawCommon;
import com.igaworks.adpopcorn.IgawAdpopcornExtension;
import com.igaworks.adpopcorn.activity.layout.ApOfferWallLayout;
import com.igaworks.adpopcorn.cores.model.APClientRewardItem;
import com.igaworks.adpopcorn.interfaces.IAPClientRewardCallbackListener;

import java.io.UnsupportedEncodingException;


import util.Base64Util;
import util.Progressbar_wheel;
import vmp.company.vexmall.R;

import static vmp.company.vexmall.addpack.Addpack_Activity.stId;


public class Addpack_Frag2 extends android.support.v4.app.Fragment {
    private static View tab2_parent;
    private LinearLayout Frag_view;
    private ApOfferWallLayout ApOfferWallLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_addpack_tab2, container, false);
        tab2_parent = rootView;


        Async async = new Async();
        async.execute();
        return rootView;
    }

    public void init(View rootView) {
        try {
            String enc_stid = Base64Util.encode(stId);
            IgawCommon.setUserId(getActivity(), enc_stid);
        } catch (UnsupportedEncodingException e) {

        }
        IgawAdpopcornExtension.setCashRewardAppFlag(getContext(), true);
        Frag_view = (LinearLayout) rootView.findViewById(R.id.frag_view);
        ApOfferWallLayout = new ApOfferWallLayout(getContext());
        Frag_view.addView(ApOfferWallLayout);

        IgawAdpopcornExtension.setCustomOfferwallLayout(getContext(), ApOfferWallLayout, true);
        IgawAdpopcornExtension.setOfferwallImpressions(getContext());

        IgawAdpopcornExtension.setClientRewardCallbackListener(getContext(), new IAPClientRewardCallbackListener() {
            @Override
            public void onGetRewardInfo(boolean isSuccess, String resultMsg, APClientRewardItem[] rewardItems) {
                for (APClientRewardItem rewardItem : rewardItems) {
                    //아래 정보를 이용하여 유저에게 리워드를 지급합니다.

                    rewardItem.getCampaignKey();
                    rewardItem.getCampaignTitle();
                    rewardItem.getRTID();
                    rewardItem.getRewardQuantity();

                    // didGiveRewardItem api를 이용하여 유효한 rewardkey 인지 확인합니다.


// 확인결과는 onDidGiveRewardItemResult로 전달됩니다.
                    rewardItem.didGiveRewardItem();
                }
            }

            @Override
            public void onDidGiveRewardItemResult(boolean isSuccess, String resultMsg, int resultCode, String completedRewardKey) {
                // 본 이벤트리스너의 수신 결과가 성공일 때에만 유저에게 리워드 지급 처리를 합니다.
                // 한번 지급 처리한 completedRewardKey 에 대해서는 다시 리워드 지급을 하면 안됩니다.

            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            Async async = new Async();
            async.execute();
            //ApOfferWallLayout.resume(false);
        } else {
            // fragment is no longer visible
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ApOfferWallLayout != null)
            ApOfferWallLayout.resume(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (ApOfferWallLayout != null)
            ApOfferWallLayout.destroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            ApOfferWallLayout.onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ApOfferWallLayout != null)
            ApOfferWallLayout.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    public class Async extends AsyncTask<String, Void, String> {
        public Progressbar_wheel progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog= Progressbar_wheel.show(getActivity(),"","",true,true,null);
            progressDialog.setCanceledOnTouchOutside(false);

            //** adsync 등록 완료되면 아래 userid와 메타데이터에 key 값 수정해야 함
            //변수 초기화
            init(tab2_parent);

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                return "succed";
            } catch (Exception e) {
                e.printStackTrace();
                return "failed";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            progressDialog.dismiss();
        }
    }
}





