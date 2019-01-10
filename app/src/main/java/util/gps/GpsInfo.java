package util.gps;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import util.Permission;
import util.category.LogTag;


public class GpsInfo {

    private boolean result;
    private final String TAG = LogTag.getTag(this);

    Context context;

    private double lon;
    private double lat;

    private static GpsInfo gpsInfo;

    private GpsInfo() {

    }

    public static GpsInfo getInstance(Context context) {
        if (gpsInfo == null) {
            gpsInfo = new GpsInfo();
        }
        gpsInfo.context = context;
        return gpsInfo;
    }


    //Google Play 서비스 접근승인 요청
    public GoogleApiClient.Builder setGoogleServiceBuilder() {


        //Google Api Client 생성
        GoogleApiClient.Builder mGoogleApiClientBuilder = new GoogleApiClient.Builder(context);
        mGoogleApiClientBuilder.addApi(LocationServices.API);//Fused Location Provider API 사용요청


        //Google Client Connection Callback 클래스
        CallbackConnectedGoogleService callbackConnectedGoogleService = new CallbackConnectedGoogleService(this);
        mGoogleApiClientBuilder.addConnectionCallbacks(callbackConnectedGoogleService);
        mGoogleApiClientBuilder.addOnConnectionFailedListener(callbackConnectedGoogleService);
        GoogleApiClient mGoogleApiClient = mGoogleApiClientBuilder.build();
        mGoogleApiClient.connect();
        return mGoogleApiClientBuilder;

    }


    //Google Play 서비스 접근승인 응답
    class CallbackConnectedGoogleService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
        private final String TAG = LogTag.getTag(this);
        private GpsInfo googleServiceControl;

        public CallbackConnectedGoogleService(GpsInfo googleServiceControl) {
            this.googleServiceControl = googleServiceControl;
        }

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
        new Permission().checkPermission(context);

        Task task = LocationServices.getFusedLocationProviderClient(context).getLastLocation();
        task.addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task task) {
                Log.d(TAG, "Task is successful.");
                if (task.getResult() instanceof Location) {
                    Location mLastLocation = (Location) task.getResult();
                    lon = mLastLocation.getLongitude();
                    lat = mLastLocation.getLatitude();
                    Log.d(TAG, "Fused Location Provider API :Latitude" + mLastLocation.getLatitude());
                    Log.d(TAG, "Fused Location Provider API :Longitude" + mLastLocation.getLongitude());
                    Log.d(TAG, "Fused Location Provider API :Accuracy" + mLastLocation.getAccuracy());
                    result = true;
                } else {
                    result = false;
                }
            }
        });

    }

    public double getLongitude() {
        return lon;
    }

    public double getLatitude() {
        return lat;
    }

    public boolean hasLocation() {
        return result;
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        alertDialog.setTitle("GPS 사용유무셋팅");
        alertDialog.setMessage("GPS 셋팅이 되지 않았을수도 있습니다. \n 설정창으로 가시겠습니까?");

        // OK 를 누르게 되면 설정창으로 이동합니다.
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        context.startActivity(intent);
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
}


