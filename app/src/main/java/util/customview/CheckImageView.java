package util.customview;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Project   : VexMall
 * Packages  : util.customview
 * Author    : Marty
 * Date      : 2018-12-27 / 오후 4:44
 * Comment   :
 */
public class CheckImageView extends android.support.v7.widget.AppCompatImageView {
    public CheckImageView(Context context) {
        super(context);
    }

    public CheckImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    int onSelectedimage;
    int onUnselectedimage;
    public void setSelector(int selected,int unseleted){
        onSelectedimage = selected;
        onUnselectedimage = unseleted;
    }

    public int getOnSelectedimage() {
        return onSelectedimage;
    }

    public void setOnSelectedimage(int onSelectedimage) {
        this.onSelectedimage = onSelectedimage;
    }

    public int getOnUnselectedimage() {
        return onUnselectedimage;
    }

    public void setOnUnselectedimage(int onUnselectedimage) {
        this.onUnselectedimage = onUnselectedimage;
    }

    private boolean ischecked = false;

    public void toggleCheck(){
        ischecked = !ischecked;
        setImageResource(ischecked ? onSelectedimage : onUnselectedimage);

    }

    public boolean ischecked() {
        return ischecked;
    }

    public void setchecked(boolean ischecked) {
        this.ischecked = ischecked;
        setImageResource(ischecked ? onSelectedimage : onUnselectedimage);

    }

}
