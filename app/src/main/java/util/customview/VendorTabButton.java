package util.customview;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Project   : VexMall
 * Packages  : util.customview
 * Author    : Marty
 * Date      : 2018-12-11 / 오후 6:13
 * Comment   :
 */
public class VendorTabButton extends android.support.v7.widget.AppCompatRadioButton {
    public VendorTabButton(Context context) {
        super(context);
    }

    public VendorTabButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VendorTabButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void toggle() {
        super.toggle();
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        return super.getAccessibilityClassName();
    }
}
