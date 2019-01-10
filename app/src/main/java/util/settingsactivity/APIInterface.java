package util.settingsactivity;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIInterface {


    @Multipart
    @POST("back/vdSet.php")
    Call<Res> Post(@Part("mb_id") RequestBody mb_id, @Part("companyName") RequestBody companyName,
                   @Part("industry") RequestBody industry, @Part("postCode") RequestBody postCode,
                   @Part("addr1") RequestBody addr1, @Part("addr2") RequestBody addr2,
                   @Part("vm") RequestBody vm, @Part MultipartBody.Part app_icon_img);

}