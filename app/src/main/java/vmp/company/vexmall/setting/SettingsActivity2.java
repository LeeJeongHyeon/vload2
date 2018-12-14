package vmp.company.vexmall.setting;

import android.Manifest;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import util.ImageConverter;
import util.category.LogTag;
import util.recommender.StatusBar;
import util.settingsactivity.APIClient;
import util.settingsactivity.APIInterface;
import util.settingsactivity.Res;
import vmp.company.vexmall.signup.DaumZipDialog;
import util.MySingleton;
import vmp.company.vexmall.R;

public class SettingsActivity2 extends AppCompatActivity implements Runnable {

    final private String TAG = LogTag.getTag(this);
    LinearLayout settings2_layout;
    FrameLayout s2_back;    // 뒤로가기
    EditText s2_1_business_name_et; // 업체명
    EditText s2_2_business_type_et; // 업종
    ImageView s2_3_address0_iv;
    EditText s2_3_address1_et; // 주소1 (우편번호)
    EditText s2_3_address2_et; // 주소2
    EditText s2_3_address3_et; // 주소3
    ImageView s2_4_shop_image_iv; // 매장 사진
    ImageView s2_4_shop_image_fork_iv;
    ImageButton s2_4_camera_ib; // 카메라 버튼
    EditText s2_5_vm_benefit_et; // VM 혜택
    TextView s2_6_complete_tv; // 설정 완료
    String mb_id;
    String companyName, industry, postCode, addr1, addr2, vm;

    ////////////// 매장 이미지 //////////////////
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_ALBUM = 2;
    private static final int CROP_FROM_CAMERA = 3;
    private Uri serverImageUri;
    private Uri beforeCropImageUri;
    private Uri afterCropImageUri;
    private File currentImageFile;
    private Bitmap currentBitmap;
    private boolean isImageSelectedFromLocal = false;
    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    private static final int MULTIPLE_PERMISSIONS = 101;
    private ImageConverter converter = ImageConverter.getInstance(SettingsActivity2.this);
    ///////////////////////////////////////////


    TextWatcher onKey = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            //입력되는 텍스트에 변화가 있을때
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            //입력이 끝났을 때
            checkAvailable();
        }

        @Override
        public void afterTextChanged(Editable editable) {
            //입력하기 전에 호출
        }
    };
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, "Setting2.handleMessage() begins to run...");
            super.handleMessage(msg);
            try {
                updateImageView(BitmapFactory.decodeFile(currentImageFile.getAbsolutePath()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isImageSelectedFromLocal = false;
        Log.d(TAG, "Setting2.onCreate() begins to run...");
        setContentView(R.layout.activity_settings2);
        checkPermissions();
        mb_id = PreferenceManager.getDefaultSharedPreferences(this).getString("mb_id", "");
        if (mb_id.isEmpty()) {
            Toast.makeText(SettingsActivity2.this, "로그인 후 사용하실 수 있습니다.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, SettingsActivity.class));
            return;
        }
        StatusBar.getInstance().setStatusBar(this, R.color.settings);  // 화면 상단 색상
        getInfo();     // 설정 정보 가져오기

    }

    // getInfo() 내부에서 호출됨
    private void initUI() {
        Log.d(TAG, "Setting2.initUI() begins to run...");
        s2_back = findViewById(R.id.s2_back);

        s2_1_business_name_et = findViewById(R.id.s2_1_business_name_et);
        if (companyName != null) s2_1_business_name_et.setText(companyName);

        s2_2_business_type_et = findViewById(R.id.s2_2_business_type_et);
        if (industry != null) s2_2_business_type_et.setText(industry);

        s2_3_address0_iv = findViewById(R.id.s2_3_address0_iv);

        s2_3_address1_et = findViewById(R.id.s2_3_address1_et);
        if (postCode != null) s2_3_address1_et.setText(postCode);

        s2_3_address2_et = findViewById(R.id.s2_3_address2_et);
        if (addr1 != null) s2_3_address2_et.setText(addr1);

        s2_3_address3_et = findViewById(R.id.s2_3_address3_et);
        if (addr2 != null) s2_3_address3_et.setText(addr2);

        s2_4_shop_image_iv = findViewById(R.id.s2_4_shop_image_iv);
        s2_4_shop_image_fork_iv = findViewById(R.id.s2_4_shop_image_fork_iv);

        s2_4_camera_ib = findViewById(R.id.s2_4_camera_ib);

        s2_5_vm_benefit_et = findViewById(R.id.s2_5_vm_benefit_et);
        if (vm != null) s2_5_vm_benefit_et.setText(vm);

        s2_6_complete_tv = findViewById(R.id.s2_6_complete_tv);
        Log.d(TAG, "Setting2.initUI() returns...");
    }

    private String checkAvailable() {
        Log.d(TAG, "Setting2.checkAvailable() begins to run...");
        boolean check = true;
        String message = "";
        if (s2_1_business_name_et.getText().toString().isEmpty()) {
            message = "업체명을 등록해주세요.";
            check = false;
        } else if (s2_2_business_type_et.getText().toString().isEmpty()) {
            message = "업종을 등록해주세요.";
            check = false;
        } else if (s2_3_address1_et.getText().toString().isEmpty()) {
            message = "우편번호를 등록해주세요.";
            check = false;
        } else if (s2_3_address2_et.getText().toString().isEmpty()) {
            message = "기본 주소를 등록해주세요.";
            check = false;
        } else if (s2_3_address3_et.getText().toString().isEmpty()) {
            message = "상세 주소를 등록해주세요.";
            check = false;
        } else if (currentImageFile == null) {
            message = "매장 이미지를 등록해주세요.";
            check = false;
        }
        if (check) {
            s2_6_complete_tv.setBackground(getDrawable(R.color.settings));
        } else {
            s2_6_complete_tv.setBackground(getDrawable(R.color.disabled));
        }
        Log.d(TAG, "Setting2.checkAvailable() returns..");
        return message;
    }

    private void setListeners() {
        Log.d(TAG, "Setting2.setListeners() begins to run...");
        s2_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        s2_1_business_name_et.addTextChangedListener(onKey);
        s2_2_business_type_et.addTextChangedListener(onKey);
        s2_3_address0_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DaumZipDialog daumZipDialog = new DaumZipDialog(SettingsActivity2.this, R.layout.activity_daum_zip, s2_3_address1_et, s2_3_address2_et, R.color.settings);
                daumZipDialog.show();
                s2_3_address3_et.requestFocus();
            }
        });
        s2_3_address1_et.addTextChangedListener(onKey);
        s2_3_address2_et.addTextChangedListener(onKey);
        s2_3_address3_et.addTextChangedListener(onKey);


        View.OnClickListener cameraListener = new View.OnClickListener() {

            DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    takePhoto();
                }
            };
            DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    goToAlbum();
                }
            };
            DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            };

            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(SettingsActivity2.this)
                        .setTitle("매장사진 선택")
                        .setNeutralButton("취소", cancelListener)
                        .setPositiveButton("앨범 선택", albumListener)
                        .setNegativeButton("사진 촬영", cameraListener)
                        .show();
            }

        };

        s2_4_camera_ib.setOnClickListener(cameraListener);
        if (currentImageFile == null) {
            s2_4_shop_image_iv.setOnClickListener(cameraListener);
        }
        s2_6_complete_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 1. 데이터 유효성 검사
                String message = checkAvailable();
                if (!message.isEmpty()) {
                    Toast.makeText(SettingsActivity2.this, message, Toast.LENGTH_SHORT).show();
                    return;
                }
                new UploadInfoAsync().execute(); // 서버에 현재 정보를 모두 업로드

            }
        });
        Log.d(TAG, "Setting2.setListeners() returns...");
    }

    // 서버에서 기존 설정정보를 가져옴 (JSON) + 필드 초기화 (반드시 getInfo() 호출 후 initUI()를 실행해야 함)
    private void getInfo() {
        String url = "http://vmp.company/vexMall/back/vdSetLookup.php";
        Log.d(TAG, "Setting2.getInfo() begins to run...");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.d(TAG, "Settings2's getInfo() response : " + response);
                            companyName = obj.getString("companyName");
                            industry = obj.getString("industry");
                            postCode = obj.getString("postCode");
                            addr1 = obj.getString("addr1");
                            addr2 = obj.getString("addr2");
                            vm = obj.getString("vm");

                            // 유저가 이미지 선택을 안했다면
                            if (!isImageSelectedFromLocal) {
                                String uri = obj.getString("imagePath");
                                if (uri != null) {
                                    serverImageUri = Uri.parse(uri);
                                    // 백그라운드 스레드 생성
                                    Thread th = new Thread(SettingsActivity2.this);
                                    // 동작 수행
                                    th.start();
                                }
                            }
                            Log.d(TAG, "initUI() is called by getInfo()..");
                            initUI();      // UI 설정
                            Log.d(TAG, "setListeners() is called by getInfo()..");
                            setListeners(); // 리스너 설정
                            Log.d(TAG, "checkAvailable() is called by getInfo()..");
                            checkAvailable();
                            Log.d(TAG, "Setting2.getInfo() returns..");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("mb_id", mb_id);
                return params;
            }
        };
        stringRequest.setTag(TAG);
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }


    // 서버에게 모든 설정 정보를 보내는 메서드 (multipart form-data)
    // 설정 완료 텍스트뷰의 리스너에서 호출됨
    public class UploadInfoAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            uploadInfo();
            return null;
        }

        @Override
        protected void onPostExecute(Void params) {
            super.onPostExecute(null);

        }
    }

    private void uploadInfo() {
        Log.d(TAG, "Setting2.uploadInfo() begins to run...");

        mb_id = mb_id.trim();
        companyName = s2_1_business_name_et.getText().toString().trim();
        industry = s2_2_business_type_et.getText().toString().trim();
        postCode = s2_3_address1_et.getText().toString().trim();
        addr1 = s2_3_address2_et.getText().toString().trim();
        addr2 = s2_3_address3_et.getText().toString().trim();
        vm = s2_5_vm_benefit_et.getText().toString().trim();

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        RequestBody mb_id = RequestBody.create(MediaType.parse("multipart/form-data"), this.mb_id);
        RequestBody companyName = RequestBody.create(MediaType.parse("multipart/form-data"), this.companyName);
        RequestBody industry = RequestBody.create(MediaType.parse("multipart/form-data"), this.industry);
        RequestBody postCode = RequestBody.create(MediaType.parse("multipart/form-data"), this.postCode);
        RequestBody addr1 = RequestBody.create(MediaType.parse("multipart/form-data"), this.addr1);
        RequestBody addr2 = RequestBody.create(MediaType.parse("multipart/form-data"), this.addr2);
        RequestBody vm = RequestBody.create(MediaType.parse("multipart/form-data"), this.vm);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), currentImageFile);
        MultipartBody.Part app_icon_img = MultipartBody.Part.createFormData("app_icon_img", currentImageFile.getName(), requestFile);
        Call<Res> call = apiInterface.Post(mb_id, companyName, industry, postCode, addr1, addr2, vm, app_icon_img);
        call.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, retrofit2.Response<Res> response) {
                Log.d(TAG, "Setting2.uploadInfo().call.enqueue.onResponse() begins to run...");
                if (response != null) {
                    Res res = response.body();
                    String message = res.getResult();
                    message = message.equals("true") ? "설정을 완료하였습니다." : message;
                    Toast.makeText(SettingsActivity2.this, message, Toast.LENGTH_SHORT).show();
                    converter.deleteTempDir();
                }
            }

            @Override
            public void onFailure(Call<Res> call, Throwable t) {
                call.cancel();
                Toast.makeText(SettingsActivity2.this, "설정을 완료하지 못했습니다. " + t.getMessage(), Toast.LENGTH_SHORT).show();
                converter.deleteTempDir();
            }
        });
    }


    // 서버로 받은 이미지 경로로 비트맵이미지 설정
    @Override
    public void run() {
        URL url = null;
        HttpURLConnection conn = null;
        InputStream is = null;
        FileOutputStream out = null;
        try {

            url = new URL(serverImageUri.toString());
            // url에 접속 시도
            conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            // 스트림 생성
            is = conn.getInputStream();
            // 스트림에서 받은 데이터를 비트맵 변환
            // 인터넷에서 이미지 가져올 때는 Bitmap을 사용해야함
            currentBitmap = converter.uriToBitmap(serverImageUri, true);

            // Bitmap이미지를 임시 디렉토리의 파일로 생성
            currentImageFile = converter.bitmapToFile(currentBitmap);

            // 핸들러에게 화면 갱신을 요청한다.
            handler.sendEmptyMessage(0);

            // 연결 종료
            is.close();
            Log.d(TAG, "run().currentImageFile : " + currentImageFile);
            conn.disconnect();
            checkAvailable();
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }


    private boolean checkPermissions() {
        int result;
        List<String> permissionList = new ArrayList<>();
        for (String pm : permissionList) {
            result = ContextCompat.checkSelfPermission(this, pm);
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(pm);
            }
        }
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    private void goToAlbum() {
        isImageSelectedFromLocal = true;
        Intent intent = new Intent(Intent.ACTION_PICK); // 사진을 고름
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM); // 앨범 선택 인텐트 실행 후 result로 감 + data는 선택한 사진의 uri
    }

    private void takePhoto() {
        isImageSelectedFromLocal = true;
        File tempFile = null;
        try {
            tempFile = converter.createEmptyTempFile();
        } catch (IOException e) {
            Toast.makeText(SettingsActivity2.this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            finish();
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // 인텐트 준비
        beforeCropImageUri = FileProvider.getUriForFile(SettingsActivity2.this,
                "vmp.company.vexmall.provider", tempFile);
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, beforeCropImageUri); // 방금 생성한 임시파일 경로 (여기에 찍은 사진을 담을 것임)
        Log.d(TAG, "takePhoto() calls Camera Intent..");
        startActivityForResult(intent, PICK_FROM_CAMERA);

    }

    public void cropImage() {
        Log.d(TAG, "cropImage() begins to run..");
        this.grantUriPermission("com.android.camera", beforeCropImageUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(beforeCropImageUri, "image/*");
        Log.d(TAG, "cropImage().pcs 1 complete. beforeCropImageUri : " + beforeCropImageUri);
        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0);
        grantUriPermission(list.get(0).activityInfo.packageName, beforeCropImageUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

        int size = list.size();
        if (size == 0) {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Toast.makeText(this, "용량이 큰 사진의 경우 시간이 오래 걸릴 수 있습니다.", Toast.LENGTH_SHORT).show();
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
          //  intent.putExtra("rotate", false);
            Log.d(TAG, "cropImage().pcs 2 complete. beforeCropImageUri : " + beforeCropImageUri);
            try {
                afterCropImageUri = FileProvider.getUriForFile(SettingsActivity2.this,
                        "vmp.company.vexmall.provider", converter.createEmptyTempFile());
                Log.d(TAG, "cropImage().pcs 3 complete. afterCropImageUri : " + afterCropImageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "cropImage().afterCropImageUri (파일 복사 전): " + afterCropImageUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG);
            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, afterCropImageUri); // 크랍할 사진을 저장할 위치
            Log.d(TAG, "cropImage().pcs 4 complete. afterCropImageUri.getPath() : " + afterCropImageUri.getPath());
            Intent i = new Intent(intent);
            ResolveInfo res = list.get(0);
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            grantUriPermission(res.activityInfo.packageName, afterCropImageUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

            i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            Log.d(TAG, "cropImage().pcs 5 complete. afterCropImageUri.getPath() : " + afterCropImageUri.getPath());
            startActivityForResult(i, CROP_FROM_CAMERA); // CROP 실행
            Log.d(TAG, "cropImage().pcs 6 complete. currentBitmap : " + currentBitmap);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        data.setExtrasClassLoader(SettingsActivity2.class.getClassLoader());
        if (resultCode != RESULT_OK) {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (requestCode) {
            case PICK_FROM_ALBUM: { // 앨범에서 선택
                if (data == null) {
                    return;
                }
                beforeCropImageUri = data.getData(); // 선택된 사진의 uri 받아옴
                Log.d(TAG, "앨범에서 선택 완료. beforeCropImageUri.getPath() : " + beforeCropImageUri.getPath());
                Log.d(TAG, "앨범에서 선택 완료. beforeCropImageUri.toString() : " + beforeCropImageUri.toString());
                Log.d(TAG, "앨범에서 선택 완료. beforeCropImageUri : " + beforeCropImageUri);
                cropImage();
                break;
            }
            case PICK_FROM_CAMERA: {
                Bundle extras = data.getExtras();
                currentBitmap = (Bitmap) extras.get("data");
                beforeCropImageUri = FileProvider.getUriForFile(SettingsActivity2.this,
                        "vmp.company.vexmall.provider", converter.bitmapToFile(currentBitmap));
                //이미지를 불러올때 고용량의 경우 OutOfMemory가 발생할 수 있으므로
                //이미지 크기를 줄여서 호출함
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;
                currentBitmap = converter.uriToBitmap(beforeCropImageUri);
                Log.d(TAG, "Step1. currentBitmap : " + currentBitmap);
                try {
                    // 기본 카메라 모듈을 이용해 촬영할 경우 가끔씩 이미지가
                    // 회전되어 출력되는 경우가 존재하여
                    // 이미지를 상황에 맞게 회전시킨다
                    ExifInterface exif = new ExifInterface(beforeCropImageUri.getPath());
                    int exifOrientation = exif.getAttributeInt(
                            ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    int exifDegree = exifOrientationToDegrees(exifOrientation);

                    //회전된 이미지를 다시 회전시켜 정상 출력
                    currentBitmap = rotate(currentBitmap, exifDegree);
                    Log.d(TAG, "Step2. currentBitmap : " + currentBitmap);
                    //회전시킨 이미지를 저장
                    currentImageFile = converter.bitmapToFile(currentBitmap);
                    //saveExifFile(currentBitmap , beforeCropImageUri.getPath());



                } catch (IOException e) {
                    Log.d(TAG, "onActivityResult().PICK_FROM_CAMERA exception occurred : " + e.getMessage());
                }
                cropImage();
                // 비트맵 메모리 반환
                // currentBitmap.recycle();
                break;
            }
            case CROP_FROM_CAMERA: {
                try {
                    Log.d(TAG, "case CROP_FROM_CAMERA's afterCropImageUri : " + afterCropImageUri);
                    Log.d(TAG, "case CROP_FROM_CAMERA's Uri.parse(afterCropImageUri.getPath()) : " + Uri.parse(afterCropImageUri.getPath()));
                    //currentImageFile = converter.uriToFile(afterCropImageUri);
                    currentBitmap = converter.uriToBitmap(afterCropImageUri);
                    Log.d(TAG, "case CROP_FROM_CAMERA's currentBitmap : " + currentBitmap);
                    currentImageFile = converter.bitmapToFile(currentBitmap);
                    Log.d(TAG, "Step3. currentBitmap : " + currentBitmap);
                    updateImageView(currentBitmap);
                } catch (Exception e) {
                    Log.e(TAG, "onActivityResult().CROP_FROM_CAMERA exception occurred : " + e.getMessage());
                }
                break;
            }
        }
        checkAvailable();
    }

    // 이미지 crop

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++) {
                        if (permissions[i].equals(this.permissions[0])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();
                            }
                        } else if (permissions[i].equals(this.permissions[1])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();

                            }
                        } else if (permissions[i].equals(this.permissions[2])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();

                            }
                        }
                    }
                } else {
                    showNoPermissionToastAndFinish();
                }
            }
        }
    }

    private void showNoPermissionToastAndFinish() {
        Toast.makeText(this, "권한 요청에 동의 해주셔야 이용 가능합니다. 설정에서 권한 허용 하시기 바랍니다.", Toast.LENGTH_SHORT).show();
    }


    private void updateImageView(Bitmap bitmap) {
        s2_4_shop_image_iv.setImageBitmap(bitmap);
        s2_4_shop_image_fork_iv.setVisibility(View.GONE);
    }

    // 사진의 회전 각도 구하기
    public int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    // Bitmap 회전
    public Bitmap rotate(Bitmap bitmap, int degrees) {
        if (degrees != 0 && bitmap != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) bitmap.getWidth() / 2,
                    (float) bitmap.getHeight() / 2);

            try {
                Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), m, true);
                if (bitmap != converted) {
                    bitmap.recycle();
                    bitmap = converted;
                }
            } catch (OutOfMemoryError ex) {
            }
        }
        return bitmap;
    }

    public void saveExifFile(Bitmap imageBitmap, String savePath) {
        FileOutputStream fos = null;
        File saveFile = null;

        try {
            saveFile = new File(savePath);
            fos = new FileOutputStream(saveFile);
            //원본형태를 유지해서 이미지 저장
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

        } catch (FileNotFoundException e) {
            //("FileNotFoundException", e.getMessage());
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
                Log.d(TAG, "saveExifFile has Exception : " + e.getMessage());
            }
        }
    }
}

