package vmp.company.vexmall.shoplist.callback;

import android.support.design.widget.TabLayout;

/**
 * Project   : VexMall
 * Packages  : vmp.company.vexmall.shoplist.callback
 * Author    : Marty
 * Date      : 2018-12-13 / 오후 12:48
 * Comment   :
 */
public interface OnTabSelectedListener extends TabLayout.OnTabSelectedListener{

        @Override
        default void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        default void onTabReselected(TabLayout.Tab tab) {

        }
    }

