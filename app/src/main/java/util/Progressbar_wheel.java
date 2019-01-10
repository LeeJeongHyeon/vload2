package util;

import android.app.Dialog;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import vmp.company.vexmall.R;

/**
 * Created by 박효근 on 2018-08-21.
 */

public class Progressbar_wheel extends Dialog {


    public static Progressbar_wheel show(Context context, CharSequence title,

                                         CharSequence message) {

        return show(context, title, message, false);
    }


    public static Progressbar_wheel show(Context context, CharSequence title,

                                         CharSequence message, boolean indeterminate) {

        return show(context, title, message, indeterminate, false, null);

    }


    public static Progressbar_wheel show(Context context, CharSequence title,

                                         CharSequence message, boolean indeterminate, boolean cancelable) {

        return show(context, title, message, indeterminate, cancelable, null);

    }


    public static Progressbar_wheel show(Context context, CharSequence title,

                                         CharSequence message, boolean indeterminate,

                                         boolean cancelable, OnCancelListener cancelListener) {

        Progressbar_wheel dialog = new Progressbar_wheel(context);

        dialog.setTitle(title);

        dialog.setCancelable(cancelable);

        dialog.setOnCancelListener(cancelListener);

/* The next line will add the ProgressBar to the dialog. */

        dialog.addContentView(new ProgressBar(context), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        dialog.show();


        return dialog;

    }


    public Progressbar_wheel(Context context) {

        super(context, R.style.NewDialog);

    }

}
