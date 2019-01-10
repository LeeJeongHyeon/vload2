package vmp.company.vexmall.vendormap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;

import util.category.LogTag;
import util.vendormap.VendorMapVO;
import vmp.company.vexmall.R;


public class VendormapFrag1 extends Fragment {
    final private String TAG = LogTag.getTag(this);
    private final String DAUM_API_KEY = "L0U1D8Iy9y4oo7fmp+t9XpfDmqk=×";
    private static final String ARG_PARAM1 = "mapList";

    private ArrayList<VendorMapVO> mapList = new ArrayList<>();
    private ArrayList<Bitmap> imagePathBitmapList = new ArrayList<>();

    private OnFragmentInteractionListener mListener;

    private View rootView;

    private double user_latitude;
    private double user_longitude;

    private static MapView mapView;
    private ViewGroup mapViewContainer;

    private void initUI() {
        Log.d(TAG, "initUI() begins to run..");


        // Bundle 받기
        if (getArguments() != null) {
            Log.d(TAG, "getting bundles...");

            mapList = getArguments().getParcelableArrayList(ARG_PARAM1);
            user_latitude = getArguments().getDouble("user_latitude");
            user_longitude = getArguments().getDouble("user_longitude");
            Log.d(TAG, "mapList : " + mapList);
            Log.d(TAG, "user_longitude : " + user_longitude);
            Log.d(TAG, "user_latitude : " + user_latitude);
        } else {
            Log.d(TAG, "no bundle found.");
        }

        // 매장 사진들
        inflateMapImages();

        // 로드맵 이미지 가져오기
        setLoadMap();
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView() begins to run.. savedInstanceState : " + savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_vendormap_frag1, container, false);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void inflateMapImages() {
        // HorizontalScrollView의 child 레이아웃 생성
        Log.d(TAG, "inflateMapImages() begins to run..");
        LinearLayout vendormap_1_hsv_outer_ll = rootView.findViewById(R.id.vendormap_1_hsv_ll);

        for (int i = 0; i < mapList.size(); ++i) {
            VendorMapVO vo = mapList.get(i);
            Log.d(TAG, "vo : " + vo);
            // 내부 아이템 1개 생성
            View voItem = getLayoutInflater().inflate(R.layout.item_vendormap, vendormap_1_hsv_outer_ll, true);

            // 비트맵 이미지뷰
            ImageView f_iv = voItem.findViewById(R.id.f_iv);
            f_iv.setImageBitmap(imagePathBitmapList.get(i));

            // 넘버링 텍스트뷰
            TextView f_index_tv = voItem.findViewById(R.id.f_index_tv);
            f_index_tv.setText(String.valueOf((i + 1) + "."));

            // 상호명 텍스트뷰
            TextView f_company_name_tv = voItem.findViewById(R.id.f_company_name_tv);
            f_company_name_tv.setText(vo.getCompanyName());

            // 주소 텍스트뷰
            TextView f_addr1_tv = voItem.findViewById(R.id.f_addr1_tv);
            f_addr1_tv.setText(vo.getAddr1());

            // vm 혜택
            TextView f_vm_tv = voItem.findViewById(R.id.f_vm_tv);
            f_vm_tv.setText(vo.getVm());

            // 아이템 프레임 레이아웃을 out 레이아웃에 추가
            vendormap_1_hsv_outer_ll.addView(voItem);

        }
        Log.d(TAG, "inflateMapImages() ended..");
    }

    private void setLoadMap() {
        Log.d(TAG, "setLoadMap() begins to run..");
        mapViewContainer = rootView.findViewById(R.id.vendormap_1_mapview_rl);

         if(mapView != null){
            mapViewContainer.removeView(mapView);
            mapView = null;
         }
        mapView = new MapView(getActivity());
        mapView.setDaumMapApiKey(DAUM_API_KEY);


        // 중앙점 설정
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(user_latitude, user_longitude),true);

        // 화면에 표시할 마커의 개수
        int count = mapList.size();
        Log.d(TAG, "화면에 표시할 마커의 개수 : " + count);



        // 반경 5km의 마커 표시
        for (int i = 0; i < count; ++i) {
            VendorMapVO vo = mapList.get(i);
            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(vo.getLatitude(), vo.getLongitude());

            MapPOIItem marker = new MapPOIItem();
            marker.setItemName(mapList.get(i).getCompanyName());
            marker.setTag(0);
            marker.setMapPoint(mapPoint);
            marker.setMarkerType(MapPOIItem.MarkerType.RedPin);
            marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);

            mapView.addPOIItem(marker);
            Log.d(TAG, vo.toString());
        }
        MapView.POIItemEventListener listener = new MapView.POIItemEventListener() {
            @Override
            public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
                // 마커를 터치했을 경우 : Balloon 띄우기 (내용 : 상호명, 카카오맵으로 길찾기)
            }

            @Override
            public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {
                // 말풍선 클릭했을 경우 : 카카오맵으로 길찾기

                // 현위치 위경도(WGS84)와 목적지 위경도 세팅
                MapPoint.GeoCoordinate coord = mapView.getMapCenterPoint().getMapPointGeoCoord();
                String startPointLongitude = String.valueOf(user_longitude);
                String startPointLatitude = String.valueOf(user_latitude);
                String destPointLongitude = String.valueOf(coord.longitude);
                String destPointLatitude = String.valueOf(coord.latitude);


                // 카카오맵 앱 실행 (대중교통으로 길찾기)
                String url = "daummaps://route?sp="
                        + startPointLongitude + ","
                        + startPointLatitude + "&ep="
                        + destPointLongitude + ","
                        + destPointLatitude + "&by=PUBLICTRANSIT";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }

            @Override
            public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

            }

            @Override
            public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

            }
        };

        // 마커 클릭 리스너 추가
        mapView.setPOIItemEventListener(listener);
        Log.d(TAG, "mapView.setPOIItemEventListener(listener)");
        // 현재 위치를 센터로 잡는다
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);
        Log.d(TAG, "mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading)");
        // 표현할 반경 ( 5Km )
        mapView.setCurrentLocationRadius(5000);
        Log.d(TAG, "mapView.setCurrentLocationRadius(5000)");
        // 기본 제공되는 현위치 아이콘 이미지를 사용
        mapView.setDefaultCurrentLocationMarker();
        Log.d(TAG, "mapView.setDefaultCurrentLocationMarker()");
        // 레이아웃에 적용
        mapViewContainer.addView(mapView);
        Log.d(TAG, "mapViewContainer.addView(mapView)");
    }

    @Override
    public void onPause() {
        super.onPause();
        mapViewContainer.removeAllViews();
    }

    @Override
    public void onStart() {
        super.onStart();
        initUI();
    }
}