package vmp.company.vexmall.vendormap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import util.ImageConverter;
import util.Permission;
import util.category.LogTag;
import util.vendormap.VendorMapVO;
import util.MySingleton;
import vmp.company.vexmall.R;

// Google fusedLocation을 사용하여 현재 사용자의 위치를 수신하는 액티비티
public class GpsActivity extends AppCompatActivity implements Parcelable {

    private final String TAG = LogTag.getTag(this);

    private double user_longitude;
    private double user_latitude;

    // 리스트 타입으로 호출된 건지, 맵 타입으로 호출된 건지 ( VendorMapActivity 에게 전달 )
    private boolean isMapType;

    // 서버 수신 파람
    ArrayList<VendorMapVO> mapList;

    // 이미지 컨버터
    ImageConverter converter = ImageConverter.getInstance(this);

    // 프로그레스바
    private ProgressBar progress_circular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        GoogleApiClient.Builder mGoogleApiClientBuilder = setGoogleServiceBuilder();
        getLastKnownLocation(mGoogleApiClientBuilder.build());

        Intent intent = new Intent();
        if (intent.getData() != null) {
            isMapType = intent.getBooleanExtra("isMapType", false);
        }

        progress_circular = findViewById(R.id.progress_circular);
        progress_circular.setVisibility(View.VISIBLE);
    }


    //Google Play 서비스 접근승인 요청
    public GoogleApiClient.Builder setGoogleServiceBuilder() {


        //Google Api Client 생성
        GoogleApiClient.Builder mGoogleApiClientBuilder = new GoogleApiClient.Builder(this);
        mGoogleApiClientBuilder.addApi(LocationServices.API);//Fused Location Provider API 사용요청


        //Google Client Connection Callback 클래스
        CallbackConnectedGoogleService callbackConnectedGoogleService = new CallbackConnectedGoogleService();
        mGoogleApiClientBuilder.addConnectionCallbacks(callbackConnectedGoogleService);
        mGoogleApiClientBuilder.addOnConnectionFailedListener(callbackConnectedGoogleService);
        GoogleApiClient mGoogleApiClient = mGoogleApiClientBuilder.build();
        mGoogleApiClient.connect();
        return mGoogleApiClientBuilder;

    }


    //Google Play 서비스 접근승인 응답
    class CallbackConnectedGoogleService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
        private String TAG = LogTag.getTag(this);

        @Override
        public void onConnectionFailed(ConnectionResult result) {
            Log.d(TAG, "GoogleService onConnectionSuspended");
            Log.d(TAG, "Connected Failed : " + result.getErrorCode());
        }


        @Override
        public void onConnected(Bundle connectionHint) {
            Log.d(TAG, "GoogleService onConnected");
            Log.d(TAG, "Connected Success");
        }


        @Override
        public void onConnectionSuspended(int cause) {
            Log.d(TAG, "GoogleService onConnectionSuspended");
            Log.d(TAG, "Suspended cause : " + cause);
        }

    }


    //Fused Location Provider API 호출
    public void getLastKnownLocation(GoogleApiClient mGoogleApiClient) {
        new Permission().checkPermission(this);

        Task task = LocationServices.getFusedLocationProviderClient(this).getLastLocation();
        task.addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task task) {
                Log.d(TAG, "Task is successful.");
                if (task.getResult() instanceof Location) {
                    Location mLastLocation = (Location) task.getResult();
                    user_longitude = mLastLocation.getLongitude();
                    user_latitude = mLastLocation.getLatitude();
                    Log.d(TAG, "Fused Location Provider API :Latitude" + mLastLocation.getLatitude());
                    Log.d(TAG, "Fused Location Provider API :Longitude" + mLastLocation.getLongitude());
                    Log.d(TAG, "Fused Location Provider API :Accuracy" + mLastLocation.getAccuracy());


                    new vdMapLookUpAsync().execute();


                } else {
                    showSettingsAlert();
                }
            }
        });

    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("GPS 사용유무셋팅");
        alertDialog.setMessage("GPS 셋팅이 되지 않았을수도 있습니다. \n 설정창으로 가시겠습니까?");

        // OK 를 누르게 되면 설정창으로 이동합니다.
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                });


        // Cancel 하면 종료 합니다.
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    // 현위치를 얻어 온 후 서버에 송신
    // 서버에게 모든 설정 정보를 보내는 메서드 (multipart form-data)
    // 설정 완료 텍스트뷰의 리스너에서 호출됨
    public class vdMapLookUpAsync extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            vdMapLookUp();
            return null;
        }

        @Override
        protected void onPostExecute(Void params) {
            super.onPostExecute(null);
        }
    }


    private void vdMapLookUp() {
        String url = "http://vmp.company/vexMall/back/vdMapLookup.php";
        Log.d(TAG, "vdMapLookUp() begins to run..");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.d(TAG, "vdMapLookUp() takes response. response : " + response);

                            JSONArray array = new JSONArray(response);

                            int length = array.length();
                            mapList = new ArrayList<>(length);

                            VendorMapVO.setContext(GpsActivity.this);
                            for (int i = 0; i < length; ++i) {
                                JSONObject obj = array.getJSONObject(i);
                                mapList.add(new VendorMapVO(
                                        obj.getString("companyName"),
                                        obj.getString("industry"),
                                        obj.getString("postCode"),
                                        obj.getString("addr1"),
                                        obj.getString("addr2"),
                                        obj.getString("imagePath"),
                                        obj.getString("vm"),
                                        obj.getDouble("lat"),
                                        obj.getDouble("lng")
                                ));
                            }
                            for (VendorMapVO vo : mapList) {
                                Log.d(TAG, vo.toString());
                            }
                            // Vendormap intent args 세팅
                            Intent intent = new Intent(GpsActivity.this, VendormapAcitivity.class);

                            intent.putExtra("longitude", user_longitude);
                            intent.putExtra("latitude", user_latitude);
                            intent.putParcelableArrayListExtra("mapList", mapList);
                            intent.putExtra("isMapType", isMapType);

                            // Vendormap 실행
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.i(TAG, "err:" + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d(TAG, "Error Response : " + error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("lat", String.valueOf(user_latitude));
                params.put("lng", String.valueOf(user_longitude));
                params.put("distance", String.valueOf(5));
                Log.d(TAG, "getParams().params : " + params);

                return params;
            }
        };

        stringRequest.setTag(TAG);
        MySingleton.getInstance(GpsActivity.this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
