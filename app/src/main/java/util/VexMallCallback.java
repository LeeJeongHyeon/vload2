package util;

import android.support.v7.app.AppCompatActivity;

import soft.neunge.com.library.utils.BaseCallback;

/**
 * Project   : VexMall
 * Packages  : util
 * Author    : Marty
 * Date      : 2018-12-14 / 오후 5:52
 * Comment   :
 */
public class VexMallCallback extends BaseCallback{

    public VexMallCallback(AppCompatActivity baseView, MartyCall rationCall) {
        super(baseView, rationCall);
    }

    public VexMallCallback(AppCompatActivity baseView, MartyCall rationCall, int failAlertText) {
        super(baseView, rationCall, failAlertText);
    }

    public VexMallCallback(AppCompatActivity baseView, MartyCallWithFailure rationCall) {
        super(baseView, rationCall);
    }

    public VexMallCallback(AppCompatActivity baseView, MartyCallWithFailure rationCall, int failAlertText) {
        super(baseView, rationCall, failAlertText);
    }
}
