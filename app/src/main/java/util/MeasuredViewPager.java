package util;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Project   : VexMall
 * Packages  : util
 * Author    : Marty
 * Date      : 2018-12-13 / 오후 4:34
 * Comment   :
 */
public class MeasuredViewPager extends ViewPager {


        public MeasuredViewPager(Context context) {
            super(context);
        }

        public MeasuredViewPager(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int mode = MeasureSpec.getMode(heightMeasureSpec);
            if (mode == MeasureSpec.UNSPECIFIED || mode == MeasureSpec.AT_MOST) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                int height = 0;
                for (int i = 0; i < getChildCount(); i++) {
                    View child = getChildAt(i);
                    child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                    int h = child.getMeasuredHeight();
                    if (h > height) height = h;
                }
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
            }
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
}



