package vmp.company.vexmall.api;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import util.data.MainCategory;
import util.data.Shop;

/**
 * Project   : VexMall
 * Packages  : vmp.company.vexmall.api
 * Author    : Marty
 * Date      : 2018-12-26 / 오전 10:23
 * Comment   :
 */
public interface ShopAPI {

    @POST("back/subCategoryJoin.php")
    Call<MainCategory> getCategories();

    @POST("back/productListJoin.php")
    @FormUrlEncoded
    Call<ArrayList<Shop>> getShops(@Field("ca_id") String ca_id);
}
