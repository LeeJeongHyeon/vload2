package vmp.company.vexmall.addpack;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kyad.adlibrary.AppAllOfferwallSDK;

import util.Progressbar_wheel;
import util.category.LogTag;
import vmp.company.vexmall.R;

public class Addpack_Frag1 extends android.support.v4.app.Fragment implements AppAllOfferwallSDK.AppAllOfferwallSDKListener {
    private final String TAG = LogTag.getTag(this);
    String stKey = "";
    String stId = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_addpack_tab1, container, false);
        return rootView;
    }

    public void init(View rootView){
    }

    @Override
    public void AppAllOfferwallSDKCallback(int i) {
        switch (i) {
            case AppAllOfferwallSDK.AppAllOfferwallSDK_SUCCES:
                Log.d(TAG, "성공");
                if (AppAllOfferwallSDK.getInstance().showAppAllOfferwall(getContext())) {
                    //성공
                    getActivity().finish();
                } else {
                    Toast.makeText(getActivity(), "SDK initialization error.", Toast.LENGTH_SHORT).show();
                }
                break;
            case AppAllOfferwallSDK.AppAllOfferwallSDK_INVALID_USER_ID:
                Log.d(TAG, "잘못 된 유저아이디입니다.");
                break;
            case AppAllOfferwallSDK.AppAllOfferwallSDK_INVALID_KEY:
                Log.d(TAG, "오퍼월 KEY를 확인해주세요");
                break;
            case AppAllOfferwallSDK.AppAllOfferwallSDK_NOT_GET_ADID:
                Log.d(TAG, "고객님의 폰으로는 무료충전소를 이용하실 수 없습니다. 고객센터에 문의해주세요.");
                break;
        }
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Async async = new Async();
            async.execute();
        }
    }
    public class Async extends AsyncTask<String, Void, String> {
        public Progressbar_wheel progressDialog;

        @Override
        protected void onPreExecute() {
            if(getContext() != null){
                progressDialog= Progressbar_wheel.show(getContext(),"","",true,true,null);
                progressDialog.setCanceledOnTouchOutside(false);

                //** adsync 등록 완료되면 아래 userid와 메타데이터에 key 값 수정해야 함
                //변수 초기화
                Intent in  = new Intent(getContext(), AddpackLaunchActivity.class);
                in.putExtra("key","8c02e8229f8018fd6ac259d2a377a153f847225e");
                in.putExtra("id","123");
                startActivity(in);
            }

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




