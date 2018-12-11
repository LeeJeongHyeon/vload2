package util;

import android.Manifest;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.List;

import util.category.LogTag;

public class Permission {
    final private String TAG = LogTag.getTag(this);
    public void checkPermission(Context context) {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
            }

            @Override
            public void onFailed(int requestCode, List<String> deniedPermissions) {
            }
        };
        Log.d(TAG, "checkPermission() begins to start..");
        AndPermission.with(context)
                .requestCode(300)
                .permission(
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.GET_ACCOUNTS,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                )
                .callback(permissionListener)
                .start();
        Log.d(TAG, "checkPermission() begins finished..");
    }

}
