package util.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Project   : VexMall
 * Packages  : util.data
 * Author    : Marty
 * Date      : 2018-12-26 / 오후 12:48
 * Comment   :
 */
public class MainCategory {

    @SerializedName("category")
    public List<ShopCategory> categories;
}
