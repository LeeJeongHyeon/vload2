package vmp.company.vexmall.vendormap;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Project   : VexMall
 * Packages  : vmp.company.vexmall.vendormap
 * Author    : Marty
 * Date      : 2018-12-14 / 오후 5:35
 * Comment   :
 */

/**
 * # HTTP POST 통신 파라미터 정보
 * - 	lat : GPS상 현재 위도
 * - 	lng : GPS상 현재 경도
 * - 	distance : 해당 위, 경도 기준 반경 몇 KM를 조회할 지 숫자(단위 KM)
 *          ex) 5
 *
 * # Response 파라미터(JSON Array)
 *   에러 발생시 result라는 이름으로 에러내용을 값으로 전달함.
 *   result는 1차원 json 형태로 전달함
 * - 	companyName : 업체명
 * - 	industry : 업종
 * - 	postCode : 우편번호 5자리(숫자만)
 * - 	addr1 : 주소
 * - 	addr2 : 상세주소
 * - 	imagePath : 업체 사진 URL
 * - 	vm : VM 혜택
 * - 	lat : 위도
 * - 	lng : 경도
 */
public interface SampleApi {
    @POST("back/vdMapLookup.php")
    @FormUrlEncoded
    Call<ArrayList<SampleDataClass_VO>> getStores(@Field("lat") String lat , @Field("lng") String lng , @Field("distance") int distance);
}
