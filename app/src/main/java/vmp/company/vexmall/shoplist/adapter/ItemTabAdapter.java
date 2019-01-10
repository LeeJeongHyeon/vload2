package vmp.company.vexmall.shoplist.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import vmp.company.vexmall.shoplist.tab_fragments.Item_Tabfrag_BasicInform;
import vmp.company.vexmall.shoplist.tab_fragments.Item_Tabfrag_DetailInform;
import vmp.company.vexmall.shoplist.tab_fragments.Item_Tabfrag_QA;
import vmp.company.vexmall.shoplist.tab_fragments.Item_Tabfrag_Review;

/**
 * Project   : VexMall
 * Packages  : vmp.company.vexmall.shoplist.adapter
 * Author    : Marty
 * Date      : 2018-12-13 / 오후 12:49
 * Comment   :
 */
public class ItemTabAdapter extends FragmentPagerAdapter {

    private int tabCnt;

    Fragment [] fragments;
    public ItemTabAdapter(FragmentManager fm, int tabCnt) {
        super(fm);
        this.tabCnt = tabCnt;
        fragments = new Fragment[4];
        fragments[0] = Item_Tabfrag_DetailInform.newInstance();
        fragments[1] = Item_Tabfrag_BasicInform.newInstance();
        fragments[2] = Item_Tabfrag_Review.newInstance();
        fragments[3] = Item_Tabfrag_QA.newInstance();

    }

    @Override
    public Fragment getItem(int i) {

        switch (i){
            case 0: // 상세정보
                break;
            case 1 : // 기본정보
                break;
            case 2: // 구매후기
                break;
            case 3 : // 문의하기
                break;

        }


        return  fragments[i];
    }

    @Override
    public int getCount() {
        return tabCnt;
    }
}
