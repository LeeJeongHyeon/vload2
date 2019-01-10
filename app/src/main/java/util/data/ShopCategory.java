package util.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Project   : VexMall
 * Packages  : vmp.company.vexmall.shoplist
 * Author    : Marty
 * Date      : 2018-12-26 / 오전 10:24
 * Comment   :
 */
public class ShopCategory {

    @SerializedName("sub")
    public List<SubCategory> sub;
    @SerializedName("ca_id")
    public String ca_id;
    @SerializedName("ca_name")
    public String ca_name;
    @SerializedName("imagePath")
    public String imagePath;

    public ShopCategory(List<SubCategory> sub, String ca_id, String ca_name, String imagePath) {
        this.sub = sub;
        this.ca_id = ca_id;
        this.ca_name = ca_name;
        this.imagePath = imagePath;
    }

    public ShopCategory() {
    }
}
