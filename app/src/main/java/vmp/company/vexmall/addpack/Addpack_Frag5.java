package vmp.company.vexmall.addpack;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tnkfactory.ad.AdListView;
import com.tnkfactory.ad.TnkLayout;
import com.tnkfactory.ad.TnkSession;

import util.Progressbar_wheel;
import vmp.company.vexmall.R;

import static vmp.company.vexmall.addpack.Addpack_Activity.stId;

public class Addpack_Frag5 extends android.support.v4.app.Fragment {
    private static View tab5_parent;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_addpack_tab5, container, false);
        tab5_parent = rootView;

        Async async = new Async();
        async.execute();
        return rootView;
    }

    public void init(View rootView){


    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            init(tab5_parent);
        }else{
            // fragment is no longer visible
        }
    }

    public class Async extends AsyncTask<String, Void, String> {
        public Progressbar_wheel progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog= Progressbar_wheel.show(getActivity(),"","",true,true,null);
            progressDialog.setCanceledOnTouchOutside(false);

            //** adsync 등록 완료되면 아래 userid와 메타데이터에 key 값 수정해야 함
            //변수 초기화
            TnkSession.setUserName(getContext(), stId);
            TnkLayout layout= new TnkLayout();

            ViewGroup viewGroup = (ViewGroup)tab5_parent.findViewById(R.id.adlist);

            AdListView offerwallView = TnkSession.createAdListView(getActivity(), layout);
            viewGroup.addView(offerwallView);

            offerwallView.loadAdList();

            //변수 초기화
            init(tab5_parent);
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




