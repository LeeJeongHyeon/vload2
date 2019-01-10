package vmp.company.vexmall.main;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import vmp.company.vexmall.R;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    int image=R.drawable.event;
    int count;
    Bitmap[] bitmaps;

    public ViewPagerAdapter(FragmentManager fm, Bitmap[] bitmaps) {
        super(fm);
        this.count = MainActivity.top_count;
        this.bitmaps = bitmaps;
    }

    @Override
    public Fragment getItem(int position) {
        ItemFragment fragment = new ItemFragment();
        fragment.setPosition( position, bitmaps );
        return fragment;
    }

    @Override
    public int getCount() {
        return count;
    }
}
